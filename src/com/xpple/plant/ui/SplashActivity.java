package com.xpple.plant.ui;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import cn.bmob.im.BmobChat;
import cn.bmob.v3.update.BmobUpdateAgent;

import com.xpple.plant.R;
import com.xpple.plant.config.Config;
import com.xpple.plant.view.BaseActivity;

public class SplashActivity extends BaseActivity {
	private Handler mHandler;

	private ImageView gifView;// GIF动画控件
	private ImageView logoView;// LOGO动画控件

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setUp();
		if (!isRunning()) {
			initActivity();
		} else {
			mHandler = new Handler();
			mHandler.postDelayed(runnable, 500);// 延迟执行跳转
		}
	}

	private void setUp() {
		// 可设置调试模式，当为true的时候，会在logcat的BmobChat下输出一些日志，包括推送服务是否正常运行，如果服务端返回错误，也会一并打印出来。方便开发者调试
		BmobChat.DEBUG_MODE = true;
		// BmobIM SDK初始化--只需要这一段代码即可完成初始化
		BmobChat.getInstance(this).init(Config.applicationId);
		// 创建自动更新表
		// BmobUpdateAgent.initAppVersion(this);
		// 静默下载更新:当用户进入应用首页后如果处于wifi环境检测更新，如果有更新，后台下载新版本，如果下载成功，则进行通知栏展示，用户点击通知栏开始安装。静默下载过程中如果wifi断开，则会停止下载。
		BmobUpdateAgent.silentUpdate(this);
		// 开始定位
//		onLocation();
	}

	private void initActivity() {
		setContentView(R.layout.activity_splash);
		gifView = (ImageView) findViewById(R.id.activity_logo_gif);
		logoView = (ImageView) findViewById(R.id.activity_logo_name);

		final Animation logoAnim = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.activity_logo);
		logoView.startAnimation(logoAnim);

		// 动画监听，结束时播放GIF动画
		logoAnim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@SuppressWarnings("deprecation")
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				final AnimationDrawable anim = (AnimationDrawable) getResources()
						.getDrawable(R.anim.activity_droidman);
				gifView.setBackgroundDrawable(anim);
				gifView.getViewTreeObserver().addOnPreDrawListener(
						new OnPreDrawListener() {

							@Override
							public boolean onPreDraw() {
								// TODO Auto-generated method stub
								anim.start();
								return true;// 必须true才能正常显示动画效果
							}
						});
			}
		});

		mHandler = new Handler();
		mHandler.postDelayed(runnable, 4200);// 延迟执行跳转
	}

	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (userManager.getCurrentUser() != null) {
				// 每次自动登陆的时候就需要更新下当前位置和好友的资料，因为好友的头像，昵称啥的是经常变动的
				updateUserInfos();
				startAnimActivity(MainActivity.class);
			} else {
				startAnimActivity(LoginActivity.class);
			}
			SplashActivity.this.finish();
		}
	};

	private boolean isRunning() {
		boolean isRunning = true;

		return isRunning;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 停止定位
//		stopLocation();
		if (mHandler != null) {
			mHandler.removeCallbacks(runnable);
		}
	}
}
