package com.qh.data;

import android.util.Log;

public class DreamYdata {
  public byte Led_number_H;
  
  public byte Led_number_L;
  
  public int RGB_Line;
  
  public boolean connect_need_pwd;
  
  public int run_mode;
  
  public byte run_mode_next_time_H;
  
  public byte run_mode_next_time_L;
  
  public DreamYdata() {
    this.RGB_Line = 0;
    this.Led_number_H = (byte)0;
    this.Led_number_L = (byte)0;
    this.run_mode = 0;
    this.run_mode_next_time_H = (byte)0;
    this.run_mode_next_time_L = (byte)0;
    this.connect_need_pwd = false;
  }
  
  public DreamYdata(byte[] paramArrayOfbyte) {
    boolean bool = false;
    this.RGB_Line = 0;
    this.Led_number_H = (byte)0;
    this.Led_number_L = (byte)0;
    this.run_mode = 0;
    this.run_mode_next_time_H = (byte)0;
    this.run_mode_next_time_L = (byte)0;
    this.connect_need_pwd = false;
    if (paramArrayOfbyte != null && paramArrayOfbyte.length == 12 && (paramArrayOfbyte[0] & 0xFF) == 208 && (paramArrayOfbyte[11] & 0xFF) == 13) {
      this.RGB_Line = paramArrayOfbyte[2];
      this.Led_number_H = (byte)paramArrayOfbyte[3];
      this.Led_number_L = (byte)paramArrayOfbyte[4];
      this.run_mode = paramArrayOfbyte[5];
      this.run_mode_next_time_H = (byte)paramArrayOfbyte[6];
      this.run_mode_next_time_L = (byte)paramArrayOfbyte[7];
      if (paramArrayOfbyte[8] != 0)
        bool = true; 
      this.connect_need_pwd = bool;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("RGB_Line=");
    stringBuilder.append(this.RGB_Line);
    Log.e("DreamYdata", stringBuilder.toString());
    stringBuilder = new StringBuilder();
    stringBuilder.append("run_mode=");
    stringBuilder.append(this.run_mode);
    Log.e("DreamYdata", stringBuilder.toString());
    stringBuilder = new StringBuilder();
    stringBuilder.append("connect_need_pwd=");
    stringBuilder.append(this.connect_need_pwd);
    Log.e("DreamYdata", stringBuilder.toString());
    stringBuilder = new StringBuilder();
    stringBuilder.append("getLedNumber=");
    stringBuilder.append(getLedNumber());
    Log.e("DreamYdata", stringBuilder.toString());
    stringBuilder = new StringBuilder();
    stringBuilder.append("getRunModeNextTime=");
    stringBuilder.append(getRunModeNextTime());
    Log.e("DreamYdata", stringBuilder.toString());
  }
  
  public int getLedNumber() {
    return (this.Led_number_H & 0xFF) * 256 + (this.Led_number_L & 0xFF);
  }
  
  public int getRunModeNextTime() {
    return (this.run_mode_next_time_H & 0xFF) * 256 + (this.run_mode_next_time_L & 0xFF);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\data\DreamYdata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */