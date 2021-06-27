package com.android.soundrecorder;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import java.io.File;
import java.io.IOException;

public class Recorder implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
  public static final int IDLE_STATE = 0;
  
  public static final int INTERNAL_ERROR = 2;
  
  public static final int IN_CALL_RECORD_ERROR = 3;
  
  public static final int NO_ERROR = 0;
  
  public static final int PLAYING_STATE = 2;
  
  public static final int RECORDING_STATE = 1;
  
  static final String SAMPLE_LENGTH_KEY = "sample_length";
  
  static final String SAMPLE_PATH_KEY = "sample_path";
  
  static final String SAMPLE_PREFIX = "recording";
  
  public static final int SDCARD_ACCESS_ERROR = 1;
  
  OnStateChangedListener mOnStateChangedListener = null;
  
  MediaPlayer mPlayer = null;
  
  MediaRecorder mRecorder = null;
  
  File mSampleFile = null;
  
  int mSampleLength = 0;
  
  long mSampleStart = 0L;
  
  int mState = 0;
  
  private void setError(int paramInt) {
    if (this.mOnStateChangedListener != null)
      this.mOnStateChangedListener.onError(paramInt); 
  }
  
  private void setState(int paramInt) {
    if (paramInt == this.mState)
      return; 
    this.mState = paramInt;
    signalStateChanged(this.mState);
  }
  
  private void signalStateChanged(int paramInt) {
    if (this.mOnStateChangedListener != null)
      this.mOnStateChangedListener.onStateChanged(paramInt); 
  }
  
  public void clear() {
    stop();
    this.mSampleLength = 0;
    signalStateChanged(0);
  }
  
  public void delete() {
    stop();
    if (this.mSampleFile != null)
      this.mSampleFile.delete(); 
    this.mSampleFile = null;
    this.mSampleLength = 0;
    signalStateChanged(0);
  }
  
  public int getMaxAmplitude() {
    return (this.mState != 1) ? 0 : this.mRecorder.getMaxAmplitude();
  }
  
  public void onCompletion(MediaPlayer paramMediaPlayer) {
    stop();
  }
  
  public boolean onError(MediaPlayer paramMediaPlayer, int paramInt1, int paramInt2) {
    stop();
    setError(1);
    return true;
  }
  
  public int progress() {
    return (this.mState == 1 || this.mState == 2) ? (int)((System.currentTimeMillis() - this.mSampleStart) / 1000L) : 0;
  }
  
  public void restoreState(Bundle paramBundle) {
    String str = paramBundle.getString("sample_path");
    if (str == null)
      return; 
    int i = paramBundle.getInt("sample_length", -1);
    if (i == -1)
      return; 
    File file = new File(str);
    if (!file.exists())
      return; 
    if (this.mSampleFile != null && this.mSampleFile.getAbsolutePath().compareTo(file.getAbsolutePath()) == 0)
      return; 
    delete();
    this.mSampleFile = file;
    this.mSampleLength = i;
    signalStateChanged(0);
  }
  
  public File sampleFile() {
    return this.mSampleFile;
  }
  
  public int sampleLength() {
    return this.mSampleLength;
  }
  
  public void saveState(Bundle paramBundle) {
    paramBundle.putString("sample_path", this.mSampleFile.getAbsolutePath());
    paramBundle.putInt("sample_length", this.mSampleLength);
  }
  
  public void setOnStateChangedListener(OnStateChangedListener paramOnStateChangedListener) {
    this.mOnStateChangedListener = paramOnStateChangedListener;
  }
  
  public void startPlayback() {
    stop();
    this.mPlayer = new MediaPlayer();
    try {
      this.mPlayer.setDataSource(this.mSampleFile.getAbsolutePath());
      this.mPlayer.setOnCompletionListener(this);
      this.mPlayer.setOnErrorListener(this);
      this.mPlayer.prepare();
      this.mPlayer.start();
      this.mSampleStart = System.currentTimeMillis();
      setState(2);
      return;
    } catch (IllegalArgumentException illegalArgumentException) {
      setError(2);
      this.mPlayer = null;
      return;
    } catch (IOException iOException) {
      setError(1);
      this.mPlayer = null;
      return;
    } 
  }
  
  public void startRecording(int paramInt, String paramString, Context paramContext) {
    stop();
    File file = this.mSampleFile;
    boolean bool = true;
    if (file == null) {
      File file1 = Environment.getExternalStorageDirectory();
      file = file1;
      if (!file1.canWrite())
        file = new File("/sdcard/sdcard"); 
      try {
        this.mSampleFile = File.createTempFile("recording", paramString, file);
      } catch (IOException iOException) {
        setError(1);
        return;
      } 
    } 
    this.mRecorder = new MediaRecorder();
    this.mRecorder.setAudioSource(1);
    this.mRecorder.setOutputFormat(paramInt);
    this.mRecorder.setAudioEncoder(1);
    this.mRecorder.setOutputFile(this.mSampleFile.getAbsolutePath());
    try {
      this.mRecorder.prepare();
      try {
        this.mRecorder.start();
        this.mSampleStart = System.currentTimeMillis();
        setState(1);
        return;
      } catch (RuntimeException runtimeException) {
        AudioManager audioManager = (AudioManager)paramContext.getSystemService("audio");
        paramInt = bool;
        if (audioManager.getMode() != 2)
          if (audioManager.getMode() == 3) {
            paramInt = bool;
          } else {
            paramInt = 0;
          }  
        if (paramInt != 0) {
          setError(3);
        } else {
          setError(2);
        } 
        this.mRecorder.reset();
        this.mRecorder.release();
        this.mRecorder = null;
        return;
      } 
    } catch (IOException iOException) {
      setError(2);
      this.mRecorder.reset();
      this.mRecorder.release();
      this.mRecorder = null;
      return;
    } 
  }
  
  public int state() {
    return this.mState;
  }
  
  public void stop() {
    stopRecording();
    stopPlayback();
  }
  
  public void stopPlayback() {
    if (this.mPlayer == null)
      return; 
    this.mPlayer.stop();
    this.mPlayer.release();
    this.mPlayer = null;
    setState(0);
  }
  
  public void stopRecording() {
    if (this.mRecorder == null)
      return; 
    this.mRecorder.stop();
    this.mRecorder.release();
    this.mRecorder = null;
    this.mSampleLength = (int)((System.currentTimeMillis() - this.mSampleStart) / 1000L);
    setState(0);
  }
  
  public static interface OnStateChangedListener {
    void onError(int param1Int);
    
    void onStateChanged(int param1Int);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\android\soundrecorder\Recorder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */