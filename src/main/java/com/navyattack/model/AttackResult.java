package com.navyattack.model;

/**
 * Enumeración que representa los posibles resultados de un ataque en el juego.
 *
 * Esta clase es parte del MODELO DE DOMINIO porque representa conceptos
 * que existen en el juego real de Batalla Naval.
 *
 * @author NavyAttack Team
 * @version 1.0
 */
public enum AttackResult {

    /**
     * El ataque impactó una celda vacía (agua).
     * El jugador falló el disparo.
     */
    MISS("Miss", "The attack hit water", "○"),

    /**
     * El ataque impactó parte de un barco.
     * El barco fue dañado pero NO hundido.
     */
    HIT("Hit", "The attack hit a ship", "X"),

    /**
     * El ataque impactó y hundió completamente un barco.
     * Todas las partes del barco han sido golpeadas.
     */
    SUNK("Sunk", "The ship has been sunk", "💥"),

    /**
     * Se intentó atacar una celda que ya fue atacada previamente.
     * No es un movimiento válido.
     */
    ALREADY_ATTACKED("Already Attacked", "This position was already attacked", "⚠"),

    /**
     * Posición inválida (fuera del tablero).
     * No debería ocurrir con validación adecuada.
     */
    INVALID_POSITION("Invalid Position", "Position is out of bounds", "✗");

    // ==================== ATRIBUTOS ====================

    /**
     * Nombre corto del resultado (para logs y UI simple)
     */
    private final String displayName;

    /**
     * Descripción detallada del resultado
     */
    private final String description;

    /**
     * Símbolo visual para representar el resultado en la UI
     */
    private final String symbol;

    // ==================== CONSTRUCTOR ====================

    /**
     * Constructor privado del enum.
     *
     * @param displayName Nombre para mostrar en la UI
     * @param description Descripción detallada
     * @param symbol Símbolo visual
     */
    AttackResult(String displayName, String description, String symbol) {
        this.displayName = displayName;
        this.description = description;
        this.symbol = symbol;
    }

    // ==================== GETTERS ====================

    /**
     * Obtiene el nombre para mostrar del resultado.
     *
     * @return Nombre corto del resultado
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Obtiene la descripción detallada del resultado.
     *
     * @return Descripción completa
     */
    public String getDescription() {
        return description;
    }

    /**
     * Obtiene el símbolo visual del resultado.
     *
     * @return Símbolo para mostrar en la UI
     */
    public String getSymbol() {
        return symbol;
    }

    // ==================== MÉTODOS DE UTILIDAD ====================

    /**
     * Verifica si el resultado es un impacto exitoso (HIT o SUNK).
     *
     * @return true si el ataque fue exitoso, false en caso contrario
     */
    public boolean isSuccessfulHit() {
        return this == HIT || this == SUNK;
    }

    /**
     * Verifica si el resultado terminó el juego para un barco (SUNK).
     *
     * @return true si un barco fue hundido, false en caso contrario
     */
    public boolean isShipSunk() {
        return this == SUNK;
    }

    /**
     * Verifica si el resultado es un movimiento inválido.
     *
     * @return true si el movimiento no es válido, false en caso contrario
     */
    public boolean isInvalidMove() {
        return this == ALREADY_ATTACKED || this == INVALID_POSITION;
    }

    /**
     * Verifica si el resultado requiere cambio de turno.
     * Solo ALREADY_ATTACKED e INVALID_POSITION no cambian el turno.
     *
     * @return true si debe cambiar el turno, false en caso contrario
     */
    public boolean shouldChangeTurn() {
        return !isInvalidMove();
    }

    /**
     * Obtiene el color asociado al resultado para la UI.
     * Útil para colorear celdas según el resultado.
     *
     * @return Código de color hexadecimal
     */
    public String getColorCode() {
        return switch (this) {
            case MISS -> "#4444ff";              // Azul (agua)
            case HIT -> "#ff4444";               // Rojo (impacto)
            case SUNK -> "#ff0000";              // Rojo oscuro (hundido)
            case ALREADY_ATTACKED -> "#888888";  // Gris (ya atacado)
            case INVALID_POSITION -> "#000000";  // Negro (inválido)
        };
    }

    /**
     * Obtiene puntos asociados al resultado (para sistema de puntuación).
     *
     * @return Puntos obtenidos por este resultado
     */
    public int getPoints() {
        return switch (this) {
            case MISS -> 0;
            case HIT -> 10;
            case SUNK -> 50;
            case ALREADY_ATTACKED -> -5;  // Penalización
            case INVALID_POSITION -> 0;
        };
    }

    /**
     * Verifica si el resultado debe mostrarse con efecto especial.
     *
     * @return true si debe tener animación/efecto especial
     */
    public boolean hasSpecialEffect() {
        return this == SUNK;
    }

    /**
     * Obtiene un mensaje personalizado para el jugador.
     *
     * @param shipName Nombre del barco (opcional, puede ser null)
     * @return Mensaje formateado para mostrar al jugador
     */
    public String getPlayerMessage(String shipName) {
        return switch (this) {
            case MISS -> "Water! You missed the target.";
            case HIT -> shipName != null
                    ? String.format("Hit! You damaged the %s!", shipName)
                    : "Hit! You damaged an enemy ship!";
            case SUNK -> shipName != null
                    ? String.format("SUNK! You destroyed the %s!", shipName)
                    : "SUNK! You destroyed an enemy ship!";
            case ALREADY_ATTACKED -> "You already attacked this position!";
            case INVALID_POSITION -> "Invalid position. Try again.";
        };
    }

    /**
     * Representación en String del resultado.
     *
     * @return String con el nombre del resultado
     */
    @Override
    public String toString() {
        return displayName;
    }

    // ==================== MÉTODOS ESTÁTICOS ====================

    /**
     * Obtiene un AttackResult basado en el estado de una celda y un barco.
     * Método helper para determinar el resultado de un ataque.
     *
     * @param cellState Estado de la celda atacada
     * @param shipSunk Si el barco fue hundido (true) o no (false)
     * @return AttackResult correspondiente
     */
    public static AttackResult fromCellState(CellState cellState, boolean shipSunk) {
        return switch (cellState) {
            case EMPTY -> MISS;
            case SHIP -> shipSunk ? SUNK : HIT;
            case HIT, MISS -> ALREADY_ATTACKED;
        };
    }

    /**
     * Obtiene todos los resultados que son válidos para una jugada.
     * Excluye INVALID_POSITION que es para manejo de errores.
     *
     * @return Array de resultados válidos
     */
    public static AttackResult[] getValidResults() {
        return new AttackResult[]{MISS, HIT, SUNK, ALREADY_ATTACKED};
    }

    /**
     * Obtiene todos los resultados que cuentan como jugadas exitosas.
     *
     * @return Array de resultados exitosos
     */
    public static AttackResult[] getSuccessfulResults() {
        return new AttackResult[]{HIT, SUNK};
    }
}