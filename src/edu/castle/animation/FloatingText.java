package edu.castle.animation;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class FloatingText {

    private int x;
    private int y;
    private String text;
    private Color color;
    private Font font;
    private boolean visible;

    private int stepX;
    private int stepY;
    private int counter;

    public FloatingText(String text, int x, int y) {
	this.x = x;
	this.y = y;
	this.text = text;
	this.color = Color.yellow;
	this.font = new Font("basilisk", Font.BOLD, 12);
	this.visible = true;
	this.stepX = 0;
	this.stepY = -1;
	this.counter = 24;
    }

    public void draw(Graphics g) {
	if(counter > 0) {
	    g.setColor(color);
	    g.setFont(font);
	    g.drawString(text, x, y);

	    x += stepX;
	    y += stepY;
	    counter--;
	}
	else {
	    setVisible(false);
	}
    }

    public int getX() {
	return x;
    }

    public void setX(int x) {
	this.x = x;
    }

    public String getText() {
	return text;
    }

    public int getY() {
	return y;
    }

    public void setY(int y) {
	this.y = y;
    }

    public void setText(String text) {
	this.text = text;
    }

    public Color getColor() {
	return color;
    }

    public void setColor(Color color) {
	this.color = color;
    }

    public Font getFont() {
	return font;
    }

    public void setFont(Font font) {
	this.font = font;
    }

    public boolean isVisible() {
	return visible;
    }

    public void setVisible(boolean visible) {
	this.visible = visible;
    }

    public int getStep() {
	return stepX;
    }

    public void setStep(int step) {
	this.stepX = step;
    }

    public int getStepX() {
	return stepX;
    }

    public void setStepX(int stepX) {
	this.stepX = stepX;
    }

    public int getStepY() {
	return stepY;
    }

    public void setStepY(int stepY) {
	this.stepY = stepY;
    }

    public int getCounter() {
	return counter;
    }

    public void setCounter(int counter) {
	this.counter = counter;
    }
    
}
