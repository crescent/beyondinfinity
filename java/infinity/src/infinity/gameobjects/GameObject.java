package infinity.gameobjects;

import infinity.GameWorld;
import infinity.MyUUID;
import infinity.gameengine.Vector2;
import infinity.networking.GameServerClient;

import java.awt.Graphics2D;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.UUID;

public abstract class GameObject {

	private MyUUID gameObjectId;

	protected GameWorld gameWorld;
	protected Vector2 gamePosition;

	private Vector2 positionOnScreen;

	protected GameObject(GameWorld gameWorld, Vector2 gamePosition) {
		this();
		this.gameWorld = gameWorld;
		this.gamePosition = gamePosition;
	}
	
	protected GameObject() {
		this.gameObjectId = MyUUID.random();
		this.positionOnScreen = new Vector2();
	}
	
	public abstract void Update();

	public abstract void Draw(Graphics2D g);

	protected Vector2 PositionOnScreen() {
		return positionOnScreen.assign(gameWorld.getScreenCenter()).subtract(
				gameWorld.getCameraPosition()).add(gamePosition);
	}

	public boolean IsAlive() {
		return true;
	}

	public MyUUID getGameObjectId() {
		return gameObjectId;
	}

	public void createFromStream(GameWorld gameWorld, DataInput in) throws IOException
	{
		readExternal(in);
	}

	public void readExternal(DataInput in) throws IOException {
		gameObjectId.readExternal(in);
	}

	public void writeExternal(DataOutput out) throws IOException {
		//out.writeUTF((getClass().getName()));
		out.writeInt(getGameObjectTypeId());
		gameObjectId.writeExternal(out);
	}

	public Vector2 getGamePosition() {
		return gamePosition;
	}
	
	public abstract int getGameObjectTypeId();
}