package com.pilipala.entity.query;

import java.util.Date;

/**
 * @author Gqingci
 * @Description: 视频弹幕查询对象
 * @date: 2025/10/05
 */
public class VideoDanmuQuery extends BaseQuery{

	/**
	 * 自增加
	 */
	private Integer danmuId;

	/**
	 * 视频ID
	 */
	private String videoId;

	private String videoIdFuzzy;

	/**
	 * 唯一ID
	 */
	private String fileId;

	private String fileIdFuzzy;

	/**
	 * 用户ID
	 */
	private String userId;

	private String userIdFuzzy;

	/**
	 * 发布时间
	 */
	private Date postTime;

	private String postTimeStart;

	private String postTimeEnd;

	/**
	 * 内容
	 */
	private String text;

	private String textFuzzy;

	/**
	 * 展示位置
	 */
	private Integer node;

	/**
	 * 颜色
	 */
	private String color;

	private String colorFuzzy;

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

	public void setVideoIdFuzzy(String videoIdFuzzy) {
		this.videoIdFuzzy = videoIdFuzzy;
	}

	public String getVideoIdFuzzy() {
		return this.videoIdFuzzy;
	}

	public void setFileIdFuzzy(String fileIdFuzzy) {
		this.fileIdFuzzy = fileIdFuzzy;
	}

	public String getFileIdFuzzy() {
		return this.fileIdFuzzy;
	}

	public void setUserIdFuzzy(String userIdFuzzy) {
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getUserIdFuzzy() {
		return this.userIdFuzzy;
	}

	public void setPostTimeStart(String postTimeStart) {
		this.postTimeStart = postTimeStart;
	}

	public String getPostTimeStart() {
		return this.postTimeStart;
	}

	public void setPostTimeEnd(String postTimeEnd) {
		this.postTimeEnd = postTimeEnd;
	}

	public String getPostTimeEnd() {
		return this.postTimeEnd;
	}

	public void setTextFuzzy(String textFuzzy) {
		this.textFuzzy = textFuzzy;
	}

	public String getTextFuzzy() {
		return this.textFuzzy;
	}

	public void setColorFuzzy(String colorFuzzy) {
		this.colorFuzzy = colorFuzzy;
	}

	public String getColorFuzzy() {
		return this.colorFuzzy;
	}
}