package com.xpple.plant.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 实体类
 */
public class Good extends BmobObject implements Serializable {

	private static final long serialVersionUID = 3316497474443759730L;
	private Boolean key; // 供/求 1/0
	private String title = ""; // 标题
	private String price = ""; // 价格
	private String details = ""; // 细节
	private String type = ""; // 类别
	private String transaction = ""; // 交易地

	private User user; // 发布者
	private BmobRelation comment; // 评论
	private int love = 1;
	private BmobFile pica = null; // 商品主图
	private BmobFile picb = null; // 商品主图

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTransaction() {
		return transaction;
	}

	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}

	public Boolean getKey() {
		return key;
	}

	public void setKey(Boolean key) {
		this.key = key;
	}

	public BmobFile getPica() {
		return pica;
	}

	public void setPica(BmobFile pica) {
		this.pica = pica;
	}

	public BmobFile getPicb() {
		return picb;
	}

	public void setPicb(BmobFile picb) {
		this.picb = picb;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public BmobRelation getComment() {
		return comment;
	}

	public void setComment(BmobRelation comment) {
		this.comment = comment;
	}

	public int getLove() {
		return love;
	}

	public void setLove(int love) {
		this.love = love;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}