package com.qh.managegroup;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.qh.blelight.MyApplication;
import com.qh.tools.DBAdapter;
import com.qh.ui.dialog.EditTextDialog;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.regex.Pattern;

public class DragListActivity extends Activity {
  private DragListAdapter adapter = null;
  
  private ArrayList<MyListData> data = new ArrayList<MyListData>();
  
  public DBAdapter dbAdapter;
  
  private RelativeLayout lin_back;
  
  public Handler mHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message param1Message) {
          if (param1Message.what == 1) {
            DragListActivity.this.initData();
            DragListActivity.this.adapter.notifyDataSetChanged();
          } 
          return false;
        }
      });
  
  public MyApplication mMyApplication;
  
  public Resources mResources;
  
  public Context mcontext;
  
  private RelativeLayout rel_add;
  
  private RelativeLayout rel_main;
  
  private void init() {
    this.lin_back = (RelativeLayout)findViewById(2131230957);
    this.rel_add = (RelativeLayout)findViewById(2131231054);
    this.rel_main = (RelativeLayout)findViewById(2131231070);
    if (this.mMyApplication.bgsrc.length > this.mMyApplication.typebg)
      this.rel_main.setBackgroundResource(this.mMyApplication.bgsrc[this.mMyApplication.typebg]); 
    this.lin_back.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            Intent intent = new Intent();
            intent.putExtra("result", 1);
            DragListActivity.this.setResult(-1, intent);
            DragListActivity.this.finish();
          }
        });
    this.rel_add.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            (new EditTextDialog((Context)DragListActivity.this, DragListActivity.this.getString(2131623998), "", "", "", new EditTextDialog.ISingleDialogEnsureClickListener1() {
                  public void onEnsureClick(String param2String) {
                    StringBuilder stringBuilder2;
                    Context context;
                    StringBuilder stringBuilder1;
                    if ("".equals(param2String))
                      return; 
                    if (DragListActivity.this.mResources.getString(2131623941).equals(param2String)) {
                      Context context1 = DragListActivity.this.mcontext;
                      stringBuilder2 = new StringBuilder();
                      stringBuilder2.append("");
                      stringBuilder2.append(DragListActivity.this.mResources.getString(2131624019));
                      Toast.makeText(context1, stringBuilder2.toString(), 1).show();
                      return;
                    } 
                    Cursor cursor = DragListActivity.this.dbAdapter.queryAllData("mygroup");
                    while (cursor.moveToNext()) {
                      if (stringBuilder2.equals(cursor.getString(cursor.getColumnIndex("groupName")))) {
                        context = DragListActivity.this.mcontext;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("");
                        stringBuilder.append(DragListActivity.this.mResources.getString(2131624019));
                        Toast.makeText(context, stringBuilder.toString(), 1).show();
                        return;
                      } 
                    } 
                    cursor = DragListActivity.this.dbAdapter.queryAllData("mylight");
                    while (cursor.moveToNext()) {
                      cursor.getString(cursor.getColumnIndex("LightName"));
                      if (context.equals(cursor)) {
                        Context context1 = DragListActivity.this.mcontext;
                        stringBuilder1 = new StringBuilder();
                        stringBuilder1.append("");
                        stringBuilder1.append(DragListActivity.this.mResources.getString(2131624019));
                        Toast.makeText(context1, stringBuilder1.toString(), 1).show();
                        return;
                      } 
                    } 
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("name = ");
                    stringBuilder3.append((String)stringBuilder1);
                    Log.e("", stringBuilder3.toString());
                    ContentValues contentValues = new ContentValues();
                    stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("");
                    stringBuilder3.append((String)stringBuilder1);
                    contentValues.put("groupName", stringBuilder3.toString());
                    DragListActivity.this.dbAdapter.insert("mygroup", contentValues);
                    DragListActivity.this.initData();
                    DragListActivity.this.adapter.notifyDataSetChanged();
                  }
                })).show();
          }
        });
  }
  
  public void initData() {
    int i;
    new Hashtable<Object, Object>();
    this.data.clear();
    MyListData myListData = new MyListData();
    myListData.name = this.mResources.getString(2131623941);
    myListData.isGroup = true;
    byte b = 0;
    myListData.groupId = 0;
    myListData.addr = "0";
    this.data.add(myListData);
    Hashtable<Object, Object> hashtable = new Hashtable<Object, Object>();
    Cursor cursor2 = this.dbAdapter.queryDataByGroup(0);
    while (cursor2.moveToNext()) {
      i = cursor2.getInt(cursor2.getColumnIndex("lightgroup"));
      String str1 = cursor2.getString(cursor2.getColumnIndex("Mac"));
      String str2 = cursor2.getString(cursor2.getColumnIndex("LightName"));
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("mac = ");
      stringBuilder.append(str1);
      stringBuilder.append(" myGroup =  ");
      stringBuilder.append(i);
      stringBuilder.append(" name = ");
      stringBuilder.append(str2);
      Log.e("", stringBuilder.toString());
      MyListData myListData1 = new MyListData();
      myListData1.isGroup = false;
      myListData1.addr = str1;
      myListData1.name = str2;
      myListData1.groupId = i;
      hashtable.put(str1, myListData1);
    } 
    if (this.mMyApplication.mBluetoothLeService == null)
      return; 
    Iterator<String> iterator = this.mMyApplication.mBluetoothLeService.mDevices.keySet().iterator();
    Pattern pattern = Pattern.compile("^Triones-A00100|^Triones-A10100|^Triones-A20100|^Triones-A30100|^Triones-A40100|^Triones-A50100|^Triones-A60100|^Triones-A70100|^Triones-A80100|^Triones-A90100|^Triones-AA0100|^Triones-AB0100|^Triones-AC0100|^Triones-AD0100|^Triones-AE0100|^Triones-AF0100");
    while (iterator.hasNext()) {
      String str = iterator.next();
      if (hashtable.containsKey(str)) {
        this.data.add((MyListData)hashtable.get(str));
        continue;
      } 
      if (this.dbAdapter.queryDataByMAC(str).getCount() == 0) {
        BluetoothDevice bluetoothDevice = (BluetoothDevice)this.mMyApplication.mBluetoothLeService.mDevices.get(str);
        MyListData myListData1 = new MyListData();
        myListData1.isGroup = false;
        myListData1.addr = bluetoothDevice.getAddress();
        myListData1.name = bluetoothDevice.getName();
        if (!TextUtils.isEmpty(myListData1.name) && pattern.matcher(myListData1.name).find())
          myListData1.name = "LD-0003"; 
        myListData1.groupId = 0;
        this.data.add(myListData1);
      } 
    } 
    Cursor cursor1 = this.dbAdapter.queryAllData("mygroup");
    while (true) {
      i = b;
      if (cursor1.moveToNext()) {
        i = cursor1.getInt(cursor1.getColumnIndex("_id"));
        String str = cursor1.getString(cursor1.getColumnIndex("groupName"));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("mac = ");
        stringBuilder.append(i);
        stringBuilder.append(" myGroup =  ");
        stringBuilder.append(str);
        Log.e("", stringBuilder.toString());
        MyListData myListData1 = new MyListData();
        myListData1.name = str;
        myListData1.isGroup = true;
        myListData1.groupId = i;
        if (i == 0)
          continue; 
        this.data.add(myListData1);
        Cursor cursor = this.dbAdapter.queryDataByGroup(i);
        while (cursor.moveToNext()) {
          i = cursor.getInt(cursor.getColumnIndex("lightgroup"));
          String str1 = cursor.getString(cursor.getColumnIndex("Mac"));
          str = cursor.getString(cursor.getColumnIndex("LightName"));
          MyListData myListData2 = new MyListData();
          myListData2.isGroup = false;
          myListData2.addr = str1;
          myListData2.name = str;
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append("initData: ");
          stringBuilder1.append(str);
          Log.e(" = = ==  ", stringBuilder1.toString());
          myListData2.groupId = i;
          if (this.mMyApplication.mBluetoothLeService.mDevices.containsKey(str1))
            this.data.add(myListData2); 
        } 
        continue;
      } 
      break;
    } 
    while (i < this.data.size()) {
      MyListData myListData1 = this.data.get(i);
      if (myListData1 != null && myListData1.groupId != 0)
        myListData1.isdel = true; 
      i++;
    } 
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131361865);
    Log.e("", "11");
    this.mResources = getResources();
    this.mcontext = (Context)this;
    this.mMyApplication = (MyApplication)getApplication();
    this.dbAdapter = DBAdapter.init((Context)this);
    this.dbAdapter.open();
    init();
    initData();
    DragListView dragListView = (DragListView)findViewById(2131231006);
    this.adapter = new DragListAdapter((Context)this, this.data);
    dragListView.setAdapter((ListAdapter)this.adapter);
    dragListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
          public boolean onItemLongClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("position = ");
            stringBuilder.append(param1Int);
            Log.e("long", stringBuilder.toString());
            ItemInfo itemInfo = DragListActivity.this.adapter.ItemInfos.get(Integer.valueOf(param1Int));
            if (param1Int != 0 && itemInfo != null && itemInfo.tx_delete != null) {
              MyListData myListData = (MyListData)itemInfo.light_img.getTag();
              if (myListData != null && myListData.groupId != 0)
                itemInfo.tx_delete.setVisibility(0); 
            } 
            return true;
          }
        });
    dragListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onItemClick = ");
            stringBuilder.append(param1Int);
            Log.e("", stringBuilder.toString());
            ItemInfo itemInfo = DragListActivity.this.adapter.ItemInfos.get(Integer.valueOf(param1Int));
            if (param1Int != 0 && itemInfo != null && itemInfo.tx_delete != null)
              itemInfo.tx_delete.setVisibility(8); 
          }
        });
    dragListView.setDragListChange(new DragListView.DragListChange() {
          public void change(MyListData param1MyListData1, MyListData param1MyListData2) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("lightgroup", Integer.valueOf(param1MyListData2.groupId));
            contentValues.put("Mac", param1MyListData1.addr);
            contentValues.put("LightName", param1MyListData1.name);
            long l = DragListActivity.this.dbAdapter.insert("mylight", contentValues);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("kkkkk = ");
            stringBuilder.append(l);
            Log.e("", stringBuilder.toString());
            if (l == -1L) {
              String str = param1MyListData1.addr;
              ContentValues contentValues1 = new ContentValues();
              contentValues1.put("lightgroup", Integer.valueOf(param1MyListData2.groupId));
              DragListActivity.this.dbAdapter.upDataforTable("mylight", contentValues1, "Mac=?", new String[] { str });
            } 
            DragListActivity.this.mHandler.sendEmptyMessage(1);
          }
          
          public void delect(MyListData param1MyListData) {
            String str;
            if (param1MyListData == null)
              return; 
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("");
            stringBuilder.append(param1MyListData.isGroup);
            Log.e("", stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append("");
            stringBuilder.append(param1MyListData.groupId);
            Log.e("", stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append("");
            stringBuilder.append(param1MyListData.name);
            Log.e("", stringBuilder.toString());
            if (param1MyListData.isGroup) {
              stringBuilder = new StringBuilder();
              stringBuilder.append("");
              stringBuilder.append(param1MyListData.groupId);
              String str1 = stringBuilder.toString();
              DragListActivity.this.dbAdapter.deleteOneData("mygroup", "_id=?", new String[] { str1 });
              StringBuilder stringBuilder1 = new StringBuilder();
              stringBuilder1.append("");
              stringBuilder1.append(param1MyListData.groupId);
              str = stringBuilder1.toString();
              DragListActivity.this.dbAdapter.deleteOneData("mylight", "lightgroup=?", new String[] { str });
            } else {
              str = ((MyListData)str).addr;
              DragListActivity.this.dbAdapter.deleteOneData("mylight", "Mac=?", new String[] { str });
            } 
            DragListActivity.this.mHandler.sendEmptyMessage(1);
          }
          
          public void resetname(final MyListData data) {
            if (data == null)
              return; 
            if (!TextUtils.isEmpty(data.name))
              String str = data.name; 
            (new EditTextDialog((Context)DragListActivity.this, DragListActivity.this.getString(2131624061), "", "", "", new EditTextDialog.ISingleDialogEnsureClickListener1() {
                  public void onEnsureClick(String param2String) {
                    Context context;
                    if ("".equals(param2String))
                      return; 
                    if (DragListActivity.this.mResources.getString(2131623941).equals(param2String)) {
                      context = DragListActivity.this.mcontext;
                      StringBuilder stringBuilder = new StringBuilder();
                      stringBuilder.append("");
                      stringBuilder.append(DragListActivity.this.mResources.getString(2131624019));
                      Toast.makeText(context, stringBuilder.toString(), 1).show();
                      return;
                    } 
                    Cursor cursor = DragListActivity.this.dbAdapter.queryAllData("mygroup");
                    while (cursor.moveToNext()) {
                      if (context.equals(cursor.getString(cursor.getColumnIndex("groupName")))) {
                        context = DragListActivity.this.mcontext;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("");
                        stringBuilder.append(DragListActivity.this.mResources.getString(2131624019));
                        Toast.makeText(context, stringBuilder.toString(), 1).show();
                        return;
                      } 
                    } 
                    cursor = DragListActivity.this.dbAdapter.queryAllData("mylight");
                    while (cursor.moveToNext()) {
                      cursor.getString(cursor.getColumnIndex("LightName"));
                      if (context.equals(cursor)) {
                        context = DragListActivity.this.mcontext;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("");
                        stringBuilder.append(DragListActivity.this.mResources.getString(2131624019));
                        Toast.makeText(context, stringBuilder.toString(), 1).show();
                        return;
                      } 
                    } 
                    if (data.isGroup) {
                      StringBuilder stringBuilder = new StringBuilder();
                      stringBuilder.append("");
                      stringBuilder.append(data.groupId);
                      String str = stringBuilder.toString();
                      ContentValues contentValues = new ContentValues();
                      contentValues.put("groupName", (String)context);
                      DragListActivity.this.dbAdapter.upDataforTable("mygroup", contentValues, "_id=?", new String[] { str });
                    } else {
                      StringBuilder stringBuilder = new StringBuilder();
                      stringBuilder.append("");
                      stringBuilder.append(data.addr);
                      String str = stringBuilder.toString();
                      ContentValues contentValues = new ContentValues();
                      contentValues.put("LightName", (String)context);
                      if (DragListActivity.this.dbAdapter.upDataforTable("mylight", contentValues, "Mac=?", new String[] { str }) == 0) {
                        contentValues = new ContentValues();
                        contentValues.put("lightgroup", Integer.valueOf(0));
                        contentValues.put("Mac", data.addr);
                        contentValues.put("LightName", (String)context);
                        DragListActivity.this.dbAdapter.insert("mylight", contentValues);
                      } 
                    } 
                    DragListActivity.this.mHandler.sendEmptyMessage(1);
                  }
                })).show();
          }
        });
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\managegroup\DragListActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */