package com.baiyi.future;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @description: netty 服务端
 * @author: liaozicai
 * @date: 2024/1/17 14:48
 */
public class MyNettyServer {
	public static void main(String[] args) {
		// 构建服务端
		ServerBootstrap bootstrap = new ServerBootstrap();
		// 绑定channel
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.group(new NioEventLoopGroup());

	}
}
