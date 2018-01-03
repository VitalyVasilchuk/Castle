package edu.castle.actor.player;

import edu.castle.actor.Actor;
import edu.castle.actor.Fireball;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Player extends Actor {

    public final int LIFE_MAX = 5;
    public final int MANA_MAX = 100;

    private int startX;
    private int startY;
    private int life;
    private int mana;
    private int manaRegen;
    private ArrayList<Fireball> fireballs = new ArrayList();

    public Player(int x, int y, String sImage, int col, int row) {
	super(x, y, sImage, col, row);
	init();
    }

    private void init() {
	life = LIFE_MAX;
	mana = MANA_MAX;
	manaRegen = 0;
	startX = x;
	startY = y;
	
	setDirection(Actor.SpriteDirection.LEFT);
	setDelta(6);
    }

    public int getLife() {
	return life;
    }

    public void setLife(int life) {
	if (life > LIFE_MAX) {
	    this.life = LIFE_MAX;;
	}
	else {
	    this.life = life;
	}	
    }

    public void setMana(int mana) {
	if (mana > MANA_MAX) {
	    this.mana = MANA_MAX;
	}
	else {
	    this.mana = mana;
	}
    }

    public void giveLife() {
	if (life < LIFE_MAX) {
	    sound1Up();
	    life++;
	}
    }

    public void takeLife() {
	if (life > 0) {
	    sound1Down();
	    life--;
	}
    }

    public int getMana() {
	return mana;
    }
    
    public int getManaPercent() {
	return (int) mana*100 / MANA_MAX;
    }
    public void giveMana(int m) {
	if (mana < MANA_MAX) {
	    mana +=m ;
	}
    }

    public void takeMana(int m) {
	if (mana > 0) {
	    mana -= m;
	}
    }

    public ArrayList<Fireball> getFireballs() {
	return fireballs;
    }

    public void fire(int speed, int distance) {
	if (isVisible() && mana >= 10) {
	    takeMana(10);
	    Fireball fb = new Fireball(x, y + 10, speed, distance);
	    fb.setDirection(direction); // задаем направление полета шара по курсу игрока
	    fb.setMoving(true);
	    fireballs.add(fb);
	}
    }
    
    public void manaRegen(int m) {
	manaRegen++;
	if (manaRegen > 10) {
	    giveMana(m);
	    manaRegen = 0;
	}
    }
    
    public void restart() {
	setMoving(false);
	setFalling(false);
	x = startX;
	y = startY;
    }
    
    public void sound1Up() {
	try {
	    soundPlay("/res/sounds/heartbeat.wav");
	} catch (Exception ex) {
	    Logger.getLogger(Fireball.class.getName()).log(Level.SEVERE, null, ex);
	}
    }    

    public void sound1Down() {
	try {
	    soundPlay("/res/sounds/player_die.wav");
	} catch (Exception ex) {
	    Logger.getLogger(Fireball.class.getName()).log(Level.SEVERE, null, ex);
	}
    }    
    
    @Override
    public String toString() {
	return super.toString() + "\n"+
		"Player{" + "LIFE_MAX=" + LIFE_MAX + ", MANA_MAX=" + MANA_MAX + 
		", life=" + life + ", mana=" + mana + ", manaRegen=" + manaRegen +
		", h= "+getBounds().height+"}";
    }
    
    
}
