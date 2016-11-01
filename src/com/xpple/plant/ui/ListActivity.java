package com.xpple.plant.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.xpple.plant.R;
import com.xpple.plant.adapter.LazyAdapter;
import com.xpple.plant.bean.Good;
import com.xpple.plant.bean.User;
import com.xpple.plant.util.CollectionUtils;
import com.xpple.plant.view.ActivityBase;
import com.xpple.plant.view.CustomProgressDialog;
import com.xpple.plant.view.xlist.XListView;
import com.xpple.plant.view.xlist.XListView.IXListViewListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

public class ListActivity extends ActivityBase implements IXListViewListener {

    private XListView mListView;
    private ArrayList<Good> items = new ArrayList<>();
    private LazyAdapter adapter;
    private int key;
    private User my;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setUpViews();
    }

    private void setUpViews() {
        my = BmobUser.getCurrentUser(this, User.class);
        Intent intent = getIntent();
        key = intent.getIntExtra("key", 1);
        switch (key) {
            case 1:
                initTopBarForLeft("二手市场");
                break;
            case 2:
                initTopBarForLeft("求购中心");
                break;
            case 3:
                initTopBarForLeft("我的发布");
                break;
            case 4:
                initTopBarForLeft("我的需求");
                break;
            case 5:
                initTopBarForLeft("收藏发布");
                break;
            case 6:
                initTopBarForLeft("收藏需求");
                break;
            default:
                break;
        }

        initXListView();
    }

    private void initXListView() {
        mListView = (XListView) findViewById(R.id.list);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent itemshow = new Intent(ListActivity.this,
                        ItemshowActivity.class);
                itemshow.putExtra("items", items.get(position - 1));
                startActivity(itemshow);
            }
        });
        if (!isNetConnected()) {
            ShowToast(R.string.network_tips);
            return;
        }
        // 首先不允许加载更多
        mListView.setPullLoadEnable(false);
        // 允许下拉
        mListView.setPullRefreshEnable(true);
        // 设置监听器
        mListView.setXListViewListener(this);
        mListView.pullRefreshing();

        adapter = new LazyAdapter(this, items);
        mListView.setAdapter(adapter);
        initNearByList();
    }

    int curPage = 0;

    private void initNearByList() {
        final CustomProgressDialog dialog = new CustomProgressDialog(this,
                "正在加载中", R.anim.frame);
        dialog.show();
        BmobQuery<Good> query = new BmobQuery<>();
        query.include("user");
        switch (key) {
            case 1:
                query.addWhereEqualTo("key", true);
                break;
            case 2:
                query.addWhereEqualTo("key", false);
                break;
            case 3:
                initTopBarForLeft("我的发布");
                query.addWhereRelatedTo("good", new BmobPointer(my));
                query.addWhereEqualTo("key", true);
                break;
            case 4:
                query.addWhereRelatedTo("good", new BmobPointer(my));
                query.addWhereEqualTo("key", false);
                initTopBarForLeft("我的需求");
                break;
            case 5:
                initTopBarForLeft("发布收藏");
                query.addWhereRelatedTo("favorite", new BmobPointer(my));
                query.addWhereEqualTo("key", true);
                break;
            case 6:
                initTopBarForLeft("需求收藏");
                query.addWhereRelatedTo("favorite", new BmobPointer(my));
                query.addWhereEqualTo("key", false);
                break;
            default:
                break;
        }
        query.order("-updatedAt"); // 按修改时间排序
        query.setLimit(10);
        query.findObjects(this, new FindListener<Good>() {
            @Override
            public void onSuccess(List<Good> object) {
                // TODO Auto-generated method stub
                if (CollectionUtils.isNotNull(object)) {
                    items.clear();
                    adapter.addAll(object);
                    if (object.size() < 10) {
                        mListView.setPullLoadEnable(false);
                        ShowToast("查询完成!");
                    } else {
                        mListView.setPullLoadEnable(true);
                    }
                } else {
                    ShowToast("暂无数据!");
                }

                dialog.dismiss();
                refreshPull();
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                ShowToast("这都能出错:" + msg);
                mListView.setPullLoadEnable(false);
                dialog.dismiss();
                refreshPull();
            }
        });
    }

    /**
     * 查询更多
     */
    private void queryMoreNearList(int page) {
        BmobQuery<Good> query = new BmobQuery<>();
        query.include("user");
        if (key == 1) {
            query.addWhereEqualTo("key", true);
        } else {
            query.addWhereEqualTo("key", false);
        }
        query.order("-updatedAt"); // 按修改时间排序
        query.setLimit(10);
        query.setSkip(page * 10); // 从第几条数据开始，
        query.findObjects(this, new FindListener<Good>() {
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
                ShowLog("又出错，也是醉了" + msg);
                mListView.setPullLoadEnable(false);
                refreshLoad();
            }
        });

    }

    public void onRefresh() {
        // TODO Auto-generated method stub
        initNearByList();
    }

    private void refreshLoad() {
        if (mListView.getPullLoading()) {
            mListView.stopLoadMore();
        }
    }

    private void refreshPull() {
        if (mListView.getPullRefreshing()) {
            mListView.stopRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        // TODO Auto-generated method stub
        BmobQuery<Good> query = new BmobQuery<>();

        if (key == 1) {
            query.addWhereEqualTo("key", true);
        } else {
            query.addWhereEqualTo("key", false);
        }
        query.count(this, Good.class, new CountListener() {
            @Override
            public void onSuccess(int count) {
                // TODO Auto-generated method stub
                if (count > items.size()) {
                    curPage++;
                    queryMoreNearList(curPage);
                } else {
                    ShowToast("数据加载完成");
                    mListView.setPullLoadEnable(false);
                    refreshLoad();
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                ShowLog("妈蛋，又失败了" + msg);
                refreshLoad();
            }
        });

    }

}