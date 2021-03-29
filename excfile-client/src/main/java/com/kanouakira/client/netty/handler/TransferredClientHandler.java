package com.kanouakira.client.netty.handler;

import com.kanouakira.common.BasicInstruction;
import com.kanouakira.common.pojo.UploadFile;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author KanouAkira
 * @date 2021/3/26 13:42
 */
@Slf4j
public class TransferredClientHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof UploadFile){
            UploadFile uf = (UploadFile) msg;
            ctx.fireChannelRead(uf.getTransferredLength());
        }
        if (msg instanceof BasicInstruction){
            BasicInstruction bs = (BasicInstruction) msg;
            // 传输完成
            if (bs.name() == "COMPLETE"){
                // TODO: 2021/3/26 传输完成操作
                log.info("传输完成,关闭通道");
                ctx.close();
            }
        }
        ctx.fireChannelRead(msg);
    }
}
