package com.navyattack.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests unitarios para UserStatistics
 */
class UserStatisticsTest {

    private User user;
    private List<History> historyList;

    @BeforeEach
    void setUp() {
        user = new User("testuser", "password123");
        historyList = new ArrayList<>();
    }

    // ==================== TESTS CON HISTORIAL VACÍO ====================

    @Test
    @DisplayName("Estadísticas con historial vacío")
    void testStatisticsWithEmptyHistory() {
        UserStatistics stats = new UserStatistics("testuser", historyList);

        assertEquals(0, stats.getTotalGames());
        assertEquals(0, stats.getVictories());
        assertEquals(0, stats.getDefeats());
        assertEquals(0.0, stats.getWinRate());
        assertEquals(0.0, stats.getAverageShipsSunk());
        assertEquals(0.0, stats.getAverageTurns());
        assertEquals("00:00", stats.getAveragePlayTime());
    }

    // ==================== TESTS DE VICTORIAS Y DERROTAS ====================

    @Test
    @DisplayName("Calcular victorias correctamente")
    void testCalculateVictories() {
        // Usuario gana 3 partidas
        historyList.add(createHistory("testuser", "opponent1", 10, 5, 3));
        historyList.add(createHistory("testuser", "opponent2", 12, 5, 2));
        historyList.add(createHistory("testuser", "CPU", 8, 5, 4));

        UserStatistics stats = new UserStatistics("testuser", historyList);

        assertEquals(3, stats.getTotalGames());
        assertEquals(3, stats.getVictories());
        assertEquals(0, stats.getDefeats());
    }

    @Test
    @DisplayName("Calcular derrotas correctamente")
    void testCalculateDefeats() {
        // Usuario pierde 2 partidas
        historyList.add(createHistory("opponent1", "testuser", 10, 5, 3));
        historyList.add(createHistory("CPU", "testuser", 15, 5, 2));

        UserStatistics stats = new UserStatistics("testuser", historyList);

        assertEquals(2, stats.getTotalGames());
        assertEquals(0, stats.getVictories());
        assertEquals(2, stats.getDefeats());
    }

    @Test
    @DisplayName("Calcular mix de victorias y derrotas")
    void testCalculateMixedResults() {
        historyList.add(createHistory("testuser", "opponent1", 10, 5, 3)); // Victoria
        historyList.add(createHistory("opponent2", "testuser", 12, 5, 2)); // Derrota
        historyList.add(createHistory("testuser", "CPU", 8, 5, 4));       // Victoria

        UserStatistics stats = new UserStatistics("testuser", historyList);

        assertEquals(3, stats.getTotalGames());
        assertEquals(2, stats.getVictories());
        assertEquals(1, stats.getDefeats());
    }

    // ==================== TESTS DE WIN RATE ====================

    @Test
    @DisplayName("Win rate con 100% victorias")
    void testWinRateAllWins() {
        historyList.add(createHistory("testuser", "opponent1", 10, 5, 3));
        historyList.add(createHistory("testuser", "opponent2", 12, 5, 2));

        UserStatistics stats = new UserStatistics("testuser", historyList);

        assertEquals(100.0, stats.getWinRate(), 0.01);
    }

    @Test
    @DisplayName("Win rate con 0% victorias")
    void testWinRateNoWins() {
        historyList.add(createHistory("opponent1", "testuser", 10, 5, 3));
        historyList.add(createHistory("opponent2", "testuser", 12, 5, 2));

        UserStatistics stats = new UserStatistics("testuser", historyList);

        assertEquals(0.0, stats.getWinRate(), 0.01);
    }

    @Test
    @DisplayName("Win rate con 50% victorias")
    void testWinRateFiftyPercent() {
        historyList.add(createHistory("testuser", "opponent1", 10, 5, 3));
        historyList.add(createHistory("opponent2", "testuser", 12, 5, 2));

        UserStatistics stats = new UserStatistics("testuser", historyList);

        assertEquals(50.0, stats.getWinRate(), 0.01);
    }

    @Test
    @DisplayName("Win rate con 66.67% victorias")
    void testWinRateTwoOfThree() {
        historyList.add(createHistory("testuser", "opponent1", 10, 5, 3));
        historyList.add(createHistory("testuser", "opponent2", 12, 5, 2));
        historyList.add(createHistory("opponent3", "testuser", 8, 5, 4));

        UserStatistics stats = new UserStatistics("testuser", historyList);

        assertEquals(66.67, stats.getWinRate(), 0.01);
    }

    // ==================== TESTS DE BARCOS HUNDIDOS ====================

    @Test
    @DisplayName("Calcular total de barcos hundidos")
    void testTotalShipsSunk() {
        // Usuario gana y hunde 5, 4, 5 barcos
        historyList.add(createHistory("testuser", "opponent1", 10, 5, 3));
        historyList.add(createHistory("testuser", "opponent2", 12, 4, 2));
        historyList.add(createHistory("testuser", "CPU", 8, 5, 1));

        UserStatistics stats = new UserStatistics("testuser", historyList);

        assertEquals(14, stats.getTotalShipsSunk()); // 5 + 4 + 5
    }

    @Test
    @DisplayName("Calcular total de barcos perdidos")
    void testTotalShipsLost() {
        // Usuario gana pero pierde 3, 2, 1 barcos
        historyList.add(createHistory("testuser", "opponent1", 10, 5, 3));
        historyList.add(createHistory("testuser", "opponent2", 12, 4, 2));
        historyList.add(createHistory("testuser", "CPU", 8, 5, 1));

        UserStatistics stats = new UserStatistics("testuser", historyList);

        assertEquals(6, stats.getTotalShipsLost()); // 3 + 2 + 1
    }

    @Test
    @DisplayName("Calcular barcos hundidos cuando el usuario pierde")
    void testShipsSunkWhenUserLoses() {
        // Usuario pierde pero hundió 3 barcos antes de perder
        historyList.add(createHistory("opponent1", "testuser", 10, 5, 3));

        UserStatistics stats = new UserStatistics("testuser", historyList);

        assertEquals(3, stats.getTotalShipsSunk());
        assertEquals(5, stats.getTotalShipsLost());
    }

    @Test
    @DisplayName("Promedio de barcos hundidos por partida")
    void testAverageShipsSunk() {
        historyList.add(createHistory("testuser", "opponent1", 10, 5, 3));
        historyList.add(createHistory("testuser", "opponent2", 12, 3, 2));
        historyList.add(createHistory("testuser", "CPU", 8, 4, 1));

        UserStatistics stats = new UserStatistics("testuser", historyList);

        assertEquals(4.0, stats.getAverageShipsSunk(), 0.01); // (5 + 3 + 4) / 3
    }

    // ==================== TESTS DE TURNOS ====================

    @Test
    @DisplayName("Calcular total de turnos")
    void testTotalTurns() {
        historyList.add(createHistory("testuser", "opponent1", 10, 5, 3));
        historyList.add(createHistory("testuser", "opponent2", 15, 4, 2));
        historyList.add(createHistory("testuser", "CPU", 20, 5, 1));

        UserStatistics stats = new UserStatistics("testuser", historyList);

        assertEquals(45, stats.getTotalTurns()); // 10 + 15 + 20
    }

    @Test
    @DisplayName("Calcular promedio de turnos")
    void testAverageTurns() {
        historyList.add(createHistory("testuser", "opponent1", 10, 5, 3));
        historyList.add(createHistory("testuser", "opponent2", 20, 4, 2));
        historyList.add(createHistory("testuser", "CPU", 15, 5, 1));

        UserStatistics stats = new UserStatistics("testuser", historyList);

        assertEquals(15.0, stats.getAverageTurns(), 0.01); // (10 + 20 + 15) / 3
    }

    // ==================== TESTS DE TIEMPO ====================

    @Test
    @DisplayName("Calcular tiempo total de juego")
    void testTotalPlayTime() {
        // 5 minutos + 10 minutos + 3 minutos = 18 minutos = 1080000 ms
        historyList.add(createHistoryWithTime("testuser", "opponent1", 10, 5, 3, 300000));
        historyList.add(createHistoryWithTime("testuser", "opponent2", 15, 4, 2, 600000));
        historyList.add(createHistoryWithTime("testuser", "CPU", 8, 5, 1, 180000));

        UserStatistics stats = new UserStatistics("testuser", historyList);

        assertEquals(1080000, stats.getTotalPlayTime()); // 300000 + 600000 + 180000
    }

    @Test
    @DisplayName("Formato de tiempo total con horas")
    void testTotalPlayTimeFormattedWithHours() {
        // 3661 segundos = 1 hora, 1 minuto, 1 segundo
        historyList.add(createHistoryWithTime("testuser", "opponent1", 10, 5, 3, 3661000));

        UserStatistics stats = new UserStatistics("testuser", historyList);

        assertEquals("01:01:01", stats.getTotalPlayTimeFormatted());
    }

    @Test
    @DisplayName("Formato de tiempo total sin horas")
    void testTotalPlayTimeFormattedWithoutHours() {
        // 330 segundos = 5 minutos, 30 segundos
        historyList.add(createHistoryWithTime("testuser", "opponent1", 10, 5, 3, 330000));

        UserStatistics stats = new UserStatistics("testuser", historyList);

        assertEquals("05:30", stats.getTotalPlayTimeFormatted());
    }

    @Test
    @DisplayName("Promedio de tiempo de juego")
    void testAveragePlayTime() {
        historyList.add(createHistoryWithTime("testuser", "opponent1", 10, 5, 3, 300000)); // 5:00
        historyList.add(createHistoryWithTime("testuser", "opponent2", 15, 4, 2, 600000)); // 10:00
        historyList.add(createHistoryWithTime("testuser", "CPU", 8, 5, 1, 300000));        // 5:00

        UserStatistics stats = new UserStatistics("testuser", historyList);

        // Promedio: (300000 + 600000 + 300000) / 3 = 400000 ms = 6:40
        assertEquals("06:40", stats.getAveragePlayTime());
    }

    // ==================== TESTS DE SUMMARY ====================

    @Test
    @DisplayName("getSummary retorna información completa")
    void testGetSummary() {
        historyList.add(createHistory("testuser", "opponent1", 10, 5, 3));
        historyList.add(createHistory("opponent2", "testuser", 15, 4, 2));

        UserStatistics stats = new UserStatistics("testuser", historyList);
        String summary = stats.getSummary();

        assertNotNull(summary);
        assertTrue(summary.contains("Games: 2"));
        assertTrue(summary.contains("W: 1"));
        assertTrue(summary.contains("L: 1"));
        assertTrue(summary.contains("Win Rate: 50"));
    }

    @Test
    @DisplayName("toString retorna el summary")
    void testToString() {
        historyList.add(createHistory("testuser", "CPU", 10, 5, 3));

        UserStatistics stats = new UserStatistics("testuser", historyList);

        assertEquals(stats.getSummary(), stats.toString());
    }

    @Test
    @DisplayName("getUsername retorna el username correcto")
    void testGetUsername() {
        UserStatistics stats = new UserStatistics("testplayer", historyList);

        assertEquals("testplayer", stats.getUsername());
    }

    // ==================== TESTS DE CASOS EDGE ====================

    @Test
    @DisplayName("Estadísticas con una sola partida ganada")
    void testSingleGameWon() {
        historyList.add(createHistory("testuser", "CPU", 10, 5, 3));

        UserStatistics stats = new UserStatistics("testuser", historyList);

        assertEquals(1, stats.getTotalGames());
        assertEquals(1, stats.getVictories());
        assertEquals(0, stats.getDefeats());
        assertEquals(100.0, stats.getWinRate());
        assertEquals(5.0, stats.getAverageShipsSunk());
        assertEquals(3, stats.getTotalShipsLost());
    }

    @Test
    @DisplayName("Estadísticas con una sola partida perdida")
    void testSingleGameLost() {
        historyList.add(createHistory("CPU", "testuser", 10, 5, 3));

        UserStatistics stats = new UserStatistics("testuser", historyList);

        assertEquals(1, stats.getTotalGames());
        assertEquals(0, stats.getVictories());
        assertEquals(1, stats.getDefeats());
        assertEquals(0.0, stats.getWinRate());
        assertEquals(3.0, stats.getAverageShipsSunk());
        assertEquals(5, stats.getTotalShipsLost());
    }

    @Test
    @DisplayName("Estadísticas con muchas partidas")
    void testManyGames() {
        for (int i = 0; i < 50; i++) {
            if (i % 2 == 0) {
                historyList.add(createHistory("testuser", "opponent" + i, 10 + i, 5, 2));
            } else {
                historyList.add(createHistory("opponent" + i, "testuser", 10 + i, 5, 3));
            }
        }

        UserStatistics stats = new UserStatistics("testuser", historyList);

        assertEquals(50, stats.getTotalGames());
        assertEquals(25, stats.getVictories());
        assertEquals(25, stats.getDefeats());
        assertEquals(50.0, stats.getWinRate());
    }

    @Test
    @DisplayName("Estadísticas con historial null")
    void testStatisticsWithNullHistory() {
        UserStatistics stats = new UserStatistics("testuser", null);

        assertEquals(0, stats.getTotalGames());
        assertEquals(0, stats.getVictories());
        assertEquals(0, stats.getDefeats());
    }

    // ==================== MÉTODOS HELPER ====================

    /**
     * Crea un History para testing
     */
    private History createHistory(String winner, String loser, int turns,
                                  int winnerShips, int loserShips) {
        return new History(
                List.of(user),
                winner,
                loser,
                "05:00",
                300000,
                "PVP",
                turns,
                winnerShips,
                loserShips
        );
    }

    /**
     * Crea un History con tiempo específico
     */
    private History createHistoryWithTime(String winner, String loser, int turns,
                                          int winnerShips, int loserShips, long timeMillis) {
        int minutes = (int) (timeMillis / 60000);
        int seconds = (int) ((timeMillis % 60000) / 1000);
        String timeStr = String.format("%02d:%02d", minutes, seconds);

        return new History(
                List.of(user),
                winner,
                loser,
                timeStr,
                timeMillis,
                "PVP",
                turns,
                winnerShips,
                loserShips
        );
    }
}