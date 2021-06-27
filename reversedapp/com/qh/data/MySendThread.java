package com.qh.data;

import android.os.Handler;
import com.qh.blelight.MyBluetoothGatt;

public class MySendThread {
  private Handler handler;
  
  private boolean isopen;
  
  private MyBluetoothGatt mMyBluetoothGatt;
  
  private Runnable runnable;
  
  private int tag = 0;
  
  public MySendThread(MyBluetoothGatt paramMyBluetoothGatt, boolean paramBoolean) {
    this.mMyBluetoothGatt = paramMyBluetoothGatt;
    this.isopen = paramBoolean;
    this.handler = new Handler();
    this.runnable = new Runnable() {
        public void run() {
          if (MySendThread.this.tag < 15) {
            MySendThread.this.mMyBluetoothGatt.openLight(MySendThread.this.isopen);
            MySendThread.this.handler.postDelayed(MySendThread.this.runnable, 50L);
          } else {
            MySendThread.this.handler.removeCallbacks(MySendThread.this.runnable);
          } 
          MySendThread.access$008(MySendThread.this);
        }
      };
    this.handler.postDelayed(this.runnable, 0L);
  }
  
  public MySendThread(MyBluetoothGatt paramMyBluetoothGatt, final boolean isOpen, final int speed, final int modid) {
    this.mMyBluetoothGatt = paramMyBluetoothGatt;
    this.handler = new Handler();
    this.runnable = new Runnable() {
        public void run() {
          if (MySendThread.this.tag < 10) {
            MySendThread.this.mMyBluetoothGatt.sendDreamWM(isOpen, speed, modid);
            MySendThread.this.handler.postDelayed(MySendThread.this.runnable, 50L);
          } else {
            MySendThread.this.handler.removeCallbacks(MySendThread.this.runnable);
          } 
          MySendThread.access$008(MySendThread.this);
        }
      };
    this.handler.postDelayed(this.runnable, 0L);
  }
  
  public MySendThread(MyBluetoothGatt paramMyBluetoothGatt, final boolean isOpen, final int speed, final int modid, boolean paramBoolean2) {
    this.mMyBluetoothGatt = paramMyBluetoothGatt;
    this.handler = new Handler();
    this.runnable = new Runnable() {
        public void run() {
          if (MySendThread.this.tag < 10) {
            MySendThread.this.mMyBluetoothGatt.sendColor_m_data(isOpen, speed, modid);
            MySendThread.this.handler.postDelayed(MySendThread.this.runnable, 50L);
          } else {
            MySendThread.this.handler.removeCallbacks(MySendThread.this.runnable);
          } 
          MySendThread.access$008(MySendThread.this);
        }
      };
    this.handler.postDelayed(this.runnable, 0L);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\data\MySendThread.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */