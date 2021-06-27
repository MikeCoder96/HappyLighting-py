package com.android.soundrecorder;

import android.os.Environment;
import android.os.StatFs;
import java.io.File;

class RemainingTimeCalculator {
  public static final int DISK_SPACE_LIMIT = 2;
  
  public static final int FILE_SIZE_LIMIT = 1;
  
  public static final int UNKNOWN_LIMIT = 0;
  
  private long mBlocksChangedTime;
  
  private int mBytesPerSecond;
  
  private int mCurrentLowerLimit = 0;
  
  private long mFileSizeChangedTime;
  
  private long mLastBlocks;
  
  private long mLastFileSize;
  
  private long mMaxBytes;
  
  private File mRecordingFile;
  
  private File mSDCardDirectory = Environment.getExternalStorageDirectory();
  
  final float maxAngle = 2.7488937F;
  
  final float minAngle = 0.3926991F;
  
  public int currentLowerLimit() {
    return this.mCurrentLowerLimit;
  }
  
  public boolean diskSpaceAvailable() {
    int i = (new StatFs(this.mSDCardDirectory.getAbsolutePath())).getAvailableBlocks();
    boolean bool = true;
    if (i <= 1)
      bool = false; 
    return bool;
  }
  
  public void reset() {
    this.mCurrentLowerLimit = 0;
    this.mBlocksChangedTime = -1L;
    this.mFileSizeChangedTime = -1L;
  }
  
  public void setBitRate(int paramInt) {
    this.mBytesPerSecond = paramInt / 8;
  }
  
  public void setFileSizeLimit(File paramFile, long paramLong) {
    this.mRecordingFile = paramFile;
    this.mMaxBytes = paramLong;
  }
  
  public long timeRemaining() {
    StatFs statFs = new StatFs(this.mSDCardDirectory.getAbsolutePath());
    long l1 = statFs.getAvailableBlocks();
    long l2 = statFs.getBlockSize();
    long l3 = System.currentTimeMillis();
    if (this.mBlocksChangedTime == -1L || l1 != this.mLastBlocks) {
      this.mBlocksChangedTime = l3;
      this.mLastBlocks = l1;
    } 
    l1 = this.mLastBlocks * l2 / this.mBytesPerSecond - (l3 - this.mBlocksChangedTime) / 1000L;
    File file = this.mRecordingFile;
    byte b = 2;
    if (file == null) {
      this.mCurrentLowerLimit = 2;
      return l1;
    } 
    this.mRecordingFile = new File(this.mRecordingFile.getAbsolutePath());
    l2 = this.mRecordingFile.length();
    if (this.mFileSizeChangedTime == -1L || l2 != this.mLastFileSize) {
      this.mFileSizeChangedTime = l3;
      this.mLastFileSize = l2;
    } 
    l3 = (this.mMaxBytes - l2) / this.mBytesPerSecond - (l3 - this.mFileSizeChangedTime) / 1000L - 1L;
    if (l1 >= l3)
      b = 1; 
    this.mCurrentLowerLimit = b;
    return Math.min(l1, l3);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\android\soundrecorder\RemainingTimeCalculator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */