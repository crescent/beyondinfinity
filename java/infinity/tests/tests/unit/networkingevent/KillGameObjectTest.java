package tests.unit.networkingevent;

import static org.mockito.Mockito.*;
import infinity.MyUUID;
import infinity.gameengine.Vector2;
import infinity.gameobjects.Spacecraft;
import infinity.networkingevent.FireBullet;
import infinity.networkingevent.KillGameObject;
import infinity.networkingevent.RotateAndMove;

import java.io.IOException;

import org.junit.Test;



public class KillGameObjectTest extends GameNetworkingEventTest {
	@Test
	public void endToEnd() throws IOException {
		MyUUID spacecraftId = MyUUID.random();
		
		new KillGameObject(spacecraftId).sendToServer(clientOut);
		new KillGameObject().serverSideProcess(clientIn, clientGameWorld);
		
		clientToServerByteBuffer.flip();
		
		new KillGameObject(spacecraftId).sendToClient(serverOut);
		
		sendToClient();
		
		Spacecraft clientSpacecraft = mock(Spacecraft.class);
		when(clientGameWorld.getGameObject(spacecraftId)).thenReturn(clientSpacecraft);

		KillGameObject killGameObject = new KillGameObject();
		doNothing().when(clientSpacecraft).event(killGameObject);
		
		killGameObject.clientSideProcess(clientIn, clientGameWorld);

		verify(clientSpacecraft).event(killGameObject);
		verifyNoMoreInteractions(clientSpacecraft);
	}
}
