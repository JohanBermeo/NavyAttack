package com.navyattack.model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Cronómetro del juego que cuenta el tiempo transcurrido durante una partida de NavyAttack.
 * Utiliza JavaFX Timeline para actualizaciones automáticas cada segundo y proporciona
 * una propiedad observable para binding con la interfaz de usuario.
 * 
 * El cronómetro soporta operaciones de inicio, pausa, detención y reinicio,
 * manteniendo el tiempo transcurrido en formato legible (MM:SS) y en milisegundos.
 * 
 * @author Juan Manuel Otálora Hernández - Johan Stevan Bermeo Buitrago
 * @version 1.0
 */
public class GameTimer {
    
    /**
     * Timeline de JavaFX que ejecuta la actualización del cronómetro cada segundo.
     */
    private Timeline timeline;
    
    /**
     * Tiempo de inicio del cronómetro en milisegundos (timestamp).
     */
    private long startTime;
    
    /**
     * Tiempo transcurrido acumulado en milisegundos.
     */
    private long elapsedTime;
    
    /**
     * Indica si el cronómetro está actualmente en ejecución.
     */
    private boolean isRunning;
    
    /**
     * Propiedad observable del tiempo en formato String para binding con la UI.
     */
    private StringProperty timeStringProperty;
    
    /**
     * Constructor del cronómetro.
     * Inicializa el cronómetro en estado detenido con tiempo en cero
     * y configura el Timeline para actualizaciones automáticas.
     */
    public GameTimer() {
        this.elapsedTime = 0;
        this.isRunning = false;
        this.timeStringProperty = new SimpleStringProperty("00:00");
        initializeTimeline();
    }
    
    /**
     * Inicializa el Timeline de JavaFX para actualizar el cronómetro cada segundo.
     * Configura un ciclo infinito que actualiza el tiempo mientras esté en ejecución.
     */
    private void initializeTimeline() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (isRunning) {
                elapsedTime = System.currentTimeMillis() - startTime;
                updateTimeString();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }
    
    /**
     * Inicia o reanuda el cronómetro.
     * Si el cronómetro estaba pausado, continúa desde el tiempo acumulado.
     * Si es la primera vez, inicia desde cero.
     */
    public void start() {
        if (!isRunning) {
            startTime = System.currentTimeMillis() - elapsedTime;
            isRunning = true;
            timeline.play();
        }
    }
    
    /**
     * Pausa el cronómetro sin perder el tiempo acumulado.
     * El cronómetro puede reanudarse posteriormente con start().
     */
    public void pause() {
        if (isRunning) {
            isRunning = false;
            timeline.pause();
        }
    }
    
    /**
     * Detiene completamente el cronómetro.
     * El tiempo acumulado se mantiene pero el Timeline se detiene.
     */
    public void stop() {
        isRunning = false;
        timeline.stop();
    }
    
    /**
     * Reinicia el cronómetro a cero.
     * Detiene el cronómetro y resetea el tiempo transcurrido a 00:00.
     */
    public void reset() {
        stop();
        elapsedTime = 0;
        updateTimeString();
    }
    
    /**
     * Actualiza la representación en String del tiempo transcurrido.
     * Convierte los milisegundos a formato "MM:SS" y actualiza la propiedad observable.
     */
    private void updateTimeString() {
        long seconds = (elapsedTime / 1000) % 60;
        long minutes = (elapsedTime / 1000) / 60;
        timeStringProperty.set(String.format("%02d:%02d", minutes, seconds));
    }
    
    /**
     * Obtiene el tiempo transcurrido en formato legible.
     * 
     * @return String con formato "MM:SS" representando minutos y segundos
     */
    public String getFormattedTime() {
        return timeStringProperty.get();
    }
    
    /**
     * Obtiene el tiempo transcurrido en milisegundos.
     * Útil para almacenar el tiempo exacto de la partida.
     * 
     * @return Tiempo transcurrido en milisegundos
     */
    public long getElapsedTimeMillis() {
        return elapsedTime;
    }
    
    /**
     * Obtiene la propiedad del tiempo para binding con la interfaz de usuario.
     * Permite que los componentes de JavaFX se actualicen automáticamente
     * cuando el tiempo cambia.
     * 
     * @return StringProperty observable para binding
     */
    public StringProperty timeStringProperty() {
        return timeStringProperty;
    }
    
    /**
     * Verifica si el cronómetro está actualmente en ejecución.
     * 
     * @return true si el cronómetro está corriendo, false si está pausado o detenido
     */
    public boolean isRunning() {
        return isRunning;
    }
}