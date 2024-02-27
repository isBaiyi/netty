package com.baiyi.channel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @description:
 * @author: liaozicai
 * @date: 2024/1/25 17:50
 */
public class MyNettyClient {

	private static final Logger log = LoggerFactory.getLogger(MyNettyClient.class);

	public static void main(String[] args) {
		NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		ChannelFuture connect = bootstrap.channel(NioSocketChannel.class)
				.group(nioEventLoopGroup)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline()
								// 添加loggingHandler是为了查看日志
								.addLast(new LoggingHandler())
								.addLast(new StringEncoder());
					}
				})
				.connect(new InetSocketAddress(8000));

		connect.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				log.info("连接成功!");
				// 注意，只使用write，只会写到程序的ByteBuf中，不会发送出去，需要调用flush才能发送
				Channel channel = future.channel();
				channel.writeAndFlush("hello netty");
				channel.close().sync();
				log.info("执行异常善后的工作，去关闭一些资源");
			}
		});

		// 优雅关闭，结束所有线程
		nioEventLoopGroup.shutdownGracefully();
	}
}
