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
 * @date: 2025/10/04
 */
public class Users implements Serializable{

	/**
	 * 用户唯一标识
	 */
	private String id;

	/**
	 * 头像
	 */
	private String avatar;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 存放加密后的密码
	 */
	private String passwordHash;

	/**
	 * 1：男，0：女，2：未知
	 */
	private Integer gender;

	/**
	 * 出生日期
	 */
	private String birthday;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 学校
	 */
	private String school;

	/**
	 * 个人简介
	 */
	private String biography;

	/**
	 * 加入时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm::ss",timezone="GMT+8")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm::ss")
	private Date joinDate;

	/**
	 * 最后登录时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm::ss",timezone="GMT+8")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm::ss")
	private Date lastLogin;

	/**
	 * 最后登录IP
	 */
	private String lastLoginIp;

	/**
	 * 状态 (Status: 1=Active, 0=Disabled)
	 */
	private Integer isActive;

	/**
	 * 空间公告
	 */
	private String spaceAnnouncement;

	/**
	 * 硬币总数量
	 */
	private Integer totalCoinsEarned;

	/**
	 * 当前硬币数
	 */
	private Integer coinBalance;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getAvatar() {
		return this.avatar;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getPasswordHash() {
		return this.passwordHash;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Integer getGender() {
		return this.gender;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getBirthday() {
		return this.birthday;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getSchool() {
		return this.school;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	public String getBiography() {
		return this.biography;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	public Date getJoinDate() {
		return this.joinDate;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Date getLastLogin() {
		return this.lastLogin;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public String getLastLoginIp() {
		return this.lastLoginIp;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public Integer getIsActive() {
		return this.isActive;
	}

	public void setSpaceAnnouncement(String spaceAnnouncement) {
		this.spaceAnnouncement = spaceAnnouncement;
	}

	public String getSpaceAnnouncement() {
		return this.spaceAnnouncement;
	}

	public void setTotalCoinsEarned(Integer totalCoinsEarned) {
		this.totalCoinsEarned = totalCoinsEarned;
	}

	public Integer getTotalCoinsEarned() {
		return this.totalCoinsEarned;
	}

	public void setCoinBalance(Integer coinBalance) {
		this.coinBalance = coinBalance;
	}

	public Integer getCoinBalance() {
		return this.coinBalance;
	}

	@Override
	public String toString() {
		return "用户唯一标识:" + (id == null ? "空" : id) + ",头像:" + (avatar == null ? "空" : avatar) + ",用户名:" + (username == null ? "空" : username) + ",存放加密后的密码:" + (passwordHash == null ? "空" : passwordHash) + ",1：男，0：女，2：未知:" + (gender == null ? "空" : gender) + ",出生日期:" + (birthday == null ? "空" : birthday) + ",邮箱:" + (email == null ? "空" : email) + ",学校:" + (school == null ? "空" : school) + ",个人简介:" + (biography == null ? "空" : biography) + ",加入时间:" + (joinDate == null ? "空" : DateUtils.format(joinDate, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + ",最后登录时间:" + (lastLogin == null ? "空" : DateUtils.format(lastLogin, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + ",最后登录IP:" + (lastLoginIp == null ? "空" : lastLoginIp) + ",状态 (Status: 1=Active, 0=Disabled):" + (isActive == null ? "空" : isActive) + ",空间公告:" + (spaceAnnouncement == null ? "空" : spaceAnnouncement) + ",硬币总数量:" + (totalCoinsEarned == null ? "空" : totalCoinsEarned) + ",当前硬币数:" + (coinBalance == null ? "空" : coinBalance);
	}

}