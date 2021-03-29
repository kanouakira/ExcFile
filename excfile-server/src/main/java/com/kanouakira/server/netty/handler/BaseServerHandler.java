package com.kanouakira.server.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author KanouAkira
 * @date 2021/2/23 15:42
 */
@Slf4j
public class BaseServerHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        String remote = ctx.channel().remoteAddress().toString();
        log.debug("收到来自客户端{}的消息", remote);
        ctx.fireChannelRead(msg); // 传递给下一个handler
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        String remote = ctx.channel().remoteAddress().toString();
        log.info("客户端{} >>> 建立连接", remote);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        String remote = ctx.channel().remoteAddress().toString();
        log.info("客户端{} >>> 断开连接", remote);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        String remote = ctx.channel().remoteAddress().toString();
        log.info("客户端{} >>> 连接异常:{}", remote, cause.getMessage());
        // 当出现异常就关闭连接
        ctx.close();
    }
}
