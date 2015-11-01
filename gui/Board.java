package gui;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import client.Client;
import server.Server;
import map.TronMap;

public class Board extends JPanel 
        implements ActionListener {

	private static final long serialVersionUID = 1576678169022665921L;

    private Timer timer;
    private boolean isStarted = false;
    private boolean isPaused = false;
    private JLabel statusbar;
    
    private Server server;
    private Tron tron;
    
    final int mapWidth = 50;
	final int mapHeight = 50;

    public Board(Tron tron, Server server) {
    	this.server = server;
    	this.tron = tron;
    	
        initBoard();
    }
    
    private void initBoard() {
        
       setFocusable(true);
       timer = new Timer(server.getRefreshInterval(), this);
       timer.start(); 

       statusbar =  tron.getStatusBar();
       
       addKeyListener(new GenericControls());
       
       for( Client client : server.getClients() ){
    	   addKeyListener(client.getControls());
       }
       
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	if( !this.server.isOver() ){
	    	this.server.refreshMap();
	    	repaint();
    	} else {
    		try {
    			int winner = this.server.getWinner();
    			if( winner == -1 ){
    				this.statusbar.setText("Tie!");
    			} else {
    				Client winnerClient = this.server.getClient(winner);
    				
    				this.statusbar.setForeground(winnerClient.color);
    				this.statusbar.setText(winnerClient.getName()+" won!");
    			}
    		} catch (Exception ex){
    			
    		}
    		timer.stop();
    	}
    }

    private int squareWidth() { return (int) getSize().getWidth() / mapWidth; }
    private int squareHeight() { return (int) (getSize().getHeight() - statusbar.getHeight()) / mapHeight; }
//    private Tetrominoes shapeAt(int x, int y) { return board[(y * BoardWidth) + x]; }


    public void start()  {
        
        if (this.isPaused)
            return;

        this.isStarted = true;
        
        this.server.generateMap(this.mapWidth, this.mapHeight);
        
        this.server.startGame();
        
        this.timer.start();
    }

    private void pause()  {
        
        if (!isStarted)
            return;

        isPaused = !isPaused;
        
        if (isPaused) {
            
            timer.stop();
            statusbar.setText("paused");
        } else {
            
            timer.start();
            statusbar.setText(" ");
        }
        
        repaint();
    }
    
    private void doDrawing(Graphics g) {
        
        int mapValue;
        Color color;
        
        for (int y = 0; y < mapHeight; y++){
			for (int x = 0; x < mapWidth; x++){
				mapValue = server.getMap().getValue(x, y);
				if( mapValue == TronMap.FREE ){
					//üres mezõ
					color = new Color(200, 200, 200);
				} else if( mapValue > 0 ){
					//akadály
					color = new Color(10, 10, 10);
				} else {
					//kliens
					color = server.getClient(-mapValue).color;
				}
				drawSquare(g, x * squareWidth(), y * squareHeight(), color);
			}
        }

    }

    @Override
    public void paintComponent(Graphics g) { 

        super.paintComponent(g);
        doDrawing(g);
    }


    private void drawSquare(Graphics g, int x, int y, Color color)  {
        
        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);
        
        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);

        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight() - 1,
                         x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1,
                         x + squareWidth() - 1, y + 1);

    }

    class GenericControls implements KeyListener {
        
         @Override
         public void keyPressed(KeyEvent e) {

             int keycode = e.getKeyCode();

             if (keycode == 'p' || keycode == 'P') {
                 pause();
                 return;
             }
             
             if (keycode == 'r' || keycode == 'R'){
            	 System.out.println("START");
            	 start();
             }

         }

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
     }
}