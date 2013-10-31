package cn.chaoren;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import com.mobclick.android.MobclickAgent;

import cn.chaoren.action.OnLiveAlertAction;
import cn.chaoren.action.SelectAlertDialog;
import cn.chaoren.action.StatusAction;
import cn.chaoren.adapter.ScoreLiveAdapter;
import cn.chaoren.adapter.ScoreOnLiveAdapter;
import cn.chaoren.net.HeaderJSONBean;
import cn.chaoren.net.HttpServiceNew;
import cn.chaoren.obj.ScoreOnLive;
import cn.chaoren.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ScoreLiveActivity extends Activity implements View.OnClickListener{
	private HashMap<String, String> data;
	private TextView homeNameText,homeNameTextTitle,awayNameText,awayNameTextTitle,leagulText,sessionIdText,timeText,
					 halfText,audienceText,statusText,rewardText,scoreText,handText;
	private String[] soccerStatus;
	private ListView listview;
	private ArrayList<ScoreOnLive> dataList = new ArrayList<ScoreOnLive>();
	private ScoreOnLiveAdapter adapter;
	private String status;
	private String mid;
	private boolean stopFresh = false;
	private StatusAction statusAction;
	private boolean finish = false;
	private OnLiveAlertAction onliveAction = new OnLiveAlertAction(this);
     @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
    	soccerStatus = getResources().getStringArray(R.array.status_shown_array);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	setContentView(R.layout.score_onlive_detail);
        data = (HashMap<String, String>) getIntent().getSerializableExtra("map"); 
        if (data == null) {
			onBackPressed();
		}
        mid = getIntent().getStringExtra("m_id");
        finish = getIntent().getBooleanExtra("finish", false);
        listview = (ListView) findViewById(R.id.messageListView);
        adapter = new ScoreOnLiveAdapter(this);
        new Thread(getIncreasements).start();
        stopFresh = false;
        firstAccess = true;
        firstDownLoad = true;
        firstHalf = true;
        /*listview.setAdapter(adapter);*/
        statusAction = new StatusAction();
        init();
        
    }
     
     private boolean dialogPrepared = true;
     
     @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	if (firstAccess) {
			
		}
    	if (stopFresh) {
			stopFresh = false;
			new Thread(getIncreasements).start();
		}
    	System.out.println("go");
    	MobclickAgent.onResume(this);
    	
    }
     
     @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	MobclickAgent.onPause(this);
    	stopFresh = true;
    	goback = true;
    }
     
     
     
     private HashMap<String, String> header = null;
     /***
	  * 1,竞赛id @+id/sessionId    sessionId
	  * 2,选中框 @+id/mToggleView   mToggleView
	  * 3,主队  @+id/homeNameText   homeNameText
	  * 4，客队 @+id/aWayNameText   aWayNameText
	  * 5，胜   @+id/vectory    vectory
	  * 6，平 @+id/tie   tie
	  * 7，负   @+id/lose  lose
	  * 8，奖金  @+id/award award
	  * 9，剩余时间 @+id/remainTime  remainTime
	  * 10，日期   @+id/date  date
	  * 11，联队 @+id/league league
	  * 
	  * 12，让分 @+id/handicap  handicap
	  * 13，半场 @+id/half  half
	  * 14，全场 @+id/audience  audience
	  * 
	  * 15,状态 status status
	  */
     private void init()
     {
    	 
    	  header = (HashMap<String, String>) getIntent().getSerializableExtra("header");
    	 adapter.setOnLiveList(dataList);
    	 listview.setAdapter(adapter);
    	 //adapter.notifyDataSetChanged();
    	 if (mid == null) {
    		 mid = data.get("m_id");
		}
    	 if (mid == null||"".equals(mid)) {
			onBackPressed();
			return;
		}
    	 //new GetDetailTask().execute(mid);
    	 //获取对象
    	 homeNameText = (TextView) findViewById(R.id.homeNameText);
    	 homeNameTextTitle = (TextView) findViewById(R.id.homeNameText_title);
    	 awayNameText = (TextView) findViewById(R.id.awayNameText);
    	 awayNameTextTitle = (TextView) findViewById(R.id.awayNameText_title);
    	 leagulText = (TextView) findViewById(R.id.legualText);
    	 sessionIdText = (TextView) findViewById(R.id.sesseionId);
    	 timeText = (TextView) findViewById(R.id.time);
    	 halfText = (TextView) findViewById(R.id.half);
    	 audienceText = (TextView) findViewById(R.id.audience);
    	 statusText = (TextView) findViewById(R.id.status);
    	 rewardText = (TextView) findViewById(R.id.reward);
    	 scoreText = (TextView) findViewById(R.id.scoreText);
    	 handText = (TextView) findViewById(R.id.handText);
    	 
    	 //初始化对象
    	 homeNameText.setText("" + data.get("homeNameText"));
    	 homeNameTextTitle.setText("" + data.get("homeNameText"));
    	 awayNameText.setText("" + data.get("aWayNameText"));
    	 awayNameTextTitle.setText("" + data.get("aWayNameText"));
    	 leagulText.setText("" + data.get("league"));
    	 sessionIdText.setText("" + data.get("sessionId"));
    	 leagulText.setText("" + data.get("league"));
    	 halfText.setText("" + data.get("half"));
    	 audienceText.setText("" + data.get("audience"));
    	 //status =soccerStatus[new Integer(data.get("status")) - 1];
    	 status = (String) data.get("status");
    	// stopFresh = !ScoreLiveAdapter.showStatus(soccerStatus, statusText, (String) data.get("status"), (String) data.get("date"), header.get("rspdt"));
    	 stopFresh = !statusAction.setStatus(statusText, soccerStatus, status, data);
    	 //now date
	    // statusText.setText("" + status);
	     //rewardText.setText("" + data.get("aWayNameText"));//不需要设置什么的
	     //scoreText.setText("" + data.get("aWayNameText"));//动态比分
    	
    	 
    	 String handicap = (String) data.get("handicap");
    	 if (handicap == null) {
			handicap="";
		}
    	 handText.setText("" + handicap);//    	 
    	 
    	 /*if ("1".equals(status)) {
			rewardText.setVisibility(View.VISIBLE);
		}else
		{
			rewardText.setVisibility(View.GONE);
		}    	 */
    	 timeText.setText("" + data.get("date"));//当前时间

    	 
     }
     
    
     
     private boolean goback = false;
    
     private void initStatus(HashMap<String, String> header)
     {
    	 scoreText.setText("" + header.get("lsc"));
    	 halfText.setText("" + header.get("half"));
    	 audienceText.setText("" + header.get("full"));
    	 
    	 //stopFresh = !ScoreLiveAdapter.showStatus(soccerStatus, statusText, header.get("status"), (String) data.get("date"), header.get("rspdt"));
        stopFresh = !statusAction.setStatus(statusText, soccerStatus, header.get("status"), header);
        if (!goback&&stopFresh&&!finish) {
			//已完场
        
           SelectAlertDialog.showConfirmDialog(this, getString(R.string.onlive_tint), getString(R.string.gameover));
		}
     }
     
     
     protected String getTime()
     {
    	SimpleDateFormat dataFormat = new SimpleDateFormat("HH:mm");
    	
    	return dataFormat.format(new Date());
     }
     
     private ArrayList<ScoreOnLive> newEventList = new ArrayList<ScoreOnLive>();
     
     
     
     
     public class GetDetailTask extends AsyncTask<String, Integer, HeaderJSONBean>
     {

		@Override
		protected HeaderJSONBean doInBackground(String... params) {
			HttpServiceNew serviceNew = new HttpServiceNew();
			
			return serviceNew.getSoccerOnLiveDetail(params[0]);
		}
    	
		@Override
		protected void onPostExecute(HeaderJSONBean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result == null) {
				adapter.notifyDataSetChanged();
				return;
			}
			List<Map<String, String>> datas = result.bodys;
			initStatus(result.header);
			String sta = result.header.get("status");
			if ("2".equals(sta)) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						if (!goback&&firstHalf) {							
							SelectAlertDialog.showConfirmDialog(ScoreLiveActivity.this, getString(R.string.onlive_tint), getString(R.string.alert_half));							
							firstHalf = false;
						}
					}
				});
			}
			newEventList.clear();
			ArrayList<ScoreOnLive> tempList =  null;
			if (datas != null && datas.size()>0) {
				 ScoreOnLive onLive = null;
				 tempList = new ArrayList<ScoreOnLive>();
				 Map<String, String> map = null;
				 
				 int size = datas.size();
		    	 for (int i = 0; i < size; i++) {
		    		 map = datas.get(i);
					onLive = new ScoreOnLive();
					/*onLive.home = i%2==0;
					onLive.player = "QQ";*/
					onLive.time = map.get("t");
					onLive.eid = new Integer(map.get("eid"));
					onLive.ho = map.get("ho");
					onLive.ao = map.get("ao");
					if (!dataList.contains(onLive)) {
							newEventList.add(onLive);
					}
					tempList.add(onLive);
				}
		    	
			}
			dataList.clear();
			
if (tempList !=null) {
	dataList.addAll(tempList);
			}
			
			
			adapter.notifyDataSetChanged();
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					if (!firstDownLoad&&newEventList.size()>0) {
						
						SelectAlertDialog.showConfirmDialog(ScoreLiveActivity.this, getString(R.string.onlive_tint), new AlertAdapter());
						
					}else
					{
						firstDownLoad = false;
					}
				}
			});
			
			
			
			
		}
     }
     
     public class AlertAdapter extends BaseAdapter
     {
        private String[]array =null;
    	 public AlertAdapter() {
			array = getResources().getStringArray(R.array.events);
		}
		@Override
		public int getCount() {
			if (newEventList != null) {
				return newEventList.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			if (newEventList!=null) {
				return newEventList.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			ScoreOnLive onLive = (ScoreOnLive) getItem(position);
			return onLive.eid;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.eid_item, null);
			}
			
			ScoreOnLive score = (ScoreOnLive) getItem(position);
			if (score != null) {
				TextView text = (TextView) convertView.findViewById(R.id.player);
				if (null!=score.ho&&!"".equals(score.ho)) {
					text.setText(score.ho+"");
				}else if(null!=score.ao&&!"".equals(score.ao))
				{
					text.setText(score.ao+"");
				}
				text = (TextView) convertView.findViewById(R.id.time);
				text.setText("" + score.time);
				text = (TextView) convertView.findViewById(R.id.eid);
				int index = 0;
				
				switch (score.eid) {
				case 1:
					index = 0;
					break;
				case 2:
					index = 4;
						break;
				case 3:
				index = 3;
					break;
				case 7:
					index = 1;
					
					break;
				case 8:
					index = 2;
					
					break;
				case 9:
					index = 5;
					
					break;
					
				case 55:
					index = 6;
					
					break;
				case 11:
					index = 7;
					
					break;
				case 4:
					index = 8;
					
					break;
				case 5:
					index = 9;
					break;
				default:
					break;
				}
				text.setText(array[index]);
			}
			
			return convertView;
		}
    	 
     }
     
    /* public class GetIncrease extends AsyncTask<String, Integer, HeaderJSONBean>
     {

		@Override
		protected HeaderJSONBean doInBackground(String... params) {
			// TODO Auto-generated method stub
			return null;
		}
    	 
     }*/
     private boolean firstDownLoad = true;
     private boolean firstHalf = true;
     private boolean firstAccess= true;
     
     private Runnable getIncreasements = new Runnable() {
		
		@Override
		public void run() {
			while (!stopFresh) {
				if (firstAccess||"1".equals(status)||"3".equals(status)||"2".equals(status)) {
					new GetDetailTask().execute("" + mid);
					firstAccess = false;
				}
				
				SystemClock.sleep(15*1000);
				//刷新新增接口
				
			}
			
		}
	};
     
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reward:
			Intent intent = new Intent(this,
					ZuqiuResultDetailActivity_temp.class);
			intent.putExtra("date", data.get("date")+"");
			intent.putExtra("num", data.get("sessionId")+"");
			intent.putExtra("m_id", data.get("m_id")+"");
			intent.putExtra("l_cn", data.get("league")+"");
			intent.putExtra("h_cn", data.get("homeNameText")+"");
			intent.putExtra("a_cn", data.get("aWayNameText")+"");
			startActivity(intent);
			System.out.println("yes");
			break;
		default:
			break;
		}
		
	}
}
