package com.baiyi.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @description: 客户端
 * @author: liaozicai
 * @date: 2024/1/11 21:34
 */
public class MyClient3 {

	public static void main(String[] args) throws IOException {
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.connect(new InetSocketAddress(8000));
		ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
		int read = 0;
		while (true) {
			read += socketChannel.read(buffer);
			System.out.println("read = " + read);
			buffer.clear();
		}
	}

}
