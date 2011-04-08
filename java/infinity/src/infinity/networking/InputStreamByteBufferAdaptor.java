package infinity.networking;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class InputStreamByteBufferAdaptor extends InputStream {
	public InputStreamByteBufferAdaptor(ByteBuffer byteBuffer) {
		super();
		this.byteBuffer = byteBuffer;
	}

	ByteBuffer byteBuffer;

	@Override
	public int available() {
		return byteBuffer.remaining();
	}

	@Override
	public synchronized void mark(int arg0) {
		byteBuffer.mark();
	}

	@Override
	public boolean markSupported() {
		return true;
	}

	@Override
	public int read() {

		if (byteBuffer.remaining() == 0)
			return -1;
		byte b = byteBuffer.get();

		// TEST! bLOOdY
		return (((int) b) & 0xff);
	}

	@Override
	public int read(byte[] b, int off, int len) {
		if (len == 0)
			return 0;

		int bytesToRead = byteBuffer.remaining();
		if (byteBuffer.remaining() == 0)
			System.out.println("NO");
		if (bytesToRead == 0)
			return -1;
		if (bytesToRead > len)
			bytesToRead = len;

		byteBuffer.get(b, off, bytesToRead);
		return bytesToRead;
	}

	@Override
	public int read(byte[] arg0)  {
		return read(arg0, 0, arg0.length);
	}

	@Override
	public synchronized void reset() throws IOException {
		byteBuffer.reset();
	}
	/*
	 * Base class take this one - reads into an InputStream until n bytes are
	 * reached
	 * 
	 * @Override public long skip(long arg0) throws IOException {
	 * byteBuffer.position((int) (byteBuffer.position()+arg0)); return
	 * super.skip(arg0); }
	 */
}