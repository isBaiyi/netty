package com.baiyi.codec.json;

import com.alibaba.fastjson.JSON;
import com.baiyi.codec.User;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * @description: 客户端
 * @author: liaozicai
 * @date: 2024/2/22 14:43
 */
public class JsonClient {
	public static void main(String[] args) throws InterruptedException {
		Channel channel = new Bootstrap()
				.channel(NioSocketChannel.class)
				.group(new NioEventLoopGroup())
				.handler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						ch.pipeline().addLast(new LoggingHandler());
					}
				})
				.connect(new InetSocketAddress(8000)).sync().channel();

		// 把java对象转换为JSON格式，再封装为ByteBuf方式发送出去
		User user = new User("star-bright", 18);
		String userJSON = JSON.toJSONString(user);
		ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
		buffer.writeCharSequence(userJSON, StandardCharsets.UTF_8);
		// 写出这个对象
		channel.writeAndFlush(buffer);
	}
}
