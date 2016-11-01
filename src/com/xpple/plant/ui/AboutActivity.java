package com.xpple.plant.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import com.xpple.plant.R;
import com.xpple.plant.view.ActivityBase;

/**
 * 关于软件
 * 
 * @author nEdAy
 */
public class AboutActivity extends ActivityBase implements OnTouchListener,
		OnClickListener {
	private ImageView app_logo, imageView1, imageView2, imageView3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		initView();
	}

	private void initView() {
		initTopBarForLeft("关于");
		app_logo = (ImageView) findViewById(R.id.app_logo);
		app_logo.setOnTouchListener(this);
		imageView1 = (ImageView) findViewById(R.id.myhead);
		imageView1.setOnClickListener(this);
		imageView1.setOnTouchListener(this);
		imageView2 = (ImageView) findViewById(R.id.imageView2);
		imageView2.setOnClickListener(this);
		imageView2.setOnTouchListener(this);
		imageView3 = (ImageView) findViewById(R.id.imageView3);
		imageView3.setOnClickListener(this);
		imageView3.setOnTouchListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO 自动生成的方法存根
		int Action = event.getAction();
		if (Action == MotionEvent.ACTION_DOWN) {
			playHeartbeatAnimation(v);
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.myhead:
			startAnimActivity(SendFeedbackActivity.class);
			break;
		case R.id.imageView2:
			startAnimActivity(PayActivity.class);
			break;
		case R.id.imageView3:
			Intent toShare = new Intent(Intent.ACTION_SEND);
			toShare.setType("text/plain");
			toShare.putExtra(Intent.EXTRA_SUBJECT, "分享");
			toShare.putExtra(Intent.EXTRA_TEXT, "校园二手" + "\n"
					+ "针对天津西青大学城测试版上线了，赶紧下载体验吧" + "http://hxxxx.bmob.cn");
			startActivity(Intent.createChooser(toShare, "分享到"));
			break;
		default:
			break;
		}
	}
}
