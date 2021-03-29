package com.kanouakira.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * @author KanouAkira
 * @date 2021/2/23 15:28
 */
@Slf4j
@Component
public class NettyFileServer {
    /* 1 NioEventLoopGroup是一个处理I/O操作的多线程事件循环
    使用多少个线程以及如何将它们映射到创建的通道取决于EventLoopGroup的实现，甚至可以通过构造函数进行配置 */
    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1); // 通常称为“老板”，接受传入的连接。
    private final EventLoopGroup workerGroup = new NioEventLoopGroup(); // 一旦“老板”接受连接并向"工人"注册接受的连接，"工人"便处理接受的连接的流量。
    private Channel channel;

    /**
     * 启动服务
     */
    public ChannelFuture run (InetSocketAddress address){
        ChannelFuture f = null;
        try {
            ServerBootstrap b = new ServerBootstrap(); // 2 Bootstrap意为引导程序 设置服务器的帮助引导类
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // 3 指定NioServerSocketChannel用于实例化新通道以接受传入连接
                    .childHandler(new NettyServerInitializer()) // 4 指定处理链
                    .option(ChannelOption.SO_KEEPALIVE, true);
            f = b.bind(address).syncUninterruptibly();
            channel = f.channel();
        }catch (Exception e){
            log.error("Netty启动失败 : {}", e);
        }finally {
            if (f != null && f.isSuccess()){
                log.info("Netty服务端监听主机:{}, 端口:{}", address.getHostName(), address.getPort());
            } else {
                log.error("Netty启动失败!");
            }
        }
        return f;
    }

    public void destroy(){
        log.info("正在关闭Netty服务...");
        if (channel != null) channel.close();
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        log.info("关闭Netty服务成功!");
    }

}
