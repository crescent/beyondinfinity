package tests.unit.networkingevent;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import infinity.GameWorld;
import infinity.networking.InputStreamByteBufferAdaptor;
import infinity.networking.OutputStreamByteBufferAdaptor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.junit.After;
import org.junit.Before;


public class GameNetworkingEventTest {
	protected DataInputStream clientIn;
	protected DataOutputStream serverOut;
	protected ByteBuffer serverToClientByteBuffer;
	
	protected DataInputStream serverIn;
	protected DataOutputStream clientOut;
	protected ByteBuffer clientToServerByteBuffer;

	protected GameWorld serverGameWorld;
	protected GameWorld clientGameWorld;
	
	@Before
	public void setUp()
	{
		clientToServerByteBuffer = ByteBuffer.allocate(1000);
		clientOut = new DataOutputStream(new OutputStreamByteBufferAdaptor(clientToServerByteBuffer));
		serverIn = new DataInputStream(new InputStreamByteBufferAdaptor(clientToServerByteBuffer));
		
		serverToClientByteBuffer = ByteBuffer.allocate(1000);
		serverOut = new DataOutputStream(new OutputStreamByteBufferAdaptor(serverToClientByteBuffer));
		clientIn = new DataInputStream(new InputStreamByteBufferAdaptor(serverToClientByteBuffer));
		
		serverGameWorld = mock(GameWorld.class);
		clientGameWorld = mock(GameWorld.class);
	}
	
	@After
	public void tearDown()
	{
		assertEquals(0, serverToClientByteBuffer.remaining());
		assertEquals(0, clientToServerByteBuffer.remaining());
	}

	protected void sendToClient() throws IOException {
		serverToClientByteBuffer.flip();
		clientIn.readInt();
	}

	protected void sendToServer() throws IOException {
		clientToServerByteBuffer.flip();
		serverIn.readInt();
	}
	

}
