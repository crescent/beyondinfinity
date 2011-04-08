package infinity;

import infinity.gameengine.*;
import infinity.gameobjects.*;
import infinity.networking.GameServer;
import infinity.networking.GameServerClient;
import infinity.networkingevent.RotateAndMove;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class Infinity extends Game {

	String serverAddress = "localhost";
	int serverPort = 5000;
	private int numBots = 32;

	public Infinity() {
		super("Game");
	}

	public Infinity(String serverAddress, int serverPort, int numBots) {
		this();
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		this.numBots = numBots;
	}

	public static StringBuffer debugString = new StringBuffer();

	public static void main(String[] args) {
		new Thread(new GameServer()).start();
		Game myGame = new Infinity();
		myGame.run();
	}

	public void render() {
		Graphics2D graphics = (Graphics2D) screenBuffer.getDrawGraphics();
		graphics.clearRect(0, 0, getScreenWidth(), getScreenHeight());
		graphics.setColor(Color.WHITE);
		gameWorld.draw(graphics);
		// background.Draw(graphics);
		// graphics.drawImage(background, 0, 0, null);
		graphics.drawString(debugString.toString(), 0, 850);
		graphics.drawString("FPS: " + fps, 500, 60);
		graphics.drawString("UPS: " + ups, 500, 120);
		debugString.delete(0, debugString.length());

		// AffineTransform affineTransform = new AffineTransform();
		// affineTransform.translate(getScreenWidth()/2, getScreenHeight()/2);
		// affineTransform.rotate(getScreenHeight()/2-
		// Mouse.getY(),Mouse.getX()-getScreenWidth()/2);
	}

	int x = 0, y = 0;

	static int timeCount = 0;
	public void update() {
		try {
			gameServerClient.processIncoming(gameWorld);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gameWorld.update();
	}

	private Spacecraft player;
	private GameWorld gameWorld;
	private GameServerClient gameServerClient;

	// Image background;
	public void init() {
		MyContentManager.LoadContent(this);

		gameServerClient = new GameServerClient();
		Vector2 screenCenter = new Vector2(getScreenWidth() / 2, getScreenHeight() / 2);
		gameWorld = new GameWorld(new Vector2(), screenCenter, gameServerClient);
		gameWorld.add(new Background(gameWorld));
		player = Spacecraft.CreatePlayer(gameWorld, new Vector2());
		//gameWorld.add(player);

		try {
			gameServerClient.connectToServer(serverAddress, serverPort);
			gameServerClient.sendAdd(player);
			for(int i=0;i<numBots;i++)
			{
				Spacecraft enemy = Spacecraft.CreateEnemy(gameWorld, new Vector2(MyMath.Random.nextInt(2000)-1000,MyMath.Random.nextInt(2000)-1000));
				//gameWorld.add(enemy);
				gameServerClient.sendAdd(enemy);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void cleanup() {
	}
}
