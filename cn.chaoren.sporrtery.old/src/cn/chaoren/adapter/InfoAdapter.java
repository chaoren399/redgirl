package cn.chaoren.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.chaoren.obj.Information;
import cn.chaoren.R;

public class InfoAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    public InfoAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
	}
    public InfoAdapter(LayoutInflater inflater) {
		mInflater = inflater;
	}
	private ArrayList<Information> mInfoList;
	
	public void setInfoList(ArrayList<Information> infoList) {
		this.mInfoList = infoList;
	}
	
	public ArrayList<Information> getInfoList() {
		return mInfoList;
	}
	
	@Override
	public int getCount() {
		if (mInfoList != null) {
			return mInfoList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (mInfoList != null) {
			return mInfoList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		Information info = (Information) getItem(position);
		if (info != null) {
			return info.id;
		}
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.info_list_item, null);
			
		}
		Information info = (Information) getItem(position);
		if (info!= null) {
			TextView title = (TextView) convertView.findViewById(R.id.title);
			TextView date = (TextView) convertView.findViewById(R.id.date);
			title.setText("" + info.title);
			date.setText("" + info.date);
		}
		return convertView;
	}

}
