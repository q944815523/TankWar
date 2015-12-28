import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import util.Property;


public class TankWar extends Frame{
	
	private static final int SLEEP_TIME = 20;
	
	private static final int WINDOW_WIDTH = 800, WINDOW_HEIGHT = 600;
	private static final boolean GAME_ON = true, GAME_OVER = false;
	
	private int enimyTankNum;
	
	private ArrayList<Tank> tanks = new ArrayList<Tank>();
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	private ArrayList<Bullet> enimyBullets = new ArrayList<Bullet>();
	private ArrayList<TankBoom> tankBooms = new ArrayList<TankBoom>();
	private static Tank myTank;
	
	private int keyPressed;
	private boolean gameState = GAME_ON;
	
	public static void main(String[] args) {
		new TankWar().launchFrame();
	}

	private void launchFrame() {
		setLocation(100, 50);
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				System.exit(0);
			}
		});
		addKeyListener(new KeyMonitor());
		initTank();
		new PaintThread().start();
		setVisible(true);
	}
	
	public static Tank getHostTank() {
		return myTank;
	}
	
	private void initTank(){
		enimyTankNum = Property.getConfig("EnimyNum");
		myTank = new Tank(100, 100, Tank.TOWARDS_RIGHT, WINDOW_WIDTH, WINDOW_HEIGHT);
		myTank.setTanks(tanks);
		myTank.setEnimy(false);
		
		for (int i = 0; i < enimyTankNum; i++) {
			createTank(100 + i*60, 200, Tank.TOWARDS_DOWN);
		}
	}

	public static int getSleepTime() {
		return SLEEP_TIME;
	}
	
	@Override
	public void paint(Graphics g) {
		if (gameState) {
			myTank.draw(g);
			for(int i = 0; i < tanks.size(); i++) {
				tanks.get(i).draw(g);
			}
			for(int i = 0; i < bullets.size(); i++) {
				bullets.get(i).draw(g);
			}
			for(int i = 0; i < enimyBullets.size(); i++) {
				enimyBullets.get(i).draw(g);
			}
			for(int i = 0; i < tankBooms.size(); i++) {
				if(tankBooms.get(i).draw(g)){
					tankBooms.remove(i);
					i--;
				}
			}
		}else {
			g.setColor(Color.GRAY);
			g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		}
		
		super.paint(g);
	}
	
	private class PaintThread extends Thread{
		@Override
		public void run() {
			while(true){
				try {
					Thread.sleep(SLEEP_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(myTank.getState() == Tank.MOVING){
					myTank.move();
				}
				/*敌人坦克随机移动并发射子弹*/
				for (int i = 0; i < tanks.size(); i++) {
					Bullet b = tanks.get(i).randomMove();
					if (b != null) {
						enimyBullets.add(b);
					}
				}
				/*子弹如果飞出屏幕则删除*/
				for (int i = bullets.size(); i > 0; i--) {
					Bullet bullet = bullets.get(i - 1);
					if(bullet.outOrNot()){
						bullets.remove(i - 1);
					}
				}
				/*判断主角子弹与敌人坦克是否碰撞*/
				for (int i = 0; i < bullets.size(); i++) {
					Bullet bullet = bullets.get(i);
					for (int j = 0; j < tanks.size(); j++) {
						if (bullet.hitTank(tanks.get(j))) {
							deleteTank(j);
							bullets.remove(i);
							i--;
							continue;
						}
					}
				}
				/*判断敌人子弹是否与主角碰撞*/
				for (int i = 0; i < enimyBullets.size(); i++) {
					Bullet bullet = enimyBullets.get(i);
					if (bullet.hitTank(myTank)) {
						gameEnd();
					}
				}
				repaint();
			}
		}
	}
	
	private void deleteTank(int index) {
		tankBooms.add(new TankBoom(tanks.get(index).getX(), tanks.get(index).getY()));
		tanks.remove(index);
	}
	
	private void createTank(int x, int y, int towards) {
		Tank tank = new Tank(x, y, towards, WINDOW_WIDTH, WINDOW_HEIGHT);
		tank.setTanks(tanks);
		tanks.add(tank);
	}
	
	private class KeyMonitor extends KeyAdapter{

		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()){
				case KeyEvent.VK_UP:keyPressed = KeyEvent.VK_UP;myTank.setState(Tank.MOVING);myTank.setTowards(Tank.TOWARDS_UP);break;
				case KeyEvent.VK_RIGHT:keyPressed = KeyEvent.VK_RIGHT;myTank.setState(Tank.MOVING);myTank.setTowards(Tank.TOWARDS_RIGHT);break;
				case KeyEvent.VK_DOWN:keyPressed = KeyEvent.VK_DOWN;myTank.setState(Tank.MOVING);myTank.setTowards(Tank.TOWARDS_DOWN);break;
				case KeyEvent.VK_LEFT:keyPressed = KeyEvent.VK_LEFT;myTank.setState(Tank.MOVING);myTank.setTowards(Tank.TOWARDS_LEFT);break;
				case KeyEvent.VK_SPACE:bullets.add(myTank.createBullet());
			}
			super.keyPressed(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == keyPressed) {
				myTank.setState(Tank.STOP);
			}
			super.keyReleased(e);
		}
		
	}
	
	private void gameEnd(){
		gameState = GAME_OVER;
	}
}
