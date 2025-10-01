package com.pilipala.entity.po;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Gqingci
 * @Description: 分类信息
 * @date: 2025/10/01
 */

public class Category implements Serializable{

	/**
	 * 自增分类ID
	 */
	private Integer categoryId;

	/**
	 * 分类编码
	 */
	private String categoryCode;

	/**
	 * 分类名称
	 */
	private String categoryName;

	/**
	 * 父级分类ID
	 */
	private Integer pCategoryId;

	/**
	 * 排序号
	 */
	private Integer sort;

	private List<Category> children;

	public List<Category> getChildren() {
		return children;
	}

	public void setChildren(List<Category> children) {
		this.children = children;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getCategoryCode() {
		return this.categoryCode;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryName() {
		return this.categoryName;
	}

	public void setPCategoryId(Integer pCategoryId) {
		this.pCategoryId = pCategoryId;
	}

	public Integer getPCategoryId() {
		return this.pCategoryId;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getSort() {
		return this.sort;
	}

	@Override
	public String toString() {
		return "自增分类ID:" + (categoryId == null ? "空" : categoryId) + ",分类编码:" + (categoryCode == null ? "空" : categoryCode) + ",分类名称:" + (categoryName == null ? "空" : categoryName) + ",父级分类ID:" + (pCategoryId == null ? "空" : pCategoryId) + ",排序号:" + (sort == null ? "空" : sort);
	}

}