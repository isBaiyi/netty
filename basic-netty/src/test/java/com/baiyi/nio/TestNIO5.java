package com.baiyi.nio;

import java.nio.ByteBuffer;

/**
 * @description: 测试NIO的结构
 * @author: baiyi
 * @date: 2023/6/27 21:41
 */
public class TestNIO5 {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        int capacity = buffer.capacity(); // 10
        int position = buffer.position(); // 0
        int limit = buffer.limit(); // 10
        System.out.printf("buffer为空，处于写模式下 capacity: %s position: %s limit: %s %n", capacity, position, limit);
        System.out.println();

        buffer.flip();
        capacity = buffer.capacity(); // 10
        position = buffer.position(); // 0
        limit = buffer.limit(); // 0
        System.out.printf("buffer为空，处于读模式下 capacity: %s position: %s limit: %s %n", capacity, position, limit);
        System.out.println();

        buffer.clear();
        buffer.put(new byte[]{'a', 'b', 'c', 'd'});

        capacity = buffer.capacity(); // 10
        position = buffer.position(); // 4
        limit = buffer.limit(); // 10
        System.out.printf("buffer不为空，处于写模式下 capacity: %s position: %s limit: %s %n", capacity, position, limit);
        System.out.println();

        buffer.flip();
        capacity = buffer.capacity(); // 10
        position = buffer.position(); // 0
        limit = buffer.limit(); // 4
        System.out.printf("buffer不为空，处于读模式下 capacity: %s position: %s limit: %s %n", capacity, position, limit);
        System.out.println();

        buffer.clear();
        capacity = buffer.capacity(); // 10
        position = buffer.position(); // 0
        limit = buffer.limit(); // 10
        System.out.printf("buffer不为空，调用clear后处于写模式下 capacity: %s position: %s limit: %s %n", capacity, position, limit);
        System.out.println();

        buffer.put(new byte[]{'a', 'b', 'c', 'd'});
        buffer.compact(); // 注意compact会保留未读取的数据，然后position往后移动，例如现在position为4，因为abcd还没有读

//        buffer.flip();
        capacity = buffer.capacity(); // 10
        position = buffer.position(); // 6
        limit = buffer.limit(); // 10
        System.out.printf("buffer不为空，调用compact后处于写模式下 capacity: %s position: %s limit: %s %n", capacity, position, limit);
        System.out.println();
    }
}
