package com.pilipala.web.task;

import com.pilipala.service.StatisticsService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SysTask {
    @Resource
    private StatisticsService statisticsService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void statisticsData(){
        statisticsService.statisticsData();
    }
}
