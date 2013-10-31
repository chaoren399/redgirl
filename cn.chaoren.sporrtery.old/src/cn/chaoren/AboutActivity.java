package cn.chaoren;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
//import android.widget.Toast;

import cn.chaoren.R;

import com.mobclick.android.MobclickAgent;

/**
 * splash界面，
 */
public class AboutActivity extends Activity implements OnClickListener {
	View web, wap;
	TextView version1, web_text, wap_text;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about);
		web = findViewById(R.id.web);
		web.setOnClickListener(this);
		wap = findViewById(R.id.wap);
		wap.setOnClickListener(this);
		version1 = (TextView) findViewById(R.id.version1);
		String versionName = "";
		try {
			// 获得到当前软件使用的
			PackageInfo info = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			versionName = info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		version1.setText(version1.getText().toString() + versionName);
		web_text = (TextView) findViewById(R.id.web_text);
		wap_text = (TextView) findViewById(R.id.wap_text);
		// umeng 在线参数测试
	}

	// mobclickAgent超统计分析的作用
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	// 在界面中各个条目设置的点击事件
	@Override
	public void onClick(View v) {
		if (v == web) {
			Uri uri = Uri.parse(web_text.getText().toString());
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			intent.setData(uri);
			intent.setClassName("com.android.browser",
					"com.android.browser.BrowserActivity");
			startActivity(intent);
		} else if (v == wap) {
			Uri uri = Uri.parse(wap_text.getText().toString());
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			intent.setData(uri);
			intent.setClassName("com.android.browser",
					"com.android.browser.BrowserActivity");
			startActivity(intent);
		}
	}
}
