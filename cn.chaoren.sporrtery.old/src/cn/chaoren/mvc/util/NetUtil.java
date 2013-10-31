package cn.chaoren.mvc.util;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;
import cn.chaoren.mvc.model.RequestVo;
import cn.chaoren.mvc.model.SelectInfo;
import cn.chaoren.R;

/**
 * @author Mathew
 */
public class NetUtil {
	private static final String TAG = "NetUtil";
	public  static BasicHeader[] headers = new BasicHeader[10];
	static {
		headers[0] = new BasicHeader("Appkey", "12343");
		headers[1] = new BasicHeader("Udid", "");//手机串号
		headers[2] = new BasicHeader("Os", "android");//
		headers[3] = new BasicHeader("Osversion", "");//
		headers[4] = new BasicHeader("Appversion", "");//1.0
		headers[5] = new BasicHeader("Sourceid", "");//
		headers[6] = new BasicHeader("Ver", "");
		headers[7] = new BasicHeader("Userid", "");
		headers[8] = new BasicHeader("Usersession", "");
		headers[9] = new BasicHeader("Unique", "");
	}
	
	/*
	 * 
	 */
	
	public static String GetSystemVersion()
	{
	return android.os.Build.VERSION.RELEASE;
	}


	public static Object post(RequestVo vo){
		DefaultHttpClient client = new DefaultHttpClient();
		Log.e("URL:", vo.context.getString(R.string.app_host).concat("c=fb&state=%E6%B2%B3%E5%8C%97%E7%9C%81&city=%E4%BF%9D%E5%AE%9A%E5%B8%82&auth_type=uuid"));
		
		HttpPost post = new HttpPost(vo.context.getString(R.string.app_host).concat("c=fb&state=%E6%B2%B3%E5%8C%97%E7%9C%81&city=%E4%BF%9D%E5%AE%9A%E5%B8%82&auth_type=uuid"));
		HttpParams params = new BasicHttpParams();// 
		params = new BasicHttpParams();   
	    HttpConnectionParams.setConnectionTimeout(params, 8000);   //连接超时
	    HttpConnectionParams.setSoTimeout(params, 5000);   //响应超时
		post.setParams(params);
		post.setHeaders(headers);
		Object obj = null;
		try {
		
				HashMap<String,String> map = vo.requestDataMap;
				ArrayList<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
				for(Map.Entry<String,String> entry:map.entrySet()){
					BasicNameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue());
					pairList.add(pair);
				}
				HttpEntity entity = new UrlEncodedFormEntity(pairList,"UTF-8");
				post.setEntity(entity);
			
		
			HttpResponse response = client.execute(post);//包含响应的状态和返回的结果==
			if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
				String jsonStr = EntityUtils.toString(response.getEntity(),"UTF-8");//系统提供的工具类，把HttpEntity对象转成String
				Log.e(NetUtil.class.getSimpleName(), jsonStr);
				try {
					obj = vo.jsonParser.parseJSON(jsonStr);//回调类真正起作用的地方 
				} catch (JSONException e) {
					Log.e(NetUtil.class.getSimpleName(), e.getLocalizedMessage(),e);
				}
				Log.i(TAG, "obj"+obj.toString());
				return obj;
			}
		} catch (ClientProtocolException e) {
			Log.e(NetUtil.class.getSimpleName(), e.getLocalizedMessage(),e);
		} catch (IOException e) {
			Log.e(NetUtil.class.getSimpleName(), e.getLocalizedMessage(),e);
		}
		return null;
	}
	
	/** 
	 * 省市的体彩店
	 * @param vo
	 * @return
	 */
	public static Object get(RequestVo vo,SelectInfo selectInfo){
		//float distance=selectInfo.distance;
	//	String sheng=ConvertCharSet.toUtf8String("省")
		String city=selectInfo.city;
		String province=selectInfo.province;
		if (province.equals(city)){
			province=province+"市";
			city=city+"市";
		}else{
			//province=province+"省";
			city=city+"市";
		}
		Log.i(TAG,"city+province"+ city+province);
		DefaultHttpClient client = new DefaultHttpClient();
		Log.i(TAG, "urlget:"+vo.context.getString(R.string.app_host).concat("c=fb&state=%E6%B2%B3%E5%8C%97%E7%9C%81&city=%E4%BF%9D%E5%AE%9A%E5%B8%82&auth_type=uuid"));
		Log.i(TAG,"urlget: 1"+ vo.context.getString(R.string.app_host).concat("c=fb&state=").concat(ConvertCharSet.toUtf8String(province)).concat("&city=").concat(ConvertCharSet.toUtf8String(city)).concat("&auth_type=uuid"));
		HttpGet get = new HttpGet(vo.context.getString(R.string.app_host).concat("c=fb&state=").concat(ConvertCharSet.toUtf8String(province)).concat("&city=").concat(ConvertCharSet.toUtf8String(city)).concat("&auth_type=uuid"));
//		HttpGet get = new HttpGet(vo.context.getString(R.string.app_host).concat("c=fb&state=%E6%B2%B3%E5%8C%97%E7%9C%81&city=%E4%BF%9D%E5%AE%9A%E5%B8%82&auth_type=uuid"));
		get.setHeaders(headers);
		Object obj = null;
		try {
			HttpResponse response = client.execute(get);
			if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
				String result = EntityUtils.toString(response.getEntity(),"UTF-8");
				Log.e(NetUtil.class.getSimpleName(), result);
				Log.e(TAG, "result"+result);
				try {
					obj = vo.jsonParserForLottery.parseJSON(result);
				} catch (JSONException e) {
					Log.e(NetUtil.class.getSimpleName(), e.getLocalizedMessage(),e);
				}
				return obj;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获得网络连接是否可用
	 * @param context
	 * @return
	 */
	public static boolean hasNetwork(Context context){
		ConnectivityManager con = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo workinfo = con.getActiveNetworkInfo();
		if(workinfo == null || !workinfo.isAvailable())
		{
			Toast.makeText(context, R.string.net_error, Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	/**
	 * 返回有距离信息的RequestVo
	 * @param vo
	 * @param selectInfo
	 * @return
	 */
	public static Object getDistance(RequestVo vo,SelectInfo selectInfo){
		//float distance=selectInfo.distance;
	//	Strign lat=selectInfo.startLatitude;
	//	String lon=selectInfo.startLongitude;
		String city=selectInfo.city;
		String province=selectInfo.province;
		DefaultHttpClient client = new DefaultHttpClient();
		
		//http://home.lottery.org.cn/mapinfo/getNear.php?lat=39.9&lon=116.7&count=100
		Log.i(TAG, vo.context.getString(R.string.app_host_getNear).concat("lat=").concat(selectInfo.startLatitude+"").concat("&lon=").concat(selectInfo.startLongitude+"").concat("&count=").concat(selectInfo.count+""));
		HttpGet get = new HttpGet(vo.context.getString(R.string.app_host_getNear).concat("lat=").concat(selectInfo.startLatitude+"").concat("&lon=").concat(selectInfo.startLongitude+"").concat("&count=").concat(selectInfo.count+""));
		get.setHeaders(headers);
		Object obj = null;
		try {
			HttpResponse response = client.execute(get);
			if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
				String result = EntityUtils.toString(response.getEntity(),"UTF-8");
				Log.e(NetUtil.class.getSimpleName(), result);
				try {
					obj = vo.jsonParser.parseJSON(result,selectInfo);
				} catch (JSONException e) {
					Log.e(NetUtil.class.getSimpleName(), e.getLocalizedMessage(),e);
				}
				return obj;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
