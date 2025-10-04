package com.pilipala.mappers;

import com.pilipala.entity.po.VideoFilePost;
import com.pilipala.entity.query.VideoFilePostQuery;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Gqingci
 * @Description: 视频文件信息的Mapper类
 * @date: 2025/10/02
 */

@Mapper
public interface VideoFilePostMapper<T, P> extends BaseMapper {
    /**
     * 根据FileId查询
     */
    T selectByFileId(@Param("fileId") String fileId);

    /**
     * 根据FileId更新
     */
    Integer updateByFileId(@Param("bean") T t, @Param("fileId") String fileId);

    /**
     * 根据FileId删除
     */
    Integer deleteByFileId(@Param("fileId") String fileId);

    /**
     * 根据UploadIdAndUserId查询
     */
    T selectByUploadIdAndUserId(@Param("uploadId") String uploadId, @Param("userId") String userId);

    /**
     * 根据UploadIdAndUserId更新
     */
    Integer updateByUploadIdAndUserId(@Param("bean") T t, @Param("uploadId") String uploadId, @Param("userId") String userId);

    /**
     * 根据UploadIdAndUserId删除
     */
    Integer deleteByUploadIdAndUserId(@Param("uploadId") String uploadId, @Param("userId") String userId);

    /**
     * 批量删除
     */
    void deleteBatchByFileIdList(@Param("fileIdList") List<String> fileIdList, @Param("userId") String userId);

    /**
     * 根据视频id查询视频时长
     */
    Integer sumDuration(@Param("videoId") String videoId);

    /**
     * 通用条件更新
     */
    Integer updateByParam(@Param("bean") VideoFilePost bean, @Param("query") VideoFilePostQuery query);
}