package com.xpple.plant.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xpple.plant.R;
import com.xpple.plant.adapter.base.BaseListAdapter;
import com.xpple.plant.adapter.base.ViewHolder;
import com.xpple.plant.bean.Comment;
import com.xpple.plant.util.ImageLoadOptions;

public class CommentAdapter extends BaseListAdapter<Comment> {

	public CommentAdapter(Context context, List<Comment> list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("InflateParams")
	@Override
	public View bindView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_comment, null);
		}
		final Comment contract = getList().get(position);

		ImageView imgView = ViewHolder.get(convertView, R.id.commentavatar);
		String Avatar = contract.getUser().getAvatar();
		if (Avatar != null && !Avatar.equals("")) {
			ImageLoader.getInstance().displayImage(Avatar, imgView,
					ImageLoadOptions.getOptions());
		} else {
			imgView.setImageResource(R.drawable.default_head);
		}
		TextView userName = ViewHolder.get(convertView, R.id.userName_comment);
		TextView commentContent = ViewHolder.get(convertView,
				R.id.content_comment);
		TextView index = ViewHolder.get(convertView, R.id.index_comment);

		userName.setText(contract.getUser().getNick());
		commentContent.setText(contract.getCommentContent());
		index.setText((position + 1) + "楼");

		return convertView;
	}

}
