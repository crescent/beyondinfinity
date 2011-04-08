package tests.unit.networkingevent;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import infinity.gameengine.Vector2;
import infinity.gameobjects.GameObject;
import infinity.gameobjects.Spacecraft;
import infinity.networkingevent.AddGameObject;
import infinity.networkingevent.FireBullet;
import infinity.networkingevent.KillGameObject;
import infinity.networkingevent.RotateAndMove;

import java.awt.Graphics2D;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class AddGameObjectTest extends GameNetworkingEventTest {

	@Test
	public void endToEnd() throws IOException {
		
		new AddGameObject(new StubGameObject("hello")).sendToServer(clientOut);
		
		sendToServer();
		
		AddGameObject addGameObject = new AddGameObject();
		addGameObject.serverSideProcess(serverIn, serverGameWorld);
		
		verify(serverGameWorld).add(new StubGameObject("hello"));
		
		addGameObject.sendToClient(serverOut);
		
		sendToClient();
		
		new AddGameObject().clientSideProcess(clientIn, clientGameWorld);

		verify(clientGameWorld).add(new StubGameObject("hello"));
	}
}
