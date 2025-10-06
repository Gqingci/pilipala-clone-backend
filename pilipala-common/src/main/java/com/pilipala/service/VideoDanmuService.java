package com.pilipala.service;

import com.pilipala.entity.query.VideoDanmuQuery;
import com.pilipala.entity.po.VideoDanmu;
import com.pilipala.entity.vo.PaginationResultVO;
import java.util.List;
/**
 * @author Gqingci
 * @Description: 视频弹幕对应的Service
 * @date: 2025/10/05
 */

public interface VideoDanmuService{

	/**
	 * 根据条件查询列表
	 */
	List<VideoDanmu>findListByParam(VideoDanmuQuery query);

	/**
	 * 根据条件查询数量
	 */
    Integer findCountByParam(VideoDanmuQuery query);

	/**
	 * 分页查询
	 */
	PaginationResultVO<VideoDanmu> findListByPage(VideoDanmuQuery query );

	/**
	 * 新增
	 */
	Integer add(VideoDanmu bean);
	/**
	 * 批量新增
	 */
	Integer addBatch(List<VideoDanmu> listBean);
	/**
	 * 新增或修改
	 */
	Integer addOrUpdate(VideoDanmu bean);
	/**
	 * 批量新增或修改
	 */
	Integer addOrUpdateBatch(List<VideoDanmu> listBean);
	/**
	 * 根据DanmuId查询
	 */
	 VideoDanmu getByDanmuId(Integer danmuId);

	/**
	 * 根据DanmuId查询
	 */
	 Integer updateByDanmuId(VideoDanmu bean , Integer danmuId);

	/**
	 * 根据DanmuId删除
	 */
	 Integer deleteByDanmuId(Integer danmuId);


    void saveVideoDanmu(VideoDanmu videoDanmu);
}