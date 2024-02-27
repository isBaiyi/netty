package com.baiyi.handler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @description: 服务端
 * @author: liaozicai
 * @date: 2024/1/29 21:59
 */
@Slf4j
public class MyNettyServer {

	public static void main(String[] args) {
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		NioEventLoopGroup boss = new NioEventLoopGroup(1);
		NioEventLoopGroup worker = new NioEventLoopGroup(2);
		serverBootstrap.channel(NioServerSocketChannel.class)
				.group(boss, worker)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						// 添加多个handler
						ch.pipeline()
								.addLast(new LoggingHandler())
								// 添加handler用于接收数据，用ChannelInboundHandlerAdapter类型
								.addLast("handler1", new ChannelInboundHandlerAdapter() {
									@Override
									public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
										log.info("handler1 execute, before msg is {}", msg);
										ByteBuf byteBuf = (ByteBuf) msg;
										String result = byteBuf.toString(StandardCharsets.UTF_8);
										log.info("handler1 execute, decode after msg is {}", result);
										super.channelRead(ctx, result);
									}
								})
								.addLast("handler2", new ChannelInboundHandlerAdapter() {
									@Override
									public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
										log.info("handler2 execute, msg is {}", msg);
										super.channelRead(ctx, msg);
									}
								})
								// 添加输出Handler，也就是ChannelOutboundHandlerAdapter
								.addLast("handler5", new ChannelOutboundHandlerAdapter() {
									@Override
									public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
										log.info("handler5 execute");
										super.write(ctx, msg, promise);
									}
								})

								.addLast("handler3", new ChannelInboundHandlerAdapter() {
									@Override
									public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
										log.info("handler3 execute, msg is {}", msg);
										ctx.writeAndFlush("服务端往外写数据");
										super.channelRead(ctx, msg);
									}
								})

								// 下面添加输出Handler，也就是ChannelOutboundHandlerAdapter
								.addLast("handler4", new ChannelOutboundHandlerAdapter() {
									@Override
									public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
										log.info("handler4 execute");
										super.write(ctx, msg, promise);
									}
								})
								.addLast("handler6", new ChannelOutboundHandlerAdapter() {
									@Override
									public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
										log.info("handler6 execute");
										super.write(ctx, msg, promise);
									}
								});
					}
				}).bind(8000);
	}

}
