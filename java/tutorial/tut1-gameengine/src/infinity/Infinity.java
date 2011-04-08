package infinity;

import infinity.gameengine.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Random;

import sun.security.util.Password;

public class Infinity extends Game {

	public Infinity() {
		super("MyGame");
	}


	public static void main(String[] args) {
		Game myGame = new Infinity();
		myGame.run();
	}

	public void render() {
		Graphics graphics = screenBuffer.getDrawGraphics();
		graphics.clearRect(0, 0, getScreenWidth(), getScreenHeight());
		graphics.setFont(new Font("Ariel",Font.BOLD,32));
		graphics.drawString("FPS: " + fps, 10, 60);
		graphics.drawString("UPS: " + ups, 10, 100);

		if((System.nanoTime()-delayLastDisplayed)/1000000>1000)
		{
			delayLastDisplayed = System.nanoTime();
			lastDisplayedDelay = totalDelay;
			totalDelay=0;
		}
		graphics.drawString("Delay: " + lastDisplayedDelay, 10, 140);
		sleep(random.nextInt(25));
	}
	
	Random random = new Random();
	
	long totalDelay;
	long delayLastDisplayed;
	long lastDisplayedDelay;

	public void update() {
		sleep(random.nextInt(20));
	}
	
	public void sleep(int milis)
	{
		long start = System.nanoTime();
		try {
			Thread.sleep(milis);
		} catch (InterruptedException e) {
		}
		totalDelay += (System.nanoTime()-start)/1000000;
	}

	// Image background;
	public void init() {
	}

	public void cleanup() {
	}
}
