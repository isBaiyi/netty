package com.baiyi.customprotocol;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * @description: 编码器，负责封装数据格式，然后再发出去
 * @author: liaozicai
 * @date: 2024/2/23 14:42
 */
public class MyMessageToByteEncoder extends MessageToByteEncoder<Message> {

	private static final Logger log = LoggerFactory.getLogger(MyMessageToByteEncoder.class);

	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
		log.info("MyMessageToByteEncoder invoke.");

		// 1. 魔数，4个字节 cafe
		out.writeBytes(new byte[]{'c', 'a', 'f', 'e'});

		// 2. 版本，1个字节
		out.writeByte(1);

		// 3. 序列化方式，1个字节 1代表json，2代表protobuf，其实这个需要定义为常量，暂时不定义了
		out.writeByte(1);

		// 4. 指令功能值，1个字节 1代表注册 2代表登录等
		out.writeByte(1);

		String msgJson = JSON.toJSONString(msg);
		// 5. 正文长度，4个字节
		out.writeInt(msgJson.length());

		// 6. 正文，直接使用CharSequence
		out.writeCharSequence(msgJson, StandardCharsets.UTF_8);

	}
}
