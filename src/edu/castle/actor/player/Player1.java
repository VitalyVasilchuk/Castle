package edu.castle.actor.player;

public class Player1 extends Player {

    public Player1(int x, int y) {
	super(x, y, "/res/images/boy.png", 3, 4);
	init();
    }

    private void init() {
	setLife(LIFE_MAX);
	setMana(MANA_MAX);
	setDirection(SpriteDirection.LEFT);
	setDelta(6);
    }
    
}
