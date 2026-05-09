package com.pilipala.admin.controller;

import com.pilipala.entity.enums.StatisticsTypeEnum;
import com.pilipala.entity.po.Statistics;
import com.pilipala.entity.query.StatisticsQuery;
import com.pilipala.entity.query.UsersQuery;
import com.pilipala.entity.vo.ResponseVO;
import com.pilipala.service.StatisticsService;
import com.pilipala.service.UsersService;
import com.pilipala.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/index")
@Validated
@Slf4j
public class IndexController extends ABaseController {
    @Resource
    private StatisticsService statisticsService;

    @Resource
    private UsersService usersService;

    @RequestMapping("/getActualTimeStatisticsInfo")
    public ResponseVO getActualTimeStatisticsInfo() {
        String preDate = DateUtils.getBeforeDay(1);

        StatisticsQuery param = new StatisticsQuery();
        param.setStatisticsDate(preDate);

        List<Statistics> preDayData = statisticsService.findListTotalByParam(param);

        UsersQuery query = new UsersQuery();
        query.setJoinDateStart(preDate);
        query.setJoinDateEnd(preDate);
        Integer userCount = usersService.findCountByParam(query);
        preDayData.forEach(item -> {
            if (StatisticsTypeEnum.FANS.getType().equals(item.getDataType())) {
                item.setStatisticsCount(userCount);
            }
        });

        Map<Integer, Integer> preDayDataMap = preDayData
                .stream()
                .collect(Collectors.toMap(Statistics::getDataType, Statistics::getStatisticsCount, (item1, item2) -> item2));

        Map<String, Integer> totalCount = statisticsService.getStatisticsActualTime(null);

        Map<String, Object> result = new HashMap<>();
        result.put("preDayData", preDayDataMap);
        result.put("totalCountInfo", totalCount);

        return getSuccessResponseVo(result);
    }

    @RequestMapping("/getWeekStatisticsInfo")
    public ResponseVO getWeekStatisticsInfo(Integer dataType) {
        List<String> dateList = DateUtils.getBeforeDate(7);

        StatisticsQuery param = new StatisticsQuery();
        param.setStatisticsDateStart(dateList.get(0));
        param.setStatisticsDateEnd(dateList.get(dateList.size() - 1));
        param.setDataType(dataType);
        param.setOrderBy("statistics_date asc");
        List<Statistics> statisticsList;
        if (!StatisticsTypeEnum.FANS.getType().equals(dataType)) {
            statisticsList = statisticsService.findListTotalByParam(param);
        } else {
            statisticsList = statisticsService.findUserCountTotalByParam(param);
        }

        Map<String, Statistics> dataMap = statisticsList
                .stream()
                .collect(Collectors.toMap(Statistics::getStatisticsDate, Function.identity(), (date1, date2) -> date2));

        List<Statistics> resultList = new ArrayList<>();
        for (String date : dateList) {
            Statistics dateItem = dataMap.get(date);
            if (dateItem == null) {
                dateItem = new Statistics();
                dateItem.setStatisticsDate(date);
                dateItem.setStatisticsCount(0);
            }
            resultList.add(dateItem);
        }
        return getSuccessResponseVo(resultList);
    }
}
