package infinity;


import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ClientInfo {
	private ByteBuffer readByteBuffer;

	private final SocketChannel socketChannel;

	private ByteBuffer writeByteBuffer;

	private DataInputStream dataInputStream;

	private DataOutputStream dataOutputStream;

	public ClientInfo(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
		readByteBuffer = ByteBuffer.allocate(10000);
		writeByteBuffer = ByteBuffer.allocate(10000);

		//dataInputStream = new DataInputStream(new InputStreamByteBufferAdaptor(readByteBuffer));
		//dataOutputStream = new DataOutputStream(new OutputStreamByteBufferAdaptor(writeByteBuffer));

	}

	public ByteBuffer getReadByteBuffer() {
		return readByteBuffer;
	}

	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	public ByteBuffer getWriteByteBuffer() {
		return writeByteBuffer;
	}

	public DataInputStream getDataInputStream() {

		return dataInputStream;
	}

	public DataOutputStream getDataOutputStream() {

		return dataOutputStream;
	}
}
