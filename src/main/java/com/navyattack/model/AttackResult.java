package com.navyattack.model;

/**
 * Enumeración que representa los posibles resultados de un ataque en el juego NavyAttack.
 * Define todos los estados posibles después de realizar un disparo en el tablero enemigo,
 * incluyendo información visual, descripción y comportamiento asociado a cada resultado.
 * 
 * @author Juan Manuel Otálora Hernández - Johan Stevan Bermeo Buitrago
 * @version 1.0
 */
public enum AttackResult {

    /**
     * El ataque impactó una celda vacía (agua).
     * El jugador falló el disparo y no dañó ningún barco.
     */
    MISS("Miss", "The attack hit water", "○"),

    /**
     * El ataque impactó parte de un barco.
     * El barco fue dañado pero NO está completamente hundido.
     */
    HIT("Hit", "The attack hit a ship", "X"),

    /**
     * El ataque impactó y hundió completamente un barco.
     * Todas las partes del barco han sido golpeadas exitosamente.
     */
    SUNK("Sunk", "The ship has been sunk", "💥"),

    /**
     * Se intentó atacar una celda que ya fue atacada previamente.
     * No es un movimiento válido y no cuenta como turno.
     */
    ALREADY_ATTACKED("Already Attacked", "This position was already attacked", "⚠"),

    /**
     * Posición inválida (fuera de los límites del tablero).
     * No debería ocurrir con validación adecuada en la interfaz.
     */
    INVALID_POSITION("Invalid Position", "Position is out of bounds", "✗");

    /**
     * Nombre corto del resultado para logs y visualización simple en UI.
     */
    private final String displayName;

    /**
     * Descripción detallada del resultado del ataque.
     */
    private final String description;

    /**
     * Símbolo visual para representar el resultado en la interfaz de usuario.
     */
    private final String symbol;

    /**
     * Constructor privado del enum.
     * Inicializa los atributos de cada resultado de ataque.
     *
     * @param displayName Nombre para mostrar en la UI
     * @param description Descripción detallada del resultado
     * @param symbol Símbolo visual del resultado
     */
    AttackResult(String displayName, String description, String symbol) {
        this.displayName = displayName;
        this.description = description;
        this.symbol = symbol;
    }

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
     * @return Descripción completa del resultado del ataque
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

    /**
     * Verifica si el resultado es un impacto exitoso.
     * Considera exitosos tanto HIT como SUNK.
     *
     * @return true si el ataque fue exitoso (HIT o SUNK), false en caso contrario
     */
    public boolean isSuccessfulHit() {
        return this == HIT || this == SUNK;
    }

    /**
     * Verifica si el resultado hundió completamente un barco.
     *
     * @return true si un barco fue hundido, false en caso contrario
     */
    public boolean isShipSunk() {
        return this == SUNK;
    }

    /**
     * Verifica si el resultado representa un movimiento inválido.
     * Incluye intentos de atacar celdas ya atacadas o posiciones fuera del tablero.
     *
     * @return true si el movimiento no es válido, false en caso contrario
     */
    public boolean isInvalidMove() {
        return this == ALREADY_ATTACKED || this == INVALID_POSITION;
    }

    /**
     * Verifica si el resultado requiere cambio de turno.
     * Solo ALREADY_ATTACKED e INVALID_POSITION no provocan cambio de turno.
     *
     * @return true si debe cambiar el turno, false en caso contrario
     */
    public boolean shouldChangeTurn() {
        return !isInvalidMove();
    }

    /**
     * Obtiene el código de color asociado al resultado para la UI.
     * Útil para colorear celdas del tablero según el resultado del ataque.
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
     * Obtiene puntos asociados al resultado para sistema de puntuación.
     * MISS no otorga puntos, HIT otorga puntos moderados, SUNK otorga máximos puntos.
     * ALREADY_ATTACKED aplica una penalización.
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
     * Verifica si el resultado debe mostrarse con efecto especial en la UI.
     * Solo SUNK tiene efectos especiales (animaciones, sonidos, etc.).
     *
     * @return true si debe tener animación o efecto especial
     */
    public boolean hasSpecialEffect() {
        return this == SUNK;
    }

    /**
     * Obtiene un mensaje personalizado para mostrar al jugador.
     * Incluye el nombre del barco si está disponible para mayor contexto.
     *
     * @param shipName Nombre del barco afectado (puede ser null)
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
     * @return String con el nombre para mostrar del resultado
     */
    @Override
    public String toString() {
        return displayName;
    }

    /**
     * Obtiene un AttackResult basado en el estado de una celda y si el barco fue hundido.
     * Método auxiliar para determinar el resultado correcto de un ataque.
     *
     * @param cellState Estado de la celda atacada
     * @param shipSunk Indica si el barco fue completamente hundido
     * @return AttackResult correspondiente al ataque
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
     * Excluye INVALID_POSITION que es solo para manejo de errores.
     *
     * @return Array de resultados válidos
     */
    public static AttackResult[] getValidResults() {
        return new AttackResult[]{MISS, HIT, SUNK, ALREADY_ATTACKED};
    }

    /**
     * Obtiene todos los resultados que cuentan como jugadas exitosas.
     * Solo incluye HIT y SUNK.
     *
     * @return Array de resultados exitosos
     */
    public static AttackResult[] getSuccessfulResults() {
        return new AttackResult[]{HIT, SUNK};
    }
}