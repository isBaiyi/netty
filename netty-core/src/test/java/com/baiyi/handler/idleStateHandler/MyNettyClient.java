package com.baiyi.handler.idleStateHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

/**
 * @description: IdleStateHandler客户端
 * @author: liaozicai
 * @date: 2024/2/26 14:10
 */
public class MyNettyClient {
	public static void main(String[] args) throws InterruptedException {
		Channel channel = new Bootstrap()
				.channel(NioSocketChannel.class)
				.group(new NioEventLoopGroup())
				.handler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						ch.pipeline()
								.addLast(new LoggingHandler())
								.addLast(new StringEncoder());
					}
				})
				.connect(new InetSocketAddress(8000)).sync().channel();

		channel.writeAndFlush("hello netty");
	}
}
