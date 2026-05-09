package com.pilipala.entity.po;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.pilipala.entity.enums.DateTimePatternEnum;
import com.pilipala.utils.DateUtils;

/**
 * @author Gqingci
 * @Description: 
 * @date: 2025/10/24
 */
public class UserFocus implements Serializable{

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 用户ID
	 */
	private String focusUserId;

	/**
	 * 
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date focusTime;

	private String otherUsername;
	private String otherUserId;
	private String otherBiography;
	private String otherAvatar;
	private Integer focusType;

	public String getOtherUsername() {
		return otherUsername;
	}

	public void setOtherUsername(String otherUsername) {
		this.otherUsername = otherUsername;
	}

	public String getOtherUserId() {
		return otherUserId;
	}

	public void setOtherUserId(String otherUserId) {
		this.otherUserId = otherUserId;
	}

	public String getOtherBiography() {
		return otherBiography;
	}

	public void setOtherBiography(String otherBiography) {
		this.otherBiography = otherBiography;
	}

	public String getOtherAvatar() {
		return otherAvatar;
	}

	public void setOtherAvatar(String otherAvatar) {
		this.otherAvatar = otherAvatar;
	}

	public Integer getFocusType() {
		return focusType;
	}

	public void setFocusType(Integer focusType) {
		this.focusType = focusType;
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

	@Override
	public String toString() {
		return "用户ID:" + (userId == null ? "空" : userId) + ",用户ID:" + (focusUserId == null ? "空" : focusUserId) + ",:" + (focusTime == null ? "空" : DateUtils.format(focusTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()));
	}

}