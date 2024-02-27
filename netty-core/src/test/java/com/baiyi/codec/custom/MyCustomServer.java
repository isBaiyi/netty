package com.baiyi.codec.custom;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: 自定义编解码器服务端
 * @author: liaozicai
 * @date: 2024/2/23 10:49
 */
public class MyCustomServer {

	private static final Logger log = LoggerFactory.getLogger(MyCustomServer.class);

	public static void main(String[] args) {
		new ServerBootstrap()
				.channel(NioServerSocketChannel.class)
				.group(new NioEventLoopGroup())
				.childHandler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						ch.pipeline()
								.addLast(new LoggingHandler())
								.addLast(new MyByteToLongDecoder())
								.addLast(new ChannelInboundHandlerAdapter() {
									@Override
									public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
										/* if (msg instanceof Long) {
											log.info("msg is {}", msg);
										} */
										log.info("msg is {}", msg.toString());
									}
								});
					}
				}).bind(8000);
	}
}
