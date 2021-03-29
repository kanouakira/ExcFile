package com.kanouakira.server.controller;

import com.kanouakira.server.common.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

/**
 * 文件控制器
 * @author KanouAkira
 * @date 2021/2/23 17:48
 */
@Slf4j
@RequestMapping("file")
@RestController
public class FileController {

    /**
     * 初始化网盘根目录
     */
    @PostConstruct
    private void init(){
        File root = new File(Constant.root);
        boolean exists = root.exists();
        if (!exists){
            log.info("网盘根目录:{}不存在", Constant.root);
            boolean mkdirs = root.mkdirs();
            log.info("初始化网盘根目录:{}", mkdirs?"成功":"失败");
        }
    }

    /**
     *
     */
    @PostMapping(value = "upload/{upload-key}", headers = "content-type=multipart/form-data")
    private void uploadFile(@PathVariable("upload-key") String uploadKey,
                            @RequestParam("file") MultipartFile file) throws IOException, InterruptedException {
//        String fileName = file.getOriginalFilename();
//        String suffix = fileName.substring(fileName.lastIndexOf("."));
//        // 用uuid作为文件名，防止生成的临时文件重复
//        File tempFile = File.createTempFile(String.valueOf(UUID.randomUUID()), suffix);
//        file.transferTo(tempFile);
//        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 10888);
//        NettyClient nettyClient = new NettyClient();
//
//        nettyClient.init();
//        try {
//            nettyClient.future = nettyClient.b.connect(address).sync();
//            UploadFile uploadFile = new UploadFile();
//            uploadFile.setUploadKey(uploadKey);
//            uploadFile.setFile(tempFile);
//            uploadFile.setFile_md5(fileName);
//            uploadFile.setStartPos(0);
//            nettyClient.sendFile(uploadFile);
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            nettyClient.future.channel().closeFuture().sync();
//            nettyClient.future.channel().close();
//            nettyClient.workerGroup.shutdownGracefully();
//        }
    }
}
