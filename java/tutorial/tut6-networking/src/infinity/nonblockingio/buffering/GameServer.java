package infinity.nonblockingio.buffering;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Hashtable;

public class GameServer  {

	private ServerSocketChannel serverSocketChannel;
	private Selector selector;
	Hashtable<SocketChannel, ClientInfo> clientInfoList = new Hashtable<SocketChannel, ClientInfo>();
	int port = 5000;
	
	public static void main(String[] args) throws IOException, InterruptedException {

		GameServer gameServer = new GameServer();

		try {
			gameServer.start(5000);
			
			while(true)
			{
				gameServer.acceptConnections();
				gameServer.processIncoming();
				gameServer.update();
				gameServer.render();
				//Thread.sleep(100);
			}
		}
		finally {
			gameServer.stop();
		}
	}
	
	private void acceptConnections() throws IOException {
		selector.selectNow();
		for (SelectionKey selectionKey : selector.selectedKeys()) {
			if (selectionKey.isAcceptable()) {
				System.out.println("Server recieved client conn!");
				SocketChannel socketChannel = ((ServerSocketChannel) selectionKey.channel()).accept();
				socketChannel.configureBlocking(false);
				socketChannel.finishConnect();
				socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
				clientInfoList.put(socketChannel, new ClientInfo(socketChannel));
				System.out.println("Server recieved client conn!");
			}
		}
	}

	private void render() {
	}

	private void update() {
	}

	public void start(int port) throws IOException {
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.socket().bind(new InetSocketAddress(port));
		selector = Selector.open();
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
	}

	public void stop() throws IOException {
		serverSocketChannel.close();
	}


	private void processIncoming() throws IOException{
		for (SelectionKey selectionKey : selector.selectedKeys()) {
			if (selectionKey.isReadable()) {
				process(clientInfoList.get(selectionKey.channel()));
			}
		}
		selector.selectedKeys().clear();
	}

	private void process(ClientInfo clientInfo) throws IOException {
		ByteBuffer inputStreamByteBuffer = clientInfo.getInputStreamByteBuffer();
		clientInfo.getSocketChannel().read(inputStreamByteBuffer);

		inputStreamByteBuffer.flip();
		while(inputStreamByteBuffer.remaining()>=2)
		{
			char c = (char) inputStreamByteBuffer.get();
			System.out.println(""+c);
			sendToAll(c);
		}
		inputStreamByteBuffer.compact();
	}

	public void sendToAll(char c) throws IOException {
		for (ClientInfo clientInfo : clientInfoList.values()) {
			ByteBuffer outputStreamByteBuffer = clientInfo.getOutputStreamByteBuffer();
			outputStreamByteBuffer.put((byte) c);
			outputStreamByteBuffer.flip();
			clientInfo.getSocketChannel().write(outputStreamByteBuffer);
			outputStreamByteBuffer.compact();
		}
	}
}
