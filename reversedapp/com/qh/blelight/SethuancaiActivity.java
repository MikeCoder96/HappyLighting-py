package com.qh.blelight;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.qh.WheelView.OnWheelScrollListener;
import com.qh.WheelView.WheelView;
import com.qh.WheelView.WheelViewAdapter;
import com.qh.data.DreamYdata;
import com.qh.data.MyChild;
import com.qh.ui.dialog.SingleEnsureDialog;
import com.qh.ui.dialog.listener.ISingleDialogEnsureClickListener;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class SethuancaiActivity extends Activity {
  private Button btn_send;
  
  private String deviceName = "";
  
  private EditText et_input;
  
  public EditText et_time_input;
  
  public int[] huancaitypes = new int[] { 
      0, 1, 2, 3, 4, 5, 6, 7, 9, 17, 
      11 };
  
  public ImageView img_open;
  
  public int lightnum = -1;
  
  public int lighttype = 0;
  
  private RelativeLayout lin_back;
  
  private ArrayList<String> listhuancainum = new ArrayList<String>();
  
  private ArrayList<String> listhuancairgb = new ArrayList<String>();
  
  private ArrayList<String> listhuancaitype = new ArrayList<String>();
  
  private ArrayList mDataArrayList = new ArrayList();
  
  public DreamYdata mDreamYdata = new DreamYdata();
  
  private LayoutInflater mInflator;
  
  private MyApplication mMyApplication;
  
  private Resources mResources;
  
  private Context mcontext;
  
  private MyWheelViewAdapter myWheelViewAdapter;
  
  public int order = 0;
  
  public RelativeLayout re_cotent;
  
  public RelativeLayout rel_5;
  
  public RelativeLayout rel_6;
  
  private RelativeLayout rel_main;
  
  private String selectMac = "";
  
  public long sendt = -1L;
  
  private Spinner spinner_huancaitype;
  
  private Spinner spinner_rgb;
  
  private Spinner spinner_xunhuan;
  
  public int sysmod = 0;
  
  private WheelView timing_wheelview;
  
  private TextView tv_shuancaitype;
  
  private TextView tv_srgb;
  
  public Handler uiHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(@NonNull Message param1Message) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("uiHandler =");
          stringBuilder.append(param1Message.what);
          Log.e("00", stringBuilder.toString());
          int i = param1Message.what;
          if (i != 4) {
            switch (i) {
              default:
                return false;
              case 2:
                SethuancaiActivity.this.showDialog(SethuancaiActivity.this.getResources().getString(2131624046));
              case 1:
                SethuancaiActivity.this.showDialog(SethuancaiActivity.this.getResources().getString(2131624051));
              case 0:
                break;
            } 
            int j = param1Message.arg1;
            i = SethuancaiActivity.this.mDataArrayList.size();
            if (i > --j) {
              if (j < 0)
                SethuancaiActivity.access$102(SethuancaiActivity.this, ""); 
              MyBluetoothGatt myBluetoothGatt = SethuancaiActivity.this.mDataArrayList.get(j);
              if (myBluetoothGatt != null) {
                StringBuilder stringBuilder1;
                SethuancaiActivity.access$102(SethuancaiActivity.this, myBluetoothGatt.mAddr);
                SethuancaiActivity.access$202(SethuancaiActivity.this, myBluetoothGatt.mLEdevice.getName());
                SethuancaiActivity.this.lighttype = myBluetoothGatt.huancaidata[1];
                SethuancaiActivity.this.lightnum = (myBluetoothGatt.huancaidata[2] & 0xFF) * 256 | myBluetoothGatt.huancaidata[3] & 0xFF;
                SethuancaiActivity.this.order = myBluetoothGatt.huancaidata[4];
                SethuancaiActivity.this.sysmod = myBluetoothGatt.huancaidata[5];
                if (SethuancaiActivity.this.lighttype == 9)
                  SethuancaiActivity.this.lighttype = 8; 
                if (SethuancaiActivity.this.lighttype == 17)
                  SethuancaiActivity.this.lighttype = 9; 
                if (SethuancaiActivity.this.lighttype == 11)
                  SethuancaiActivity.this.lighttype = 10; 
                if (SethuancaiActivity.this.spinner_huancaitype.getAdapter().getCount() > SethuancaiActivity.this.lighttype)
                  SethuancaiActivity.this.spinner_huancaitype.setSelection(SethuancaiActivity.this.lighttype); 
                if (SethuancaiActivity.this.spinner_rgb.getAdapter().getCount() > SethuancaiActivity.this.order)
                  SethuancaiActivity.this.spinner_rgb.setSelection(SethuancaiActivity.this.order); 
                if (SethuancaiActivity.this.spinner_xunhuan.getAdapter().getCount() > SethuancaiActivity.this.sysmod)
                  SethuancaiActivity.this.spinner_xunhuan.setSelection(SethuancaiActivity.this.sysmod); 
                if (SethuancaiActivity.this.lightnum != -1) {
                  EditText editText = SethuancaiActivity.this.et_input;
                  stringBuilder = new StringBuilder();
                  stringBuilder.append("");
                  stringBuilder.append(SethuancaiActivity.this.lightnum);
                  editText.setText(stringBuilder.toString());
                } 
                if (SethuancaiActivity.this.isDreamY(myBluetoothGatt.mLEdevice.getName())) {
                  SethuancaiActivity.this.rel_5.setVisibility(0);
                  SethuancaiActivity.this.rel_6.setVisibility(0);
                  SethuancaiActivity.this.mDreamYdata = myBluetoothGatt.mDreamYdata;
                  SethuancaiActivity.this.spinner_huancaitype.setVisibility(8);
                  SethuancaiActivity.this.tv_shuancaitype.setVisibility(0);
                  SethuancaiActivity.this.tv_shuancaitype.setText(SethuancaiActivity.this.mResources.getString(2131624015));
                  SethuancaiActivity.this.order = SethuancaiActivity.this.mDreamYdata.RGB_Line;
                  if (SethuancaiActivity.this.spinner_rgb.getAdapter().getCount() > SethuancaiActivity.this.order)
                    SethuancaiActivity.this.spinner_rgb.setSelection(SethuancaiActivity.this.order); 
                  String[] arrayOfString1 = SethuancaiActivity.this.getResources().getStringArray(2130837512);
                  SethuancaiActivity.this.listhuancainum.clear();
                  for (i = 0; i < arrayOfString1.length; i++)
                    SethuancaiActivity.this.listhuancainum.add(arrayOfString1[i]); 
                  ArrayAdapter arrayAdapter1 = new ArrayAdapter(SethuancaiActivity.this.mcontext, 2131361855, SethuancaiActivity.this.listhuancainum);
                  arrayAdapter1.setDropDownViewResource(2131361854);
                  SethuancaiActivity.this.spinner_xunhuan.setAdapter((SpinnerAdapter)arrayAdapter1);
                  SethuancaiActivity.this.sysmod = SethuancaiActivity.this.mDreamYdata.run_mode;
                  if (SethuancaiActivity.this.spinner_xunhuan.getAdapter().getCount() > SethuancaiActivity.this.sysmod)
                    SethuancaiActivity.this.spinner_xunhuan.setSelection(SethuancaiActivity.this.sysmod); 
                  EditText editText1 = SethuancaiActivity.this.et_input;
                  stringBuilder = new StringBuilder();
                  stringBuilder.append("");
                  stringBuilder.append(SethuancaiActivity.this.mDreamYdata.getLedNumber());
                  editText1.setText(stringBuilder.toString());
                  EditText editText2 = SethuancaiActivity.this.et_time_input;
                  stringBuilder1 = new StringBuilder();
                  stringBuilder1.append("");
                  stringBuilder1.append(SethuancaiActivity.this.mDreamYdata.getRunModeNextTime());
                  editText2.setText(stringBuilder1.toString());
                  if (SethuancaiActivity.this.mDreamYdata.connect_need_pwd)
                    SethuancaiActivity.this.img_open.setImageResource(2131165415); 
                  SethuancaiActivity.this.img_open.setImageResource(2131165349);
                } 
                SethuancaiActivity.this.spinner_huancaitype.setVisibility(0);
                SethuancaiActivity.this.tv_shuancaitype.setVisibility(8);
                SethuancaiActivity.this.rel_5.setVisibility(8);
                SethuancaiActivity.this.rel_6.setVisibility(8);
                String[] arrayOfString = SethuancaiActivity.this.getResources().getStringArray(2130837511);
                SethuancaiActivity.this.listhuancainum.clear();
                for (i = 0; i < arrayOfString.length; i++)
                  SethuancaiActivity.this.listhuancainum.add(arrayOfString[i]); 
                ArrayAdapter arrayAdapter = new ArrayAdapter(SethuancaiActivity.this.mcontext, 2131361855, SethuancaiActivity.this.listhuancainum);
                arrayAdapter.setDropDownViewResource(2131361854);
                SethuancaiActivity.this.spinner_xunhuan.setAdapter((SpinnerAdapter)arrayAdapter);
                SethuancaiActivity.this.order = ((MyBluetoothGatt)stringBuilder1).huancaidata[4];
                SethuancaiActivity.this.sysmod = ((MyBluetoothGatt)stringBuilder1).huancaidata[5];
                if (SethuancaiActivity.this.spinner_xunhuan.getAdapter().getCount() > SethuancaiActivity.this.sysmod)
                  SethuancaiActivity.this.spinner_xunhuan.setSelection(SethuancaiActivity.this.sysmod); 
              } 
            } 
          } 
          SethuancaiActivity.this.setData(false);
        }
      });
  
  private void setData(boolean paramBoolean) {
    if (paramBoolean) {
      setDatas();
    } else {
      this.mDataArrayList.clear();
      for (String str : this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.keySet()) {
        MyBluetoothGatt myBluetoothGatt = this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt.mLEdevice.getName().contains("Dream") || myBluetoothGatt.mLEdevice.getName().contains("Flash"))
          this.mDataArrayList.add(myBluetoothGatt); 
      } 
      if (this.mDataArrayList != null && this.mDataArrayList.size() > 0 && this.myWheelViewAdapter.getdata() != null && this.myWheelViewAdapter.getdata().size() > 0 && this.timing_wheelview.getCurrentItem() > this.myWheelViewAdapter.getdata().size()) {
        byte b = 0;
        paramBoolean = false;
        while (b < this.mDataArrayList.size()) {
          paramBoolean = ((MyBluetoothGatt)this.mDataArrayList.get(b)).mAddr.equals(((MyBluetoothGatt)this.myWheelViewAdapter.getdata().get(this.timing_wheelview.getCurrentItem() - 1)).mAddr);
          b++;
        } 
      } else {
        paramBoolean = false;
      } 
      if (!paramBoolean) {
        setDatas();
        this.re_cotent.setVisibility(8);
      } else {
        this.re_cotent.setVisibility(0);
      } 
    } 
  }
  
  private void setDatas() {
    if (this.myWheelViewAdapter != null)
      this.myWheelViewAdapter = null; 
    this.myWheelViewAdapter = new MyWheelViewAdapter();
    this.mDataArrayList.clear();
    for (String str : this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.keySet()) {
      MyBluetoothGatt myBluetoothGatt = this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.get(str);
      if (myBluetoothGatt.mLEdevice.getName().contains("Dream") || myBluetoothGatt.mLEdevice.getName().contains("Flash"))
        this.mDataArrayList.add(myBluetoothGatt); 
    } 
    this.myWheelViewAdapter.setData(this.mDataArrayList);
    this.timing_wheelview.setViewAdapter(this.myWheelViewAdapter);
    this.timing_wheelview.setVisibleItems(1);
    this.timing_wheelview.setCurrentItem(0);
  }
  
  private void showDialog(String paramString) {
    (new SingleEnsureDialog((Context)this, "", paramString, new ISingleDialogEnsureClickListener() {
          public void onEnsureClick() {}
        })).show();
  }
  
  public void init() {
    this.img_open = (ImageView)findViewById(2131230915);
    this.et_time_input = (EditText)findViewById(2131230797);
    this.rel_5 = (RelativeLayout)findViewById(2131231049);
    this.rel_6 = (RelativeLayout)findViewById(2131231051);
    this.tv_srgb = (TextView)findViewById(2131231228);
    this.tv_shuancaitype = (TextView)findViewById(2131231227);
    this.re_cotent = (RelativeLayout)findViewById(2131231042);
    this.mInflator = getLayoutInflater();
    this.timing_wheelview = (WheelView)findViewById(2131231178);
    setData(true);
    this.rel_main = (RelativeLayout)findViewById(2131231070);
    if (this.mMyApplication.bgsrc.length > this.mMyApplication.typebg)
      this.rel_main.setBackgroundResource(this.mMyApplication.bgsrc[this.mMyApplication.typebg]); 
    this.et_input = (EditText)findViewById(2131230793);
    this.spinner_huancaitype = (Spinner)findViewById(2131231141);
    String[] arrayOfString3 = getResources().getStringArray(2130837504);
    boolean bool = false;
    byte b;
    for (b = 0; b < arrayOfString3.length; b++)
      this.listhuancaitype.add(arrayOfString3[b]); 
    ArrayAdapter arrayAdapter3 = new ArrayAdapter((Context)this, 2131361855, this.listhuancaitype);
    arrayAdapter3.setDropDownViewResource(2131361854);
    this.spinner_huancaitype.setAdapter((SpinnerAdapter)arrayAdapter3);
    this.btn_send = (Button)findViewById(2131230765);
    this.spinner_rgb = (Spinner)findViewById(2131231144);
    this.spinner_xunhuan = (Spinner)findViewById(2131231145);
    String[] arrayOfString2 = getResources().getStringArray(2130837511);
    this.listhuancainum.clear();
    for (b = 0; b < arrayOfString2.length; b++)
      this.listhuancainum.add(arrayOfString2[b]); 
    ArrayAdapter arrayAdapter2 = new ArrayAdapter((Context)this, 2131361855, this.listhuancainum);
    arrayAdapter2.setDropDownViewResource(2131361854);
    this.spinner_xunhuan.setAdapter((SpinnerAdapter)arrayAdapter2);
    String[] arrayOfString1 = getResources().getStringArray(2130837508);
    for (b = bool; b < arrayOfString1.length; b++)
      this.listhuancairgb.add(arrayOfString1[b]); 
    ArrayAdapter arrayAdapter1 = new ArrayAdapter((Context)this, 2131361855, this.listhuancairgb);
    arrayAdapter1.setDropDownViewResource(2131361854);
    this.spinner_rgb.setAdapter((SpinnerAdapter)arrayAdapter1);
    this.btn_send.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (SethuancaiActivity.this.timing_wheelview.getCurrentItem() <= 0) {
              SethuancaiActivity.this.showDialog(SethuancaiActivity.this.getResources().getString(2131624071));
              return;
            } 
            long l = System.currentTimeMillis();
            if (l - SethuancaiActivity.this.sendt <= 3500L)
              return; 
            SethuancaiActivity.this.sendt = l;
            String str = SethuancaiActivity.this.et_input.getEditableText().toString();
            try {
              int i = Integer.parseInt(str);
              String str1 = SethuancaiActivity.this.et_time_input.getEditableText().toString();
              try {
                byte[] arrayOfByte;
                int j = Integer.parseInt(str1);
                if (j < 6 || j >= 65535) {
                  SethuancaiActivity.this.showDialog(SethuancaiActivity.this.getResources().getString(2131624009));
                  return;
                } 
                if (i > 1024) {
                  SethuancaiActivity.this.showDialog(SethuancaiActivity.this.getResources().getString(2131624045));
                  return;
                } 
                if ("".equals(SethuancaiActivity.this.selectMac))
                  return; 
                if (!SethuancaiActivity.this.mMyApplication.isconn(SethuancaiActivity.this.selectMac)) {
                  SethuancaiActivity.this.showDialog(SethuancaiActivity.this.getResources().getString(2131624046));
                  return;
                } 
                if (!SethuancaiActivity.this.mMyApplication.isDreamY(SethuancaiActivity.this.selectMac)) {
                  if (SethuancaiActivity.this.huancaitypes.length > SethuancaiActivity.this.spinner_huancaitype.getSelectedItemPosition()) {
                    j = SethuancaiActivity.this.huancaitypes[SethuancaiActivity.this.spinner_huancaitype.getSelectedItemPosition()];
                  } else {
                    j = 0;
                  } 
                  int k = SethuancaiActivity.this.spinner_rgb.getSelectedItemPosition();
                  int m = SethuancaiActivity.this.spinner_xunhuan.getSelectedItemPosition();
                  byte b = (byte)(i / 256);
                  i = (byte)(i & 0xFF);
                  arrayOfByte = new byte[8];
                  arrayOfByte[0] = (byte)27;
                  arrayOfByte[1] = (byte)(byte)j;
                  arrayOfByte[2] = (byte)b;
                  arrayOfByte[3] = (byte)i;
                  arrayOfByte[4] = (byte)(byte)k;
                  arrayOfByte[5] = (byte)(byte)m;
                  arrayOfByte[6] = (byte)0;
                  arrayOfByte[7] = (byte)-16;
                  if (SethuancaiActivity.this.deviceName.contains("Dream&") || SethuancaiActivity.this.deviceName.contains("Flash&")) {
                    arrayOfByte[1] = (byte)11;
                    arrayOfByte[2] = (byte)0;
                  } 
                  SethuancaiActivity.this.mMyApplication.setData(SethuancaiActivity.this.selectMac, arrayOfByte);
                  SethuancaiActivity.this.showDialog(SethuancaiActivity.this.getResources().getString(2131624073));
                  (new Handler()).postDelayed(new Runnable() {
                        public void run() {
                          SethuancaiActivity.this.mMyApplication.readhuancai(SethuancaiActivity.this.selectMac);
                        }
                      },  500L);
                } else {
                  str1 = SethuancaiActivity.this.et_time_input.getEditableText().toString();
                  i = SethuancaiActivity.this.spinner_rgb.getSelectedItemPosition();
                  int k = SethuancaiActivity.this.spinner_xunhuan.getSelectedItemPosition();
                  try {
                    int m = Integer.parseInt((String)arrayOfByte);
                    j = Integer.parseInt(str1);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("num=");
                    stringBuilder.append(m);
                    stringBuilder.append(" time=");
                    stringBuilder.append(j);
                    Log.e("aaa", stringBuilder.toString());
                    byte b1 = (byte)(m / 256);
                    byte b2 = (byte)(m & 0xFF);
                    byte b3 = (byte)(j / 256);
                    byte b4 = (byte)(j & 0xFF);
                    boolean bool = SethuancaiActivity.this.mDreamYdata.connect_need_pwd;
                    byte b5 = (byte)i;
                    byte b6 = (byte)k;
                    SethuancaiActivity.this.mMyApplication.setData(SethuancaiActivity.this.selectMac, new byte[] { 
                          -14, 0, b5, b1, b2, b6, b3, b4, bool, 0, 
                          0, 47 });
                    (new Handler()).postDelayed(new Runnable() {
                          public void run() {
                            SethuancaiActivity.this.mMyApplication.readhuancai(SethuancaiActivity.this.selectMac);
                          }
                        },  700L);
                    return;
                  } catch (NumberFormatException numberFormatException) {
                    SethuancaiActivity.this.showDialog(SethuancaiActivity.this.getResources().getString(2131624045));
                    return;
                  } 
                } 
                return;
              } catch (NumberFormatException numberFormatException) {
                SethuancaiActivity.this.showDialog(SethuancaiActivity.this.getResources().getString(2131624009));
                return;
              } 
            } catch (NumberFormatException numberFormatException) {
              SethuancaiActivity.this.showDialog(SethuancaiActivity.this.getResources().getString(2131624045));
              return;
            } 
          }
        });
    this.timing_wheelview.addScrollingListener(new OnWheelScrollListener() {
          public void onScrollingFinished(WheelView param1WheelView) {
            int i = param1WheelView.getCurrentItem();
            Message message = new Message();
            message.what = 0;
            message.arg1 = i;
            SethuancaiActivity.this.uiHandler.sendMessage(message);
            if (i == 0) {
              SethuancaiActivity.this.re_cotent.setVisibility(8);
            } else {
              SethuancaiActivity.this.re_cotent.setVisibility(0);
            } 
          }
          
          public void onScrollingStarted(WheelView param1WheelView) {}
        });
    this.img_open.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            SethuancaiActivity.this.mDreamYdata.connect_need_pwd ^= 0x1;
            if (SethuancaiActivity.this.mDreamYdata.connect_need_pwd) {
              SethuancaiActivity.this.img_open.setImageResource(2131165415);
            } else {
              SethuancaiActivity.this.img_open.setImageResource(2131165349);
            } 
          }
        });
  }
  
  public boolean isDreamY(String paramString) {
    return Pattern.compile("^Dream\\#|^Dream~").matcher(paramString).find();
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    setContentView(2131361840);
    this.mcontext = (Context)this;
    this.lin_back = (RelativeLayout)findViewById(2131230957);
    this.lin_back.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            SethuancaiActivity.this.finish();
          }
        });
    this.mResources = getResources();
    this.mMyApplication = (MyApplication)getApplication();
    this.mMyApplication.huancaiHandler = this.uiHandler;
    init();
  }
  
  private class MyWheelViewAdapter implements WheelViewAdapter {
    public ArrayList<String> arraylistName = new ArrayList<String>();
    
    public ArrayList<MyBluetoothGatt> data = new ArrayList<MyBluetoothGatt>();
    
    private MyWheelViewAdapter() {}
    
    public View getEmptyItem(View param1View, ViewGroup param1ViewGroup) {
      return null;
    }
    
    public View getItem(int param1Int, View param1View, ViewGroup param1ViewGroup) {
      View view = SethuancaiActivity.this.mInflator.inflate(2131361889, null);
      TextView textView = (TextView)view.findViewById(2131231244);
      ((ImageView)view.findViewById(2131230881)).setVisibility(8);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("");
      stringBuilder.append(this.arraylistName.get(param1Int));
      textView.setText(stringBuilder.toString());
      return view;
    }
    
    public int getItemsCount() {
      return this.arraylistName.size();
    }
    
    public ArrayList<MyBluetoothGatt> getdata() {
      return this.data;
    }
    
    public void registerDataSetObserver(DataSetObserver param1DataSetObserver) {}
    
    public void setData(ArrayList<MyBluetoothGatt> param1ArrayList) {
      this.arraylistName.clear();
      ArrayList<String> arrayList = this.arraylistName;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("");
      stringBuilder.append(SethuancaiActivity.this.mResources.getString(2131624032));
      arrayList.add(stringBuilder.toString());
      for (byte b = 0; b < param1ArrayList.size(); b++) {
        MyBluetoothGatt myBluetoothGatt = param1ArrayList.get(b);
        if (myBluetoothGatt != null) {
          MyChild myChild;
          if (SethuancaiActivity.this.mMyApplication.mMyExpandableListAdapter.DBdata.containsKey(myBluetoothGatt.mAddr)) {
            myChild = SethuancaiActivity.this.mMyApplication.mMyExpandableListAdapter.DBdata.get(myBluetoothGatt.mAddr);
            this.arraylistName.add(myChild.name);
          } else if (SethuancaiActivity.this.mMyApplication.mBluetoothLeService.mDevices.containsKey(((MyBluetoothGatt)myChild).mAddr)) {
            BluetoothDevice bluetoothDevice = SethuancaiActivity.this.mMyApplication.mBluetoothLeService.mDevices.get(((MyBluetoothGatt)myChild).mAddr);
            ArrayList<String> arrayList1 = this.arraylistName;
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append("");
            stringBuilder1.append(bluetoothDevice.getName());
            arrayList1.add(stringBuilder1.toString());
          } 
        } 
      } 
      this.data.addAll(param1ArrayList);
    }
    
    public void unregisterDataSetObserver(DataSetObserver param1DataSetObserver) {}
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\blelight\SethuancaiActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */