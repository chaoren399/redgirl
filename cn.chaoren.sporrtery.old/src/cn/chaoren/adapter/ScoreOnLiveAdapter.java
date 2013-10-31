package cn.chaoren.adapter;

import java.util.ArrayList;

import com.mobclick.android.r;

import cn.chaoren.obj.ScoreOnLive;
import cn.chaoren.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ScoreOnLiveAdapter extends BaseAdapter{
	private ArrayList<ScoreOnLive> onLiveList;
	private LayoutInflater mInflater;
	private Context mContext;
	public ScoreOnLiveAdapter(Context context) {
		mContext =context;
		mInflater = LayoutInflater.from(mContext);
	}
	public void setOnLiveList(ArrayList<ScoreOnLive> onLiveList) {
		System.out.println("setOnLiveList");
		this.onLiveList = onLiveList;
	}
	@Override
	public int getCount() {
		System.out.println("getCount");
		if (onLiveList != null) {
			return onLiveList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (onLiveList != null) {
			return onLiveList.get(getCount()-position-1);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		Object item = getItem(position);
		return item.hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		System.out.println("getView");
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.score_onlive_player_item, null);
		}
		ScoreOnLive score = (ScoreOnLive) getItem(position);
		if (score != null) {
			View show = null;
			if (null!=score.ho&&!"".equals(score.ho)) {
				show = convertView.findViewById(R.id.awayLayout);
				show.setVisibility(View.GONE);
				show = convertView.findViewById(R.id.homeLayout);
				show.setVisibility(View.VISIBLE);
			}else if(null!=score.ao&&!"".equals(score.ao))
			{
				show = convertView.findViewById(R.id.homeLayout);
				show.setVisibility(View.GONE);
				show = convertView.findViewById(R.id.awayLayout);
				show.setVisibility(View.VISIBLE);
			}
			View temp = null;
			if (show!=null) {
				temp= show.findViewById(R.id.type);
				TextView text = (TextView) show.findViewById(R.id.tText);
				text.setVisibility(View.GONE);
				int resId = 0;
				switch (score.eid) {
				case 1:
					resId = R.drawable.ruqiu;
					break;
				case 2:
					resId = R.drawable.hongpai;
						break;
				case 3:
				resId = R.drawable.huangpai;
					break;
				case 7:
					resId = R.drawable.dianqiu;
					text.setVisibility(View.VISIBLE);
					text.setTextColor(Color.GREEN);
					text.setText("P");
					break;
				case 8:
					resId = R.drawable.wulong;
					text.setVisibility(View.VISIBLE);
					text.setTextColor(Color.YELLOW);
					text.setText("O");
					break;
				case 9:
					resId = R.drawable.lianghuangbianhong;
					break;
					
				case 55:
					resId = R.drawable.biaozhu;
					break;
				case 11:
					resId = R.drawable.huanren;
					break;
				case 4:
					resId = R.drawable.huanru;
					
					break;
				case 5:
					resId = R.drawable.huanchu;
					break;
				default:
					break;
				}
				temp.setBackgroundResource(resId);
				
				if (score.ho!=null&&!"".equals(score.ho)) {
					TextView play = (TextView) show.findViewById(R.id.playerText);
					play.setText("" + score.ho);
				}
				if (score.ao!= null&&!"".equals(score.ao)) {
					TextView play = (TextView) show.findViewById(R.id.playerText);
					play.setText("" + score.ao);
				}
				
			}else
			{
				show = convertView.findViewById(R.id.awayLayout);
				show.setVisibility(View.INVISIBLE);
				show = convertView.findViewById(R.id.homeLayout);
				show.setVisibility(View.INVISIBLE);
			}
			if (score.time!=null) {
				TextView time = (TextView) convertView.findViewById(R.id.time);
				time.setText( score.time+"'");
			}
			/*if (score.eid == 1) {
				temp = show.findViewById(R.id.type);
				temp.setVisibility(View.GONE);
				temp = show.findViewById(R.id.goal);
				temp.setVisibility(View.VISIBLE);
				temp.setBackgroundResource(R.drawable.ruqiu);
			}else if (score.eid == 2) {
				temp = show.findViewById(R.id.goal);
				temp.setVisibility(View.GONE);
				temp = show.findViewById(R.id.type);
				temp.setVisibility(View.VISIBLE);
				temp.setBackgroundResource(R.drawable.icon13);
			}else if (score.eid == 3) {
				temp = show.findViewById(R.id.goal);
				temp.setVisibility(View.GONE);
				temp = show.findViewById(R.id.type);
				temp.setVisibility(View.VISIBLE);
				temp.setBackgroundResource(R.drawable.icon14);
				temp.setBackgroundResource(resid);
			}*/
			
			
		}
		return convertView;
	}

}
