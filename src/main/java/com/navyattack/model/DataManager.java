package com.navyattack.model;

import java.util.List;
import java.util.ArrayList;

public class DataManager {

    private List<User> users;
    private List<User> loggedUsers;

    public DataManager() {
        this.users = new ArrayList<>();
	    this.loggedUsers = new ArrayList<>();
    }
    
    public void setUsers(List<User> data) {
        if (data != null) {
            this.users = data;
        } else {
            this.users = new ArrayList<>();
        }
    }
    
    public List<User> getUsers() {
        return this.users;
    }

    public List<User> getLoggedUsers() {
        return this.loggedUsers;
    }
    
    public void addLoggedUser(User user) {
        if (user != null) {
            this.loggedUsers.add(user);
        }
    }

    public void addUser(User user) {
        if (user != null) {
            this.users.add(user);
        }
    }
    
    public void deleteUser(User user) {
        users.remove(user);
    }

    public void deleteLoggedUser(User user) {
        loggedUsers.remove(user);
    }
    
    public User findUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    
    public int getUsersCount() {
        return users.size();
    }
    
    public boolean isEmpty() {
        return users.isEmpty();
    }

    public boolean isLoggedEmpty() {
        return loggedUsers.isEmpty();
    }
    
    public void clearData() {
        users.clear();
    }
}