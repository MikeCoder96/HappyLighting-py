package com.consmart.ble;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {
  static String MOD = "AES/ECB/PKCS5Padding";
  
  static byte[] sKey = new byte[] { 
      -48, -7, -12, -116, 89, -94, 105, 29, 24, 83, 
      -53, -38, Byte.MIN_VALUE, -124, 67, -109 };
  
  public static byte[] Decrypt(byte[] paramArrayOfbyte) throws Exception {
    try {
      if (sKey == null) {
        System.out.print("Key为空null");
        return null;
      } 
      if (sKey.length != 16) {
        System.out.print("Key长度不是16位");
        return null;
      } 
      SecretKeySpec secretKeySpec = new SecretKeySpec();
      this(sKey, "AES");
      Cipher cipher = Cipher.getInstance(MOD);
      cipher.init(2, secretKeySpec);
      try {
        return cipher.doFinal(paramArrayOfbyte);
      } catch (Exception exception) {
        System.out.println(exception.toString());
        return null;
      } 
    } catch (Exception exception) {
      System.out.println(exception.toString());
      return null;
    } 
  }
  
  public static byte[] Encrypt(byte[] paramArrayOfbyte) throws Exception {
    if (sKey == null) {
      System.out.print("Key为空null");
      return null;
    } 
    if (sKey.length != 16) {
      System.out.print("Key长度不是16位");
      return null;
    } 
    SecretKeySpec secretKeySpec = new SecretKeySpec(sKey, "AES");
    Cipher cipher = Cipher.getInstance(MOD);
    cipher.init(1, secretKeySpec);
    return cipher.doFinal(paramArrayOfbyte);
  }
  
  public static void main(String[] paramArrayOfString) throws Exception {
    byte[] arrayOfByte1 = new byte[4];
    arrayOfByte1[0] = 10;
    arrayOfByte1[1] = 11;
    arrayOfByte1[2] = 52;
    arrayOfByte1[3] = 12;
    System.out.println(arrayOfByte1);
    byte[] arrayOfByte2 = Encrypt(arrayOfByte1);
    String str1 = "原数据加密后- ";
    byte b = 0;
    if (arrayOfByte2 != null) {
      str1 = "原数据加密后- ";
      for (byte b1 = 0; b1 < arrayOfByte2.length; b1++) {
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(str1));
        stringBuilder.append(Integer.toHexString(arrayOfByte2[b1] & 0xFF));
        stringBuilder.append(" ");
        str1 = stringBuilder.toString();
      } 
    } 
    System.out.println(str1);
    byte[] arrayOfByte3 = Decrypt(arrayOfByte2);
    str1 = "解密后的-";
    String str2 = str1;
    if (arrayOfByte3 != null)
      for (byte b1 = b;; b1++) {
        if (b1 >= arrayOfByte3.length) {
          str2 = str1;
          break;
        } 
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(str1));
        stringBuilder.append(Integer.toHexString(arrayOfByte3[b1] & 0xFF));
        stringBuilder.append(" ");
        String str = stringBuilder.toString();
      }  
    System.out.println(str2);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\consmart\ble\AES.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */