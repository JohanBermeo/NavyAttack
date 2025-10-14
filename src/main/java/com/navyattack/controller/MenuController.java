package com.navyattack.controller;

import java.io.*;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.navyattack.model.User;
import com.navyattack.model.DataManager;
import com.navyattack.model.Authentication;

public class MenuController {

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

    public void saveGameData() {
        saveUserData();
    }

    // ==================== MÉTODOS DE AUTENTICACIÓN ====================

    public boolean handleLogin(String username, String password) {
        User user = dataManager.findUser(username);
        boolean result = Authentication.login(username, password, user);
        if (result) {
            if (!dataManager.getLoggedUsers().contains(user)) {
                dataManager.addLoggedUser(user);
            }
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
            return true;
        }
        return false;
    }

    public void logoutUser(User user) {
        dataManager.deleteLoggedUser(user);
    }

    // ==================== GETTERS ====================

    public List<User> getLoggedUsers() {
        return dataManager.getLoggedUsers();
    }

    public boolean hasSingleUser() {
        return dataManager.getLoggedUsers().size() == 1;
    }

    public boolean hasNoUsers() {
        return dataManager.isLoggedEmpty();
    }
}