package com.qh.tools;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
  public static final String DB_ACTION = "db_action";
  
  private static final String DB_NAME = "BD001.db";
  
  private static final int DB_VERSION = 1;
  
  private static DBAdapter mDBAdapter;
  
  private static Resources mResources;
  
  private static Context xContext;
  
  private SQLiteDatabase db;
  
  private DBOpenHelper dbOpenHelper;
  
  private boolean isOpen = false;
  
  public static DBAdapter init(Context paramContext) {
    if (mDBAdapter != null)
      return mDBAdapter; 
    xContext = paramContext;
    mResources = xContext.getResources();
    mDBAdapter = new DBAdapter();
    return mDBAdapter;
  }
  
  public void close() {
    SQLiteDatabase sQLiteDatabase = this.db;
  }
  
  public long deleteAllData(String paramString) {
    return this.db.delete(paramString, null, null);
  }
  
  public long deleteOneData(String paramString1, String paramString2, String[] paramArrayOfString) {
    return this.db.delete(paramString1, paramString2, paramArrayOfString);
  }
  
  public long insert(ContentValues paramContentValues) {
    return (paramContentValues == null) ? -1L : this.db.insert("mygroup", null, paramContentValues);
  }
  
  public long insert(String paramString, ContentValues paramContentValues) {
    return (paramContentValues == null) ? -1L : this.db.insert(paramString, null, paramContentValues);
  }
  
  public void open() throws SQLiteException {
    if (this.isOpen)
      return; 
    this.dbOpenHelper = new DBOpenHelper(xContext, "BD001.db", null, 1);
    try {
      this.db = this.dbOpenHelper.getWritableDatabase();
    } catch (SQLiteException sQLiteException) {
      this.db = this.dbOpenHelper.getReadableDatabase();
    } 
  }
  
  public Cursor queryAllData() {
    return this.db.query("mygroup", null, null, null, null, null, null);
  }
  
  public Cursor queryAllData(String paramString) {
    return this.db.query(paramString, null, null, null, null, null, null);
  }
  
  public Cursor queryAllData(String[] paramArrayOfString) {
    return this.db.query("mygroup", paramArrayOfString, null, null, null, null, null);
  }
  
  public Cursor queryDataByGroup(int paramInt) {
    SQLiteDatabase sQLiteDatabase = this.db;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("lightgroup=");
    stringBuilder.append(paramInt);
    return sQLiteDatabase.query("mylight", null, stringBuilder.toString(), null, null, null, null);
  }
  
  public Cursor queryDataByMAC(String paramString) {
    SQLiteDatabase sQLiteDatabase = this.db;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Mac='");
    stringBuilder.append(paramString);
    stringBuilder.append("'");
    return sQLiteDatabase.query("mylight", null, stringBuilder.toString(), null, null, null, null);
  }
  
  public int upDataforTable(String paramString1, ContentValues paramContentValues, String paramString2, String[] paramArrayOfString) {
    return this.db.update(paramString1, paramContentValues, paramString2, paramArrayOfString);
  }
  
  private static class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DB_CREATE = "CREATE TABLE mygroup (_id integer primary key autoincrement, groupName varchar );";
    
    private static final String LIGHT_DB_CREATE = "CREATE TABLE mylight (Mac varchar primary key, LightName varchar , lightgroup integer);";
    
    public DBOpenHelper(Context param1Context, String param1String, SQLiteDatabase.CursorFactory param1CursorFactory, int param1Int) {
      super(param1Context, param1String, param1CursorFactory, param1Int);
    }
    
    public void onCreate(SQLiteDatabase param1SQLiteDatabase) {
      param1SQLiteDatabase.execSQL("CREATE TABLE mygroup (_id integer primary key autoincrement, groupName varchar );");
      param1SQLiteDatabase.execSQL("CREATE TABLE mylight (Mac varchar primary key, LightName varchar , lightgroup integer);");
    }
    
    public void onUpgrade(SQLiteDatabase param1SQLiteDatabase, int param1Int1, int param1Int2) {
      param1SQLiteDatabase.execSQL("DROP TABLE IF EXISTS mygroup");
      onCreate(param1SQLiteDatabase);
      Log.e("db_action", "Upgrade");
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\tools\DBAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */