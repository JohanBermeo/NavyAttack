package com.navyattack.model;

import java.util.*;

/**
 * Clase que simula una inteligencia artificial para jugar NavyAttack.
 * Implementa estrategias de ataque inteligente basadas en resultados previos,
 * alternando entre modo búsqueda (HUNT) para encontrar barcos y modo objetivo (TARGET)
 * para hundir barcos una vez detectados.
 * 
 * La IA utiliza patrones de ataque optimizados y aprende de los resultados
 * para mejorar la efectividad de sus ataques subsiguientes.
 * 
 * @author Juan Manuel Otálora Hernández - Johan Stevan Bermeo Buitrago
 * @version 1.0
 */
public class CPU {

    /**
     * Tamaño del tablero de juego (10x10).
     */
    private static final int BOARD_SIZE = 10;
    
    /**
     * Conjunto de posiciones que ya han sido atacadas.
     * Almacena coordenadas en formato String para evitar ataques repetidos.
     */
    private final Set<String> attackedPositions;
    
    /**
     * Cola de posiciones objetivo para atacar en modo TARGET.
     * Contiene posiciones adyacentes a impactos previos.
     */
    private final Queue<int[]> targetQueue;
    
    /**
     * Lista de posiciones donde se ha impactado un barco.
     * Se utiliza para rastrear barcos parcialmente hundidos.
     */
    private final List<int[]> hitPositions;
    
    /**
     * Generador de números aleatorios para ataques en modo HUNT.
     */
    private final Random random;
    
    /**
     * Modo de ataque actual de la CPU.
     */
    private AttackMode currentMode;
    
    /**
     * Dirección actual de ataque cuando se está siguiendo un barco.
     */
    private Direction currentDirection;
    
    /**
     * Posición del primer impacto en un barco.
     * Se utiliza como referencia para determinar la dirección del barco.
     */
    private int[] firstHit;
    
    /**
     * Enumeración que define los modos de ataque de la CPU.
     */
    private enum AttackMode {
        /**
         * Modo búsqueda aleatoria para encontrar barcos.
         */
        HUNT,
        
        /**
         * Modo ataque dirigido para hundir barcos detectados.
         */
        TARGET
    }

    /**
     * Enumeración que define las direcciones de ataque posibles.
     */
    private enum Direction {
        /**
         * Dirección norte (arriba).
         */
        NORTH,
        
        /**
         * Dirección sur (abajo).
         */
        SOUTH,
        
        /**
         * Dirección este (derecha).
         */
        EAST,
        
        /**
         * Dirección oeste (izquierda).
         */
        WEST,
        
        /**
         * Sin dirección definida.
         */
        NONE
    }
    
    /**
     * Constructor de la CPU.
     * Inicializa el estado y estrategia de ataque en modo HUNT.
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
     * Genera la siguiente posición de ataque según el modo actual.
     * En modo TARGET, ataca posiciones adyacentes a impactos previos.
     * En modo HUNT, ataca posiciones aleatorias con patrón de tablero de ajedrez.
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
     * Actualiza el modo de ataque y las posiciones objetivo según el resultado.
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
     * Cambia al modo TARGET y agrega posiciones adyacentes a la cola de objetivos.
     * Si es el segundo impacto, determina la dirección del barco.
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
     * Reinicia el modo TARGET y limpia las posiciones objetivo.
     * Vuelve al modo HUNT para buscar el siguiente barco.
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
     * Si está en modo TARGET con una dirección definida, invierte la dirección
     * para intentar atacar el barco desde el otro extremo.
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
     * Modo de búsqueda: realiza ataques aleatorios con patrón de tablero de ajedrez.
     * Optimiza la búsqueda atacando solo posiciones donde podría haber un barco
     * según el tamaño mínimo de barco (2 celdas).
     * 
     * @return Posición aleatoria [row, col] no atacada previamente
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
     * Determina la dirección del barco basándose en dos impactos consecutivos.
     * Identifica si el barco es horizontal o vertical y establece la dirección de ataque.
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
     * Se utiliza cuando se alcanza el final del barco en una dirección
     * para intentar atacar desde el extremo opuesto.
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
     * Agrega la siguiente posición en la dirección actual a la cola de objetivos.
     * 
     * @param from Posición desde donde calcular la siguiente
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
     * @return Nueva posición o null si está fuera de los límites del tablero
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
     * Agrega todas las posiciones adyacentes (arriba, abajo, izquierda, derecha)
     * a la cola de objetivos. Se usa después del primer impacto en un barco.
     * 
     * @param position Posición central desde donde calcular adyacentes
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
     * Verifica si una posición ya fue atacada anteriormente.
     * 
     * @param position Posición a verificar [row, col]
     * @return true si ya fue atacada, false en caso contrario
     */
    private boolean isAlreadyAttacked(int[] position) {
        return attackedPositions.contains(positionToString(position));
    }
    
    /**
     * Verifica si una posición es válida dentro de los límites del tablero.
     * 
     * @param row Fila a verificar
     * @param col Columna a verificar
     * @return true si la posición está dentro del tablero, false en caso contrario
     */
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE;
    }
    
    /**
     * Convierte una posición a String para almacenamiento en el conjunto.
     * 
     * @param position Posición [row, col]
     * @return String en formato "row,col"
     */
    private String positionToString(int[] position) {
        return position[0] + "," + position[1];
    }
    
    /**
     * Encuentra la primera posición disponible (no atacada) del tablero.
     * Se usa como fallback cuando no quedan posiciones en el patrón optimizado.
     * 
     * @return Primera posición no atacada encontrada
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
     * Limpia todas las posiciones atacadas, la cola de objetivos y
     * restablece el modo de ataque a HUNT.
     */
    public void reset() {
        attackedPositions.clear();
        targetQueue.clear();
        hitPositions.clear();
        firstHit = null;
        currentDirection = Direction.NONE;
        currentMode = AttackMode.HUNT;
    }
    
    /**
     * Obtiene el número total de ataques realizados por la CPU.
     * 
     * @return Cantidad de posiciones atacadas
     */
    public int getAttackCount() {
        return attackedPositions.size();
    }
    
    /**
     * Obtiene el modo de ataque actual de la CPU.
     * 
     * @return String con el nombre del modo actual (HUNT o TARGET)
     */
    public String getCurrentMode() {
        return currentMode.toString();
    }
}