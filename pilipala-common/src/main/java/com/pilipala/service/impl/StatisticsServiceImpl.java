package com.pilipala.service.impl;

import com.pilipala.component.RedisComponent;
import com.pilipala.entity.enums.StatisticsTypeEnum;
import com.pilipala.entity.enums.UserActionTypeEnum;
import com.pilipala.entity.po.UserFocus;
import com.pilipala.entity.po.Users;
import com.pilipala.entity.po.Video;
import com.pilipala.entity.query.*;
import com.pilipala.entity.po.Statistics;
import com.pilipala.entity.vo.PaginationResultVO;
import com.pilipala.mappers.StatisticsMapper;
import com.pilipala.mappers.UserFocusMapper;
import com.pilipala.mappers.UsersMapper;
import com.pilipala.mappers.VideoMapper;
import com.pilipala.utils.DateUtils;
import com.pilipala.utils.RedisConstants;
import com.pilipala.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import com.pilipala.entity.enums.PageSize;
import com.pilipala.service.StatisticsService;

/**
 * @author Gqingci
 * @Description: 数据统计对应的ServiceImpl
 * @date: 2025/11/15
 */

@Service("statisticsService")
public class StatisticsServiceImpl implements StatisticsService {

    @Resource
    private StatisticsMapper<Statistics, StatisticsQuery> statisticsMapper;

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private VideoMapper<Video, VideoQuery> videoMapper;

    @Resource
    private UserFocusMapper<UserFocus, UserFocusQuery> userFocusMapper;

    @Resource
    private UsersMapper<Users, UsersQuery> usersMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<Statistics> findListByParam(StatisticsQuery query) {
        return this.statisticsMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
    @Override
    public Integer findCountByParam(StatisticsQuery query) {
        return this.statisticsMapper.selectCount(query);
    }

    /**
     * 分页查询
     */
    @Override
    public PaginationResultVO<Statistics> findListByPage(StatisticsQuery query) {
        Integer count = this.findCountByParam(query);
        Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<Statistics> list = this.findListByParam(query);
        PaginationResultVO<Statistics> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(Statistics bean) {
        return this.statisticsMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<Statistics> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.statisticsMapper.insertBatch(listBean);
    }

    /**
     * 新增或者修改
     */
    @Override
    public Integer addOrUpdate(Statistics statistics) {
        return this.statisticsMapper.insertOrUpdate(statistics);
    }

    /**
     * 批量新增或修改
     */
    @Override
    public Integer addOrUpdateBatch(List<Statistics> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.statisticsMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 根据StatisticsDateAndUserIdAndDataType查询
     */
    @Override
    public Statistics getByStatisticsDateAndUserIdAndDataType(String statisticsDate, String userId, Integer dataType) {
        return this.statisticsMapper.selectByStatisticsDateAndUserIdAndDataType(statisticsDate, userId, dataType);
    }

    /**
     * 根据StatisticsDateAndUserIdAndDataType更新
     */
    @Override
    public Integer updateByStatisticsDateAndUserIdAndDataType(Statistics bean, String statisticsDate, String userId, Integer dataType) {
        return this.statisticsMapper.updateByStatisticsDateAndUserIdAndDataType(bean, statisticsDate, userId, dataType);
    }

    /**
     * 根据StatisticsDateAndUserIdAndDataType删除
     */
    @Override
    public Integer deleteByStatisticsDateAndUserIdAndDataType(String statisticsDate, String userId, Integer dataType) {
        return this.statisticsMapper.deleteByStatisticsDateAndUserIdAndDataType(statisticsDate, userId, dataType);
    }

    @Override
    public void statisticsData() {
        List<Statistics> statisticsList = new ArrayList<>();
        final String statisticsDate = DateUtils.getBeforeDay(1);

        // 获取视频播放量
        Map<String, Integer> videoPlayCountMap = redisComponent.getVideoPlayCount(statisticsDate);
        List<String> playVideoKeys = new ArrayList<>(videoPlayCountMap.keySet());
        playVideoKeys = playVideoKeys
                .stream()
                .map(item -> item.substring(item.indexOf(":") + 1))
                .collect(Collectors.toList());
        VideoQuery videoQuery = new VideoQuery();
        videoQuery.setVideoIdArray(playVideoKeys.toArray(new String[playVideoKeys.size()]));
        List<Video> videoList = videoMapper.selectList(videoQuery);

        Map<String, Integer> videoCountMap = videoList
                .stream()
                .collect(Collectors
                        .groupingBy(Video::getUserId, Collectors
                                .summingInt(item -> videoPlayCountMap.get(RedisConstants.REDIS_VIDEO_PLAY_COUNT_KEY + statisticsDate + ":" + item.getVideoId()))));

        videoCountMap.forEach((key, value) -> {
            Statistics statistics = new Statistics();
            statistics.setStatisticsCount(value);
            statistics.setStatisticsDate(statisticsDate);
            statistics.setUserId(key);
            statistics.setDataType(StatisticsTypeEnum.PLAY.getType());
            statisticsList.add(statistics);
        });

        // 统计粉丝数
        List<Statistics> fansDataList = this.statisticsMapper.selectStatisticsFans(statisticsDate);
        for (Statistics statistics : fansDataList) {
            statistics.setStatisticsDate(statisticsDate);
            statistics.setDataType(StatisticsTypeEnum.FANS.getType());
        }
        statisticsList.addAll(fansDataList);

        // 统计评论
        List<Statistics> commentDataList = this.statisticsMapper.selectStatisticsComment(statisticsDate);
        for (Statistics statistics : commentDataList) {
            statistics.setStatisticsDate(statisticsDate);
            statistics.setDataType(StatisticsTypeEnum.COMMENT.getType());
        }
        statisticsList.addAll(commentDataList);

        // 统计弹幕数
        List<Statistics> danmuDataList = this.statisticsMapper.selectStatisticsDanmu(statisticsDate);
        for (Statistics statistics : danmuDataList) {
            statistics.setStatisticsDate(statisticsDate);
            statistics.setDataType(StatisticsTypeEnum.DANMU.getType());
        }
        statisticsList.addAll(danmuDataList);

        // 统计点赞，收藏，投币
        List<Statistics> statisticsOthersList = this.statisticsMapper.selectStatisticsOther(statisticsDate,
                new Integer[]{UserActionTypeEnum.VIDEO_LIKE.getType(),
                        UserActionTypeEnum.VIDEO_COIN.getType(),
                        UserActionTypeEnum.VIDEO_COLLECT.getType(),});
        for (Statistics statistics : statisticsOthersList) {
            statistics.setStatisticsDate(statisticsDate);
            if (UserActionTypeEnum.VIDEO_LIKE.getType().equals(statistics.getDataType())) {
                statistics.setDataType(StatisticsTypeEnum.LIKE.getType());
            } else if (UserActionTypeEnum.VIDEO_COIN.getType().equals(statistics.getDataType())) {
                statistics.setDataType(StatisticsTypeEnum.COIN.getType());
            } else if (UserActionTypeEnum.VIDEO_COLLECT.getType().equals(statistics.getDataType())) {
                statistics.setDataType(StatisticsTypeEnum.COLLECTION.getType());
            }
        }
        statisticsList.addAll(statisticsOthersList);

        this.statisticsMapper.insertOrUpdateBatch(statisticsList);
    }

    @Override
    public Map<String, Integer> getStatisticsActualTime(String userId) {
        Map<String, Integer> result = this.statisticsMapper.selectTotalCount(userId);
        if(!StringUtils.isEmpty(userId)) {
            result.put("userCount", userFocusMapper.selectFansCount(userId));
        } else {
            result.put("userCount", usersMapper.selectCount(new UsersQuery()));
        }
        return result;
    }

    @Override
    public List<Statistics> findListTotalByParam(StatisticsQuery query) {
        return this.statisticsMapper.selectListTotalByParam(query);
    }

    @Override
    public List<Statistics> findUserCountTotalByParam(StatisticsQuery param) {
        return this.statisticsMapper.selectUserCountTotalByParam(param);
    }
}