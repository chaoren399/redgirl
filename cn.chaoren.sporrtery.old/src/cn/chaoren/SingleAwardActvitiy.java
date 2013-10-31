package cn.chaoren;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.mobclick.android.MobclickAgent;

import cn.chaoren.action.ListViewUtil.ViewFactory;
import cn.chaoren.net.HttpServices;
import cn.chaoren.net.JSONBeanSingleResultFB;
import cn.chaoren.net.JSONBean.Data;
import cn.chaoren.R;
import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class SingleAwardActvitiy extends Activity{
	private LinearLayout awardLayout;
	private String[]titles;
	private TextView homeNameText,handicap,legual,sesseionId,date,awayNameText;
	private Map<String, String> map;
	private String mid = "";
	private List<ArrayList<Data>> data;
     protected void onCreate(android.os.Bundle savedInstanceState) 
     {
    	 super.onCreate(savedInstanceState);
    	 setContentView(R.layout.single_award);
    	 
    	 titles =new String[] {getString(R.string.h1),getString(R.string.d1),getString(R.string.a1)};
    	 map = (HashMap<String, String>)getIntent().getSerializableExtra("map");
    	 if (map != null) {
    		 System.out.println("not_null");
    		 
		}else
		{
			onBackPressed();
			return;
		}
    	 mid = map.get("m_id");
    	 homeNameText = (TextView) findViewById(R.id.homeNameText_title);
    	 handicap = (TextView) findViewById(R.id.handText);
    	 legual = (TextView) findViewById(R.id.legualText);
    	 sesseionId = (TextView) findViewById(R.id.sessionId);
    	 date = (TextView) findViewById(R.id.date);
    	 awardLayout = (LinearLayout) findViewById(R.id.award);
    	 awayNameText = (TextView) findViewById(R.id.awayNameText_title);
    	 
    	 homeNameText.setText("" + map.get("h_cn"));//主队
    	 awayNameText.setText("" + map.get("a_cn"));
    	 legual.setText("" + map.get("l_cn"));
    	 sesseionId.setText("" + map.get("num"));
    	 date.setText("" + map.get("date"));
    	 String temp = map.get("goaline");
    	 if (temp != null &&!"".equals(temp)) {
			handicap.setText("(" + temp +")");
		}else 
		{
			handicap.setText("");
		}
    	 
    	 new SingleAwardFB().execute(mid);
     };
     
     @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	MobclickAgent.onResume(this);
    }
     
     @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	MobclickAgent.onPause(this);
    }
     
     class SingleAwardFB extends AsyncTask<String, Integer, JSONBeanSingleResultFB>
     {

		@Override
		protected JSONBeanSingleResultFB doInBackground(String... params) {
			// TODO Auto-generated method stub
			return HttpServices.getSingleResult_FB(mid);
		}
    	 
		private TextView[] texts = new TextView[5];
		private int []ids={R.id.text0,R.id.text1,R.id.text2,R.id.text3,R.id.text4};
		private TextView[] values= new TextView[5];
		private int count = 0;
		private View line;
		
		
		
		private void newLine()
		{
			count = 0;
			line =  getLayoutInflater().inflate(R.layout.single_award_item_tablerow, null);
			item.addView(line);
			View view = line.findViewById(R.id.titleLayout);
			View valueView = line.findViewById(R.id.valueLayout);
			for (int i = 0; i < 5; i++) {
				texts[i] = (TextView) view.findViewById(ids[i]);
				values[i]=(TextView) valueView.findViewById(ids[i]);
			}
		}
		
		private ViewGroup item;
		
		private void newItem(int i)
		{
			View view = (ViewGroup) getLayoutInflater().inflate(R.layout.single_award_item, null);
			awardLayout.addView(view);
			TextView t = (TextView) view.findViewById(R.id.title);
			
			t.setText("" + titles[i]);
			item = (ViewGroup) view.findViewById(R.id.tableLayout);
		}
		
		@Override
		protected void onPostExecute(JSONBeanSingleResultFB result) {
			 data = result.singleAward;
			 if (data != null && data.size() > 0) {
				ArrayList<Data> _map = null;
				int size = data.size();
				int length =0;
				/*Set<Entry<String, String>> entrySize;*/
				Data aData = null;
				for (int i = 0; i < size; i++) {
					newItem(i);					
					newLine();
					
					_map = data.get(i);
					/*entrySize = _map.entrySet();*/
					length = _map.size();
					 for (int j = 0;j<length;j++) {
						 if (count > 4) {
								newLine();
							}
						 aData = _map.get(j);
						 texts[count].setText(aData.getKey()+"");
						 texts[count].setVisibility(View.VISIBLE);
						 values[count].setText("" + aData.getValue());
						 values[count].setVisibility(View.VISIBLE);
						 count ++;
						 
					}
					 count=0;
				}
			}
			super.onPostExecute(result);
		}
     }


	
}
