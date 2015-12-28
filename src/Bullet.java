import java.awt.Color;
import java.awt.Graphics;

public class Bullet {
	private int x, y;
	private int towards;
	private int radius = 10;
	private int speed = 7;
	private int boxWidth, boxHeight;
	private boolean isOut = false;
	private static final int LEFT = Tank.TOWARDS_LEFT, RIGHT = Tank.TOWARDS_RIGHT, UP = Tank.TOWARDS_UP, DOWN = Tank.TOWARDS_DOWN;
	private boolean fromMe;
	public Bullet(int x, int y, int towards) {
		this.x = x;
		this.y = y;
		this.towards = towards;
	}
	
	public Bullet(int x, int y, int towards, int boxWidth, int boxHeight) {
		this(x, y, towards);
		this.setBox(boxWidth, boxHeight);
	}
	
	public void setBox(int boxWidth, int boxHeight) {
		this.boxWidth = boxWidth;
		this.boxHeight = boxHeight;
	}
	
	public boolean outOrNot(){
		return isOut;
	}
	
	public void draw(Graphics graphics) {
		Color color = graphics.getColor();
		graphics.setColor(Color.BLACK);
		graphics.fillOval(x - radius/2, y - radius/2, radius, radius);
		graphics.setColor(color);
		move();
	}
	
	public void move(){
		if (!isOut) {
			switch(towards) {
				case UP:if(y <= 0) {isOut = true;}else{y -= speed;} break;
				case DOWN:if(y >= boxHeight) {isOut = true;}else{y += speed;} break;
				case LEFT:if(x <= 0) {isOut = true;}else{x -= speed;} break;
				case RIGHT:if(x >= boxWidth) {isOut = true;}else{x += speed;} break;
			}
		}
	}
	
	public boolean hitTank(Tank tank) {
		double xd = (double)x;
		double yd = (double)y;
		double xdTank = (double)tank.getX();
		double ydTank = (double)tank.getY();
		if(((xd - xdTank)*(xd - xdTank) + (yd - ydTank)*(yd - ydTank)) < tank.getRadiu()*tank.getRadiu()){
			return true;
		}
		return false;
	}
	
	public void testGit() {
		System.out.println("Add this method to test github");
	}
}
