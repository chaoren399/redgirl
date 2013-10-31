package cn.chaoren.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.util.SparseArray;

public class MatchSelectList {

	private static List<Map<String, String>> mData;
	
	private static List<String> betlistt;

	//SparseArray相当于HashMap，里面也是key,value的值对。
	private static SparseArray<List<String>> contest = new SparseArray<List<String>>();

	public MatchSelectList() {

	}

	public static void addContest(int key, List<String> ma) {
		if (ma.size() == 0) {
			contest.remove(key);
		} else {
			contest.put(key, ma);
		}
	}

	public static SparseArray<List<String>> getContest() {
		return contest;
	}

	public static List<String> getContestByKey(int key) {

		return contest.get(key);
	}

	public static List<Map<String, String>> getMatchData() {

		return mData;
	}
	
	
	public static void setMatchData(List<Map<String, String>> md) {
		mData = md;
	}
	
	public static void setBet(List<String> betlist){
		
		betlistt=betlist;
	}
   public static List <String> getBet(){
	   return betlistt;
   }
	
	public static String toSerial() {

		String s = "";
		JSONArray res= new JSONArray();
		for (int i = 0; i < contest.size(); i++) {
//			Map<String, String> sd = mData.get(i);
		
			
			int key = contest.keyAt(i);
			List<String> arr = contest.get(key);
			Map<String, String> sd = mData.get(key);
			
			
			Log.d("arr", arr+"");//状态数组
			JSONObject jo = getJSON(sd,arr);
			res.put(jo);
			Log.d("json", jo.toString());
		}
		
//		for(int i=0;i<contest.size();i++){
//			
//			int key = contest.keyAt(i);
//			Map<String,String> sd = mData.get(key);
//			
//			List<String> arr = contest.get(i);
//			JSONObject jo = getJSON(sd,arr);
//			res.put(jo);
//			Log.d("json", jo.toString());
//		}
//		
		
		
		return res.toString();
	}

	public static JSONObject getJSON(Map m,List l) {

		JSONObject data = new JSONObject();
		try {

			Iterator iter2 = m.entrySet().iterator();
			while (iter2.hasNext()) {

				Map.Entry pairs2 = (Map.Entry) iter2.next();
				data.put((String) pairs2.getKey(), (String) pairs2.getValue());

			}
			data.put("num_str",getJSON( l));
		} catch (JSONException e) {
			Log.e("Transforming", "There was an error packaging JSON", e);
		}
		return data;
	}
/**
 * 将 List 转化为JSONArray 
 * @param l
 * @return
 */

	public static JSONArray getJSON(List l) {
		int c = l.size();
		JSONArray j = new JSONArray();
		try {
			
			for (int i = 0; i < c; i++) {
				j.put(l.get(i));
			}
		} catch (Exception e) {
			Log.e("Transforming", "There was an error packaging JSON", e);
		}
		return j;
	}

}
