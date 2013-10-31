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

/**本界面是复制MatchResultActivity界面，没有意义
 * 
 * 
 * 
 * @创建时间 2011-7-19 下午06:06:48
 * @创建人： 陆林
 * @邮箱：15366189868@189.cn
 */
public class CopyOfMatchResultActivity extends Activity implements
		OnClickListener {
	boolean zuqiuInit = true;
	/**
	 * start modify
	 */
	private View match_type_layout, b_match_type_layout;
	private TextView match_type_text, b_match_type_text;
	private int matchType = 0;
	private String[] mactchTypes;
	private int awardType = 0;
	/**
	 * end modify
	 */
	private TextView zuqiu_result_time_text, zuqiu_match_liansai_text,
			zuqiu_match_liansai_id_text;
	private View singleTab, zuqiu_result_layout, zuqiu_match_result_sel,
			zuqiu_time_layout;
	private boolean zuqiu_result_loading = false;
	private ProgressLinearLayout zuqiu_leagues_progress,
			zuqiu_result_progressbar;
	private List<Map<String, String>> zuqiuLeagues = new ArrayList<Map<String, String>>();
	private ListView zuqiu_result_list;
	private List<Map<String, String>> zuqiu_results = new ArrayList<Map<String, String>>();
	private SimpleAdapter zuqiu_result_adapter;

	boolean lanqiuInit = true;
	private TextView lanqiu_result_time_text, lanqiu_match_liansai_text,
			lanqiu_match_liansai_id_text;
	private View resultTab, lanqiu_result_layout, lanqiu_match_result_sel,
			lanqiu_time_layout;
	private boolean lanqiu_result_loading = false;
	private ProgressLinearLayout lanqiu_leagues_progress,
			lanqiu_result_progressbar;
	private List<Map<String, String>> lanqiuLeagues = new ArrayList<Map<String, String>>();
	private ListView lanqiu_result_list;
	private List<Map<String, String>> lanqiu_results = new ArrayList<Map<String, String>>();
	private SimpleAdapter lanqiu_result_adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.match_result);
		/**
		 * start modify
		 */
		b_match_type_text = (TextView) findViewById(R.id.b_match_type_text);
		b_match_type_layout = findViewById(R.id.b_match_type_layout);
		b_match_type_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(CopyOfMatchResultActivity.this)
						.setTitle(R.string.qxzls)
						.setItems(mactchTypes,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if (which == 0) {
											zuqiu_result_layout
													.setVisibility(View.VISIBLE);
											lanqiu_result_layout
													.setVisibility(View.GONE);
											match_type_text
													.setText(mactchTypes[0]);
											b_match_type_text.setText(""
													+ mactchTypes[0]);
											if (zuqiuInit && matchType != 0) {
												matchType = 0;
												new ZuqiuLeagueListTask()
														.execute();
											}
										} else if (which == 1) {
											lanqiu_result_layout
													.setVisibility(View.VISIBLE);
											zuqiu_result_layout
													.setVisibility(View.GONE);
											match_type_text.setText(""
													+ mactchTypes[1]);
											b_match_type_text.setText(""
													+ mactchTypes[1]);
											if (lanqiuInit && matchType != 1) {
												matchType = 1;
												new LanqiuLeagueListTask()
														.execute();
											}
										}
										/*
										 * Map<String, String> _map =
										 * zuqiuLeagues .get(a1);
										 * zuqiu_match_liansai_id_text
										 * .setText(_map.get("l_id"));
										 * zuqiu_match_liansai_text.setText(_map
										 * .get("l_cn"));
										 */
									}
								}).show();
			}
		});
		mactchTypes = new String[] { getString(R.string.str_zuqiu),
				getString(R.string.str_lanqiu) };
		match_type_layout = findViewById(R.id.match_type_layout);
		match_type_text = (TextView) findViewById(R.id.match_type_text);
		match_type_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(CopyOfMatchResultActivity.this)
						.setTitle(R.string.select_ball_type)
						.setItems(mactchTypes,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if (which == 0) {
											zuqiu_result_layout
													.setVisibility(View.VISIBLE);
											lanqiu_result_layout
													.setVisibility(View.GONE);
											match_type_text
													.setText(mactchTypes[0]);
											b_match_type_text
													.setText(mactchTypes[0]);
											matchType = 0;
											if (zuqiuInit) {
												new ZuqiuLeagueListTask()
														.execute();
											}
										} else if (which == 1) {
											lanqiu_result_layout
													.setVisibility(View.VISIBLE);
											zuqiu_result_layout
													.setVisibility(View.GONE);
											matchType = 1;
											match_type_text.setText(""
													+ mactchTypes[1]);
											b_match_type_text.setText(""
													+ mactchTypes[1]);
											if (lanqiuInit) {
												new LanqiuLeagueListTask()
														.execute();
											}
										}
										/*
										 * Map<String, String> _map =
										 * zuqiuLeagues .get(a1);
										 * zuqiu_match_liansai_id_text
										 * .setText(_map.get("l_id"));
										 * zuqiu_match_liansai_text.setText(_map
										 * .get("l_cn"));
										 */
									}
								}).show();

			}
		});

		/**
		 * end modify
		 * 
		 */
		singleTab = findViewById(R.id.single_tab);
		singleTab.setOnClickListener(this);
		zuqiu_result_list = (ListView) findViewById(R.id.zuqiu_match_result_list);
		zuqiu_result_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (awardType == 0) {
					Intent intent = new Intent(CopyOfMatchResultActivity.this,
							SingleAwardActvitiy.class);
					intent.putExtra("map",
							(HashMap<String, String>) zuqiu_results.get(arg2));
					startActivity(intent);
					// Toast.makeText(MatchResultActivity.this, "单固定奖金",
					// Toast.LENGTH_LONG).show();
				} else if (awardType == 1) {
					Map<String, String> _map = zuqiu_results.get(arg2);
					if (_map.get("status").indexOf("进行") >= 0) {
						Toast.makeText(getApplicationContext(),
								R.string.result_tip, Toast.LENGTH_SHORT).show();//进行中
					} else if (_map.get("status").indexOf("取消") >= 0) {
						Toast.makeText(getApplicationContext(),
								R.string.result_tip1, Toast.LENGTH_SHORT)//已取消
								.show();
					} else {
						Intent intent = new Intent(
								CopyOfMatchResultActivity.this,
								ZuqiuResultDetailActivity.class);
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
		zuqiu_result_list.setAdapter(zuqiu_result_adapter);
		zuqiu_result_layout = findViewById(R.id.zuqiu_match_result_layout);
		zuqiu_match_result_sel = findViewById(R.id.zuqiu_match_result_sel);
		zuqiu_result_time_text = (TextView) findViewById(R.id.zuqiu_match_result_time_text);
		zuqiu_result_time_text.setText(StringUtils.getTime());
		zuqiu_time_layout = findViewById(R.id.zuqiu_time_layout);
		zuqiu_time_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String[] values = zuqiu_result_time_text.getText().toString()
						.split("-");
				if (values.length != 3) {
					values = StringUtils.getTime().split("-");
				}
				new DatePickerDialog(CopyOfMatchResultActivity.this,
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
								/*
								 * if (matchType == 0) { zuqiuMatchResult();
								 * }else if (matchType == 1) {
								 * lanqiuMatchResult(); }
								 */
								zuqiuMatchResult();
							}

						}, Integer.parseInt(values[0]), Integer
								.parseInt(values[1]) - 1, Integer
								.parseInt(values[2])).show();
			}
		});
		zuqiu_match_liansai_id_text = (TextView) findViewById(R.id.zuqiu_match_liansai_id_text);
		zuqiu_match_liansai_text = (TextView) findViewById(R.id.zuqiu_match_liansai_text);
		zuqiu_match_liansai_text.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final String[] _strings = new String[zuqiuLeagues.size()];
				for (int i = 0; i < zuqiuLeagues.size(); i++) {
					Map<String, String> _map = zuqiuLeagues.get(i);
					_strings[i] = _map.get("l_cn");
				}
				new AlertDialog.Builder(CopyOfMatchResultActivity.this)
						.setTitle(R.string.qxzls)
						.setItems(_strings,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface a0,
											int a1) {
										Map<String, String> _map = zuqiuLeagues
												.get(a1);
										zuqiu_match_liansai_id_text
												.setText(_map.get("l_id"));
										zuqiu_match_liansai_text.setText(_map
												.get("l_cn"));
										zuqiuMatchResult();
									}
								}).show();
			}
		});
		zuqiu_leagues_progress = (ProgressLinearLayout) findViewById(R.id.zuqiu_leagues_progress);
		zuqiu_leagues_progress.initView();
		zuqiu_leagues_progress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new ZuqiuLeagueListTask().execute();
			}
		});
		/**
		 * modify start here to delete btn
		 */
		/*
		 * zuqiu_math_result_btn = findViewById(R.id.zuqiu_math_result_btn);
		 * zuqiu_math_result_btn.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { zuqiuMatchResult(); } });
		 */

		resultTab = findViewById(R.id.result_tab);
		resultTab.setOnClickListener(this);
		lanqiu_result_list = (ListView) findViewById(R.id.lanqiu_match_result_list);
		lanqiu_result_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (awardType == 0) {
					// Toast.makeText(MatchResultActivity.this, "单固定奖金",
					// Toast.LENGTH_LONG).show();
				} else if (awardType == 1) {
					Map<String, String> _map = lanqiu_results.get(arg2);
					if (_map.get("status").indexOf("进行") >= 0) {
						Toast.makeText(getApplicationContext(),
								R.string.result_tip, Toast.LENGTH_SHORT).show();
					} else if (_map.get("status").indexOf("取消") >= 0) {
						Toast.makeText(getApplicationContext(),
								R.string.result_tip1, Toast.LENGTH_SHORT)
								.show();
					} else {
						Intent intent = new Intent(
								CopyOfMatchResultActivity.this,
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
						"s3", "s4", "goaline" }, new int[] { R.id.num,
						R.id.l_cn, R.id.h_cn, R.id.status, R.id.date,
						R.id.a_cn, R.id.finaled, R.id.s1, R.id.s2, R.id.s3,
						R.id.s4, R.id.handicapText });
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
				new DatePickerDialog(CopyOfMatchResultActivity.this,
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
		lanqiu_match_liansai_text.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final String[] _strings = new String[lanqiuLeagues.size()];
				for (int i = 0; i < lanqiuLeagues.size(); i++) {
					Map<String, String> _map = lanqiuLeagues.get(i);
					_strings[i] = _map.get("t_cn");
				}
				new AlertDialog.Builder(CopyOfMatchResultActivity.this)
						.setTitle(R.string.qxznba)
						.setItems(_strings,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface a0,
											int a1) {
										Map<String, String> _map = lanqiuLeagues
												.get(a1);
										lanqiu_match_liansai_id_text
												.setText(_map.get("t_id"));
										lanqiu_match_liansai_text.setText(_map
												.get("t_cn"));
										lanqiuMatchResult();
									}
								}).show();
			}
		});
		lanqiu_leagues_progress = (ProgressLinearLayout) findViewById(R.id.lanqiu_leagues_progress);
		lanqiu_leagues_progress.initView();
		lanqiu_leagues_progress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new LanqiuLeagueListTask().execute();
			}
		});
		/*
		 * lanqiu_math_result_btn = findViewById(R.id.lanqiu_math_result_btn);
		 * lanqiu_math_result_btn.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { lanqiuMatchResult(); } });
		 */
		onClick(singleTab);
		soccerTask();
	}

	private void soccerTask() {
		zuqiu_result_layout.setVisibility(View.VISIBLE);
		lanqiu_result_layout.setVisibility(View.GONE);
		match_type_text.setText(mactchTypes[0]);
		matchType = 0;
		if (zuqiuInit) {
			new ZuqiuLeagueListTask().execute();
		}
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	class ZuqiuLeagueListTask extends AsyncTask<String, Integer, JSONBean> {
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
	}

	class LanqiuLeagueListTask extends AsyncTask<String, Integer, JSONBean> {
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
				map.put("t_cn", getResources().getString(R.string.djxznba));//全部球队
				lanqiuLeagues.add(map);
				lanqiuLeagues.addAll(result.bodys);
				lanqiuMatchResult();
			}
			super.onPostExecute(result);
		}
	}

	class ZuqiuMathResultTask extends AsyncTask<String, Integer, JSONBean> {
		@Override
		protected void onPreExecute() {
			zuqiu_result_loading = true;
			zuqiu_results.clear();
			zuqiu_result_progressbar.setVisibility(View.VISIBLE);
			zuqiu_result_progressbar.setVisibility(View.VISIBLE, View.VISIBLE,
					View.GONE, View.GONE);
			zuqiu_result_list.setVisibility(View.GONE);
			super.onPreExecute();
		}

		@Override
		protected JSONBean doInBackground(String... params) {
			if (params.length == 2) {
				return HttpServices.getMatchResultFB(params[0], params[0],
						params[1]);
			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONBean result) {
			zuqiu_result_loading = false;
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
					zuqiu_results.addAll(result.bodys);
					zuqiu_result_progressbar.setVisibility(View.GONE);
					zuqiu_result_list.setVisibility(View.VISIBLE);
				}
			}
			zuqiu_result_adapter.notifyDataSetChanged();
			super.onPostExecute(result);
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
			lanqiu_result_loading = false;
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
					lanqiu_results.addAll(result.bodys);
					lanqiu_result_progressbar.setVisibility(View.GONE);
					lanqiu_result_list.setVisibility(View.VISIBLE);
				}
			}
			zuqiu_result_adapter.notifyDataSetChanged();
			super.onPostExecute(result);
		}
	}

	private void zuqiuMatchResult() {
		if (zuqiu_result_loading)
			return;
		String time = zuqiu_result_time_text.getText().toString();
		new ZuqiuMathResultTask().execute(new String[] { time,
				zuqiu_match_liansai_id_text.getText().toString() });
	}

	private void lanqiuMatchResult() {
		if (lanqiu_result_loading)
			return;
		String time = lanqiu_result_time_text.getText().toString();
		new LanqiuMathResultTask().execute(new String[] { time,
				lanqiu_match_liansai_id_text.getText().toString() });
	}

	@Override
	public void onClick(View arg0) {
		if (arg0 == singleTab) {
			singleTab.setBackgroundResource(R.drawable.tab_l_select);
			resultTab.setBackgroundResource(R.drawable.tab_l_normal);
			/*
			 * if (zuqiuInit) new ZuqiuLeagueListTask().execute();
			 */
			awardType = 0;
		} else if (arg0 == resultTab) {
			resultTab.setBackgroundResource(R.drawable.tab_l_select);
			singleTab.setBackgroundResource(R.drawable.tab_l_normal);
			awardType = 1;
		}
	}
}
