package cn.chaoren;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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
import cn.chaoren.action.SelectAlertDialog;
import cn.chaoren.adapter.FeedBackItemAdapterFB;
import cn.chaoren.adapter.SimpleAdapter;
import cn.chaoren.net.HeaderJSONBean;
import cn.chaoren.net.HttpServiceNew;
import cn.chaoren.net.HttpServices;
import cn.chaoren.net.JSONBean;
import cn.chaoren.net.JSONBeanPlayFB;
import cn.chaoren.net.KeyMapBean;
import cn.chaoren.net.KeyMapBean.KeyMap;
import cn.chaoren.obj.PlayBifen;
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
public class ZuqiuMatchDetailActivity extends Activity implements
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

	boolean europeInit = true;
	ListView europeList;
	EuropeListAdapter europeAdapter;
	View europeHead;
	String[] euroCompanies;
	List<Map<String, String>> europes = new ArrayList<Map<String, String>>();
	ProgressLinearLayout europeProgressbar;

	boolean asiaInit = true;
	ListView asiaList;
	SimpleAdapter asiaAdapter;
	View asiaHead;
	String[] asiaCompanies;
	boolean [] asiaChecked;
	List<Map<String, String>> asias = new ArrayList<Map<String, String>>();
	ProgressLinearLayout asiaProgressbar;

	boolean feedbackInit = true;
	ListView feedbackList;
	View feedbackLayout, requstResultView = null;;
	int homeSelected = 0;

	// String[]
	private HashMap<String, KeyMap> dataList = new HashMap<String, KeyMap>();
	// TextView
	// homeWinSelect,awayWinSelect,drawSelect,handicapSelect,homeSelect,awaySelect,leagulSelect,queryBtn,resetBtn;
	private Spinner homeWinSpinner, awayWinSpinner, drawSpinner,
			handicapSpinner, homeSpinner, awaySpinner, leagulSpinner;
	private android.widget.ArrayAdapter<String> homeWinAdpater, awayWinAdapter,
			drawAdpater, handicapAdapter, homeAdapter, awayAdapter,
			leagulAdapter;
	private String homeWinSelection, awayWinSelection, drawSelection,
			handicapSelection, homeSelection, awaySelection, leagulSelection;
	private FeedBackItemAdapterFB feedAdapter;
	private ArrayList<Map<String, String>> feedBackData = new ArrayList<Map<String, String>>();
	ProgressLinearLayout feedBackProgressbar, feedBackPreProgress;

	private Map<String, String> requestResult = new HashMap<String, String>();

	Button play, analyze, europe, asia, feedback;// feedback--反馈
	String m_id, num, l_cn, l_cn2, l_id, h_cn, h_id, a_cn, a_id, sstime,goaline;
	TextView num_text, l_cn_text, h_cn_text, a_cn_text, sstime_text,goalline_text;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.match_detail);
         
		m_id = getIntent().getStringExtra("m_id");
		num = getIntent().getStringExtra("num");
		l_cn = getIntent().getStringExtra("l_cn");
		l_cn2 = getIntent().getStringExtra("l_cn2");
		l_id = getIntent().getStringExtra("l_id");
		h_cn = getIntent().getStringExtra("h_cn");
		h_id = getIntent().getStringExtra("h_id");
		a_cn = getIntent().getStringExtra("a_cn");
		a_id = getIntent().getStringExtra("a_id");
		sstime = getIntent().getStringExtra("sstime");
       // goaline = getIntent().getStringExtra("goalline");
		num_text = (TextView) findViewById(R.id.num_text);
		num_text.setText(num);
		l_cn_text = (TextView) findViewById(R.id.l_cn_text);
		l_cn_text.setText(l_cn);
		h_cn_text = (TextView) findViewById(R.id.h_cn_text);
		h_cn_text.setText(h_cn);
		a_cn_text = (TextView) findViewById(R.id.a_cn_text);
		a_cn_text.setText(a_cn);
		sstime_text = (TextView) findViewById(R.id.sstime_text);
		sstime_text.setText(sstime);
        /*goalline_text = (TextView) findViewById(R.id.goallineText);
        if (goaline == null ||"".equals(goaline)) {
			goalline_text.setVisibility(View.GONE);
		}else
		{
			  goalline_text.setText("("+goaline+")");
		}*/
      
		play = (Button) findViewById(R.id.play);
		play.setOnClickListener(this);
		play.setSelected(true);
		analyze = (Button) findViewById(R.id.analyze);
		analyze.setOnClickListener(this);
		europe = (Button) findViewById(R.id.europe);
		europe.setOnClickListener(this);
		asia = (Button) findViewById(R.id.asia);
		asia.setOnClickListener(this);
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
				new ZuqiuPlayTask().execute();
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
				new ZuqiuAnalyzeTask().execute();
			}
		});
		analyzeList.addFooterView(analyzeProgressbar, null, false);
		analyzeList.setAdapter(analyzeListAdapter);
		analyzeList.setGroupIndicator(null);
		analyzeList.setDivider(null);

		europeList = (ListView) findViewById(R.id.europe_list);
		europeProgressbar = (ProgressLinearLayout) getLayoutInflater().inflate(
				R.layout.progress_small, null);
		europeProgressbar.initView();
		europeProgressbar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				europeProgressbar.setVisibility(View.VISIBLE, View.VISIBLE,
						View.GONE, View.GONE);
				new ZuqiuEuropeTask().execute();
			}
		});
		europeHead = getLayoutInflater().inflate(R.layout.zuqiu_europe_head,
				null);
		europeList.addHeaderView(europeHead, null, false);
		europeList.addFooterView(europeProgressbar, null, false);
		europeAdapter = new EuropeListAdapter();
		europeAdapter.setDataList(europes);
		europeList.setAdapter(europeAdapter);

		asiaList = (ListView) findViewById(R.id.asia_list);
		asiaProgressbar = (ProgressLinearLayout) getLayoutInflater().inflate(
				R.layout.progress_small, null);
		asiaProgressbar.initView();
		asiaProgressbar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				asiaProgressbar.setVisibility(View.VISIBLE, View.VISIBLE,
						View.GONE, View.GONE);
				new ZuqiuAsiaTask().execute();
			}
		});
		asiaHead = getLayoutInflater().inflate(R.layout.zuqiu_asia_head, null);
		asiaList.addHeaderView(asiaHead, null, false);
		asiaList.addFooterView(asiaProgressbar, null, false);
		asiaAdapter = new SimpleAdapter(this,
				(ArrayList<Map<String, String>>) asias,
				R.layout.zuqiu_asia_list_item, new String[] { "corp", "w1",
						"d1", "l1", "w2", "d2", "l2", "h3", "d3", "a3" },
				new int[] { R.id.corp, R.id.w1, R.id.d1, R.id.l1, R.id.w2,
						R.id.d2, R.id.l2, R.id.h3, R.id.d3, R.id.a3 });

		asiaList.setAdapter(asiaAdapter);

		// homeSelect = (TextView) findViewById(R.id.selectHomes);
		// homeWinSelect = (TextView) findViewById(R.id.selectHomeWin);
		homeWinSpinner = (Spinner) findViewById(R.id.selectHomeWin);
		awayWinSpinner = (Spinner) findViewById(R.id.selectAwayWin);
		drawSpinner = (Spinner) findViewById(R.id.selectDraw);
		handicapSpinner = (Spinner) findViewById(R.id.selectHandicap);
		homeSpinner = (Spinner) findViewById(R.id.selectHomes);
		awaySpinner = (Spinner) findViewById(R.id.selectAways);
		leagulSpinner = (Spinner) findViewById(R.id.selectLeguals);
		homeWinAdpater = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, new ArrayList<String>());
		homeWinAdpater
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		awayWinAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, new ArrayList<String>());
		awayWinAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		drawAdpater = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, new ArrayList<String>());
		drawAdpater
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		handicapAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, new ArrayList<String>());
		handicapAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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
		homeWinSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						homeWinSelection = homeWinAdpater.getItem(position);
						dataList.get("hlist").select(position);
						new RefreshResultTask().execute("");
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}

				});
		homeWinSpinner.setAdapter(homeWinAdpater);

		awayWinSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						awayWinSelection = awayWinAdapter.getItem(position);
						dataList.get("alist").select(position);
						new RefreshResultTask().execute("");
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}

				});
		awayWinSpinner.setAdapter(awayWinAdapter);

		drawSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						drawSelection = drawAdpater.getItem(position);
						dataList.get("dlist").select(position);
						new RefreshResultTask().execute("");
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}

				});
		drawSpinner.setAdapter(drawAdpater);

		handicapSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						handicapSelection = handicapAdapter.getItem(position);
						dataList.get("goalline").select(position);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}

				});
		handicapSpinner.setAdapter(handicapAdapter);

		homeSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						homeSelection = homeAdapter.getItem(position);
						dataList.get("hteam").select(position);
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
						dataList.get("ateam").select(position);
						
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}

				});
		awaySpinner.setAdapter(awayAdapter);

		leagulSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						leagulSelection = leagulAdapter.getItem(position);
						dataList.get("llist").select(position);
						new RefreshLeagual().execute("");
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}

				});
		leagulSpinner.setAdapter(leagulAdapter);

		// awayWinSpinner,drawSpinner,handicapSpinner,homeSpinner,awaySpinner,leagulSpinner
		/*
		 * awaySelect = (TextView) findViewById(R.id.selectAways); awayWinSelect
		 * = (TextView) findViewById(R.id.selectAwayWin); drawSelect =
		 * (TextView) findViewById(R.id.selectDraw); handicapSelect = (TextView)
		 * findViewById(R.id.selectHandicap); leagulSelect = (TextView)
		 * findViewById(R.id.selectLeguals); queryBtn = (TextView)
		 * findViewById(R.id.query);
		 */

		// drawSelect,handicapSelect,homeSelect,awaySelect,leagulSelect,queryBtn,resetBtn;
		requstResultView = findViewById(R.id.feedbackResult);

		feedbackList = (ListView) findViewById(R.id.feedbackResultList);
		feedAdapter = new FeedBackItemAdapterFB(this);
		feedAdapter.setDataList(feedBackData);
		/*
		 * feedBackPreProgress =(ProgressLinearLayout)
		 * getLayoutInflater().inflate(R.layout.progress_small, null);
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
				new ZuqiuFeedBackPreTask().execute();
			}
		});
		feedbackList.addFooterView(feedBackProgressbar, null, false);
		feedbackList.setAdapter(feedAdapter);
		/*feedbackList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(
								ZuqiuMatchDetailActivity.this,
								FeedBackDetailActiviy.class);
						intent.putExtra("type", 1);
						intent.putExtra("map",
								(HashMap<String, String>) feedBackData
										.get(position));
						startActivity(intent);
					}
				});*/
		feedbackLayout = findViewById(R.id.feedbackLayout);

		onClick(play);
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	
	public class RefreshLeagual extends AsyncTask<String, Integer, KeyMap>
	{

		@Override
		protected KeyMap doInBackground(String... params) {
			
			HttpServiceNew serviceNew = new HttpServiceNew();
			String sele =  dataList.get("llist").selectValue;
			KeyMap key = null;
			if (sele== null || "".equals(sele)) {
				key = new KeyMap();
				key.initDefOne(getString(R.string.all));
			}else
			{
				key = serviceNew.getSearchLListSelections(getString(R.string.all), dataList.get("llist").selectValue);
			}
			
			return key;
		}
		
		@Override
		protected void onPostExecute(KeyMap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dataList.put("hteam", result);
			dataList.put("ateam", result.clone());
			homeAdapter.clear();
			String[]array=null;
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
	
	private void initResultTexts(View parent, Map<String, String> map) {
		if (map == null) {
			return;
		}
		TextView text = (TextView) parent.findViewById(R.id.resultHeader);
		StringBuilder sb = new StringBuilder();
		sb.append("共");
		String temp = map.get("total");
		int total = parseInt(temp);
		sb.append(total + "场比赛符合条件:\n其中主队   胜");
		
		if (total != 0) {
			temp = map.get("h");
			int h = parseInt(temp);
			sb.append(h).append(" 场[").append(h * 100 / total).append("%]   平");
			temp = map.get("d");
			int d = parseInt(temp);
			sb.append(d).append(" 场[").append(d * 100 / total).append("%]   负");
			temp = map.get("a");
			int a = parseInt(temp);
			sb.append(a).append(" 场[").append(a * 100 / total).append("%]");
			text.setText(sb.toString());
		}else
		{
			text.setText("共0场比赛符合条件");
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

	/*
	 * public static class ArrayAdapter extends BaseAdapter { private
	 * String[]array ; private LayoutInflater mLayoutInflater; public void
	 * setArray(String[] array) { this.array = array; } public
	 * ArrayAdapter(String[]array,Context context) { this.array = array;
	 * this.mLayoutInflater = LayoutInflater.from(context); }
	 * 
	 * @Override public int getCount() { if (array == null) { return 0; } return
	 * array.length; }
	 * 
	 * @Override public Object getItem(int position) { if (array == null) {
	 * return null; }
	 * 
	 * return array[position]; }
	 * 
	 * @Override public long getItemId(int position) {
	 * 
	 * return 0; }
	 * 
	 * @Override public View getView(int position, View convertView, ViewGroup
	 * parent) { if (convertView == null) { convertView =
	 * mLayoutInflater.inflate(R.layout.array_item, null); } if
	 * (position<array.length) { TextView text = (TextView)
	 * convertView.findViewById(R.id.title); text.setText(""+array[position]); }
	 * return convertView; }
	 * 
	 * }
	 */

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	class ZuqiuPlayTask extends AsyncTask<String, Integer, JSONBeanPlayFB> {
		public ZuqiuPlayTask() {
			playInit = false;
		}

		@Override
		protected JSONBeanPlayFB doInBackground(String... params) {
			try {
				return HttpServices.getPlayFB(m_id);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(JSONBeanPlayFB result) {
			analyzeChildData.clear();
			analyzeGroupData.clear();
			if (result == null) {
				playProgressbar.setVisibility(View.GONE, View.GONE,
						View.VISIBLE, View.VISIBLE);
			} else {
				Map<String, String> curGroupMap = new HashMap<String, String>();
				curGroupMap.put("text",
						getResources().getString(R.string.wf_spf)
								+ "("
								+ getResources().getString(R.string.wf_rq)
								+ ":"
								+ (result.goaline == null ? "0"
										: result.goaline) + ")");
				curGroupMap.put("type", "spf");
				Map<String, Object> children = new HashMap<String, Object>();
				if (result.hhad.size() > 0) {
					for (int i = 0; i < result.hhad.size(); i++) {
						List<String> l = result.hhad.get(i);
						children.put(l.get(0), l.get(1));
					}
					playChildData.add(children);
					playGroupData.add(curGroupMap);
				}

				curGroupMap = new HashMap<String, String>();
				curGroupMap.put("text", getResources()
						.getString(R.string.wf_bf));
				curGroupMap.put("type", "bf");
				children = new HashMap<String, Object>();
				if (result.crs.size() > 0) {
					List<PlayBifen> list = new ArrayList<PlayBifen>();
					List<List<String>> hlist = result.crs.get(0);
					for (int i = 0; i < hlist.size(); i++) {
						list.add(new PlayBifen(hlist.get(i).get(0), hlist
								.get(i).get(1)));
					}
					children.put("h", list);
					list = new ArrayList<PlayBifen>();
					List<List<String>> dlist = result.crs.get(1);
					for (int i = 0; i < dlist.size(); i++) {
						list.add(new PlayBifen(dlist.get(i).get(0), dlist
								.get(i).get(1)));
					}
					children.put("d", list);
					list = new ArrayList<PlayBifen>();
					List<List<String>> alist = result.crs.get(2);
					for (int i = 0; i < alist.size(); i++) {
						list.add(new PlayBifen(alist.get(i).get(0), alist
								.get(i).get(1)));
					}
					children.put("a", list);
					playChildData.add(children);
					playGroupData.add(curGroupMap);
				}

				curGroupMap = new HashMap<String, String>();
				curGroupMap.put("text",
						getResources().getString(R.string.wf_zjq));
				children = new HashMap<String, Object>();
				curGroupMap.put("type", "zjq");
				if (result.ttg.size() > 0) {
					children.put("g0", result.ttg.get(0).get(1));
					children.put("g1", result.ttg.get(1).get(1));
					children.put("g2", result.ttg.get(2).get(1));
					children.put("g3", result.ttg.get(3).get(1));
					children.put("g4", result.ttg.get(4).get(1));
					children.put("g5", result.ttg.get(5).get(1));
					children.put("g6", result.ttg.get(6).get(1));
					children.put("g7", result.ttg.get(7).get(1));
					playChildData.add(children);
					playGroupData.add(curGroupMap);
				}

				curGroupMap = new HashMap<String, String>();
				curGroupMap.put("text",
						getResources().getString(R.string.wf_bqc));
				children = new HashMap<String, Object>();
				curGroupMap.put("type", "bqc");
				if (result.hafu.size() > 0) {
					children.put("hh", result.hafu.get(0).get(1));
					children.put("hd", result.hafu.get(1).get(1));
					children.put("ha", result.hafu.get(2).get(1));
					children.put("dh", result.hafu.get(3).get(1));
					children.put("dd", result.hafu.get(4).get(1));
					children.put("da", result.hafu.get(5).get(1));
					children.put("ah", result.hafu.get(6).get(1));
					children.put("ad", result.hafu.get(7).get(1));
					children.put("aa", result.hafu.get(8).get(1));
					playChildData.add(children);
					playGroupData.add(curGroupMap);
				}
				if (playGroupData.size() > 0) {
					if (playList.getFooterViewsCount() > 0) {
						playList.removeFooterView(playProgressbar);
					}
				} else {
					playProgressbar.setVisibility(View.GONE, View.GONE,
							View.VISIBLE, View.GONE);
					playProgressbar.setText(null,
							getResources().getString(R.string.tip3));
				}
				playListAdapter.notifyDataSetChanged();
			}
			super.onPostExecute(result);
		}
	}

	class ZuqiuAnalyzeTask extends AsyncTask<String, Integer, JSONBean[]> {
		public ZuqiuAnalyzeTask() {
			analyzeInit = false;
		}

		@Override
		protected JSONBean[] doInBackground(String... params) {
			try {
				JSONBean[] result = new JSONBean[6];
				// 近期对阵
				result[0] = HttpServices.getTwoPrevFB(h_id, a_id);
				// 主队近期10场
				result[1] = HttpServices.getMatchInfoFB(h_id, 10);
				// 客队近期10场
				result[2] = HttpServices.getMatchInfoFB(a_id, 10);
				// 主队未来5场
				result[3] = HttpServices.getFutureFB(h_id);
				// 客队未来5场
				result[4] = HttpServices.getFutureFB(a_id);
				// 联赛排名
				result[5] = HttpServices.getOrderFB(l_cn2, "");
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
				if (result[0] != null && result[0].bodys.size() > 0) {
					analyzeChildData.add(result[0].bodys);
					analyzeGroupData.add(curGroupMap);
				}

				curGroupMap = new HashMap<String, String>();
				curGroupMap.put("text",
						getResources().getString(R.string.fx_zdjq));
				curGroupMap.put("type", "zdjq");
				if (result[1] != null && result[1].bodys.size() > 0) {
					analyzeChildData.add(result[1].bodys);
					analyzeGroupData.add(curGroupMap);
				}

				curGroupMap = new HashMap<String, String>();
				curGroupMap.put("text",
						getResources().getString(R.string.fx_kdjq));
				curGroupMap.put("type", "kdjq");
				if (result[2] != null && result[2].bodys.size() > 0) {
					analyzeChildData.add(result[2].bodys);
					analyzeGroupData.add(curGroupMap);
				} else {
					analyzeChildData.add(null);
				}

				curGroupMap = new HashMap<String, String>();
				curGroupMap.put("text",
						getResources().getString(R.string.fx_zdwl));
				curGroupMap.put("type", "zdwl");
				if (result[3] != null && result[3].bodys.size() > 0) {
					analyzeChildData.add(result[3].bodys);
					analyzeGroupData.add(curGroupMap);
				}

				curGroupMap = new HashMap<String, String>();
				curGroupMap.put("text",
						getResources().getString(R.string.fx_kdwl));
				curGroupMap.put("type", "kdwl");
				if (result[4] != null && result[4].bodys.size() > 0) {
					analyzeChildData.add(result[4].bodys);
					analyzeGroupData.add(curGroupMap);
				}

				curGroupMap = new HashMap<String, String>();
				curGroupMap.put("text",
						getResources().getString(R.string.fx_lspm));
				curGroupMap.put("type", "lspm");
				if (result[5] != null && result[5].bodys.size() > 0) {
					analyzeChildData.add(result[5].bodys);
					analyzeGroupData.add(curGroupMap);
				}

				if (analyzeGroupData.size() > 0) {
					if (analyzeList.getFooterViewsCount() > 0) {
						analyzeList.removeFooterView(analyzeProgressbar);
					}
				} else {
					analyzeProgressbar.setVisibility(View.GONE, View.GONE,
							View.VISIBLE, View.GONE);
					analyzeProgressbar.setText(null,
							getResources().getString(R.string.tip4));
				}
				analyzeListAdapter.notifyDataSetChanged();
			}
			super.onPostExecute(result);
		}
	}

	class ZuqiuEuropeTask extends AsyncTask<String, Integer, JSONBean> {
		public ZuqiuEuropeTask() {
			europeInit = false;
		}

		@Override
		protected JSONBean doInBackground(String... params) {
			try {
				return HttpServices.getEuropeFB(m_id);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(JSONBean result) {
			if (result == null) {
				europeProgressbar.setVisibility(View.GONE, View.GONE,
						View.VISIBLE, View.VISIBLE);
			} else {
				europes.addAll(result.bodys);
				euroCompanies = fliter(europes, "corp");
				if (euroCompanies != null) {
					for (int i = 0; i < euroCompanies.length; i++) {
						System.out.println("cor:" + euroCompanies[i]);
					}
				}

				if (result.bodys.size() > 0) {
					if (europeList.getFooterViewsCount() > 0) {
						europeList.removeFooterView(europeProgressbar);
					}
				} else {
					europeProgressbar.setVisibility(View.GONE, View.GONE,
							View.VISIBLE, View.GONE);
					europeProgressbar.setText(null,
							getResources().getString(R.string.tip1));
				}
				europeAdapter.notifyDataSetChanged();
			}
			super.onPostExecute(result);
		}
	}

	class ZuqiuFeedBackPreTask extends AsyncTask<String, Integer, KeyMapBean> {
		public ZuqiuFeedBackPreTask() {
			feedbackInit = false;
		}

		@Override
		protected KeyMapBean doInBackground(String... params) {
			// TODO Auto-generated method stub
			return HttpServices.getFeedBackPreFB(m_id, getResources()
					.getString(R.string.all));
		}

		@Override
		protected void onPostExecute(KeyMapBean result) {
			if (result == null) {
				// feedbackLayout.setVisibility(View.VISIBLE);
			} else {
				dataList.clear();
				if (result.keyList.size() > 0) {

					dataList = result.keyList;
					String[] array = null;
					homeWinAdpater.clear();
					array = dataList.get("hlist").keys;

					if (array != null) {
						for (int i = 0; i < array.length; i++) {
							homeWinAdpater.add(array[i]);
						}
					}
					awayWinAdapter.clear();
					array = dataList.get("alist").keys;

					if (array != null) {
						for (int i = 0; i < array.length; i++) {
							awayWinAdapter.add(array[i]);
						}
					}
					drawAdpater.clear();
					array = dataList.get("dlist").keys;

					if (array != null) {
						for (int i = 0; i < array.length; i++) {
							drawAdpater.add(array[i]);
						}
					}
					handicapAdapter.clear();
					array = dataList.get("goalline").keys;

					if (array != null) {
						for (int i = 0; i < array.length; i++) {
							handicapAdapter.add(array[i]);
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

	private boolean preparing;

	class ZuqiuFeedBackTask extends AsyncTask<String, Integer, HeaderJSONBean> {

		@Override
		protected HeaderJSONBean doInBackground(String... params) {
			//
			return HttpServices.getFeedBackFB(dataList.get("hteam").selectValue,dataList.get("ateam").selectValue,
					dataList.get("llist").selectValue,dataList.get("goalline").selectValue,null,//
					dataList.get("hlist").selectValue,dataList.get("dlist").selectValue,dataList.get("alist").selectValue,null);//count
		}

		@Override
		protected void onPostExecute(HeaderJSONBean result) {
			if (result == null) {
				feedBackProgressbar.setVisibility(View.GONE, View.GONE,
						View.VISIBLE, View.VISIBLE);
			} else {
				feedBackData.clear();
				if (result.bodys.size() > 0) {
					if (feedbackList.getFooterViewsCount() > 0) {
						feedbackList.removeFooterView(feedBackProgressbar);
					}
					feedBackData.addAll(result.bodys);
				} else {
					feedBackProgressbar.setVisibility(View.GONE, View.GONE,
							View.VISIBLE, View.GONE);
					feedBackProgressbar.setText(null,
							getResources().getString(R.string.tip5));
				}
				feedAdapter.notifyDataSetChanged();
				feedbackHeader = result.header;
				initResultTexts(requstResultView, result.header);
			}
			super.onPostExecute(result);
		}

	}

	
	private HashMap<String, String> feedbackHeader = null;
	class ZuqiuAsiaTask extends AsyncTask<String, Integer, JSONBean> {
		public ZuqiuAsiaTask() {
			asiaInit = false;
		}

		@Override
		protected JSONBean doInBackground(String... params) {
			try {
				return HttpServices.getAsiaFB(m_id);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(JSONBean result) {
			if (result == null) {
				asiaProgressbar.setVisibility(View.GONE, View.GONE,
						View.VISIBLE, View.VISIBLE);
			} else {
				asias.addAll(result.bodys);
				asiaCompanies = fliter(asias, "corp");
				if (asiaCompanies != null) {
					asiaChecked = new boolean[asiaCompanies.length];
					for (int i = 0; i < asiaCompanies.length; i++) {
						asiaChecked[i] = false;
					}
				}
				if (result.bodys.size() > 0) {
					if (asiaList.getFooterViewsCount() > 0) {
						asiaList.removeFooterView(asiaProgressbar);
					}
				} else {
					asiaProgressbar.setVisibility(View.GONE, View.GONE,
							View.VISIBLE, View.GONE);
					asiaProgressbar.setText(null,
							getResources().getString(R.string.tip2));
				}
				asiaAdapter.notifyDataSetChanged();
			}
			super.onPostExecute(result);
		}
	}

	private List<Map<String, String>> fliter(List<Map<String, String>> data,
			String key, String value) {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		int size = data.size();
		String value1;
		if (value != null && !"".equals(value)) {
			for (int i = 0; i < size; i++) {
				value1 = data.get(i).get(key);
				if (value.equals(value1)) {
					result.add(data.get(i));
				}
			}
		} else {
			result.addAll(data);
		}

		return result;
	}

	private String[] fliter(List<Map<String, String>> data, String key) {
		ArrayList<String> values = new ArrayList<String>();
		int size = data.size();
		String temp = null;
		for (int i = 0; i < size; i++) {
			temp = data.get(i).get(key);
			if (temp != null && !values.contains(temp)) {
				values.add(temp);
			}
		}
		String[] result = null;
		size = values.size();
		if (size > 0) {
			result = new String[size + 1];
			result[0] = getString(R.string.all);
			for (int i = 0; i < size; i++) {
				result[i + 1] = values.get(i);
			}
		}
		return result;
	}

	class EuropeListAdapter extends BaseAdapter {
		public EuropeListAdapter() {
			// TODO Auto-generated constructor stub
		}

		private List<Map<String, String>> dataList;

		public void setDataList(List<Map<String, String>> dataList) {
			this.dataList = dataList;
		}

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return dataList.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.zuqiu_europe_list_item, null);
			}
			Map<String, String> map = dataList.get(position);
			((TextView) convertView.findViewById(R.id.corp)).setText(map
					.get("corp"));
			((TextView) convertView.findViewById(R.id.w)).setText(map.get("w"));
			((TextView) convertView.findViewById(R.id.d)).setText(map.get("d"));
			((TextView) convertView.findViewById(R.id.a)).setText(map.get("a"));
			((TextView) convertView.findViewById(R.id.wa)).setText(map
					.get("wa"));
			((TextView) convertView.findViewById(R.id.da)).setText(map
					.get("da"));
			((TextView) convertView.findViewById(R.id.aa)).setText(map
					.get("aa"));
			((TextView) convertView.findViewById(R.id.kw)).setText(map
					.get("kw"));
			((TextView) convertView.findViewById(R.id.kd)).setText(map
					.get("kd"));
			((TextView) convertView.findViewById(R.id.ka)).setText(map
					.get("ka"));
			((TextView) convertView.findViewById(R.id.rr)).setText(map
					.get("rr"));
			return convertView;
		}
	}

	class ExPlayAdapter extends BaseExpandableListAdapter {

		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				view = getLayoutInflater().inflate(
						R.layout.match_detail_list_group_item, null);
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
			if (map.get("type").equals("spf")) {
				convertView = getLayoutInflater().inflate(
						R.layout.zuqiu_play_hhad_list_child_item, null);
				TextView h = (TextView) convertView.findViewById(R.id.h);
				h.setText(playChildData.get(groupPosition).get("h").toString());
				TextView d = (TextView) convertView.findViewById(R.id.d);
				d.setText(playChildData.get(groupPosition).get("d").toString());
				TextView a = (TextView) convertView.findViewById(R.id.a);
				a.setText(playChildData.get(groupPosition).get("a").toString());
			} else if (map.get("type").equals("bf")) {
				convertView = getLayoutInflater().inflate(
						R.layout.zuqiu_play_crs_list_child_item, null);
				TableLayout crs_table = (TableLayout) convertView
						.findViewById(R.id.crs_table);
				if (childPosition == 0) {
					((TextView) convertView.findViewById(R.id.table_title))
							.setText(R.string.h1);
					List<PlayBifen> hlist = (List<PlayBifen>) playChildData
							.get(groupPosition).get("h");
					addRows(hlist, crs_table);
				} else if (childPosition == 1) {
					((TextView) convertView.findViewById(R.id.table_title))
							.setText(R.string.d1);
					List<PlayBifen> dlist = (List<PlayBifen>) playChildData
							.get(groupPosition).get("d");
					addRows(dlist, crs_table);
				} else {
					((TextView) convertView.findViewById(R.id.table_title))
							.setText(R.string.a1);
					List<PlayBifen> alist = (List<PlayBifen>) playChildData
							.get(groupPosition).get("a");
					addRows(alist, crs_table);
				}
			} else if (map.get("type").equals("zjq")) {
				convertView = getLayoutInflater().inflate(
						R.layout.zuqiu_play_ttg_list_child_item, null);
				TextView g0 = (TextView) convertView.findViewById(R.id.g0);
				g0.setText(playChildData.get(groupPosition).get("g0")
						.toString());
				TextView g1 = (TextView) convertView.findViewById(R.id.g1);
				g1.setText(playChildData.get(groupPosition).get("g1")
						.toString());
				TextView g2 = (TextView) convertView.findViewById(R.id.g2);
				g2.setText(playChildData.get(groupPosition).get("g2")
						.toString());
				TextView g3 = (TextView) convertView.findViewById(R.id.g3);
				g3.setText(playChildData.get(groupPosition).get("g3")
						.toString());
				TextView g4 = (TextView) convertView.findViewById(R.id.g4);
				g4.setText(playChildData.get(groupPosition).get("g4")
						.toString());
				TextView g5 = (TextView) convertView.findViewById(R.id.g5);
				g5.setText(playChildData.get(groupPosition).get("g5")
						.toString());
				TextView g6 = (TextView) convertView.findViewById(R.id.g6);
				g6.setText(playChildData.get(groupPosition).get("g6")
						.toString());
				TextView g7 = (TextView) convertView.findViewById(R.id.g7);
				g7.setText(playChildData.get(groupPosition).get("g7")
						.toString());
			} else if (map.get("type").equals("bqc")) {
				convertView = getLayoutInflater().inflate(
						R.layout.zuqiu_play_hafu_list_child_item, null);
				TextView hh = (TextView) convertView.findViewById(R.id.hh);
				hh.setText(playChildData.get(groupPosition).get("hh")
						.toString());
				TextView hd = (TextView) convertView.findViewById(R.id.hd);
				hd.setText(playChildData.get(groupPosition).get("hd")
						.toString());
				TextView ha = (TextView) convertView.findViewById(R.id.ha);
				ha.setText(playChildData.get(groupPosition).get("ha")
						.toString());
				TextView dh = (TextView) convertView.findViewById(R.id.dh);
				dh.setText(playChildData.get(groupPosition).get("dh")
						.toString());
				TextView dd = (TextView) convertView.findViewById(R.id.dd);
				dd.setText(playChildData.get(groupPosition).get("dd")
						.toString());
				TextView da = (TextView) convertView.findViewById(R.id.da);
				da.setText(playChildData.get(groupPosition).get("da")
						.toString());
				TextView ah = (TextView) convertView.findViewById(R.id.ah);
				ah.setText(playChildData.get(groupPosition).get("ah")
						.toString());
				TextView ad = (TextView) convertView.findViewById(R.id.ad);
				ad.setText(playChildData.get(groupPosition).get("ad")
						.toString());
				TextView aa = (TextView) convertView.findViewById(R.id.aa);
				aa.setText(playChildData.get(groupPosition).get("aa")
						.toString());
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
			if (groupPosition == 1)
				return 3;
			return 1;
		}

		public boolean hasStableIds() {
			return true;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		private void addRows(List<PlayBifen> list, TableLayout item_table) {
			int size = 0, mold = 0;
			TextView key1, value1, key2, value2, key3, value3, key4, value4, key5, value5;
			size = list.size() / 5;
			for (int i = 0; i < size; i++) {
				TableLayout layout = (TableLayout) getLayoutInflater().inflate(
						R.layout.zuqiu_play_crs_list_child_table_item, null);
				item_table.addView(layout);
				key1 = (TextView) layout.findViewById(R.id.key1);
				value1 = (TextView) layout.findViewById(R.id.value1);
				PlayBifen bifen = list.get(0 + i * 5);
				key1.setText(bifen.key);
				value1.setText(bifen.value);
				key2 = (TextView) layout.findViewById(R.id.key2);
				value2 = (TextView) layout.findViewById(R.id.value2);
				bifen = list.get(1 + i * 5);
				key2.setText(bifen.key);
				value2.setText(bifen.value);
				key3 = (TextView) layout.findViewById(R.id.key3);
				value3 = (TextView) layout.findViewById(R.id.value3);
				bifen = list.get(2 + i * 5);
				key3.setText(bifen.key);
				value3.setText(bifen.value);
				key4 = (TextView) layout.findViewById(R.id.key4);
				value4 = (TextView) layout.findViewById(R.id.value4);
				bifen = list.get(3 + i * 5);
				key4.setText(bifen.key);
				value4.setText(bifen.value);
				key5 = (TextView) layout.findViewById(R.id.key5);
				value5 = (TextView) layout.findViewById(R.id.value5);
				bifen = list.get(4 + i * 5);
				key5.setText(bifen.key);
				value5.setText(bifen.value);
			}
			mold = list.size() % 5;
			if (mold > 0) {
				TableLayout layout = (TableLayout) getLayoutInflater().inflate(
						R.layout.zuqiu_play_crs_list_child_table_item, null);
				item_table.addView(layout);
				View row2, row3, row4, row5;
				row2 = layout.findViewById(R.id.row2);
				row3 = layout.findViewById(R.id.row3);
				row4 = layout.findViewById(R.id.row4);
				row5 = layout.findViewById(R.id.row5);
				row5.setVisibility(View.INVISIBLE);
				key1 = (TextView) layout.findViewById(R.id.key1);
				value1 = (TextView) layout.findViewById(R.id.value1);
				key2 = (TextView) layout.findViewById(R.id.key2);
				value2 = (TextView) layout.findViewById(R.id.value2);
				key3 = (TextView) layout.findViewById(R.id.key3);
				value3 = (TextView) layout.findViewById(R.id.value3);
				key4 = (TextView) layout.findViewById(R.id.key4);
				value4 = (TextView) layout.findViewById(R.id.value4);
				PlayBifen bifen = list.get(size * 5);
				switch (mold) {
				case 1: {
					key1.setText(bifen.key);
					value1.setText(bifen.value);
					row2.setVisibility(View.INVISIBLE);
					row3.setVisibility(View.INVISIBLE);
					row4.setVisibility(View.INVISIBLE);
				}
					break;
				case 2: {
					key1.setText(bifen.key);
					value1.setText(bifen.value);
					bifen = list.get(size * 5 + 1);
					key2.setText(bifen.key);
					value2.setText(bifen.value);
					row3.setVisibility(View.INVISIBLE);
					row4.setVisibility(View.INVISIBLE);
				}
					break;
				case 3: {
					key1.setText(bifen.key);
					value1.setText(bifen.value);
					bifen = list.get(size * 5 + 1);
					key2.setText(bifen.key);
					value2.setText(bifen.value);
					bifen = list.get(size * 5 + 2);
					key3.setText(bifen.key);
					value3.setText(bifen.value);
					row4.setVisibility(View.INVISIBLE);
				}
					break;
				case 4: {
					key1.setText(bifen.key);
					value1.setText(bifen.value);
					bifen = list.get(size * 5 + 1);
					key2.setText(bifen.key);
					value2.setText(bifen.value);
					bifen = list.get(size * 5 + 2);
					key3.setText(bifen.key);
					value3.setText(bifen.value);
					bifen = list.get(size * 5 + 3);
					key3.setText(bifen.key);
					value3.setText(bifen.value);
				}
					break;
				}
			}
		}
	}

	class ExAnalyzeAdapter extends BaseExpandableListAdapter {
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				view = getLayoutInflater().inflate(
						R.layout.match_detail_list_group_item, null);
			}
			TextView text = (TextView) view.findViewById(R.id.text);
			if (analyzeGroupData != null && analyzeGroupData.size() > 0) {
				text.setText(analyzeGroupData.get(groupPosition).get("text"));
			}

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
			Map<String, String> titleMap = analyzeGroupData.get(groupPosition);
			if (titleMap.get("type").equals("ldwj")) {
				LinearLayout layout = (LinearLayout) getLayoutInflater()
						.inflate(R.layout.zuqiu_analyze_ldwjdzjcjjhsg, null);
				convertView = layout;
				List<Map<String, String>> list = analyzeChildData
						.get(groupPosition);
				if (list != null) {
					for (int i = 0; i < list.size(); i++) {
						View view = getLayoutInflater()
								.inflate(
										R.layout.zuqiu_analyze_ldwjdzjcjjhsg_item,
										null);
						Map<String, String> map = list.get(i);
						if (view.findViewById(R.id.l_cn) == null) {
							System.out.println("View l_cn is NULL");
						}
						if (map.get("l_cn") == null) {
							System.out.println("map l_cn is NULL");
						}
						((TextView) view.findViewById(R.id.l_cn)).setText(map
								.get("l_cn"));
						((TextView) view.findViewById(R.id.date)).setText(map
								.get("date"));
						((TextView) view.findViewById(R.id.h_cn)).setText(map
								.get("h_cn"));
						((TextView) view.findViewById(R.id.a_cn)).setText(map
								.get("a_cn"));
						((TextView) view.findViewById(R.id.goalline))
								.setText(map.get("goalline"));
						((TextView) view.findViewById(R.id.half)).setText(map
								.get("half"));
						((TextView) view.findViewById(R.id.final1)).setText(map
								.get("final"));
						((TextView) view.findViewById(R.id.ttg)).setText(map
								.get("ttg"));
						((TextView) view.findViewById(R.id.hafu)).setText(map
								.get("hafu"));
						((TextView) view.findViewById(R.id.hhad)).setText(map
								.get("hhad"));
						((TextView) view.findViewById(R.id.crs)).setText(map
								.get("crs"));
						((TextView) view.findViewById(R.id.h)).setText(map
								.get("h"));
						((TextView) view.findViewById(R.id.d)).setText(map
								.get("d"));
						((TextView) view.findViewById(R.id.a)).setText(map
								.get("a"));
						layout.addView(view);
					}
				}
			} else if (titleMap.get("type").equals("zdjq")
					|| titleMap.get("type").equals("kdjq")) {
				convertView = getLayoutInflater().inflate(
						R.layout.zuqiu_analyze_jqzj, null);
				LinearLayout table_layout = (LinearLayout) convertView
						.findViewById(R.id.table);

				List<Map<String, String>> list = analyzeChildData
						.get(groupPosition);
				if (list != null) {
					for (int i = 0; i < list.size(); i++) {
						TableLayout table_item = (TableLayout) getLayoutInflater()
								.inflate(R.layout.zuqiu_analyze_jqzj_item, null);
						Map<String, String> map = list.get(i);
						((TextView) table_item.findViewById(R.id.l_cn))
								.setText(map.get("l_cn"));
						((TextView) table_item.findViewById(R.id.date))
								.setText(map.get("date"));
						((TextView) table_item.findViewById(R.id.h_cn))
								.setText(map.get("h_cn"));
						((TextView) table_item.findViewById(R.id.goalline))
								.setText(map.get("goalline"));
						((TextView) table_item.findViewById(R.id.a_cn))
								.setText(map.get("a_cn"));
						((TextView) table_item.findViewById(R.id.half))
								.setText(map.get("half"));
						((TextView) table_item.findViewById(R.id.finaled))
								.setText(map.get("final"));
						((TextView) table_item.findViewById(R.id.hhad))
								.setText(map.get("hhad"));
						((TextView) table_item.findViewById(R.id.h1))
								.setText(map.get("h"));
						((TextView) table_item.findViewById(R.id.d1))
								.setText(map.get("d"));
						((TextView) table_item.findViewById(R.id.a1))
								.setText(map.get("a"));
						((TextView) table_item.findViewById(R.id.crs))
								.setText(map.get("crs"));
						((TextView) table_item.findViewById(R.id.ttg))
								.setText(map.get("ttg"));
						((TextView) table_item.findViewById(R.id.hafu))
								.setText(map.get("hafu"));
						table_layout.addView(table_item);
					}
				}
			} else if (titleMap.get("type").equals("zdwl")
					|| titleMap.get("type").equals("kdwl")) {
				convertView = getLayoutInflater().inflate(
						R.layout.zuqiu_analyze_wlss, null);
				TableLayout table_layout = (TableLayout) convertView
						.findViewById(R.id.table);

				List<Map<String, String>> list = analyzeChildData
						.get(groupPosition);
				if (list != null) {
					for (int i = 0; i < list.size(); i++) {
						TableLayout table_item = (TableLayout) getLayoutInflater()
								.inflate(R.layout.zuqiu_analyze_wlss_item, null);
						Map<String, String> map = list.get(i);
						((TextView) table_item.findViewById(R.id.l_cn))
								.setText(map.get("l_cn"));
						((TextView) table_item.findViewById(R.id.date))
								.setText(map.get("date"));
						((TextView) table_item.findViewById(R.id.h_cn))
								.setText(map.get("h_cn"));
						((TextView) table_item.findViewById(R.id.a_cn))
								.setText(map.get("a_cn"));
						table_layout.addView(table_item);
					}
				}
			} else if (titleMap.get("type").equals("lspm")) {
				convertView = getLayoutInflater().inflate(
				/**
				 * index cn count win draw lose goal losegoal net score
				 */
				R.layout.zuqiu_analyze_lspm, null);
				TableLayout table_layout = (TableLayout) convertView
						.findViewById(R.id.table);
				List<Map<String, String>> list = analyzeChildData
						.get(groupPosition);
				if (list != null) {
					for (int i = 0; i < list.size(); i++) {
						TableLayout table_item = (TableLayout) getLayoutInflater()
								.inflate(R.layout.zuqiu_analyze_lspm_item, null);
						Map<String, String> map = list.get(i);
						((TextView) table_item.findViewById(R.id.index))
								.setText(map.get("index"));
						((TextView) table_item.findViewById(R.id.cn))
								.setText(map.get("cn"));
						((TextView) table_item.findViewById(R.id.count))
								.setText(map.get("count"));
						((TextView) table_item.findViewById(R.id.win))
								.setText(map.get("win"));
						((TextView) table_item.findViewById(R.id.draw))
								.setText(map.get("draw"));
						((TextView) table_item.findViewById(R.id.lose))
								.setText(map.get("lose"));
						((TextView) table_item.findViewById(R.id.goal))
								.setText(map.get("goal"));
						((TextView) table_item.findViewById(R.id.losegoal))
								.setText(map.get("losegoal"));
						((TextView) table_item.findViewById(R.id.net))
								.setText(map.get("net"));
						((TextView) table_item.findViewById(R.id.score))
								.setText(map.get("score"));
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
		// TODO Auto-generated method stub
		System.out.println("ok.....");
		if (backList) {
			backList = false;
			feedbackLayout.setVisibility(View.VISIBLE);
			requstResultView.setVisibility(View.GONE);
		}else
		{
			super.onBackPressed();
		}
		
	}
	
	
	/*
	 * private OnClickListener feedBackOnClick = new OnClickListener() {
	 * //TextView
	 * homeWinSelect,awayWinSelect,drawSelect,handicapSelect,homeSelect
	 * ,awaySelect,leagulSelect,queryBtn,resetBtn;
	 * 
	 * @Override public void onClick(View v) { switch (v.getId()) { case R.id:
	 * 
	 * break;
	 * 
	 * default: break; }
	 * 
	 * } };
	 */
	/**
	 * dataList.add(homeWins); dataList.add(draws); dataList.add(awayWins);
	 * dataList.add(handicaps); dataList.add(leaguls); //主队,客队
	 * 
	 * @param view
	 */
	public void feedBackOnClick(View view) {
		TextView text = null;
		switch (view.getId()) {
		case R.id.selectHomeWin:
			text = (TextView) view;
			new SelectAlertDialog(this, R.string.select_homeWin_,
					dataList.get("hlist").keys, new DialogClick(
							dataList.get("hlist").keys, R.id.selectHomeWin,
							text)).show();
			break;
		case R.id.selectDraw:
			text = (TextView) view;
			new SelectAlertDialog(this, R.string.select_draw_,
					dataList.get("dlist").keys, new DialogClick(
							dataList.get("dlist").keys, R.id.selectDraw, text))
					.show();
			break;
		case R.id.selectAwayWin:
			text = (TextView) view;
			new SelectAlertDialog(this, R.string.select_awayWin_,
					dataList.get("alist").keys, new DialogClick(
							dataList.get("alist").keys, R.id.selectAwayWin,
							text)).show();
			break;
		case R.id.selectHandicap:
			text = (TextView) view;
			new SelectAlertDialog(this, R.string.select_handicap_,
					dataList.get("goalline").keys, new DialogClick(
							dataList.get("goalline").keys, R.id.selectHandicap,
							text)).show();
			break;
		case R.id.selectLeguals:
			text = (TextView) view;
			new SelectAlertDialog(this, R.string.select_leguals,
					dataList.get("llist").keys, new DialogClick(
							dataList.get("llist").keys, R.id.selectLeguals,
							text)).show();
			break;
		case R.id.selectHomes:
			text = (TextView) view;
			new SelectAlertDialog(this, R.string.select_home,
					dataList.get("hteam").keys, new DialogClick(
							dataList.get("hteam").keys, R.id.selectHomes, text))
					.show();
			break;
		case R.id.selectAways:
			text = (TextView) view;
			new SelectAlertDialog(this, R.string.select_away,
					dataList.get("ateam").keys, new DialogClick(
							dataList.get("ateam").keys, R.id.selectAways, text))
					.show();
			break;
		case R.id.query:
			backList = true;
			
			feedbackLayout.setVisibility(View.GONE);
			requstResultView.setVisibility(View.VISIBLE);
			new ZuqiuFeedBackTask().execute();
			break;
		case R.id.reset:
			homeWinSpinner.setSelection(0, true);
			awayWinSpinner.setSelection(0, true);
			drawSpinner.setSelection(0, true);
			handicapSpinner.setSelection(0, true);
			homeSpinner.setSelection(0, true);
			awaySpinner.setSelection(0, true);
			leagulSpinner.setSelection(0, true);
			homeWinSelection = null;
			awayWinSelection = null;
			drawSelection = null;
			handicapSelection = null;
			leagulSelection = null;
			homeSelection = null;
			awaySelection = null;
			// drawSelect,handicapSelect,homeSelect,awaySelect,leagulSelect,queryBtn,resetBtn;
			/*
			 * homeSelect.setText(R.string.all);
			 * handicapSelect.setText(R.string.all);
			 * awaySelect.setText(R.string.all);
			 * drawSelect.setText(R.string.all);
			 * leagulSelect.setText(R.string.all);
			 * //homeWinSelect.setText(R.string.all);
			 * awayWinSelect.setText(R.string.all);
			 */
			// 回到问题中去

			break;
		case R.id.europe_company_select:
			text = (TextView) findViewById(R.id.e_companyText);
			new SelectAlertDialog(this, R.string.select_company, euroCompanies,
					new DialogClick(euroCompanies, R.id.europe_company_select,
							text)).show();
			break;
		case R.id.asia_company_select:
			text = (TextView) findViewById(R.id.companyText);
			new SelectAlertDialog(this, R.string.select_company, asiaCompanies,
					new DialogClick(asiaCompanies, R.id.asia_company_select,
							text)).show();
			break;
		default:
			break;
		}
	}

	class  DialogMult implements DialogInterface.OnMultiChoiceClickListener
	{
		int type = 0;//2,代表亚指 1,代表 欧指
		@Override
		public void onClick(DialogInterface dialog, int which, boolean isChecked) {
			asiaChecked[which] = isChecked;			
		}
		
	}
	
	
	
	class DialogClick implements DialogInterface.OnClickListener {
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
			List<Map<String, String>> tempList = new ArrayList<Map<String, String>>();
			switch (id) {
			case R.id.asia_company_select:
				if (which == 0) {
					tempList = fliter(asias, "corp", null);
				} else {
					tempList = fliter(asias, "corp", array[which]);
				}
				if (text != null) {
					text.setText("" + array[which]);
				}
				asiaAdapter
						.setDataList((ArrayList<Map<String, String>>) tempList);
				asiaAdapter.notifyDataSetChanged();
				break;
			case R.id.europe_company_select:
				if (which == 0) {
					tempList = fliter(europes, "corp", null);
				} else {
					tempList = fliter(europes, "corp", array[which]);
				}
				if (text != null) {
					text.setText("" + array[which]);
				}
				europeAdapter
						.setDataList((ArrayList<Map<String, String>>) tempList);
				europeAdapter.notifyDataSetChanged();
				break;

			case R.id.selectDraw:
				dataList.get("dlist").select(which);
				if (text != null) {
					text.setText("" + array[which]);
				}

				new RefreshResultTask().execute("");
				break;
			case R.id.selectHomeWin:
				dataList.get("hlist").select(which);
				if (text != null) {
					text.setText("" + array[which]);
				}

				new RefreshResultTask().execute("");
				break;

			case R.id.selectAwayWin:
				dataList.get("alist").select(which);
				if (text != null) {
					text.setText("" + array[which]);
				}

				new RefreshResultTask().execute("");
				break;
			case R.id.selectAways:
			case R.id.selectLeguals:
			case R.id.selectHandicap:
			case R.id.selectHomes:
				if (text != null) {
					text.setText("" + array[which]);
				}
				break;

			default:
				break;
			}
			dialog.dismiss();
		}

	}

	public class RefreshResultTask extends
			AsyncTask<String, Integer, KeyMapBean> {

		@Override
		protected KeyMapBean doInBackground(String... params) {
			// TODO Auto-generated method stub

			HashMap<String, KeyMap> map = new HashMap<String, KeyMapBean.KeyMap>();
			HttpServiceNew serviceNew = new HttpServiceNew();
			serviceNew.getSearchHDASelections(getString(R.string.all),
					dataList.get("hlist").selectValue,
					dataList.get("dlist").selectValue,
					dataList.get("alist").selectValue, map);
			KeyMapBean ms = new KeyMapBean("0");
			ms.keyList = map;
			return ms;
		}

		@Override
		protected void onPostExecute(KeyMapBean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			String[] array = null;
			homeWinAdpater.clear();
			KeyMap key = result.keyList.get("hlist");
			dataList.put("hlist", key);
			array = key.keys;

			if (array != null) {
				for (int i = 0; i < array.length; i++) {
					homeWinAdpater.add(array[i]);
				}
			}
			awayWinAdapter.clear();
			key = result.keyList.get("alist");
			dataList.put("alist", key);
			array = key.keys;
			// array = dataList.get("alist").keys;

			if (array != null) {
				for (int i = 0; i < array.length; i++) {
					awayWinAdapter.add(array[i]);
				}
			}
			drawAdpater.clear();
			key = result.keyList.get("dlist");
			dataList.put("dlist", key);
			array = key.keys;
			// array = dataList.get("dlist").keys;

			if (array != null) {
				for (int i = 0; i < array.length; i++) {
					drawAdpater.add(array[i]);
				}
			}

		}

	}

	
	
	
	@Override
	public void onClick(View v) {
		backList = false;
		play.setSelected(false);
		analyze.setSelected(false);
		europe.setSelected(false);
		asia.setSelected(false);

		playList.setVisibility(View.GONE);
		analyzeList.setVisibility(View.GONE);
		europeList.setVisibility(View.GONE);
		asiaList.setVisibility(View.GONE);
		feedback.setSelected(false);
		requstResultView.setVisibility(View.GONE);
		feedbackLayout.setVisibility(View.GONE);
		if (v == play) {
			play.setSelected(true);
			playList.setVisibility(View.VISIBLE);
			if (playInit)
				new ZuqiuPlayTask().execute();
		} else if (v == analyze) {
			analyze.setSelected(true);
			analyzeList.setVisibility(View.VISIBLE);
			if (analyzeInit)
				new ZuqiuAnalyzeTask().execute();
		} else if (v == europe) {
			europe.setSelected(true);
			europeList.setVisibility(View.VISIBLE);
			if (europeInit)
				new ZuqiuEuropeTask().execute();
		} else if (v == asia) {
			asia.setSelected(true);
			asiaList.setVisibility(View.VISIBLE);
			if (asiaInit)
				new ZuqiuAsiaTask().execute();
		} else if (v == feedback) {
			
			feedbackLayout.setVisibility(View.VISIBLE);

			feedback.setSelected(true);
			if (feedbackInit) {
				new ZuqiuFeedBackPreTask().execute();

			}
		}
	}
}
