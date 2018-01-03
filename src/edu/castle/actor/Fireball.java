package edu.castle.actor;

import java.awt.Rectangle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Fireball extends Actor {

    private int distance;

    public Fireball(int x, int y, int speed, int distance) {
	super(x, y, "/res/images/fireball.png", 3, 4);
	this.distance = distance;
	this.delta = speed;
	this.canUp = true;
	this.canDown = true;
	init();
    }

    private void init() {
	setDirection(SpriteDirection.LEFT);
	setDelta(8);
	setMoving(true);
	try {
	    soundPlay("/res/sounds/fireball.wav");
	} catch (Exception ex) {
	    Logger.getLogger(Fireball.class.getName()).log(Level.SEVERE, null, ex);
	}

    }

    public void explosionWall() {
	try {
	    soundPlay("/res/sounds/explosion_wall.wav");
	} catch (Exception ex) {
	    Logger.getLogger(Fireball.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    public void explosionActor() {
	try {
	    soundPlay("/res/sounds/explosion_montser.wav");
	} catch (Exception ex) {
	    Logger.getLogger(Fireball.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    public void explosionBox() {
	try {
	    soundPlay("/res/sounds/explosion_box.wav");
	} catch (Exception ex) {
	    Logger.getLogger(Fireball.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    // переопределяя метод реализуем ограничение полета шара на заданную дистанцию
    @Override
    public void tick() {
	if (distance > 0) {
	    super.tick();
	    distance -= getDelta();
	} else {
	    setVisible(false);
	}
    }
}
