package cn.chaoren;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import cn.chaoren.action.SelectAlertDialog;
import cn.chaoren.widget.SelectItem;
import cn.chaoren.widget.SelectorLayout;
import cn.chaoren.R;

import com.mobclick.android.MobclickAgent;

public class SettingActivity extends Activity {
	private int type = 1;//1,足球；2，篮球
	private SelectorLayout totalGoalGridView;
	private SelectorLayout scoreSelectorLayout;
	private SelectorLayout alertTypeSelector;
	private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	setContentView(R.layout.setting);
    	type = getIntent().getIntExtra("type", type);
    	init();
    }
    
    public static class SettingObject
    {
    	public String[]scores;
    	public String totalScore ;
    	public String alertType;
    	public Context context;
    	private ArrayList<String> scoresList;
    	private ArrayList<String> tempList;
    	private boolean hasHmore;
    	private boolean hasDmore;
    	private boolean hasAmore;
        public int type = 0;
    	private ArrayList<String> allList;
    	public static SettingObject  getInstance(Context con)//暂时不考虑 篮球
    	{
    		
    		SharedPreferences sharedPreferences = con.getSharedPreferences(con.getString(R.string.setting_shared_name_xml), MODE_PRIVATE);
    		SettingObject obj = new SettingObject();
    		obj.context = con;
    		obj.alertType = sharedPreferences.getString(con.getString(R.string.setting_socer_alert_type_xml), "0");
    		obj.totalScore = sharedPreferences.getString(con.getString(R.string.setting_socer_total_goal_xml), "");
    		String value = sharedPreferences.getString(con.getString(R.string.setting_socer_score_xml), "");
    		obj.scores = value.split(""+con.getString(R.string.setting_seperator));
    		
    		return obj;
    	}
    	
    	public int getAlertType()
    	{
    		/*if (type != 0) {
				return type;
			}*/
    		type = 0;
    		String[] types = context.getResources().getStringArray(R.array.tint_type);
    		if (types[0].equalsIgnoreCase(alertType)) {
    			type = 0;
				
			}else if (types[1].equalsIgnoreCase(alertType)) {
				type = 1;
			}else if (types[2].equalsIgnoreCase(alertType)) {
				type =  2;
			}
    		
    		return type;
    	}
    	
    	public int win(String score)
    	{
    		String[]sA = score.split(":");
    		int h = 0;
    		int a = 0;
    		try {
				h = new Integer(sA[0]);
				a = new Integer(sA[1]);
				if (h>a) {
					return 1;
				}else if(h ==a)
				{
					return 0;
				}else
				{
					return -1;
				}
			} catch (Exception e) {
				return -2;
			}
    	}
    	
    	/**
    	 * @param score
    	 * @return
    	 */
    	@SuppressWarnings("unchecked")
		public boolean contains(String score)
    	{
    		if (score==null||scores == null||scores.length==0) {
				return false;
			}
    		int type = win(score);
    		if (type == -2) {
				return false;
			}
    		if (scoresList == null) {
				scoresList = new ArrayList<String>();
				if (scores != null) {
					int length = scores.length;
					for (int i = 0; i < length; i++) {
						scoresList.add(scores[i]);
					}
					
					
				}
				
				tempList = (ArrayList<String>) scoresList.clone();
				if (tempList.contains(context.getString(R.string.othersWin))) {
					hasHmore = true;
					tempList.remove(context.getString(R.string.othersWin));
				}else if (tempList.contains(context.getString(R.string.othersDraw))) {
					hasDmore = true;
					tempList.remove(context.getString(R.string.othersDraw));
				}else if (tempList.contains(context.getString(R.string.othersLose))) {
					hasAmore = true;
					tempList.remove(context.getString(R.string.othersLose));
				}
			}
    		if (tempList.contains(score)) {
				return true;
			}
    		else
			{
				if (allList == null) {
					allList = new ArrayList<String>();
				    String []array = context.getResources().getStringArray(R.array.score);
				    for (int i = 0; i < array.length; i++) {
						allList.add(array[i]);
					}				    
				    allList.remove(context.getString(R.string.othersWin));
				    allList.remove(context.getString(R.string.othersDraw));
				    allList.remove(context.getString(R.string.othersLose));
				}
				
				if (allList.contains(score)) {
					return false;
				}
				else
				{
					if (type == 1) {
						return hasHmore;
					}else if(type == -1)
					{
						return hasAmore;
					}else if (type == 0) {
						return hasDmore;
					}
				}
			}
    		
    		
    		
    		return false;
    	}
    	
    	public void reset()
    	{
    		SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.setting_shared_name_xml), MODE_PRIVATE);
    		
    		
    		alertType = sharedPreferences.getString(context.getString(R.string.setting_socer_alert_type_xml), null);
    		totalScore = sharedPreferences.getString(context.getString(R.string.setting_socer_total_goal_xml), null);
    		String value = sharedPreferences.getString(context.getString(R.string.setting_socer_score_xml), null);
    		scores = value.split(""+context.getString(R.string.setting_seperator));
    		scoresList = null;
    		tempList = null;
    		hasAmore = false;
    		hasDmore = false;
    		hasHmore = false;
    	}
    	
    	
    	/**
    	 * @param score 0:1
    	 * @return
    	 */
    	public boolean rightTotal(String score)//s
    	{
    		if (totalScore == null) {
				return false;
			}
    		
    		int selectCount = 0;
    		boolean moreThan = false;
            if (totalScore.contains("+")) {
				selectCount = 7;
				moreThan = true;
			}else
			{
				try {
					selectCount = new Integer(totalScore);
					
				} catch (Exception e) {
					
					return false;
				}
			}
    		if (score!=null) {
				String[]scores = score.split(":");
				if (scores!=null&&scores.length==2) {
					int cout1 = 0;
					int cout2 = 0;
					try {
						cout1 = new Integer(scores[0]);
						cout2 = new Integer(scores[1]);
						if (moreThan) {
							return (cout1+cout2)>=selectCount;
						}else
						{
							return cout1+cout2==selectCount;
						}
					} catch (Exception e) {
						return false;
					}
				}
			}
    		return false;
    	}
    	
    }
    private void init()
    {
    	sharedPreferences = getSharedPreferences(getString(R.string.setting_shared_name_xml), MODE_PRIVATE);
		alertTypeSelector = (SelectorLayout) findViewById(R.id.alerTypeLinearLayout);
		
    	if (type == 1) {
			View temp = findViewById(R.id.totalGoalLayout);
			temp.setVisibility(View.VISIBLE);
			temp = findViewById(R.id.scoreLayout);
			temp.setVisibility(View.VISIBLE);
			scoreSelectorLayout = (SelectorLayout) findViewById(R.id.scoreGridView);
			totalGoalGridView = (SelectorLayout) findViewById(R.id.totalGoalGridView);
			totalGoalGridView.setCountPerLine(4);//表示每行显示多少
			totalGoalGridView.setMode(1);//1,表示单选，2，表示多选；0，表示没有选中效果
			getTotalGoal();
			
			scoreSelectorLayout.setCountPerLine(3);
			scoreSelectorLayout.setMode(2);//1,表示单选，2，表示多选；0，表示没有选中效果
			getScore();
		}
    	alertTypeSelector.setCountPerLine(1);
    	alertTypeSelector.setMode(1);
    	getAlertType();
    	
    }
    
    public void getScore()
    {
    	String[]scores=getResources().getStringArray(R.array.score);
    	int length = scores.length;
    	for (int i = 0; i < length; i++) {
    		scoreSelectorLayout.addItem(SelectItem.getInstance(this).getCheck(""+ scores[i]));
		}
    	String value = sharedPreferences.getString(getString(R.string.setting_socer_score_xml), null);
    	if (value != null) {
        	scoreSelectorLayout.setSelecteds(value.split(""+getString(R.string.setting_seperator)));
		}
    }
    
    public void getAlertType()
    {
    	String[]alertTypes=getResources().getStringArray(R.array.tint_type);
    	int length = alertTypes.length;
    	for (int i = 0; i < length; i++) {
			alertTypeSelector.addItem(SelectItem.getInstance(this).getRadio("" + alertTypes[i]));
		}
    	String alertString = getString(R.string.setting_socer_alert_type_xml);
    	if (type ==2) {
    		alertString = getString(R.string.setting_basketball_alert_type_xml);
		}
    	alertTypeSelector.setSelected(sharedPreferences.getString(alertString, null));
    }
    
    public void getTotalGoal()
    {
    	String[]totals = getResources().getStringArray(R.array.total_selections);
    	for (int i = 0; i <9; i++) {
    		totalGoalGridView.addItem(SelectItem.getInstance(this).getRadio(""+ totals[i]));
		}
    	
    	totalGoalGridView.setSelected(sharedPreferences.getString(getString(R.string.setting_socer_total_goal_xml), null));
    }
    
    private void onSave()
    {
    	Editor editor = sharedPreferences.edit();
    	if (type == 1) {
        /*	System.out.println("totalGoalGridView#" + totalGoalGridView.getSingleValue());
        	String[]values =scoreSelectorLayout.getSelectedValues();
        	
        	int length = values.length;
        	for (int i = 0; i < length; i++) {
        		System.out.println("scores:" + values[i]);
    		}*/
        	
    		editor.putString(getString(R.string.setting_socer_total_goal_xml), totalGoalGridView.getSingleValue());
        	editor.putString(getString(R.string.setting_socer_alert_type_xml), alertTypeSelector.getSingleValue());
        	editor.putString(getString(R.string.setting_socer_score_xml), arrayToString(scoreSelectorLayout.getSelectedValues()));
		}else
		{
        	editor.putString(getString(R.string.setting_basketball_alert_type_xml), alertTypeSelector.getSingleValue());
		}
    	
    	editor.commit();
    	/*System.out.println("alert type:" + alertTypeSelector.getSingleValue());*/
    	
    }
    
    
    private String arrayToString(String[]str)
    {
    	if (str== null || str.length == 0) {
			return "";
		}
    	StringBuilder buidler = new StringBuilder();
    	int length = str.length;
    	String seperator = getString(R.string.setting_seperator);
    	buidler.append("" + str[0]);
    	for (int i = 1; i <length; i++) {
			buidler.append(""+seperator+str[i]);
		}
    	return buidler.toString();
    }
    
    @Override
    protected void onStop() {
    	System.out.println("stop");
    	super.onStop();
    }
    
    @Override
    public void onBackPressed() {
    	SelectAlertDialog.showAlertDialog(this, getString(R.string.save_confirm_title), getString(R.string.save_confirm), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == DialogInterface.BUTTON_POSITIVE) {
                      onSave();
				}
				dialog.dismiss();
				finish();
			}
		});
    	
    	System.out.println("back");
    
    }
    
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	MobclickAgent.onResume(this);
    }
    
    @Override
    protected void onPause() {
    	System.out.println("pause");
    	super.onPause();
    	MobclickAgent.onPause(this);
    }
    
    @Override
    protected void onDestroy() {
    	System.out.println("destory");
    	super.onDestroy();
    }
}
