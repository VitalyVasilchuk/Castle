package edu.castle.sprite;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Bag extends Sprite {

    public Bag(int x, int y) {
	super(x, y);
	init();
    }

    private void init() {
	loadImage("/res/images/woodbox.png");
	loadImage("/res/images/bag.png");
	setStage(0);
    }
    
    public void sound() {
	try {
	    soundPlay("/res/sounds/bag_gold.wav");
	} catch (Exception ex) {
	    Logger.getLogger(LifeRed.class.getName()).log(Level.SEVERE, null, ex);
	}
    }       
}
