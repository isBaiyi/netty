package com.baiyi.reactor;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @description: netty 客户端
 * @author: liaozicai
 * @date: 2024/1/19 16:41
 */
public class MyNettyClient {

	private static final Logger log = LoggerFactory.getLogger(MyNettyClient.class);

	public static void main(String[] args) {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.channel(NioSocketChannel.class)
				.group(new NioEventLoopGroup())
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new StringEncoder());
					}
				})
				.connect(new InetSocketAddress(8000))
				// 通过添加addListener去监听是否连接到服务端，连接到服务端才会执行下面的操作，主线程可以去处理其他的事情
				.addListener(new ChannelFutureListener() {
					@Override
					public void operationComplete(ChannelFuture future) throws Exception {
						log.info("连接服务端成功");
						// 发送数据
						future.channel().writeAndFlush("hello netty");
					}
				});
	}
}
