package com.baiyi.nio;

import java.nio.ByteBuffer;

/**
 * @description:
 * @author: liaozicai
 * @date: 2024/1/7 17:38
 */
public class TestNIO {
	private static final int BUFFER_SIZE = 9;

	public static void main(String[] args) {
		ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

		System.out.println("---------------buffer刚创建出来---------------");
		int capacity = buffer.capacity();
		int limit = buffer.limit();
		int position = buffer.position();
		System.out.printf("capacity: %d, limit: %d, position: %d%n", capacity, limit, position);

		// 写数据
		buffer.put(new byte[]{'a', 'b', 'c', 'd','e' });

		System.out.println("---------------buffer写了五个数据进去---------------");
		capacity = buffer.capacity();
		limit = buffer.limit();
		position = buffer.position();
		System.out.printf("capacity: %d, limit: %d, position: %d%n", capacity, limit, position);

		// 切换到读模式
		buffer.flip();
		System.out.println("---------------buffer切换到读模式---------------");
		capacity = buffer.capacity();
		limit = buffer.limit();
		position = buffer.position();
		System.out.printf("capacity: %d, limit: %d, position: %d%n", capacity, limit, position);

		// 读取两个数据
		System.out.println("buffer读取数据1 = " + (char)buffer.get());
		System.out.println("buffer读取数据2 = " + (char)buffer.get());
		System.out.println("---------------buffer切换到读模式，读取了两个数据---------------");
		capacity = buffer.capacity();
		limit = buffer.limit();
		position = buffer.position();
		System.out.printf("capacity: %d, limit: %d, position: %d%n", capacity, limit, position);

		// 切回读模式
		buffer.compact();
		System.out.println("---------------buffer调用compact切换回写模式---------------");
		capacity = buffer.capacity();
		limit = buffer.limit();
		position = buffer.position();
		System.out.printf("capacity: %d, limit: %d, position: %d%n", capacity, limit, position);
	}
}
