package com.xpple.plant.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.bmob.pay.tool.BmobPay;
import com.bmob.pay.tool.PayListener;
import com.xpple.plant.R;
import com.xpple.plant.config.Config;
import com.xpple.plant.view.ActivityBase;

public class PayActivity extends ActivityBase implements
		OnCheckedChangeListener {

	BmobPay bmobPay;

	EditText price;
	Button go;
	RadioGroup type;
	TextView tv;
	String name = "支持作者";
	ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay);
		initTopBarForLeft("支持作者");
		// 必须先初始化
		BmobPay.init(this, Config.applicationId);

		// 初始化BmobPay对象,可以在赞助时再初始化
		bmobPay = new BmobPay(PayActivity.this);
		price = (EditText) findViewById(R.id.price);
		go = (Button) findViewById(R.id.go);
		type = (RadioGroup) findViewById(R.id.type);
		tv = (TextView) findViewById(R.id.tv);
		type.setOnCheckedChangeListener(this);
		go.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 当选择的是赞助宝赞助时
				if (type.getCheckedRadioButtonId() == R.id.alipay) {
					payByAli();
				} else {
					// 调用插件用微信赞助
					payByWeiXin();
				}
			}
		});
	}

	// 调用赞助宝赞助
	void payByAli() {
		showDialog("正在获取订单...");

		bmobPay.pay(getPrice(), name, "赞助宝赞助", new PayListener() {

			// 因为网络等原因,赞助结果未知(小概率事件),出于保险起见稍后手动查询
			@Override
			public void unknow() {
				ShowToast("赞助结果未知,请稍后手动查询");
				tv.append(name + "'s pay status is unknow\n\n");
				hideDialog();
			}

			// 赞助成功,如果金额较大请手动查询确认
			@Override
			public void succeed() {
				ShowToast("赞助成功,感谢您的支持!");
				tv.append(name + "'s pay status is success\n\n");
				hideDialog();
			}

			// 无论成功与否,返回订单号
			@Override
			public void orderId(String orderId) {
				// 此处应该保存订单号,比如保存进数据库等,以便以后查询
				tv.append(name + "'s orderid is " + orderId + "\n\n");
				showDialog("获取订单成功!请等待跳转到赞助页面~");
			}

			// 赞助失败,原因可能是用户中断赞助操作,也可能是网络原因
			@Override
			public void fail(int code, String reason) {
				ShowToast("赞助中断!");
				tv.append(name + "'s pay status is fail, error code is " + code
						+ " ,reason is " + reason + "\n\n");
				hideDialog();
			}
		});
	}

	// 调用微信赞助
	void payByWeiXin() {
		showDialog("正在获取订单...");

		bmobPay.payByWX(getPrice(), name, "微信赞助", new PayListener() {

			// 因为网络等原因,赞助结果未知(小概率事件),出于保险起见稍后手动查询
			@Override
			public void unknow() {
				ShowToast("赞助结果未知,请稍后手动查询");
				tv.append(name + "'s pay status is unknow\n\n");
				hideDialog();
			}

			// 赞助成功,如果金额较大请手动查询确认
			@Override
			public void succeed() {
				ShowToast("赞助成功,感谢您的支持!");
				tv.append(name + "'s pay status is success\n\n");
				hideDialog();
			}

			// 无论成功与否,返回订单号
			@Override
			public void orderId(String orderId) {
				// 此处应该保存订单号,比如保存进数据库等,以便以后查询
				tv.append(name + "'s orderid is " + orderId + "\n\n");
				showDialog("获取订单成功!请等待跳转到赞助页面~");
			}

			// 赞助失败,原因可能是用户中断赞助操作,也可能是网络原因
			@Override
			public void fail(int code, String reason) {

				// 当code为-2,意味着用户中断了操作
				// code为-3意味着没有安装BmobPlugin插件
				if (code == -3) {
					new AlertDialog.Builder(PayActivity.this)
							.setMessage(
									"监测到你尚未安装赞助插件,无法进行微信赞助,请选择安装插件(已打包在本地,无流量消耗)还是用赞助宝赞助")
							.setPositiveButton("安装",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											installBmobPayPlugin("BmobPayPlugin.apk");
										}
									})
							.setNegativeButton("赞助宝赞助",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											payByAli();
										}
									}).create().show();
				} else {
					ShowToast("赞助中断!");
				}
				tv.append(name + "'s pay status is fail, error code is " + code
						+ " ,reason is " + reason + "\n\n");
				hideDialog();
			}
		});
	}

	// 以下仅为控件操作，可以略过
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.alipay:
			// 以下仅为控件操作，可以略过
			go.setText("赞助宝赞助");
			break;
		case R.id.wxpay:
			// 以下仅为控件操作，可以略过
			go.setText("微信赞助");
			break;
		default:
			break;
		}
	}

	// 默认为0.02
	double getPrice() {
		double price = 0.02;
		try {
			price = Double.parseDouble(this.price.getText().toString());
		} catch (NumberFormatException e) {
		}
		return price;
	}

	void showDialog(String message) {
		if (dialog == null) {
			dialog = new ProgressDialog(this);
			dialog.setCancelable(false);
		}
		dialog.setMessage(message);
		dialog.show();
	}

	void hideDialog() {
		if (dialog != null && dialog.isShowing())
			dialog.dismiss();
	}

	void installBmobPayPlugin(String fileName) {
		try {
			InputStream is = getAssets().open(fileName);
			File file = new File(Environment.getExternalStorageDirectory()
					+ File.separator + fileName);
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			fos.close();
			is.close();

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.parse("file://" + file),
					"application/vnd.android.package-archive");
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
