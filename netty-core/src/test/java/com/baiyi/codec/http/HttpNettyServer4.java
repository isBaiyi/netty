package com.baiyi.codec.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;


/**
 * @description: 第四版本服务端代码
 * @author: liaozicai
 * @date: 2024/2/22 16:15
 */
public class HttpNettyServer4 {

	private static final Logger log = LoggerFactory.getLogger(HttpNettyServer4.class);

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

						/*
							添加输入处理器，这里使用SimpleChannelInboundHandler，限定输入处理的类型为HttpRequest,这里就不需要判断了
							它只会处理这个类型的数据，下面也不需要强制类型转换了
						 */
						pipeline.addLast(new SimpleChannelInboundHandler<HttpRequest>() {
							@Override
							protected void channelRead0(ChannelHandlerContext ctx, HttpRequest msg) throws Exception {
								log.info("httpRequest headers is {}", msg.headers());
								log.info("httpRequest uri is {}", msg.uri());
								log.info("httpRequest protocolVersion is {}", msg.protocolVersion());

								// get请求我们就不在请求体做处理了，在请求头这里做一个响应
								// 这里使用DefaultFullHttpResponse进行返回而不是DefaultHttpResponse，因为full是会把响应头和响应体一起返回
								// 如果使用DefaultHttpResponse，那就得发两次了，和前面看到的接收的msg也是分为两次
								DefaultFullHttpResponse response = new DefaultFullHttpResponse(msg.protocolVersion(), HttpResponseStatus.OK);
								// 响应内容
								byte[] content = "<h1>hello netty</h1>".getBytes(StandardCharsets.UTF_8);
								// 设置响应头的长度，告诉浏览器或客户端我们数据的长度，他们就知道需要取多长的数据了
								response.headers().add(HttpHeaderNames.CONTENT_LENGTH, content.length);
								// 数据写入
								response.content().writeBytes(content);
								ctx.writeAndFlush(response);
							}
						});
					}
				})
				// 监听8000端口，客户端访问http://ip:8000/xxx
				.bind(8000);
	}
}
