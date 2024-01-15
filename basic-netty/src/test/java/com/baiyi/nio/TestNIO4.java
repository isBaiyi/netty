package com.baiyi.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

/**
 * @description: 通过FileChannel方式读取文件
 * @author: baiyi
 * @date: 2023/6/26 21:49
 */
public class TestNIO4 {
    // buffer的大小
    private static final int BUFFER_SIZE = 10;

    public static void main(String[] args) {
        FileChannel fileChannel = null;
        try {
            // r表示读取文件，w表示往文件写内容
            fileChannel = FileChannel.open(Paths.get("/Users/baiyi/github/netty/basic-netty/src/test/resources/data.txt"), StandardOpenOption.READ);
            ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            while (true) {
                int readLength = fileChannel.read(byteBuffer);
                if (readLength == -1) return;
                byteBuffer.flip();
                while (byteBuffer.hasRemaining()) {
                    byte data = byteBuffer.get();
                    System.out.println("读取到的数据为：" + (char) data);
                }
                byteBuffer.clear();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(fileChannel)) {
                try {
                    fileChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
