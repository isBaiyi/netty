package com.baiyi.handler.webSocketProtocolHandler;

import com.alibaba.fastjson2.util.DateUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @description: 自定义handler处理websocket处理后的TextWebSocketFrame
 * @author: liaozicai
 * @date: 2024/2/26 15:03
 */
public class MyWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	private static final Logger log = LoggerFactory.getLogger(MyWebSocketHandler.class);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		log.info("接收到websocket数据为: {}", msg.text());

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// 双工机制，可以继续推给客户端
				ctx.channel().writeAndFlush(new TextWebSocketFrame(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + ", 服务端接收到客户端到数据, 给你响应一个hello"));
			}
		}, 4000, 4000); // 4s一次


	}
}
