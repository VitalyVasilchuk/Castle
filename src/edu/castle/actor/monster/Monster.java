package edu.castle.actor.monster;

import edu.castle.actor.Actor;

public class Monster extends Actor {

    private int score;
    private int life;
    private boolean dead;

    public Monster(int x, int y, String sSprite, int cols, int rows) {
	super(x, y, sSprite, cols, rows);
	this.score = 0;
	this.life = 3;
    }

    public int getScore() {
	return score;
    }

    public void setScore(int score) {
	this.score = score;
    }

    public int getLife() {
	return life;
    }

    public void setLife(int life) {
	this.life = life;
    }

    public void takeLife() {
	if (life > 1) {
	    life--;
	} else {
	    life = 0;
	    setDead(true);
	}
    }

    public boolean isDead() {
	return dead;
    }

    public void setDead(boolean dead) {
	this.dead = dead;
    }

}
