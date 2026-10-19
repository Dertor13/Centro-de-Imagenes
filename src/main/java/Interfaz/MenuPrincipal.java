package Interfaz;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        setTitle("Sistema Centro de Imágenes - Menú Principal (TP3)");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new GridLayout(3, 1, 15, 15));
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Botones del Menú
        JButton btnPacientes = new JButton("Gestionar Pacientes");
        JButton btnTurnos = new JButton("Gestionar Turnos");
        JButton btnSalir = new JButton("Salir");

        // Acción para abrir la ventana de Pacientes
        btnPacientes.addActionListener(e -> {
            new FormularioPaciente().setVisible(true);
        });

        // Acción para abrir la ventana de Turnos
        btnTurnos.addActionListener(e -> {
            new GrillaTurnos().setVisible(true);
        });

        // Acción para salir
        btnSalir.addActionListener(e -> System.exit(0));

        // Agregamos al menú
        add(btnPacientes);
        add(btnTurnos);
        add(btnSalir);
    }
}