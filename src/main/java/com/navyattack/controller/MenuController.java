package com.navyattack.controller;

import javafx.application.Application;
import javafx.stage.Stage;
import com.navyattack.model.User;
import com.navyattack.view.LoginView;
import com.navyattack.view.SignUpView;
import com.navyattack.model.DataManager;
import com.navyattack.model.Authentication;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class MenuController {
    
    private LoginView loginView;
    private SignUpView signUpView;
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
            navigateToGameMenu();
            return true;
        } else {
            return false;
        }
    }
    
    // Método para manejar el registro - MODIFICADO para persistencia
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
            return true;
        }
        return false;
    }
    
    // Método para navegar al menú del juego
    private void navigateToGameMenu() {
        System.out.println("Navegando al menú del juego...");
    }
    
    public void navigateToSignUp() {
        Stage currentStage = (Stage) loginView.getScene().getWindow();
        this.signUpView = new SignUpView(this);
        signUpView.start(currentStage);
    }
    
    public void navigateToLogin() {
        Stage currentStage = (Stage) signUpView.getScene().getWindow();
        loginView.start(currentStage);
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