package com.android.soundrecorder.sound;

import android.graphics.Color;
import android.util.Log;

public class ColorUtils {
  public static final int[] COLOR_LIST = new int[] { 16711680, 16743680, 16776960, 65280, 65535, 255, 16711935, 16711680 };
  
  public static final int[] COLOR_LIST_140;
  
  public static final int[] COLOR_LIST_70 = new int[] { 
      -65536, -62464, -59136, -56064, -52736, -49664, -46336, -43264, -39936, -36864, 
      -33536, -30208, -26880, -23552, -20224, -16896, -13568, -10240, -6912, -3584, 
      -256, -1704192, -3342592, -5046528, -6684928, -8388864, -10027264, -11731200, -13369600, -15073536, 
      -16711936, -16711911, -16711885, -16711860, -16711834, -16711809, -16711783, -16711758, -16711732, -16711707, 
      -16711681, -16718337, -16724737, -16731393, -16737793, -16744449, -16750849, -16757505, -16763905, -16770561, 
      -16776961, -15138561, -13434625, -11796225, -10092289, -8453889, -6749953, -5111553, -3407617, -1769217, 
      -65281, -65307, -65332, -65358, -65383, -65409, -65434, -65460, -65485, -65511 };
  
  static {
    COLOR_LIST_140 = new int[] { 
        -65536, -64000, -62464, -60928, -59136, -57600, -56064, -54528, -52736, -51200, 
        -49664, -48128, -46336, -44800, -43264, -41728, -39936, -38400, -36864, -35328, 
        -33536, -32000, -30208, -28672, -26880, -25344, -23552, -22016, -20224, -18688, 
        -16896, -15360, -13568, -12032, -10240, -8704, -6912, -5376, -3584, -2048, 
        -256, -852224, -1704192, -2556160, -3342592, -4194560, -5046528, -5898496, -6684928, -7536896, 
        -8388864, -9240832, -10027264, -10879232, -11731200, -12583168, -13369600, -14221568, -15073536, -15925504, 
        -16711936, -16711924, -16711911, -16711898, -16711885, -16711873, -16711860, -16711847, -16711834, -16711822, 
        -16711809, -16711796, -16711783, -16711771, -16711758, -16711745, -16711732, -16711720, -16711707, -16711694, 
        -16711681, -16715009, -16718337, -16721665, -16724737, -16728065, -16731393, -16734721, -16737793, -16741121, 
        -16744449, -16747777, -16750849, -16754177, -16757505, -16760833, -16763905, -16767233, -16770561, -16773889, 
        -16776961, -15990529, -15138561, -14286593, -13434625, -12648193, -11796225, -10944257, -10092289, -9305857, 
        -8453889, -7601921, -6749953, -5963521, -5111553, -4259585, -3407617, -2621185, -1769217, -917249, 
        -65281, -65294, -65307, -65320, -65332, -65345, -65358, -65371, -65383, -65396, 
        -65409, -65422, -65434, -65447, -65460, -65473, -65485, -65498, -65511, -65524 };
  }
  
  public static int getColor(int paramInt1, int paramInt2, float paramFloat) {
    int i = Color.red(paramInt1);
    int j = Color.blue(paramInt1);
    int k = Color.green(paramInt1);
    int m = Color.red(paramInt2);
    paramInt1 = Color.blue(paramInt2);
    paramInt2 = Color.green(paramInt2);
    return Color.rgb((int)(i + (m - i) * paramFloat), (int)(k + (paramInt2 - k) * paramFloat), (int)(j + (paramInt1 - j) * paramFloat));
  }
  
  public static void initColors() {
    for (byte b = 0; b < COLOR_LIST.length - 1; b++) {
      String str = "[";
      for (byte b1 = 0; b1 < 20; b1++) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append(str);
        stringBuilder1.append(Integer.toHexString(getColor(COLOR_LIST[b], COLOR_LIST[b + 1], b1 * 0.05F)));
        stringBuilder1.append(",");
        str = stringBuilder1.toString();
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str);
      stringBuilder.append("]");
      Log.i("xiaozhu------------", stringBuilder.toString());
    } 
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\android\soundrecorder\sound\ColorUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */