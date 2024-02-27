package com.baiyi.templete;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @description: 规范化客户端
 * <p>
 * 客户端何时优雅关闭断开？
 * <br>1. 发生异常</br>
 * <br>2. 监听到对应的事件回调，需要退出的时候，具体看业务需求</br>
 * <br>3. 正常退出</br>
 * </p>
 * @author: liaozicai
 * @date: 2024/2/26 17:31
 */
public class MyNettyClient {

	private static final Logger log = LoggerFactory.getLogger(MyNettyClient.class);

	public static void main(String[] args) {
		// 提取共享
		final LoggingHandler loggingHandler = new LoggingHandler();
		final StringEncoder stringEncoder = new StringEncoder();
		NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
		try {
			Channel channel = new Bootstrap()
					.channel(NioSocketChannel.class)
					.group(nioEventLoopGroup)
					.handler(new ChannelInitializer<NioSocketChannel>() {
						@Override
						protected void initChannel(NioSocketChannel ch) throws Exception {
							ch.pipeline()
									.addLast(loggingHandler)
									.addLast(stringEncoder)
									.addLast(new ChannelInboundHandlerAdapter() {
										@Override
										public void channelActive(ChannelHandlerContext ctx) throws Exception {
											// active事件触发代表连接完全建立，可以直接发送数据
											ctx.channel().writeAndFlush("hello");
										}
									});
						}
					}).connect(new InetSocketAddress(8000)).sync().channel();

			// 监控channel的关闭，它是阻塞的事件，channel关闭后就会执行finally代码块的内容进行优雅关闭释放资源
			channel.closeFuture().sync();
		} catch (InterruptedException e) {
			log.error("客户端出现异常, e: {}", e.getMessage(), e);
		} finally {
			// 优雅关闭
			nioEventLoopGroup.shutdownGracefully();
		}

	}
}
