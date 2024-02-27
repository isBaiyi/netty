package com.baiyi.handler.idleStateHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description: 测试IdleStateHandler服务端
 * @author: liaozicai
 * @date: 2024/2/26 11:39
 */
public class MyNettyServer {

	private static final Logger log = LoggerFactory.getLogger(MyNettyServer.class);

	public static void main(String[] args) {
		new ServerBootstrap()
				.channel(NioServerSocketChannel.class)
				.group(new NioEventLoopGroup())
				.childHandler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {

						// 每个链接都会经过该方法，从而执行pipeline流程，所以在这里定义对应的是每个客户端独立的，不会有安全问题
						AtomicInteger count = new AtomicInteger(0);

						ch.pipeline()
								.addLast(new LoggingHandler())
								.addLast(new StringDecoder())
								/*
									添加IdleStateHandler，指定三个参数
									第一个参数：读空闲触发时间，认为超过3s就是读空闲
									第二个参数：写空闲触发时间，认为超过5s就是写空闲
									第三个参数：读写空闲触发时间，认为超过7s就是读写空闲
									第四个参数：时间单位，配置的是秒
								 */
								.addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS))
								.addLast(new ChannelInboundHandlerAdapter() {
									/**
									 * 监听读写空闲事件
									 * @param ctx 上下文
									 * @param evt 事件
									 * @throws Exception 异常信息
									 */
									@Override
									public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
										IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
										/* if (idleStateEvent.state() == IdleState.READER_IDLE) {
											log.info("连接 {} 出现读空闲", ctx.channel());
										} else if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
											log.info("连接 {} 出现写空闲", ctx.channel());
										} else if (idleStateEvent.state() == IdleState.ALL_IDLE) {
											log.info("连接 {} 出现读写空闲", ctx.channel());
										} */
										/* if (idleStateEvent.state() == IdleState.ALL_IDLE) {
											log.info("连接 {} 出现读写空闲，断开链接避免资源浪费", ctx.channel());
											ctx.channel().close();
										} */

										if (idleStateEvent.state() == IdleState.ALL_IDLE) {
											log.info("连接 {} 出现读写空闲", ctx.channel());
											count.incrementAndGet();
											if (count.get() == 4) {
												log.info("连接 {} 出现4次读写空闲，断开链接避免资源浪费", ctx.channel());
												ctx.channel().close();
											}
										}
									}
								});
					}
				}).bind(8000);
	}
}
