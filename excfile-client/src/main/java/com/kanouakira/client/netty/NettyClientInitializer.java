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
import javafx.concurrent.Task;
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
    // 记录任务,用于获取任务状态设置任务进度
    private Task task;

    @Override
    protected void initChannel(SocketChannel channel) {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast("encoder", new ObjectEncoder());
        pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
        pipeline.addLast("TransferredCheck", new TransferredClientHandler());
        pipeline.addLast("UploadHandler", new FileUploadClientHandler(uploadFile, task));
    }
}
