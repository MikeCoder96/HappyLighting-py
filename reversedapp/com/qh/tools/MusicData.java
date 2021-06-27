package com.qh.tools;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

public class MusicData {
  public static final int PLAY_MOD_ORDER = 2;
  
  public static final int PLAY_MOD_RANDOM = 1;
  
  public static final int PLAY_MOD_SIGLE = 0;
  
  public static Cursor getMP3MusicInfo(Context paramContext, String paramString) {
    return paramContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, "_data like '%.mp3'", null, "title_key");
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\tools\MusicData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */