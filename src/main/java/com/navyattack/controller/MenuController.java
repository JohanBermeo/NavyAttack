package com.navyattack.controller;

import javafx.stage.Stage;
import javafx.application.Application;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.navyattack.model.User;
import com.navyattack.model.Board;
import com.navyattack.model.Ship;
import com.navyattack.model.ShipType;
import com.navyattack.view.PlayView;
import com.navyattack.view.MenuView;
import com.navyattack.view.LoginView;
import com.navyattack.view.SignUpView;
import com.navyattack.view.HistoryView;
import com.navyattack.view.DeploymentView;
import com.navyattack.view.TransitionView;
import com.navyattack.view.GameView;
import com.navyattack.view.VictoryView;
import com.navyattack.model.DataManager;
import com.navyattack.model.Authentication;

public class MenuController {

    private GameView gameView;
    private MenuView menuView;
    private PlayView playView;
    private LoginView loginView;
    private SignUpView signUpView;
    private HistoryView historyView;
    private DataManager dataManager;
    private VictoryView victoryView;
    private DeploymentView deploymentView;
    private TransitionView transitionView;

    private static final String DATA_DIR = "data";
    private static final String USERS_FILE = "users.dat";
    private static final String DATA_PATH = DATA_DIR + File.separator + USERS_FILE;

    public MenuController() {
        this.dataManager = new DataManager();
        loadUserData();
    }

    // ==================== MÉTODOS DE CARGA DE DATOS ====================

    private void loadUserData() {
        try {
            createDataDirectory();

            File file = new File(DATA_PATH);
            if (file.exists() && file.length() > 0) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    @SuppressWarnings("unchecked")
                    List<User> users = (List<User>) ois.readObject();

                    for (User user : users) {
                        dataManager.addUser(user);
                    }

                } catch (ClassNotFoundException e) {
                    System.err.println("Error al deserializar los datos de usuarios: " + e.getMessage());
                } catch (InvalidClassException e) {
                    System.err.println("Versión de clase incompatible. Creando nuevo archivo de datos.");
                    saveUserData();
                }
            } else {
                System.out.println("No se encontró archivo de datos existente. Iniciando con datos vacíos.");
            }
        } catch (IOException e) {
            System.err.println("Error al cargar datos de usuarios: " + e.getMessage());
        }
    }

    private void saveUserData() {
        try {
            createDataDirectory();
            List<User> users = dataManager.getUsers();

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_PATH))) {
                oos.writeObject(users);
            }
        } catch (IOException e) {
            System.err.println("Error al guardar datos de usuarios: " + e.getMessage());
        }
    }

    private void createDataDirectory() {
        try {
            Path dataDir = Paths.get(DATA_DIR);
            if (!Files.exists(dataDir)) {
                Files.createDirectories(dataDir);
            }
        } catch (IOException e) {
            System.err.println("Error al crear directorio de datos: " + e.getMessage());
        }
    }

    private void saveNewUser(User user) {
        dataManager.addUser(user);
        saveUserData();
    }

    private void clearViewReferences() {
        this.loginView = null;
        this.signUpView = null;
        this.historyView = null;
        this.menuView = null;
        this.playView = null;
        this.deploymentView = null;
        this.transitionView = null;
        this.gameView = null;
        this.victoryView = null;
    }

    public void saveGameData() {
        saveUserData();
    }

    // ==================== MÉTODOS DE INICIALIZACIÓN ====================

    public void initializeView(Stage primaryStage) {
        this.loginView = new LoginView(this);
        loginView.start(primaryStage);
    }

    public static void launchApp(String[] args) {
        Application.launch(MenuApplication.class, args);
    }

    // ==================== MÉTODOS DE AUTENTICACIÓN ====================

    public boolean handleLogin(String username, String password) {
        User user = dataManager.findUser(username);
        boolean result = Authentication.login(username, password, user);
        if (result) {
            if (!dataManager.getLoggedUsers().contains(user)) {
                dataManager.addLoggedUser(user);
            }
            navigateToGameMenu();
            return true;
        } else {
            return false;
        }
    }

    public boolean handleSignUp(String username, String password, String passwordConfirm) throws Exception {
        User existingUser = dataManager.findUser(username);

        User registered;
        try {
            registered = Authentication.createAccount(username, password, passwordConfirm, existingUser);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        if (registered instanceof User) {
            saveNewUser(registered);
            dataManager.addLoggedUser(registered);
            navigateToGameMenu();
            return true;
        }
        return false;
    }

    public void logoutUser(User user) {
        dataManager.deleteLoggedUser(user);

        if (dataManager.isLoggedEmpty()) {
            navigateToLogin();
        } else {
            navigateToGameMenu();
        }
    }

    // ==================== MÉTODOS DE NAVEGACIÓN ====================

    public void navigateToGameMenu() {
        Stage currentStage = null;

        // Buscar el Stage actual desde cualquier vista activa
        if (loginView != null && loginView.getScene() != null) {
            currentStage = (Stage) loginView.getScene().getWindow();
        } else if (signUpView != null && signUpView.getScene() != null) {
            currentStage = (Stage) signUpView.getScene().getWindow();
        } else if (historyView != null && historyView.getScene() != null) {
            currentStage = (Stage) historyView.getScene().getWindow();
        } else if (playView != null && playView.getScene() != null) {
            currentStage = (Stage) playView.getScene().getWindow();
        } else if (menuView != null && menuView.getScene() != null) {
            currentStage = (Stage) menuView.getScene().getWindow();
        } else if (deploymentView != null && deploymentView.getScene() != null) {
            currentStage = (Stage) deploymentView.getScene().getWindow();
        } else if (gameView != null && gameView.getScene() != null) {
            currentStage = (Stage) gameView.getScene().getWindow();
        } else if (victoryView != null && victoryView.getScene() != null) {  // ← AGREGAR
            currentStage = (Stage) victoryView.getScene().getWindow();
        } else {
            System.err.println("ERROR: No se pudo obtener el Stage actual");
            return;
        }

        // Limpiar referencias
        clearViewReferences();

        // Crear y mostrar MenuView
        this.menuView = new MenuView(this, dataManager.getLoggedUsers());
        menuView.start(currentStage);
    }


    public void navigateToSecondUserLogin() {
        Stage currentStage;
        if (menuView != null && menuView.getScene() != null) {
            currentStage = (Stage) menuView.getScene().getWindow();
        } else {
            System.err.println("ERROR: menuView is null");
            return;
        }

        clearViewReferences();
        this.loginView = new LoginView(this);
        loginView.start(currentStage);
    }

    public void navigateToSignUp() {
        Stage currentStage;
        if (loginView != null && loginView.getScene() != null) {
            currentStage = (Stage) loginView.getScene().getWindow();
        } else {
            System.err.println("ERROR: loginView is null");
            return;
        }

        clearViewReferences();
        this.signUpView = new SignUpView(this);
        signUpView.start(currentStage);
    }

    public void navigateToHistory(User user) {
        Stage currentStage;
        if (menuView != null && menuView.getScene() != null) {
            currentStage = (Stage) menuView.getScene().getWindow();
        } else {
            System.err.println("ERROR: menuView is null");
            return;
        }

        clearViewReferences();
        this.historyView = new HistoryView(this, user);
        historyView.start(currentStage);
    }

    public void navigateToPlay() {
        Stage currentStage = null;

        if (menuView != null && menuView.getScene() != null) {
            currentStage = (Stage) menuView.getScene().getWindow();
        } else if (victoryView != null && victoryView.getScene() != null) {  // ← AGREGAR
            currentStage = (Stage) victoryView.getScene().getWindow();
        } else {
            System.err.println("ERROR: No se pudo obtener el Stage para navigateToPlay");
            return;
        }

        clearViewReferences();
        this.playView = new PlayView(this);
        playView.start(currentStage);
    }

    public void navigateToLogin() {
        Stage currentStage = null;

        if (signUpView != null && signUpView.getScene() != null) {
            currentStage = (Stage) signUpView.getScene().getWindow();
        } else if (menuView != null && menuView.getScene() != null) {
            currentStage = (Stage) menuView.getScene().getWindow();
        } else {
            System.err.println("ERROR: No se pudo obtener el Stage");
            return;
        }

        clearViewReferences();
        this.loginView = new LoginView(this);
        loginView.start(currentStage);
    }

    /**
     * Navega a la vista de deployment (colocación de barcos).
     *
     * @param gameMode Modo de juego ("PVC" para Player vs CPU, "PVP" para Player vs Player)
     */
    public void navigateToDeployment(String gameMode) {
        Stage currentStage = null;

        if (playView != null && playView.getScene() != null) {
            currentStage = (Stage) playView.getScene().getWindow();
        } else {
            System.err.println("ERROR: playView is null or has no scene");
            return;
        }

        clearViewReferences();

        // Crear el tablero del jugador
        Board playerBoard = new Board();

        User currentPlayer = dataManager.getLoggedUsers().isEmpty() ? null : dataManager.getLoggedUsers().get(0);

        // Crear la vista de deployment
        this.deploymentView = new DeploymentView(this, gameMode, currentPlayer);

        // Mostrar la vista
        deploymentView.start(currentStage);

        // Crear el controlador (esto conecta automáticamente los eventos)
        new DeploymentController(playerBoard, deploymentView, this);
    }

    /**
     * Navega a la pantalla de transición entre jugadores (solo PVP).
     */
    public void navigateToTransition(String gameMode) {
        Stage currentStage = null;

        if (deploymentView != null && deploymentView.getScene() != null) {
            currentStage = (Stage) deploymentView.getScene().getWindow();
        } else {
            System.err.println("ERROR: deploymentView is null");
            return;
        }

        clearViewReferences();

        // Obtener el segundo jugador
        User nextPlayer = dataManager.getLoggedUsers().size() > 1 ?
                dataManager.getLoggedUsers().get(1) : null;

        this.transitionView = new TransitionView(this, nextPlayer, gameMode);
        transitionView.start(currentStage);
    }

    /**
     * Navega al deployment del segundo jugador.
     */
    public void navigateToSecondPlayerDeployment(String gameMode) {
        Stage currentStage = null;

        if (transitionView != null && transitionView.getScene() != null) {
            currentStage = (Stage) transitionView.getScene().getWindow();
        } else {
            System.err.println("ERROR: transitionView is null");
            return;
        }

        clearViewReferences();

        Board player2Board = new Board();
        User player2 = dataManager.getLoggedUsers().size() > 1 ?
                dataManager.getLoggedUsers().get(1) : null;

        this.deploymentView = new DeploymentView(this, gameMode, player2);
        deploymentView.start(currentStage);

        new DeploymentController(player2Board, deploymentView, this);
    }

    /**
     * Navega a la vista de juego (batalla).
     *
     * @param player1Board Tablero del jugador 1 con barcos colocados
     * @param player2Board Tablero del jugador 2 con barcos colocados (null en modo PVC)
     * @param gameMode Modo de juego ("PVC" o "PVP")
     */
    public void navigateToGame(Board player1Board, Board player2Board, String gameMode) {
        Stage currentStage = null;

        if (deploymentView != null && deploymentView.getScene() != null) {
            currentStage = (Stage) deploymentView.getScene().getWindow();
        } else {
            System.err.println("ERROR: deploymentView is null");
            return;
        }

        clearViewReferences();

        // Obtener usuarios
        User player1 = dataManager.getLoggedUsers().isEmpty() ? null : dataManager.getLoggedUsers().get(0);
        User player2 = null;

        // Si es modo PVC, crear tablero de CPU con barcos aleatorios
        if (gameMode.equals("PVC")) {
            player2Board = new Board();
            placeShipsRandomly(player2Board);
            player2 = null;  // CPU no tiene usuario
        } else {
            // Modo PVP
            player2 = dataManager.getLoggedUsers().get(1);
        }

        // Crear vista de juego
        this.gameView = new GameView(this, player1, player2, gameMode);
        gameView.start(currentStage);

        new GameController(player1Board, player2Board, gameView, this);
    }

    /**
     * Coloca barcos aleatoriamente en el tablero (para CPU).
     *
     * @param board Tablero donde colocar los barcos
     */
    public void placeShipsRandomly(Board board) {
        java.util.Random random = new java.util.Random();

        // Para cada tipo de barco
        for (ShipType type : ShipType.values()) {
            int quantity = board.getRemainingShips(type);

            for (int i = 0; i < quantity; i++) {
                boolean placed = false;
                int attempts = 0;

                while (!placed && attempts < 100) {
                    // Generar posición aleatoria
                    int row = random.nextInt(Board.BOARD_SIZE);
                    int col = random.nextInt(Board.BOARD_SIZE);

                    // Crear barco
                    Ship ship = new Ship(type);

                    // ✓ Orientación aleatoria: rotar o no rotar
                    if (random.nextBoolean()) {
                        ship.rotate();  // Esto cambia a VERTICAL
                    }
                    // Si no se rota, queda en HORIZONTAL (por defecto)

                    // Intentar colocar
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

    /**
     * Navega a la pantalla de victoria.
     */
    public void navigateToVictory(User winner, User loser, String gameMode, int totalTurns,
                                  String timePlayed, long timePlayedMillis,
                                  int winnerShipsSunk, int loserShipsSunk) {
        Stage currentStage = null;

        if (gameView != null && gameView.getScene() != null) {
            currentStage = (Stage) gameView.getScene().getWindow();
        } else {
            System.err.println("ERROR: gameView is null");
            return;
        }

        clearViewReferences();

        // ✓ Pasar los nuevos parámetros a VictoryView
        this.victoryView = new VictoryView(this, winner, loser, gameMode, totalTurns,
                timePlayed, timePlayedMillis,
                winnerShipsSunk, loserShipsSunk);
        victoryView.start(currentStage);
    }

    // ==================== GETTERS ====================

    public List<User> getLoggedUsers() {
        return dataManager.getLoggedUsers();
    }

    public boolean hasSingleUser() {
        return dataManager.getLoggedUsers().size() == 1;
    }

    // ==================== CLASE INTERNA ====================

    public static class MenuApplication extends Application {
        @Override
        public void start(Stage primaryStage) {
            MenuController controller = new MenuController();
            controller.initializeView(primaryStage);
        }
    }
}