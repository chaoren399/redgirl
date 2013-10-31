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

public class FeedBackItemAdapterFB extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
	private ArrayList<Map<String, String>> dataList;
	public FeedBackItemAdapterFB(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
	}
	public void setDataList(ArrayList<Map<String, String>> dataList) {
		this.dataList = dataList;
	}
	
	public ArrayList<Map<String, String>> getDataList() {
		return dataList;
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

	/**
	 * date
	 * home
	 * away
	 * handicap handicap
	 * h
	 * d
	 * a
	 * result
	 * legual
	 * status
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    if (convertView == null) {
			convertView =mInflater.inflate(R.layout.feedback_fb, null);
		}
	    Map<String, String> data = (Map<String, String>) getItem(position);
	    TextView tempText = (TextView) convertView.findViewById(R.id.date);
	    tempText.setText("" + data.get("" + tempText.getTag()));
	    
	    tempText = (TextView) convertView.findViewById(R.id.homeText);
	    tempText.setText("" + data.get("" + tempText.getTag()));
	    tempText = (TextView) convertView.findViewById(R.id.awayText);
	    tempText.setText("" + data.get("" + tempText.getTag()));
	    
	    tempText = (TextView) convertView.findViewById(R.id.h);
	    
	    tempText.setText(mContext.getString(R.string.h)+"" + data.get("" + tempText.getTag()));
	    tempText = (TextView) convertView.findViewById(R.id.d);
	    tempText.setText(mContext.getString(R.string.d)+"" + data.get("" + tempText.getTag()));
	    tempText = (TextView) convertView.findViewById(R.id.a);
	    tempText.setText(mContext.getString(R.string.a)+"" + data.get("" + tempText.getTag()));
	    tempText = (TextView) convertView.findViewById(R.id.result);
	    tempText.setText(mContext.getString(R.string.sg1)+"" + data.get("" + tempText.getTag()));
	    tempText = (TextView) convertView.findViewById(R.id.legualText);
	    tempText.setText("" + data.get("" + tempText.getTag()));
	    tempText = (TextView) convertView.findViewById(R.id.stutasText);
	    tempText.setText("" + data.get("" + tempText.getTag()));
	    tempText = (TextView) convertView.findViewById(R.id.handicapText);
	    String temp = data.get("" + tempText.getTag());
	    if (temp == null || "".equals(temp) || "0".equals(temp)) {
	    	 tempText.setVisibility(View.GONE);
		}else
		{
			tempText.setVisibility(View.VISIBLE);
			tempText.setText("(" + temp+")");
		}	   
	    
	    return convertView;
	}

}
