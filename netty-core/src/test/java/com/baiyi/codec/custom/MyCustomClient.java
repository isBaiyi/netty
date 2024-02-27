package com.baiyi.codec.custom;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

/**
 * @description: 自定义编码器客户端
 * @author: liaozicai
 * @date: 2024/2/23 10:41
 */
public class MyCustomClient {
	public static void main(String[] args) throws InterruptedException {
		Channel channel = new Bootstrap()
				.channel(NioSocketChannel.class)
				.group(new NioEventLoopGroup())
				.handler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						ch.pipeline()
								.addLast(new LoggingHandler())
								// 自定义编码器
								.addLast(new MyLongToByteEncoder());
					}
				})
				.connect(new InetSocketAddress(8000)).sync().channel();

		// 写数据
		channel.writeAndFlush("10-20");
	}
}
