package infinity.gameengine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.VolatileImage;
import java.util.Random;

public abstract class Game implements Runnable {

	protected GraphicsDevice graphicsDevice;
	protected BufferStrategy screenBuffer;
	protected Frame window;
	private boolean quit;
	private String windowTitle;

	public Game(String windowTitle) {
		this.windowTitle = windowTitle;
		quit = false;
	}

	public void run() {
		try {
			gameInit();
			gameLoop();
		} catch (Throwable e) {
			e.printStackTrace();
			while (e.getCause() != null) {
				e = e.getCause();
				e.printStackTrace();
			}
		} finally {
			gameCleanup();
			System.exit(0);
		}
	}

	private void gameCleanup() {
		window.dispose();
		cleanup();
	}

	protected void gameInit() {
		window = new Frame();
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});
		window.setTitle(windowTitle);
		window.setUndecorated(true);
		window.setResizable(false);
		window.setIgnoreRepaint(true);
		window.addKeyListener(new Keyboard(this));
		Mouse mouse = new Mouse(this);
		window.addMouseMotionListener(mouse);
		window.addMouseListener(mouse);
		window.setBackground(Color.BLUE);
		// if (graphicsDevice.isFullScreenSupported())

		graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
		graphicsDevice.setFullScreenWindow(window);

		/*
		 * if (gd.isDisplayChangeSupported()) {
		 * gd.setDisplayMode(newDisplayMode); }
		 */
		window.createBufferStrategy(2);
		screenBuffer = window.getBufferStrategy();

		init();
	}

	protected int fps = 0;
	protected int ups = 0;

	protected void gameLoop() {
		long desiredUpdateRate = 60;
		long maxFrameSkip = 2; // minfps 20
		long maximumTimeperiod = 1000000000L / desiredUpdateRate;

		int framesSkipped = 0;
		long excessTimeTaken = 0; // to play catchup

		int updatesDone = 0;
		int framesDrawn = 0;
		long fpsMeasureStartTime = System.nanoTime();

		long startTime = System.nanoTime();
		while (!quit) {
			excessTimeTaken -= maximumTimeperiod;

			excessTimeTaken += System.nanoTime() - startTime;
			startTime = System.nanoTime();

			update();
			updatesDone++;

			excessTimeTaken += System.nanoTime() - startTime;
			startTime = System.nanoTime();

			if (excessTimeTaken > 0 && framesSkipped < maxFrameSkip) {
				framesSkipped++;
				continue;
			}

			gameRender();
			framesDrawn++;
			framesSkipped = 0;

			if (System.nanoTime() - fpsMeasureStartTime > 1000000000L) {
				ups = updatesDone;
				fps = framesDrawn;
				framesDrawn = 0;
				updatesDone = 0;
				fpsMeasureStartTime += 1000000000L;
			}

			excessTimeTaken += System.nanoTime() - startTime;
			startTime = System.nanoTime();

			if (excessTimeTaken < 0)
				sleep(-excessTimeTaken);
			else
				Thread.yield();
		}
	}

	protected void gameRender() {
		render();

		screenBuffer.show();
		Toolkit.getDefaultToolkit().sync();
	}

	protected void sleep(long timeInNanoSeconds) {
		try {
			Thread.sleep(timeInNanoSeconds / 1000000,
					(int) (timeInNanoSeconds % 1000000));
		} catch (InterruptedException ex) {
		}
	}

	public int getScreenHeight() {
		return window.getHeight();
	}

	public int getScreenWidth() {
		return window.getWidth();
	}

	public void exit() {
		quit = true;
	}

	public abstract void init();

	public abstract void update();

	public abstract void render();

	public abstract void cleanup();
}
