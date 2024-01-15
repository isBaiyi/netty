package com.baiyi.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * @description: 客户端
 * @author: liaozicai
 * @date: 2024/1/9 16:59
 */
public class MyClient2 {

	public static void main(String[] args) throws IOException {
		// 1. 创建客户端连接
		SocketChannel socketChannel = SocketChannel.open();
		// 2. 绑定连接的服务端IP端口，本处IP都是本机不用写
		socketChannel.connect(new InetSocketAddress(8000));
		System.out.println("socketChannel = " + socketChannel);
		System.out.println("客户端连接成功");
//		socketChannel.write(StandardCharsets.UTF_8.encode("star-bright\n"));
	}

}
