package com.qh.data;

public class NewTime {
  public int h = 0;
  
  public int m = 0;
  
  public boolean open = true;
  
  public int s = 0;
  
  public boolean valid = false;
  
  public byte w = (byte)0;
  
  public NewTime() {}
  
  public NewTime(boolean paramBoolean1, int paramInt1, int paramInt2, int paramInt3, byte paramByte, boolean paramBoolean2) {
    this.valid = paramBoolean1;
    this.h = paramInt1;
    this.m = paramInt2;
    this.s = paramInt3;
    this.w = (byte)paramByte;
    this.open = paramBoolean2;
  }
  
  public int getH() {
    return this.h;
  }
  
  public int getM() {
    return this.m;
  }
  
  public int getS() {
    return this.s;
  }
  
  public int getW() {
    return this.w;
  }
  
  public boolean isOpen() {
    return this.open;
  }
  
  public boolean isValid() {
    return this.valid;
  }
  
  public void setH(int paramInt) {
    this.h = paramInt;
  }
  
  public void setM(int paramInt) {
    this.m = paramInt;
  }
  
  public void setOpen(boolean paramBoolean) {
    this.open = paramBoolean;
  }
  
  public void setS(int paramInt) {
    this.s = paramInt;
  }
  
  public void setValid(boolean paramBoolean) {
    this.valid = paramBoolean;
  }
  
  public void setW(byte paramByte) {
    this.w = (byte)paramByte;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\data\NewTime.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */