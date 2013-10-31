package cn.chaoren;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.chaoren.db.DatabaseOpener1;
import cn.chaoren.util.CalulateBonus;
import cn.chaoren.util.MatchSelectList;
import cn.chaoren.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

//选择好要投注项目的界面
public class BetShowActivity extends Activity {

	protected static final String TAG = "BetShowActivity";
	private List<Map<String, String>> mData;
	private SparseArray<List<String>> sp2;
	private int MaxPlay_time = 0;
	private int status = 0;
	private String win_match = null;
	private int win_status = 0;
	private String win_fee = null;

	SparseArray<int[]> sparseArray;
	String[] choices= null;
	String[] myChoices;

	TextView foretx;
	TextView staketx;
	TextView moneytx;
	LinearLayout layout, bet_btn;
	ScrollView scroll;

	String method = null;
	String myMethod = null;
	String str = null;
	View v;
	private SharedPreferences shp;
	private LinearLayout guoguan_layout;
	private RelativeLayout zhifu_layout;
	private RelativeLayout wuxuan;// 没有选择的界面
	private EditText beishu_edit;
	String beishu = null;
	private int beishu1 = 1;
	int flag =0;

	private int key = -1;
	private Map<String, Float> cres = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.match_bet_show);

		shp = getSharedPreferences("zhanji", MODE_PRIVATE);

		wuxuan = (RelativeLayout) findViewById(R.id.wuxian);
		zhifu_layout = (RelativeLayout) findViewById(R.id.zhifu_layout);
		guoguan_layout = (LinearLayout) findViewById(R.id.guoguan_layout);
		foretx = (TextView) findViewById(R.id.win_money_forecast);
		staketx = (TextView) findViewById(R.id.stake_num);
		moneytx = (TextView) findViewById(R.id.money_num);
		scroll = (ScrollView) findViewById(R.id.list_scroll);
		layout = (LinearLayout) findViewById(R.id.list_block);
		beishu_edit = (EditText) findViewById(R.id.beishu_edit);
		beishu_edit.addTextChangedListener(textWacher);

		

		fillData();

		View methodBtn = findViewById(R.id.bet_method_btn);
		// 选择过关方式
		methodBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
             
				if(sp2.size()>=2&&sp2.size()<=8){
				bet_btn.setVisibility(View.VISIBLE);
				// 包含多个选项及复选框的对话框
				int matchCount = MatchSelectList.getContest().size();
				sparseArray = CalulateBonus.gateway.get(matchCount);

				choices = new String[sparseArray.size()];
				myChoices = new String[sparseArray.size()];

				for (int i = 0; i < sparseArray.size(); i++) {
					int key = sparseArray.keyAt(i);//
					choices[i] = String.valueOf(matchCount) + "串"
							+ String.valueOf(key);
					myChoices[i] = String.valueOf(matchCount)
							+ String.valueOf(key);
				}
				// 包含多个选项的对话框
				AlertDialog dialog = new AlertDialog.Builder(
						BetShowActivity.this)
						.setIcon(android.R.drawable.btn_star).setTitle("过关方式")
						.setItems(choices, onselect).create();
				dialog.show();
			}else if(sp2.size()>8){
				Toast.makeText(getApplicationContext(), "最多为八场", 0).show();
				
			}
			}
		});

		// 点击保存键，执行下面操作
		bet_btn = (LinearLayout) findViewById(R.id.bet_btn);
		bet_btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Editor editor = shp.edit();
				editor.putString("staketx", staketx.getText().toString());
				editor.putString("moneytx", moneytx.getText().toString());// 注数的钱数
				editor.putString("foretx", foretx.getText().toString());
				editor.putString("strtime", str);
				editor.putString("method", method);
				editor.commit();

				// 这里用到了数据库
				DatabaseOpener1 opener = new DatabaseOpener1(
						BetShowActivity.this);
				/**
				 * add by zzy 1.添加字段，paly time，status,win_match,win_status;
				 * 2.解析字符串numStr;
				 */
				String numStr = MatchSelectList.toSerial();// jsonArray
				MaxPlay_time = getMaxTime(numStr);// 判断当前时间。修改status

				if ("".equals(staketx.getText().toString().trim())) {
					Toast.makeText(getApplicationContext(), "须选择过关方式", 0)
							.show();
				} else {
					// 将投注的注数和共计多少元，添加到数据库去。
					opener.insert(numStr,
							Integer.valueOf(staketx.getText().toString()),
							Integer.valueOf(moneytx.getText().toString()),
							foretx.getText().toString(), MaxPlay_time, status,
							win_match, win_status, win_fee, null, method,
							beishu1);
					//保存成功后，显示保存成功；同时到达我的战绩详情界面；
					Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
					Intent intent=new Intent(BetShowActivity.this, MyMakedMatchActivity.class);
					intent.putExtra("showMyMakeeMatch", true);
					startActivity(intent);
					
					bet_btn.setVisibility(View.GONE);
					cres=null;
					finish();
				}
			}
		});
	}
	/**
	 * 获取比赛结束最晚的时间
	 * @param numStr
	 * @return
	 */
	protected int getMaxTime(String numStr) {
		int time = 0;
		try {
			JSONArray jsonObjs = new JSONArray(numStr);
			for (int i = 0; i < jsonObjs.length(); i++) {
				JSONObject jsonObj = jsonObjs.optJSONObject(i);
				if (Integer.parseInt(jsonObj.optString("play_time")) >= time) {
					time = Integer.parseInt(jsonObj.optString("play_time"));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return time;
	}
	/**
	 * 测试看能否得到正确的num_str
	 * @param numStr
	 * @return
	 */
	public static ArrayList<String> getNum_Str(String numStr) {
		String num_str = null;
		ArrayList<String> num_strs = new ArrayList<String>();
		try {
			JSONArray jsonObjs = new JSONArray(numStr);
			for (int i = 0; i < jsonObjs.length(); i++) {
				JSONObject jsonObj = jsonObjs.optJSONObject(i);
				num_str = jsonObj.optString("num_str");
				num_strs.add(num_str);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return num_strs;
	}  

	private void fillData() {
		mData = MatchSelectList.getMatchData();

		sp2 = MatchSelectList.getContest();
		if (sp2.size() >= 2) {
			guoguan_layout.setVisibility(View.VISIBLE);
			zhifu_layout.setVisibility(View.VISIBLE);
			int si = sp2.size();
			for (int i = 0; i < si; i++) {
				// 给每一个条目加个索引，传个int类型的值进去，获得到这个条目对应的key
				int key = sp2.keyAt(i);
				v = getMatchItem(key);
				layout.addView(v);
			}
		}
		else  {
			guoguan_layout.setVisibility(View.GONE);
			zhifu_layout.setVisibility(View.GONE);
			wuxuan.setVisibility(View.VISIBLE);
			Toast.makeText(getApplicationContext(), "须选择两场或者两场以上赛程", 1).show();
		}
	}


	
	// 利用集合来实现两个界面之间的数据传递
	// 投注后界面的布局，挂在layout上面。
	public View getMatchItem(final int pos) {// 指定哪个条目，获得到对应的string类型的list集合

		View convertView = this.getLayoutInflater().inflate(
				R.layout.match_bet_item, null);
		ImageView button = (ImageView) convertView.findViewById(R.id.del_btn);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (sp2.size() > 2) {
					sp2.remove(pos);
					layout.removeAllViews();
					for (int i = 0; i < sp2.size(); i++) {
						// 给每一个条目加个索引，传个int类型的值进去，获得到这个条目对应的key。
						int key = sp2.keyAt(i);
						v = getMatchItem(key);
						layout.addView(v);
					}
					foretx.setText("");
					staketx.setText("");
					moneytx.setText("");
				}
				// else if(sp2.size()==2){
				// layout.removeAllViews();
				// sp2.remove(pos);
				// for (int i = 0; i < sp2.size(); i++) {
				// // 给每一个条目加个索引，传个int类型的值进去，获得到这个条目对应的key。
				// int key = sp2.keyAt(i);
				// v = getMatchItem(key);
				// layout.addView(v);
				// guoguan_layout.setVisibility(View.GONE);
				// zhifu_layout.setVisibility(View.GONE);
				// }
				// }
				else if (sp2.size() == 2) {
					foretx.setText("");
					staketx.setText("");
					moneytx.setText("");
					Toast.makeText(getApplicationContext(), "须选择两场或者两场以上赛程", 0)
							.show();
				}
			}
		});

		TextView homeView = (TextView) convertView.findViewById(R.id.home_text);
		homeView.setText(mData.get(pos).get("team_host"));// 主队

		// 得到SpareArray集合中value的值
		List<String> winList = MatchSelectList.getContestByKey(pos);
		// winList=[3],winList=[0],winList=[1],
		if (winList.contains(String.valueOf(3))) {
			homeView.setBackgroundColor(Color.parseColor("#ff0000"));
			homeView.setTextColor(Color.parseColor("#ffffff"));
		}

		TextView drawView = (TextView) convertView.findViewById(R.id.draw_text);
		if (winList.contains(String.valueOf(1))) {
			drawView.setBackgroundColor(Color.parseColor("#ff0000"));
			drawView.setTextColor(Color.parseColor("#ffffff"));
		}

		TextView guestView = (TextView) convertView
				.findViewById(R.id.guest_text);
		guestView.setText(mData.get(pos).get("team_visiting"));// 客队

		if (winList.contains(String.valueOf(0))) {
			guestView.setBackgroundColor(Color.parseColor("#ff0000"));// 红色
			guestView.setTextColor(Color.parseColor("#ffffff"));// 纯白
		}
		
		return convertView;
	}

	// 选择过关方式，再根椐其计算要支付的钱数和注数。
	DialogInterface.OnClickListener onselect = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {

			key = sparseArray.keyAt(which);

			method = choices[which];// 过关方式 which 可以作注数的的变量
			myMethod = myChoices[which];// string类型的过关方式；

			// 获得到当前的时间
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			str = sdf.format(date);

			beishu = beishu_edit.getText().toString();
		
			cres = CalulateBonus.getBonusbeishu(key, beishu1);
			//Float stake= cres.get("len")*beishu1;//单倍注数
			Float stake= cres.get("len");//单倍注数
//			Float fee=stake*2*beishu1;// 投注的钱数
			foretx.setText(String.format("%.2f", cres.get("min")) + "元  ~ "
					+ String.format("%.2f", cres.get("max")) + "元");// 奖金范围
			staketx.setText(String.format("%.0f",stake )+"");//注数
		 
			
			moneytx.setText((int) (cres.get("len") * 2*beishu1) + "");// 投注的钱数
			//moneytx.setText( (int) fee+ "");// 投注的钱数
//			beishu_edit.setText("1");
			
		}
	};
	          
	TextWatcher  textWacher=new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			String str = beishu_edit.getText().toString().trim();
		
			if(str.matches("^[1-9]\\d*$")){
				if(str.equals("")&&key<=0){
					beishu1=1;
				}else{
					beishu1 = Integer.parseInt(str);
					Log.i(TAG, "beishu1="+beishu1);
					}
				
				   
				if(cres!=null){
					cres = CalulateBonus.getBonusbeishu(key, beishu1);
					Float stake= cres.get("len");//单倍注数
					foretx.setText(String.format("%.2f", cres.get("min")) + "元  ~ "
							+ String.format("%.2f", cres.get("max")) + "元");// 奖金范围
	    
				
					staketx.setText(String.format("%.0f",stake )+"");//注数
					moneytx.setText((int) (cres.get("len") * 2*beishu1) + "");// 投注的钱数
				}
				
				
			} /*else{
				foretx.setText("");// 奖金范围
				staketx.setText("");//注数
				moneytx.setText( "");// 投注的钱数
			}*/
			
		}
	};

	//在停止的时候 清空选中集合中的数据
	@Override
	protected void onStop() {
		super.onStop();
		sp2 = MatchSelectList.getContest();
		sp2.clear();
//		flag =1;  
		
	}
//	@Override
//	protected void onStart() {
//		super.onStart();
//		
//		if(flag ==1){
//           layout.removeAllViews();	
//           guoguan_layout.setVisibility(View.GONE);
//           zhifu_layout.setVisibility(View.GONE);
//           Toast.makeText(getApplicationContext(), "请选择赛程！", Toast.LENGTH_SHORT).show();
//		}
//	}

	}