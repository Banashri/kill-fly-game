package com.rmi.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.rmi.interfaces.IGameClient;
import com.rmi.interfaces.IGameServer;

public class GameUI extends JPanel implements Runnable{

	private static JTable table ;
	
	private static final long serialVersionUID = 1L;
	public static boolean RIGHT_TO_LEFT = false;
	
	static JButton buttonImage = new JButton();
	
	public static int counter = 1;
	public static int posX = 30;
	public static int posY = 40;
	
	public static String userName;
	public static int userPoints = 0;
	public static JLabel flyImageJLabel;
	public static JTable table1;
	public static JPanel gamePanel, /*instructionPanel,*/ scoresPanel;
	public static Map<String, Integer> data = new HashMap<String, Integer>();
	public static TableModel model;
	
	public static JFrame frame;
	public static IGameServer gameServer;
	public static IGameClient gameClient;
	
	public GameUI(String playerName,IGameServer x,IGameClient y) {
		userName = playerName;
		gameServer = x;
		gameClient = y;
		windowOpen();
	}
	public GameUI() {
		super();
	}

	public void addComponentsToPane(final Container pane) {
        
		System.out.println("called: addComponentsToPane()");
        if (!(pane.getLayout() instanceof BorderLayout)) {
            pane.add(new JLabel("Container doesn't use BorderLayout!"));
            return;
        }
        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(
                    java.awt.ComponentOrientation.RIGHT_TO_LEFT);
        }
         
        JLabel welcomeLabel = new JLabel("Welcome to FLY HUNT Game");
        welcomeLabel.setFont(new Font("Garamond", Font.PLAIN, 30));

        pane.add(welcomeLabel, BorderLayout.PAGE_START);
        
        JPanel panel = new JPanel();
        panel = new JPanel();
        panel.setBackground(Color.ORANGE);
        panel.setPreferredSize(new Dimension(160,120));
        pane.add(panel, BorderLayout.LINE_START);
        JLabel instruction = new JLabel();
        instruction.setText("<html><font align='center' size='5'>Game Instructions</font><br/><br/><div><b>1. Click on Fly to hunt</b>"
        		+ "<br/><b>2.Make maximum scores</b></html>");
        panel.add(instruction);
        
        panel = new JPanel();
        panel.setPreferredSize(new Dimension(200, 100));
        panel.setBackground(Color.WHITE);
        pane.add(panel, BorderLayout.CENTER);
        gamePanel = panel;
        
        
        panel = new JPanel();
        panel.setPreferredSize(new Dimension(150, 100));
        panel.setBackground(Color.LIGHT_GRAY);
        pane.add(panel, BorderLayout.LINE_END);
        scoresPanel = panel;
        String curr_dir = System.getProperty("user.dir");
		System.out.println(curr_dir);
        flyImageJLabel = new JLabel(new ImageIcon("file:".concat(curr_dir).concat("/src/fly.jpg")));

        gamePanel.add(flyImageJLabel);
    	flyImageJLabel.setBounds(posX,posY,70,70);
    	
        
    	buttonImage.setLocation(posX, posY);
    	buttonImage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					gameServer.huntFly(userName);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				System.out.println("Button is clicked");
				buttonImage.setLocation(posX, posY);
				
			}
    	});
		try {
			flyImageJLabel = new JLabel(new ImageIcon("file:".concat(curr_dir).concat("/src/fly.jpg")));

			buttonImage.setIcon(new ImageIcon(ImageIO.read(new File(curr_dir.concat("/src/fly.jpg")))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		gamePanel.add(buttonImage);
    	
    	
        
        flyImageJLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent mouseEvent) {
				Toolkit.getDefaultToolkit().beep();
				System.out.println("Hunted");
				
				try {
					gameServer.huntFly(userName);

					/*try {
						flyImageJLabel.setIcon(new ImageIcon(ImageIO.read(file)));
					} catch (IOException e) {
						e.printStackTrace();
					} */
					flyImageJLabel.setBounds(posX,posY,70,70);
					flyImageJLabel.removeMouseListener(this);
					
					System.out.println(counter);

				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});

        JLabel headingText = new JLabel();
        headingText.setText("<html><font size='5'>Scoreboard</font><br/></html>");
        
        scoresPanel.add(headingText);
        
        table = new JTable();
        table.setFillsViewportHeight(true);
		scoresPanel.add(table);
        
        JButton button = new JButton("Click here to LOGOUT");
        pane.add(button, BorderLayout.PAGE_END);
        
        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	System.out.println("Logout Button Clicked");
            	frame.setVisible(false);
            	try {
					gameServer.logout(userName);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
            	new WelcomeUI(gameServer, gameClient);
            }
        });
    }
	
	/**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private void createAndShowGUI() {
    	System.out.println("called : createAndShowGUI()");
    	frame = new JFrame("Fly Hunting Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addComponentsToPane(frame.getContentPane());
        frame.pack();

        Insets insets = frame.getInsets();
        frame.setSize(new Dimension(insets.left + insets.right + 1800,
                insets.top + insets.bottom + 600));
        
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        System.out.println("Create and show GUI finished");
    }

    public void run() {
    	createAndShowGUI();
    }

    private void windowOpen() {
		System.out.println("called : windowOpen()");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        
        Thread t = new Thread(new GameUI());
        t.start();
        try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
	public synchronized static void receiveFlyPosition(int x, int y) {
		System.out.printf("Received x :%d y: %d",x,y);
    	posX = x;
    	posY = y;
    }
	/*
	 * This method is used for updating the scorePanel
	 * Called from Server Side
	 */
	public synchronized static void receiveFlyHunted(String playerName, int newPoints) {
		System.out.printf("Player : %s, points : %d", playerName, newPoints);

		if(playerName.contains("_logout"))
		{
			System.out.println("Removing");
			playerName = playerName.substring(0, playerName.length() - 7);
			data.remove(playerName);
		}
		else
		{
			data.put(playerName, newPoints);
		}
		System.out.println("Initializing for new position at ("+posX+","+posY);
		buttonImage.setLocation(posX, posY);
		
		
		
		model = toTableModel(data);
		table.setModel(model);
		table.setFillsViewportHeight(true);
    	
		System.out.println("Fly is now at ("+posX+","+posY);
		
        //model.addRow(rowData);
        System.out.println("Added");
        
        gamePanel.add(flyImageJLabel);
    	flyImageJLabel.setBounds(posX,posY,70,70);
    	
    	if (newPoints != 0)
        gamePanel.remove(flyImageJLabel);
		gamePanel.validate();
		
	}
	public static TableModel toTableModel(Map<String, Integer> map) {
		DefaultTableModel model = new DefaultTableModel (
		new Object[] { "Key", "Value" }, 0
		);
		for (Iterator<Entry<String, Integer>> it = map.entrySet().iterator(); it.hasNext();) {
		Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)it.next();
		model.addRow(new Object[] { entry.getKey(), entry.getValue() });
		}
		return model;
	}
}