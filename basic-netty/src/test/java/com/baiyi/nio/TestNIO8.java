package com.baiyi.nio;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @description: 测试字符串操作
 * @author: liaozicai
 * @date: 2024/1/8 10:11
 */
public class TestNIO8 {

	/**
	 * 用于测试： 默认编码方式把字符串存储在buffer中
	 */
	@Test
	public void testDefault() {
		// 1. 创建buffer
		ByteBuffer buffer = ByteBuffer.allocate(10);

		// 2. 存储数据
		buffer.put("baiyi".getBytes());

		// 3. 切换到读模式进行读取数据
		buffer.flip();
		while (buffer.hasRemaining()) {
			System.out.println("buffer.get() = " + (char) buffer.get());
		}

		// 4. 切换回写模式，好的编程习惯
		buffer.clear();
	}

	/**
	 * 用于测试： 指定编码格式
	 */
	@Test
	public void test_charset() {
		// 1. 显式的指定编码格式为utf-8
		ByteBuffer buffer = StandardCharsets.UTF_8.encode("star-bright");

		// 2. 读取数据
		while (buffer.hasRemaining()) {
			System.out.println("(char) buffer.get() = " + (char) buffer.get());
		}

		// 3. 切换回写模式
		buffer.clear();
	}

	/**
	 * 用于测试： 使用wrap方式
	 */
	@Test
	public void test_wrap() {
		ByteBuffer buffer = ByteBuffer.wrap("star-bright".getBytes(StandardCharsets.UTF_8));
		while (buffer.hasRemaining()) {
			System.out.println("(char) buffer.get() = " + (char) buffer.get());
		}
		buffer.clear();
	}

	/**
	 * 用于测试： buffer中的数据转换为字符串
	 */
	@Test
	public void test_decode() {
		// 1. 申请buffer空间
		ByteBuffer buffer = ByteBuffer.allocate(20);

		// 2. 写数据
		buffer.put("星光璀璨".getBytes(StandardCharsets.UTF_8));

		// 3. 切换为读模式
		buffer.flip();

		// 4. 解码
		String data = StandardCharsets.UTF_8.decode(buffer).toString();
		System.out.println("data = " + data);
	}
}
