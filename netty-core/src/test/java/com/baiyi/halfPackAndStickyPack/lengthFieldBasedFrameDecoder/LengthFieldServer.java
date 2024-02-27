package com.baiyi.halfPackAndStickyPack.lengthFieldBasedFrameDecoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;

/**
 * @description: 服务端
 * @author: liaozicai
 * @date: 2024/2/21 15:44
 */
public class LengthFieldServer {

	public static void main(String[] args) {
		NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
		NioEventLoopGroup workerGroup = new NioEventLoopGroup(2);
		new ServerBootstrap()
				.channel(NioServerSocketChannel.class)
				.group(bossGroup, workerGroup)
				.childHandler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						ch.pipeline()
								// 这里使用LengthFieldBasedFrameDecoder作为解码器，限制数据长度为1024
								.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 1, 5))
								.addLast(new LoggingHandler());
					}
				})
				.bind(8000);
	}
}
