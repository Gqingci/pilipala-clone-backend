package com.pilipala.web.task;

import com.pilipala.component.EsSearchComponent;
import com.pilipala.component.RedisComponent;
import com.pilipala.dto.VideoPlayInfoDTO;
import com.pilipala.entity.enums.SearchOrderTypeEnum;
import com.pilipala.entity.po.VideoFilePost;
import com.pilipala.service.VideoPlayHistoryService;
import com.pilipala.service.VideoPostService;
import com.pilipala.service.VideoService;
import com.pilipala.service.impl.VideoServiceImpl;
import com.pilipala.utils.Constants;
import com.pilipala.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class ExecuteQueueTask {
    private final ExecutorService executorService = Executors.newFixedThreadPool(Constants.TREE);
    @Resource
    private RedisComponent redisComponent;

    @Resource
    private VideoPostService videoPostService;

    @Resource
    private VideoService videoService;

    @Resource
    private EsSearchComponent esSearchComponent;

    @Resource
    private VideoPlayHistoryService videoPlayHistoryService;

    @PostConstruct
    public void consumeTransferalQueue() {
        executorService.execute(() -> {
            while (true) {
                try {
                    VideoFilePost videoFilePost = redisComponent.getFile2TransferQueue();
                    if (videoFilePost == null) {
                        Thread.sleep(1000);
                        continue;
                    }
                    videoPostService.transferVideoFile(videoFilePost);
                } catch (InterruptedException e) {
                    log.error("获取转码文件列表失败");
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @PostConstruct
    public void consumeDelFileList() {
        executorService.execute(() -> {
            while (true) {
                try {
                    VideoPlayInfoDTO videoPlayInfoDTO = redisComponent.getVideoPlayFromVideoPlayQueue();
                    if (videoPlayInfoDTO == null) {
                        Thread.sleep(1000);
                        continue;
                    }
                    // 更新播放数
                    videoService.addReadCount(videoPlayInfoDTO.getVideoId());

                    if (!StringUtils.isEmpty(videoPlayInfoDTO.getUserId())) {
                        // 记录历史
                        videoPlayHistoryService.saveHistory(videoPlayInfoDTO.getUserId(), videoPlayInfoDTO.getVideoId(), videoPlayInfoDTO.getFileIndex());
                    }
                    // 按天记录视频播放
                    redisComponent.recordVideoPlay(videoPlayInfoDTO.getVideoId());

                    // 更新es播放量
                    esSearchComponent.updateDocCount(videoPlayInfoDTO.getVideoId(), SearchOrderTypeEnum.VIDEO_PLAY.getField(), 1);
                } catch (InterruptedException e) {
                    log.error("获取视频播放文件消息队列失败");
                }
            }
        });
    }
}
