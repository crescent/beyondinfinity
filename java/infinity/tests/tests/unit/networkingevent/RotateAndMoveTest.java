package tests.unit.networkingevent;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import infinity.GameWorld;
import infinity.MyUUID;
import infinity.gameengine.Vector2;
import infinity.gameobjects.Spacecraft;
import infinity.networking.InputStreamByteBufferAdaptor;
import infinity.networking.OutputStreamByteBufferAdaptor;
import infinity.networkingevent.RotateAndMove;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.junit.Before;
import org.junit.Test;



public class RotateAndMoveTest extends GameNetworkingEventTest {

	@Test
	public void endToEnd() throws IOException {
		MyUUID spacecraftId = MyUUID.random();
		new RotateAndMove(spacecraftId, new Vector2(1,1)).sendToServer(clientOut);
		
		sendToServer();
		
		Spacecraft serverSpacecraft = mock(Spacecraft.class);
		when(serverGameWorld.getGameObject(spacecraftId)).thenReturn(serverSpacecraft);
		doNothing().when(serverSpacecraft).rotateAndMove(new Vector2(1,1));
		when(serverSpacecraft.getVelocity(new Vector2(1,1))).thenReturn(new Vector2(1,1));
		when(serverSpacecraft.getPosition(new Vector2())).thenReturn(new Vector2(2,2));
		when(serverSpacecraft.getDirection()).thenReturn(1.0f);
		
		RotateAndMove serverRotateAndMove = new RotateAndMove();
		serverRotateAndMove.serverSideProcess(serverIn, serverGameWorld);
		serverRotateAndMove.sendToClient(serverOut);
		
		Spacecraft clientSpacecraft = mock(Spacecraft.class);
		when(clientGameWorld.getGameObject(spacecraftId)).thenReturn(clientSpacecraft);
		
		sendToClient();
		
		RotateAndMove rotateAndMove = new RotateAndMove();
		rotateAndMove.clientSideProcess(clientIn, clientGameWorld);
		
		verify(serverSpacecraft).rotateAndMove(new Vector2(1,1));
		verify(serverSpacecraft).getVelocity(new Vector2(1,1));
		verify(serverSpacecraft).getPosition(new Vector2());
		verify(serverSpacecraft).getDirection();
		verifyNoMoreInteractions(serverSpacecraft);
		
		verify(clientSpacecraft).event(rotateAndMove);
	}
	
	@Test
	public void withDeadSpacecraft() throws IOException {
		MyUUID spacecraftId = MyUUID.random();

		new RotateAndMove(spacecraftId, new Vector2(1,1)).sendToServer(clientOut);
		
		sendToServer();
		
		when(serverGameWorld.getGameObject(spacecraftId)).thenReturn(null);
		
		RotateAndMove serverRotateAndMove = new RotateAndMove();
		serverRotateAndMove.serverSideProcess(serverIn, serverGameWorld);
		serverRotateAndMove.sendToClient(serverOut);
		
		//Spacecraft clientSpacecraft = mock(Spacecraft.class);
		when(clientGameWorld.getGameObject(spacecraftId)).thenReturn(null);
		
		sendToClient();
		
		new RotateAndMove().clientSideProcess(clientIn, clientGameWorld);
	}
}
