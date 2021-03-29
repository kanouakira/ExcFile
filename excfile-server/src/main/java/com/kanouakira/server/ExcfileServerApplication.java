package com.kanouakira.server;

import com.kanouakira.server.netty.NettyFileServer;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetSocketAddress;

@Slf4j
@SpringBootApplication
public class ExcfileServerApplication implements CommandLineRunner {

    @Value("${CustomNettyConf.host}")
    private String host;

    @Value("${CustomNettyConf.port}")
    private int port;

    @Autowired
    private NettyFileServer nettyFileServer;

    @Override
    public void run(String... args) {
        log.info("服务启动时执行,开启Netty服务!");
        InetSocketAddress address = new InetSocketAddress(host, port);
        ChannelFuture future = nettyFileServer.run(address);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> nettyFileServer.destroy()));
        future.channel().closeFuture().syncUninterruptibly();
    }

    public static void main(String[] args) {
        SpringApplication.run(ExcfileServerApplication.class, args);
    }

}
