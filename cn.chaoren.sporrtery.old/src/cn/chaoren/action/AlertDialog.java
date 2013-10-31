package cn.chaoren.action;

import cn.chaoren.R;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialog {
     private Context context;
     public AlertDialog(Context context) {
		this.context = context;
	}
     
     private Dialog dialog;
     
     public void showConfirmDialog(int titleId,int msgId)
     {
    	 if (dialog != null && dialog.isShowing()) {
			return;
		}
    	 android.app.AlertDialog.Builder adb = new  android.app.AlertDialog.Builder(context);
    	 
    	 if (titleId != 0) {
			adb.setTitle(titleId);
		}
    	 
    	 if (msgId != 0) {
			adb.setMessage(msgId);
		}
    	 adb.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();				
			}
		});
    	 dialog = adb.create();
    	 dialog.show();
    	 /*adb.show();*/
     }
     
     
     public void dismiss()
     {
    	 if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
     }
}
