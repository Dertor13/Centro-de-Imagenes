package com.mycompany.centrodeimagenes;

import Interfaz.MenuPrincipal; // Coincide con el nombre del archivo en la imagen
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.SwingUtilities;

public class CentroDeImagenes {

    public static void main(String[] args) {
        try {
            // Inicializa FlatLaf moderno
            FlatLightLaf.setup();
        } catch (Exception ex) {
            System.err.println("No se pudo inicializar FlatLaf");
        }

        // Lanza la ventana apuntando a MenuPrincipal
        SwingUtilities.invokeLater(() -> {
            new MenuPrincipal().setVisible(true);
        });
    }
}