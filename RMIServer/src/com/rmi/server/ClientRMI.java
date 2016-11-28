package com.rmi.server;

import com.rmi.interfaces.IGameClient;

public class ClientRMI {
	int points;
	String playerName;
	IGameClient gameClient;
	
	public ClientRMI(int x, String y, IGameClient z){
		this.points = x;
		this.playerName = y;
		this.gameClient = z;
		
	}
}