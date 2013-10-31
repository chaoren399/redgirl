package cn.chaoren.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.chaoren.R;

/**
 * 类说明：
 * 
 * @创建时间 2011-6-1 下午03:51:16
 * @创建人： 陆林
 * @邮箱：15301586841@189.cn
 */




//进度条对话框 
public class ProgressLinearLayout extends LinearLayout {

	ProgressBar progressBar;
	TextView loadingText;
	TextView reloadText;
	Button reload;

	public ProgressLinearLayout(Context context) {
		super(context);
	}

	public ProgressLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void initView() {
		progressBar = (ProgressBar) findViewById(R.id.progress_small);
		loadingText = (TextView) findViewById(R.id.loadingtext);
		reloadText = (TextView) findViewById(R.id.reloadtext);
		reload = (Button) findViewById(R.id.reload);
	}

	public void setText(String _loadingText, String _reloadText) {
		if (_loadingText != null)
			loadingText.setText(_loadingText);
		if (_reloadText != null)
			reloadText.setText(_reloadText);
	}

	public void setVisibility(int _progressBarVisibility,
			int _loadingTextVisibility, int _reloadTextVisibility,
			int _reloadVisibility) {
		//progressBar.setVisibility(_progressBarVisibility);
		if (progressBar != null) {
			progressBar.setVisibility(_progressBarVisibility);
		}
		if (_progressBarVisibility == View.VISIBLE) {
			loadingText.setText(R.string.progress_wait);
		}
		loadingText.setVisibility(_loadingTextVisibility);
		reloadText.setVisibility(_reloadTextVisibility);
		reload.setVisibility(_reloadVisibility);
	}

	public void setOnClickListener(OnClickListener listener) {
		reload.setOnClickListener(listener);
	}
}
