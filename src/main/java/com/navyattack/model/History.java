package com.navyattack.model;

import java.util.List;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa el historial de una partida jugada.
 * Contiene información detallada sobre el resultado, jugadores y estadísticas.
 */
public class History implements Serializable {

    private static final long serialVersionUID = 2L; // Cambiado para nueva versión

    private String winner;
    private String loser;
    private List<User> users;
    private String timePlayed; // Formato "MM:SS"
    private long timePlayedMillis; // Tiempo en milisegundos
    private String gameMode; // "PVC" o "PVP"
    private int totalTurns;
    private int winnerShipsSunk; // Barcos hundidos por el ganador
    private int loserShipsSunk; // Barcos hundidos por el perdedor
    private LocalDateTime gameDate;
    private String gameDateFormatted;

    /**
     * Constructor completo para crear un registro de historial.
     */
    public History(List<User> users, String winner, String loser, String timePlayed,
                   long timePlayedMillis, String gameMode, int totalTurns,
                   int winnerShipsSunk, int loserShipsSunk) {
        this.users = users;
        this.winner = winner;
        this.loser = loser;
        this.timePlayed = timePlayed;
        this.timePlayedMillis = timePlayedMillis;
        this.gameMode = gameMode;
        this.totalTurns = totalTurns;
        this.winnerShipsSunk = winnerShipsSunk;
        this.loserShipsSunk = loserShipsSunk;
        this.gameDate = LocalDateTime.now();
        this.gameDateFormatted = formatGameDate();
    }

    /**
     * Constructor para compatibilidad con versiones anteriores.
     */
    public History(List<User> users, String winner, String timePlayed) {
        this.users = users;
        this.winner = winner;
        this.loser = "Unknown";
        this.timePlayed = timePlayed;
        this.timePlayedMillis = 0;
        this.gameMode = "Unknown";
        this.totalTurns = 0;
        this.winnerShipsSunk = 0;
        this.loserShipsSunk = 0;
        this.gameDate = LocalDateTime.now();
        this.gameDateFormatted = formatGameDate();
    }

    private String formatGameDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return gameDate.format(formatter);
    }

    // ==================== GETTERS ====================

    public List<User> getPlayers() {
        return this.users;
    }

    public String getWinner() {
        return this.winner;
    }

    public String getLoser() {
        return this.loser;
    }

    public String getTimePlayed() {
        return this.timePlayed;
    }

    public long getTimePlayedMillis() {
        return this.timePlayedMillis;
    }

    public String getGameMode() {
        return this.gameMode;
    }

    public int getTotalTurns() {
        return this.totalTurns;
    }

    public int getWinnerShipsSunk() {
        return this.winnerShipsSunk;
    }

    public int getLoserShipsSunk() {
        return this.loserShipsSunk;
    }

    public LocalDateTime getGameDate() {
        return this.gameDate;
    }

    public String getGameDateFormatted() {
        return this.gameDateFormatted;
    }

    /**
     * Determina si un jugador específico ganó esta partida.
     */
    public boolean didPlayerWin(String username) {
        return winner != null && winner.equals(username);
    }

    /**
     * Obtiene las estadísticas resumidas de la partida.
     */
    public String getSummary() {
        return String.format("Winner: %s | Mode: %s | Time: %s | Turns: %d",
                winner, gameMode, timePlayed, totalTurns);
    }

    @Override
    public String toString() {
        return String.format("History[winner=%s, loser=%s, time=%s, mode=%s, turns=%d]",
                winner, loser, timePlayed, gameMode, totalTurns);
    }
}