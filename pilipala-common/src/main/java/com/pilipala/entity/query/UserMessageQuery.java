package com.pilipala.entity.query;

import java.util.Date;

/**
 * @author Gqingci
 * @Description: 用户消息表查询对象
 * @date: 2025/11/15
 */
public class UserMessageQuery extends BaseQuery{

	/**
	 * 消息ID自增
	 */
	private Integer messageId;

	/**
	 * 用户ID
	 */
	private String userId;

	private String userIdFuzzy;

	/**
	 * 主体ID
	 */
	private String videoId;

	private String videoIdFuzzy;

	/**
	 * 消息类型
	 */
	private Integer messageType;

	/**
	 * 发送人ID
	 */
	private String sendUserId;

	private String sendUserIdFuzzy;

	/**
	 * 0:未读 1:已读
	 */
	private Integer readType;

	/**
	 * 创建时间
	 */
	private Date createTime;

	private String createTimeStart;

	private String createTimeEnd;

	/**
	 * 扩展信息
	 */
	private String extendJson;

	private String extendJsonFuzzy;

	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	public Integer getMessageId() {
		return this.messageId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getVideoId() {
		return this.videoId;
	}

	public void setMessageType(Integer messageType) {
		this.messageType = messageType;
	}

	public Integer getMessageType() {
		return this.messageType;
	}

	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}

	public String getSendUserId() {
		return this.sendUserId;
	}

	public void setReadType(Integer readType) {
		this.readType = readType;
	}

	public Integer getReadType() {
		return this.readType;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setExtendJson(String extendJson) {
		this.extendJson = extendJson;
	}

	public String getExtendJson() {
		return this.extendJson;
	}

	public void setUserIdFuzzy(String userIdFuzzy) {
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getUserIdFuzzy() {
		return this.userIdFuzzy;
	}

	public void setVideoIdFuzzy(String videoIdFuzzy) {
		this.videoIdFuzzy = videoIdFuzzy;
	}

	public String getVideoIdFuzzy() {
		return this.videoIdFuzzy;
	}

	public void setSendUserIdFuzzy(String sendUserIdFuzzy) {
		this.sendUserIdFuzzy = sendUserIdFuzzy;
	}

	public String getSendUserIdFuzzy() {
		return this.sendUserIdFuzzy;
	}

	public void setCreateTimeStart(String createTimeStart) {
		this.createTimeStart = createTimeStart;
	}

	public String getCreateTimeStart() {
		return this.createTimeStart;
	}

	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public String getCreateTimeEnd() {
		return this.createTimeEnd;
	}

	public void setExtendJsonFuzzy(String extendJsonFuzzy) {
		this.extendJsonFuzzy = extendJsonFuzzy;
	}

	public String getExtendJsonFuzzy() {
		return this.extendJsonFuzzy;
	}

}