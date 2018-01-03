package edu.castle.sprite;

public class Blank extends Sprite{
    
    public Blank(int x, int y) {
	super(x, y);
	init();
    }

    private void init() {
	loadImage("/res/images/blank.png");
    }
    
}
