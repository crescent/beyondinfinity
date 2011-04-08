package infinity.networking;

import infinity.GameWorld;
import infinity.MyMath;
import infinity.gameobjectproperties.ColloidableObject;
import infinity.gameobjects.GameObject;
import infinity.networkingevent.GameEventsList;
import infinity.networkingevent.GameNetworkingEvent;

import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Stack;

public class GameServerCommand {
	private GameServerCommandType commandType;
	private GameNetworkingEvent event;
	GameEventsList events = new GameEventsList();

	public GameServerCommand create(GameNetworkingEvent event) {
		this.commandType = GameServerCommandType.network_event;
		this.event = event;
		return this;
	}
	
	void processIncoming(GameWorld gameWorld, DataInput in) throws IOException,
			InstantiationException, IllegalAccessException, ClassNotFoundException {
		commandType = GameServerCommandType.valueOf(in.readUTF());

		if (commandType == GameServerCommandType.network_event)
			processNetworkEvent(gameWorld, in);
		
		in.readFully(packetDemarkerClone);
	}

	private void processNetworkEvent(GameWorld gameWorld, DataInput in) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		event = events.get(in.readInt());
		if(!gameWorld.isClient()) event.serverSideProcess(in, gameWorld);
		else event.clientSideProcess(in, gameWorld);
	}

	private void write(DataOutputStream out, boolean isClientSide) throws IOException {

		out.writeUTF(commandType.toString());

		if(commandType == GameServerCommandType.network_event) {
			if(isClientSide) event.sendToServer(out);
			else event.sendToClient(out);
		}

		out.write(packetDemarker);
		out.flush();

	}

	public void send(ClientInfo clientInfo) throws IOException {
		send(clientInfo, true);
	}
	
	public void send(ClientInfo clientInfo, boolean isClientSide) throws IOException {
		
		ByteBuffer byteBuffer = clientInfo.getWriteByteBuffer();
		write(clientInfo.getDataOutputStream(), isClientSide);
		byteBuffer.flip();

		clientInfo.getSocketChannel().configureBlocking(false);
		try {
			totalBytesWritten += clientInfo.getSocketChannel().write(byteBuffer);
			//System.out.println(bytesWritten + " bytes written");
		} catch (Exception e) {
			System.out.println("FAILED" + e);
		}
		byteBuffer.compact();
	}
	
	public long totalBytesWritten=0;
	public long totalBytesRead=0;

	public boolean extractAndProcess(ClientInfo clientInfo, GameWorld gameWorld)
			throws IOException, InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		ByteBuffer byteBuffer = clientInfo.getReadByteBuffer();
		totalBytesRead += clientInfo.getSocketChannel().read(byteBuffer);

		if (containsCompletePacket(byteBuffer)) {
			byteBuffer.flip();

			processIncoming(gameWorld, clientInfo.getDataInputStream());

			byteBuffer.compact(); // Remove object read from buffer

			return true;
		}
		return false;
	}

	public static byte[] packetDemarker = new byte[] { -1, 0, -1, 0, 1, 0, 1, 0 };
	public static byte[] packetDemarkerClone = packetDemarker.clone();

	public static boolean containsCompletePacket(ByteBuffer byteBuffer) {
		// A written to byteBuffer

		for (int i = 0; i <= byteBuffer.position() - packetDemarker.length; i++) {
			int j = 0;

			while (j < packetDemarker.length
					&& byteBuffer.array()[byteBuffer.arrayOffset() + i + j] == packetDemarker[j])
				j++;

			if (j == packetDemarker.length)
				return true;
		}
		return false;
	}
}
