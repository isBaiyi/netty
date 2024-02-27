package com.baiyi.codec.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;


/**
 * @description: 第三版本服务端代码
 * @author: liaozicai
 * @date: 2024/2/22 16:15
 */
public class HttpNettyServer3 {

	private static final Logger log = LoggerFactory.getLogger(HttpNettyServer3.class);

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
								if (msg instanceof HttpRequest httpRequest) {
									log.info("httpRequest headers is {}", httpRequest.headers());
									log.info("httpRequest uri is {}", httpRequest.uri());
									log.info("httpRequest protocolVersion is {}", httpRequest.protocolVersion());

									// get请求我们就不在请求体做处理了，在请求头这里做一个响应
									// 这里使用DefaultFullHttpResponse进行返回而不是DefaultHttpResponse，因为full是会把响应头和响应体一起返回
									// 如果使用DefaultHttpResponse，那就得发两次了，和前面看到的接收的msg也是分为两次
									DefaultFullHttpResponse response = new DefaultFullHttpResponse(httpRequest.protocolVersion(), HttpResponseStatus.OK);
									// 响应内容
									byte[] content = "<h1>hello netty</h1>".getBytes(StandardCharsets.UTF_8);
									// 设置响应头的长度，告诉浏览器或客户端我们数据的长度，他们就知道需要取多长的数据了
									response.headers().add(HttpHeaderNames.CONTENT_LENGTH, content.length);
									// 数据写入
									response.content().writeBytes(content);
									ctx.writeAndFlush(response);
								} else if (msg instanceof HttpContent httpContent) {
									// 注意：因为get请求不通过请求体传递数据，所以这个就是空的，类型为EmptyLastHttpContent
									// 获取ByteBuf的请求体，就可以按照要求进行处理了
									ByteBuf content = httpContent.content();
									log.info("content is {}", content);
									// 后续业务处理
								}
							}
						});
					}
				})
				// 监听8000端口，客户端访问http://ip:8000/xxx
				.bind(8000);
	}
}
