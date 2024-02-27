package com.baiyi.handler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description:
 * @author: liaozicai
 * @date: 2024/2/26 16:47
 */
public class MyNettyServer4 {

	private static final Logger log = LoggerFactory.getLogger(MyNettyServer4.class);

	public static void main(String[] args) {
		LoggingHandler loggingHandler = new LoggingHandler();
		StringDecoder stringDecoder = new StringDecoder();
		new ServerBootstrap()
				.channel(NioServerSocketChannel.class)
				.group(new NioEventLoopGroup())
				.childHandler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						ch.pipeline()
								.addLast(stringDecoder)
								.addLast(loggingHandler)
								.addLast(new ChannelInboundHandlerAdapter() {
									@Override
									public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
										log.info("msg is {}", msg);
									}
								});
					}
				}).bind(8000);
	}
}
