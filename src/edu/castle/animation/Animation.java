package edu.castle.animation;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Animation {

    private int x;
    private int y;
    protected int width;
    protected int height;
    private int loop;
    
    private String spriteSheetPath;
    private int cols;
    private int rows;
    private int maxMovingIndex;
    private List<Image> imgList;
    private boolean visible;
    private int movingIndex;

    public Animation(int x, int y, String spriteSheetPath, int cols, int rows) {
	
	this.x = x;
	this.y = y;
	this.spriteSheetPath = spriteSheetPath;
	this.cols = cols;
	this.rows = rows;
	this.maxMovingIndex = 0;
	this.movingIndex = 0;
	this.visible = true;
	this.imgList = new ArrayList<>();
	this.loop = 1;
	try {    
	    createSprites();
	} catch (IOException ex) {
	    Logger.getLogger(Animation.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    private void createSprites() throws IOException {
	BufferedImage img;
	if (spriteSheetPath.startsWith("http")) {
	    URL spriteSheetUrl = new URL(spriteSheetPath);
	    img = ImageIO.read(spriteSheetUrl);
	} else {
	    img = ImageIO.read(getClass().getResource(spriteSheetPath));
	}

	int x0 = 0;
	int y0 = 0;
	int rW = img.getWidth() / cols;
	int rH = img.getHeight() / rows;

	for (int row = 0; row < rows; row++) {
	    int rY = y0 + row * rH;
	    for (int col = 0; col < cols; col++) {
		int rX = x0 + col * rW;
		BufferedImage subImg = img.getSubimage(rX, rY, rW, rH);
		imgList.add(subImg);
		maxMovingIndex++;
	    }
	}
    }
    
    public Image getImage() {
	Image image;
	if (movingIndex == maxMovingIndex-1) {
	    loop--;
	    if (loop == 0) {
	       setVisible(false);
	    }
	}
	movingIndex %= maxMovingIndex;
	
	image = imgList.get(movingIndex++);
	
	width = image.getWidth(null);
	height = image.getHeight(null);
	
	return image;
    }
    
    public void draw(Graphics g) {
	Image img = getImage();
	g.drawImage(img, x, y, null);
    } 
    
    public void soundPlay(String sURL) throws Exception {
	URL file = getClass().getResource(sURL);

	Clip clip = AudioSystem.getClip();
	AudioInputStream ais = AudioSystem.getAudioInputStream(file);
	clip.open(ais);
	clip.loop(0);
    }    

    public int getX() {
	return x;
    }

    public void setX(int x) {
	this.x = x;
    }

    public int getY() {
	return y;
    }

    public void setY(int y) {
	this.y = y;
    }

    public boolean isVisible() {
	return visible;
    }

    public void setVisible(boolean visible) {
	this.visible = visible;
    }

    public int getLoop() {
	return loop;
    }

    public void setLoop(int loop) {
	this.loop = loop;
    }
    
    public Rectangle getBounds() {
	return new Rectangle(x, y, width, height);
    }    
}
