package cn.chaoren.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.util.Log;
import android.util.SparseArray;




//计算资金
public class CalulateBonus { 

	// static final Map<Integer,String> gateway = new HashMap<Integer,String>();
	public static final SparseArray<SparseArray<int[]>> gateway = new SparseArray<SparseArray<int[]>>();

	static {
		final SparseArray<int[]> gate2 = new SparseArray<int[]>();
		gate2.put(1, new int[] { 2 });
		gateway.put(2, gate2);

		final SparseArray<int[]> gate3 = new SparseArray<int[]>();
		gate3.put(1, new int[] { 3 });
		gate3.put(3, new int[] { 2 });
		gate3.put(4, new int[] { 3, 2 });
		gateway.put(3, gate3);

		final SparseArray<int[]> gate4 = new SparseArray<int[]>();
		gate4.put(1, new int[] { 4 });
		gate4.put(4, new int[] { 3 });
		gate4.put(5, new int[] { 4, 3 });
		gate4.put(6, new int[] { 2 });
		gate4.put(11, new int[] { 4, 3, 2 });
		gateway.put(4, gate4);

		final SparseArray<int[]> gate5 = new SparseArray<int[]>();
		gate5.put(1, new int[] { 5 });
		gate5.put(5, new int[] { 4 });
		gate5.put(6, new int[] { 5, 4 });
		gate5.put(10, new int[] { 2 });
		gate5.put(16, new int[] { 5, 4, 3 });
		gate5.put(20, new int[] { 3, 2 });
		gate5.put(26, new int[] { 5, 4, 3, 2 });
		gateway.put(5, gate5);

		final SparseArray<int[]> gate6 = new SparseArray<int[]>();
		gate6.put(1, new int[] { 6 });
		gate6.put(6, new int[] { 5 });
		gate6.put(7, new int[] { 6, 5 });
		gate6.put(15, new int[] { 2 });
		gate6.put(20, new int[] { 3 });
		gate6.put(22, new int[] { 6, 5, 4 });
		gate6.put(42, new int[] { 6, 5, 4, 3 });
		gate6.put(50, new int[] { 4, 3, 2 });
		gate6.put(57, new int[] { 6, 5, 4, 3, 2 });
		gateway.put(6, gate6);

		final SparseArray<int[]> gate7 = new SparseArray<int[]>();
		gate7.put(1, new int[] { 7 });
		gate7.put(7, new int[] { 6 });
		gate7.put(8, new int[] { 7, 6 });
		gate7.put(21, new int[] { 5 });
		gate7.put(35, new int[] { 4 });
		gate7.put(120, new int[] { 7, 6, 5, 4, 3, 2 });
		gateway.put(7, gate7);

		final SparseArray<int[]> gate8 = new SparseArray<int[]>();
		gate8.put(1, new int[] { 8 });
		gate8.put(8, new int[] { 7 });
		gate8.put(9, new int[] { 8, 7 });
		gate8.put(28, new int[] { 6 });
		gate8.put(56, new int[] { 5 });
		gate8.put(70, new int[] { 4 });
		gate8.put(247, new int[] { 8, 7, 6, 5, 4, 3, 2 });
		gateway.put(8, gate8);

	}

	public static Map<String, Float> getBonus(int gate) {

		//调用这个方法返回这个数组
		SparseArray<List<String>> list = MatchSelectList.getContest();
		int si = list.size();

		float[] minArr = new float[si];
		float[] maxArr = new float[si];
		float[] lenArr = new float[si];
		for (int i = 0; i < si; i++) {
			int key = list.keyAt(i);
			Map<String, String> ma = MatchSelectList.getMatchData().get(key);
			
			List<String> win = list.get(key);
			
			float minsp = 0;
			float maxsp = 0;
			for (int j = 0; j < win.size(); j++) {
				String wl = win.get(j);
				float sp = Float.parseFloat(ma.get("sp" + wl));

				if (minsp == 0) {
					minsp = maxsp = sp;
				} else {
					minsp = sp < minsp ? sp : minsp;
					maxsp = sp > maxsp ? sp : maxsp;
				}
			}
			minArr[i] = minsp;
			maxArr[i] = maxsp;
			lenArr[i] = win.size();
		}

		SparseArray<int[]> gat = gateway.get(si);
		int[] gs = gat.get(gate);
		List<Float> mins = new ArrayList<Float>();
		List<Float> maxs = new ArrayList<Float>();
		List<Float> lens = new ArrayList<Float>();
		
		
		for (int k = 0; k < gs.length; k++) {
			List<Float> minq = calulateBonusByMethod(minArr, gs[k]);
			List<Float> maxq = calulateBonusByMethod(maxArr, gs[k]);
			List<Float> lenq = calulateBonusByMethod(lenArr, gs[k]);
			mins.addAll(minq);
			maxs.addAll(maxq);
			lens.addAll(lenq);
		}

		float minp = mins.get(0);
		
		float maxp = 0;
		float lenp = 0;
		for (int k = 0; k < mins.size(); k++) {

			minp = mins.get(k) < minp ? mins.get(k) : minp;
			maxp += maxs.get(k);
			lenp +=lens.get(k);
		}
		
		
	
		Map<String, Float> res = new HashMap<String, Float>();
		res.put("min", minp * 2);//最小金额
		res.put("max", maxp * 2);//最大金额
		res.put("len", lenp );//注数
		
		return res;
	}

	public static Map<String, Float> getBonusbeishu(int gate,int beishu) {

		//调用这个方法返回这个数组
		SparseArray<List<String>> list = MatchSelectList.getContest();
		int si = list.size();

		float[] minArr = new float[si];
		float[] maxArr = new float[si];
		float[] lenArr = new float[si];
		for (int i = 0; i < si; i++) {
			int key = list.keyAt(i);
			Map<String, String> ma = MatchSelectList.getMatchData().get(key);
			
			List<String> win = list.get(key);
			
			float minsp = 0;
			float maxsp = 0;
			for (int j = 0; j < win.size(); j++) {
				String wl = win.get(j);
				float sp = Float.parseFloat(ma.get("sp" + wl));

				if (minsp == 0) {
					minsp = maxsp = sp;
				} else {
					minsp = sp < minsp ? sp : minsp;
					maxsp = sp > maxsp ? sp : maxsp;
				}
			}
			minArr[i] = minsp;
			maxArr[i] = maxsp;
			lenArr[i] = win.size();
		}

		SparseArray<int[]> gat = gateway.get(si);
		int[] gs = gat.get(gate);
		List<Float> mins = new ArrayList<Float>();
		List<Float> maxs = new ArrayList<Float>();
		List<Float> lens = new ArrayList<Float>();
		
		
		for (int k = 0; k < gs.length; k++) {
			List<Float> minq = calulateBonusByMethod(minArr, gs[k]);
			List<Float> maxq = calulateBonusByMethod(maxArr, gs[k]);
			List<Float> lenq = calulateBonusByMethod(lenArr, gs[k]);
			mins.addAll(minq);
			maxs.addAll(maxq);
			lens.addAll(lenq);
		}

		float minp = mins.get(0);
		
		float maxp = 0;
		float lenp = 0;
		for (int k = 0; k < mins.size(); k++) {

			minp = mins.get(k) < minp ? mins.get(k) : minp;
			maxp += maxs.get(k);
			lenp +=lens.get(k);
		}
		
		
	
		Map<String, Float> res = new HashMap<String, Float>();
		res.put("min", minp * 2*beishu);//最小金额
		res.put("max", maxp * 2*beishu);//最大金额
		res.put("len", lenp );//注数
		
		return res;
	}

	
	public static List<Float> calulateBonusByMethod(float[] arr, int s) {

		int key_count = arr.length;

		List<Float> res = new ArrayList<Float>();

		int max = (int) (Math.pow(2, key_count) - 1);
		for (int i = max; i >= 1; i--) {
			String value = Integer.toBinaryString(i);
			String zero = "";
			if (value.length() != key_count) {
				for (int j = key_count; j > value.length(); j--) {
					zero += "0";
				}
				value = zero + value;
			}
			// -----
			int mx = countOne(value);
			if (mx != s) {
				continue;
			}
			float bonus = 1;
			for (int j = 0; j < value.length(); j++) {
				if (value.substring(j, j + 1).equals("1")) {

					float bon = arr[j];
					bonus *= bon;
				}
			}
			res.add(bonus);
		}
		return res;

	}

	public static int countOne(String s) {
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			String str = s.substring(i, i + 1);
			if (str.equals("1")) {
				count++;
			}
		}
		return count;
	}

	public static float getBonuss(int gate,List<Map<String, Object>> maplist,SparseArray<List<String>> list ,SparseArray<String>  result) {

		//SparseArray<List<String>> list = MatchSelectList.getContest();
		
		int si = list.size();//选择比赛方案

		
		float[] maxArr = new float[si];
		
		for (int i = 0; i < si; i++) {
			int key = list.keyAt(i);
			Map<String, Object> ma = maplist.get(key);
			
			List<String> win = list.get(key);//所选择的方案
			
			String res = result.get(i);//服务器返回的结果状态，只有一个状态
			float sp =0;
			for (int j = 0; j < win.size(); j++) {
				
				String wl = win.get(j);
				
				if(res.equals(wl)){//判断选择的和服务器的是否一样
				sp = Float.parseFloat((String) ma.get("sp" + wl));
				
				}
			}
			maxArr[i] = sp;
		}

		SparseArray<int[]> gat = gateway.get(si);
	
		int[] gs = gat.get(gate);

		List<Float> maxs = new ArrayList<Float>();

		
		
		for (int k = 0; k < gs.length; k++) {
			
			List<Float> maxq = calulateBonusByMethod(maxArr, gs[k]);
		
			maxs.addAll(maxq);
		}

		
		float maxp = 0;
		for (int k = 0; k < maxs.size(); k++) {	
			maxp += maxs.get(k);
		}
		return  maxp * 2;
	}

}
