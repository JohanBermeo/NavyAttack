package com.navyattack.model; 

import java.util.List; 
import java.util.ArrayList; 
import java.io.Serializable; 

public class User implements Serializable { 

    private static final long serialVersionUID = 1L; 

    private String username; 
    private String password; 
    private List<History> history; 

    public User(String username, String password) { 
        this.username = username; 
        this.password = password; 
        this.history = new ArrayList<>(); 
    } 

    public void addHistory(History game) { 
        this.history.add(game); 
    } 

    public String getUsername() { 
        return this.username; 
    } 

    public String getPassword() { 
        return this.password; 
    } 

    public List<History> getHistory() { 
        return this.history; 
    } 
} 