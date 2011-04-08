package infinity.networking;

import infinity.GameWorld;
import infinity.MyMath;
import infinity.gameobjects.GameObject;
import infinity.networkingevent.AddGameObject;
import infinity.networkingevent.GameNetworkingEvent;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class GameServerClient {

	private ClientInfo clientInfo;
	private GameServerCommand gameServerCommand = new GameServerCommand();

	public void connectToServer(String address, int port) throws IOException {

		SocketChannel socketChannel;

		socketChannel = SocketChannel.open();
		socketChannel.connect(new InetSocketAddress(InetAddress.getByName(address), port));
		socketChannel.finishConnect();
		socketChannel.configureBlocking(false);
		clientInfo = new ClientInfo(socketChannel);
	}

	public void sendAdd(GameObject gameObject) throws IOException {
		gameServerCommand.create(new AddGameObject(gameObject)).send(clientInfo);
	}

	public void sendNetworkEvent(GameNetworkingEvent e) throws IOException {
		gameServerCommand.create(e).send(clientInfo);
	}

	public void processIncoming(GameWorld gameWorld) throws IOException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		while(gameServerCommand.extractAndProcess(clientInfo, gameWorld));
	}
}
