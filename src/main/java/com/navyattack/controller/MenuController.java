package com.navyattack.controller;

import java.io.*;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.navyattack.model.User;
import com.navyattack.model.DataManager;
import com.navyattack.model.Authentication;

/**
 * Controlador del menú principal del juego NavyAttack.
 * Gestiona la autenticación de usuarios, la persistencia de datos
 * y la coordinación entre el sistema de usuarios y las diferentes vistas.
 * 
 * @author Juan Manuel Otálora Hernández - Johan Stevan Bermeo Buitrago
 * @version 1.0
 */
public class MenuController {

    /**
     * Gestor de datos que mantiene la información de usuarios y sesiones activas.
     */
    private DataManager dataManager;

    /**
     * Directorio donde se almacenan los archivos de datos del juego.
     */
    private static final String DATA_DIR = "data";
    
    /**
     * Nombre del archivo que contiene los datos de usuarios serializados.
     */
    private static final String USERS_FILE = "users.dat";
    
    /**
     * Ruta completa al archivo de datos de usuarios.
     */
    private static final String DATA_PATH = DATA_DIR + File.separator + USERS_FILE;

    /**
     * Constructor del controlador del menú.
     * Inicializa el gestor de datos y carga la información de usuarios almacenada.
     */
    public MenuController() {
        this.dataManager = new DataManager();
        loadUserData();
    }

    /**
     * Carga los datos de usuarios desde el archivo de persistencia.
     * Si el archivo no existe o está vacío, inicia con datos vacíos.
     * Maneja errores de deserialización e incompatibilidad de versiones.
     */
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

    /**
     * Guarda todos los datos de usuarios en el archivo de persistencia.
     * Serializa la lista completa de usuarios en formato binario.
     */
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

    /**
     * Crea el directorio de datos si no existe.
     * Utilizado para asegurar que la estructura de directorios esté presente
     * antes de realizar operaciones de lectura/escritura.
     */
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

    /**
     * Guarda un nuevo usuario en el sistema.
     * Añade el usuario al gestor de datos y persiste los cambios en disco.
     * 
     * @param user Usuario a guardar
     */
    private void saveNewUser(User user) {
        dataManager.addUser(user);
        saveUserData();
    }

    /**
     * Guarda los datos del juego actual.
     * Persiste los cambios realizados durante una partida, como historiales
     * y estadísticas actualizadas.
     */
    public void saveGameData() {
        saveUserData();
    }

    /**
     * Maneja el proceso de inicio de sesión de un usuario.
     * Valida las credenciales y añade el usuario a la lista de usuarios activos.
     * 
     * @param username Nombre de usuario
     * @param password Contraseña del usuario
     * @return true si el inicio de sesión fue exitoso, false en caso contrario
     */
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

    /**
     * Maneja el proceso de registro de un nuevo usuario.
     * Valida los datos, crea la cuenta y añade el usuario al sistema.
     * 
     * @param username Nombre de usuario deseado
     * @param password Contraseña del usuario
     * @param passwordConfirm Confirmación de la contraseña
     * @return true si el registro fue exitoso, false en caso contrario
     * @throws Exception Si ocurre un error durante la validación o creación de la cuenta
     */
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

    /**
     * Cierra la sesión de un usuario.
     * Elimina al usuario de la lista de usuarios activos.
     * 
     * @param username Nombre del usuario que cerrará sesión
     */
    public void logoutUser(String username) {
        User user = dataManager.findUser(username);
        dataManager.deleteLoggedUser(user);
    }

    /**
     * Obtiene la lista de usuarios que tienen sesión activa.
     * 
     * @return Lista de usuarios con sesión activa
     */
    public List<User> getLoggedUsers() {
        return dataManager.getLoggedUsers();
    }

    /**
     * Verifica si hay exactamente un usuario con sesión activa.
     * 
     * @return true si hay un solo usuario activo, false en caso contrario
     */
    public boolean hasSingleUser() {
        return dataManager.getLoggedUsers().size() == 1;
    }

    /**
     * Verifica si no hay usuarios con sesión activa.
     * 
     * @return true si no hay usuarios activos, false en caso contrario
     */
    public boolean hasNoUsers() {
        return dataManager.isLoggedEmpty();
    }

    /**
     * Busca y obtiene un usuario por su nombre de usuario.
     * 
     * @param username Nombre de usuario a buscar
     * @return Usuario encontrado, o null si no existe
     */
    public User getUserByUsername(String username) {
        return dataManager.findUser(username);
    }
}