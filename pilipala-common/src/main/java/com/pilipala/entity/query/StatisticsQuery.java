package com.pilipala.entity.query;


/**
 * @author Gqingci
 * @Description: 数据统计查询对象
 * @date: 2025/11/15
 */
public class StatisticsQuery extends BaseQuery{

	/**
	 * 统计日期
	 */
	private String statisticsDate;

	private String statisticsDateFuzzy;

	/**
	 * 用户ID
	 */
	private String userId;

	private String userIdFuzzy;

	/**
	 * 数据统计类型
	 */
	private Integer dataType;

	/**
	 * 统计数量
	 */
	private Integer statisticsCount;

	private String statisticsDateStart;

	private String statisticsDateEnd;

	public String getStatisticsDateStart() {
		return statisticsDateStart;
	}

	public void setStatisticsDateStart(String statisticsDateStart) {
		this.statisticsDateStart = statisticsDateStart;
	}

	public String getStatisticsDateEnd() {
		return statisticsDateEnd;
	}

	public void setStatisticsDateEnd(String statisticsDateEnd) {
		this.statisticsDateEnd = statisticsDateEnd;
	}

	public void setStatisticsDate(String statisticsDate) {
		this.statisticsDate = statisticsDate;
	}

	public String getStatisticsDate() {
		return this.statisticsDate;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public Integer getDataType() {
		return this.dataType;
	}

	public void setStatisticsCount(Integer statisticsCount) {
		this.statisticsCount = statisticsCount;
	}

	public Integer getStatisticsCount() {
		return this.statisticsCount;
	}

	public void setStatisticsDateFuzzy(String statisticsDateFuzzy) {
		this.statisticsDateFuzzy = statisticsDateFuzzy;
	}

	public String getStatisticsDateFuzzy() {
		return this.statisticsDateFuzzy;
	}

	public void setUserIdFuzzy(String userIdFuzzy) {
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getUserIdFuzzy() {
		return this.userIdFuzzy;
	}

}