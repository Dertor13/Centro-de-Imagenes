package Interfaz;

import datos.EspecialidadDAO;
import datos.TurnoDAO;
import modelo.Especialidad;
import modelo.Turno;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.text.MaskFormatter;

public class GrillaTurnos extends JFrame {

    private JComboBox<Especialidad> cmbEspecialidad;
    private JFormattedTextField txtFecha;
    private JButton btnFiltrar;
    private JTable tablaTurnos;
    private DefaultTableModel modeloTabla;
    private JButton btnNuevoTurno;
    private JButton btnModificarTurno;
    private JButton btnEliminarTurno; // Nuevo botón
    
    public GrillaTurnos() {
        setTitle("Grilla de Turnos");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- PANEL SUPERIOR: FILTROS ---
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelFiltros.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Filtros", TitledBorder.LEFT, TitledBorder.TOP
        ));

        panelFiltros.add(new JLabel("Especialidad:"));
        cmbEspecialidad = new JComboBox<>();
        panelFiltros.add(cmbEspecialidad);

        panelFiltros.add(new JLabel("Fecha:"));
        JFormattedTextField campoFechaTemp = null;
        try {
            MaskFormatter mascaraFecha = new MaskFormatter("##/##/####");
            mascaraFecha.setPlaceholderCharacter('_'); // Muestra guiones bajos para rellenar (ej: __/__/____)
            campoFechaTemp = new JFormattedTextField(mascaraFecha);
            // Seteamos la fecha actual con el formato correcto
            campoFechaTemp.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } catch (Exception e) {
            campoFechaTemp = new JFormattedTextField(); // Plan B si falla la máscara
            campoFechaTemp.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        txtFecha = campoFechaTemp;
        txtFecha.setColumns(10);
        panelFiltros.add(txtFecha);

        btnFiltrar = new JButton("Filtrar Agenda");
        panelFiltros.add(btnFiltrar);

        add(panelFiltros, BorderLayout.NORTH);

        // --- PANEL CENTRO: TABLA DE TURNOS ---
        String[] columnas = {"ID", "Hora", "Paciente", "Práctica", "Derivante", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // La tabla no se edita directamente
            }
        };

        tablaTurnos = new JTable(modeloTabla);
        // Ocultar columna ID
        tablaTurnos.getColumnModel().getColumn(0).setMinWidth(0);
        tablaTurnos.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaTurnos.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scrollPane = new JScrollPane(tablaTurnos);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        add(scrollPane, BorderLayout.CENTER);

        // --- PANEL INFERIOR: BOTONES DE ACCIÓN ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        btnNuevoTurno = new JButton("Asignar / Gestionar Turno");
        btnModificarTurno = new JButton("Ver / Modificar Seleccionado");
        btnEliminarTurno = new JButton("Eliminar Turno"); // Inicialización del nuevo botón

        panelBotones.add(btnNuevoTurno);
        panelBotones.add(btnModificarTurno);
        panelBotones.add(btnEliminarTurno);
        add(panelBotones, BorderLayout.SOUTH);

        // Configuración inicial y eventos
        cargarEspecialidades();
        configurarEventos();
        cargarTurnosTabla();
    }

    private void cargarEspecialidades() {
        try {
            EspecialidadDAO dao = new EspecialidadDAO();
            List<Especialidad> lista = dao.listar();
            for (Especialidad e : lista) {
                cmbEspecialidad.addItem(e);
            }
        } catch (Exception ex) {
            // Manejo de error si falla la lista
        }
    }

    public void cargarTurnosTabla() {
        modeloTabla.setRowCount(0); // Limpia la tabla
        try {
            String textoFecha = txtFecha.getText().trim();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            Date fechaSql = Date.valueOf(LocalDate.parse(textoFecha, formato));

            Especialidad espSel = (Especialidad) cmbEspecialidad.getSelectedItem();
            int idEspecialidad = (espSel != null) ? espSel.getIdEspecialidad() : -1;

            TurnoDAO dao = new TurnoDAO();
            List<Object[]> datosGrilla = dao.obtenerGrillaPorFechaYEspecialidad(fechaSql, idEspecialidad);

            for (Object[] fila : datosGrilla) {
                modeloTabla.addRow(fila);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar la grilla: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configurarEventos() {
        btnFiltrar.addActionListener(e -> cargarTurnosTabla());

        // Botón "Ver / Modificar Seleccionado"
        btnModificarTurno.addActionListener(e -> abrirFormularioParaSeleccionado());

        // Botón "Asignar / Gestionar Turno"
        btnNuevoTurno.addActionListener(e -> abrirFormularioParaSeleccionado());

        // Botón "Eliminar Turno"
        btnEliminarTurno.addActionListener(e -> eliminarTurnoSeleccionado());

        // Doble Clic en cualquier fila de la tabla
        tablaTurnos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    abrirFormularioParaSeleccionado();
                }
            }
        });
    }

    private void abrirFormularioParaSeleccionado() {
        int fila = tablaTurnos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione una fila de la grilla.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object idObj = modeloTabla.getValueAt(fila, 0);
        int idTurno = (idObj != null) ? (int) idObj : -1;
        
        String hora = (String) modeloTabla.getValueAt(fila, 1);
        String fechaTexto = txtFecha.getText().trim();

        FormularioTurno form = new FormularioTurno(this, idTurno, fechaTexto, hora);
        form.setVisible(true);
    }

    private void eliminarTurnoSeleccionado() {
        int fila = tablaTurnos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione un turno de la grilla para eliminar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object idObj = modeloTabla.getValueAt(fila, 0);
        int idTurno = (idObj != null) ? (int) idObj : -1;

        // Si la celda seleccionada es un slot disponible (ID -1), no hay nada que eliminar
        if (idTurno == -1) {
            JOptionPane.showMessageDialog(this, "El horario seleccionado se encuentra disponible, no hay ningún turno asignado para eliminar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String hora = (String) modeloTabla.getValueAt(fila, 1);
        String paciente = (String) modeloTabla.getValueAt(fila, 2);

        // Cuadro de dialogo para confirmar la eliminación
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro que desea eliminar permanentemente el turno de las " + hora + " hs del paciente " + paciente + "?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                if (new TurnoDAO().eliminar(idTurno)) {
                    JOptionPane.showMessageDialog(this, "Turno eliminado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarTurnosTabla(); // Refresca la grilla liberando el espacio
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el turno.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar turno: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
