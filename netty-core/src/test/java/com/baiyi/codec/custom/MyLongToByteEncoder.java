package com.baiyi.codec.custom;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: 自定义类型编码器，泛型为String，表示这个编码器需要处理的类型，处理的数据为"10-20"
 * @author: liaozicai
 * @date: 2024/2/23 10:43
 */
public class MyLongToByteEncoder extends MessageToByteEncoder<String> {

	private static final Logger log = LoggerFactory.getLogger(MyLongToByteEncoder.class);

	@Override
	protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
		log.info("MyLongToByteEncoder invoke.");
		String[] split = msg.split("-");
		for (String s : split) {
			long parsed = Long.parseLong(s);
			// 往外写数据
			out.writeLong(parsed);
		}
	}
}
