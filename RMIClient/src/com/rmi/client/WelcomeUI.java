package com.rmi.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.rmi.interfaces.IGameClient;
import com.rmi.interfaces.IGameServer;

public class WelcomeUI extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	JButton startButton;
	JLabel userNameLabel;
	JTextField userNameText;
	JPanel panel;
	public static IGameServer gameServer;
	public static IGameClient gameClient;
	
	public WelcomeUI(IGameServer x,IGameClient y) {
		
		gameServer = x;
		gameClient = y;
		setTitle("Hunt the fly");
		setSize(500,300);
		setLayout(new FlowLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setBackground(Color.WHITE);
		
		userNameLabel = new JLabel();
		userNameLabel.setText("Player Name");
		userNameText = new JTextField(15);
		
		JLabel flyImageJLabel = new JLabel(new ImageIcon("C:\\Users\\Banashri\\workspace\\RMIClient\\src\\fly.jpg"));
		getContentPane().add(flyImageJLabel);
		
		startButton = new JButton("Login");
		
		setVisible(true);
		setLocationRelativeTo(null);
		
		panel = new JPanel(new GridLayout(3,1));
		panel.add(userNameLabel);
		panel.add(userNameText);
		panel.add(startButton);
		add(panel,BorderLayout.CENTER);
		startButton.addActionListener(this);
	}

	public void actionPerformed(ActionEvent actionEvent) {
		String userName = userNameText.getText();
		
		if (!userName.isEmpty()) {
			System.out.println("called: Game console");
			new GameUI(userNameText.getText(), gameServer, gameClient);
			setVisible(false);
			try {
				gameServer.login(userName, gameClient);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
}
