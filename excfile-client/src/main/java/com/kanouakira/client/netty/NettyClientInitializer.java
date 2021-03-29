package com.kanouakira.client.netty;

import com.kanouakira.client.netty.handler.FileUploadClientHandler;
import com.kanouakira.client.netty.handler.TransferredClientHandler;
import com.kanouakira.common.pojo.UploadFile;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author KanouAkira
 * @date 2021/3/26 10:01
 */
@Setter
@Accessors(chain = true)
public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {

    // 上传需要上传的文件
    private UploadFile uploadFile;

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast("encoder", new ObjectEncoder());
        pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
        pipeline.addLast("TransferredCheck", new TransferredClientHandler());
        pipeline.addLast("UploadHandler", new FileUploadClientHandler(uploadFile));
    }
}
