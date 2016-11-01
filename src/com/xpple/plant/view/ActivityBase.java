package com.xpple.plant.view;

import android.os.Bundle;
import cn.bmob.im.BmobUserManager;

import com.xpple.plant.ui.LoginActivity;

/**
 * 除登陆注册和欢迎页面外继承的基类-用于检测是否有其他设备登录了同一账号
 * 
 * @ClassName: ActivityBase
 * @Description: TODO
 * @author smile
 * @date 2014-6-13 下午5:18:24
 */
public abstract class ActivityBase extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 自动登陆状态下检测是否在其他设备登陆
		checkLogin();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 锁屏状态下的检测
		checkLogin();
	}

	public void checkLogin() {
		BmobUserManager userManager = BmobUserManager.getInstance(this);
		if (userManager.getCurrentUser() == null) {
			ShowToast("您的账号已在其他设备上登录!");
			startAnimActivity(LoginActivity.class);
			finish();
		}
	}

}
