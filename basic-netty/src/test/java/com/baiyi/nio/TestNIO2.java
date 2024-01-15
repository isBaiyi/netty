package com.baiyi.nio;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @description:
 * @author: baiyi
 * @date: 2023/6/26 21:49
 */
public class TestNIO2 {

    private static final int BUFFER_SIZE = 10;

    public static void main(String[] args) {
        try (FileChannel channel = new FileInputStream("/Users/baiyi/github/netty/basic-netty/src/test/resources/data.txt").getChannel()) {
            // 1. 先创建channel，准备nio的组件

            // 2. 创建缓冲区，注意刚创建的时候是写模式
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

            // 3. 我们在循环里面不断的从channel中把channel的数据写到buffer中，直到最后写空了。就是你先读一批读满，然后后面不断读满，直到读出来的长度为-1
            while (true) {
                // 此时读取出来的长度
                int read = channel.read(buffer);
                // 为-1就是读取完了
                if (read == -1) break;

                buffer.flip();

                while (buffer.hasRemaining()) {
                    byte b = buffer.get();
                    System.out.println("读取到的数据: " + (char) b);
                }

                // 切换写模式，继续读取文件数据
                buffer.clear();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
