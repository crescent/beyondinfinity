package infinity.networkingevent;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;

import infinity.GameWorld;
import infinity.gameobjects.Bullet;
import infinity.gameobjects.GameObject;
import infinity.gameobjects.GameObjectsList;
import infinity.gameobjects.Spacecraft;

public class AddGameObject implements GameNetworkingEvent {

	private GameObject gameObject;

	public AddGameObject(GameObject gameObject) {
		this.gameObject = gameObject;
	}

	public AddGameObject() {
		gameObject = null;
	}

	@Override
	public void clientSideProcess(DataInput in, GameWorld gameWorld)
			throws IOException {
		//String className = in.readUTF();
		int gameObjectTypeId = in.readInt();

		instantiate(gameObjectTypeId, gameWorld);
		gameObject.createFromStream(gameWorld, in);
		gameWorld.add(gameObject);
	}

	private void instantiate(int gameObjectTypeId, GameWorld gameWorld) {
		try {
			gameObject = GameObjectsList.get(gameObjectTypeId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		try {
			gameObject = ((GameObject) Class.forName(gameObjectTypeId).newInstance());
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	@Override
	public void sendToClient(DataOutput out) throws IOException {
		out.writeInt(GameEventsList.ADD);
		gameObject.writeExternal(out);
	}

	@Override
	public void sendToServer(DataOutput out) throws IOException {
		out.writeInt(GameEventsList.ADD);
		gameObject.writeExternal(out);
	}

	@Override
	public void serverSideProcess(DataInput in, GameWorld gameWorld)
			throws IOException {
		//String className = in.readUTF();
		int gameObjectTypeId = in.readInt();

		instantiate(gameObjectTypeId, gameWorld);
		gameObject.createFromStream(gameWorld, in);
		gameWorld.add(gameObject);
	}

	@Override
	public void reset() {
		gameObject = null;
	}
}
