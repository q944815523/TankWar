import java.awt.Color;
import java.awt.Graphics;


public class TankBoom {
	private int x, y;
	private int speed = 5;
	private int count = 0;
	
	public TankBoom(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean draw(Graphics graphics) {
		if (count == 10) {
			return true;
		}
		Color color = graphics.getColor();
		graphics.setColor(Color.CYAN);
		graphics.fillOval(x - count*speed, y - count*speed, 2*count*speed, 2*count*speed);
		count++;
		graphics.setColor(color);
		return false;
	}
}
