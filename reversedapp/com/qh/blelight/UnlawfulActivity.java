package com.qh.blelight;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class UnlawfulActivity extends Activity {
  private TextView OK;
  
  private ListView error_list;
  
  private ArrayList<BluetoothDevice> errors = new ArrayList<BluetoothDevice>();
  
  private LayoutInflater mInflator;
  
  public MyApplication mMyApplication;
  
  public Resources mResources;
  
  private Handler msgHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message param1Message) {
          if (param1Message.what == 1)
            UnlawfulActivity.this.updata(); 
          return false;
        }
      });
  
  private BaseAdapter myAdapter = new BaseAdapter() {
      public int getCount() {
        return UnlawfulActivity.this.errors.size();
      }
      
      public Object getItem(int param1Int) {
        return Integer.valueOf(param1Int);
      }
      
      public long getItemId(int param1Int) {
        return param1Int;
      }
      
      public View getView(int param1Int, View param1View, ViewGroup param1ViewGroup) {
        BluetoothDevice bluetoothDevice = UnlawfulActivity.this.errors.get(param1Int);
        View view = param1View;
        if (param1View == null) {
          view = UnlawfulActivity.this.mInflator.inflate(2131361852, null);
          TextView textView = (TextView)view.findViewById(2131231213);
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("");
          stringBuilder.append(bluetoothDevice.getName());
          textView.setText(stringBuilder.toString());
        } 
        return view;
      }
    };
  
  private void init() {
    this.error_list = (ListView)findViewById(2131230791);
    this.error_list.setAdapter((ListAdapter)this.myAdapter);
    updata();
    this.OK = (TextView)findViewById(2131230725);
    this.OK.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            UnlawfulActivity.this.mMyApplication.isshow = false;
            UnlawfulActivity.this.finish();
          }
        });
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    setContentView(2131361879);
    this.mResources = getResources();
    this.mInflator = getLayoutInflater();
    this.mMyApplication = (MyApplication)getApplication();
    this.mMyApplication.errorHandler = this.msgHandler;
    init();
  }
  
  protected void onDestroy() {
    this.mMyApplication.isshow = false;
    super.onDestroy();
  }
  
  public void updata() {
    this.errors.clear();
    for (String str : this.mMyApplication.errorDevices.keySet()) {
      BluetoothDevice bluetoothDevice = this.mMyApplication.errorDevices.get(str);
      if (bluetoothDevice != null)
        this.errors.add(bluetoothDevice); 
    } 
    this.myAdapter.notifyDataSetChanged();
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\blelight\UnlawfulActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */