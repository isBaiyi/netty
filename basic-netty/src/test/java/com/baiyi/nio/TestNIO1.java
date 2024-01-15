package com.baiyi.nio;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @description:
 * @author: baiyi
 * @date: 2023/6/26 21:32
 */
public class TestNIO1 {

    // buffer大小
    private static final int BUFFER_SIZE = 10;

    public static void main(String[] args) throws IOException {
        // 1. 先创建channel，准备nio的组件
        FileChannel channel = new FileInputStream("/Users/baiyi/github/netty/basic-netty/src/test/resources/data.txt").getChannel();

        // 2. 创建缓冲区，注意刚创建的时候是写模式
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

        // 3. 把channel读取的数据写入缓冲区中,此时就是要往缓冲区写数据，此时注意缓冲区是写模式，创建出来默认是写模式
        // 此时数据进入了channel，我们需要把数据写到buffer里面，站在channel的角度就是读取自己的数据去buffer里面
        channel.read(buffer);

        // 4、此时程序已经在buffer里面了，此时我们需要在程序中读取buffer里面的数据，因为要读，所以要设置读模式
        buffer.flip();

        // 5. 循环读取缓冲区中的数据，直到最后读完
        while (buffer.hasRemaining()) {
            byte data = buffer.get();
            System.out.println("读到的buffer中的数据是:" + (char) data);
        }

        // 6.数据全部读完了，后面就又可以写入了，所以需要设置为写模式，因为后面也没写入了，所以这里其实设置不设置都行，但是要是你要继续写入，那就需要设置为写模式
        buffer.clear();
    }

}
