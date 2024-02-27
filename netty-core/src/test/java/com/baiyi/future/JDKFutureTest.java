package com.baiyi.future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * @description: jdk future 测试
 * @author: liaozicai
 * @date: 2024/1/18 09:51
 */
public class JDKFutureTest {

	private static final Logger log = LoggerFactory.getLogger(JDKFutureTest.class);

	public static void main(String[] args) throws ExecutionException, InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		Future<Integer> future = executorService.submit(() -> {
			log.info("执行异步线程");
			TimeUnit.SECONDS.sleep(10);
			return 10;
		});
		log.info("开始执行异步线程");
		// 此处会阻塞主线程，直到异步线程执行完成
		log.info("获取异步执行结果, {}", future.get());
		log.info("执行结束");
	}

}
