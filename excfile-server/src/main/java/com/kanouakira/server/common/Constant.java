package com.kanouakira.server.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 常量类
 * @author KanouAkira
 * @date 2021/2/24 9:59
 */
@Component
@ConfigurationProperties("net-disk")
public class Constant {
    public static String root;
    public void setRoot(String root){
        Constant.root = root;
    }
}
