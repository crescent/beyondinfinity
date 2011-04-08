package tests.unit.networkingevent;

import static org.mockito.Mockito.*;
import infinity.MyUUID;
import infinity.gameengine.Vector2;
import infinity.gameobjects.Bullet;
import infinity.gameobjects.Spacecraft;
import infinity.networkingevent.Collide;
import infinity.networkingevent.FireBullet;
import infinity.networkingevent.KillGameObject;
import infinity.networkingevent.RotateAndMove;

import java.io.IOException;

import org.junit.Test;



public class CollideTest extends GameNetworkingEventTest {
	@Test
	public void endToEnd() throws IOException {
		
		MyUUID spacecraftId1 = MyUUID.random();
		MyUUID bulletId1 = MyUUID.random();
		new Collide(spacecraftId1,bulletId1).sendToServer(clientOut);
		new Collide().serverSideProcess(clientIn, clientGameWorld);
		
		clientToServerByteBuffer.flip();
		
		new Collide(spacecraftId1,bulletId1).sendToClient(serverOut);
		
		sendToClient();
		
		Spacecraft spacecraft = mock(Spacecraft.class);
		Bullet bullet = mock(Bullet.class);
		when(clientGameWorld.getGameObject(spacecraftId1)).thenReturn(spacecraft);
		when(clientGameWorld.getGameObject(bulletId1)).thenReturn(bullet);

		Collide Collide = new Collide();
		doNothing().when(spacecraft).CollidedWith(bullet);
		doNothing().when(bullet).CollidedWith(spacecraft);
		
		Collide.clientSideProcess(clientIn, clientGameWorld);

		verify(spacecraft).CollidedWith(bullet);
		verifyNoMoreInteractions(spacecraft);
		verify(bullet).CollidedWith(spacecraft);
		verifyNoMoreInteractions(bullet);
	}

	@Test
	public void withDeadGameObjects() throws IOException {

		MyUUID spacecraftId1 = MyUUID.random();
		MyUUID bulletId1 = MyUUID.random();
		new Collide(spacecraftId1,bulletId1).sendToServer(clientOut);
		new Collide().serverSideProcess(clientIn, clientGameWorld);
		
		clientToServerByteBuffer.flip();
		
		new Collide(spacecraftId1,bulletId1).sendToClient(serverOut);
		
		sendToClient();
		
		Spacecraft spacecraft = mock(Spacecraft.class);
		Bullet bullet = mock(Bullet.class);
		when(clientGameWorld.getGameObject(spacecraftId1)).thenReturn(null);
		when(clientGameWorld.getGameObject(bulletId1)).thenReturn(null);

		new Collide().clientSideProcess(clientIn, clientGameWorld);
	}
}
