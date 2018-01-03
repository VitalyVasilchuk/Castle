package edu.castle.sprite;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.net.URL;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;

public class Sprite {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected boolean visible;
    
    protected Image image;
    protected ArrayList<Image> images = new ArrayList<>();
    protected int stage;

    public Sprite(int x, int y) {
	this.x = x;
	this.y = y;
	visible = true;
    }
    
    public void draw(Graphics g) {
	//img = stageImgMap.get(stage).get(stageIndex);
	width = image.getWidth(null);
	height = image.getHeight(null);

	g.drawImage(image, x, y, null);
    }
    protected void getImageDimensions() {
	width = image.getWidth(null);
	height = image.getHeight(null);
    }

    protected void loadImage(String imageName) {
	image = new ImageIcon(getClass().getResource(imageName)).getImage();
	getImageDimensions();
	images.add(image);
    }

    public Image getImage() {
	return image;
    }

    public void setImage(int index) {
	image = images.get(index);
    }

    public int getX() {
	return x;
    }

    public int getY() {
	return y;
    }

    public boolean isVisible() {
	return visible;
    }

    public void setVisible(Boolean visible) {
	this.visible = visible;
    }

    public Rectangle getBounds() {
	return new Rectangle(x, y, width, height);
    }

    public int getStage() {
	return stage;
    }

    public void setStage(int stage) {
	if (stage >= 0 && stage < images.size()) {
	    this.stage = stage;
	    setImage(stage);
	}
    }
    
    public void soundPlay(String sURL) throws Exception {
	URL file = getClass().getResource(sURL);

	Clip clip = AudioSystem.getClip();
	AudioInputStream ais = AudioSystem.getAudioInputStream(file);
	clip.open(ais);
	clip.loop(0);
    }
  
}
