package com.kanouakira.client.netty.handler;

import com.kanouakira.common.BasicInstruction;
import com.kanouakira.common.pojo.BlockFile;
import com.kanouakira.common.pojo.UploadFile;
import com.kanouakira.common.util.MathUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author KanouAkira
 * @date 2021/2/24 10:11
 */
@Slf4j
public class FileUploadClientHandler extends SimpleChannelInboundHandler<Object> {

    private static byte[] bytes = new byte[8192 * 1024];
    private UploadFile uploadFile;

    public FileUploadClientHandler(UploadFile uploadFile){
        this.uploadFile = uploadFile;
    }

    public void channelActive(ChannelHandlerContext ctx) {
        String remote = ctx.channel().remoteAddress().toString();
        log.info("服务器{} >>> 建立连接", remote);
        // 传输文件前， 根据文件名MD5确认服务器上是否有上回中断的内容
        ctx.writeAndFlush(uploadFile);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws IOException {
        if (msg instanceof Long){
            Long startIndex = (Long) msg;
            log.info("进度:{}", MathUtil.calPercent(startIndex, uploadFile.getFile().length()));
            RandomAccessFile raf = null;
            int readByte;
            try {
                raf = new RandomAccessFile(uploadFile.getFile(), "r");
                raf.seek(startIndex);
                if ((readByte = raf.read(bytes)) != -1){
                    BlockFile bf = new BlockFile();
                    bf.setFile_md5(uploadFile.getFile_md5());
                    bf.setBytes(bytes);
                    bf.setLength(readByte);
                    ctx.writeAndFlush(bf);
                }else {
                    log.info("文件传输完成");
                    ctx.writeAndFlush(BasicInstruction.COMPLETE);
                }
            } catch (Exception e){
                log.error(e.getMessage());
            } finally {
                if (raf != null){
                    raf.close();
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(cause.getMessage());
    }
}
