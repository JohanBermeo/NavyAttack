package com.navyattack.model;

public class User {
    
	History[] history;
	private String username;
	private String password;

	public User(String username, String password) {
		this.username = username;
		this.password = password;	
	}

	public void addHistory(History game) {
		//this.history.add(game);
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}

	public History[] getHistory() {
		return this.history;
	}
}