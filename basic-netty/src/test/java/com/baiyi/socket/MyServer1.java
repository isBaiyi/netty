package com.baiyi.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 服务端
 * @author: liaozicai
 * @date: 2024/1/9 16:21
 */
public class MyServer1 {

	public static void main(String[] args) throws IOException {
		// 1. 创建连接信息的severSocketChannel，这是用来处理连接
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		// 2. 服务端监听的端口，需要做绑。客户端访问的时候ip+端口，端口就是这个，ip就是服务端自己运行的服务器的ip地址
		serverSocketChannel.bind(new InetSocketAddress(8000));

		// 用来保存socketChannel
		List<SocketChannel> socketChannelList = new ArrayList<>();

		// 用来存储数据的buffer
		ByteBuffer buffer = ByteBuffer.allocate(1024);

		// 开启监听，监听对于客户端的连接，因为监听是一直进行的，所以需要写在死循环里面，不能停，来一个接收一个连接
		while (true) {
			System.out.println("准备进行客户端连接，等待客户端连接");

			// 建立连接通道，用于传输数据
			SocketChannel socketChannel = serverSocketChannel.accept();
			socketChannelList.add(socketChannel);

			System.out.println("客户端连接完成，对应的channel为: " + socketChannel);

			// 遍历集合，取出数据进行输出
			for (SocketChannel channel : socketChannelList) {
				channel.read(buffer);
				buffer.flip();
				String data = StandardCharsets.UTF_8.decode(buffer).toString();
				System.out.println("接收到的数据为: " + data);
				buffer.clear();
			}
		}
	}

}
