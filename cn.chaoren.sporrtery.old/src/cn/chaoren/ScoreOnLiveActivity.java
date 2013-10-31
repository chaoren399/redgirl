package cn.chaoren;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import cn.chaoren.SettingActivity.SettingObject;
import cn.chaoren.action.OnLiveAlertAction;
import cn.chaoren.adapter.ScoreLiveAdapter;
import cn.chaoren.net.HeaderJSONBean;
import cn.chaoren.net.HttpServiceNew;
import cn.chaoren.obj.Selection;
import cn.chaoren.util.DatabaseOpener;
import cn.chaoren.view.ProgressLinearLayout;
import cn.chaoren.widget.ItemClickListener;
import cn.chaoren.widget.ToggleView;
import cn.chaoren.R;

import com.mobclick.android.MobclickAgent;

/*
 * 比分直播界面，加载score_onlive布局文件
 */

public class ScoreOnLiveActivity extends Activity implements
		View.OnClickListener {
	private View footView, basketView;
	private final int[] menuIds = { R.id.zuqiu_tab, R.id.lanqiu_tab };// 足球比分直播,
	private View tabsView, settingView, leagelsSelectView, playingView;
	private int type = 1;// 1,足球;2,篮球
	private ScoreLiveAdapter liveAdapter, basketAdapter;
	private ListView listView;
	private View soccerLayout, basketLayout;
	private ListView baskListView;
	private List<Map<String, String>> soccerData = new ArrayList<Map<String, String>>();// 足球返回的数据
	private List<Map<String, String>> allSoccerData = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> basketBallData = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> allBasketBallData = new ArrayList<Map<String, String>>();
	private ArrayList<Selection> saveList;
	private ArrayList<Selection> oldList;
	private ArrayList<Selection> baskSaveList;
	private ArrayList<Selection> basketOldList;
	private boolean bottomShowing = false;
	private boolean needAlert = false;
	private View bottoemLayout;
	public String[] soccerStatus;// 所有状态的内容
	private String[] soccerLeguas;// 所有赛制的内容
	private ProgressLinearLayout soccerProgress;
	private ProgressLinearLayout basketProgress;
	private int currentSoccerStatus = 0;
	private int currentSoccerLeguas = 0;
	private String[] basketStatus;
	private String[] basketLeguas;
	private int currentBasketStatus = 0;
	private int currentBasketLeguas = 0;
	private TextView leagulText, statusText, basketLeagulText,
			basketStatusText;
	private HashMap<String, String> basketHeader = new HashMap<String, String>();
	public SettingObject setObject;

	public void switchTab(int type) {
		if (type == 1) {// 足球
			liveAdapter.notifyDataSetChanged();
			soccerLayout.setVisibility(View.VISIBLE);
			leagulText.setVisibility(View.VISIBLE);
			statusText.setVisibility(View.VISIBLE);
			footView.setBackgroundResource(R.drawable.tab_select);
			basketView.setBackgroundResource(R.drawable.tab_normal);
			basketLayout.setVisibility(View.GONE);
			basketLeagulText.setVisibility(View.GONE);
			basketStatusText.setVisibility(View.GONE);
		} else if (type == 2) {// 篮球
			basketAdapter.notifyDataSetChanged();
			soccerLayout.setVisibility(View.GONE);
			leagulText.setVisibility(View.GONE);
			statusText.setVisibility(View.GONE);
			footView.setBackgroundResource(R.drawable.tab_normal);
			basketView.setBackgroundResource(R.drawable.tab_select);

			basketLayout.setVisibility(View.VISIBLE);
			basketLeagulText.setVisibility(View.VISIBLE);
			basketStatusText.setVisibility(View.VISIBLE);
		}

	}

	public static boolean continueUpdate = true;

	private Handler handler = new Handler();

	private HashMap<String, String> header;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.score_onlive);

		setObject = SettingObject.getInstance(this);
		dialog = new cn.chaoren.action.AlertDialog(this);
		// alertAction.alert(1, handler, 1000);
		soccerLayout = findViewById(R.id.soccerList);
		basketLayout = findViewById(R.id.basketList);
		soccerLayout.setVisibility(View.VISIBLE);
		basketLayout.setVisibility(View.GONE);
		soccerProgress = (ProgressLinearLayout) findViewById(R.id.soccer_progress);
		soccerProgress.setVisibility(View.GONE);
		soccerProgress.initView();
		soccerProgress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new DownLoadTask().execute();
			}
		});
		basketProgress = (ProgressLinearLayout) findViewById(R.id.basket_progress);
		basketProgress.setVisibility(View.GONE);
		basketProgress.initView();
		basketProgress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new DownLoadTask().execute();
			}
		});
		// 初使化各控键
		soccerLeguas = getResources().getStringArray(R.array.leguas_array);// 从资源文件中读取赛制数据
		soccerStatus = getResources().getStringArray(R.array.status_array);// 从资源文件中读取赛制数据
		basketStatus = getResources().getStringArray(
				R.array.status_basket_array);
		leagulText = (TextView) findViewById(R.id.legualText);// 初使化所有赛制

		statusText = (TextView) findViewById(R.id.statusText);// 所有状态
		basketLeagulText = (TextView) findViewById(R.id.basketLegualText);
		basketStatusText = (TextView) findViewById(R.id.basketStatusText);
		listView = (ListView) findViewById(R.id.allGameList);// 足球的listview
		baskListView = (ListView) findViewById(R.id.basketGameList);// 篮球的listview
		tabsView = findViewById(R.id.tabsLayout);// 最顶目的条线性布局条目
		bottoemLayout = findViewById(R.id.bottomLayout);
		footView = tabsView.findViewById(R.id.zuqiu_tab);// 足球比分直播
		basketView = tabsView.findViewById(R.id.lanqiu_tab);
		// footView.setOnClickListener(this);

		// 为各按键设置点击事件
		basketView.setOnClickListener(this);
		settingView = findViewById(R.id.setting);// 提示
		settingView.setOnClickListener(this);
		leagelsSelectView = findViewById(R.id.leagueSel);// 所有赛制 的布局
		leagelsSelectView.setOnClickListener(this);
		playingView = findViewById(R.id.playingSel);
		playingView.setOnClickListener(this);
		liveAdapter = new ScoreLiveAdapter(this, 1);
		liveAdapter.setData(soccerData);
		basketAdapter = new ScoreLiveAdapter(this, 2);
		listView.setAdapter(liveAdapter);
		baskListView.setAdapter(basketAdapter);
		basketAdapter.setData(basketBallData);
		liveAdapter.setItemClick(itemClick);
		basketAdapter.setItemClick(itemClick);
		statusText.setText("" + soccerStatus[0]);// 为所有状态设置内容
		leagulText.setText("" + soccerLeguas[0]);// 为所有赛制内容
		// basketStatusText.setText("" + basketStatus[0]);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			// 为足球赛程设置点击事件，并带参数到跳转的界面
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(ScoreOnLiveActivity.this,
						ScoreLiveActivity.class);
				intent.putExtra("map",
						(HashMap<String, String>) soccerData.get(position));
				intent.putExtra("m_id", soccerData.get(position).get("m_id"));
				intent.putExtra("finish", (currentSoccerStatus == 1));
				intent.putExtra("header", header);
				startActivity(intent);
			}
		});
		baskListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(ScoreOnLiveActivity.this,
								ScoreLiveBasketActivity.class);
						intent.putExtra("map",
								(HashMap<String, String>) basketBallData
										.get(position));
						intent.putExtra("header", basketHeader);
						startActivity(intent);
					}
				});

	}

	private void vibrator() {
		Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		long[] pattern = { 800, 50, 400, 30 }; // OFF/ON/OFF/ON...
		vibrator.vibrate(pattern, -1);
	}

	private cn.chaoren.action.AlertDialog dialog = null;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setObject = SettingObject.getInstance(this);

		liveAdapter.notifyDataSetChanged();// 更新界面
		basketAdapter.notifyDataSetChanged();
		MobclickAgent.onResume(this);
		/*
		 * if (type == 2) {
		 * 
		 * } soccerFreshStop = false;
		 */
		if (type == 1) {// 如果是足球则开启一个子线程

			new Thread(new FreshOnLiveTask()).start();
		} else if (type == 2) {
			basketFreshStop = false;
			new Thread(baskFreshTask).start();
		}

	}

	private boolean soccerFreshStop = false;
	private boolean basketFreshStop = false;
	private OnLiveAlertAction alertAction = new OnLiveAlertAction(this);

	private HttpServiceNew serviceNew = new HttpServiceNew();

	public class RefreshBasketTask extends
			AsyncTask<String, Integer, HeaderJSONBean> {

		@Override
		protected HeaderJSONBean doInBackground(String... params) {
			// TODO Auto-generated method stub
			return serviceNew.refreshBasketOnLiveList();
		}

		private void replace(Map<String, String> map) {
			int size = soccerData.size();
			Map<String, String> data = null;
			String lsc = null;
			for (int i = 0; i < size; i++) {
				data = soccerData.get(i);
				if (data != null && map != null
						&& data.get("m_id").equals(map.get("m_id"))) {
					data.put("status", map.get("status"));
					data.put("lsc", map.get("lsc"));
					break;
				}
			}
		}

		@Override
		protected void onPostExecute(HeaderJSONBean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != null) {
				HashMap<String, String> header = result.header;
				String state = header.get("state");
				if (state != null) {
					List<Map<String, String>> dataList = result.bodys;
					int size = dataList.size();
					Map<String, String> map = null;
					for (int i = 0; i < size; i++) {
						map = dataList.get(i);
						replace(map);
					}

				}
				basketAdapter.notifyDataSetChanged();

			}
		}
	}

	//足球向服务器请求数据，每隔5秒刷新时，向服务器发送请求
	public class RefreshTask extends AsyncTask<String, Integer, HeaderJSONBean> {//404行

		@Override
		protected HeaderJSONBean doInBackground(String... params) {

			return serviceNew.refreshSoccerOnLiveList();
		}

		@Override
		protected void onPostExecute(HeaderJSONBean result) {

			if (result != null) {
				HashMap<String, String> header = result.header;
				String state = header.get("state");
				if (state != null) {
					List<Map<String, String>> dataList = result.bodys;
					System.out.println("刷新后的数据量："+result.bodys);
					
					int size = dataList.size();
					Map<String, String> map = null;
					for (int i = 0; i < size; i++) {
						map = dataList.get(i);
						replace(map);
					}

					if (needAlert) {
						if (setObject.getAlertType() == 1) {
							vibrator();
						} else {
							alertAction.alert(setObject.getAlertType(),
									handler, 1000);
						}
					}
				}
				liveAdapter.notifyDataSetChanged();
			}
			super.onPostExecute(result);
		}
		
		private void replace(Map<String, String> map) {
			int size = soccerData.size();
			Map<String, String> data = null;
			String lsc = null;
			for (int i = 0; i < size; i++) {
				data = soccerData.get(i);
				if (data != null && map != null
						&& data.get("m_id").equals(map.get("m_id"))) {
					data.put("half", map.get("half"));
					data.put("audience", map.get("audience"));
					lsc = map.get("lsc");
					data.put("lsc", lsc);
					if (selectionSelected(data.get("m_id"))) {
						if (needAlert) {

						} else if (setObject.rightTotal(lsc)) {
							needAlert = true;

						} else if (setObject.contains(lsc)) {
							needAlert = true;
						}
					}

					break;
				}
			}
		}
	}

	
	//篮球开启的子线程
	private Runnable baskFreshTask = new Runnable() {

		@Override
		public void run() {
			int count = 0;
			while (!basketFreshStop) {
				if (count == 0) {

				} else {
					new RefreshBasketTask().execute("");
				}
				count++;
				count %= 60;
				SystemClock.sleep(5 * 1000);
				if (currentBasketStatus != 0) {
					basketFreshStop = true;
				}
			}
		}
	};
	
	
     //足球开启的子线程
	private class FreshOnLiveTask implements Runnable {

		@Override
		public void run() {
			soccerFreshStop = false;
			int count = 0;

			//这里有两个方法可以和服务器交互
			while (!soccerFreshStop) {
				if (count == 0) {
					new DownLoadTask().execute((currentSoccerStatus + 1) + "");// 5分钟的刷新  第490行
				} else {
					// 5s的刷新
					new RefreshTask().execute();//比分更新获取数据方法      第300行
				}
  
				count++;
				count %= 60;
				SystemClock.sleep(5 * 1000);

				if (currentSoccerStatus != 0) {
					soccerFreshStop = true;
				}

			}
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		hideBottom();
		soccerFreshStop = true;
		basketFreshStop = true;
		MobclickAgent.onPause(this);
	}

	private ItemClickListener itemClick = new ItemClickListener() {

		@Override
		public void onItemClick(View view, View item, int position) {
			if (view instanceof ToggleView) {
				ToggleView toggle = (ToggleView) view;
				Selection save = new Selection();
				save.position = position;
				Map<String, String> map = null;
				if (type == 1) {
					map = soccerData.get(position);// 如果篮球也有的话，先判断一下type
					save.guid = (String) map.get("m_id");
					save.selected = toggle.isOn();
					map.put("mToggleView", save.selected + "");
					if (saveList.contains(save)) {
						saveList.remove(save);
					} else {
						saveList.add(save);
					}
					if (listEquel(saveList, oldList)) {
						hideBottom();
					} else {
						showBottom();
					}
				} else if (type == 2) {
					map = basketBallData.get(position);// 如果篮球也有的话，先判断一下type
					save.guid = (String) map.get("sessionId");
					save.selected = toggle.isOn();
					map.put("mToggleView", save.selected + "");
					if (baskSaveList.contains(save)) {
						baskSaveList.remove(save);
					} else {
						baskSaveList.add(save);
					}
					if (listEquel(baskSaveList, basketOldList)) {
						hideBottom();
					} else {
						showBottom();
					}
				}

			}
		}
	};

	/*
	 * private class GetBasketListTask extends AsyncTask<String, Integer,
	 * HeaderJSONBean> {
	 * 
	 * @Override protected HeaderJSONBean doInBackground(String... params) { //
	 * TODO Auto-generated method stub return null; }
	 * 
	 * @Override protected void onPostExecute(HeaderJSONBean result) { // TODO
	 * Auto-generated method stub super.onPostExecute(result); } }
	 */

	
	//异步加载，从服务器上获取数据
	private class DownLoadTask extends//403行
			AsyncTask<String, Integer, HeaderJSONBean> {

		@Override
		protected void onPreExecute() {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (type == 1) {
						soccerProgress.setVisibility(View.VISIBLE);
						soccerProgress.setVisibility(View.VISIBLE,
								View.VISIBLE, View.GONE, View.GONE);
						listView.setVisibility(View.GONE);
					} else if (type == 2) {
						basketProgress.setVisibility(View.VISIBLE);
						basketProgress.setVisibility(View.VISIBLE,
								View.VISIBLE, View.GONE, View.GONE);
						baskListView.setVisibility(View.GONE);
					}
				}
			});
			super.onPreExecute();
		}

		// 足球的项目
		/***
		 * 1,竞赛id @+id/sessionId 2,选中框 @+id/mToggleView 3,主队 @+id/homeNameText
		 * 4，客队 @+id/aWayNameText 5，胜 @+id/vectory 6，平 @+id/tie 7，负 @+id/lose
		 * 8，奖金 @+id/award 9，剩余时间 @+id/remainTime 10，日期 @+id/date 11，联队
		 * @+id/league
		 * 12，让分 @+id/handicap 13，半场 @+id/half 14，全场 @+id/audience
		 * 15,状态 status status
		 * 篮球的项目 1,竞赛id @+id/sessionId 2,选中框 @+id/mToggleView 3,主队
		 * @+id/homeNameText 4，客队 @+id/aWayNameText
		 */
		@Override
		protected HeaderJSONBean doInBackground(String... params) {//在后台运行与服务器交互，并获得返回值

			if (type == 1) {
				/*
				 * soccerData.clear(); allSoccerData.clear();
				 */
				//这个类连接到服务器，并解析出返回的json数据
				HttpServiceNew http = new HttpServiceNew();
				//指定联赛（默认全部），比赛状态（默认为1是比赛进行中），当前第几页（默认为1），每页个数（默认为20）
				return http.getScoreOnLiveList("", params[0] + "", null, null);
			} else if (type == 2) {

				basketBallData.clear();
				allBasketBallData.clear();
				HttpServiceNew serviceNew = new HttpServiceNew();
				return serviceNew.getBasketOnLiveList("", params[0] + "", null,
						null);
			}
			return null;
		}
          
		
		//将运行的结果，显示到UI界面 上来，result为返回的结果
		protected synchronized void onPostExecute(HeaderJSONBean result) {

			needAlert = false;
			DatabaseOpener opener = new DatabaseOpener(ScoreOnLiveActivity.this);
			List<Map<String, String>> datas = null;
			if (result != null) {
				datas = result.bodys;
				//System.out.println("返回的结果为：~~~~"+datas);
			} else {

				/**
				 * 获取数据失败，请重新选择状态!(onlive_data)
				 */
				if (type == 1) {
					soccerProgress.setVisibility(View.GONE, View.GONE,
							View.GONE, View.GONE);
					liveAdapter.notifyDataSetChanged();
					/*
					 * cn.sporttery.action.AlertDialog dialog = new
					 * cn.sporttery.
					 * action.AlertDialog(ScoreOnLiveActivity.this);
					 */
					dialog.showConfirmDialog(R.string.tint,
							R.string.onlive_data);
					return;
				} else if (type == 2) {
					basketProgress.setVisibility(View.GONE, View.GONE,
							View.VISIBLE, View.VISIBLE);
					basketAdapter.notifyDataSetChanged();
					return;
				}
			}
			if (datas == null || datas.size() == 0) {
				if (type == 1) {
					soccerProgress.setVisibility(View.GONE, View.GONE,
							View.GONE, View.GONE);
					soccerData.clear();
					allSoccerData.clear();
					liveAdapter.notifyDataSetChanged();
					/*
					 * cn.sporttery.action.AlertDialog dialog = new
					 * cn.sporttery.
					 * action.AlertDialog(ScoreOnLiveActivity.this);
					 */
					dialog.showConfirmDialog(R.string.tint,
							R.string.onlive_data);
				} else if (type == 2) {
					basketProgress.setVisibility(View.GONE, View.GONE,
							View.GONE, View.GONE);
					basketBallData.clear();
					allBasketBallData.clear();
					basketAdapter.notifyDataSetChanged();
				}
				return;
			}
			if (type == 1) {
				soccerData.clear();
				allSoccerData.clear();
				dialog.dismiss();
				soccerProgress.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				liveAdapter.rspdt = result.header.get("rspdt");
				if (liveAdapter.rspdt == null || "".equals(liveAdapter.rspdt)) {

				}
				System.out.println("li");
				// liveAdapter.setData(datas);
				// sortByKey(datas, "date");
				allSoccerData.addAll(datas);//先定义出一个集合，再将从服务器返回的数据添加到集合中去
				soccerData.addAll(datas);

				/*
				 * soccerData = (List<Map<String, String>>)
				 * ((ArrayList<Map<String,String>>)allSoccerData).clone();
				 */
				header = result.header;
				soccerLeguas = queryL_CN(soccerData, "league",//将返回的数据传入
						getString(R.string.djxzls));
				System.out.println("soccer" + soccerLeguas);
				// liveAdapter.setType(type);
				oldList = opener.queryAll(type);//到数据库查询
				if (oldList == null) {
					oldList = new ArrayList<Selection>();
				}

				Map<String, String> data = null;
				Selection selection = null;
				int length = datas.size();
				int has = length;
				String lsc = null;
				for (int i = 0; i < oldList.size(); i++) {
					selection = oldList.get(i);
					for (int j = 0; j < length; j++) {
						data = datas.get(j);
						lsc = data.get("lsc");

						if (selection.guid.equals(data.get("m_id"))) {
							has = j;
							/*
							 * if (needAlert) {
							 * 
							 * }else if (setObject.rightTotal(lsc)) { needAlert
							 * = true;
							 * 
							 * }else if (setObject.contains(lsc)) { needAlert =
							 * true; }
							 */
							data.put("mToggleView", "true");
							selection.position = j;
							selection.selected = true;
							break;
						}
					}
					if (has < length) {
						has = length;
					} else {
						oldList.remove(selection);
					}
				}

				/*
				 * if (needAlert&&currentSoccerStatus==0) {
				 * alertAction.alert(setObject.getAlertType(), handler, 1000);
				 * if (setObject.getAlertType()==1) { vibrator(); }else {
				 * alertAction.alert(setObject.getAlertType(), handler, 1000); }
				 * 
				 * needAlert = false;
				 * 
				 * }
				 */
				saveList = (ArrayList<Selection>) oldList.clone();
				liveAdapter.notifyDataSetChanged();
			} else if (type == 2) {
				basketProgress.setVisibility(View.GONE);
				baskListView.setVisibility(View.VISIBLE);
				basketAdapter.rspdt = result.header.get("rspdt");
				if (basketAdapter.rspdt == null
						|| "".equals(basketAdapter.rspdt)) {

				}
				System.out.println("li");
				// liveAdapter.setData(datas);
				allBasketBallData.addAll(datas);
				basketBallData.addAll(datas);

				/*
				 * soccerData = (List<Map<String, String>>)
				 * ((ArrayList<Map<String,String>>)allSoccerData).clone();
				 */
				basketHeader = result.header;
				basketLeguas = queryL_CN(basketBallData, "league",
						getString(R.string.qxzls));
				System.out.println("soccer" + soccerLeguas);
				// liveAdapter.setType(type);
				basketOldList = opener.queryAll(type);
				if (basketOldList == null) {
					basketOldList = new ArrayList<Selection>();
				}
				int size = basketOldList.size();
				Map<String, String> data = null;
				Selection selection = null;
				int length = datas.size();
				int has = length;
				String lsc = null;
				for (int i = 0; i < size; i++) {
					selection = basketOldList.get(i);
					for (int j = 0; j < length; j++) {
						data = datas.get(j);
						lsc = data.get("lsc");
						/*
						 * if (needAlert) {
						 * 
						 * }else if (setObject.rightTotal(lsc)) { needAlert =
						 * true;
						 * 
						 * }else if (setObject.contains(lsc)) { needAlert =
						 * true; }
						 */
						if (selection.guid.equals(data.get("m_id"))) {
							has = j;
							data.put("mToggleView", "true");
							selection.position = j;
							selection.selected = true;
							break;
						}
					}
					if (has < length) {
						has = length;
					} else {
						basketOldList.remove(selection);
					}
				}

				baskSaveList = (ArrayList<Selection>) basketOldList.clone();
				liveAdapter.notifyDataSetChanged();
			}
		};
		/**
		 * @param data  服务器返回的数据
		 * @param key
		 * @param first 所有赛制类型
		 * @return
		 */
		private String[] queryL_CN(List<Map<String, String>> data, String key,
				String first) {
			ArrayList<String> les = new ArrayList<String>();
			String[] array = null;
			if (data != null && data.size() > 0) {
				int size = data.size();
				System.out.println("size." + size);
				String value = null;
				for (int i = 0; i < size; i++) {
					value = data.get(i).get(key);
					if (les.contains(value)) {//判断是否包含
						continue;
					} else {
						les.add(value);
					}
				}
			}
			leagulText.setText(first);//添加传入的赛制参数
			les.add(0, first);
			int length = les.size();
			array = new String[length];
			array = les.toArray(array);
			return array;
		}
		/*
		 * private List<Map<String, String>> replaceAddRefresh(List<Map<String,
		 * String>> newOne,List<Map<String, String>> oldOne) {
		 * 
		 * }
		 */
	};

	public <E extends Object> boolean listEquel(List<E> list1, List<E> list2) {
		int size1 = -1;
		int size2 = -1;
		if (list1 != null) {
			size1 = list1.size();
		}
		if (list2 != null) {
			size2 = list2.size();
		}
		if (size1 == size2) {
			E e1, e2;
			boolean noEquals = false;
			for (int i = 0; i < size1; i++) {
				e1 = list1.get(i);
				e2 = list2.get(i);
				if (!e1.equals(e2)) {
					noEquals = true;
					break;
				}
			}
			return !noEquals;
		}

		return false;
	}

	public void onBackPressed() {
		getParent().onBackPressed();
		// onBackPressed();
	};

	private void toSave() {
		if (type == 1) {
			oldList = (ArrayList<Selection>) saveList.clone();
			System.out.println("to save");
			DatabaseOpener opener = new DatabaseOpener(this);
			opener.deleteAll(type);
			opener.insertAll(saveList, type);
		} else if (type == 2) {
			basketOldList = (ArrayList<Selection>) baskSaveList.clone();
			System.out.println("to save");
			DatabaseOpener opener = new DatabaseOpener(this);
			opener.deleteAll(type);
			opener.insertAll(baskSaveList, type);
		}

	}

	public void destory() {
		soccerFreshStop = true;
		basketFreshStop = true;
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}

	
	
	
	@Override
	public void onClick(View view) {
		Intent intent = null;
		switch (view.getId()) {
		case R.id.zuqiu_tab:
			System.out.println("足球");
			type = 1;
			switchTab(1);
			settingView.setVisibility(View.VISIBLE);
			/* basketFreshStop = true; */
			if (soccerFreshStop) {

				new Thread(new FreshOnLiveTask()).start();
			}

			break;
		case R.id.lanqiu_tab:
			System.out.println("go...");
			type = 2;
			/* soccerFreshStop = true; */
			switchTab(2);

			if (basketFreshStop) {
				basketFreshStop = false;
				new Thread(baskFreshTask).start();
			}
			settingView.setVisibility(View.GONE);
			break;
		case R.id.setting://提示
			intent = new Intent(this, SettingActivity.class);
			intent.putExtra("type", type);
			startActivity(intent);
			break;
		case R.id.leagueSel://所有赛制
			if (type == 1) {

				showSelectDiloag("soccerLeguas", soccerLeguas);
			} else if (type == 2) {

				showSelectDiloag("basketLeguas", basketLeguas);
			}
			break;
		case R.id.playingSel://所有比赛状态
			if (type == 1) {
				showSelectDiloag("soccerStatus", soccerStatus);
			} else if (type == 2) {
				showSelectDiloag("basketStatus", basketStatus);
			}
			break;
		case R.id.ok:
			toSave();
			hideBottom();
			break;
		case R.id.cancel:
			toCancel();
			hideBottom();
			break;
		default:
			break;
		}

	}

	private void toCancel() {
		if (type == 1) {
			Map<String, String> data = null;
			Selection selection = null;
			int length = soccerData.size();
			int has = length;
			String lsc = null;
			for (int i = 0; i < oldList.size(); i++) {
				selection = oldList.get(i);
				for (int j = 0; j < length; j++) {
					data = soccerData.get(j);
					lsc = data.get("lsc");

					if (selection.guid.equals(data.get("m_id"))) {
						has = j;
						data.put("mToggleView", "true");
						selection.position = j;
						selection.selected = true;
						break;
					}
				}
				if (has < length) {
					has = length;
				} else {
					oldList.remove(selection);
				}
			}
			saveList = (ArrayList<Selection>) oldList.clone();
			liveAdapter.notifyDataSetChanged();
		}

	}

	private boolean selectionSelected(String mid) {
		int size = oldList.size();
		Selection s = null;
		for (int i = 0; i < size; i++) {
			s = oldList.get(i);
			if (s.equals(mid)) {
				return true;
			}
		}
		return false;
	}

	//可以选择的对话框
	private AlertDialog showSelectDiloag(final String mType,
			final String[] items) {
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		String title = "";
		if ("soccerStatus".equals(mType)) {
			title = getString(R.string.select_status);
		} else if ("soccerLeguas".equals(mType)) {
			title = getString(R.string.qxzls);
		} else if ("basketLeguas".equals(mType)) {
			title = getString(R.string.qxzls);
		} else if ("basketStatus".equals(mType)) {
			title = getString(R.string.select_status);
		}
		adb.setTitle("" + title);
		System.out.println("items：" + items);
		/* 、 */
		adb.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (type == 1) {
					String item = items[which];
					if ("soccerStatus".equals(mType)) {

						currentSoccerStatus = which;
						statusText.setText("" + item);
						/*
						 * new
						 * DownLoadTask().execute(""+(currentSoccerStatus+1));
						 */
						/*
						 * allSoccerData.clear(); soccerData.clear();
						 */
						// liveAdapter.notifyDataSetChanged();
						new Thread(new FreshOnLiveTask()).start();

					} else if ("soccerLeguas".equals(mType)) {
						/*
						 * allSoccerData.clear(); soccerData.clear();
						 */
						/* liveAdapter.notifyDataSetChanged(); */
						currentSoccerLeguas = which;
						leagulText.setText("" + item);
						/*
						 * new
						 * DownLoadTask().execute(""+(currentSoccerStatus+1));
						 */
						selectData();

					}
				} else if (type == 2) {
					String item = items[which];
					if ("basketStatus".equals(mType)) {
						if (which != currentBasketStatus) {
							currentBasketStatus = which;
							basketStatusText.setText("" + item);
							// selectData();
							/*
							 * new
							 * DownLoadTask().execute(""+(currentBasketStatus
							 * +1));
							 */
							basketBallData.clear();
							allBasketBallData.clear();
							basketAdapter.notifyDataSetChanged();
							new Thread(new FreshOnLiveTask()).start();
						}
					} else if ("soccerLeguas".equals(mType)) {

						currentSoccerLeguas = which;
						basketLeagulText.setText("" + item);
						/*
						 * basketBallData.clear(); allBasketBallData.clear();
						 */
						/* basketAdapter.notifyDataSetChanged(); */
						selectData();

					}
				}
				dialog.dismiss();
			}
		});
		AlertDialog dialog = adb.create();
		dialog.show();
		return dialog;
	}

	private void selectData() {
		if (type == 1) {
			if (allSoccerData == null) {
				return;
			}
			int size = allSoccerData.size();
			Map<String, String> map = null;
			soccerData.clear(); // boolean statusPass = false;

			if (currentSoccerLeguas == 0) {
				soccerData.addAll(allSoccerData);
			} else {
				for (int i = 0; i < size; i++) {
					map = allSoccerData.get(i);
					if ((soccerLeguas[currentSoccerLeguas] + "").equals(map
							.get("league"))) {
						// TODO
						soccerData.add(map);
					}
				}
			}
			// statusPass = 0== currentSoccerStatus;

			liveAdapter.notifyDataSetChanged();
		} else if (type == 2) {
			if (allBasketBallData == null) {
				return;
			}
			int size = allBasketBallData.size();
			Map<String, String> map = null;
			basketBallData.clear();
			boolean leagulPass = false;
			leagulPass = 0 == currentBasketLeguas;
			// statusPass = soccerStatus[0].equals("" + currentSoccerStatus);
			for (int i = 0; i < size; i++) {
				map = allBasketBallData.get(i);
				if ((leagulPass || (soccerLeguas[currentBasketLeguas] + "")
						.equals(map.get("league")))) {
					// TODO
					basketBallData.add(map);
				}
			}
			basketAdapter.notifyDataSetChanged();
		}
	}

	private void showBottom() {
		if (!bottomShowing) {
			bottoemLayout.setVisibility(View.VISIBLE);
			bottomShowing = true;
		}
	}

	private void sortByKey(List<Map<String, String>> dataList, String key) {
		String temp1, temp2;
		Map<String, String> map1, map2;
		int com, size = dataList.size();
		for (int i = 0; i < size - 1; i++) {
			for (int j = i + 1; j < size; j++) {
				map1 = dataList.get(i);
				map2 = dataList.get(j);
				temp1 = map1.get(key);
				temp2 = map2.get(key);
				System.out.println("temp1:" + temp1);
				System.out.println("temp2:" + temp2);

				if (temp1 != null && temp2 != null) {
					com = temp1.compareTo(temp2);
					System.out.println("com:" + com);
					if (com > 0) {
						dataList.set(i, map2);
						dataList.set(j, map1);
					}
				}
			}

		}
	}

	private void hideBottom() {
		if (bottomShowing) {
			bottoemLayout.setVisibility(View.GONE);
			bottomShowing = false;
		}
	}

}
