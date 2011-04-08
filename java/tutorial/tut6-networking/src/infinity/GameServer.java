package infinity;

import java.io.IOException;
import java.net.InetSocketAddress;
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
	
	public static void main(String[] args) {
		GameServer gameServer = new GameServer();
		try {
			gameServer.start(5000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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


	private void processData() throws IOException {
		for (SelectionKey selectionKey : selector.selectedKeys()) {
			if (selectionKey.isAcceptable()) {
				SocketChannel socketChannel = ((ServerSocketChannel) selectionKey.channel()).accept();
				socketChannel.configureBlocking(false);
				socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
				ClientInfo clientInfo = new ClientInfo(socketChannel);
				clientInfoList.put(socketChannel, clientInfo);
				System.out.println("Server recieved client conn!");
			}
			if (selectionKey.isReadable()) {
				try {
					processIncoming(clientInfoList.get(selectionKey.channel()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
					System.out.println(e);
					clientInfoList.remove(selectionKey.channel());
					selectionKey.cancel();
				}

			}
		}
		selector.selectedKeys().clear();
	}

	private void processIncoming(ClientInfo clientInfo) throws IOException{
		//clientInfo.
	}

	public void sendToAll() throws IOException {
		for (ClientInfo clientInfo : clientInfoList.values()) {
			if(clientInfo.getSocketChannel().isConnected());
			//gameServerCommand.send(clientInfo, false);
		}
	}
}
