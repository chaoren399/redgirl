package cn.chaoren;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import cn.chaoren.net.HttpServices;
import cn.chaoren.net.JSONBeanResultBK;
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
public class LanqiuResultDetailActivity extends Activity {
	private String m_id;
	private ProgressLinearLayout progress;
	private LinearLayout layout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lanqiu_result_detail);
		m_id = getIntent().getStringExtra("m_id");
		((TextView) findViewById(R.id.h_cn_text)).setText(getIntent()
				.getStringExtra("a_cn"));
		((TextView) findViewById(R.id.a_cn_text)).setText(getIntent()
				.getStringExtra("h_cn"));
		((TextView) findViewById(R.id.l_cn_text)).setText(getIntent()
				.getStringExtra("l_cn"));
		((TextView) findViewById(R.id.num_text)).setText(getIntent()
				.getStringExtra("num"));
		((TextView) findViewById(R.id.sstime_text)).setText(getIntent()
				.getStringExtra("date"));
		
		findViewById(R.id.team1).setBackgroundResource(R.drawable.team_04);
		findViewById(R.id.team2).setBackgroundResource(R.drawable.team_03);
		
		progress = (ProgressLinearLayout) findViewById(R.id.lanqiu_result_detail_progress);
		progress.initView();
		progress.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new LanqiuResultDetailTask().execute();
				
			}
		});
		layout = (LinearLayout) findViewById(R.id.layout);
		new LanqiuResultDetailTask().execute();
	}

	class LanqiuResultDetailTask extends
			AsyncTask<String, Integer, JSONBeanResultBK> {
		@Override
		protected JSONBeanResultBK doInBackground(String... params) {
			return HttpServices.getMatchResultDetailBK(m_id);
		}

		@Override                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
		protected void onPostExecute(JSONBeanResultBK result) {
			if (result == null&&progress!=null) {
				progress.setVisibility(View.GONE, View.GONE, View.VISIBLE,
						View.VISIBLE);
			} else {
				progress.setVisibility(View.GONE);

				View mnlview = getLayoutInflater()
						.inflate(R.layout.lanqiu_result_detail_item_mnl_wnm,
								null, false);
				((TextView) mnlview.findViewById(R.id.title))
						.setText(R.string.wf_sf);
				((TextView) mnlview.findViewById(R.id.c)).setText(result.mnl_c);
				((TextView) mnlview.findViewById(R.id.value))
						.setText(result.mnl_value);
				((TextView) mnlview.findViewById(R.id.m)).setText(result.mnl_m);
				((TextView) mnlview.findViewById(R.id.count))
						.setText(result.mnl_count);
				((TextView) mnlview.findViewById(R.id.c2))
						.setText(result.mnl_c2);
				layout.addView(mnlview);

				View hdcview = getLayoutInflater().inflate(
						R.layout.lanqiu_result_detail_item_hdc_hilo, null,
						false);
				((TextView) hdcview.findViewById(R.id.title))
						.setText(getResources().getString(R.string.wf_rfsf)
								+ "(" + result.hdc_goalline + ")");
				((TextView) hdcview.findViewById(R.id.c)).setText(result.hdc_c);
				((TextView) hdcview.findViewById(R.id.value))
						.setText(result.hdc_value);
				((TextView) hdcview.findViewById(R.id.m)).setText(result.hdc_m);
				((TextView) hdcview.findViewById(R.id.count))
						.setText(result.hdc_count);
				// TableRow rowtitle = (TableRow) hdcview
				// .findViewById(R.id.row_title);
				TableRow rowvalue = (TableRow) hdcview
						.findViewById(R.id.row_value);
				for (String string : result.hdc_c2_list) {
					// TextView titleview = (TextView) getLayoutInflater()
					// .inflate(
					// R.layout.lanqiu_result_detail_item_hdc_hilo_title,
					// rowtitle, false);
					// rowtitle.addView(titleview);
					TextView valueview = (TextView) getLayoutInflater()
							.inflate(
									R.layout.lanqiu_result_detail_item_hdc_hilo_value,
									rowvalue, false);
					valueview.setText(string);
					rowvalue.addView(valueview);
				}
				layout.addView(hdcview);

				View wnmview = getLayoutInflater()
						.inflate(R.layout.lanqiu_result_detail_item_mnl_wnm,
								null, false);
				((TextView) wnmview.findViewById(R.id.title))
						.setText(R.string.wf_sfc);
				((TextView) wnmview.findViewById(R.id.c)).setText(result.wnm_c);
				((TextView) wnmview.findViewById(R.id.value))
						.setText(result.wnm_value);
				((TextView) wnmview.findViewById(R.id.m)).setText(result.wnm_m);
				((TextView) wnmview.findViewById(R.id.count))
						.setText(result.wnm_count);
				((TextView) wnmview.findViewById(R.id.c2))
						.setText(result.wnm_c2);
				layout.addView(wnmview);

				View hiloview = getLayoutInflater().inflate(
						R.layout.lanqiu_result_detail_item_hdc_hilo, null,
						false);
				((TextView) hiloview.findViewById(R.id.title))
						.setText(getResources().getString(R.string.wf_dxf)
								+ "(" + getResources().getString(R.string.yszf)
								+ result.hilo_goalline + ")");
				((TextView) hiloview.findViewById(R.id.c))
						.setText(result.hilo_c);
				((TextView) hiloview.findViewById(R.id.value))
						.setText(result.hilo_value);
				((TextView) hiloview.findViewById(R.id.m))
						.setText(result.hilo_m);
				((TextView) hiloview.findViewById(R.id.count))
						.setText(result.hilo_count);
				// TableRow row1title = (TableRow) hiloview
				// .findViewById(R.id.row_title);
				TableRow row1value = (TableRow) hiloview
						.findViewById(R.id.row_value);
				for (String string : result.hilo_c2_list) {
					// TextView titleview = (TextView) getLayoutInflater()
					// .inflate(
					// R.layout.lanqiu_result_detail_item_hdc_hilo_title,
					// row1title, false);
					// row1title.addView(titleview);
					TextView valueview = (TextView) getLayoutInflater()
							.inflate(
									R.layout.lanqiu_result_detail_item_hdc_hilo_value,
									row1value, false);
					valueview.setText(string);
					row1value.addView(valueview);
				}
				layout.addView(hiloview);
			}
			super.onPostExecute(result);
		}
	}

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
}
