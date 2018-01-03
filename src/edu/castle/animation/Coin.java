package edu.castle.animation;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Coin extends Animation{
    private int score;
    
    public Coin(int x, int y, int score) {
	super(x, y, "/res/images/coin.png", 8, 1);
	this.score = score;
	setLoop(25); // ~15 секунд = (15с * 1000мс) / (75мс/тик * 8кадров)
    }

    public int getScore() {
	return score;
    }

    public void setScore(int score) {
	this.score = score;
    }
    public void sound() {
	try {
	    soundPlay("/res/sounds/coin.wav");
	} catch (Exception ex) {
	    Logger.getLogger(Coin.class.getName()).log(Level.SEVERE, null, ex);
	}
    }     
}
