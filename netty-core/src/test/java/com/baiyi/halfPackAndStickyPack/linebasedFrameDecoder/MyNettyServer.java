package com.baiyi.halfPackAndStickyPack.linebasedFrameDecoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;

/**
 * @description: 服务端
 * @author: liaozicai
 * @date: 2024/2/20 14:32
 */
public class MyNettyServer {
	public static void main(String[] args) {
		NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
		NioEventLoopGroup workerGroup = new NioEventLoopGroup(4);
		new ServerBootstrap()
				.channel(NioServerSocketChannel.class)
				.group(bossGroup, workerGroup)
				//.childOption(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(16, 16, 16))
				.childHandler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
						// 这里限制了消息的长度为50，一旦长度超出了50还没有找到分隔符，消息直接就丢掉了，防止消息太长
						// pipeline.addLast(new LineBasedFrameDecoder(50));
						pipeline.addLast(new LineBasedFrameDecoder(5));
						pipeline.addLast(new LoggingHandler());
					}
				})
				.bind(8000);

	}
}
