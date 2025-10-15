package com.navyattack.model;

import java.util.List;

/**
 * Clase utilitaria para calcular y gestionar estadísticas de un jugador en NavyAttack.
 * Analiza el historial de partidas de un usuario y calcula métricas como victorias,
 * derrotas, porcentaje de victorias, promedios de barcos hundidos, turnos y tiempo de juego.
 * 
 * Esta clase proporciona un análisis completo del rendimiento del jugador basado
 * en todas sus partidas registradas.
 * 
 * @author Juan Manuel Otálora Hernández - Johan Stevan Bermeo Buitrago
 * @version 1.0
 */
public class UserStatistics {

    /**
     * Nombre del usuario cuyas estadísticas se están calculando.
     */
    private final String username;
    
    /**
     * Historial de partidas del usuario.
     */
    private final List<History> history;

    /**
     * Número total de derrotas del usuario.
     */
    private int defeats;
    
    /**
     * Número total de victorias del usuario.
     */
    private int victories;
    
    /**
     * Número total de partidas jugadas.
     */
    private int totalGames;
    
    /**
     * Suma total de turnos en todas las partidas.
     */
    private int totalTurns;
    
    /**
     * Número total de barcos hundidos por el usuario.
     */
    private int totalShipsSunk;
    
    /**
     * Número total de barcos perdidos por el usuario.
     */
    private int totalShipsLost;
    
    /**
     * Tiempo total de juego acumulado en milisegundos.
     */
    private long totalPlayTime;

    /**
     * Constructor de las estadísticas del usuario.
     * Calcula automáticamente todas las estadísticas basándose en el historial proporcionado.
     * 
     * @param username Nombre del usuario
     * @param history Lista de partidas jugadas por el usuario
     */
    public UserStatistics(String username, List<History> history) {
        this.history = history;
        this.username = username;
        calculateStatistics();
    }

    /**
     * Calcula todas las estadísticas del usuario basándose en su historial de partidas.
     * Procesa cada partida para determinar victorias, derrotas, barcos hundidos,
     * tiempo de juego y turnos totales.
     */
    private void calculateStatistics() {
        if (history == null || history.isEmpty()) {
            return;
        }

        totalGames = history.size();
        victories = 0;
        defeats = 0;
        totalShipsSunk = 0;
        totalShipsLost = 0;
        totalPlayTime = 0;
        totalTurns = 0;

        for (History game : history) {
            // Contar victorias y derrotas
            if (game.didPlayerWin(username)) {
                victories++;
                totalShipsSunk += game.getWinnerShipsSunk();
                totalShipsLost += game.getLoserShipsSunk();
            } else {
                defeats++;
                totalShipsSunk += game.getLoserShipsSunk();
                totalShipsLost += game.getWinnerShipsSunk();
            }

            // Acumular tiempo y turnos
            totalPlayTime += game.getTimePlayedMillis();
            totalTurns += game.getTotalTurns();
        }
    }

    /**
     * Calcula el porcentaje de victorias del usuario.
     * 
     * @return Porcentaje de victorias (0-100), o 0.0 si no hay partidas jugadas
     */
    public double getWinRate() {
        if (totalGames == 0) return 0.0;
        return (victories * 100.0) / totalGames;
    }

    /**
     * Calcula el promedio de barcos hundidos por partida.
     * 
     * @return Promedio de barcos hundidos, o 0.0 si no hay partidas jugadas
     */
    public double getAverageShipsSunk() {
        if (totalGames == 0) return 0.0;
        return (double) totalShipsSunk / totalGames;
    }

    /**
     * Calcula el promedio de turnos por partida.
     * 
     * @return Promedio de turnos, o 0.0 si no hay partidas jugadas
     */
    public double getAverageTurns() {
        if (totalGames == 0) return 0.0;
        return (double) totalTurns / totalGames;
    }

    /**
     * Obtiene el tiempo promedio de juego formateado en formato "MM:SS".
     * 
     * @return Tiempo promedio en formato "MM:SS", o "00:00" si no hay partidas jugadas
     */
    public String getAveragePlayTime() {
        if (totalGames == 0) return "00:00";
        long avgMillis = totalPlayTime / totalGames;
        long seconds = (avgMillis / 1000) % 60;
        long minutes = (avgMillis / 1000) / 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Obtiene el tiempo total de juego formateado.
     * Si el tiempo supera una hora, usa formato "HH:MM:SS", si no, "MM:SS".
     * 
     * @return Tiempo total formateado
     */
    public String getTotalPlayTimeFormatted() {
        long seconds = (totalPlayTime / 1000) % 60;
        long minutes = (totalPlayTime / 1000) / 60 % 60;
        long hours = (totalPlayTime / 1000) / 3600;

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

    /**
     * Obtiene el número total de partidas jugadas.
     * 
     * @return Número total de partidas
     */
    public int getTotalGames() {
        return totalGames;
    }

    /**
     * Obtiene el número total de victorias.
     * 
     * @return Número de victorias
     */
    public int getVictories() {
        return victories;
    }

    /**
     * Obtiene el número total de derrotas.
     * 
     * @return Número de derrotas
     */
    public int getDefeats() {
        return defeats;
    }

    /**
     * Obtiene el número total de barcos hundidos por el usuario.
     * 
     * @return Número total de barcos hundidos
     */
    public int getTotalShipsSunk() {
        return totalShipsSunk;
    }

    /**
     * Obtiene el número total de barcos perdidos por el usuario.
     * 
     * @return Número total de barcos perdidos
     */
    public int getTotalShipsLost() {
        return totalShipsLost;
    }

    /**
     * Obtiene el tiempo total de juego en milisegundos.
     * 
     * @return Tiempo total en milisegundos
     */
    public long getTotalPlayTime() {
        return totalPlayTime;
    }

    /**
     * Obtiene el número total de turnos acumulados en todas las partidas.
     * 
     * @return Número total de turnos
     */
    public int getTotalTurns() {
        return totalTurns;
    }

    /**
     * Obtiene el nombre del usuario.
     * 
     * @return Nombre del usuario
     */
    public String getUsername() {
        return username;
    }

    /**
     * Genera un resumen textual compacto de las estadísticas del usuario.
     * Incluye partidas totales, victorias, derrotas, porcentaje de victorias,
     * barcos hundidos y tiempo total de juego.
     * 
     * @return String con el resumen de estadísticas
     */
    public String getSummary() {
        return String.format(
                "Games: %d | W: %d | L: %d | Win Rate: %.1f%% | Ships Sunk: %d | Total Time: %s",
                totalGames, victories, defeats, getWinRate(), totalShipsSunk, getTotalPlayTimeFormatted()
        );
    }

    /**
     * Representación en String de las estadísticas.
     * 
     * @return Resumen de las estadísticas del usuario
     */
    @Override
    public String toString() {
        return getSummary();
    }
}