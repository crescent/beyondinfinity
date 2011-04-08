package infinity.networking;

import infinity.GameWorld;
import infinity.MyContentManager;
import infinity.MyMath;
import infinity.gameengine.Game;
import infinity.gameengine.Vector2;
import infinity.gameobjects.GameObject;
import infinity.networkingevent.AddGameObject;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Externalizable;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.JFrame;

public class GameServer extends Game {

	public static void main(String[] args) {
		new GameServer().run();
	}

	private ServerSocketChannel serverSocketChannel;
	private Selector selector;
	Hashtable<SocketChannel, ClientInfo> clientInfoList = new Hashtable<SocketChannel, ClientInfo>();
	int port = 5000;
	
	public GameServer()
	{
		this(5000);
	}

	public GameServer(int port)
	{
		super("Infinity server");
		this.port = port;
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

	public void update() {
		try {
			gameWorld.update();
			gameWorld.checkCollisions(this);
			selector.selectNow();
			//selector.select(); // BLOCK
			processData();
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void processData() throws IOException, ClosedChannelException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		for (SelectionKey selectionKey : selector.selectedKeys()) {
			if (selectionKey.isAcceptable()) {
				SocketChannel socketChannel = ((ServerSocketChannel) selectionKey.channel())
						.accept();
				socketChannel.configureBlocking(false);
				socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
				ClientInfo clientInfo = new ClientInfo(socketChannel);
				clientInfoList.put(socketChannel, clientInfo);
				System.out.println("Server recieved client conn!");

				for (GameObject gameObject : gameWorld.getGameObjectsClone()) {
					gameServerCommand.create(new AddGameObject(gameObject))
						.send(clientInfo);
				}

			}
			// if(!selectionKey.isValid())

			if (selectionKey.isReadable()) {
				try {
					processIncomingCommands(clientInfoList.get(selectionKey.channel()));
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
	
	@Override
	protected void sleep(long timeInNanoSeconds) {
		// Not required
		// Utilise sever sleep time to process data
		// This was causing major jerkiness
		/*long startTime = System.nanoTime();
		try {
			while(System.nanoTime() - startTime < timeInNanoSeconds)
			{
				long timeout = (System.nanoTime() - startTime)/1000000;
				if(timeout>0) selector.select(timeout);
				processData();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		super.sleep(timeInNanoSeconds);
	}

	GameServerCommand gameServerCommand = new GameServerCommand();
	GameWorld gameWorld = new GameWorld(new Vector2(), new Vector2(), this);
	//private Frame window;

	private void processIncomingCommands(ClientInfo clientInfo) throws IOException,
			InstantiationException, IllegalAccessException, ClassNotFoundException {

		while(gameServerCommand.extractAndProcess(clientInfo, gameWorld))
		{
			//gameServerCommand.updateWithGameObjectFromGameWorld(gameWorld);
			//if(gameServerCommand.getCommandType() != GameServerCommandType.add)
				sendToAll(gameServerCommand);
		}
	}

	public void sendToAll(GameServerCommand gameServerCommand) throws IOException {
		for (ClientInfo clientInfo : clientInfoList.values()) {
			if(clientInfo.getSocketChannel().isConnected())
			gameServerCommand.send(clientInfo, false);
			else System.out.println("not connected.. sendToAll");
		}
	}
/*
	@Override
	public void run() {
		init();

		gameLoop();
		cleanup();

	}*/

	@Override
	public void cleanup() {
		window.dispose();
		try {
			stop();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Server exit!");
	}

	@Override
	public void gameInit() {
		window = new Frame();
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});
		window.setTitle("Game Server");
		window.setExtendedState(Frame.ICONIFIED);
		window.setVisible(true);

		MyContentManager.LoadContent(this);
		try {
			start(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			exit();
		}
	}

	@Override
	public void render() {
	}
	
	@Override
	public void gameRender() {
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

}
