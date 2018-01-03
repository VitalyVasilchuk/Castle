package edu.castle.actor.monster;

import edu.castle.actor.Actor;
import java.awt.Color;
import java.awt.Graphics;

public class Monster extends Actor {

    private int score;	    // размер бонуса, выпадающего в виде монеты
    private int life;	    // количество жизней
    private boolean dead;   // признак, что жизни закончились
    private int showHealth; // количество тиков, в течении которых отрисовывается индикатор здоровья

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
	    showHealth = 10;
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
    
    // переопределяем, чтобы отрисовать индикатор здоровья
    @Override
    public void draw(Graphics g) {
	super.draw(g);
	if (showHealth > 0) {
	    g.setColor(Color.red);
	    for (int i = 0; i < getLife(); i++) {
		g.fillRect(x+i*3+i*2, y, 3, 3);
	    }
	    showHealth--;
	}
    }

}
