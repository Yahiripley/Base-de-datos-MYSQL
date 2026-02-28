/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
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

/**
 * JFrame para gestión de Activistas - CRUD completo
 * Reemplaza la interfaz por consola (ActivistasForm)
 * @author omars
 */
public class ActivistasJFrame extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(ActivistasJFrame.class.getName());

    // DAO para operaciones con la BD
    private final IActivistaDAO activistaDAO;

    // Formato de fecha
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // ========== COMPONENTES ==========
    // Campos de texto
    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtApellidoPaterno;
    private JTextField txtApellidoMaterno;
    private JTextField txtTelefono;
    private JTextField txtFechaInicio;

    // Botones
    private JButton btnAgregar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnConsultar;
    private JButton btnConsultarTodos;
    private JButton btnLimpiar;

    // Tabla
    private JTable tablaActivistas;
    private DefaultTableModel modeloTabla;

    // Área de mensajes
    private JLabel lblMensaje;

    public ActivistasJFrame() {
        // Configurar conexión (ajusta tus credenciales)
        String url = "jdbc:mysql://localhost:3306/eco_activistas_JDBC?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String pass = ""; // tu contraseña

        IConexionBD conexion = new ConexionBD(url, user, pass);
        this.activistaDAO = new ActivistaDAO(conexion);

        initComponentsCustom();
        cargarTabla();
    }

    private void initComponentsCustom() {
        setTitle("Gestión de Activistas - Eco Activistas JDBC");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setResizable(false);

        // ========== PANEL PRINCIPAL ==========
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ========== PANEL SUPERIOR (Título) ==========
        JLabel lblTitulo = new JLabel("GESTIÓN DE ACTIVISTAS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(0, 102, 51));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // ========== PANEL CENTRAL (Formulario + Tabla) ==========
        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));

        // --- Panel Formulario ---
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Datos del Activista"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 0 - ID
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        txtId = new JTextField(15);
        txtId.setToolTipText("Dejar vacío para agregar nuevo. Escribir ID para consultar/actualizar/eliminar.");
        panelFormulario.add(txtId, gbc);

        // Fila 1 - Nombre
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        txtNombre = new JTextField(15);
        panelFormulario.add(txtNombre, gbc);

        // Fila 2 - Apellido Paterno
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Apellido Paterno:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        txtApellidoPaterno = new JTextField(15);
        panelFormulario.add(txtApellidoPaterno, gbc);

        // Fila 3 - Apellido Materno
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Apellido Materno:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0;
        txtApellidoMaterno = new JTextField(15);
        txtApellidoMaterno.setToolTipText("Opcional");
        panelFormulario.add(txtApellidoMaterno, gbc);

        // Fila 4 - Teléfono
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 1.0;
        txtTelefono = new JTextField(15);
        panelFormulario.add(txtTelefono, gbc);

        // Fila 5 - Fecha Inicio
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Fecha Inicio (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; gbc.weightx = 1.0;
        txtFechaInicio = new JTextField(15);
        txtFechaInicio.setToolTipText("Ejemplo: 2024-02-01");
        panelFormulario.add(txtFechaInicio, gbc);

        // --- Panel Botones ---
        JPanel panelBotones = new JPanel(new GridLayout(3, 2, 8, 8));
        panelBotones.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Acciones"));

        btnAgregar = new JButton("➕ Agregar");
        btnAgregar.setBackground(new Color(46, 139, 87));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);

        btnActualizar = new JButton("✏️ Actualizar");
        btnActualizar.setBackground(new Color(30, 144, 255));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);

        btnEliminar = new JButton("🗑️ Eliminar");
        btnEliminar.setBackground(new Color(220, 53, 69));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);

        btnConsultar = new JButton("🔍 Consultar por ID");
        btnConsultar.setFocusPainted(false);

        btnConsultarTodos = new JButton("📋 Consultar Todos");
        btnConsultarTodos.setFocusPainted(false);

        btnLimpiar = new JButton("🧹 Limpiar");
        btnLimpiar.setFocusPainted(false);

        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnConsultar);
        panelBotones.add(btnConsultarTodos);
        panelBotones.add(btnLimpiar);

        // Panel izquierdo (formulario + botones)
        JPanel panelIzquierdo = new JPanel(new BorderLayout(5, 10));
        panelIzquierdo.add(panelFormulario, BorderLayout.CENTER);
        panelIzquierdo.add(panelBotones, BorderLayout.SOUTH);

        // --- Panel Tabla ---
        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Nombre", "Ap. Paterno", "Ap. Materno", "Teléfono", "Fecha Inicio"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No editable directamente
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

        // ========== PANEL INFERIOR (Mensajes) ==========
        lblMensaje = new JLabel("Bienvenido al sistema de gestión de activistas.", SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Arial", Font.ITALIC, 13));
        lblMensaje.setForeground(Color.DARK_GRAY);
        lblMensaje.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        panelPrincipal.add(lblMensaje, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);

        // ========== EVENTOS ==========
        btnAgregar.addActionListener(e -> accionAgregar());
        btnActualizar.addActionListener(e -> accionActualizar());
        btnEliminar.addActionListener(e -> accionEliminar());
        btnConsultar.addActionListener(e -> accionConsultarPorId());
        btnConsultarTodos.addActionListener(e -> cargarTabla());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        // Al hacer clic en una fila de la tabla, llenar el formulario
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

    // ========================================
    // ACCIONES DE LOS BOTONES
    // ========================================

    private void accionAgregar() {
        if (!validarCamposObligatorios()) return;

        LocalDate fecha = parsearFecha();
        if (fecha == null) return;

        String apMat = txtApellidoMaterno.getText().trim();
        Activista a = new Activista(
                txtNombre.getText().trim(),
                txtApellidoPaterno.getText().trim(),
                apMat.isEmpty() ? null : apMat,
                txtTelefono.getText().trim(),
                fecha
        );

        boolean ok = activistaDAO.agregar(a);
        if (ok) {
            mostrarMensaje("✅ Activista agregado con ID: " + a.getId(), new Color(0, 128, 0));
            cargarTabla();
            limpiarFormulario();
        } else {
            mostrarMensaje("❌ Error al agregar el activista.", Color.RED);
        }
    }

    private void accionActualizar() {
        String idTexto = txtId.getText().trim();
        if (idTexto.isEmpty()) {
            mostrarMensaje("⚠️ Escribe el ID del activista a actualizar.", Color.ORANGE.darker());
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idTexto);
        } catch (NumberFormatException ex) {
            mostrarMensaje("⚠️ El ID debe ser un número entero.", Color.ORANGE.darker());
            return;
        }

        if (!validarCamposObligatorios()) return;

        LocalDate fecha = parsearFecha();
        if (fecha == null) return;

        String apMat = txtApellidoMaterno.getText().trim();
        Activista a = new Activista(
                id,
                txtNombre.getText().trim(),
                txtApellidoPaterno.getText().trim(),
                apMat.isEmpty() ? null : apMat,
                txtTelefono.getText().trim(),
                fecha
        );

        boolean ok = activistaDAO.actualizar(a);
        if (ok) {
            mostrarMensaje("✅ Activista actualizado correctamente.", new Color(0, 128, 0));
            cargarTabla();
            limpiarFormulario();
        } else {
            mostrarMensaje("❌ No se pudo actualizar. Verifica que el ID exista.", Color.RED);
        }
    }

    private void accionEliminar() {
        String idTexto = txtId.getText().trim();
        if (idTexto.isEmpty()) {
            mostrarMensaje("⚠️ Escribe el ID del activista a eliminar.", Color.ORANGE.darker());
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idTexto);
        } catch (NumberFormatException ex) {
            mostrarMensaje("⚠️ El ID debe ser un número entero.", Color.ORANGE.darker());
            return;
        }

        // Confirmar eliminación
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que deseas eliminar el activista con ID " + id + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (respuesta != JOptionPane.YES_OPTION) {
            mostrarMensaje("Eliminación cancelada.", Color.GRAY);
            return;
        }

        boolean ok = activistaDAO.eliminar(id);
        if (ok) {
            mostrarMensaje("✅ Activista eliminado correctamente.", new Color(0, 128, 0));
            cargarTabla();
            limpiarFormulario();
        } else {
            mostrarMensaje("❌ No se pudo eliminar. Verifica que el ID exista.", Color.RED);
        }
    }

    private void accionConsultarPorId() {
        String idTexto = txtId.getText().trim();
        if (idTexto.isEmpty()) {
            mostrarMensaje("⚠️ Escribe el ID del activista a consultar.", Color.ORANGE.darker());
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idTexto);
        } catch (NumberFormatException ex) {
            mostrarMensaje("⚠️ El ID debe ser un número entero.", Color.ORANGE.darker());
            return;
        }

        Activista a = activistaDAO.consultar(id);
        if (a == null) {
            mostrarMensaje("⚠️ No se encontró activista con ID " + id, Color.ORANGE.darker());
            return;
        }

        // Llenar formulario con los datos encontrados
        txtNombre.setText(a.getNombre());
        txtApellidoPaterno.setText(a.getApellidoPaterno());
        txtApellidoMaterno.setText(a.getApellidoMaterno() != null ? a.getApellidoMaterno() : "");
        txtTelefono.setText(a.getTelefono());
        txtFechaInicio.setText(a.getFechaInicio().format(FORMATO_FECHA));

        mostrarMensaje("🔍 Activista encontrado: " + a.getNombre() + " " + a.getApellidoPaterno(), new Color(0, 102, 153));
    }

    // ========================================
    // MÉTODOS AUXILIARES
    // ========================================

    private void cargarTabla() {
        modeloTabla.setRowCount(0); // Limpiar tabla

        List<Activista> lista = activistaDAO.consultarTodos();
        for (Activista a : lista) {
            modeloTabla.addRow(new Object[]{
                    a.getId(),
                    a.getNombre(),
                    a.getApellidoPaterno(),
                    a.getApellidoMaterno() != null ? a.getApellidoMaterno() : "",
                    a.getTelefono(),
                    a.getFechaInicio().format(FORMATO_FECHA)
            });
        }

        mostrarMensaje("📋 Total de activistas: " + lista.size(), Color.DARK_GRAY);
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        txtApellidoPaterno.setText("");
        txtApellidoMaterno.setText("");
        txtTelefono.setText("");
        txtFechaInicio.setText("");
        tablaActivistas.clearSelection();
        mostrarMensaje("Formulario limpiado.", Color.GRAY);
    }

    private boolean validarCamposObligatorios() {
        if (txtNombre.getText().trim().isEmpty()) {
            mostrarMensaje("⚠️ El nombre es obligatorio.", Color.ORANGE.darker());
            txtNombre.requestFocus();
            return false;
        }
        if (txtApellidoPaterno.getText().trim().isEmpty()) {
            mostrarMensaje("⚠️ El apellido paterno es obligatorio.", Color.ORANGE.darker());
            txtApellidoPaterno.requestFocus();
            return false;
        }
        if (txtTelefono.getText().trim().isEmpty()) {
            mostrarMensaje("⚠️ El teléfono es obligatorio.", Color.ORANGE.darker());
            txtTelefono.requestFocus();
            return false;
        }
        if (txtFechaInicio.getText().trim().isEmpty()) {
            mostrarMensaje("⚠️ La fecha de inicio es obligatoria.", Color.ORANGE.darker());
            txtFechaInicio.requestFocus();
            return false;
        }
        return true;
    }

    private LocalDate parsearFecha() {
        try {
            return LocalDate.parse(txtFechaInicio.getText().trim(), FORMATO_FECHA);
        } catch (DateTimeParseException e) {
            mostrarMensaje("⚠️ Formato de fecha inválido. Usa: yyyy-MM-dd (Ej: 2024-02-01)", Color.RED);
            txtFechaInicio.requestFocus();
            return null;
        }
    }

    private void mostrarMensaje(String mensaje, Color color) {
        lblMensaje.setText(mensaje);
        lblMensaje.setForeground(color);
    }

    // ========================================
    // MAIN
    // ========================================
    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
      }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 530, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 396, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
   
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
      

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
