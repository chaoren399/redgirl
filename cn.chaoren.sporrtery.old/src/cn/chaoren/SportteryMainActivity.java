package cn.chaoren;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.LocalActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import cn.chaoren.mvc.activity.MyActivity;
import cn.chaoren.service.ResultService;
import cn.chaoren.R;

import com.mobclick.android.MobclickAgent;
import com.mobclick.android.UmengFeedbackListener;


/**
 * 主界面，包括下面的底部导行栏
 * 
 * @author Administrator
 */

public class SportteryMainActivity extends ActivityGroup implements
		OnClickListener {
	private View title_shouzhu, title_result, title_wymaked, title_gengduo,
			title_score_onlive, menuView;
	private LinearLayout container;
	public LocalActivityManager manager;
	private boolean menuShowed = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 实时更新umeng统计发送策略
		MobclickAgent.updateOnlineConfig(this);

		Intent intent = new Intent(getApplicationContext(), ResultService.class);
		startService(intent); // 开启后台服务

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		MobclickAgent.setFeedbackListener(new UmengFeedbackListener() {
			public void onFeedbackReturned(int arg0) {
				// 0表示反馈发送成功，1表示反馈发送失败，2表示无网络连接，反馈失败
				switch (arg0) {
				case 0:// 反馈发送成功
					Toast.makeText(SportteryMainActivity.this,
							R.string.report_ok, Toast.LENGTH_SHORT).show();
					break;
				case 1:
					Toast.makeText(SportteryMainActivity.this,
							R.string.report_fail, Toast.LENGTH_SHORT).show();
					break;
				case 2:
					Toast.makeText(SportteryMainActivity.this,
							R.string.progress_hqsjsb, Toast.LENGTH_SHORT)
							.show();
					break;
				}
			}
		});
		manager = getLocalActivityManager();
		container = (LinearLayout) findViewById(R.id.container);
		title_shouzhu = findViewById(R.id.title_shouzhu);// 受注赛程
		title_shouzhu.setOnClickListener(this);
		title_result = findViewById(R.id.title_saiguokaijiang);// 赛果开奖
		title_result.setOnClickListener(this);
		title_wymaked = findViewById(R.id.title_wodeguanzhu);// 我的关注
		title_wymaked.setOnClickListener(this);
		title_gengduo = findViewById(R.id.str_gengduo);// 更多
		title_gengduo.setOnClickListener(this);
		title_score_onlive = findViewById(R.id.title_score_onlive);// 比分直播
		title_score_onlive.setOnClickListener(this);
//		menuView = findViewById(R.id.menuLayout);// 初使化整个菜单栏，可设置为显示和隐藏（更多）
		onClick(title_shouzhu);

		MobclickAgent.onError(this);
		
		
		
//		.detectDiskReads()       
//		        .detectDiskWrites()       
//		        .detectNetwork()   // or .detectAll() for all detectable problems       
//		        .penaltyLog()       
//		        .build());       
//		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()       
//		        .detectLeakedSqlLiteObjects()    
//		        .penaltyLog()       
//		        .penaltyDeath()       
//		        .build()); 

	}

	@Override
	public void onResume() {
		super.onResume();
		menuShowed = true;
//		showMenu();
		MobclickAgent.onResume(this);
	}
     
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {

		if (manager.getCurrentActivity() instanceof InfoDetailActivity) {
			switch (event.getAction()) {
			case KeyEvent.ACTION_DOWN:
				switch (event.getKeyCode()) {
				case KeyEvent.KEYCODE_BACK:
					manager.getCurrentActivity().onKeyDown(
							KeyEvent.KEYCODE_BACK, event);
					return true;
				}
			}
		}

		return super.dispatchKeyEvent(event);
	}

	@Override
	public void onBackPressed() {
		showExitDialog();
		// super.onBackPressed();
	}

	private void showExitDialog() {
		new AlertDialog.Builder(this)
				.setTitle(R.string.exit)
				.setMessage(R.string.isexit)
				.setPositiveButton(R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(
										getApplicationContext(),
										ResultService.class);
								stopService(intent); // 退出程序时关闭服务
								dialog.dismiss();
								finish();
							}
						})
				.setNegativeButton(R.string.no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).create().show();
	}

	public void showChild(String id, Class activityClass) {
		container.removeAllViews();
		container.addView(getLocalActivityManager().startActivity(
				id,
				new Intent(this, activityClass)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
				.getDecorView());
	}

	public void showChild(String id, Class activityClass, Bundle bundle) {
		container.removeAllViews();
		container.addView(getLocalActivityManager().startActivity(
				"result",
				new Intent(this, activityClass).putExtras(bundle).addFlags(
						Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView());
	}

	// 定义变量，设置菜单栏是否显示出来
//	private void showMenu() {
//		if (menuShowed) {
//			menuShowed = false;
//			menuView.setVisibility(View.GONE);
//		} else {
//			menuShowed = true;
//			menuView.setVisibility(View.VISIBLE);
//		}
//	}

	// 定义颜色选择器
	private void changeState(View view) {
		title_shouzhu.setSelected(false);
		title_result.setSelected(false);
		title_wymaked.setSelected(false);
		title_gengduo.setSelected(false);
		title_score_onlive.setSelected(false);
		view.setSelected(true);
	}

	// 设置点击事件
	@Override
	public void onClick(View v) {
		if (getCurrentActivity() instanceof ScoreOnLiveActivity) {
			((ScoreOnLiveActivity) getCurrentActivity()).destory();
		}
//		switch (v.getId()) {
//		case R.id.guanzhu:
//			Intent intentt = new Intent(this, MyMakedMatchActivity.class);
//			startActivity(intentt);
//			break;
//		case R.id.msgText:// 消息
//			container.removeAllViews();
//			container.addView(getLocalActivityManager().startActivity(
//					"result",
//					new Intent(this, InformationListActivity.class)
//							.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
//					.getDecorView());
//			showChild("infolist", InformationListActivity.class);
//			break;
//		case R.id.backText:// 反馈
//			MobclickAgent.openFeedbackActivity(this);
//			break;
//		case R.id.aboutText:// 关于
//			Intent intent = new Intent(SportteryMainActivity.this,
//					AboutActivity.class);
//			startActivity(intent);
//			break;
//		case R.id.quitText:// 退出
//			showExitDialog();
//			break;
//
//		default:
//			break;
//		}

		// 当点更多的时候，则显示菜单栏
//		if (v == title_gengduo) {
//			showMenu();
//		} else {
//			menuShowed = true;
//			showMenu();
//		}
		// 点击 受注赛程
		if (v == title_shouzhu) {
			changeState(v);
			container.removeAllViews();
			container.addView(getLocalActivityManager().startActivity(
					"zuqiu",
					new Intent(this, MatchShouzhuActivity.class)
							.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
					.getDecorView());
		} else if (v == title_result) {// 点击 赛果开奖
			changeState(v);
			container.removeAllViews();
			container.addView(getLocalActivityManager().startActivity(
					"result",
					new Intent(this, MatchResultActivity.class)
							.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
					.getDecorView());
		} else if (v == title_wymaked) {// 竞彩店
			changeState(v);
			container.removeAllViews();
			container.addView(getLocalActivityManager().startActivity(
					"maked",
					new Intent(this, MyActivity.class)
							.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
					.getDecorView());
		} else if (v == title_gengduo) { // 更多
			changeState(v);
			container.removeAllViews();
			container.addView(getLocalActivityManager().startActivity(
					"maked",
					new Intent(this, MoreActivity.class)
							.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
					.getDecorView());

		} else if (v == title_score_onlive) { // 竞彩足球
			changeState(v);
			container.removeAllViews();
			container.addView(getLocalActivityManager().startActivity(
					"score",
					new Intent(this, DisplayBettingFBActivity.class)
							.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
					.getDecorView());
		}
	}
}