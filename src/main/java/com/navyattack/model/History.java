package com.navyattack.model; 

import java.util.List; 
import java.io.Serializable; 

public class History implements Serializable { 

    private static final long serialVersionUID = 1L; 

    private String winner; 
    private List<User> users; 
    private String timePlayed; 

    public History(List<User> users, String winner, String timePlayed) { 
        this.users = users; 
        this.winner = winner; 
        this.timePlayed = timePlayed; 
    } 

    public List<User> getPlayers() { 
        return this.users; 
    } 

    public String getWinner() { 
        return this.winner; 

    } 

    public String getTimePlayed() { 
        return this.timePlayed; 
    } 
} 