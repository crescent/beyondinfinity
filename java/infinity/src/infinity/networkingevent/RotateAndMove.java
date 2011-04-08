package infinity.networkingevent;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import infinity.GameWorld;
import infinity.MyUUID;
import infinity.gameengine.Vector2;
import infinity.gameobjects.GameObject;
import infinity.gameobjects.Spacecraft;

public class RotateAndMove implements GameNetworkingEvent {
	
	MyUUID gameObjectId;
	private Vector2 velocity;
	private Vector2 position;
	private float direction;
	
	public RotateAndMove init(MyUUID spacecraftGameObjectId, Vector2 newVelocity)
	{
		gameObjectId = spacecraftGameObjectId;
		velocity = velocity == null ? new Vector2(newVelocity) : velocity.assign(newVelocity);
		position = position == null ? new Vector2() : position;
		direction = 0f;
		return this;
	}

	public RotateAndMove(MyUUID spacecraftGameObjectId, Vector2 newVelocity)
	{
		init(spacecraftGameObjectId, newVelocity);
	}

	public RotateAndMove()
	{
		this(MyUUID.random(), new Vector2());
	}
	
	public void sendToServer(DataOutput out) throws IOException {
		out.writeInt(GameEventsList.SPACECRAFT_MOVE);
		gameObjectId.writeExternal(out);
		velocity.writeExternal(out);
	}

	public void serverSideProcess(DataInput in, GameWorld gameWorld) throws IOException {
		gameObjectId.readExternal(in);
		velocity.readExternal(in);
		
		Spacecraft spacecraft = (Spacecraft) gameWorld.getGameObject(gameObjectId);
		
		if(spacecraft==null) gameObjectId = MyUUID.random();
		if(spacecraft==null) return;
		
		spacecraft.rotateAndMove(velocity);
		velocity = spacecraft.getVelocity(velocity);
		position = spacecraft.getPosition(position);
		direction = spacecraft.getDirection();
		//System.out.println("serverSideProcess:"+gameObjectId+" "+velocity+" "+position);
	}
	
	public void sendToClient(DataOutput out) throws IOException {
		out.writeInt(GameEventsList.SPACECRAFT_MOVE);
		gameObjectId.writeExternal(out);
		velocity.writeExternal(out);
		position.writeExternal(out);
		out.writeFloat(direction);
	}
	
	public void clientSideProcess(DataInput in, GameWorld gameWorld) throws IOException {
		gameObjectId.readExternal(in);
		velocity.readExternal(in);
		position.readExternal(in);
		direction = in.readFloat();
		
		Spacecraft spacecraft = (Spacecraft)gameWorld.getGameObject(gameObjectId);
		//System.out.println(position);
		if(spacecraft != null) spacecraft.event(this);
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public Vector2 getPosition() {
		return position;
	}

	public float getDirection() {
		return direction;
	}

	@Override
	public void reset() {
		gameObjectId.reset();
		velocity = velocity == null ? new Vector2() : velocity.assign(Vector2.zero);
		position = velocity == null ? new Vector2() : velocity.assign(Vector2.zero);
		direction = 0f;
	}
}
