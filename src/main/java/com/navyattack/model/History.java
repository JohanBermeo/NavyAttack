package com.navyattack.model;

public class History {
    
	private String winner;
	private User[] users;
	private String timePlayed;

	public History(User[] users, String winner, String timePlayed) {
		this.users = users;
		this.winner = winner;
		this.timePlayed = timePlayed;	
	}

	public User[] getPlayers() {
		return this.users;
	}
}