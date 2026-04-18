package com.mycompany;

import com.mycompany.presentacion.JFrameActivistas;

/**
 * Punto de entrada de la aplicacion de gestion de libros con JPA.
 */
public class Main {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new JFrameActivistas().setVisible(true));
    }
}
