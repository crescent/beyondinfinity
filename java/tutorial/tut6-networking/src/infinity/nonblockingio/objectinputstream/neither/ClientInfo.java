package infinity.nonblockingio.objectinputstream.neither;


import infinity.nonblockingio.inputstream.InputStreamByteBufferAdaptor;
import infinity.nonblockingio.inputstream.OutputStreamByteBufferAdaptor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ClientInfo {
	private ByteBuffer readByteBuffer;

	private final SocketChannel socketChannel;

	private ByteBuffer writeByteBuffer;

	private ObjectInputStream objectInputStream;

	private ObjectOutputStream objectOutputStream;

	public ClientInfo(SocketChannel socketChannel) throws IOException {
		this.socketChannel = socketChannel;
		readByteBuffer = ByteBuffer.allocate(10000);
		writeByteBuffer = ByteBuffer.allocate(10000);

		objectInputStream = null;
		objectOutputStream = new ObjectOutputStream(new OutputStreamByteBufferAdaptor(writeByteBuffer));

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

	public ObjectInputStream getObjectInputStream() throws IOException {
		objectInputStream = objectInputStream == null ? new ObjectInputStream(new InputStreamByteBufferAdaptor(readByteBuffer)) : objectInputStream;
		return objectInputStream;
	}

	public ObjectOutputStream getObjectOutputStream() {

		return objectOutputStream;
	}
}
