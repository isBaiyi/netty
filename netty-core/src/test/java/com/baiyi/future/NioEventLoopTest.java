package com.baiyi.future;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.NettyRuntime;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * @description: nioEventLoop单元测试
 * @author: liaozicai
 * @date: 2024/1/17 14:50
 */
public class NioEventLoopTest {

	private static final Logger log = LoggerFactory.getLogger(NioEventLoopTest.class);

	/**
	 * 用于测试： 测试nioEventLoop
	 */
	@Test
	public void test_nioEventLoopGroupThreadNum() {
		// 查看默认创建的线程数
		int threadNum = NettyRuntime.availableProcessors() * 2;
		log.info("threadNum: {}", threadNum);
	}

	/**
	 * 用于测试： NioEventLoopGroup
	 */
	@Test
	public void test_nioEventLoopGroup() {
		NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(2);
		EventLoop next1 = nioEventLoopGroup.next();
		EventLoop next2 = nioEventLoopGroup.next();
		EventLoop next3 = nioEventLoopGroup.next();

		log.info("next1: {}", next1);
		log.info("next2: {}", next2);
		log.info("next3: {}", next3);
	}

	/**
	 * 用于测试： DefaultEventLoopGroup
	 */
	@Test
	public void test_defaultEventLoop() throws ExecutionException, InterruptedException {
		DefaultEventLoopGroup defaultEventLoopGroup = new DefaultEventLoopGroup();
		EventLoop eventLoop = defaultEventLoopGroup.next();
		Future<Integer> future = eventLoop.submit(() -> {
			log.info("hello netty");
			return 1;
		});

		log.info("执行结果: {}", future.get());
	}

	/**
	 * 用于测试： promise
	 */
	@Test
	public void test_call_promise() {
		DefaultEventLoopGroup eventLoopGroup = new DefaultEventLoopGroup();
		EventLoop eventLoop = eventLoopGroup.next();
		// 提交任务
		Future<String> future = eventLoop.submit(new Callable<String>() {
			@Override
			public String call() throws Exception {
				log.info("run start.");
				return "hello netty";
			}
		});

		// 获取结果
		future.addListener(new GenericFutureListener<Future<? super String>>() {
			@Override
			public void operationComplete(Future<? super String> future) throws Exception {
				log.info("get callable result");
				log.info("callable result: {}", future.get());
			}
		});
	}

}
