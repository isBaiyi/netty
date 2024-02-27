package com.baiyi.future;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @description: netty封装的future测试
 * @author: liaozicai
 * @date: 2024/1/18 10:03
 */
public class NettyFutureTest {

	private static final Logger log = LoggerFactory.getLogger(NettyFutureTest.class);

	public static void main(String[] args) throws ExecutionException, InterruptedException {
		DefaultEventLoopGroup defaultEventLoopGroup = new DefaultEventLoopGroup();
		Future<Integer> future = defaultEventLoopGroup.submit(() -> {
			log.info("执行异步线程");
			TimeUnit.SECONDS.sleep(10);
			return 10;
		});
		log.info("开始执行异步线程");
		// 此处会阻塞主线程，直到异步线程执行完成
//		log.info("获取异步执行结果, {}", future.get());

		// Netty对JDK的Future进行了封装，可以通过阻塞的get获取结果，也可以通过listener回调的方式获取结果，底层是promise
		future.addListener(new GenericFutureListener<Future<? super Integer>>() {
			@Override
			public void operationComplete(Future<? super Integer> future) throws Exception {
				log.info("异步线程执行完成回调，执行结果为: {}", future.get());
			}
		});
		log.info("执行结束");
	}

}
