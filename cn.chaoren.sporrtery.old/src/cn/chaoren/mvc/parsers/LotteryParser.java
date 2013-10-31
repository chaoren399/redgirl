package cn.chaoren.mvc.parsers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.util.Log;
import cn.chaoren.mvc.model.LotteryInfo;
import cn.chaoren.mvc.model.SelectInfo;

public class LotteryParser extends BaseParser<ArrayList<LotteryInfo>> {

	private static final String TAG = "LotteryParser";

	@Override
	public ArrayList<LotteryInfo> parseJSON(String json,
			SelectInfo selectInfo) throws JSONException {
		// TODO Auto-generated method stub
		float[] results=new float[1]; 
				ArrayList<LotteryInfo> lst=new  ArrayList<LotteryInfo>();
				//	LotteryInfo lotteryInfo=new LotteryInfo();
					if(json !=null){
						
					
					try {
						JSONObject  jsonObj=new JSONObject(json);
						Log.i(TAG, "jsonObj"+jsonObj.toString());
						String data=jsonObj.getString("data");
						JSONArray jsonObjs=new JSONArray(data);
						for(int  i = 0;i < jsonObjs.length();++i){
							JSONObject datas = jsonObjs.optJSONObject(i);
							LotteryInfo	lotteryInfo=new LotteryInfo();
							//JSONObject datas = jsonObj.
							lotteryInfo.id=datas.optString("id");
							lotteryInfo.province= datas.optString("province");
							lotteryInfo.city=datas.optString("city");
							lotteryInfo.address=datas.optString("address");
							lotteryInfo.tel= datas.optString("tel");
							lotteryInfo.lat=datas.getDouble("lat");
							lotteryInfo.lon=datas.getDouble("lon");
							
							 
							Location.distanceBetween(selectInfo.startLatitude, selectInfo.startLongitude, datas.getDouble("lat"), datas.getDouble("lon"), results);
							if(results[0]< Float.parseFloat(selectInfo.distance)	){
								float b = (float)(Math.round(results[0])); 
								lotteryInfo.btDistance=b;
								lst.add(lotteryInfo);  // 将距离在选择范围的Info放入集合中，以便于界面显示
								
							}
							
						}
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
					Log.i(TAG, "lst:"+lst.size()+lst.toString());
					return lst;
	}

	@Override
	public ArrayList<LotteryInfo> parseJSON(String json)
			throws JSONException {
		/// TODO Auto-generated method stub
		ArrayList<LotteryInfo> lst=new  ArrayList<LotteryInfo>();
		//	LotteryInfo lotteryInfo=new LotteryInfo();
			if(json !=null){
				
			
			try {
				JSONObject  jsonObj=new JSONObject(json);
				Log.i(TAG, "jsonObj"+jsonObj.toString());
				String data=jsonObj.getString("data");
				JSONArray jsonObjs=new JSONArray(data);
				for(int  i = 0;i < jsonObjs.length();++i){
					JSONObject datas = jsonObjs.optJSONObject(i);
					LotteryInfo	lotteryInfo=new LotteryInfo();
					//JSONObject datas = jsonObj.
					lotteryInfo.id=datas.optString("id");
					lotteryInfo.province= datas.optString("province");
					lotteryInfo.city=datas.optString("city");
					lotteryInfo.address=datas.optString("address");
					lotteryInfo.tel= datas.optString("tel");
					lotteryInfo.lat=datas.getDouble("lat");
					lotteryInfo.lon=datas.getDouble("lon");
					lst.add(lotteryInfo);
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			Log.i(TAG, "lst:"+lst.size()+lst.toString());
			return lst;
	}

	
	

	
}
