package cn.chaoren.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.SystemClock;

import cn.chaoren.util.DisplayData;
import cn.chaoren.util.StringUtils;


//获取服务器比赛之后的结果
public class zhanjiResultNutil {

	public static String getMatchSchedule(String match_id) {

		
		String url = "http://bbs.lottery.org.cn/match/matchresult.php?match_id="
				+ match_id+"&p_code=HHAD&type=fb";
		String result = HttpServices.getHttpData(url);

		if (result == null || "".equals(result)) {
			SystemClock.sleep(15);
			result = HttpServices.getHttpData(url);
		}
		if (result != null) {
              
			try {   
				JSONObject jsonObj = new JSONObject(result);
			    JSONObject status = jsonObj.optJSONObject("status");
			    String code = status.getString("code");
			    String zhanjiResult=null;
			    JSONObject data =jsonObj.optJSONObject("data");
			    JSONObject body = data.optJSONObject("body");
			    System.out.println("json字符串"+body);
			    if(body!=null){
			    	zhanjiResult = body.getString(match_id);
			    	return zhanjiResult;
			    }
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}   
		return null;
	}
	
	//还没用到
	public static JSONObject getBody(String match_id){
		
		String url = "http://bbs.lottery.org.cn/match/matchresult.php?match_id="
				+ match_id+"&p_code=HHAD&type=fb";
		String result = HttpServices.getHttpData(url);

		if (result == null || "".equals(result)) {
			SystemClock.sleep(15);
			result = HttpServices.getHttpData(url);
		}
		if (result != null) {
              
			try {   
				JSONObject jsonObj = new JSONObject(result);
			    JSONObject status = jsonObj.optJSONObject("status");
			    String code = status.getString("code");
			    JSONObject data =jsonObj.optJSONObject("data");
			    JSONObject body = data.optJSONObject("body");
			    System.out.println("json字符串"+body);
			    if(body!=null){
			    	return body;
			    }
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}   
		
		
		
		
		return null;
		
	}

}
