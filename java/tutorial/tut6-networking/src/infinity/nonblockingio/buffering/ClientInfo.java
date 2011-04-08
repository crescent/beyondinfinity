package infinity.nonblockingio.buffering;


import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ClientInfo {
	private final SocketChannel socketChannel;
	private ByteBuffer inputStreamByteBuffer;
	private ByteBuffer outputStreamByteBuffer;

	public ClientInfo(SocketChannel socketChannel) throws IOException {
		this.socketChannel = socketChannel;
		inputStreamByteBuffer = ByteBuffer.allocate(1000);
		outputStreamByteBuffer = ByteBuffer.allocate(1000);
	}

	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	public ByteBuffer getOutputStreamByteBuffer() {
		return outputStreamByteBuffer;
	}

	public ByteBuffer getInputStreamByteBuffer() {
		return inputStreamByteBuffer;
	}
}
