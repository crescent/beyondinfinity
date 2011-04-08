package tests.performance;

import java.io.IOException;

import org.junit.Ignore;

import junit.framework.TestCase;
import infinity.GameWorld;
import infinity.MyContentManager;
import infinity.gameengine.Vector2;
import infinity.gameobjects.Spacecraft;
import infinity.networking.*;
import infinity.networkingevent.AddGameObject;
import infinity.networkingevent.RotateAndMove;

@Ignore("Performace tests")
public class ServerPerformaceTest extends TestCase {
	
	public void testRotateAndMovePerformace() throws Exception {
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

		client1.sendAdd(spacecraft);
		
		RotateAndMove rotateAndMoveEvent = new RotateAndMove(
				spacecraft.getGameObjectId(), new Vector2());
		
		GameWorld gameWorld1 = new GameWorld(null,null,client1);
		GameWorld gameWorld2 = new GameWorld(null,null,client2);
		
		for (int i = 0; i < 10000; i++) {
			client1.sendNetworkEvent(rotateAndMoveEvent);
			
			server.update();
			
			client1.processIncoming(gameWorld1);
			client2.processIncoming(gameWorld2);
		}

		System.out.println("Client1: Total bytes read = " + client1.getTotalBytesRead());
		System.out.println("Client1: Total bytes written = " + client1.getTotalBytesWritten());
		
		server.stop();
	}
}
