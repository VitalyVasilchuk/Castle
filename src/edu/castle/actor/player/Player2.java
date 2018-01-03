package edu.castle.actor.player;

public class Player2 extends Player {

    public Player2(int x, int y) {
	super(x, y, "/res/images/girl.png", 3, 4);
	init();
    }

    private void init() {
	setLife(LIFE_MAX);
	setMana(MANA_MAX);
	setDirection(SpriteDirection.RIGHT);
	setDelta(6);
    }

}
