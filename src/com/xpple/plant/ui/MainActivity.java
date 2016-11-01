package com.xpple.plant.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xpple.plant.R;
import com.xpple.plant.ui.fragment.ClassesFragment;
import com.xpple.plant.ui.fragment.FindFragment;
import com.xpple.plant.ui.fragment.MeFragment;
import com.xpple.plant.view.ActionSheet;
import com.xpple.plant.view.ActionSheet.ActionSheetListener;
import com.xpple.plant.view.ActivityBase;

import cn.bmob.v3.update.BmobUpdateAgent;

public class MainActivity extends ActivityBase implements View.OnClickListener,
		ActionSheetListener {

    private FragmentManager fm;

	private LinearLayout mTabIndex, mTabClass, mTabFind, mTabMe;

	private ImageView mIndexImg, mClassImg, mFindImg, mMeImg;

	private Fragment tabindex, tabclass, tabfind, tabme;

	private TextView title_text;

	private ImageView title_search_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initEvent();
		setSelect(0);
	}

	private void initView() {

		mTabIndex = (LinearLayout) findViewById(R.id.id_index);
		mTabClass = (LinearLayout) findViewById(R.id.id_class);
		mTabFind = (LinearLayout) findViewById(R.id.id_find);
		mTabMe = (LinearLayout) findViewById(R.id.id_me);

		mIndexImg = (ImageView) findViewById(R.id.id_indeximg);
		mClassImg = (ImageView) findViewById(R.id.id_classimg);
		mFindImg = (ImageView) findViewById(R.id.id_findimg);
		mMeImg = (ImageView) findViewById(R.id.id_meimg);

		title_text = (TextView) findViewById(R.id.title_text);
		title_search_btn = (ImageView) findViewById(R.id.title_search_btn);

	}

	private void initEvent() {
		mTabIndex.setOnClickListener(this);
		mTabClass.setOnClickListener(this);
		mTabFind.setOnClickListener(this);
		mTabMe.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.id_index:
			setSelect(0);
			break;
		case R.id.id_class:
			setSelect(1);
			break;
		case R.id.id_find:
			setSelect(2);
			break;
		case R.id.id_me:
			setSelect(3);
			break;
		}
	}

	private void setSelect(int i) {
		switch (i) {
        case 0:
            if (tabclass == null) {
				tabclass = new ClassesFragment();
            }
            changeFragment(tabclass);
            break;
        case 1:
            if (tabclass == null) {
            	tabclass = new ClassesFragment();
            }
            changeFragment(tabclass);
            break;
        case 2:
            if (tabfind == null) {
            	tabfind = new FindFragment();
            }
            changeFragment(tabfind);
            break;
        case 3:
            if (tabme == null) {
            	tabme = new MeFragment();
            }
            changeFragment(tabme);
            break;
    }
		setTab(i);

	}

	private void setTab(int i) {
		// 默认图片
		resetImgs();
		// 设置图片为亮色,切换内容区域
		switch (i) {
		case 0:
			title_search_btn.setVisibility(View.GONE);
			title_text.setVisibility(View.VISIBLE);
			title_text.setClickable(true);
			title_text.setText("天津理工大学(华信)");
			title_text.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ShowToast("暂时无法切换学校，敬请期待");
				}
			});
			mIndexImg.setImageResource(R.drawable.index_pressed);
			break;
		case 1:
			title_text.setVisibility(View.GONE);
			title_search_btn.setVisibility(View.VISIBLE);
			title_search_btn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// startActivity(new Intent(getActivity(),
					// SchoolListActivity.class));
				}
			});
			mClassImg.setImageResource(R.drawable.classes_pressed);
			break;
		case 2:
			title_search_btn.setVisibility(View.GONE);
			title_text.setVisibility(View.VISIBLE);
			title_text.setClickable(false);
			title_text.setText("发现");
			mFindImg.setImageResource(R.drawable.find_pressed);
			break;
		case 3:
			title_search_btn.setVisibility(View.GONE);
			title_text.setVisibility(View.VISIBLE);
			title_text.setClickable(false);
			title_text.setText("我");
			mMeImg.setImageResource(R.drawable.me_pressed);
			break;
		}
	}
    private void changeFragment(Fragment targetFragment){
        fm = getSupportFragmentManager();
        if(fm!=null) {
            fm.beginTransaction()
                    .replace(R.id.fmcontent, targetFragment, "fragment")
                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }
    }
	private void resetImgs() {
		mIndexImg.setImageResource(R.drawable.index);
		mClassImg.setImageResource(R.drawable.classes);
		mFindImg.setImageResource(R.drawable.find);
		mMeImg.setImageResource(R.drawable.me);
	}

	Boolean ActionSheetFlag = false;
	private static long firstTime;

	/**
	 * 连续按两次返回键就退出
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (ActionSheetFlag) {
			super.onBackPressed();
		} else {
			if (firstTime + 2000 > System.currentTimeMillis()) {
				super.onBackPressed();
			} else {
				ShowToast("再按一次退出程序");
			}
			firstTime = System.currentTimeMillis();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			// 在这里做你想做的事情
			if (!ActionSheetFlag) {
				setTheme(R.style.ActionSheetStyleIOS7);
				showActionSheet(); // 调用这个，就可以弹出菜单
			}
		}
		return super.onKeyDown(keyCode, event);

	}

	public void showActionSheet() {
		ActionSheetFlag = true;
		ActionSheet.createBuilder(this, getSupportFragmentManager())
				.setCancelButtonTitle("取消")
				.setOtherButtonTitles("版本更新", "关于与合作", "退出应用")
				.setCancelableOnTouchOutside(true).setListener(this).show();
	}

	@SuppressLint("ShowToast")
	@Override
	public void onOtherButtonClick(ActionSheet actionSheet, int index) {
		if (index == 0) {
			BmobUpdateAgent.forceUpdate(this);
			ShowToast("已是最新版本");
		} else if (index == 1) {
			startAnimActivity(new Intent(MainActivity.this, AboutActivity.class));
		} else if (index == 2) {
			this.finish();
		}
		ActionSheetFlag = false;
	}

	@Override
	public void onDismiss(ActionSheet actionSheet, boolean isCancle) {
		ActionSheetFlag = false;
	}

}
