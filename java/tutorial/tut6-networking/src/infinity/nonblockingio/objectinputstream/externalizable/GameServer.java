package infinity.nonblockingio.objectinputstream.externalizable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.Serializable;
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
	
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

		GameServer gameServer = new GameServer();

		try {
			gameServer.start(5000);
			
			while(true)
			{
				gameServer.acceptConnections();
				gameServer.processIncoming();
//				gameServer.update();
//				gameServer.render();
				//Thread.sleep(100);
			}
		}
		finally {
			gameServer.stop();
		}
	}
	
	public void acceptConnections() throws IOException {
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


	public void processIncoming() throws IOException, ClassNotFoundException{
		selector.selectNow();
		for (SelectionKey selectionKey : selector.selectedKeys()) {
			if (selectionKey.isReadable()) {
				process(clientInfoList.get(selectionKey.channel()));
			}
		}
		selector.selectedKeys().clear();
	}

	private void process(ClientInfo clientInfo) throws IOException, ClassNotFoundException {
		ByteBuffer inputStreamByteBuffer = clientInfo.getReadByteBuffer();
		clientInfo.getSocketChannel().read(inputStreamByteBuffer);

		inputStreamByteBuffer.flip();
		while(inputStreamByteBuffer.remaining()>0)
		{
			//System.out.print((char)inputStreamByteBuffer.get());
			
			RotateAndMove rotateAndMove = (RotateAndMove) 
			clientInfo.getObjectInputStream().readObject();
			
			if(!displayed) System.out.println("server: "+rotateAndMove.toString());
			if(!displayed) displayed=true;
			//sendToAll(rotateAndMove);
		}
		//System.out.println();
		inputStreamByteBuffer.compact();
	}
	boolean displayed = false;
/*
	public void sendToAll(rotateAndMove rotateAndMove) throws IOException {
		for (ClientInfo clientInfo : clientInfoList.values()) {
			clientInfo.getObjectOutputStream().writeObject(rotateAndMove);
		}
	}*/
}
