package infinity.networkingevent;

import infinity.GameWorld;
import infinity.MyUUID;
import infinity.gameobjectproperties.ColloidableObject;
import infinity.gameobjects.GameObject;
import infinity.gameobjects.Spacecraft;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Collide implements GameNetworkingEvent {

	private MyUUID gameObjectId1;
	private MyUUID gameObjectId2;

	public Collide(MyUUID gameObjectId1, MyUUID gameObjectId2) {
		this.gameObjectId1 = gameObjectId1;
		this.gameObjectId2 = gameObjectId2;
		// TODO Auto-generated constructor stub
	}
	
	public Collide() {
		gameObjectId1 = MyUUID.random();
		gameObjectId2 = MyUUID.random();
	}

	@Override
	public void clientSideProcess(DataInput in, GameWorld gameWorld)
			throws IOException {
		gameObjectId1.readExternal(in);
		gameObjectId2.readExternal(in);
		
		ColloidableObject gameObject1 = (ColloidableObject)gameWorld.getGameObject(gameObjectId1);
		ColloidableObject gameObject2 = (ColloidableObject)gameWorld.getGameObject(gameObjectId2);
		
		if(gameObject1!=null) gameObject1.CollidedWith(gameObject2);
		if(gameObject2!=null) gameObject2.CollidedWith(gameObject1);
	}

	@Override
	public void sendToClient(DataOutput out) throws IOException {
		out.writeInt(GameEventsList.COLLIDE);
		gameObjectId1.writeExternal(out);
		gameObjectId2.writeExternal(out);
	}

	@Override
	public void sendToServer(DataOutput out) throws IOException {
	}

	@Override
	public void serverSideProcess(DataInput in, GameWorld gameWorld)
			throws IOException {
	}

	@Override
	public void reset() {
		gameObjectId1.reset();
		gameObjectId2.reset();
	}
}
