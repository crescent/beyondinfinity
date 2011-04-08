package infinity;

import infinity.gameengine.*;
import infinity.gameobjects.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class Infinity extends Game {

    public static String debugString;
	public static void main(String[] args) {
        Game myGame = new Infinity();
        myGame.Run();
    }

    public void render() {
        Graphics2D graphics = (Graphics2D) screenBuffer.getDrawGraphics();
        graphics.clearRect(0, 0, getScreenWidth(), getScreenHeight());
        graphics.setColor(Color.WHITE);
        gameWorld.draw(graphics);
        //background.Draw(graphics);
        //graphics.drawImage(background, 0, 0, null);
        String debugString = "isFullScreenSupported:"+graphicsDevice.isFullScreenSupported()+" isDisplayChangeSupported:"+graphicsDevice.isDisplayChangeSupported();
		graphics.drawString(debugString, 0, 850);
        graphics.drawString("FPS: " + fps, 500, 60);
        graphics.drawString("UPS: " + ups, 500, 120);
        graphics.drawString(""+Keyboard.isKeyDown(KeyEvent.VK_CONTROL), 600, 60);
        graphics.drawString(""+Mouse.count, 600, 120);
        
            //AffineTransform affineTransform = new AffineTransform();
            //affineTransform.translate(getScreenWidth()/2, getScreenHeight()/2);
            //affineTransform.rotate(getScreenHeight()/2- Mouse.getY(),Mouse.getX()-getScreenWidth()/2);
    }
    int x=0, y=0;
    public void update() {
        gameWorld.update();
        gameWorld.setCameraPostion(player.gamePosition);
    }


    Spacecraft player;
     GameWorld gameWorld;
    //Image background;
    public void init() {
        MyContentManager.LoadContent(this);
         gameWorld = new GameWorld(new Vector2(), new Vector2(getScreenWidth()/2, getScreenHeight()/2));
         gameWorld.Add(new Background(gameWorld));
         player = Spacecraft.CreatePlayer(gameWorld, new Vector2());
         gameWorld.Add(player);
         
         for(int i=0;i<20;i++)
        	 gameWorld.Add(Spacecraft.CreateEnemy(gameWorld, new Vector2(MyMath.Random.nextInt(200),MyMath.Random.nextInt(200))));
    }

    public void cleanup() {
    }
}
