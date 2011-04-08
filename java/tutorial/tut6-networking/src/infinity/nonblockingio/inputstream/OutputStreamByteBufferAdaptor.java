package infinity.nonblockingio.inputstream;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class OutputStreamByteBufferAdaptor extends OutputStream
{
	public OutputStreamByteBufferAdaptor(ByteBuffer byteBuffer) {
		super();
		this.byteBuffer = byteBuffer;
	}

	ByteBuffer byteBuffer;

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		byteBuffer.put(b, off, len);
	}

	@Override
	public void write(byte[] b) throws IOException {
		byteBuffer.put(b);
	}

	@Override
	public void write(int b) throws IOException {
		byteBuffer.put((byte)b);		
	}
}
