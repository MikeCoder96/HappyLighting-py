package com.qh.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.media.AudioManager;
import android.os.Vibrator;
import android.view.animation.AlphaAnimation;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class tools {
  private static String strRingtoneFolder = "/system/media/audio/ringtones";
  
  public static byte[] BitmapToByte(Bitmap paramBitmap) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    paramBitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
    return byteArrayOutputStream.toByteArray();
  }
  
  public static byte[] BitmapToByte(Bitmap paramBitmap, int paramInt) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    paramBitmap.compress(Bitmap.CompressFormat.PNG, paramInt, byteArrayOutputStream);
    byte[] arrayOfByte = byteArrayOutputStream.toByteArray();
    try {
      byteArrayOutputStream.close();
    } catch (IOException iOException) {
      iOException.printStackTrace();
    } 
    return arrayOfByte;
  }
  
  public static Bitmap ByteToBitmap(byte[] paramArrayOfbyte) {
    return BitmapFactory.decodeByteArray(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public static Bitmap comp(Bitmap paramBitmap) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    paramBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
    if ((byteArrayOutputStream.toByteArray()).length / 1024 > 400) {
      byteArrayOutputStream.reset();
      paramBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
    } 
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeStream(byteArrayInputStream, null, options);
    options.inJustDecodeBounds = false;
    int i = options.outWidth;
    int j = options.outHeight;
    if (i > j && i > 480.0F) {
      i = (int)(options.outWidth / 480.0F);
    } else if (i < j && j > 800.0F) {
      i = (int)(options.outHeight / 800.0F);
    } else {
      i = 1;
    } 
    j = i;
    if (i <= 0)
      j = 1; 
    options.inSampleSize = j;
    return compressImage(BitmapFactory.decodeStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), null, options));
  }
  
  public static Bitmap comp(Bitmap paramBitmap, int paramInt) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    paramBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
    if ((byteArrayOutputStream.toByteArray()).length / 1024 > 400) {
      byteArrayOutputStream.reset();
      paramBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
    } 
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeStream(byteArrayInputStream, null, options);
    options.inJustDecodeBounds = false;
    int i = options.outWidth;
    i = options.outHeight;
    options.inSampleSize = paramInt;
    return compressImage(BitmapFactory.decodeStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), null, options));
  }
  
  public static Bitmap comp(Bitmap paramBitmap, int paramInt1, int paramInt2) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    paramBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
    if ((byteArrayOutputStream.toByteArray()).length / 1024 > 400) {
      byteArrayOutputStream.reset();
      paramBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
    } 
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeStream(byteArrayInputStream, null, options);
    options.inJustDecodeBounds = false;
    int i = options.outWidth;
    int j = options.outHeight;
    float f1 = paramInt1;
    float f2 = paramInt2;
    if (i > j && i > f2) {
      paramInt1 = (int)(options.outWidth / f2);
    } else if (i < j && j > f1) {
      paramInt1 = (int)(options.outHeight / f1);
    } else {
      paramInt1 = 1;
    } 
    paramInt2 = paramInt1;
    if (paramInt1 <= 0)
      paramInt2 = 1; 
    options.inSampleSize = paramInt2;
    return compressImage(BitmapFactory.decodeStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), null, options));
  }
  
  public static Bitmap compressImage(Bitmap paramBitmap) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    paramBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
    int i = 100;
    while ((byteArrayOutputStream.toByteArray()).length / 1024 > 100) {
      byteArrayOutputStream.reset();
      paramBitmap.compress(Bitmap.CompressFormat.JPEG, i, byteArrayOutputStream);
      int j = i - 10;
      i = j;
      if (j <= 0)
        break; 
    } 
    return BitmapFactory.decodeStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), null, null);
  }
  
  public static Bitmap createCircleImage(Bitmap paramBitmap, int paramInt) {
    Paint paint = new Paint();
    paint.setAntiAlias(true);
    Bitmap bitmap = Bitmap.createBitmap(paramInt, paramInt, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    float f = (paramInt / 2);
    canvas.drawCircle(f, f, f, paint);
    paint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    canvas.drawBitmap(paramBitmap, 0.0F, 0.0F, paint);
    return bitmap;
  }
  
  public static void doVibrator(boolean paramBoolean, Context paramContext) {
    Vibrator vibrator = (Vibrator)paramContext.getSystemService("vibrator");
    if (!vibrator.hasVibrator())
      return; 
    if (!paramBoolean) {
      vibrator.cancel();
    } else {
      vibrator.vibrate(new long[] { 10L, 200L }, -1);
    } 
  }
  
  public static File[] getAllRings(File paramFile) {
    return (paramFile == null) ? null : paramFile.listFiles();
  }
  
  public static AlphaAnimation getAnimation(float paramFloat1, float paramFloat2, long paramLong, int paramInt) {
    AlphaAnimation alphaAnimation = new AlphaAnimation(paramFloat1, paramFloat2);
    alphaAnimation.setDuration(paramLong);
    alphaAnimation.setRepeatCount(paramInt);
    alphaAnimation.setRepeatMode(2);
    alphaAnimation.setFillAfter(true);
    return alphaAnimation;
  }
  
  public static InputStream getfilesFromAssets(Context paramContext, String paramString) {
    ClassLoader classLoader = paramContext.getClass().getClassLoader();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("assets/");
    stringBuilder.append(paramString);
    return classLoader.getResourceAsStream(stringBuilder.toString());
  }
  
  public static Bitmap postScale(Bitmap paramBitmap, float paramFloat) {
    if (paramBitmap == null)
      return null; 
    if (paramBitmap.getWidth() == 0)
      return paramBitmap; 
    if (paramBitmap.getHeight() == 0)
      return paramBitmap; 
    Matrix matrix = new Matrix();
    matrix.postScale(paramFloat, paramFloat);
    return Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight(), matrix, true);
  }
  
  public static void recoverRound(Context paramContext) {
    AudioManager audioManager = (AudioManager)paramContext.getSystemService("audio");
    audioManager.setSpeakerphoneOn(false);
    audioManager.setMode(0);
  }
  
  public static Bitmap scaleBitmap(Bitmap paramBitmap, int paramInt1, int paramInt2) {
    if (paramBitmap == null)
      return null; 
    int i = paramBitmap.getHeight();
    int j = paramBitmap.getWidth();
    float f1 = paramInt1 / j;
    float f2 = paramInt2 / i;
    Matrix matrix = new Matrix();
    matrix.postScale(f1, f2);
    Bitmap bitmap = Bitmap.createBitmap(paramBitmap, 0, 0, j, i, matrix, false);
    if (!paramBitmap.isRecycled())
      paramBitmap.recycle(); 
    return bitmap;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\tools\tools.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */