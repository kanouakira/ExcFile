package com.kanouakira.server.netty.handler;

import com.kanouakira.common.BasicInstruction;
import com.kanouakira.common.pojo.UploadFile;
import com.kanouakira.server.common.BeanContext;
import com.kanouakira.server.common.Constant;
import com.kanouakira.server.mapper.FileMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * 根据MD5 校验是否传输过，返回传输过的长度
 * 接收 UploadFile
 * 返回 UploadFile
 * @author KanouAkira
 * @date 2021/3/26 13:54
 */
@Slf4j
public class TransferredServerHandler extends SimpleChannelInboundHandler<Object> {

    private String pathTemplate = Constant.root + File.separator + "%s";
    private UploadFile uploadFile;
    private FileMapper fileMapper;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        // 接收到开始传输的信号
        if (msg instanceof UploadFile){
            UploadFile uf = (UploadFile) msg;
            this.uploadFile = uf;
            String path = String.format(pathTemplate, uf.getFile_md5());
            // 容器外获取bean
            fileMapper = BeanContext.getBean(FileMapper.class);
            // 检查是否已完成
            UploadFile uploadFile = fileMapper.queryByMD5(uf.getFile_md5());
            if (uploadFile != null && uploadFile.isFile_status()){
                // 回复客户端传输完成
                ctx.writeAndFlush(BasicInstruction.COMPLETE);
                return;
            }
            // 传输未完成或未开始
            File file = new File(path);
            if (file.exists() && file.isFile()){
                // 存在对应文件， 从上回结束的字节开始传输
                uf.setTransferredLength(file.length());
            }else {
                // 文件不存在，视为新上传从0开始
                uf.setTransferredLength(0);
                int i = fileMapper.insertUploadFile(uf);
                log.debug("初始化任务:{}", i==0?"失败":"成功");
            }
            ctx.writeAndFlush(uf);
            return;
        }
        // 接收到基本指令
        if (msg instanceof BasicInstruction){
            BasicInstruction bs = (BasicInstruction) msg;
            // 传输完成
            if (bs.name() == "COMPLETE"){
                // 重命名
                String path = String.format(pathTemplate, uploadFile.getFile_md5());
                File file = new File(path);
                boolean rename = file.renameTo(new File(String.format(pathTemplate, uploadFile.getFile_name())));
                log.debug("重命名{}", rename ? "成功" : "失败");
                // 修改数据库状态
                uploadFile.setFile_path(String.format("/net-disk/%s", uploadFile.getFile_name()));
                uploadFile.setFile_status(true);
                int i = fileMapper.updateUploadFile(uploadFile);
                // 回复客户端传输完成
                ctx.writeAndFlush(BasicInstruction.COMPLETE);
                log.info("接收{}完成", uploadFile.getFile_name());
                return;
            }
        }
        // 可能为传输过程中的数据，交给下个handler
        ctx.fireChannelRead(msg);
    }
}
