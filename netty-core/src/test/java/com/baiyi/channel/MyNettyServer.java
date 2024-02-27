package com.baiyi.channel;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description:
 * @author: liaozicai
 * @date: 2024/1/25 17:42
 */
public class MyNettyServer {

	private static final Logger log = LoggerFactory.getLogger(MyNettyServer.class);

	public static void main(String[] args) {
		NioEventLoopGroup boss = new NioEventLoopGroup(1);
		NioEventLoopGroup worker = new NioEventLoopGroup(2);
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.channel(NioServerSocketChannel.class)
				.group(boss, worker)
				.childHandler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						ch.pipeline()
								.addLast(new StringDecoder())
								.addLast(new ChannelInboundHandlerAdapter() {
									@Override
									public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
										log.info("receive msg: {}", msg);
									}
								});
					}
				})
				.bind(8000);


		serverBootstrap.handler(new ChannelInitializer<NioServerSocketChannel>() {
			@Override
			protected void initChannel(NioServerSocketChannel ch) throws Exception {

			}
		});

		serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
			@Override
			protected void initChannel(NioSocketChannel ch) throws Exception {

			}
		});
	}
}
