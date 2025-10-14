package com.navyattack.model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Cronómetro del juego que cuenta el tiempo transcurrido durante una partida.
 * Utiliza JavaFX Timeline para actualizaciones automáticas.
 */
public class GameTimer {

    private Timeline timeline;
    private long startTime;
    private long elapsedTime;
    private boolean isRunning;
    private StringProperty timeStringProperty;

    public GameTimer() {
        this.elapsedTime = 0;
        this.isRunning = false;
        this.timeStringProperty = new SimpleStringProperty("00:00");
        initializeTimeline();
    }

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
     * Inicia el cronómetro.
     */
    public void start() {
        if (!isRunning) {
            startTime = System.currentTimeMillis() - elapsedTime;
            isRunning = true;
            timeline.play();
        }
    }

    /**
     * Pausa el cronómetro.
     */
    public void pause() {
        if (isRunning) {
            isRunning = false;
            timeline.pause();
        }
    }

    /**
     * Detiene el cronómetro.
     */
    public void stop() {
        isRunning = false;
        timeline.stop();
    }

    /**
     * Reinicia el cronómetro a cero.
     */
    public void reset() {
        stop();
        elapsedTime = 0;
        updateTimeString();
    }

    /**
     * Actualiza la representación en String del tiempo.
     */
    private void updateTimeString() {
        long seconds = (elapsedTime / 1000) % 60;
        long minutes = (elapsedTime / 1000) / 60;
        timeStringProperty.set(String.format("%02d:%02d", minutes, seconds));
    }

    /**
     * Obtiene el tiempo transcurrido en formato legible.
     * @return String con formato "MM:SS"
     */
    public String getFormattedTime() {
        return timeStringProperty.get();
    }

    /**
     * Obtiene el tiempo transcurrido en milisegundos.
     * @return tiempo en milisegundos
     */
    public long getElapsedTimeMillis() {
        return elapsedTime;
    }

    /**
     * Obtiene la propiedad del tiempo para binding con UI.
     * @return StringProperty para binding
     */
    public StringProperty timeStringProperty() {
        return timeStringProperty;
    }

    /**
     * Verifica si el cronómetro está corriendo.
     * @return true si está corriendo, false en caso contrario
     */
    public boolean isRunning() {
        return isRunning;
    }
}