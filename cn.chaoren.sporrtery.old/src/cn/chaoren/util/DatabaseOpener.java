package cn.chaoren.util;

import java.util.ArrayList;

import cn.chaoren.db.MyBettingDO;
import cn.chaoren.obj.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


//新建数据库
public class DatabaseOpener extends SQLiteOpenHelper {
    public static final String _ID ="_id";
    public static final String GUID="guid";
    public static final String TYPE = "type";
	private final String TABLE_NAME="alert_game";
	
	private final String CREATE_TABLE = "create table if not exists " + TABLE_NAME +"("+_ID+" integer primary key autoincrement,"+
										 GUID+" text,"+TYPE+" integer)";; 
	public DatabaseOpener(Context context) {
		super(context, "alert_data.db", null, 1);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);

	}
	
	
	public void delete(String guid)
	{
		SQLiteDatabase database = getWritableDatabase();
		database.delete(TABLE_NAME, GUID+"='"+guid+"'", null);
		database.close();
	}
	
	
	public void insert(String guid)
	{
		if (guid == null || "".equals(guid)) {
			return;
		}
		SQLiteDatabase database = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(GUID, ""+guid);
		database.insert(TABLE_NAME, null, values);
		database.close();
	}
	
	public boolean exists(String guid,int type)
	{
		SQLiteDatabase database = getReadableDatabase();
		Cursor cursor = database.query(CREATE_TABLE, new String[]{_ID,GUID,TYPE},"("+GUID+"='"+guid+"' and " + TYPE + "='" +type+"')", null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			String guid2 = cursor.getString(cursor.getColumnIndex(GUID));
			if (guid.equals(guid2)) {
				return true;
			}
		}
		database.close();
		return false;
	}
	
	/**
	 * @param activity
Selection
	 */
	public ArrayList<Selection> queryAll(int type)
	{
		ArrayList<Selection> result = null;
		SQLiteDatabase database = getReadableDatabase();
		Cursor cursor = database.query(TABLE_NAME, new String[]{_ID,GUID,TYPE},TYPE +" = " +type, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			String guid = null;
			result = new ArrayList<Selection>();
			Selection selection=null;
			do {
				selection = new Selection();
				guid =cursor.getString(cursor.getColumnIndex(GUID));
				selection.guid = guid;
				selection.selected = true;
				result.add(selection);
			} while (cursor.moveToNext());
			
		}
		if (cursor != null) {
			cursor.close();
		}
		database.close();
		return result;
	}
	
	public void insertAll(ArrayList<Selection> selectionList,int type)
	{
		if (selectionList == null || selectionList.size()==0) {
			return;
		}
		SQLiteDatabase database = getWritableDatabase();
		int size = selectionList.size();
		ContentValues values = new ContentValues();
		Selection selection;
		for (int i = 0; i < size; i++) {
			selection = selectionList.get(i);
			if (selection.selected) {
				values.put(GUID, ""+selection.guid);
				values.put(TYPE, "" + type);
				database.insert(TABLE_NAME, null, values);
			}
			
		}
		
		database.close();
	}
	
	public void deleteAll(int type)
	{
		SQLiteDatabase database = getWritableDatabase();
		database.delete(TABLE_NAME, TYPE+"=" + type, null);
		database.close();
	}
}
