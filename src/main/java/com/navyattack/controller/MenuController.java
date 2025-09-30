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
import com.navyattack.view.PlayView;
import com.navyattack.view.MenuView;
import com.navyattack.view.LoginView;
import com.navyattack.view.SignUpView;
import com.navyattack.view.HistoryView;
import com.navyattack.view.DeploymentView;
import com.navyattack.model.DataManager;
import com.navyattack.model.Authentication;

public class MenuController {

    private MenuView menuView;
    private PlayView playView;
    private LoginView loginView;
    private SignUpView signUpView;
    private HistoryView historyView;
    private DeploymentView deploymentView;
    private DataManager dataManager;

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
        Stage currentStage;
        if (menuView != null && menuView.getScene() != null) {
            currentStage = (Stage) menuView.getScene().getWindow();
        } else {
            System.err.println("ERROR: menuView is null");
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
        // ✓ PRIMERO obtener el Stage
        Stage currentStage = null;

        if (playView != null && playView.getScene() != null) {
            currentStage = (Stage) playView.getScene().getWindow();
        } else {
            System.err.println("ERROR: playView is null or has no scene");
            return;
        }

        // ✓ DESPUÉS limpiar las referencias
        clearViewReferences();

        // Crear el tablero del jugador
        Board playerBoard = new Board();

        // Crear la vista de deployment
        this.deploymentView = new DeploymentView(this, gameMode);

        // Mostrar la vista
        deploymentView.start(currentStage);

        // Crear el controlador (esto conecta automáticamente los eventos)
        new DeploymentController(playerBoard, deploymentView, this);
    }

    /**
     * Navega a la vista de juego (batalla).
     * Este método se llamará cuando el usuario termine de colocar sus barcos.
     *
     * @param playerBoard Tablero del jugador con todos los barcos colocados
     * @param gameMode Modo de juego ("PVC" o "PVP")
     */
    public void navigateToGame(Board playerBoard, String gameMode) {
        // TODO: Implementar cuando tengas GameView
        System.out.println("=================================");
        System.out.println("Navegando a GameView");
        System.out.println("Modo: " + gameMode);
        System.out.println("Barcos colocados: " + playerBoard.getPlacedShipsCount());
        System.out.println("=================================");

        // Por ahora, volver al menú
        navigateToGameMenu();

        /*
        // Cuando implementes GameView, descomentar esto:

        Stage currentStage = null;

        if (deploymentView != null && deploymentView.getScene() != null) {
            currentStage = (Stage) deploymentView.getScene().getWindow();
        } else {
            System.err.println("ERROR: deploymentView is null");
            return;
        }

        clearViewReferences();

        // Crear tablero del enemigo (CPU o segundo jugador)
        Board enemyBoard = new Board();

        // Si es PVC, colocar barcos automáticamente para la CPU
        if (gameMode.equals("PVC")) {
            placeShipsRandomly(enemyBoard);
        }

        // Crear vista de juego
        this.gameView = new GameView(this, playerBoard, enemyBoard, gameMode);

        // Crear controlador de juego
        new GameController(playerBoard, enemyBoard, gameView, this);

        // Mostrar vista
        gameView.start(currentStage);
        */
    }

    public void logoutUser(User user) {
        dataManager.deleteLoggedUser(user);

        if (dataManager.isLoggedEmpty()) {
            navigateToLogin();
        } else {
            navigateToGameMenu();
        }
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