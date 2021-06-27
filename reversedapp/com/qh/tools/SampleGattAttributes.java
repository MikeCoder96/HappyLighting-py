package com.qh.tools;

import java.util.HashMap;

public class SampleGattAttributes {
  public static String CLIENT_CHARACTERISTIC_CONFIG;
  
  public static String HEART_RATE_MEASUREMENT;
  
  public static String HEART_RATE_MEASUREMENT2;
  
  private static HashMap<String, String> attributes = new HashMap<String, String>();
  
  static {
    HEART_RATE_MEASUREMENT = "0000ffe1-0000-1000-8000-00805f9b34fb";
    HEART_RATE_MEASUREMENT2 = "0000ffe2-0000-1000-8000-00805f9b34fb";
    CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    attributes.put("0000180d-0000-1000-8000-00805f9b34fb", "Heart Rate Service");
    attributes.put("0000180a-0000-1000-8000-00805f9b34fb", "Device Information Service");
    attributes.put(HEART_RATE_MEASUREMENT, "Heart Rate Measurement");
    attributes.put("00002a29-0000-1000-8000-00805f9b34fb", "Manufacturer Name String");
  }
  
  public static String lookup(String paramString1, String paramString2) {
    String str = attributes.get(paramString1);
    paramString1 = str;
    if (str == null)
      paramString1 = paramString2; 
    return paramString1;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\tools\SampleGattAttributes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */