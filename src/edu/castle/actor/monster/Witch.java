package edu.castle.actor.monster;

public class Witch extends Monster {

    public Witch(int x, int y) {
	super(x, y,  "/res/images/witch.png", 4, 4);
	init();
    }

    private void init() {
	setDirection(SpriteDirection.LEFT);
	setDelta((int) (Math.random() * 3 + 1));
	setScore(40);
	setLife(4);
    }
}
