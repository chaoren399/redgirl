package cn.chaoren.net;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.SystemClock;
import android.util.Log;

import cn.chaoren.action.StatusAction;
import cn.chaoren.action.StringUtil;
import cn.chaoren.net.KeyMapBean.KeyMap;
import cn.chaoren.obj.Information;



  //比分直播向服务器请求方法

public class HttpServiceNew {
    public HttpServiceNew() {
		// TODO Auto-generated constructor stub
	}
    
    private  String auth_value = "0123456789012345";
    
    public String[] addfirst(String all,int length,HashMap<String, String> map)
    {
    	String[] array = new String[length+1];
    	array[0] = all;
    	map.put(all, "");
    	return array;
    }
    
   /* public KeyValuesBean getMatchResultDetail(String mid)
    {
    	
    }*/
    
    public ArrayList<Information> getInformationList(String id)//last max id // num  //page
    {
    	if (id == null || "".equals(id)) {
			id = "";
		}else {
			id = "&stid = " +id;
		}
    	//String uri = "http://info.sporttery.com/interface/m/?c=app&a=get_mesg"+id+"&auth_type=uuid&auth_value=0123456789012345";
    	
    	String uri = "http://info.sporttery.com/interface/m/?c=add&a=get_msg_list"+id+"&auth_type=uuid&auth_value=0123456789012345";
    	
    	String result = HttpServices.getHttpData(uri);//调用HttpServices里面的方法与服务器交互
    	ArrayList<Information> infoList = null;
    	if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = HttpServices.getHttpData(uri);
		}
    	/**
    	 * 如果服务器返回来的数据不为空，定义方法对返回来的json数据解析
    	 * 最后返回的是infoList
    	 * 
    	 */
    	if (result != null) {
			try {
				JSONObject jobj = new JSONObject(result);
				if (jobj != null) {
					JSONObject dataObj = jobj.optJSONObject("data");
					if (dataObj != null) {
						JSONArray bodyArr = dataObj.optJSONArray("body");
						if (bodyArr != null) {
							infoList = new ArrayList<Information>();
							Information info = null;
							int length = bodyArr.length();
							JSONObject temp = null;
							for (int i = 0; i < length; i++) {
								info = new Information();
							    temp = bodyArr.optJSONObject(i);
							    info.date = temp.optString("sdt");
							    info.guid = temp.optString("id");
							    info.title = temp.optString("t");
							    info.prefile = temp.optString("con");
							    info.edt = temp.optString("edt");
							    infoList.add(info);
							}
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
    	return infoList;
    }
    
    
    public KeyMapBean getSelectionBB(String all)
    {
    	KeyMapBean ms = new KeyMapBean("0");
    	getSearchHDASelectionsBB(all, null, null, ms.keyList);
    	KeyMap key = getSearchLListSelectionsBB(all, null);
    	ms.keyList.put("llist", key);
    	ms.keyList.put("hteam",new KeyMap());
    	ms.keyList.put("ateam",new KeyMap());
    	return ms;
    }
    
    @SuppressWarnings("unchecked")
  	public KeyMap  getSearchLListSelectionsBB(String all,String lid)
      {
      	
      	if (lid != null && !"".equals(lid)) {
  			lid = "&lid="+lid;
  		}else
  		{
  			lid = "";
  		}      
      	String url = "http://info.sporttery.com/interface/m/?c=bk&a=get_team_list"+lid+"&auth_type=uuid&auth_value=0123456789012345";
      	String result = HttpServices.getHttpData(url);
      	Log.d("HttpServiceNew#getSearchHDASelections", "url#result:"+ url+"#"+ result);
      	String[] listArray = null;
      	KeyMap key = null;
      	if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = HttpServices.getHttpData(url);
		}
      	int length = 0;
      	try {    		
  			JSONObject jsonObj = new JSONObject(result);
  			if (jsonObj != null) {
  				JSONObject dataObj = jsonObj.optJSONObject("data");
  				if (dataObj != null) {
  					JSONObject bodyObj = dataObj.optJSONObject("body");
  					if (bodyObj != null) {
  						
  						JSONArray listArr = bodyObj.optJSONArray("llist");
  						if (listArr == null) {
  							listArr = bodyObj.optJSONArray("teamlist");
  						}
  						
  						
  						if (listArr != null) {
  							 length = listArr.length();
  							key = new KeyMap();
  							
  							listArray = addfirst(all, length, key.map);
  							
  							 String str = null;
  								String value = null;
  								JSONObject tempObj = null;
  							for (int j = 0; j < length; j++) {
  								tempObj = listArr.getJSONObject(j);
  								str = iterator(tempObj.keys());
  								value = tempObj.getString(str);
  								listArray[j+1] = value;								
  								key.put(value, str);
  							}
  							
  							
  							 
  							key.keys = listArray;
  							 
  							 
  						}
  					
  					}
  				}
  				
  			
  			}
  		} catch (Exception e) {
  			System.out.println("e:...." + result);
  			e.printStackTrace();
  		}
      	if (key == null) {
  			key = new KeyMap();
  			key.initDefOne(all);
  		}
      	return key;
      }
      
    
    
    public  HeaderJSONBean getFeedBackBB(String hid,String aid,String lid,String type,String ho,String ao,String count)
	{
		
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
		
		if (type != null &&  !"".equals(type)) {
			sb.append("&type="+type);
		}
		if (ho != null &&  !"".equals(ho)) {
			sb.append("&ho="+ho);
		}
	
		if (ao != null &&  !"".equals(ao)) {
			sb.append("&ao="+ao);
		}
		if (count != null &&  !"".equals(count)) {
			sb.append("&count="+count);
		}
		
		String url = "http://info.sporttery.com/interface/m/?c=bk&a=get_sodds_mnl_result"+sb.toString()+"&auth_type=uuid&auth_value=0123456789012345";
		String result = HttpServices.getHttpData(url);
		if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = HttpServices.getHttpData(url);
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
						
							Map<String, String> header = ms.header;
							JSONObject staObj = headObj.optJSONObject("sta");//sta
							if (staObj != null) {							
								
								header.put("h", staObj.optString("win","0"));//win
								header.put("a", staObj.optString("lose","0"));//lose
								StringUtil util = new StringUtil();
								
								header.put("total", ""+(util.optInt(header.get("h"), 0)+util.optInt(header.get("a"), 0)));//
							     System.out.println("h-" + header.get("h"));
							     System.out.println("a-" + header.get("a"));
							     System.out.println("total-" + header.get("total"));
							}
							
							/*JSONObject typeObj = dictObj.optJSONObject("type");
							if (typeObj != null) {
								Iterator<String> iterator = typeObj.keys();
								
								while (iterator.hasNext()) {
									temp = iterator.next();
									header.put(temp, typeObj.optString(temp));
								}
							}*/
						
						
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
							
							/*map.put("date", "2012-01-12 08:45:25");
							map.put("home", "杜伊斯堡");
							map.put("away", "杜赛尔多夫");
							map.put("handicap", "+1");
							map.put("result", "0:1");
							map.put("legual", "德乙");
							map.put("status", "变化中奖金");
							map.put("homeWin", "2.05");
							map.put("homeLose", "3.8");*/
							
							map.put("date", obj.optString("date"));//date
							map.put("home", obj.optString("h_cn"));//h_cn
							map.put("away", obj.optString("a_cn"));//a_cn
							
							//map.put("handicap", obj.optString("goalline"));//goalline
							map.put("homeWin", obj.optString("ho"));//ho
							//map.put("d", obj.optString("do"));//do
							map.put("homeLose", obj.optString("ao"));//ao
							map.put("result",obj.optString("final"));//final
							map.put("legual", obj.optString("l_cn"));//l_cn
							//map.put("type", obj.optString("type"));//type 类型
							//map.put("status", ms.header.get(obj.opt("type")));
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
	
    
    
    
    public HashMap<String, KeyMap> getSearchHDASelectionsBB(String all,String h,String a,HashMap<String, KeyMap> dataList)
    {
    	StringBuilder sb = new StringBuilder();
    	if (h != null && !"".equals(h)) {
			sb.append("&ho="+h);
		}
    	
    	if (a != null && !"".equals(a)) {
			sb.append("&ao="+a);
		}
    	//String url = "http://info.sporttery.com/interface/m/?c=fb&a=get_sodds_had"+sb.toString()+"&auth_type=uuid&auth_value=0123456789012345";
    	String url =" http://info.sporttery.com/interface/m/?c=bk&a=get_sodds_mnl"+sb.toString()+"&auth_type=uuid&auth_value=0123456789012345";
    	String result = HttpServices.getHttpData(url);
    	Log.d("HttpServiceNew#getSearchHDASelections", "url#result:"+ url+"#"+ result);
    	if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = HttpServices.getHttpData(url);
		}
    	KeyMap key;
    	String[] tempArray;
    	int length = 0;
    	try {
    		
			JSONObject jsonObject = new JSONObject(result);
			if (jsonObject != null) {
				JSONObject dataObj = jsonObject.optJSONObject("data");
				if (dataObj!= null) {
					JSONObject bodyObj = dataObj.optJSONObject("body");
					if (bodyObj != null) {
						JSONArray hlistObj = bodyObj.optJSONArray("hlist");
						if (hlistObj != null) {
							key = new KeyMap();
							length = hlistObj.length();
							tempArray = addfirst(all, length,key.map);
							for (int i = 0; i < length; i++) {
								tempArray[i+1] = hlistObj.optString(i, "");
								key.put(tempArray[i+1], tempArray[i+1]);
							}
							key.keys = tempArray;
							dataList.put("hlist",key);
						}else
						{
							key = new KeyMap();
							key.keys = addfirst(all, 0, key.map);
							dataList.put("hlist",key);
						}
						
						
						JSONArray alistObj = bodyObj.optJSONArray("alist");
						if (alistObj != null) {
							key = new KeyMap();
							length = alistObj.length();
							tempArray = addfirst(all, length,key.map);
							for (int i = 0; i < length; i++) {
								tempArray[i+1] = alistObj.optString(i, "");
								key.put(tempArray[i+1], tempArray[i+1]);
							}
							key.keys = tempArray;
							dataList.put("alist",key);
						}else
						{
							key = new KeyMap();
							key.keys = addfirst(all, 0, key.map);
							dataList.put("alist",key);
						}
					}
				}
				
			}
			
			
		} catch (Exception e) {
			System.out.println("e...........:" +result);
			e.printStackTrace();
		}
    	if (dataList.size()==0) {
    		key = new KeyMap();
			key.keys = addfirst(all, 0, key.map);
			dataList.put("hlist",key);
			
			key = new KeyMap();
			key.keys = addfirst(all, 0, key.map);
			dataList.put("alist",key);
		}
    	return dataList;
    }
    
    
    //search 
    public HashMap<String, KeyMap> getSearchHDASelections(String all,String h,String d,String a,HashMap<String, KeyMap> dataList)
    {
    	StringBuilder sb = new StringBuilder();
    	if (h != null && !"".equals(h)) {
			sb.append("&ho="+h);
		}
    	if (d != null && !"".equals(d)) {
			sb.append("&do="+d);
		}
    	if (a != null && !"".equals(a)) {
			sb.append("&ao="+a);
		}
    	String url = "http://info.sporttery.com/interface/m/?c=fb&a=get_sodds_had"+sb.toString()+"&auth_type=uuid&auth_value=0123456789012345";
    	String result = HttpServices.getHttpData(url);
    	Log.d("HttpServiceNew#getSearchHDASelections", "url#result:"+ url+"#"+ result);
    	if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = HttpServices.getHttpData(url);
		}
    	KeyMap key;
    	String[] tempArray;
    	int length = 0;
    	try {
    		
			JSONObject jsonObject = new JSONObject(result);
			if (jsonObject != null) {
				JSONObject dataObj = jsonObject.optJSONObject("data");
				if (dataObj!= null) {
					JSONObject bodyObj = dataObj.optJSONObject("body");
					if (bodyObj != null) {
						JSONArray hlistObj = bodyObj.optJSONArray("hlist");
						if (hlistObj != null) {
							key = new KeyMap();
							length = hlistObj.length();
							tempArray = addfirst(all, length,key.map);
							for (int i = 0; i < length; i++) {
								tempArray[i+1] = hlistObj.optString(i, "");
								key.put(tempArray[i+1], tempArray[i+1]);
							}
							key.keys = tempArray;
							dataList.put("hlist",key);
						}else
						{
							key = new KeyMap();
							key.keys = addfirst(all, 0, key.map);
							dataList.put("hlist",key);
						}
						
						JSONArray dlistObj = bodyObj.optJSONArray("dlist");
						if (dlistObj != null) {
							key = new KeyMap();
							length = dlistObj.length();
							tempArray = addfirst(all, length,key.map);
							for (int i = 0; i < length; i++) {
								tempArray[i+1] = dlistObj.optString(i, "");
								key.put(tempArray[i+1], tempArray[i+1]);
							}
							key.keys = tempArray;
							dataList.put("dlist",key);
						}else
						{
							key = new KeyMap();
							key.keys = addfirst(all, 0, key.map);
							dataList.put("dlist",key);
						}
						
						JSONArray alistObj = bodyObj.optJSONArray("alist");
						if (alistObj != null) {
							key = new KeyMap();
							length = alistObj.length();
							tempArray = addfirst(all, length,key.map);
							for (int i = 0; i < length; i++) {
								tempArray[i+1] = alistObj.optString(i, "");
								key.put(tempArray[i+1], tempArray[i+1]);
							}
							key.keys = tempArray;
							dataList.put("alist",key);
						}else
						{
							key = new KeyMap();
							key.keys = addfirst(all, 0, key.map);
							dataList.put("alist",key);
						}
					}
				}
				
			}
			
			
		} catch (Exception e) {
			System.out.println("e...........:" +result);
			e.printStackTrace();
		}
    	if (dataList.size()==0) {
    		key = new KeyMap();
			key.keys = addfirst(all, 0, key.map);
			dataList.put("hlist",key);
			key = new KeyMap();
			key.keys = addfirst(all, 0, key.map);
			dataList.put("dlist",key);
			key = new KeyMap();
			key.keys = addfirst(all, 0, key.map);
			dataList.put("alist",key);
		}
    	return dataList;
    }
    
    private String iterator(Iterator<String> iterator)
    {
    	if (iterator.hasNext()) {
			return iterator.next();
		}
    	return "";
    }
    
    @SuppressWarnings("unchecked")
	public KeyMap getSearchGoallineSelections()
    {
    	String url = "http://info.sporttery.com/interface/m/?c=fb&a=get_sodds_had_goalline&auth_type=uuid&auth_value=0123456789012345";
    	String result = HttpServices.getHttpData(url);
    	Log.d("HttpServiceNew#getSearchHDASelections", "url#result:"+ url+"#"+ result);
    	if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = HttpServices.getHttpData(url);
		}
    	KeyMap key = null;
    	String[] goalineArray = null;
    	int length = 0;
    	try {
			JSONObject jsonObj = new JSONObject(result);
			if (jsonObj != null) {
				JSONObject dataObj = jsonObj.optJSONObject("data");
				if (dataObj != null) {
					JSONObject bodyObj = dataObj.optJSONObject("body");
					if (bodyObj != null) {
						JSONArray goalineArr = bodyObj.optJSONArray("goalline");
						
						if (goalineArr != null) {
							 length = goalineArr.length();
							key = new KeyMap();
							
							 goalineArray = new String[length];
							
							 String str = null;
								String value = null;
								JSONObject tempObj = null;
							for (int j = 0; j < length; j++) {
								tempObj = goalineArr.getJSONObject(j);
								str = iterator(tempObj.keys());
								value = tempObj.getString(str);
								goalineArray[j] = value;								
								key.put(value, str);
							}
							
							/* while (iterator.hasNext()) {
								str = iterator.next();
								value = goalineObj.optString(str);
								goalineArray[i] = value;								
								key.put(value, str);
								i++;								
							}*/
							 
							key.keys = goalineArray;
							 
							 
						}
					}
				}
				
			
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if (key == null) {
			key = new KeyMap();
			
		}
    	return key;
    }
    
    
    @SuppressWarnings("unchecked")
	public KeyMap  getSearchLListSelections(String all,String lid)
    {
    	
    	if (lid != null && !"".equals(lid)) {
			lid = "&lid="+lid;
		}else
		{
			lid = "";
		}
    	String url = "http://info.sporttery.com/interface/m/?c=fb&a=get_team_list"+lid+"&auth_type=uuid&auth_value=0123456789012345";
    	String result = HttpServices.getHttpData(url);
    	if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = HttpServices.getHttpData(url);
		}
    	Log.d("HttpServiceNew#getSearchHDASelections", "url#result:"+ url+"#"+ result);
    	String[] listArray = null;
    	KeyMap key = null;
    	int length = 0;
    	try {    		
			JSONObject jsonObj = new JSONObject(result);
			if (jsonObj != null) {
				JSONObject dataObj = jsonObj.optJSONObject("data");
				if (dataObj != null) {
					JSONObject bodyObj = dataObj.optJSONObject("body");
					if (bodyObj != null) {
						
						JSONArray listArr = bodyObj.optJSONArray("llist");
						if (listArr == null) {
							listArr = bodyObj.optJSONArray("teamlist");
						}
						
						
						if (listArr != null) {
							 length = listArr.length();
							key = new KeyMap();
							
							listArray = addfirst(all, length, key.map);
							
							 String str = null;
								String value = null;
								JSONObject tempObj = null;
							for (int j = 0; j < length; j++) {
								tempObj = listArr.getJSONObject(j);
								str = iterator(tempObj.keys());
								value = tempObj.getString(str);
								listArray[j+1] = value;								
								key.put(value, str);
							}
							
							
							 
							key.keys = listArray;
							 
							 
						}
					
					}
				}
				
			
			}
		} catch (Exception e) {
			System.out.println("e:...." + result);
			e.printStackTrace();
		}
    	if (key == null) {
			key = new KeyMap();
			key.initDefOne(all);
		}
    	return key;
    }
    
    
    
    public HeaderJSONBean refreshBasketOnLiveList()
    {
    	String uri = "http://info.sporttery.com/interface/m/?c=bbls&a=get_change_list&step=5&auth_type=uuid&auth_value="
				+ auth_value;
    	String result = HttpServices.getHttpData(uri);
    	System.out.println("url.." + uri);
    	System.out.println("result:" + result);
    	if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = HttpServices.getHttpData(uri);
		}
    	HeaderJSONBean ms = null;
    	if (result != null) {
    		try {
    			
				JSONObject json = new JSONObject(result);
				
				if (json != null) {
					JSONObject statusObj = json.optJSONObject("status");
					if (statusObj != null) {
						ms = new HeaderJSONBean(statusObj.optString("code"));
					}
					
					JSONObject dataObj = json.optJSONObject("data");
					if (dataObj != null) {
						JSONObject headObj = dataObj.optJSONObject("head");
						if (headObj != null) {
							HashMap<String, String> header = ms.header;
							header.put("state", headObj.optString("state")+"");
						}
						
						JSONArray bodyObj = json.optJSONArray("body");
					 	List<Map<String, String>> dataList = ms.bodys;
					 	if (bodyObj != null) {
							int length = bodyObj.length();
							JSONObject tempObj = null;
							Map<String, String> map = null;
							for (int i = 0; i < length; i++) {
								tempObj = bodyObj.getJSONObject(i);
								map = new HashMap<String, String>();
								map.put("m_id", tempObj.optString("mid",""));
								map.put("status", tempObj.optString("status",""));
								
								map.put("lsc", tempObj.optString("lsc",""));
								dataList.add(map);
							}
						}
					}
				}
			} catch (Exception e) {
				System.out.println("e:" + result);
				e.printStackTrace();
			}
    	}
    	return ms;
    }
    
    public HeaderJSONBean refreshSoccerOnLiveList()
    {
    	String uri = "http://info.sporttery.com/interface/m/?c=fbls&a=get_change_list&step=5&auth_type=uuid&auth_value="
				+ auth_value;
    	String result = HttpServices.getHttpData(uri);
    	System.out.println("url.." + uri);
    	System.out.println("result:" + result);
    	if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = HttpServices.getHttpData(uri);
		}
    	HeaderJSONBean ms = null;//ms继承了JSONBean
    	if (result != null) {
    		try {
    			
				JSONObject json = new JSONObject(result);
				
				if (json != null) {
					JSONObject statusObj = json.optJSONObject("status");
					if (statusObj != null) {
						ms = new HeaderJSONBean(statusObj.optString("code"));
					}
					
					JSONObject dataObj = json.optJSONObject("data");
					if (dataObj != null) {
						JSONObject headObj = dataObj.optJSONObject("head");
						if (headObj != null) {
							HashMap<String, String> header = ms.header;//从ms中调用header方法
							header.put("state", headObj.optString("state")+"");
						}
						
						JSONArray bodyObj = json.optJSONArray("body");
					 	List<Map<String, String>> dataList = ms.bodys;
					 	if (bodyObj != null) {
							int length = bodyObj.length();
							JSONObject tempObj = null;
							Map<String, String> map = null;
							for (int i = 0; i < length; i++) {
								tempObj = bodyObj.getJSONObject(i);
								map = new HashMap<String, String>();
								map.put("m_id", tempObj.optString("mid",""));
								map.put("half", tempObj.optString("half",""));
								map.put("audience", tempObj.optString("full",""));
								map.put("lsc", tempObj.optString("lsc",""));
								dataList.add(map);
							}
						}
					}
				}
			} catch (Exception e) {
				System.out.println("e:" + result);
				e.printStackTrace();
			}
    	}
    	return ms;
    }
    
    
    public HeaderJSONBean getSoccerOnLiveDetail(String mid)
    {
    	String url = "http://info.sporttery.com/interface/m/?c=fbls&a=get_event_list&mid="+mid+"&"+"auth_type=uuid&auth_value="
				+ auth_value;
    	String result = HttpServices.getHttpData(url);
    	System.out.println("url.." + url);
    	System.out.println("result:" + result);
    	if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = HttpServices.getHttpData(url);
		}
    	HeaderJSONBean ms = null;
    	if (result != null) {
			try {
				JSONObject json = new JSONObject(result);
				JSONObject statusObject = json.optJSONObject("status");
				if (statusObject != null) {
					ms = new HeaderJSONBean(statusObject.optString("code"));
					List<Map<String, String>> bodys = ms.bodys;
					Map<String, String> map = null;
					JSONObject dataObject = json.optJSONObject("data");
					if (dataObject != null) {
						JSONObject headObj = dataObject.optJSONObject("head");
						if (headObj != null) {
							HashMap<String, String> header = ms.header;
							header.put("half", headObj.optString("half",""));
							header.put("full", headObj.optString("full",""));
							header.put("status", headObj.optString("status",""));
							header.put("rspdt", headObj.optString("rspdt",""));
							header.put("lsc", headObj.optString("lsc","-"));
							header.put("date", headObj.optString("t1",""));
							header.put("dt2", headObj.optString("dt2",""));
							header.put("startDate", new Date().getTime()+"");
							String startTime = header.get("dt2");
							if (startTime == null || "".equals(startTime.trim())) {
								header.put("nowDiff", ""+(StatusAction.parse(header.get("rspdt"), "yyyy-MM-dd hh:mm:ss").getTime()-StatusAction.parse(header.get("date"), "hh:mm").getTime()));
							}else
							{
								header.put("nowDiff", ""+(StatusAction.parse(header.get("rspdt"), "yyyy-MM-dd hh:mm:ss").getTime()-StatusAction.parse(header.get("dt2"), "yyyy-MM-dd hh:mm").getTime()));
							}
							
						}
						/*JSONObject bodyObject = dataObject.optJSONObject("body");*/
						JSONArray bodyArray = dataObject.optJSONArray("body");
						if (bodyArray != null) {
							int length = bodyArray.length();
							JSONObject data = null;
							for (int i = 0; i < length; i++) {
								data = bodyArray.getJSONObject(i);
								map = new HashMap<String, String>();
								map.put("eid", data.optString("eid",""));
								map.put("t", data.optString("t",""));
								map.put("ho", data.optString("ho",""));
								map.put("ao", data.optString("ao",""));
								bodys.add(map);
							}
						}
					}
					
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
    	return ms;
    }
    
    
    public HeaderJSONBean getBasketOnLiveList(String lid,String status ,String page,String num)
    {
    	StringBuilder sb = new StringBuilder();
    	sb.append("");
    	if (status == null||status.equals("")) {
			status="1";
		}
    	sb.append("tabstatus="+status+"&");
    	if (page!=null&&!page.equals("")) {
			sb.append("page="+page+"&");
		}else
		{
			sb.append("page=1&");
		}
    	if (num!=null&&!num.equals("")) {
    		sb.append("num="+num+"&");
		}
    	else
    	{
    		sb.append("num=9999&");
    	}
    	//http://info.sporttery.com/interface/m/?c=fbls&a=get_list&auth_type=uuid&auth_value=0123456789012345&tabstatus=2&page=1&num=10
    	String url = "http://info.sporttery.com/interface/m/?c=bbls&a=get_list&lid="+lid+"&"+sb.toString()+"auth_type=uuid&auth_value="
				+ auth_value;
    	HeaderJSONBean ms = null;
    	
    	String result = HttpServices.getHttpData(url);
    	if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = HttpServices.getHttpData(url);
		}
    	if (result != null) {
			try {
				JSONObject json = new JSONObject(result);
				JSONObject statusObject = json.optJSONObject("status");
				if (statusObject!=null) {
					String code = statusObject.getString("code");
					ms = new HeaderJSONBean(code);
					if ("0".equals(code)) {
						JSONObject dataObject = json.optJSONObject("data");
						if (dataObject != null) {
							JSONObject headObj = dataObject.optJSONObject("head");
							HashMap<String, String> map = null;
								if (headObj!=null) {
									map = ms.header;
									map.put("total",  headObj.optInt("total")+"");
									map.put("rspdt", headObj.optString("rspdt"));
									System.out.println();
								}
																		
							
							
							JSONArray bodyArray = dataObject.optJSONArray("body");
						
							if (bodyArray != null) {
								initScoreOnLive(ms,bodyArray,map.get("rspdt"),status);
								
							}
						}
						
					}
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	return ms;
    }
    
    //指定联赛，比赛状态，（当前第几页，每页个数，两者为可先参数）
    public HeaderJSONBean getScoreOnLiveList(String lid,String status ,String page,String num)
    {
    	StringBuilder sb = new StringBuilder();
    	sb.append("");
    	if (status == null||status.equals("")) {
			status="1";
		}
    	sb.append("tabstatus="+status+"&");
    	if (page!=null&&!page.equals("")) {
			sb.append("page="+page+"&");
		}else
		{
			sb.append("page=1&");
		}
    	if (num!=null&&!num.equals("")) {
    		sb.append("num="+num+"&");
		}
    	else
    	{
    		sb.append("num=9999&");
    	}
    	//lid=&tabstatus=1&page=1&num=9999
    	//http://info.sporttery.com/interface/m/?c=fbls&a=get_list&auth_type=uuid&auth_value=0123456789012345&tabstatus=2&page=1&num=10
    	String url = "http://info.sporttery.com/interface/m/?c=fbls&a=get_list&lid="
    	              +lid
    	              +"&"
    			      +sb.toString()
    			      +"auth_type=uuid&auth_value="+ auth_value;
   //http://info.sporttery.com/interface/m/?c=fbls&a=get_list&lid=&tabstatus=1&page=1&num=9999&auth_type=uuid&auth_value=0123456789012345
    	/**赛果开奖路径
    	 * String url = "http://info.sporttery.com/interface/m/?c=fb&a=get_match_result&sdate="
				+ sdate
				+ "&edate="
				+ edate
				+ "&lid="
				+ lid
				+ "&page=1&num=9999&auth_type=uuid&auth_value=" + auth_value;
http://info.sporttery.com/interface/m/?c=fb&a=get_match_result&sdate=2012-12-06&edate=2012-12-06&lid=&page=1&num=9999&auth_type=uuid&auth_value=0123456789012345
    	 */
    	HeaderJSONBean ms = null;
    	String result = HttpServices.getHttpData(url);//通过URL向服务器请求后返回的数据
    	if (result==null||"".equals(result)) {
			SystemClock.sleep(15);
			result = HttpServices.getHttpData(url);
			System.out.println("第一次返回的数据为：！！！！ " + result.toString());
		}
    	if (result != null) {
			try {
				JSONObject json = new JSONObject(result);
				JSONObject statusObject = json.optJSONObject("status");
				if (statusObject!=null) {
					String code = statusObject.getString("code");
					ms = new HeaderJSONBean(code);
					if ("0".equals(code)) {
						JSONObject dataObject = json.optJSONObject("data");
						if (dataObject != null) {
							JSONObject headObj = dataObject.optJSONObject("head");
							HashMap<String, String> map = null;
								if (headObj!=null) {
									map = ms.header;
									map.put("total",  headObj.optInt("total")+"");
									map.put("rspdt", headObj.optString("rspdt"));
									System.out.println();
								}
																		
							
							
							JSONArray bodyArray = dataObject.optJSONArray("body");
						
							if (bodyArray != null) {
								initScoreOnLive(ms,bodyArray,map.get("rspdt"),status);
							}
						}
						
					}
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	return ms;
    }
    
    
    
    private void initScoreOnLive(HeaderJSONBean tjb,JSONArray bodyObj,String rspdt,String status)
    {
    	List<Map<String, String>> body = tjb.bodys;
    	int length =  bodyObj.length();
    	Map<String, String> map = null;
    	JSONObject jsonObj = null;
    	for (int i = 0; i < length; i++) {
			try {
				jsonObj = bodyObj.getJSONObject(i);
				map = new HashMap<String, String>();
				map.put("date", jsonObj.optString("t1", "-"));
				map.put("sessionId", jsonObj.optString("num", "-"));//FIXME 到后来要和第一期一样
				map.put("dt2", jsonObj.optString("dt2", "-"));
				map.put("lsc", jsonObj.optString("lsc", ""));
			    map.put("m_id", jsonObj.optString("m_id", ""));
			    map.put("league", jsonObj.optString("l_cn", ""));
			    map.put("l_id", jsonObj.optString("l_id"));
			    map.put("homeNameText", jsonObj.optString("h_cn", ""));
			    map.put("aWayNameText", jsonObj.optString("a_cn", ""));
			    map.put("handicap", jsonObj.optString("goalline", "-"));
			    map.put("half",jsonObj.optString("half", ""));
			    map.put("audience",jsonObj.optString("full", ""));
			    map.put("startDate", new Date().getTime()+"");
			    map.put("selectS", status);//用户选择的状态
			    map.put("nowDiff", ""+(StatusAction.parse(rspdt, "yyyy-MM-dd hh:mm:ss").getTime()-StatusAction.parse(map.get("dt2"), "yyyy-MM-dd hh:mm:ss").getTime()));
			   
			    map.put("status", jsonObj.optString("status", ""));
			   
			    body.add(map);
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
    		
		}
    }
    
   
}
