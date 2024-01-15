package com.baiyi.nio;

import java.nio.ByteBuffer;

/**
 * @description:
 * @author: baiyi
 * @date: 2023/6/28 09:49
 */
public class TestNIO6 {
    public static void main(String[] args) {

        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new byte[]{'a', 'b', 'c', 'd'});

        int capacity = buffer.capacity();
        int position = buffer.position();
        int limit = buffer.limit();

        System.out.printf("buffer不为空，capacity: %s position: %s limit: %s %n", capacity, position, limit);

        System.out.println("-----切换到读模式获取数据----");
        buffer.flip();
        System.out.println("buffer.get() = " + (char) buffer.get());

        System.out.println("-----未从buffer中获取元素直接调用buffer.compact()切换到写模式----");

        buffer.compact();
        capacity = buffer.capacity();
        position = buffer.position();
        limit = buffer.limit();

        System.out.printf("buffer不为空，capacity: %s position: %s limit: %s %n", capacity, position, limit);

        buffer.put(new byte[]{'a', 'b', 'c', 'd'});

        capacity = buffer.capacity();
        position = buffer.position();
        limit = buffer.limit();

        System.out.printf("buffer不为空重新添加数据，capacity: %s position: %s limit: %s %n", capacity, position, limit);


        System.out.println("-----切换到读模式获取数据----");
        buffer.flip();
        System.out.println("buffer.get() = " + (char) buffer.get());

        System.out.println("-----从buffer中获取元素直接调用buffer.compact()切换到写模式----");

        buffer.compact();
        capacity = buffer.capacity();
        position = buffer.position();
        limit = buffer.limit();

        System.out.printf("buffer不为空，capacity: %s position: %s limit: %s %n", capacity, position, limit);
    }
}
