package cn.chaoren.mvc.model;

import java.io.Serializable;

import android.support.v4.os.ParcelableCompat;

public class LotteryInfo  implements Serializable {
	public String id;
//	public String uid;
	public String province; //省
	public String city;//市
	public String address; //"保定市双胜街北口471号",
	public String tel; //"0312-5062742",
	public double lat;//纬度
	public double lon;//经度
	public String wdinfoNum;//  经营编号
	public String serviceScope; // "竞彩、传统足彩、数字彩票、即开彩票"
	
	public float btDistance; // 与我的距离
	public String toString() {
		return "LotteryInfo [id=" + id + ", province=" + province + ", city="
				+ city + ", address=" + address + ", tel=" + tel + ", lat="
				+ lat + ", lon=" + lon + ", wdinfoNum=" + wdinfoNum
				+ ", serviceScope=" + serviceScope + "]";
	}
	
	
	

}
