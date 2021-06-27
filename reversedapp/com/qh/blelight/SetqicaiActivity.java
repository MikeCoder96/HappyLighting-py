package com.qh.blelight;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.qh.WheelView.OnWheelScrollListener;
import com.qh.WheelView.WheelView;
import com.qh.WheelView.WheelViewAdapter;
import com.qh.data.MyChild;
import com.qh.ui.dialog.SingleEnsureDialog;
import com.qh.ui.dialog.listener.ISingleDialogEnsureClickListener;
import java.util.ArrayList;

public class SetqicaiActivity extends Activity {
  private Button btn_send;
  
  private Context context;
  
  private ImageView img_open;
  
  private boolean isopenpwd = false;
  
  private RelativeLayout lin_back;
  
  private ArrayList<String> listqicairgb = new ArrayList<String>();
  
  private ArrayList<String> listqicaitype = new ArrayList<String>();
  
  private ArrayList mDataArrayList = new ArrayList();
  
  private LayoutInflater mInflator;
  
  private MyApplication mMyApplication;
  
  public BluetoothLeService.Qicaidata mQicaidata = new BluetoothLeService.Qicaidata() {
      public void readqicai(String param1String, int param1Int1, int param1Int2, boolean param1Boolean) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("addr=");
        stringBuilder.append(param1String);
        stringBuilder.append(" lighttype=");
        stringBuilder.append(param1Int2);
        stringBuilder.append(" rgb=");
        stringBuilder.append(param1Int1);
        Log.e("readqicai", stringBuilder.toString());
        if (SetqicaiActivity.this.listqicaitype.size() > SetqicaiActivity.this.type && param1Int2 >= 0) {
          SetqicaiActivity.access$102(SetqicaiActivity.this, param1Int2);
        } else {
          SetqicaiActivity.access$102(SetqicaiActivity.this, 0);
        } 
        if (SetqicaiActivity.this.listqicairgb.size() > param1Int1 && param1Int1 >= 0) {
          SetqicaiActivity.access$302(SetqicaiActivity.this, param1Int1);
        } else {
          SetqicaiActivity.access$302(SetqicaiActivity.this, 0);
        } 
        SetqicaiActivity.access$402(SetqicaiActivity.this, param1Boolean);
        SetqicaiActivity.this.uiHandler.sendEmptyMessage(0);
      }
    };
  
  private Resources mResources;
  
  private MyWheelViewAdapter myWheelViewAdapter;
  
  private RelativeLayout re_cotent;
  
  private RelativeLayout rel_4;
  
  private int rgbn = 0;
  
  private String selectMac = "";
  
  private Spinner spinner_qicairgb;
  
  private Spinner spinner_qicaitype;
  
  private WheelView timing_wheelview;
  
  private int type = 0;
  
  public Handler uiHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message param1Message) {
          if (param1Message.what == 0) {
            SetqicaiActivity.this.upui();
          } else if (param1Message.what == 4) {
            SetqicaiActivity.this.setData();
          } 
          return false;
        }
      });
  
  private void init() {
    this.context = (Context)this;
    this.mMyApplication = (MyApplication)getApplication();
    this.mResources = getResources();
    this.re_cotent = (RelativeLayout)findViewById(2131231042);
    this.spinner_qicaitype = (Spinner)findViewById(2131231143);
    this.spinner_qicairgb = (Spinner)findViewById(2131231142);
    this.img_open = (ImageView)findViewById(2131230915);
    this.btn_send = (Button)findViewById(2131230765);
    this.timing_wheelview = (WheelView)findViewById(2131231178);
    this.mInflator = getLayoutInflater();
    this.lin_back = (RelativeLayout)findViewById(2131230957);
    this.rel_4 = (RelativeLayout)findViewById(2131231048);
    this.rel_4.setVisibility(8);
    setData();
    String[] arrayOfString2 = getResources().getStringArray(2130837507);
    boolean bool = false;
    byte b;
    for (b = 0; b < arrayOfString2.length; b++)
      this.listqicaitype.add(arrayOfString2[b]); 
    ArrayAdapter arrayAdapter2 = new ArrayAdapter((Context)this, 2131361855, this.listqicaitype);
    arrayAdapter2.setDropDownViewResource(2131361854);
    this.spinner_qicaitype.setAdapter((SpinnerAdapter)arrayAdapter2);
    String[] arrayOfString1 = getResources().getStringArray(2130837509);
    for (b = bool; b < arrayOfString1.length; b++)
      this.listqicairgb.add(arrayOfString1[b]); 
    ArrayAdapter arrayAdapter1 = new ArrayAdapter((Context)this, 2131361855, this.listqicairgb);
    arrayAdapter1.setDropDownViewResource(2131361854);
    this.spinner_qicairgb.setAdapter((SpinnerAdapter)arrayAdapter1);
  }
  
  private void setData() {
    this.myWheelViewAdapter = new MyWheelViewAdapter();
    this.mDataArrayList.clear();
    for (String str : this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.keySet()) {
      MyBluetoothGatt myBluetoothGatt = this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.get(str);
      str = myBluetoothGatt.mLEdevice.getName();
      if (str.contains("Triones:") || str.contains("Triones#"))
        this.mDataArrayList.add(myBluetoothGatt); 
    } 
    this.myWheelViewAdapter.setData(this.mDataArrayList);
    this.timing_wheelview.setViewAdapter(this.myWheelViewAdapter);
    this.timing_wheelview.setVisibleItems(1);
    this.timing_wheelview.setCurrentItem(0);
    if (this.timing_wheelview.getCurrentItem() == 0) {
      this.re_cotent.setVisibility(8);
    } else {
      this.re_cotent.setVisibility(0);
    } 
  }
  
  private void setListener() {
    this.lin_back.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            SetqicaiActivity.this.finish();
          }
        });
    this.timing_wheelview.addScrollingListener(new OnWheelScrollListener() {
          public void onScrollingFinished(WheelView param1WheelView) {
            int i = param1WheelView.getCurrentItem();
            int j = SetqicaiActivity.this.mDataArrayList.size();
            int k = i - 1;
            if (j > k)
              if (k < 0) {
                SetqicaiActivity.access$702(SetqicaiActivity.this, "");
              } else {
                MyBluetoothGatt myBluetoothGatt = SetqicaiActivity.this.mDataArrayList.get(k);
                if (myBluetoothGatt != null) {
                  SetqicaiActivity.access$702(SetqicaiActivity.this, myBluetoothGatt.mAddr);
                  myBluetoothGatt.readqicai();
                } 
              }  
            if (i == 0) {
              SetqicaiActivity.this.re_cotent.setVisibility(8);
            } else {
              SetqicaiActivity.this.re_cotent.setVisibility(0);
            } 
          }
          
          public void onScrollingStarted(WheelView param1WheelView) {}
        });
    this.img_open.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            SetqicaiActivity.access$402(SetqicaiActivity.this, SetqicaiActivity.this.isopenpwd ^ true);
            if (SetqicaiActivity.this.isopenpwd) {
              SetqicaiActivity.this.img_open.setImageResource(2131165415);
            } else {
              SetqicaiActivity.this.img_open.setImageResource(2131165349);
            } 
          }
        });
    this.btn_send.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("spinner_qicaitype = ");
            stringBuilder.append(SetqicaiActivity.this.spinner_qicaitype.getSelectedItemPosition());
            Log.e("btn_send", stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append("spinner_qicairgb = ");
            stringBuilder.append(SetqicaiActivity.this.spinner_qicairgb.getSelectedItemPosition());
            Log.e("btn_send", stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append("isopen = ");
            stringBuilder.append(SetqicaiActivity.this.isopenpwd);
            Log.e("btn_send", stringBuilder.toString());
            if (SetqicaiActivity.this.mMyApplication.setqicaidata(SetqicaiActivity.this.selectMac, SetqicaiActivity.this.spinner_qicairgb.getSelectedItemPosition(), SetqicaiActivity.this.spinner_qicaitype.getSelectedItemPosition(), SetqicaiActivity.this.isopenpwd)) {
              SetqicaiActivity.this.showDialog(SetqicaiActivity.this.getResources().getString(2131624077));
            } else {
              SetqicaiActivity.this.showDialog(SetqicaiActivity.this.getResources().getString(2131624074));
            } 
          }
        });
  }
  
  private void showDialog(String paramString) {
    (new SingleEnsureDialog((Context)this, "", paramString, new ISingleDialogEnsureClickListener() {
          public void onEnsureClick() {}
        })).show();
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    setContentView(2131361841);
    init();
    setListener();
    this.mMyApplication.mBluetoothLeService.mQicaidata = this.mQicaidata;
    this.mMyApplication.qicaiHandler = this.uiHandler;
  }
  
  public void upui() {
    this.rel_4.setVisibility(8);
    if (this.isopenpwd) {
      this.img_open.setImageResource(2131165415);
    } else {
      this.img_open.setImageResource(2131165349);
    } 
    if (this.mMyApplication.mBluetoothLeService.mDevices.containsKey(this.selectMac)) {
      BluetoothDevice bluetoothDevice = this.mMyApplication.mBluetoothLeService.mDevices.get(this.selectMac);
      if (bluetoothDevice != null && bluetoothDevice.getName().contains("Triones:"))
        this.rel_4.setVisibility(0); 
    } 
    this.spinner_qicaitype.setSelection(this.type);
    this.spinner_qicairgb.setSelection(this.rgbn);
  }
  
  private class MyWheelViewAdapter implements WheelViewAdapter {
    public ArrayList<String> arraylistName = new ArrayList<String>();
    
    public ArrayList<MyBluetoothGatt> data = new ArrayList<MyBluetoothGatt>();
    
    private MyWheelViewAdapter() {}
    
    public View getEmptyItem(View param1View, ViewGroup param1ViewGroup) {
      return null;
    }
    
    public View getItem(int param1Int, View param1View, ViewGroup param1ViewGroup) {
      View view = SetqicaiActivity.this.mInflator.inflate(2131361889, null);
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
    
    public void registerDataSetObserver(DataSetObserver param1DataSetObserver) {}
    
    public void setData(ArrayList<MyBluetoothGatt> param1ArrayList) {
      this.arraylistName.clear();
      ArrayList<String> arrayList = this.arraylistName;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("");
      stringBuilder.append(SetqicaiActivity.this.mResources.getString(2131624032));
      arrayList.add(stringBuilder.toString());
      for (byte b = 0; b < param1ArrayList.size(); b++) {
        MyBluetoothGatt myBluetoothGatt = param1ArrayList.get(b);
        if (myBluetoothGatt != null) {
          MyChild myChild;
          if (SetqicaiActivity.this.mMyApplication.mMyExpandableListAdapter.DBdata.containsKey(myBluetoothGatt.mAddr)) {
            myChild = SetqicaiActivity.this.mMyApplication.mMyExpandableListAdapter.DBdata.get(myBluetoothGatt.mAddr);
            this.arraylistName.add(myChild.name);
          } else if (SetqicaiActivity.this.mMyApplication.mBluetoothLeService.mDevices.containsKey(((MyBluetoothGatt)myChild).mAddr)) {
            BluetoothDevice bluetoothDevice = SetqicaiActivity.this.mMyApplication.mBluetoothLeService.mDevices.get(((MyBluetoothGatt)myChild).mAddr);
            arrayList = this.arraylistName;
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append("");
            stringBuilder1.append(bluetoothDevice.getName());
            arrayList.add(stringBuilder1.toString());
          } 
        } 
      } 
      this.data.addAll(param1ArrayList);
    }
    
    public void unregisterDataSetObserver(DataSetObserver param1DataSetObserver) {}
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\blelight\SetqicaiActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */