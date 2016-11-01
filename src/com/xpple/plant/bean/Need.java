package com.xpple.plant.bean;

import cn.bmob.v3.BmobObject;

/**
 * 创建需求对象
 * 
 * @ClassName: Need
 * @Description: TODO
 * @author nEdAy
 * @date 2015-5-21 上午11:27:03
 */
public class Need extends BmobObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2530452531781608419L;
	private String title;// 标题
	private String details;// 描述
	private String price = ""; // 价格
	private String userId = ""; // 发布者
	private String transaction = ""; // 交易地

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTransaction() {
		return transaction;
	}

	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}

}
