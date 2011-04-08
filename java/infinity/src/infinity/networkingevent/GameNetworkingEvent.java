package infinity.networkingevent;

import infinity.GameWorld;
import infinity.gameobjects.Spacecraft;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Hashtable;

public interface GameNetworkingEvent {

	public void reset();
	
	public void sendToServer(DataOutput out) throws IOException;

	public void serverSideProcess(DataInput in, GameWorld gameWorld) throws IOException;
	
	public void sendToClient(DataOutput out) throws IOException;
	
	public void clientSideProcess(DataInput in, GameWorld gameWorld) throws IOException;
}
