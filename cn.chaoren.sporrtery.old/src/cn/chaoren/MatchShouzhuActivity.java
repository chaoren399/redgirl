package cn.chaoren;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.chaoren.net.HttpServices;
import cn.chaoren.net.JSONBean;
import cn.chaoren.util.DBService;
import cn.chaoren.util.StringUtils;
import cn.chaoren.R;

import com.mobclick.android.MobclickAgent;

/**
 * 类说明：
 * 
 * @创建时间 2011-8-5 上午08:49:22
 * @创建人： 陆林
 * @邮箱：15366189868@189.cn
 */
public class MatchShouzhuActivity extends Activity implements OnClickListener {
	private View tab_zuqiu, tab_lanqiu, zuqiu_layout, lanqiu_layout,
			zuqiu_match_flush_btn, lanqiu_match_flush_btn,
			zuqiu_refresh_progress, lanqiu_refresh_progress;

	boolean zuqiuInit = true;
	private ListView zuqiuMatchListView;
	private ZuqiuMatchListAdapter zuqiuMatchListAdapter;
	private ArrayList<Map<String, String>> zuqiuMatchList = new ArrayList<Map<String, String>>();//返回的数据，也是填充的数据
	private View zuqiu_match_list_progress_layout, zuqiu_match_list_progress;
	private TextView zuqiu_match_list_progress_text;
	private TextView zuqiu_time_text;
	private View zuqiu_time_layout, zuqiu_match_list_progress_button;
	private boolean zuqiu_loading = false;
	private String[] zuqiu_weeks;

	boolean lanqiuInit = true;
	private ListView lanqiuMatchListView;
	private LanqiuMatchListAdapter lanqiuMatchListAdapter;
	private ArrayList<Map<String, String>> lanqiuMatchList = new ArrayList<Map<String, String>>();
	private View lanqiu_match_list_progress_layout, lanqiu_match_list_progress;
	private TextView lanqiu_match_list_progress_text;
	private TextView lanqiu_time_text;
	private View lanqiu_time_layout, lanqiu_match_list_progress_button;
	private boolean lanqiu_loading = false;
	private String[] lanqiu_weeks;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.match_shouzhu);

		tab_zuqiu = findViewById(R.id.zuqiu_tab);
		tab_zuqiu.setOnClickListener(this);
		tab_lanqiu = findViewById(R.id.lanqiu_tab);
		tab_lanqiu.setOnClickListener(this);

		zuqiu_layout = findViewById(R.id.zuqiu_layout);
		zuqiu_layout.setVisibility(View.VISIBLE);
		lanqiu_layout = findViewById(R.id.lanqiu_layout);
		lanqiu_layout.setVisibility(View.GONE);

		zuqiuMatchListView = (ListView) this
				.findViewById(R.id.zuqiu_match_list);
		zuqiuMatchListAdapter = new ZuqiuMatchListAdapter();
		zuqiuMatchListView.setAdapter(zuqiuMatchListAdapter);
		zuqiu_match_list_progress_layout = findViewById(R.id.zuqiu_match_list_progress_layout);
		zuqiu_match_list_progress = findViewById(R.id.zuqiu_match_list_progress);
		zuqiu_match_list_progress_text = (TextView) findViewById(R.id.zuqiu_match_list_progress_text);
		zuqiu_match_list_progress_button = findViewById(R.id.zuqiu_match_list_progress_button);
		zuqiu_match_list_progress_button
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						new ZuqiuMatchListTask()
								.execute(new String[] { zuqiu_time_text
										.getText().toString() });
					}
				});

		zuqiu_time_text = (TextView) findViewById(R.id.zuqiu_time_text);
		zuqiu_time_text.setText(StringUtils.getWeek());
		zuqiu_time_layout = findViewById(R.id.zuqiu_time_layout);
		zuqiu_time_layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (zuqiu_loading)
					return;
				if (zuqiu_weeks == null || zuqiu_weeks.length == 0) {
					Toast.makeText(MatchShouzhuActivity.this, "无可选日期数据",
							Toast.LENGTH_SHORT).show();
				} else {
					new AlertDialog.Builder(MatchShouzhuActivity.this)
							.setTitle("请选择")
							.setItems(zuqiu_weeks,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											if (!zuqiu_weeks[which]
													.equals(zuqiu_time_text
															.getText()
															.toString())) {
												zuqiu_time_text
														.setText(zuqiu_weeks[which]);
												new ZuqiuMatchListTask()
														.execute(new String[] { zuqiu_time_text
																.getText()
																.toString() });
											}
										}
									}).create().show();
				}
			}
		});
		zuqiu_refresh_progress = findViewById(R.id.zuqiu_refresh_progress);
		zuqiu_match_flush_btn = findViewById(R.id.zuqiu_match_flush_btn);
		zuqiu_match_flush_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new ZuqiuMatchListTask().execute(new String[] { zuqiu_time_text
						.getText().toString() });
			}
		});

		lanqiuMatchListView = (ListView) this
				.findViewById(R.id.lanqiu_match_list);
		lanqiuMatchListAdapter = new LanqiuMatchListAdapter();
		lanqiuMatchListView.setAdapter(lanqiuMatchListAdapter);
		lanqiu_match_list_progress_layout = findViewById(R.id.lanqiu_match_list_progress_layout);
		lanqiu_match_list_progress = findViewById(R.id.lanqiu_match_list_progress);
		lanqiu_match_list_progress_text = (TextView) findViewById(R.id.lanqiu_match_list_progress_text);
		lanqiu_match_list_progress_button = findViewById(R.id.lanqiu_match_list_progress_button);
		lanqiu_match_list_progress_button
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						new LanqiuMatchListTask()
								.execute(new String[] { lanqiu_time_text
										.getText().toString() });
					}
				});
		lanqiu_time_text = (TextView) findViewById(R.id.lanqiu_time_text);
		lanqiu_time_text.setText(StringUtils.getWeek());
		lanqiu_time_layout = findViewById(R.id.lanqiu_time_layout);
		lanqiu_time_layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (lanqiu_loading)
					return;
				if (lanqiu_weeks == null || lanqiu_weeks.length == 0) {
					Toast.makeText(MatchShouzhuActivity.this, "无可选日期数据",
							Toast.LENGTH_SHORT).show();
				} else {
					new AlertDialog.Builder(MatchShouzhuActivity.this)
							.setTitle("请选择")
							.setItems(lanqiu_weeks,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											if (!lanqiu_weeks[which]
													.equals(lanqiu_time_text
															.getText()
															.toString())) {
												lanqiu_time_text
														.setText(lanqiu_weeks[which]);
												new LanqiuMatchListTask()
														.execute(new String[] { lanqiu_time_text
																.getText()
																.toString() });
											}
										}
									}).create().show();
				}
			}
		});
		lanqiu_refresh_progress = findViewById(R.id.lanqiu_refresh_progress);
		lanqiu_match_flush_btn = findViewById(R.id.lanqiu_match_flush_btn);
		lanqiu_match_flush_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new LanqiuMatchListTask()
						.execute(new String[] { lanqiu_time_text.getText()
								.toString() });
			}
		});
		onClick(tab_zuqiu);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		flushZuqiuMaked();
		flushLanqiuMaked();
	}

	private void flushZuqiuMaked() {
		DBService dbService = new DBService(MatchShouzhuActivity.this);
		dbService.open();
		ArrayList<Map<String, String>> makedList = new ArrayList<Map<String, String>>();
		makedList.addAll(dbService.getAll());
		dbService.close();
		for (int i = 0; i < zuqiuMatchList.size(); i++) {
			Map<String, String> map = zuqiuMatchList.get(i);
			String m_id = map.get("m_id");
			boolean maked = false;
			for (int j = 0; j < makedList.size(); j++) {
				if (makedList.get(j).get("m_id").equals(m_id)) {
					maked = true;
					break;
				}
			}
			map.put("maked", maked ? "1" : "0");
		}
		zuqiuMatchListAdapter.notifyDataSetChanged();
	}

	private void addZuqiuMaked(int index) {
		Map<String, String> _map = zuqiuMatchList.get(index);
		String m_id = _map.get("m_id");
		DBService dbService = null;
		try {
			dbService = new DBService(MatchShouzhuActivity.this);
			dbService.open();
			if (_map.get("maked") != null && _map.get("maked").equals("1")) {
				dbService.del(m_id);
				_map.put("maked", "0");
				Toast.makeText(MatchShouzhuActivity.this, R.string.yscgz,
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(MatchShouzhuActivity.this, R.string.ytjdgz,
						Toast.LENGTH_SHORT).show();
				dbService.insert(_map);
				_map.put("maked", "1");
			}
			zuqiuMatchListAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dbService != null) {
				dbService.close();
			}
		}
	}

	private void addLanqiuMaked(int index) {
		Map<String, String> _map = lanqiuMatchList.get(index);
		String m_id = _map.get("m_id");
		DBService dbService = null;
		try {
			dbService = new DBService(MatchShouzhuActivity.this);
			dbService.open();
			if (_map.get("maked") != null && _map.get("maked").equals("1")) {
				dbService.del(m_id);
				_map.put("maked", "0");
				Toast.makeText(MatchShouzhuActivity.this, R.string.yscgz,
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(MatchShouzhuActivity.this, R.string.ytjdgz,
						Toast.LENGTH_SHORT).show();
				_map.put("maked", "1");
				dbService.insert(_map);
			}
			lanqiuMatchListAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dbService != null) {
				dbService.close();
			}
		}
	}

	private void flushLanqiuMaked() {
		DBService dbService = new DBService(MatchShouzhuActivity.this);
		dbService.open();
		ArrayList<Map<String, String>> makedList = new ArrayList<Map<String, String>>();
		makedList.addAll(dbService.getAll());
		dbService.close();
		for (int i = 0; i < lanqiuMatchList.size(); i++) {
			Map<String, String> map = lanqiuMatchList.get(i);
			String m_id = map.get("m_id");
			boolean maked = false;
			for (int j = 0; j < makedList.size(); j++) {
				if (makedList.get(j).get("m_id").equals(m_id)) {
					maked = true;
					break;
				}
			}
			map.put("maked", maked ? "1" : "0");
		}
		lanqiuMatchListAdapter.notifyDataSetChanged();
	}

	class LanqiuMatchListTask extends AsyncTask<String, Integer, JSONBean> {
		public LanqiuMatchListTask() {
			lanqiuInit = false;
		}

		//之前的操作
		@Override
		protected void onPreExecute() {
			lanqiu_match_flush_btn.setVisibility(View.INVISIBLE);
			lanqiu_refresh_progress.setVisibility(View.VISIBLE);
			lanqiu_match_list_progress_layout.setVisibility(View.VISIBLE);
			lanqiu_match_list_progress.setVisibility(View.VISIBLE);
			lanqiu_match_list_progress_text.setText(R.string.progress_wait);
			lanqiu_match_list_progress_button.setVisibility(View.GONE);
			lanqiuMatchListView.setVisibility(View.GONE);
			lanqiu_loading = true;
			super.onPreExecute();
		}

		//向服务器取数据
		@Override
		protected JSONBean doInBackground(String... params) {
			if (params == null || params.length == 0) {
				return HttpServices.getMatchScheduleBK(StringUtils.getWeek());
			} else {
				return HttpServices.getMatchScheduleBK(params[0]);
			}
		}

		//之后的操作
		@Override
		protected void onPostExecute(JSONBean result) {
			lanqiu_match_flush_btn.setVisibility(View.VISIBLE);
			lanqiu_refresh_progress.setVisibility(View.INVISIBLE);
			lanqiu_loading = false;
			if (result == null) {
				lanqiu_match_list_progress.setVisibility(View.GONE);
				lanqiu_match_list_progress_text
						.setText(R.string.progress_net_error);
				lanqiu_match_list_progress_button.setVisibility(View.VISIBLE);
			} else if (!result.code.equals("0")) {
				lanqiu_match_list_progress.setVisibility(View.GONE);
				lanqiu_match_list_progress_text.setText(R.string.cwdztm);
				lanqiu_match_list_progress_button.setVisibility(View.GONE);
			} else {
				lanqiu_weeks = new String[result.times.size()];
				result.times.toArray(lanqiu_weeks);
				lanqiu_time_text.setText(result.cnumw);
				lanqiuMatchList.clear();
				if (result.bodys.size() == 0) {
					lanqiu_match_list_progress.setVisibility(View.GONE);
					lanqiu_match_list_progress_text.setText(R.string.mycxdbssj);
					lanqiu_match_list_progress_button.setVisibility(View.GONE);
				} else {
					lanqiuMatchList.addAll(result.bodys);
					lanqiu_match_list_progress_layout.setVisibility(View.GONE);
					lanqiu_match_list_progress_button.setVisibility(View.GONE);
					lanqiuMatchListView.setVisibility(View.VISIBLE);
					flushLanqiuMaked();
				}
			}
			super.onPostExecute(result);
		}
	}

	class LanqiuMatchListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return lanqiuMatchList.size();
		}

		@Override
		public Object getItem(int position) {
			return lanqiuMatchList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final Map<String, String> map = lanqiuMatchList.get(position);
			if (map == null)
				return convertView;
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.lanqiu_match_list_item, parent, false);
			}
			TextView num = (TextView) convertView.findViewById(R.id.num);
			num.setText(map.get("num"));
			TextView l_cn = (TextView) convertView.findViewById(R.id.l_cn);
			l_cn.setText(map.get("l_cn"));
			TextView h_cn = (TextView) convertView.findViewById(R.id.h_cn);
			h_cn.setText(map.get("h_cn"));
			TextView a_cn = (TextView) convertView.findViewById(R.id.a_cn);
			a_cn.setText(map.get("a_cn"));
			View sstime = convertView.findViewById(R.id.sstime);
			if (map.get("maked") != null && map.get("maked").equals("1")) {
				sstime.setBackgroundResource(R.drawable.guanzhu);
			} else {
				sstime.setBackgroundResource(R.drawable.quxiao);
			}
			sstime.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					addLanqiuMaked(position);
				}
			});
			
			TextView handicat = (TextView) convertView.findViewById(R.id.handicap);
			String temp = map.get("handicap");
			if (temp == null ||"".equals(temp)|| "0".equals(temp)) {
				handicat.setVisibility(View.GONE);
			}else
			{
				handicat.setVisibility(View.VISIBLE);
				handicat.setText("(" + temp + ")");
			}
			View message = convertView.findViewById(R.id.message);
			temp = map.get("message");
			if (temp != null && !"".equals(temp)) {
				message.setVisibility(View.VISIBLE);
				message.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Toast.makeText(MatchShouzhuActivity.this, ""+map.get("message"), Toast.LENGTH_LONG).show();						
					}
				});
				
			}else {
				message.setVisibility(View.GONE);
			}
			
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MatchShouzhuActivity.this,
							LanqiuMatchDetailActivity.class);
					intent.putExtra("m_id", map.get("m_id"));
					intent.putExtra("num", map.get("num"));
					intent.putExtra("l_cn", map.get("l_cn"));
					intent.putExtra("l_id", map.get("l_id"));
					intent.putExtra("h_cn", map.get("h_cn"));
					intent.putExtra("h_id", map.get("h_id"));
					intent.putExtra("a_cn", map.get("a_cn"));
					intent.putExtra("a_id", map.get("a_id"));
					intent.putExtra("sstime", map.get("sstime").toString());
					startActivity(intent);
				}
			});
			TextView shottime = (TextView) convertView
					.findViewById(R.id.shottime);
			shottime.setText(map.get("shottime"));
			TextView shotdate = (TextView) convertView
					.findViewById(R.id.shotdate);
			shotdate.setText(map.get("shotdate"));
			return convertView;
		}
	}

	class ZuqiuMatchListTask extends AsyncTask<String, Integer, JSONBean> {
		public ZuqiuMatchListTask() {
			zuqiuInit = false;
		}

		@Override
		protected void onPreExecute() {
			zuqiu_match_flush_btn.setVisibility(View.INVISIBLE);
			zuqiu_refresh_progress.setVisibility(View.VISIBLE);
			zuqiu_match_list_progress_layout.setVisibility(View.VISIBLE);
			zuqiu_match_list_progress.setVisibility(View.VISIBLE);
			zuqiu_match_list_progress_text.setText(R.string.progress_wait);
			zuqiuMatchListView.setVisibility(View.GONE);
			zuqiu_match_list_progress_button.setVisibility(View.GONE);
			zuqiu_loading = true;
			super.onPreExecute();
		}

		@Override
		protected JSONBean doInBackground(String... params) {
			if (params == null || params.length == 0) {
				return HttpServices.getMatchScheduleFB(StringUtils.getWeek());//主要指星期几的比赛
			} else {
				return HttpServices.getMatchScheduleFB(params[0]);//参数为比赛日期
			}
		}

		
		//得到从服务器上获得的数据，
		@Override
		protected void onPostExecute(JSONBean result) {
			zuqiu_loading = false;
			zuqiu_match_flush_btn.setVisibility(View.VISIBLE);
			zuqiu_refresh_progress.setVisibility(View.INVISIBLE);
			if (result == null) {
				zuqiu_match_list_progress.setVisibility(View.GONE);
				zuqiu_match_list_progress_text
						.setText(R.string.progress_net_error);
				zuqiu_match_list_progress_button.setVisibility(View.VISIBLE);
			} else if (!result.code.equals("0")) {
				zuqiu_match_list_progress.setVisibility(View.GONE);
				zuqiu_match_list_progress_text.setText(R.string.cwdztm);
				zuqiu_match_list_progress_button.setVisibility(View.GONE);
			} else {
				zuqiuMatchList.clear();
				zuqiu_weeks = new String[result.times.size()];
				result.times.toArray(zuqiu_weeks);
				zuqiu_time_text.setText(result.cnumw);
				if (result.bodys.size() == 0) {
					zuqiu_match_list_progress.setVisibility(View.GONE);
					zuqiu_match_list_progress_text.setText(R.string.mycxdbssj);
					zuqiu_match_list_progress_button.setVisibility(View.GONE);
				} else {
					zuqiuMatchList.addAll(result.bodys);//添加从服务器返回回来的数据
					zuqiu_match_list_progress_layout.setVisibility(View.GONE);
					zuqiuMatchListView.setVisibility(View.VISIBLE);
					flushZuqiuMaked();
				}
			}
			super.onPostExecute(result);
		}
	}

	//数据适配器
	class ZuqiuMatchListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return zuqiuMatchList.size();
		}

		@Override
		public Object getItem(int position) {
			return zuqiuMatchList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final Map<String, String> map = zuqiuMatchList.get(position);
			if (map == null)
			{
				return convertView;
			}
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.zuqiu_match_list_item, parent, false);
			}
			TextView num = (TextView) convertView.findViewById(R.id.num);
			num.setText(map.get("num"));
			TextView l_cn = (TextView) convertView.findViewById(R.id.l_cn);
			l_cn.setText(map.get("l_cn"));
			TextView h_cn = (TextView) convertView.findViewById(R.id.h_cn);
			h_cn.setText(map.get("h_cn"));
			TextView a_cn = (TextView) convertView.findViewById(R.id.a_cn);
			a_cn.setText(map.get("a_cn"));
			View sstime = convertView.findViewById(R.id.sstime);
			if (map.get("maked") != null && map.get("maked").equals("1")) {
				sstime.setBackgroundResource(R.drawable.guanzhu);
			} else {
				sstime.setBackgroundResource(R.drawable.quxiao);
			}
			sstime.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					addZuqiuMaked(position);
				}
			});
			TextView shottime = (TextView) convertView
					.findViewById(R.id.shottime);
			shottime.setText(map.get("shottime"));
			TextView shotdate = (TextView) convertView
					.findViewById(R.id.shotdate);
			shotdate.setText(map.get("shotdate"));
			TextView h = (TextView) convertView.findViewById(R.id.h);
			h.setText(getResources().getString(R.string.h) + map.get("h"));
			TextView d = (TextView) convertView.findViewById(R.id.d);
			d.setText(getResources().getString(R.string.d) + map.get("d"));
			TextView a = (TextView) convertView.findViewById(R.id.a);
			a.setText(getResources().getString(R.string.a) + map.get("a"));
			
			
			TextView handicat = (TextView) convertView.findViewById(R.id.handicap);
			String temp = map.get("handicap");
			if (temp == null ||"".equals(temp)|| "0".equals(temp)) {
				handicat.setVisibility(View.GONE);
			}else
			{
				handicat.setVisibility(View.VISIBLE);
				handicat.setText("(" + temp + ")");
			}
			View message = convertView.findViewById(R.id.message);
			temp = map.get("message");
			if (temp != null && !"".equals(temp)) {
				message.setVisibility(View.VISIBLE);
				message.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Toast.makeText(MatchShouzhuActivity.this, ""+map.get("message"), Toast.LENGTH_LONG).show();						
					}
				});
				
			}else {
				message.setVisibility(View.GONE);
			}
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MatchShouzhuActivity.this,
							ZuqiuMatchDetailActivity.class);
					intent.putExtra("m_id", map.get("m_id"));
					intent.putExtra("num", map.get("num"));
					intent.putExtra("l_cn", map.get("l_cn"));
					intent.putExtra("l_cn2", map.get("l_cn2"));
					intent.putExtra("l_id", map.get("l_id"));
					intent.putExtra("h_cn", map.get("h_cn"));
					intent.putExtra("h_id", map.get("h_id"));
					intent.putExtra("a_cn", map.get("a_cn"));
					intent.putExtra("a_id", map.get("a_id"));
					intent.putExtra("sstime", map.get("sstime").toString());
					intent.putExtra("goalline", map.get("handicap"));
					startActivity(intent);
				}
			});
			return convertView;
		}
	}

	@Override
	public void onBackPressed() {
		if (getParent() != null) {
			getParent().onBackPressed();
			
		}else
		{
			super.onBackPressed();
		}
		
	}
	
	@Override
	public void onClick(View arg0) {
		if (arg0 == tab_zuqiu) {
			tab_zuqiu.setBackgroundResource(R.drawable.tab_l_select);
			tab_lanqiu.setBackgroundResource(R.drawable.tab_l_normal);
			zuqiu_layout.setVisibility(View.VISIBLE);
			lanqiu_layout.setVisibility(View.GONE);
			if (zuqiuInit)
				new ZuqiuMatchListTask().execute();
		} else if (arg0 == tab_lanqiu) {
			tab_lanqiu.setBackgroundResource(R.drawable.tab_l_select);
			tab_zuqiu.setBackgroundResource(R.drawable.tab_l_normal);
			lanqiu_layout.setVisibility(View.VISIBLE);
			zuqiu_layout.setVisibility(View.GONE);
			if (lanqiuInit)
				new LanqiuMatchListTask().execute();
		}
	}
}
