package com.kanouakira.server.netty.handler;

import com.kanouakira.common.pojo.BlockFile;
import com.kanouakira.server.common.Constant;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 接收文件分块字节数据
 * @author KanouAkira
 * @date 2021/3/24 16:57
 */
@Slf4j
public class FileUploadServerHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 接收处理分块后的字节数据
        if (msg instanceof BlockFile){
            BlockFile bf = (BlockFile) msg;
            String path = Constant.root + File.separator + bf.getFile_md5();
            File file = new File(path);
            long length = file.length();
            FileOutputStream fos = null;
            try {
                // 追加写入
                fos = new FileOutputStream(file, true);
                fos.write(bf.getBytes(), 0, bf.getLength());
                // 返回当前长度
                long transferredLength = length + bf.getLength();
                ctx.writeAndFlush(transferredLength);
            } catch (Exception e){
                log.error(e.getMessage());
            } finally {
                // 关闭流
                if (fos != null){
                    fos.close();
                }
            }
        }
    }
}
