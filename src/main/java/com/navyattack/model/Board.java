package com.navyattack.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Representa el tablero de juego de Batalla Naval en NavyAttack.
 * Esta clase es parte del MODELO DE DOMINIO y contiene:
 * - Estado del grid (celdas vacías, con barcos, impactadas, etc.)
 * - Colección de barcos colocados
 * - Lógica de validación y colocación de barcos
 * - Lógica de ataque y detección de impactos
 * 
 * El tablero tiene dimensiones estándar de 10x10 celdas y gestiona
 * todos los aspectos del juego relacionados con la colocación de barcos
 * y los ataques realizados.
 *
 * @author Juan Manuel Otálora Hernández - Johan Stevan Bermeo Buitrago
 * @version 1.0
 */
public class Board {

    /**
     * Tamaño estándar del tablero (10x10 celdas).
     */
    public static final int BOARD_SIZE = 10;

    /**
     * Grid bidimensional que representa el estado de cada celda del tablero.
     * Cada celda puede estar vacía, ocupada por un barco, impactada o fallada.
     */
    private final CellState[][] grid;

    /**
     * Lista de barcos que han sido colocados en el tablero.
     */
    private final List<Ship> ships;

    /**
     * Mapa que mantiene la cantidad disponible de cada tipo de barco.
     * La clave es el tipo de barco y el valor es la cantidad disponible para colocar.
     */
    private final Map<ShipType, Integer> availableShips;

    /**
     * Constructor que inicializa un tablero vacío.
     * Configura el grid en estado EMPTY y establece las cantidades
     * iniciales de cada tipo de barco según las reglas del juego.
     */
    public Board() {
        this.grid = new CellState[BOARD_SIZE][BOARD_SIZE];
        this.ships = new ArrayList<>();
        this.availableShips = initializeAvailableShips();
        initializeGrid();
    }

    /**
     * Inicializa el grid con todas las celdas en estado EMPTY.
     */
    private void initializeGrid() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                grid[row][col] = CellState.EMPTY;
            }
        }
    }

    /**
     * Inicializa las cantidades disponibles de cada tipo de barco.
     * Según reglas estándar de Batalla Naval:
     * - 1 Portaaviones (Carrier)
     * - 2 Cruceros (Cruiser)
     * - 3 Destructores (Destroyer)
     * - 4 Submarinos (Submarine)
     *
     * @return Map con las cantidades iniciales de cada tipo de barco
     */
    private Map<ShipType, Integer> initializeAvailableShips() {
        Map<ShipType, Integer> ships = new HashMap<>();
        ships.put(ShipType.CARRY, 1);
        ships.put(ShipType.CRUISER, 2);
        ships.put(ShipType.DESTROYER, 3);
        ships.put(ShipType.SUBMARINE, 4);
        return ships;
    }

    /**
     * Verifica si una posición está dentro de los límites del tablero.
     *
     * @param row Fila a verificar
     * @param col Columna a verificar
     * @return true si la posición es válida (dentro del tablero), false en caso contrario
     */
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE;
    }

    /**
     * Verifica si hay barcos disponibles de un tipo específico para colocar.
     *
     * @param type Tipo de barco a verificar
     * @return true si hay al menos un barco disponible de ese tipo, false en caso contrario
     */
    private boolean isShipTypeAvailable(ShipType type) {
        return availableShips.getOrDefault(type, 0) > 0;
    }

    /**
     * Verifica si un barco puede ser colocado en una posición específica.
     * Valida:
     * - Que haya barcos disponibles de ese tipo
     * - Que todas las posiciones estén dentro del tablero
     * - Que todas las posiciones estén vacías (sin otros barcos)
     *
     * @param ship Barco a colocar
     * @param row Fila inicial donde se colocará el barco
     * @param col Columna inicial donde se colocará el barco
     * @return true si el barco puede colocarse en esa posición, false en caso contrario
     */
    public boolean canPlaceShip(Ship ship, int row, int col) {
        // Validar que haya barcos disponibles de este tipo
        if (!isShipTypeAvailable(ship.getType())) {
            return false;
        }

        // Calcular las posiciones que ocuparía el barco
        List<int[]> positions = ship.calculatePositions(row, col);

        // Verificar que todas las posiciones sean válidas y estén vacías
        for (int[] pos : positions) {
            int r = pos[0];
            int c = pos[1];

            // Verificar límites del tablero
            if (!isValidPosition(r, c)) {
                return false;
            }

            // Verificar que la celda esté vacía
            if (grid[r][c] != CellState.EMPTY) {
                return false;
            }
        }

        return true;
    }

    /**
     * Coloca un barco en el tablero en la posición especificada.
     * Actualiza el grid, registra el barco y decrementa la cantidad disponible.
     *
     * @param ship Barco a colocar
     * @param row Fila inicial donde se colocará el barco
     * @param col Columna inicial donde se colocará el barco
     * @throws IllegalStateException si el barco no puede colocarse en esa posición
     */
    public void placeShip(Ship ship, int row, int col) {
        // Validar que se puede colocar
        if (!canPlaceShip(ship, row, col)) {
            throw new IllegalStateException(
                    String.format("Cannot place %s at position (%d, %d)",
                            ship.getType(), row, col)
            );
        }

        // Colocar el barco (esto actualiza su posición interna)
        ship.place(row, col);

        // Obtener las posiciones que ocupa el barco
        List<int[]> positions = ship.getPositions();

        // Marcar las celdas en el grid como ocupadas
        for (int[] pos : positions) {
            grid[pos[0]][pos[1]] = CellState.SHIP;
        }

        // Agregar el barco a la lista de barcos colocados
        ships.add(ship);

        // Decrementar la cantidad disponible de este tipo de barco
        decrementAvailableShip(ship.getType());
    }

    /**
     * Decrementa la cantidad disponible de un tipo de barco.
     * Se llama después de colocar exitosamente un barco en el tablero.
     *
     * @param type Tipo de barco a decrementar
     */
    private void decrementAvailableShip(ShipType type) {
        int current = availableShips.getOrDefault(type, 0);
        if (current > 0) {
            availableShips.put(type, current - 1);
        }
    }

    /**
     * Procesa un ataque en una posición específica del tablero.
     * Determina si el ataque impacta un barco, falla o es inválido.
     * Actualiza el estado del grid y registra impactos en los barcos.
     *
     * @param row Fila atacada
     * @param col Columna atacada
     * @return AttackResult indicando el resultado del ataque (HIT, MISS, SUNK, etc.)
     */
    public AttackResult attack(int row, int col) {
        // Validar que la posición sea válida
        if (!isValidPosition(row, col)) {
            return AttackResult.INVALID_POSITION;
        }

        CellState currentState = grid[row][col];

        // Verificar si ya fue atacada
        if (currentState == CellState.HIT || currentState == CellState.MISS) {
            return AttackResult.ALREADY_ATTACKED;
        }

        // Procesar el ataque
        if (currentState == CellState.SHIP) {
            // Impacto en un barco
            grid[row][col] = CellState.HIT;

            // Encontrar el barco impactado y registrar el golpe
            Ship hitShip = findShipAt(row, col);
            if (hitShip != null) {
                hitShip.registerHit();

                // Verificar si el barco fue hundido
                if (hitShip.isSunk()) {
                    return AttackResult.SUNK;
                }
            }

            return AttackResult.HIT;
        }

        // Fallo - agua
        grid[row][col] = CellState.MISS;
        return AttackResult.MISS;
    }

    /**
     * Encuentra el barco que ocupa una posición específica del tablero.
     *
     * @param row Fila a buscar
     * @param col Columna a buscar
     * @return Ship si hay un barco en esa posición, null en caso contrario
     */
    private Ship findShipAt(int row, int col) {
        for (Ship ship : ships) {
            for (int[] pos : ship.getPositions()) {
                if (pos[0] == row && pos[1] == col) {
                    return ship;
                }
            }
        }
        return null;
    }

    /**
     * Verifica si todos los barcos en el tablero han sido hundidos.
     * Condición de victoria para el oponente.
     *
     * @return true si todos los barcos están hundidos, false en caso contrario
     */
    public boolean areAllShipsSunk() {
        if (ships.isEmpty()) {
            return false;
        }

        for (Ship ship : ships) {
            if (!ship.isSunk()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Obtiene la cantidad de barcos restantes disponibles para colocar de un tipo específico.
     *
     * @param type Tipo de barco a consultar
     * @return Cantidad de barcos disponibles para colocar
     */
    public int getRemainingShips(ShipType type) {
        return availableShips.getOrDefault(type, 0);
    }

    /**
     * Obtiene el estado de una celda específica del tablero.
     *
     * @param row Fila de la celda
     * @param col Columna de la celda
     * @return CellState de la celda especificada
     * @throws IndexOutOfBoundsException si la posición está fuera de los límites del tablero
     */
    public CellState getCellState(int row, int col) {
        if (!isValidPosition(row, col)) {
            throw new IndexOutOfBoundsException(
                    String.format("Position (%d, %d) is out of bounds", row, col)
            );
        }
        return grid[row][col];
    }

    /**
     * Obtiene una lista inmutable de los barcos colocados en el tablero.
     *
     * @return Lista de barcos (no modificable)
     */
    public List<Ship> getShips() {
        return Collections.unmodifiableList(ships);
    }

    /**
     * Obtiene la cantidad total de barcos colocados en el tablero.
     *
     * @return Número de barcos colocados
     */
    public int getPlacedShipsCount() {
        return ships.size();
    }

    /**
     * Verifica si se han colocado todos los barcos permitidos.
     *
     * @return true si todos los barcos están colocados, false en caso contrario
     */
    public boolean areAllShipsPlaced() {
        for (int count : availableShips.values()) {
            if (count > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Obtiene la cantidad total de celdas con barcos que aún no han sido impactadas.
     * Útil para calcular el progreso del juego.
     *
     * @return Número de celdas con barcos intactas
     */
    public int getRemainingShipCells() {
        int count = 0;
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (grid[row][col] == CellState.SHIP) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Genera una representación en String del tablero.
     * Útil para debugging y testing.
     *
     * @return String representando el estado completo del tablero
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Board State:\n");
        sb.append("  ");

        // Encabezado de columnas
        for (int col = 0; col < BOARD_SIZE; col++) {
            sb.append(col).append(" ");
        }
        sb.append("\n");

        // Filas del tablero
        for (int row = 0; row < BOARD_SIZE; row++) {
            sb.append(row).append(" ");
            for (int col = 0; col < BOARD_SIZE; col++) {
                char symbol = switch (grid[row][col]) {
                    case EMPTY -> '·';
                    case SHIP -> 'S';
                    case HIT -> 'X';
                    case MISS -> 'O';
                };
                sb.append(symbol).append(" ");
            }
            sb.append("\n");
        }

        // Información de barcos
        sb.append("\nShips placed: ").append(ships.size()).append("\n");
        sb.append("Available ships:\n");
        for (ShipType type : ShipType.values()) {
            sb.append("  ").append(type).append(": ")
                    .append(getRemainingShips(type)).append("\n");
        }

        return sb.toString();
    }

    /**
     * Reinicia el tablero a su estado inicial.
     * Limpia el grid, elimina todos los barcos y restaura las cantidades disponibles.
     * Útil para tests o reiniciar el juego.
     */
    public void reset() {
        // Limpiar grid
        initializeGrid();

        // Limpiar barcos
        ships.clear();

        // Restaurar cantidades disponibles
        availableShips.clear();
        availableShips.putAll(initializeAvailableShips());
    }

    /**
     * Coloca barcos aleatoriamente en el tablero.
     * Intenta colocar todos los barcos de todos los tipos en posiciones válidas aleatorias.
     * Utiliza hasta 100 intentos por barco para encontrar una posición válida.
     *
     * @param board Tablero donde se colocarán los barcos aleatoriamente
     */
    public static void placeShipsRandomly(Board board) {
        java.util.Random random = new java.util.Random();

        for (ShipType type : ShipType.values()) {
            int quantity = board.getRemainingShips(type);

            for (int i = 0; i < quantity; i++) {
                boolean placed = false;
                int attempts = 0;

                while (!placed && attempts < 100) {
                    int row = random.nextInt(Board.BOARD_SIZE);
                    int col = random.nextInt(Board.BOARD_SIZE);

                    Ship ship = new Ship(type);

                    if (random.nextBoolean()) {
                        ship.rotate();
                    }

                    if (board.canPlaceShip(ship, row, col)) {
                        board.placeShip(ship, row, col);
                        placed = true;

                        if (attempts > 0) {
                            System.out.println("  " + type + " colocado en intento " + (attempts + 1));
                        }
                    }

                    attempts++;
                }

                if (!placed) {
                    System.err.println("WARNING: No se pudo colocar " + type + " después de 100 intentos");
                    System.err.println("Estado del tablero:");
                    System.err.println(board.toString());
                }
            }
        }
    }
}