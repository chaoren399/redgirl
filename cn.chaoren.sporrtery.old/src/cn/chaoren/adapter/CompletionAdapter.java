package cn.chaoren.adapter;

import java.util.ArrayList;
import java.util.Map;

import cn.chaoren.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CompletionAdapter extends BaseAdapter{
	private LayoutInflater mInflater;
	private Context mContext;
	private int type = 1;//type 1,足球，2，篮球
	 public CompletionAdapter(Context context) {
			mContext = context;
			mInflater = LayoutInflater.from(mContext);
		}
    public CompletionAdapter(Context context,int type) {
    	this.type = type;
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
	}
	private ArrayList<Map<String, String>> dataList ;
   
    public void setDataList(ArrayList<Map<String, String>> dataList) {
		this.dataList = dataList;
	}
    
    
    
	@Override
	public int getCount() {
		if (dataList != null) {
			return dataList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (dataList != null) {
			return dataList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		Object obj = getItem(position);
		if (obj != null) {
			return obj.hashCode();
		}
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			if (type ==1) {
				convertView = mInflater.inflate(R.layout.feedback_detail_item, null);
			}else if (type == 2) {
				convertView = mInflater.inflate(R.layout.feedback_detail_item_bb, null);
			}
			
		}
		Map<String, String> map = (Map<String, String>) getItem(position);
		if (map != null) {
			
			int[] ids= {R.id.text0,R.id.text1,R.id.text2,R.id.text3};
			TextView temp=null;
			temp = (TextView) convertView.findViewById(R.id.date);
			temp.setText("" + map.get(temp.getTag()+""));
			View line = convertView.findViewById(R.id.value_line0);
			for (int i = 0; i < 4; i++) {
				temp = (TextView) line.findViewById(ids[i]);
				temp.setText("" + map.get("" + temp.getTag()));
			}
			
			line = convertView.findViewById(R.id.value_line1);
			for (int i = 0; i < 4; i++) {
				temp = (TextView) line.findViewById(ids[i]);
				temp.setText("" + map.get("" + temp.getTag()));
			}
			if (type == 2) {
				line = convertView.findViewById(R.id.value_line2);
				for (int i = 0; i < 3; i++) {
					temp = (TextView) line.findViewById(ids[i]);
					temp.setText("" + map.get("" + temp.getTag()));
				}
				temp = (TextView) line.findViewById(ids[3]);
				temp.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Toast.makeText(mContext, "进入详情", 0).show();						
					}
				});
			}
		}
		
		//2
		/**
		 * xiangxi
		 * daxiaofen
		 * shengfencha
		 * rangfenzhufu
		 * quanchang
		 * kedui
		 * zhudui
		 * shengfu
		 */
		return convertView;
	}
  
}
