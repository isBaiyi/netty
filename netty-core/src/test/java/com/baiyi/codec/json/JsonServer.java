package com.baiyi.codec.json;

import com.alibaba.fastjson.JSON;
import com.baiyi.codec.User;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * @description: 服务端
 * @author: liaozicai
 * @date: 2024/2/22 14:43
 */
public class JsonServer {

	private static final Logger log = LoggerFactory.getLogger(JsonServer.class);

	public static void main(String[] args) {
		new ServerBootstrap()
				.channel(NioServerSocketChannel.class)
				.group(new NioEventLoopGroup())
				// 接收端每个ByteBuf的大小
				// .childOption(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(16, 16, 16))
				.childHandler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
						// 添加json解码器
						pipeline.addLast(new JsonObjectDecoder());
						// 添加输入处理器
						pipeline.addLast(new ChannelInboundHandlerAdapter() {
							@Override
							public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
								log.info("msg is {}", msg);
								ByteBuf buf = (ByteBuf) msg;
								byte[] req = new byte[buf.readableBytes()];
								buf.readBytes(req);
								String body = new String(req, StandardCharsets.UTF_8);
								log.info("body is {}", body);
								User user = JSON.parseObject(body, User.class);
								log.info("user is {}", user);
							}
						});
						pipeline.addLast(new LoggingHandler());
					}
				}).bind(8000);

	}
}
