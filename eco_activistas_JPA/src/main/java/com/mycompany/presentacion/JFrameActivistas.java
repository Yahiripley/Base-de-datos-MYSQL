package com.mycompany.presentacion;

import com.mycompany.modelo.Libro;
import com.mycompany.persistencia.JpaConfig;
import com.mycompany.persistencia.LibroDAO;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

public class JFrameActivistas extends JFrame {

    private final LibroDAO libroDAO;

    private final JTextField txtId = new JTextField();
    private final JTextField txtTitulo = new JTextField();
    private final JTextField txtAutor = new JTextField();
    private final JTextField txtAnioPublicacion = new JTextField();
    private final JTextField txtGenero = new JTextField();

    private final JLabel lblEstado = new JLabel("Gestión de libros", SwingConstants.LEFT);

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new String[]{"ID", "Título", "Autor", "Año publicación", "Género"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable tablaLibros = new JTable(modeloTabla);

    public JFrameActivistas() {
        super("CRUD de Libros - JPA");
        this.libroDAO = new LibroDAO();
        initComponents();
        cargarTabla();

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                JpaConfig.close();
            }
        });
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(980, 560);
        setLocationRelativeTo(null);

        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 8, 8));
        panelFormulario.add(new JLabel("ID"));
        panelFormulario.add(txtId);
        panelFormulario.add(new JLabel("Título"));
        panelFormulario.add(txtTitulo);
        panelFormulario.add(new JLabel("Autor"));
        panelFormulario.add(txtAutor);
        panelFormulario.add(new JLabel("Año publicación"));
        panelFormulario.add(txtAnioPublicacion);
        panelFormulario.add(new JLabel("Género"));
        panelFormulario.add(txtGenero);
        panelFormulario.add(lblEstado);
        panelFormulario.add(new JLabel(""));

        JButton btnAgregar = new JButton("Agregar libro");
        JButton btnMostrar = new JButton("Mostrar libros");
        JButton btnBuscar = new JButton("Buscar por ID");
        JButton btnActualizar = new JButton("Actualizar libro");
        JButton btnEliminar = new JButton("Eliminar libro");
        JButton btnLimpiar = new JButton("Limpiar");

        btnAgregar.addActionListener(e -> agregarLibro());
        btnMostrar.addActionListener(e -> cargarTabla());
        btnBuscar.addActionListener(e -> buscarLibro());
        btnActualizar.addActionListener(e -> actualizarLibro());
        btnEliminar.addActionListener(e -> eliminarLibro());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        JPanel panelBotones = new JPanel(new GridLayout(2, 3, 8, 8));
        panelBotones.add(btnAgregar);
        panelBotones.add(btnMostrar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        JPanel panelIzquierdo = new JPanel(new BorderLayout(10, 10));
        panelIzquierdo.add(panelFormulario, BorderLayout.NORTH);
        panelIzquierdo.add(panelBotones, BorderLayout.CENTER);

        setLayout(new BorderLayout(10, 10));
        add(panelIzquierdo, BorderLayout.WEST);
        add(new JScrollPane(tablaLibros), BorderLayout.CENTER);
    }

    private void agregarLibro() {
        Libro libro = construirLibroDesdeFormulario(false);
        if (libro == null) {
            return;
        }

        if (libroDAO.insertar(libro)) {
            lblEstado.setText("Libro agregado con ID: " + libro.getId());
            cargarTabla();
            limpiarFormulario();
        } else {
            lblEstado.setText("No se pudo agregar el libro.");
        }
    }

    private void buscarLibro() {
        Integer id = leerId();
        if (id == null) {
            return;
        }

        Libro libro = libroDAO.buscarPorId(id);
        if (libro == null) {
            lblEstado.setText("No existe un libro con ID " + id);
            return;
        }

        txtTitulo.setText(libro.getTitulo());
        txtAutor.setText(libro.getAutor());
        txtAnioPublicacion.setText(String.valueOf(libro.getAnioPublicacion()));
        txtGenero.setText(libro.getGenero());
        lblEstado.setText("Libro encontrado.");
    }

    private void actualizarLibro() {
        Libro libro = construirLibroDesdeFormulario(true);
        if (libro == null) {
            return;
        }

        if (libroDAO.actualizar(libro)) {
            lblEstado.setText("Libro actualizado correctamente.");
            cargarTabla();
            limpiarFormulario();
        } else {
            lblEstado.setText("No se pudo actualizar el libro.");
        }
    }

    private void eliminarLibro() {
        Integer id = leerId();
        if (id == null) {
            return;
        }

        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el libro con ID " + id + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);
        if (confirmar != JOptionPane.YES_OPTION) {
            return;
        }

        if (libroDAO.eliminar(id)) {
            lblEstado.setText("Libro eliminado correctamente.");
            cargarTabla();
            limpiarFormulario();
        } else {
            lblEstado.setText("No se pudo eliminar el libro.");
        }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Libro> libros = libroDAO.obtenerTodos();
        for (Libro libro : libros) {
            modeloTabla.addRow(new Object[]{
                libro.getId(),
                libro.getTitulo(),
                libro.getAutor(),
                libro.getAnioPublicacion(),
                libro.getGenero()
            });
        }
        lblEstado.setText("Total de libros: " + libros.size());
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtTitulo.setText("");
        txtAutor.setText("");
        txtAnioPublicacion.setText("");
        txtGenero.setText("");
    }

    private Integer leerId() {
        String idTexto = txtId.getText().trim();
        if (idTexto.isEmpty()) {
            lblEstado.setText("El ID es obligatorio.");
            return null;
        }
        try {
            return Integer.valueOf(idTexto);
        } catch (NumberFormatException ex) {
            lblEstado.setText("El ID debe ser numérico.");
            return null;
        }
    }

    private Libro construirLibroDesdeFormulario(boolean requiereId) {
        Integer id = null;
        if (requiereId) {
            id = leerId();
            if (id == null) {
                return null;
            }
        }

        String titulo = txtTitulo.getText().trim();
        String autor = txtAutor.getText().trim();
        String anioTexto = txtAnioPublicacion.getText().trim();
        String genero = txtGenero.getText().trim();

        if (titulo.isEmpty() || autor.isEmpty() || anioTexto.isEmpty() || genero.isEmpty()) {
            lblEstado.setText("Título, autor, año y género son obligatorios.");
            return null;
        }

        int anio;
        try {
            anio = Integer.parseInt(anioTexto);
        } catch (NumberFormatException ex) {
            lblEstado.setText("El año de publicación debe ser numérico.");
            return null;
        }

        if (anio <= 0) {
            lblEstado.setText("El año de publicación debe ser mayor a 0.");
            return null;
        }

        return requiereId
                ? new Libro(id, titulo, autor, anio, genero)
                : new Libro(titulo, autor, anio, genero);
    }
}
