package com.baiyi.handler.webSocketProtocolHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LoggingHandler;

/**
 * @description: 服务端
 * @author: liaozicai
 * @date: 2024/2/26 14:56
 */
public class MyNettyServer {
	public static void main(String[] args) {
		new ServerBootstrap()
				.channel(NioServerSocketChannel.class)
				.group(new NioEventLoopGroup())
				.childHandler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						ch.pipeline()
								.addLast(new LoggingHandler())
								// webSocket是基于Http的，这里需要给相关的编解码器去解码数据，然后给websocket处理
								.addLast(new HttpServerCodec())
								// 对上面封帧得到的头体分离的数据，聚合为一个fullRequest，不然websocket无法处理
								.addLast(new HttpObjectAggregator(2048))
								/*
									进行websocket的handler处理，它处理完是个TextWebSocketFrame类型数据。
									它识别的协议格式是ws://ip:port/url，所以WebSocketServerProtocolHandler这个参数就是url
									这样才能让这个请求走到它对应处理的地方
								 */
								.addLast(new WebSocketServerProtocolHandler("/hello"))
								// 自定义handler，用于处理websocket处理完成的TextWebSocketFrame
								.addLast(new MyWebSocketHandler());

					}
				}).bind(8000);
	}
}
