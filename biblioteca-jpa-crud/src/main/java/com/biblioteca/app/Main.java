package com.biblioteca.app;

import com.biblioteca.dao.LibroDAO;
import com.biblioteca.model.Libro;
import java.util.List;
import java.util.Scanner;

/**
 * Punto de entrada de la aplicación de gestión de biblioteca con JPA.
 * Proporciona un menu de consola para realizar operaciones CRUD sobre libros.
 */
public class Main {

    public static void main(String[] args) {
        LibroDAO dao = new LibroDAO();
        try (Scanner sc = new Scanner(System.in)) {
            boolean salir = false;
            while (!salir) {
                mostrarMenu();
                int opcion = leerEntero(sc, "Seleccione una opción: ");
                switch (opcion) {
                    case 1 -> agregarLibro(sc, dao);
                    case 2 -> mostrarLibros(dao);
                    case 3 -> buscarPorId(sc, dao);
                    case 4 -> actualizarLibro(sc, dao);
                    case 5 -> eliminarLibro(sc, dao);
                    case 6 -> salir = true;
                    default -> System.out.println("Opción inválida. Intente de nuevo.");
                }
            }
            System.out.println("Saliendo del sistema...");
        } finally {
            LibroDAO.cerrarEMF();
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n===== MENÚ BIBLIOTECA =====");
        System.out.println("1. Agregar libro");
        System.out.println("2. Mostrar libros");
        System.out.println("3. Buscar libro por ID");
        System.out.println("4. Actualizar libro");
        System.out.println("5. Eliminar libro");
        System.out.println("6. Salir");
    }

    private static void agregarLibro(Scanner sc, LibroDAO dao) {
        String titulo = leerTexto(sc, "Título: ");
        String autor = leerTexto(sc, "Autor: ");
        int anio = leerEntero(sc, "Año de publicación: ");
        String genero = leerTexto(sc, "Género: ");

        Libro libro = new Libro(titulo, autor, anio, genero);
        boolean ok = dao.insertar(libro);
        if (ok) {
            System.out.println("Libro agregado correctamente. ID asignado: " + libro.getId());
        } else {
            System.out.println("No se pudo agregar el libro.");
        }
    }

    private static void mostrarLibros(LibroDAO dao) {
        List<Libro> libros = dao.obtenerTodos();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
            return;
        }
        System.out.println("\n--- Lista de libros ---");
        libros.forEach(Main::imprimirLibro);
    }

    private static void buscarPorId(Scanner sc, LibroDAO dao) {
        int id = leerEntero(sc, "ID del libro: ");
        Libro libro = dao.buscarPorId(id);
        if (libro != null) {
            imprimirLibro(libro);
        } else {
            System.out.println("No existe un libro con ID " + id + ".");
        }
    }

    private static void actualizarLibro(Scanner sc, LibroDAO dao) {
        int id = leerEntero(sc, "ID del libro a actualizar: ");
        Libro libro = dao.buscarPorId(id);
        if (libro == null) {
            System.out.println("No existe un libro con ID " + id + ".");
            return;
        }

        System.out.print("Nuevo título (" + libro.getTitulo() + "): ");
        String titulo = sc.nextLine().trim();
        if (!titulo.isEmpty()) libro.setTitulo(titulo);

        System.out.print("Nuevo autor (" + libro.getAutor() + "): ");
        String autor = sc.nextLine().trim();
        if (!autor.isEmpty()) libro.setAutor(autor);

        System.out.print("Nuevo año (" + libro.getAnioPublicacion() + "): ");
        String anioStr = sc.nextLine().trim();
        if (!anioStr.isEmpty()) {
            try {
                libro.setAnioPublicacion(Integer.parseInt(anioStr));
            } catch (NumberFormatException ex) {
                System.out.println("Año inválido; se conserva el valor anterior.");
            }
        }

        System.out.print("Nuevo género (" + libro.getGenero() + "): ");
        String genero = sc.nextLine().trim();
        if (!genero.isEmpty()) libro.setGenero(genero);

        boolean ok = dao.actualizar(libro);
        if (ok) {
            System.out.println("Libro actualizado correctamente.");
        } else {
            System.out.println("No se pudo actualizar el libro.");
        }
    }

    private static void eliminarLibro(Scanner sc, LibroDAO dao) {
        int id = leerEntero(sc, "ID del libro a eliminar: ");
        boolean eliminado = dao.eliminar(id);
        if (eliminado) {
            System.out.println("Libro eliminado correctamente.");
        } else {
            System.out.println("No existe un libro con ese ID o no se pudo eliminar.");
        }
    }

    private static int leerEntero(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String valor = sc.nextLine().trim();
            try {
                return Integer.parseInt(valor);
            } catch (NumberFormatException ex) {
                System.out.println("Debes ingresar un número entero válido.");
            }
        }
    }

    private static String leerTexto(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String valor = sc.nextLine().trim();
            if (!valor.isEmpty()) {
                return valor;
            }
            System.out.println("Este campo es obligatorio.");
        }
    }

    private static void imprimirLibro(Libro libro) {
        System.out.println("ID: " + libro.getId()
                + " | Título: " + libro.getTitulo()
                + " | Autor: " + libro.getAutor()
                + " | Año: " + libro.getAnioPublicacion()
                + " | Género: " + libro.getGenero());
    }
}
