package com.pilipala.service;

import com.pilipala.entity.po.VideoFilePost;
import com.pilipala.entity.query.VideoPostQuery;
import com.pilipala.entity.po.VideoPost;
import com.pilipala.entity.vo.PaginationResultVO;

import java.util.List;

/**
 * @author Gqingci
 * @Description: 视频信息对应的Service
 * @date: 2025/10/02
 */

public interface VideoPostService {

    /**
     * 根据条件查询列表
     */
    List<VideoPost> findListByParam(VideoPostQuery query);

    /**
     * 根据条件查询数量
     */
    Integer findCountByParam(VideoPostQuery query);

    /**
     * 分页查询
     */
    PaginationResultVO<VideoPost> findListByPage(VideoPostQuery query);

    /**
     * 新增
     */
    Integer add(VideoPost bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<VideoPost> listBean);

    /**
     * 新增或修改
     */
    Integer addOrUpdate(VideoPost bean);

    /**
     * 批量新增或修改
     */
    Integer addOrUpdateBatch(List<VideoPost> listBean);

    /**
     * 根据VideoId查询
     */
    VideoPost getByVideoId(String videoId);

    /**
     * 根据VideoId查询
     */
    Integer updateByVideoId(VideoPost bean, String videoId);

    /**
     * 根据VideoId删除
     */
    Integer deleteByVideoId(String videoId);

    /**
     * 保存视频信息
     */
    void saveVideo(VideoPost videoPost, List<VideoFilePost> videoFilePostList);

    /**
     * 转码视频文件
     */
    void transferVideoFile(VideoFilePost videoFilePost);

    void auditVideo(String videoId, Integer status, String reason);
}