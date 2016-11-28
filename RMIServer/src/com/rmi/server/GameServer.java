package com.rmi.server;

import java.io.IOException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.Permission;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PropertyPermission;
import java.util.Random;

import com.rmi.interfaces.IGameClient;
import com.rmi.interfaces.IGameServer;

public class GameServer implements IGameServer {
	
	//co-ordinates of fly
	private static int x,y;
	private static final long serialVersionUID = 1L;
	LinkedList<ClientRMI> clients;
	
	public GameServer() throws RemoteException{
		clients = new LinkedList<ClientRMI>();
	}
	@Override
	public synchronized void login(String playerName, IGameClient client)
			throws RemoteException {
		// TODO Auto-generated method stub
		boolean player_name_present = false;
		int index = 0;
		int size = clients.size();
		for(int i = 0;i < size;i++)
			if(clients.get(i).playerName.equals(playerName))
			{
				player_name_present = true;
			}
		if(player_name_present)
			System.out.println("A player with the same name has already registered\n Please choose another name");
		else
			clients.add(new ClientRMI(0,playerName,client));
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("size is : %d",clients.size());
		index = clients.size() - 1;
		System.out.printf("%s logged in",playerName);
		
		if(clients.size() == 1) {
			x = new Random().nextInt(Math.abs((500)+new Random().nextInt(100)));
			y = new Random().nextInt(Math.abs((600)+new Random().nextInt(100)));
		}

		client.receiveFlyPosition(x, y);
		client.receiveFlyHunted(playerName, 0);
		for(int i = 0; i < clients.size() - 1; i++)
			clients.get(index).gameClient.receiveFlyHunted(clients.get(i).playerName, clients.get(i).points);
		
		/*for(int i = 0;i < clients.size();i++)
			clients.get(i).gameClient.receiveFlyHunted(playerName, 0);*/
	}

	@Override
	public synchronized void logout(String playerName) throws RemoteException {
		// TODO Auto-generated method stub
		int size = clients.size();
		for(int i = 0;i < size;i++) {
			if(clients.get(i).playerName.equals(playerName))
			{
				clients.remove(i);
				break;
			}
		}
		for(int i = 0;i < clients.size();i++)
			clients.get(i).gameClient.receiveFlyHunted(playerName.concat("_logout"),0);
	}

	@Override
	public synchronized void huntFly(String playerName) throws RemoteException {
		// TODO Auto-generated method stub
		int size = clients.size();
		
		// score updated
		for(int i = 0;i < size;i++)
			if(clients.get(i).playerName.equals(playerName))
				clients.get(i).points += 2;
		x = new Random().nextInt(Math.abs((500)+new Random().nextInt(100)));
		y = new Random().nextInt(Math.abs((500)+new Random().nextInt(100)));
		for(int i = 0;i < size;i++)
			clients.get(i).gameClient.receiveFlyPosition(x, y);
		
		for(int i = 0;i < size;i++)
			for(int j = 0;j < size;j++)
			{
				clients.get(i).gameClient.receiveFlyHunted(clients.get(j).playerName,clients.get(j).points);
			}
		
		
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ArrayList<Permission> perms = new ArrayList<Permission>();
		perms.add(new PropertyPermission("user.dir", "read"));
		System.out.println(System.getProperty("java.security.policy"));
		if(System.getSecurityManager() == null)
			System.setSecurityManager(new RMISecurityManager());
		String curr_dir = System.getProperty("user.dir");
		System.out.println(curr_dir);
		System.setProperty("java.rmi.server.codebase","file:".concat(curr_dir).concat("/src"));
		try
		{
		String name = "GameServer";
		IGameServer GameServer = new GameServer();
		IGameServer stub = (IGameServer) UnicastRemoteObject.exportObject(GameServer, 0);
		System.out.println("Server started 1");
		Registry registry = LocateRegistry.createRegistry(1000);
		System.out.println("Server started 2");
		registry.rebind(name, stub);
		System.out.println("Server started");
		}
		catch(Exception e)
		{
			System.out.println(e);
			e.printStackTrace();
		}
	}

}
