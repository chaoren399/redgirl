package cn.chaoren.service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.chaoren.MyMakedMatchActivity;
import cn.chaoren.ZhanjiResultActivity;
import cn.chaoren.db.DatabaseOpener1;
import cn.chaoren.db.MyBettingDO;
import cn.chaoren.net.zhanjiResultNutil;
import cn.chaoren.util.CalulateBonus;
import cn.chaoren.util.MatchSelectList;
import cn.chaoren.util.MyCalulateBonus;
import cn.chaoren.util.ZhanjiresultData;
import cn.chaoren.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint.Join;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

public class ResultService extends Service {
	private static final String TAG = "ResultService";
	private SQLiteDatabase database=null;
	private String serverData;
	private int status;
	private int flag=0;// 对每个win_match 字段中是否有null做标记

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// showNotification();
		Log.i(TAG, "result服务开启了");

		DatabaseOpener1 opener = new DatabaseOpener1(ResultService.this);
		database = opener.getReadableDatabase();
		int currentTime = (int) System.currentTimeMillis();
		Cursor cursor = database.rawQuery(
				"select * from buy_game where status = 0 and play_time <'currentTime' ", null); //这里的条件少了一个判断时间。测试用
		if(database.isOpen()){
		
		while (cursor.moveToNext()) {
			int id=cursor.getInt(cursor.getColumnIndex("_id"));
			Log.i(TAG, "id="+id);

			// 1.需要bet_style的中 4串11 的11
			ArrayList<MyBettingDO> myResult=new ArrayList<MyBettingDO>();
			MyBettingDO selection=new MyBettingDO();
			selection.bet_style=cursor.getString(cursor.getColumnIndex("bet_style"));
			selection.numberString=cursor.getString(cursor.getColumnIndex("number_string"));
			selection.beishu=cursor.getInt(cursor.getColumnIndex("beishu"));
			
			myResult.add(selection);
			
			String bet_Style = myResult.get(0).bet_style;
			String [] bet_Style2=bet_Style.split("串");
			String m1=bet_Style2[1];
			
			

			// 2.List<Map<String, String>> list number_string 解析后的map
			String number_String = myResult.get(0).numberString;
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();

			SparseArray<List<String>> list2 = new SparseArray<List<String>>();

			SparseArray<String> list3 = new SparseArray<String>();
			
			List<String> win_Match_List = new ArrayList< String>();
			if (number_String != null) {
				JSONArray jsonObjs;
				try {
					jsonObjs = new JSONArray(number_String);

					for (int i = 0; i < jsonObjs.length(); i++) {
						JSONObject jsonObj = jsonObjs.optJSONObject(i);
						String match_id = jsonObj.optString("match_id");
						String sp3 = jsonObj.optString("sp3");
						String sp0 = jsonObj.optString("sp0");
						String sp1 = jsonObj.optString("sp1");

						Map<String, String> map = new HashMap<String, String>();
						map.put("match_id", match_id);
						
								map.put("sp3", sp3);
								map.put("sp1", sp1);
								map.put("sp0", sp0);
								list.add(map);
						// 3. SparseArray<List<String>> list2 number_string
						// 解析后返回的<match_id , num_str[]>
						String num_str = jsonObj.optString("num_str");
						ArrayList<String> num_strs = new ArrayList<String>();

						int length = num_str.length();
						if (length == 13) {
							num_strs.add(num_str.charAt(2) + "");
							num_strs.add(num_str.charAt(6) + "");
							num_strs.add(num_str.charAt(10) + "");
						} else if (length == 9) {
							num_strs.add(num_str.charAt(2) + "");
							num_strs.add(num_str.charAt(6) + "");
						} else if (length == 5) {
							num_strs.add(num_str.charAt(2) + "");
						}

						Log.i(TAG,
								"num_strs" + num_strs.toString()
										+ num_strs.size() + length);

						// list2.put(Integer.parseInt(match_id), num_strs);
						list2.append(Integer.parseInt(match_id), num_strs);
						// 4. SparseArray<String> list3 连接服务器得到 <match_id,
						// 1>比赛结果 将其加入win_match中

						 serverData = zhanjiResultNutil.getMatchSchedule(match_id);
						 if(serverData==null){
							 flag+=1;
						 }
						Log.i(TAG, "serverData="+serverData);
						
						
						
						// list3.put(Integer.parseInt(match_id), serverData);
						list3.append(Integer.parseInt(match_id), serverData);
						String match_Result="{"+"\""+match_id+"\""+":"+"\""+serverData+"\""+"}";
						win_Match_List.add(match_Result);
						
						

					}
					// <match_id, 1>比赛结果 将其加入win_match中
/*
					SQLiteDatabase db = opener.getWritableDatabase();
					if (db.isOpen()) {
						db.execSQL("update buy_game set win_match=? where _id=?",
								new Object[] { win_Match_List,id});
						db.close();
					}*/
					// 5. 计算奖金 并且更改win_fee中的值
					int gate = Integer.parseInt(m1);
					
					
					float bonus1 = MyCalulateBonus.getBonus(gate, list, list2,
							list3);
					Log.i(TAG, "bonus1="+bonus1);
					
					
					float multiple=	myResult.get(0).beishu;
				    float   bonus2=bonus1*multiple;
				    String bonus=bonus2+"";
					//bonus=bonus*multiple;
					

					Log.i(TAG, "gate=" + gate + "-list=" + list + "-list2="
							+ list2.toString() + "-list3=" + list3.toString());
					Log.i(TAG, "bonus=" + bonus);
					list=null;
					list2=null;
					list3=null;
					
					if (database.isOpen()) {
						int win_status=0;
						if (!(bonus.equals("0")||bonus.equals("0.0")||bonus.equals("0.00"))){ //这里的判断有问题    改好
							win_status = 1;
							//showNotification("尊敬的球迷中奖了");
						}
						if(flag>=1){
							Log.i(TAG, "flag="+flag);
							status=0;
							
						}else{
							status=1;
							
						}
						flag=0;
						
						database.execSQL("update buy_game set win_fee=?,win_match=?,win_status=?,status=? where _id=?",
								new Object[] { bonus,win_Match_List,win_status,status,id });
						
						// 6.如果 win_fee！＝null 则置win_stats==1
						/*if (bonus != "0") {
						  int win_status = 1;
							db1.execSQL("update buy_game set win_status=?,status=1 where _id=?",
									new Object[] { win_status ,id});
						}*/
						
					/*SQLiteDatabase db1 = opener.getWritableDatabase();
					if (db1.isOpen()) {
						int win_status=0;
						if (!(bonus.equals("0")||bonus.equals("0.0")||bonus.equals("0.00"))){ //这里的判断有问题    改好
							win_status = 1;
							showNotification("尊敬的球迷中奖了");
						}
						db1.execSQL("update buy_game set win_fee=?,win_match=?,win_status=?,status=1 where _id=?",
								new Object[] { bonus,win_Match_List,win_status,id });
						
						// 6.如果 win_fee！＝null 则置win_stats==1
						if (bonus != "0") {
						  int win_status = 1;
							db1.execSQL("update buy_game set win_status=?,status=1 where _id=?",
									new Object[] { win_status ,id});
						}
						db1.close();*/
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			// 最后更改status的 值
			/*SQLiteDatabase db2 = opener.getWritableDatabase();
			if (db2.isOpen()) {
				db2.execSQL("update buy_game set status=1 where _id=?",new Object[]{id});
				db2.close();
			}*/
		}
		cursor.close();
		Log.i(TAG, "cursor.close();");
		database.close();
		
		
		}
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
/**
 * 显示Notification 
 * @param message  要显示的消息
 */
	private void showNotification(String message) {
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification notification = new Notification(
				cn.chaoren.R.drawable.logo, "中奖通知",
				System.currentTimeMillis());
		notification.flags = Notification.FLAG_AUTO_CANCEL;  

		Intent intent = new Intent(this, MyMakedMatchActivity.class);
		intent.putExtra("fromResultService", true);//检测是不是从ResultService 传过去的。
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_CANCEL_CURRENT);
		notification.setLatestEventInfo(this, "中体彩彩票运营", message, contentIntent);

		nm.notify(0, notification);

	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(database!=null	){
			database.close();
			Log.i(TAG, "database.close();");
		}
	}

}
