package com.baiyi.nio;

import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * @description: 测试核心API
 * Netty核心API：
 * 1. 写操作
 * a. buffer.read(channel);
 * b. buffer.put(byte);
 * c. buffer.put(byte[]);
 * d. buffer.clear();
 * e. buffer.compact();
 * 2. 读操作
 * a. buffer.write(); 往文件写
 * b. buffer.flip();
 * c. buffer.rewind();
 * d. buffer.mark() 和 buffer.reset()
 * e. buffer.get();
 * f. buffer.get(position);
 * @author: liaozicai
 * @date: 2024/1/5 10:49
 */
public class TestNIO7 {

    private static final Integer BUFFER_SIZE = 10;

    /**
     * 用于测试： 测试buffer.put
     */
    @Test
    public void testBufferPut() {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        buffer.put(new byte[]{'a', 'b', 'c'});
        // 切换为读模式
        buffer.flip();
        while (buffer.hasRemaining()) {
            System.out.println("buffer.get() = " + (char) buffer.get());
        }
        buffer.clear();
    }

    /**
     * 用于测试： 测试buffer.rewind
     */
    @Test
    public void testBufferRewind() {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        buffer.put(new byte[]{'a', 'b', 'c', 'd'});
        buffer.flip();
        while (buffer.hasRemaining()) {
            System.out.println("buffer.get() = " + (char) buffer.get());
        }

        System.out.println("------------------ 未设置rewind-------------------------");
        while (buffer.hasRemaining()) {
            System.out.println("buffer.get() = " + (char) buffer.get());
        }

        System.out.println("------------------ 设置rewind-------------------------");
        buffer.rewind();
        while (buffer.hasRemaining()) {
            System.out.println("buffer.get() = " + (char) buffer.get());
        }
    }

    /**
     * 用于测试： 测试buffer.mark 和 buffer.reset
     */
    @Test
    public void testBufferMarkAndReset() {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        buffer.put(new byte[]{'a', 'b', 'c', 'd'});
        buffer.flip();
        // 先取出一个a
        System.out.println("buffer.get() = " + (char) buffer.get());
        System.out.println("----------------mark--------------------");

        // 设置标志位
        buffer.mark();

        // 取出剩余元素
        while (buffer.hasRemaining()) {
            System.out.println("buffer.get() = " + (char) buffer.get());
        }

        // 重置标志位
        System.out.println("----------------reset-------------------");
        buffer.reset();
        while (buffer.hasRemaining()) {
            System.out.println("buffer.get() = " + (char) buffer.get());
        }
    }

    /**
     * 用于测试：注意，必须要在读模式下调用buffer.compact，否则会丢数据
     */
    @Test
    public void testCompact() {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        buffer.put(new byte[]{'a', 'b', 'c', 'd'});
        int capacity = buffer.capacity();
        int position = buffer.position();
        int limit = buffer.limit();

        System.out.printf("buffer不为空，capacity: %s position: %s limit: %s %n", capacity, position, limit);

        System.out.println("-----未设置buffer读模式直接调用buffer.compact()切换到写模式----");

        buffer.compact();
        capacity = buffer.capacity();
        position = buffer.position();
        limit = buffer.limit();

        System.out.printf("buffer不为空，capacity: %s position: %s limit: %s %n", capacity, position, limit);

        System.out.println("------------重置buffer-------------");
        buffer = ByteBuffer.allocate(BUFFER_SIZE);
        buffer.put(new byte[]{'a', 'b', 'c', 'd'});

        buffer.flip();
        System.out.println("-----设置buffer读模式后再调用buffer.compact()切换到写模式----");

        buffer.compact();
        capacity = buffer.capacity();
        position = buffer.position();
        limit = buffer.limit();

        System.out.printf("buffer不为空，capacity: %s position: %s limit: %s %n", capacity, position, limit);
    }
}
