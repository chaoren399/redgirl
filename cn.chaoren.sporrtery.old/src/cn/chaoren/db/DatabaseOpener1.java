package cn.chaoren.db;



import java.util.ArrayList;
import java.util.Date;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.SyncStateContract.Helpers;
import android.text.Selection;

public class DatabaseOpener1 extends SQLiteOpenHelper {
    public static final String _ID ="_id";
    public static final String NUMBER_STRING="number_string";
    public static final String BUY_TIME="buy_time";
    public static final String STAKE="stake";
    public static final String FEE="fee";  
    public static final String  FORETX="foretx";
    
    public static final String BET_STYLE="bet_style";//过关方式
    public static final String TYPE = "type";// 玩法
	private static final String TABLE_NAME="buy_game";
	private static final String PLAY_TIME="play_time";
	private static final String  STATUS="status";
	private static final String WIN_MATCH="win_match";
	private static final String WIN_STATUS="win_status";
	private static final String WIN_FEE="win_fee";
	private static final String BEISHU="beishu";
	
	
	private final String CREATE_TABLE = "create table if not exists " + TABLE_NAME +"("+_ID+" integer primary key autoincrement,"+			
			NUMBER_STRING+" text,"+TYPE+" integer," + BUY_TIME+" integer, "+ PLAY_TIME+" integer, "+ BET_STYLE+" String, "+WIN_FEE+" String, "+ STATUS+" integer, "+ 
			WIN_MATCH+" String, "+ WIN_STATUS+" integer, "+ STAKE  + " integer," + BEISHU  + " integer," +FEE + " integer,"+ FORETX+ " text )";; 
	public DatabaseOpener1(Context context) {
		super(context, "buy_data.db", null, 3);
		
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
	
	
	public void delete(String id)
	{
		SQLiteDatabase database = getWritableDatabase();
		database.delete(TABLE_NAME, _ID+"='"+id+"'", null);
		database.close();
	}
	
	
	public void insert(String numberString,int stake,int fee,String foretx,int play_time,int status,String win_matach,int win_status, String win_fee,String type,String bet_style,int beishu)
	{
		if (numberString == null || "".equals(numberString)) {
			return;
		}
		SQLiteDatabase database = getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(STAKE, stake);
		values.put(FEE, fee);
		values.put(NUMBER_STRING, numberString); 
		values.put(BUY_TIME, 	Math.round(new Date().getTime()/1000));
		values.put(FORETX, foretx);
		values.put(PLAY_TIME, play_time);
		values.put(STATUS, status);
		values.put(WIN_MATCH, win_matach);
		values.put(WIN_STATUS, win_status);
		values.put(WIN_FEE, win_fee);
		values.put(TYPE, type);
		values.put(BET_STYLE, bet_style);
		values.put(BEISHU, beishu);
		
		
		database.insert(TABLE_NAME, null, values);
		database.close();
	}
	
//	public boolean exists(String guid,int type)
//	{
//		SQLiteDatabase database = getReadableDatabase();
//		Cursor cursor = database.query(CREATE_TABLE, new String[]{_ID,GUID,TYPE},"("+GUID+"='"+guid+"' and " + TYPE + "='" +type+"')", null, null, null, null);
//		if (cursor != null && cursor.moveToFirst()) {
//			String guid2 = cursor.getString(cursor.getColumnIndex(GUID));
//			if (guid.equals(guid2)) {
//				return true;
//			}
//		}
//		database.close();
//		return false;
//	}
	
	/**
	 * @param activity
	 * @return
	 */
	
	//对数据据库的操作  查询
	public ArrayList<MyBettingDO> queryAll(int type)
	{
		ArrayList<MyBettingDO> result = null;
		SQLiteDatabase database = getReadableDatabase();
		Cursor cursor = database.query(TABLE_NAME, new String[]{_ID,NUMBER_STRING,BUY_TIME,STAKE,FEE,FORETX,PLAY_TIME,STATUS,WIN_MATCH,TYPE,BET_STYLE,WIN_STATUS,BEISHU,WIN_FEE},null, null, null, null, null,null);
		if (cursor != null && cursor.moveToFirst()) {
			
			result = new ArrayList<MyBettingDO>();
			MyBettingDO selection=null;    
			do {
				selection = new MyBettingDO();
				
				//共查询了字段
				selection.buyTime= cursor.getInt(cursor.getColumnIndex(BUY_TIME));
				selection.id = cursor.getInt(cursor.getColumnIndex(_ID));
				selection.fee = cursor.getInt(cursor.getColumnIndex(FEE));
				selection.stake = cursor.getInt(cursor.getColumnIndex(STAKE));
				selection.numberString = cursor.getString(cursor.getColumnIndex(NUMBER_STRING));
				selection.foretx = cursor.getString(cursor.getColumnIndex(FORETX));
				selection.play_time=cursor.getInt(cursor.getColumnIndex(PLAY_TIME));
				selection.status=cursor.getInt(cursor.getColumnIndex(STATUS));
				selection.win_match=cursor.getString(cursor.getColumnIndex(WIN_MATCH));
				selection.type=cursor.getString(cursor.getColumnIndex(TYPE));
				selection.bet_style=cursor.getString(cursor.getColumnIndex(BET_STYLE));
				selection.win_status=cursor.getInt(cursor.getColumnIndex(WIN_STATUS));
				selection.beishu=cursor.getInt(cursor.getColumnIndex(BEISHU));
				selection.win_fee=cursor.getString(cursor.getColumnIndex(WIN_FEE));
				
//				result.add(selection);
				result.add(0, selection);
			} while (cursor.moveToNext());
			
		}   
		if (cursor != null) {
			cursor.close();
		}
		database.close();
		return result;
	}    
	
//	public void insertAll(ArrayList<Selection> selectionList,int type)
//	{
//		if (selectionList == null || selectionList.size()==0) {
//			return;
//		}
//		SQLiteDatabase database = getWritableDatabase();
//		int size = selectionList.size();
//		ContentValues values = new ContentValues(); 
//		Selection selection;
//		for (int i = 0; i < size; i++) {
//			selection = selectionList.get(i);
//			if (selection.selected) {
//				values.put(GUID, ""+selection.guid);
//				values.put(TYPE, "" + type);
//				database.insert(TABLE_NAME, null, values);
//			}
//			
//		}
//		database.close();
//	}
	
	public void deleteAll(int type)
	{
		SQLiteDatabase database = getWritableDatabase();
		database.delete(TABLE_NAME, TYPE+"=" + type, null);
		database.close();
	}
	
	public String findZhanji(String _id){
		
		SQLiteDatabase database = getReadableDatabase();
		String result = null;
		 if(database.isOpen()){
			Cursor cursor = database.rawQuery("select number_string from buy_game where _id=?",  new String[]{_id});
			 if(cursor.moveToNext()){
				 result = cursor.getString(0);
			 }
			cursor.close();
		 }
		 database.close();
		return result;
	}
	/**
	 * add by zzy
	 * 根据ID 找到play_time比赛最大时间字段
	 * @param _id
	 * @return
	 */
	public int findPlay_Time(String _id){
		SQLiteDatabase database=getReadableDatabase();
		int  result=0;
		if(database.isOpen()){
			Cursor cursor=database.rawQuery("select play_time from buy_game where _id=?", new String[]{_id});
			if(cursor.moveToNext()){
				result=cursor.getInt(0);
			}
			cursor.close();
		}
		database.close();
		return result ;
	}
	/**
	 * add by zzy
	 * 根据 ID 找到 status 比赛状态字段
	 * @param _id
	 * @return
	 */
	public int  findStatus(String _id){
		SQLiteDatabase database=getReadableDatabase();
		int  result =0;
		if(database.isOpen()){
			Cursor cursor=database.rawQuery("select status from buy_game where _id=?", new String[]{_id});
			if(cursor.moveToNext()){
				result=cursor.getInt(0);
			}
			cursor.close();
		}
		database.close();
		
		return result;
	}
	/**
	 * add by zzy
	 * 根据 ID 找到 win_match 获胜球队的字段
	 * @param _id
	 * @return
	 */
	public String  findWin_Match(String _id){
		SQLiteDatabase database=getReadableDatabase();
		String result=null;
		if(database.isOpen()){
			Cursor cursor=database.rawQuery("select win_match from buy_game where _id=?",new String[]{_id});
			if(cursor.moveToNext()){
				result=cursor.getString(0);
			}
			cursor.close();
		}
		database.close();
		
		return result;
	}
	
		
		
	
	
	/**
	 * add by zzy
	 * 根据 ID 找到 win_fee 奖金字段
	 * @param _id
	 * @return
	 */
	public String  findWin_fee(String _id){
		return null;
	}
	/**
	 * add by zzy
	 * 根据 ID 找到 win_status 获胜状态字段
	 * @param _id
	 * @return
	 */
	public String  findWin_Status(String _id){
		return null;
	}
	
	/**
	 * 根据ID删除战绩记录
	 */
	
	public boolean deleteinfo(String id){
		SQLiteDatabase database=getReadableDatabase();
		if(database.isOpen()){
			String sql ="delete from buy_game where _id=?";
			database.execSQL(sql,new Object[]{id});
		}
		database.close();
		return true;
	}
}
