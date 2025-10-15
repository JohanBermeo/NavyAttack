package com.navyattack.model;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * Representa un usuario del sistema NavyAttack.
 * Contiene la información de autenticación del usuario (nombre de usuario y contraseña)
 * y mantiene un historial de todas las partidas jugadas.
 * 
 * Esta clase es serializable para permitir la persistencia de los datos de usuario
 * en el sistema de archivos.
 * 
 * @author Juan Manuel Otálora Hernández - Johan Stevan Bermeo Buitrago
 * @version 1.0
 */
public class User implements Serializable {
    
    /**
     * Serial version UID para control de versiones durante la serialización.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Nombre de usuario único que identifica al usuario en el sistema.
     */
    private String username;
    
    /**
     * Contraseña del usuario para autenticación.
     */
    private String password;
    
    /**
     * Lista de partidas jugadas por el usuario.
     * Cada elemento representa una partida completada con sus estadísticas.
     */
    private List<History> history;
    
    /**
     * Constructor del usuario.
     * Inicializa un nuevo usuario con credenciales y un historial vacío.
     * 
     * @param username Nombre de usuario
     * @param password Contraseña del usuario
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.history = new ArrayList<>();
    }
    
    /**
     * Añade una partida al historial del usuario.
     * Registra una nueva partida completada con todas sus estadísticas.
     * 
     * @param game Registro de historial de la partida a añadir
     */
    public void addHistory(History game) {
        this.history.add(game);
    }
    
    /**
     * Obtiene el nombre de usuario.
     * 
     * @return Nombre de usuario
     */
    public String getUsername() {
        return this.username;
    }
    
    /**
     * Obtiene la contraseña del usuario.
     * 
     * @return Contraseña del usuario
     */
    public String getPassword() {
        return this.password;
    }
    
    /**
     * Obtiene el historial completo de partidas del usuario.
     * 
     * @return Lista de partidas jugadas por el usuario
     */
    public List<History> getHistory() {
        return this.history;
    }
}