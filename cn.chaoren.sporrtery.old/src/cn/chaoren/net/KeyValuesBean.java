package cn.chaoren.net;

import java.util.ArrayList;
import java.util.HashMap;


public class KeyValuesBean {
	public String code;
	public KeyValuesBean(String c) {
		code = c;
	}
     public static class KeyValues
     {
    	 public String[]keys;
    	 public ArrayList<HashMap<String, String>> valuesList = new  ArrayList<HashMap<String,String>>();
     }
     
     public HashMap<String, KeyValues> data = new HashMap<String, KeyValuesBean.KeyValues>();
}
