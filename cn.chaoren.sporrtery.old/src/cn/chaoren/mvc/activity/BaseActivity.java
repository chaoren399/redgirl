package cn.chaoren.mvc.activity;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import cn.chaoren.mvc.model.RequestVo;
import cn.chaoren.mvc.model.SelectInfo;
import cn.chaoren.mvc.util.CommonUtil;
import cn.chaoren.mvc.util.Constant;
import cn.chaoren.mvc.util.NetUtil;
import cn.chaoren.mvc.util.ThreadPoolManager;
import cn.chaoren.R;



public abstract class BaseActivity extends Activity {
	protected Context context;
	protected ProgressDialog progressDialog;
	
	 private ThreadPoolManager threadPoolManager;
	 
		public BaseActivity() {
			threadPoolManager = ThreadPoolManager.getInstance();
		}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		initView();
		//processLogic();
		
		// TODO Auto-generated method stub
		String strVer=NetUtil.GetSystemVersion();
		strVer=strVer.substring(0,3).trim();
		float fv=Float.valueOf(strVer);
		if(fv>2.3)
		{
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		.detectDiskReads()
		.detectDiskWrites()
		.detectNetwork() // 这里可以替换为detectAll() 就包括了磁盘读写和网络I/O
		.penaltyLog() //打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
		.build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
		.detectLeakedSqlLiteObjects() //探测SQLite数据库操作
		.penaltyLog() //打印logcat
		.penaltyDeath()
		.build()); 
		}
		super.onCreate(savedInstanceState);

		}
	
	private void initView() {
		// TODO Auto-generated method stub
		loadViewLayout();
		findViewById();
		setListener();
		/*Message msg=new Message();
		msg.what=Constant.OVER;
		handler.sendMessage(msg);*/
		    
		
		
	}
	class BaseHandler extends Handler{
		private Context context;
		private DataCallback callBack;
		private RequestVo reqVo;

		public BaseHandler(Context context, DataCallback callBack,RequestVo reqVo) {
			this.context = context;
			this.callBack = callBack;
			this.reqVo = reqVo;
		}
		
		public void handleMessage(Message msg){ 
			closeProgressDialog();
			if(msg.what==Constant.SUCCESS){
				if(msg.obj==null){
					CommonUtil.showInfoDialog(context, getString(R.string.net_error));
				}else{
					callBack.processData(msg.obj, true);
				}
			}else if(msg.what==Constant.NET_FAILED){
				CommonUtil.showInfoDialog(context, getString(R.string.net_error));
			}
		}
	}
	class BaseTask implements Runnable{
		private Context context;
		private RequestVo reqVo;
		private Handler handler;
		private SelectInfo selectInfo;

		public BaseTask(Context context, RequestVo reqVo, Handler handler, SelectInfo selectInfo) {
			this.context = context;
			this.reqVo = reqVo;
			this.handler = handler;
			this.selectInfo=selectInfo;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg=new Message();
			Object obj = null;
			
			if(NetUtil.hasNetwork(context)){
				obj = NetUtil.getDistance(reqVo,selectInfo);//省市的体彩店
				msg.what = Constant.SUCCESS;
				
				msg.obj = obj;
				handler.sendMessage(msg);
			}else{
				msg.what = Constant.NET_FAILED;
				msg.obj = obj;
				handler.sendMessage(msg);
			}
		}
		
			
		
		
	}
	/**
	 *
	 * @param reqVo
	 * @param callBack
	 */
	protected void getDataFromServer(RequestVo reqVo, DataCallback callBack,SelectInfo selectInfo) {
		showProgressDialog();
		BaseHandler handler = new BaseHandler(this, callBack, reqVo);//handle类数据得到后使用
		BaseTask taskThread = new BaseTask(this, reqVo, handler,selectInfo);//线程类--获取数据
		this.threadPoolManager.addTask(taskThread);
	}
	public abstract interface DataCallback<T> {
		public abstract void processData(T paramObject,boolean paramBoolean);
	}
		
	protected abstract void  findViewById() ;
	protected abstract void  setListener();
	protected abstract void  processLogic(); 
	protected abstract void loadViewLayout();
	/**
	 * 显示提示框
	 */
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

}
