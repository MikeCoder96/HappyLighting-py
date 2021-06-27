package com.android.soundrecorder.sound;

import android.util.Log;

public class FFT {
  public Complex[] dft(Complex[] paramArrayOfComplex) {
    int i = paramArrayOfComplex.length;
    if (i == 1)
      return paramArrayOfComplex; 
    Complex[] arrayOfComplex = new Complex[i];
    for (byte b = 0; b < i; b++) {
      arrayOfComplex[b] = new Complex(0.0D, 0.0D);
      for (byte b1 = 0; b1 < i; b1++) {
        double d1 = (b1 * -2);
        Double.isNaN(d1);
        double d2 = i;
        Double.isNaN(d2);
        d2 = d1 * Math.PI / d2;
        Complex complex = new Complex(Math.cos(d2), Math.sin(d2));
        arrayOfComplex[b].plus(paramArrayOfComplex[b1].multiple(complex));
      } 
    } 
    return arrayOfComplex;
  }
  
  public Complex[] fft(Complex[] paramArrayOfComplex) {
    int i = paramArrayOfComplex.length;
    if (i == 1)
      return paramArrayOfComplex; 
    if (i % 2 != 0)
      return dft(paramArrayOfComplex); 
    int j = i / 2;
    Complex[] arrayOfComplex1 = new Complex[j];
    boolean bool = false;
    byte b;
    for (b = 0; b < j; b++)
      arrayOfComplex1[b] = paramArrayOfComplex[b * 2]; 
    Complex[] arrayOfComplex2 = fft(arrayOfComplex1);
    for (b = 0; b < j; b++)
      arrayOfComplex1[b] = paramArrayOfComplex[b * 2 + 1]; 
    Complex[] arrayOfComplex3 = fft(arrayOfComplex1);
    paramArrayOfComplex = new Complex[i];
    for (b = bool; b < j; b++) {
      double d1 = (b * -2);
      Double.isNaN(d1);
      double d2 = i;
      Double.isNaN(d2);
      d2 = d1 * Math.PI / d2;
      Complex complex = new Complex(Math.cos(d2), Math.sin(d2));
      paramArrayOfComplex[b] = arrayOfComplex2[b].plus(complex.multiple(arrayOfComplex3[b]));
      paramArrayOfComplex[b + j] = arrayOfComplex2[b].minus(complex.multiple(arrayOfComplex3[b]));
    } 
    return paramArrayOfComplex;
  }
  
  public double getFrequency(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    if (paramArrayOfbyte.length >= paramInt2) {
      Complex[] arrayOfComplex = new Complex[paramInt2];
      byte b1 = 0;
      byte b2;
      for (b2 = 0; b2 < paramInt2; b2++)
        arrayOfComplex[b2] = new Complex(paramArrayOfbyte[b2], 0.0D); 
      String str = "[";
      for (b2 = 0; b2 < arrayOfComplex.length; b2++) {
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append(str);
        stringBuilder3.append(arrayOfComplex[b2].getReal());
        stringBuilder3.append("-");
        stringBuilder3.append(arrayOfComplex[b2].getImage());
        stringBuilder3.append(",   ");
        str = stringBuilder3.toString();
      } 
      StringBuilder stringBuilder2 = new StringBuilder();
      stringBuilder2.append(str);
      stringBuilder2.append("]");
      stringBuilder2.toString();
      arrayOfComplex = fft(arrayOfComplex);
      str = "[";
      for (b2 = 0; b2 < arrayOfComplex.length; b2++) {
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append(str);
        stringBuilder2.append(arrayOfComplex[b2].getReal());
        stringBuilder2.append("-");
        stringBuilder2.append(arrayOfComplex[b2].getImage());
        stringBuilder2.append(",   ");
        str = stringBuilder2.toString();
      } 
      stringBuilder2 = new StringBuilder();
      stringBuilder2.append(str);
      stringBuilder2.append("]");
      stringBuilder2.toString();
      int i = paramInt2 / 2;
      double[] arrayOfDouble = new double[i];
      for (b2 = 0; b2 < i; b2++)
        arrayOfDouble[b2] = arrayOfComplex[b2].getMod(); 
      b2 = 1;
      while (b2 < i) {
        byte b = b1;
        if (arrayOfDouble[b2] > arrayOfDouble[b1])
          b = b2; 
        b2++;
        b1 = b;
      } 
      double d1 = b1;
      double d2 = paramInt1;
      Double.isNaN(d1);
      Double.isNaN(d2);
      double d3 = paramInt2;
      Double.isNaN(d3);
      d2 = d1 * d2 / d3;
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("fre:");
      stringBuilder1.append(d2);
      Log.i("FFT", stringBuilder1.toString());
      return d2;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Data length lower than ");
    stringBuilder.append(paramInt2);
    throw new RuntimeException(stringBuilder.toString());
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\android\soundrecorder\sound\FFT.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */