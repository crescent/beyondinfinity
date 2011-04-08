/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package infinity;

import infinity.gameengine.Vector2;
import infinity.gameobjectproperties.*;
import infinity.gameobjects.Spacecraft;

import java.awt.Graphics2D;
import java.util.ArrayList;


/**
 * 
 * @author firefly
 */
public class GameWorld {

	Vector2 cameraPosition;

	void setCameraPostion(Vector2 position) {
		cameraPosition.assign(position);
	}

	Vector2 getCameraPosition() {
		return cameraPosition;
	}

	private Vector2 screenCenter;
	private ArrayList<GameObject> gameObjects;

	public GameWorld(Vector2 cameraPosition, Vector2 screenCenter) {
		this.cameraPosition = cameraPosition;
		this.screenCenter = screenCenter;
		gameObjects = new ArrayList<GameObject>();
	}

	public Vector2 getScreenCenter() {
		return screenCenter;
	}

	public void Add(GameObject gameObject) {
		gameObjects.add(gameObject);
	}

	public void UpdateCamera(Vector2 position) {
		cameraPosition.assign(position);
	}

	public void draw(Graphics2D g) {
		for (GameObject gameObject : gameObjects)
			gameObject.Draw(g);
	}

	@SuppressWarnings("unchecked")
	public void update() {
		ArrayList<GameObject> gameObjectsToRemove = new ArrayList<GameObject>();
		for (GameObject gameObject : gameObjects)
			if (!gameObject.IsAlive())
				gameObjectsToRemove.add(gameObject);

		gameObjects.removeAll(gameObjectsToRemove);

		for (GameObject gameObject : (ArrayList<GameObject>) gameObjects
				.clone()) {
			if (gameObject instanceof Spacecraft)
				CheckCollisions((ColloidableObject) gameObject);
			gameObject.Update();
		}
	}

	public ArrayList<AiIdentifiableObjectInfo> GetIdentifiableObjects() {
		ArrayList<AiIdentifiableObjectInfo> gameObjectInfos = new ArrayList<AiIdentifiableObjectInfo>();

		for (GameObject gameObject : gameObjects) {
			if (gameObject instanceof AiIdentifiableObject) {
				AiIdentifiableObjectInfo gameObjectInfo = new AiIdentifiableObjectInfo();
				gameObjectInfos.add(((AiIdentifiableObject) gameObject).getAiIdentifiableObjectInfo());
			}
		}
		return gameObjectInfos;
	}

	@SuppressWarnings("unchecked")
	public void CheckCollisions(ColloidableObject spacecraft) {
		for (GameObject gameObject : (ArrayList<GameObject>) gameObjects
				.clone()) {
			if (gameObject instanceof ColloidableObject) {
				ColloidableObject colloidableObject = (ColloidableObject) gameObject;
				if (spacecraft.CollisionSphere().Intersects(
						colloidableObject.CollisionSphere())) {
					spacecraft.CollidedWith(colloidableObject);
					colloidableObject.CollidedWith(spacecraft);
				}
			}
		}
	}

}
