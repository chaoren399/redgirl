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
public class JSONBeanPlayBK extends JSONBean {
	// 让球情况，“胜平负”玩法的让球数据，主队让1球， 如为+2，表示客队让2球
	public String goaline;

	// 玩法: 胜负
	public List<List<String>> mnl = new ArrayList<List<String>>();

	// 玩法：让分胜负
	public List<List<String>> hdc = new ArrayList<List<String>>();

	// 玩法: 胜分差
	public List<List<String>> wnm = new ArrayList<List<String>>();

	// 玩法：大小分
	public List<List<String>> hilo = new ArrayList<List<String>>();

	public JSONBeanPlayBK(String _code) {
		super(_code);
	}

}