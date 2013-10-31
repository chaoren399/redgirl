package cn.chaoren.mvc.adapter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.chaoren.mvc.model.LotteryInfo;
import cn.chaoren.R;

public class MapListAdapter extends BaseAdapter {
	private Context context;
	private  TextView tv_shopname;
	private TextView tv_shopaddress;
	private TextView tv_shopnumber;
	private TextView tv_shopdistance;
	private ArrayList<LotteryInfo>  lottteryInfo;


	public MapListAdapter(Context context,ArrayList<LotteryInfo>  lottteryInfo) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.lottteryInfo=lottteryInfo;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lottteryInfo.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lottteryInfo.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int positon , View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		View view ;
		HolderView holderView;
		if(convertView!=null){
			view = convertView;
			holderView = (HolderView) view.getTag();
		}else{
			view = View.inflate(context,R.layout.dianmianlist_item, null);
			holderView = new HolderView();
			
			holderView. tv_shopname = (TextView) view.findViewById(R.id.tv_shopname);
			holderView. tv_shopaddress = (TextView) view
					.findViewById(R.id.tv_shopaddress);
			holderView. tv_shopnumber = (TextView) view
					.findViewById(R.id.tv_shopnumber);
			holderView. tv_shopdistance = (TextView) view
					.findViewById(R.id.tv_shopdistance);
			view.setTag(holderView);
			
		
		}
		 

		holderView.tv_shopname.setText(lottteryInfo.get(positon).wdinfoNum+"-"+lottteryInfo.get(positon).address);
		holderView.tv_shopaddress.setText("地址："+lottteryInfo.get(positon).address);
		holderView.tv_shopnumber.setText("电话："+lottteryInfo.get(positon).tel);
		holderView.tv_shopdistance.setText(lottteryInfo.get(positon).btDistance+"米");
		
		
		
		
		return view;
		
		
	}
	class HolderView{
		
		public  TextView tv_shopname;
		public TextView tv_shopaddress;
		public TextView tv_shopnumber;
		public TextView tv_shopdistance;
	}

}
