package cn.chaoren;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import cn.chaoren.net.HttpServices;
import cn.chaoren.net.JSONBean;
import cn.chaoren.util.StringUtils;
import cn.chaoren.view.ProgressLinearLayout;
import cn.chaoren.R;

import com.mobclick.android.MobclickAgent;

/**
 * 赛果开奖界面 
 */
public class MatchResultActivity extends Activity implements OnClickListener {
	boolean zuqiuInit = true;
	private TextView zuqiu_result_time_text, zuqiu_match_liansai_text,
			zuqiu_match_liansai_id_text;
	private View zuqiuTab, zuqiu_result_layout, zuqiu_match_result_sel,
			 zuqiu_time_layout,zuqiu_liansai_layout;
	private boolean zuqiu_result_loading = false;
	private ProgressLinearLayout 
			zuqiu_result_progressbar;
	//private List<Map<String, String>> zuqiuLeagues = new ArrayList<Map<String, String>>();
	private ArrayList<String> zuqiuLeagues = new ArrayList<String>();//足球状态
	private ListView zuqiu_result_list;
	private List<Map<String, String>> zuqiu_results = new ArrayList<Map<String, String>>();//返回足球结果条目集合
	private List<Map<String, String>> zuqiu_results_all = new ArrayList<Map<String, String>>();
	private SimpleAdapter zuqiu_result_adapter;

	boolean lanqiuInit = true;
	private TextView lanqiu_result_time_text, lanqiu_match_liansai_text,
			lanqiu_match_liansai_id_text;
	private View lanqiuTab, lanqiu_result_layout, lanqiu_match_result_sel,
			 lanqiu_time_layout,lanqiu_liansai_layout;
	private boolean lanqiu_result_loading = false;
	private ProgressLinearLayout 
			lanqiu_result_progressbar;
	//private List<Map<String, String>> lanqiuLeagues = new ArrayList<Map<String, String>>();
	private ArrayList<String>lanqiuLeagues = new ArrayList<String>();
	private ListView lanqiu_result_list;
	private List<Map<String, String>> lanqiu_results_all = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> lanqiu_results = new ArrayList<Map<String, String>>();
	private SimpleAdapter lanqiu_result_adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.match_result);
		
		zuqiuTab = findViewById(R.id.zuqiu_tab);
		zuqiuTab.setOnClickListener(this);
		zuqiu_result_list = (ListView) findViewById(R.id.zuqiu_match_result_list);
		zuqiu_result_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Map<String, String> _map = zuqiu_results.get(arg2);
				if (_map.get("status").indexOf("进行") >= 0) {
					Toast.makeText(getApplicationContext(),//进行中
							R.string.result_tip, Toast.LENGTH_SHORT).show();
				} else if (_map.get("status").indexOf("取消") >= 0) {
					Toast.makeText(getApplicationContext(),//已取消
							R.string.result_tip1, Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(MatchResultActivity.this,
							ZuqiuResultDetailActivity.class);//跳转到足球结果详情界面
					intent.putExtra("date", _map.get("date"));
					intent.putExtra("num", _map.get("num"));
					intent.putExtra("m_id", _map.get("m_id"));
					intent.putExtra("l_cn", _map.get("l_cn"));
					intent.putExtra("h_cn", _map.get("h_cn"));
					intent.putExtra("a_cn", _map.get("a_cn"));
					intent.putExtra("goalline", _map.get("goalline"));
					startActivity(intent);
				}
			}
		});
		zuqiu_result_progressbar = (ProgressLinearLayout) findViewById(R.id.zuqiu_result_progress);
		zuqiu_result_progressbar.setVisibility(View.GONE);
		zuqiu_result_progressbar.initView();
		zuqiu_result_progressbar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				zuqiuMatchResult();
			}
		});
		zuqiu_result_adapter = new SimpleAdapter(this, zuqiu_results,
				R.layout.zuqiu_result_list_item, new String[] { "num", "l_cn",
						"h_cn", "goalline", "half", "status", "date", "a_cn",
						"final" }, new int[] { R.id.num, R.id.l_cn, R.id.h_cn,
						R.id.goalline, R.id.half, R.id.status, R.id.date,
						R.id.a_cn, R.id.finaled });
		zuqiu_result_list.setAdapter(zuqiu_result_adapter);//足球结果设置填充器
		
		zuqiu_result_layout = findViewById(R.id.zuqiu_match_result_layout);
		zuqiu_match_result_sel = findViewById(R.id.zuqiu_match_result_sel);
		zuqiu_result_time_text = (TextView) findViewById(R.id.zuqiu_match_result_time_text);
		zuqiu_result_time_text.setText(StringUtils.getTime());
		zuqiu_time_layout = findViewById(R.id.zuqiu_time_layout);
		zuqiu_time_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//对选中的时间，切割
				String[] values = zuqiu_result_time_text.getText().toString()
						.split("-");
				if (values.length != 3) {
					values = StringUtils.getTime().split("-");
				}
				new DatePickerDialog(MatchResultActivity.this,
						new DatePickerDialog.OnDateSetListener() {
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								monthOfYear++;
								StringBuffer buf = new StringBuffer();
								buf.append(year + "-");
								if (monthOfYear < 10) {
									buf.append("0" + monthOfYear + "-");
								} else {
									buf.append(monthOfYear + "-");
								}
								if (dayOfMonth < 10) {
									buf.append("0" + dayOfMonth);
								} else {
									buf.append(dayOfMonth);
								}
								zuqiu_result_time_text.setText(buf.toString());
								zuqiuMatchResult();
							}
						}, Integer.parseInt(values[0]), Integer
								.parseInt(values[1]) - 1, Integer
								.parseInt(values[2])).show();
			}
		});
		zuqiu_match_liansai_id_text = (TextView) findViewById(R.id.zuqiu_match_liansai_id_text);
		zuqiu_match_liansai_text = (TextView) findViewById(R.id.zuqiu_match_liansai_text);
		zuqiu_liansai_layout  = findViewById(R.id.zuqiu_liansai_layout);
		zuqiu_liansai_layout.setOnClickListener(new OnClickListener() {
			
			private void selectList(String l_cn,int index)
			{
				//zuqiu_results_all;
				zuqiu_results.clear();
				int size = zuqiu_results_all.size();
				if (index == 0) {
					zuqiu_results.addAll(zuqiu_results_all);
				}else
				{
					Map<String, String> map = null;
					for (int i = 0; i < size; i++) {
						map = zuqiu_results_all.get(i);
						if (map.get("l_cn").equals(l_cn)) {
							zuqiu_results.add(map);
						}
					}
				}
				
				zuqiu_result_adapter.notifyDataSetChanged();
			}
			@Override
			public void onClick(View v) {
				final String[] _strings = new String[zuqiuLeagues.size()];
				for (int i = 0; i < zuqiuLeagues.size(); i++) {
					//Map<String, String> _map = zuqiuLeagues.get(i);
					_strings[i] = zuqiuLeagues.get(i);
				}
				new AlertDialog.Builder(MatchResultActivity.this)
						.setTitle(R.string.qxzls)
						.setItems(_strings,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface a0,
											int a1) {
										/*Map<String, String> _map = zuqiuLeagues
												.get(a1);*/
										zuqiu_match_liansai_id_text
												.setText("");
										zuqiu_match_liansai_text.setText(zuqiuLeagues
												.get(a1));
										selectList(zuqiuLeagues
												.get(a1),a1);
									}
								}).show();
			}
		});
		/*zuqiu_leagues_progress = (ProgressLinearLayout) findViewById(R.id.zuqiu_leagues_progress);
		zuqiu_leagues_progress.initView();
		zuqiu_leagues_progress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new ZuqiuLeagueListTask().execute();
			}
		});*/
		/*zuqiu_math_result_btn = findViewById(R.id.zuqiu_math_result_btn);
		zuqiu_math_result_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				zuqiuMatchResult();
			}
		});*/

		lanqiuTab = findViewById(R.id.lanqiu_tab);
		lanqiuTab.setOnClickListener(this);
		lanqiu_result_list = (ListView) findViewById(R.id.lanqiu_match_result_list);
		lanqiu_result_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Map<String, String> _map = lanqiu_results.get(arg2);
				if (_map.get("status").indexOf("进行") >= 0) {
					Toast.makeText(getApplicationContext(),
							R.string.result_tip, Toast.LENGTH_SHORT).show();
				} else if (_map.get("status").indexOf("取消") >= 0) {
					Toast.makeText(getApplicationContext(),
							R.string.result_tip1, Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(MatchResultActivity.this,
							LanqiuResultDetailActivity.class);
					intent.putExtra("date", _map.get("date"));
					intent.putExtra("num", _map.get("num"));
					intent.putExtra("m_id", _map.get("m_id"));
					intent.putExtra("l_cn", _map.get("l_cn"));
					intent.putExtra("h_cn", _map.get("h_cn"));
					intent.putExtra("a_cn", _map.get("a_cn"));
					startActivity(intent);
				}
			}
		});
		lanqiu_result_progressbar = (ProgressLinearLayout) findViewById(R.id.lanqiu_result_progress);
		lanqiu_result_progressbar.setVisibility(View.GONE);
		lanqiu_result_progressbar.initView();
		lanqiu_result_progressbar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				lanqiuMatchResult();
			}
		});
		lanqiu_result_adapter = new SimpleAdapter(this, lanqiu_results,
				R.layout.lanqiu_result_list_item, new String[] { "num", "l_cn",
						"h_cn", "status", "date", "a_cn", "final", "s1", "s2",
						"s3", "s4" }, new int[] { R.id.num, R.id.l_cn,
						R.id.h_cn, R.id.status, R.id.date, R.id.a_cn,
						R.id.finaled, R.id.s1, R.id.s2, R.id.s3, R.id.s4 });
		lanqiu_result_list.setAdapter(lanqiu_result_adapter);
		lanqiu_result_layout = findViewById(R.id.lanqiu_match_result_layout);
		lanqiu_match_result_sel = findViewById(R.id.lanqiu_match_result_sel);
		lanqiu_result_time_text = (TextView) findViewById(R.id.lanqiu_match_result_time_text);
		lanqiu_result_time_text.setText(StringUtils.getTime());
		lanqiu_time_layout = findViewById(R.id.lanqiu_time_layout);
		lanqiu_time_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String[] values = lanqiu_result_time_text.getText().toString()
						.split("-");
				if (values.length != 3) {
					values = StringUtils.getTime().split("-");
				}
				new DatePickerDialog(MatchResultActivity.this,
						new DatePickerDialog.OnDateSetListener() {
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								monthOfYear++;
								StringBuffer buf = new StringBuffer();
								buf.append(year + "-");
								if (monthOfYear < 10) {
									buf.append("0" + monthOfYear + "-");
								} else {
									buf.append(monthOfYear + "-");
								}
								if (dayOfMonth < 10) {
									buf.append("0" + dayOfMonth);
								} else {
									buf.append(dayOfMonth);
								}
								lanqiu_result_time_text.setText(buf.toString());
								lanqiuMatchResult();
							}
						}, Integer.parseInt(values[0]), Integer
								.parseInt(values[1]) - 1, Integer
								.parseInt(values[2])).show();
			}
		});
		lanqiu_match_liansai_id_text = (TextView) findViewById(R.id.lanqiu_match_liansai_id_text);
		lanqiu_match_liansai_text = (TextView) findViewById(R.id.lanqiu_match_liansai_text);
		lanqiu_liansai_layout = findViewById(R.id.lanqiu_liansai_layout);
		lanqiu_liansai_layout.setOnClickListener(new OnClickListener() {
			private void selectList(String t_cn,int index)
			{
				//zuqiu_results_all;
				lanqiu_results.clear();
				int size = lanqiu_results_all.size();
				if (index == 0) {
					lanqiu_results.addAll(lanqiu_results_all);
				}else
				{
					Map<String, String> map = null;
					for (int i = 0; i < size; i++) {
						map = lanqiu_results_all.get(i);
						if (map.get("l_cn").equals(t_cn)) {
							System.out.println(".....");
							lanqiu_results.add(map);
						}
					}
				}
				
				lanqiu_result_adapter.notifyDataSetChanged();
			}
			@Override
			public void onClick(View v) {
				final String[] _strings = new String[lanqiuLeagues.size()];
				for (int i = 0; i < lanqiuLeagues.size(); i++) {
					
					_strings[i] = lanqiuLeagues.get(i);
				}
			
				new AlertDialog.Builder(MatchResultActivity.this)
						.setTitle(R.string.qxznba)//请选择球队
						.setItems(_strings,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface a0,
											int a1) {
										/*Map<String, String> _map = lanqiuLeagues
												.get(a1);*/
										lanqiu_match_liansai_id_text
												.setText("");
										lanqiu_match_liansai_text.setText( lanqiuLeagues
												.get(a1));
										
										/*zuqiu_match_liansai_text.setText(zuqiuLeagues
												.get(a1));*/
										selectList(lanqiuLeagues
												.get(a1),a1);
									}
								}).show();
			}
		});
		/*lanqiu_leagues_progress = (ProgressLinearLayout) findViewById(R.id.lanqiu_leagues_progress);
		lanqiu_leagues_progress.initView();
		lanqiu_leagues_progress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new LanqiuLeagueListTask().execute();
			}
		});*/
		/*lanqiu_math_result_btn = findViewById(R.id.lanqiu_math_result_btn);
		lanqiu_math_result_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				lanqiuMatchResult();
			}
		});*/
		onClick(zuqiuTab);
	}

	public void onResume() {
		
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	public void onBackPressed() {
		if (getParent() != null) {
			getParent().onBackPressed();
		}
		//super.onBackPressed();
	}
	
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	/*class ZuqiuLeagueListTask extends AsyncTask<String, Integer, JSONBean> {
		public ZuqiuLeagueListTask() {
			zuqiuInit = false;
		}

		@Override
		protected JSONBean doInBackground(String... params) {
			return HttpServices.getLeagueListFB();
		}

		@Override
		protected void onPostExecute(JSONBean result) {
			if (result == null) {
				zuqiu_leagues_progress.setVisibility(View.GONE, View.GONE,
						View.VISIBLE, View.VISIBLE);
			} else {
				zuqiu_leagues_progress.setVisibility(View.GONE);
				zuqiu_match_result_sel.setVisibility(View.VISIBLE);
				zuqiuLeagues.clear();
				Map<String, String> map = new HashMap<String, String>();
				map.put("l_id", "");
				map.put("l_cn", getResources().getString(R.string.djxzls));
				zuqiuLeagues.add(map);
				zuqiuLeagues.addAll(result.bodys);
				zuqiuMatchResult();
			}
			super.onPostExecute(result);
		}
	}*/

	/*class LanqiuLeagueListTask extends AsyncTask<String, Integer, JSONBean> {
		public LanqiuLeagueListTask() {
			lanqiuInit = false;
		}

		@Override
		protected JSONBean doInBackground(String... params) {
			return HttpServices.getLeagueListBK();
		}

		@Override
		protected void onPostExecute(JSONBean result) {
			if (result == null) {
				lanqiu_leagues_progress.setVisibility(View.GONE, View.GONE,
						View.VISIBLE, View.VISIBLE);
			} else {
				lanqiu_leagues_progress.setVisibility(View.GONE);
				lanqiu_match_result_sel.setVisibility(View.VISIBLE);
				lanqiuLeagues.clear();
				Map<String, String> map = new HashMap<String, String>();
				map.put("t_id", "");
				map.put("t_cn", getResources().getString(R.string.djxznba));
				lanqiuLeagues.add(map);
				lanqiuLeagues.addAll(result.bodys);
				lanqiuMatchResult();
			}
			super.onPostExecute(result);
		}
	}*/

	//向服务器获取数据，得到足球各条目的数据
	class ZuqiuMathResultTask extends AsyncTask<String, Integer, JSONBean> {
		@Override
		protected void onPreExecute() {//运行前的操作
			zuqiu_result_loading = true;
			zuqiu_results.clear();
			zuqiu_result_progressbar.setVisibility(View.VISIBLE);
			zuqiu_result_progressbar.setVisibility(View.VISIBLE, View.VISIBLE,
					View.GONE, View.GONE);
			zuqiu_result_list.setVisibility(View.GONE);
			super.onPreExecute();
		}

		//从服务器返回回来的数据
		@Override
		protected JSONBean doInBackground(String... params) {
			if (params.length == 2) {//先判断数组的长度
				return HttpServices.getMatchResultFB(params[0], params[0],
						params[1]);//连接服务器，获得足球赛果开奖。开始日期，结束日期，联赛ID
			}
			return null;
		}

		
		//将数据显示到UI界面上来
		//result为服务器返回并解析好的数据
		@Override
		protected void onPostExecute(JSONBean result) {
			zuqiu_results_all.clear();
			zuqiu_results.clear();
			zuqiu_result_loading = false;
			zuqiu_match_result_sel.setVisibility(View.VISIBLE);
			if (result == null) {
				zuqiu_result_progressbar.setVisibility(View.GONE, View.GONE,
						View.VISIBLE, View.VISIBLE);
			} else {
				if (result.bodys.size() == 0) {
					zuqiu_result_progressbar.setVisibility(View.GONE,
							View.GONE, View.VISIBLE, View.GONE);
					zuqiu_result_progressbar.setText(null, getResources()
							.getString(R.string.myzdfhtjdsgkjsj));
				} else {
					select(result.bodys, "l_cn");//第二个参数是：联赛中文名
					
					zuqiu_results_all.addAll(result.bodys);//取出服务器数据，再放到定义好的集合中去
					zuqiu_results.addAll(result.bodys);//先定义一个集合，再将返回回来的数据添加到集合中去
					
					zuqiu_result_progressbar.setVisibility(View.GONE);
					zuqiu_result_list.setVisibility(View.VISIBLE);
				}
			}
			zuqiu_result_adapter.notifyDataSetChanged();
			super.onPostExecute(result);
		}
	}
	
	//服务器返回数据，第二个参数是：联赛中文名。得到选择的赛制名称，显示在界面上。
	private void select(List<Map<String, String>> body,String key)
	{
		
		zuqiuLeagues.clear();
		int size = body.size();
		///map.put("l_cn", getResources().getString(R.string.djxzls));
		Map<String, String> map = null;
		zuqiuLeagues.add(getResources().getString(R.string.djxzls));
		zuqiu_match_liansai_text.setText(zuqiuLeagues.get(0));
		for (int i = 0; i < size; i++) {
			map = body.get(i);				
			if (!zuqiuLeagues.contains(map.get(key))) {
				zuqiuLeagues.add(map.get(key));
			}
		}
	}
	
	
	

	class LanqiuMathResultTask extends AsyncTask<String, Integer, JSONBean> {
		@Override
		protected void onPreExecute() {
			lanqiu_result_loading = true;
			lanqiu_results.clear();
			lanqiu_result_progressbar.setVisibility(View.VISIBLE);
			lanqiu_result_progressbar.setVisibility(View.VISIBLE, View.VISIBLE,
					View.GONE, View.GONE);
			lanqiu_result_list.setVisibility(View.GONE);
			super.onPreExecute();
		}

		
		@Override
		protected JSONBean doInBackground(String... params) {
			if (params.length == 2) {
				return HttpServices.getMatchResultBK(params[0], params[0],
						params[1]);
			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONBean result) {
			lanqiu_results_all.clear();
			lanqiu_results.clear();
			lanqiu_result_loading = false;
			lanqiu_match_result_sel.setVisibility(View.VISIBLE);
			if (result == null) {
				lanqiu_result_progressbar.setVisibility(View.GONE, View.GONE,
						View.VISIBLE, View.VISIBLE);
			} else {
				if (result.bodys.size() == 0) {
					lanqiu_result_progressbar.setVisibility(View.GONE,
							View.GONE, View.VISIBLE, View.GONE);
					lanqiu_result_progressbar.setText(null, getResources()
							.getString(R.string.myzdfhtjdsgkjsj));
				} else {
					
					select(result.bodys, "l_cn");
					lanqiu_results_all.addAll(result.bodys);
					lanqiu_results.addAll(result.bodys);
					lanqiu_result_progressbar.setVisibility(View.GONE);
					lanqiu_result_list.setVisibility(View.VISIBLE);
				}
			}
			lanqiu_result_adapter.notifyDataSetChanged();
			super.onPostExecute(result);
		}
		
		private void select(List<Map<String, String>> body,String key)
		{
			
			lanqiuLeagues.clear();
			int size = body.size();
			///map.put("l_cn", getResources().getString(R.string.djxzls));
			
			Map<String, String> map = null;
			lanqiuLeagues.add(getResources().getString(R.string.djxzls));
			lanqiu_match_liansai_text.setText(lanqiuLeagues.get(0));
			for (int i = 0; i < size; i++) {
				map = body.get(i);				
				if (!lanqiuLeagues.contains(map.get(key))) {
					lanqiuLeagues.add(map.get(key));
				}
			}
		}
		
	}
	/**
	 * 在以下两个方法中进行异步加载操作
	 * 
	 */
	private void zuqiuMatchResult() {
		if (zuqiu_result_loading)
			return;
		String time = zuqiu_result_time_text.getText().toString();//得到选中的时间
		 //开启足球界面异步服务器数据获取
		new ZuqiuMathResultTask().execute(new String[] { time,     //里面是个数组，有两个参数,起止的时间
				zuqiu_match_liansai_id_text.getText().toString() });
	}

	private void lanqiuMatchResult() {
		if (lanqiu_result_loading)
			return;
		String time = lanqiu_result_time_text.getText().toString();
		new LanqiuMathResultTask().execute(new String[] { time,//开启篮球界面异步服务器数据获取
				lanqiu_match_liansai_id_text.getText().toString() });
	}

	
	
	
	//实现界面中的点击事件
	@Override
	public void onClick(View arg0) {
		if (arg0 == zuqiuTab) {
			zuqiuTab.setBackgroundResource(R.drawable.tab_l_select);
			zuqiu_result_layout.setVisibility(View.VISIBLE);
			lanqiuTab.setBackgroundResource(R.drawable.tab_l_normal);
			lanqiu_result_layout.setVisibility(View.GONE);
			if (zuqiuInit)
				zuqiuMatchResult();//如果点击足球就调用该方法
		} else if (arg0 == lanqiuTab) {
			lanqiuTab.setBackgroundResource(R.drawable.tab_l_select);
			lanqiu_result_layout.setVisibility(View.VISIBLE);
			zuqiuTab.setBackgroundResource(R.drawable.tab_l_normal);
			zuqiu_result_layout.setVisibility(View.GONE);
			if (lanqiuInit)
				lanqiuMatchResult();//如果点击篮球就调用该方法
		}
	}
}
