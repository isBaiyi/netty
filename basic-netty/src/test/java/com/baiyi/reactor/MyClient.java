package com.baiyi.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * @description:
 * @author: liaozicai
 * @date: 2024/1/14 21:24
 */
public class MyClient {

	public static void main(String[] args) throws IOException {
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.connect(new InetSocketAddress(8000));
		socketChannel.write(StandardCharsets.UTF_8.encode("hello\nstar-bright\n"));
	}

}
