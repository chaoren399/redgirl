package cn.chaoren.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.inputmethodservice.Keyboard.Key;

import cn.chaoren.util.StringUtils;

/**
 * 类说明：
 * 
 * @创建时间 2011-6-25 下午11:07:24
 * @创建人： 陆林
 * @邮箱：15301586841@189.cn
 */
public class JSONBean {
	public static class KeyData
	{
	 	public ArrayList<Data> dataList =new ArrayList<Data>();
	 	
	 	public Map<String, String> keyList = new HashMap<String, String>();
	 	public String singleKey;
	 	
	}
	public static class  Data
	{
		public Data() {
			// TODO Auto-generated constructor stub
		}
		public Data(String key,String value) {
			this.key = key;
			this.value = value;
		}
		public String getKey() {
			return key;
		}
		
		public void setKey(String key) {
			this.key = key;
		}
		public void setValue(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
		private String key;
		private String value;
	}
	// 0 - 无错误
	public String code;
	public List<String> times = new ArrayList<String>();
	public String cnumw = StringUtils.getWeek();
	public List<Map<String, String>> bodys = new ArrayList<Map<String, String>>();

	public JSONBean(String _code) {//这个方法很重要
		code = _code;
	}
	
	public Data newData()
	{
		return new Data();
	}
	
	public KeyData newKeyData()
	{
		return new KeyData();
	}
}
