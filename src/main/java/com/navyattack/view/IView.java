package com.navyattack.view;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Interfaz base para todas las vistas del sistema NavyAttack.
 * Define los métodos esenciales que deben implementar las vistas
 * para su inicialización y obtención de la escena principal.
 * 
 * @author Juan Manuel Otálora Hernández - Johan Stevan Bermeo Buitrago
 * @version 1.0
 */
public interface IView {

    /**
     * Inicia la vista y configura los componentes gráficos dentro del escenario especificado.
     *
     * @param stage el {@link Stage} principal donde se mostrará la vista.
     */
    void start(Stage stage);

    /**
     * Devuelve la escena asociada a la vista actual.
     *
     * @return la {@link Scene} correspondiente a la vista.
     */
    Scene getScene();
}
