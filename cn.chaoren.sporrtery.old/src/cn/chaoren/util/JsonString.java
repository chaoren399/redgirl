package cn.chaoren.util;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonString {
	
	public static String getJsonData(String numstr){
	
		try {
            JSONObject result = new JSONObject(numstr);//转换为JSONObject
            int num = result.length();
            JSONArray nameList = result.getJSONArray("name");//获取JSONArray
            int length = nameList.length();
            String aa = "";
            for(int i = 0; i < length; i++){//遍历JSONArray
                JSONObject oj = nameList.getJSONObject(i);
                aa = aa + oj.getString("name")+"|";
                
            }
            Iterator<?> it = result.keys();
            String aa2 = "";
            String bb2 = null;
            while(it.hasNext()){//遍历JSONObject
                bb2 = (String) it.next().toString();
                aa2 = aa2 + result.getString(bb2);
                
            }
            return aa;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

	}
}
