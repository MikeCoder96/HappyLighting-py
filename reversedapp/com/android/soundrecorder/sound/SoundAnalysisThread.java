package com.android.soundrecorder.sound;

import android.media.AudioRecord;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SoundAnalysisThread extends Thread {
  private static final int[] SAMPLE_COUNT;
  
  private static final int[] SAMPLE_RATES_LIST = new int[] { 11025, 8000, 22050, 44100, 16000 };
  
  private AudioRecord audioRecord;
  
  private double currentFrequency;
  
  private double currentVolume;
  
  private FFT fft = new FFT();
  
  private Handler handler;
  
  private int sampleCount = 32768;
  
  private int sampleRate = 44100;
  
  private Sound sound = new Sound();
  
  static {
    SAMPLE_COUNT = new int[] { 8192, 4096, 16384, 32768, 8192 };
  }
  
  public SoundAnalysisThread(Handler paramHandler) {
    this.handler = paramHandler;
    initAudioRecord();
  }
  
  private void initAudioRecord() {
    Log.i("xiaozhu----------", "initAudioRecord");
    for (byte b = 0; b < SAMPLE_RATES_LIST.length; b++) {
      this.audioRecord = new AudioRecord(1, SAMPLE_RATES_LIST[b], 2, 2, AudioRecord.getMinBufferSize(SAMPLE_RATES_LIST[b], 2, 2));
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("STATE_INITIALIZED");
      stringBuilder.append(this.audioRecord.getState());
      Log.i("xiaozhu----------", stringBuilder.toString());
      if (this.audioRecord.getState() == 1) {
        Log.i("xiaozhu----------", "STATE_INITIALIZED");
        this.sampleRate = SAMPLE_RATES_LIST[b];
        this.sampleCount = 1024;
        break;
      } 
    } 
  }
  
  public void close() {
    if (this.audioRecord != null && this.audioRecord.getState() == 1) {
      this.audioRecord.stop();
      this.audioRecord.release();
    } 
  }
  
  public void run() {
    super.run();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("run");
    stringBuilder.append(this.sampleCount);
    Log.i("xiaozhu----------", stringBuilder.toString());
    this.audioRecord.startRecording();
    byte[] arrayOfByte = new byte[this.sampleCount];
    while (true) {
      int i = this.audioRecord.read(arrayOfByte, 0, this.sampleCount);
      if (i > 0) {
        this.currentFrequency = this.fft.getFrequency(arrayOfByte, this.sampleRate, this.sampleCount);
        this.currentVolume = VoiceUtil.getVolume(arrayOfByte, i);
        this.sound.mFrequency = this.currentFrequency;
        this.sound.mVolume = this.currentVolume;
        Message message = Message.obtain();
        message.obj = this.sound;
        message.what = 1;
        this.handler.sendMessage(message);
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("currentFrequency");
        stringBuilder1.append(this.currentFrequency);
        stringBuilder1.append("---");
        stringBuilder1.append(this.currentVolume);
        Log.i("xiaozhu----------", stringBuilder1.toString());
        if (this.currentFrequency > 0.0D)
          try {
            if (this.audioRecord.getState() == 1)
              this.audioRecord.stop(); 
            Thread.sleep(20L);
            if (this.audioRecord.getState() == 1)
              this.audioRecord.startRecording(); 
          } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
          }  
        continue;
      } 
      break;
    } 
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\android\soundrecorder\sound\SoundAnalysisThread.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */