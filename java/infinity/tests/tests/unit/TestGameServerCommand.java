package tests.unit;

import infinity.networking.GameServerCommand;

import java.io.Console;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import org.junit.Test;

import junit.framework.TestCase;

public class TestGameServerCommand extends TestCase {

	//@Test
	public void testname() throws Exception {
		System.out.println(InetAddress.getLocalHost().toString());
	}
	
	public void testContainEmptyPacket()
	{
		ByteBuffer byteBuffer = ByteBuffer.allocate(1000);
		byteBuffer.put(GameServerCommand.packetDemarker);
		assertTrue(GameServerCommand.containsCompletePacket(byteBuffer));
	}

	public void testContainIntPacket()
	{
		ByteBuffer byteBuffer = ByteBuffer.allocate(1000);
		
		byteBuffer.putInt(4);
		byteBuffer.put(GameServerCommand.packetDemarker);
		assertTrue(GameServerCommand.containsCompletePacket(byteBuffer));
		
		byteBuffer.compact();
		assertFalse(GameServerCommand.containsCompletePacket(byteBuffer));
		
		byteBuffer.putInt(4);
		byteBuffer.put(GameServerCommand.packetDemarker);
		assertTrue(GameServerCommand.containsCompletePacket(byteBuffer));
	}

}
