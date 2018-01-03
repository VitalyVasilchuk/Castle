package edu.castle.actor.monster;

public class Ghost extends Monster {

    public Ghost(int x, int y) {
	super(x, y, "/res/images/ghost.png", 4, 4);
	init();
    }

    private void init() {
	setCanSoar(true);
	setDirection(SpriteDirection.LEFT);
	setDelta((int) (Math.random() * 3 + 1));
	setScore(10);
	setLife(1);
    }
}
