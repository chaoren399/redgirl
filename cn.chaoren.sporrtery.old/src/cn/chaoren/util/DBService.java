package cn.chaoren.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


//对数据库进行操作


public class DBService {
	// 数据库名
	private static final String DATABASE_NAME = "ticket";
	// 表名
	private static final String DATABASE_TABLE = "maked";

	// 主键
	public static final String KEY_ID = "_id";
	public static final String KEY_TYPE = "c_type";
	public static final String KEY_NUM = "num";
	public static final String KEY_M_ID = "m_id";
	public static final String KEY_L_CN = "l_cn";
	public static final String KEY_L_ID = "l_id";
	public static final String KEY_H_CN = "h_cn";
	public static final String KEY_H_ID = "h_id";
	public static final String KEY_A_CN = "a_cn";
	public static final String KEY_A_ID = "a_id";
	public static final String KEY_SSTIME = "sstime";

	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_CREATE = "create table if not exists "
			+ DATABASE_TABLE + " " + "(_id integer primary key autoincrement, "
			+ KEY_TYPE + " text, " + KEY_NUM + " text, " + KEY_M_ID + " text, "
			+ KEY_L_CN + " text, " + KEY_L_ID + " text, " + KEY_H_CN
			+ " text, " + KEY_H_ID + " text," + KEY_A_CN + " text, " + KEY_A_ID
			+ " text," + KEY_SSTIME + " text" + " );";
	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DBService(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
	}

	// ---打开数据库---
	public DBService open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	// ---关闭数据库---
	public void close() {
		DBHelper.close();
		db.close();
	}

	// 增加记录
	public long insert(Map<String, String> _map) {
		ContentValues cv = new ContentValues();
		cv.put(KEY_TYPE, _map.get(KEY_TYPE));
		cv.put(KEY_NUM, _map.get(KEY_NUM));
		cv.put(KEY_M_ID, _map.get(KEY_M_ID));
		cv.put(KEY_L_CN, _map.get(KEY_L_CN));
		cv.put(KEY_L_ID, _map.get(KEY_L_ID));
		cv.put(KEY_H_CN, _map.get(KEY_H_CN));
		cv.put(KEY_H_ID, _map.get(KEY_H_ID));
		cv.put(KEY_A_CN, _map.get(KEY_A_CN));
		cv.put(KEY_A_ID, _map.get(KEY_A_ID));
		cv.put(KEY_SSTIME, _map.get(KEY_SSTIME));
		return db.insert(DATABASE_TABLE, null, cv);
	}

	// 获取所有
	public List<Map<String, String>> getAll(String type) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Cursor cursor = db.query(DATABASE_TABLE, new String[] { KEY_ID,
				KEY_TYPE, KEY_NUM, KEY_M_ID, KEY_L_CN, KEY_L_ID, KEY_H_CN,
				KEY_H_ID, KEY_A_CN, KEY_A_ID, KEY_SSTIME }, KEY_TYPE + " = '"
				+ type + "'", null, null, null, "_id DESC");
		if (cursor.moveToFirst()) {
			do {
				Map<String, String> map = new HashMap<String, String>();
				map.put(KEY_TYPE, cursor.getString(cursor
						.getColumnIndex(DBService.KEY_TYPE)));
				map.put(KEY_NUM, cursor.getString(cursor
						.getColumnIndex(DBService.KEY_NUM)));
				map.put(KEY_M_ID, cursor.getString(cursor
						.getColumnIndex(DBService.KEY_M_ID)));
				map.put(KEY_L_CN, cursor.getString(cursor
						.getColumnIndex(DBService.KEY_L_CN)));
				map.put(KEY_L_ID, cursor.getString(cursor
						.getColumnIndex(DBService.KEY_L_ID)));
				map.put(KEY_H_CN, cursor.getString(cursor
						.getColumnIndex(DBService.KEY_H_CN)));
				map.put(KEY_H_ID, cursor.getString(cursor
						.getColumnIndex(DBService.KEY_H_ID)));
				map.put(KEY_A_CN, cursor.getString(cursor
						.getColumnIndex(DBService.KEY_A_CN)));
				map.put(KEY_A_ID, cursor.getString(cursor
						.getColumnIndex(DBService.KEY_A_ID)));
				String sstime = cursor.getString(cursor
						.getColumnIndex(DBService.KEY_SSTIME));
				map.put(KEY_SSTIME, sstime);
				map.put("shottime",
						sstime.substring(sstime.indexOf(" "),
								sstime.lastIndexOf(":")));
				map.put("shotdate", sstime.substring(0, sstime.indexOf(" ")));
				list.add(map);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return list;
	}

	// 获取所有
	public List<Map<String, String>> getAll() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Cursor cursor = db.query(DATABASE_TABLE, new String[] { KEY_ID,
				KEY_TYPE, KEY_NUM, KEY_M_ID, KEY_L_CN, KEY_L_ID, KEY_H_CN,
				KEY_H_ID, KEY_A_CN, KEY_A_ID, KEY_SSTIME }, null, null, null,
				null, "_id DESC");
		if (cursor.moveToFirst()) {
			do {
				Map<String, String> map = new HashMap<String, String>();
				map.put(KEY_TYPE, cursor.getString(cursor
						.getColumnIndex(DBService.KEY_TYPE)));
				map.put(KEY_NUM, cursor.getString(cursor
						.getColumnIndex(DBService.KEY_NUM)));
				map.put(KEY_M_ID, cursor.getString(cursor
						.getColumnIndex(DBService.KEY_M_ID)));
				map.put(KEY_L_CN, cursor.getString(cursor
						.getColumnIndex(DBService.KEY_L_CN)));
				map.put(KEY_L_ID, cursor.getString(cursor
						.getColumnIndex(DBService.KEY_L_ID)));
				map.put(KEY_H_CN, cursor.getString(cursor
						.getColumnIndex(DBService.KEY_H_CN)));
				map.put(KEY_H_ID, cursor.getString(cursor
						.getColumnIndex(DBService.KEY_H_ID)));
				map.put(KEY_A_CN, cursor.getString(cursor
						.getColumnIndex(DBService.KEY_A_CN)));
				map.put(KEY_A_ID, cursor.getString(cursor
						.getColumnIndex(DBService.KEY_A_ID)));
				String sstime = cursor.getString(cursor
						.getColumnIndex(DBService.KEY_SSTIME));
				map.put(KEY_SSTIME, sstime);
				map.put("shottime",
						sstime.substring(sstime.indexOf(" "),
								sstime.lastIndexOf(":")));
				map.put("shotdate", sstime.substring(0, sstime.indexOf(" ")));
				list.add(map);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return list;
	}

	// 删除指定记录
	public void del(String m_id) {
		db.delete(DATABASE_TABLE, KEY_M_ID + "=" + m_id, null);
	}
}