package infinity.gameengine;

import java.awt.Color;
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

public abstract class Game {

    protected GraphicsDevice graphicsDevice;
    protected BufferStrategy screenBuffer;
    private Frame window;
    private boolean quit;

    public void Run() {
    	try
    	{
        myInit();
        myGameLoop();
    	}
    	catch(Throwable e)
    	{
    		e.printStackTrace();
    		while(e.getCause()!=null)
    		{
    			e = e.getCause();
    			e.printStackTrace();
    			
    		}
    	}
    	finally
    	{
    		myCleanUp();
    		System.exit(0);
    	}
    }

    private void myCleanUp() {
        window.dispose();
        cleanup();
    }

    private void myInit() {
        quit = false;
        graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        window = new Frame();
        window.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });

        window.setUndecorated(true);
        window.setResizable(false);
        window.setIgnoreRepaint(true);
        window.addKeyListener(new Keyboard(this));
        Mouse mouse = new Mouse(this);
        window.addMouseMotionListener(mouse);
        window.addMouseListener(mouse);
        window.setBackground(Color.BLUE);
        if (graphicsDevice.isFullScreenSupported()) {
            graphicsDevice.setFullScreenWindow(window);
        } else {
            window.setSize(graphicsDevice.getDisplayMode().getWidth(), graphicsDevice.getDisplayMode().getHeight());
            window.setLocation(0, 0);
            window.setVisible(true);
        }
        /*if (gd.isDisplayChangeSupported()) {
        gd.setDisplayMode(newDisplayMode);
        }*/
        window.createBufferStrategy(2);
        screenBuffer = window.getBufferStrategy();

        init();
    }
    protected int fps = 0;
    protected int ups = 0;
    Random random = new Random();
	private boolean pause;

    private void myGameLoop() {
        long desiredUpdateRate = 60;
        long maxFrameSkip = 2; // minfps 20
        long maximumTimeperiod = 1000000000 / desiredUpdateRate;
        long maxYieldInterval = maximumTimeperiod * 20; // Yeild for one timeperiod every 20 periods at least

        int framesSkipped = 0;
        long excessTimeTaken = 0; // to play catchup

        int updatesDone = 0;
        int framesDrawn = 0;
        long fpsMeasureStartTime = 0;

        while (!quit) {
            long startTime = System.nanoTime();
            excessTimeTaken -= maximumTimeperiod;

            update();
            updatesDone++;

            if (excessTimeTaken > maximumTimeperiod && framesSkipped < maxFrameSkip) {
                excessTimeTaken += System.nanoTime() - startTime;
                framesSkipped++;
                continue;
            }

            gameRender();
            framesDrawn++;
            if (System.nanoTime() - fpsMeasureStartTime > 1000000000L) {
                ups = updatesDone;
                fps = framesDrawn;
                framesDrawn = 0;
                updatesDone = 0;
                fpsMeasureStartTime = System.nanoTime();
            }

            framesSkipped = 0;
            excessTimeTaken += System.nanoTime() - startTime;

            if (excessTimeTaken > maxYieldInterval) {
                excessTimeTaken = -maximumTimeperiod;
            }
            if (excessTimeTaken < 0) {
                sleep(-excessTimeTaken);
                excessTimeTaken = 0;
            }
            else
            {
                Thread.yield();
                //sleep(1000000);
                //excessTimeTaken-=1000000;
            }
            
            while(pause) sleep(1000000);
        }
    }

    public void gameRender() {
        render();
        screenBuffer.show();
        Toolkit.getDefaultToolkit().sync();
    }

    private void sleep(long timeInNanoSeconds) {
        try {
            //System.out.println("sleeping for "+-excessTimeTaken);
            Thread.sleep(timeInNanoSeconds / 1000000, (int) (timeInNanoSeconds % 1000000));
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

    public void pause() {
        pause = true;
    }

    public void unpause() {
        pause = false;
    }

    public abstract void init();

    public abstract void update();

    public abstract void render();

    public abstract void cleanup();
}
