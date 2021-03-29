package com.kanouakira.server.netty;

import com.kanouakira.server.netty.handler.BaseServerHandler;
import com.kanouakira.server.netty.handler.FileUploadServerHandler;
import com.kanouakira.server.netty.handler.TransferredServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * @author KanouAkira
 * @date 2021/2/23 15:37
 */
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("encoder", new ObjectEncoder());
        pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
        pipeline.addLast(new BaseServerHandler());
        // 验证长度
        pipeline.addLast(new TransferredServerHandler());
        // 接收文件
        pipeline.addLast(new FileUploadServerHandler());
    }
}
