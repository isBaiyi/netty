package com.baiyi.nio;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @description: 测试黏包和半包
 * @author: liaozicai
 * @date: 2024/1/8 11:06
 */
public class TestNIO9 {

	public static void main(String[] args) {
		// 申请一个空间为100的buffer
		ByteBuffer buffer = ByteBuffer.allocate(100);

		// 往里面存放数据，其实也就是第一趟接收的数据
		buffer.put("How are your?\nI am f".getBytes(StandardCharsets.UTF_8));

		// 拆解这个放了数据的buffer
		boolean flag = false;
		doLineSplit(buffer, flag);
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
