package gui;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import client.Client;
import client.Player1;
import client.Player2;
import server.Server;


public class Tron extends JFrame {

	private static final long serialVersionUID = -7965987958710763944L;
	
	private JLabel statusbar;
    private Server server;

    public Tron(Server server) {
        this.server = server;
        initUI();
   }
    
   private void initUI() {

        statusbar = new JLabel(" ");
        add(statusbar, BorderLayout.SOUTH);
        Board board = new Board(this, server);
        add(board);
        board.start();

        setSize(
        	server.getMap().getWidth() * 10,
        	server.getMap().getHeight() * 10 + statusbar.getHeight()
        );
        setTitle("TRON");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
   }

   public JLabel getStatusBar() {
       
       return statusbar;
   }

    public static void main(String[] args) {
    	
        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
            	
        		Client[] clients = {
        				new Player1(1),
        				new Player2(2),
        		};
            	
            	Server server = new Server(clients);
        		
        		Tron game = new Tron(server);
                game.setVisible(true);
            }
        });                
    } 
}