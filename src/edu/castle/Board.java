package edu.castle;

import edu.castle.actor.Fireball;
import edu.castle.actor.player.Player1;
import edu.castle.actor.player.Player2;
import edu.castle.actor.monster.Anaconda;
import edu.castle.actor.monster.Death;
import edu.castle.actor.monster.Ghost;
import edu.castle.actor.monster.Monk;
import edu.castle.actor.monster.Monster;
import edu.castle.actor.monster.Spider;
import edu.castle.actor.monster.Witch;
import edu.castle.animation.Animation;
import edu.castle.animation.Coin;
import edu.castle.animation.Explosion;
import edu.castle.animation.FloatingText;
import edu.castle.sprite.Bag;
import edu.castle.sprite.Beam;
import edu.castle.sprite.Blank;
import edu.castle.sprite.Ladder;
import edu.castle.sprite.LifeBlue;
import edu.castle.sprite.LifeRed;
import edu.castle.sprite.Sprite;
import edu.castle.sprite.Wall;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

    private Timer timer;

    private final int DELAY = 75;
    private final int BLOCK_SIZE = 48;
    private final int SPACE = 0;//32 + 4 + 16;
    private int boardWidth;
    private int boardHeight;
    private int score = 0;

    private Level level;
    private char[][] levelMap;
    private int levelCols;
    private int levelRows;

    private Image imgHRed;
    private Image imgHBlack;
    private Image imgHBlue;
    private Image imgScore;

    // объекты персонажей
    private Player1 player1;
    private Player2 player2;

    // списки движущихся объектов
    private ArrayList monsters = new ArrayList();
    private ArrayList anacondas = new ArrayList();
    private ArrayList deaths = new ArrayList();
    private ArrayList ghosts = new ArrayList();
    private ArrayList monks = new ArrayList();
    private ArrayList spiders = new ArrayList();
    private ArrayList witchs = new ArrayList();

    // списки неподвижных объектов;
    private ArrayList fixed = new ArrayList();
    private ArrayList ladders = new ArrayList();
    private ArrayList blanks = new ArrayList();
    private ArrayList beams = new ArrayList();
    private ArrayList precipices = new ArrayList();
    private ArrayList walls = new ArrayList();

    // списки предметов
    private ArrayList bags = new ArrayList();
    private ArrayList livesRed = new ArrayList();
    private ArrayList livesBlue = new ArrayList();

    private ArrayList floatingTexts = new ArrayList();

    private JProgressBar pbMana1 = new JProgressBar();
    private JProgressBar pbMana2 = new JProgressBar();

    private boolean serviceMode;
    private ServiceRect service;

    private ArrayList explosions = new ArrayList();
    private ArrayList coins = new ArrayList();

    public Board() {
	serviceMode = false;
	initBoard();
    }

    private void initBoard() {
	service = new ServiceRect();
	addKeyListener(new TAdapter());

	setFocusable(true);
	setBackground(new Color(48, 48, 48));

	loadImages();

	initLevel();
	initObjectArray();
	initProgressBars();

	// формируем общий список "живых" объектов
	monsters.addAll(anacondas);
	monsters.addAll(deaths);
	monsters.addAll(ghosts);
	monsters.addAll(monks);
	monsters.addAll(spiders);
	monsters.addAll(witchs);

	// формируем общий список для фиксированых объектов,
	// пустые блоки должны обрабатываться в первую очередь
	fixed.addAll(blanks);
	fixed.addAll(beams);
	fixed.addAll(ladders);
	fixed.addAll(walls);
	fixed.addAll(bags);
	fixed.addAll(livesBlue);
	fixed.addAll(livesRed);

	setPreferredSize(new Dimension(boardWidth, boardHeight));

	timer = new Timer(DELAY, this);
	timer.start();
    }

    private void loadImages() {
	imgHRed = new ImageIcon(getClass().getResource("/res/images/heart_red.png")).getImage();
	imgHBlue = new ImageIcon(getClass().getResource("/res/images/heart_blue.png")).getImage();
	imgHBlack = new ImageIcon(getClass().getResource("/res/images/heart_black.png")).getImage();
	imgScore = new ImageIcon(getClass().getResource("/res/images/score.png")).getImage();
    }

    private void initLevel() {
	level = new Level(2);
	System.out.println(level.toString());
	levelMap = level.getLevelData();
	levelCols = level.getCols();
	levelRows = level.getRows();

	boardWidth = levelCols * BLOCK_SIZE + SPACE;
	boardHeight = levelRows * BLOCK_SIZE;
    }

    /* инициализация массива начальных координат объектов на уровне */
    private void initObjectArray() {
	int x = 0;
	int y = SPACE;

	for (char[] aRow : levelMap) {
	    for (char iBlock : aRow) {
		// все что не содержит блок стены, должно быть представлено пустым блок
		if (iBlock != '#') {
		    blanks.add(new Blank(x, y));
		}

		switch (iBlock) {
		    case '-':
			beams.add(new Beam(x, y));
			break;

		    case ' ':
			Blank blank = new Blank(x, y);
			if (levelMap[(y - SPACE) / BLOCK_SIZE - 1][x / BLOCK_SIZE] == ' ') {
			    precipices.add(blank);
			} else {
			    blanks.add(blank);
			}
			break;

		    case 'H':
			Ladder ladder = new Ladder(x, y);
			if (levelMap[(y - SPACE) / BLOCK_SIZE - 1][x / BLOCK_SIZE] != 'H') {
			    ladder.setImage(0);
			}
			ladders.add(ladder);
			break;

		    case '#':
			walls.add(new Wall(x, y));
			break;

		    case '$':
			bags.add(new Bag(x, y));
			break;

		    case 'A':
			Anaconda a = new Anaconda(x, y);
			a.setMoving(true);
			anacondas.add(a);
			break;

		    case 'B':
			livesBlue.add(new LifeBlue(x, y));
			break;

		    case 'R':
			livesRed.add(new LifeRed(x, y));
			break;

		    case 'D':
			Death d = new Death(x, y);
			d.setMoving(true);
			deaths.add(d);
			break;

		    case '2':
			player2 = new Player2(x, y);
			break;

		    case 'G':
			Ghost g = new Ghost(x, y);
			g.setMoving(true);
			ghosts.add(g);
			break;

		    case 'M':
			Monk m = new Monk(x, y);
			m.setMoving(true);
			monks.add(m);
			break;

		    case 'S':
			Spider s = new Spider(x, y);
			s.setMoving(true);
			spiders.add(s);
			break;

		    case 'W':
			Witch w = new Witch(x, y);
			w.setMoving(true);
			witchs.add(w);
			break;

		    case '1':
			player1 = new Player1(x, y);
			break;
		}
		x += BLOCK_SIZE;
	    }
	    x = 0;
	    y += BLOCK_SIZE;
	}
    }

    private void initProgressBars() {
	pbMana1.setValue(100);
	pbMana1.setStringPainted(false);
	pbMana1.setBorderPainted(false);
	pbMana1.setBackground(new Color(139, 174, 197));
	pbMana1.setForeground(new Color(22, 119, 184));
	pbMana1.setPreferredSize(new Dimension(32 * 5, 8));
	add(pbMana1);

	pbMana2.setValue(100);
	pbMana2.setStringPainted(false);
	pbMana2.setBorderPainted(false);
	pbMana2.setBackground(new Color(190, 112, 112));
	pbMana2.setForeground(new Color(170, 0, 0));
	pbMana2.setPreferredSize(new Dimension(32 * 5, 8));
	if (player2 != null) {
	    add(pbMana2);
	}
    }

    @Override
    protected void paintComponent(Graphics g) {
	super.paintComponent(g);

	drawObjects(g);

	Toolkit.getDefaultToolkit().sync();
    }

    private void drawObjects(Graphics g) {
	drawFixedObjects(g);

	drawActors(g);

	drawFireballs(g);

	drawAnimations(g, explosions);
	drawAnimations(g, coins);

	drawLives(g);
	drawScore(g);
	drawFloatingText(g);

	drawServiceRect(g);
    }

    // рендеринг неподвижных объектов
    private void drawFixedObjects(Graphics g) {
	for (int i = 0; i < fixed.size(); i++) {
	    Sprite sprite = (Sprite) fixed.get(i);
	    if (sprite.isVisible()) {
		sprite.draw(g);
	    }
	}
    }

    // отрисовываем движущихся персонажей
    private void drawActors(Graphics g) {
	for (int i = 0; i < monsters.size(); i++) {
	    Monster monster = (Monster) monsters.get(i);
	    if (monster.isVisible()) {
		monster.draw(g);
	    }
	}

	if (player1.isVisible()) {
	    player1.draw(g);
	}

	if (player2 != null && player2.isVisible()) {
	    player2.draw(g);
	}
    }

    private void drawFireballs(Graphics g) {
	ArrayList<Fireball> fireBalls = new ArrayList();
	fireBalls.addAll(player1.getFireballs());
	if (player2 != null) {
	    fireBalls.addAll(player2.getFireballs());
	}

	for (int i = 0; i < fireBalls.size(); i++) {
	    Fireball fb = fireBalls.get(i);
	    if (fb.isVisible()) {
		fb.draw(g);
	    }
	}
    }

    // отрисовываем сердечки жизни героя и помощника
    private void drawLives(Graphics g) {
	for (int i = player1.LIFE_MAX; i > 0; i--) {
	    if (player1.getLife() < i) {
		g.drawImage(imgHBlack, this.getWidth() - i * imgHBlack.getWidth(this) - 4, 0, this);
	    } else {
		g.drawImage(imgHBlue, this.getWidth() - i * imgHBlue.getWidth(this) - 4, 0, this);
	    }
	}

	if (player2 != null) {
	    for (int i = 0; i < player2.LIFE_MAX; i++) {
		if (player2.getLife() <= i) {
		    g.drawImage(imgHBlack, i * imgHBlack.getWidth(this) + 4, 0, this);
		} else {
		    g.drawImage(imgHRed, i * imgHRed.getWidth(this) + 4, 0, this);
		}
	    }
	}
    }

    private void drawAnimations(Graphics g, ArrayList animations) {
	for (int i = 0; i < animations.size(); i++) {
	    Animation animation = (Animation) animations.get(i);
	    if (animation.isVisible()) {
		animation.draw(g);
	    }
	}
    }

    private void drawScore(Graphics g) {
	g.drawImage(imgScore, getWidth() / 2 - imgScore.getWidth(this) / 2, 8, this);
	String msg = "" + score;
	Font fScore = new Font("Tahoma", Font.ITALIC, 24);
	FontMetrics metr = getFontMetrics(fScore);
	g.setColor(Color.yellow);
	g.setFont(fScore);
	g.drawString(msg, (this.getWidth() - metr.stringWidth(msg)) / 2, 33);
    }

    private void drawFloatingText(Graphics g) {
	for (int i = 0; i < floatingTexts.size(); i++) {
	    FloatingText ft = (FloatingText) floatingTexts.get(i);
	    if (ft.isVisible()) {
		ft.draw(g);
	    }
	}
    }

    private void drawServiceRect(Graphics g) {
	if (serviceMode) {
	    Rectangle p = service.getPrecipice();
	    if (p != null) {
		g.setColor(Color.green);
		g.drawRect(p.x, p.y, p.width - 1, p.height - 1);
	    }

	    Rectangle b = service.getBeam();
	    if (b != null) {
		g.setColor(Color.green);
		g.drawRect(b.x, b.y, b.width - 1, b.height - 1);
	    }

	    Rectangle w = service.getWall();
	    if (w != null) {
		g.setColor(Color.green);
		g.drawRect(w.x, w.y, w.width - 1, w.height - 1);
	    }

	    Rectangle l = service.getLadder();
	    if (l != null) {
		g.setColor(Color.green);
		g.drawRect(l.x, l.y, l.width - 1, l.height - 1);
	    }

	    Rectangle rPlayer1 = service.getPlayer1();
	    g.setColor(Color.blue);
	    g.drawRect(rPlayer1.x, rPlayer1.y, rPlayer1.width - 1, rPlayer1.height - 1);

	    Rectangle rPlayer2 = service.getPlayer2();
	    g.setColor(Color.red);
	    g.drawRect(rPlayer2.x, rPlayer2.y, rPlayer2.width - 1, rPlayer2.height - 1);
	}
    }

    private void updateActors() {
	for (int i = 0; i < monsters.size(); i++) {
	    Monster monster = (Monster) monsters.get(i);
	    if (monster.isDead()) {
		coins.add(new Coin(monster.getX(), monster.getY(), monster.getScore()));
		monsters.remove(i);
	    } else {
		monster.tick();
	    }
	}

	if (player1.isMoving() || player1.isFalling()) {
	    player1.tick();
	}

	if (player2 != null && (player2.isMoving() || player2.isFalling())) {
	    player2.tick();
	}

    }

    private void updateBags() {
	for (int i = 0; i < bags.size(); i++) {
	    Bag bag = (Bag) bags.get(i);
	    if (bag.isVisible()) {
	    } else {
		bags.remove(i);
	    }
	}
    }

    private void updateLivesRed() {
	for (int i = 0; i < livesRed.size(); i++) {
	    LifeRed lr = (LifeRed) livesRed.get(i);
	    if (lr.isVisible()) {
	    } else {
		livesRed.remove(i);
	    }
	}
    }

    private void updateLivesBlue() {
	for (int i = 0; i < livesBlue.size(); i++) {
	    LifeBlue lb = (LifeBlue) livesBlue.get(i);
	    if (lb.isVisible()) {
	    } else {
		livesBlue.remove(i);
	    }
	}
    }

    private void updateAnimations(ArrayList animations) {
	for (int i = 0; i < animations.size(); i++) {
	    Animation animation = (Animation) animations.get(i);
	    if (animation.isVisible()) {
	    } else {
		animations.remove(i);
	    }
	}
    }

    private void updateFireballs() {
	ArrayList<Fireball> fireBalls;
	fireBalls = player1.getFireballs();

	for (int i = 0; i < fireBalls.size(); i++) {
	    Fireball fb = fireBalls.get(i);
	    if (fb.isVisible()) {
		fb.tick();
	    } else {
		fireBalls.remove(i);
	    }
	}

	if (player2 != null) {
	    fireBalls = player2.getFireballs();
	    for (int i = 0; i < fireBalls.size(); i++) {
		Fireball fb = fireBalls.get(i);
		if (fb.isVisible()) {
		    fb.tick();
		} else {
		    fireBalls.remove(i);
		}
	    }
	}
    }

    private void updateProgressBar() {
	pbMana1.setBounds(this.getWidth() - 32 * 5 - 4, 32 + 4, 32 * 5, 8);
	pbMana1.setValue(player1.getManaPercent());

	if (player2 != null) {
	    pbMana2.setBounds(4, 32 + 4, 32 * 5, 8);
	    pbMana2.setValue(player2.getManaPercent());
	}
    }

    private void updateFloatingText() {
	for (int i = 0; i < floatingTexts.size(); i++) {
	    FloatingText ft = (FloatingText) floatingTexts.get(i);
	    if (!ft.isVisible()) {
		floatingTexts.remove(i);
	    }
	}
    }

    private void checkCollisionsFireball() {
	ArrayList<Fireball> fireBalls = new ArrayList();
	fireBalls.addAll(player1.getFireballs());
	if (player2 != null) {
	    fireBalls.addAll(player2.getFireballs());
	}

	// движемся по списку шаров обоих игроков
	for (int i = 0; i < fireBalls.size(); i++) {
	    Fireball fb = fireBalls.get(i);
	    Rectangle rFireball = fb.getBounds(); // весь контур шара;
	    rFireball.x += 24;
	    rFireball.width = 1;

	    // пересечение с монстрами
	    for (int j = 0; j < monsters.size(); j++) {
		Monster monster = (Monster) monsters.get(j);
		Rectangle rActor = monster.getBounds();
		if (rActor.contains(rFireball)) {
		    monster.takeLife();

		    fb.explosionActor();
		    fb.setVisible(false);

		    explosions.add(new Explosion(monster.getX(), monster.getY()));
		}
	    }

	    // пересечение со стеновым блоком
	    for (int j = 0; j < walls.size(); j++) {
		Wall w = (Wall) walls.get(j);
		Rectangle rWall = w.getBounds();
		//if (fb.isCollision(rWall)) {
		if (rWall.intersects(fb.getBounds())) {
		    fb.explosionWall();
		    fb.setVisible(false);
		}
	    }

	    // проверка пересечения с мешками золота в состоянии "в ящике"
	    for (int j = 0; j < bags.size(); j++) {
		Bag b = (Bag) bags.get(j);
		Rectangle rBag = b.getBounds();
		if (b.getStage() == 0 && rBag.contains(rFireball)) {
		    fb.explosionBox();
		    fb.setVisible(false);
		    b.setStage(1);
		    explosions.add(new Explosion(b.getX(), b.getY()));
		}
	    }

	    // проверка пересечения с красной жизнью в состоянии "в ящике"
	    for (int j = 0; j < livesRed.size(); j++) {
		LifeRed lr = (LifeRed) livesRed.get(j);
		Rectangle rLife = lr.getBounds();
		if (lr.getStage() == 0 && rLife.contains(rFireball)) {
		    fb.explosionBox();
		    fb.setVisible(false);
		    lr.setStage(1);
		    explosions.add(new Explosion(lr.getX(), lr.getY()));
		}
	    }

	    // проверка пересечения с синей жизнью в состоянии "в ящике"
	    for (int j = 0; j < livesBlue.size(); j++) {
		LifeBlue lb = (LifeBlue) livesBlue.get(j);
		Rectangle bLife = lb.getBounds();
		if (lb.getStage() == 0 && bLife.contains(rFireball)) {
		    fb.explosionBox();
		    fb.setVisible(false);
		    lb.setStage(1);
		    explosions.add(new Explosion(lb.getX(), lb.getY()));
		}
	    }
	}
    }

    // проверка пересечения с пропастью
    private void checkCollisionsPrecipice() {
	for (int j = 0; j < precipices.size(); j++) {
	    Blank precipice = (Blank) precipices.get(j);
	    Rectangle rPrecipice = precipice.getBounds();
	    rPrecipice.y -= BLOCK_SIZE;

	    // поведение монстров
	    for (int i = 0; i < monsters.size(); i++) {
		Monster monster = (Monster) monsters.get(i);
		if (!monster.isCanSoar()) {
		    if (monster.isCollision(rPrecipice)) {
			monster.turn();
			monster.setDelta((int) (Math.random() * 3 + 1));
		    }
		}
	    }

	    if (rPrecipice.contains(player1.getBounds())) {
		service.setPrecipice(rPrecipice);
		service.setPlayer1(player1.getBounds());
		player1.setFalling(true);
	    }

	    if (player2 != null && rPrecipice.contains(player2.getBounds())) {
		service.setPlayer2(player2.getBounds());
		player2.setFalling(true);
	    }
	}
    }

    // проверка пересечения с лестницей
    private void checkCollisionsLadder() {
	player1.setCanUp(false);
	player1.setCanDown(false);
	if (player2 != null) {
	    player2.setCanUp(false);
	    player2.setCanDown(false);
	}

	for (int j = 0; j < ladders.size(); j++) {
	    Ladder ladder = (Ladder) ladders.get(j);
	    Rectangle rLadder = ladder.getBounds();

	    // определяем область для текущей ячейки
	    Rectangle rAreaTop = player1.getBounds();
	    rAreaTop.y += (rAreaTop.height - 1);
	    rAreaTop.height = 1;
	    if (rLadder.contains(rAreaTop)) {
		service.setLadder(rLadder);
		service.setPlayer1(rAreaTop);
		player1.setCanUp(true);
	    }

	    // определяем область для ячейки ниже
	    Rectangle rAreaBottom = player1.getBounds();
	    rAreaBottom.y += (rAreaBottom.height);
	    rAreaBottom.height = 1;
	    if (rLadder.contains(rAreaBottom)) {
		service.setLadder(rLadder);
		service.setPlayer1(rAreaBottom);
		player1.setCanDown(true);
		if (player1.isFalling()) {
		    player1.setFalling(false);
		}
	    }

	    if (player2 != null) {
		// определяем область для текущей ячейки
		rAreaTop = player2.getBounds();
		rAreaTop.y += (rAreaTop.height - 1);
		rAreaTop.height = 1;
		if (rLadder.contains(rAreaTop)) {
		    player2.setCanUp(true);
		}

		// определяем область для ячейки ниже
		rAreaBottom = player2.getBounds();
		rAreaBottom.y += (rAreaBottom.height);
		rAreaBottom.height = 1;
		if (rLadder.contains(rAreaBottom)) {
		    player2.setCanDown(true);
		    if (player2.isFalling()) {
			player2.setFalling(false);
		    }
		}
	    }
	}
    }

    // проверка пересечения с полкой
    private void checkCollisionsBeam() {
	for (int j = 0; j < beams.size(); j++) {
	    Beam beam = (Beam) beams.get(j);
	    Rectangle rBeam = beam.getBounds();

	    // определяем область для текущей ячейки
	    Rectangle rAreaTop = player1.getBounds();
	    rAreaTop.y += (rAreaTop.height - 1);
	    rAreaTop.height = 1;
	    if (rBeam.contains(rAreaTop)) {
		service.setBeam(rBeam);
		service.setPlayer1(rAreaTop);
		player1.setFalling(true);
	    }

	    // определяем область для ячейки ниже
	    Rectangle rAreaBottom = player1.getBounds();
	    rAreaBottom.y += (rAreaBottom.height);
	    rAreaBottom.height = 1;
	    if (rBeam.contains(rAreaBottom)) {
		service.setBeam(rBeam);
		service.setPlayer1(rAreaBottom);
		player1.setCanDown(true);
	    }

	    if (player2 != null) {
		// определяем область для текущей ячейки
		rAreaTop = player2.getBounds();
		rAreaTop.y += (rAreaTop.height - 1);
		rAreaTop.height = 1;
		if (rBeam.contains(rAreaTop)) {
		    player2.setFalling(true);
		}

		// определяем область для ячейки ниже
		rAreaBottom = player2.getBounds();
		rAreaBottom.y += (rAreaBottom.height);
		rAreaBottom.height = 1;
		if (rBeam.contains(rAreaBottom)) {
		    player2.setCanDown(true);
		}
	    }
	}
    }

    private void checkCollisions() {
	for (int j = 0; j < walls.size(); j++) {
	    Wall w = (Wall) walls.get(j);
	    Rectangle rWall = w.getBounds();

	    // столконовения игрока1 со стеной
	    if (player1.isCollision(rWall)) {
		service.setWall(rWall);
		service.setPlayer1(player1.getBounds());
		player1.setFalling(false);
		player1.setMoving(false);
	    }

	    // столконовения игрока2 со стеной
	    if (player2 != null && player2.isCollision(rWall)) {
		player2.setFalling(false);
		player2.setMoving(false);
	    }

	    for (int i = 0; i < monsters.size(); i++) {
		Monster monster = (Monster) monsters.get(i);
		// проверка столкновения движущихся объектов со стенами
		if (monster.isCollision(rWall)) {
		    //actor.back();
		    monster.turn();
		    monster.setDelta((int) (Math.random() * 3 + 1));
		}

		// проверка столкновения движущихся объектов с игроками
		Rectangle rp1 = player1.getBounds();
		if (monster.isCollision(rp1)) {
		    player1.setMoving(false);
		    player1.takeLife();
		    player1.restart();
		}

		if (player2 != null) {
		    Rectangle rp2 = player2.getBounds();
		    if (monster.isCollision(rp2)) {
			player2.setMoving(false);
			player2.takeLife();
			player2.restart();
		    }
		}
	    }
	}

	// проверка пересечения с мешком сокровищей
	for (int j = 0; j < bags.size(); j++) {
	    Bag bag = (Bag) bags.get(j);
	    // если "ящик" разбит, то вещь можно взять
	    if (bag.getStage() == 1) {
		Rectangle rBag = bag.getBounds();
		rBag.x += rBag.width / 2;
		rBag.width = 1;
		if (player1.isCollision(rBag) || (player2 != null && player2.isCollision(rBag))) {
		    bag.sound();
		    bag.setVisible(false);
		    score += 100;
		    floatingTexts.add(new FloatingText("+100", rBag.x, rBag.y));
		}
	    }
	}

	// проверка пересечения монетой
	for (int j = 0; j < coins.size(); j++) {
	    Coin coin = (Coin) coins.get(j);
	    Rectangle rCoin = coin.getBounds();
	    rCoin.x += rCoin.width / 2;
	    rCoin.width = 1;
	    if (player1.isCollision(rCoin) || (player2 != null && player2.isCollision(rCoin))) {
		coin.sound();
		coin.setVisible(false);
		score += coin.getScore();
		floatingTexts.add(new FloatingText("+" + coin.getScore(), rCoin.x, rCoin.y));
	    }
	}

	// проверка пересечения с КРАСНЫМ сердцем
	for (int j = 0; j < livesRed.size(); j++) {
	    LifeRed lred = (LifeRed) livesRed.get(j);
	    // если "ящик" разбит, то вещь можно взять
	    if (lred.getStage() == 1) {
		Rectangle rHRed = lred.getBounds();
		rHRed.x += rHRed.width / 2;
		rHRed.width = 1;
		if (player2 != null && player2.isCollision(rHRed)) {
		    lred.sound();
		    lred.setVisible(false);
		    player2.giveLife();
		    floatingTexts.add(new FloatingText("+1UP", rHRed.x, rHRed.y));
		}
	    }
	}

	// проверка пересечения с СИНИМ сердцем
	for (int j = 0; j < livesBlue.size(); j++) {
	    LifeBlue lblue = (LifeBlue) livesBlue.get(j);
	    if (lblue.getStage() == 1) {
		Rectangle rHBlue = lblue.getBounds();
		rHBlue.x += rHBlue.width / 2;
		rHBlue.width = 1;
		if (player1.isCollision(rHBlue)) {
		    lblue.sound();
		    lblue.setVisible(false);
		    player1.giveLife();
		    floatingTexts.add(new FloatingText("+1UP", rHBlue.x, rHBlue.y));
		}
	    }
	}
    }

    // обработчик событий по таймеру, инициирует изменения и взаимодейтвие объектов
    @Override
    public void actionPerformed(ActionEvent e) {
	checkCollisionsLadder();
	checkCollisionsBeam();
	checkCollisions();
	checkCollisionsPrecipice();
	checkCollisionsFireball();

	updateActors();
	updateBags();
	updateLivesRed();
	updateLivesBlue();
	updateFireballs();

	updateAnimations(explosions);
	updateAnimations(coins);

	updateFloatingText();

	player1.manaRegen(1);
	if (player2 != null) {
	    player2.manaRegen(1);
	}

	updateProgressBar();

	// вызов переопределенного метода paintComponent() для наследников класса JComponent
	repaint();
    }

    private class TAdapter implements KeyListener {

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	    int key = e.getKeyCode();

	    if (!player1.isFalling()) {
		// клавиши для перемещения героя
		if (key == KeyEvent.VK_LEFT) {
		    player1.setMoving(true);
		    player1.setDirection(Player1.SpriteDirection.LEFT);
		}

		if (key == KeyEvent.VK_RIGHT) {
		    player1.setMoving(true);
		    player1.setDirection(Player1.SpriteDirection.RIGHT);
		}

		if (key == KeyEvent.VK_UP) {
		    player1.setMoving(true);
		    player1.setDirection(Player1.SpriteDirection.UP);
		}

		if (key == KeyEvent.VK_DOWN) {
		    player1.setMoving(true);
		    player1.setDirection(Player1.SpriteDirection.DOWN);
		}
	    }

	    if (key == KeyEvent.VK_SPACE) {
		System.out.println(player1.toString());
	    }

	    if (key == KeyEvent.VK_ADD) {
		player1.giveLife();
	    }

	    if (key == KeyEvent.VK_SUBTRACT) {
		player1.takeLife();
	    }

	    if (key == KeyEvent.VK_ENTER) {
		player1.fire(8, BLOCK_SIZE * 5);
	    }

	    // клавиши для управления помощником
	    if (player2 != null && !player2.isFalling()) {
		if (key == KeyEvent.VK_A) {
		    player2.setMoving(true);
		    player2.setDirection(Player2.SpriteDirection.LEFT);
		}

		if (key == KeyEvent.VK_D) {
		    player2.setMoving(true);
		    player2.setDirection(Player2.SpriteDirection.RIGHT);
		}

		if (key == KeyEvent.VK_W) {
		    player2.setMoving(true);
		    player2.setDirection(Player2.SpriteDirection.UP);
		}

		if (key == KeyEvent.VK_S) {
		    player2.setMoving(true);
		    player2.setDirection(Player2.SpriteDirection.DOWN);
		}

		if (key == KeyEvent.VK_Q) {
		    player2.fire(8, BLOCK_SIZE * 5);
		}
	    }

	    if (key == KeyEvent.VK_P) {
		if (timer.isRunning()) {
		    timer.stop();
		} else {
		    timer.start();
		}
	    }

	    if (key == KeyEvent.VK_R) {
		player1.restart();
	    }

	    if (key == KeyEvent.VK_F12) {
		serviceMode = !serviceMode;
	    }
	}

	@Override
	public void keyReleased(KeyEvent e) {
	    int key = e.getKeyCode();

	    if (key == KeyEvent.VK_LEFT
		    || key == KeyEvent.VK_RIGHT
		    || key == KeyEvent.VK_UP
		    || key == KeyEvent.VK_DOWN) {
		player1.setMoving(false);
	    }

	    if (player2 != null
		    && (key == KeyEvent.VK_A
		    || key == KeyEvent.VK_D
		    || key == KeyEvent.VK_W
		    || key == KeyEvent.VK_S)) {
		player2.setMoving(false);
	    }
	}
    }

}
