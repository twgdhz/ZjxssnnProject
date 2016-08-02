package com.zjxfood.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "shishangnannv.db"; // 数据库名称
	private static final int version = 1; // 数据库版本
	private static DatabaseHelper mInstance = null;
	private static final String TABLE_NAME = "attention"; // 数据库表名

	public static DatabaseHelper getInstance(Context context){
		if (mInstance == null) {
		      mInstance = new DatabaseHelper(context.getApplicationContext());
		    }
		    return mInstance;
	}
	public DatabaseHelper(Context context) {  
        //必须通过super调用父类当中的构造函数  
        super(context, DB_NAME, null, version);
    }  
	public DatabaseHelper(Context context, String name, CursorFactory factory,  
            int version) {  
        //必须通过super调用父类当中的构造函数  
        super(context, name, factory, version);  
    }  
      
    public DatabaseHelper(Context context, String name, int version){  
        super(context,name,null,version);  
    }  
  
    public DatabaseHelper(Context context, String name){  
    	super(context,name,null, version);  
    }  


	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("create a database"); 
		db.execSQL("CREATE TABLE IF NOT EXISTS attention(id INTEGER PRIMARY KEY AUTOINCREMENT,uid varchar(50),commodityId integer)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
	
	

}
