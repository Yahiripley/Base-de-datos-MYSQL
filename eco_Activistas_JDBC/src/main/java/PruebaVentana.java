/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author omars
 */
import javax.swing.*;

public class PruebaVentana {
    public static void main(String[] args) {
        JFrame ventana = new JFrame("Prueba");
        ventana.setSize(400, 300);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLocationRelativeTo(null);
        
        JLabel label = new JLabel("Si ves esto, Swing funciona!", SwingConstants.CENTER);
        ventana.add(label);
        
        ventana.setVisible(true);
        
        System.out.println("Ventana creada correctamente");
    }
}