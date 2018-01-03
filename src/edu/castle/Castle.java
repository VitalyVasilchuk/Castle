package edu.castle;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class Castle extends JFrame {

    public Castle() {
	initUI();
    }

    private void initUI() {
	add(new Board());

	setResizable(false);
	pack();

	setTitle("Haunted Castle");
	setLocationRelativeTo(null);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
	EventQueue.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		Castle castle = new Castle();
		castle.setVisible(true);
	    }
	});
    }
}
