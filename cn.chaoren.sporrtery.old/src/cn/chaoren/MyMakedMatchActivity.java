package cn.chaoren;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.chaoren.db.DatabaseOpener1;
import cn.chaoren.db.MyBettingDO;
import cn.chaoren.util.DBService;
import cn.chaoren.R;

import com.mobclick.android.MobclickAgent;

public class MyMakedMatchActivity extends Activity {
	


	protected static final String TAG = "MyMakedMatchActivity";

	private TextView zuqiu_tab, lanqiu_tab;
	private ListView zuqiuGuanzhuListView;
	private ZuqiuGuanzhuListAdapter zuqiuGuanzhuListAdapter;
	private List<Map<String, String>> zuqiuLists;

	private ListView lanqiuGuanzhuListView,zhanji_list;
	private LanqiuGuanzhuListAdapter lanqiuGuanzhuListAdapter;
	private List<Map<String, String>> lanqiuLists;
	
	private TextView zhanji_tab;
	private LinearLayout zhanji_layout;
	private ArrayList<MyBettingDO> myBetting;//数据库中的数据
	private SharedPreferences shp;
	List<String> result2;
	private zhanjibianAdpter zhanjibianAdpter;
	private boolean isShowMyMakeeMatch;
	private boolean isFromResultService;
	
    
	
	@Override
	protected void onStart() {
		super.onStart();
		if(myBetting!=null){
		zhanjibianAdpter = new zhanjibianAdpter();
		zhanjibianAdpter.notifyDataSetChanged();
	}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		DatabaseOpener1 opener = new DatabaseOpener1(MyMakedMatchActivity.this);
		//从数据库读取数据
		myBetting = opener.queryAll(0);
		setContentView(R.layout.mymaked);
		
		
		findView();
		Intent intent=getIntent();
		//Uri uri = intent.getData();//获取该意图中的数据	//uri.get...获取相应的数据
		isShowMyMakeeMatch	=intent.getBooleanExtra("showMyMakeeMatch", false);	//获取该意图中的参数，根据键来获取
		isFromResultService =intent.getBooleanExtra("fromResultService", false);
		// 检测是不是在投注页面跳转过来的
		if(isShowMyMakeeMatch||isFromResultService){
			zhanji_tab.setBackgroundResource(R.drawable.tab_l_select);
			zuqiu_tab.setBackgroundResource(R.drawable.tab_l_normal);
			lanqiu_tab.setBackgroundResource(R.drawable.tab_l_normal);
			zuqiuGuanzhuListView.setVisibility(View.GONE);
			lanqiuGuanzhuListView.setVisibility(View.GONE);
			zhanji_layout.setVisibility(View.VISIBLE);
			zhanji_list.setVisibility(View.VISIBLE);
			zhanjibianAdpter = new zhanjibianAdpter();
			zhanji_list.setAdapter(zhanjibianAdpter);
			opener.close();
			
		}
		


		
		zuqiu_tab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				zuqiu_tab.setBackgroundResource(R.drawable.tab_l_select);
				zuqiuGuanzhuListView.setVisibility(View.VISIBLE);
				lanqiu_tab.setBackgroundResource(R.drawable.tab_l_normal);
				zhanji_tab.setBackgroundResource(R.drawable.tab_l_normal);
				lanqiuGuanzhuListView.setVisibility(View.GONE);
				zhanji_layout.setVisibility(View.GONE);
				zhanji_list.setVisibility(View.GONE);
			}
		});
		
		
		
		lanqiu_tab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				lanqiu_tab.setBackgroundResource(R.drawable.tab_l_select);
				lanqiuGuanzhuListView.setVisibility(View.VISIBLE);
				zuqiu_tab.setBackgroundResource(R.drawable.tab_l_normal);
				zhanji_tab.setBackgroundResource(R.drawable.tab_l_normal);
				zuqiuGuanzhuListView.setVisibility(View.GONE);
				zhanji_layout.setVisibility(View.GONE);
				zhanji_list.setVisibility(View.GONE);
			}
		});

		zuqiuLists = new ArrayList<Map<String, String>>();
	
		zuqiuGuanzhuListAdapter = new ZuqiuGuanzhuListAdapter();
		zuqiuGuanzhuListView.setAdapter(zuqiuGuanzhuListAdapter);

		lanqiuLists = new ArrayList<Map<String, String>>();
		
		lanqiuGuanzhuListAdapter = new LanqiuGuanzhuListAdapter();
		lanqiuGuanzhuListView.setAdapter(lanqiuGuanzhuListAdapter);
		
		shp=getSharedPreferences("zhanbiao", MODE_PRIVATE);
		
	
		
		//每个条目的长按事件
		zhanji_list.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
                 AlertDialog.Builder builder = new AlertDialog.Builder(MyMakedMatchActivity.this);
				builder.setTitle("提示");
				builder.setMessage("确定删除所选项？");
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
                         //什么也不做      		  				
					}
				});
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
  
					@Override
					public void onClick(DialogInterface dialog, int which) {
//						DatabaseOpener1 opener1 = new DatabaseOpener1(MyMakedMatchActivity.this);
//						boolean flag =opener1.deleteinfo(position+"");
//						if(flag){
//							zhanjibianAdpter.notifyDataSetChanged();
//							zhanjibianAdpter.notifyDataSetInvalidated();
//					     Toast.makeText(getApplicationContext(), "删除成功", 0).show();
//						}else{
//							Toast.makeText(getApplicationContext(), "删除失败", 0).show();
//						}
//						zhanji_list.getItemAtPosition(position).toString().
//				    	String aa =myBetting.get(position).id.toString;
//						view.getTag(position);
						myBetting.remove(position);
						zhanjibianAdpter.notifyDataSetChanged();
				}
				});
				builder.create().show();
				
				return true;
			}
		});
		
		
		zhanji_tab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				zhanji_tab.setBackgroundResource(R.drawable.tab_l_select);
				zuqiu_tab.setBackgroundResource(R.drawable.tab_l_normal);
				lanqiu_tab.setBackgroundResource(R.drawable.tab_l_normal);
				zuqiuGuanzhuListView.setVisibility(View.GONE);
				lanqiuGuanzhuListView.setVisibility(View.GONE);
				zhanji_layout.setVisibility(View.VISIBLE);
				zhanji_list.setVisibility(View.VISIBLE);
			 
				    
				DatabaseOpener1 opener = new DatabaseOpener1(MyMakedMatchActivity.this);
				//从数据库读取数据
				myBetting = opener.queryAll(0);
				if(myBetting!=null){
					zhanjibianAdpter = new zhanjibianAdpter();
					zhanji_list.setAdapter(zhanjibianAdpter);
					opener.close();
				}else{
					Toast.makeText(getApplicationContext(), "还未选择赛程", 0).show();
				}
			}
		});
		
		//为每一个条目设置点击 事件
		zhanji_list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 获得到当前的时间
//				Date date = new Date();
			//	long timeminutes = date.getTime();//获得当前时间的毫秒值
				int timeminutes=myBetting.get(position).play_time;//获取比赛结束时间来当作发布时间。 modify by zzy
				  
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String stime = sdf.format(timeminutes*1000l);
				Editor editor = shp.edit();
				editor.putString("stime", stime);
				editor.putString("staketx",myBetting.get(position).stake+"" );
			    editor.putString("moneytx", myBetting.get(position).foretx);
			    editor.putString("fee", myBetting.get(position).fee+"");
			    editor.putString("bet_style", myBetting.get(position).bet_style);
			    editor.putInt("win_status", myBetting.get(position).win_status);
			    editor.putString("win_match", myBetting.get(position).win_match);
			    editor.putString("beishu", myBetting.get(position).beishu+"");
			    editor.putString("win_fee", myBetting.get(position).win_fee);
			    editor.putInt("status", myBetting.get(position).status);
			    editor.putString("foretx", myBetting.get(position).foretx);
			    
			    editor.commit();
			    
			    Intent intent = new Intent(MyMakedMatchActivity.this,ZhanjiResultActivity.class);	
			    intent.putExtra("_id",myBetting.get(position).id+"");
			  
			    startActivity(intent);
				  
			}});
	}
	/**
	 * 初始化化 布局
	 */
	private void findView() {
		zuqiu_tab = (TextView) findViewById(R.id.zuqiu_tab);
		lanqiu_tab = (TextView) findViewById(R.id.lanqiu_tab);
		zuqiuGuanzhuListView = (ListView) findViewById(R.id.zuqiu_guanzhu_list);
		lanqiuGuanzhuListView = (ListView) findViewById(R.id.lanqiu_guanzhu_list);
		zhanji_list = (ListView) findViewById(R.id.zhanji_list);
		zhanji_layout= (LinearLayout) findViewById(R.id.zhanji_layout);
		zhanji_tab = (TextView) findViewById(R.id.zhanji_tab);
	}



	//为战绩列表填充数据
	public class zhanjibianAdpter extends BaseAdapter{

		@Override
		public int getCount() {
			return myBetting.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(MyMakedMatchActivity.this, R.layout.zhanji_item, null);
			TextView wanfa = (TextView) view.findViewById(R.id.wanfa);
			TextView zhushu = (TextView) view.findViewById(R.id.zhushu);
			TextView zhongjiang = (TextView) view.findViewById(R.id.zhongjiang);
			TextView zhuangtai = (TextView) view.findViewById(R.id.zhuangtai);
			TextView fashi = (TextView) view.findViewById(R.id.fashi);
	
			zhushu.setText(myBetting.get(position).stake+"注");
			zhongjiang.setText(myBetting.get(position).fee+"元");
			fashi.setText(myBetting.get(position).bet_style);
			
			if (myBetting.get(position).status==0){
				zhuangtai.setText("未开奖");
				
			}else if((myBetting.get(position).win_status==1 )){
				zhuangtai.setText("中奖");
				zhuangtai.setTextColor(Color.parseColor("#ffff0000"));
				
			}else if((myBetting.get(position).win_status==0)){
				zhuangtai.setText("未中奖");
			}
			wanfa.setText("胜负平");
	
			return view;
		}
	}
	
	private void cancelZuqiuMaked(int index) {
		Map<String, String> _map = zuqiuLists.remove(index);
		zuqiuGuanzhuListAdapter.notifyDataSetChanged();
		Toast.makeText(MyMakedMatchActivity.this, R.string.yscgz,
				Toast.LENGTH_SHORT).show();
		DBService dbService = new DBService(MyMakedMatchActivity.this);
		dbService.open();
		dbService.del(_map.get("m_id"));
		dbService.close();
	}

	private void cancelLanqiuMaked(int index) {
		Map<String, String> _map = lanqiuLists.remove(index);
		lanqiuGuanzhuListAdapter.notifyDataSetChanged();
		Toast.makeText(MyMakedMatchActivity.this, R.string.yscgz,
				Toast.LENGTH_SHORT).show();
		DBService dbService = new DBService(MyMakedMatchActivity.this);
		dbService.open();
		dbService.del(_map.get("m_id"));
		dbService.close();
	}

//	@Override
//	public void onBackPressed() {//不同的启动方式对应的返回键事件不同
//		if(isShowMyMakeeMatch){
//			finish();
//		}else if(isFromResultService){
//			Intent intent=new Intent(MyMakedMatchActivity.this, SportteryMainActivity.class);
//			startActivity(intent);
//		}
//		else if (getParent()!=null) {
//			getParent().onBackPressed();
//		}
//	}
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		DBService dbService = new DBService(MyMakedMatchActivity.this);
		dbService.open();
		zuqiuLists.clear();
		zuqiuLists.addAll(dbService.getAll("fb"));
		lanqiuLists.clear();
		lanqiuLists.addAll(dbService.getAll("bk"));
		dbService.close();
		zuqiuGuanzhuListAdapter.notifyDataSetChanged();
		lanqiuGuanzhuListAdapter.notifyDataSetChanged();
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	class ZuqiuGuanzhuListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return zuqiuLists.size();
		}

		@Override
		public Object getItem(int position) {
			return zuqiuLists.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final Map<String, String> map = zuqiuLists.get(position);
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.my_guanzhu_list_item, parent, false);
			}
			TextView num = (TextView) convertView.findViewById(R.id.num);
			num.setText(map.get("num"));
			TextView h_cn = (TextView) convertView.findViewById(R.id.h_cn);
			h_cn.setText(map.get("h_cn"));
			TextView l_cn = (TextView) convertView.findViewById(R.id.l_cn);
			l_cn.setText(map.get("l_cn"));
			TextView a_cn = (TextView) convertView.findViewById(R.id.a_cn);
			a_cn.setText(map.get("a_cn"));
			View sstime = convertView.findViewById(R.id.sstime);
			sstime.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					cancelZuqiuMaked(position);
				}
			});
			TextView shottime = (TextView) convertView
					.findViewById(R.id.shottime);
			shottime.setText(map.get("shottime"));
			TextView shotdate = (TextView) convertView
					.findViewById(R.id.shotdate);
			shotdate.setText(map.get("shotdate"));
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MyMakedMatchActivity.this,
							ZuqiuMatchDetailActivity.class);
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
			
			return convertView;
		}
	}

	class LanqiuGuanzhuListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return lanqiuLists.size();
		}

		@Override
		public Object getItem(int position) {
			return lanqiuLists.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final Map<String, String> map = lanqiuLists.get(position);
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.my_guanzhu_list_item, parent, false);
			}
			TextView num = (TextView) convertView.findViewById(R.id.num);
			num.setText(map.get("num"));
			TextView h_cn = (TextView) convertView.findViewById(R.id.h_cn);
			h_cn.setText(map.get("a_cn"));
			TextView l_cn = (TextView) convertView.findViewById(R.id.l_cn);
			l_cn.setText(map.get("l_cn"));
			TextView a_cn = (TextView) convertView.findViewById(R.id.a_cn);
			a_cn.setText(map.get("h_cn"));
			View sstime = convertView.findViewById(R.id.sstime);
			sstime.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					cancelLanqiuMaked(position);
				}
			});
			TextView shottime = (TextView) convertView
					.findViewById(R.id.shottime);
			shottime.setText(map.get("shottime"));
			TextView shotdate = (TextView) convertView
					.findViewById(R.id.shotdate);
			shotdate.setText(map.get("shotdate"));
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MyMakedMatchActivity.this,
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
			return convertView;
		}
		
	}

	
}
