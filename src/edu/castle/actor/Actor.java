package edu.castle.actor;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Actor {

    public enum SpriteDirection {
	DOWN, LEFT, RIGHT, UP
    }

    protected SpriteDirection direction;
    protected Map<SpriteDirection, Image> standingImgMap = new EnumMap<>(SpriteDirection.class);
    protected Map<SpriteDirection, List<Image>> movingImgMap = new EnumMap<>(SpriteDirection.class);

    protected int x;
    protected int y;
    protected int delta = 4;
    protected int width;
    protected int height;
    protected int rows;
    protected int cols;
    protected String spriteSheetPath;

    protected boolean visible;
    protected boolean moving;
    protected boolean falling;

    protected boolean canUp;
    protected boolean canDown;
    protected boolean canSoar;

    protected int maxMovingIndex = 4;
    protected int movingIndex = 0;

    public Actor(int x, int y, String sSprite, int cols, int rows) {
	this.x = x;
	this.y = y;
	this.rows = rows;
	this.cols = cols;
	this.spriteSheetPath = sSprite;
	this.direction = SpriteDirection.RIGHT;
	this.canUp = false;
	this.canDown = false;
	this.canSoar = false;
	this.moving = false;
	this.visible = true;
	this.falling = false;

	try {
	    createSprites();
	} catch (IOException ex) {
	    Logger.getLogger(Actor.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    private void createSprites() throws IOException {
	BufferedImage img;
	if (spriteSheetPath.startsWith("http")) {
	    URL spriteSheetUrl = new URL(spriteSheetPath);
	    img = ImageIO.read(spriteSheetUrl);
	} else {
	    //img = ImageIO.read(new File(spriteSheetPath));
	    img = ImageIO.read(getClass().getResource(spriteSheetPath));
	}

	int x0 = 0;
	int y0 = 0;
	int rW = img.getWidth() / cols;
	int rH = img.getHeight() / rows;
	maxMovingIndex = cols - 1;

	for (int row = 0; row < rows; row++) {
	    SpriteDirection dir = SpriteDirection.values()[row];
	    List<Image> imgList = new ArrayList<>();
	    movingImgMap.put(dir, imgList);
	    int rY = y0 + row * rH;
	    for (int col = 0; col < cols; col++) {
		int rX = x0 + col * rW;
		BufferedImage subImg = img.getSubimage(rX, rY, rW, rH);
		//System.out.println(subImg.getWidth() + "; " + subImg.getHeight());
		if (col == 0) {
		    // first image is standing
		    standingImgMap.put(dir, subImg);
		} else {
		    // all others are moving
		    imgList.add(subImg);
		}
	    }
	}
    }

    public void draw(Graphics g) {
	Image img = getImage();
	g.drawImage(img, x, y, null);
    }

    public Image getImage() {
	Image img;
	if (!moving) {
	    img = standingImgMap.get(direction);
	} else {
	    img = movingImgMap.get(direction).get(movingIndex);
	}

	width = img.getWidth(null);
	height = img.getHeight(null);
	return img;
    }

    public Rectangle getBounds() {
	return new Rectangle(x, y, width, height);
    }

    public SpriteDirection getDirection() {
	return direction;
    }

    public void setDirection(SpriteDirection direction) {
	if (this.direction != direction) {
	    setMoving(false);
	}
	this.direction = direction;
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

    public boolean isMoving() {
	return moving;
    }

    public void setMoving(boolean moving) {
	this.moving = moving;
	if (!moving) {
	    movingIndex = 0;
	}
    }

    public boolean isFalling() {
	return falling;
    }

    public void setFalling(boolean falling) {
	this.falling = falling;
	if (falling) {
	    setDirection(SpriteDirection.DOWN);
	} else {
	    setMoving(false);
	}
    }

    public boolean isCanUp() {
	return canUp;
    }

    public void setCanUp(boolean canUp) {
	this.canUp = canUp;
    }

    public boolean isCanDown() {
	return canDown;
    }

    public void setCanDown(boolean canDown) {
	this.canDown = canDown;
    }

    public boolean isCanSoar() {
	return canSoar;
    }

    public void setCanSoar(boolean canSoar) {
	this.canSoar = canSoar;
    }

    // смещает объект в пространстве, устанавливает следующий кадр
    public void tick() {
	if (falling) {
	    direction = SpriteDirection.DOWN;
	}

	if (moving || falling) {
	    switch (direction) {
		case RIGHT:
		    x += delta;
		    break;
		case LEFT:
		    x -= delta;
		    break;
		case DOWN:
		    if (canDown || falling) {
			y += delta;
		    }
		    break;
		case UP:
		    if (canUp) {
			y -= delta;
		    }
		    break;
	    }
	    movingIndex++;
	    movingIndex %= maxMovingIndex;
	}
    }

    public boolean isCollision(Rectangle rArea) {
	Rectangle rThis = this.getBounds();
	/**/
	// проверка препятсвия при следующем перемещении объекта	
	switch (this.getDirection()) {
	    case LEFT:
		rThis.x -= delta;
		break;
	    case RIGHT:
		rThis.x += delta;
		break;
	    case UP:
		rThis.y -= delta;
		break;
	    case DOWN:
		rThis.y += delta;
		break;
	}
	/**/
	return rArea.intersects(rThis);
    }

    public int getMovingIndex() {
	return movingIndex;
    }

    public void setMovingIndex(int movingIndex) {
	this.movingIndex = movingIndex;
    }

    public int getDelta() {
	return delta;
    }

    public void setDelta(int delta) {
	this.delta = delta;
    }

    public void turn() {
	switch (direction) {
	    case RIGHT:
		direction = SpriteDirection.LEFT;
		break;
	    case LEFT:
		direction = SpriteDirection.RIGHT;
		break;
	    case DOWN:
		direction = SpriteDirection.UP;
		break;
	    case UP:
		direction = SpriteDirection.DOWN;
		break;
	}
    }

    public void back() {
	switch (direction) {
	    case RIGHT:
		x += -delta;
		break;
	    case LEFT:
		x -= -delta;
		break;
	    case DOWN:
		y += -delta;
		break;
	    case UP:
		y += -delta;
		break;
	}
    }

    public void soundPlay(String sURL) throws Exception {
	URL file = getClass().getResource(sURL);

	Clip clip = AudioSystem.getClip();
	AudioInputStream ais = AudioSystem.getAudioInputStream(file);
	clip.open(ais);
	clip.loop(0);
    }

    @Override
    public String toString() {
	return "Actor{" + "direction=" + direction + ", x=" + x + ", y=" + y
		+ ", delta=" + delta + ", width=" + width + ", height=" + height
		+ ", moving=" + moving + ", falling=" + falling
		+ ", canSoar=" + canSoar + ", canUp=" + canUp + ", canDown=" + canDown + "}";
    }

}
