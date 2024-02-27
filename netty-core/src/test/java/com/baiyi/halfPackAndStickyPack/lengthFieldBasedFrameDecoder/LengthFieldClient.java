package com.baiyi.halfPackAndStickyPack.lengthFieldBasedFrameDecoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * @description: 客户端
 * @author: liaozicai
 * @date: 2024/2/21 15:25
 */
public class LengthFieldClient {

	private static final Logger log = LoggerFactory.getLogger(LengthFieldClient.class);

	public static void main(String[] args) {
		NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
		try {
			Channel channel = new Bootstrap()
					.channel(NioSocketChannel.class)
					.group(nioEventLoopGroup)
					.handler(new ChannelInitializer<NioSocketChannel>() {
						@Override
						protected void initChannel(NioSocketChannel ch) throws Exception {
							ch.pipeline()
									.addLast(new StringEncoder())
									.addLast(new LoggingHandler());
						}
					})
					.connect(new InetSocketAddress(8000))
					.sync()
					.channel();

			// 消息
			String msg1 = "Hello Jack";
			String msg2 = "Hi Mike";

			ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();

			/*
				创建一个消息，其结构为:
				第一部分写入一个int类型，其中Int类型本身就是占4字节的长度，这里存的就是length属性，也就是消息实体的长度
				第二部分为写入一个头，可以理解为我们之前图示的head1，使用byte写入，占用1字节，
				第三部分为写入真实数据的内容也就是msg1字符串
			*/
			buffer.writeInt(msg1.length());
			buffer.writeByte(1);
			buffer.writeCharSequence(msg1, StandardCharsets.UTF_8);

			/*
				创建一个消息，其结构为:
				第一部分写入一个int类型，其中Int类型本身就是占4字节的长度，这里存的就是length属性，也就是消息实体的长度
				第二部分为写入一个头，可以理解为我们之前图示的head1，使用byte写入，占用1字节，
				第三部分为写入真实数据的内容也就是msg2字符串
			*/
			buffer.writeInt(msg2.length());
			buffer.writeByte(1);
			buffer.writeCharSequence(msg2, StandardCharsets.UTF_8);

			channel.writeAndFlush(buffer);
		} catch (Exception e) {
			log.error("出现异常, e: {}", e.getMessage(), e);
		} finally {
			nioEventLoopGroup.shutdownGracefully();
		}
	}
}
