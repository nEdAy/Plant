package com.xpple.plant.bean;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 重载BmobChatUser对象：若还有其他需要增加的属性可在此添加
 * 
 * @ClassName: TextUser
 * @Description: TODO
 * @author smile
 * @date 2014-5-29 下午6:15:45
 */
public class User extends BmobChatUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = -483239423981591772L;

	/**
	 * //性别-true-男
	 */
	private boolean sex;

	private String school;

	private String number;

	private String qq;

	private String phone;

	private String weixin;
	/**
	 * 用户的货物 一个人可能有多个不同的货物，这里选择使用了BmobRelation类型
	 */
	private BmobRelation good;
	
	private BmobRelation favorite;

	private BmobRelation feedback;
	/**
	 * 地理坐标
	 */
	private BmobGeoPoint location;

	public BmobGeoPoint getLocation() {
		return location;
	}

	public void setLocation(BmobGeoPoint location) {
		this.location = location;
	}

	public boolean getSex() {
		return sex;
	}

	public void setSex(boolean sex) {
		this.sex = sex;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public BmobRelation getGood() {
		return good;
	}

	public void setGood(BmobRelation good) {
		this.good = good;
	}

	public BmobRelation getFavorite() {
		return favorite;
	}

	public void setFavorite(BmobRelation favorite) {
		this.favorite = favorite;
	}

	public BmobRelation getFeedback() {
		return feedback;
	}

	public void setFeedback(BmobRelation feedback) {
		this.feedback = feedback;
	}

}
