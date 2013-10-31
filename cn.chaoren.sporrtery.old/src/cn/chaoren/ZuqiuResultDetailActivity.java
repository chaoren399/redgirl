package cn.chaoren;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.chaoren.net.HttpServices;
import cn.chaoren.net.JSONBeanResultFB;
import cn.chaoren.net.JSONBean.Data;
import cn.chaoren.net.JSONBean.KeyData;
import cn.chaoren.view.ProgressLinearLayout;
import cn.chaoren.widget.ItemClickListener;
import cn.chaoren.R;

import com.mobclick.android.MobclickAgent;

/**
 * 足球查询结果细节的条目界面
 * 
 * @创建时间 2011-7-20 下午11:18:32
 * @创建人： 陆林
 * @邮箱：15366189868@189.cn
 */

    


public class ZuqiuResultDetailActivity extends Activity {
	private String m_id;
	private ProgressLinearLayout progress;
	private LinearLayout layout;
    private TextView goallineText;
	String[] titles = new String[4];
    private View conView;
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
		/*goallineText = (TextView) findViewById(R.id.goallineText);
		if (goalline == null || goalline.equals("")) {
			goallineText.setVisibility(View.GONE);
		}else
		{
			goallineText.setVisibility(View.VISIBLE);
			goallineText.setText(goalline);
		}*/
	
		conView = findViewById(R.id.content);
		conView.setVisibility(View.GONE);
		titles[0] = getResources().getString(R.string.wf_spf);
		titles[1] = getResources().getString(R.string.wf_bf);
		titles[2] = getResources().getString(R.string.wf_zjq);
		titles[3] = getResources().getString(R.string.wf_bqc);
		progress = (ProgressLinearLayout) findViewById(R.id.zuqiu_result_detail_progress);
		progress.initView();
		progress.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				conView.setVisibility(View.GONE);
				new ZuqiuResultDetailTask().execute();
				
			}
		});
		layout = (LinearLayout) findViewById(R.id.layout);
		new ZuqiuResultDetailTask().execute();
	}

	class ZuqiuResultDetailTask extends
			AsyncTask<String, Integer, JSONBeanResultFB> {
		@Override
		protected JSONBeanResultFB doInBackground(String... params) {
			return HttpServices.getMatchResultDetailFB(m_id);
		}

		private void setCom(TextView text,String str)
		{
			Float com = 0f;
			try {
				com = new Float(str);
			} catch (Exception e) {
				com = 0f;
			}
			
			if (com==0) {
				text.setVisibility(View.GONE);
			}else if (com >0) {
				text.setVisibility(View.VISIBLE);
				text.setText(R.string.up);
				text.setTextColor(Color.parseColor("#FF7200"));
			}else if (com<0) {
				text.setText(R.string.down);
				text.setVisibility(View.VISIBLE);
				text.setTextColor(Color.parseColor("#0D8B00"));
			}
		}
		
		@Override
		protected void onPostExecute(JSONBeanResultFB result) {
			if (result == null) {
				progress.setVisibility(View.GONE, View.GONE, View.VISIBLE,
						View.VISIBLE);
			} else {
				conView.setVisibility(View.VISIBLE);
				View temp = findViewById(R.id.resultSingleLayout);
				LinearLayout layout = (LinearLayout) temp.findViewById(R.id.mlayout);
				View titleLayout = temp.findViewById(R.id.titleLayout);
				titleLayout.setOnClickListener(new ItemClick((ViewGroup)temp, mItem));
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
				
				
				
				temp = findViewById(R.id.hdaPassLayout);
				layout = (LinearLayout) temp.findViewById(R.id.mlayout);
				titleLayout = temp.findViewById(R.id.titleLayout);
				titleLayout.setOnClickListener(new ItemClick((ViewGroup)temp, mItem));
				ArrayList<Map<String, String>> mapList = result.hdaPass;
				View view = null;
				int size = mapList.size();
				TextView tempText;
				Map<String, String> map = null;
				for (int i = 0; i < size; i++) {
					view  = getLayoutInflater().inflate(R.layout.result_hda_pass_item, null);
					map = mapList.get(i);
					tempText = (TextView) view.findViewById(R.id.date);
					tempText.setText("" + map.get("date"));
					
					tempText = (TextView) view.findViewById(R.id.h);
					tempText.setText("" + map.get("h"));
					
					
					tempText = (TextView) view.findViewById(R.id.d);
					tempText.setText("" + map.get("d"));
					
					tempText = (TextView) view.findViewById(R.id.a);
					tempText.setText("" + map.get("a"));
					
					tempText = (TextView) view.findViewById(R.id.h1);
					setCom(tempText, map.get("h1"));
					
					tempText = (TextView) view.findViewById(R.id.d1);
					setCom(tempText, map.get("d1"));
					
					
					tempText = (TextView) view.findViewById(R.id.a1);
					
					setCom(tempText, map.get("a1"));
					
					layout.addView(view);
				}
				
				temp = findViewById(R.id.totalPassLayout);//总进球过关固定
				mapList = result.totalPass;
				layout = (LinearLayout) temp.findViewById(R.id.mlayout);
				titleLayout = temp.findViewById(R.id.titleLayout);
				titleLayout.setOnClickListener(new ItemClick((ViewGroup)temp, mItem));
				 size = mapList.size();
				 TextView date = null;
				 View mView = null;
				 for (int j = 0; j < size; j++) {
					map = mapList.get(j);
					mView = getLayoutInflater().inflate(R.layout.result_total, null);
					date =(TextView) mView.findViewById(R.id.date);
					date.setText(getString(R.string.show_time)+"" + map.get("date"));
				
					
					view = mView.findViewById(R.id.value_line0);
					init(view);
					for (int i = 0; i < 4; i++) {
						texts[i].setText("" + map.get("" + texts[i].getTag()));
						setCom(values_[i], map.get(values_[i].getTag()));
					}
					
					view = mView.findViewById(R.id.value_line1);
					init(view);
					for (int i = 0; i < 4; i++) {
						texts[i].setText("" + map.get("" + texts[i].getTag()));
						setCom(values_[i], map.get(values_[i].getTag()));
					}
					layout.addView(mView);
				}
				
				
				//半全场
				temp = findViewById(R.id.hAllPassLayout);
				layout = (LinearLayout) temp.findViewById(R.id.mlayout);
				titleLayout = temp.findViewById(R.id.titleLayout);
				titleLayout.setOnClickListener(new ItemClick((ViewGroup)temp, mItem));
				mapList = result.hAPass;
				size = mapList.size();
				View line=null;
				for (int i = 0; i < size; i++) {
					view  = getLayoutInflater().inflate(R.layout.result_hall_pass_item, null);
					map = mapList.get(i);
					tempText = (TextView) view.findViewById(R.id.date);
					tempText.setText("" + map.get("date"));
					
					line = view.findViewById(R.id.value_line0);
					init(line);
					System.out.println("start....");
					for (int j = 0; j < 3; j++) {
						texts[j].setText("" + map.get(texts[j].getTag()));
						//values_[j].setText("" + map.get(values_[j].getTag()));
						
						setCom(values_[j], map.get(values_[j].getTag()));
					}
					
					line = view.findViewById(R.id.value_line1);
					init(line);
					for (int j = 0; j < 3; j++) {
						texts[j].setText("" + map.get(texts[j].getTag()));
						setCom(values_[j], map.get(values_[j].getTag()));
					}
					
					line = view.findViewById(R.id.value_line2);
					init(line);
					for (int j = 0; j < 3; j++) {
						texts[j].setText("" + map.get(texts[j].getTag()));
						setCom(values_[j], map.get(values_[j].getTag()));
					}
					System.out.println("end.....");
					layout.addView(view);
					/*tempText = (TextView) view.findViewById(R.id.h);
					tempText.setText("" + map.get("h"));
					
					
					tempText = (TextView) view.findViewById(R.id.d);
					tempText.setText("" + map.get("d"));
					
					tempText = (TextView) view.findViewById(R.id.a);
					tempText.setText("" + map.get("a"));
					layout.addView(view);*/
				}
				
				ArrayList<Map<String, String>> keyList =result.scorePass;
				temp = findViewById(R.id.scoreAwardLayout);
				titleLayout = temp.findViewById(R.id.titleLayout);
				titleLayout.setOnClickListener(new ItemClick((ViewGroup)temp, mItem));
				
				size = keyList.size();
				ArrayList<String> keys = result.scoreKeys;
				layout = (LinearLayout) temp.findViewById(R.id.mlayout);
				ViewGroup itemLayout = null;
				int dateSize = keys.size();
				String keyTemp = null;
				for (int i = 0; i < size; i++) {
					itemLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.result_score, null);
					 layout.addView(itemLayout);
					 tempText = (TextView) itemLayout.findViewById(R.id.date);

					map = keyList.get(i);
					
					tempText.setText(map.get(keys.get(0)));
					 
					 newLine(itemLayout);
					 for (int j = 1; j <dateSize; j++) {
						 if (count > 4) {
								newLine(layout);
							}
						 keyTemp = keys.get(j);
						 texts[count].setText(keyTemp+"");
						 texts[count].setVisibility(View.VISIBLE);
						 values[count].setText("" + map.get(keyTemp));
						 values[count].setVisibility(View.VISIBLE);
						 setCom(values_[count], map.get(keyTemp+"1"));
						 count ++;
					}
					
				}
				
				
				
				
				
			}
			super.onPostExecute(result);
		}
		
		private TextView[]texts = new TextView[5];
		private int []ids={R.id.text0,R.id.text1,R.id.text2,R.id.text3,R.id.text4};
		private int []ids_={R.id.text01,R.id.text11,R.id.text21,R.id.text31,R.id.text41};
		private TextView[] values= new TextView[5];
		private TextView[] values_ = new TextView[5];
		private int count = 0;
		private View line;
		
		
		
		private void newLine(ViewGroup item)
		{
			count = 0;
			line =  getLayoutInflater().inflate(R.layout.single_award_item_tablerow, null);
			
			item.addView(line);
			View view = line.findViewById(R.id.titleLayout);
			View valueView = line.findViewById(R.id.valueLayout);
			
			for (int i = 0; i < 5; i++) {
				texts[i] = (TextView) view.findViewById(ids[i]);
				values[i]=(TextView) valueView.findViewById(ids[i]);
				values_[i] = (TextView) valueView.findViewById(ids_[i]);
				
			}
		}
		
		
		private void init(View view)
		{
			for (int i = 0; i <5; i++) {
				texts[i] = (TextView) view.findViewById(ids[i]);
				values_[i] = (TextView) view.findViewById(ids_[i]);
			}
		}
	}

	
	
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public interface ItemClickListener
	{
		public void onItemClick(ViewGroup item,View view,boolean expand);
	}
	
	private ItemClickListener mItem = new ItemClickListener() {
		
		
		@Override
		public void onItemClick(ViewGroup item, View view, boolean expand) {
			View layout = item.findViewById(R.id.mlayout);
			if (expand) {
				layout.setVisibility(View.VISIBLE);
			}else
			{
				layout.setVisibility(View.GONE);
			}
		}
	};
	
	public static class ItemClick implements View.OnClickListener
	{
		private ItemClickListener mItemClick;
		private ViewGroup item;
		public ItemClick(ViewGroup item,ItemClickListener itemClick) {
			this.item = item;
			mItemClick = itemClick;
		}
		@Override
		public void onClick(View v) {
			if (mItemClick != null) 
			{
				Object tag = v.getTag();
				boolean expand = false;
				if (tag != null) {
					expand = (Boolean)tag;
				}
				expand =!expand;				
				mItemClick.onItemClick(item, v, expand);
			    v.setTag(expand);
			}
			
		}

		
		
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
