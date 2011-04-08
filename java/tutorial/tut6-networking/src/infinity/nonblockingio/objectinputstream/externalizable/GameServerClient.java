package infinity.nonblockingio.objectinputstream.externalizable;


import java.io.IOException;
import java.io.DataInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Random;

public class GameServerClient {

	private ClientInfo clientInfo;

	public void connectToServer(String address, int port) throws IOException {

		SocketChannel socketChannel;

		socketChannel = SocketChannel.open();
		socketChannel.connect(new InetSocketAddress(InetAddress.getByName(address), port));
		socketChannel.configureBlocking(false);
		clientInfo = new ClientInfo(socketChannel);
	}

	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException 
	{
		GameServer gameServer = new GameServer();
		gameServer.start(5000);

		GameServerClient gameClient = new GameServerClient();

		gameClient.connectToServer("localhost",5000);
		gameServer.acceptConnections();
		gameClient.clientInfo.getSocketChannel().finishConnect();
		
		RotateAndMove rotateAndMove = new RotateAndMove("spacecraft1", new Vector2(1,2), new Vector2(3,4), 5);
		
		for (int i = 0; i < 10000; i++) {
			gameClient.send(new RotateAndMove("spacecraft1", new Vector2(1,2), new Vector2(3,4), 5));
			//rotateAndMove.getVelocity().X=new Random().nextInt(10);
			//rotateAndMove.getVelocity().Y=11;
			gameServer.processIncoming();
		}
		System.out.println("Client:"+gameClient.totalBytesWritten+" bytes written");

		gameServer.stop();
	}

	private void send(RotateAndMove rotateAndMove) throws IOException {
		clientInfo.getObjectOutputStream().writeObject(rotateAndMove);
		clientInfo.getObjectOutputStream().flush();
		ByteBuffer writeByteBuffer = clientInfo.getWriteByteBuffer();
		writeByteBuffer.flip();
		totalBytesWritten += clientInfo.getSocketChannel().write(writeByteBuffer);
		writeByteBuffer.compact();
	}
	

	public long totalBytesWritten = 0;
}
