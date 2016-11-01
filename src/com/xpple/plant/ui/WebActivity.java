package com.xpple.plant.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xpple.plant.R;
import com.xpple.plant.view.ActivityBase;

/**
 * Web主界面
 */
public class WebActivity extends ActivityBase {

	private WebView mWebView;
	private String URL_WEB;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		Intent intent = getIntent();
		int key = intent.getIntExtra("key", 4);
		switch (key) {
		case 0:
			URL_WEB = "http://www.qq.com";
			break;
		case 1:
			URL_WEB = "http://www.baidu.com";
			break;
		case 2:
			URL_WEB = "http://www.sina.com";
			break;
		case 3:
			URL_WEB = "http://www.apicloud.com";
			break;
		case 4:
			URL_WEB = "http://m.wsq.qq.com/263536241";
			break;
		default:
			break;
		}
		mWebView = (WebView) findViewById(R.id.web);

		mWebView.getSettings().setJavaScriptEnabled(true); // 设置使用够执行JS脚本
		// mWebView.getSettings().setBuiltInZoomControls(true); // 设置使支持缩放
		mWebView.getSettings().setDefaultFontSize(12);
		mWebView.setWebChromeClient(new WebChromeClient());
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);// 使用当前WebView处理跳转
				return true;// true表示此事件在此处被处理，不需要再广播
			}

			@Override
			// 转向错误时的处理
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				ShowToast("Oh no! " + description);
			}
		});
		mWebView.loadUrl(URL_WEB);

	}

}
