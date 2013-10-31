package cn.chaoren.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




//获取的是竞彩足球界面的数据
//从服务器上获取数据
public class DisplayData {

public static List<Map<String, String>> getJson(){
        
    	String jsonText = getHTML("http://bbs.lottery.org.cn/match/matchlist.php","utf-8");
    	
        try {
			    
			JSONArray jsonObjs =new JSONObject(jsonText).getJSONArray("data");//.getJSONArray("data");
		   
			
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();

  
			
            for(int i = 0; i < jsonObjs.length() ; i++){   
            	JSONObject jsonObj = jsonObjs.optJSONObject(i);            	
                
    			Map<String, String> map = new HashMap<String, String>();

    			map.put("match_id", jsonObj.optString("match_id"));
    			map.put("play_time", jsonObj.optString("play_time"));
    			map.put("match_name", jsonObj.optString("game_name"));
    			map.put("league", jsonObj.optString("league"));//联赛
    			map.put("team_host", jsonObj.optString("team_host"));//主队
    			map.put("team_visiting", jsonObj.optString("team_visiting"));//客队
    			map.put("adjust", jsonObj.optString("adjust"));//0
    			map.put("color", jsonObj.optString("game_color"));//联赛的颜色
    			map.put("sp1",jsonObj.optString("sp1"));
    			map.put("sp3",jsonObj.optString("sp3"));
    			map.put("sp0",jsonObj.optString("sp0"));

    			list.add(map);
            }   
           
            return list;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;  
    }
    
    //连接服务器
    public static String getHTML(String pageURL, String encoding) {
    		 
    		        StringBuilder pageHTML = new StringBuilder();
    	
    		        try {
    		 
    		            URL url = new URL(pageURL);    		 
    		            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    		 
    		            connection.setRequestProperty("User-Agent", "MSIE 7.0");    		 
    		            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));
    		 
    		            String line = null;    		 
    		            while ((line = br.readLine()) != null) {    		 
    		                pageHTML.append(line);    		 
    		                pageHTML.append("\r\n");
    		            }
    		            connection.disconnect();
    		 
    		        } catch (Exception e) {    		 
    		            e.printStackTrace();    		 
    		        }
    		 
    		        return pageHTML.toString();
    		 
    		    }
	
}
