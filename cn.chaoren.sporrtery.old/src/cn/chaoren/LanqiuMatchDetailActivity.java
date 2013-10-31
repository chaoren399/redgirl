package cn.chaoren;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import cn.chaoren.adapter.FeedBackItemAdapterBB;
import cn.chaoren.net.HeaderJSONBean;
import cn.chaoren.net.HttpServiceNew;
import cn.chaoren.net.HttpServices;
import cn.chaoren.net.JSONBean;
import cn.chaoren.net.JSONBeanPlayBK;
import cn.chaoren.net.KeyMapBean;
import cn.chaoren.net.KeyMapBean.KeyMap;
import cn.chaoren.view.ProgressLinearLayout;
import cn.chaoren.R;

import com.mobclick.android.MobclickAgent;

/**
 * 类说明：比赛详情介绍页面
 * 
 * @创建时间 2011-6-2 下午01:50:11
 * @创建人： 陆林
 * @邮箱：15301586841@189.cn
 */
public class LanqiuMatchDetailActivity extends Activity implements
		OnClickListener {
	boolean backList = false;
	boolean playInit = true;
	ExPlayAdapter playListAdapter;
	ExpandableListView playList;
	List<Map<String, String>> playGroupData = new ArrayList<Map<String, String>>();
	List<Map<String, Object>> playChildData = new ArrayList<Map<String, Object>>();
	ProgressLinearLayout playProgressbar;

	boolean analyzeInit = true;
	ExAnalyzeAdapter analyzeListAdapter;
	ExpandableListView analyzeList;
	List<Map<String, String>> analyzeGroupData = new ArrayList<Map<String, String>>();
	List<List<Map<String, String>>> analyzeChildData = new ArrayList<List<Map<String, String>>>();
	ProgressLinearLayout analyzeProgressbar;

	boolean feedbackInit = true;
	ListView feedbackList;
	View feedbackLayout, feedbackView;
	// private ArrayList<String[]> dataList = new ArrayList<String[]>();

	private HashMap<String, KeyMap> dataList = new HashMap<String, KeyMap>();

	TextView handicapSelect, leagulSelect, queryBtn, resetBtn, similarQureyBtn,
			homeSelect, awaySelect;// FIXME

	private Spinner handicapSpinner, leagulSpinner, homeSpinner, awaySpinner,
			hoSpinner, aoSpinner;
	private String handicapSelection, leagulSelection, homeSelection,
			awaySelection;
	private ArrayAdapter<String> handicapAdapter, leagulAdapter, homeAdapter,
			awayAdapter, hoAdapter, aoAdapter;
	private FeedBackItemAdapterBB feedAdapter;

	private ArrayList<Map<String, String>> feedBackData = new ArrayList<Map<String, String>>();
	ProgressLinearLayout feedBackProgressbar, feedBackPreProgress;

	private Map<String, String> feedBackHeader = new HashMap<String, String>();

	Button play, analyze, europe, asia, feedback;
	String m_id, num, l_cn, l_id, h_cn, h_id, a_cn, a_id, sstime;
	TextView num_text, l_cn_text, h_cn_text, a_cn_text, sstime_text;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.match_detail);
		View view = findViewById(R.id.europeLayout);
		view.setVisibility(View.GONE);
		view = findViewById(R.id.asiaLayout);
		view.setVisibility(View.GONE);
		view = findViewById(R.id.line1Layout);
		view.setVisibility(View.GONE);
		view = findViewById(R.id.line2Layout);
		view.setVisibility(View.GONE);

		view = findViewById(R.id.selectAoLayout);
		view.setVisibility(View.VISIBLE);
		view = findViewById(R.id.selectHoLayout);
		view.setVisibility(View.VISIBLE);
		view = findViewById(R.id.similar_query);
		view.setVisibility(View.GONE);// FIXME
	// ,添加时，view.setVisibility(View.VISIBLE);
		m_id = getIntent().getStringExtra("m_id");
		num = getIntent().getStringExtra("num");
		l_cn = getIntent().getStringExtra("l_cn");
		l_id = getIntent().getStringExtra("l_id");
		h_cn = getIntent().getStringExtra("h_cn");
		h_id = getIntent().getStringExtra("h_id");
		a_cn = getIntent().getStringExtra("a_cn");
		a_id = getIntent().getStringExtra("a_id");
		sstime = getIntent().getStringExtra("sstime");

		findViewById(R.id.team1).setBackgroundResource(R.drawable.team_04);
		findViewById(R.id.team2).setBackgroundResource(R.drawable.team_03);

		num_text = (TextView) findViewById(R.id.num_text);
		num_text.setText(num);
		l_cn_text = (TextView) findViewById(R.id.l_cn_text);
		l_cn_text.setText(l_cn);
		h_cn_text = (TextView) findViewById(R.id.h_cn_text);
		h_cn_text.setText(a_cn);
		a_cn_text = (TextView) findViewById(R.id.a_cn_text);
		a_cn_text.setText(h_cn);
		sstime_text = (TextView) findViewById(R.id.sstime_text);
		sstime_text.setText(sstime);

		play = (Button) findViewById(R.id.play);
		play.setOnClickListener(this);
		play.setSelected(true);
		analyze = (Button) findViewById(R.id.analyze);
		analyze.setOnClickListener(this);
		/*
		 * europe = (Button) findViewById(R.id.europe);
		 * europe.setOnClickListener(this);
		 * europe.setVisibility(View.INVISIBLE); asia = (Button)
		 * findViewById(R.id.asia); asia.setOnClickListener(this);
		 * asia.setVisibility(View.INVISIBLE);
		 */
		feedback = (Button) findViewById(R.id.feedback);
		feedback.setOnClickListener(this);
		playListAdapter = new ExPlayAdapter();
		playList = (ExpandableListView) findViewById(R.id.play_list);
		playList.setOnGroupExpandListener(new OnGroupExpandListener() {
			@Override
			public void onGroupExpand(int arg0) {
				for (int i = 0; i < playListAdapter.getGroupCount(); i++) {
					if (arg0 != i) {
						playList.collapseGroup(i);
					}
				}
			}
		});
		playProgressbar = (ProgressLinearLayout) getLayoutInflater().inflate(
				R.layout.progress_small, null);
		playProgressbar.initView();
		playProgressbar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				playProgressbar.setVisibility(View.VISIBLE, View.VISIBLE,
						View.GONE, View.GONE);
				new LanqiuPlayTask().execute();
			}
		});
		playList.addFooterView(playProgressbar, null, false);
		playList.setAdapter(playListAdapter);
		playList.setGroupIndicator(null);
		playList.setDivider(null);

		analyzeListAdapter = new ExAnalyzeAdapter();
		analyzeList = (ExpandableListView) findViewById(R.id.analyze_list);
		analyzeList.setOnGroupExpandListener(new OnGroupExpandListener() {
			@Override
			public void onGroupExpand(int arg0) {
				for (int i = 0; i < analyzeListAdapter.getGroupCount(); i++) {
					if (arg0 != i) {
						analyzeList.collapseGroup(i);
					}
				}
			}
		});
		analyzeProgressbar = (ProgressLinearLayout) getLayoutInflater()
				.inflate(R.layout.progress_small, null);
		analyzeProgressbar.initView();
		analyzeProgressbar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				analyzeProgressbar.setVisibility(View.VISIBLE, View.VISIBLE,
						View.GONE, View.GONE);
				new LanqiuAnalyzeTask().execute();
			}
		});
		analyzeList.addFooterView(analyzeProgressbar, null, false);
		analyzeList.setAdapter(analyzeListAdapter);
		analyzeList.setGroupIndicator(null);
		analyzeList.setDivider(null);

		homeSpinner = (Spinner) findViewById(R.id.selectHomes);
		awaySpinner = (Spinner) findViewById(R.id.selectAways);
		// handicapSpinner = (Spinner) findViewById(R.id.selectHandicap_bb);
		hoSpinner = (Spinner) findViewById(R.id.selectHo);
		aoSpinner = (Spinner) findViewById(R.id.selectAo);
		leagulSpinner = (Spinner) findViewById(R.id.selectLeguals);

		homeAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, new ArrayList<String>());
		homeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		awayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, new ArrayList<String>());
		awayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		leagulAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, new ArrayList<String>());
		leagulAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		/*
		 * handicapAdapter = new ArrayAdapter<String>(this,
		 * android.R.layout.simple_spinner_item, new ArrayList<String>());
		 * handicapAdapter
		 * .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item
		 * );
		 */
		hoAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, new ArrayList<String>());
		hoAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		aoAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, new ArrayList<String>());
		aoAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		homeSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						homeSelection = homeAdapter.getItem(position);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}

				});

		homeSpinner.setAdapter(homeAdapter);
		awaySpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						awaySelection = awayAdapter.getItem(position);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}

				});
		awaySpinner.setAdapter(awayAdapter);

		hoSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						dataList.get("hlist").select(position);
						new RefreshHDATask().execute("");
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}

				});

		aoSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// homeSelection = homeAdapter.getItem(position);
						dataList.get("alist").select(position);
						new RefreshHDATask().execute("");
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}

				});

		hoSpinner.setAdapter(hoAdapter);
		aoSpinner.setAdapter(aoAdapter);

		leagulSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						leagulSelection = leagulAdapter.getItem(position);
						dataList.get("llist").select(position);
						new RefreshTeamTask().execute("");
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}

				});
		leagulSpinner.setAdapter(leagulAdapter);
		queryBtn = (TextView) findViewById(R.id.query);
		similarQureyBtn = (TextView) findViewById(R.id.similar_query);

		feedbackView = findViewById(R.id.feedbackResult);
		// TODO
		feedbackList = (ListView) findViewById(R.id.feedbackResultList);
		feedAdapter = new FeedBackItemAdapterBB(this);
		feedAdapter.setDataList(feedBackData);
		/*
		 * feedBackPreProgress =(ProgressLinearLayout)
		 * getLayoutInflater().inflate( R.layout.progress_small, null);
		 * feedBackPreProgress.initView();
		 */
		feedBackProgressbar = (ProgressLinearLayout) getLayoutInflater()
				.inflate(R.layout.progress_small, null);
		feedBackProgressbar.initView();
		feedBackProgressbar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				feedBackProgressbar.setVisibility(View.VISIBLE, View.VISIBLE,
						View.GONE, View.GONE);
				new LanqiuFeedBackPreTask().execute();
			}
		});
		feedbackList.addFooterView(feedBackProgressbar, null, false);
		feedbackList.setAdapter(feedAdapter);
		/*
		 * feedbackList.setOnItemClickListener(new
		 * AdapterView.OnItemClickListener() { public void
		 * onItemClick(AdapterView<?> parent, View view, int position, long id)
		 * { Intent intent = new Intent(LanqiuMatchDetailActivity.this,
		 * FeedBackDetailActiviy.class); intent.putExtra("type", 2);
		 * intent.putExtra("map", (HashMap<String,
		 * String>)feedBackData.get(position)); startActivity(intent); } });
		 */
		feedbackLayout = findViewById(R.id.feedbackLayout);
		onClick(play);
	}

	public class RefreshHDATask extends
			AsyncTask<String, Integer, HashMap<String, KeyMap>> {

		@Override
		protected HashMap<String, KeyMap> doInBackground(String... params) {
			HttpServiceNew serviceNew = new HttpServiceNew();
			HashMap<String, KeyMap> keyMap = new HashMap<String, KeyMap>();
			serviceNew.getSearchHDASelectionsBB(getString(R.string.all),
					dataList.get("hlist").selectValue,
					dataList.get("alist").selectValue, keyMap);
			return keyMap;
		}

		@Override
		protected void onPostExecute(HashMap<String, KeyMap> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dataList.put("hlist", result.get("hlist"));
			dataList.put("alist", result.get("alist"));
			String[] array = null;
			hoAdapter.clear();
			array = dataList.get("hlist").keys;
			for (int i = 0; i < array.length; i++) {
				hoAdapter.add(array[i]);
			}

			aoAdapter.clear();
			array = dataList.get("alist").keys;
			for (int i = 0; i < array.length; i++) {
				aoAdapter.add(array[i]);
			}

		}
	}

	public class RefreshTeamTask extends AsyncTask<String, Integer, KeyMap> {

		@Override
		protected KeyMap doInBackground(String... params) {
			HttpServiceNew serviceNew = new HttpServiceNew();
			String select =  dataList.get("llist").selectValue;
			KeyMap keyMap = null;
			if (select != null && !"".equals(select)) {
				keyMap = serviceNew.getSearchLListSelectionsBB(
						getString(R.string.all), select);
			}else
			{
				keyMap = new KeyMap();
			}
			

			return keyMap;
		}

		@Override
		protected void onPostExecute(KeyMap result) {

			super.onPostExecute(result);
			dataList.put("hteam", result);
			dataList.put("ateam", result.clone());
			homeAdapter.clear();
			String[] array = null;
			array = dataList.get("hteam").keys;

			if (array != null) {
				for (int i = 0; i < array.length; i++) {
					homeAdapter.add(array[i]);
				}
			}
			awayAdapter.clear();
			array = dataList.get("ateam").keys;
			if (array != null) {
				for (int i = 0; i < array.length; i++) {
					awayAdapter.add(array[i]);
				}
			}
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

	class LanqiuFeedBackPreTask extends AsyncTask<String, Integer, KeyMapBean> {
		public LanqiuFeedBackPreTask() {
			feedbackInit = false;
		}

		@Override
		protected KeyMapBean doInBackground(String... params) {
			
			HttpServiceNew serviceNew = new HttpServiceNew();
			return serviceNew.getSelectionBB(getString(R.string.all));
		}

		@Override
		protected void onPostExecute(KeyMapBean result) {
			if (result == null) {
				// feedbackLayout.setVisibility(View.VISIBLE);
			} else {
				dataList.clear();
				HashMap<String, KeyMap> keyList = result.keyList;
				if (keyList.size() > 0) {

					// dataList.addAll(result.dataList);
					dataList.put("hlist", keyList.get("hlist"));
					dataList.put("alist", keyList.get("alist"));
					dataList.put("llist", keyList.get("llist"));
					dataList.put("hteam", keyList.get("hteam"));
					dataList.put("ateam", keyList.get("ateam"));
					String[] array = null;
					hoAdapter.clear();
					array = dataList.get("hlist").keys;
					if (array != null) {
						for (int i = 0; i < array.length; i++) {
							hoAdapter.add(array[i]);
						}
					}

					aoAdapter.clear();
					array = dataList.get("alist").keys;
					if (array != null) {
						for (int i = 0; i < array.length; i++) {
							aoAdapter.add(array[i]);
						}
					}
					leagulAdapter.clear();
					array = dataList.get("llist").keys;
					if (array != null) {
						for (int i = 0; i < array.length; i++) {
							leagulAdapter.add(array[i]);
						}
					}
					homeAdapter.clear();
					array = dataList.get("hteam").keys;
					if (array != null) {
						for (int i = 0; i < array.length; i++) {
							homeAdapter.add(array[i]);
						}
					}
					awayAdapter.clear();
					array = dataList.get("ateam").keys;
					if (array != null) {
						for (int i = 0; i < array.length; i++) {
							awayAdapter.add(array[i]);
						}
					}
					feedbackLayout.setVisibility(View.VISIBLE);
					feedbackList.removeFooterView(feedBackProgressbar);
				} else {
					feedBackProgressbar.setVisibility(View.GONE, View.GONE,
							View.VISIBLE, View.GONE);
					feedBackProgressbar.setText(null,
							getResources().getString(R.string.tip5));
					/*
					 * europeProgressbar.setVisibility(View.GONE, View.GONE,
					 * View.VISIBLE, View.GONE); europeProgressbar.setText(null,
					 * getResources().getString(R.string.tip1));
					 */
				}
			}
			super.onPostExecute(result);
		}
	}

	class LanqiuFeedBackTask extends AsyncTask<String, Integer, HeaderJSONBean> {
		/*	*//**
		 * date home away handicap homeWin homeLose result legual status
		 */
		/*
		 * List<Map<String, String>> mapList = ms.bodys; Map<String, String> map
		 * = null;
		 * 
		 * map = new HashMap<String, String>(); mapList.add(map);
		 * map.put("date", "2012-01-12 08:45:25"); map.put("home", "杜伊斯堡");
		 * map.put("away", "杜赛尔多夫"); map.put("handicap", "+1"); map.put("h",
		 * "2.8"); map.put("d", "3.6"); map.put("a", "2.05"); map.put("result",
		 * "0:1"); map.put("legual", "德乙"); map.put("status", "变化中奖金");
		 */
		@Override
		protected HeaderJSONBean doInBackground(String... params) {
			
			/*JSONBean ms = new JSONBean("0");
			List<Map<String, String>> datas = ms.bodys;
			Map<String, String> map = null;
			map = new HashMap<String, String>();
			datas.add(map);
			map.put("date", "2012-01-12 08:45:25");
			map.put("home", "杜伊斯堡");
			map.put("away", "杜赛尔多夫");
			map.put("handicap", "+1");
			map.put("result", "0:1");
			map.put("legual", "德乙");
			map.put("status", "变化中奖金");
			map.put("homeWin", "2.05");
			map.put("homeLose", "3.8");

			map = new HashMap<String, String>();
			datas.add(map);
			map.put("date", "2012-01-10 08:45:25");
			map.put("home", "湖人");
			map.put("away", "热火");
			map.put("handicap", "+1");
			map.put("result", "0:1");
			map.put("legual", "nba");
			map.put("status", "变化中奖金");
			map.put("homeWin", "2.05");
			map.put("homeLose", "3.8");

			map = new HashMap<String, String>();
			datas.add(map);
			map.put("date", "2012-01-13 08:45:25");
			map.put("home", "湖人");
			map.put("away", "热火");
			map.put("handicap", "+1");
			map.put("result", "0:1");
			map.put("legual", "nba");
			map.put("status", "变化中奖金");
			map.put("homeWin", "2.05");
			map.put("homeLose", "3.8");*/
			HttpServiceNew serviceNew = new HttpServiceNew();
			return serviceNew.getFeedBackBB(dataList.get("hteam").selectValue, dataList.get("ateam").selectValue, dataList.get("llist").selectValue, null, dataList.get("hlist").selectValue, dataList.get("alist").selectValue, null);
		}

		@Override
		protected void onPostExecute(HeaderJSONBean result) {
			if (result == null) {
				// feedbackLayout.setVisibility(View.VISIBLE);
			} else {
				feedBackData.clear();
				if (result.bodys.size() > 0) {

					feedBackData.addAll(result.bodys);
					feedbackLayout.setVisibility(View.GONE);
					feedbackList.removeFooterView(feedBackProgressbar);
					feedAdapter.notifyDataSetChanged();
					
				} else {
					feedBackProgressbar.setVisibility(View.GONE, View.GONE,
							View.VISIBLE, View.GONE);
					feedBackProgressbar.setText(null,
							getResources().getString(R.string.tip5));
					/*
					 * europeProgressbar.setVisibility(View.GONE, View.GONE,
					 * View.VISIBLE, View.GONE); europeProgressbar.setText(null,
					 * getResources().getString(R.string.tip1));
					 */
				}
				feedBackHeader = result.header;
				initResultTexts(feedbackView, result.header);
			}
			super.onPostExecute(result);
		}
	}

	private int parseInt(String str) {
		int result = 0;
		try {
			result = Integer.parseInt(str);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	private void initResultTexts(View parent, Map<String, String> map) {
		if (map == null) {
			return;
		}
		TextView text = (TextView) parent.findViewById(R.id.resultHeader);
		StringBuilder sb = new StringBuilder();
		sb.append("共");
		String temp = map.get("total");
		
		System.out.println("total-" + temp);
		int total = parseInt(temp);
		sb.append(total + "场比赛符合条件:\n其中主队   胜");
		
		if (total != 0) {
			temp = map.get("h");
			System.out.println("h-" + temp);
			int h = parseInt(temp);
			sb.append(h).append(" 场[").append(h * 100 / total).append("%]   负");
			temp = map.get("a");
			System.out.println("a-" + temp);
			int a = parseInt(temp);
			sb.append(a).append(" 场[").append(a * 100 / total).append("%]");
			text.setText(sb.toString());
		}else
		{
			text.setText("共0场比赛符合条件");
		}
		
		

		
	}

	
	class LanqiuPlayTask extends AsyncTask<String, Integer, JSONBeanPlayBK> {
		public LanqiuPlayTask() {
			playInit = false;
		}

		@Override
		protected JSONBeanPlayBK doInBackground(String... params) {
			try {
				return HttpServices.getPlayBK(m_id);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(JSONBeanPlayBK result) {
			analyzeChildData.clear();
			analyzeGroupData.clear();
			if (result == null) {
				playProgressbar.setVisibility(View.GONE, View.GONE,
						View.VISIBLE, View.VISIBLE);
			} else {
				// 胜负
				Map<String, String> curGroupMap = new HashMap<String, String>();
				curGroupMap.put("text", getResources()
						.getString(R.string.wf_sf));
				curGroupMap.put("type", "sf");
				Map<String, Object> children = new HashMap<String, Object>();
				if (result.mnl.size() > 0) {
					for (int i = 0; i < result.mnl.size(); i++) {
						List<String> l = result.mnl.get(i);
						children.put(l.get(0), l.get(1));
					}
					playChildData.add(children);
					playGroupData.add(curGroupMap);
				}

				// 让分胜负
				curGroupMap = new HashMap<String, String>();
				curGroupMap.put("text",
						getResources().getString(R.string.wf_rfsf)
								+ "("
								+ getResources().getString(R.string.wf_rf)
								+ ":"
								+ (result.goaline == null ? "0"
										: result.goaline) + ")");
				curGroupMap.put("type", "rfsf");
				children = new HashMap<String, Object>();
				if (result.hdc.size() > 0) {
					for (int i = 0; i < result.hdc.size(); i++) {
						List<String> l = result.hdc.get(i);
						children.put(l.get(0), l.get(1));
					}
					playChildData.add(children);
					playGroupData.add(curGroupMap);
				}

				curGroupMap = new HashMap<String, String>();
				curGroupMap.put("text",
						getResources().getString(R.string.wf_sfc));
				curGroupMap.put("type", "sfc");
				children = new HashMap<String, Object>();
				// 胜分差
				if (result.wnm.size() > 0) {
					for (int i = 0; i < result.wnm.size(); i++) {
						List<String> l = result.wnm.get(i);
						children.put("" + i, l);
					}
					playChildData.add(children);
					playGroupData.add(curGroupMap);
				}

				// 大小分
				curGroupMap = new HashMap<String, String>();
				curGroupMap.put("text",
						getResources().getString(R.string.wf_dxf));
				curGroupMap.put("type", "dxf");
				children = new HashMap<String, Object>();
				if (result.hilo.size() > 0) {
					for (int i = 0; i < result.hilo.size(); i++) {
						List<String> l = result.hilo.get(i);
						children.put(l.get(0), l.get(1));
					}
					playChildData.add(children);
					playGroupData.add(curGroupMap);
				}

				if (playList.getFooterViewsCount() > 0) {
					playList.removeFooterView(playProgressbar);
				}
				playListAdapter.notifyDataSetChanged();
			}
			super.onPostExecute(result);
		}
	}

	class LanqiuAnalyzeTask extends AsyncTask<String, Integer, JSONBean[]> {
		public LanqiuAnalyzeTask() {
			analyzeInit = false;
		}

		@Override
		protected JSONBean[] doInBackground(String... params) {
			try {
				JSONBean[] result = new JSONBean[3];
				// 近期对阵
				result[0] = HttpServices.getTwoPrevBK(h_id, a_id);
				// 客队近期10场
				result[1] = HttpServices.getMatchInfoBK(a_id, 10);
				// 主队近期10场
				result[2] = HttpServices.getMatchInfoBK(h_id, 10);
				int size = 0;
				for (int i = 0; i < result.length; i++) {
					if (result[i] == null)
						size++;
				}
				if (size == result.length)
					return null;
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(JSONBean[] result) {
			analyzeChildData.clear();
			analyzeGroupData.clear();
			if (result == null) {
				analyzeProgressbar.setVisibility(View.GONE, View.GONE,
						View.VISIBLE, View.VISIBLE);
			} else {
				Map<String, String> curGroupMap = new HashMap<String, String>();
				curGroupMap.put("text",
						getResources().getString(R.string.fx_ldwj));
				curGroupMap.put("type", "ldwj");
				if (result[0] != null) {
					analyzeChildData.add(result[0].bodys);
					analyzeGroupData.add(curGroupMap);
				}

				curGroupMap = new HashMap<String, String>();
				curGroupMap.put("text",
						getResources().getString(R.string.fx_kdjq));
				curGroupMap.put("type", "kdjq");
				if (result[1] != null) {
					analyzeChildData.add(result[1].bodys);
					analyzeGroupData.add(curGroupMap);
				}

				curGroupMap = new HashMap<String, String>();
				curGroupMap.put("text",
						getResources().getString(R.string.fx_zdjq));
				curGroupMap.put("type", "zdjq");
				if (result[2] != null) {
					analyzeChildData.add(result[2].bodys);
					analyzeGroupData.add(curGroupMap);
				}

				if (analyzeList.getFooterViewsCount() > 0) {
					analyzeList.removeFooterView(analyzeProgressbar);
				}
				analyzeListAdapter.notifyDataSetChanged();
			}
			super.onPostExecute(result);
		}
	}

	class ExPlayAdapter extends BaseExpandableListAdapter {
		LayoutInflater inflater;

		public ExPlayAdapter() {
			inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				view = inflater.inflate(R.layout.match_detail_list_group_item,
						null);
			}
			TextView text = (TextView) view.findViewById(R.id.text);
			text.setText(playGroupData.get(groupPosition).get("text"));
			ImageView image = (ImageView) view.findViewById(R.id.icon);
			if (isExpanded)
				image.setBackgroundResource(R.drawable.arrow_select);
			else
				image.setBackgroundResource(R.drawable.arrow_normal);
			return view;
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		public Object getGroup(int groupPosition) {
			return playGroupData.get(groupPosition);
		}

		public int getGroupCount() {
			return playGroupData.size();
		}

		@SuppressWarnings("unchecked")
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			Map<String, String> map = playGroupData.get(groupPosition);
			if (map.get("type").equals("sf")) {
				convertView = inflater.inflate(
						R.layout.lanqiu_play_mnl_list_child_item, null);
				if (playChildData.get(groupPosition).size() > 0) {
					TextView h = (TextView) convertView.findViewById(R.id.h);
					h.setText(playChildData.get(groupPosition).get("h")
							.toString());
					TextView a = (TextView) convertView.findViewById(R.id.a);
					a.setText(playChildData.get(groupPosition).get("a")
							.toString());
				}
			} else if (map.get("type").equals("rfsf")) {
				convertView = inflater.inflate(
						R.layout.lanqiu_play_hdc_list_child_item, null);
				if (playChildData.get(groupPosition).size() > 0) {
					TextView h = (TextView) convertView.findViewById(R.id.h);
					h.setText(playChildData.get(groupPosition).get("h")
							.toString());
					TextView a = (TextView) convertView.findViewById(R.id.a);
					a.setText(playChildData.get(groupPosition).get("a")
							.toString());
				}
			} else if (map.get("type").equals("sfc")) {
				convertView = inflater.inflate(
						R.layout.lanqiu_play_wnm_list_child_item, null);
				if (playChildData.get(groupPosition).size() > 0) {
					List<String> list = (List<String>) playChildData.get(
							groupPosition).get("0");
					TextView w1 = (TextView) convertView.findViewById(R.id.w1);
					w1.setText(list.get(1));
					TextView wa1 = (TextView) convertView
							.findViewById(R.id.wa1);
					wa1.setText(list.get(2));

					list = (List<String>) playChildData.get(groupPosition).get(
							"1");
					TextView w2 = (TextView) convertView.findViewById(R.id.w2);
					w2.setText(list.get(1));
					TextView wa2 = (TextView) convertView
							.findViewById(R.id.wa2);
					wa2.setText(list.get(2));

					list = (List<String>) playChildData.get(groupPosition).get(
							"2");
					TextView w3 = (TextView) convertView.findViewById(R.id.w3);
					w3.setText(list.get(1));
					TextView wa3 = (TextView) convertView
							.findViewById(R.id.wa3);
					wa3.setText(list.get(2));

					list = (List<String>) playChildData.get(groupPosition).get(
							"3");
					TextView w4 = (TextView) convertView.findViewById(R.id.w4);
					w4.setText(list.get(1));
					TextView wa4 = (TextView) convertView
							.findViewById(R.id.wa4);
					wa4.setText(list.get(2));

					list = (List<String>) playChildData.get(groupPosition).get(
							"4");
					TextView w5 = (TextView) convertView.findViewById(R.id.w5);
					w5.setText(list.get(1));
					TextView wa5 = (TextView) convertView
							.findViewById(R.id.wa5);
					wa5.setText(list.get(2));

					list = (List<String>) playChildData.get(groupPosition).get(
							"5");
					TextView w6 = (TextView) convertView.findViewById(R.id.w6);
					w6.setText(list.get(1));
					TextView wa6 = (TextView) convertView
							.findViewById(R.id.wa6);
					wa6.setText(list.get(2));
				}
			} else if (map.get("type").equals("dxf")) {
				convertView = inflater.inflate(
						R.layout.lanqiu_play_hilo_list_child_item, null);
				if (playChildData.get(groupPosition).size() > 0) {
					TextView g = (TextView) convertView.findViewById(R.id.g);
					g.setText(playChildData.get(groupPosition).get("g")
							.toString());
					TextView h = (TextView) convertView.findViewById(R.id.h);
					h.setText(playChildData.get(groupPosition).get("h")
							.toString());
					TextView l = (TextView) convertView.findViewById(R.id.l);
					l.setText(playChildData.get(groupPosition).get("l")
							.toString());
				}
			}
			return convertView;
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public Object getChild(int groupPosition, int childPosition) {
			return playChildData.get(groupPosition);
		}

		public int getChildrenCount(int groupPosition) {
			return 1;
		}

		public boolean hasStableIds() {
			return true;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}

	class ExAnalyzeAdapter extends BaseExpandableListAdapter {
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.match_detail_list_group_item,
						null);
			}
			TextView text = (TextView) view.findViewById(R.id.text);
			text.setText(analyzeGroupData.get(groupPosition).get("text"));
			ImageView image = (ImageView) view.findViewById(R.id.icon);
			if (isExpanded)
				image.setBackgroundResource(R.drawable.arrow_select);
			else
				image.setBackgroundResource(R.drawable.arrow_normal);
			return view;
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		public Object getGroup(int groupPosition) {
			return analyzeGroupData.get(groupPosition);
		}

		public int getGroupCount() {
			return analyzeGroupData.size();

		}

		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			Map<String, String> titleMap = analyzeGroupData.get(groupPosition);
			if (titleMap.get("type").equals("ldwj")) {
				LinearLayout layout = (LinearLayout) inflater.inflate(
						R.layout.lanqiu_analyze_ldwjdzjcjjhsg, null);
				convertView = layout;
				List<Map<String, String>> list = analyzeChildData
						.get(groupPosition);
				if (list != null) {
					for (int i = 0; i < list.size(); i++) {
						View view = inflater.inflate(
								R.layout.lanqiu_analyze_ldwjdzjcjjhsg_item,
								null);
						Map<String, String> map = list.get(i);
						((TextView) view.findViewById(R.id.date)).setText(map
								.get("date"));
						((TextView) view.findViewById(R.id.h_cn)).setText(map
								.get("h_cn"));
						((TextView) view.findViewById(R.id.a_cn)).setText(map
								.get("a_cn"));
						((TextView) view.findViewById(R.id.a1)).setText(map
								.get("a1"));
						((TextView) view.findViewById(R.id.a2)).setText(map
								.get("a2"));
						((TextView) view.findViewById(R.id.a3)).setText(map
								.get("a3"));
						((TextView) view.findViewById(R.id.a4)).setText(map
								.get("a4"));
						((TextView) view.findViewById(R.id.af)).setText(map
								.get("af"));
						((TextView) view.findViewById(R.id.h1)).setText(map
								.get("h1"));
						((TextView) view.findViewById(R.id.h2)).setText(map
								.get("h2"));
						((TextView) view.findViewById(R.id.h3)).setText(map
								.get("h3"));
						((TextView) view.findViewById(R.id.h4)).setText(map
								.get("h4"));
						((TextView) view.findViewById(R.id.hf)).setText(map
								.get("hf"));
						((TextView) view.findViewById(R.id.mnl)).setText(map
								.get("mnl"));
						((TextView) view.findViewById(R.id.hdc)).setText(map
								.get("hdc"));
						((TextView) view.findViewById(R.id.wnm)).setText(map
								.get("wnm"));
						((TextView) view.findViewById(R.id.hilo)).setText(map
								.get("hilo"));
						layout.addView(view);
					}
				}
			} else if (titleMap.get("type").equals("zdjq")
					|| titleMap.get("type").equals("kdjq")) {
				convertView = inflater.inflate(R.layout.lanqiu_analyze_jqzj,
						null);
				LinearLayout table_layout = (LinearLayout) convertView
						.findViewById(R.id.table);

				List<Map<String, String>> list = analyzeChildData
						.get(groupPosition);
				if (list != null) {
					for (int i = 0; i < list.size(); i++) {
						TableLayout table_item = (TableLayout) inflater
								.inflate(R.layout.lanqiu_analyze_jqzj_item,
										null);
						Map<String, String> map = list.get(i);
						((TextView) table_item.findViewById(R.id.l_cn))
								.setText(map.get("l_cn"));
						((TextView) table_item.findViewById(R.id.date))
								.setText(map.get("date"));
						((TextView) table_item.findViewById(R.id.h_cn))
								.setText(map.get("h_cn"));
						((TextView) table_item.findViewById(R.id.a_cn))
								.setText(map.get("a_cn"));
						((TextView) table_item.findViewById(R.id.h_s))
								.setText(map.get("h_s"));
						((TextView) table_item.findViewById(R.id.a_s))
								.setText(map.get("a_s"));
						((TextView) table_item.findViewById(R.id.hhad))
								.setText(map.get("hhad"));
						((TextView) table_item.findViewById(R.id.hilo))
								.setText(map.get("hilo"));
						table_layout.addView(table_item);
					}
				}
			}
			return convertView;
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public Object getChild(int groupPosition, int childPosition) {
			return analyzeChildData.get(groupPosition);
		}

		public int getChildrenCount(int groupPosition) {
			return 1;
		}

		public boolean hasStableIds() {
			return true;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}

	@Override
	public void onBackPressed() {
		if (backList) {
			backList = false;
			feedbackLayout.setVisibility(View.VISIBLE);
			feedbackView.setVisibility(View.GONE);
		}else
		{
			super.onBackPressed();
		}
		
	}
	
	public void feedBackOnClick(View view)// FIXME
	{
		TextView text = null;
		switch (view.getId()) {
		case R.id.similar_query:
			feedbackLayout.setVisibility(View.GONE);
			feedbackView.setVisibility(View.VISIBLE);
			new LanqiuFeedBackTask().execute();
			break;

		case R.id.query:
			backList = true;
			feedbackLayout.setVisibility(View.GONE);
			feedbackView.setVisibility(View.VISIBLE);
			new LanqiuFeedBackTask().execute();
			break;
		case R.id.reset:
			handicapSpinner.setSelection(0, true);
			homeSpinner.setSelection(0, true);
			awaySpinner.setSelection(0, true);
			leagulSpinner.setSelection(0, true);
			handicapSelection = null;
			leagulSelection = null;
			homeSelection = null;
			awaySelection = null;
			break;
		case R.id.selectAo:
			break;
		case R.id.selectHo:

			// new SelectAlertDialog(this, R.string.select_homeWin_,
			// dataList.get("hlist").keys,new
			// DialogClick(dataList.get("hlist").keys,
			// R.id.selectHo,null)).show();
			break;
		/*
		 * case R.id.selectHandicap_bb: text = (TextView) view;
		 * 
		 * break;
		 */
		/*
		 * case R.id.selectLeguals: text = (TextView) view; new
		 * SelectAlertDialog(this, R.string.select_leguals, dataList.get(1),new
		 * DialogClick(dataList.get(1), R.id.selectLeguals,text)).show(); break;
		 * case R.id.selectHomes: text = (TextView) view; new
		 * SelectAlertDialog(this, R.string.select_home, dataList.get(2),new
		 * DialogClick(dataList.get(2), R.id.selectHomes,text)).show(); break;
		 * case R.id.selectAways: text = (TextView) view; new
		 * SelectAlertDialog(this, R.string.select_away, dataList.get(3),new
		 * DialogClick(dataList.get(3), R.id.selectAways,text)).show(); break;
		 */

		default:
			break;
		}
	}

	public static class DialogClick implements DialogInterface.OnClickListener {
		private String[] array;
		private int id;
		private TextView text;

		//
		public DialogClick(String[] items, int id, TextView text) {
			this.id = id;
			array = items;
			this.text = text;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (id) {
			case R.id.selectAways:

			case R.id.selectHomes:

			case R.id.selectLeguals:

				/*
				 * case R.id.selectHandicap_bb:
				 * 
				 * if (text != null) { text.setText("" + array[which]); }
				 * 
				 * break;
				 */
			default:
				break;
			}
			dialog.dismiss();
		}

	}

	@Override
	public void onClick(View v) {
		backList = false;
		play.setSelected(false);
		analyze.setSelected(false);
		/*
		 * europe.setSelected(false); asia.setSelected(false);
		 */
		playList.setVisibility(View.GONE);
		analyzeList.setVisibility(View.GONE);
		feedback.setSelected(false);
		feedbackView.setVisibility(View.GONE);
		feedbackLayout.setVisibility(View.GONE);
		if (v == play) {
			play.setSelected(true);
			playList.setVisibility(View.VISIBLE);
			if (playInit)
				new LanqiuPlayTask().execute();
		} else if (v == analyze) {
			analyze.setSelected(true);
			analyzeList.setVisibility(View.VISIBLE);
			if (analyzeInit)
				new LanqiuAnalyzeTask().execute();
		} else if (v == feedback) {
			feedbackLayout.setVisibility(View.VISIBLE);

			feedback.setSelected(true);
			if (feedbackInit) {
				new LanqiuFeedBackPreTask().execute();

			}
		}
	}
}
