package Interfaz;

import datos.MedicoSolicitanteDAO;
import modelo.MedicoSolicitante;

import javax.swing.*;
import java.sql.SQLException;

public class FormularioMedico extends JFrame {
    private JTextField txtMatricula, txtNombre, txtApellido, txtEspecialidad;
    private JButton btnBuscar, btnNuevo, btnGuardar, btnModificar, btnDarDeBaja;
    private MedicoSolicitanteDAO medicoDAO;

    public FormularioMedico() {
        medicoDAO = new MedicoSolicitanteDAO();
        setTitle("Gestión de Médicos Solicitantes");
        setSize(450, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        // Componentes del Buscador
        JLabel lblBuscador = new JLabel("Ingresar Matrícula:");
        lblBuscador.setBounds(20, 20, 120, 25);
        add(lblBuscador);

        txtMatricula = new JTextField();
        txtMatricula.setBounds(140, 20, 150, 25);
        add(txtMatricula);

        btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(300, 20, 100, 25);
        add(btnBuscar);

        // Campos de datos del médico
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(20, 70, 100, 25);
        add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(120, 70, 280, 25);
        add(txtNombre);

        JLabel lblApellido = new JLabel("Apellido:");
        lblApellido.setBounds(20, 110, 100, 25);
        add(lblApellido);

        txtApellido = new JTextField();
        txtApellido.setBounds(120, 110, 280, 25);
        add(txtApellido);

        JLabel lblEspecialidad = new JLabel("Especialidad:");
        lblEspecialidad.setBounds(20, 150, 100, 25);
        add(lblEspecialidad);

        txtEspecialidad = new JTextField();
        txtEspecialidad.setBounds(120, 150, 280, 25);
        add(txtEspecialidad);

        // Botones de acción inferior
        btnNuevo = new JButton("Nuevo");
        btnNuevo.setBounds(20, 215, 85, 30);
        add(btnNuevo);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(115, 215, 85, 30);
        add(btnGuardar);

        btnModificar = new JButton("Modificar");
        btnModificar.setBounds(210, 215, 95, 30);
        add(btnModificar);

        btnDarDeBaja = new JButton("Dar de baja");
        btnDarDeBaja.setBounds(315, 215, 105, 30);
        add(btnDarDeBaja);

        // Eventos
        btnNuevo.addActionListener(e -> limpiarCampos());

        btnBuscar.addActionListener(e -> {
            String mat = txtMatricula.getText().trim();
            if (mat.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese una matrícula para buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                MedicoSolicitante m = medicoDAO.buscarPorMatricula(mat);
                if (m != null) {
                    txtNombre.setText(m.getNombre());
                    txtApellido.setText(m.getApellido());
                    txtEspecialidad.setText(m.getEspecialidad());
                } else {
                    JOptionPane.showMessageDialog(this, "Médico no encontrado.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error en la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnGuardar.addActionListener(e -> {
            if (txtMatricula.getText().isEmpty() || txtNombre.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Matrícula y Nombre son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                MedicoSolicitante m = new MedicoSolicitante();
                m.setMatricula(txtMatricula.getText().trim());
                m.setNombre(txtNombre.getText().trim());
                m.setApellido(txtApellido.getText().trim());
                m.setEspecialidad(txtEspecialidad.getText().trim());

                if (medicoDAO.registrar(m)) {
                    JOptionPane.showMessageDialog(this, "Médico registrado con éxito.");
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo registrar el médico.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al registrar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnModificar.addActionListener(e -> {
            String mat = txtMatricula.getText().trim();
            if (mat.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe especificar la matrícula del médico a modificar.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                MedicoSolicitante m = new MedicoSolicitante();
                m.setMatricula(mat);
                m.setNombre(txtNombre.getText().trim());
                m.setApellido(txtApellido.getText().trim());
                m.setEspecialidad(txtEspecialidad.getText().trim());

                if (medicoDAO.modificar(m)) {
                    JOptionPane.showMessageDialog(this, "Médico modificado con éxito.");
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo modificar el médico.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al modificar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnDarDeBaja.addActionListener(e -> {
            String mat = txtMatricula.getText().trim();
            if (mat.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese la matrícula del médico a dar de baja.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar al médico con matrícula " + mat + "?", "Confirmar baja", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    if (medicoDAO.eliminar(mat)) {
                        JOptionPane.showMessageDialog(this, "Médico dado de baja exitosamente.");
                        limpiarCampos();
                    } else {
                        JOptionPane.showMessageDialog(this, "No se pudo eliminar el médico.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void limpiarCampos() {
        txtMatricula.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtEspecialidad.setText("");
    }
}