package edu.castle.actor.monster;

public class Death extends Monster {

    public Death(int x, int y) {
	super(x, y, "/res/images/death.png", 4, 4);
	init();
    }

    private void init() {
	setDirection(SpriteDirection.LEFT);
	setDelta((int) (Math.random() * 3 + 1));
	setScore(30);
	setLife(3);
    }
}
