package com.baiyi.codec.custom;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @description:
 * @author: liaozicai
 * @date: 2024/2/23 10:51
 */
public class MyByteToLongDecoder extends ByteToMessageDecoder {

	private static final Logger log = LoggerFactory.getLogger(MyByteToLongDecoder.class);

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		log.info("MyByteToLongDecoder invoke.");
		// 获取可读写的指针
		int readableBytes = in.readableBytes();
		// 由于写进去的数据为long类型，占8个字节，需要判断是否足够
		if (readableBytes >= 8) {
			// 发过来的数据为"10-20"，所以这里第一次为10L，第二次为20L
			// long readLong = in.readLong();
			// out.add(readLong);
			// 一次性读完16个字节
			out.add(in.readBytes(16));
		}
	}
}
