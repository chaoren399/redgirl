package cn.chaoren.net;

import java.util.ArrayList;
import java.util.List;

/**
 * 类说明：
 * 
 * @创建时间 2011-7-21 下午02:17:11
 * @创建人： 陆林
 * @邮箱：15366189868@189.cn
 */
public class JSONBeanResultBK extends JSONBean {

	public String mnl_c, mnl_value, mnl_m, mnl_count, mnl_c2;

	public String hdc_goalline, hdc_c, hdc_value, hdc_m, hdc_count;
	public List<String> hdc_c2_list = new ArrayList<String>();

	public String wnm_c, wnm_value, wnm_m, wnm_count, wnm_c2;

	public String hilo_goalline, hilo_c, hilo_value, hilo_m, hilo_count;
	public List<String> hilo_c2_list = new ArrayList<String>();

	public JSONBeanResultBK(String _code) {
		super(_code);
	}

}
