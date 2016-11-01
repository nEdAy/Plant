package com.xpple.plant.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xpple.plant.R;
import com.xpple.plant.ui.ImActivity;
import com.xpple.plant.ui.ListActivity;
import com.xpple.plant.ui.SetActivity;
import com.xpple.plant.ui.SetMyInfoActivity;
import com.xpple.plant.util.ImageLoadOptions;
import com.xpple.plant.view.BaseFragment;
import com.xpple.plant.view.RoundImageView;

import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;

public class MeFragment extends BaseFragment implements OnClickListener,
		OnTouchListener {
	private View parentView;
	private RoundImageView avatar;
	private TextView name;
	public ImageLoader imageLoader;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		parentView = inflater.inflate(R.layout.layout_me, container, false);
		// Inflate the layout for this fragment
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
		setUpViews();
		return parentView;
	}

	private void setUpViews() {

		name = (TextView) parentView.findViewById(R.id.showName);
		avatar = (RoundImageView) parentView.findViewById(R.id.avatar);
		ImageView image1 = (ImageView) parentView.findViewById(R.id.image1);
		ImageView image2 = (ImageView) parentView.findViewById(R.id.image2);
		ImageView image3 = (ImageView) parentView.findViewById(R.id.image3);
		ImageView image4 = (ImageView) parentView.findViewById(R.id.image4);

		ImageView push_btn = (ImageView) parentView.findViewById(R.id.push_btn);
		ImageView set_btn = (ImageView) parentView.findViewById(R.id.set_btn);
		push_btn.setOnClickListener(this);
		set_btn.setOnClickListener(this);

		name.setOnClickListener(this);
		avatar.setOnClickListener(this);
		image1.setOnClickListener(this);
		image2.setOnClickListener(this);
		image3.setOnClickListener(this);
		image4.setOnClickListener(this);

		push_btn.setOnTouchListener(this);
		set_btn.setOnTouchListener(this);

		image1.setOnTouchListener(this);
		image2.setOnTouchListener(this);
		image3.setOnTouchListener(this);
		image4.setOnTouchListener(this);
		loadInformation();
	}

	private void loadInformation() {
		BmobChatUser user = BmobUserManager.getInstance(getActivity())
				.getCurrentUser();
		name.setText(user.getUsername());

		String Avatar = user.getAvatar();
		if (Avatar != null && !Avatar.equals("")) {
			ImageLoader.getInstance().displayImage(Avatar, avatar,
					ImageLoadOptions.getOptions());
		} else {
			avatar.setImageResource(R.drawable.default_head);
		}

	}

	private void setMyInfo() {
		Intent intent = new Intent(getActivity(), SetMyInfoActivity.class);
		intent.putExtra("from", "me");
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.push_btn:
			startAnimActivity(ImActivity.class);
			break;
		case R.id.set_btn:
			startAnimActivity(SetActivity.class);
			break;
		case R.id.name:
			setMyInfo();
			break;
		case R.id.avatar:
			setMyInfo();
			break;
		case R.id.image1:
			Intent intent3 = new Intent(getActivity(), ListActivity.class);
			intent3.putExtra("key", 3);
			startAnimActivity(intent3);
			break;
		case R.id.image2:
			Intent intent4 = new Intent(getActivity(), ListActivity.class);
			intent4.putExtra("key", 4);
			startAnimActivity(intent4);
			break;
		case R.id.image3:
			Intent intent5 = new Intent(getActivity(), ListActivity.class);
			intent5.putExtra("key", 5);
			startAnimActivity(intent5);
			break;
		case R.id.image4:
			Intent intent6 = new Intent(getActivity(), ListActivity.class);
			intent6.putExtra("key", 6);
			startAnimActivity(intent6);
			break;
		default:
			break;
		}
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
}
