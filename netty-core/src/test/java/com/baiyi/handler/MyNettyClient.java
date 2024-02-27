package com.baiyi.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

/**
 * @description: 客户端
 * @author: liaozicai
 * @date: 2024/1/29 22:06
 */
public class MyNettyClient {

	public static void main(String[] args) throws InterruptedException {
		NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		ChannelFuture connect = bootstrap.channel(NioSocketChannel.class)
				.group(nioEventLoopGroup)
				.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline()
								.addLast(new LoggingHandler())
								.addLast(new StringEncoder())
								.addLast(new ChannelInboundHandlerAdapter() {
									@Override
									public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
										// 这里获取分配器，ALLOCATOR配置的就是这个分配器
										ByteBufAllocator alloc = ctx.alloc();
										ByteBuf buffer = alloc.buffer();
									}
								});
					}
				}).connect(new InetSocketAddress(8000));

		ChannelFuture sync = connect.sync();
		sync.channel().writeAndFlush("hello netty");

		nioEventLoopGroup.shutdownGracefully();
	}
}
