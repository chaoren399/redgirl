package cn.chaoren;

import java.util.HashMap;
import java.util.Map;

import cn.chaoren.R;

import com.mobclick.android.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ScoreLiveBasketActivity extends Activity implements View.OnClickListener{
	private Map<String, Object> data = null;
	private TextView firstText,secondText,thirdText,forthText,statusText,scoreText,remainText,currentText,rewardText;
	private View messageLayout;
	private String status;
	private boolean gameOver = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		data = (HashMap<String, Object>) getIntent().getSerializableExtra("map"); 
        if (data == null) {
			onBackPressed();
		}
        setContentView(R.layout.score_onlive_basketball);
        firstText = (TextView) findViewById(R.id.first);
        secondText = (TextView) findViewById(R.id.second);
        thirdText = (TextView) findViewById(R.id.third);
        forthText = (TextView) findViewById(R.id.forth);
        statusText = (TextView) findViewById(R.id.status);
        scoreText = (TextView) findViewById(R.id.scoreText);
        firstText.setText(""+ getMap(data, "first", "0:0"));
        secondText.setText(""+ getMap(data, "second", "0:0"));
        thirdText.setText(""+ getMap(data, "third", "0:0"));
        forthText.setText(""+ getMap(data, "forth", "0:0"));
        statusText.setText(""+ getMap(data, "status", "未开赛"));
        scoreText.setText(""+ getMap(data, "score", "0:0"));
        messageLayout = findViewById(R.id.messageLayout);
        status = getMap(data, "status", "未开赛");
        rewardText = (TextView) findViewById(R.id.reward);
        currentText = (TextView) findViewById(R.id.currentText);
        remainText = (TextView) findViewById(R.id.remainTimeText);
        rewardText.setVisibility(View.GONE);
        if ("已完场".equals(status)||"已派奖".equals(status)) {
        	if ("已派奖".equals(status)) {
				rewardText.setVisibility(View.VISIBLE);
			}
			gameOver(true);
		}else 
		{
			currentText.setText("" + getMap(data, "current", "第二节"));
			remainText.setText("" + getMap(data, "remainTime", "0'"));
			gameOver(false);
		}
	}
	
	private void gameOver(boolean over)
	{
		gameOver = over;
		if (gameOver) {
			messageLayout.setVisibility(View.GONE);
		}
		
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
		MobclickAgent.onPause(this);
	}
	
	public String getMap(Map<String, Object> map,String key,String def)
	{
		String result = def;
		if (map != null && map.containsKey(key)) {
			Object obj = (String) map.get(key);
			if (obj != null) {
				result = (String) obj;				
			}
		}
		return result;
	}
	
	public void onClick(View view)
	{
		switch (view.getId()) {
		case R.id.reward:
			Intent intent = new Intent(this,
					LanqiuResultDetailActivity.class);
			intent.putExtra("date", data.get("date")+"");
			intent.putExtra("num", data.get("sessionId")+"");
			intent.putExtra("m_id", data.get("m_id")+"");
			intent.putExtra("l_cn", data.get("league")+"");
			intent.putExtra("h_cn", data.get("homeNameText")+"");
			intent.putExtra("a_cn", data.get("aWayNameText")+"");
			startActivity(intent);
			System.out.println("ook");//FIXME TURN TO REWARD PAGE
			break;

		default:
			break;
		}
	}
}
