package infinity;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class GameServerClient {

	private ClientInfo clientInfo;

	public void connectToServer(String address, int port) throws IOException {

		SocketChannel socketChannel;

		socketChannel = SocketChannel.open();
		socketChannel.connect(new InetSocketAddress(InetAddress.getByName(address), port));
		socketChannel.finishConnect();
		socketChannel.configureBlocking(false);
		clientInfo = new ClientInfo(socketChannel);
	}

}
