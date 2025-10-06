package com.pilipala.service.impl;

import com.pilipala.entity.enums.ResponseCodeEnum;
import com.pilipala.entity.enums.UserActionTypeEnum;
import com.pilipala.entity.po.Video;
import com.pilipala.entity.query.VideoDanmuQuery;
import com.pilipala.entity.query.SimplePage;
import com.pilipala.entity.po.VideoDanmu;
import com.pilipala.entity.query.VideoQuery;
import com.pilipala.entity.vo.PaginationResultVO;
import com.pilipala.exception.BusinessException;
import com.pilipala.mappers.VideoDanmuMapper;
import com.pilipala.mappers.VideoMapper;
import com.pilipala.utils.Constants;
import org.springframework.stereotype.Service;

import java.util.List;
import javax.annotation.Resource;

import com.pilipala.entity.enums.PageSize;
import com.pilipala.service.VideoDanmuService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Gqingci
 * @Description: 视频弹幕对应的ServiceImpl
 * @date: 2025/10/05
 */

@Service("videoDanmuService")
public class VideoDanmuServiceImpl implements VideoDanmuService {

    @Resource
    private VideoDanmuMapper<VideoDanmu, VideoDanmuQuery> videoDanmuMapper;

    @Resource
    private VideoMapper<Video, VideoQuery> videoMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<VideoDanmu> findListByParam(VideoDanmuQuery query) {
        return this.videoDanmuMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
    @Override
    public Integer findCountByParam(VideoDanmuQuery query) {
        return this.videoDanmuMapper.selectCount(query);
    }

    /**
     * 分页查询
     */
    @Override
    public PaginationResultVO<VideoDanmu> findListByPage(VideoDanmuQuery query) {
        Integer count = this.findCountByParam(query);
        Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<VideoDanmu> list = this.findListByParam(query);
        PaginationResultVO<VideoDanmu> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(VideoDanmu bean) {
        return this.videoDanmuMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<VideoDanmu> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.videoDanmuMapper.insertBatch(listBean);
    }

    /**
     * 新增或者修改
     */
    @Override
    public Integer addOrUpdate(VideoDanmu videoDanmu) {
        return this.videoDanmuMapper.insertOrUpdate(videoDanmu);
    }

    /**
     * 批量新增或修改
     */
    @Override
    public Integer addOrUpdateBatch(List<VideoDanmu> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.videoDanmuMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 根据DanmuId查询
     */
    @Override
    public VideoDanmu getByDanmuId(Integer danmuId) {
        return this.videoDanmuMapper.selectByDanmuId(danmuId);
    }

    /**
     * 根据DanmuId更新
     */
    @Override
    public Integer updateByDanmuId(VideoDanmu bean, Integer danmuId) {
        return this.videoDanmuMapper.updateByDanmuId(bean, danmuId);
    }

    /**
     * 根据DanmuId删除
     */
    @Override
    public Integer deleteByDanmuId(Integer danmuId) {
        return this.videoDanmuMapper.deleteByDanmuId(danmuId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveVideoDanmu(VideoDanmu videoDanmu) {
        Video video = videoMapper.selectByVideoId(videoDanmu.getVideoId());
        if (video == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (video.getInteraction() != null && video.getInteraction().contains(Constants.ONE.toString())) {
            throw new BusinessException("弹幕已被关闭");
        }
        this.videoDanmuMapper.insert(videoDanmu);

        this.videoMapper.updateCount(video.getVideoId(), UserActionTypeEnum.VIDEO_DANMU.getField(), 1);

        // TODO 更新es弹幕数量
    }
}