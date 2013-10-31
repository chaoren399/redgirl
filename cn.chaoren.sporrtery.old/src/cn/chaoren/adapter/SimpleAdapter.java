package cn.chaoren.adapter;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SimpleAdapter extends BaseAdapter {
   
	private int onClickId;
	private View.OnClickListener mOnClick;
	public void setOnClickId(int onClickId,View.OnClickListener onClick) {
		this.onClickId = onClickId;
		mOnClick = onClick;
	}
	
	private int layoutResId;
	private LayoutInflater mInflater;
	private Context mContext;
	public SimpleAdapter(Context context,ArrayList<Map<String, String>> data,int layoutResId,String[]from,int []to) {
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		dataList = data;
		this.layoutResId = layoutResId;
		this.from = from;
		this.to = to;
	}
	private ArrayList<Map<String, String>> dataList;
    
    public void setDataList(ArrayList<Map<String, String>> dataList) {
		this.dataList = dataList;
	}
    
    private String[]from;
    private int[]to;
    
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
			convertView = mInflater.inflate(layoutResId, null);
		}
		Map<String, String> map = (Map<String, String>) getItem(position);
		int length = from.length;
		View view;
		if (onClickId!=0) {
			view = convertView.findViewById(onClickId);
			view.setOnClickListener(mOnClick);
		}
		
		TextView text;
		for (int i = 0; i < length; i++) {
			view = convertView.findViewById(to[i]);
			if (view instanceof TextView) {
				text = (TextView) view;
				text.setText("" + map.get(from[i]));
			}
			//else if 其他情况，暂时不考虑
				
		}
		return convertView;
	}

}
