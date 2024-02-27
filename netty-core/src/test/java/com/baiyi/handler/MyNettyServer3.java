package com.baiyi.handler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: 服务端
 * @author: liaozicai
 * @date: 2024/2/23 15:51
 */
public class MyNettyServer3 {

	private static final Logger log = LoggerFactory.getLogger(MyNettyServer3.class);

	public static void main(String[] args) {
		new ServerBootstrap()
				.channel(NioServerSocketChannel.class)
				.group(new NioEventLoopGroup())
				.childHandler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						ch.pipeline()
								.addLast(new StringDecoder())
								.addLast(new StringEncoder())
								.addLast(new ChannelInboundHandlerAdapter() {
									@Override
									public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
										log.info("channelRead invoke, msg is {}", msg);
										// 模拟空指针异常
										throw new NullPointerException("空指针异常！");
									}

									@Override
									public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
										log.info("exceptionCaught invoke, cause is {}", cause.getMessage());
										if (cause instanceof NullPointerException) {
											// 往客户端发送异常信息
											ctx.channel().writeAndFlush(cause.getMessage());
										}
									}
								});

					}
				}).bind(8000);
	}
}
