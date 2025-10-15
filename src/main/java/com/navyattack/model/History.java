package com.navyattack.model;

import java.util.List;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa el historial de una partida jugada en NavyAttack.
 * Contiene información detallada sobre el resultado de la partida, jugadores participantes,
 * estadísticas de juego y fecha/hora. Esta clase es serializable para permitir
 * la persistencia del historial de partidas.
 * 
 * @author Juan Manuel Otálora Hernández - Johan Stevan Bermeo Buitrago
 * @version 1.0
 */
public class History implements Serializable {

    /**
     * Serial version UID para control de versiones durante la serialización.
     */
    private static final long serialVersionUID = 2L;

    /**
     * Nombre del jugador ganador de la partida.
     */
    private String winner;
    
    /**
     * Nombre del jugador perdedor de la partida.
     */
    private String loser;
    
    /**
     * Lista de usuarios que participaron en la partida.
     */
    private List<User> users;
    
    /**
     * Tiempo de duración de la partida en formato "MM:SS".
     */
    private String timePlayed;
    
    /**
     * Tiempo de duración de la partida en milisegundos.
     */
    private long timePlayedMillis;
    
    /**
     * Modo de juego de la partida ("PVC" para Player vs CPU o "PVP" para Player vs Player).
     */
    private String gameMode;
    
    /**
     * Número total de turnos transcurridos durante la partida.
     */
    private int totalTurns;
    
    /**
     * Número de barcos hundidos por el ganador.
     */
    private int winnerShipsSunk;
    
    /**
     * Número de barcos hundidos por el perdedor.
     */
    private int loserShipsSunk;
    
    /**
     * Fecha y hora en que se jugó la partida.
     */
    private LocalDateTime gameDate;
    
    /**
     * Fecha y hora formateada de la partida.
     */
    private String gameDateFormatted;

    /**
     * Constructor completo para crear un registro de historial con todas las estadísticas.
     * 
     * @param users Lista de usuarios que participaron en la partida
     * @param winner Nombre del jugador ganador
     * @param loser Nombre del jugador perdedor
     * @param timePlayed Tiempo de juego en formato "MM:SS"
     * @param timePlayedMillis Tiempo de juego en milisegundos
     * @param gameMode Modo de juego ("PVC" o "PVP")
     * @param totalTurns Número total de turnos
     * @param winnerShipsSunk Barcos hundidos por el ganador
     * @param loserShipsSunk Barcos hundidos por el perdedor
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
     * Constructor simplificado para compatibilidad con versiones anteriores.
     * Inicializa valores por defecto para estadísticas no proporcionadas.
     * 
     * @param users Lista de usuarios que participaron en la partida
     * @param winner Nombre del jugador ganador
     * @param timePlayed Tiempo de juego en formato "MM:SS"
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

    /**
     * Formatea la fecha y hora de la partida en formato "dd/MM/yyyy HH:mm".
     * 
     * @return Fecha y hora formateada como String
     */
    private String formatGameDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return gameDate.format(formatter);
    }

    /**
     * Obtiene la lista de jugadores que participaron en la partida.
     * 
     * @return Lista de usuarios participantes
     */
    public List<User> getPlayers() {
        return this.users;
    }

    /**
     * Obtiene el nombre del jugador ganador.
     * 
     * @return Nombre del ganador
     */
    public String getWinner() {
        return this.winner;
    }

    /**
     * Obtiene el nombre del jugador perdedor.
     * 
     * @return Nombre del perdedor
     */
    public String getLoser() {
        return this.loser;
    }

    /**
     * Obtiene el tiempo de duración de la partida en formato legible.
     * 
     * @return Tiempo en formato "MM:SS"
     */
    public String getTimePlayed() {
        return this.timePlayed;
    }

    /**
     * Obtiene el tiempo de duración de la partida en milisegundos.
     * 
     * @return Tiempo en milisegundos
     */
    public long getTimePlayedMillis() {
        return this.timePlayedMillis;
    }

    /**
     * Obtiene el modo de juego de la partida.
     * 
     * @return "PVC" para Player vs CPU o "PVP" para Player vs Player
     */
    public String getGameMode() {
        return this.gameMode;
    }

    /**
     * Obtiene el número total de turnos transcurridos en la partida.
     * 
     * @return Número total de turnos
     */
    public int getTotalTurns() {
        return this.totalTurns;
    }

    /**
     * Obtiene el número de barcos hundidos por el ganador.
     * 
     * @return Barcos hundidos por el ganador
     */
    public int getWinnerShipsSunk() {
        return this.winnerShipsSunk;
    }

    /**
     * Obtiene el número de barcos hundidos por el perdedor.
     * 
     * @return Barcos hundidos por el perdedor
     */
    public int getLoserShipsSunk() {
        return this.loserShipsSunk;
    }

    /**
     * Obtiene la fecha y hora en que se jugó la partida.
     * 
     * @return LocalDateTime de la partida
     */
    public LocalDateTime getGameDate() {
        return this.gameDate;
    }

    /**
     * Obtiene la fecha y hora formateada de la partida.
     * 
     * @return Fecha y hora en formato "dd/MM/yyyy HH:mm"
     */
    public String getGameDateFormatted() {
        return this.gameDateFormatted;
    }

    /**
     * Determina si un jugador específico ganó esta partida.
     * 
     * @param username Nombre del usuario a verificar
     * @return true si el usuario ganó la partida, false en caso contrario
     */
    public boolean didPlayerWin(String username) {
        return winner != null && winner.equals(username);
    }

    /**
     * Obtiene un resumen de las estadísticas de la partida.
     * Incluye ganador, modo de juego, tiempo y turnos.
     * 
     * @return String con el resumen de la partida
     */
    public String getSummary() {
        return String.format("Winner: %s | Mode: %s | Time: %s | Turns: %d",
                winner, gameMode, timePlayed, totalTurns);
    }

    /**
     * Representación en String del historial.
     * 
     * @return String con los datos principales del historial
     */
    @Override
    public String toString() {
        return String.format("History[winner=%s, loser=%s, time=%s, mode=%s, turns=%d]",
                winner, loser, timePlayed, gameMode, totalTurns);
    }
}