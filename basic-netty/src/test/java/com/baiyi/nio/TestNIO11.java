package com.baiyi.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @description: 文件复制
 * @author: liaozicai
 * @date: 2024/1/9 16:10
 */
public class TestNIO11 {

	public static void main(String[] args) throws IOException {
		// data.txt --> data2.txt
		// method1();
		// method2();
		// method3();
		 method4();
	}

	/**
	 * 方法一:原始 IO 操作方式
	 */
	private static void method1() throws IOException {
		FileInputStream inputStream = new FileInputStream("/Users/baiyi/github/netty/basic-netty/src/test/resources/data.txt");
		FileOutputStream fileOutputStream = new FileOutputStream("/Users/baiyi/github/netty/basic-netty/src/test/resources/data2.txt");
		byte[] buffer = new byte[1024];
		while (true) {
			int read = inputStream.read(buffer);
			if (read == -1) break;
			fileOutputStream.write(buffer, 0, read);
		}
	}

	/**
	 * 引入common-lang的IO工具类
	 */
	private static void method2() throws FileNotFoundException {
		FileInputStream inputStream = new FileInputStream("/Users/baiyi/github/netty/basic-netty/src/test/resources/data.txt");
		FileOutputStream fileOutputStream = new FileOutputStream("/Users/baiyi/github/netty/basic-netty/src/test/resources/data2.txt");
		// 引入common-lang的pom IOUtils.copy(inputStream,fileOutputStream);
	}

	/**
	 * 使用NIO的方式
	 */
	private static void method3() throws IOException {
		FileChannel from = new FileInputStream("/Users/baiyi/github/netty/basic-netty/src/test/resources/data.txt").getChannel();
		FileChannel to = new FileOutputStream("/Users/baiyi/github/netty/basic-netty/src/test/resources/data2.txt").getChannel();

		//传输数据上线的 2G，如果实际文件大小就是超过2G ，取2G大小，源码是取的最小值。
		from.transferTo(0, from.size(), to);
	}

	/**
	 * 使用NIO的方式，该方法和method3的区别是这里不担心文件大小超过2G
	 */
	private static void method4() throws IOException {
		FileChannel from = new FileInputStream("/Users/baiyi/github/netty/basic-netty/src/test/resources/data.txt").getChannel();
		FileChannel to = new FileOutputStream("/Users/baiyi/github/netty/basic-netty/src/test/resources/data2.txt").getChannel();

		long left = from.size();
		while (left > 0) {
			left = left - from.transferTo(from.size() - left, left, to);
		}
	}
}
