package com.baiyi.customprotocol;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @description: 解码器，把ByteBuf转换为定义好的格式
 * @author: liaozicai
 * @date: 2024/2/23 14:51
 */
public class MyByteToMessageDecoder extends ByteToMessageDecoder {

	private static final Logger log = LoggerFactory.getLogger(MyByteToMessageDecoder.class);

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		log.info("MyByteToMessageDecoder invoke.");

		// 1. 魔数，4个字节
		ByteBuf magic = in.readBytes(4);
		log.info("魔数为: {}", magic.toString(StandardCharsets.UTF_8));

		// 2. 版本，1个字节
		byte version = in.readByte();
		log.info("版本为: {}", version);

		// 3. 序列化方式，1个字节
		byte serializeNo = in.readByte();
		log.info("序列化方式为: {}", serializeNo);

		// 4. 功能指令，1个字节
		byte function = in.readByte();
		log.info("功能指令为: {}", function);

		// 5. 正文长度，4个字节
		int contentLength = in.readInt();
		log.info("正文长度为: {}", contentLength);

		// 6. 正文
		Message message = null;
		if (serializeNo == 1) {
			// 前面已经把其他数据都取出来了，可以直接读后面剩余的数据
			message = JSON.parseObject(in.readCharSequence(contentLength, StandardCharsets.UTF_8).toString(), Message.class);
			log.info("消息正文为: {}", message);
			out.add(message);
		}
	}
}
