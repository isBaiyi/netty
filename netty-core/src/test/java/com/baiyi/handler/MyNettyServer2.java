package com.baiyi.handler;

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
 * @description: 服务端
 * @author: liaozicai
 * @date: 2024/2/23 15:51
 */
public class MyNettyServer2 {

	private static final Logger log = LoggerFactory.getLogger(MyNettyServer2.class);

	public static void main(String[] args) {
		new ServerBootstrap()
				.channel(NioServerSocketChannel.class)
				.group(new NioEventLoopGroup())
				.childHandler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						ch.pipeline()
								.addLast(new StringDecoder())
								.addLast(new ChannelInboundHandlerAdapter() {
									@Override
									public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
										log.info("channelRegistered invoke");
									}

									@Override
									public void channelActive(ChannelHandlerContext ctx) throws Exception {
										log.info("channelActive invoke");
									}

									@Override
									public void channelInactive(ChannelHandlerContext ctx) throws Exception {
										log.info("channelInactive invoke");
									}

									@Override
									public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
										log.info("channelUnregistered invoke");
									}

									@Override
									public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
										log.info("channelRead invoke");
									}

									@Override
									public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
										log.info("channelReadComplete invoke");
									}

									@Override
									public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
										log.info("handlerRemoved invoke");
									}

									@Override
									public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
										super.exceptionCaught(ctx, cause);
									}
								});

					}
				}).bind(8000);
	}
}
