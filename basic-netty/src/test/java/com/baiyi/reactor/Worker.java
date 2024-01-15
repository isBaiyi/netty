package com.baiyi.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @description: 子线程
 * @author: liaozicai
 * @date: 2024/1/13 15:41
 */
public class Worker implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(Worker.class);

	/**
	 * selector轮训注册器，每一个子线程worker都有一个轮训器，其实也可以用主线程那个，但是我们避免和主线程阻塞，分开设计
	 */
	private Selector selector;

	/**
	 * worker是一个子线程的任务，所以需要一个线程来执行读写操作
	 */
	private Thread thread;
	/**
	 * worker线程的名字
	 */
	private final String name;

	/**
	 * 判断是否已创建线程
	 */
	private volatile boolean isCreated;

	/**
	 * 传递代码，解决线程执行顺序问题
	 */
	private final ConcurrentLinkedQueue<Runnable> tasks = new ConcurrentLinkedQueue<>();

	public Worker(String name) {
		this.name = name;
		// 推荐在此处进行初始化数据
		/*this.thread = new Thread(this);
		this.thread.start();
		selector = Selector.open();*/
	}

	public void register(SocketChannel socketChannel) throws IOException {
		// 保证每次进来都是只创建一次一个worker线程
		if (!isCreated) {
			this.thread = new Thread(this, name);
			selector = Selector.open();
			this.thread.start();
			isCreated = true;
		}

		// 在这里放入队列，在下面的selector时取出来使用
		tasks.add(() -> {
			try {
				// 真正注册在这里完成，为什么要传递下去呢？是为了保证selector.select()和sc.register()在同一个线程里，解决线程执行顺序的问题
				socketChannel.register(selector, SelectionKey.OP_READ);
			} catch (ClosedChannelException e) {
				e.printStackTrace();
			}
		});

		// 唤醒selector，这种情况是为了解决，selector.select() 先执行，导致一直在阻塞，sc.register() 注册不了事件进去，导致死锁问题
		selector.wakeup();
	}

	@Override
	public void run() {
		while (true) {
			try {
				log.debug("Worker [{}] invoke", this.name);
				selector.select();

				// 把上面传进来的sc.register取出来允运行
				Runnable poll = tasks.poll();
				if (poll != null) {
					log.debug("Worker register selector event");
					poll.run();
				}

				Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
				while (iterator.hasNext()) {
					SelectionKey key = iterator.next();
					iterator.remove();
					if (key.isReadable()) {
						SocketChannel sc = (SocketChannel) key.channel();
						ByteBuffer buffer = ByteBuffer.allocate(50);
						int read = sc.read(buffer);
						if (read == -1) {
							key.cancel();
						} else {
							buffer.flip();
							String result = StandardCharsets.UTF_8.decode(buffer).toString();
							log.info("result: {}", result);
						}
					}
					log.debug("Worker [{}] invoked", this.name);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
