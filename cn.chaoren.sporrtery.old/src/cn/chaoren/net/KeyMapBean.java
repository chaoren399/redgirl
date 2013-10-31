package cn.chaoren.net;

import java.util.HashMap;

public class KeyMapBean {
	public KeyMapBean(String code) {
		this.code = code;
	}
	
	public String code;
     public static class KeyMap
     {
    	 @SuppressWarnings("unchecked")
		public KeyMap clone()
    	 {
    		 KeyMap key = new KeyMap();
    		 if (keys!=null) {
    			 key.keys = keys.clone();
			}
    		
    		 key.map = (HashMap<String, String>) map.clone();
    		 key.selectValue = selectValue;
    		
    		 return key;
    	 }
    	 
    	 public void initDefOne(String def)
    	 {
    		 keys = new String[1];
    		 keys[0] = def;
    		 
    	 }
    	 
    	 public String[]keys;
    	 public HashMap<String, String> map = new HashMap<String, String>();
    	 public String getValue(int index)
    	 {
    		 if (keys != null && index <keys.length) {
				return getValue(keys[index]);
			}
    		 return "";
    	 }
    	 
    	 public void put(String key,String value)
    	 {
    		 map.put(key, value);
    	 }
    	 
    	 public String select(int position)
    	 {
    		 selectValue = getValue(position);
    		 return selectValue;
    	 }
    	 
    	 public String selectValue;
    	 
    	 //has key,and has values
    	 public String getValue(String key)
    	 {
    		 if (map.containsKey(key)) {
				return map.get(key);
			}
    		 return "";
    	 }
    	 
     }
     
     
     
     public HashMap<String ,KeyMap> keyList = new HashMap<String, KeyMapBean.KeyMap>();
}
