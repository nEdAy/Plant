package com.xpple.plant.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xpple.plant.R;
import com.xpple.plant.adapter.base.BaseListAdapter;
import com.xpple.plant.adapter.base.ViewHolder;
import com.xpple.plant.bean.Good;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

public class LazyAdapter extends BaseListAdapter<Good> {

	public LazyAdapter(Context context, List<Good> items) {
		super(context, items);
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("InflateParams")
	@Override
	public View bindView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.night_item, null);
		}
		final Good contract = getList().get(position);
		if (contract.getKey()) {
			//ImageView imgView = ViewHolder.get(convertView, R.id.list_image);
			ImageView imgView = ViewHolder.get(convertView, R.id.id_gv_img);
			imgView.setVisibility(View.VISIBLE);
			BmobFile avatar = contract.getPica();
			if (avatar != null) {
				avatar.loadImageThumbnail(mContext, imgView, 120, 120, 100);
			}
		}

		TextView title = ViewHolder.get(convertView, R.id.id_gv_txt);

		title.setText(contract.getTitle());

		return convertView;
	}

}
