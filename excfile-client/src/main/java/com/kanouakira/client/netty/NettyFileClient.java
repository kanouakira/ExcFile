package com.kanouakira.client.netty;

import com.kanouakira.common.pojo.UploadFile;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.File;

/**
 * @author KanouAkira
 * @date 2021/3/26 10:17
 */
public class NettyFileClient implements FileClient {

    private int port;
    private String host;
    private UploadFile uploadFile;

    public NettyFileClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(workerGroup)
                .channel(NioSocketChannel.class) // 代替NioServerSocketChannel，NioSocketChannel被用来创建客户端通道。
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new NettyClientInitializer().setUploadFile(uploadFile));
        ChannelFuture f = b.connect(host, port).sync(); // 客户端使用connect 代替 bind
        f.channel().closeFuture().sync();
        f.channel().close();
        workerGroup.shutdownGracefully();
    }

    @Override
    public FileClient setUploadFile(File file) throws Exception {
        this.uploadFile = UploadFile.getInstance(file);
        return this;
    }
}
