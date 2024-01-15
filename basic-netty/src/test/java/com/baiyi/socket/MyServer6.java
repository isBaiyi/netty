package com.baiyi.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * @description: 服务端
 * @author: liaozicai
 * @date: 2024/1/9 16:21
 */
public class MyServer6 {

	public static boolean flag = false;

	public static void main(String[] args) throws IOException {
		// 1. 创建连接信息的severSocketChannel，这是用来处理连接
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		// 2. 服务端监听的端口，需要做绑。客户端访问的时候ip+端口，端口就是这个，ip就是服务端自己运行的服务器的ip地址
		serverSocketChannel.bind(new InetSocketAddress(8000));
		// 设置serverSocketChannel为非阻塞，需要注意：必须设置为非阻塞，selector才能使用
		serverSocketChannel.configureBlocking(false);

		// 创建selector
		Selector selector = Selector.open();

		// 这里是指把serverSocketChannel注册在selector中。具体可以理解把serverSocketChannel存储在selector的一个名称为keys的HashSet中
		SelectionKey keys = serverSocketChannel.register(selector, 0, null);
		// 告知selector你需要监管的是什么东西。这里只需要监控客户端的连接，只要有新的连接过来，你就通知我，我去执行后续的代码逻辑
		keys.interestOps(SelectionKey.OP_ACCEPT);
		System.out.printf("刚注册时selector的keys长度为: %s，selectedKeys长度为: %s%n", selector.keys().size(), selector.selectedKeys().size());

		// 开启监听，监听对于客户端的连接，因为监听是一直进行的，所以需要写在死循环里面，不能停，来一个接收一个连接
		while (true) {
			System.out.println("selector开始监控，阻塞在此处");
			/*
			    这里就是如果没有他的兴趣事件[连接、读写操作]，监管器就会卡在这里，不会一直while空转下去。有了对应的事件才会继续执行下去
			    当有对应的兴趣事件过来后是存储在selector#selectedKeys中，为什么需要有两个集合呢？keys和selectedKeys，主要是为了职责分离，各司其职
			 */
			selector.select();

			System.out.printf("有事件触发，当前的keys长度: %s，当前的selectedKeys长度: %s%n", selector.keys().size(), selector.selectedKeys().size());

			// 使用迭代器进行遍历
			Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
			while (iterator.hasNext()) {
				SelectionKey key = iterator.next();
				// 用完就删除
				iterator.remove();
				if (key.isAcceptable()) {
					ServerSocketChannel channel = (ServerSocketChannel) key.channel();
					System.out.println("准备获取连接");
					SocketChannel socketChannel = channel.accept();
					System.out.println("连接已获取，对应连接为：" + socketChannel);
					// 设置非阻塞模式
					socketChannel.configureBlocking(false);
					SelectionKey scKey = socketChannel.register(selector, 0, null);
					scKey.interestOps(SelectionKey.OP_READ);
				} else if (key.isReadable()) {
					try {
						SocketChannel sc = (SocketChannel) key.channel();
						ByteBuffer byteBuffer = ByteBuffer.allocate(6);
						int read = sc.read(byteBuffer);
						// 因为客户端直接断开等同于发一个-1过来，我们这里读取的就是一个长度，要是-1要么是读完了，要么就是客户端异常断开了
						if (read == -1) {
							// 所以这里根据返回直接cancel，结束这次调用，避免频繁调用select
							key.cancel();
						} else {
							// 正常的逻辑
							doLineSplit(byteBuffer, flag);
						}
					} catch (IOException e) {
						// 异常也直接返回cancel
						key.cancel();
						System.err.println(e.getMessage());
					}
				}
			}
		}
	}

	/**
	 * 拆解数据的方法
	 *
	 * @param buffer 存放数据的buffer
	 * @param flag   上次是否读取到数据
	 */
	private static void doLineSplit(ByteBuffer buffer, boolean flag) {
		// 切换到读模式
		buffer.flip();

		for (int i = 0; i < buffer.limit(); i++) {
			// 循环到约定好的\n换行符
			if ('\n' == buffer.get(i)) {
				// 等到了换行符的时候，我们申请一个新的buffer，大小是这个换行符位置的长度，下标是i，长度是i+1-buffer.position()
				int len = i + 1 - buffer.position();
				// 把换行符之前的数据进行存起来
				ByteBuffer targetBuffer = ByteBuffer.allocate(len);
				for (int j = 0; j < len; j++) {
					// 调用buffer.get(i)方法不会改变position的位置
					targetBuffer.put(buffer.get());
				}
				// 截取工作完成，切换到读模式
				targetBuffer.flip();
				String result = StandardCharsets.UTF_8.decode(targetBuffer).toString();
				System.out.println("最后buffer处理的结果: " + result);
				flag = true;
			}
		}

		// 说明上面没有读取到分隔符，也就是本次数据不是一个完整的句子
		if (!flag) {
			int len = buffer.limit();
			ByteBuffer targetBuffer = ByteBuffer.allocate(len);
			// 循环把数据存在在当前的targetBuffer中
			for (int i = 0; i < len; i++) {
				// 此处调用get(i)方式不会改变buffer的position位置
				targetBuffer.put(buffer.get(i));
			}
			// 截取工作完成，切换到读模式
			targetBuffer.flip();
			String result = StandardCharsets.UTF_8.decode(targetBuffer).toString();
			System.out.println("最后buffer处理的结果: " + result);
		}

		// 我们前面通过get()一直遍历到了换行符的位置，此时把后面的挪到前面，切换写模式，开始下一波写入
		buffer.compact();
	}

}
