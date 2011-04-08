package tests.unit;

import static org.junit.Assert.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import infinity.MyUUID;
import infinity.networking.InputStreamByteBufferAdaptor;
import infinity.networking.OutputStreamByteBufferAdaptor;

import org.junit.Test;



public class MyUUIDTest {

	@Test
	public void equal() throws IOException {
		MyUUID uuid = MyUUID.random();
		
		ByteBuffer byteBuffer = ByteBuffer.allocate(1000);
		DataOutputStream out = new DataOutputStream(new OutputStreamByteBufferAdaptor(byteBuffer));
		DataInputStream in = new DataInputStream(new InputStreamByteBufferAdaptor(byteBuffer));

		uuid.writeExternal(out);
		byteBuffer.flip();
		
		MyUUID recievedUuid = MyUUID.random();
		
		assertFalse(recievedUuid.equals(uuid));
		
		recievedUuid.readExternal(in);
		
		assertEquals(uuid, recievedUuid);
	}
}
