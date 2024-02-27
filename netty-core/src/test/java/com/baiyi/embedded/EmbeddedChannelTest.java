package com.baiyi.embedded;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: EmbeddedChannel 单元测试
 * @author: liaozicai
 * @date: 2024/2/1 15:50
 */
public class EmbeddedChannelTest {

	private static final Logger log = LoggerFactory.getLogger(EmbeddedChannelTest.class);

	/**
	 * 用于测试：EmbeddedChannel
	 */
	@Test
	public void testEmbeddedChannel() {
		// 实际开发可以在web容器中启动加载，然后链式处理,这里不用内部类实现，我写成了外部类抽出去了实现
		// 声明输入输出handler
		InboundHandlerAdapter1 h1 = new InboundHandlerAdapter1();
		InboundHandlerAdapter2 h2 = new InboundHandlerAdapter2();
		InboundHandlerAdapter3 h3 = new InboundHandlerAdapter3();
		OutboundHandlerAdapter4 h4 = new OutboundHandlerAdapter4();
		OutboundHandlerAdapter5 h5 = new OutboundHandlerAdapter5();
		OutboundHandlerAdapter6 h6 = new OutboundHandlerAdapter6();

		// 把handler绑定到Channel上面
		EmbeddedChannel embeddedChannel = new EmbeddedChannel(h1, h2, h3, h4, h5, h6);
		// 读入操作，和之前的inbound一样
		embeddedChannel.writeInbound("inbound msg");
		// 写出操作，和之前的outbound一样
		embeddedChannel.writeOutbound("outbound msg");
	}

	static class InboundHandlerAdapter1 extends ChannelInboundHandlerAdapter {
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			log.info("InboundHandlerAdapter1 invoke, msg is {}", msg);
			super.channelRead(ctx, msg);
		}
	}

	static class InboundHandlerAdapter2 extends ChannelInboundHandlerAdapter {
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			log.info("InboundHandlerAdapter2 invoke, msg is {}", msg);
			super.channelRead(ctx, msg);
		}
	}

	static class InboundHandlerAdapter3 extends ChannelInboundHandlerAdapter {
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			log.info("InboundHandlerAdapter1 invoke, msg is {}", msg);
			super.channelRead(ctx, msg);
		}
	}

	static class OutboundHandlerAdapter4 extends ChannelOutboundHandlerAdapter {
		@Override
		public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
			log.info("OutboundHandlerAdapter4 invoke, msg is {}", msg);
			super.write(ctx, msg, promise);
		}
	}

	static class OutboundHandlerAdapter5 extends ChannelOutboundHandlerAdapter {
		@Override
		public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
			log.info("OutboundHandlerAdapter5 invoke, msg is {}", msg);
			super.write(ctx, msg, promise);
		}
	}

	static class OutboundHandlerAdapter6 extends ChannelOutboundHandlerAdapter {
		@Override
		public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
			log.info("OutboundHandlerAdapter6 invoke, msg is {}", msg);
			super.write(ctx, msg, promise);
		}
	}
}
