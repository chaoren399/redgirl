package cn.chaoren.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类说明：
 * 
 * @创建时间 2011-7-21 上午11:26:10
 * @创建人： 陆林
 * @邮箱：15366189868@189.cn
 */
public class JSONBeanResultFB extends JSONBean {
	// -1,让球情况，“胜平负”玩法的让球数据，主队让1球， 如为+2，表示客队让2球
	public String goaline;

	public List<Map<String, String>> wanfa = new ArrayList<Map<String, String>>();//玩法

	//public List<ArrayList<Map<String, String>>> player = new ArrayList<ArrayList<Map<String,String>>>();
	public ArrayList<Map<String, String>> hdaPass = new ArrayList<Map<String,String>>();//胜平负闯关

	//public KeyData totalPass = new KeyData();//总进球过关
	public ArrayList<Map<String, String>> totalPass = new ArrayList<Map<String, String>>();//总进球过关
	//public ArrayList<KeyData> hAPass = new ArrayList<KeyData>();//半全场过关
	public ArrayList<Map<String, String>> hAPass = new ArrayList<Map<String,String>>();//半全场过关
	public ArrayList<Map<String, String>> scorePass = new ArrayList<Map<String, String>>();//比分固定奖金
	public ArrayList<String> scoreKeys = new ArrayList<String>();//比分固定奖金 的key
	public JSONBeanResultFB(String _code) {
		super(_code);
	}
	
	
	
	
	
	
}
