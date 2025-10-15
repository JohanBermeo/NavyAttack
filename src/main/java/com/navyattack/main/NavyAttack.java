package com.navyattack.main;

import com.navyattack.controller.NavigationController;

/**
 * Clase principal del juego NavyAttack.
 * Punto de entrada de la aplicación que inicia el sistema de navegación
 * y el ciclo de vida de JavaFX.
 * 
 * @author Juan Manuel Otálora Hernández - Johan Stevan Bermeo Buitrago
 * @version 1.0
 */
public class NavyAttack {
    
    /**
     * Método principal que inicia la aplicación NavyAttack.
     * Delega el lanzamiento al controlador de navegación que gestiona
     * la aplicación JavaFX y las transiciones entre vistas.
     * 
     * @param args Argumentos de línea de comandos
     */
    public static void main(String[] args) {
        NavigationController.launchApp(args);
    }
}