package edu.castle.actor.monster;

public class Spider extends Monster{
    
    public Spider(int x, int y) {
	super(x, y, "/res/images/spider4.png", 4, 4);
	init();
    }

    private void init() {
	setDirection(SpriteDirection.LEFT);
	setDelta((int) (Math.random() * 3 + 1));
	setScore(30);
	setLife(3);
    }
    
}
