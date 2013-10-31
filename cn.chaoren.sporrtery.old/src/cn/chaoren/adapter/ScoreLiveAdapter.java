package cn.chaoren.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.chaoren.action.StatusAction;
import cn.chaoren.widget.ItemClickListener;
import cn.chaoren.widget.ToggleView;
import cn.chaoren.R;

   /**
    * 比分直播界面的足球listview的填充器
    * @author Administrator
    *
    */



public class ScoreLiveAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context mContext;
	private String[]status = null;
	public ScoreLiveAdapter(Context context,int type) {
		mContext = context;
		status = context.getResources().getStringArray(R.array.status_shown_array);
		mInflater = LayoutInflater.from(context);
		this.type =type;
		statusAction = new StatusAction();
	}
    private List<Map<String, String>> soccerData;//数据
    
    private int type  = -1;//1,足球；2，篮球
    
    private boolean typeChanged = false;
    
    public void setType(int type) {
    	if (type >2||type <1) {
			throw new IllegalArgumentException("Only type = 2 or 1 is current!");
		}
		if (this.type != type) {
	    	this.type = type;
	    	typeChanged = true;
		}
		
	}
    
    public String rspdt;
    
    private StatusAction statusAction = null;
    
    public void setData(List<Map<String, String>> soccerData) {
		this.soccerData = soccerData;
	}
    
    public List<Map<String, String>> getData() {
		return soccerData;
	}
    
	@Override
	public int getCount() {
		if (soccerData != null) {
			return soccerData.size();
		}
		return 0;
	}

	private ItemClickListener itemClick;
	
	public void setItemClick(ItemClickListener itemClick) {
		this.itemClick = itemClick;
	}
	
	/**
	 * @author Administrator
	 *
	 */
	public class ItemClick implements View.OnClickListener
	{
		private View item;
		public ItemClick(int pos,View item) {
			position = pos;
		}
        private int position;
		@Override
		public void onClick(View v) {
			if (itemClick != null) {
				itemClick.onItemClick(v, item, position);
			}
		}
	}
	
	
	
	@Override
	public Object getItem(int position) {
		if (soccerData != null && position < soccerData.size()) {
			return soccerData.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	public String getString(int resId)
	{
		return mContext.getString(resId);
	}
	
	
	public static int count = 0;
	
	
	/*public static boolean showStatus(String[]array,TextView text,String status,String date,String nowDate)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		long diff = 0;
		try {
			Date start = dateFormat.parse(date);
			Date now = null;
			if (nowDate==null||"".equals(nowDate)) {
				now = new Date();
			}else
			{
				now = dateFormat.parse(nowDate);
			}
		
			diff = now.getTime()-start.getTime();
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if ("0".equals(status)) {
			text.setText(array[0]);
		}else if ("1".equals(status)) {
			text.setText((diff/(1000*60)+count)+"'" );			
		}else if ("2".equals(status)) {
			text.setText(array[2]);
		}else if ("3".equals(status)) {
			text.setText((diff/(1000*60)-15+count)+"'");
		}else if ("-14".equals(status)) {
			text.setText(array[4]);
		}else if ("-13".equals(status)) {
			text.setText(array[5]);
		}else if ("-12".equals(status)) {
			text.setText(array[6]);
		}else if ("-11".equals(status)) {
			text.setText("" + array[7]);
		}else if ("-10".equals(status)) {
			text.setText("" + array[8]);
		}else if ("-1".equals(status)) {
			text.setText("" + array[9]);
		}
		try {
			int intStatus  = new Integer(status);
			return intStatus>0;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	*/
	
	
	/***
	  * 1,竞赛id @+id/sessionId
	  * 2,选中框 @+id/mToggleView
	  * 3,主队  @+id/homeNameText
	  * 4，客队 @+id/aWayNameText
	  * 5，胜   @+id/vectory
	  * 6，平 @+id/tie
	  * 7，负   @+id/lose
	  * 8，奖金  @+id/award
	  * 9，剩余时间 @+id/remainTime
	  * 10，日期   @+id/date
	  * 11，联队 @+id/league
	  * 
	  * 12，让分 @+id/handicap
	  * 13，半场 @+id/half
	  * 14，全场 @+id/audience
	  */
	
	//对数据分别填充，判断是否为足球或是篮球
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null || typeChanged) {
			if (type == 1) {
				convertView = mInflater.inflate(R.layout.score_onlive_list_item_soccer, null);
			}else if (type == 2) {
				convertView = mInflater.inflate(R.layout.score_onlive_list_item_basketball, null);
			}			
		}		

		if (type == 1) {
			Map<String, String> map = (Map<String, String>) getItem(position);
			if (map != null) {
				ToggleView toggle = (ToggleView) convertView.findViewById(R.id.mToggleView);
                String temp = (String) map.get("mToggleView");
                String selS = map.get("selectS");
                if ("1".equals(selS)) {
					toggle.setVisibility(View.VISIBLE);
				}else if ("2".equals(selS)) {
					toggle.setVisibility(View.GONE);
				}
                toggle.setOnClickListener(new ItemClick(position, convertView));
                if ("true".equals(temp)) {
					toggle.setOn(true);
				}else
				{
					toggle.setOn(false);
				}
                temp = (String) map.get("sessionId");
                TextView tempText = (TextView) convertView.findViewById(R.id.sessionId);
                tempText.setText("" + temp);
                
                temp = (String) map.get("homeNameText");
                tempText = (TextView) convertView.findViewById(R.id.homeNameText);
                tempText.setText("" + temp);
                
                
                temp = (String) map.get("aWayNameText");
                tempText = (TextView) convertView.findViewById(R.id.aWayNameText);
                tempText.setText("" + temp);
                
                
                
                temp = (String) map.get("date");
                tempText = (TextView) convertView.findViewById(R.id.date);
                tempText.setText("" + temp);
                
                temp = (String) map.get("league");
                tempText = (TextView) convertView.findViewById(R.id.league);
                tempText.setText("" + temp);
                
                temp = (String) map.get("status");
                tempText = (TextView) convertView.findViewById(R.id.remainTime);
                View resultView = convertView.findViewById(R.id.overResult);
                TextView lsc = (TextView) convertView.findViewById(R.id.lsc);
                //showStatus(status,tempText, temp, (String) map.get("date"), rspdt);//
             
                System.out.println("rspdt:");
                {
                	TextView goallineText = (TextView) convertView.findViewById(R.id.handicap);
                	boolean playing = statusAction.setStatus(tempText, status, temp, map);
                	if (!playing) {//比赛结束
                		goallineText.setVisibility(View.VISIBLE);
						lsc.setVisibility(View.GONE);
						resultView.setVisibility(View.VISIBLE);
						temp = (String) map.get("handicap");
		                if (temp != null && !"".equals(temp)) {
							temp = "(" + temp+")";
						}else
						{
							temp = "";
						}
						goallineText.setText(temp);
		                
		                
		                temp = (String) map.get("half");
		                tempText = (TextView) convertView.findViewById(R.id.half);
		                tempText.setText(getString(R.string.half)+"" + temp);
		                
		                temp = (String) map.get("audience");
		                tempText = (TextView) convertView.findViewById(R.id.audience);
		                tempText.setText(getString(R.string.audience)+"" + temp);
					}else
					{
						
						goallineText.setVisibility(View.GONE);
						lsc.setVisibility(View.VISIBLE);
						resultView.setVisibility(View.GONE);
						lsc.setText(map.get("lsc"));
						
					}
                }
                
                
             
				
			}
			
		}else if (type == 2) {
			Map<String, Object> map = (Map<String, Object>) getItem(position);
			if (map != null) {
				ToggleView toggle = (ToggleView) convertView.findViewById(R.id.mToggleView);
                String temp = (String) map.get("mToggleView");
                toggle.setOnClickListener(new ItemClick(position, convertView));
                if ("true".equals(temp)) {
					toggle.setOn(true);
				}else
				{
					toggle.setOn(false);
				}
                temp = (String) map.get("sessionId");
                TextView tempText = (TextView) convertView.findViewById(R.id.sessions);
                tempText.setText("" + temp);
                
                temp = (String) map.get("homeNameText");
                tempText = (TextView) convertView.findViewById(R.id.homeNameText);
                tempText.setText("" + temp);
                
                
                temp = (String) map.get("aWayNameText");
                tempText = (TextView) convertView.findViewById(R.id.aWayNameText);
                tempText.setText("" + temp);
                
                
                
                temp = (String) map.get("date");
                tempText = (TextView) convertView.findViewById(R.id.date);
                tempText.setText("" + temp);
                
                temp = (String) map.get("league");
                tempText = (TextView) convertView.findViewById(R.id.league);
                tempText.setText("" + temp);
                
                temp = (String) map.get("status");
                tempText = (TextView) convertView.findViewById(R.id.remainTime);
                View resultView = convertView.findViewById(R.id.overResult);
                View playingView = convertView.findViewById(R.id.playingResult);
                if (!"未开场".equals(temp)) {
					//tempText.setVisibility(View.GONE);
                	if ("进行中".equals(temp)) {
						tempText.setText("" + map.get("remainTime"));
					}
                	else 
                	{
                		tempText.setText("" +temp);//数字转换问题
                	}
					playingView.setVisibility(View.GONE);
					resultView.setVisibility(View.VISIBLE);
					
					temp = (String) map.get("first");
                    tempText = (TextView) convertView.findViewById(R.id.first);
                    tempText.setText("" + temp);
                    
                    
                    temp = (String) map.get("second");
                    tempText = (TextView) convertView.findViewById(R.id.second);
                    tempText.setText("" + temp);
                    
                    temp = (String) map.get("third");
                    tempText = (TextView) convertView.findViewById(R.id.third);
                    tempText.setText("" + temp);
                    
                    temp = (String) map.get("forth");
                    tempText = (TextView) convertView.findViewById(R.id.forth);
                    tempText.setText("" + temp);
                    
                    temp = (String) map.get("score");
                    tempText = (TextView) convertView.findViewById(R.id.score);
                    tempText.setText("" + temp);
                    
                    
				}else
				{
					tempText.setText(""+temp);
					playingView.setVisibility(View.VISIBLE);
					resultView.setVisibility(View.GONE);
					tempText.setVisibility(View.GONE);
					
					temp = (String) map.get("vectory");
                    tempText = (TextView) convertView.findViewById(R.id.vectory);
                    tempText.setText(getString(R.string.h)+"" + temp);
					
                    
                    
                    temp = (String) map.get("tie");
                    tempText = (TextView) convertView.findViewById(R.id.tie);
                    tempText.setText(getString(R.string.d)+"" + temp);
                    
                    temp = (String) map.get("lose");
                    tempText = (TextView) convertView.findViewById(R.id.lose);
                    tempText.setText(getString(R.string.a)+"" + temp);
                    
                    temp = (String) map.get("award");
                    tempText = (TextView) convertView.findViewById(R.id.award);
                    tempText.setText(getString(R.string.award)+"" + temp);
				}
				
			}
		}
		Map<String, Object> map = (Map<String, Object>) getItem(position);
		
		
		return convertView;
	}

}
