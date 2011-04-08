package infinity.nonblockingio.buffering;

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
		//socketChannel.configureBlocking(false);
		clientInfo = new ClientInfo(socketChannel);
	}

	public static void main(String[] args) throws IOException, InterruptedException {

		GameServerClient gameClient = new GameServerClient();

		gameClient.connectToServer("localhost",5000);
		
		while(true)
		{
			gameClient.send('a');
			gameClient.processIncoming();
			gameClient.update();
			gameClient.render();
			Thread.sleep(200);
		}
	}

	private void send(char c) throws IOException {
		clientInfo.getOutputStreamByteBuffer().putChar(c);
	}

	private void processIncoming() throws IOException {
		System.out.println(clientInfo.getInputStreamByteBuffer().getChar());
		
		ByteBuffer byteBuffer = ByteBuffer.allocate(100);
		clientInfo.getSocketChannel().read(byteBuffer);
		byteBuffer.getChar();
		
	}

	private void render() {
	}

	private void update() {
	}
	
}
