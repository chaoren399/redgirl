package cn.chaoren.mvc.model;

import java.util.HashMap;

import cn.chaoren.mvc.parsers.BaseParser;
import cn.chaoren.mvc.parsers.LotteryParser;



import android.content.Context;

public class RequestVo {
	public int requestUrl;
	public Context context;
	public HashMap<String,String> requestDataMap;
	public BaseParser<?> jsonParser;
	public LotteryParser  jsonParserForLottery;
	
	public Float  distance;
	
}
