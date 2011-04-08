package infinity.networkingevent;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import infinity.GameWorld;
import infinity.MyUUID;
import infinity.gameobjects.Spacecraft;

public class KillGameObject implements GameNetworkingEvent {

	private MyUUID gameObjectId;

	public KillGameObject(MyUUID gameObjectId) {
		this.gameObjectId = gameObjectId;
		// TODO Auto-generated constructor stub
	}

	public KillGameObject() {
		gameObjectId = MyUUID.random();
	}

	@Override
	public void clientSideProcess(DataInput in, GameWorld gameWorld)
			throws IOException {
		gameObjectId.readExternal(in);
		Spacecraft spacecraft = (Spacecraft) gameWorld.getGameObject(gameObjectId);
		spacecraft.event(this);
	}

	@Override
	public void sendToClient(DataOutput out) throws IOException {
		out.writeInt(GameEventsList.KILL);
		gameObjectId.writeExternal(out);

	}

	@Override
	public void sendToServer(DataOutput out) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void serverSideProcess(DataInput in, GameWorld gameWorld)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void reset() {
		gameObjectId.reset();
	}

}
