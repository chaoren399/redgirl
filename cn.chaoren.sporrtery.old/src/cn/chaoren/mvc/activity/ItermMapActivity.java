package cn.chaoren.mvc.activity;

import java.util.List;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import cn.chaoren.mvc.util.Constant;
import cn.chaoren.R;

import com.amap.mapapi.core.AMapException;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.map.MapActivity;
import com.amap.mapapi.map.MapController;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.MyLocationOverlay;
import com.amap.mapapi.map.RouteMessageHandler;
import com.amap.mapapi.map.RouteOverlay;
import com.amap.mapapi.route.Route;
import com.amap.mapapi.route.Route.FromAndTo;

public class ItermMapActivity extends MapActivity implements RouteMessageHandler {
	
	private static final String TAG = "ItermMapActivity";
	private MapView mMapView;
	private MapController mMapController;
	private GeoPoint endPoint ;
	private MyLocationOverlay mLocationOverlay;
	private   RouteOverlay routeOverlay;
	private TextView txtNumLottery;
	private TextView txtAddress;
	private GeoPoint startPoint;
	private ImageView btnItemLocate ;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_iterm_view);
        txtNumLottery=(TextView) findViewById(R.id.txtNumLottery);
        txtAddress=(TextView) findViewById(R.id.txtAddress);
        btnItemLocate = (ImageView) findViewById(R.id.btnItemLocate);
        /**
         *定位 按钮 ，设置中心点为我的当前位置
         */
        btnItemLocate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mMapController.setCenter(startPoint);
				
			}
		});
      
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        Bundle bundle = this.getIntent().getExtras();
	     Double lat = bundle.getDouble("lat");
	     Double lon = bundle.getDouble("lon");
	     Double geoLng = bundle.getDouble("geoLng");//geoLng为我的位置
	     Double geoLat = bundle.getDouble("geoLat");
	     String wdinfoNum = bundle.getString("wdinfoNum");
	     String name = bundle.getString("name"); 
	     txtNumLottery.setText(wdinfoNum+"-"+name);
	     
	     String tel = bundle.getString("tel");
	     String serviceScope =  bundle.getString("serviceScope");
	     String address = bundle.getString("address");
	     txtAddress.setText("电话:"+tel+"  "+ "经营 :"+serviceScope+"\r\n"+"地址 :"+address);
	     Log.i(TAG, geoLng+""+geoLat);
	    
	     

		mMapView = (MapView) findViewById(R.id.mapView);
		mMapView.setBuiltInZoomControls(true);  
		
		mMapController = mMapView.getController();  
		
		
		/*float[] results=new float[1];  
		Location.distanceBetween(geoLat, geoLng, lat, lon, results); //获取两点之间的距离  
		Log.i(TAG, "results"+results[0]);*/
		//Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, results)
		
		
		 startPoint=new GeoPoint((int)(geoLat* 1E6) ,(int)(geoLng* 1E6)); //我的位置
		 endPoint=new GeoPoint((int)(lat* 1E6),(int)(lon* 1E6));
		FromAndTo fromAndTo = new FromAndTo(startPoint, endPoint);
		
		mMapController.setCenter(endPoint);  //设置 店家为地图中心点 
		List<Route> route;
		try {
			route = Route.calculateRoute(ItermMapActivity.this, fromAndTo, Route.DrivingDefault);
		
		// 构造RouteOverlay 参数为MapActivity cnt, Route rt。这里只取了查到路径的第一条。
		if (route.size() > 0) {
		routeOverlay = new RouteOverlay(ItermMapActivity.this, route.get(0));
		routeOverlay.registerRouteMessage(ItermMapActivity.this);
		//routeOverlay.registerRouteMessage(ItermMapActivity.this);//注册人消息处理函数
		routeOverlay.addToMap(mMapView);//加入地图
		
		} } catch (AMapException e) {
			e.printStackTrace();
		}
		
    }
    
    

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == Constant.FIRST_LOCATION) {
				mMapController.animateTo(mLocationOverlay.getMyLocation());
			}
		}
    };
   public void onBackPressed() {
	   finish();
   }

@Override
public void onDrag(MapView arg0, RouteOverlay arg1, int arg2, GeoPoint arg3) {
	// TODO Auto-generated method stub
	
}

@Override
public void onDragBegin(MapView arg0, RouteOverlay arg1, int arg2, GeoPoint arg3) {
	// TODO Auto-generated method stub
	
}

@Override
public void onDragEnd(MapView arg0, RouteOverlay arg1, int arg2, GeoPoint arg3) {
	// TODO Auto-generated method stub
	
}

@Override
public boolean onRouteEvent(MapView arg0, RouteOverlay arg1, int arg2, int arg3) {
	// TODO Auto-generated method stub
	return false;
};

}
