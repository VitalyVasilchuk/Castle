package edu.castle.sprite;

public class Wall extends Sprite {

    public Wall(int x, int y) {
	super(x, y);
	init();
    }

    private void init() {
	loadImage("/res/images/wall.png");
    }
}
