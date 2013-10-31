package cn.chaoren.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ZhanjiresultData {

	public static List<Map<String, Object>> getZhanji(String numberstring) {

		try {
			JSONArray jsonresult = new JSONArray(numberstring);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			
			for (int i = 0; i < jsonresult.length(); i++) {
				JSONObject jsonObj = jsonresult.optJSONObject(i);

				JSONArray jsonarray = jsonObj.optJSONArray("num_str");
				List<String> list1  = new ArrayList<String>();
				for (int j = 0; j < jsonarray.length(); j++) {
					String aa = jsonarray.get(j).toString();
					list1.add(aa);
				}
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("num_str", list1);
				map.put("match_id", jsonObj.optString("match_id"));
				map.put("play_time", jsonObj.optString("play_time"));
				map.put("match_name", jsonObj.optString("match_name"));
				map.put("league", jsonObj.optString("league"));// 联赛
				map.put("team_host", jsonObj.optString("team_host"));// 主队
				map.put("team_visiting", jsonObj.optString("team_visiting"));// 客队
				map.put("adjust", jsonObj.optString("adjust"));// 0
				map.put("color", jsonObj.optString("game_color"));// 联赛的颜色
				map.put("sp1", jsonObj.optString("sp1"));
				map.put("sp3", jsonObj.optString("sp3"));
				map.put("sp0", jsonObj.optString("sp0"));

				list.add(map);
			}
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

	}

}
