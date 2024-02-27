package com.baiyi.codec.custom;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

/**
 * @description: 使用ByteToMessageCodec组合体进行编码和解码操作
 * @author: liaozicai
 * @date: 2024/2/23 11:35
 */
public class MyByteToLongCodec extends ByteToMessageCodec<String> {
	@Override
	protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
		String[] split = msg.split("-");
		for (String s : split) {
			out.writeLong(Long.parseLong(s));
		}

	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		int readableBytes = in.readableBytes();
		if (readableBytes >= 8) {
			long readLong = in.readLong();
			out.add(readLong);
		}
	}
}
