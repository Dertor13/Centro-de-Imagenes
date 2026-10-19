package Interfaz;

import datos.MedicoSolicitanteDAO;
import datos.PacienteDAO;
import datos.PracticaDAO;
import datos.TurnoDAO;
import modelo.MedicoSolicitante;
import modelo.Paciente;
import modelo.Practica;
import modelo.Turno;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class FormularioTurno extends JFrame {

    private JTextField txtDniPaciente;
    private JButton btnBuscarPaciente;
    private JLabel lblNombrePaciente;

    private JTextField txtCodigoPractica;
    private JButton btnBuscarPractica;
    private JLabel lblNombrePractica;

    private JTextField txtMatriculaMedico;
    private JButton btnBuscarMedico;
    private JLabel lblNombreMedico;

    // Campos de Fecha, Hora y Estado (Solo Lectura)
    private JTextField txtFecha;
    private JTextField txtHora;
    private JTextField txtEstado;
    
    // Desplegable de Asistencia (Editable por el operador)
    private JComboBox<String> cmbAsistencia;

    // Botones de Acción
    private JButton btnAsignar;
    private JButton btnModificar;
    private JButton btnConfirmar;
    private JButton btnCancelarTurno;
    private JButton btnCerrar;

    private GrillaTurnos grillaPadre;
    private int idTurnoEdicion = -1;

    private int idPacienteSeleccionado = -1;
    private int idPracticaSeleccionada = -1;
    private int idMedicoSeleccionado = -1;

    public FormularioTurno() {
        setTitle("Detalle y Gestión de Turno");
        setSize(720, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelContenedor = new JPanel();
        panelContenedor.setLayout(new BoxLayout(panelContenedor, BoxLayout.Y_AXIS));
        panelContenedor.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelDatos = new JPanel(new GridBagLayout());
        panelDatos.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Datos del Turno", TitledBorder.LEFT, TitledBorder.TOP
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Paciente
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
        panelDatos.add(new JLabel("Paciente (DNI):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.2;
        txtDniPaciente = new JTextField();
        panelDatos.add(txtDniPaciente, gbc);
        gbc.gridx = 2; gbc.weightx = 0.0;
        btnBuscarPaciente = new JButton("Buscar");
        panelDatos.add(btnBuscarPaciente, gbc);
        gbc.gridx = 3; gbc.weightx = 0.6;
        lblNombrePaciente = new JLabel("[Ingrese DNI]");
        lblNombrePaciente.setForeground(Color.GRAY);
        panelDatos.add(lblNombrePaciente, gbc);

        // Práctica
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        panelDatos.add(new JLabel("Práctica (Código):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.2;
        txtCodigoPractica = new JTextField();
        panelDatos.add(txtCodigoPractica, gbc);
        gbc.gridx = 2; gbc.weightx = 0.0;
        btnBuscarPractica = new JButton("Buscar");
        panelDatos.add(btnBuscarPractica, gbc);
        gbc.gridx = 3; gbc.weightx = 0.6;
        lblNombrePractica = new JLabel("[Ingrese Código]");
        lblNombrePractica.setForeground(Color.GRAY);
        panelDatos.add(lblNombrePractica, gbc);

        // Médico Derivante
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        panelDatos.add(new JLabel("Prof. Derivante (Mat):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.2;
        txtMatriculaMedico = new JTextField();
        panelDatos.add(txtMatriculaMedico, gbc);
        gbc.gridx = 2; gbc.weightx = 0.0;
        btnBuscarMedico = new JButton("Buscar");
        panelDatos.add(btnBuscarMedico, gbc);
        gbc.gridx = 3; gbc.weightx = 0.6;
        lblNombreMedico = new JLabel("[Ingrese Matrícula]");
        lblNombreMedico.setForeground(Color.GRAY);
        panelDatos.add(lblNombreMedico, gbc);

        // Fecha y Hora (CAMPOS BLOQUEADOS / SOLO LECTURA)
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0;
        panelDatos.add(new JLabel("Fecha:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 0.4;
        txtFecha = new JTextField();
        txtFecha.setEditable(false);
        txtFecha.setBackground(new Color(240, 240, 240));
        panelDatos.add(txtFecha, gbc);

        gbc.gridx = 2; gbc.gridwidth = 1; gbc.weightx = 0.0;
        JPanel panelHoraSub = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelHoraSub.add(new JLabel("Hora:"));
        txtHora = new JTextField(6);
        txtHora.setEditable(false);
        txtHora.setBackground(new Color(240, 240, 240));
        panelHoraSub.add(txtHora);
        gbc.gridx = 3; gbc.weightx = 0.6;
        panelDatos.add(panelHoraSub, gbc);

        // Estado (Fijo) y Asistencia (Desplegable interactivo)
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1; gbc.weightx = 0.0;
        panelDatos.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 0.4;
        txtEstado = new JTextField();
        txtEstado.setEditable(false);
        txtEstado.setBackground(new Color(240, 240, 240));
        panelDatos.add(txtEstado, gbc);

        gbc.gridx = 2; gbc.gridwidth = 1; gbc.weightx = 0.0;
        JPanel panelAsisSub = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelAsisSub.add(new JLabel("Asistencia:"));
        
        // ComboBox con las opciones del ENUM de tu BD
        cmbAsistencia = new JComboBox<>(new String[]{"PENDIENTE", "ASISTIO", "NO_ASISTIO"});
        panelAsisSub.add(cmbAsistencia);
        
        gbc.gridx = 3; gbc.weightx = 0.6;
        panelDatos.add(panelAsisSub, gbc);

        panelContenedor.add(panelDatos);
        panelContenedor.add(Box.createVerticalStrut(15));

        // Botones Inferiores
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnAsignar = new JButton("Asignar");
        btnModificar = new JButton("Modificar");
        btnConfirmar = new JButton("Confirmar");
        btnCancelarTurno = new JButton("Cancelar Turno");
        btnCerrar = new JButton("Cerrar");

        panelBotones.add(btnAsignar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnConfirmar);
        panelBotones.add(btnCancelarTurno);
        panelBotones.add(btnCerrar);

        panelContenedor.add(panelBotones);
        add(panelContenedor, BorderLayout.CENTER);

        configurarEventos();
    }

    public FormularioTurno(GrillaTurnos grillaPadre, int idTurno, String fecha, String hora) {
        this();
        this.grillaPadre = grillaPadre;
        this.idTurnoEdicion = idTurno;
        this.txtFecha.setText(fecha);
        this.txtHora.setText(hora);

        if (idTurnoEdicion != -1) {
            // Edición de un turno existente
            btnAsignar.setEnabled(false);
            btnModificar.setEnabled(true);
            btnConfirmar.setEnabled(true);
            btnCancelarTurno.setEnabled(true);
            cargarDatosTurno(idTurnoEdicion);
        } else {
            // Turno nuevo
            btnAsignar.setEnabled(true);
            btnModificar.setEnabled(false);
            btnConfirmar.setEnabled(false);
            btnCancelarTurno.setEnabled(false);
            
            txtEstado.setText("PENDIENTE");
            cmbAsistencia.setSelectedItem("PENDIENTE");
        }
    }

    private void configurarEventos() {
        btnBuscarPaciente.addActionListener(e -> buscarPacientePorDni());
        btnBuscarPractica.addActionListener(e -> buscarPracticaPorCodigo());
        btnBuscarMedico.addActionListener(e -> buscarMedicoPorMatricula());

        // Botones de acción principales
        btnAsignar.addActionListener(e -> registrarTurno());
        btnModificar.addActionListener(e -> modificarTurno());
        btnConfirmar.addActionListener(e -> cambiarEstadoTurno("CONFIRMADO", (String) cmbAsistencia.getSelectedItem()));
        btnCancelarTurno.addActionListener(e -> cambiarEstadoTurno("CANCELADO", "NO_ASISTIO"));
        btnCerrar.addActionListener(e -> dispose());
    }

    private void cargarDatosTurno(int idTurno) {
        try {
            TurnoDAO turnoDAO = new TurnoDAO();
            Turno t = turnoDAO.buscarPorId(idTurno);

            if (t != null) {
                this.idPacienteSeleccionado = t.getIdPaciente();
                this.idPracticaSeleccionada = t.getIdPractica();
                this.idMedicoSeleccionado = t.getIdMedicoSolicitante();

                Paciente p = new PacienteDAO().buscarPorId(t.getIdPaciente());
                if (p != null) {
                    txtDniPaciente.setText(p.getDni());
                    lblNombrePaciente.setText("✓ " + p.getApellido() + ", " + p.getNombre());
                    lblNombrePaciente.setForeground(new Color(0, 128, 0));
                }

                Practica pr = new PracticaDAO().buscarPorId(t.getIdPractica());
                if (pr != null) {
                    txtCodigoPractica.setText(pr.getCodigoNomenclador());
                    lblNombrePractica.setText("✓ " + pr.getNombre());
                    lblNombrePractica.setForeground(new Color(0, 128, 0));
                }

                MedicoSolicitante m = new MedicoSolicitanteDAO().buscarPorId(t.getIdMedicoSolicitante());
                if (m != null) {
                    txtMatriculaMedico.setText(m.getMatricula());
                    lblNombreMedico.setText("✓ Dr/a. " + m.getApellido() + ", " + m.getNombre());
                    lblNombreMedico.setForeground(new Color(0, 128, 0));
                }

                txtEstado.setText(t.getEstado());
                cmbAsistencia.setSelectedItem(t.getAsistencia());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar turno: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarPacientePorDni() {
        String dni = txtDniPaciente.getText().trim();
        if (dni.isEmpty()) return;
        try {
            Paciente p = new PacienteDAO().buscarPorDni(dni);
            if (p != null) {
                idPacienteSeleccionado = p.getIdPaciente();
                lblNombrePaciente.setText("✓ " + p.getApellido() + ", " + p.getNombre());
                lblNombrePaciente.setForeground(new Color(0, 128, 0));
            } else {
                idPacienteSeleccionado = -1;
                lblNombrePaciente.setText("✗ No encontrado");
                lblNombrePaciente.setForeground(Color.RED);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarPracticaPorCodigo() {
        String codigo = txtCodigoPractica.getText().trim();
        if (codigo.isEmpty()) return;
        try {
            Practica p = new PracticaDAO().buscarPorCodigo(codigo);
            if (p != null) {
                idPracticaSeleccionada = p.getIdPractica();
                lblNombrePractica.setText("✓ " + p.getNombre());
                lblNombrePractica.setForeground(new Color(0, 128, 0));
            } else {
                idPracticaSeleccionada = -1;
                lblNombrePractica.setText("✗ No encontrada");
                lblNombrePractica.setForeground(Color.RED);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarMedicoPorMatricula() {
        String matricula = txtMatriculaMedico.getText().trim();
        if (matricula.isEmpty()) return;
        try {
            MedicoSolicitante m = new MedicoSolicitanteDAO().buscarPorMatricula(matricula);
            if (m != null) {
                idMedicoSeleccionado = m.getIdMedicoSolicitante();
                lblNombreMedico.setText("✓ Dr/a. " + m.getApellido() + ", " + m.getNombre());
                lblNombreMedico.setForeground(new Color(0, 128, 0));
            } else {
                idMedicoSeleccionado = -1;
                lblNombreMedico.setText("✗ No encontrado");
                lblNombreMedico.setForeground(Color.RED);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarTurno() {
        if (idPacienteSeleccionado == -1 || idPracticaSeleccionada == -1 || idMedicoSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Valide Paciente, Práctica y Médico antes de asignar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Date fechaSql = Date.valueOf(LocalDate.parse(txtFecha.getText().trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            Time horaSql = Time.valueOf(LocalTime.parse(txtHora.getText().trim() + ":00"));

            String asistenciaSeleccionada = (String) cmbAsistencia.getSelectedItem();

            Turno t = new Turno(0, fechaSql, horaSql, txtEstado.getText().trim(),
                                asistenciaSeleccionada, idPacienteSeleccionado,
                                idPracticaSeleccionada, idMedicoSeleccionado);

            if (new TurnoDAO().registrar(t)) {
                JOptionPane.showMessageDialog(this, "¡Turno asignado con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                if (grillaPadre != null) grillaPadre.cargarTurnosTabla();
                dispose();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarTurno() {
        if (idTurnoEdicion == -1) return;
        try {
            String asistenciaSeleccionada = (String) cmbAsistencia.getSelectedItem();

            Turno t = new Turno(idTurnoEdicion, null, null, txtEstado.getText().trim(),
                                asistenciaSeleccionada, idPacienteSeleccionado,
                                idPracticaSeleccionada, idMedicoSeleccionado);

            if (new TurnoDAO().modificar(t)) {
                JOptionPane.showMessageDialog(this, "¡Turno modificado con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                if (grillaPadre != null) grillaPadre.cargarTurnosTabla();
                dispose();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cambiarEstadoTurno(String nuevoEstado, String nuevaAsistencia) {
        if (idTurnoEdicion == -1) return;
        
        txtEstado.setText(nuevoEstado);
        cmbAsistencia.setSelectedItem(nuevaAsistencia);

        try {
            if (new TurnoDAO().cambiarEstado(idTurnoEdicion, nuevoEstado, nuevaAsistencia)) {
                JOptionPane.showMessageDialog(this, "El turno pasó a estado: " + nuevoEstado, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                if (grillaPadre != null) grillaPadre.cargarTurnosTabla();
                dispose();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cambiar estado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}