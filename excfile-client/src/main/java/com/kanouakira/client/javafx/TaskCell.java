package com.kanouakira.client.javafx;

import com.kanouakira.client.netty.NettyFileClient;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * 自定义ListView中的Task
 * @author KanouAkira
 * @date 2021/3/29 17:19
 */
@Slf4j
public class TaskCell extends Task<Void> {
    ReadOnlyStringWrapper btnText = new ReadOnlyStringWrapper(this, "btnText", "暂停");
    private String host;
    private int port;
    private File file;
    private boolean PAUSE = false;

    TaskCell(String host, int port, File file){
        this.host = host;
        this.port = port;
        this.file = file;
    }

    @Override
    protected Void call() throws Exception {
        NettyFileClient nettyFileClient = new NettyFileClient(host, port);
        nettyFileClient.setUploadFile(file).setTask(this).run();
        return null;
    }

    public String fileName() {
        return file.getName();
    }
    public boolean isPause(){
        return PAUSE;
    }

    public void pauseTask(boolean status){
        this.PAUSE = status;
        btnText.set(PAUSE?"开始":"暂停");
    }

    public ReadOnlyStringWrapper getBtnText(){
        return btnText;
    }

    public void refreshProgress(long finishedLength, long totalLength){
        updateProgress(finishedLength, totalLength);
    }
}
