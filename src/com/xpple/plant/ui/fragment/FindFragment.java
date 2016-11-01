package com.xpple.plant.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ViewSwitcher.ViewFactory;

import com.xpple.plant.R;
import com.xpple.plant.ui.EditActivity;
import com.xpple.plant.ui.ListActivity;
import com.xpple.plant.ui.NearPeopleActivity;
import com.xpple.plant.ui.WebActivity;
import com.xpple.plant.view.BaseFragment;
import com.xpple.plant.view.Rotate3D;
import com.xpple.plant.view.SolarSystem;
import com.xpple.plant.view.SolarSystem.MenuStatus;
import com.xpple.plant.view.SolarSystem.onMenuItemClickListener;

@SuppressWarnings("deprecation")
public class FindFragment extends BaseFragment {
	private View parentView;
	int[] imageIds = new int[] { R.drawable.a, R.drawable.b, R.drawable.c,
			R.drawable.d };
	View[] views = new View[4];
	ImageSwitcher imswitcher;
	GestureDetector mGestureDetector;
	int i = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		parentView = inflater.inflate(R.layout.layout_find, container, false);
		setUpViews();
		return parentView;
	}

	@SuppressWarnings("unchecked")
	private void setUpViews() {
		imswitcher = (ImageSwitcher) parentView
				.findViewById(R.id.imageSwitcher1);
		imswitcher.setFactory(new ViewFactory() {
			@Override
			public View makeView() {
				ImageView imageView = new ImageView(getActivity());
				imageView.setBackgroundColor(0xff0000);
				imageView.setScaleType(ImageView.ScaleType.FIT_START);
				imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
						android.view.ViewGroup.LayoutParams.FILL_PARENT,
						android.view.ViewGroup.LayoutParams.FILL_PARENT));
				return imageView;
			}
		});

		imswitcher.setImageResource(R.drawable.a);
		View v1 = parentView.findViewById(R.id.view1);
		View v2 = parentView.findViewById(R.id.view2);
		View v3 = parentView.findViewById(R.id.view3);
		View v4 = parentView.findViewById(R.id.view4);
		views[0] = v1;
		views[1] = v2;
		views[2] = v3;
		views[3] = v4;
		mGestureDetector = new GestureDetector(getActivity(),
				new MyGestureListener());
		imswitcher.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				mGestureDetector.onTouchEvent(event);
				return true;
			}
		});

		DownloadTask dTask = new DownloadTask();
		dTask.execute(100);

		RelativeLayout relative = (RelativeLayout) parentView
				.findViewById(R.id.relative);
		DisplayMetrics outMetrics = new DisplayMetrics();
		getActivity().getWindow().getWindowManager().getDefaultDisplay()
				.getMetrics(outMetrics);
		int screenWidth = outMetrics.widthPixels;
		LayoutParams lp = relative.getLayoutParams();
		lp.width = screenWidth / 3;
		lp.height = screenWidth / 3;
		relative.setLayoutParams(lp);
		relative.setBackgroundResource(R.drawable.animation_rect_list);
		animationDrawable = (AnimationDrawable) relative.getBackground();
		SolarSystem solarSystem = (SolarSystem) parentView
				.findViewById(R.id.solar);
		int childCount = solarSystem.getChildCount();
		FrameLayout.LayoutParams tvParams = new FrameLayout.LayoutParams(
				screenWidth / 5, screenWidth / 5);

		for (int i = 0; i < childCount - 1; i++) {
			solarSystem.getChildAt(i).setLayoutParams(tvParams);
		}
		solarSystem.setRadius(screenWidth / 3);
		solarSystem.setRotaMenu(true);
		solarSystem.setOnMenuStatus(new MenuStatus() {
			@Override
			public void openMenu() {
				animationDrawable.start();
				handler.sendEmptyMessageDelayed(101, 600);
			}

			@Override
			public void closeMenu() {
				animationDrawable.start();
				handler.sendEmptyMessageDelayed(101, 600);
			}
		});
		solarSystem.setOnMenuItemClickListener(new onMenuItemClickListener() {

			@Override
			public void onItem(View view, int position) {
				String tag = (String) view.getTag();
				switch (tag) {
				case "a":
					startAnimActivity(WebActivity.class);
					break;
				case "b":
					Intent intent1 = new Intent(getActivity(),
							ListActivity.class);
					intent1.putExtra("key", 1);
					startAnimActivity(intent1);
					break;
				case "c":
					Intent intent2 = new Intent(getActivity(),
							ListActivity.class);
					intent2.putExtra("key", 2);
					startAnimActivity(intent2);
					break;
				case "d":
					Intent intent3 = new Intent(getActivity(),
							EditActivity.class);
					intent3.putExtra("key", 1);
					startAnimActivity(intent3);
					break;
				case "e":
					Intent intent4 = new Intent(getActivity(),
							EditActivity.class);
					intent4.putExtra("key", 2);
					startAnimActivity(intent4);
					break;
				case "f":
					startAnimActivity(NearPeopleActivity.class);
					break;
				default:
					break;
				}

			}
		});

	}

	public void setpic(int m) {

		for (int i = 0; i < views.length; i++) {
			if (i == m) {
				views[i].setBackgroundColor(0xffb50202);
			} else {
				views[i].setBackgroundColor(0xffebeaea);
			}

		}

	}

	private class MyGestureListener implements
			GestureDetector.OnGestureListener {

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub

			if (velocityX > 0) {
				// Toast.makeText(getApplicationContext(), "速度大于0",
				// Toast.LENGTH_SHORT).show();

				float halfWidth = imswitcher.getWidth() / 2.0f;
				float halfHeight = imswitcher.getHeight() / 2.0f;
				int duration = 500;

				Rotate3D rdin = new Rotate3D(-75, 0, 0, halfWidth, halfHeight);
				rdin.setDuration(duration);
				rdin.setFillAfter(true);
				imswitcher.setInAnimation(rdin);
				Rotate3D rdout = new Rotate3D(15, 90, 0, halfWidth, halfHeight);

				rdout.setDuration(duration);
				rdout.setFillAfter(true);
				imswitcher.setOutAnimation(rdout);

				i = (i - 1);
				int p = i % 4;
				if (p >= 0) {
					setpic(p);
					imswitcher.setImageResource(imageIds[p]);

				} else {

					int k = 4 + p;
					setpic(k);
					imswitcher.setImageResource(imageIds[k]);

				}

			}
			if (velocityX < 0) {

				float halfWidth = imswitcher.getWidth() / 2.0f;
				float halfHeight = imswitcher.getHeight() / 2.0f;
				int duration = 500;

				Rotate3D rdin = new Rotate3D(75, 0, 0, halfWidth, halfHeight);
				rdin.setDuration(duration);
				rdin.setFillAfter(true);
				imswitcher.setInAnimation(rdin);
				Rotate3D rdout = new Rotate3D(-15, -90, 0, halfWidth,
						halfHeight);

				rdout.setDuration(duration);
				rdout.setFillAfter(true);
				imswitcher.setOutAnimation(rdout);

				i = (i + 1);
				int p = i % 4;

				if (p >= 0) {
					setpic(p);
					imswitcher.setImageResource(imageIds[p]);

				} else {

					int k = 4 + p;
					setpic(k);
					imswitcher.setImageResource(imageIds[k]);

				}
			}
			return true;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO 自动生成的方法存根
			int p = i % 4;

			if (p >= 0) {
				startWebViewActivity(p);
			} else {
				int k = 4 + p;
				startWebViewActivity(k);
			}
			return true;
		}

	}

	@SuppressWarnings("rawtypes")
	class DownloadTask extends AsyncTask {

		@SuppressWarnings("unchecked")
		@Override
		protected Object doInBackground(Object... arg0) {
			// TODO Auto-generated method stub

			for (int i = 0; i < Integer.MAX_VALUE; i++) {
				try {
					Thread.sleep(6000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				publishProgress();

			}

			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onProgressUpdate(Object... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);

			float halfWidth = imswitcher.getWidth() / 2.0f;
			float halfHeight = imswitcher.getHeight() / 2.0f;
			int duration = 500;
			Rotate3D rdin = new Rotate3D(75, 0, 0, halfWidth, halfHeight);
			rdin.setDuration(duration);
			rdin.setFillAfter(true);
			imswitcher.setInAnimation(rdin);
			Rotate3D rdout = new Rotate3D(-15, -90, 0, halfWidth, halfHeight);

			rdout.setDuration(duration);
			rdout.setFillAfter(true);
			imswitcher.setOutAnimation(rdout);

			i = (i + 1);
			int p = i % 4;

			if (p >= 0) {
				setpic(p);
				imswitcher.setImageResource(imageIds[p]);
			} else {

				int k = 4 + p;
				setpic(k);
				imswitcher.setImageResource(imageIds[k]);
			}

		}

	}

	private void startWebViewActivity(int key) {
		// TODO 自动生成的方法存根
		Intent intent = new Intent(getActivity(), WebActivity.class);
		intent.putExtra("key", key);
		startActivity(intent);

	}

	private AnimationDrawable animationDrawable;
	Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what == 101)
				animationDrawable.stop();
			return false;
		}
	});
}
