package com.xpple.plant.ui;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xpple.plant.R;
import com.xpple.plant.adapter.CommentAdapter;
import com.xpple.plant.bean.Comment;
import com.xpple.plant.bean.Good;
import com.xpple.plant.bean.User;
import com.xpple.plant.view.ActivityBase;
import com.xpple.plant.view.dialog.NiftyDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class ItemshowActivity extends ActivityBase implements OnTouchListener,
		OnClickListener {

	private User user;
	private User my;
	private Good good;

	private ImageSwitcher mSwitcher;
	private TextView details, transaction, price;
	private TextView nick, weixin, phone, qq;
	private BmobFile gim[] = new BmobFile[2];
	private Boolean tiFlag;
	private String weixinString, phoneString, qqString;
	private ClipboardManager mClipboardManager;
	private ClipData mClipData;
	private ListView commentList;
	private String commentEdit = "";
	private TextView footer;
	private EditText commentContent;
	private Button commentCommit;
	boolean isFav = false;
	private ImageView myFav;
	private TextView comment;
	private TextView delete;
	private TextView share;
	private TextView love;
	private int pageNum;
	private CommentAdapter mAdapter;
	private List<Comment> comments = new ArrayList<Comment>();
	/**
	 * 点点数组
	 */
	private View[] tips;

	// 左右滑动时手指按下的X坐标
	private float touchDownX;
	// 左右滑动时手指松开的X坐标
	private float touchUpX;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itemshow);
		initviews();
		setevents();
	}

	private void initviews() {
		// TODO Auto-generated method stub
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		my = BmobUser.getCurrentUser(this, User.class);
		Intent intent = getIntent();
		good = (Good) intent.getSerializableExtra("items");

		commentList = (ListView) findViewById(R.id.comment_list);
		footer = (TextView) findViewById(R.id.loadmore);
		comment = (TextView) findViewById(R.id.item_action_comment);
		delete = (TextView) findViewById(R.id.item_action_delete);
		myFav = (ImageView) findViewById(R.id.item_action_fav);
		share = (TextView) findViewById(R.id.item_action_share);
		love = (TextView) findViewById(R.id.item_action_love);
		pageNum = 0;
		mAdapter = new CommentAdapter(ItemshowActivity.this, comments);
		commentList.setAdapter(mAdapter);
		setListViewHeightBasedOnChildren(commentList);
		commentList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
			}
		});
		commentList.setCacheColorHint(0);
		commentList.setScrollingCacheEnabled(false);
		commentList.setScrollContainer(false);
		commentList.setFastScrollEnabled(true);
		commentList.setSmoothScrollbarEnabled(true);

		commentContent = (EditText) findViewById(R.id.comment_content);
		commentCommit = (Button) findViewById(R.id.comment_commit);
		commentContent.setOnClickListener(this);
		commentCommit.setOnClickListener(this);
		footer.setOnClickListener(this);
		myFav.setOnClickListener(this);
		comment.setOnClickListener(this);
		share.setOnClickListener(this);
		initMoodView();

	}

	private void initMoodView() {
		// TODO Auto-generated method stub
		details = (TextView) findViewById(R.id.details);
		transaction = (TextView) findViewById(R.id.transaction);
		price = (TextView) findViewById(R.id.price);

		nick = (TextView) findViewById(R.id.nick);
		phone = (TextView) findViewById(R.id.phone);
		qq = (TextView) findViewById(R.id.qq);
		weixin = (TextView) findViewById(R.id.weixin);

		weixin = (TextView) findViewById(R.id.weixin);

		mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);

		mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

		initTopBarForLeft(good.getTitle());

		tips = new View[2];
		tips[0] = findViewById(R.id.v1);
		tips[1] = findViewById(R.id.v2);
		tiFlag = false;

		details.setText("详情："+good.getDetails());
		transaction.setText("交易地："+good.getTransaction());
		price.setText("价格："+good.getPrice());

		love.setText(good.getLove() + "");

		gim[0] = good.getPica();
		gim[1] = good.getPicb();

		ImageView im = new ImageView(ItemshowActivity.this);
		if (gim[0] != null) {
			gim[0].loadImage(ItemshowActivity.this, im);
			mSwitcher.addView(im);
		}
		im = new ImageView(ItemshowActivity.this);
		if (gim[1] != null) {
			gim[1].loadImage(ItemshowActivity.this, im);
			mSwitcher.addView(im);
		}
		user = good.getUser();
		weixinString = user.getWeixin();
		qqString = user.getQq();
		phoneString = user.getPhone();
		String Avatar = user.getAvatar();
		if (user.getObjectId().equals(my.getObjectId())) {
			delete.setVisibility(View.VISIBLE);
			delete.setOnClickListener(this);
		}

		nick.setText("聊天号："+user.getNick()+"（←点击交流）");
		phone.setText("手机："+phoneString);
		qq.setText("QQ："+qqString);
		weixin.setText("微信："+weixinString);

		nick.setOnClickListener(ItemshowActivity.this);
		qq.setOnClickListener(ItemshowActivity.this);
		weixin.setOnClickListener(ItemshowActivity.this);
		phone.setOnClickListener(ItemshowActivity.this);
		BmobQuery<Good> favgood = new BmobQuery<Good>();
		/**
		 * 注意这里的查询条件 第一个参数：是User表中的cards字段名 第二个参数：是指向User表中的某个用户的BmobPoer对象
		 */

		favgood.addWhereRelatedTo("favorite", new BmobPointer(my));
		favgood.findObjects(this, new FindListener<Good>() {

			@Override
			public void onSuccess(List<Good> arg0) {
				// TODO Auto-generated method stub
				for (Good favgood : arg0) {
					if (favgood.getObjectId().equals(good.getObjectId())) {
						myFav.setImageResource(R.drawable.ic_action_fav_choose);
						isFav = true;
						break;
					} else {
						myFav.setImageResource(R.drawable.ic_action_fav_normal);
						isFav = false;
					}

				}
			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
			}
		});

		good.increment("love"); // 分数递增1
		good.update(this, new UpdateListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
			}
		});

		fetchComment();
		seletcor();
	}

	private void setevents() {
		// TODO Auto-generated method stub
		// 设置ImageSwitcher左右滑动事件
		mSwitcher.setOnTouchListener(this);
	}

	private void seletcor() {
		int a = 0, b = 1;
		if (tiFlag) {
			a = 1;
			b = 0;
		}
		tips[a].setBackgroundColor(Color.RED);
		tips[b].setBackgroundColor(Color.GRAY);
		tiFlag = !tiFlag;
	}

	private void fetchComment() {
		BmobQuery<Comment> query = new BmobQuery<Comment>();
		query.addWhereRelatedTo("comment", new BmobPointer(good));
		query.include("user");
		query.order("createdAt");
		query.setLimit(10);
		query.setSkip(10 * (pageNum++));
		query.findObjects(this, new FindListener<Comment>() {

			@Override
			public void onSuccess(List<Comment> data) {
				// TODO Auto-generated method stub
				if (data.size() != 0 && data.get(data.size() - 1) != null) {

					if (data.size() < 10) {
						ShowToast("已加载完所有评论~");
						footer.setText("暂无更多评论~");
					}

					mAdapter.getList().addAll(data);
					mAdapter.notifyDataSetChanged();
					setListViewHeightBasedOnChildren(commentList);
				} else {
					ShowToast("暂无更多评论~");
					footer.setText("暂无更多评论~");
					pageNum--;
				}
			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ShowToast("获取评论失败。请检查网络~");
				pageNum--;
			}
		});
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// 取得左右滑动时手指按下的X坐标
			touchDownX = event.getX();
			return true;
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			// 取得左右滑动时手指松开的X坐标
			touchUpX = event.getX();
			// 从左往右，看前一张
			if (touchUpX - touchDownX > 100) {
				mSwitcher.getVisibility();

				// 设置图片切换的动画
				mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
						android.R.anim.slide_in_left));
				mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
						android.R.anim.slide_out_right));
				// 设置当前要看的图片

				mSwitcher.showPrevious();

				seletcor();
				// 从右往左，看下一张
			} else if (touchDownX - touchUpX > 100) {

				// 取得当前要看的图片的index

				// 设置图片切换的动画
				// 由于Android没有提供slide_out_left和slide_in_right，所以仿照slide_in_left和slide_out_right编写了slide_out_left和slide_in_right
				mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
						R.anim.slide_in_right));
				mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
						R.anim.slide_out_left));
				// 设置当前要看的图片
				mSwitcher.showNext();

				seletcor();
			}
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.nick:
			Intent intent = new Intent(this, ChatActivity.class);
			intent.putExtra("user", user);
			startAnimActivity(intent);
			break;
		case R.id.qq:
			mClipData = ClipData.newPlainText("Label", qqString);
			mClipboardManager.setPrimaryClip(mClipData);
			ShowToast("已将对方QQ号码复制到剪切板");
			break;
		case R.id.weixin:
			mClipData = ClipData.newPlainText("Label", weixinString);
			mClipboardManager.setPrimaryClip(mClipData);
			ShowToast("已将对方微信号码复制到剪切板");
			break;
		case R.id.phone:
			// 用intent启动拨打电话
			Intent intentPhone = new Intent(Intent.ACTION_CALL,
					Uri.parse("tel:" + phoneString));
			startActivity(intentPhone);
			break;
		case R.id.comment_commit:
			onClickCommit();
			break;
		case R.id.loadmore:
			onClickLoadMore();
			break;
		case R.id.item_action_delete:
			onClickDelete();
			break;
		case R.id.item_action_fav:
			onClickFav(v);
			break;
		case R.id.item_action_share:
			onClickShare();
			break;
		case R.id.item_action_comment:
			onClickComment();
			break;
		default:
			break;
		}
	}

	private void onClickDelete() {
		final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder
				.getInstance(this);
		dialogBuilder.withTitle("提示").withMessage("确定删除该条信息？")
				.isCancelableOnTouchOutside(true).isCancelable(true)
				.withDuration(700).withButton1Text("取消").withButton2Text("确定")
				.setCustomView(0, this)
				.setButton1Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialogBuilder.getDismiss();
					}
				}).setButton2Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Good delgood = new Good();
						delgood.setObjectId(good.getObjectId());
						delgood.delete(getBaseContext(), new DeleteListener() {

							@Override
							public void onSuccess() {
								// TODO Auto-generated method stub
								ShowToast("删除成功");
								finish();
							}

							@Override
							public void onFailure(int code, String msg) {
								// TODO Auto-generated method stub
								ShowToast("删除失败：" + msg);
							}
						});

						dialogBuilder.getDismiss();
					}
				}).show();
	}

	private void onClickShare() {
		Intent toShare = new Intent(Intent.ACTION_SEND);
		toShare.setType("text/plain");
		toShare.putExtra(Intent.EXTRA_SUBJECT, "分享");
		toShare.putExtra(Intent.EXTRA_TEXT, "校园二手上架了《" + good.getTitle()
				+ "》\n" + "赶紧进来看看吧" + "http://hxxxx.bmob.cn");
		startActivity(Intent.createChooser(toShare, "分享到"));
	}

	private void onClickFav(final View v) {
		// TODO Auto-generated method stub

		BmobRelation favorites = new BmobRelation();
		isFav = !isFav;
		if (isFav) {
			((ImageView) v).setImageResource(R.drawable.ic_action_fav_choose);
			favorites.add(good);
			my.setFavorite(favorites);
			my.update(this, new UpdateListener() {

				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					ShowToast("收藏成功。");
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					((ImageView) v)
							.setImageResource(R.drawable.ic_action_fav_normal);
					ShowToast("收藏失败。请检查网络~");
				}
			});
		} else {
			((ImageView) v).setImageResource(R.drawable.ic_action_fav_normal);
			favorites.remove(good);
			my.setFavorite(favorites);
			my.update(this, new UpdateListener() {

				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					ShowToast("取消收藏。");
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					((ImageView) v)
							.setImageResource(R.drawable.ic_action_fav_choose);
					ShowToast("收藏失败。请检查网络~");
				}
			});
		}

	}

	private void onClickLoadMore() {
		// TODO Auto-generated method stub
		fetchComment();
	}

	private void onClickCommit() {
		// TODO Auto-generated method stub
		User currentUser = BmobUser.getCurrentUser(this, User.class);
		commentEdit = commentContent.getText().toString().trim();
		if (TextUtils.isEmpty(commentEdit)) {
			ShowToast("评论内容不能为空。");
			return;
		}
		publishComment(currentUser, commentEdit);

	}

	private void publishComment(User user, String content) {

		final Comment comment = new Comment();
		comment.setUser(user);
		comment.setGood(good);
		comment.setCommentContent(content);
		comment.save(this, new SaveListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				ShowToast("评论成功。");
				if (mAdapter.getList().size() < 10) {
					mAdapter.getList().add(comment);
					mAdapter.notifyDataSetChanged();
					setListViewHeightBasedOnChildren(commentList);
				}
				commentContent.setText("");
				hideSoftInput();

				// 将该评论与货物绑定到一起
				BmobRelation comments = new BmobRelation();
				comments.add(comment);
				good.setComment(comments);
				good.update(getBaseContext(), new UpdateListener() {

					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						ShowToast("更新评论成功。");
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						ShowToast("更新评论失败。" + arg1);
					}
				});

			}

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ShowToast("评论失败。请检查网络~");
			}
		});
	}

	private void onClickComment() {
		// TODO Auto-generated method stub
		commentContent.requestFocus();

		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.showSoftInput(commentContent, 0);
	}

	private void hideSoftInput() {
		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.hideSoftInputFromWindow(commentContent.getWindowToken(), 0);
	}

	/***
	 * 动态设置listview的高度 item 总布局必须是linearLayout
	 * 
	 * @param listView
	 */
	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1))
				+ 10;
		listView.setLayoutParams(params);
	}
}
