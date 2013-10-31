package cn.chaoren.net;

import java.util.ArrayList;
import java.util.Map;

public class JSONBeanSingleResultFB extends JSONBean {
	
	
	
	
	public JSONBeanSingleResultFB(String _code) {
		super(_code);

	}

	// -1,让球情况，“胜平负”玩法的让球数据，主队让1球， 如为+2，表示客队让2球
		public String goaline;
		
		public ArrayList<ArrayList<Data>> singleAward = new ArrayList<ArrayList<Data>>();
	
}
