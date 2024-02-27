package com.baiyi.codec.objectcodec;

import com.baiyi.codec.User;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: 服务端
 * @author: liaozicai
 * @date: 2024/2/22 11:38
 */
public class ObjectCodecServer {

	private static final Logger log = LoggerFactory.getLogger(ObjectCodecServer.class);

	public static void main(String[] args) {
		new ServerBootstrap()
				.channel(NioServerSocketChannel.class)
				.group(new NioEventLoopGroup())
				.childHandler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
						// 添加一个对象解码器
						pipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
						// 添加输入解码器
						pipeline.addLast(new ChannelInboundHandlerAdapter() {
							@Override
							public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
								// 在前面已经经过ObjectDecoder进行解码了，这里得到的就是Java对象
								User user = (User) msg;
								log.info("user is {}", user);
							}
						});
						// 添加日志输出管理器
						pipeline.addLast(new LoggingHandler());
					}
				}).bind(8000);
	}
}
