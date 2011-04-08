package tests.unit.networkingevent;

import static org.mockito.Mockito.*;
import infinity.MyUUID;
import infinity.gameengine.Vector2;
import infinity.gameobjects.Spacecraft;
import infinity.networkingevent.FireBullet;
import infinity.networkingevent.RotateAndMove;

import java.io.IOException;

import org.junit.Test;


public class FireBulletTest extends GameNetworkingEventTest {
	@Test
	public void endToEnd() throws IOException {
		MyUUID spacecraftId = MyUUID.random();
		new FireBullet(spacecraftId).sendToServer(clientOut);
		
		sendToServer();
		
		Spacecraft serverSpacecraft = mock(Spacecraft.class);
		when(serverGameWorld.getGameObject(spacecraftId)).thenReturn(serverSpacecraft);
		doNothing().when(serverSpacecraft).FireBullet();
		when(serverSpacecraft.Energy()).thenReturn(99.1f);
		
		FireBullet serverFireBullet = new FireBullet();
		serverFireBullet.serverSideProcess(serverIn, serverGameWorld);
		serverFireBullet.sendToClient(serverOut);
		
		sendToClient();
		
		FireBullet fireBullet = new FireBullet();
		Spacecraft clientSpacecraft = mock(Spacecraft.class);
		when(clientGameWorld.getGameObject(spacecraftId)).thenReturn(clientSpacecraft);
		doNothing().when(clientSpacecraft).event(fireBullet);

		fireBullet.clientSideProcess(clientIn, clientGameWorld);

		verify(serverSpacecraft).FireBullet();
		verify(serverSpacecraft).Energy();
		verifyNoMoreInteractions(serverSpacecraft);
		
		verify(clientSpacecraft).event(fireBullet);
	}

	@Test
	public void withDeadSpacecraft() throws IOException {
		MyUUID spacecraftId = MyUUID.random();

		new FireBullet(spacecraftId).sendToServer(clientOut);
		
		sendToServer();
		
		when(serverGameWorld.getGameObject(spacecraftId)).thenReturn(null);
		
		FireBullet serverRotateAndMove = new FireBullet();
		serverRotateAndMove.serverSideProcess(serverIn, serverGameWorld);
		serverRotateAndMove.sendToClient(serverOut);
		
		sendToClient();
		
		when(clientGameWorld.getGameObject(spacecraftId)).thenReturn(null);
		
		new FireBullet().clientSideProcess(clientIn, clientGameWorld);
	}
}
