package cn.chaoren.net;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.SystemClock;

import com.mobclick.android.a;

import cn.chaoren.action.StringUtil;
import cn.chaoren.net.JSONBean.Data;
import cn.chaoren.net.JSONBean.KeyData;
import cn.chaoren.net.KeyMapBean.KeyMap;
import cn.chaoren.util.StringUtils;

/**
 * 类说明：
 * 
 * @创建时间 2011-6-25 下午10:40:06
 * @创建人： 陆林
 * @邮箱：15301586841@189.cn
 */


// 受注赛程界面，赛果开将界面向服务器发送请求
public class HttpServices {
	private static String auth_value = "0123456789012345";

	//根据路径向服务器发送请求，并获得到对应的结果
	public static synchronized String getHttpData(String httpurl) {
		InputStream is = null;
		HttpURLConnection conn = null;
		System.out.println("URL=" + httpurl);
		try {
			URL url = new URL(httpurl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(2000);
			conn.setReadTimeout(2000);
			conn.connect();
			url = null;
			is = conn.getInputStream();
			int code = conn.getResponseCode();
			if (code == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				StringBuffer result = new StringBuffer();
				String line = null;
				while ((line = reader.readLine()) != null) {
					result.append(line + "\n");
				}
				reader.close();
				System.out.println("result = " + result.toString());
				return result.toString();
			}
			return null;
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if (is != null)
					is.close();
				if (conn != null)
					conn.disconnect();
				is = null;
				conn = null;
			} catch (Exception e) {
			}
			SystemClock.sleep(5);
		}
	}

	
	//请求服务器的路径
	public static JSONBean getNewVersion() {
		String url = "http://info.sporttery.com/interface/m/?c=app&a=get_android_ver&auth_type=uuid&auth_value="
				+ auth_value;
		/*String url = "http://info.sporttery.com/interface/m/?c=app&a=get_android_ver2&auth_type=uuid&auth_value="
				+ auth_value;*/
		String result = getHttpData(url);
		JSONBean ms = null;
		if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = getHttpData(url);
		}
		if (result != null) {
			try {
				// System.out.println(result);
				JSONObject jsonObj = new JSONObject(result);
				JSONObject status = jsonObj.optJSONObject("status");
				String code = status.getString("code");
				// System.out.println("code=" + code);
				ms = new JSONBean(code);
				JSONObject data = jsonObj.optJSONObject("data");
				JSONArray body = data.optJSONArray("body");
				if (body != null) {
					// System.out.println("body length = " + body.length());
					for (int i = 0; i < body.length(); i++) {
						JSONObject obj = body.optJSONObject(i);
						Map<String, String> map = new HashMap<String, String>();
						map.put("appversion", obj.getString("appversion"));
						ms.bodys.add(map);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ms;
	}

	public static JSONBean getNewVersionData() {
		String url = "http://info.sporttery.com/interface/m/?c=app&a=get_android&auth_type=uuid&auth_value="
				+ auth_value;
		/*String url = "http://info.sporttery.com/interface/m/?c=app&a=get_android2&auth_type=uuid&auth_value="
				+ auth_value;*/
		String result = getHttpData(url);
		JSONBean ms = null;
		if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = getHttpData(url);
		}
		if (result != null) {
			try {
				// System.out.println(result);
				JSONObject jsonObj = new JSONObject(result);
				JSONObject status = jsonObj.optJSONObject("status");
				String code = status.getString("code");
				// System.out.println("code=" + code);
				ms = new JSONBean(code);
				JSONObject data = jsonObj.optJSONObject("data");
				JSONArray body = data.optJSONArray("body");
				if (body != null) {
					// System.out.println("body length = " + body.length());
					for (int i = 0; i < body.length(); i++) {
						JSONObject obj = body.optJSONObject(i);
						Map<String, String> map = new HashMap<String, String>();
						map.put("appdownurl", obj.getString("appdownurl"));
						map.put("appreadme", obj.getString("appreadme"));
						ms.bodys.add(map);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ms;
	}

	/**
	 * 得到足球赛程
	 * 
	 * @param date
	 *            日期 yyyy-MM-dd
	 * @return
	 */
	public static JSONBean getMatchScheduleFB(String numw) {
		String url = "http://info.sporttery.com/interface/m/?c=fb&a=get_betting&numw="
				+ StringUtils.getWeekKey(numw)
				+ "&auth_type=uuid&auth_value="
				+ auth_value;
		String result = getHttpData(url);
		JSONBean ms = null;
		if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = getHttpData(url);
		}
		if (result != null) {
			try {
				// System.out.println(result);
				JSONObject jsonObj = new JSONObject(result);
				JSONObject status = jsonObj.optJSONObject("status");
				String code = status.getString("code");
				// System.out.println("code=" + code);
				ms = new JSONBean(code);
				JSONObject data = jsonObj.optJSONObject("data");
				JSONObject head = data.optJSONObject("head");
				String cnumw = head.getString("cnumw");
				if (cnumw != null && !cnumw.equals("")) {
					ms.cnumw = StringUtils.WEEK1.get(cnumw);
				}
				JSONArray datearray = head.optJSONArray("numw");
				for (int i = 0; i < datearray.length(); i++) {
					System.out.println(datearray.get(i));
					ms.times.add(StringUtils.WEEK1.get(datearray.get(i)
							.toString()));
				}
				JSONArray body = data.optJSONArray("body");
				if (body != null) {
					// System.out.println("body length = " + body.length());
					for (int i = 0; i < body.length(); i++) {
						JSONObject obj = body.optJSONObject(i);
						Map<String, String> map = new HashMap<String, String>();
						map.put("c_type", "fb");
						map.put("num", obj.getString("num"));
						map.put("m_id", obj.getString("m_id"));
						map.put("l_cn", obj.getString("l_cn"));
						map.put("l_cn2", obj.getString("l_cn2"));
						map.put("l_id", obj.getString("l_id"));
						map.put("h_cn", obj.getString("h_cn"));
						map.put("h_id", obj.getString("h_id"));
						map.put("a_cn", obj.getString("a_cn"));
						map.put("a_id", obj.getString("a_id"));
						map.put("a", obj.getString("a"));
						map.put("d", obj.getString("d"));
						map.put("h", obj.getString("h"));
						String sstime = obj.getString("sstime");
						map.put("sstime", sstime);
						map.put("shottime",
								sstime.substring(sstime.indexOf(" "),
										sstime.lastIndexOf(":")));
						map.put("shotdate",
								sstime.substring(0, sstime.indexOf(" ")));
						String handicap = obj.optString("goalline","0");
						map.put("handicap", handicap);
						map.put("message", obj.optString("tip", ""));
						
						ms.bodys.add(map);
						// System.out.println("num=" + obj.getString("num"));
						// System.out.println("m_id=" + obj.getString("m_id"));
						// System.out.println("l_cn=" + obj.getString("l_cn"));
						// System.out.println("l_cn2=" +
						// obj.getString("l_cn2"));
						// System.out.println("l_id=" + obj.getString("l_id"));
						// System.out.println("h_cn=" + obj.getString("h_cn"));
						// System.out.println("h_id=" + obj.getString("h_id"));
						// System.out.println("a_cn=" + obj.getString("a_cn"));
						// System.out.println("a_id=" + obj.getString("a_id"));
						// System.out.println("a=" + obj.getString("a"));
						// System.out.println("d=" + obj.getString("d"));
						// System.out.println("h=" + obj.getString("h"));
						// System.out.println("sstime=" +
						// obj.getString("sstime"));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ms;
	}

	/**
	 * 欧盘数据
	 * 
	 * @param id
	 *            比赛ID
	 * @return
	 */
	public static JSONBean getEuropeFB(String id) {
		String url = "http://info.sporttery.com/interface/m/?c=fb&a=get_europe&mid="
				+ id + "&auth_type=uuid&auth_value=" + auth_value;
		String result = getHttpData(url);
		JSONBean ms = null;
		if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = getHttpData(url);
		}
		if (result != null) {
			try {
				// System.out.println(result);
				JSONObject jsonObj = new JSONObject(result);
				JSONObject status = jsonObj.optJSONObject("status");
				String code = status.getString("code");
				// System.out.println("code=" + code);
				ms = new JSONBean(code);
				JSONObject data = jsonObj.optJSONObject("data");
				JSONArray body = data.optJSONArray("body");
				if (body != null) {
					// System.out.println("body length = " + body.length());
					for (int i = 0; i < body.length(); i++) {
						JSONObject obj = body.optJSONObject(i);
						Map<String, String> map = new HashMap<String, String>();
						map.put("corp",
								StringUtils.getString(obj.getString("corp")));
						map.put("w", StringUtils.getString(obj.getString("w")));
						map.put("d", StringUtils.getString(obj.getString("d")));
						map.put("a", StringUtils.getString(obj.getString("a")));
						map.put("wa",
								StringUtils.getString(obj.getString("wa")));
						map.put("da",
								StringUtils.getString(obj.getString("da")));
						map.put("aa",
								StringUtils.getString(obj.getString("aa")));
						map.put("kw",
								StringUtils.getString(obj.getString("kw")));
						map.put("kd",
								StringUtils.getString(obj.getString("kd")));
						map.put("ka",
								StringUtils.getString(obj.getString("ka")));
						map.put("rr",
								StringUtils.getString(obj.getString("rr")));
						ms.bodys.add(map);
						
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ms;
	}

	
	
	
	public static KeyMapBean getFeedBackPreFB(String id,String all)//FIXME
	{
		
		KeyMapBean ms = new KeyMapBean("0");
		HttpServiceNew newHttp = new HttpServiceNew();
		
		
	/*	String[]homeWins={"全部","1:0","2:0","2:1","3:0","3:1","3:2","4:0","4:1","4:2","5:0","5:1","5:2","胜其他"};
		String[]awayWins={"全部","0:1","0:2","1:2","0:3","1:3","2:3","0:4","1:4","2:4","0:5","1:5","2:5","负其他"};
		String []draws={"全部","0:0","1:1","2:2","3:3","平其他"};
		String[]handicaps={"全部","-1","1","-2","2","-3","3","-4","4","其他"};
		String[]leaguls={"全部","法甲","德甲","意甲"};*/
		
		HashMap<String, KeyMap> dataList = ms.keyList;
		newHttp.getSearchHDASelections(all, null, null, null,dataList);
		dataList.put("goalline", newHttp.getSearchGoallineSelections());
		dataList.put("llist", newHttp.getSearchLListSelections(all, null));
		dataList.put("hteam",new KeyMap());
		dataList.put("ateam",new KeyMap());
		
		/*dataList.add(homeWins);
		dataList.add(draws);
		dataList.add(awayWins);
		dataList.add(handicaps);
		dataList.add(leaguls);
		//主队,客队
		String[]data=new String[6];
		dataList.add(data);
		data[0]="全部";
		for (int i = 0; i <5; i++) {
			data[i+1]="主队"+(i+1);
		}
		
		data=new String[6];
		dataList.add(data);
		data[0]="全部";
		for (int i = 0; i <5; i++) {
			data[i+1]="客队"+(i+1);
		}*/
		return ms;
	}
	
	
	
	public static HeaderJSONBean getFeedBackFB(String hid,String aid,String lid,String goalline,String type,String ho,String d,String ao,String count)
	{
		//对请求路径拼接
		StringBuilder sb = new StringBuilder();
		if (hid != null && !"".equals(hid)) {
			sb.append("&hid="+hid);
		}
		if (aid != null &&  !"".equals(aid)) {
			sb.append("&aid="+aid);
		}
		if (lid != null &&  !"".equals(lid)) {
			sb.append("&lid="+lid);
		}
		if (goalline != null &&  !"".equals(goalline)) {
			sb.append("&goalline="+goalline);
		}
		if (type != null &&  !"".equals(type)) {
			sb.append("&type="+type);
		}
		if (ho != null &&  !"".equals(ho)) {
			sb.append("&ho="+ho);
		}
		if (d != null &&  !"".equals(d)) {
			sb.append("&do="+d);
		}
		if (ao != null &&  !"".equals(ao)) {
			sb.append("&ao="+ao);
		}
		if (count != null &&  !"".equals(count)) {
			sb.append("&count="+count);
		}
		
		String url = "http://info.sporttery.com/interface/m/?c=fb&a=get_sodds_had_result"+sb.toString()+"&auth_type=uuid&auth_value=0123456789012345";
		String result = getHttpData(url);
		if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = getHttpData(url);
		}
		HeaderJSONBean ms = null;
		try {
			JSONObject jsonObj = new JSONObject(result);
			if (jsonObj != null) {
				JSONObject statusObj = jsonObj.optJSONObject("status");
				if (statusObj != null) {
					ms= new HeaderJSONBean(statusObj.optString("code", "0"));
				}
				
				JSONObject dataObj = jsonObj.optJSONObject("data");
				if (dataObj != null) {
					if (ms == null) {
						ms = new HeaderJSONBean("0");
					}
					
					JSONObject headObj = dataObj.optJSONObject("head");
					if (headObj != null) {
						String temp = null;
						JSONObject dictObj = headObj.optJSONObject("dict");
						if (dictObj != null) {
							Map<String, String> header = ms.header;
							JSONObject staObj = headObj.optJSONObject("sta");//sta
							if (staObj != null) {
								
								
								header.put("h", staObj.optString("win","0"));//win
								header.put("d", staObj.optString("draw","0"));//draw
								header.put("a", staObj.optString("lose","0"));//lose
								StringUtil util = new StringUtil();
								header.put("total", ""+(util.optInt(header.get("h"), 0)+util.optInt(header.get("d"), 0)+util.optInt(header.get("a"), 0)));//
							}
							
							JSONObject typeObj = dictObj.optJSONObject("type");
							if (typeObj != null) {
								Iterator<String> iterator = typeObj.keys();
								
								while (iterator.hasNext()) {
									temp = iterator.next();
									header.put(temp, typeObj.optString(temp));
								}
							}
						}
						
					}
					JSONArray bodyArray = dataObj.optJSONArray("body");
					if (bodyArray != null) {
						/**
						 * date
						 * home
						 * away
						 * handicap
						 * h
						 * d
						 * a
						 * result
						 * legual
						 * status
						 */
						List<Map<String, String>> mapList = ms.bodys;
						Map<String, String> map = null;
						JSONObject obj = null;
						int length = bodyArray.length();
						for (int i = 0; i < length; i++) {
							obj = bodyArray.optJSONObject(i);
							map = new HashMap<String, String>();
							mapList.add(map);
							map.put("date", obj.optString("date"));//date
							map.put("home", obj.optString("h_cn"));//h_cn
							map.put("away", obj.optString("a_cn"));//a_cn
							
							map.put("handicap", obj.optString("goalline"));//goalline
							map.put("h", obj.optString("ho"));//ho
							map.put("d", obj.optString("do"));//do
							map.put("a", obj.optString("ao"));//ao
							map.put("result",obj.optString("final"));//final
							map.put("legual", obj.optString("l_cn"));//l_cn
							map.put("type", obj.optString("type"));//type 类型
							map.put("status", ms.header.get(obj.opt("type")));
						}
						
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		/*String url = "";
		String result = getHttpData(url);
		if (result != null) {			
			
		}*/
		
		
		
		return ms;
	}
	
	/**
	 * 亚盘数据
	 * 
	 * @param id
	 *            比赛ID
	 * @return
	 */
	public static JSONBean getAsiaFB(String id) {
		String url = "http://info.sporttery.com/interface/m/?c=fb&a=get_asia&mid="
				+ id + "&auth_type=uuid&auth_value=" + auth_value;
		String result = getHttpData(url);
		JSONBean ms = null;
		if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = getHttpData(url);
		}
		if (result != null) {
			try {
				// System.out.println(result);
				JSONObject jsonObj = new JSONObject(result);
				JSONObject status = jsonObj.optJSONObject("status");
				String code = status.getString("code");
				// System.out.println("code=" + code);
				ms = new JSONBean(code);
				JSONObject data = jsonObj.optJSONObject("data");
				JSONArray body = data.optJSONArray("body");
				if (body != null) {
					// System.out.println("body length = " + body.length());
					for (int i = 0; i < body.length(); i++) {
						JSONObject obj = body.optJSONObject(i);
						Map<String, String> map = new HashMap<String, String>();
						map.put("corp",
								StringUtils.getString(obj.getString("corp")));
						map.put("w1",
								StringUtils.getString(obj.getString("w1")));
						map.put("d1",
								StringUtils.getString(obj.getString("d1")));
						map.put("l1",
								StringUtils.getString(obj.getString("l1")));
						map.put("w2",
								StringUtils.getString(obj.getString("w2")));
						map.put("d2",
								StringUtils.getString(obj.getString("d2")));
						map.put("l2",
								StringUtils.getString(obj.getString("l2")));
						map.put("h3",
								StringUtils.getString(obj.getString("h3")));
						map.put("d3",
								StringUtils.getString(obj.getString("d3")));
						map.put("a3",
								StringUtils.getString(obj.getString("a3")));
						ms.bodys.add(map);
						// System.out.println("corp=" + obj.getString("corp"));
						// System.out.println("w1=" + obj.getString("w1"));
						// System.out.println("d1=" + obj.getString("d1"));
						// System.out.println("l1=" + obj.getString("l1"));
						// System.out.println("w2=" + obj.getString("w2"));
						// System.out.println("d2=" + obj.getString("d2"));
						// System.out.println("l2=" + obj.getString("l2"));
						// System.out.println("h3=" + obj.getString("h3"));
						// System.out.println("d3=" + obj.getString("d3"));
						// System.out.println("a3=" + obj.getString("a3"));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ms;
	}

	/**
	 * 足球两队往季对阵竞彩奖金赛果
	 * 
	 * @param hid
	 *            主队
	 * @param aid
	 *            客队
	 * @return
	 */
	public static JSONBean getTwoPrevFB(String hid, String aid) {
		String url = "http://info.sporttery.com/interface/m/?c=fb&a=get_two_prev&hid="
				+ hid
				+ "&aid="
				+ aid
				+ "&auth_type=uuid&auth_value="
				+ auth_value;
		String result = getHttpData(url);
		JSONBean ms = null;
		if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = getHttpData(url);
		}
		if (result != null) {
			try {
				// System.out.println(result);
				JSONObject jsonObj = new JSONObject(result);
				JSONObject status = jsonObj.optJSONObject("status");
				String code = status.getString("code");
				// System.out.println("code=" + code);
				ms = new JSONBean(code);
				JSONObject data = jsonObj.optJSONObject("data");
				JSONArray body = data.optJSONArray("body");
				if (body != null) {
					// System.out.println("body length = " + body.length());
					for (int i = 0; i < body.length(); i++) {
						JSONObject obj = body.optJSONObject(i);
						Map<String, String> map = new HashMap<String, String>();
						map.put("l_cn", obj.getString("l_cn"));
						map.put("date", obj.getString("date"));
						map.put("h_cn", obj.getString("h_cn"));
						map.put("goalline", obj.getString("goalline"));
						map.put("a_cn", obj.getString("a_cn"));
						map.put("half",
								StringUtils.getString(obj.getString("half")));
						map.put("final",
								StringUtils.getString(obj.getString("final")));
						map.put("hhad",
								StringUtils.getString(obj.getString("hhad")));
						map.put("a", StringUtils.getString(obj.getString("a")));
						map.put("d", StringUtils.getString(obj.getString("d")));
						map.put("h", StringUtils.getString(obj.getString("h")));
						map.put("crs",
								StringUtils.getString(obj.getString("crs")));
						map.put("ttg",
								StringUtils.getString(obj.getString("ttg")));
						map.put("hafu",
								StringUtils.getString(obj.getString("hafu")));
						ms.bodys.add(map);
						// System.out.println("l_cn=" + obj.getString("l_cn"));
						// System.out.println("date=" + obj.getString("date"));
						// System.out.println("h_cn" + obj.getString("h_cn"));
						// System.out.println("goalline="
						// + obj.getString("goalline"));
						// System.out.println("a_cn=" + obj.getString("a_cn"));
						// System.out.println("half=" + obj.getString("half"));
						// System.out.println("final=" +
						// obj.getString("final"));
						// System.out.println("hhad=" + obj.getString("hhad"));
						// System.out.println("a=" + obj.getString("a"));
						// System.out.println("d=" + obj.getString("d"));
						// System.out.println("h=" + obj.getString("h"));
						// System.out.println("crs=" + obj.getString("crs"));
						// System.out.println("ttg=" + obj.getString("ttg"));
						// System.out.println("hafu=" + obj.getString("hafu"));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ms;
	}

	/**
	 * 得到篮球赛程
	 * 
	 * @param date
	 *            日期 yyyy-MM-dd
	 * @return
	 */
	public static JSONBean getMatchScheduleBK(String numw) {
		String url = "http://info.sporttery.com/interface/m/?c=bk&a=get_betting&numw="
				+ StringUtils.getWeekKey(numw)
				+ "&auth_type=uuid&auth_value="
				+ auth_value;
		String result = getHttpData(url);
		JSONBean ms = null;
		if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = getHttpData(url);
		}
		if (result != null) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				JSONObject status = jsonObj.optJSONObject("status");
				String code = status.getString("code");
				// System.out.println("code=" + code);
				ms = new JSONBean(code);
				JSONObject data = jsonObj.optJSONObject("data");
				if (data != null) {
					JSONObject head = data.optJSONObject("head");
					String cnumw = head.getString("cnumw");
					if (cnumw != null && !cnumw.equals("")) {
						ms.cnumw = StringUtils.WEEK1.get(cnumw);
					}
					JSONArray datearray = head.optJSONArray("numw");
					// System.out.println("date length = " +
					// datearray.length());
					for (int i = 0; i < datearray.length(); i++) {
						System.out.println(datearray.get(i));
						ms.times.add(StringUtils.WEEK1.get(datearray.get(i)
								.toString()));
					}
					JSONArray body = data.optJSONArray("body");
					if (body != null) {
						// System.out.println("body length = " + body.length());
						for (int i = 0; i < body.length(); i++) {
							JSONObject obj = body.optJSONObject(i);
							Map<String, String> map = new HashMap<String, String>();
							map.put("c_type", "bk");
							map.put("num", obj.getString("num"));
							map.put("m_id", obj.getString("m_id"));
							map.put("l_cn", obj.getString("l_cn"));
							map.put("l_id", obj.getString("l_id"));
							map.put("h_cn", obj.getString("h_cn"));
							map.put("h_id", obj.getString("h_id"));
							map.put("a_cn", obj.getString("a_cn"));
							map.put("a_id", obj.getString("a_id"));
							String sstime = obj.getString("sstime");
							map.put("sstime", sstime);
							map.put("shottime", sstime.substring(
									sstime.indexOf(" "),
									sstime.lastIndexOf(":")));
							map.put("shotdate",
									sstime.substring(0, sstime.indexOf(" ")));
							
							/*String handicap = (i%2==0)?"0":i+"";*/
							map.put("handicap", "");
							map.put("message", obj.optString("tip", ""));
							ms.bodys.add(map);
							
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ms;
	}

	/**
	 * 篮球两队往季对阵竞彩奖金赛果
	 * 
	 * @param hid
	 *            主队
	 * @param aid
	 *            客队
	 * @return
	 */
	public static JSONBean getTwoPrevBK(String hid, String aid) {
		String url = "http://info.sporttery.com/interface/m/?c=bk&a=get_two_prev&hid="
				+ hid
				+ "&aid="
				+ aid
				+ "&auth_type=uuid&auth_value="
				+ auth_value;
		String result = getHttpData(url);
		JSONBean ms = null;
		if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = getHttpData(url);
		}
		if (result != null) {
			try {
				// System.out.println(result);
				JSONObject jsonObj = new JSONObject(result);
				JSONObject status = jsonObj.optJSONObject("status");
				String code = status.getString("code");
				// System.out.println("code=" + code);
				ms = new JSONBean(code);
				JSONObject data = jsonObj.optJSONObject("data");
				JSONArray body = data.optJSONArray("body");
				if (body != null) {
					// System.out.println("body length = " + body.length());
					for (int i = 0; i < body.length(); i++) {
						JSONObject obj = body.optJSONObject(i);
						Map<String, String> map = new HashMap<String, String>();
						map.put("date", obj.getString("date"));
						map.put("a_cn", obj.getString("a_cn"));
						map.put("h_cn", obj.getString("h_cn"));
						map.put("a1",
								StringUtils.getString(obj.getString("a1")));
						map.put("a2",
								StringUtils.getString(obj.getString("a2")));
						map.put("a3",
								StringUtils.getString(obj.getString("a3")));
						map.put("a4",
								StringUtils.getString(obj.getString("a4")));
						map.put("af",
								StringUtils.getString(obj.getString("af")));
						map.put("h1",
								StringUtils.getString(obj.getString("h1")));
						map.put("h2",
								StringUtils.getString(obj.getString("h2")));
						map.put("h3",
								StringUtils.getString(obj.getString("h3")));
						map.put("h4",
								StringUtils.getString(obj.getString("h4")));
						map.put("hf",
								StringUtils.getString(obj.getString("hf")));
						map.put("mnl",
								StringUtils.getString(obj.getString("mnl")));
						map.put("hdc",
								StringUtils.getString(obj.getString("hdc")));
						map.put("wnm",
								StringUtils.getString(obj.getString("wnm")));
						map.put("hilo",
								StringUtils.getString(obj.getString("hilo")));
						ms.bodys.add(map);
						
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ms;
	}

	/**
	 * 某足球球队近期战绩
	 * 
	 * @param id
	 *            球队ID
	 * @param num
	 *            场次
	 * @return
	 */
	public static JSONBean getMatchInfoFB(String id, int num) {
		String url = "http://info.sporttery.com/interface/m/?c=fb&a=get_recent_record&id="
				+ id
				+ "&num="
				+ num
				+ "&auth_type=uuid&auth_value="
				+ auth_value;
		String result = getHttpData(url);
		JSONBean ms = null;
		if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = getHttpData(url);
		}
		if (result != null) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				JSONObject status = jsonObj.optJSONObject("status");
				String code = status.getString("code");
				// System.out.println("code=" + code);
				ms = new JSONBean(code);
				JSONObject data = jsonObj.optJSONObject("data");
				JSONArray body = data.optJSONArray("body");
				// System.out.println("body length = " + body.length());
				for (int i = 0; i < body.length(); i++) {
					JSONObject obj = body.optJSONObject(i);
					Map<String, String> map = new HashMap<String, String>();
					map.put("l_cn", obj.getString("l_cn"));
					map.put("date", obj.getString("date"));
					map.put("h_cn", obj.getString("h_cn"));
					map.put("goalline", obj.getString("goalline"));
					map.put("a_cn", obj.getString("a_cn"));
					map.put("half",
							StringUtils.getString(obj.getString("half")));
					map.put("final",
							StringUtils.getString(obj.getString("final")));
					map.put("hhad",
							StringUtils.getString(obj.getString("hhad")));
					map.put("h", StringUtils.getString(obj.getString("h")));
					map.put("d", StringUtils.getString(obj.getString("d")));
					map.put("a", StringUtils.getString(obj.getString("a")));
					map.put("crs", StringUtils.getString(obj.getString("crs")));
					map.put("ttg", StringUtils.getString(obj.getString("ttg")));
					map.put("hafu",
							StringUtils.getString(obj.getString("hafu")));
					ms.bodys.add(map);
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ms;
	}

	/**
	 * 某篮球球队近期战绩
	 * 
	 * @param id
	 *            球队id
	 * @param num
	 *            场次
	 * @return
	 */
	public static JSONBean getMatchInfoBK(String id, int num) {
		String url = "http://info.sporttery.com/interface/m/?c=bk&a=get_recent_record&id="
				+ id
				+ "&num="
				+ num
				+ "&auth_type=uuid&auth_value="
				+ auth_value;
		String result = getHttpData(url);
		JSONBean ms = null;
		if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = getHttpData(url);
		}
		if (result != null) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				JSONObject status = jsonObj.optJSONObject("status");
				String code = status.getString("code");
				// System.out.println("code=" + code);
				ms = new JSONBean(code);
				JSONObject data = jsonObj.optJSONObject("data");
				JSONArray body = data.optJSONArray("body");
				// System.out.println("body length = " + body.length());
				for (int i = 0; i < body.length(); i++) {
					JSONObject obj = body.optJSONObject(i);
					Map<String, String> map = new HashMap<String, String>();
					map.put("l_cn", obj.getString("l_cn"));
					map.put("date", obj.getString("date"));
					map.put("h_cn", obj.getString("h_cn"));
					map.put("h_id", obj.getString("h_id"));
					map.put("a_cn", obj.getString("a_cn"));
					map.put("a_id", obj.getString("a_id"));
					map.put("h_s", StringUtils.getString(obj.getString("h_s")));
					map.put("a_s", StringUtils.getString(obj.getString("a_s")));
					map.put("hhad",
							StringUtils.getString(obj.getString("hhad")));
					map.put("mnl", StringUtils.getString(obj.getString("mnl")));
					map.put("hilo",
							StringUtils.getString(obj.getString("hilo")));
					ms.bodys.add(map);
				
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ms;
	}

	/**
	 * 足球未来5场赛事
	 * 
	 * @param id
	 *            球队ID
	 * @return
	 */
	public static JSONBean getFutureFB(String id) {
		String url = "http://info.sporttery.com/interface/m/?c=fb&a=get_future&id="
				+ id + "&auth_type=uuid&auth_value=" + auth_value;
		String result = getHttpData(url);
		JSONBean ms = null;
		if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = getHttpData(url);
		}
		if (result != null) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				JSONObject status = jsonObj.optJSONObject("status");
				String code = status.getString("code");
				// System.out.println("code=" + code);
				ms = new JSONBean(code);
				JSONObject data = jsonObj.optJSONObject("data");
				JSONArray body = data.optJSONArray("body");
				// System.out.println("body length = " + body.length());
				for (int i = 0; i < body.length(); i++) {
					JSONObject obj = body.optJSONObject(i);
					Map<String, String> map = new HashMap<String, String>();
					map.put("l_cn", obj.getString("l_cn"));
					map.put("date", obj.getString("date"));
					map.put("h_cn", obj.getString("h_cn"));
					map.put("a_cn", obj.getString("a_cn"));
					ms.bodys.add(map);
				
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ms;
	}

	/**
	 * 足球联赛排名
	 * 
	 * @param sid
	 *            联赛ID
	 * @param order
	 *            可选 按主场、客场还是全部，主场:’h’，客场:’a’, 全部:’’, 默认为全部
	 * @return
	 */
	public static JSONBean getOrderFB(String l_cn, String order) {
		if (l_cn == null || l_cn.equals("")) {
			return new JSONBean("0");
		}
		String url = "http://info.sporttery.com/interface/m/?c=fb&a=get_team_order&lcn="
				+ StringUtils.encode(l_cn)
				+ "&order="
				+ order
				+ "&auth_type=uuid&auth_value=" + auth_value;
		String result = getHttpData(url);
		JSONBean ms = null;
		if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = getHttpData(url);
		}
		if (result != null) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				JSONObject status = jsonObj.optJSONObject("status");
				String code = status.getString("code");
				System.out.println("code=" + code);
				ms = new JSONBean(code);
				JSONObject data = jsonObj.optJSONObject("data");
				JSONArray body = data.optJSONArray("body");
				// System.out.println("body length = " + body.length());
				for (int i = 0; i < body.length(); i++) {
					JSONObject obj = body.optJSONObject(i);
					Map<String, String> map = new HashMap<String, String>();
					map.put("index", obj.getString("index"));
					map.put("cn", obj.getString("cn"));
					map.put("id", obj.getString("id"));
					map.put("count", obj.getString("count"));
					map.put("win", obj.getString("win"));
					map.put("draw", obj.getString("draw"));
					map.put("lose", obj.getString("lose"));
					map.put("goal", obj.getString("goal"));
					map.put("losegoal", obj.getString("losegoal"));
					map.put("net", obj.getString("net"));
					map.put("score", obj.getString("score"));
					ms.bodys.add(map);
				
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ms;
	}

	/**
	 * 足球玩法
	 * 
	 * @param id
	 *            比赛ID
	 * @return
	 */
	public static JSONBeanPlayFB getPlayFB(String id) {
		String url = "http://info.sporttery.com/interface/m/?c=fb&a=get_play&id="
				+ id + "&auth_type=uuid&auth_value=" + auth_value;
		String result = getHttpData(url);
		JSONBeanPlayFB ms = null;
		if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = getHttpData(url);
		}
		if (result != null) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				JSONObject status = jsonObj.optJSONObject("status");
				String code = status.getString("code");
				// System.out.println("code=" + code);
				ms = new JSONBeanPlayFB(code);
				JSONObject data = jsonObj.optJSONObject("data");
				JSONObject goaline = data.optJSONObject("goalline");
				if (goaline != null) {
					String hhad = goaline.getString("hhad");
					// System.out.println("hdc=" + hdc);
					ms.goaline = hhad;
				}
				JSONObject body = data.optJSONObject("body");
				JSONArray hhad = body.optJSONArray("hhad");
				// System.out.println("hhad length = " + hhad.length());
				for (int i = 0; i < hhad.length(); i++) {
					JSONArray obj = hhad.optJSONArray(i);
					List<String> list = new ArrayList<String>();
					// System.out.println("obj length= " + obj.length());
					for (int j = 0; j < obj.length(); j++) {
						String s = obj.getString(j);
						list.add(s);
						// System.out.println("s= " + s);
					}
					ms.hhad.add(list);
				}
				JSONObject crs = body.optJSONObject("crs");
				{
					JSONArray h = crs.optJSONArray("h");
					List<List<String>> list = new ArrayList<List<String>>();
					// System.out.println("h length = " + h.length());
					for (int n = 0; n < h.length(); n++) {
						JSONArray obj = h.optJSONArray(n);
						// System.out.println("obj length= " + obj.length());
						List<String> list1 = new ArrayList<String>();
						for (int m = 0; m < obj.length(); m++) {
							String s = obj.getString(m);
							list1.add(s);
							// System.out.println("s= " + s);
						}
						list.add(list1);
					}
					ms.crs.add(list);
				}
				{
					JSONArray d = crs.optJSONArray("d");
					List<List<String>> list = new ArrayList<List<String>>();
					// System.out.println("d length = " + d.length());
					for (int n = 0; n < d.length(); n++) {
						JSONArray obj = d.optJSONArray(n);
						// System.out.println("obj length= " + obj.length());
						List<String> list1 = new ArrayList<String>();
						for (int m = 0; m < obj.length(); m++) {
							String s = obj.getString(m);
							list1.add(s);
							// System.out.println("s= " + s);
						}
						list.add(list1);
					}
					ms.crs.add(list);
				}
				{
					JSONArray a = crs.optJSONArray("a");
					List<List<String>> list = new ArrayList<List<String>>();
					// System.out.println("a length = " + a.length());
					for (int n = 0; n < a.length(); n++) {
						JSONArray obj = a.optJSONArray(n);
						// System.out.println("obj length= " + obj.length());
						List<String> list1 = new ArrayList<String>();
						for (int m = 0; m < obj.length(); m++) {
							String s = obj.getString(m);
							list1.add(s);
							System.out.println("s= " + s);
						}
						list.add(list1);
					}
					ms.crs.add(list);
				}

				JSONArray ttg = body.optJSONArray("ttg");
				// System.out.println("ttg length = " + ttg.length());
				for (int n = 0; n < ttg.length(); n++) {
					JSONArray obj = ttg.optJSONArray(n);
					List<String> list = new ArrayList<String>();
					System.out.println("obj length= " + obj.length());
					for (int m = 0; m < obj.length(); m++) {
						String s = obj.getString(m);
						list.add(s);
						// System.out.println("s= " + s);
					}
					ms.ttg.add(list);
				}

				JSONArray hafu = body.optJSONArray("hafu");
				// System.out.println("ttg length = " + hafu.length());
				for (int n = 0; n < hafu.length(); n++) {
					JSONArray obj = hafu.optJSONArray(n);
					List<String> list = new ArrayList<String>();
					System.out.println("obj length= " + obj.length());
					for (int m = 0; m < obj.length(); m++) {
						String s = obj.getString(m);
						list.add(s);
						// System.out.println("s= " + s);
					}
					ms.hafu.add(list);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ms;
	}

	/**
	 * 篮球玩法
	 * 
	 * @param id
	 *            比赛ID
	 * @return
	 */
	public static JSONBeanPlayBK getPlayBK(String id) {
		String url = "http://info.sporttery.com/interface/m/?c=bk&a=get_play&id="
				+ id + "&auth_type=uuid&auth_value=" + auth_value;
		String result = getHttpData(url);
		JSONBeanPlayBK ms = null;
		if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = getHttpData(url);
		}
		if (result != null) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				JSONObject status = jsonObj.optJSONObject("status");
				String code = status.getString("code");
				// System.out.println("code=" + code);
				ms = new JSONBeanPlayBK(code);
				JSONObject data = jsonObj.optJSONObject("data");
				{
					JSONObject goaline = data.optJSONObject("goalline");
					if (goaline != null) {
						String hdc = goaline.getString("hdc");
						ms.goaline = hdc;
					}
				}
				JSONObject body = data.optJSONObject("body");
				JSONArray mnl = body.optJSONArray("mnl");
				// System.out.println("mnl length = " + mnl.length());
				for (int i = 0; i < mnl.length(); i++) {
					JSONArray obj = mnl.optJSONArray(i);
					List<String> list = new ArrayList<String>();
					// System.out.println("obj length= " + obj.length());
					for (int j = 0; j < obj.length(); j++) {
						String s = obj.getString(j);
						list.add(s);
						// System.out.println("s= " + s);
					}
					ms.mnl.add(list);
				}

				JSONArray hdc = body.optJSONArray("hdc");
				// System.out.println("hdc length = " + hdc.length());
				for (int i = 0; i < hdc.length(); i++) {
					JSONArray obj = hdc.optJSONArray(i);
					List<String> list = new ArrayList<String>();
					// System.out.println("obj length= " + obj.length());
					for (int j = 0; j < obj.length(); j++) {
						String s = obj.getString(j);
						list.add(s);
						// System.out.println("s= " + s);
					}
					ms.hdc.add(list);
				}

				JSONArray wnm = body.optJSONArray("wnm");
				// System.out.println("wnm length = " + wnm.length());
				for (int i = 0; i < wnm.length(); i++) {
					JSONArray obj = wnm.optJSONArray(i);
					List<String> list = new ArrayList<String>();
					// System.out.println("obj length= " + obj.length());
					for (int j = 0; j < obj.length(); j++) {
						String s = obj.getString(j);
						list.add(s);
						// System.out.println("s= " + s);
					}
					ms.wnm.add(list);
				}

				JSONArray hilo = body.optJSONArray("hilo");
				// System.out.println("hilo length = " + hilo.length());
				for (int i = 0; i < hilo.length(); i++) {
					JSONArray obj = hilo.optJSONArray(i);
					List<String> list = new ArrayList<String>();
					System.out.println("obj length= " + obj.length());
					for (int j = 0; j < obj.length(); j++) {
						String s = obj.getString(j);
						list.add(s);
						// System.out.println("s= " + s);
					}
					ms.hilo.add(list);
				}
				return ms;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 获得足球联赛列表
	 * 
	 * @return
	 */
	public static JSONBean getLeagueListFB() {//
		String url = "http://info.sporttery.com/interface/m/?c=fb&a=get_league_list&auth_type=uuid&auth_value="
				+ auth_value;
		String result = getHttpData(url);
		JSONBean ms = null;
		if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = getHttpData(url);
		}
		if (result != null) {
			try {
				// System.out.println(result);
				JSONObject jsonObj = new JSONObject(result);
				JSONObject status = jsonObj.optJSONObject("status");
				String code = status.getString("code");
				// System.out.println("code=" + code);
				ms = new JSONBean(code);
				JSONObject data = jsonObj.optJSONObject("data");
				JSONArray body = data.optJSONArray("body");
				if (body != null) {
					// System.out.println("body length = " + body.length());
					for (int i = 0; i < body.length(); i++) {
						JSONObject obj = body.optJSONObject(i);
						Map<String, String> map = new HashMap<String, String>();
						map.put("l_id", obj.getString("l_id"));
						map.put("l_cn", obj.getString("l_cn"));
						ms.bodys.add(map);
						
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ms;
	}

	/**
	 * 获得足球赛果开奖
	 * 
	 * @param sdate
	 *            开始日期
	 * @param edate
	 *            结束日期
	 * @param lid
	 *            联赛ID
	 * @return
	 */
	public static JSONBean getMatchResultFB(String sdate, String edate,
			String lid) {
		String url = "http://info.sporttery.com/interface/m/?c=fb&a=get_match_result&sdate="
				+ sdate
				+ "&edate="
				+ edate
				+ "&lid="
				+ lid
				+ "&page=1&num=9999&auth_type=uuid&auth_value=" + auth_value;
		System.out.println("赛果开奖服务器的路径为：~~~~"+url);
		String result = getHttpData(url);
		JSONBean ms = null;
		if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = getHttpData(url);
		}
		if (result != null) {
			try {
				System.out.println("赛果开奖服务器数据：！！！"+result);
				JSONObject jsonObj = new JSONObject(result);
				JSONObject status = jsonObj.optJSONObject("status");
				String code = status.getString("code");
				// System.out.println("code=" + code);
				ms = new JSONBean(code);
				JSONObject data = jsonObj.optJSONObject("data");
				JSONArray body = data.optJSONArray("body");//body在data目录下面
				if (body != null) {
				// System.out.println("body length = " + body.length());
					String goalline = "";
					for (int i = 0; i < body.length(); i++) {
						JSONObject obj = body.optJSONObject(i);
						Map<String, String> map = new HashMap<String, String>();
						map.put("date", obj.getString("date"));
						map.put("num", obj.getString("num"));
						map.put("m_id", obj.getString("m_id"));
						map.put("l_cn", obj.getString("l_cn"));
						map.put("h_cn", obj.getString("h_cn"));
						goalline = obj.optString("goalline", "");
						if (!"".equals(goalline)) {
							goalline = "("+goalline+")";
						}
						map.put("goalline", goalline);
						/*if (obj.getString("goalline") != null
								&& obj.getString("goalline").length() > 1)
							map.put("goalline",
									"让球" + obj.getString("goalline"));
						else
							map.put("goalline", "");*/
						map.put("a_cn", obj.getString("a_cn"));
						map.put("half",
								"半场"
										+ (obj.getString("half").length() <= 1 ? "- : -"
												: obj.getString("half")));
						map.put("final",
								"全场"
										+ (obj.getString("final").length() <= 1 ? "- : -"
												: obj.getString("final")));
						map.put("status", obj.getString("status"));
						//将返回的数据放到map集合，并bodys集合当中去
						ms.bodys.add(map);
					
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ms;
	}

	
	
	public static JSONBeanSingleResultFB getSingleResult_FB(String mid)
	{
		JSONBeanSingleResultFB ss_fb = null;
		/**
		 * 测试
		 */
		ss_fb = new JSONBeanSingleResultFB("0");
		ss_fb.goaline="+1";
		String[][]h = {{"1:0","9.00"},{"2:0","9.50"},{"2:1","7.50"},{"3:0","15.00"},{"3:1","14.00"},
		{"3:2","25.00"},{"4:0","35.00"},{"4:1","30.00"},{"4:2","70.00"},{"5:0","100.0"},
		{"5:1","150.0"},{"5:2","150.0"},{"胜其他","70.00"}};
		
		String[][]d = {{"0:0","10.00"},{"1:1","5.90"},{"2:2","13.00"},{"3:3","70.00"},{"平其他","300.0"}};
		String[][]a = {{"0:1","9.00"},{"0:2","15.00"},{"1:2","9.50"},{"0:3","40.00"},{"1:3","28.00"},
				{"2:3","35.00"},{"0:4","150.0"},{"1:4","100.0"},{"2:4","125.0"},{"0:5","400.0"},
				{"1:5","300.0"},{"2:5","400.0"},{"负其他","150.0"}
				};
		
		int length = 0;
		ss_fb.bodys.clear();
		ArrayList<Data> datas =new ArrayList<Data>();

		length = h.length;
		for (int i = 0; i < length; i++) {
			//map.put(h[i][0], h[i][1]);
			datas.add(new Data(h[i][0], h[i][1]));
		}
		ss_fb.singleAward.add(datas);
		
		length = d.length;
		datas =new ArrayList<Data>();
		for (int i = 0; i < length; i++) {
			datas.add(new Data(d[i][0], d[i][1]));
		}
		ss_fb.singleAward.add(datas);

		
		length = a.length;
		datas =new ArrayList<Data>();
		for (int i = 0; i < length; i++) {
			datas.add(new Data(a[i][0],a[i][1]));
		}
		ss_fb.singleAward.add(datas);
		
		return ss_fb;
	}
	
	
	
	public static JSONBeanResultFB getMatchResultDetailFB(String mid) {
		String url = "http://info.sporttery.com/interface/m/?c=fb&a=get_pool_result&mid="
				+ mid + "&auth_type=uuid&auth_value=" + auth_value;
		String result = getHttpData(url);
		JSONBeanResultFB ms = null;
		if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = getHttpData(url);
		}
		if (result != null) {
			try {
				// System.out.println(result);
				JSONObject jsonObj = new JSONObject(result);
				JSONObject status = jsonObj.optJSONObject("status");
				String code = status.getString("code");
				// System.out.println("code=" + code);
				ms = new JSONBeanResultFB(code);
				JSONObject data = jsonObj.optJSONObject("data");
				JSONObject body = data.optJSONObject("body");
				if (body != null) {
					JSONObject hhad = body.optJSONObject("hhad");
					if (hhad != null) {
						ms.goaline = hhad.getString("goalline");
						Map<String, String> _map = new HashMap<String, String>();
						_map.put("c",
								StringUtils.getString(hhad.getString("c")));
						_map.put("value",
								StringUtils.getString(hhad.getString("value")));
						_map.put("m",
								StringUtils.getString(hhad.getString("m")));
						_map.put("count",
								StringUtils.getString(hhad.getString("count")));
						_map.put("c2",
								StringUtils.getString(hhad.getString("c2")));
						ms.wanfa.add(_map);
					}
					JSONObject crs = body.optJSONObject("crs");
					if (crs != null) {
						Map<String, String> _map = new HashMap<String, String>();
						_map.put("c", StringUtils.getString(crs.getString("c")));
						_map.put("value",
								StringUtils.getString(crs.getString("value")));
						_map.put("m", StringUtils.getString(crs.getString("m")));
						_map.put("count",
								StringUtils.getString(crs.getString("count")));
						_map.put("c2",
								StringUtils.getString(crs.getString("c2")));
						ms.wanfa.add(_map);
					}
					JSONObject ttg = body.optJSONObject("ttg");
					if (ttg != null) {
						Map<String, String> _map = new HashMap<String, String>();
						_map.put("c", StringUtils.getString(ttg.getString("c")));
						_map.put("value",
								StringUtils.getString(ttg.getString("value")));
						_map.put("m", StringUtils.getString(ttg.getString("m")));
						_map.put("count",
								StringUtils.getString(ttg.getString("count")));
						_map.put("c2",
								StringUtils.getString(ttg.getString("c2")));
						ms.wanfa.add(_map);
					}
					JSONObject hafu = body.optJSONObject("hafu");
					if (hafu != null) {
						Map<String, String> _map = new HashMap<String, String>();
						_map.put("c",
								StringUtils.getString(hafu.getString("c")));
						_map.put("value",
								StringUtils.getString(hafu.getString("value")));
						_map.put("m",
								StringUtils.getString(hafu.getString("m")));
						_map.put("count",
								StringUtils.getString(hafu.getString("count")));
						_map.put("c2",
								StringUtils.getString(hafu.getString("c2")));
						ms.wanfa.add(_map);
					}
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			
			 url = "http://info.sporttery.com/interface/m/?c=fb&a=get_fixed_odds&mid="+mid+"&auth_type=uuid&auth_value=0123456789012345&auth_type=uuid&auth_value=" + auth_value;
			 result = getHttpData(url);
			 if (result==null||"".equals(result)) {
					SystemClock.sleep(15);
					result = getHttpData(url);
				}
			if (result != null) {
				try
				{
					JSONObject jobj = new JSONObject(result);
					JSONObject dataObj = jobj.optJSONObject("data");
					if (dataObj != null) {
						JSONObject bodyObj = dataObj.optJSONObject("body");
						
						if (bodyObj != null) {
							int length = 0;
							Map<String, String> map = null;
							ArrayList<Map<String, String>> datas;
							Map<String, String> lastMap = null;
							JSONArray tempArray = null;
							JSONObject hdaObj = bodyObj.optJSONObject("had");
							if (hdaObj != null) {
								JSONArray valueArray = hdaObj.optJSONArray("values");
								if (valueArray != null) {
									length = valueArray.length();
									datas = ms.hdaPass;
									
									for (int i = 0; i < length; i++) {
										tempArray = valueArray.optJSONArray(i);
										map = new HashMap<String, String>();
										map.put("date",tempArray.optString(0));
										map.put("h", tempArray.optString(1));
										map.put("d", tempArray.optString(2));
										map.put("a", tempArray.optString(3));
										datas.add(map);
										com(map,lastMap,"h");
										com(map, lastMap, "d");
										com(map, lastMap, "a");
										
										lastMap = map;
									}
									lastMap = null;
								}
								
							}
							
							JSONObject ttgObj = bodyObj.optJSONObject("ttg");
							if (ttgObj != null) {
								JSONArray valueArray = ttgObj.optJSONArray("values");
								if (valueArray != null) {
									length = valueArray.length();
									datas = ms.totalPass;
									
									for (int i = 0; i < length; i++) {
										tempArray = valueArray.optJSONArray(i);
										map = new HashMap<String, String>();
										map.put("date", tempArray.optString(0));
										map.put("0",tempArray.optString(1));
										map.put("1",tempArray.optString(2));
										map.put("2",tempArray.optString(3));
										map.put("3",tempArray.optString(4));
										map.put("4",tempArray.optString(5));
										map.put("5",tempArray.optString(6));
										map.put("6",tempArray.optString(7));
										map.put("7+",tempArray.optString(8));
										com(map,lastMap,"0");
										com(map, lastMap, "1");
										com(map, lastMap, "2");
										com(map,lastMap,"3");
										com(map, lastMap, "4");
										com(map, lastMap, "5");
										com(map,lastMap,"6");
										com(map, lastMap, "7+");
										
										datas.add(map);
										lastMap = map;
									}
									
									/*map = new HashMap<String, String>();
									map.put("date", tempArray.optString(0));
									map.put("0","100");
									map.put("1","100");
									map.put("2","100");
									map.put("3","100");
									map.put("4","100");
									map.put("5","100");
									map.put("6","100");
									map.put("7+","100");
									com(map,lastMap,"0");
									com(map, lastMap, "1");
									com(map, lastMap, "2");
									com(map,lastMap,"3");
									com(map, lastMap, "4");
									com(map, lastMap, "5");
									com(map,lastMap,"6");
									com(map, lastMap, "7+");
									
									datas.add(map);
									lastMap = map;
									
									map = new HashMap<String, String>();
									map.put("date", tempArray.optString(0));
									map.put("0","1.0");
									map.put("1","1.0");
									map.put("2","1.0");
									map.put("3","1.0");
									map.put("4","1.0");
									map.put("5","1.0");
									map.put("6","1.0");
									map.put("7+","1.0");
									com(map,lastMap,"0");
									com(map, lastMap, "1");
									com(map, lastMap, "2");
									com(map,lastMap,"3");
									com(map, lastMap, "4");
									com(map, lastMap, "5");
									com(map,lastMap,"6");
									com(map, lastMap, "7+");
									
									datas.add(map);
									lastMap = map;
									*/
									
									lastMap = null;
								}
								
							}
							
							JSONObject hafuObj = bodyObj.optJSONObject("hafu");
							if (hafuObj != null) {
								JSONArray valueArray = hafuObj.optJSONArray("values");
								if (valueArray != null) {
									length = valueArray.length();
									datas = ms.hAPass;
									
									for (int i = 0; i < length; i++) {
										tempArray = valueArray.optJSONArray(i);
										map = new HashMap<String, String>();
										map.put("date", tempArray.optString(0));
										map.put("胜胜",tempArray.optString(1));
										map.put("胜平",tempArray.optString(2));
										map.put("胜负",tempArray.optString(3));
										map.put("平胜",tempArray.optString(4));
										map.put("平平",tempArray.optString(5));
										map.put("平负",tempArray.optString(6));
										map.put("负胜",tempArray.optString(7));
										map.put("负平",tempArray.optString(8));
										map.put("负负",tempArray.optString(9));	
										com(map,lastMap,"胜胜");
										com(map, lastMap, "胜平");
										com(map, lastMap, "胜负");
										com(map,lastMap,"平胜");
										com(map, lastMap, "平平");
										com(map, lastMap, "平负");
										com(map,lastMap,"负胜");
										com(map, lastMap, "负平");
										com(map, lastMap, "负负");
										datas.add(map);
										lastMap =map;
									}
									/*map = new HashMap<String, String>();
									map.put("date", tempArray.optString(0));
									map.put("胜胜","100");
									map.put("胜平","100");
									map.put("胜负","100");
									map.put("平胜","100");
									map.put("平平","100");
									map.put("平负","100");
									map.put("负胜","100");
									map.put("负平","100");
									map.put("负负","100");	
									com(map,lastMap,"胜胜");
									com(map, lastMap, "胜平");
									com(map, lastMap, "胜负");
									com(map,lastMap,"平胜");
									com(map, lastMap, "平平");
									com(map, lastMap, "平负");
									com(map,lastMap,"负胜");
									com(map, lastMap, "负平");
									com(map, lastMap, "负负");
									datas.add(map);
									lastMap = map;
									
									map = new HashMap<String, String>();
									map.put("date", tempArray.optString(0));
									map.put("胜胜","1.0");
									map.put("胜平","1.0");
									map.put("胜负","1.0");
									map.put("平胜","1.0");
									map.put("平平","1.0");
									map.put("平负","1.0");
									map.put("负胜","1.0");
									map.put("负平","1.0");
									map.put("负负","1.0");	
									com(map,lastMap,"胜胜");
									com(map, lastMap, "胜平");
									com(map, lastMap, "胜负");
									com(map,lastMap,"平胜");
									com(map, lastMap, "平平");
									com(map, lastMap, "平负");
									com(map,lastMap,"负胜");
									com(map, lastMap, "负平");
									com(map, lastMap, "负负");
									datas.add(map);*/
									lastMap = null;
								}
								
							}
							
							
							
							JSONObject crsObj = bodyObj.optJSONObject("crs");
							if (crsObj != null) {
								JSONArray valueArray = crsObj.optJSONArray("values");
								JSONArray keyArray = crsObj.optJSONArray("key");
								 ArrayList<String> keys = ms.scoreKeys;
								if (valueArray != null && keyArray != null) {
									length = keyArray.length();
									for (int i = 0; i < length; i++) {
										keys.add(keyArray.optString(i));
									}
									length = valueArray.length();
									
									ArrayList<Map<String, String>> keyList = ms.scorePass;
									int size = 0;						
									
									for (int i = 0; i < length; i++) {
										map = new HashMap<String, String>();
										tempArray = valueArray.optJSONArray(i);
										size = keyArray.length();
										for (int j = 0; j < size; j++) {
											map.put(keys.get(j), tempArray.optString(j));
											com(map, lastMap, keys.get(j));
										}										
										keyList.add(map);
										lastMap = map;
									}
									
									
									
									
									lastMap = null;
									

									
								}
								
							}
						}
					}
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
		
		}
		return ms;
	}

	private static float com(Map<String, String> map,Map<String, String> lastMap,String key)
	{
		float com =0 ;
		if (lastMap == null||map == null) {
			
		}else
		{
			float m = 0;
			float last = 0;
			try {
				m = new Float(map.get(key));
				last = new Float(lastMap.get(key));
				com = m - last;
			} catch (Exception e) {
				com = 0;
			}
		}	
		
		map.put(key+"1", com+"");
		return com;
	}
	
	public static JSONBean getLeagueListBK() {
		String url = "http://info.sporttery.com/interface/m/?c=bk&a=get_nba_list&auth_type=uuid&auth_value="
				+ auth_value;
		String result = getHttpData(url);
		JSONBean ms = null;
		if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = getHttpData(url);
		}
		if (result != null) {
			try {
				// System.out.println(result);
				JSONObject jsonObj = new JSONObject(result);
				JSONObject status = jsonObj.optJSONObject("status");
				String code = status.getString("code");
				// System.out.println("code=" + code);
				ms = new JSONBean(code);
				JSONObject data = jsonObj.optJSONObject("data");
				JSONArray body = data.optJSONArray("body");
				if (body != null) {
					System.out.println("body length = " + body.length());
					for (int i = 0; i < body.length(); i++) {
						JSONObject obj = body.optJSONObject(i);
						Map<String, String> map = new HashMap<String, String>();
						map.put("t_id", obj.getString("t_id"));
						map.put("t_cn", obj.getString("t_cn"));
						ms.bodys.add(map);
						
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ms;
	}

	/**
	 * 获得篮球赛果开奖
	 * 
	 * @param sdate
	 *            开始日期
	 * @param edate
	 *            结束日期
	 * @param tid
	 *            NBA球队ID
	 * @return
	 */
	public static JSONBean getMatchResultBK(String sdate, String edate,
			String tid) {
		String url = "http://info.sporttery.com/interface/m/?c=bk&a=get_match_result&sdate="
				+ sdate
				+ "&edate="
				+ edate
				+ "&tid="
				+ tid
				+ "&page=1&num=9999&auth_type=uuid&auth_value=" + auth_value;
		String result = getHttpData(url);
		JSONBean ms = null;
		if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = getHttpData(url);
		}
		if (result != null) {
			try {
				// System.out.println(result);
				JSONObject jsonObj = new JSONObject(result);
				JSONObject status = jsonObj.optJSONObject("status");
				String code = status.getString("code");
				// System.out.println("code=" + code);
				ms = new JSONBean(code);
				JSONObject data = jsonObj.optJSONObject("data");
				JSONArray body = data.optJSONArray("body");
				if (body != null) {
					// System.out.println("body length = " + body.length());
					for (int i = 0; i < body.length(); i++) {
						JSONObject obj = body.optJSONObject(i);
						Map<String, String> map = new HashMap<String, String>();
						map.put("date", obj.getString("date"));
						map.put("num", obj.getString("num"));
						map.put("m_id", obj.getString("m_id"));
						map.put("l_cn", obj.getString("l_cn"));
						map.put("h_cn", obj.getString("h_cn"));
						map.put("a_cn", obj.getString("a_cn"));
						map.put("final",
								obj.getString("final").length() <= 1 ? "- : -"
										: obj.getString("final"));
						map.put("status", obj.getString("status"));
						map.put("s1",
								obj.getString("s1").length() <= 1 ? "- : -"
										: obj.getString("s1"));
						map.put("s2",
								obj.getString("s2").length() <= 1 ? "- : -"
										: obj.getString("s2"));
						map.put("s3",
								obj.getString("s3").length() <= 1 ? "- : -"
										: obj.getString("s3"));
						map.put("s4",
								obj.getString("s4").length() <= 1 ? "- : -"
										: obj.getString("s4"));
						map.put("goalline", "(+2)");
						ms.bodys.add(map);
						
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ms;
	}

	public static JSONBeanResultBK getMatchResultDetailBK(String mid) {
		String url = "http://info.sporttery.com/interface/m/?c=bk&a=get_pool_result&mid="
				+ mid + "&auth_type=uuid&auth_value=" + auth_value;
		String result = getHttpData(url);
		JSONBeanResultBK ms = null;
		if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = getHttpData(url);
		}
		if (result != null) {
			try {
				// System.out.println(result);
				JSONObject jsonObj = new JSONObject(result);
				JSONObject status = jsonObj.optJSONObject("status");
				String code = status.getString("code");
				// System.out.println("code=" + code);
				ms = new JSONBeanResultBK(code);
				JSONObject data = jsonObj.optJSONObject("data");
				JSONObject body = data.optJSONObject("body");
				if (body != null) {
					JSONObject mnl = body.optJSONObject("mnl");
					if (mnl != null) {
						ms.mnl_c = StringUtils.getString(mnl.getString("c"));
						ms.mnl_value = StringUtils.getString(mnl
								.getString("value"));
						ms.mnl_m = StringUtils.getString(mnl.getString("m"));
						ms.mnl_count = StringUtils.getString(mnl
								.getString("count"));
						ms.mnl_c2 = StringUtils.getString(mnl.getString("c2"));
					}
					JSONObject hdc = body.optJSONObject("hdc");
					if (hdc != null) {
						ms.hdc_goalline = hdc.getString("goalline");
						ms.hdc_c = StringUtils.getString(hdc.getString("c"));
						ms.hdc_value = StringUtils.getString(hdc
								.getString("value"));
						ms.hdc_m = StringUtils.getString(hdc.getString("m"));
						ms.hdc_count = StringUtils.getString(hdc
								.getString("count"));
						JSONArray array = hdc.getJSONArray("c2");
						if (array.length() == 0) {
							for (int i = 0; i < 2; i++) {
								ms.hdc_c2_list.add(StringUtils.getString(""));
							}
						} else {
							for (int i = 0; i < array.length(); i++) {
								ms.hdc_c2_list.add(StringUtils.getString(array
										.getString(i)));
							}
						}

					}
					JSONObject wnm = body.optJSONObject("wnm");
					if (wnm != null) {
						ms.wnm_c = StringUtils.getString(wnm.getString("c"));
						ms.wnm_value = StringUtils.getString(wnm
								.getString("value"));
						ms.wnm_m = StringUtils.getString(wnm.getString("m"));
						ms.wnm_count = StringUtils.getString(wnm
								.getString("count"));
						ms.wnm_c2 = StringUtils.getString(wnm.getString("c2"));
					}
					JSONObject hilo = body.optJSONObject("hilo");
					if (hilo != null) {
						ms.hilo_goalline = hilo.getString("goalline");
						ms.hilo_c = StringUtils.getString(hilo.getString("c"));
						ms.hilo_value = StringUtils.getString(hilo
								.getString("value"));
						ms.hilo_m = StringUtils.getString(hilo.getString("m"));
						ms.hilo_count = StringUtils.getString(hilo
								.getString("count"));
						JSONArray array = hilo.getJSONArray("c2");
						if (array.length() == 0) {
							for (int i = 0; i < 3; i++) {
								ms.hilo_c2_list.add(StringUtils.getString(""));
							}
						} else {
							for (int i = 0; i < array.length(); i++) {
								ms.hilo_c2_list.add(StringUtils.getString(array
										.getString(i)));
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ms;
	}
}
