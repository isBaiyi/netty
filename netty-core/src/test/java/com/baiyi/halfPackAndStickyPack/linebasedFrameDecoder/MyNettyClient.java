package com.baiyi.halfPackAndStickyPack.linebasedFrameDecoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

/**
 * @description: 客户端
 * @author: liaozicai
 * @date: 2024/2/20 14:31
 */
public class MyNettyClient {
	public static void main(String[] args) throws InterruptedException {
		NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
		Channel channel = new Bootstrap()
				.channel(NioSocketChannel.class)
				.group(nioEventLoopGroup)
				.handler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						ch.pipeline()
								.addLast(new LoggingHandler())
								.addLast(new StringEncoder());
					}
				})
				.connect(new InetSocketAddress(8000))
				.sync().channel();

		// 使用\n作为分隔符进行拆分
		channel.writeAndFlush("What your name?\nMy name is xx.\n");

		nioEventLoopGroup.shutdownGracefully();
	}
}
