package Interfaz;

import datos.PracticaDAO;
import modelo.Practica;

import javax.swing.*;
import java.sql.SQLException;

public class FormularioPractica extends JFrame {
    private JTextField txtCodigoNomenclador, txtNombre;
    private JButton btnBuscar, btnNuevo, btnGuardar, btnModificar, btnDarDeBaja;
    private PracticaDAO practicaDAO;

    public FormularioPractica() {
        practicaDAO = new PracticaDAO();
        setTitle("Gestión de Prácticas Médicas");
        setSize(420, 260);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        // Componentes del Buscador
        JLabel lblBuscador = new JLabel("Código Nomenclador:");
        lblBuscador.setBounds(20, 20, 130, 25);
        add(lblBuscador);

        txtCodigoNomenclador = new JTextField();
        txtCodigoNomenclador.setBounds(150, 20, 140, 25);
        add(txtCodigoNomenclador);

        btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(300, 20, 85, 25);
        add(btnBuscar);

        // Campos de datos de la práctica
        JLabel lblNombre = new JLabel("Nombre Práctica:");
        lblNombre.setBounds(20, 70, 130, 25);
        add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(150, 70, 235, 25);
        add(txtNombre);

        // Botones de acción inferior
        btnNuevo = new JButton("Nuevo");
        btnNuevo.setBounds(20, 150, 80, 30);
        add(btnNuevo);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(108, 150, 90, 30);
        add(btnGuardar);

        btnModificar = new JButton("Modificar");
        btnModificar.setBounds(205, 150, 95, 30);
        add(btnModificar);

        btnDarDeBaja = new JButton("Baja");
        btnDarDeBaja.setBounds(307, 150, 78, 30);
        add(btnDarDeBaja);

        // Eventos
        btnNuevo.addActionListener(e -> limpiarCampos());

        btnBuscar.addActionListener(e -> {
            String cod = txtCodigoNomenclador.getText().trim();
            if (cod.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese un código de nomenclador para buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                Practica p = practicaDAO.buscarPorCodigo(cod);
                if (p != null) {
                    txtNombre.setText(p.getNombre());
                } else {
                    JOptionPane.showMessageDialog(this, "Práctica no encontrada.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error en la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnGuardar.addActionListener(e -> {
            if (txtCodigoNomenclador.getText().isEmpty() || txtNombre.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Código y Nombre son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                Practica p = new Practica();
                p.setCodigoNomenclador(txtCodigoNomenclador.getText().trim());
                p.setNombre(txtNombre.getText().trim());

                if (practicaDAO.registrar(p)) {
                    JOptionPane.showMessageDialog(this, "Práctica registrada con éxito.");
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo registrar la práctica.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al registrar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnModificar.addActionListener(e -> {
            String cod = txtCodigoNomenclador.getText().trim();
            if (cod.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe especificar el código de la práctica a modificar.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                Practica p = new Practica();
                p.setCodigoNomenclador(cod);
                p.setNombre(txtNombre.getText().trim());

                if (practicaDAO.modificar(p)) {
                    JOptionPane.showMessageDialog(this, "Práctica modificada con éxito.");
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo modificar la práctica.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al modificar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnDarDeBaja.addActionListener(e -> {
            String cod = txtCodigoNomenclador.getText().trim();
            if (cod.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese el código de la práctica a dar de baja.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar la práctica con código " + cod + "?", "Confirmar baja", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    if (practicaDAO.eliminar(cod)) {
                        JOptionPane.showMessageDialog(this, "Práctica dada de baja exitosamente.");
                        limpiarCampos();
                    } else {
                        JOptionPane.showMessageDialog(this, "No se pudo eliminar la práctica.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void limpiarCampos() {
        txtCodigoNomenclador.setText("");
        txtNombre.setText("");
    }
}