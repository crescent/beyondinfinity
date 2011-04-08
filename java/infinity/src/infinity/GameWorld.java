/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package infinity;

import infinity.gameengine.Vector2;
import infinity.gameobjectproperties.*;
import infinity.gameobjects.BulletExplosion;
import infinity.gameobjects.GameObject;
import infinity.gameobjects.Spacecraft;
import infinity.gameobjects.SpacecraftSpawner;
import infinity.networking.GameServer;
import infinity.networking.GameServerClient;
import infinity.networking.GameServerCommand;
import infinity.networking.GameServerCommandType;
import infinity.networkingevent.AddGameObject;
import infinity.networkingevent.Collide;

import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * 
 * @author firefly
 */
public class GameWorld {

	private Vector2 cameraPosition;

	public void setCameraPostion(Vector2 position) {
		cameraPosition.assign(position);
	}

	public Vector2 getCameraPosition() {
		return cameraPosition;
	}

	private Vector2 screenCenter;
	private ArrayList<GameObject> gameObjects;
	//private Hashtable<String,ArrayList<GameObject>> recyclingPool = new Hashtable<String,ArrayList<GameObject>>(); 
	public ArrayList<GameObject> recyclingPool = new ArrayList<GameObject>();
	private GameServer gameServer = null;

	public GameWorld(Vector2 cameraPosition, Vector2 screenCenter) {
		this.cameraPosition = cameraPosition;
		this.screenCenter = screenCenter;
		gameObjects = new ArrayList<GameObject>();
	}

	public GameWorld(Vector2 cameraPosition, Vector2 screenCenter, GameServer gameServer) {
		this(cameraPosition, screenCenter);
		this.gameServer = gameServer;
	}

	public GameWorld(Vector2 cameraPosition, Vector2 screenCenter, GameServerClient gameServerClient) {
		this(cameraPosition, screenCenter);
		this.gameServerClient = gameServerClient;
	}

	public Vector2 getScreenCenter() {
		return screenCenter;
	}

	GameServerCommand gameServerCommandCache = new GameServerCommand();
	
	public void add(GameObject gameObject) {
		gameObjects.add(gameObject);
/*
		try {
			if (!isClient()) {
				if(gameObject instanceof BulletExplosion ) return;
				//if(gameObject instanceof SpacecraftSpawner) return;
				//gameServer.sendToAll(gameServerCommand.create(GameServerCommandType.add,gameObject));
				gameServer.sendToAll(gameServerCommand.createNetworkEvent(new AddGameObject(gameObject)));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	public void draw(Graphics2D g) {
		for (GameObject gameObject : gameObjects)
			gameObject.Draw(g);
	}

	public void update() {
		ArrayList<GameObject> gameObjectsToRemove = new ArrayList<GameObject>();
		for (GameObject gameObject : gameObjects)
			if (!gameObject.IsAlive())
				gameObjectsToRemove.add(gameObject);

		//recyclingPool.addAll(gameObjectsToRemove);
		gameObjects.removeAll(gameObjectsToRemove);

		for (GameObject gameObject : getGameObjectsClone()) {
			gameObject.Update();
		}
	}

	public void checkCollisions(GameServer gameServer) throws IOException {
		ArrayList<GameObject> gameObjectsClone = getGameObjectsClone();
		for (GameObject gameObject : gameObjectsClone) {
			if (gameObject instanceof Spacecraft)
				checkCollisionsFor((ColloidableObject) gameObject, gameServer);
		}
	}

	private void checkCollisionsFor(ColloidableObject spacecraft, GameServer gameServer)
			throws IOException {
		for (GameObject gameObject : getGameObjectsClone()) {
			if (gameObject instanceof ColloidableObject
					&& gameObject.getGameObjectId() != ((GameObject) spacecraft).getGameObjectId()) {
				ColloidableObject colloidableObject = (ColloidableObject) gameObject;
				if (spacecraft.CollisionSphere().Intersects(colloidableObject.CollisionSphere())) {
					spacecraft.CollidedWith(colloidableObject);
					colloidableObject.CollidedWith(spacecraft);
					gameServer.sendToAll(gameServerCommandCache.create(
							new Collide(((GameObject) spacecraft).getGameObjectId(),
									((GameObject) colloidableObject).getGameObjectId())));
				}
			}
		}
	}

	public ArrayList<AiIdentifiableObjectInfo> GetIdentifiableObjects() {
		ArrayList<AiIdentifiableObjectInfo> gameObjectInfos = new ArrayList<AiIdentifiableObjectInfo>();

		for (int i = 0; i < gameObjects.size(); i++) {
			if (gameObjects.get(i) instanceof AiIdentifiableObject) {
				gameObjectInfos.add(((AiIdentifiableObject) gameObjects.get(i))
						.getAiIdentifiableObjectInfo());
			}

		}
		return gameObjectInfos;
	}

	public GameObject getGameObject(MyUUID gameObjectId) {
		for (GameObject gameObject : gameObjects) {
			if (gameObject.getGameObjectId().equals(gameObjectId))
				return gameObject;
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (GameObject gameObject : gameObjects) {
			sb.append(gameObject.getClass());
			sb.append(" ");
			sb.append(gameObject.getGameObjectId());
			sb.append("\n");
		}
		;
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	public ArrayList<GameObject> getGameObjectsClone() {
		// Used for addition / removal while traversing
		return (ArrayList<GameObject>) gameObjects.clone();
	}

	public GameServerClient getGameServerClient() {
		return gameServerClient;
	}

	public boolean isClient() {
		return getGameServerClient() != null;
	}

	public GameServer getGameServer() {
		return gameServer;
	}

	private GameServerClient gameServerClient;
}
