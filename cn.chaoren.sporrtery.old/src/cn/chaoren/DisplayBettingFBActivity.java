package cn.chaoren;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.chaoren.util.CalulateBonus;
import cn.chaoren.util.DisplayData;
import cn.chaoren.util.MatchSelectList;
import cn.chaoren.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//竞彩足球界面

public class DisplayBettingFBActivity extends Activity {

	View view;
	private ProgressBar match_list_progress;
	private TextView match_list_progress_text;
	private Button addBet;
	private static final int DDD_ERROR = 11;
	private static final int DDD_SUCC = 12;
	protected static final String TAG = "DisplayBettingFBActivity";
	List<Map<String, String>> ddd = null;
	ListView categorylist;
	MyAdapter adapter;
	int flag = 0;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case DDD_ERROR:
				Toast.makeText(getApplicationContext(), "数据连接失败", 0).show();
				break;
			case DDD_SUCC:

				match_list_progress.setVisibility(View.INVISIBLE);
				match_list_progress_text.setText("");
				List<Map<String, String>> mData = (List<Map<String, String>>) msg.obj;
				MatchSelectList.setMatchData(mData);
				adapter = new MyAdapter(getApplicationContext());
				categorylist.setAdapter(adapter);
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 获取服务器的数据
		// List<Map<String,String >> mData = DisplayData.getJson();
		// 将服务器获取的数据放在静态方法中，以便以后使用方便。
		// MatchSelectList.setMatchData(mData);
		setContentView(R.layout.activity_display_betting);
		// fillData();//填充数据
		match_list_progress = (ProgressBar) findViewById(R.id.match_list_progress);
		match_list_progress_text = (TextView) findViewById(R.id.match_list_progress_text);

		addBet = (Button) findViewById(R.id.bet_btn);
		addBet.setOnClickListener(betOnClickListener);// 按钮的点击事件
		categorylist = (ListView) findViewById(R.id.categorylist);
		 new Thread(new fillData()).start();// 开启子线程
		

	}

	@Override
	protected void onStart() {
		super.onStart();
		flag=0;
	}
	
	@Override
	protected void onStop() {
		super.onStop();
         flag=1;
     adapter.notifyDataSetChanged();
	}
	


	private class fillData implements Runnable {

		@Override
		public void run() {

			ddd = DisplayData.getJson();
			if (ddd == null) {
				Message msg = Message.obtain();
				msg.what = DDD_ERROR;
				handler.sendMessage(msg);
			} else {
				Message msg = Message.obtain();
				msg.what = DDD_SUCC;
				msg.obj = ddd;
				handler.sendMessage(msg);
			}
		}
	}

	// 对三个大块的，每个条目的点击事件
	private View.OnClickListener mOnClickListener = new View.OnClickListener() {

		public void onClick(View block) {
			// setTag()相当于容器，打标记，放将各种类型的值存放进去，然后通过getTag()方法取出来，针对不同类型再强转
			Integer[] taglist = (Integer[]) block.getTag();
			int pos = taglist[1];

			// 将选中的条目加到集合中（value）,在BetShowActivity界面从该集合中取出数据，再填充到布局中，展示在界面上。
			List<String> mat = MatchSelectList.getContestByKey(pos);
			String winLose = String.valueOf(taglist[0]);

			// 先判断SparseArray里面的value的值是否为空
			if (mat != null && mat.contains(winLose)) {
				block.setBackgroundColor(Color.WHITE);

				((TextView) block.findViewById(R.id.name_txt))
						.setTextColor(Color.parseColor("#000000"));// 黑色
				((TextView) block.findViewById(R.id.sp_txt)).setTextColor(Color
						.parseColor("#000000"));
				mat.remove(winLose);

				MatchSelectList.addContest(pos, mat);

			} else {
				block.setBackgroundColor(Color.parseColor("#FFA500"));
				((TextView) block.findViewById(R.id.name_txt))
						.setTextColor(Color.parseColor("#ffffff"));// 白色
				((TextView) block.findViewById(R.id.sp_txt)).setTextColor(Color
						.parseColor("#ffffff"));
				if (mat == null) {
					mat = new ArrayList<String>();
				}
				mat.add(winLose);
				// 添加到集合中去，SparseArray<List<String>> contest = new
				// SparseArray<List<String>>();
				MatchSelectList.addContest(pos, mat);
			}
		}
	};

	// 按钮的点击事件
	private View.OnClickListener betOnClickListener = new View.OnClickListener() {

		public void onClick(View block) {
			SparseArray<List<String>> sp11 = MatchSelectList.getContest();
			if (sp11.size() <= 8) {
				Intent intent = new Intent();// 跳到详情界面
				intent.setClass(DisplayBettingFBActivity.this,
						BetShowActivity.class);
				startActivity(intent);
			} else if (sp11.size() > 8) {
				Toast.makeText(getApplicationContext(), "最多选择八场比赛！", 0).show();
			}
		}
	};

	public final class ViewHolder {
		public TextView match_time;
		public TextView league;
		public TextView team_host;
		public TextView team_visiting;
		public TextView team_vs;
		public View home_block;
		public View vs_block;
		public View visiting_block;
		public TextView stop_time;
		public TextView sp3;
		public TextView sp1;
		public TextView sp0;
	}

	public class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			int size = MatchSelectList.getMatchData().size();
			return size;
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();

				convertView = mInflater.inflate(R.layout.match_list_item, null);

				holder.league = (TextView) convertView
						.findViewById(R.id.league_name);// 联赛名称
				holder.match_time = (TextView) convertView
						.findViewById(R.id.stop_time);// 到止时间

				View home_block = (View) convertView
						.findViewById(R.id.home_block);
				holder.team_host = (TextView) home_block
						.findViewById(R.id.name_txt);
				holder.sp3 = (TextView) home_block.findViewById(R.id.sp_txt);
				((TextView) home_block.findViewById(R.id.win_txt))
						.setText(R.string.win_txt);
				holder.home_block = home_block;

				home_block.setOnClickListener(mOnClickListener);

				View vs_block = (View) convertView.findViewById(R.id.vs_block);
				holder.team_vs = (TextView) vs_block
						.findViewById(R.id.name_txt);
				holder.sp1 = (TextView) vs_block.findViewById(R.id.sp_txt);
				((TextView) vs_block.findViewById(R.id.win_txt))
						.setText(R.string.draw_txt);
				holder.vs_block = vs_block;

				vs_block.setOnClickListener(mOnClickListener);

				View visiting_block = (View) convertView
						.findViewById(R.id.guest_block);
				holder.team_visiting = (TextView) visiting_block
						.findViewById(R.id.name_txt);
				holder.sp0 = (TextView) visiting_block
						.findViewById(R.id.sp_txt);
				((TextView) visiting_block.findViewById(R.id.win_txt))
						.setText(R.string.fail_txt);

				holder.visiting_block = visiting_block;

				visiting_block.setOnClickListener(mOnClickListener);

				View header = convertView.findViewById(R.id.header);
				header.setVisibility(View.GONE);
				convertView.setTag(holder);

			} else {

				holder = (ViewHolder) convertView.getTag();
			}

			List<Map<String, String>> mData = MatchSelectList.getMatchData();// 服务器的数据

			// 填充数据, 只有九个字段
			holder.league.setText(mData.get(position).get("league"));// 联赛名称及显示的颜色
			holder.match_time.setText(mData.get(position).get("match_name"));// 显示的时间

			holder.team_host.setText(mData.get(position).get("team_host"));// 主队
			holder.team_vs.setText(mData.get(position).get("adjust"));
			holder.team_visiting.setText(mData.get(position).get(
					"team_visiting"));// 客队
			holder.sp3.setText(mData.get(position).get("sp3"));
			holder.sp1.setText(mData.get(position).get("sp1"));
			holder.sp0.setText(mData.get(position).get("sp0"));

			holder.home_block.setTag(new Integer[] { 3, position });// 胜
			holder.vs_block.setTag(new Integer[] { 1, position });// 平
			holder.visiting_block.setTag(new Integer[] { 0, position });// 负

		
				// 在这里取出，再判断
				List<String> winList = MatchSelectList
						.getContestByKey(position);

				// 设置三大块点击后的颜色
				if (winList != null && winList.contains(String.valueOf(3))) {
					holder.home_block.setBackgroundColor(Color
							.parseColor("#FFA500"));
					((TextView) holder.home_block.findViewById(R.id.name_txt))
							.setTextColor(Color.parseColor("#ffffff"));
					((TextView) ((holder.home_block).findViewById(R.id.sp_txt)))
							.setTextColor(Color.parseColor("#ffffff"));
				} else {
					holder.home_block.setBackgroundColor(Color.WHITE);
					((TextView) holder.home_block.findViewById(R.id.name_txt))
							.setTextColor(Color.parseColor("#000000"));
					((TextView) holder.home_block.findViewById(R.id.sp_txt))
							.setTextColor(Color.parseColor("#000000"));
				}
				if (winList != null && winList.contains(String.valueOf(1))) {
					holder.vs_block.setBackgroundColor(Color
							.parseColor("#FFA500"));
					((TextView) holder.vs_block.findViewById(R.id.name_txt))
							.setTextColor(Color.parseColor("#ffffff"));
					((TextView) ((holder.vs_block).findViewById(R.id.sp_txt)))
							.setTextColor(Color.parseColor("#ffffff"));
				} else {
					holder.vs_block.setBackgroundColor(Color.WHITE);
					((TextView) holder.vs_block.findViewById(R.id.name_txt))
							.setTextColor(Color.parseColor("#000000"));
					((TextView) holder.vs_block.findViewById(R.id.sp_txt))
							.setTextColor(Color.parseColor("#000000"));
				}
				if (winList != null && winList.contains(String.valueOf(0))) {
					holder.visiting_block.setBackgroundColor(Color
							.parseColor("#FFA500"));
					((TextView) holder.visiting_block
							.findViewById(R.id.name_txt)).setTextColor(Color
							.parseColor("#ffffff"));
					((TextView) ((holder.visiting_block)
							.findViewById(R.id.sp_txt))).setTextColor(Color
							.parseColor("#ffffff"));
				} else {
					holder.visiting_block.setBackgroundColor(Color.WHITE);
					((TextView) holder.visiting_block
							.findViewById(R.id.name_txt)).setTextColor(Color
							.parseColor("#000000"));
					((TextView) holder.visiting_block.findViewById(R.id.sp_txt))
							.setTextColor(Color.parseColor("#000000"));
				}
		             
				if(flag ==1){
				holder.home_block.setBackgroundColor(Color.WHITE);
				((TextView) holder.home_block.findViewById(R.id.name_txt))
						.setTextColor(Color.parseColor("#000000"));
				((TextView) holder.home_block.findViewById(R.id.sp_txt))
						.setTextColor(Color.parseColor("#000000"));
				holder.vs_block.setBackgroundColor(Color.WHITE);
				((TextView) holder.vs_block.findViewById(R.id.name_txt))
						.setTextColor(Color.parseColor("#000000"));
				((TextView) holder.vs_block.findViewById(R.id.sp_txt))
						.setTextColor(Color.parseColor("#000000"));
				holder.visiting_block.setBackgroundColor(Color.WHITE);
				((TextView) holder.visiting_block.findViewById(R.id.name_txt))
						.setTextColor(Color.parseColor("#000000"));
				((TextView) holder.visiting_block.findViewById(R.id.sp_txt))
						.setTextColor(Color.parseColor("#000000"));
				
				
				}
			return convertView;
		}

	}

	private void fillData() {
		new AsyncTask<Void, Void, List<Map<String, String>>>() {
			// 运行之中
			@Override
			protected List<Map<String, String>> doInBackground(Void... params) {
				return DisplayData.getJson();
			}

			// 运行之前
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			// 运行之后
			@Override
			protected void onPostExecute(List<Map<String, String>> resultt) {
				super.onPostExecute(resultt);
				if (resultt == null) {
				} else {
					MatchSelectList.setMatchData(resultt);
				}
			}
		}.execute();
	}
	/**
	 * 设置竞彩足球机界面的的返回键监听 add by zzy
	 */
	@Override
	public void onBackPressed() {
		if (getParent() != null) {
			getParent().onBackPressed();
		} else {
			super.onBackPressed();
		}
	}
}
