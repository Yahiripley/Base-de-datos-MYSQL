import java.sql.*;
import java.time.LocalDate;

//INSERTAMOS CLIENTE
public class EcoActivistasDemoJDBC {

    // 1) Configuración de conexión
    private static final String URL  = "jdbc:mysql://localhost:3306/eco_activistas_JDBC?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "admin"; // <- pon tu contraseña si tienes

    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {

            System.out.println("Conexion exitosa a MySQL.");

            // 2) Insertar Cliente
            int idCliente = insertarCliente(con,
                    "Jose Luis",
                    "Robles",
                    "Reyes",
                    "Guaymas, Sonora, México");

            // 3) Insertar teléfonos del cliente
            insertarTelefonoCliente(con, idCliente, "642-111-2222");
            insertarTelefonoCliente(con, idCliente, "642-333-4444");

            // 4) Insertar Problema
            int idProblema = insertarProblema(con,
                    LocalDate.now(),
                    LocalDate.now().plusDays(15),
                    "pendiente",
                    idCliente);

            // 5) Insertar Activista
            int idActivista = insertarActivista(con,
                    "Luis",
                    "Ramirez",
                    "Lopez",
                    "644-555-6666",
                    LocalDate.of(2024, 1, 10));

            // 6) Asignar activista al problema (tabla puente)
            asignarActivistaAProblema(con, idProblema, idActivista);

            // 7) Consultar y mostrar
            System.out.println("\nCONSULTA: Problemas con cliente:");
            listarProblemasConCliente(con);

        } catch (SQLException ex) {
            System.err.println("Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // ------------------------------
    // INSERTS (con retorno de ID)
    // ------------------------------

    private static int insertarCliente(Connection con, String nombre, String apPat, String apMat, String direccion)
            throws SQLException {

        String sql = """
                INSERT INTO cliente (nombre, apellido_paterno, apellido_materno, direccion)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre);
            ps.setString(2, apPat);
            ps.setString(3, apMat);
            ps.setString(4, direccion);

            ps.executeUpdate();

            // Recuperar ID generado
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    System.out.println("Cliente insertado. ID = " + id);
                    return id;
                }
            }
        }

        throw new SQLException("No se pudo obtener el ID del cliente.");
    }

    private static void insertarTelefonoCliente(Connection con, int idCliente, String numTelefono)
            throws SQLException {

        String sql = """
                INSERT INTO telefonos_cliente (id_cliente, num_telefono)
                VALUES (?, ?)
                """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ps.setString(2, numTelefono);

            ps.executeUpdate();
            System.out.println("Teléfono agregado al cliente " + idCliente + ": " + numTelefono);
        }
    }

    private static int insertarProblema(Connection con, LocalDate fechaInicio, LocalDate fechaFin, String estado, int idCliente)
            throws SQLException {

        String sql = """
                INSERT INTO problema (fecha_inicio, fecha_fin, estado, id_cliente)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(fechaInicio));
            ps.setDate(2, (fechaFin != null) ? Date.valueOf(fechaFin) : null);
            ps.setString(3, estado); // estado es ENUM en la BD
            ps.setInt(4, idCliente);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    System.out.println("Problema insertado. ID = " + id);
                    return id;
                }
            }
        }

        throw new SQLException("No se pudo obtener el ID del problema.");
    }

    private static int insertarActivista(Connection con, String nombre, String apPat, String apMat, String telefono, LocalDate fechaInicioColab)
            throws SQLException {

        String sql = """
                INSERT INTO activista (nombre, apellido_paterno, apellido_materno, telefono, fecha_inicio_colaboracion)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre);
            ps.setString(2, apPat);
            ps.setString(3, apMat);
            ps.setString(4, telefono);
            ps.setDate(5, Date.valueOf(fechaInicioColab));

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    System.out.println("Activista insertado. ID = " + id);
                    return id;
                }
            }
        }

        throw new SQLException("No se pudo obtener el ID del activista.");
    }

    private static void asignarActivistaAProblema(Connection con, int idProblema, int idActivista)
            throws SQLException {

        String sql = """
                INSERT INTO problema_activistas (id_problema, id_activista)
                VALUES (?, ?)
                """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProblema);
            ps.setInt(2, idActivista);

            ps.executeUpdate();
            System.out.println("Activista " + idActivista + " asignado al problema " + idProblema);
        }
    }

    // ------------------------------
    // CONSULTA
    // ------------------------------

    private static void listarProblemasConCliente(Connection con) throws SQLException {
        String sql = """
                SELECT p.id AS problema_id,
                       p.estado,
                       p.fecha_inicio,
                       p.fecha_fin,
                       c.nombre,
                       c.apellido_paterno,
                       c.apellido_materno
                FROM problema p
                INNER JOIN cliente c ON c.id = p.id_cliente
                ORDER BY p.id DESC
                """;

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int idProblema = rs.getInt("problema_id");
                String estado = rs.getString("estado");
                Date fi = rs.getDate("fecha_inicio");
                Date ff = rs.getDate("fecha_fin");
                String nombre = rs.getString("nombre");
                String apPat = rs.getString("apellido_paterno");
                String apMat = rs.getString("apellido_materno");

                System.out.println("• Problema #" + idProblema
                        + " | Estado: " + estado
                        + " | Inicio: " + fi
                        + " | Fin: " + ff
                        + " | Cliente: " + nombre + " " + apPat + " " + (apMat != null ? apMat : ""));
            }
        }
    }
}
