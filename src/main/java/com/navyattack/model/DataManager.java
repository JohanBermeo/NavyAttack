package com.navyattack.model;

import java.util.List;
import java.util.ArrayList;

/**
 * Gestor de datos del sistema de usuarios de NavyAttack.
 * Mantiene y administra las colecciones de usuarios registrados en el sistema
 * y los usuarios con sesión activa. Proporciona métodos para realizar operaciones
 * CRUD sobre estas colecciones.
 * 
 * @author Juan Manuel Otálora Hernández - Johan Stevan Bermeo Buitrago
 * @version 1.0
 */
public class DataManager {
    
    /**
     * Lista de todos los usuarios registrados en el sistema.
     */
    private List<User> users;
    
    /**
     * Lista de usuarios que tienen sesión activa actualmente.
     */
    private List<User> loggedUsers;
    
    /**
     * Constructor del gestor de datos.
     * Inicializa las listas de usuarios registrados y usuarios con sesión activa.
     */
    public DataManager() {
        this.users = new ArrayList<>();
        this.loggedUsers = new ArrayList<>();
    }
    
    /**
     * Establece la lista completa de usuarios registrados.
     * Si se proporciona null, inicializa con una lista vacía.
     * 
     * @param data Lista de usuarios a establecer
     */
    public void setUsers(List<User> data) {
        if (data != null) {
            this.users = data;
        } else {
            this.users = new ArrayList<>();
        }
    }
    
    /**
     * Obtiene la lista completa de usuarios registrados en el sistema.
     * 
     * @return Lista de todos los usuarios
     */
    public List<User> getUsers() {
        return this.users;
    }
    
    /**
     * Obtiene la lista de usuarios con sesión activa.
     * 
     * @return Lista de usuarios con sesión iniciada
     */
    public List<User> getLoggedUsers() {
        return this.loggedUsers;
    }
    
    /**
     * Añade un usuario a la lista de usuarios con sesión activa.
     * 
     * @param user Usuario a añadir a la lista de sesiones activas (no debe ser null)
     */
    public void addLoggedUser(User user) {
        if (user != null) {
            this.loggedUsers.add(user);
        }
    }
    
    /**
     * Añade un nuevo usuario a la lista de usuarios registrados.
     * 
     * @param user Usuario a registrar en el sistema (no debe ser null)
     */
    public void addUser(User user) {
        if (user != null) {
            this.users.add(user);
        }
    }
    
    /**
     * Elimina un usuario de la lista de usuarios registrados.
     * 
     * @param user Usuario a eliminar del sistema
     */
    public void deleteUser(User user) {
        users.remove(user);
    }
    
    /**
     * Elimina un usuario de la lista de sesiones activas.
     * Cierra la sesión del usuario sin eliminarlo del sistema.
     * 
     * @param user Usuario cuya sesión se cerrará
     */
    public void deleteLoggedUser(User user) {
        loggedUsers.remove(user);
    }
    
    /**
     * Busca un usuario por su nombre de usuario.
     * 
     * @param username Nombre de usuario a buscar
     * @return Usuario encontrado, o null si no existe
     */
    public User findUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    
    /**
     * Obtiene la cantidad total de usuarios registrados en el sistema.
     * 
     * @return Número de usuarios registrados
     */
    public int getUsersCount() {
        return users.size();
    }
    
    /**
     * Verifica si no hay usuarios registrados en el sistema.
     * 
     * @return true si no hay usuarios registrados, false en caso contrario
     */
    public boolean isEmpty() {
        return users.isEmpty();
    }
    
    /**
     * Verifica si no hay usuarios con sesión activa.
     * 
     * @return true si no hay usuarios con sesión iniciada, false en caso contrario
     */
    public boolean isLoggedEmpty() {
        return loggedUsers.isEmpty();
    }
    
    /**
     * Elimina todos los usuarios registrados del sistema.
     * Limpia completamente la lista de usuarios.
     */
    public void clearData() {
        users.clear();
    }
}