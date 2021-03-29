package com.kanouakira.common.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author KanouAkira
 * @date 2021/3/25 17:35
 */
@Data
public class BlockFile implements Serializable {
    private static final long serialVersionUID = 1L;
    private String file_md5; // 文件名
    private byte[] bytes; // 文件字节数组
    private int length; // 数组内容长度
}
