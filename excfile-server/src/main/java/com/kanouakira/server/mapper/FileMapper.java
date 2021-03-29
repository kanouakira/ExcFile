package com.kanouakira.server.mapper;

import com.kanouakira.common.pojo.UploadFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author KanouAkira
 * @date 2021/3/26 15:20
 */
@Mapper
@Repository
public interface FileMapper {
    /**
     * 根据文件MD5 查询是否存在上传记录
     * @param MD5
     * @return
     */
    UploadFile queryByMD5(String MD5);
    int insertUploadFile(@Param("uploadFile") UploadFile uploadFile);
    int updateUploadFile(@Param("uploadFile") UploadFile uploadFile);
}
