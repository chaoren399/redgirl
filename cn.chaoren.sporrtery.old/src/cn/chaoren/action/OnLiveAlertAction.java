package cn.chaoren.action;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import android.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Vibrator;
import android.widget.ListAdapter;

public class OnLiveAlertAction {
     private int alertType;
     
     private  Vibrator vibrator=null;
     
     private PlayerThread player;
     /**
      * 该方法
      * 提示，暂时只支持；语音和震动
      * 1，震动
      * 2，语音
     * @param type
     */
    public void alert(final int type,Handler handler,int longMill)
     {
    	 alertType = type;
    	 System.out.println("alert..type=" + type);
    	 int timeAdded = 0;
    	 if (type == 0) {
			return;
		}
    	 if (alertType==1) {
    		 if (vibrator != null) {
				vibrator.cancel();
			}
    		 vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    		
    		 long[] pattern = {800, 50, 400, 30}; // OFF/ON/OFF/ON...    
    		 vibrator.vibrate(pattern, -1);

    		 
		}else if (alertType == 2) {
			player = new PlayerThread(context);
			player.start();
			timeAdded = 0;
		}
    	 handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				if (type == 1 && vibrator!= null) {
					vibrator.cancel();  
					vibrator = null;
				}else if (type == 2 && player !=  null) {
					player.stopAlarmRing();
					player = null;

				}
				
			}
		}, longMill+timeAdded);
     }
     
    
    public class PlayerThread extends Thread
    {
    	private Context context;
    	public PlayerThread(Context con) {
			context = con;
			player = new MediaPlayer();
		}
    	private MediaPlayer player = null;
    	@Override
    	public void run() {
    		Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    		try {

    			player.setDataSource(context, alert);
    		final AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
    		if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
    			player.setAudioStreamType(AudioManager.STREAM_ALARM);
    		player.setLooping(true);
    		player.prepare();
    		}
    		} catch (IllegalStateException e) {
    		e.printStackTrace();
    		} catch (IOException e) {
    		e.printStackTrace();
    		}
    		player.start();

    	}
    	
    	
    	public void stopAlarmRing() {
    		player.stop();
    		}

    }
    
    
    
    
    /**
     * 只支持，半场，全场，等关系时间的事件
     * @param message
     */
    public void confirmAlert(int messageId)
    {
    	AlertDialog.Builder adb = new AlertDialog.Builder(context);
    	adb.setMessage(messageId);
    	adb.setPositiveButton(R.string.ok, onClickListener);
    	adb.show();
    }
    
    
    /**
     * 用于事件列表
     * @param adapter
     * @param onClickListener
     */
    public void confirmAlert(ListAdapter adapter,DialogInterface.OnClickListener onClickListener)
    {
    	AlertDialog.Builder adb = new AlertDialog.Builder(context);
    	adb.setAdapter(adapter, null);
    	adb.setPositiveButton(R.string.ok, onClickListener);
    	adb.show();
    }
    
    
    
    private DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
			
		}
	};
    
    private Context context;
    
    public OnLiveAlertAction(Context con) {
		context = con;
	}
    
    
     
     
}
