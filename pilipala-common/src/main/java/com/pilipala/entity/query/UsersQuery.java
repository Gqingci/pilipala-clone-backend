package com.pilipala.entity.query;

import java.util.Date;

/**
 * @author Gqingci
 * @Description: 查询对象
 * @date: 2025/09/15
 */
public class UsersQuery extends BaseQuery{

	/**
	 * 用户唯一标识
	 */
	private String id;

	private String idFuzzy;

	/**
	 * 用户名
	 */
	private String username;

	private String usernameFuzzy;

	/**
	 * 存放加密后的密码
	 */
	private String passwordHash;

	private String passwordHashFuzzy;

	/**
	 * 1：男，0：女，2：未知
	 */
	private Integer gender;

	/**
	 * 出生日期
	 */
	private String birthday;

	private String birthdayFuzzy;

	/**
	 * 邮箱
	 */
	private String email;

	private String emailFuzzy;

	/**
	 * 学校
	 */
	private String school;

	private String schoolFuzzy;

	/**
	 * 个人简介
	 */
	private String biography;

	private String biographyFuzzy;

	/**
	 * 加入时间
	 */
	private Date joinDate;

	private String joinDateStart;

	private String joinDateEnd;

	/**
	 * 最后登录时间
	 */
	private Date lastLogin;

	private String lastLoginStart;

	private String lastLoginEnd;

	/**
	 * 最后登录IP
	 */
	private String lastLoginIp;

	private String lastLoginIpFuzzy;

	/**
	 * 状态 (Status: 1=Active, 0=Disabled)
	 */
	private Integer isActive;

	/**
	 * 空间公告
	 */
	private String spaceAnnouncement;

	private String spaceAnnouncementFuzzy;

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

	public void setIdFuzzy(String idFuzzy) {
		this.idFuzzy = idFuzzy;
	}

	public String getIdFuzzy() {
		return this.idFuzzy;
	}

	public void setUsernameFuzzy(String usernameFuzzy) {
		this.usernameFuzzy = usernameFuzzy;
	}

	public String getUsernameFuzzy() {
		return this.usernameFuzzy;
	}

	public void setPasswordHashFuzzy(String passwordHashFuzzy) {
		this.passwordHashFuzzy = passwordHashFuzzy;
	}

	public String getPasswordHashFuzzy() {
		return this.passwordHashFuzzy;
	}

	public void setBirthdayFuzzy(String birthdayFuzzy) {
		this.birthdayFuzzy = birthdayFuzzy;
	}

	public String getBirthdayFuzzy() {
		return this.birthdayFuzzy;
	}

	public void setEmailFuzzy(String emailFuzzy) {
		this.emailFuzzy = emailFuzzy;
	}

	public String getEmailFuzzy() {
		return this.emailFuzzy;
	}

	public void setSchoolFuzzy(String schoolFuzzy) {
		this.schoolFuzzy = schoolFuzzy;
	}

	public String getSchoolFuzzy() {
		return this.schoolFuzzy;
	}

	public void setBiographyFuzzy(String biographyFuzzy) {
		this.biographyFuzzy = biographyFuzzy;
	}

	public String getBiographyFuzzy() {
		return this.biographyFuzzy;
	}

	public void setJoinDateStart(String joinDateStart) {
		this.joinDateStart = joinDateStart;
	}

	public String getJoinDateStart() {
		return this.joinDateStart;
	}

	public void setJoinDateEnd(String joinDateEnd) {
		this.joinDateEnd = joinDateEnd;
	}

	public String getJoinDateEnd() {
		return this.joinDateEnd;
	}

	public void setLastLoginStart(String lastLoginStart) {
		this.lastLoginStart = lastLoginStart;
	}

	public String getLastLoginStart() {
		return this.lastLoginStart;
	}

	public void setLastLoginEnd(String lastLoginEnd) {
		this.lastLoginEnd = lastLoginEnd;
	}

	public String getLastLoginEnd() {
		return this.lastLoginEnd;
	}

	public void setLastLoginIpFuzzy(String lastLoginIpFuzzy) {
		this.lastLoginIpFuzzy = lastLoginIpFuzzy;
	}

	public String getLastLoginIpFuzzy() {
		return this.lastLoginIpFuzzy;
	}

	public void setSpaceAnnouncementFuzzy(String spaceAnnouncementFuzzy) {
		this.spaceAnnouncementFuzzy = spaceAnnouncementFuzzy;
	}

	public String getSpaceAnnouncementFuzzy() {
		return this.spaceAnnouncementFuzzy;
	}

}