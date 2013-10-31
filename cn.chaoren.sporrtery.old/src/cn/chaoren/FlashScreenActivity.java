package cn.chaoren;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import cn.chaoren.net.HttpServices;
import cn.chaoren.net.JSONBean;
import cn.chaoren.R;

import com.mobclick.android.MobclickAgent;

/**
 * 类说明：
 * 
 * @创建时间 2011-8-1 上午11:02:23
 * @创建人： 陆林
 * @邮箱：15366189868@189.cn
 */


//升级界面
public class FlashScreenActivity extends Activity {

	/**
	 * 升级进度框
	 */
	ProgressDialog updateProgressDialog;
	/**
	 * 是否升级提示框
	 */
	AlertDialog updateAlertDialog;
	/**
	 * 网络异常提示框
	 */
	AlertDialog netErrorDialog;
	/**
	 * 升级路径
	 */
	String updatePath = "";
	/**
	 * 下载线程是否退出
	 */
	boolean isAlive = true;

	public static final int BUFFER_SIZE = 1024 * 16; // stream缓冲大小/**
	private final static String tempfile = "sporttery.apk";
	private final static String tempfilepath = "/data/data/cn.chaoren/files/";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.flash);

		updateAlertDialog = new AlertDialog.Builder(this).create();
		updateAlertDialog.setTitle(R.string.app_name);//竞彩网
		updateAlertDialog.setCancelable(false);

		netErrorDialog = new AlertDialog.Builder(FlashScreenActivity.this)
				.setTitle(R.string.tip).setMessage(R.string.progress_hqsjsb)
				.create();
		netErrorDialog.setCancelable(false);
		netErrorDialog.setButton(getResources().getString(R.string.queding),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						finish();
					}
				});

		//升级的对话框
		updateProgressDialog = new ProgressDialog(FlashScreenActivity.this) {
			@Override
			public boolean dispatchKeyEvent(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.KEYCODE_SEARCH
						|| event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
					return true;
				}
				return super.dispatchKeyEvent(event);
			}

		};
		updateProgressDialog.setTitle(R.string.app_name);
		updateProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		updateProgressDialog.setMax(100);
		updateProgressDialog.setCancelable(false);
		updateProgressDialog.setButton(getResources()
				.getString(R.string.cancel),//取消
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						isAlive = false;
						finish();
					}
				});
		new GetUpdateTask().execute();
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	class GetUpdateTask extends AsyncTask<String, Integer, JSONBean> {
		private long beginTime;

		@Override
		protected JSONBean doInBackground(String... params) {
			try {
				beginTime = System.currentTimeMillis();
				JSONBean result = HttpServices.getNewVersion();
				long endTime = System.currentTimeMillis();
				long time = endTime - beginTime;
				if (time < 4000) {
					Thread.sleep(4000 - time);
				}
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(final JSONBean result) {
			super.onPostExecute(result);
			if (FlashScreenActivity.this.isFinishing()) {
				return;
			}
			if (result != null) {
				PackageInfo info = null;
				try {
					info = getPackageManager().getPackageInfo(getPackageName(),
							0);
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
				String versionName = info.versionName; // 当前版本名
				String appversion = result.bodys.get(0).get("appversion");

				Log.e("当前的版本号为：", versionName);//1.1
				Log.e("新的版本号为：", appversion);//1.0
				//判断两个版本是否是一样的
				if (versionName.compareTo(appversion) >= 0) {
					Intent intent = new Intent(FlashScreenActivity.this,
							SportteryMainActivity.class);
					startActivity(intent);
					finish();
				} else {
					//如果不相同，则下载安装
					new GetUpdateDataTask().execute();
				}
			} else {
				netErrorDialog.show();
			}
		}
	}

	class GetUpdateDataTask extends AsyncTask<String, Integer, JSONBean> {

		@Override
		protected JSONBean doInBackground(String... params) {
			try {
				return HttpServices.getNewVersionData();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(final JSONBean result) {
			super.onPostExecute(result);
			if (FlashScreenActivity.this.isFinishing()) {
				return;
			}
			if (result != null) {
				updatePath = result.bodys.get(0).get("appdownurl");
				updateAlertDialog.setTitle(R.string.update_tip);
				updateAlertDialog.setMessage(result.bodys.get(0).get(
						"appreadme"));
				updateAlertDialog.setButton(
						getResources().getString(R.string.update),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								new DownloadUpdate().execute();
							}
						});
				updateAlertDialog.setButton2(
						getResources().getString(R.string.exit),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								finish();
							}
						});
				updateAlertDialog.show();
			} else {
				netErrorDialog.show();
			}
		}
	}

	
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
		.setTitle(R.string.exit)
		.setMessage(R.string.isexit)
		.setPositiveButton(R.string.yes,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						dialog.dismiss();
						finish();
					}
				})
		.setNegativeButton(R.string.no,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						dialog.dismiss();
					}
				}).create().show();
		//super.onBackPressed();
	}
	
	/**
	 * 下载更新文件的任务
	 * 
	 */
	class DownloadUpdate extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			/* 取得URL */
			return down();//调用下面的方法
		}

		private boolean down() {
			HttpURLConnection conn = null;
			InputStream is = null;
			BufferedInputStream bis = null;
			FileOutputStream fos = null;
			BufferedOutputStream bos = null;
			URL myURL;
			try {
				myURL = new URL(updatePath);
				/* 创建连接 */
				conn = (HttpURLConnection) myURL.openConnection();
				conn.setConnectTimeout(1000 * 20);
				conn.setReadTimeout(1000 * 20);
				conn.connect();
				System.out.println("conn.getResponseCode() ="
						+ conn.getResponseCode());
				int size = conn.getContentLength();
				publishProgress(1, size);
				/* InputStream 下载文件 */
				is = conn.getInputStream();
				bis = new BufferedInputStream(is, BUFFER_SIZE);
				if (is == null) {
					return false;
				}
				fos = FlashScreenActivity.this.openFileOutput(tempfile,
						Context.MODE_WORLD_READABLE);
				bos = new BufferedOutputStream(fos, BUFFER_SIZE);

				byte buf[] = new byte[1024 * 32];
				do {
					int numread = bis.read(buf);
					if (numread <= 0) {
						break;
					}
					bos.write(buf, 0, numread);
					publishProgress(2, numread);
				} while (isAlive);
				bos.flush();
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} finally {
				try {
					if (is != null)
						is.close();
					if (fos != null)
						fos.close();
					if (bos != null)
						bos.close();
					if (bis != null)
						bis.close();
					if (conn != null)
						conn.disconnect();
				} catch (Exception ex) {
				}
			}
			return true;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (!isAlive) {
				return;
			}
			if (!FlashScreenActivity.this.isFinishing()) {
				updateProgressDialog.dismiss();
				if (result) {
					new AlertDialog.Builder(FlashScreenActivity.this)
							.setTitle(R.string.down_finish)
							.setMessage(R.string.down_finish1)
							.setPositiveButton(
									getResources().getString(R.string.install),
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											try {
												Field field = dialog
														.getClass()
														.getSuperclass()
														.getDeclaredField(
																"mShowing");
												field.setAccessible(true);
												field.set(dialog, false);
												dialog.dismiss();
											} catch (Exception e) {
											}
											Intent intent = new Intent();
											intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
											intent.setAction(android.content.Intent.ACTION_VIEW);
											intent.setDataAndType(
													Uri.parse("file://"
															+ tempfilepath
															+ tempfile),
													"application/vnd.android.package-archive");
											startActivity(intent);
										}
									})
							.setNegativeButton(R.string.exit,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											finish();
										}
									}).show();
				} else {
					new AlertDialog.Builder(FlashScreenActivity.this)
							.setTitle(R.string.down_error)
							.setMessage(R.string.down_error1)
							.setPositiveButton(R.string.queding,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											finish();
										}
									}).show();
				}
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			if (values[0] == 1) {
				updateProgressDialog.setMax(values[1]);
			} else if (values[0] == 2) {
				updateProgressDialog.setProgress(updateProgressDialog
						.getProgress() + values[1]);
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			updateProgressDialog.show();
		}
	}
}
