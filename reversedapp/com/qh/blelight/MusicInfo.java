package com.qh.blelight;

public class MusicInfo {
  private final String TAG = "TAG";
  
  private boolean Tag = false;
  
  private String album;
  
  private String artist;
  
  private String audioPath;
  
  private String comment;
  
  private String display_name;
  
  private boolean isAssets = false;
  
  private long playTime;
  
  private byte r1;
  
  private byte r2;
  
  private byte r3;
  
  private String songName;
  
  private boolean valid;
  
  private String year;
  
  public MusicInfo() {}
  
  public MusicInfo(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte.length == 128) {
      new String(paramArrayOfbyte, 0, 3);
      this.valid = true;
      this.songName = (new String(paramArrayOfbyte, 3, 30)).trim();
      this.artist = (new String(paramArrayOfbyte, 33, 30)).trim();
      this.album = (new String(paramArrayOfbyte, 63, 30)).trim();
      this.year = (new String(paramArrayOfbyte, 93, 4)).trim();
      this.comment = (new String(paramArrayOfbyte, 97, 28)).trim();
      this.r1 = (byte)paramArrayOfbyte[125];
      this.r2 = (byte)paramArrayOfbyte[126];
      this.r3 = (byte)paramArrayOfbyte[127];
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("length");
    stringBuilder.append(paramArrayOfbyte.length);
    throw new RuntimeException(stringBuilder.toString());
  }
  
  public String getAlbum() {
    return this.album;
  }
  
  public String getArtist() {
    return this.artist;
  }
  
  public String getAudioPath() {
    return this.audioPath;
  }
  
  public byte[] getBytes() {
    byte[] arrayOfByte1 = new byte[128];
    System.arraycopy("TAG".getBytes(), 0, arrayOfByte1, 0, 3);
    byte[] arrayOfByte2 = this.songName.getBytes();
    int i = arrayOfByte2.length;
    int j = 30;
    if (i > 30) {
      i = 30;
    } else {
      i = arrayOfByte2.length;
    } 
    System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 3, i);
    arrayOfByte2 = this.artist.getBytes();
    if (arrayOfByte2.length > 30) {
      i = 30;
    } else {
      i = arrayOfByte2.length;
    } 
    System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 33, i);
    arrayOfByte2 = this.album.getBytes();
    if (arrayOfByte2.length > 30) {
      i = j;
    } else {
      i = arrayOfByte2.length;
    } 
    System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 63, i);
    arrayOfByte2 = this.year.getBytes();
    j = arrayOfByte2.length;
    i = 4;
    if (j <= 4)
      i = arrayOfByte2.length; 
    System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 93, i);
    arrayOfByte2 = this.comment.getBytes();
    j = arrayOfByte2.length;
    i = 28;
    if (j <= 28)
      i = arrayOfByte2.length; 
    System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 97, i);
    arrayOfByte1[125] = (byte)this.r1;
    arrayOfByte1[126] = (byte)this.r2;
    arrayOfByte1[127] = (byte)this.r3;
    return arrayOfByte1;
  }
  
  public String getComment() {
    return this.comment;
  }
  
  public String getDisplay_name() {
    return this.display_name;
  }
  
  public long getPlayTime() {
    return this.playTime;
  }
  
  public byte getR1() {
    return this.r1;
  }
  
  public byte getR2() {
    return this.r2;
  }
  
  public byte getR3() {
    return this.r3;
  }
  
  public String getSongName() {
    return this.songName;
  }
  
  public boolean getTag() {
    return this.Tag;
  }
  
  public String getYear() {
    return this.year;
  }
  
  public boolean isValid() {
    return this.valid;
  }
  
  public void setAlbum(String paramString) {
    this.album = paramString;
  }
  
  public void setArtist(String paramString) {
    this.artist = paramString;
  }
  
  public void setAudioPath(String paramString) {
    this.audioPath = paramString;
  }
  
  public void setComment(String paramString) {
    this.comment = paramString;
  }
  
  public void setDisplay_name(String paramString) {
    this.display_name = paramString;
  }
  
  public void setPlayTime(long paramLong) {
    this.playTime = paramLong;
  }
  
  public void setR1(byte paramByte) {
    this.r1 = (byte)paramByte;
  }
  
  public void setR2(byte paramByte) {
    this.r2 = (byte)paramByte;
  }
  
  public void setR3(byte paramByte) {
    this.r3 = (byte)paramByte;
  }
  
  public void setSongName(String paramString) {
    this.songName = paramString;
  }
  
  public void setTag(boolean paramBoolean) {
    this.Tag = paramBoolean;
  }
  
  public void setValid(boolean paramBoolean) {
    this.valid = paramBoolean;
  }
  
  public void setYear(String paramString) {
    this.year = paramString;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\blelight\MusicInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */