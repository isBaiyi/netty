package com.baiyi.nio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @description: 往文件中写内容
 * @author: liaozicai
 * @date: 2024/1/9 15:51
 */
public class TestNIO10 {

	public static void main(String[] args) throws IOException {
		// 1. 获取channel，可以通过FileOutputStream、RandomAccessFile
		FileChannel channel = new FileOutputStream("/Users/baiyi/github/netty/basic-netty/src/test/resources/data1.txt").getChannel();

		// 2. 获得buffer
		ByteBuffer buffer = StandardCharsets.UTF_8.encode("星光璀璨");

		// 3. 写数据
		channel.write(buffer);
	}
}
