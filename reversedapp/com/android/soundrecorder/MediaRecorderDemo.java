package com.android.soundrecorder;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.io.File;
import java.io.IOException;

public class MediaRecorderDemo {
  static final float DROPOFF_STEP = 0.18F;
  
  public static final int MAX_LENGTH = 3600000;
  
  private int BASE = 1;
  
  private int SPACE = 150;
  
  private final String TAG = "MediaRecord";
  
  private long endTime;
  
  private String filePath = "/dev/null";
  
  private boolean isPlay = false;
  
  float mCurrentAngle;
  
  private final Handler mHandler = new Handler();
  
  private MediaRecorder mMediaRecorder;
  
  public Handler mShowHandler;
  
  private Runnable mUpdateMicStatusTimer = new Runnable() {
      public void run() {
        MediaRecorderDemo.this.updateMicStatus();
      }
    };
  
  private double old = 0.0D;
  
  private long startTime;
  
  public MediaRecorderDemo() {}
  
  public MediaRecorderDemo(File paramFile) {}
  
  private void updateMicStatus() {
    if (this.mMediaRecorder != null) {
      double d1 = this.mMediaRecorder.getMaxAmplitude();
      double d2 = this.BASE;
      Double.isNaN(d1);
      Double.isNaN(d2);
      d2 = d1 / d2;
      d1 = 0.0D;
      if (d2 > 1.0D)
        d1 = 20.0D * Math.log10(d2); 
      if (this.mShowHandler != null) {
        Bundle bundle = new Bundle();
        bundle.putDouble("-db", d1 - this.old);
        bundle.putDouble("db", d1);
        Message message = new Message();
        message.setData(bundle);
        this.mShowHandler.sendMessage(message);
      } 
      this.old = d1;
      this.mHandler.postDelayed(this.mUpdateMicStatusTimer, this.SPACE);
    } 
  }
  
  public boolean isPlay() {
    return this.isPlay;
  }
  
  public void setPlay(boolean paramBoolean) {
    this.isPlay = paramBoolean;
  }
  
  public void startRecord() {
    if (this.isPlay)
      return; 
    if (this.mMediaRecorder == null)
      this.mMediaRecorder = new MediaRecorder(); 
    try {
      this.mMediaRecorder.setAudioSource(1);
      this.mMediaRecorder.setOutputFormat(0);
      this.mMediaRecorder.setAudioEncoder(1);
      this.mMediaRecorder.setOutputFile(this.filePath);
      this.mMediaRecorder.setMaxDuration(3600000);
      this.mMediaRecorder.prepare();
      this.mMediaRecorder.start();
      this.startTime = System.currentTimeMillis();
      updateMicStatus();
      this.isPlay = true;
    } catch (IllegalStateException illegalStateException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("call startAmr(File mRecAudioFile) failed!");
      stringBuilder.append(illegalStateException.getMessage());
      Log.i("MediaRecord", stringBuilder.toString());
    } catch (IOException iOException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("call startAmr(File mRecAudioFile) failed!");
      stringBuilder.append(iOException.getMessage());
      Log.i("MediaRecord", stringBuilder.toString());
    } 
  }
  
  public long stopRecord() {
    if (!this.isPlay)
      return 0L; 
    if (this.mMediaRecorder == null)
      return 0L; 
    this.endTime = System.currentTimeMillis();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("endTime");
    stringBuilder.append(this.endTime);
    Log.i("ACTION_END", stringBuilder.toString());
    this.mMediaRecorder.stop();
    this.mMediaRecorder.reset();
    this.mMediaRecorder.release();
    this.mMediaRecorder = null;
    stringBuilder = new StringBuilder();
    stringBuilder.append("Time");
    stringBuilder.append(this.endTime - this.startTime);
    Log.i("ACTION_LENGTH", stringBuilder.toString());
    this.isPlay = false;
    return this.endTime - this.startTime;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\android\soundrecorder\MediaRecorderDemo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */