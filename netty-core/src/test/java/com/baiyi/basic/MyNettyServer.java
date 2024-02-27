package com.baiyi.basic;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: netty 服务端
 * @author: liaozicai
 * @date: 2024/1/16 11:38
 */
public class MyNettyServer {

	private static final Logger log = LoggerFactory.getLogger(MyNettyServer.class);

	public static void main(String[] args) throws InterruptedException {
		// 构建服务端处理器
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		// 处理器绑定一个ServerSocketChannel，对应netty就是NioServerSocketChannel
		serverBootstrap.channel(NioServerSocketChannel.class);

		// 启动一组线程，可以理解为线程池，就是管理eventLoop的，实际就是我们NIO的worker， 通过死循环监控accept read write事件
		// 每个eventLoop就是一个线程，每个线程都有自己的selector
		serverBootstrap.group(new NioEventLoopGroup());

		serverBootstrap.option(ChannelOption.SO_RCVBUF, 2048);
		// channel：SeverSocketChannel、SocketChannel，这里需要初始化SocketChannel
		// 上面处理了事件，这里就开始处理接收到事件的IO处理，所以就要初始化NioSocketChannel。也就是通过handler，通过pipeline来流水线处理
		serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {

			/**
			 * 通过SocketChannel接通监控 accept、read、write处理，通过pipeline用handler进行处理
			 *
			 * @param ch          socketChannel
			 */
			@Override
			protected void initChannel(NioSocketChannel ch) {
				/*
					添加一个解码器，意思就是把收到的bytebuffer解码为字符串，客户端传给我们服务端的都是Bytebuffer，网络传输不能传字符串
					你可以在这里添加多个handler，你可以按照你得需求在后面不断追加，完成你的业务，这是内置的处理器，你也可以你自己定义，然后添加进去
				*/
				ch.pipeline().addLast(new StringDecoder());
				/*
					添加一个InboundHandler解析器，所有我们从外部读入数据到这里都是Inbound。 就是输入数据。因为我们是addLast，所以前面加了一个
					字符串解码器，这里又追加了一个输出处理器。所以按照流水线就是先执行字符串解码器，然后执行这个输出处理器
				 */
				ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
					/*
						Adapter是适配器，我们不用全部实现所有的方法，只需要执行我们自己想要的，我们就实现一个读操作。上面执行完解码器，把bytebuffer处理为字符串
						客户端:
						那么到了这里我们Inbound就是输入到这里，所以这里对应的就是读操作，我们就实现读操作channel即可。他会通过参数传递给这里面的处理器，也就是
						这个方法里面的，msg，第一个处理器要是解码为json，这个msg就是json，我们处理json为java对象，就传下去了，你后面还能添加处理器获取这个java对象
						你要是没有第一个处理器，那你这里拿到的就是bytebuffer，你自己看着来组合。你要灵活应用，按照需求组合。
					*/
					@Override
					public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
						log.info("获取到的消息: {}", msg);
					}
				});
			}
		});

		// 服务端监听端口
		serverBootstrap.bind(8000);

	}
}
