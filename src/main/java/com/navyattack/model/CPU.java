package com.navyattack.model;

import java.util.*;

/**
 * Clase que simula una inteligencia artificial para jugar NavyAttack.
 * Implementa estrategias de ataque inteligente basadas en resultados previos.
 * 
 * @author NavyAttack Team
 * @version 1.0
 */
public class CPU {

    private static final int BOARD_SIZE = 10;
    
    private final Set<String> attackedPositions;
    private final Queue<int[]> targetQueue;
    private final List<int[]> hitPositions;
    private final Random random;
    private AttackMode currentMode;
    private Direction currentDirection;
    private int[] firstHit;
    
    private enum AttackMode {
        HUNT,    // Modo búsqueda aleatoria
        TARGET   // Modo ataque dirigido
    }

    private enum Direction {
        NORTH, SOUTH, EAST, WEST, NONE
    }
    
    /**
     * Constructor de la CPU.
     * Inicializa el estado y estrategia de ataque.
     */
    public CPU() {
        this.attackedPositions = new HashSet<>();
        this.targetQueue = new LinkedList<>();
        this.hitPositions = new ArrayList<>();
        this.random = new Random();
        this.currentMode = AttackMode.HUNT;
        this.currentDirection = Direction.NONE;
        this.firstHit = null;
    }
    
    /**
     * Genera la siguiente posición de ataque.
     * 
     * @return Arreglo [row, col] con la posición a atacar
     */
    public int[] attack() {
        int[] position;
        
        if (currentMode == AttackMode.TARGET && !targetQueue.isEmpty()) {
            position = targetQueue.poll();
            
            while (position != null && isAlreadyAttacked(position)) {
                position = targetQueue.isEmpty() ? null : targetQueue.poll();
            }
            
            if (position == null) {
                currentMode = AttackMode.HUNT;
                position = huntMode();
            }
        } else {
            currentMode = AttackMode.HUNT;
            position = huntMode();
        }
        
        attackedPositions.add(positionToString(position));
        return position;
    }
    
    /**
     * Procesa el resultado del último ataque para ajustar la estrategia.
     * 
     * @param result Resultado del ataque realizado
     * @param position Posición que fue atacada [row, col]
     */
    public void processResult(AttackResult result, int[] position) {
        if (result == AttackResult.HIT) {
            handleHit(position);
        } else if (result == AttackResult.SUNK) {
            handleSunk(position);
        } else if (result == AttackResult.MISS) {
            handleMiss(position);
        }
    }
    
    /**
     * Maneja un resultado de impacto (HIT).
     * 
     * @param position Posición donde se impactó
     */
    private void handleHit(int[] position) {
        currentMode = AttackMode.TARGET;
        hitPositions.add(position);
        
        if (firstHit == null) {
            firstHit = position;
            addAdjacentPositions(position);
        } else {
            if (currentDirection == Direction.NONE) {
                determineDirection(firstHit, position);
            }
            addPositionInDirection(position);
        }
    }
    
    /**
     * Maneja un resultado de barco hundido (SUNK).
     * 
     * @param position Posición del último impacto
     */
    private void handleSunk(int[] position) {
        hitPositions.clear();
        targetQueue.clear();
        firstHit = null;
        currentDirection = Direction.NONE;
        currentMode = AttackMode.HUNT;
    }
    
    /**
     * Maneja un resultado de fallo (MISS).
     * 
     * @param position Posición donde se falló
     */
    private void handleMiss(int[] position) {
        if (currentMode == AttackMode.TARGET && currentDirection != Direction.NONE) {
            reverseDirection();
            if (firstHit != null) {
                addPositionInDirection(firstHit);
            }
        }
    }
    
    
    /**
     * Modo de búsqueda: ataque aleatorio con patrón de tablero de ajedrez.
     * 
     * @return Posición aleatoria [row, col]
     */
    private int[] huntMode() {
        int[] position = null;
        int attempts = 0;
        int maxAttempts = BOARD_SIZE * BOARD_SIZE;
        
        do {
            int row = random.nextInt(BOARD_SIZE);
            int col = random.nextInt(BOARD_SIZE);
            
            if ((row + col) % 2 == 0) {
                position = new int[]{row, col};
            } else {
                continue;
            }
            
            attempts++;
            if (attempts > maxAttempts) {
                position = findFirstAvailablePosition();
                break;
            }
        } while (position == null || isAlreadyAttacked(position));
        
        return position;
    }
    
    /**
     * Determina la dirección del barco basándose en dos impactos.
     * 
     * @param first Primera posición de impacto
     * @param second Segunda posición de impacto
     */
    private void determineDirection(int[] first, int[] second) {
        if (first[0] == second[0]) {
            currentDirection = second[1] > first[1] ? Direction.EAST : Direction.WEST;
        } else if (first[1] == second[1]) {
            currentDirection = second[0] > first[0] ? Direction.SOUTH : Direction.NORTH;
        }
        
        targetQueue.clear();
        addPositionInDirection(second);
    }
    
    /**
     * Invierte la dirección actual de ataque.
     */
    private void reverseDirection() {
        currentDirection = switch (currentDirection) {
            case NORTH -> Direction.SOUTH;
            case SOUTH -> Direction.NORTH;
            case EAST -> Direction.WEST;
            case WEST -> Direction.EAST;
            case NONE -> Direction.NONE;
        };
        targetQueue.clear();
    }
    
    /**
     * Agrega la siguiente posición en la dirección actual.
     * 
     * @param from Posición desde donde calcular
     */
    private void addPositionInDirection(int[] from) {
        int[] next = getNextPositionInDirection(from, currentDirection);
        if (next != null && !isAlreadyAttacked(next)) {
            targetQueue.offer(next);
        }
    }
    
    /**
     * Obtiene la siguiente posición en una dirección específica.
     * 
     * @param position Posición actual
     * @param direction Dirección a seguir
     * @return Nueva posición o null si está fuera del tablero
     */
    private int[] getNextPositionInDirection(int[] position, Direction direction) {
        int row = position[0];
        int col = position[1];
        
        return switch (direction) {
            case NORTH -> isValidPosition(row - 1, col) ? new int[]{row - 1, col} : null;
            case SOUTH -> isValidPosition(row + 1, col) ? new int[]{row + 1, col} : null;
            case EAST -> isValidPosition(row, col + 1) ? new int[]{row, col + 1} : null;
            case WEST -> isValidPosition(row, col - 1) ? new int[]{row, col - 1} : null;
            case NONE -> null;
        };
    }
    
    /**
     * Agrega todas las posiciones adyacentes a la cola de objetivos.
     * 
     * @param position Posición central
     */
    private void addAdjacentPositions(int[] position) {
        int row = position[0];
        int col = position[1];
        
        int[][] adjacents = {
            {row - 1, col},  // Norte
            {row + 1, col},  // Sur
            {row, col + 1},  // Este
            {row, col - 1}   // Oeste
        };
        
        for (int[] adj : adjacents) {
            if (isValidPosition(adj[0], adj[1]) && !isAlreadyAttacked(adj)) {
                targetQueue.offer(adj);
            }
        }
    }
    
    /**
     * Verifica si una posición ya fue atacada.
     * 
     * @param position Posición a verificar
     * @return true si ya fue atacada, false en caso contrario
     */
    private boolean isAlreadyAttacked(int[] position) {
        return attackedPositions.contains(positionToString(position));
    }
    
    /**
     * Verifica si una posición es válida dentro del tablero.
     * 
     * @param row Fila
     * @param col Columna
     * @return true si es válida, false en caso contrario
     */
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE;
    }
    
    /**
     * Convierte una posición a String para almacenamiento.
     * 
     * @param position Posición [row, col]
     * @return String en formato "row,col"
     */
    private String positionToString(int[] position) {
        return position[0] + "," + position[1];
    }
    
    /**
     * Encuentra la primera posición disponible del tablero.
     * 
     * @return Primera posición no atacada
     */
    private int[] findFirstAvailablePosition() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                int[] pos = {row, col};
                if (!isAlreadyAttacked(pos)) {
                    return pos;
                }
            }
        }
        return new int[]{0, 0};
    }
    
    /**
     * Reinicia el estado de la CPU para una nueva partida.
     */
    public void reset() {
        attackedPositions.clear();
        targetQueue.clear();
        hitPositions.clear();
        firstHit = null;
        currentDirection = Direction.NONE;
        currentMode = AttackMode.HUNT;
    }
    
    public int getAttackCount() {
        return attackedPositions.size();
    }
    
    public String getCurrentMode() {
        return currentMode.toString();
    }
}