package com.navyattack.controller;

import javafx.stage.Stage;
import javafx.application.Application;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;

import com.navyattack.model.User;
import com.navyattack.view.PlayView;
import com.navyattack.view.MenuView;
import com.navyattack.view.LoginView;
import com.navyattack.view.SignUpView;
import com.navyattack.view.HistoryView;
import com.navyattack.model.DataManager;
import com.navyattack.model.Authentication;


public class MenuController {
    
    private MenuView menuView;
    private PlayView playView;
    private LoginView loginView;
    private SignUpView signUpView;
    private HistoryView historyView;
    private DataManager dataManager;
    
    private static final String DATA_DIR = "data";
    private static final String USERS_FILE = "users.dat";
    private static final String DATA_PATH = DATA_DIR + File.separator + USERS_FILE;
    
    public MenuController() {
        this.dataManager = new DataManager();
        loadUserData();
    }
    
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
                    // Si hay incompatibilidad, crear archivo nuevo
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
    }
    
    // Método para inicializar la vista y pasarle la referencia del controlador
    public void initializeView(Stage primaryStage) {
        this.loginView = new LoginView(this);
        loginView.start(primaryStage);
    }
    
    // Método estático para lanzar la aplicación
    public static void launchApp(String[] args) {
        Application.launch(MenuApplication.class, args);
    }
    
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
    
    public void navigateToGameMenu() {
        Stage currentStage = null;
        
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
        } else {
            return;
        }
        
        clearViewReferences();
        
        this.menuView = new MenuView(this, dataManager.getLoggedUsers());
        menuView.start(currentStage);
    }
    
    // Método para permitir login de un segundo usuario
    public void navigateToSecondUserLogin() {
        Stage currentStage;
        if (menuView != null && menuView.getScene() != null) {
            currentStage = (Stage) menuView.getScene().getWindow();
        } else {
            return;
        }
        
        clearViewReferences();
        this.loginView = new LoginView(this);
        loginView.start(currentStage);
    }

    public void navigateToSignUp() {
        Stage currentStage = (Stage) loginView.getScene().getWindow();
        clearViewReferences();
        this.signUpView = new SignUpView(this);
        signUpView.start(currentStage);
    }

    public void navigateToHistory(User user) {
        Stage currentStage = (Stage) menuView.getScene().getWindow();
        clearViewReferences();
        this.historyView = new HistoryView(this, user);
        historyView.start(currentStage);
    }
    
    public void navigateToPlay() {
        Stage currentStage = (Stage) menuView.getScene().getWindow();
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
            return;
        }
        
        clearViewReferences();
        this.loginView = new LoginView(this);
        loginView.start(currentStage);
    }

    // Método para cerrar sesión
    public void logoutUser(User user) {
        dataManager.deleteLoggedUser(user);

        if (dataManager.isLoggedEmpty()) {
            navigateToLogin();
        } else {
            navigateToGameMenu();
        }
    }
    
    // Getters para que las vistas puedan acceder a la información
    public List<User> getLoggedUsers() {
        return dataManager.getLoggedUsers();
    }
    
    public boolean hasSingleUser() {
        return dataManager.getLoggedUsers().size() == 1;
    }
    
    // Clase interna para manejar el lanzamiento de la aplicación JavaFX
    public static class MenuApplication extends Application {
        @Override
        public void start(Stage primaryStage) {
            MenuController controller = new MenuController();
            controller.initializeView(primaryStage);
        }
    }
}