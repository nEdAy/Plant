package com.xpple.plant.bean;

import cn.bmob.v3.BmobObject;

public class Feedback extends BmobObject {
	private static final long serialVersionUID = 1L;
	// 反馈内容
	private String content;
	// 联系方式
	private String contacts;

	private User user;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
