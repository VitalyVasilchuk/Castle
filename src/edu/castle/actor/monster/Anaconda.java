package edu.castle.actor.monster;

public class Anaconda extends Monster {

    public Anaconda(int x, int y) {
	super(x, y, "/res/images/anaconda.png", 3, 4);
	init();
    }

    private void init() {
	setDirection(SpriteDirection.LEFT);
	setDelta((int) (Math.random() * 3 + 1));
	setScore(100);
	setLife(5);
    }
}
