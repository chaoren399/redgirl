package cn.chaoren.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.SparseArray;



public class MyCalulateBonus {
	



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

		public static float getBonus(int gate,List<Map<String, String>> maplist,SparseArray<List<String>> list ,  SparseArray<String>  result) {

			//SparseArray<List<String>> list = MatchSelectList.getContest();
			int si = list.size();//选了几场比赛

			
			float[] maxArr = new float[si];  // 这是赔率
			
			for (int i = 0; i < si; i++) {
				int key = list.keyAt(i);  // match_id
				Map<String, String> ma = maplist.get(i);
				List<String> win = list.get(key);//根据match_id得到选择胜平负的list集合《1/0》
				String res = result.get(key);//
				
				float sp =0;
				for (int j = 0; j < win.size(); j++) {
					
					String wl = win.get(j);
					if("cancel".equals(res)){ // add by zzh
						sp = 1.00f;
						
					}else	if(wl.equals(res)){//与比赛结果比较。如果中奖则返回。赔率：sp?
					sp = Float.parseFloat(ma.get("sp" + wl));

					}
				}

				maxArr[i] = sp;//赔率的数组
		
			}

			SparseArray<int[]> gat = gateway.get(si);
		
			int[] gs = gat.get(gate);//得到每种过关方式中中奖的组合。例如  gate4.put(11, new int[] { 4, 3, 2 });中的int[]

			List<Float> maxs = new ArrayList<Float>();

			
			
			for (int k = 0; k < gs.length; k++) {
				
				List<Float> maxq = calulateBonusByMethod(maxArr, gs[k]);
			
				
				maxs.addAll(maxq);
				
			}

			
			
			float maxp = 0;
			for (int k = 0; k < maxs.size(); k++) {	
				//maxp += maxs.get(k);
				//这里要做转化   --得出的数值先精确到分，然后再x2x购买倍数=最终奖金，
				BigDecimal b=new BigDecimal(maxs.get(k));
				 maxp +=b.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
				
			}

		


			return  maxp * 2;
		
		
		}

		public static List<Float> calulateBonusByMethod(float[] arr, int s) {

			int key_count = arr.length;//赔率数组的大小4
			// String max="";
			// for(int i=1;i<=key_count;i++){
			// max+="1";
			// }

			List<Float> res = new ArrayList<Float>();

			int max = (int) (Math.pow(2, key_count) - 1); //2的4次幂－1=15
			for (int i = max; i >= 1; i--) {//15，14，13,12,11
				String value = Integer.toBinaryString(i); // 15－－1111
				
				String zero = "";
				if (value.length() != key_count) {
					for (int j = key_count; j > value.length(); j--) {
						zero += "0";
					}
					value = zero + value;
				}
				System.out.println(value);
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
			/**
			 * 返回1的个数
			 * @param s
			 * @return
			 */
		public static int countOne(String s) {//s==1111
			int count = 0;
			for (int i = 0; i < s.length(); i++) {
				String str = s.substring(i, i + 1);
				if (str.equals("1")) {
					count++;
				}
			}
			return count;
		}

		 

}
