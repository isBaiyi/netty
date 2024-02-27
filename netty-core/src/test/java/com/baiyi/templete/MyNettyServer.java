package com.baiyi.templete;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: 规范化服务端
 * @author: liaozicai
 * @date: 2024/2/26 17:24
 */
public class MyNettyServer {

	private static final Logger log = LoggerFactory.getLogger(MyNettyServer.class);

	public static void main(String[] args) {
		// 提出来共用
		final LoggingHandler loggingHandler = new LoggingHandler();
		// 创建boss的EventLoopGroup，用于处理accept事件，一个线程就够了
		NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
		// 创建worker的EventLoopGroup，用于处理客户端的IO读写操作
		NioEventLoopGroup workerGroup = new NioEventLoopGroup(4);
		try {
			Channel channel = new ServerBootstrap()
					.channel(NioServerSocketChannel.class)
					.group(bossGroup, workerGroup)
					.childHandler(new ChannelInitializer<NioSocketChannel>() {
						@Override
						protected void initChannel(NioSocketChannel ch) throws Exception {
							ch.pipeline().addLast(loggingHandler);
							// 可以根据业务继续添加handler
						}
					})
					// bind在这里监听，它是阻塞的，然后获取到channel
					.bind(8000).sync().channel();
			// 我们在这里阻塞等待，监听到关闭之后就往下走finally，执行优雅关闭了
			channel.closeFuture().sync();
		} catch (Exception e) {
			log.error("服务端出现异常, e: {}", e.getMessage(), e);
		} finally {
			// 优雅关闭
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
