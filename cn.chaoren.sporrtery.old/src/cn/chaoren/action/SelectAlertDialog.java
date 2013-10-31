package cn.chaoren.action;

import cn.chaoren.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.widget.BaseAdapter;

public class SelectAlertDialog {
	public SelectAlertDialog() {
		// TODO Auto-generated constructor stub
	}
	private String[]strArray;
	private Context mContext;
	private String title;
	private int titleResId;
	private DialogInterface.OnClickListener dialogClick;
	
	public static void showAlertDialog(Context context,CharSequence title,CharSequence message,DialogInterface.OnClickListener onclick)
	{
		AlertDialog.Builder adb = new AlertDialog.Builder(context);
		if (title != null && !"".equals(title)) {
		   adb.setTitle(title);
		}
		if (message != null && !"".equals(message)) {
			adb.setMessage(message);
		}
		adb.setPositiveButton(context.getString(R.string.setting), onclick);
		adb.setNegativeButton(context.getString(R.string.cancel),onclick);
		adb.show();
	}
	
	public static void showConfirmDialog(Context context,CharSequence title,CharSequence message)
	{
		AlertDialog.Builder adb = new AlertDialog.Builder(context);
		if (title != null && !"".equals(title)) {
		   adb.setTitle(title);
		}
		if (message != null && !"".equals(message)) {
			adb.setMessage(message);
		}
		adb.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();				
			}
		});
	
		adb.show();
	}
	
	
	public static void showConfirmDialog(Context context,CharSequence title,BaseAdapter adapter)
	{
		AlertDialog.Builder adb = new AlertDialog.Builder(context);
		if (title != null && !"".equals(title)) {
		   adb.setTitle(title);
		}
		
		adb.setAdapter(adapter, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				
			}
		});
		adb.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();				
			}
		});
	
		adb.show();
	}
	
     public SelectAlertDialog(Context context,String title,String[]array) {
		mContext = context;
		strArray = array;
		this.title = title;
	}

     public SelectAlertDialog(Context context,int title,String[]array,DialogInterface.OnClickListener onclick) {
 		mContext = context;
 		strArray = array;
 		this.titleResId = title;
 		dialogClick = onclick;
 	}
     
     
     public Dialog showCheckDiloag(DialogInterface.OnClickListener onclick,boolean[]selectedItems,OnMultiChoiceClickListener multListener)
     {
    	 if (dialog != null && dialog.isShowing()) {
			return dialog;
		}
    	
    	AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
    	if (title != null) {
			adb.setTitle(title);
		 }else if ( titleResId!= 0) {
			adb.setTitle(titleResId);
		}
    	adb.setMultiChoiceItems(strArray, selectedItems, multListener);
    	adb.setCancelable(false);
    	adb.setPositiveButton(R.string.ok, onclick);
    	adb.setNegativeButton(R.string.cancel, onclick);
    	dialog = adb.create();
    	dialog.show();
    	return dialog;
     }
     
     
     public Dialog dialog;
     public Dialog show()
     {
    	 if (dialog!=null&& dialog.isShowing()) {
			return dialog;
		}
    	 AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
    	 if (title != null) {
			adb.setTitle(title);
		 }else if ( titleResId!= 0) {
			adb.setTitle(titleResId);
		}
    	 adb.setItems(strArray, dialogClick);
    	 dialog = adb.create();
    	 dialog.show();
    	 return dialog;
     }
     
     public void close()
     {
    	 if (dialog.isShowing()) {
			dialog.dismiss();
		}
     }
}
