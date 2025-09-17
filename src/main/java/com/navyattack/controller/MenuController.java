package com.navyattack.controller;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.ArrayList;

import com.navyattack.model.User;
import com.navyattack.view.MenuView;
import com.navyattack.view.LoginView;
import com.navyattack.view.SignUpView;
import com.navyattack.model.DataManager;
import com.navyattack.model.Authentication;


public class MenuController {
    
    private MenuView menuView;
    private LoginView loginView;
    private SignUpView signUpView;
    private List<User> loggedUsers;
    private DataManager dataManager;
    
    private static final String DATA_DIR = "data";
    private static final String USERS_FILE = "users.dat";
    private static final String DATA_PATH = DATA_DIR + File.separator + USERS_FILE;
    
    public MenuController() {
        this.dataManager = new DataManager();
        this.loggedUsers = new ArrayList<>();
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
	        if (!loggedUsers.contains(user)) {
                loggedUsers.add(user);
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
	        navigateToGameMenu();
            return true;
        }
        return false;
    }
    
    private void navigateToGameMenu() {
        Stage currentStage;
        if (loginView != null && loginView.getScene() != null) {
            currentStage = (Stage) loginView.getScene().getWindow();
        } else if (signUpView != null && signUpView.getScene() != null) {
            currentStage = (Stage) signUpView.getScene().getWindow();
        } else {
            return;
        }
        
        this.menuView = new MenuView(this, new ArrayList<>(loggedUsers));
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
        
        this.loginView = new LoginView(this);
        loginView.start(currentStage);
    }

    public void navigateToSignUp() {
        Stage currentStage = (Stage) loginView.getScene().getWindow();
        this.signUpView = new SignUpView(this);
        signUpView.start(currentStage);
    }
    
    public void navigateToLogin() {
        Stage currentStage;
        if (signUpView != null && signUpView.getScene() != null) {
            currentStage = (Stage) signUpView.getScene().getWindow();
        } else if (menuView != null && menuView.getScene() != null) {
            currentStage = (Stage) menuView.getScene().getWindow();
        } else {
            return;
        }
        
        this.loginView = new LoginView(this);
        loginView.start(currentStage);
    }

    // Método para cerrar sesión de un usuario específico
    public void logoutUser(User user) {
        loggedUsers.remove(user);
        // Si no quedan usuarios logueados, regresar al login
        if (loggedUsers.isEmpty()) {
            navigateToLogin();
        } else {
            // Actualizar MenuView con los usuarios restantes
            navigateToGameMenu();
        }
    }
    
    // Getters para que las vistas puedan acceder a la información
    public List<User> getLoggedUsers() {
        return new ArrayList<>(loggedUsers);
    }
    
    public boolean hasMultipleUsers() {
        return loggedUsers.size() > 1;
    }
    
    public boolean hasSingleUser() {
        return loggedUsers.size() == 1;
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