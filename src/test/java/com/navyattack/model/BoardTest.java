package com.navyattack.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase Board
 */
class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    // ==================== TESTS DE INICIALIZACIÓN ====================

    @Test
    @DisplayName("El tablero debe inicializar con todas las celdas vacías")
    void testBoardInitializesWithEmptyCells() {
        for (int row = 0; row < Board.BOARD_SIZE; row++) {
            for (int col = 0; col < Board.BOARD_SIZE; col++) {
                assertEquals(CellState.EMPTY, board.getCellState(row, col),
                        String.format("Celda (%d, %d) debería estar vacía", row, col));
            }
        }
    }

    @Test
    @DisplayName("El tablero debe inicializar con las cantidades correctas de barcos")
    void testBoardInitializesWithCorrectShipQuantities() {
        assertEquals(1, board.getRemainingShips(ShipType.CARRY));
        assertEquals(2, board.getRemainingShips(ShipType.CRUISER));
        assertEquals(3, board.getRemainingShips(ShipType.DESTROYER));
        assertEquals(4, board.getRemainingShips(ShipType.SUBMARINE));
    }

    @Test
    @DisplayName("El tablero debe iniciar sin barcos colocados")
    void testBoardStartsWithNoShipsPlaced() {
        assertEquals(0, board.getPlacedShipsCount());
        assertFalse(board.areAllShipsPlaced());
    }

    // ==================== TESTS DE VALIDACIÓN DE POSICIONES ====================

    @Test
    @DisplayName("Posiciones válidas dentro de los límites del tablero")
    void testValidPositionsWithinBounds() {
        assertDoesNotThrow(() -> board.getCellState(0, 0));
        assertDoesNotThrow(() -> board.getCellState(9, 9));
        assertDoesNotThrow(() -> board.getCellState(5, 5));
    }

    @Test
    @DisplayName("Posiciones inválidas fuera de los límites del tablero")
    void testInvalidPositionsOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> board.getCellState(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> board.getCellState(0, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> board.getCellState(10, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> board.getCellState(0, 10));
    }

    @Test
    @DisplayName("Se puede colocar un barco en espacio vacío")
    void testCanPlaceShipInEmptySpace() {
        Ship carrier = new Ship(ShipType.CARRY);
        assertTrue(board.canPlaceShip(carrier, 0, 0));
        assertTrue(board.canPlaceShip(carrier, 5, 4));
    }

    @Test
    @DisplayName("No se puede colocar un barco que se sale del tablero horizontalmente")
    void testCannotPlaceShipOutOfBoundsHorizontal() {
        Ship carrier = new Ship(ShipType.CARRY); // Longitud 6
        assertFalse(board.canPlaceShip(carrier, 0, 5),
                "Carrier horizontal en (0,5) se sale del tablero");
        assertFalse(board.canPlaceShip(carrier, 0, 9));
    }

    @Test
    @DisplayName("No se puede colocar un barco que se sale del tablero verticalmente")
    void testCannotPlaceShipOutOfBoundsVertical() {
        Ship carrier = new Ship(ShipType.CARRY);
        carrier.rotate(); // Vertical
        assertFalse(board.canPlaceShip(carrier, 5, 0),
                "Carrier vertical en (5,0) se sale del tablero");
        assertFalse(board.canPlaceShip(carrier, 9, 0));
    }

    @Test
    @DisplayName("No se puede colocar un barco sobre otro barco")
    void testCannotPlaceShipOverAnotherShip() {
        Ship submarine1 = new Ship(ShipType.SUBMARINE);
        board.placeShip(submarine1, 0, 0); // Ocupa (0,0) y (0,1)

        Ship submarine2 = new Ship(ShipType.SUBMARINE);
        assertFalse(board.canPlaceShip(submarine2, 0, 0),
                "No debería poder colocar sobre el mismo inicio");
        assertFalse(board.canPlaceShip(submarine2, 0, 1),
                "No debería poder colocar sobre parte del barco");
    }

    @Test
    @DisplayName("No se puede colocar un barco cuando no hay disponibles")
    void testCannotPlaceShipWhenNoneAvailable() {
        // Colocar el único carrier disponible
        Ship carrier = new Ship(ShipType.CARRY);
        board.placeShip(carrier, 0, 0);
        assertEquals(0, board.getRemainingShips(ShipType.CARRY));

        // Intentar colocar otro carrier
        Ship carrier2 = new Ship(ShipType.CARRY);
        assertFalse(board.canPlaceShip(carrier2, 5, 0));
    }

    // ==================== TESTS DE COLOCACIÓN DE BARCOS ====================

    @Test
    @DisplayName("Colocar un barco horizontal actualiza el grid correctamente")
    void testPlaceShipHorizontallyUpdatesGrid() {
        Ship submarine = new Ship(ShipType.SUBMARINE); // Longitud 2
        board.placeShip(submarine, 0, 0);

        assertEquals(CellState.SHIP, board.getCellState(0, 0));
        assertEquals(CellState.SHIP, board.getCellState(0, 1));
        assertEquals(CellState.EMPTY, board.getCellState(0, 2));
    }

    @Test
    @DisplayName("Colocar un barco vertical actualiza el grid correctamente")
    void testPlaceShipVerticallyUpdatesGrid() {
        Ship submarine = new Ship(ShipType.SUBMARINE);
        submarine.rotate(); // Vertical
        board.placeShip(submarine, 0, 0);

        assertEquals(CellState.SHIP, board.getCellState(0, 0));
        assertEquals(CellState.SHIP, board.getCellState(1, 0));
        assertEquals(CellState.EMPTY, board.getCellState(2, 0));
    }

    @Test
    @DisplayName("Colocar un barco decrementa el contador de disponibles")
    void testPlaceShipDecrementsAvailableCount() {
        assertEquals(4, board.getRemainingShips(ShipType.SUBMARINE));

        Ship submarine = new Ship(ShipType.SUBMARINE);
        board.placeShip(submarine, 0, 0);

        assertEquals(3, board.getRemainingShips(ShipType.SUBMARINE));
    }

    @Test
    @DisplayName("Colocar un barco lo añade a la lista de barcos")
    void testPlaceShipAddsToShipsList() {
        assertEquals(0, board.getPlacedShipsCount());

        Ship submarine = new Ship(ShipType.SUBMARINE);
        board.placeShip(submarine, 0, 0);

        assertEquals(1, board.getPlacedShipsCount());
        assertEquals(submarine, board.getShips().get(0));
    }

    @Test
    @DisplayName("Lanza excepción al colocar en posición inválida")
    void testPlaceShipThrowsExceptionOnInvalidPosition() {
        Ship carrier = new Ship(ShipType.CARRY);

        assertThrows(IllegalStateException.class, () -> {
            board.placeShip(carrier, 0, 9); // Se sale del tablero
        });
    }

    @Test
    @DisplayName("Se pueden colocar varios barcos del mismo tipo")
    void testPlaceMultipleShipsOfSameType() {
        Ship submarine1 = new Ship(ShipType.SUBMARINE);
        Ship submarine2 = new Ship(ShipType.SUBMARINE);

        board.placeShip(submarine1, 0, 0);
        board.placeShip(submarine2, 2, 0);

        assertEquals(2, board.getRemainingShips(ShipType.SUBMARINE));
        assertEquals(2, board.getPlacedShipsCount());
    }

    @Test
    @DisplayName("Verificar que todos los barcos están colocados")
    void testAreAllShipsPlaced() {
        assertFalse(board.areAllShipsPlaced());

        // Colocar todos los barcos
        Ship carrier = new Ship(ShipType.CARRY);
        board.placeShip(carrier, 0, 0);

        Ship cruiser1 = new Ship(ShipType.CRUISER);
        Ship cruiser2 = new Ship(ShipType.CRUISER);
        board.placeShip(cruiser1, 2, 0);
        board.placeShip(cruiser2, 4, 0);

        Ship destroyer1 = new Ship(ShipType.DESTROYER);
        Ship destroyer2 = new Ship(ShipType.DESTROYER);
        Ship destroyer3 = new Ship(ShipType.DESTROYER);
        board.placeShip(destroyer1, 6, 0);
        board.placeShip(destroyer2, 8, 0);
        board.placeShip(destroyer3, 0, 7);

        Ship submarine1 = new Ship(ShipType.SUBMARINE);
        Ship submarine2 = new Ship(ShipType.SUBMARINE);
        Ship submarine3 = new Ship(ShipType.SUBMARINE);
        Ship submarine4 = new Ship(ShipType.SUBMARINE);
        board.placeShip(submarine1, 2, 5);
        board.placeShip(submarine2, 4, 5);
        board.placeShip(submarine3, 6, 5);
        board.placeShip(submarine4, 8, 5);

        assertTrue(board.areAllShipsPlaced());
    }

    // ==================== TESTS DE ATAQUE ====================

    @Test
    @DisplayName("Atacar celda vacía retorna MISS")
    void testAttackEmptyCellReturnsMiss() {
        AttackResult result = board.attack(0, 0);
        assertEquals(AttackResult.MISS, result);
        assertEquals(CellState.MISS, board.getCellState(0, 0));
    }

    @Test
    @DisplayName("Atacar celda con barco retorna HIT")
    void testAttackShipCellReturnsHit() {
        Ship submarine = new Ship(ShipType.SUBMARINE); // 2 hits para hundir
        board.placeShip(submarine, 0, 0);

        AttackResult result = board.attack(0, 0);
        assertEquals(AttackResult.HIT, result);
        assertEquals(CellState.HIT, board.getCellState(0, 0));
    }

    @Test
    @DisplayName("Atacar y hundir barco retorna SUNK")
    void testAttackSinksShipReturnsSunk() {
        Ship submarine = new Ship(ShipType.SUBMARINE); // Longitud 2
        board.placeShip(submarine, 0, 0);

        board.attack(0, 0); // Primer hit
        AttackResult result = board.attack(0, 1); // Segundo hit - debería hundir

        assertEquals(AttackResult.SUNK, result);
        assertTrue(submarine.isSunk());
    }

    @Test
    @DisplayName("Atacar celda ya atacada retorna ALREADY_ATTACKED")
    void testAttackAlreadyAttackedCell() {
        board.attack(0, 0); // Primer ataque
        AttackResult result = board.attack(0, 0); // Segundo ataque

        assertEquals(AttackResult.ALREADY_ATTACKED, result);
    }

    @Test
    @DisplayName("Atacar fuera del tablero retorna INVALID_POSITION")
    void testAttackOutOfBoundsReturnsInvalidPosition() {
        assertEquals(AttackResult.INVALID_POSITION, board.attack(-1, 0));
        assertEquals(AttackResult.INVALID_POSITION, board.attack(0, -1));
        assertEquals(AttackResult.INVALID_POSITION, board.attack(10, 0));
        assertEquals(AttackResult.INVALID_POSITION, board.attack(0, 10));
    }

    @Test
    @DisplayName("Múltiples ataques en el mismo barco")
    void testMultipleAttacksOnSameShip() {
        Ship destroyer = new Ship(ShipType.DESTROYER); // Longitud 3
        board.placeShip(destroyer, 0, 0);

        assertEquals(AttackResult.HIT, board.attack(0, 0));
        assertFalse(destroyer.isSunk());

        assertEquals(AttackResult.HIT, board.attack(0, 1));
        assertFalse(destroyer.isSunk());

        assertEquals(AttackResult.SUNK, board.attack(0, 2));
        assertTrue(destroyer.isSunk());
    }

    // ==================== TESTS DE ESTADO DEL JUEGO ====================

    @Test
    @DisplayName("Verificar si todos los barcos están hundidos - tablero vacío")
    void testAreAllShipsSunkReturnsFalseOnEmptyBoard() {
        assertFalse(board.areAllShipsSunk());
    }

    @Test
    @DisplayName("Verificar si todos los barcos están hundidos - algunos quedan")
    void testAreAllShipsSunkReturnsFalseWhenSomeRemain() {
        Ship submarine1 = new Ship(ShipType.SUBMARINE);
        Ship submarine2 = new Ship(ShipType.SUBMARINE);
        board.placeShip(submarine1, 0, 0);
        board.placeShip(submarine2, 2, 0);

        // Hundir solo el primero
        board.attack(0, 0);
        board.attack(0, 1);

        assertFalse(board.areAllShipsSunk());
    }

    @Test
    @DisplayName("Verificar si todos los barcos están hundidos - todos hundidos")
    void testAreAllShipsSunkReturnsTrueWhenAllSunk() {
        Ship submarine1 = new Ship(ShipType.SUBMARINE);
        Ship submarine2 = new Ship(ShipType.SUBMARINE);
        board.placeShip(submarine1, 0, 0);
        board.placeShip(submarine2, 2, 0);

        // Hundir ambos
        board.attack(0, 0);
        board.attack(0, 1);
        board.attack(2, 0);
        board.attack(2, 1);

        assertTrue(board.areAllShipsSunk());
    }

    @Test
    @DisplayName("Contar celdas con barcos no impactadas")
    void testGetRemainingShipCells() {
        Ship submarine = new Ship(ShipType.SUBMARINE); // 2 celdas
        Ship destroyer = new Ship(ShipType.DESTROYER); // 3 celdas
        board.placeShip(submarine, 0, 0);
        board.placeShip(destroyer, 2, 0);

        assertEquals(5, board.getRemainingShipCells());

        board.attack(0, 0); // Hit en submarine
        assertEquals(4, board.getRemainingShipCells());

        board.attack(2, 0); // Hit en destroyer
        assertEquals(3, board.getRemainingShipCells());
    }

    // ==================== TESTS DE TABLERO ALEATORIO ====================

    @Test
    @DisplayName("Colocación aleatoria llena el tablero correctamente")
    void testPlaceShipsRandomlyFillsBoard() {
        Board.placeShipsRandomly(board);

        assertTrue(board.areAllShipsPlaced());
        assertEquals(10, board.getPlacedShipsCount()); // 1+2+3+4 = 10 barcos
    }

    @Test
    @DisplayName("Colocación aleatoria respeta las cantidades de cada tipo")
    void testPlaceShipsRandomlyRespectsQuantities() {
        Board.placeShipsRandomly(board);

        assertEquals(0, board.getRemainingShips(ShipType.CARRY));
        assertEquals(0, board.getRemainingShips(ShipType.CRUISER));
        assertEquals(0, board.getRemainingShips(ShipType.DESTROYER));
        assertEquals(0, board.getRemainingShips(ShipType.SUBMARINE));
    }

    @Test
    @DisplayName("Colocación aleatoria no genera superposiciones")
    void testPlaceShipsRandomlyNoOverlaps() {
        Board.placeShipsRandomly(board);

        int shipCells = 0;
        for (int row = 0; row < Board.BOARD_SIZE; row++) {
            for (int col = 0; col < Board.BOARD_SIZE; col++) {
                if (board.getCellState(row, col) == CellState.SHIP) {
                    shipCells++;
                }
            }
        }

        // Total de celdas ocupadas: 1*6 + 2*4 + 3*3 + 4*2 = 6+8+9+8 = 31
        int expectedCells = (1 * 6) + (2 * 4) + (3 * 3) + (4 * 2);
        assertEquals(expectedCells, shipCells);
    }

    // ==================== TESTS DE RESET ====================

    @Test
    @DisplayName("Reset restaura el tablero a su estado inicial")
    void testResetRestoresBoardToInitialState() {
        // Colocar barcos y atacar
        Ship submarine = new Ship(ShipType.SUBMARINE);
        board.placeShip(submarine, 0, 0);
        board.attack(0, 0);
        board.attack(5, 5);

        // Reset
        board.reset();

        // Verificar estado inicial
        assertEquals(0, board.getPlacedShipsCount());
        assertTrue(board.getCellState(0, 0)== CellState.EMPTY);
        assertTrue(board.getCellState(5, 5)== CellState.EMPTY);
        assertEquals(1, board.getRemainingShips(ShipType.CARRY));
        assertEquals(4, board.getRemainingShips(ShipType.SUBMARINE));
    }
}