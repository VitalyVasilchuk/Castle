package edu.castle.sprite;

public class Ladder extends Sprite{
    
    public Ladder(int x, int y) {
	super(x, y);
	init();
    }

    private void init() {
	loadImage("/res/images/ladderwall.png");
	loadImage("/res/images/ladder.png");
    }
    
}
