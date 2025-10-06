package com.pilipala.entity.po;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.pilipala.entity.enums.DateTimePatternEnum;
import com.pilipala.utils.DateUtils;

/**
 * @author Gqingci
 * @Description: 视频弹幕
 * @date: 2025/10/05
 */
public class VideoDanmu implements Serializable{

	/**
	 * 自增加
	 */
	private Integer danmuId;

	/**
	 * 视频ID
	 */
	private String videoId;

	/**
	 * 唯一ID
	 */
	private String fileId;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 发布时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm::ss",timezone="GMT+8")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm::ss")
	private Date postTime;

	/**
	 * 内容
	 */
	private String text;

	/**
	 * 展示位置
	 */
	private Integer node;

	/**
	 * 颜色
	 */
	private String color;

	/**
	 * 展示时间
	 */
	private Integer time;

	public void setDanmuId(Integer danmuId) {
		this.danmuId = danmuId;
	}

	public Integer getDanmuId() {
		return this.danmuId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getVideoId() {
		return this.videoId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileId() {
		return this.fileId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}

	public Date getPostTime() {
		return this.postTime;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	public void setNode(Integer node) {
		this.node = node;
	}

	public Integer getNode() {
		return this.node;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getColor() {
		return this.color;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public Integer getTime() {
		return this.time;
	}

	@Override
	public String toString() {
		return "自增加:" + (danmuId == null ? "空" : danmuId) + ",视频ID:" + (videoId == null ? "空" : videoId) + ",唯一ID:" + (fileId == null ? "空" : fileId) + ",用户ID:" + (userId == null ? "空" : userId) + ",发布时间:" + (postTime == null ? "空" : DateUtils.format(postTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + ",内容:" + (text == null ? "空" : text) + ",展示位置:" + (node == null ? "空" : node) + ",颜色:" + (color == null ? "空" : color) + ",展示时间:" + (time == null ? "空" : time);
	}

}