package com.android.soundrecorder.sound;

public class VoiceUtil {
  public static double getVolume(byte[] paramArrayOfbyte, int paramInt) {
    byte b = 0;
    int i = 0;
    while (b < paramArrayOfbyte.length) {
      i += paramArrayOfbyte[b] * paramArrayOfbyte[b];
      b++;
    } 
    return (i / paramInt);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\android\soundrecorder\sound\VoiceUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */