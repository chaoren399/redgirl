package cn.chaoren;

import java.util.Map;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.chaoren.net.HttpServices;
import cn.chaoren.net.JSONBeanResultFB;
import cn.chaoren.view.ProgressLinearLayout;
import cn.chaoren.R;

import com.mobclick.android.MobclickAgent;

/**
 * 类说明：
 * 
 * @创建时间 2011-7-20 下午11:18:32
 * @创建人： 陆林
 * @邮箱：15366189868@189.cn
 */
public class ZuqiuResultDetailActivity_temp extends Activity {
	private String m_id;
	private ProgressLinearLayout progress;
	private LinearLayout layout;

	String[] titles = new String[4];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.zuqiu_result_detail);
		m_id = getIntent().getStringExtra("m_id");
		((TextView) findViewById(R.id.h_cn_text)).setText(getIntent()
				.getStringExtra("h_cn"));
		((TextView) findViewById(R.id.a_cn_text)).setText(getIntent()
				.getStringExtra("a_cn"));
		((TextView) findViewById(R.id.l_cn_text)).setText(getIntent()
				.getStringExtra("l_cn"));
		((TextView) findViewById(R.id.num_text)).setText(getIntent()
				.getStringExtra("num"));
		((TextView) findViewById(R.id.sstime_text)).setText(getIntent()
				.getStringExtra("date"));
		titles[0] = getResources().getString(R.string.wf_spf);
		titles[1] = getResources().getString(R.string.wf_bf);
		titles[2] = getResources().getString(R.string.wf_zjq);
		titles[3] = getResources().getString(R.string.wf_bqc);
		progress = (ProgressLinearLayout) findViewById(R.id.zuqiu_result_detail_progress);
		layout = (LinearLayout) findViewById(R.id.layout);
		new ZuqiuResultDetailTask().execute();
	}

	class ZuqiuResultDetailTask extends
			AsyncTask<String, Integer, JSONBeanResultFB> {
		@Override
		protected JSONBeanResultFB doInBackground(String... params) {
			return HttpServices.getMatchResultDetailFB(m_id);
		}

		@Override
		protected void onPostExecute(JSONBeanResultFB result) {
			if (result == null) {
				progress.setVisibility(View.GONE, View.GONE, View.VISIBLE,
						View.VISIBLE);
			} else {
				progress.setVisibility(View.GONE);
				for (int i = 0; i < result.wanfa.size(); i++) {
					Map<String, String> _map = result.wanfa.get(i);
					View view = getLayoutInflater().inflate(
							R.layout.zuqiu_result_detail_item, null, false);
					TextView title = (TextView) view.findViewById(R.id.title);
					if (i == 0) {
						if (result.goaline.length() > 0) {
							title.setText(titles[i] + "(" + result.goaline
									+ ")");
						} else {
							title.setText(titles[i]);
						}
					} else {
						title.setText(titles[i]);
					}
					((TextView) view.findViewById(R.id.c)).setText(_map
							.get("c"));
					((TextView) view.findViewById(R.id.value)).setText(_map
							.get("value"));
					((TextView) view.findViewById(R.id.m)).setText(_map
							.get("m"));
					((TextView) view.findViewById(R.id.count)).setText(_map
							.get("count"));
					((TextView) view.findViewById(R.id.c2)).setText(_map
							.get("c2"));
					layout.addView(view);
				}
			}
			super.onPostExecute(result);
		}
	}

	
	
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public interface ItemClickListener
	{
		public void onItemClick(ViewGroup view);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
