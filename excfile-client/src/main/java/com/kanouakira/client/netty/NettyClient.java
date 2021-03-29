package com.kanouakira.client.netty;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.File;

/**
 * @author KanouAkira
 * @date 2021/2/23 15:51
 */
@Slf4j
public class NettyClient {
    public static void main(String[] args) throws Exception {
//        NettyFileClient nettyFileClient = new NettyFileClient("127.0.0.1", 10888);
//        File file = new File("D:\\nginx-1.19.4.zip");
//        nettyFileClient.setUploadFile(file).run();
        JFrame excFile = new JFrame("ExcFile");
        excFile.setSize(400, 300);
        excFile.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* 创建面板，这个类似于 HTML 的 div 标签
         * 我们可以创建多个面板并在 JFrame 中指定位置
         * 面板中我们可以添加文本字段，按钮及其他组件。
         */
        JPanel panel = new JPanel();
        excFile.add(panel);
        init(panel);

        excFile.setVisible(true);
    }

    private static void init(JPanel panel){
        panel.setLayout(null);

        JLabel hostLabel = new JLabel("主机地址:");
        /* 这个方法定义了组件的位置。
         * setBounds(x, y, width, height)
         * x 和 y 指定左上角的新位置，由 width 和 height 指定新的大小。
         */
        hostLabel.setBounds(10,20,80,25);
        panel.add(hostLabel);
        /*
         * 创建文本域用于用户输入
         */
        JTextField hostText = new JTextField(20);
        hostText.setBounds(100,20,165,25);
        panel.add(hostText);

        JLabel portLabel = new JLabel("端口:");
        /* 这个方法定义了组件的位置。
         * setBounds(x, y, width, height)
         * x 和 y 指定左上角的新位置，由 width 和 height 指定新的大小。
         */
        portLabel.setBounds(10,50,80,25);
        panel.add(portLabel);
        /*
         * 创建文本域用于用户输入
         */
        JTextField portText = new JTextField(20);
        portText.setBounds(100,50,165,25);
        panel.add(portText);
    }
}
