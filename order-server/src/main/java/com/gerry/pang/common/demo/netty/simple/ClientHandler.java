package com.gerry.pang.common.demo.netty.simple;

import com.gerry.pang.common.utils.InputUtil;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Sharable
public class ClientHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.copiedBuffer("Hello netty!", CharsetUtil.UTF_8));
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf in = (ByteBuf) msg;
		try {
			String readData = in.toString(CharsetUtil.UTF_8);
			log.info("client receive: {}", readData);
			if ("quit".equalsIgnoreCase(readData)) {
				log.info("byebye ……");
				ctx.close();
			} else {
				String inputData = InputUtil.getString("请输入发送内容:");
				byte[] data = inputData.getBytes();
				ByteBuf sendBuf = Unpooled.buffer(data.length);
				sendBuf.writeBytes(data);
				ctx.writeAndFlush(sendBuf);
			}
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error("client exception: {}", cause.getMessage());
		ctx.close();
	}
}
