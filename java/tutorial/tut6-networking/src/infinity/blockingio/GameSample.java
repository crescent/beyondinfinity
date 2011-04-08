package infinity.blockingio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameSample {

	public static void main(String[] args) throws IOException {

		new AcceptConnections(5001).run();
		
		while(true)
		{
			render();
			update();
		}
	}

	private static void update() { 	}

	private static void render() { 	}
}

class AcceptConnections
{
	private final int port;

	public AcceptConnections(int port) {
		this.port = port;
	}

	public void run() throws IOException
	{
		ServerSocket serverSocket = new ServerSocket(port);
		Socket clientConnection = serverSocket.accept();
		
		new HandleClientConnection(clientConnection).run();
	}
}

class HandleClientConnection
{
	private final Socket socket;

	public HandleClientConnection(Socket socket)
	{
		this.socket = socket;
	}
	
	public void run() throws IOException
	{
		int bytes = socket.getInputStream().read();
		process(bytes);
	}

	private void process(int bytes) {
		// player = gameWorld.getPlayer();
		// player.update(bytes);
	}
}


