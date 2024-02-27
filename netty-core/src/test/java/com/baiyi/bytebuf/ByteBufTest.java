package com.baiyi.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: 测试ByteBuf
 * @author: liaozicai
 * @date: 2024/2/1 17:14
 */
public class ByteBufTest {

	private static final Logger log = LoggerFactory.getLogger(ByteBufTest.class);

	/**
	 * 用于测试： ByteBuf的基础使用
	 */
	@Test
	public void test_byteBuf() {
		// 默认大小为256，最大大小为Integer.MAX_VALUE，可以手动设置ByteBuf的大小，一旦设置了大小，在不超过设置的ByteBuf最大值时容量不足时会自动扩容
		ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(64);

		// 写入数据
		buffer.writeByte('a');
		buffer.writeDouble(1D);
		buffer.writeDouble(1D);
		buffer.writeDouble(1D);
		buffer.writeDouble(1D);
		buffer.writeDouble(1D);
		buffer.writeDouble(1D);
		buffer.writeDouble(1D);
		buffer.writeDouble(1D);

		log.info("buffer {}", buffer);
		// ByteBufUtil.prettyHexDump(buffer)会以16进制的方式输出ByteBuf的内存占用情况
		System.out.println("buffer的内存监控信息: " + ByteBufUtil.prettyHexDump(buffer));
	}

	/**
	 * 用于测试： ByteBuf内存
	 */
	@Test
	public void test_memory() {
		ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
		ByteBuf directBuffer = ByteBufAllocator.DEFAULT.directBuffer();
		ByteBuf heapBuffer = ByteBufAllocator.DEFAULT.heapBuffer();
		log.info("buffer is {}", buffer);
		log.info("directBuffer is {}", directBuffer);
		log.info("heapBuffer is {}", heapBuffer);
	}

	/**
	 * 用于测试： 测试读数据超出写数据的指针
	 */
	@Test
	public void test_read() {
		ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(10);
		System.out.println("buffer: " + buffer);
		System.out.println("buffer的内存监控信息: " + ByteBufUtil.prettyHexDump(buffer));
		System.out.println();

		// 写一个字节
		buffer.writeByte(1);
		System.out.println("buffer: " + buffer);
		System.out.println("buffer的内存监控信息: " + ByteBufUtil.prettyHexDump(buffer));
		System.out.println();

		// 读一个字节，先设置读标志
		buffer.markReaderIndex();
		buffer.readByte();
		System.out.println("buffer: " + buffer);
		System.out.println("buffer的内存监控信息: " + ByteBufUtil.prettyHexDump(buffer));
		System.out.println();

		// 重置读标志位
		buffer.resetReaderIndex();
		System.out.println("reset buffer: " + buffer);
		System.out.println("reset buffer的内存监控信息: " + ByteBufUtil.prettyHexDump(buffer));
		System.out.println();

		// 读一个字节
		buffer.readByte();
		System.out.println("buffer: " + buffer);
		System.out.println("buffer的内存监控信息: " + ByteBufUtil.prettyHexDump(buffer));
	}


	/**
	 * 用于测试： Netty的切片机制 slice
	 */
	@Test
	public void test_slice() {
		ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(10);
		for (int i = 0; i < 10; i++) {
			buffer.writeByte(i);
		}
		System.out.println("buffer: " + buffer + " hashcode: " + buffer.hashCode());
		System.out.println("buffer的内存监控信息: " + ByteBufUtil.prettyHexDump(buffer));
		System.out.println();

		// 从下标0开始，取5个数据
		ByteBuf buffer1 = buffer.slice(0, 5);
		System.out.println("buffer1: " + buffer1 + " hashcode: " + buffer1.hashCode());
		System.out.println("buffer1的内存监控信息: " + ByteBufUtil.prettyHexDump(buffer1));
		System.out.println();

		// 从下标5开始，取3个数据
		ByteBuf buffer2 = buffer.slice(5, 3);
		System.out.println("buffer2: " + buffer2 + " hashcode: " + buffer2.hashCode());
		System.out.println("buffer2的内存监控信息: " + ByteBufUtil.prettyHexDump(buffer2));
		System.out.println();
	}

	/**
	 * 用于测试： slice是否共享一个原先的ByteBuf内存空间
	 */
	@Test
	public void test_slice2() {
		ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(10);
		for (int i = 0; i < 10; i++) {
			buffer.writeByte(i);
		}

		// 切片
		ByteBuf buffer1 = buffer.slice(0, 5);
		ByteBuf buffer2 = buffer.slice(5, 3);

		// 调用release进行释放ByteBuf
		buffer.release();

		// 这里我们按道理已经申请了，要是产生了新空间自然不受上面释放的影响，这里会报错
		System.out.println("buffer1的内存监控信息: " + ByteBufUtil.prettyHexDump(buffer1));
		System.out.println("buffer2的内存监控信息: " + ByteBufUtil.prettyHexDump(buffer2));
	}

	/**
	 * 用于测试： slice是否共享一个原先的ByteBuf内存空间
	 */
	@Test
	public void test_slice3() {
		ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(10);
		for (int i = 0; i < 10; i++) {
			buffer.writeByte(i);
		}

		// 切片
		ByteBuf buffer1 = buffer.slice(0, 5);
		// 增加引用次数
		buffer.retain();

		ByteBuf buffer2 = buffer.slice(5, 3);
		// 增加引用次数
		buffer.retain();

		// 调用release进行释放ByteBuf，只会导致引用次数-1，那当前的ByteBuf的引用次数还为2，所以下面不会出现报错
		buffer.release();

		System.out.println("buffer1的内存监控信息: " + ByteBufUtil.prettyHexDump(buffer1));
		System.out.println("buffer2的内存监控信息: " + ByteBufUtil.prettyHexDump(buffer2));
	}

}
