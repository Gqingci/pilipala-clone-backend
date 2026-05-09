package com.pilipala.entity.query;

import java.util.Date;

/**
 * @author Gqingci
 * @Description: 用户视频序列归档查询对象
 * @date: 2025/10/24
 */
public class UserVideoSeriesQuery extends BaseQuery{

	/**
	 * 列表ID
	 */
	private Integer seriesId;

	/**
	 * 列表名称
	 */
	private String seriesName;

	private String seriesNameFuzzy;

	/**
	 * 描述
	 */
	private String seriesDescription;

	private String seriesDescriptionFuzzy;

	/**
	 * 用户ID
	 */
	private String userId;

	private String userIdFuzzy;

	/**
	 * 排序
	 */
	private Integer sort;

	/**
	 * 更新时间
	 */
	private Date updateTime;

	private String updateTimeStart;

	private String updateTimeEnd;

	public void setSeriesId(Integer seriesId) {
		this.seriesId = seriesId;
	}

	public Integer getSeriesId() {
		return this.seriesId;
	}

	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}

	public String getSeriesName() {
		return this.seriesName;
	}

	public void setSeriesDescription(String seriesDescription) {
		this.seriesDescription = seriesDescription;
	}

	public String getSeriesDescription() {
		return this.seriesDescription;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getSort() {
		return this.sort;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setSeriesNameFuzzy(String seriesNameFuzzy) {
		this.seriesNameFuzzy = seriesNameFuzzy;
	}

	public String getSeriesNameFuzzy() {
		return this.seriesNameFuzzy;
	}

	public void setSeriesDescriptionFuzzy(String seriesDescriptionFuzzy) {
		this.seriesDescriptionFuzzy = seriesDescriptionFuzzy;
	}

	public String getSeriesDescriptionFuzzy() {
		return this.seriesDescriptionFuzzy;
	}

	public void setUserIdFuzzy(String userIdFuzzy) {
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getUserIdFuzzy() {
		return this.userIdFuzzy;
	}

	public void setUpdateTimeStart(String updateTimeStart) {
		this.updateTimeStart = updateTimeStart;
	}

	public String getUpdateTimeStart() {
		return this.updateTimeStart;
	}

	public void setUpdateTimeEnd(String updateTimeEnd) {
		this.updateTimeEnd = updateTimeEnd;
	}

	public String getUpdateTimeEnd() {
		return this.updateTimeEnd;
	}

}