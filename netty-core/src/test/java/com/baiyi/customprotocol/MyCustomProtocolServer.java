package com.baiyi.customprotocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: 服务端
 * @author: liaozicai
 * @date: 2024/2/23 14:50
 */
public class MyCustomProtocolServer {

	private static final Logger log = LoggerFactory.getLogger(MyCustomProtocolServer.class);

	public static void main(String[] args) {
		new ServerBootstrap()
				.channel(NioServerSocketChannel.class)
				.group(new NioEventLoopGroup())
				.childHandler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						ch.pipeline()
								/*
									处理封帧的问题
									第一个参数：设置长度最大为1024，实际的你可以根据要求调整
									第二个参数：length属性所在的位置偏移量，魔数4个字节+版本1个字节+序列化1个字节+指令功能值1个字节，所以就是7
									第三个参数：length属性的长度，前面使用的是writeInt所以是4个字节
									第四个参数：长度是否需要调整，不调整
									第五个参数：是否需要跳过，不跳过
								 */
								.addLast(new LengthFieldBasedFrameDecoder(1024, 7, 4, 0, 0))
								.addLast(new LoggingHandler())
								.addLast(new MyByteToMessageDecoder())
								.addLast(new ChannelInboundHandlerAdapter() {
									@Override
									public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
										Message message = (Message) msg;
										log.info("message is {}", message);
									}
								});
					}
				})
				.bind(8000);
	}
}
