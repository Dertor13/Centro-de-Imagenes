package Interfaz;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        setTitle("Sistema Centro de Imágenes - Menú Principal (TP4)");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Cambiamos a 5 filas para alojar Pacientes, Médicos, Prácticas, Turnos y Salir
        setLayout(new GridLayout(5, 1, 12, 12));
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Botones del Menú
        JButton btnPacientes = new JButton("Gestionar Pacientes");
        JButton btnMedicos = new JButton("Gestionar Médicos Solicitantes");
        JButton btnPracticas = new JButton("Gestionar Prácticas"); // NUEVO
        JButton btnTurnos = new JButton("Gestionar Turnos");
        JButton btnSalir = new JButton("Salir");
        
        // Acción para abrir la ventana de Pacientes
        btnPacientes.addActionListener(e -> {
            new FormularioPaciente().setVisible(true);
        });

        // Acción para abrir la ventana de Médicos
        btnMedicos.addActionListener(e -> {
            new FormularioMedico().setVisible(true);
        });

        // Acción para abrir la ventana de Prácticas (NUEVO)
        btnPracticas.addActionListener(e -> {
            new FormularioPractica().setVisible(true);
        });

        // Acción para abrir la ventana de Turnos
        btnTurnos.addActionListener(e -> {
            new GrillaTurnos().setVisible(true);
        });

        // Acción para salir
        btnSalir.addActionListener(e -> System.exit(0));

        // Agregamos al menú en orden
        add(btnPacientes);
        add(btnMedicos);
        add(btnPracticas); // NUEVO
        add(btnTurnos);
        add(btnSalir);
    }
}