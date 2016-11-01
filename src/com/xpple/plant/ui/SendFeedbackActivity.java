package com.xpple.plant.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.xpple.plant.R;
import com.xpple.plant.bean.Feedback;
import com.xpple.plant.bean.User;
import com.xpple.plant.view.ActivityBase;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class SendFeedbackActivity extends ActivityBase implements
		OnClickListener {
	private User my;
	private Feedback feedback;
	private EditText et_content;
	private static String msg;
	private Button btn_feedback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sendfeedback);
		btn_feedback = (Button) findViewById(R.id.btn_feedback);
		btn_feedback.setOnClickListener(this);
		initTopBarForLeft("帮助与反馈");
		et_content = (EditText) findViewById(R.id.et_content);
	}

	public void sendFeedback() {
		String content = et_content.getText().toString();
		if (!TextUtils.isEmpty(content)) {
			if (content.equals(msg)) {
				ShowToast("请勿重复提交反馈");
			} else {
				msg = content;
				saveFeedbackMsg(content);
			}
		} else {
			ShowToast("请填写反馈内容");
		}
	}

	/**
	 * 保存反馈信息到服务器
	 * 
	 * @param msg
	 *            反馈信息
	 */
	private void saveFeedbackMsg(String msg) {
		my = BmobUser.getCurrentUser(this, User.class);
		feedback = new Feedback();
		feedback.setContent(msg);
		feedback.setUser(my);
		feedback.save(this, new SaveListener() {

			@Override
			public void onSuccess() {
				addFeedbackToUser();
			}

			@Override
			public void onFailure(int code, String arg0) {
				ShowToast("发送反馈信息失败：" + arg0);
			}
		});
	}

	private void addFeedbackToUser() {
		BmobRelation feedbacks = new BmobRelation();
		feedbacks.add(feedback);
		my.setFeedback(feedbacks);
		my.update(this, new UpdateListener() {

			@Override
			public void onSuccess() {
				ShowToast("您的反馈信息已发送");
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				ShowToast("发送反馈信息失败：" + arg0);
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		sendFeedback();
	}

}
