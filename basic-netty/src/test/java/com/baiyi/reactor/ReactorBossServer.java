package com.baiyi.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description: Reactor boss线程【服务端】
 * @author: liaozicai
 * @date: 2024/1/13 15:28
 */
public class ReactorBossServer {

	private static final Logger log = LoggerFactory.getLogger(ReactorBossServer.class);

	public static void main(String[] args) throws IOException {
		Thread.currentThread().setName("Boss");
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.bind(new InetSocketAddress(8000));
		serverSocketChannel.configureBlocking(false);

		Selector selector = Selector.open();
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

		// 初始化一个worker数组，模拟两个线程的worker组
		Worker[] workers = new Worker[2];
		for (int i = 0; i < workers.length; i++) {
			workers[i] = new Worker("Worker-" + i);
		}

		// 统计有几次连接事件过来，我们把每次连接的IO操作交给不同的worker进行处理
		AtomicInteger index = new AtomicInteger();

		while (true) {
			log.debug("Boss invoke");
			selector.select();

			Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
			while (iterator.hasNext()) {
				SelectionKey key = iterator.next();
				iterator.remove();

				if (key.isAcceptable()) {
					SocketChannel socketChannel = serverSocketChannel.accept();
					socketChannel.configureBlocking(false);
					// socketChannel.register(selector, SelectionKey.OP_READ);
					// 在子线程来完成注册以及sc的读写事件的处理，通过取模的方式去均衡每个worker执行
					workers[index.getAndIncrement() % workers.length].register(socketChannel);
				}
			}
		}
	}

}
