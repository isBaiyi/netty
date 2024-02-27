package com.baiyi.reactor;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: reactor模型的netty服务端
 * @author: liaozicai
 * @date: 2024/1/19 16:28
 */
public class MyNettyReactorServer {

	private static final Logger log = LoggerFactory.getLogger(MyNettyReactorServer.class);

	public static void main(String[] args) {
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		// 一个boss线程，处理accept事件
		NioEventLoopGroup boss = new NioEventLoopGroup(1);
		// 两个worker线程，处理IO操作
		NioEventLoopGroup worker = new NioEventLoopGroup(2);
		// 执行异步操作
		DefaultEventLoopGroup defaultEventLoopGroup = new DefaultEventLoopGroup();
		serverBootstrap
				.channel(NioServerSocketChannel.class)
				.group(boss, worker)
				.childHandler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						ch.pipeline()
								.addLast(new StringDecoder())
								.addLast(new LoggingHandler())
								// 这里在添加处理器的时候，把异步线程池传递进去，这样就使得我们这个处理器里 面的操作交给了这个defaultEventLoopGroup来异步处理
								.addLast(defaultEventLoopGroup, new ChannelInboundHandlerAdapter() {
									@Override
									public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
										log.info("channel {} msg {}", ctx.channel(), msg);
									}
								});
					}
				})
				.bind(8000);
	}
}
