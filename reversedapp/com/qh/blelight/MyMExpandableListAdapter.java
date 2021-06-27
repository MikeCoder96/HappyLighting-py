package com.qh.blelight;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.qh.data.MyChild;
import com.qh.tools.DBAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MyMExpandableListAdapter extends BaseExpandableListAdapter {
  public HashMap<String, MyChild> DBdata = new HashMap<String, MyChild>();
  
  private ArrayList<ArrayList> children = new ArrayList<ArrayList>();
  
  public DBAdapter dbAdapter;
  
  public int groupH = 121;
  
  private ArrayList<Integer> groupID = new ArrayList<Integer>();
  
  private HashMap<Integer, View> groupItemViews = new HashMap<Integer, View>();
  
  private ArrayList<String> groupNames = new ArrayList<String>();
  
  public HashMap<Integer, HashMap> groupViews = new HashMap<Integer, HashMap>();
  
  public ArrayList<Boolean> isExpandeds = new ArrayList<Boolean>();
  
  private Activity mActivity;
  
  private BluetoothLeService mBluetoothLeService;
  
  private Context mContext;
  
  public Handler mHandler;
  
  private LayoutInflater mInflator;
  
  public GroupViewHolder mShowGroupViewHolder;
  
  public HashMap<Integer, View> mViews = new HashMap<Integer, View>();
  
  public View moveV;
  
  MyMExpandableListAdapter(Context paramContext, Activity paramActivity, DBAdapter paramDBAdapter, BluetoothLeService paramBluetoothLeService) {
    this.mContext = paramContext;
    this.mInflator = paramActivity.getLayoutInflater();
    this.dbAdapter = paramDBAdapter;
    this.mBluetoothLeService = paramBluetoothLeService;
  }
  
  public Object getChild(int paramInt1, int paramInt2) {
    return ((ArrayList)this.children.get(paramInt1)).get(paramInt2);
  }
  
  public long getChildId(int paramInt1, int paramInt2) {
    return paramInt2;
  }
  
  public View getChildView(int paramInt1, int paramInt2, boolean paramBoolean, View paramView, ViewGroup paramViewGroup) {
    MChildViewHolder mChildViewHolder;
    if (!this.groupViews.containsKey(Integer.valueOf(paramInt1)))
      this.groupViews.put(Integer.valueOf(paramInt1), new HashMap<Object, Object>()); 
    HashMap<Integer, View> hashMap = this.groupViews.get(Integer.valueOf(paramInt1));
    if (hashMap.containsKey(Integer.valueOf(paramInt2))) {
      paramView = (View)hashMap.get(Integer.valueOf(paramInt2));
    } else {
      paramView = null;
    } 
    if (paramView == null) {
      paramView = this.mInflator.inflate(2131361867, null);
      mChildViewHolder = new MChildViewHolder();
      mChildViewHolder.groupPosition = paramInt1;
      mChildViewHolder.childPosition = paramInt2;
      mChildViewHolder.light_name_tx = (TextView)paramView.findViewById(2131230952);
      mChildViewHolder.device_img = (ImageView)paramView.findViewById(2131230951);
      mChildViewHolder.device_img.setOnTouchListener((new moveOnTouchListener()).setViewHolder(paramView));
      mChildViewHolder.tx_delete = (TextView)paramView.findViewById(2131231241);
      mChildViewHolder.tx_delete.setVisibility(8);
      mChildViewHolder.item_resetname = (ImageView)paramView.findViewById(2131230944);
      paramView.setTag(mChildViewHolder);
      hashMap.put(Integer.valueOf(paramInt2), paramView);
      this.groupViews.put(Integer.valueOf(paramInt1), hashMap);
    } else {
      mChildViewHolder = (MChildViewHolder)paramView.getTag();
    } 
    String str = "";
    ArrayList arrayList = this.children.get(paramInt1);
    if (arrayList.size() > paramInt2)
      str = ((MyChild)arrayList.get(paramInt2)).name; 
    paramView.setTag(2131230821, Boolean.valueOf(false));
    paramView.setTag(2131230825, Integer.valueOf(paramInt1));
    paramView.setTag(2131230826, Integer.valueOf(paramInt2));
    paramView.setTag(2131230827, mChildViewHolder);
    TextView textView = mChildViewHolder.light_name_tx;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("");
    stringBuilder.append(str);
    textView.setText(stringBuilder.toString());
    mChildViewHolder.tx_delete.setOnClickListener((new delectOnClickListener()).setPos(paramInt1, paramInt2));
    mChildViewHolder.item_resetname.setOnClickListener((new OnResetNameChildClickListener()).setChildViewHolder(mChildViewHolder).setPos(paramInt1, paramInt2));
    return paramView;
  }
  
  public int getChildrenCount(int paramInt) {
    if (this.children.size() > paramInt) {
      ArrayList arrayList = this.children.get(paramInt);
      if (arrayList != null)
        return arrayList.size(); 
    } 
    return 0;
  }
  
  public Object getGroup(int paramInt) {
    return this.groupNames.get(paramInt);
  }
  
  public int getGroupCount() {
    return this.groupNames.size();
  }
  
  public long getGroupId(int paramInt) {
    return paramInt;
  }
  
  public View getGroupView(int paramInt, boolean paramBoolean, View paramView, ViewGroup paramViewGroup) {
    GroupViewHolder groupViewHolder1;
    GroupViewHolder groupViewHolder2;
    String str;
    View view = this.groupItemViews.get(Integer.valueOf(paramInt));
    if (this.groupItemViews.containsKey(Integer.valueOf(paramInt))) {
      paramView = this.groupItemViews.get(Integer.valueOf(paramInt));
    } else {
      paramView = null;
    } 
    if (paramView == null) {
      View view1 = this.mInflator.inflate(2131361866, null);
      view1.setOnTouchListener((new OnGroupTouchListener()).setGroupPosition(paramInt));
      groupViewHolder1 = new GroupViewHolder();
      groupViewHolder1.groupPosition = paramInt;
      groupViewHolder1.groupPosition = paramInt;
      groupViewHolder1.group_name_tx = (TextView)view1.findViewById(2131230808);
      groupViewHolder1.arrow_img = (ImageView)view1.findViewById(2131230754);
      groupViewHolder1.reset_name_tx = (ImageView)view1.findViewById(2131231098);
      groupViewHolder1.reset_name_tx.setOnClickListener((new OnResetNameClickListener()).setGroupPosition(paramInt).setGroupViewHolder(groupViewHolder1));
      groupViewHolder1.delect = (TextView)view1.findViewById(2131231241);
      view1.setTag(groupViewHolder1);
      this.groupItemViews.put(Integer.valueOf(paramInt), view);
    } else {
      GroupViewHolder groupViewHolder = (GroupViewHolder)groupViewHolder1.getTag();
      groupViewHolder2 = groupViewHolder1;
      groupViewHolder1 = groupViewHolder;
    } 
    if (paramInt == 0)
      groupViewHolder1.reset_name_tx.setVisibility(4); 
    if (paramBoolean) {
      groupViewHolder1.arrow_img.setImageResource(2131165270);
    } else {
      groupViewHolder1.arrow_img.setImageResource(2131165269);
    } 
    if (this.isExpandeds.size() > paramInt)
      this.isExpandeds.set(paramInt, Boolean.valueOf(paramBoolean)); 
    groupViewHolder2.setTag(2131230821, Boolean.valueOf(true));
    groupViewHolder2.setTag(2131230825, Integer.valueOf(paramInt));
    groupViewHolder2.setTag(2131230826, Integer.valueOf(paramInt));
    groupViewHolder2.setTag(2131230827, groupViewHolder1);
    this.mViews.put(Integer.valueOf(paramInt), groupViewHolder2);
    if (this.groupNames.size() > paramInt) {
      str = this.groupNames.get(paramInt);
    } else {
      str = "error!";
    } 
    TextView textView = groupViewHolder1.group_name_tx;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("");
    stringBuilder.append(str);
    textView.setText(stringBuilder.toString());
    groupViewHolder1.delect.setOnClickListener((new delectGroupOnClickListener()).setPos(paramInt));
    return (View)groupViewHolder2;
  }
  
  public boolean hasStableIds() {
    return true;
  }
  
  public boolean isChildSelectable(int paramInt1, int paramInt2) {
    return true;
  }
  
  public void move(int paramInt1, int paramInt2, int paramInt3) {
    if (paramInt2 == -1)
      return; 
    if (this.children.size() > paramInt1 && this.children.size() > paramInt2) {
      ArrayList<MyChild> arrayList1 = this.children.get(paramInt1);
      ArrayList<MyChild> arrayList2 = this.children.get(paramInt2);
      if (arrayList1.size() > paramInt3) {
        MyChild myChild = arrayList1.get(paramInt3);
        arrayList1.remove(paramInt3);
        arrayList2.add(myChild);
        this.children.set(paramInt1, arrayList1);
        this.children.set(paramInt2, arrayList2);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("name--->");
        stringBuilder.append(myChild.mac);
        Log.e("", stringBuilder.toString());
        if (paramInt1 == 0) {
          ContentValues contentValues = new ContentValues();
          contentValues.put("lightgroup", this.groupID.get(paramInt2));
          contentValues.put("Mac", myChild.mac);
          contentValues.put("LightName", myChild.name);
          long l = this.dbAdapter.insert("mylight", contentValues);
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append("kkkkk = ");
          stringBuilder1.append(l);
          Log.e("", stringBuilder1.toString());
          if (l == -1L) {
            String str = myChild.mac;
            ContentValues contentValues1 = new ContentValues();
            contentValues1.put("lightgroup", this.groupID.get(paramInt2));
            this.dbAdapter.upDataforTable("mylight", contentValues1, "Mac=?", new String[] { str });
          } 
        } else {
          String str = myChild.mac;
          ContentValues contentValues = new ContentValues();
          contentValues.put("lightgroup", this.groupID.get(paramInt2));
          this.dbAdapter.upDataforTable("mylight", contentValues, "Mac=?", new String[] { str });
        } 
        if (paramInt2 == 0) {
          String str = myChild.mac;
          ContentValues contentValues = new ContentValues();
          contentValues.put("lightgroup", Integer.valueOf(1));
          this.dbAdapter.upDataforTable("mylight", contentValues, "Mac=?", new String[] { str });
        } 
        notifyDataSetChanged();
      } 
    } 
  }
  
  public void setgroupNames() {
    this.groupID.clear();
    this.groupNames.clear();
    Cursor cursor1 = this.dbAdapter.queryAllData("mygroup");
    while (cursor1.moveToNext()) {
      int k = cursor1.getInt(cursor1.getColumnIndex("_id"));
      String str = cursor1.getString(cursor1.getColumnIndex("groupName"));
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("mac = ");
      stringBuilder.append(k);
      stringBuilder.append(" myGroup =  ");
      stringBuilder.append(str);
      Log.e("", stringBuilder.toString());
      this.groupID.add(Integer.valueOf(k));
      this.groupNames.add(str);
    } 
    Cursor cursor2 = this.dbAdapter.queryAllData("mylight");
    while (cursor2.moveToNext()) {
      int k = cursor2.getInt(cursor2.getColumnIndex("lightgroup"));
      String str1 = cursor2.getString(cursor2.getColumnIndex("Mac"));
      String str2 = cursor2.getString(cursor2.getColumnIndex("LightName"));
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("mac = ");
      stringBuilder.append(str1);
      stringBuilder.append(" myGroup =  ");
      stringBuilder.append(k);
      stringBuilder.append(" name = ");
      stringBuilder.append(str2);
      Log.e("", stringBuilder.toString());
    } 
    this.isExpandeds.clear();
    int j = 0;
    int i;
    for (i = 0; i < this.groupNames.size(); i++) {
      if (this.isExpandeds.size() < this.groupNames.size())
        this.isExpandeds.add(Boolean.valueOf(false)); 
    } 
    this.children.clear();
    for (i = 0; i < this.groupID.size(); i++) {
      int k = ((Integer)this.groupID.get(i)).intValue();
      cursor2 = this.dbAdapter.queryDataByGroup(k);
      while (cursor2.moveToNext()) {
        MyChild myChild = new MyChild();
        myChild.mac = cursor2.getString(cursor2.getColumnIndex("Mac"));
        myChild.myGroup = cursor2.getInt(cursor2.getColumnIndex("lightgroup"));
        myChild.name = cursor2.getString(cursor2.getColumnIndex("LightName"));
        this.DBdata.put(myChild.mac, myChild);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("mac = ");
        stringBuilder.append(myChild.mac);
        Log.e("", stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("myGroup = ");
        stringBuilder.append(myChild.myGroup);
        Log.e("", stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("name = ");
        stringBuilder.append(myChild.name);
        Log.e("", stringBuilder.toString());
      } 
    } 
    if (this.mBluetoothLeService == null)
      return; 
    Iterator<String> iterator = this.mBluetoothLeService.mDevices.keySet().iterator();
    while (true) {
      i = j;
      if (iterator.hasNext()) {
        String str = iterator.next();
        BluetoothDevice bluetoothDevice = this.mBluetoothLeService.mDevices.get(str);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("addr :");
        stringBuilder.append(str);
        Log.e("", stringBuilder.toString());
        if (!this.DBdata.containsKey(str)) {
          str.substring(str.length() - 8, str.length());
          MyChild myChild = new MyChild();
          myChild.mac = str;
          myChild.myGroup = 1;
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append("");
          stringBuilder1.append(bluetoothDevice.getName());
          myChild.name = stringBuilder1.toString();
          this.DBdata.put(myChild.mac, myChild);
        } 
        continue;
      } 
      break;
    } 
    while (i < this.groupID.size()) {
      j = ((Integer)this.groupID.get(i)).intValue();
      ArrayList<MyChild> arrayList = new ArrayList();
      for (String str : this.DBdata.keySet()) {
        MyChild myChild = this.DBdata.get(str);
        if (j == myChild.myGroup)
          arrayList.add(myChild); 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("name = ");
        stringBuilder.append(myChild.name);
        stringBuilder.append(" myGroup = ");
        stringBuilder.append(myChild.myGroup);
        Log.e("", stringBuilder.toString());
      } 
      this.children.add(arrayList);
      i++;
    } 
  }
  
  public static class GroupViewHolder {
    public ImageView arrow_img;
    
    public TextView delect;
    
    public int groupPosition;
    
    public TextView group_name_tx;
    
    public ImageView reset_name_tx;
  }
  
  public static class MChildViewHolder {
    public int childPosition;
    
    public ImageView device_img;
    
    public int groupPosition;
    
    public ImageView item_resetname;
    
    public TextView light_name_tx;
    
    public TextView tx_delete;
  }
  
  private class OnChildTouchListener implements View.OnTouchListener {
    private View convertView;
    
    public MyMExpandableListAdapter.MChildViewHolder mChildViewHolder;
    
    private float x;
    
    private float y;
    
    public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
      switch (param1MotionEvent.getAction()) {
        case 2:
          this.mChildViewHolder = (MyMExpandableListAdapter.MChildViewHolder)param1View.getTag();
          if (this.x - param1MotionEvent.getX() > 30.0F) {
            Log.e("", "<---1---L");
            this.mChildViewHolder.tx_delete.setVisibility(8);
          } 
          if (param1MotionEvent.getX() - this.x > 30.0F) {
            Log.e("", "L----1--->");
            this.mChildViewHolder.tx_delete.setVisibility(0);
          } 
          break;
        case 0:
          this.x = param1MotionEvent.getX();
          break;
      } 
      return false;
    }
    
    public OnChildTouchListener setOnChildTouchListener(View param1View) {
      this.convertView = param1View;
      return this;
    }
  }
  
  private class OnGroupTouchListener implements View.OnTouchListener {
    private int groupPosition;
    
    private OnGroupTouchListener() {}
    
    public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Group-onTouch ");
      stringBuilder.append(this.groupPosition);
      Log.e("", stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("OnTouchListener - ");
      stringBuilder.append(param1View.getHeight());
      Log.e("", stringBuilder.toString());
      return false;
    }
    
    public OnGroupTouchListener setGroupPosition(int param1Int) {
      this.groupPosition = param1Int;
      return this;
    }
  }
  
  private class OnResetNameChildClickListener implements View.OnClickListener {
    private int childPosition;
    
    private int groupPosition;
    
    private MyMExpandableListAdapter.MChildViewHolder mMChildViewHolder;
    
    private OnResetNameChildClickListener() {}
    
    public void onClick(View param1View) {
      final EditText et = new EditText(MyMExpandableListAdapter.this.mContext);
      AlertDialog.Builder builder = new AlertDialog.Builder(MyMExpandableListAdapter.this.mContext);
      StringBuilder stringBuilder2 = new StringBuilder();
      stringBuilder2.append("");
      stringBuilder2.append(MyMExpandableListAdapter.this.mContext.getResources().getString(2131623948));
      builder = builder.setTitle(stringBuilder2.toString()).setIcon(17301659).setView((View)editText);
      stringBuilder2 = new StringBuilder();
      stringBuilder2.append("");
      stringBuilder2.append(MyMExpandableListAdapter.this.mContext.getResources().getString(2131624047));
      builder = builder.setPositiveButton(stringBuilder2.toString(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface param2DialogInterface, int param2Int) {
              String str = et.getText().toString();
              if ("".equals(str))
                return; 
              if (MyMExpandableListAdapter.this.children.size() > MyMExpandableListAdapter.OnResetNameChildClickListener.this.groupPosition) {
                ArrayList<MyChild> arrayList = MyMExpandableListAdapter.this.children.get(MyMExpandableListAdapter.OnResetNameChildClickListener.this.groupPosition);
                if (arrayList != null && arrayList.size() > MyMExpandableListAdapter.OnResetNameChildClickListener.this.childPosition) {
                  MyChild myChild = arrayList.get(MyMExpandableListAdapter.OnResetNameChildClickListener.this.childPosition);
                  if (myChild != null) {
                    StringBuilder stringBuilder4 = new StringBuilder();
                    stringBuilder4.append("mMyChild.mac = ");
                    stringBuilder4.append(myChild.mac);
                    stringBuilder4.append(" mMyChild.myGroup = ");
                    stringBuilder4.append(myChild.myGroup);
                    Log.e("", stringBuilder4.toString());
                    stringBuilder4 = new StringBuilder();
                    stringBuilder4.append("groupPosition = ");
                    stringBuilder4.append(MyMExpandableListAdapter.OnResetNameChildClickListener.this.groupPosition);
                    stringBuilder4.append(" childPosition = ");
                    stringBuilder4.append(MyMExpandableListAdapter.OnResetNameChildClickListener.this.childPosition);
                    Log.e("", stringBuilder4.toString());
                    stringBuilder4 = new StringBuilder();
                    stringBuilder4.append("");
                    stringBuilder4.append(myChild.mac);
                    String str1 = stringBuilder4.toString();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("LightName", str);
                    param2Int = MyMExpandableListAdapter.this.dbAdapter.upDataforTable("mylight", contentValues, "Mac=?", new String[] { str1 });
                    if (param2Int == 0) {
                      contentValues = new ContentValues();
                      contentValues.put("lightgroup", Integer.valueOf(1));
                      contentValues.put("Mac", myChild.mac);
                      contentValues.put("LightName", str);
                      MyMExpandableListAdapter.this.dbAdapter.insert("mylight", contentValues);
                    } 
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("k = ");
                    stringBuilder2.append(param2Int);
                    Log.e("", stringBuilder2.toString());
                    TextView textView = MyMExpandableListAdapter.OnResetNameChildClickListener.this.mMChildViewHolder.light_name_tx;
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("");
                    stringBuilder3.append(str);
                    textView.setText(stringBuilder3.toString());
                    MyMExpandableListAdapter.this.mHandler.sendEmptyMessage(1);
                    StringBuilder stringBuilder1 = new StringBuilder();
                    stringBuilder1.append("-- ");
                    stringBuilder1.append(et.getText());
                    Log.e("", stringBuilder1.toString());
                  } 
                } 
              } 
            }
          });
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("");
      stringBuilder1.append(MyMExpandableListAdapter.this.mContext.getResources().getString(2131624006));
      builder.setNegativeButton(stringBuilder1.toString(), null).show();
    }
    
    public OnResetNameChildClickListener setChildViewHolder(MyMExpandableListAdapter.MChildViewHolder param1MChildViewHolder) {
      this.mMChildViewHolder = param1MChildViewHolder;
      return this;
    }
    
    public OnResetNameChildClickListener setPos(int param1Int1, int param1Int2) {
      this.groupPosition = param1Int1;
      this.childPosition = param1Int2;
      return this;
    }
  }
  
  class null implements DialogInterface.OnClickListener {
    public void onClick(DialogInterface param1DialogInterface, int param1Int) {
      String str = et.getText().toString();
      if ("".equals(str))
        return; 
      if (MyMExpandableListAdapter.this.children.size() > this.this$1.groupPosition) {
        ArrayList<MyChild> arrayList = MyMExpandableListAdapter.this.children.get(this.this$1.groupPosition);
        if (arrayList != null && arrayList.size() > this.this$1.childPosition) {
          MyChild myChild = arrayList.get(this.this$1.childPosition);
          if (myChild != null) {
            StringBuilder stringBuilder4 = new StringBuilder();
            stringBuilder4.append("mMyChild.mac = ");
            stringBuilder4.append(myChild.mac);
            stringBuilder4.append(" mMyChild.myGroup = ");
            stringBuilder4.append(myChild.myGroup);
            Log.e("", stringBuilder4.toString());
            stringBuilder4 = new StringBuilder();
            stringBuilder4.append("groupPosition = ");
            stringBuilder4.append(this.this$1.groupPosition);
            stringBuilder4.append(" childPosition = ");
            stringBuilder4.append(this.this$1.childPosition);
            Log.e("", stringBuilder4.toString());
            stringBuilder4 = new StringBuilder();
            stringBuilder4.append("");
            stringBuilder4.append(myChild.mac);
            String str1 = stringBuilder4.toString();
            ContentValues contentValues = new ContentValues();
            contentValues.put("LightName", str);
            param1Int = MyMExpandableListAdapter.this.dbAdapter.upDataforTable("mylight", contentValues, "Mac=?", new String[] { str1 });
            if (param1Int == 0) {
              contentValues = new ContentValues();
              contentValues.put("lightgroup", Integer.valueOf(1));
              contentValues.put("Mac", myChild.mac);
              contentValues.put("LightName", str);
              MyMExpandableListAdapter.this.dbAdapter.insert("mylight", contentValues);
            } 
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("k = ");
            stringBuilder2.append(param1Int);
            Log.e("", stringBuilder2.toString());
            TextView textView = this.this$1.mMChildViewHolder.light_name_tx;
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("");
            stringBuilder3.append(str);
            textView.setText(stringBuilder3.toString());
            MyMExpandableListAdapter.this.mHandler.sendEmptyMessage(1);
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append("-- ");
            stringBuilder1.append(et.getText());
            Log.e("", stringBuilder1.toString());
          } 
        } 
      } 
    }
  }
  
  private class OnResetNameClickListener implements View.OnClickListener {
    private int groupPosition;
    
    private MyMExpandableListAdapter.GroupViewHolder mGroupViewHolder;
    
    private OnResetNameClickListener() {}
    
    public void onClick(View param1View) {
      MyMExpandableListAdapter.this.mShowGroupViewHolder = this.mGroupViewHolder;
      final EditText et = new EditText(MyMExpandableListAdapter.this.mContext);
      AlertDialog.Builder builder2 = new AlertDialog.Builder(MyMExpandableListAdapter.this.mContext);
      StringBuilder stringBuilder2 = new StringBuilder();
      stringBuilder2.append("");
      stringBuilder2.append(MyMExpandableListAdapter.this.mContext.getResources().getString(2131623948));
      builder2 = builder2.setTitle(stringBuilder2.toString()).setIcon(17301659).setView((View)editText);
      stringBuilder2 = new StringBuilder();
      stringBuilder2.append("");
      stringBuilder2.append(MyMExpandableListAdapter.this.mContext.getResources().getString(2131624047));
      AlertDialog.Builder builder1 = builder2.setPositiveButton(stringBuilder2.toString(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface param2DialogInterface, int param2Int) {
              String str1 = et.getText().toString();
              if ("".equals(str1))
                return; 
              StringBuilder stringBuilder2 = new StringBuilder();
              stringBuilder2.append("-- ");
              stringBuilder2.append(et.getText());
              Log.e("", stringBuilder2.toString());
              stringBuilder2 = new StringBuilder();
              stringBuilder2.append("-- ");
              stringBuilder2.append(MyMExpandableListAdapter.this.groupID.get(MyMExpandableListAdapter.OnResetNameClickListener.this.groupPosition));
              Log.e("", stringBuilder2.toString());
              Message message = new Message();
              Bundle bundle = new Bundle();
              bundle.putString("groupname", str1);
              message.setData(bundle);
              MyMExpandableListAdapter.this.mHandler.sendMessage(message);
              StringBuilder stringBuilder1 = new StringBuilder();
              stringBuilder1.append("");
              stringBuilder1.append(MyMExpandableListAdapter.this.groupID.get(MyMExpandableListAdapter.OnResetNameClickListener.this.groupPosition));
              String str2 = stringBuilder1.toString();
              ContentValues contentValues = new ContentValues();
              contentValues.put("groupName", str1);
              MyMExpandableListAdapter.this.dbAdapter.upDataforTable("mygroup", contentValues, "_id=?", new String[] { str2 });
            }
          });
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("");
      stringBuilder1.append(MyMExpandableListAdapter.this.mContext.getResources().getString(2131624006));
      builder1.setNegativeButton(stringBuilder1.toString(), null).show();
    }
    
    public OnResetNameClickListener setGroupPosition(int param1Int) {
      this.groupPosition = param1Int;
      return this;
    }
    
    public OnResetNameClickListener setGroupViewHolder(MyMExpandableListAdapter.GroupViewHolder param1GroupViewHolder) {
      this.mGroupViewHolder = param1GroupViewHolder;
      return this;
    }
  }
  
  class null implements DialogInterface.OnClickListener {
    public void onClick(DialogInterface param1DialogInterface, int param1Int) {
      String str1 = et.getText().toString();
      if ("".equals(str1))
        return; 
      StringBuilder stringBuilder2 = new StringBuilder();
      stringBuilder2.append("-- ");
      stringBuilder2.append(et.getText());
      Log.e("", stringBuilder2.toString());
      stringBuilder2 = new StringBuilder();
      stringBuilder2.append("-- ");
      stringBuilder2.append(MyMExpandableListAdapter.this.groupID.get(this.this$1.groupPosition));
      Log.e("", stringBuilder2.toString());
      Message message = new Message();
      Bundle bundle = new Bundle();
      bundle.putString("groupname", str1);
      message.setData(bundle);
      MyMExpandableListAdapter.this.mHandler.sendMessage(message);
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("");
      stringBuilder1.append(MyMExpandableListAdapter.this.groupID.get(this.this$1.groupPosition));
      String str2 = stringBuilder1.toString();
      ContentValues contentValues = new ContentValues();
      contentValues.put("groupName", str1);
      MyMExpandableListAdapter.this.dbAdapter.upDataforTable("mygroup", contentValues, "_id=?", new String[] { str2 });
    }
  }
  
  private class delectGroupOnClickListener implements View.OnClickListener {
    private int groupPosition;
    
    private delectGroupOnClickListener() {}
    
    public void onClick(View param1View) {
      if (MyMExpandableListAdapter.this.groupID.size() > this.groupPosition) {
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append("id= ");
        stringBuilder3.append(MyMExpandableListAdapter.this.groupID.get(this.groupPosition));
        Log.e("", stringBuilder3.toString());
        stringBuilder3 = new StringBuilder();
        stringBuilder3.append("");
        stringBuilder3.append(MyMExpandableListAdapter.this.groupID.get(this.groupPosition));
        String str2 = stringBuilder3.toString();
        MyMExpandableListAdapter.this.dbAdapter.deleteOneData("mygroup", "_id=?", new String[] { str2 });
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("");
        stringBuilder2.append(MyMExpandableListAdapter.this.groupID.get(this.groupPosition));
        String str1 = stringBuilder2.toString();
        long l = MyMExpandableListAdapter.this.dbAdapter.deleteOneData("mylight", "lightgroup=?", new String[] { str1 });
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("uuuuu = ");
        stringBuilder1.append(l);
        Log.e("", stringBuilder1.toString());
        MyMExpandableListAdapter.this.mHandler.sendEmptyMessage(1);
      } 
    }
    
    public delectGroupOnClickListener setPos(int param1Int) {
      this.groupPosition = param1Int;
      return this;
    }
  }
  
  private class delectOnClickListener implements View.OnClickListener {
    private int childPosition;
    
    private int groupPosition;
    
    private delectOnClickListener() {}
    
    public void onClick(View param1View) {
      if (MyMExpandableListAdapter.this.children.size() > this.groupPosition) {
        ArrayList<MyChild> arrayList = MyMExpandableListAdapter.this.children.get(this.groupPosition);
        if (arrayList != null && arrayList.size() > this.childPosition) {
          MyChild myChild = arrayList.get(this.childPosition);
          if (myChild != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("mMyChild.mac = ");
            stringBuilder.append(myChild.mac);
            stringBuilder.append(" mMyChild.myGroup = ");
            stringBuilder.append(myChild.myGroup);
            Log.e("", stringBuilder.toString());
            String str = myChild.mac;
            MyMExpandableListAdapter.this.dbAdapter.deleteOneData("mylight", "Mac=?", new String[] { str });
            MyMExpandableListAdapter.this.mHandler.sendEmptyMessage(2);
          } 
        } 
      } 
    }
    
    public delectOnClickListener setPos(int param1Int1, int param1Int2) {
      this.groupPosition = param1Int1;
      this.childPosition = param1Int2;
      return this;
    }
  }
  
  private class moveOnTouchListener implements View.OnTouchListener {
    private View convertView;
    
    private moveOnTouchListener() {}
    
    public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
      MyMExpandableListAdapter.this.moveV = this.convertView;
      return false;
    }
    
    public moveOnTouchListener setViewHolder(View param1View) {
      this.convertView = param1View;
      return this;
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\blelight\MyMExpandableListAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */