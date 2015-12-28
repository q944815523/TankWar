import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

public class Tank {
	private int x, y;
	private int towards;
	private int state;
	private static final int SIZE = 50;
	private Color tankColor = Color.GREEN;
	private boolean isEnimy = true;
	private int speed = 3;
	private static final int GUN_LENGTH = 45, GUN_WIDTH = 10;
	public static final int TOWARDS_LEFT= 1;
	public static final int TOWARDS_RIGHT = 2;
	public static final int TOWARDS_UP = 3;
	public static final int TOWARDS_DOWN = 4;
	public static final int MOVING = 1, STOP = 2;
	
	private int boxWidth, boxHeight;
	private int padding = GUN_LENGTH;
	private int rangeLeft, rangeRight, rangeUp, rangeDown;
	
	private int redLine = SIZE*SIZE;
	
	private ArrayList<Tank> tanks = new ArrayList<Tank>();
	
	private int moveCount = 0;
	private static int changeNum = 1000/TankWar.getSleepTime();
	
	public Tank(int x, int y, int towards) {
		this.x = x;
		this.y = y;
		this.towards = towards;
		this.state = STOP;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getRadiu(){
		return SIZE/2;
	}
	
	public Tank(int x, int y, int towards, int boxWidth, int boxHeight) {
		this(x, y, towards);
		this.setBox(boxWidth, boxHeight);
	}
	
	public void setBox(int boxWidth, int boxHeight) {
		this.boxWidth = boxWidth;
		this.boxHeight = boxHeight;
		this.rangeLeft = padding + 10;
		this.rangeRight = boxWidth - rangeLeft;
		this.rangeUp = padding + 30;
		this.rangeDown = boxHeight - padding - 10;
	}
	
	public void setEnimy(boolean isEnimy) {
		this.isEnimy = isEnimy;
		if (!isEnimy) {
			this.tankColor = Color.RED;
		}
	}
	
	public void setTanks(ArrayList<Tank> tanks) {
		this.tanks = tanks;
	}
	
	public void setState(int state){
		this.state = state;
	}
	
	public int getState() {
		return state;
	}
	
	public void setTowards(int towards) {
		this.towards = towards;
	}
	
	public void move() {
		switch (towards){
			case TOWARDS_LEFT:if(canMoveLeft()) {x -= speed;} break;
			case TOWARDS_RIGHT:if(canMoveRight()) {x += speed;} break;
			case TOWARDS_UP:if(canMoveUp()) {y -= speed;} break;
			case TOWARDS_DOWN:if(canMoveDown()) {y += speed;} break;
		}
	}
	
	public Bullet randomMove() {
		Bullet bullet = null;
		moveCount++;
		if (moveCount == changeNum || moveCount == changeNum/2) {
			bullet = createBullet();
		}
		if (moveCount == changeNum) {
			moveCount = 0;
			Random random = new Random();
			if (random.nextBoolean()){
				setTowards(random.nextInt(4) + 1);
			}
		}
		move();
		return bullet;
	}
	
	private boolean canMoveDown() {
		if (y >= rangeDown) {
			return false;
		}
		for (int i = 0; i < tanks.size(); i++) {
			Tank tank = tanks.get(i);
			if((x - tank.getX())*(x - tank.getX()) + (y - tank.getY())*(y - tank.getY()) <= redLine){
				if(tank.getY() > y) {
					return false;
				}
			}
		}
		Tank tank = TankWar.getHostTank();
		if((x - tank.getX())*(x - tank.getX()) + (y - tank.getY())*(y - tank.getY()) <= redLine){
			if(tank.getY() > y) {
				return false;
			}
		}
		return true;
	}
	
	private boolean canMoveUp() {
		if (y <= rangeUp) {
			return false;
		}
		for (int i = 0; i < tanks.size(); i++) {
			Tank tank = tanks.get(i);
			if((x - tank.getX())*(x - tank.getX()) + (y - tank.getY())*(y - tank.getY()) <= redLine){
				if(tank.getY() < y) {
					return false;
				}
			}
		}
		Tank tank = TankWar.getHostTank();
		if((x - tank.getX())*(x - tank.getX()) + (y - tank.getY())*(y - tank.getY()) <= redLine){
			if(tank.getY() < y) {
				return false;
			}
		}
		return true;
	}
	
	private boolean canMoveLeft() {
		if (x <= rangeLeft) {
			return false;
		}
		for (int i = 0; i < tanks.size(); i++) {
			Tank tank = tanks.get(i);
			if((x - tank.getX())*(x - tank.getX()) + (y - tank.getY())*(y - tank.getY()) <= redLine){
				if(tank.getX() < x) {
					return false;
				}
			}
		}
		Tank tank = TankWar.getHostTank();
		if((x - tank.getX())*(x - tank.getX()) + (y - tank.getY())*(y - tank.getY()) <= redLine){
			if(tank.getX() < x) {
				return false;
			}
		}
		return true;
	}
	
	private boolean canMoveRight() {
		if (x >= rangeRight){
			return false;
		}
		for (int i = 0; i < tanks.size(); i++) {
			Tank tank = tanks.get(i);
			if((x - tank.getX())*(x - tank.getX()) + (y - tank.getY())*(y - tank.getY()) <= redLine){
				if(tank.getX() > x) {
					return false;
				}
			}
		}
		Tank tank = TankWar.getHostTank();
		if((x - tank.getX())*(x - tank.getX()) + (y - tank.getY())*(y - tank.getY()) <= redLine){
			if(tank.getX() > x) {
				return false;
			}
		}
		return true;
	}
	
	public void draw(Graphics g) {
		Color color = g.getColor();
		g.setColor(tankColor);
		g.fillOval(x - SIZE/2, y - SIZE/2, SIZE, SIZE);
		drawTowards(g);
		g.setColor(color);
	}
	
	private void drawTowards(Graphics g) {
		switch(towards){
			case TOWARDS_LEFT:g.fillRect(x - GUN_LENGTH, y - GUN_WIDTH/2, GUN_LENGTH, GUN_WIDTH);break;
			case TOWARDS_RIGHT:g.fillRect(x, y - GUN_WIDTH/2, GUN_LENGTH, GUN_WIDTH);break;
			case TOWARDS_UP:g.fillRect(x - GUN_WIDTH/2, y - GUN_LENGTH, GUN_WIDTH, GUN_LENGTH);break;
			case TOWARDS_DOWN:g.fillRect(x - GUN_WIDTH/2, y, GUN_WIDTH, GUN_LENGTH);break;
		}
	}
	
	public int[] getBulletLocation() {
		switch (towards){
			case TOWARDS_UP:return new int[]{x, y - GUN_LENGTH};
			case TOWARDS_DOWN:return new int[]{x, y + GUN_LENGTH};
			case TOWARDS_LEFT:return new int[]{x - GUN_LENGTH, y};
			case TOWARDS_RIGHT:return new int[]{x + GUN_LENGTH, y};
			default:return null;
		}
	}
	
	public Bullet createBullet() {
		int[] location = getBulletLocation();
		return new Bullet(location[0], location[1], towards, boxWidth, boxHeight);
	}
}
