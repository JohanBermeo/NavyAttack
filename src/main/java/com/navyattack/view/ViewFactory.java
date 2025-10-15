package com.navyattack.view;

/**
 * Interfaz funcional que representa una fábrica para crear instancias de vistas {@link IView}.
 * Se utiliza para la gestión dinámica y desacoplada de vistas dentro del sistema.
 * 
 * @author Juan Manuel Otálora Hernández - Johan Stevan Bermeo Buitrago
 * @version 1.0
 */
@FunctionalInterface
public interface ViewFactory {

    /**
     * Crea una nueva instancia de una vista que implementa {@link IView}.
     *
     * @return una instancia concreta de {@link IView}.
     */
    IView create();
}
