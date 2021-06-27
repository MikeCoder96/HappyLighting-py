package com.android.soundrecorder;

import android.media.AudioRecord;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class AudioRecordDemo {
  static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(8000, 1, 2);
  
  static final float DROPOFF_STEP = 0.18F;
  
  static final int SAMPLE_RATE_IN_HZ = 8000;
  
  private static final String TAG = "AudioRecord";
  
  private int BASE = 1;
  
  private int SPACE = 100;
  
  public boolean isPlay = false;
  
  AudioRecord mAudioRecord;
  
  float mCurrentAngle;
  
  private final Handler mHandler = new Handler();
  
  Object mLock = new Object();
  
  public Handler mShowHandler;
  
  private Runnable mUpdateMicStatusTimer = new Runnable() {
      public void run() {
        AudioRecordDemo.this.updateMicStatus();
      }
    };
  
  private double old = 0.0D;
  
  private void updateMicStatus() {
    if (this.mAudioRecord != null) {
      short[] arrayOfShort = new short[BUFFER_SIZE];
      AudioRecord audioRecord = this.mAudioRecord;
      int i = BUFFER_SIZE;
      byte b = 0;
      i = audioRecord.read(arrayOfShort, 0, i);
      long l = 0L;
      while (b < arrayOfShort.length) {
        l += (arrayOfShort[b] * arrayOfShort[b]);
        b++;
      } 
      double d1 = l;
      double d2 = i;
      Double.isNaN(d1);
      Double.isNaN(d2);
      d2 = Math.log10(d1 / d2) * 10.0D;
      if (this.mShowHandler != null) {
        Bundle bundle = new Bundle();
        bundle.putDouble("-db", d2);
        bundle.putDouble("db", d2);
        Message message = new Message();
        message.setData(bundle);
        this.mShowHandler.sendMessage(message);
      } 
      this.old = d2;
      this.mHandler.postDelayed(this.mUpdateMicStatusTimer, this.SPACE);
    } 
  }
  
  public void startRecord() {
    if (this.isPlay)
      return; 
    this.mAudioRecord = new AudioRecord(1, 8000, 1, 2, BUFFER_SIZE);
    if (this.mAudioRecord == null)
      Log.e("sound", "mAudioRecord"); 
    this.isPlay = true;
    this.mAudioRecord.startRecording();
    this.mHandler.post(this.mUpdateMicStatusTimer);
  }
  
  public void stopRecord() {
    if (!this.isPlay)
      return; 
    if (this.mAudioRecord == null)
      return; 
    this.mAudioRecord.stop();
    this.mAudioRecord.release();
    this.mAudioRecord = null;
    this.isPlay = false;
    Log.e("", "stopRecord");
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\android\soundrecorder\AudioRecordDemo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */