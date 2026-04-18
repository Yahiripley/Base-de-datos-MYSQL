package com.mycompany;

import com.mycompany.modelo.Libro;
import com.mycompany.persistencia.JpaConfig;
import com.mycompany.persistencia.LibroDAO;
import java.util.List;
import java.util.Scanner;

/**
 * Punto de entrada de la aplicacion de gestion de libros con JPA.
 */
public class Main {

    public static void main(String[] args) {
        LibroDAO libroDAO = new LibroDAO();
        try (Scanner scanner = new Scanner(System.in)) {
            boolean salir = false;
            while (!salir) {
                mostrarMenu();
                int opcion = leerEntero(scanner, "Selecciona una opción: ");
                switch (opcion) {
                    case 1 -> agregarLibro(scanner, libroDAO);
                    case 2 -> mostrarLibros(libroDAO);
                    case 3 -> buscarLibroPorId(scanner, libroDAO);
                    case 4 -> actualizarLibro(scanner, libroDAO);
                    case 5 -> eliminarLibro(scanner, libroDAO);
                    case 6 -> salir = true;
                    default -> System.out.println("Opción inválida.");
                }
            }
            System.out.println("Saliendo del sistema...");
        } finally {
            JpaConfig.close();
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n===== CRUD DE LIBROS (JPA) =====");
        System.out.println("1. Agregar libro");
        System.out.println("2. Mostrar libros");
        System.out.println("3. Buscar libro por ID");
        System.out.println("4. Actualizar libro");
        System.out.println("5. Eliminar libro");
        System.out.println("6. Salir");
    }

    private static void agregarLibro(Scanner scanner, LibroDAO libroDAO) {
        String titulo = leerTexto(scanner, "Título: ");
        String autor = leerTexto(scanner, "Autor: ");
        int anio = leerEntero(scanner, "Año de publicación: ");
        String genero = leerTexto(scanner, "Género: ");

        Libro libro = new Libro(titulo, autor, anio, genero);
        boolean insertado = libroDAO.insertar(libro);
        if (insertado) {
            System.out.println("Libro agregado correctamente. ID: " + libro.getId());
        } else {
            System.out.println("No se pudo agregar el libro.");
        }
    }

    private static void mostrarLibros(LibroDAO libroDAO) {
        List<Libro> libros = libroDAO.obtenerTodos();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
            return;
        }
        System.out.println("\n--- Lista de libros ---");
        for (Libro libro : libros) {
            imprimirLibro(libro);
        }
    }

    private static void buscarLibroPorId(Scanner scanner, LibroDAO libroDAO) {
        int id = leerEntero(scanner, "ID del libro: ");
        Libro libro = libroDAO.buscarPorId(id);
        if (libro == null) {
            System.out.println("No existe un libro con ID " + id + ".");
            return;
        }
        imprimirLibro(libro);
    }

    private static void actualizarLibro(Scanner scanner, LibroDAO libroDAO) {
        int id = leerEntero(scanner, "ID del libro a actualizar: ");
        Libro existente = libroDAO.buscarPorId(id);
        if (existente == null) {
            System.out.println("No existe un libro con ID " + id + ".");
            return;
        }

        String titulo = leerTexto(scanner, "Nuevo título: ");
        String autor = leerTexto(scanner, "Nuevo autor: ");
        int anio = leerEntero(scanner, "Nuevo año de publicación: ");
        String genero = leerTexto(scanner, "Nuevo género: ");

        Libro actualizado = new Libro(id, titulo, autor, anio, genero);
        boolean ok = libroDAO.actualizar(actualizado);
        if (ok) {
            System.out.println("Libro actualizado correctamente.");
        } else {
            System.out.println("No se pudo actualizar el libro.");
        }
    }

    private static void eliminarLibro(Scanner scanner, LibroDAO libroDAO) {
        int id = leerEntero(scanner, "ID del libro a eliminar: ");
        boolean eliminado = libroDAO.eliminar(id);
        if (eliminado) {
            System.out.println("Libro eliminado correctamente.");
        } else {
            System.out.println("No existe un libro con ese ID o no se pudo eliminar.");
        }
    }

    private static int leerEntero(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String valor = scanner.nextLine().trim();
            try {
                return Integer.parseInt(valor);
            } catch (NumberFormatException ex) {
                System.out.println("Debes ingresar un número entero válido.");
            }
        }
    }

    private static String leerTexto(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String valor = scanner.nextLine().trim();
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
