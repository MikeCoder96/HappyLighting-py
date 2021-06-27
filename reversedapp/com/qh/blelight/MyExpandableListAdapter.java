package com.qh.blelight;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.qh.data.ConnectionInterface;
import com.qh.data.MyChild;
import com.qh.data.SwitchInterface;
import com.qh.tools.DBAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.regex.Pattern;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {
  public HashMap<String, MyChild> DBdata = new HashMap<String, MyChild>();
  
  public int childH = 105;
  
  public ArrayList<ArrayList> children = new ArrayList<ArrayList>();
  
  private DBAdapter dbAdapter;
  
  public int groupH = 121;
  
  private ArrayList<Integer> groupID = new ArrayList<Integer>();
  
  public Hashtable<Integer, GroupViewHolder> groupItem = new Hashtable<Integer, GroupViewHolder>();
  
  private HashMap<Integer, View> groupItemViews = new HashMap<Integer, View>();
  
  private ArrayList<String> groupNames = new ArrayList<String>();
  
  private HashMap<Integer, HashMap> groupViews = new HashMap<Integer, HashMap>();
  
  public ArrayList<Boolean> isExpandeds = new ArrayList<Boolean>();
  
  public HashMap<Integer, Boolean> isOpenGroupIDs = new HashMap<Integer, Boolean>();
  
  public HashMap<String, ChildViewHolder> itemViewByMac = new HashMap<String, ChildViewHolder>();
  
  public HashMap<String, View> linkViews = new HashMap<String, View>();
  
  private Activity mActivity;
  
  private BluetoothLeService mBluetoothLeService;
  
  public ConnectionInterface mConnectionInterface;
  
  private Context mContext;
  
  private Handler mHandler;
  
  private LayoutInflater mInflator;
  
  public SwitchInterface mSwitchInterface;
  
  public View moveV;
  
  public HashMap<Integer, Boolean> operatingChildIDs = new HashMap<Integer, Boolean>();
  
  public HashMap<Integer, Boolean> operatingGroupIDs = new HashMap<Integer, Boolean>();
  
  MyExpandableListAdapter(Context paramContext, Activity paramActivity, DBAdapter paramDBAdapter, Handler paramHandler, BluetoothLeService paramBluetoothLeService) {
    this.mContext = paramContext;
    this.mInflator = paramActivity.getLayoutInflater();
    this.dbAdapter = paramDBAdapter;
    this.mHandler = paramHandler;
    this.mBluetoothLeService = paramBluetoothLeService;
  }
  
  private String getMAC(int paramInt1, int paramInt2) {
    String str1 = "";
    String str2 = str1;
    if (this.children.size() > paramInt1) {
      ArrayList arrayList = this.children.get(paramInt1);
      str2 = str1;
      if (arrayList != null) {
        str2 = str1;
        if (arrayList.size() > paramInt2)
          str2 = ((MyChild)arrayList.get(paramInt2)).mac; 
      } 
    } 
    return str2;
  }
  
  public void changeLightByMac(String paramString, boolean paramBoolean) {
    if (this.itemViewByMac.containsKey(paramString)) {
      ChildViewHolder childViewHolder = this.itemViewByMac.get(paramString);
      if (childViewHolder != null)
        if (paramBoolean) {
          childViewHolder.open_img.setImageResource(2131165395);
        } else {
          childViewHolder.open_img.setImageResource(2131165397);
        }  
    } 
  }
  
  public Object getChild(int paramInt1, int paramInt2) {
    return ((ArrayList)this.children.get(paramInt1)).get(paramInt2);
  }
  
  public long getChildId(int paramInt1, int paramInt2) {
    return paramInt2;
  }
  
  public View getChildView(int paramInt1, int paramInt2, boolean paramBoolean, View paramView, ViewGroup paramViewGroup) {
    ChildViewHolder childViewHolder;
    View view;
    if (!this.groupViews.containsKey(Integer.valueOf(paramInt1)))
      this.groupViews.put(Integer.valueOf(paramInt1), new HashMap<Object, Object>()); 
    HashMap<Integer, View> hashMap = this.groupViews.get(Integer.valueOf(paramInt1));
    if (hashMap.containsKey(Integer.valueOf(paramInt2))) {
      paramView = (View)hashMap.get(Integer.valueOf(paramInt2));
    } else {
      paramView = null;
    } 
    if (paramView == null) {
      view = this.mInflator.inflate(2131361863, null);
      childViewHolder = new ChildViewHolder();
      childViewHolder.groupPosition = paramInt1;
      childViewHolder.childPosition = paramInt2;
      childViewHolder.light_name_tx = (TextView)view.findViewById(2131230952);
      childViewHolder.device_img = (ImageView)view.findViewById(2131230881);
      childViewHolder.open_img = (ImageView)view.findViewById(2131230863);
      childViewHolder.pb_conn = (ProgressBar)view.findViewById(2131231012);
      childViewHolder.device_img.setOnClickListener((new myOnClickListener()).setChildViewHolder(childViewHolder, 1));
      childViewHolder.open_img.setOnClickListener((new myOnClickListener()).setChildViewHolder(childViewHolder, 0));
      view.setOnTouchListener((new OnChildTouchListener()).setOnChildTouchListener(view).setPos(paramInt1, paramInt2, false));
      if (this.operatingGroupIDs.containsKey(Integer.valueOf(paramInt1)))
        if (((Boolean)this.operatingGroupIDs.get(Integer.valueOf(paramInt1))).booleanValue()) {
          view.setBackgroundColor(1090519039);
          this.operatingChildIDs.put(Integer.valueOf(paramInt1 * 100 + paramInt2), Boolean.valueOf(true));
        } else {
          view.setBackgroundColor(0);
          this.operatingChildIDs.put(Integer.valueOf(paramInt1 * 100 + paramInt2), Boolean.valueOf(false));
        }  
      if (MainActivity.ControlMACs.containsKey(getMAC(paramInt1, paramInt2)))
        view.setBackgroundColor(1090519039); 
      view.setTag(childViewHolder);
      view.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View param1View) {
              MyExpandableListAdapter.ChildViewHolder childViewHolder = (MyExpandableListAdapter.ChildViewHolder)param1View.getTag();
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("onLongClick ");
              stringBuilder.append(childViewHolder.mMyChild.mac);
              Log.e("Click", stringBuilder.toString());
              if (MyExpandableListAdapter.this.mBluetoothLeService != null && MyExpandableListAdapter.this.mBluetoothLeService.MyBluetoothGatts.containsKey(childViewHolder.mMyChild.mac)) {
                MyBluetoothGatt myBluetoothGatt = MyExpandableListAdapter.this.mBluetoothLeService.MyBluetoothGatts.get(childViewHolder.mMyChild.mac);
                if (myBluetoothGatt != null) {
                  String str = myBluetoothGatt.mLEdevice.getName();
                  if (!TextUtils.isEmpty(str) && (str.contains("Triones:") || str.contains("Triones#") || str.contains("Dream#") || str.contains("Dream~")) && MyExpandableListAdapter.this.mHandler != null) {
                    Message message = new Message();
                    message.what = 4002;
                    message.obj = childViewHolder.mMyChild.mac;
                    MyExpandableListAdapter.this.mHandler.sendMessage(message);
                  } 
                } 
              } 
              return true;
            }
          });
      hashMap.put(Integer.valueOf(paramInt2), view);
      this.groupViews.put(Integer.valueOf(paramInt1), hashMap);
    } else {
      childViewHolder = (ChildViewHolder)paramView.getTag();
      view = paramView;
    } 
    if (MainActivity.ControlMACs.containsKey(getMAC(paramInt1, paramInt2)))
      view.setBackgroundColor(1090519039); 
    String str2 = "";
    String str1 = str2;
    if (this.children.size() > paramInt1) {
      ArrayList<MyChild> arrayList = this.children.get(paramInt1);
      str1 = str2;
      if (arrayList.size() > paramInt2) {
        MyChild myChild = arrayList.get(paramInt2);
        str1 = str2;
        if (myChild != null) {
          str1 = myChild.name;
          this.itemViewByMac.put(myChild.mac, childViewHolder);
        } 
        myChild.isopen = false;
        childViewHolder.mMyChild = myChild;
      } 
    } 
    TextView textView = childViewHolder.light_name_tx;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("");
    stringBuilder.append(str1);
    textView.setText(stringBuilder.toString());
    if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(getMAC(paramInt1, paramInt2))) {
      MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(getMAC(paramInt1, paramInt2));
      if (myBluetoothGatt != null && (myBluetoothGatt.datas[2] & 0xFF) == 35 && childViewHolder.mMyChild != null) {
        MyChild myChild = childViewHolder.mMyChild;
        if ((myBluetoothGatt.datas[2] & 0xFF) == 35) {
          paramBoolean = true;
        } else {
          paramBoolean = false;
        } 
        myChild.isopen = paramBoolean;
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("getChildView: ");
        stringBuilder1.append(childViewHolder.mMyChild.isopen);
        Log.e(" = = =  ", stringBuilder1.toString());
      } 
    } 
    if (childViewHolder.mMyChild != null) {
      this.linkViews.put(childViewHolder.mMyChild.mac, childViewHolder.device_img);
      paramInt1 = islinkOK(childViewHolder.mMyChild.mac);
      if (paramInt1 == 2) {
        childViewHolder.device_img.setImageResource(2131165304);
        childViewHolder.device_img.setVisibility(0);
        childViewHolder.pb_conn.setVisibility(8);
        if (childViewHolder.mMyChild.isopen) {
          childViewHolder.open_img.setImageResource(2131165395);
        } else {
          childViewHolder.open_img.setImageResource(2131165397);
        } 
      } else if (paramInt1 == 1) {
        childViewHolder.device_img.setImageResource(2131165318);
        childViewHolder.device_img.setVisibility(8);
        childViewHolder.pb_conn.setVisibility(0);
        childViewHolder.mMyChild.isopen = false;
        childViewHolder.open_img.setImageResource(2131165397);
      } else {
        childViewHolder.device_img.setImageResource(2131165318);
        childViewHolder.device_img.setVisibility(0);
        childViewHolder.pb_conn.setVisibility(8);
        childViewHolder.mMyChild.isopen = false;
        childViewHolder.open_img.setImageResource(2131165397);
      } 
    } else {
      childViewHolder.open_img.setImageResource(2131165397);
    } 
    return view;
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
    GroupViewHolder groupViewHolder;
    String str;
    boolean bool;
    View view = this.groupItemViews.get(Integer.valueOf(paramInt));
    if (this.groupItemViews.containsKey(Integer.valueOf(paramInt))) {
      paramView = this.groupItemViews.get(Integer.valueOf(paramInt));
    } else {
      paramView = null;
    } 
    if (paramView == null) {
      paramView = this.mInflator.inflate(2131361862, null);
      groupViewHolder = new GroupViewHolder();
      groupViewHolder.groupPosition = paramInt;
      groupViewHolder.group_name_tx = (TextView)paramView.findViewById(2131230808);
      groupViewHolder.arrow_img = (ImageView)paramView.findViewById(2131230754);
      groupViewHolder.img_open = (ImageView)paramView.findViewById(2131230915);
      paramView.setOnTouchListener((new OnGroupTouchListener()).setGroupPosition(paramInt).setConvertView(paramView));
      groupViewHolder.arrow_img.setOnClickListener((new arrowOnClickListener()).setPos(paramInt, paramBoolean));
      groupViewHolder.img_open.setOnClickListener((new myGroupOpenListener()).setGroupViewHolder(groupViewHolder));
      paramView.setTag(groupViewHolder);
      this.groupItemViews.put(Integer.valueOf(paramInt), view);
    } else {
      groupViewHolder = (GroupViewHolder)paramView.getTag();
    } 
    if (paramBoolean) {
      groupViewHolder.arrow_img.setImageResource(2131165270);
    } else {
      groupViewHolder.arrow_img.setImageResource(2131165269);
    } 
    this.isExpandeds.set(paramInt, Boolean.valueOf(paramBoolean));
    ArrayList<MyChild> arrayList = this.children.get(paramInt);
    if (arrayList != null) {
      byte b = 0;
      boolean bool1 = true;
      while (true) {
        bool = bool1;
        if (b < arrayList.size()) {
          MyChild myChild = arrayList.get(b);
          bool = bool1;
          if (myChild != null) {
            bool = bool1;
            if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(myChild.mac)) {
              MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(myChild.mac);
              bool = bool1;
              if (myBluetoothGatt != null) {
                bool = bool1;
                if (myBluetoothGatt.datas != null) {
                  bool = bool1;
                  if (myBluetoothGatt.datas.length > 10) {
                    bool = bool1;
                    if (myBluetoothGatt.datas[2] == 35)
                      bool = false; 
                  } 
                } 
              } 
            } 
          } 
          b++;
          bool1 = bool;
          continue;
        } 
        break;
      } 
    } else {
      bool = true;
    } 
    if (bool) {
      groupViewHolder.img_open.setImageResource(2131165397);
    } else {
      groupViewHolder.img_open.setImageResource(2131165395);
    } 
    this.isOpenGroupIDs.put(Integer.valueOf(paramInt), Boolean.valueOf(true ^ bool));
    if (this.groupNames.size() > paramInt) {
      str = this.groupNames.get(paramInt);
    } else {
      str = "error!";
    } 
    if (this.operatingGroupIDs.containsKey(Integer.valueOf(paramInt)))
      if (((Boolean)this.operatingGroupIDs.get(Integer.valueOf(paramInt))).booleanValue()) {
        paramView.setBackgroundColor(1090519039);
      } else {
        paramView.setBackgroundColor(0);
      }  
    if (this.isOpenGroupIDs.containsKey(Integer.valueOf(groupViewHolder.groupPosition)))
      if (((Boolean)this.isOpenGroupIDs.get(Integer.valueOf(groupViewHolder.groupPosition))).booleanValue()) {
        groupViewHolder.img_open.setImageResource(2131165395);
      } else {
        groupViewHolder.img_open.setImageResource(2131165397);
      }  
    this.groupItem.put(Integer.valueOf(paramInt), groupViewHolder);
    TextView textView = groupViewHolder.group_name_tx;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("");
    stringBuilder.append(str);
    textView.setText(stringBuilder.toString());
    return paramView;
  }
  
  public boolean hasStableIds() {
    return true;
  }
  
  public boolean isChildSelectable(int paramInt1, int paramInt2) {
    return true;
  }
  
  public int islinkOK(String paramString) {
    if (paramString == null || "".equals(paramString))
      return 4; 
    if (this.mBluetoothLeService == null)
      return 4; 
    if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(paramString)) {
      MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(paramString);
      return (myBluetoothGatt != null) ? myBluetoothGatt.mConnectionState : 4;
    } 
    return 4;
  }
  
  public void move(int paramInt1, int paramInt2, int paramInt3) {
    if (this.children.size() > paramInt1 && this.children.size() > paramInt2) {
      ArrayList<MyChild> arrayList1 = this.children.get(paramInt1);
      ArrayList<MyChild> arrayList2 = this.children.get(paramInt2);
      if (arrayList1.size() > paramInt3) {
        MyChild myChild = arrayList1.get(paramInt3);
        arrayList1.remove(paramInt3);
        arrayList2.add(myChild);
        this.children.set(paramInt1, arrayList1);
        this.children.set(paramInt2, arrayList2);
      } 
    } 
  }
  
  public void setConnectionInterface(ConnectionInterface paramConnectionInterface) {
    this.mConnectionInterface = paramConnectionInterface;
  }
  
  public void setSwitchInterface(SwitchInterface paramSwitchInterface) {
    this.mSwitchInterface = paramSwitchInterface;
  }
  
  public void setgroupNames(ArrayList<String> paramArrayList, ArrayList<Integer> paramArrayList1) {
    if (paramArrayList == null)
      return; 
    this.groupID.clear();
    this.groupNames.clear();
    this.groupNames.add(this.mContext.getResources().getString(2131623941));
    ArrayList<Integer> arrayList = this.groupID;
    int i = 0;
    arrayList.add(Integer.valueOf(0));
    Cursor cursor = this.dbAdapter.queryAllData("mygroup");
    while (cursor.moveToNext()) {
      int k = cursor.getInt(cursor.getColumnIndex("_id"));
      String str = cursor.getString(cursor.getColumnIndex("groupName"));
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("mac = ");
      stringBuilder.append(k);
      stringBuilder.append(" myGroup =  ");
      stringBuilder.append(str);
      Log.e("", stringBuilder.toString());
      this.groupID.add(Integer.valueOf(k));
      this.groupNames.add(str);
    } 
    this.isExpandeds.clear();
    int j;
    for (j = 0; j < this.groupNames.size(); j++) {
      if (this.isExpandeds.size() < this.groupNames.size())
        this.isExpandeds.add(Boolean.valueOf(false)); 
    } 
    this.operatingChildIDs.clear();
    this.operatingGroupIDs.clear();
    this.groupViews.clear();
    this.DBdata.clear();
    this.children.clear();
    for (j = 0; j < this.groupID.size(); j++) {
      int k = ((Integer)this.groupID.get(j)).intValue();
      Cursor cursor1 = this.dbAdapter.queryDataByGroup(k);
      while (cursor1.moveToNext()) {
        MyChild myChild = new MyChild();
        myChild.mac = cursor1.getString(cursor1.getColumnIndex("Mac"));
        myChild.myGroup = cursor1.getInt(cursor1.getColumnIndex("lightgroup"));
        myChild.name = cursor1.getString(cursor1.getColumnIndex("LightName"));
        this.DBdata.put(myChild.mac, myChild);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("mMyChild.mac - ");
        stringBuilder.append(myChild.mac);
        Log.e("", stringBuilder.toString());
      } 
    } 
    Pattern pattern = Pattern.compile("^Triones-A00100|^Triones-A10100|^Triones-A20100|^Triones-A30100|^Triones-A40100|^Triones-A50100|^Triones-A60100|^Triones-A70100|^Triones-A80100|^Triones-A90100|^Triones-AA0100|^Triones-AB0100|^Triones-AC0100|^Triones-AD0100|^Triones-AE0100|^Triones-AF0100");
    Iterator<String> iterator = this.mBluetoothLeService.mDevices.keySet().iterator();
    while (true) {
      j = i;
      if (iterator.hasNext()) {
        String str = iterator.next();
        BluetoothDevice bluetoothDevice = this.mBluetoothLeService.mDevices.get(str);
        if (!this.DBdata.containsKey(str)) {
          str.substring(str.length() - 8, str.length());
          MyChild myChild = new MyChild();
          myChild.mac = str;
          myChild.myGroup = 0;
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("");
          stringBuilder.append(bluetoothDevice.getName());
          myChild.name = stringBuilder.toString();
          if (pattern.matcher(myChild.name).find())
            myChild.name = "LD-0003"; 
          if (!TextUtils.isEmpty(myChild.name) && !myChild.name.equals("null"))
            this.DBdata.put(myChild.mac, myChild); 
        } 
        continue;
      } 
      break;
    } 
    while (j < this.groupID.size()) {
      i = ((Integer)this.groupID.get(j)).intValue();
      ArrayList<MyChild> arrayList1 = new ArrayList();
      for (String str : this.DBdata.keySet()) {
        MyChild myChild = this.DBdata.get(str);
        if (i == myChild.myGroup && this.mBluetoothLeService.mDevices.containsKey(str) && !TextUtils.isEmpty(myChild.name) && !myChild.name.equals("null"))
          arrayList1.add(myChild); 
      } 
      this.children.add(arrayList1);
      j++;
    } 
  }
  
  static class ChildViewHolder {
    public int childPosition;
    
    public ImageView device_img;
    
    public int groupPosition;
    
    public TextView light_name_tx;
    
    public MyChild mMyChild;
    
    public ImageView open_img;
    
    public ProgressBar pb_conn;
  }
  
  public static class GroupViewHolder {
    public ImageView arrow_img;
    
    public int groupPosition;
    
    public TextView group_name_tx;
    
    public ImageView img_open;
  }
  
  public class OnChildTouchListener implements View.OnTouchListener {
    private int childPosition;
    
    private View convertView;
    
    private int groupPosition;
    
    public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
      StringBuilder stringBuilder;
      switch (param1MotionEvent.getAction()) {
        case 0:
          stringBuilder = new StringBuilder();
          stringBuilder.append(" c -t ");
          stringBuilder.append(this.groupPosition);
          stringBuilder.append(" ");
          stringBuilder.append(this.childPosition);
          Log.e("", stringBuilder.toString());
          if (this.convertView != null) {
            boolean bool;
            if (MyExpandableListAdapter.this.operatingChildIDs.containsKey(Integer.valueOf(this.groupPosition * 100 + this.childPosition))) {
              bool = ((Boolean)MyExpandableListAdapter.this.operatingChildIDs.get(Integer.valueOf(this.groupPosition * 100 + this.childPosition))).booleanValue();
            } else {
              bool = false;
            } 
            if (!bool) {
              this.convertView.setBackgroundColor(1090519039);
              MyExpandableListAdapter.this.operatingChildIDs.put(Integer.valueOf(this.groupPosition * 100 + this.childPosition), Boolean.valueOf(true));
              String str1 = MyExpandableListAdapter.this.getMAC(this.groupPosition, this.childPosition);
              MainActivity.ControlMACs.put(str1, str1);
              break;
            } 
            this.convertView.setBackgroundColor(0);
            MyExpandableListAdapter.this.operatingChildIDs.put(Integer.valueOf(this.groupPosition * 100 + this.childPosition), Boolean.valueOf(false));
            String str = MyExpandableListAdapter.this.getMAC(this.groupPosition, this.childPosition);
            if (MainActivity.ControlMACs.containsKey(str))
              MainActivity.ControlMACs.remove(str); 
          } 
          break;
      } 
      return false;
    }
    
    public OnChildTouchListener setOnChildTouchListener(View param1View) {
      this.convertView = param1View;
      return this;
    }
    
    public OnChildTouchListener setPos(int param1Int1, int param1Int2, boolean param1Boolean) {
      this.groupPosition = param1Int1;
      this.childPosition = param1Int2;
      return this;
    }
  }
  
  private class OnGroupTouchListener implements View.OnTouchListener {
    private View convertView;
    
    private int groupPosition;
    
    private float x;
    
    private float y;
    
    private OnGroupTouchListener() {}
    
    public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
      if (param1MotionEvent.getAction() == 0) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("group -- ");
        stringBuilder.append(this.groupPosition);
        Log.e("", stringBuilder.toString());
        if (this.convertView != null) {
          boolean bool1;
          boolean bool2;
          if (MyExpandableListAdapter.this.operatingGroupIDs.containsKey(Integer.valueOf(this.groupPosition))) {
            bool1 = ((Boolean)MyExpandableListAdapter.this.operatingGroupIDs.get(Integer.valueOf(this.groupPosition))).booleanValue();
          } else {
            bool1 = false;
          } 
          if (!bool1) {
            this.convertView.setBackgroundColor(1090519039);
            MyExpandableListAdapter.this.operatingGroupIDs.put(Integer.valueOf(this.groupPosition), Boolean.valueOf(true));
            bool2 = true;
          } else {
            this.convertView.setBackgroundColor(0);
            MyExpandableListAdapter.this.operatingGroupIDs.put(Integer.valueOf(this.groupPosition), Boolean.valueOf(false));
            bool2 = false;
          } 
          ArrayList arrayList = MyExpandableListAdapter.this.children.get(this.groupPosition);
          if (arrayList != null) {
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append("s = ");
            stringBuilder1.append(this.groupPosition);
            stringBuilder1.append(" mArrayList");
            stringBuilder1.append(arrayList.size());
            Log.e("", stringBuilder1.toString());
            for (byte b = 0; b < arrayList.size(); b++) {
              if (bool2) {
                String str = MyExpandableListAdapter.this.getMAC(this.groupPosition, b);
                MainActivity.ControlMACs.put(str, str);
              } else {
                String str = MyExpandableListAdapter.this.getMAC(this.groupPosition, b);
                if (MainActivity.ControlMACs.containsKey(str))
                  MainActivity.ControlMACs.remove(str); 
              } 
            } 
          } 
          HashMap hashMap = (HashMap)MyExpandableListAdapter.this.groupViews.get(Integer.valueOf(this.groupPosition));
          if (hashMap != null) {
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append("mHashMap ");
            stringBuilder1.append(hashMap.size());
            Log.e("", stringBuilder1.toString());
            Iterator<Integer> iterator = hashMap.keySet().iterator();
            while (iterator.hasNext()) {
              int i = ((Integer)iterator.next()).intValue();
              View view = (View)hashMap.get(Integer.valueOf(i));
              if (view != null) {
                if (bool2) {
                  view.setBackgroundColor(1090519039);
                  MyExpandableListAdapter.this.operatingChildIDs.put(Integer.valueOf(this.groupPosition * 100 + i), Boolean.valueOf(true));
                  continue;
                } 
                view.setBackgroundColor(0);
                MyExpandableListAdapter.this.operatingChildIDs.put(Integer.valueOf(this.groupPosition * 100 + i), Boolean.valueOf(false));
              } 
            } 
          } 
        } 
      } 
      return true;
    }
    
    public OnGroupTouchListener setConvertView(View param1View) {
      this.convertView = param1View;
      return this;
    }
    
    public OnGroupTouchListener setGroupPosition(int param1Int) {
      this.groupPosition = param1Int;
      return this;
    }
  }
  
  private class OnResetNameClickListener implements View.OnClickListener {
    private int groupPosition;
    
    public void onClick(View param1View) {
      final EditText et = new EditText(MyExpandableListAdapter.this.mContext);
      AlertDialog.Builder builder2 = new AlertDialog.Builder(MyExpandableListAdapter.this.mContext);
      StringBuilder stringBuilder2 = new StringBuilder();
      stringBuilder2.append("");
      stringBuilder2.append(MyExpandableListAdapter.this.mContext.getResources().getString(2131623948));
      builder2 = builder2.setTitle(stringBuilder2.toString()).setIcon(17301659).setView((View)editText);
      stringBuilder2 = new StringBuilder();
      stringBuilder2.append("");
      stringBuilder2.append(MyExpandableListAdapter.this.mContext.getResources().getString(2131624047));
      AlertDialog.Builder builder1 = builder2.setPositiveButton(stringBuilder2.toString(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface param2DialogInterface, int param2Int) {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("-- ");
              stringBuilder.append(et.getText());
              Log.e("", stringBuilder.toString());
            }
          });
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("");
      stringBuilder1.append(MyExpandableListAdapter.this.mContext.getResources().getString(2131624006));
      builder1.setNegativeButton(stringBuilder1.toString(), null).show();
    }
    
    public OnResetNameClickListener setGroupPosition(int param1Int) {
      this.groupPosition = param1Int;
      return this;
    }
  }
  
  class null implements DialogInterface.OnClickListener {
    public void onClick(DialogInterface param1DialogInterface, int param1Int) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("-- ");
      stringBuilder.append(et.getText());
      Log.e("", stringBuilder.toString());
    }
  }
  
  private class arrowOnClickListener implements View.OnClickListener {
    private boolean flay = false;
    
    private int pos = -1;
    
    private arrowOnClickListener() {}
    
    private arrowOnClickListener setPos(int param1Int, boolean param1Boolean) {
      this.pos = param1Int;
      this.flay = param1Boolean;
      return this;
    }
    
    public void onClick(View param1View) {
      if (this.flay) {
        Message message = new Message();
        message.what = 2;
        message.arg1 = this.pos;
        MyExpandableListAdapter.this.mHandler.sendMessage(message);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("arg1 = ");
        stringBuilder.append(message.arg1);
        Log.e("", stringBuilder.toString());
      } else {
        Message message = new Message();
        message.what = 1;
        message.arg1 = this.pos;
        MyExpandableListAdapter.this.mHandler.sendMessage(message);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("arg1 = ");
        stringBuilder.append(message.arg1);
        Log.e("", stringBuilder.toString());
      } 
      this.flay ^= 0x1;
    }
  }
  
  private class myGroupOpenListener implements View.OnClickListener {
    private MyExpandableListAdapter.GroupViewHolder mGroupViewHolder;
    
    private myGroupOpenListener() {}
    
    public void onClick(View param1View) {
      boolean bool = MyExpandableListAdapter.this.isOpenGroupIDs.containsKey(Integer.valueOf(this.mGroupViewHolder.groupPosition));
      byte b = 0;
      if (bool) {
        bool = ((Boolean)MyExpandableListAdapter.this.isOpenGroupIDs.get(Integer.valueOf(this.mGroupViewHolder.groupPosition))).booleanValue();
      } else {
        bool = false;
      } 
      boolean bool1 = true;
      if (!bool) {
        this.mGroupViewHolder.img_open.setImageResource(2131165395);
        MyExpandableListAdapter.this.isOpenGroupIDs.put(Integer.valueOf(this.mGroupViewHolder.groupPosition), Boolean.valueOf(true));
        bool = bool1;
      } else {
        this.mGroupViewHolder.img_open.setImageResource(2131165397);
        MyExpandableListAdapter.this.isOpenGroupIDs.put(Integer.valueOf(this.mGroupViewHolder.groupPosition), Boolean.valueOf(false));
        bool = false;
      } 
      ArrayList<MyChild> arrayList = MyExpandableListAdapter.this.children.get(this.mGroupViewHolder.groupPosition);
      if (arrayList != null)
        while (b < arrayList.size()) {
          MyChild myChild = arrayList.get(b);
          if (myChild != null && MyExpandableListAdapter.this.islinkOK(myChild.mac) == 2) {
            MyExpandableListAdapter.this.changeLightByMac(myChild.mac, bool);
            if (MyExpandableListAdapter.this.mSwitchInterface != null)
              MyExpandableListAdapter.this.mSwitchInterface.LightSwitch(myChild.mac, bool); 
          } 
          b++;
        }  
    }
    
    public myGroupOpenListener setGroupViewHolder(MyExpandableListAdapter.GroupViewHolder param1GroupViewHolder) {
      this.mGroupViewHolder = param1GroupViewHolder;
      return this;
    }
  }
  
  private class myOnClickListener implements View.OnClickListener {
    private boolean f = false;
    
    private MyExpandableListAdapter.ChildViewHolder mChildViewHolder;
    
    private int type = 0;
    
    private myOnClickListener() {}
    
    public void onClick(View param1View) {
      if (this.mChildViewHolder == null) {
        Log.e("", "------ conn ------");
        return;
      } 
      int i = this.type;
      int j = 1;
      if (i == 0) {
        String str = MyExpandableListAdapter.this.getMAC(this.mChildViewHolder.groupPosition, this.mChildViewHolder.childPosition);
        if (MyExpandableListAdapter.this.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
          MyBluetoothGatt myBluetoothGatt = MyExpandableListAdapter.this.mBluetoothLeService.MyBluetoothGatts.get(str);
          if (myBluetoothGatt != null)
            if ((myBluetoothGatt.datas[2] & 0xFF) == 35) {
              this.f = true;
            } else {
              this.f = false;
            }  
        } 
        if (this.f) {
          this.mChildViewHolder.open_img.setImageResource(2131165397);
          this.f = false;
          ArrayList<MyChild> arrayList = MyExpandableListAdapter.this.children.get(this.mChildViewHolder.groupPosition);
          if (arrayList != null) {
            byte b = 0;
            for (j = 1; b < arrayList.size(); j = i) {
              MyChild myChild = arrayList.get(b);
              if (myChild != null && myChild.mac.equals(this.mChildViewHolder.mMyChild.mac)) {
                i = j;
              } else {
                i = j;
                if (myChild != null) {
                  i = j;
                  if (MyExpandableListAdapter.this.mBluetoothLeService.MyBluetoothGatts.containsKey(myChild.mac)) {
                    MyBluetoothGatt myBluetoothGatt = MyExpandableListAdapter.this.mBluetoothLeService.MyBluetoothGatts.get(myChild.mac);
                    i = j;
                    if (myBluetoothGatt != null) {
                      i = j;
                      if (myBluetoothGatt.datas != null) {
                        i = j;
                        if (myBluetoothGatt.datas.length > 10) {
                          i = j;
                          if (myBluetoothGatt.datas[2] == 35) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("111122");
                            stringBuilder.append(myChild.mac);
                            Log.e("", stringBuilder.toString());
                            i = 0;
                          } 
                        } 
                      } 
                    } 
                  } 
                } 
              } 
              b++;
            } 
          } 
          if (j != 0) {
            MyExpandableListAdapter.GroupViewHolder groupViewHolder = MyExpandableListAdapter.this.groupItem.get(Integer.valueOf(this.mChildViewHolder.groupPosition));
            if (groupViewHolder != null) {
              groupViewHolder.img_open.setImageResource(2131165397);
              MyExpandableListAdapter.this.isOpenGroupIDs.put(Integer.valueOf(this.mChildViewHolder.groupPosition), Boolean.valueOf(false));
            } 
          } 
        } else {
          this.mChildViewHolder.open_img.setImageResource(2131165395);
          this.f = true;
          MyExpandableListAdapter.GroupViewHolder groupViewHolder = MyExpandableListAdapter.this.groupItem.get(Integer.valueOf(this.mChildViewHolder.groupPosition));
          if (groupViewHolder != null) {
            groupViewHolder.img_open.setImageResource(2131165395);
            MyExpandableListAdapter.this.isOpenGroupIDs.put(Integer.valueOf(this.mChildViewHolder.groupPosition), Boolean.valueOf(true));
          } 
        } 
        if (this.mChildViewHolder.mMyChild != null)
          MyExpandableListAdapter.this.mSwitchInterface.LightSwitch(this.mChildViewHolder.mMyChild.mac, this.f); 
      } else if (this.type == 1) {
        if (MyExpandableListAdapter.this.mBluetoothLeService.unlinkBleDevices.containsKey(this.mChildViewHolder.mMyChild.mac)) {
          MyExpandableListAdapter.this.mBluetoothLeService.unlinkBleDevices.remove(this.mChildViewHolder.mMyChild.mac);
        } else {
          MyExpandableListAdapter.this.mBluetoothLeService.unlinkBleDevices.put(this.mChildViewHolder.mMyChild.mac, this.mChildViewHolder.mMyChild.mac);
        } 
        if (MyExpandableListAdapter.this.mBluetoothLeService.MyBluetoothGatts.containsKey(this.mChildViewHolder.mMyChild.mac)) {
          MyBluetoothGatt myBluetoothGatt = MyExpandableListAdapter.this.mBluetoothLeService.MyBluetoothGatts.get(this.mChildViewHolder.mMyChild.mac);
          if (myBluetoothGatt != null)
            myBluetoothGatt.stopLEService(); 
          MyExpandableListAdapter.this.mBluetoothLeService.MyBluetoothGatts.remove(this.mChildViewHolder.mMyChild.mac);
        } else if (MyExpandableListAdapter.this.mBluetoothLeService.connBLE(this.mChildViewHolder.mMyChild.mac) == 1) {
          Context context = MyExpandableListAdapter.this.mContext;
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("");
          stringBuilder.append(MyExpandableListAdapter.this.mContext.getString(2131624037));
          Toast.makeText(context, stringBuilder.toString(), 0).show();
        } 
      } 
    }
    
    public myOnClickListener setChildViewHolder(MyExpandableListAdapter.ChildViewHolder param1ChildViewHolder, int param1Int) {
      this.mChildViewHolder = param1ChildViewHolder;
      this.type = param1Int;
      return this;
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\blelight\MyExpandableListAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */