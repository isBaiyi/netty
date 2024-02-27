package com.baiyi.halfPackAndStickyPack.fixLengthFrameDecoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: 服务端
 * @author: liaozicai
 * @date: 2024/2/5 11:38
 */
public class MyNettyServer {

	private static final Logger log = LoggerFactory.getLogger(MyNettyServer.class);

	public static void main(String[] args) {
		NioEventLoopGroup boss = new NioEventLoopGroup(1);
		NioEventLoopGroup worker = new NioEventLoopGroup(2);

		new ServerBootstrap()
				.channel(NioServerSocketChannel.class)
				.group(boss, worker)
				.childHandler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						ch.pipeline()
								// 通过定长为15进行解决封帧的问题
								.addLast(new FixedLengthFrameDecoder(15))
								.addLast(new LoggingHandler())
								.addLast("handler1", new ChannelInboundHandlerAdapter() {
									@Override
									public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
										log.info("handler1 receive msg is {}", msg);
										super.channelRead(ctx, msg);
									}
								});
					}
				}).bind(8000);
	}
}
