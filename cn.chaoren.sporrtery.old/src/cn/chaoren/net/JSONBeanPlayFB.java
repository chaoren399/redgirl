package cn.chaoren.net;

import java.util.ArrayList;
import java.util.List;

/**
 * 类说明：
 * 
 * @创建时间 2011-6-29 下午02:03:53
 * @创建人： 陆林
 * @邮箱：15366189868@189.cn
 */
public class JSONBeanPlayFB extends JSONBean {
	// 让球情况，“胜平负”玩法的让球数据，主队让1球， 如为+2，表示客队让2球
	public String goaline;

	// 玩法: 胜平负
	public List<List<String>> hhad = new ArrayList<List<String>>();

	// 玩法：比分
	public List<List<List<String>>> crs = new ArrayList<List<List<String>>>();

	//玩法: 总进球
	public List<List<String>> ttg = new ArrayList<List<String>>();
	
	//玩法：半全场胜平负
	public List<List<String>> hafu = new ArrayList<List<String>>();
	
	public JSONBeanPlayFB(String _code) {
		super(_code);
	}

}