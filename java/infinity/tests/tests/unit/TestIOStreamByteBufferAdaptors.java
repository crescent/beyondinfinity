package tests.unit;

import infinity.MyContentManager;
import infinity.networking.InputStreamByteBufferAdaptor;
import infinity.networking.OutputStreamByteBufferAdaptor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

import junit.framework.TestCase;

public class TestIOStreamByteBufferAdaptors extends TestCase {
	
		public void testInput1() throws IOException  {
			// TODO Auto-generated method stub
			ByteBuffer byteBuffer = ByteBuffer.allocate(1000);
			byteBuffer.put((byte) 100);
			byteBuffer.put((byte) 101);
			
			byte[] input = new byte[] { 1,2,3,4,5,6,7,8};
			byteBuffer.put(input);
			
			byteBuffer.flip();

			InputStreamByteBufferAdaptor myInputStream = new InputStreamByteBufferAdaptor(byteBuffer);
			assertEquals(100, myInputStream.read());
			assertEquals(101, myInputStream.read()); 

			byte[] output = new byte[20];
			assertEquals(8, myInputStream.read(output, 1, 15));
			assertEquals(  1, output[1]);
			assertEquals(  2, output[2]);
			assertEquals(  3, output[3]);
		}
		
		public void testOutput1() throws IOException  {
			// TODO Auto-generated method stub
			ByteBuffer byteBuffer = ByteBuffer.allocate(1000);

			OutputStreamByteBufferAdaptor myOutputStream = new OutputStreamByteBufferAdaptor(byteBuffer);
			myOutputStream.write(100);
			myOutputStream.write(101);

			byte[] input = new byte[] { 1,2,3,4,5,6,7,8};
			myOutputStream.write(input);
			
			byteBuffer.flip();

			assertEquals(100, byteBuffer.get());
			assertEquals(101, byteBuffer.get()); 

			byte[] output = new byte[20];
			byteBuffer.get(output,0,byteBuffer.remaining());
			//assertEquals(8, ));
			assertEquals(  1, output[0]);
			assertEquals(  2, output[1]);
			assertEquals(  3, output[2]);
		}
		
		public void testObjectInputOutput1() throws IOException  {
			// TODO Auto-generated method stub
			ByteBuffer byteBuffer = ByteBuffer.allocate(1000);

			DataOutputStream myOutputStream = new DataOutputStream(new OutputStreamByteBufferAdaptor(byteBuffer));
			myOutputStream.writeByte(100);
			myOutputStream.writeByte(101);

			byte[] input = new byte[] { 1,2,3,4,5,6,7,8};
			myOutputStream.write(input);
			myOutputStream.flush();
			
			byteBuffer.flip();
			
			System.out.print(byteBuffer.remaining());
			DataInputStream myInputStream = new DataInputStream(new InputStreamByteBufferAdaptor(byteBuffer));

			//assertEquals(100, byteBuffer.get());
			assertEquals((byte)100, myInputStream.readByte());
			assertEquals((byte)101, myInputStream.readByte()); 

			byte[] output = new byte[20];
			myInputStream.read(output);
			//assertEquals(8, ));
			assertEquals(  1, output[0]);
			assertEquals(  2, output[1]);
			assertEquals(  3, output[2]);
		}
		
}
