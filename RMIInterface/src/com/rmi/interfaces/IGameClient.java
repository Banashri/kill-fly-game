package com.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGameClient extends Remote {
	void receiveFlyHunted(String playerName, int newPoints) throws RemoteException;
	void receiveFlyPosition(int x, int y) throws RemoteException;
}
