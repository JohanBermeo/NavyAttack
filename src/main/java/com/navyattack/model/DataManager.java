package com.navyattack.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataManager {

    private List<User> users;
    
    public DataManager() {
        this.users = new ArrayList<>();
	this.users.add(new User("juan", "123"));
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
    
    public void addData(User user) {
        if (user != null) {
            this.users.add(user);
        }
    }
    
    public boolean deleteUser(String username) {
        Iterator<User> iterator = users.iterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            if (user.getUsername() == username) {
                iterator.remove();
                return true;
            }
        }
        return false;
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
    
    public void clearData() {
        users.clear();
    }
}