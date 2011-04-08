package infinity.networkingevent;

import infinity.GameWorld;
import infinity.MyUUID;
import infinity.gameobjects.GameObject;
import infinity.gameobjects.Spacecraft;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class FireBullet implements GameNetworkingEvent {

	private MyUUID gameObjectId;
	private float energy;

	public FireBullet(MyUUID gameObjectId) {
		this.gameObjectId = gameObjectId;
		// TODO Auto-generated constructor stub
	}
	
	public FireBullet() {
		gameObjectId = MyUUID.random();
	}

	@Override
	public void clientSideProcess(DataInput in, GameWorld gameWorld)
			throws IOException {
		gameObjectId.readExternal(in);
		energy = in.readFloat();
		
		Spacecraft spaceCraft = (Spacecraft) gameWorld.getGameObject(gameObjectId);
		if(spaceCraft!=null) spaceCraft.event(this);
	}

	@Override
	public void sendToClient(DataOutput out) throws IOException {
		out.writeInt(GameEventsList.SPACECRAFT_FIRE);
		gameObjectId.writeExternal(out);
		out.writeFloat(energy);
	}

	@Override
	public void sendToServer(DataOutput out) throws IOException {
		out.writeInt(GameEventsList.SPACECRAFT_FIRE);
		gameObjectId.writeExternal(out);
	}

	@Override
	public void serverSideProcess(DataInput in, GameWorld gameWorld)
			throws IOException {
		gameObjectId.readExternal(in);
		
		Spacecraft spaceCraft = (Spacecraft) gameWorld.getGameObject(gameObjectId);
		if(spaceCraft!=null) 
		{
			spaceCraft.FireBullet();
			energy = spaceCraft.Energy();
		}
	}

	public float getEnergy() {
		return energy;
	}

	@Override
	public void reset() {
		gameObjectId.reset();
		energy = 0;
	}

}
