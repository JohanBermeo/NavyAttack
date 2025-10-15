package com.navyattack.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Tests unitarios para la clase Ship
 */
class ShipTest {

    private Ship carrier;
    private Ship cruiser;
    private Ship destroyer;
    private Ship submarine;

    @BeforeEach
    void setUp() {
        carrier = new Ship(ShipType.CARRY);
        cruiser = new Ship(ShipType.CRUISER);
        destroyer = new Ship(ShipType.DESTROYER);
        submarine = new Ship(ShipType.SUBMARINE);
    }

    // ==================== TESTS DE CREACIÓN Y ESTADO ====================

    @Test
    @DisplayName("Crear barco con tipo correcto")
    void testShipCreationWithType() {
        assertEquals(ShipType.CARRY, carrier.getType());
        assertEquals(ShipType.CRUISER, cruiser.getType());
        assertEquals(ShipType.DESTROYER, destroyer.getType());
        assertEquals(ShipType.SUBMARINE, submarine.getType());
    }

    @Test
    @DisplayName("Barco tiene la longitud correcta según su tipo")
    void testShipHasCorrectLength() {
        assertEquals(6, carrier.getLength());
        assertEquals(4, cruiser.getLength());
        assertEquals(3, destroyer.getLength());
        assertEquals(2, submarine.getLength());
    }

    @ParameterizedTest
    @EnumSource(ShipType.class)
    @DisplayName("Orientación inicial es HORIZONTAL para todos los tipos")
    void testShipInitialOrientation(ShipType type) {
        Ship ship = new Ship(type);
        assertEquals(Orientation.HORIZONTAL, ship.getOrientation());
    }

    @ParameterizedTest
    @EnumSource(ShipType.class)
    @DisplayName("isPlaced() retorna false inicialmente para todos los tipos")
    void testShipNotPlacedInitially(ShipType type) {
        Ship ship = new Ship(type);
        assertFalse(ship.isPlaced());
    }

    @ParameterizedTest
    @EnumSource(ShipType.class)
    @DisplayName("isSunk() retorna false inicialmente para todos los tipos")
    void testShipNotSunkInitially(ShipType type) {
        Ship ship = new Ship(type);
        assertFalse(ship.isSunk());
    }

    // ==================== TESTS DE POSICIONAMIENTO ====================

    @Test
    @DisplayName("Calcular posiciones horizontales correctas")
    void testCalculatePositionsHorizontal() {
        List<int[]> positions = submarine.calculatePositions(0, 0);

        assertEquals(2, positions.size());
        assertArrayEquals(new int[]{0, 0}, positions.get(0));
        assertArrayEquals(new int[]{0, 1}, positions.get(1));
    }

    @Test
    @DisplayName("Calcular posiciones horizontales del carrier")
    void testCalculatePositionsHorizontalCarrier() {
        List<int[]> positions = carrier.calculatePositions(5, 2);

        assertEquals(6, positions.size());
        assertArrayEquals(new int[]{5, 2}, positions.get(0));
        assertArrayEquals(new int[]{5, 3}, positions.get(1));
        assertArrayEquals(new int[]{5, 4}, positions.get(2));
        assertArrayEquals(new int[]{5, 5}, positions.get(3));
        assertArrayEquals(new int[]{5, 6}, positions.get(4));
        assertArrayEquals(new int[]{5, 7}, positions.get(5));
    }

    @Test
    @DisplayName("Calcular posiciones verticales correctas")
    void testCalculatePositionsVertical() {
        submarine.rotate(); // Cambiar a vertical
        List<int[]> positions = submarine.calculatePositions(0, 0);

        assertEquals(2, positions.size());
        assertArrayEquals(new int[]{0, 0}, positions.get(0));
        assertArrayEquals(new int[]{1, 0}, positions.get(1));
    }

    @Test
    @DisplayName("Calcular posiciones verticales del destroyer")
    void testCalculatePositionsVerticalDestroyer() {
        destroyer.rotate();
        List<int[]> positions = destroyer.calculatePositions(3, 4);

        assertEquals(3, positions.size());
        assertArrayEquals(new int[]{3, 4}, positions.get(0));
        assertArrayEquals(new int[]{4, 4}, positions.get(1));
        assertArrayEquals(new int[]{5, 4}, positions.get(2));
    }

    @Test
    @DisplayName("place() actualiza row y col correctamente")
    void testPlaceShipUpdatesRowAndCol() {
        submarine.place(3, 5);
        assertTrue(submarine.isPlaced());
    }

    @Test
    @DisplayName("place() genera lista de posiciones")
    void testPlaceShipGeneratesPositions() {
        submarine.place(0, 0);

        List<int[]> positions = submarine.getPositions();
        assertFalse(positions.isEmpty());
        assertEquals(2, positions.size());
    }

    @Test
    @DisplayName("getPositions() retorna lista vacía antes de colocar")
    void testGetPositionsBeforePlaced() {
        List<int[]> positions = submarine.getPositions();
        assertTrue(positions.isEmpty());
    }

    @Test
    @DisplayName("getPositions() retorna lista inmutable después de colocar")
    void testGetPositionsReturnsUnmodifiableList() {
        submarine.place(0, 0);
        List<int[]> positions = submarine.getPositions();

        assertThrows(UnsupportedOperationException.class, () -> {
            positions.add(new int[]{5, 5});
        });
    }

    // ==================== TESTS DE ROTACIÓN ====================

    @Test
    @DisplayName("Rotar de horizontal a vertical")
    void testRotateFromHorizontalToVertical() {
        assertEquals(Orientation.HORIZONTAL, submarine.getOrientation());

        submarine.rotate();
        assertEquals(Orientation.VERTICAL, submarine.getOrientation());
    }

    @Test
    @DisplayName("Rotar de vertical a horizontal")
    void testRotateFromVerticalToHorizontal() {
        submarine.rotate(); // Ahora es vertical
        assertEquals(Orientation.VERTICAL, submarine.getOrientation());

        submarine.rotate(); // Volver a horizontal
        assertEquals(Orientation.HORIZONTAL, submarine.getOrientation());
    }

    @Test
    @DisplayName("Rotar varias veces alterna correctamente")
    void testRotateMultipleTimes() {
        assertEquals(Orientation.HORIZONTAL, submarine.getOrientation());

        submarine.rotate();
        assertEquals(Orientation.VERTICAL, submarine.getOrientation());

        submarine.rotate();
        assertEquals(Orientation.HORIZONTAL, submarine.getOrientation());

        submarine.rotate();
        assertEquals(Orientation.VERTICAL, submarine.getOrientation());
    }

    @Test
    @DisplayName("Rotación después de colocar actualiza posiciones")
    void testRotateAfterPlaceUpdatesPositions() {
        submarine.place(0, 0);
        List<int[]> horizontalPos = submarine.getPositions();

        submarine.rotate();
        submarine.place(0, 0); // Re-colocar
        List<int[]> verticalPos = submarine.getPositions();

        // Posiciones horizontales: (0,0), (0,1)
        assertArrayEquals(new int[]{0, 0}, horizontalPos.get(0));
        assertArrayEquals(new int[]{0, 1}, horizontalPos.get(1));

        // Posiciones verticales: (0,0), (1,0)
        assertArrayEquals(new int[]{0, 0}, verticalPos.get(0));
        assertArrayEquals(new int[]{1, 0}, verticalPos.get(1));
    }

    // ==================== TESTS DE DAÑO ====================

    @Test
    @DisplayName("registerHit() incrementa contador de hits")
    void testRegisterHitIncreasesHits() {
        assertFalse(submarine.isSunk());

        submarine.registerHit();
        assertFalse(submarine.isSunk()); // Aún no hundido (necesita 2 hits)

        submarine.registerHit();
        assertTrue(submarine.isSunk()); // Ahora sí hundido
    }

    @Test
    @DisplayName("isSunk() retorna true cuando hits == length")
    void testShipSunkAfterAllHits() {
        submarine.place(0, 0);

        for (int i = 0; i < submarine.getLength(); i++) {
            assertFalse(submarine.isSunk());
            submarine.registerHit();
        }

        assertTrue(submarine.isSunk());
    }

    @Test
    @DisplayName("isSunk() retorna false con daño parcial")
    void testShipNotSunkWithPartialHits() {
        carrier.place(0, 0); // Longitud 6

        carrier.registerHit();
        carrier.registerHit();
        carrier.registerHit();

        assertFalse(carrier.isSunk(), "Carrier con 3 hits de 6 no debería estar hundido");
    }

    @Test
    @DisplayName("Probar daño completo en diferentes tipos de barcos")
    void testMultipleHitsOnDifferentShipTypes() {
        // Submarine (2 hits)
        submarine.registerHit();
        submarine.registerHit();
        assertTrue(submarine.isSunk());

        // Destroyer (3 hits)
        destroyer.registerHit();
        destroyer.registerHit();
        assertFalse(destroyer.isSunk());
        destroyer.registerHit();
        assertTrue(destroyer.isSunk());

        // Cruiser (4 hits)
        for (int i = 0; i < 4; i++) {
            cruiser.registerHit();
        }
        assertTrue(cruiser.isSunk());

        // Carrier (6 hits)
        for (int i = 0; i < 6; i++) {
            carrier.registerHit();
        }
        assertTrue(carrier.isSunk());
    }

    @Test
    @DisplayName("Registrar más hits de los necesarios no causa problemas")
    void testRegisterExcessHits() {
        submarine.registerHit();
        submarine.registerHit();
        assertTrue(submarine.isSunk());

        // Golpes adicionales no deberían causar problemas
        submarine.registerHit();
        submarine.registerHit();
        assertTrue(submarine.isSunk());
    }

    // ==================== TESTS DE EDGE CASES ====================

    @Test
    @DisplayName("Calcular posiciones en esquina superior izquierda")
    void testCalculatePositionsTopLeftCorner() {
        List<int[]> positions = submarine.calculatePositions(0, 0);
        assertEquals(2, positions.size());
        assertArrayEquals(new int[]{0, 0}, positions.get(0));
        assertArrayEquals(new int[]{0, 1}, positions.get(1));
    }

    @Test
    @DisplayName("Calcular posiciones cerca del borde derecho")
    void testCalculatePositionsNearRightEdge() {
        List<int[]> positions = submarine.calculatePositions(5, 8);
        assertEquals(2, positions.size());
        assertArrayEquals(new int[]{5, 8}, positions.get(0));
        assertArrayEquals(new int[]{5, 9}, positions.get(1));
    }

    @Test
    @DisplayName("Verificar tipo de barco después de operaciones")
    void testShipTypeRemainsConstant() {
        submarine.rotate();
        submarine.place(5, 5);
        submarine.registerHit();

        assertEquals(ShipType.SUBMARINE, submarine.getType());
    }
}