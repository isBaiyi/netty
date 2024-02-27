package com.baiyi.codec.custom;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @description: 使用ReplayingDecoder解码器可以减少安全性的校验
 * @author: liaozicai
 * @date: 2024/2/23 11:38
 */
public class MyByteToLongRelayingDecoder extends ReplayingDecoder {
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		long readLong = in.readLong();
		out.add(readLong);
	}
}
