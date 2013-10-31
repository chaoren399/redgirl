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
import org.json.JSONObject;


public class ShopJsonData {
	
	
	public static List<Map<String, String>> getJson() {

		String jsonText = getHTML(
				"http://home.lottery.org.cn/mapinfo/getloc.php?c=fb&state=%E5%8C%97%E4%BA%AC%E5%B8%82&city=%E5%8C%97%E4%BA%AC%E5%B8%82",
				"utf-8");
		try {
			JSONArray jsonobject = new JSONObject(jsonText).getJSONArray("data");
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();

			for (int i = 0; i < jsonobject.length(); i++) {
				JSONObject jsonObject2 = (JSONObject) jsonobject.optJSONObject(i);
				
				Map<String, String> map1 = new HashMap<String, String>();
				
				map1.put("id", jsonObject2.optString("id"));
				map1.put("province", jsonObject2.optString("province"));
				map1.put("city", jsonObject2.optString("city"));
				map1.put("address", jsonObject2.optString("address"));
				map1.put("tel", jsonObject2.optString("tel"));
				map1.put("mapPos", jsonObject2.optString("mapPos"));
				map1.put("serviceScope", jsonObject2.optString("serviceScope"));
				map1.put("displayIndex", jsonObject2.optString("displayIndex"));
				map1.put("wdinfoNum", jsonObject2.optString("wdinfoNum"));
				map1.put("en_state", jsonObject2.optString("en_state"));
				map1.put("en_city", jsonObject2.optString("en_city"));
				map1.put("lat", jsonObject2.optString("lat"));
				map1.put("lon", jsonObject2.optString("lon"));

				list.add(map1);

			}
			   return list;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	public static String getHTML(String pageURL, String encoding) {

		StringBuilder pageHTML = new StringBuilder();

		try {

			URL url = new URL(pageURL);
			System.out.println("地址为："+url);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			connection.setRequestProperty("User-Agent", "MSIE 7.0");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), encoding));

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
