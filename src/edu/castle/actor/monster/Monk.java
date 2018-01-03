package edu.castle.actor.monster;

public class Monk extends Monster {

    public Monk(int x, int y) {
	super(x, y, "/res/images/monk.png", 4, 4);
	init();
    }

    private void init() {
	setDirection(SpriteDirection.LEFT);
	setDelta((int) (Math.random() * 3 + 1));
	setScore(20);
	setLife(2);
    }
    
}
