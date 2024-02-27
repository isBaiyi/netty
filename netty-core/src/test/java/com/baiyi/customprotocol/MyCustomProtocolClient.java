package com.baiyi.customprotocol;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

/**
 * @description: 客户端
 * @author: liaozicai
 * @date: 2024/2/23 14:39
 */
public class MyCustomProtocolClient {
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
								.addLast(new MyMessageToByteEncoder());
					}
				})
				.connect(new InetSocketAddress(8000)).sync().channel();

		// 客户端只负责发数据，数据为Java对象，其他的通过编码器进行封装
		channel.writeAndFlush(new Message("xiaoliao", "123456"));
	}
}
