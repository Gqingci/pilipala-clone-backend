package com.pilipala.entity.query;

import java.util.Date;

/**
 * @author Gqingci
 * @Description: 查询对象
 * @date: 2025/10/24
 */
public class UserFocusQuery extends BaseQuery{

	/**
	 * 用户ID
	 */
	private String userId;

	private String userIdFuzzy;

	/**
	 * 用户ID
	 */
	private String focusUserId;

	private String focusUserIdFuzzy;

	/**
	 * 
	 */
	private Date focusTime;

	private String focusTimeStart;

	private String focusTimeEnd;

	private Integer QueryType;

	public Integer getQueryType() {
		return QueryType;
	}

	public void setQueryType(Integer getQueryType) {
		this.QueryType = getQueryType;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setFocusUserId(String focusUserId) {
		this.focusUserId = focusUserId;
	}

	public String getFocusUserId() {
		return this.focusUserId;
	}

	public void setFocusTime(Date focusTime) {
		this.focusTime = focusTime;
	}

	public Date getFocusTime() {
		return this.focusTime;
	}

	public void setUserIdFuzzy(String userIdFuzzy) {
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getUserIdFuzzy() {
		return this.userIdFuzzy;
	}

	public void setFocusUserIdFuzzy(String focusUserIdFuzzy) {
		this.focusUserIdFuzzy = focusUserIdFuzzy;
	}

	public String getFocusUserIdFuzzy() {
		return this.focusUserIdFuzzy;
	}

	public void setFocusTimeStart(String focusTimeStart) {
		this.focusTimeStart = focusTimeStart;
	}

	public String getFocusTimeStart() {
		return this.focusTimeStart;
	}

	public void setFocusTimeEnd(String focusTimeEnd) {
		this.focusTimeEnd = focusTimeEnd;
	}

	public String getFocusTimeEnd() {
		return this.focusTimeEnd;
	}

}