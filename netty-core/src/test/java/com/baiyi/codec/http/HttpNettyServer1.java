package com.baiyi.codec.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: 第一版本服务端代码
 * @author: liaozicai
 * @date: 2024/2/22 16:15
 */
public class HttpNettyServer1 {

	private static final Logger log = LoggerFactory.getLogger(HttpNettyServer1.class);

	public static void main(String[] args) {
		new ServerBootstrap()
				.channel(NioServerSocketChannel.class)
				.group(new NioEventLoopGroup())
				.childHandler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
						// 日志输出处理器
						pipeline.addLast(new LoggingHandler());
						/*
							添加Http编解码器，这个编解码器是组合功能，拥有对接收端解码和发送端编码的功能
							等同于HttpRequestDecoder和HttpRequestEncoder的组合
							接收请求时：解码Http协议转换为HttpObject
							发送请求时：编码HttpObject转换为Http报文给接收端
						 */
						pipeline.addLast(new HttpServerCodec());
						pipeline.addLast(new ChannelInboundHandlerAdapter() {
							@Override
							public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
								log.info("msg is {}", msg);
							}
						});
					}
				})
				// 监听8000端口，客户端访问http://ip:8000/xxx
				.bind(8000);
	}
}
