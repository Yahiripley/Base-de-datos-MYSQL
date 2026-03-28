package com.mycompany;

import com.mycompany.presentacion.JFrameActivistas;

/**
 * Punto de entrada de la aplicacion Eco Activistas JPA.
 * Lanza la interfaz grafica en el hilo de eventos de Swing.
 */
public class Main {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new JFrameActivistas().setVisible(true));
    }
}
