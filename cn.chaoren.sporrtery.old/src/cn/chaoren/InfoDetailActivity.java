package cn.chaoren;

import com.mobclick.android.MobclickAgent;

import cn.chaoren.obj.Information;
import cn.chaoren.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

public class InfoDetailActivity extends Activity {
	private Information info;
	private SportteryMainActivity parent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.info_detail);
    	info = getIntent().getExtras().getParcelable("info");
    	init();
    }
    
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
    	MobclickAgent.onResume(this);
    }
    
    private void init()
    {
    	parent = (SportteryMainActivity) getParent();
    	TextView text = (TextView) findViewById(R.id.title);
    	text.setText("" + info.title);
    	text = (TextView) findViewById(R.id.date);
    	text.setText("" + info.date);
    	text = (TextView) findViewById(R.id.prefile);
    	text.setText("" + info.prefile);
    }
    
   
    
    @Override
    public void onBackPressed() {
    	parent.showChild("infolist", InformationListActivity.class);
    	//super.onBackPressed();
    	
    }
}
