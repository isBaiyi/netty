package com.baiyi.codec.objectcodec;

import com.baiyi.codec.User;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

/**
 * @description: 客户端
 * @author: liaozicai
 * @date: 2024/2/22 11:30
 */
public class ObjectCodecClient {
	public static void main(String[] args) throws InterruptedException {
		Channel channel = new Bootstrap()
				.channel(NioSocketChannel.class)
				.group(new NioEventLoopGroup())
				.handler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						ch.pipeline()
								.addLast(new LoggingHandler())
								// 添加一个序列化编码器
								.addLast(new ObjectEncoder());
					}
				}).connect(new InetSocketAddress(8000)).sync().channel();
		// 把Java对象发送给服务端，这里会被上面pipeline添加的ObjectEncoder进行编码，然后再发出去
		channel.writeAndFlush(new User("star-bright", 18));

	}
}
