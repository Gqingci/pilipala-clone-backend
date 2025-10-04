package com.pilipala.web.task;

import com.pilipala.component.RedisComponent;
import com.pilipala.entity.po.VideoFilePost;
import com.pilipala.service.VideoPostService;
import com.pilipala.utils.Constants;
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
}
