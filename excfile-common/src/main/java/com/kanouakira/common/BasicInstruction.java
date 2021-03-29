package com.kanouakira.common;

import lombok.Getter;

/**
 * 基本的指令 只有四个字节
 * @author KanouAkira
 * @date 2021/3/26 14:34
 */
public enum BasicInstruction {
    /**完成指令:接收到该指令服务端会对传输的文件进行重命名，并且修改数据库状态**/
    COMPLETE(0x12345678);

    @Getter
    private  int value;

    BasicInstruction(int value) {
        this.value = value;
    }
}
