package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Window extends JFrame {
	
	private JLabel label;

	public Window(){
		initUI();
	}
	
	private void initUI(){
		setTitle("TRON");
		setLayout(new FlowLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		label = new JLabel("randomlabel");
		label.addKeyListener(new actionHandler());
	}
	
	private class actionHandler implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()){
			//TODO
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
