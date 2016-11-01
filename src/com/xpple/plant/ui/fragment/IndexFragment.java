package com.xpple.plant.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.xpple.plant.R;
import com.xpple.plant.adapter.LazyAdapter;
import com.xpple.plant.bean.Good;
import com.xpple.plant.ui.ItemshowActivity;
import com.xpple.plant.util.CollectionUtils;
import com.xpple.plant.view.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

public class IndexFragment extends BaseFragment   {
	private View parentView;
	private PullToRefreshGridView mGvView;
	private ArrayList<Good> items = new ArrayList<>();
	private LazyAdapter adapter;
	private int curPage = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		parentView = inflater.inflate(R.layout.layout_index, container, false);
		// Inflate the layout for this fragment
		initXListView();
		return parentView;
	}

	private void initXListView() {
		mGvView = (PullToRefreshGridView)parentView.findViewById(R.id.id_grid);

		mGvView.setMode(PullToRefreshBase.Mode.BOTH);

		mGvView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent itemshow = new Intent(getActivity(),
						ItemshowActivity.class);
				itemshow.putExtra("items", items.get(position ));

				startActivity(itemshow);
			}
		});
		if (!isNetConnected()) {
			ShowToast(R.string.network_tips);
			return;
		} else {
			items.clear();
		}
		mGvView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
				initNearByList();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
				onLoadMore();
			}
		});

		mGvView.setRefreshing();
		adapter = new LazyAdapter(getActivity(), items);
		mGvView.setAdapter(adapter);

		initNearByList();
	}

	private void initNearByList() {

		BmobQuery<Good> query = new BmobQuery<Good>();
		query.include("user");
		query.addWhereEqualTo("key", true);
		query.order("-updatedAt"); // 鎸変慨鏀规椂闂存帓搴�
		query.setLimit(10);

		query.findObjects(getActivity(), new FindListener<Good>() {
			@Override
			public void onSuccess(List<Good> object) {
				// TODO Auto-generated method stub
				if (CollectionUtils.isNotNull(object)) {

					mGvView.setBackgroundResource(R.color.base_color_text_white);

					items.clear();
					curPage = 0;
					adapter.addAll(object);
					if (object.size() < 10) {
						mGvView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
					} else {
						mGvView.setMode(PullToRefreshBase.Mode.BOTH);
					}
				} else {
					ShowToast("鏆傛棤鏁版嵁!");
					mGvView.setBackgroundResource(R.drawable.bg_chat);
				}
				refreshPull();

			}

			@Override
			public void onError(int code, String msg) {
				// TODO Auto-generated method stub
				ShowToast("杩欓兘鑳藉嚭閿�:" + msg);
				mGvView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
				refreshPull();
			}
		});
	}

	/**
	 * 鏌ヨ鏇村
	 * 
	 * @Title: queryMoreNearList
	 * @Description: TODO
	 * @param @param page
	 * @return void
	 * @throws
	 */
	private void queryMoreNearList(int page) {
		BmobQuery<Good> query = new BmobQuery<Good>();
		query.include("user");
		query.order("-updatedAt"); // 鎸変慨鏀规椂闂存帓搴�
		query.addWhereEqualTo("key", true);
		query.setLimit(10);
		query.setSkip(10 * page); // 浠庣鍑犳潯鏁版嵁寮�濮嬶紝
		query.findObjects(getActivity(), new FindListener<Good>() {
			@Override
			public void onSuccess(List<Good> object) {
				// TODO Auto-generated method stub
				if (CollectionUtils.isNotNull(object)) {
					adapter.addAll(object);
				}
				refreshLoad();
			}

			@Override
			public void onError(int code, String msg) {
				// TODO Auto-generated method stub
				ShowLog("鍙堝嚭閿欙紝涔熸槸閱変簡" + msg);
				mGvView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
				refreshLoad();
			}
		});

	}

	private void refreshLoad() {
		if (mGvView.getShowViewWhileRefreshing()) {
			mGvView.onRefreshComplete();
		}
	}

	private void refreshPull() {
		if (mGvView.getShowViewWhileRefreshing()) {
			mGvView.onRefreshComplete();
		}
	}

	public void onLoadMore() {
		// TODO Auto-generated method stub
		BmobQuery<Good> query = new BmobQuery<Good>();
		query.addWhereEqualTo("key", true);
		query.count(getActivity(), Good.class, new CountListener() {
			@Override
			public void onSuccess(int count) {
				// TODO Auto-generated method stub
				if (count > items.size()) {
					curPage++;
					queryMoreNearList(curPage);
				} else {
					ShowToast("鏁版嵁鍔犺浇瀹屾垚");
					mGvView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
					refreshLoad();
				}
			}

			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				ShowLog("濡堣泲锛屽張澶辫触浜�" + msg);
				refreshLoad();
			}
		});

	}

}
