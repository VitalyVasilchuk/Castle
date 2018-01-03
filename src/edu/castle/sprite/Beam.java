package edu.castle.sprite;

public class Beam extends Sprite{
    
    public Beam(int x, int y) {
	super(x, y);
	init();
    }

    private void init() {
	loadImage("/res/images/beam.png");
    }
    
}
