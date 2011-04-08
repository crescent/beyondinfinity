package tests.integration;

import java.io.IOException;

import junit.framework.TestCase;
import infinity.GameWorld;
import infinity.MyContentManager;
import infinity.gameengine.Vector2;
import infinity.gameobjects.Spacecraft;
import infinity.networking.*;
import infinity.networkingevent.AddGameObject;

public class TestServer extends TestCase {
	public void test1() throws Exception {
		MyContentManager.LoadContent(this);
		// TODO Auto-generated method stub
		GameServer server = new GameServer();
		server.start(5001);
		
		GameServerClient client = new GameServerClient();
		client.connectToServer("127.0.0.1",5001);
		Spacecraft spacecraft = Spacecraft.CreatePlayer(null, new Vector2(1,2));
		client.sendAdd(spacecraft);
		
		server.update();
		server.update();
		//server.update();
		
		server.stop();
	}
	
	public void testSendToAll() throws Exception {
		MyContentManager.LoadContent(this);

		GameServer server = new GameServer();
		server.start(5001);
		
		GameServerClient client1 = new GameServerClient();
		GameServerClient client2 = new GameServerClient();
		client1.connectToServer("127.0.0.1",5001);
		client2.connectToServer("127.0.0.1",5001);
		
		server.update();
		server.update();

		Spacecraft spacecraft = Spacecraft.CreatePlayer(null, new Vector2(1,2));
		//client1.add(spacecraft);
		//client.send("Hello!q");
		GameServerCommand gameServerCommand = new GameServerCommand();
		gameServerCommand.create(new AddGameObject(spacecraft));
		server.sendToAll(gameServerCommand);
		
		//server.update();
		//server.update();
		
		client1.processIncoming(new GameWorld(null,null,client1));
		client1.processIncoming(new GameWorld(null,null,client1));
		client2.processIncoming(new GameWorld(null,null,client2));
		client2.processIncoming(new GameWorld(null,null,client2));
		//server.update();
		
		server.stop();
	}
}
