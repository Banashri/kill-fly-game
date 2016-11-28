package com.rmi.client;

import java.net.InetAddress;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.rmi.interfaces.IGameClient;
import com.rmi.interfaces.IGameServer;

public class GameClient implements IGameClient {

	/**
	 * @return 
	 * 
	 */
	@Override
	public void receiveFlyHunted(String playerName, int newPoints) {
		GameUI.receiveFlyHunted(playerName, newPoints);
	}

	@Override
	public void receiveFlyPosition(int x, int y) {
		GameUI.receiveFlyPosition(x,y);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		if(System.getSecurityManager() == null)
			System.setSecurityManager(new RMISecurityManager());
		
		String curr_dir = System.getProperty("user.dir");
		System.out.println(curr_dir);
		System.setProperty("java.rmi.server.codebase","file:".concat(curr_dir).concat("/src"));
		try{
			String host = null ;
			if(args.length == 0)
				host = InetAddress.getLocalHost().getHostAddress();
			else
				host = args[0];
			Registry registry = LocateRegistry.getRegistry(host,1000);
			System.out.println("Got Registry");
			IGameServer GameServer = (IGameServer) registry.lookup("GameServer");
			System.out.println("Lookup successful");
			
			IGameClient client = new GameClient();
			UnicastRemoteObject.exportObject(client, 0);
			System.out.println("Lookup successful");
			System.out.println("called: Welcome Window GUI");
			new WelcomeUI(GameServer,client);
		}
		catch(Exception e)
		{
			System.out.println(e);
			e.printStackTrace();
		}
	}

}
