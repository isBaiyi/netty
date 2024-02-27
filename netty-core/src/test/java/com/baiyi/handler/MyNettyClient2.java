package com.baiyi.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @description: 客户端
 * @author: liaozicai
 * @date: 2024/1/29 22:06
 */
public class MyNettyClient2 {

	private static final Logger log = LoggerFactory.getLogger(MyNettyClient2.class);

	public static void main(String[] args) throws InterruptedException {
		NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		ChannelFuture connect = bootstrap.channel(NioSocketChannel.class)
				.group(nioEventLoopGroup)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline()
								.addLast(new LoggingHandler())
								.addLast(new StringEncoder())
								.addLast(new StringDecoder())
								.addLast(new ChannelInboundHandlerAdapter() {
									@Override
									public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
										log.info("msg is {}", msg);
									}
								});
					}
				}).connect(new InetSocketAddress(8000));
		ChannelFuture sync = connect.sync();
		sync.channel().writeAndFlush("hello netty");
	}
}
