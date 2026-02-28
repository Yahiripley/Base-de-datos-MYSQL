import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;


/**
 * INSERTAMOS ACTIVISTA
 * @author josel
 */
public class ActivistasForm {

    private final IActivistaDAO activistaDAO;
    private final Scanner sc;

    // Para leer fechas tipo 2024-02-01
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ActivistasForm(IActivistaDAO activistaDAO) {
        this.activistaDAO = activistaDAO;
        this.sc = new Scanner(System.in);
    }

    public void iniciar() {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Elige una opcion: ");

            switch (opcion) {
                case 1 -> opcionAgregar();
                case 2 -> opcionActualizar();
                case 3 -> opcionEliminar();
                case 4 -> opcionConsultarPorId();
                case 5 -> opcionConsultarTodos();
                case 0 -> System.out.println("Saliendo... hasta luego!");
                default -> System.out.println("Opción invalida. Intenta otra vez.");
            }

            System.out.println(); // salto de línea

        } while (opcion != 0);
    }

    private void mostrarMenu() {
        System.out.println("====================================");
        System.out.println("      MENU - GESTION ACTIVISTAS     ");
        System.out.println("====================================");
        System.out.println("1) Agregar activista");
        System.out.println("2) Actualizar activista");
        System.out.println("3) Eliminar activista");
        System.out.println("4) Consultar activista por ID");
        System.out.println("5) Consultar TODOS los activistas");
        System.out.println("0) Salir");
        System.out.println("====================================");
    }

    // -------------------------
    // Opciones del menú
    // -------------------------

    private void opcionAgregar() {
        System.out.println("AGREGAR ACTIVISTA");

        String nombre = leerTexto("Nombre: ");
        String apPat = leerTexto("Apellido paterno: ");
        String apMat = leerTextoOpcional("Apellido materno (opcional): ");
        String telefono = leerTexto("Teléfono: ");
        LocalDate fechaInicio = leerFecha("Fecha inicio colaboracion (yyyy-MM-dd): ");

        Activista a = new Activista(nombre, apPat, apMat, telefono, fechaInicio);

        boolean ok = activistaDAO.agregar(a);
        if (ok) {
            System.out.println("Activista agregado con ID: " + a.getId());
            System.out.println(a);
        } else {
            System.out.println("No se pudo agregar el activista.");
        }
    }

    private void opcionActualizar() {
        System.out.println("ACTUALIZAR ACTIVISTA");

        int id = leerEntero("ID del activista a actualizar: ");
        Activista existente = activistaDAO.consultar(id);

        if (existente == null) {
            System.out.println("No existe activista con ID " + id);
            return;
        }

        System.out.println("Datos actuales:");
        System.out.println(existente);

        System.out.println("\nEscribe los nuevos datos (si quieres dejar un campo igual, presiona ENTER):");

        String nombre = leerTextoConDefault("Nombre", existente.getNombre());
        String apPat = leerTextoConDefault("Apellido paterno", existente.getApellidoPaterno());
        String apMat = leerTextoConDefault("Apellido materno", existente.getApellidoMaterno() == null ? "" : existente.getApellidoMaterno());
        String telefono = leerTextoConDefault("Telefono", existente.getTelefono());
        LocalDate fechaInicio = leerFechaConDefault("Fecha inicio colaboración (yyyy-MM-dd)", existente.getFechaInicio());

        existente.setNombre(nombre);
        existente.setApellidoPaterno(apPat);
        existente.setApellidoMaterno(apMat.isBlank() ? null : apMat);
        existente.setTelefono(telefono);
        existente.setFechaInicio(fechaInicio);

        boolean ok = activistaDAO.actualizar(existente);
        System.out.println(ok ? "Actualizado correctamente." : "No se pudo actualizar.");
    }

    private void opcionEliminar() {
        System.out.println("ELIMINAR ACTIVISTA");

        int id = leerEntero("ID del activista a eliminar: ");
        Activista existente = activistaDAO.consultar(id);

        if (existente == null) {
            System.out.println("No existe activista con ID " + id);
            return;
        }

        System.out.println("Activista encontrado:");
        System.out.println(existente);

        String confirma = leerTexto("¿Seguro que deseas eliminarlo? (s/n): ").toLowerCase();
        if (!confirma.equals("s")) {
            System.out.println("Cancelado.");
            return;
        }

        boolean ok = activistaDAO.eliminar(id);
        System.out.println(ok ? "Eliminado correctamente." : "No se pudo eliminar.");
    }

    private void opcionConsultarPorId() {
        System.out.println("CONSULTAR POR ID");

        int id = leerEntero("ID del activista: ");
        Activista a = activistaDAO.consultar(id);

        if (a == null) {
            System.out.println("No se encontro activista con ID " + id);
        } else {
            System.out.println("Activista encontrado:");
            System.out.println(a);
        }
    }

    private void opcionConsultarTodos() {
        System.out.println("LISTA DE ACTIVISTAS");

        List<Activista> lista = activistaDAO.consultarTodos();
        if (lista.isEmpty()) {
            System.out.println("No hay activistas registrados.");
            return;
        }

        System.out.println("Total: " + lista.size());
        for (Activista a : lista) {
            System.out.println(" - " + a);
        }
    }

    // -------------------------
    // Utilidades de lectura
    // -------------------------

    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String input = sc.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Debes escribir un numero entero.");
            }
        }
    }

    private String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String txt = sc.nextLine().trim();
            if (!txt.isBlank()) return txt;
            System.out.println("Este campo no puede ir vacio.");
        }
    }

    private String leerTextoOpcional(String mensaje) {
        System.out.print(mensaje);
        String txt = sc.nextLine().trim();
        return txt.isBlank() ? null : txt;
    }

    private LocalDate leerFecha(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String input = sc.nextLine().trim();
            try {
                return LocalDate.parse(input, FORMATO_FECHA);
            } catch (DateTimeParseException e) {
                System.out.println("Formato invalido. Ejemplo valido: 2024-02-01");
            }
        }
    }

    private String leerTextoConDefault(String etiqueta, String valorActual) {
        System.out.print(etiqueta + " [" + valorActual + "]: ");
        String txt = sc.nextLine().trim();
        return txt.isBlank() ? valorActual : txt;
    }

    private LocalDate leerFechaConDefault(String etiqueta, LocalDate valorActual) {
        System.out.print(etiqueta + " [" + valorActual.format(FORMATO_FECHA) + "]: ");
        String txt = sc.nextLine().trim();
        if (txt.isBlank()) return valorActual;

        try {
            return LocalDate.parse(txt, FORMATO_FECHA);
        } catch (DateTimeParseException e) {
            System.out.println("Fecha invalida, se conservara la fecha actual.");
            return valorActual;
        }
    }

    // -------------------------
    // MAIN (para correr directo)
    // -------------------------

    public static void main(String[] args) {
        // Ajusta credenciales
        String url = "jdbc:mysql://localhost:3306/eco_activistas_JDBC?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String pass = "admin"; // tu contraseña si aplica

        IConexionBD conexion = new ConexionBD(url, user, pass);
        IActivistaDAO dao = new ActivistaDAO(conexion);

        ActivistasForm form = new ActivistasForm(dao);
        form.iniciar();
    }
}
