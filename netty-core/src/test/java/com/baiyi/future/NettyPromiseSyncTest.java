package com.baiyi.future;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.DefaultPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @description: netty封装的promise测试
 * @author: liaozicai
 * @date: 2024/1/18 17:49
 */
public class NettyPromiseSyncTest {

	private static final Logger log = LoggerFactory.getLogger(NettyPromiseSyncTest.class);

	public static void main(String[] args) throws ExecutionException, InterruptedException {
		// 初始化两个线程
		DefaultEventLoopGroup eventLoopGroup = new DefaultEventLoopGroup(2);
		// 获取一个线程
		EventLoop eventLoop = eventLoopGroup.next();
		// 用于接收结果
		DefaultPromise<String> promise = new DefaultPromise<>(eventLoop);
		// 取另外一个去执行任务
		eventLoopGroup.next().submit(new Runnable() {
			@Override
			public void run() {
				log.info("run start.");
				try {
					TimeUnit.SECONDS.sleep(10);
				} catch (InterruptedException e) {
					log.error("run error: {}", e.getMessage(), e);
					promise.setFailure(e.getCause());
				}
				promise.setSuccess("run success");
			}
		});
		log.info("wait sync result.");
		log.info("get run result is {} .", promise.get());
		log.info("-------------------");
	}
}
