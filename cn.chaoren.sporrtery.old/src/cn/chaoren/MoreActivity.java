package cn.chaoren;

import cn.chaoren.service.ResultService;
import cn.chaoren.R;

import com.mobclick.android.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;

public class MoreActivity extends Activity implements OnClickListener {

	private RelativeLayout guanzhu_layout, message_layout, ideas_layout,
			about_layout, exit_layout, mailbox_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.more_genduo);

		guanzhu_layout = (RelativeLayout) findViewById(R.id.guanzhu_layout);
		message_layout = (RelativeLayout) findViewById(R.id.message_layout);
		ideas_layout = (RelativeLayout) findViewById(R.id.ideas_layout);
		about_layout = (RelativeLayout) findViewById(R.id.about_layout);
		exit_layout = (RelativeLayout) findViewById(R.id.exit_layout);
		mailbox_layout = (RelativeLayout) findViewById(R.id.mailbox_layout);

		guanzhu_layout.setOnClickListener(this);
		message_layout.setOnClickListener(this);
		ideas_layout.setOnClickListener(this);
		about_layout.setOnClickListener(this);
		exit_layout.setOnClickListener(this);
		mailbox_layout.setOnClickListener(this);
	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.guanzhu_layout:
			Intent intentt = new Intent(this, MyMakedMatchActivity.class);
			startActivity(intentt);
			break;

		case R.id.message_layout:
			Intent intentMessage = new Intent(this,
					InformationListActivity.class);
			startActivity(intentMessage);
			break;
		case R.id.ideas_layout:
			MobclickAgent.openFeedbackActivity(this);
			break;
		case R.id.about_layout:
			Intent intent = new Intent(MoreActivity.this, AboutActivity.class);
			startActivity(intent);
			break;
		case R.id.exit_layout:
			showExitDialog();
			break;
		case R.id.mailbox_layout:
			break;

		}
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

	@Override
	public void onBackPressed() {
		showExitDialog();
		// super.onBackPressed();
	}

}
