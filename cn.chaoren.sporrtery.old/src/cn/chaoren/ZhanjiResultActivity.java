package cn.chaoren;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.chaoren.db.DatabaseOpener1;
import cn.chaoren.util.ZhanjiresultData;
import cn.chaoren.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ZhanjiResultActivity extends Activity {

	public static final String TAG = "ZhanjiResultActivity";
	private SharedPreferences shp;
	private TextView zhushu,beishu1,fangshi,zhongjiang,touzhu,tv_time,tv_fee;
	private ListView list_zhanji;
	List<Map<String, Object>> zhanresult;
	String play_Time;
	List<String> result1;
	String bet_style = null;
	List<String> liststatus;
	public String matchResult1;
	HashMap<String, String> map1=null;
	public String win_fee=null;
	public int win_status=0;
	private boolean isFromResultService;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.zhanjixiangqi_result);
		shp = getSharedPreferences("zhanbiao", MODE_PRIVATE);

		Intent intent=getIntent();
		isFromResultService=intent.getBooleanExtra("fromResultService", false);
		
		String _id = getIntent().getStringExtra("_id");
	
		DatabaseOpener1 open = new DatabaseOpener1(ZhanjiResultActivity.this);
		String zhanjiresult = open.findZhanji(_id);

		
		
		
		// 获得最大时间
		int play_Timee = open.findPlay_Time(_id);
   
		Date date = new Date(play_Timee * 1000l);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		play_Time = sdf.format(date);
		 matchResult1=shp.getString("win_match", "");
		 
		 win_fee=shp.getString("win_fee", "");

        zhanresult = ZhanjiresultData.getZhanji(zhanjiresult);
        map1=new HashMap<String, String>();
        for(int i=0;i<zhanresult.size();i++){
        	String match_id = (String) zhanresult.get(i).get("match_id");
        	//解析matchResult
        	JSONArray jsonObjs;
			try {
				jsonObjs = new JSONArray(matchResult1);
				
				for (int j = 0; j < jsonObjs.length(); j++) {
					JSONObject jsonObj = jsonObjs.optJSONObject(i);
					 
					String match_id1= jsonObj.optString(match_id);
					
					 map1.put(match_id, match_id1);
	        }
			} catch (JSONException e) {
				e.printStackTrace();
			}
        }	
		

		list_zhanji = (ListView) findViewById(R.id.list_zhanji);
		zhushu = (TextView) findViewById(R.id.zhushu);
		beishu1 =(TextView) findViewById(R.id.beishu);
		fangshi = (TextView) findViewById(R.id.fangshi);
		touzhu = (TextView) findViewById(R.id.touzhu);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_fee=(TextView) findViewById(R.id.tv_fee);
		
		zhongjiang = (TextView) findViewById(R.id.zhongjiang);
		ListAdapter listAdapter = new ListAdapter();
		list_zhanji.setAdapter(listAdapter);
		listAdapter.notifyDataSetChanged();

		fillData();
		
	}       

	private void fillData() {
		String stime = shp.getString("stime", "");
		Log.i(TAG, "stime"+stime);
		String staketx = shp.getString("staketx", "");
		String moneytx = shp.getString("moneytx", "");
		String fee = shp.getString("fee", "");

		String beishu = shp.getString("beishu", "");
		String bet_style = shp.getString("bet_style", "");
		win_status=shp.getInt("win_status", win_status);
		int  status=shp.getInt("status", 0);
		String foretx=shp.getString("foretx", "");
		
		
		tv_time.setText("发布时间："+stime);
		tv_fee.setText("预测中奖金额："+"("+foretx+")");
		
		touzhu.setText(fee+"元");
		zhushu.setText(staketx + "注");
		fangshi.setText(bet_style);
		beishu1.setText(beishu+"倍");
		if(status==0){
			zhongjiang.setText("  未开奖");
			
		}else if(win_status==0){
			zhongjiang.setText("未中奖");
			tv_fee.setVisibility(8);
		}else{
			zhongjiang.setText(win_fee+"元");
			zhongjiang.setTextColor(Color.parseColor("#FF0000"));
			tv_fee.setVisibility(8);
		}
		zhushu.setText(staketx + "注");
	}

	public class ListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return zhanresult.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
   
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view = View.inflate(ZhanjiResultActivity.this,
					R.layout.zhanjixiangqin_item, null);

			TextView day1 = (TextView) view.findViewById(R.id.day1);
			TextView day2 = (TextView) view.findViewById(R.id.day2);
			TextView zhanji_zhudui = (TextView) view
					.findViewById(R.id.zhanji_zhudui);
			TextView zhanji_kedui = (TextView) view
					.findViewById(R.id.zhanji_kedui);
			TextView zhanji_sheng = (TextView) view
					.findViewById(R.id.zhanji_sheng);
			TextView zhanji_ping = (TextView) view
					.findViewById(R.id.zhanji_ping);
			TextView zhanji_fu = (TextView) view
					.findViewById(R.id.zhanji_fu);
			TextView caiguo = (TextView) view
					.findViewById(R.id.caiguo);
			TextView touzhu = (TextView) view
					.findViewById(R.id.touzhu);
            
			Date date = new Date(Integer.valueOf((String) zhanresult.get(
					position).get("play_time")) * 1000l);
			/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
			String stime = sdf.format(date);
			String stime1 = sdf1.format(date);*/
			
			String dayy = (String) zhanresult.get(position).get("match_name");
			   String []dd = dayy.split("");
			   day1.setText(dd[1]+dd[2]);
			   day2.setText(dd[3]+dd[4]+dd[5]);
			      
			result1 = (List<String>) zhanresult.get(position).get("num_str");
			
			if (result1.contains(String.valueOf(3))) {
				touzhu.setText("胜");
			//	zhanji_sheng.setTextColor(Color.parseColor("#DC143C"));
			} 
            if (result1.contains(String.valueOf(1))) {
            	touzhu.setText("平");
            //	zhanji_ping.setTextColor(Color.parseColor("#DC143C"));
			} 
            if (result1.contains(String.valueOf(0))) {
            	touzhu.setText("负");
            	//zhanji_fu.setTextColor(Color.parseColor("#DC143C"));
			}
            if(result1.contains(String.valueOf(3))&&result1.contains(String.valueOf(1))){
            	touzhu.setText("胜"+"平");
            }
            if(result1.contains(String.valueOf(3))&&result1.contains(String.valueOf(0))){
            	touzhu.setText("胜"+"负");
            }
            if(result1.contains(String.valueOf(1))&&result1.contains(String.valueOf(0))){
            	touzhu.setText("平"+"负");
            }
            if(result1.contains(String.valueOf(3))&&result1.contains(String.valueOf(0))&&result1.contains(String.valueOf(1))){
            	touzhu.setText("胜"+"平"+"负");
            }

			zhanji_zhudui.setText((String) zhanresult.get(position).get(
					"team_host"));// 主队
			zhanji_kedui.setText((String) zhanresult.get(position).get(
					"team_visiting"));// 客队
			
			zhanji_sheng.setText("胜："+(String)zhanresult.get(position).get("sp3"));
			zhanji_ping.setText("平："+(String)zhanresult.get(position).get("sp1"));
			zhanji_fu.setText("负："+(String)zhanresult.get(position).get("sp0"));

			String match_id = (String) zhanresult.get(position).get("match_id");
			
	   
				if(map1.size()==0){
					caiguo.setText("比赛中");
				}
				else if(map1.get(match_id).equals("3")){
					caiguo.setText("胜");
				}else if(map1.get(match_id).equals("1")){
					caiguo.setText("平");
				}else if(map1.get(match_id).equals("0")){
					caiguo.setText("负");
				}else if(map1.get(match_id).equals("cancel")){
					caiguo.setText("取消");
				}else {
					caiguo.setText("比赛中");
				}
					
			return view;
		}
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
