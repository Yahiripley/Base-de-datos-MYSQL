/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author omars
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ActivistasJFrame extends JFrame {

    private final IActivistaDAO activistaDAO;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtApellidoPaterno;
    private JTextField txtApellidoMaterno;
    private JTextField txtTelefono;
    private JTextField txtFechaInicio;

    private JButton btnAgregar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnConsultar;
    private JButton btnConsultarTodos;
    private JButton btnLimpiar;

    private JTable tablaActivistas;
    private DefaultTableModel modeloTabla;
    private JLabel lblMensaje;

    public ActivistasJFrame() {
        String url = "jdbc:mysql://localhost:3306/eco_activistas_JDBC?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String pass = "";

        IConexionBD conexion = new ConexionBD(url, user, pass);
        this.activistaDAO = new ActivistaDAO(conexion);

        initComponentsCustom();

        try {
            cargarTabla();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al conectar con la base de datos:\n" + e.getMessage(),
                "Error de conexion", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initComponentsCustom() {
        setTitle("Gestion de Activistas - Eco Activistas JDBC");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitulo = new JLabel("GESTION DE ACTIVISTAS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(0, 102, 51));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Datos del Activista"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        txtId = new JTextField(15);
        panelFormulario.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        txtNombre = new JTextField(15);
        panelFormulario.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Apellido Paterno:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        txtApellidoPaterno = new JTextField(15);
        panelFormulario.add(txtApellidoPaterno, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Apellido Materno:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0;
        txtApellidoMaterno = new JTextField(15);
        panelFormulario.add(txtApellidoMaterno, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Telefono:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 1.0;
        txtTelefono = new JTextField(15);
        panelFormulario.add(txtTelefono, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Fecha Inicio (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; gbc.weightx = 1.0;
        txtFechaInicio = new JTextField(15);
        panelFormulario.add(txtFechaInicio, gbc);

        JPanel panelBotones = new JPanel(new GridLayout(3, 2, 8, 8));
        panelBotones.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Acciones"));

        btnAgregar = new JButton("Agregar");
        btnAgregar.setBackground(new Color(46, 139, 87));
        btnAgregar.setForeground(Color.WHITE);

        btnActualizar = new JButton("Actualizar");
        btnActualizar.setBackground(new Color(30, 144, 255));
        btnActualizar.setForeground(Color.WHITE);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(220, 53, 69));
        btnEliminar.setForeground(Color.WHITE);

        btnConsultar = new JButton("Consultar por ID");
        btnConsultarTodos = new JButton("Consultar Todos");
        btnLimpiar = new JButton("Limpiar");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnConsultar);
        panelBotones.add(btnConsultarTodos);
        panelBotones.add(btnLimpiar);

        JPanel panelIzquierdo = new JPanel(new BorderLayout(5, 10));
        panelIzquierdo.add(panelFormulario, BorderLayout.CENTER);
        panelIzquierdo.add(panelBotones, BorderLayout.SOUTH);

        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Nombre", "Ap. Paterno", "Ap. Materno", "Telefono", "Fecha Inicio"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaActivistas = new JTable(modeloTabla);
        tablaActivistas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaActivistas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaActivistas.setRowHeight(25);

        JScrollPane scrollTabla = new JScrollPane(tablaActivistas);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Lista de Activistas"));

        panelCentral.add(panelIzquierdo, BorderLayout.WEST);
        panelCentral.add(scrollTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);

        lblMensaje = new JLabel("Bienvenido al sistema de gestion de activistas.", SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Arial", Font.ITALIC, 13));
        lblMensaje.setForeground(Color.DARK_GRAY);
        panelPrincipal.add(lblMensaje, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);

        btnAgregar.addActionListener(e -> accionAgregar());
        btnActualizar.addActionListener(e -> accionActualizar());
        btnEliminar.addActionListener(e -> accionEliminar());
        btnConsultar.addActionListener(e -> accionConsultarPorId());
        btnConsultarTodos.addActionListener(e -> cargarTabla());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        tablaActivistas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tablaActivistas.getSelectedRow();
                if (fila >= 0) {
                    txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
                    txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                    txtApellidoPaterno.setText(modeloTabla.getValueAt(fila, 2).toString());
                    Object apMat = modeloTabla.getValueAt(fila, 3);
                    txtApellidoMaterno.setText(apMat != null ? apMat.toString() : "");
                    txtTelefono.setText(modeloTabla.getValueAt(fila, 4).toString());
                    txtFechaInicio.setText(modeloTabla.getValueAt(fila, 5).toString());
                }
            }
        });
    }

    private void accionAgregar() {
        if (!validarCamposObligatorios()) return;
        LocalDate fecha = parsearFecha();
        if (fecha == null) return;

        String apMat = txtApellidoMaterno.getText().trim();
        Activista a = new Activista(txtNombre.getText().trim(), txtApellidoPaterno.getText().trim(),
                apMat.isEmpty() ? null : apMat, txtTelefono.getText().trim(), fecha);

        if (activistaDAO.agregar(a)) {
            mostrarMensaje("Activista agregado con ID: " + a.getId(), new Color(0, 128, 0));
            cargarTabla();
            limpiarFormulario();
        } else {
            mostrarMensaje("Error al agregar el activista.", Color.RED);
        }
    }

    private void accionActualizar() {
        String idTexto = txtId.getText().trim();
        if (idTexto.isEmpty()) { mostrarMensaje("Escribe el ID del activista a actualizar.", Color.ORANGE.darker()); return; }
        int id;
        try { id = Integer.parseInt(idTexto); }
        catch (NumberFormatException ex) { mostrarMensaje("El ID debe ser un numero entero.", Color.ORANGE.darker()); return; }
        if (!validarCamposObligatorios()) return;
        LocalDate fecha = parsearFecha();
        if (fecha == null) return;

        String apMat = txtApellidoMaterno.getText().trim();
        Activista a = new Activista(id, txtNombre.getText().trim(), txtApellidoPaterno.getText().trim(),
                apMat.isEmpty() ? null : apMat, txtTelefono.getText().trim(), fecha);

        if (activistaDAO.actualizar(a)) {
            mostrarMensaje("Activista actualizado correctamente.", new Color(0, 128, 0));
            cargarTabla();
            limpiarFormulario();
        } else {
            mostrarMensaje("No se pudo actualizar. Verifica que el ID exista.", Color.RED);
        }
    }

    private void accionEliminar() {
        String idTexto = txtId.getText().trim();
        if (idTexto.isEmpty()) { mostrarMensaje("Escribe el ID del activista a eliminar.", Color.ORANGE.darker()); return; }
        int id;
        try { id = Integer.parseInt(idTexto); }
        catch (NumberFormatException ex) { mostrarMensaje("El ID debe ser un numero entero.", Color.ORANGE.darker()); return; }

        int respuesta = JOptionPane.showConfirmDialog(this,
                "Estas seguro de que deseas eliminar el activista con ID " + id + "?",
                "Confirmar eliminacion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (respuesta != JOptionPane.YES_OPTION) { mostrarMensaje("Eliminacion cancelada.", Color.GRAY); return; }

        if (activistaDAO.eliminar(id)) {
            mostrarMensaje("Activista eliminado correctamente.", new Color(0, 128, 0));
            cargarTabla();
            limpiarFormulario();
        } else {
            mostrarMensaje("No se pudo eliminar. Verifica que el ID exista.", Color.RED);
        }
    }

    private void accionConsultarPorId() {
        String idTexto = txtId.getText().trim();
        if (idTexto.isEmpty()) { mostrarMensaje("Escribe el ID del activista a consultar.", Color.ORANGE.darker()); return; }
        int id;
        try { id = Integer.parseInt(idTexto); }
        catch (NumberFormatException ex) { mostrarMensaje("El ID debe ser un numero entero.", Color.ORANGE.darker()); return; }

        Activista a = activistaDAO.consultar(id);
        if (a == null) { mostrarMensaje("No se encontro activista con ID " + id, Color.ORANGE.darker()); return; }

        txtNombre.setText(a.getNombre());
        txtApellidoPaterno.setText(a.getApellidoPaterno());
        txtApellidoMaterno.setText(a.getApellidoMaterno() != null ? a.getApellidoMaterno() : "");
        txtTelefono.setText(a.getTelefono());
        txtFechaInicio.setText(a.getFechaInicio().format(FORMATO_FECHA));
        mostrarMensaje("Activista encontrado: " + a.getNombre() + " " + a.getApellidoPaterno(), new Color(0, 102, 153));
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Activista> lista = activistaDAO.consultarTodos();
        for (Activista a : lista) {
            modeloTabla.addRow(new Object[]{a.getId(), a.getNombre(), a.getApellidoPaterno(),
                    a.getApellidoMaterno() != null ? a.getApellidoMaterno() : "",
                    a.getTelefono(), a.getFechaInicio().format(FORMATO_FECHA)});
        }
        mostrarMensaje("Total de activistas: " + lista.size(), Color.DARK_GRAY);
    }

    private void limpiarFormulario() {
        txtId.setText(""); txtNombre.setText(""); txtApellidoPaterno.setText("");
        txtApellidoMaterno.setText(""); txtTelefono.setText(""); txtFechaInicio.setText("");
        tablaActivistas.clearSelection();
        mostrarMensaje("Formulario limpiado.", Color.GRAY);
    }

    private boolean validarCamposObligatorios() {
        if (txtNombre.getText().trim().isEmpty()) { mostrarMensaje("El nombre es obligatorio.", Color.ORANGE.darker()); return false; }
        if (txtApellidoPaterno.getText().trim().isEmpty()) { mostrarMensaje("El apellido paterno es obligatorio.", Color.ORANGE.darker()); return false; }
        if (txtTelefono.getText().trim().isEmpty()) { mostrarMensaje("El telefono es obligatorio.", Color.ORANGE.darker()); return false; }
        if (txtFechaInicio.getText().trim().isEmpty()) { mostrarMensaje("La fecha de inicio es obligatoria.", Color.ORANGE.darker()); return false; }
        return true;
    }

    private LocalDate parsearFecha() {
        try { return LocalDate.parse(txtFechaInicio.getText().trim(), FORMATO_FECHA); }
        catch (DateTimeParseException e) { mostrarMensaje("Formato de fecha invalido. Usa: yyyy-MM-dd", Color.RED); return null; }
    }

    private void mostrarMensaje(String mensaje, Color color) {
        lblMensaje.setText(mensaje);
        lblMensaje.setForeground(color);
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        EventQueue.invokeLater(() -> new ActivistasJFrame().setVisible(true));
    }
}