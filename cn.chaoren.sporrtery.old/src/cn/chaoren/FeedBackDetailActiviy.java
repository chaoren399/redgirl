package cn.chaoren;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobclick.android.MobclickAgent;

import cn.chaoren.adapter.CompletionAdapter;
import cn.chaoren.net.JSONBean;
import cn.chaoren.R;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class FeedBackDetailActiviy extends Activity {
	private int type = 1;//1,足球；2，篮球
    private String homeStr,awayStr;
    private ListView completionListView;
    private CompletionAdapter adapter;
    private ArrayList<Map<String, String>> dataList = new ArrayList<Map<String,String>>();
    private Map<String, String> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.feedback_detail);
    	data = (HashMap<String, String>) getIntent().getSerializableExtra("map");
    	TextView temp = (TextView) findViewById(R.id.leagulText);
    	temp.setText("(" + data.get("legual")+")");
    	temp.setVisibility(View.VISIBLE);
    	type = getIntent().getIntExtra("type", 1);
    	dataList = new ArrayList<Map<String,String>>();
    	completionListView = (ListView) findViewById(R.id.detail_list);
    	adapter = new CompletionAdapter(this,type);
    	adapter.setDataList(dataList);
    	completionListView.setAdapter(adapter);   
    	new FeedbackDetailTask().execute();
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	MobclickAgent.onResume(this);
    }
    
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	MobclickAgent.onPause(this);
    }
    
    class FeedbackDetailTask extends AsyncTask<String, Integer, JSONBean>
    {
         
		@Override
		protected JSONBean doInBackground(String... params) {
			
			JSONBean ms = new JSONBean("0");
			if (type == 1) {
				List<Map<String, String>> data = ms.bodys;
				Map<String, String> map = null;
				map = new HashMap<String, String>();
				data.add(map);
				map.put("date", "12-01-12");
				map.put("zhudui", "戈多");
				map.put("kedui", "克隆竞技");
				map.put("banchang", "0:0");
				map.put("quanchang", "0:0");
				map.put("caiguo", "负");
				map.put("h", "2.25");
				map.put("d", "3.25");
				map.put("a", "4.30");			
				
				map = new HashMap<String, String>();
				data.add(map);
				map.put("date", "12-01-11");
				map.put("zhudui", "克隆竞技");
				map.put("kedui", "戈多");
				map.put("banchang", "0:0");
				map.put("quanchang", "0:0");
				map.put("caiguo", "负");
				map.put("h", "2.25");
				map.put("d", "3.25");
				map.put("a", "4.30");
				
				map = new HashMap<String, String>();
				data.add(map);
				map.put("date", "12-01-13");
				map.put("zhudui", "戈多");
				map.put("kedui", "克隆竞技");
				map.put("banchang", "0:0");
				map.put("quanchang", "0:0");
				map.put("caiguo", "负");
				map.put("h", "2.25");
				map.put("d", "3.25");
				map.put("a", "4.30");
			}else if (type == 2) {
				List<Map<String, String>> data = ms.bodys;
				Map<String, String> map = null;
				map = new HashMap<String, String>();
				data.add(map);
				map.put("date", "12-01-12");
				map.put("zhudui", "戈多");
				map.put("kedui", "克隆竞技");
				map.put("shengfu", "负");
				map.put("quanchang", "0:0");
				map.put("dyj", "负");
				map.put("dej", "2.25");
				map.put("dsj", "3.25");
				map.put("dsij", "4.30");
				map.put("rangfenzhufu", "2.25");
				map.put("shengfencha", "3.25");
				map.put("daxiaofen", "4.30");
				
				map = new HashMap<String, String>();
				data.add(map);
				map.put("date", "12-01-12");
				map.put("zhudui", "戈多");
				map.put("kedui", "克隆竞技");
				map.put("shengfu", "负");
				map.put("quanchang", "0:0");
				map.put("dyj", "负");
				map.put("dej", "2.25");
				map.put("dsj", "3.25");
				map.put("dsij", "4.30");
				map.put("rangfenzhufu", "2.25");
				map.put("shengfencha", "3.25");
				map.put("daxiaofen", "4.30");
				
				
				map = new HashMap<String, String>();
				data.add(map);
				map.put("date", "12-01-12");
				map.put("zhudui", "戈多");
				map.put("kedui", "克隆竞技");
				map.put("shengfu", "负");
				map.put("quanchang", "0:0");
				map.put("dyj", "负");
				map.put("dej", "2.25");
				map.put("dsj", "3.25");
				map.put("dsij", "4.30");
				map.put("rangfenzhufu", "2.25");
				map.put("shengfencha", "3.25");
				map.put("daxiaofen", "4.30");
			}
			
			return ms;
		}
    	
		
		@Override
		protected void onPostExecute(JSONBean result) {
			
			if (result!=null) {
				dataList.clear();
				dataList.addAll(result.bodys);
				adapter.notifyDataSetChanged();
			}
			super.onPostExecute(result);
		}
    }
}
