package cn.chaoren.mvc.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import cn.chaoren.mvc.model.LotteryInfo;
import cn.chaoren.mvc.model.RequestVo;
import cn.chaoren.mvc.model.SelectInfo;
import cn.chaoren.mvc.parsers.LotteryParser;
import cn.chaoren.mvc.util.CommonUtil;
import cn.chaoren.mvc.util.Constant;
import cn.chaoren.mvc.util.NetUtil;
import cn.chaoren.mvc.util.ThreadPoolManager;
import cn.chaoren.R;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.OverlayItem;
import com.amap.mapapi.map.ItemizedOverlay;
import com.amap.mapapi.map.MapActivity;
import com.amap.mapapi.map.MapController;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.Projection;

public class AllMapActivity extends MapActivity {
	private static final String TAG = "AllMapActivity";
	private ArrayList<LotteryInfo> lotteryInfo;
	private TextView tv_num;
	private MapView mMapView;

	private GeoPoint point;
	private int num;
	private RelativeLayout distanceLayout, shopLayout, privnceLayout;
	private TextView distanceText, privnceText, shopText;
	private ImageView distanceImageView, privnceImage, districtImageView;
	private PopupWindow mPopupWindow_distance, mPopupWindow_dianmian,
			mPopupWindow_privnce;
	private RadioGroup radioDistance;
	private ViewGroup mContainer;
	private ListView listView_privnce, listView_city;
	private String strProvince, strCity;
	private String[] privnceArr = null;
	private String[] cityArr = null;
	ArrayAdapter<String> city_adapter;

	private int[] city = { R.array.beijin_province_item,
			R.array.tianjin_province_item, R.array.heibei_province_item,
			R.array.shanxi1_province_item, R.array.neimenggu_province_item,
			R.array.liaoning_province_item, R.array.jilin_province_item,
			R.array.heilongjiang_province_item, R.array.shanghai_province_item,
			R.array.jiangsu_province_item, R.array.zhejiang_province_item,
			R.array.anhui_province_item, R.array.fujian_province_item,
			R.array.jiangxi_province_item, R.array.shandong_province_item,
			R.array.henan_province_item, R.array.hubei_province_item,
			R.array.hunan_province_item, R.array.guangdong_province_item,
			R.array.guangxi_province_item, R.array.hainan_province_item,
			R.array.chongqing_province_item, R.array.sichuan_province_item,
			R.array.guizhou_province_item, R.array.yunnan_province_item,
			R.array.xizang_province_item, R.array.shanxi2_province_item,
			R.array.gansu_province_item, R.array.qinghai_province_item,
			R.array.linxia_province_item, R.array.xinjiang_province_item,
			R.array.hongkong_province_item, R.array.aomen_province_item,
			R.array.taiwan_province_item };
	private TextView distance_text;
	private String strDistance;
	private SelectInfo selectInfo;

	private ProgressDialog progressDialog;
	public List<OverlayItem> GeoList = new ArrayList<OverlayItem>();
	private ThreadPoolManager threadPoolManager;
	private ImageView btnLocate;
	private LotteryInfo mylocation;
	private ImageView btnBack;
	private 	String distance= 1000+"";

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			closeProgressDialog();
			if (msg.what == Constant.SUCCESS) {
				if (msg.obj == null) {
					CommonUtil.showInfoDialog(getApplicationContext(),
							getString(R.string.net_error));
				} else {
					ArrayList<LotteryInfo> loterryInfoList = (ArrayList<LotteryInfo>) msg.obj;
					if (loterryInfoList.size()!=0) {

						tv_num.setText("为您找到" + loterryInfoList.size() + "店");
						Log.i(TAG, "服务器返回的数据" + loterryInfoList.size()
								+ loterryInfoList.toString());
						GeoList = getGeoList(loterryInfoList);
						Log.i(TAG, "come the all  geolist" + GeoList.size());
						MapController mMapController = mMapView.getController();
						point = new GeoPoint(
								(int) (loterryInfoList.get(0).lat * 1E6),
								(int) (loterryInfoList.get(0).lon * 1E6)); // 用给定的经纬度构造一个GeoPoint，单位是微度(度*
																			// 1E6)
						Log.i(TAG,
								"loterryInfoList : "+ loterryInfoList.get(0).lat + "lng"
										+ loterryInfoList.get(0).lat);
						mMapController.setCenter(point); // 设置地图中心点
						Drawable marker = getResources().getDrawable(
								R.drawable.icon); // 得到需要标在地图上的资源
						marker.setBounds(0, 0, marker.getIntrinsicWidth(),
								marker.getIntrinsicHeight()); // 为maker定义位置和边界
						mMapView.getOverlays().add(
								new OverItemT(marker, AllMapActivity.this,
										loterryInfoList)); // 添加ItemizedOverlay实例到mMapView
					} else {
						tv_num.setText("为您找到" + 0 + "店");
					}
				}
			} else if(msg.what == Constant.NEAR ){  
				// 处理周围的体彩店
				if (msg.obj == null) {
					CommonUtil.showInfoDialog(getApplicationContext(),
							getString(R.string.net_error));
				} else {
					ArrayList<LotteryInfo> loterryInfoList = (ArrayList<LotteryInfo>) msg.obj;
					if (loterryInfoList .size()!=0) {

						tv_num.setText("为您找到" + loterryInfoList.size() + "店");
						Log.i(TAG, "服务器返回的数据" + loterryInfoList.size()
								+ loterryInfoList.toString());
						GeoList = getGeoList(loterryInfoList);
						Log.i(TAG, "come the all  geolist" + GeoList.size());
						MapController mMapController = mMapView.getController();
						point = new GeoPoint(
								(int) (loterryInfoList.get(0).lat * 1E6),
								(int) (loterryInfoList.get(0).lon * 1E6)); // 用给定的经纬度构造一个GeoPoint，单位是微度(度*
																			// 1E6)
						Log.i(TAG,
								"loterryInfoList : "+ loterryInfoList.get(0).lat + "lng"
										+ loterryInfoList.get(0).lat);
						mMapController.setCenter(point); // 设置地图中心点
						Drawable marker = getResources().getDrawable(
								R.drawable.icon); // 得到需要标在地图上的资源
						marker.setBounds(0, 0, marker.getIntrinsicWidth(),
								marker.getIntrinsicHeight()); // 为maker定义位置和边界
						mMapView.getOverlays().add(
								new OverItemT(marker, AllMapActivity.this,
										loterryInfoList)); // 添加ItemizedOverlay实例到mMapView
					} else {
						tv_num.setText("为您找到" + 0 + "店");
					}
					}
				
			}else if (msg.what == Constant.NET_FAILED) {
				CommonUtil.showInfoDialog(getApplicationContext(),
						getString(R.string.net_error));
			} else if(msg.what == Constant.INIT ){
				 ArrayList<LotteryInfo> lo = new ArrayList<LotteryInfo>();
				   lo = (ArrayList<LotteryInfo>) msg.obj;
				if (lo != null) {
					num = lo.size();
					tv_num.setText("为您找到" + num + "店");
					mMapView.setBuiltInZoomControls(true);
					MapController mMapController = mMapView.getController(); // 得到mMapView
																				// 的控制权,可以用它控制和驱动平移和缩放
					point = new GeoPoint((int) (lo.get(0).lat * 1E6),
							(int) (lo.get(0).lon * 1E6)); // 用给定的经纬度构造一个GeoPoint，单位是微度(度*
																	// 1E6)
					Log.i(TAG,
							"lat : " + lo.get(0).lat + "lng"
									+ lo.get(0).lat);
					mMapController.setCenter(point); // 设置地图中心点
					mMapController.setZoom(12); // 设置地图zoom 级别
					Drawable marker = getResources().getDrawable(R.drawable.icon);
					marker.setBounds(0, 0, marker.getIntrinsicWidth(),
							marker.getIntrinsicHeight());
					mMapView.getOverlays().add(
							new OverItemT(marker, AllMapActivity.this, lo));

				} else {
					num = 0;
					tv_num.setText("为您找到" + num + "店");
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		threadPoolManager.getInstance();
		setContentView(R.layout.map_all_view);
		Bundle bundle = this.getIntent().getExtras();
		strDistance = (String) bundle.get("strDistance");
		lotteryInfo = (ArrayList<LotteryInfo>) bundle
				.getSerializable("lotteryInfo");
		mylocation = (LotteryInfo) bundle.get("mylocation");
		Log.i(TAG, "first");
		privnceArr = getResources().getStringArray(R.array.province_item);
		findViewById();
		init();
		// Log.i(TAG, "lotteryInfo"+lotteryInfo.size()+lotteryInfo);
	}

	private void findViewById() {
		Log.i(TAG, "second");
		tv_num = (TextView) findViewById(R.id.txt_num);
		mMapView = (MapView) findViewById(R.id.mapallview);
		mContainer = (ViewGroup) findViewById(R.id.container);
		distanceText = (TextView) findViewById(R.id.distance_text);
		shopText = (TextView) findViewById(R.id.district_text);
		privnceText = (TextView) findViewById(R.id.privnce_text);

		distanceImageView = (ImageView) findViewById(R.id.distance_image);
		privnceImage = (ImageView) findViewById(R.id.privnce_image);
		districtImageView = (ImageView) findViewById(R.id.district_image);

		distanceLayout = (RelativeLayout) findViewById(R.id.filter_distance);
		shopLayout = (RelativeLayout) findViewById(R.id.filter_district);
		privnceLayout = (RelativeLayout) findViewById(R.id.privnce_type);
		distance_text = (TextView) findViewById(R.id.distance_text);
		btnLocate = (ImageView) findViewById(R.id.btnLocate);
		btnBack = (ImageView) findViewById(R.id.btnBack);
	}

	private void init() {
		Message  msg= new Message();
		msg.what = Constant.INIT;
		msg.obj = lotteryInfo;
		handler.sendMessage(msg);

		// 选择省市的点击事件
		privnceLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mMapView.getOverlays().clear();
				GeoList.clear();
				showDistrict(v);
			}
		});
		
		  distanceLayout.setOnClickListener(new View.OnClickListener() {//点击显示不同的距离段
		 
		  @Override public void onClick(View v) { showPopupWindowDistance(v);
		  /**/
		  mMapView.getOverlays().clear();
			GeoList.clear();
		  showPopupWindowDistance(v);
		  }
		  });
		 
		distance_text.setText(strDistance + "米");
		shopLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDianmian(v);
			}
		});
 /**
  * 定位键点击事件
  */
		btnLocate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MapController mMapController = mMapView.getController();
				if (mylocation != null
						&& (mylocation.lat != 1.00 || mylocation.lon != 1.00)) {
					ArrayList<LotteryInfo> mloterryInfoList = new ArrayList<LotteryInfo>();
					mloterryInfoList.add(mylocation);
					GeoPoint mPoint = new GeoPoint((int) (mloterryInfoList
							.get(0).lat * 1E6),
							(int) (mloterryInfoList.get(0).lon * 1E6));
					mMapController.setCenter(mPoint); // 设置地图中心点
					Drawable marker = getResources().getDrawable(
							R.drawable.myimg); // 得到需要标在地图上的资源
					marker.setBounds(0, 0, marker.getIntrinsicWidth(),
							marker.getIntrinsicHeight()); // 为maker定义位置和边界
					mMapView.getOverlays().add(
							new OverItemT(marker, AllMapActivity.this,
									mloterryInfoList)); // 添加ItemizedOverlay实例到mMapView
				}

			}
		});
 /**
  * 返回键
  */
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}

	/**
	 * 某个类型的覆盖物，包含多个类型相同、显示方式相同、处理方式相同的项时，使用此类.
	 */
	class OverItemT extends ItemizedOverlay<OverlayItem> {

		private Drawable marker;
		private Context mContext;
		private ArrayList<LotteryInfo> lotteryInfo;

		public OverItemT(Drawable marker, Context context,
				ArrayList<LotteryInfo> lotteryInfo) {
			super(boundCenterBottom(marker));
			this.marker = marker;
			this.mContext = context;
			this.lotteryInfo = lotteryInfo;

			GeoList = getGeoList(lotteryInfo);
			Log.i(TAG, "GeoList" + GeoList.size() + GeoList.toString());
			populate(); // createItem(int)方法构造item。一旦有了数据，在调用其它方法前，首先调用这个方法
		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			// Projection 接口用于屏幕像素点坐标系统和地球表面经纬度点坐标系统之间的变换
			Projection projection = mapView.getProjection();
			for (int index = size() - 1; index >= 0; index--) { // 遍历GeoList
				Log.i(TAG, "geosize" + size());
				Log.i(TAG, "index" + index);
				OverlayItem overLayItem = getItem(index); // 得到给定索引的item
				String title = overLayItem.getTitle();
				// 把经纬度变换到相对于MapView 左上角的屏幕像素坐标
				Point point = projection.toPixels(overLayItem.getPoint(), null);
				// 可在此处添加您的绘制代码
				Paint paintText = new Paint();
				paintText.setColor(Color.BLACK);
				paintText.setTextSize(15);
				canvas.drawText(title, point.x - 30, point.y - 25, paintText); // 绘制文本
			}
			super.draw(canvas, mapView, shadow);
			// 调整一个drawable 边界，使得（0，0）是这个drawable 底部最后一行中心的一个像素
			boundCenterBottom(marker);
		}

		@Override
		protected OverlayItem createItem(int i) {
			// TODO Auto-generated method stub
			return GeoList.get(i);
		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return GeoList.size();
		}

		@Override
		// 处理当点击事件
		protected boolean onTap(int i) {
			setFocus(GeoList.get(i));
			Toast.makeText(this.mContext, GeoList.get(i).getSnippet(),
					Toast.LENGTH_SHORT).show();
			return true;
		}

		@Override
		public boolean onTap(GeoPoint point, MapView mapView) {
			// TODO Auto-generated method stub
			return super.onTap(point, mapView);
		}
	}

	private void showDistrict(final View vv) {

		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.pop_type, null);

		listView_privnce = (ListView) view.findViewById(R.id.list_type1);
		listView_city = (ListView) view.findViewById(R.id.list_type2);
		// 为省市设置填充数据
		ArrayAdapter<String> privnce_adapter = new ArrayAdapter<String>(this,
				R.layout.privnce_list_item, R.id.districts_title, privnceArr);
		listView_privnce.setAdapter(privnce_adapter);
		int width = mContainer.getWidth();
		int height = mContainer.getHeight();
		mPopupWindow_privnce = new PopupWindow(view, width, height, true);

		mPopupWindow_privnce.setOutsideTouchable(true);
		mPopupWindow_privnce.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow_privnce.setFocusable(true);
		mPopupWindow_privnce.update();
		mPopupWindow_privnce
				.showAsDropDown(findViewById(R.id.filter_tab), 0, 0);
		mPopupWindow_privnce.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				clickSelectFalseBackground(vv.getId());
			}
		});

		// 针对每个省的点击事件
		listView_privnce.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				strProvince = privnceArr[position];
				int cityNumber = city[position];
				cityArr = getResources().getStringArray(cityNumber);

				select(listView_city, city_adapter, cityArr);

				listView_city.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						strCity = cityArr[position];
						selectInfo = new SelectInfo();
						selectInfo.city = strCity;
						selectInfo.province = strProvince;
						Log.i(TAG, "strCity+strProvince" + strCity
								+ strProvince);
						mPopupWindow_privnce.dismiss();
						if (strCity.equals(strProvince)) {
							privnceText.setText(strProvince);
						} else {
							privnceText.setText(strProvince + strCity);
						}
						// strProvince, strCity;
						// 启动子线程
						RequestVo vo = new RequestVo();
						vo.requestUrl = R.string.app_host;
						vo.context = getApplicationContext();
						LotteryParser jsonParserForLottery = new LotteryParser();
						vo.jsonParserForLottery = jsonParserForLottery;
						// vo.context=
						childTask ct = new childTask(vo, selectInfo,
								AllMapActivity.this, handler);
						Thread th = new Thread(ct);
						th.start();
						// ct.run();
						
						// threadPoolManager.addTask(ct);
						distance_text.setText("全部");

					}
				});
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
		mPopupWindow_distance.showAsDropDown(findViewById(R.id.filter_tab), 0,
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
				
					switch (checkedId) {
					case R.id.wubai_km:
						distance = 500+"";
						distanceText.setText("0.5km");
						mPopupWindow_distance.dismiss();
						break;
					case R.id.one_km:
						distance = 1000+"";
						distanceText.setText("1km");
						mPopupWindow_distance.dismiss();
						break;
					case R.id.two_km:
						distance = 2000+"";
						distanceText.setText("2km");
						mPopupWindow_distance.dismiss();
						break;
					case R.id.three_km:
						distance = 3000+"";
						distanceText.setText("3km");
						mPopupWindow_distance.dismiss();
						break;
					case R.id.five_km:
						distance = 5000+"";
						distanceText.setText("5km");
						mPopupWindow_distance.dismiss();
						break;
					}

					RequestVo vo =new RequestVo();
					vo.context = AllMapActivity.this	;
					vo.requestUrl = R.string.app_host_getNear;
					vo.jsonParser = new LotteryParser();
					SelectInfo si = new SelectInfo();
					si.distance = distance;
					//si.distance = 10000+"";
					si.startLatitude = mylocation.lat;
					si.startLongitude = mylocation.lon;
					childTaskDistance tt = new childTaskDistance(vo, si,
							AllMapActivity.this, handler);
					Thread th = new Thread(tt);
					th.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void showDianmian(final View v) {// 为竞彩店类型填充

		if (null == mPopupWindow_dianmian) {
			LayoutInflater inflater = getLayoutInflater();
			View view = inflater.inflate(R.layout.dianmina, null);
			radioDistance = (RadioGroup) view
					.findViewById(R.id.radiogroup_dianmian);
			// 获取屏幕宽度
			int width = mContainer.getWidth();
			// 创建一个PopuWidow对象
			mPopupWindow_dianmian = new PopupWindow(view, width, 150, true);
		}
		// 设置允许在外点击消失
		mPopupWindow_dianmian.setOutsideTouchable(true);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		mPopupWindow_dianmian.setBackgroundDrawable(new BitmapDrawable());
		// 相对某个控件的位置（正左下方），无偏移
		mPopupWindow_dianmian.showAsDropDown(findViewById(R.id.filter_tab), 0,
				0);
		mPopupWindow_dianmian.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				clickSelectFalseBackground(v.getId());
			}
		});
		radioDistance.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				try {
					switch (checkedId) {
					case R.id.jincai:
						shopText.setText("竞彩店");
						mPopupWindow_dianmian.dismiss();
						break;
					case R.id.ticai:
						shopText.setText("体彩店");
						mPopupWindow_dianmian.dismiss();
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

	/**
	 * 得到GeoList的集合，以便于绘制
	 * 
	 * @param loterryInfoList
	 * @return
	 */
	public ArrayList<OverlayItem> getGeoList(
			ArrayList<LotteryInfo> loterryInfoList) {
		ArrayList<OverlayItem> GeoList = new ArrayList<OverlayItem>();
		for (LotteryInfo list : loterryInfoList) {

			GeoList.add(new OverlayItem(new GeoPoint((int) (list.lat * 1E6),
					(int) (list.lon * 1E6)), "彩", list.address));

		}

		return GeoList;
	}
	/**
	 * 距离的子线程
	 * @author chaoren
	 *
	 */
	public class childTaskDistance implements Runnable {

		private SelectInfo selectInfo;
		private Handler handler;
		private RequestVo reqVo;
		private Context context;

		public childTaskDistance(RequestVo reqVo, SelectInfo selectInfo,
				Context context, Handler handler) {
			this.context = context;
			this.handler = handler;
			this.reqVo = reqVo;
			this.selectInfo = selectInfo;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = new Message();
			Object obj = null;
			Log.i(TAG, "子线程开启");

			if (NetUtil.hasNetwork(context)) {
				obj = NetUtil.getDistance(reqVo, selectInfo);// 附近的体彩店
				msg.what = Constant.NEAR;

				msg.obj = obj;
				handler.sendMessage(msg);
			} else {
				msg.what = Constant.NET_FAILED;
				msg.obj = obj;
				handler.sendMessage(msg);
			}
		}

	}
	
	/**
	 * 省市的子线程
	 * @author chaoren
	 *
	 */

	public class childTask implements Runnable {

		private SelectInfo selectInfo;
		private Handler handler;
		private RequestVo reqVo;
		private Context context;

		public childTask(RequestVo reqVo, SelectInfo selectInfo,
				Context context, Handler handler) {
			this.context = context;
			this.handler = handler;
			this.reqVo = reqVo;
			this.selectInfo = selectInfo;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = new Message();
			Object obj = null;
			Log.i(TAG, "子线程开启");

			if (NetUtil.hasNetwork(context)) {
				obj = NetUtil.get(reqVo, selectInfo);// 真正和后台服务器交互的方法
				msg.what = Constant.SUCCESS;

				msg.obj = obj;
				handler.sendMessage(msg);
			} else {
				msg.what = Constant.NET_FAILED;
				msg.obj = obj;
				handler.sendMessage(msg);
			}
		}

	}

	protected void showProgressDialog() {
		if ((!isFinishing()) && (this.progressDialog == null)) {
			this.progressDialog = new ProgressDialog(this);
		}
		this.progressDialog.setTitle(getString(R.string.loadTitle));
		this.progressDialog.setMessage(getString(R.string.LoadContent));
		this.progressDialog.show();
	}

	/**
	 * 关闭提示框
	 */
	protected void closeProgressDialog() {
		if (this.progressDialog != null)
			this.progressDialog.dismiss();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
}
