package cn.chaoren;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import cn.chaoren.adapter.InfoAdapter;
import cn.chaoren.net.HttpServiceNew;
import cn.chaoren.obj.Information;
import cn.chaoren.R;

import com.mobclick.android.MobclickAgent;

public class InformationListActivity extends Activity {
	private ArrayList<Information> mInfoList = new ArrayList<Information>();
	private InfoAdapter adapter;
	private ListView infoListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.infolist);   
        adapter = new InfoAdapter(this);
        infoListView = (ListView) findViewById(R.id.infoList);
        infoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SportteryMainActivity parent1 = (SportteryMainActivity) getParent();
				Bundle bundle = new Bundle();
				bundle.putParcelable("info", mInfoList.get(position));
		    	parent1.showChild("info", InfoDetailActivity.class,bundle);
				
			}
		});
        adapter.setInfoList(mInfoList);
		infoListView.setAdapter(adapter);
       new InfoGetter().execute("");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged();
		MobclickAgent.onResume(this);
	}
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
//	@Override
//	public void onBackPressed() {
//		
//		getParent().onBackPressed();
//		//super.onBackPressed();
//	}
	
	//用到AsyncTask中两个方法：doInBackground,onPostExecute
	private class InfoGetter extends AsyncTask<String, Integer, ArrayList<Information>>
	{
		@Override
		protected ArrayList<Information> doInBackground(String... params) {
		
			HttpServiceNew serviceNew = new HttpServiceNew();
			String id = null;
			if (params != null) {
				id = params[0];
			}
			return serviceNew.getInformationList(id);
		}
		@Override
		protected void onPostExecute(ArrayList<Information> result) {
			if (result !=  null ) {
				mInfoList.clear();
				mInfoList.addAll(result);
				adapter.notifyDataSetChanged();
			}
			super.onPostExecute(result);
			   
		}
	}
}
