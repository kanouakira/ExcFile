package com.kanouakira.common.pojo;

import lombok.Data;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.Serializable;

/**
 * 自定义对象用于统计文件上传进度
 * @author KanouAkira
 * @date 2021/2/24 9:53
 */
@Data
public class UploadFile implements Serializable {
    private static final long serialVersionUID = 1L;
    private String file_md5; // 上传的唯一标识
    private File file; // 文件
    private String file_name; // 文件名
    private boolean file_status; // 上传状态
    private long transferredLength; // 已传输的长度
    private String file_path; // 保存的路径

    public static UploadFile getInstance(File file) throws Exception {
        UploadFile uploadFile = new UploadFile();
        String fileMd5 = DigestUtils.md5DigestAsHex(file.getName().getBytes("utf-8"));// 文件名MD5
        uploadFile.file = file;
        uploadFile.file_md5 = fileMd5;
        uploadFile.file_name = file.getName();
        return uploadFile;
    }
}
