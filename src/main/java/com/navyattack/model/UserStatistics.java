package com.navyattack.model;

import java.util.List;

/**
 * Clase utilitaria para calcular estadísticas de un jugador basadas en su historial.
 */
public class UserStatistics {

    private final User user;
    private int totalGames;
    private int victories;
    private int defeats;
    private int totalShipsSunk;
    private int totalShipsLost;
    private long totalPlayTime; // en milisegundos
    private int totalTurns;

    public UserStatistics(User user) {
        this.user = user;
        calculateStatistics();
    }

    /**
     * Calcula todas las estadísticas del usuario basándose en su historial.
     */
    private void calculateStatistics() {
        List<History> history = user.getHistory();

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
            if (game.didPlayerWin(user.getUsername())) {
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
     * Obtiene el porcentaje de victorias.
     */
    public double getWinRate() {
        if (totalGames == 0) return 0.0;
        return (victories * 100.0) / totalGames;
    }

    /**
     * Obtiene el promedio de barcos hundidos por partida.
     */
    public double getAverageShipsSunk() {
        if (totalGames == 0) return 0.0;
        return (double) totalShipsSunk / totalGames;
    }

    /**
     * Obtiene el promedio de turnos por partida.
     */
    public double getAverageTurns() {
        if (totalGames == 0) return 0.0;
        return (double) totalTurns / totalGames;
    }

    /**
     * Obtiene el tiempo promedio de juego formateado.
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

    // ==================== GETTERS ====================

    public int getTotalGames() {
        return totalGames;
    }

    public int getVictories() {
        return victories;
    }

    public int getDefeats() {
        return defeats;
    }

    public int getTotalShipsSunk() {
        return totalShipsSunk;
    }

    public int getTotalShipsLost() {
        return totalShipsLost;
    }

    public long getTotalPlayTime() {
        return totalPlayTime;
    }

    public int getTotalTurns() {
        return totalTurns;
    }

    public User getUser() {
        return user;
    }

    /**
     * Genera un resumen textual de las estadísticas.
     */
    public String getSummary() {
        return String.format(
                "Games: %d | W: %d | L: %d | Win Rate: %.1f%% | Ships Sunk: %d | Total Time: %s",
                totalGames, victories, defeats, getWinRate(), totalShipsSunk, getTotalPlayTimeFormatted()
        );
    }

    @Override
    public String toString() {
        return getSummary();
    }
}