package com.baiyi.basic;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * @description: netty 客户端
 * @author: liaozicai
 * @date: 2024/1/16 11:41
 */
public class MyNettyClient {

	public static void main(String[] args) throws InterruptedException {
		// 初始化一个客户端启动器
		Bootstrap bootstrap = new Bootstrap();

		// 客户端也要启动一个连接通道，他是做IO和连接的，所以就是sc
		bootstrap.channel(NioSocketChannel.class);

		/*
			客户端为什么也需要一个事件组来处理，或者说为什么客户端也需要一组线程来处理？
			其实很简单：因为netty把客户端也封装成多线程异步，连接服务器只需要执行一次，后续的IO操作被封装为多个线程进行处理，防止某个IO操作出现了阻塞，
			影响了其他IO操作。
		 */
		bootstrap.group(new NioEventLoopGroup());

		bootstrap.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) {
				// 发送数据，这里就需要一个编码器，把bytebuffer转换为字符串进行发出去
				ch.pipeline().addLast(new StringEncoder());
			}
		});

		// 客户端通过端口连接到服务端，需要注意的是连接到服务端是一个异步操作，会返回一个Future，换句话说会新启动一个线程去连接服务端
		ChannelFuture connect = bootstrap.connect(new InetSocketAddress(8000));
		// 此处需要先阻塞，等待连接到服务端后再执行后续的操作，要是连接没好，你下面的操作就没法做，所以这里要阻塞，而不是直接就过去了，毕竟你不知道调度的不确定性。
		connect.sync();

		// 接上了，在这个连接获取通道，也就是当初NIO的SC。此时这里是main线程
		Channel channel = connect.channel();
		// 往服务端写数据，往外写
		channel.writeAndFlush("hello netty");
	}

}
