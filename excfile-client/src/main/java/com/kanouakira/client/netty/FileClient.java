package com.kanouakira.client.netty;

import javafx.concurrent.Task;

import java.io.File;

/**
 * @author KanouAkira
 * @date 2021/3/26 10:05
 */
public interface FileClient {

    /**
     * 启动上传
     * @throws Exception
     */
    void run() throws Exception;

    /**
     * 设置上传的文件
     * @param file 上传的文件
     * @return
     */
    FileClient setUploadFile(File file) throws Exception;

    /**
     * 设置上传的任务
     * @param task 上传的文件
     * @return
     */
    FileClient setTask(Task task) throws Exception;

}
