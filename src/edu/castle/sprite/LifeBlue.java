package edu.castle.sprite;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LifeBlue extends Sprite{

    public LifeBlue(int x, int y) {
	super(x, y);
	init();
    }

    private void init() {
	loadImage("/res/images/woodbox.png");
	loadImage("/res/images/bottle_blue.png");
	setStage(0);
    }
    
    public void sound() {
	try {
	    soundPlay("/res/sounds/bottle.wav");
	} catch (Exception ex) {
	    Logger.getLogger(LifeRed.class.getName()).log(Level.SEVERE, null, ex);
	}
    }    
    
}
