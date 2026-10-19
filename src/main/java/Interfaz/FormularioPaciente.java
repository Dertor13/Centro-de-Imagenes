package Interfaz;

import datos.ObraSocialDAO;
import datos.PacienteDAO;
import modelo.ObraSocial;
import modelo.Paciente;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FormularioPaciente extends JFrame {

    // Componentes del Buscador
    private JTextField txtDniBusqueda;
    private JButton btnBuscar;

    // Componentes de Datos del Paciente
    private JTextField txtDni;
    private JFormattedTextField txtFechaNac;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtTelefono;
    private JTextField txtEmail;
    private JComboBox<ObraSocial> cmbObraSocial;

    // Botones de acción inferiores
    private JButton btnNuevo;
    private JButton btnGuardar;
    private JButton btnModificar;
    private JButton btnDarBaja;

    // Control interno para identificar al paciente actual al modificar o dar de baja
    private int idPacienteActual = -1;

    public FormularioPaciente() {
        setTitle("Gestión de Pacientes");
        setSize(600, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel contenedor principal con márgenes internos
        JPanel panelContenedor = new JPanel();
        panelContenedor.setLayout(new BoxLayout(panelContenedor, BoxLayout.Y_AXIS));
        panelContenedor.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- 1. PANEL BUSCADOR ---
        JPanel panelBuscador = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelBuscador.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Buscador", TitledBorder.LEFT, TitledBorder.TOP
        ));
        
        panelBuscador.add(new JLabel("Ingresar DNI:"));
        txtDniBusqueda = new JTextField(15);
        panelBuscador.add(txtDniBusqueda);
        
        btnBuscar = new JButton("Buscar");
        panelBuscador.add(btnBuscar);

        panelContenedor.add(panelBuscador);
        panelContenedor.add(Box.createVerticalStrut(10));

        // --- 2. PANEL DATOS DEL PACIENTE (GridBagLayout para dos columnas prolijas) ---
        JPanel panelDatos = new JPanel(new GridBagLayout());
        panelDatos.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Datos del Paciente", TitledBorder.LEFT, TitledBorder.TOP
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 0: DNI y Fecha de Nacimiento
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
        panelDatos.add(new JLabel("DNI:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4;
        txtDni = new JTextField();
        panelDatos.add(txtDni, gbc);

        gbc.gridx = 2; gbc.weightx = 0.0;
        panelDatos.add(new JLabel("F. Nacimiento:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4;

        // CREACIÓN DE CAMPO CON MÁSCARA PARA FECHA DE NACIMIENTO
        try {
            MaskFormatter mascaraFecha = new MaskFormatter("##/##/####");
            mascaraFecha.setPlaceholderCharacter('_');
            txtFechaNac = new JFormattedTextField(mascaraFecha);
        } catch (Exception e) {
            txtFechaNac = new JFormattedTextField();
        }
        panelDatos.add(txtFechaNac, gbc);

        // Fila 1: Nombre y Apellido
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        panelDatos.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4;
        txtNombre = new JTextField();
        panelDatos.add(txtNombre, gbc);

        gbc.gridx = 2; gbc.weightx = 0.0;
        panelDatos.add(new JLabel("Apellido:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4;
        txtApellido = new JTextField();
        panelDatos.add(txtApellido, gbc);

        // Fila 2: Teléfono y Email
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        panelDatos.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4;
        txtTelefono = new JTextField();
        panelDatos.add(txtTelefono, gbc);

        gbc.gridx = 2; gbc.weightx = 0.0;
        panelDatos.add(new JLabel("Email:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4;
        txtEmail = new JTextField();
        panelDatos.add(txtEmail, gbc);

        // Fila 3: Obra Social (Ocupa todo el ancho inferior del panel)
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0;
        panelDatos.add(new JLabel("Obra Social:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        cmbObraSocial = new JComboBox<>();
        panelDatos.add(cmbObraSocial, gbc);

        panelContenedor.add(panelDatos);
        panelContenedor.add(Box.createVerticalStrut(15));

        // --- 3. PANEL BOTONES INFERIORES ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnNuevo = new JButton("Nuevo");
        btnGuardar = new JButton("Guardar");
        btnModificar = new JButton("Modificar");
        btnDarBaja = new JButton("Dar de baja");

        panelBotones.add(btnNuevo);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnDarBaja);

        panelContenedor.add(panelBotones);

        add(panelContenedor, BorderLayout.CENTER);

        // Carga de datos iniciales y configuración de eventos
        cargarObrasSociales();
        configurarEventos();
    }

    private void cargarObrasSociales() {
        try {
            ObraSocialDAO dao = new ObraSocialDAO();
            List<ObraSocial> lista = dao.listar();
            for (ObraSocial os : lista) {
                cmbObraSocial.addItem(os);
            }
        } catch (Exception e) {
            // Control de error en caso de que falle la conexión inicial
        }
    }

    private void configurarEventos() {
        // Buscar paciente por DNI del buscador superior
        btnBuscar.addActionListener(e -> buscarPaciente());

        // Botón Nuevo (Limpia el formulario para cargar uno nuevo)
        btnNuevo.addActionListener(e -> limpiarFormulario());

        // Botón Guardar
        btnGuardar.addActionListener(e -> guardarPaciente());

        // Botón Modificar
        btnModificar.addActionListener(e -> modificarPaciente());

        // Botón Dar de baja
        btnDarBaja.addActionListener(e -> darDeBajaPaciente());
    }

    private void buscarPaciente() {
        String dniBusqueda = txtDniBusqueda.getText().trim();
        if (dniBusqueda.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un DNI para buscar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            PacienteDAO dao = new PacienteDAO();
            Paciente p = dao.buscarPorDni(dniBusqueda);

            if (p != null) {
                idPacienteActual = p.getIdPaciente();
                txtDni.setText(p.getDni());
                txtNombre.setText(p.getNombre());
                txtApellido.setText(p.getApellido());
                
                if (p.getFechaNacimiento() != null) {
                    txtFechaNac.setText(p.getFechaNacimiento().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                } else {
                    txtFechaNac.setText("");
                }
                
                txtTelefono.setText(p.getTelefono());
                txtEmail.setText(p.getEmail());

                // Seleccionar en el combo haciendo coincidir idObraSocial
                if (p.getIdObraSocial() != null) {
                    for (int i = 0; i < cmbObraSocial.getItemCount(); i++) {
                        ObraSocial os = cmbObraSocial.getItemAt(i);
                        if (os.getIdObraSocial() == p.getIdObraSocial()) {
                            cmbObraSocial.setSelectedIndex(i);
                            break;
                        }
                    }
                }

                JOptionPane.showMessageDialog(this, "Paciente encontrado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró ningún paciente con ese DNI.", "Aviso", JOptionPane.WARNING_MESSAGE);
                limpiarFormulario();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al buscar paciente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarPaciente() {
        try {
            String dni = txtDni.getText().trim();
            String nombre = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            
            String textoFecha = txtFechaNac.getText().trim();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            Date fechaNac = Date.valueOf(LocalDate.parse(textoFecha, formato));
            
            String telefono = txtTelefono.getText().trim();
            String email = txtEmail.getText().trim();
            
            ObraSocial osSeleccionada = (ObraSocial) cmbObraSocial.getSelectedItem();
            Integer idObraSocial = (osSeleccionada != null) ? osSeleccionada.getIdObraSocial() : null;

            Paciente nuevo = new Paciente(0, dni, nombre, apellido, fechaNac, telefono, email, idObraSocial);
            PacienteDAO dao = new PacienteDAO();

            if (dao.registrar(nuevo)) {
                JOptionPane.showMessageDialog(this, "¡Paciente registrado con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar el paciente.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar (verifique formato DD/MM/YYYY): " + e.getMessage(), "Error de Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void modificarPaciente() {
        if (idPacienteActual == -1) {
            JOptionPane.showMessageDialog(this, "Primero debe buscar y cargar un paciente para poder modificarlo.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String dni = txtDni.getText().trim();
            String nombre = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            
            String textoFecha = txtFechaNac.getText().trim();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            Date fechaNac = Date.valueOf(LocalDate.parse(textoFecha, formato));
            
            String telefono = txtTelefono.getText().trim();
            String email = txtEmail.getText().trim();
            
            ObraSocial osSeleccionada = (ObraSocial) cmbObraSocial.getSelectedItem();
            Integer idObraSocial = (osSeleccionada != null) ? osSeleccionada.getIdObraSocial() : null;

            Paciente pacienteMod = new Paciente(idPacienteActual, dni, nombre, apellido, fechaNac, telefono, email, idObraSocial);
            PacienteDAO dao = new PacienteDAO();

            boolean exito = dao.modificar(pacienteMod);

            if (exito) {
                JOptionPane.showMessageDialog(this, "¡Paciente modificado con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo modificar el paciente.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void darDeBajaPaciente() {
        if (idPacienteActual == -1) {
            JOptionPane.showMessageDialog(this, "Primero debe buscar y seleccionar un paciente para dar de baja.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de dar de baja a este paciente?", "Confirmar baja", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                PacienteDAO dao = new PacienteDAO();
                boolean exito = dao.eliminar(idPacienteActual);

                if (exito) {
                    JOptionPane.showMessageDialog(this, "Paciente dado de baja correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarFormulario();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo dar de baja al paciente.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al dar de baja: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarFormulario() {
        txtDniBusqueda.setText("");
        txtDni.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtFechaNac.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        if (cmbObraSocial.getItemCount() > 0) {
            cmbObraSocial.setSelectedIndex(0);
        }
        idPacienteActual = -1;
    }

}