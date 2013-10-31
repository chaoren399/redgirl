package cn.chaoren.mvc.activity;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.chaoren.mvc.adapter.MapListAdapter;
import cn.chaoren.mvc.model.LotteryInfo;
import cn.chaoren.mvc.model.RequestVo;
import cn.chaoren.mvc.model.SelectInfo;
import cn.chaoren.mvc.parsers.LotteryParser;
import cn.chaoren.mvc.util.CommonUtil;
import cn.chaoren.mvc.util.Constant;
import cn.chaoren.R;

import com.amap.mapapi.core.AMapException;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.geocoder.Geocoder;
import com.amap.mapapi.location.LocationManagerProxy;
public class MyActivity extends BaseActivity  implements LocationListener  {
	protected static final String TAG = "MyActivity";
	private String strDistance;
	private ListView list_shop;
	private  ArrayList<LotteryInfo> lotteryInfo  ;
	private LotteryInfo bk_lotteryInfo  ;
	private LocationManagerProxy locationManager = null;
	private TextView myLocation;
	private SelectInfo selectInfo=new SelectInfo();
	private String searchProvince=null;
	private String searchCity=null;
	private String bestProvider = "network";
	private Double geoLat = 1.00;
	private Double geoLng = 1.00;
	private ImageView  image_map;
	private RelativeLayout distanceLayout, shopLayout, privnceLayout;
	private TextView distanceText ;
	private ImageView distanceImageView, privnceImage, districtImageView;
	private PopupWindow mPopupWindow_distance;
	private RadioGroup radioDistance;
	private ViewGroup mContainer;
	private String adsLocation = null;
	private ImageView btnRelode;

	//private String  strProvince, strCity;
	ArrayAdapter<String> city_adapter;

	
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			/*if (msg.what==Constant.SUCCESS){
				myLocation.setText(msg.obj.toString());
				closeProgressDialog();
				processLogic();
				
			}else*/
			if(msg.what==4){
			 Log.i(TAG, "监听距离选择:"+strDistance);
				processLogic();
			}else if(msg.what==Constant.RELOAD){
				disableMyLocation();
				myLocation.setText(msg.obj.toString());
				LotteryInfo myl = new LotteryInfo();
				myl = (LotteryInfo) msg.obj;
				Double  lat= myl.lat;
				Double  lon = myl.lon;
				getEncodeLocate(myl);
				myLocation.setText(adsLocation);
				processLogic();
			}
		
		}

		
	};
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		enableMyLocation();
		
	};

	@Override
	protected void findViewById() {
		list_shop = (ListView) findViewById(R.id.lstLottery);
		lotteryInfo=null;
		myLocation = (TextView) findViewById(R.id.txtMyLocation);
		locationManager = LocationManagerProxy.getInstance(this);
		image_map= (ImageView) findViewById(R.id.btnAllLottery);
		mContainer = (ViewGroup) findViewById(R.id.layoutContainer);
		distanceText = (TextView) findViewById(R.id.txtDistance);
		distanceImageView = (ImageView) findViewById(R.id.btnDistance);
		districtImageView = (ImageView) findViewById(R.id.bnDistrict);
		distanceLayout = (RelativeLayout) findViewById(R.id.layoutDistance);
		shopLayout = (RelativeLayout) findViewById(R.id.layoutDistrict);
		btnRelode = (ImageView) findViewById(R.id.btnRelode);
	}

	@Override
	protected void setListener() {
		// 选择距离的点击事件
				distanceLayout.setOnClickListener(new View.OnClickListener() {// 点击显示不同的距离段
							@Override
							public void onClick(View v) {
								showPopupWindowDistance(v);
							}
						});
			
		/**
		 * listview 进行监听   所有店面的listview条目
		 */
		list_shop.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				bk_lotteryInfo=new LotteryInfo();
				list_shop.getCount();//用来传递条目
				bk_lotteryInfo=	(LotteryInfo) list_shop.getItemAtPosition(position);
				
				Intent	intent=new Intent(MyActivity.this, ItermMapActivity.class);
				Bundle bundle = new Bundle();//该类用作携带数据
				bundle.putDouble("geoLat", geoLat);
				bundle.putDouble("geoLng", geoLng);
				bundle.putDouble("lat", bk_lotteryInfo.lat);
				bundle.putDouble("lon",  bk_lotteryInfo.lon);
				bundle.putString("wdinfoNum", bk_lotteryInfo.wdinfoNum);
				bundle.putString("name", bk_lotteryInfo.address); //这是应该是店的名字，接口中没有
				bundle.putString("tel", bk_lotteryInfo.tel);
				bundle.putString("serviceScope", bk_lotteryInfo.serviceScope);
				bundle.putString("address", bk_lotteryInfo.address);
				intent.putExtras(bundle);//附带上额外的数据
				startActivity(intent);
			}
		});
		// 对所有地图页按钮的监听
		image_map.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MyActivity.this, AllMapActivity.class);
				intent.putExtra("lotteryInfo",lotteryInfo);//为意图追加
				  intent.putExtra("strDistance", strDistance);
				  LotteryInfo lst = new LotteryInfo();
						 lst.address = adsLocation;
						 lst.lat = geoLat;
						 lst.lon = geoLng;
						 if(lst!=null){
					intent.putExtra("mylocation", lst);
						 }
				startActivity(intent);
			}
		});
		// 重新定位按钮 
		btnRelode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				myLocation.setText("定位中……");
				enableMyLocation();
				
			}
		});
	}
	
	private void select(ListView listView_city, ArrayAdapter<String> adapter,
			String[] arry) {
		adapter = new ArrayAdapter<String>(this, R.layout.city_list_item,
				R.id.trade_title, arry);
		listView_city.setAdapter(adapter);
	}
	private void showPopupWindowDistance(final View parent) {// 为距离段填充

		if (null == mPopupWindow_distance) {
			LayoutInflater inflater = getLayoutInflater();
			View view = inflater.inflate(R.layout.pop_distance, null);
			radioDistance = (RadioGroup) view
					.findViewById(R.id.radiogroup_distance);
			// 获取屏幕宽度
			int width = mContainer.getWidth();
			// 创建一个PopuWidow对象
			mPopupWindow_distance = new PopupWindow(view, width, 150, true);
		}
		// 设置允许在外点击消失
		mPopupWindow_distance.setOutsideTouchable(true);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		mPopupWindow_distance.setBackgroundDrawable(new BitmapDrawable());
		// 相对某个控件的位置（正左下方），无偏移
		mPopupWindow_distance.showAsDropDown(findViewById(R.id.layoutTab), 0,
				0);
		mPopupWindow_distance.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				clickSelectFalseBackground(parent.getId());
			}
		});

		radioDistance.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				try {
					Message msg=new Message();
					msg.what=4;
					handler.sendMessage(msg);
					switch (checkedId) {
					case R.id.wubai_km:
						distanceText.setText("0.5km");
						strDistance = 500+"";
						mPopupWindow_distance.dismiss();
						break;
					case R.id.one_km:
						distanceText.setText("1km");
						strDistance = 1000+"";
						mPopupWindow_distance.dismiss();
						break;
					case R.id.two_km:
						distanceText.setText("2km");
						strDistance = 2000+"";
						mPopupWindow_distance.dismiss();
						break;
					case R.id.three_km:
						distanceText.setText("3km");
						strDistance = 3000+"";
						mPopupWindow_distance.dismiss();
						break;
					case R.id.five_km:
						distanceText.setText("5km");
						strDistance = 5000+"";
						mPopupWindow_distance.dismiss();
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void clickSelectFalseBackground(int vId) {
		switch (vId) {
		case R.id.filter_distance:
			distanceLayout.setBackgroundResource(R.drawable.btn_bg_map_filter);
			viewVistible(distanceImageView);
			break;
		case R.id.privnce_text:
			privnceLayout.setBackgroundResource(R.drawable.btn_bg_map_filter);
			viewVistible(privnceImage);
			break;
		case R.id.filter_district:
			shopLayout.setBackgroundResource(R.drawable.btn_bg_map_filter);
			viewVistible(districtImageView);
			break;
		}
	}
	/**
	 * 控制视图显示
	 */
	private void viewVistible(View v) {
		if (!v.isShown()) {
			v.setVisibility(View.VISIBLE);
		}
	}  

	@Override
	protected void processLogic() {
		Log.i(TAG, "seconde");
		RequestVo vo = new RequestVo();
		vo.requestUrl=R.string.app_host;
		vo.context=context;
		vo.jsonParser = new LotteryParser();
		selectInfo.city=searchCity;
		selectInfo.province=searchProvince;
		selectInfo.count=10;
		selectInfo.startLatitude=geoLat;
		selectInfo.startLongitude=geoLng;
		if(strDistance==null){
			strDistance=5000+"";
			Log.i(TAG, "strDistance0"+strDistance);
		}
		Log.i(TAG, "strDistance1"+strDistance);
		selectInfo.distance=strDistance;
		
		/*if(selectInfo==null){
			selectInfo.distance=strDistance;
			selectInfo.city=strCity;
			selectInfo.province=strProvince;
		}*/
		super.getDataFromServer(vo, callback,selectInfo);
	}
	   
	/**
	 * 回调类,把后面数据显示在View元素里
	 */
	
	private DataCallback  callback = new DataCallback< ArrayList<LotteryInfo>>() {

		@Override
		public void processData(ArrayList<LotteryInfo> paramObject,	boolean paramBoolean) {
			// TODO Auto-generated method stub
			lotteryInfo = paramObject;
			Log.i(TAG, "lotteryInfo"+lotteryInfo.toString());
			if(lotteryInfo.size()==0){
				CommonUtil.showInfoDialog(MyActivity.this, "附近没有您要找的店");
			
			}
			list_shop.setAdapter( new MapListAdapter(getApplicationContext(), lotteryInfo));
		}
	};

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.map_pattern);
	}
	
	/**
	 * 定位
	 */
	public boolean enableMyLocation() {
		showLocateDialog();
		boolean result = true;
		Criteria cri = new Criteria();
		cri.setAccuracy(Criteria.ACCURACY_COARSE);
		cri.setAltitudeRequired(false);
		cri.setBearingRequired(false);
		cri.setCostAllowed(false);
	/*for (final String bestProvider :  locationManager.getProviders(true)) {
		Log.i(TAG,"bestProvider"+bestProvider);
		locationManager.requestLocationUpdates(bestProvider, 200000, 100, this);
	}*/
		bestProvider = locationManager.getBestProvider(cri, true);
		locationManager.requestLocationUpdates(bestProvider, 20000000, 100, this);
		 
		Log.i(TAG, "first");
		return result;
	}
	public void disableMyLocation() {
		closeLocateDialog();
		locationManager.removeUpdates(this);
		
	}

	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			 geoLat = location.getLatitude();
			geoLng = location.getLongitude();
			LotteryInfo myl = new LotteryInfo();
			myl.lat = geoLat;
			myl.lon = geoLng;
			String str = ("定位成功:(" + geoLng + "," + geoLat + ")");
			Log.i(TAG,"定位成功"+ str);
			
		
			Message msg = new Message();
			msg.what=Constant.RELOAD;
			msg.obj = myl;
			if (handler != null) {
				handler.sendMessage(msg);
				
			}
		} 
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	
	private void getEncodeLocate( LotteryInfo lotteryInfo) {
		// TODO Auto-generated method stub
		GeoPoint geo = new GeoPoint((int) (lotteryInfo.lat * 1E6),
				(int) (lotteryInfo.lon * 1E6));
		/*try {*/
			if (geo.toString() != "") {
				Geocoder mGeocoder01 = null ;
						mGeocoder01	=new Geocoder(MyActivity.this);
				int x = geo.getLatitudeE6(); // 得到geo 纬度，单位微度(度* 1E6)
				double x1 = ((double) x) / 1000000;
				int y = geo.getLongitudeE6(); // 得到geo 经度，单位微度(度* 1E6)
				double y1 = ((double) y) / 1000000;
				// 得到逆理编码，参数分别为：纬度，经度，最大结果集
				List<Address> lstAddress1= null;
				lstAddress1 =	new ArrayList<Address>();
				  //这里为什么会有空指针？
				Log.i(TAG, "x1"+x1+"y1"+y1);
				 try{	lstAddress1= mGeocoder01.getFromRawGpsLocation(x1, y1, 3);
				 } catch( AMapException e){
					 e.printStackTrace();
					 CommonUtil.showInfoDialog(MyActivity.this, "定位错误，请重试");
						Toast.makeText(getApplicationContext(), "连接错误！",
								Toast.LENGTH_SHORT).show();
				 }
					 adsLocation=	lstAddress1.get(0).getAddressLine(1)+lstAddress1.get(0).getAddressLine(2);
						//String  provic= ((Address) lstAddress1).getCountryName() + ((Address) lstAddress1).getAdminArea() + ((Address) lstAddress1).getFeatureName();
						searchProvince= lstAddress1.get(0).getAdminArea();
						searchCity= lstAddress1.get(0).getLocality();
						 Log.i(TAG, "provic"+ lstAddress1.get(0).getAdminArea()+ lstAddress1.get(0).getLocality());
						 Log.i(TAG, "adsLocation:"+adsLocation);
				
			}else{
				closeDialog();
				CommonUtil.showInfoDialog(MyActivity.this, "定位失败");
			}
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	/**
	 * 提示框
	 */
	public void showdialog(){
		super.showProgressDialog();
	}
	
	public void closeDialog(){
		super.closeProgressDialog();
	}
	/**
	 * 开启正在定位提示框
	 */
	public void showLocateDialog(){
		if ((!isFinishing()) && (this.progressDialog == null)) {
			this.progressDialog = new ProgressDialog(this);
		}
		this.progressDialog.setTitle(getString(R.string.locateloading));
		this.progressDialog.setMessage(getString(R.string.LoadContent));
		this.progressDialog.show();
		//this.progressDialog.setCancelable(false);
		
	}
	/**
	 * 关闭正在定位提示框
	 */
	protected void closeLocateDialog() {
		if (this.progressDialog != null)
			this.progressDialog.dismiss();
	}
}
