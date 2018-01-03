package edu.castle;

import java.awt.Rectangle;

public class ServiceRect {

    private Rectangle precipice;
    private Rectangle beam;
    private Rectangle wall;
    private Rectangle ladder;
    private Rectangle player1;
    private Rectangle player2;

    public ServiceRect() {
	this.precipice = new Rectangle();
	this.wall = new Rectangle();
	this.ladder = new Rectangle();
	this.player1 = new Rectangle();
	this.player2 = new Rectangle();
    }

    public Rectangle getPrecipice() {
	return precipice;
    }

    public void setPrecipice(Rectangle precipice) {
	this.precipice = precipice;
    }

    public Rectangle getWall() {
	return wall;
    }

    public void setWall(Rectangle wall) {
	this.wall = wall;
    }

    public Rectangle getLadder() {
	return ladder;
    }

    public void setLadder(Rectangle ladder) {
	this.ladder = ladder;
    }

    public Rectangle getPlayer1() {
	return player1;
    }

    public void setPlayer1(Rectangle player1) {
	this.player1 = player1;
    }

    public Rectangle getPlayer2() {
	return player2;
    }

    public void setPlayer2(Rectangle player2) {
	this.player2 = player2;
    }

    public Rectangle getBeam() {
	return beam;
    }

    public void setBeam(Rectangle beam) {
	this.beam = beam;
    }

}
