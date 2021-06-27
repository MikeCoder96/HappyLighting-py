package android.support.v4.graphics;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.VisibleForTesting;
import java.util.Objects;

public final class ColorUtils {
  private static final int MIN_ALPHA_SEARCH_MAX_ITERATIONS = 10;
  
  private static final int MIN_ALPHA_SEARCH_PRECISION = 1;
  
  private static final ThreadLocal<double[]> TEMP_ARRAY = (ThreadLocal)new ThreadLocal<double>();
  
  private static final double XYZ_EPSILON = 0.008856D;
  
  private static final double XYZ_KAPPA = 903.3D;
  
  private static final double XYZ_WHITE_REFERENCE_X = 95.047D;
  
  private static final double XYZ_WHITE_REFERENCE_Y = 100.0D;
  
  private static final double XYZ_WHITE_REFERENCE_Z = 108.883D;
  
  @ColorInt
  public static int HSLToColor(@NonNull float[] paramArrayOffloat) {
    float f1 = paramArrayOffloat[0];
    float f2 = paramArrayOffloat[1];
    float f3 = paramArrayOffloat[2];
    f2 = (1.0F - Math.abs(f3 * 2.0F - 1.0F)) * f2;
    float f4 = f3 - 0.5F * f2;
    f3 = (1.0F - Math.abs(f1 / 60.0F % 2.0F - 1.0F)) * f2;
    switch ((int)f1 / 60) {
      default:
        i = 0;
        j = 0;
        k = 0;
        return Color.rgb(constrain(j, 0, 255), constrain(k, 0, 255), constrain(i, 0, 255));
      case 5:
      case 6:
        j = Math.round((f2 + f4) * 255.0F);
        k = Math.round(f4 * 255.0F);
        i = Math.round((f3 + f4) * 255.0F);
        return Color.rgb(constrain(j, 0, 255), constrain(k, 0, 255), constrain(i, 0, 255));
      case 4:
        j = Math.round((f3 + f4) * 255.0F);
        k = Math.round(f4 * 255.0F);
        i = Math.round((f2 + f4) * 255.0F);
        return Color.rgb(constrain(j, 0, 255), constrain(k, 0, 255), constrain(i, 0, 255));
      case 3:
        j = Math.round(f4 * 255.0F);
        k = Math.round((f3 + f4) * 255.0F);
        i = Math.round((f2 + f4) * 255.0F);
        return Color.rgb(constrain(j, 0, 255), constrain(k, 0, 255), constrain(i, 0, 255));
      case 2:
        j = Math.round(f4 * 255.0F);
        k = Math.round((f2 + f4) * 255.0F);
        i = Math.round((f3 + f4) * 255.0F);
        return Color.rgb(constrain(j, 0, 255), constrain(k, 0, 255), constrain(i, 0, 255));
      case 1:
        j = Math.round((f3 + f4) * 255.0F);
        k = Math.round((f2 + f4) * 255.0F);
        i = Math.round(f4 * 255.0F);
        return Color.rgb(constrain(j, 0, 255), constrain(k, 0, 255), constrain(i, 0, 255));
      case 0:
        break;
    } 
    int j = Math.round((f2 + f4) * 255.0F);
    int k = Math.round((f3 + f4) * 255.0F);
    int i = Math.round(f4 * 255.0F);
    return Color.rgb(constrain(j, 0, 255), constrain(k, 0, 255), constrain(i, 0, 255));
  }
  
  @ColorInt
  public static int LABToColor(@FloatRange(from = 0.0D, to = 100.0D) double paramDouble1, @FloatRange(from = -128.0D, to = 127.0D) double paramDouble2, @FloatRange(from = -128.0D, to = 127.0D) double paramDouble3) {
    double[] arrayOfDouble = getTempDouble3Array();
    LABToXYZ(paramDouble1, paramDouble2, paramDouble3, arrayOfDouble);
    return XYZToColor(arrayOfDouble[0], arrayOfDouble[1], arrayOfDouble[2]);
  }
  
  public static void LABToXYZ(@FloatRange(from = 0.0D, to = 100.0D) double paramDouble1, @FloatRange(from = -128.0D, to = 127.0D) double paramDouble2, @FloatRange(from = -128.0D, to = 127.0D) double paramDouble3, @NonNull double[] paramArrayOfdouble) {
    double d1 = (paramDouble1 + 16.0D) / 116.0D;
    double d2 = paramDouble2 / 500.0D + d1;
    double d3 = d1 - paramDouble3 / 200.0D;
    paramDouble2 = Math.pow(d2, 3.0D);
    if (paramDouble2 <= 0.008856D)
      paramDouble2 = (d2 * 116.0D - 16.0D) / 903.3D; 
    if (paramDouble1 > 7.9996247999999985D) {
      paramDouble1 = Math.pow(d1, 3.0D);
    } else {
      paramDouble1 /= 903.3D;
    } 
    paramDouble3 = Math.pow(d3, 3.0D);
    if (paramDouble3 <= 0.008856D)
      paramDouble3 = (d3 * 116.0D - 16.0D) / 903.3D; 
    paramArrayOfdouble[0] = paramDouble2 * 95.047D;
    paramArrayOfdouble[1] = paramDouble1 * 100.0D;
    paramArrayOfdouble[2] = paramDouble3 * 108.883D;
  }
  
  public static void RGBToHSL(@IntRange(from = 0L, to = 255L) int paramInt1, @IntRange(from = 0L, to = 255L) int paramInt2, @IntRange(from = 0L, to = 255L) int paramInt3, @NonNull float[] paramArrayOffloat) {
    float f1 = paramInt1 / 255.0F;
    float f2 = paramInt2 / 255.0F;
    float f3 = paramInt3 / 255.0F;
    float f4 = Math.max(f1, Math.max(f2, f3));
    float f5 = Math.min(f1, Math.min(f2, f3));
    float f6 = f4 - f5;
    float f7 = (f4 + f5) / 2.0F;
    if (f4 == f5) {
      f4 = 0.0F;
      f6 = 0.0F;
    } else {
      if (f4 == f1) {
        f4 = (f2 - f3) / f6 % 6.0F;
      } else if (f4 == f2) {
        f4 = (f3 - f1) / f6 + 2.0F;
      } else {
        f4 = (f1 - f2) / f6 + 4.0F;
      } 
      f6 /= 1.0F - Math.abs(2.0F * f7 - 1.0F);
    } 
    f3 = f4 * 60.0F % 360.0F;
    f4 = f3;
    if (f3 < 0.0F)
      f4 = f3 + 360.0F; 
    paramArrayOffloat[0] = constrain(f4, 0.0F, 360.0F);
    paramArrayOffloat[1] = constrain(f6, 0.0F, 1.0F);
    paramArrayOffloat[2] = constrain(f7, 0.0F, 1.0F);
  }
  
  public static void RGBToLAB(@IntRange(from = 0L, to = 255L) int paramInt1, @IntRange(from = 0L, to = 255L) int paramInt2, @IntRange(from = 0L, to = 255L) int paramInt3, @NonNull double[] paramArrayOfdouble) {
    RGBToXYZ(paramInt1, paramInt2, paramInt3, paramArrayOfdouble);
    XYZToLAB(paramArrayOfdouble[0], paramArrayOfdouble[1], paramArrayOfdouble[2], paramArrayOfdouble);
  }
  
  public static void RGBToXYZ(@IntRange(from = 0L, to = 255L) int paramInt1, @IntRange(from = 0L, to = 255L) int paramInt2, @IntRange(from = 0L, to = 255L) int paramInt3, @NonNull double[] paramArrayOfdouble) {
    if (paramArrayOfdouble.length == 3) {
      double d1 = paramInt1;
      Double.isNaN(d1);
      d1 /= 255.0D;
      if (d1 < 0.04045D) {
        d1 /= 12.92D;
      } else {
        d1 = Math.pow((d1 + 0.055D) / 1.055D, 2.4D);
      } 
      double d2 = paramInt2;
      Double.isNaN(d2);
      d2 /= 255.0D;
      if (d2 < 0.04045D) {
        d2 /= 12.92D;
      } else {
        d2 = Math.pow((d2 + 0.055D) / 1.055D, 2.4D);
      } 
      double d3 = paramInt3;
      Double.isNaN(d3);
      d3 /= 255.0D;
      if (d3 < 0.04045D) {
        d3 /= 12.92D;
      } else {
        d3 = Math.pow((d3 + 0.055D) / 1.055D, 2.4D);
      } 
      paramArrayOfdouble[0] = (0.4124D * d1 + 0.3576D * d2 + 0.1805D * d3) * 100.0D;
      paramArrayOfdouble[1] = (0.2126D * d1 + 0.7152D * d2 + 0.0722D * d3) * 100.0D;
      paramArrayOfdouble[2] = (d1 * 0.0193D + d2 * 0.1192D + d3 * 0.9505D) * 100.0D;
      return;
    } 
    throw new IllegalArgumentException("outXyz must have a length of 3.");
  }
  
  @ColorInt
  public static int XYZToColor(@FloatRange(from = 0.0D, to = 95.047D) double paramDouble1, @FloatRange(from = 0.0D, to = 100.0D) double paramDouble2, @FloatRange(from = 0.0D, to = 108.883D) double paramDouble3) {
    double d1 = (3.2406D * paramDouble1 + -1.5372D * paramDouble2 + -0.4986D * paramDouble3) / 100.0D;
    double d2 = (-0.9689D * paramDouble1 + 1.8758D * paramDouble2 + 0.0415D * paramDouble3) / 100.0D;
    paramDouble3 = (paramDouble1 * 0.0557D + paramDouble2 * -0.204D + 1.057D * paramDouble3) / 100.0D;
    if (d1 > 0.0031308D) {
      paramDouble1 = Math.pow(d1, 0.4166666666666667D) * 1.055D - 0.055D;
    } else {
      paramDouble1 = d1 * 12.92D;
    } 
    if (d2 > 0.0031308D) {
      paramDouble2 = Math.pow(d2, 0.4166666666666667D) * 1.055D - 0.055D;
    } else {
      paramDouble2 = d2 * 12.92D;
    } 
    if (paramDouble3 > 0.0031308D) {
      paramDouble3 = Math.pow(paramDouble3, 0.4166666666666667D) * 1.055D - 0.055D;
    } else {
      paramDouble3 *= 12.92D;
    } 
    return Color.rgb(constrain((int)Math.round(paramDouble1 * 255.0D), 0, 255), constrain((int)Math.round(paramDouble2 * 255.0D), 0, 255), constrain((int)Math.round(paramDouble3 * 255.0D), 0, 255));
  }
  
  public static void XYZToLAB(@FloatRange(from = 0.0D, to = 95.047D) double paramDouble1, @FloatRange(from = 0.0D, to = 100.0D) double paramDouble2, @FloatRange(from = 0.0D, to = 108.883D) double paramDouble3, @NonNull double[] paramArrayOfdouble) {
    if (paramArrayOfdouble.length == 3) {
      paramDouble1 = pivotXyzComponent(paramDouble1 / 95.047D);
      paramDouble2 = pivotXyzComponent(paramDouble2 / 100.0D);
      paramDouble3 = pivotXyzComponent(paramDouble3 / 108.883D);
      paramArrayOfdouble[0] = Math.max(0.0D, 116.0D * paramDouble2 - 16.0D);
      paramArrayOfdouble[1] = (paramDouble1 - paramDouble2) * 500.0D;
      paramArrayOfdouble[2] = (paramDouble2 - paramDouble3) * 200.0D;
      return;
    } 
    throw new IllegalArgumentException("outLab must have a length of 3.");
  }
  
  @ColorInt
  public static int blendARGB(@ColorInt int paramInt1, @ColorInt int paramInt2, @FloatRange(from = 0.0D, to = 1.0D) float paramFloat) {
    float f1 = 1.0F - paramFloat;
    float f2 = Color.alpha(paramInt1);
    float f3 = Color.alpha(paramInt2);
    float f4 = Color.red(paramInt1);
    float f5 = Color.red(paramInt2);
    float f6 = Color.green(paramInt1);
    float f7 = Color.green(paramInt2);
    float f8 = Color.blue(paramInt1);
    float f9 = Color.blue(paramInt2);
    return Color.argb((int)(f2 * f1 + f3 * paramFloat), (int)(f4 * f1 + f5 * paramFloat), (int)(f6 * f1 + f7 * paramFloat), (int)(f8 * f1 + f9 * paramFloat));
  }
  
  public static void blendHSL(@NonNull float[] paramArrayOffloat1, @NonNull float[] paramArrayOffloat2, @FloatRange(from = 0.0D, to = 1.0D) float paramFloat, @NonNull float[] paramArrayOffloat3) {
    if (paramArrayOffloat3.length == 3) {
      float f = 1.0F - paramFloat;
      paramArrayOffloat3[0] = circularInterpolate(paramArrayOffloat1[0], paramArrayOffloat2[0], paramFloat);
      paramArrayOffloat3[1] = paramArrayOffloat1[1] * f + paramArrayOffloat2[1] * paramFloat;
      paramArrayOffloat3[2] = paramArrayOffloat1[2] * f + paramArrayOffloat2[2] * paramFloat;
      return;
    } 
    throw new IllegalArgumentException("result must have a length of 3.");
  }
  
  public static void blendLAB(@NonNull double[] paramArrayOfdouble1, @NonNull double[] paramArrayOfdouble2, @FloatRange(from = 0.0D, to = 1.0D) double paramDouble, @NonNull double[] paramArrayOfdouble3) {
    if (paramArrayOfdouble3.length == 3) {
      double d = 1.0D - paramDouble;
      paramArrayOfdouble3[0] = paramArrayOfdouble1[0] * d + paramArrayOfdouble2[0] * paramDouble;
      paramArrayOfdouble3[1] = paramArrayOfdouble1[1] * d + paramArrayOfdouble2[1] * paramDouble;
      paramArrayOfdouble3[2] = paramArrayOfdouble1[2] * d + paramArrayOfdouble2[2] * paramDouble;
      return;
    } 
    throw new IllegalArgumentException("outResult must have a length of 3.");
  }
  
  public static double calculateContrast(@ColorInt int paramInt1, @ColorInt int paramInt2) {
    if (Color.alpha(paramInt2) == 255) {
      int i = paramInt1;
      if (Color.alpha(paramInt1) < 255)
        i = compositeColors(paramInt1, paramInt2); 
      double d1 = calculateLuminance(i) + 0.05D;
      double d2 = calculateLuminance(paramInt2) + 0.05D;
      return Math.max(d1, d2) / Math.min(d1, d2);
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("background can not be translucent: #");
    stringBuilder.append(Integer.toHexString(paramInt2));
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  @FloatRange(from = 0.0D, to = 1.0D)
  public static double calculateLuminance(@ColorInt int paramInt) {
    double[] arrayOfDouble = getTempDouble3Array();
    colorToXYZ(paramInt, arrayOfDouble);
    return arrayOfDouble[1] / 100.0D;
  }
  
  public static int calculateMinimumAlpha(@ColorInt int paramInt1, @ColorInt int paramInt2, float paramFloat) {
    int i = Color.alpha(paramInt2);
    int j = 255;
    if (i == 255) {
      double d1 = calculateContrast(setAlphaComponent(paramInt1, 255), paramInt2);
      double d2 = paramFloat;
      if (d1 < d2)
        return -1; 
      i = 0;
      int k = 0;
      while (i <= 10 && j - k > 1) {
        int m = (k + j) / 2;
        if (calculateContrast(setAlphaComponent(paramInt1, m), paramInt2) < d2) {
          k = m;
        } else {
          j = m;
        } 
        i++;
      } 
      return j;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("background can not be translucent: #");
    stringBuilder.append(Integer.toHexString(paramInt2));
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  @VisibleForTesting
  static float circularInterpolate(float paramFloat1, float paramFloat2, float paramFloat3) {
    float f1 = paramFloat1;
    float f2 = paramFloat2;
    if (Math.abs(paramFloat2 - paramFloat1) > 180.0F)
      if (paramFloat2 > paramFloat1) {
        f1 = paramFloat1 + 360.0F;
        f2 = paramFloat2;
      } else {
        f2 = paramFloat2 + 360.0F;
        f1 = paramFloat1;
      }  
    return (f1 + (f2 - f1) * paramFloat3) % 360.0F;
  }
  
  public static void colorToHSL(@ColorInt int paramInt, @NonNull float[] paramArrayOffloat) {
    RGBToHSL(Color.red(paramInt), Color.green(paramInt), Color.blue(paramInt), paramArrayOffloat);
  }
  
  public static void colorToLAB(@ColorInt int paramInt, @NonNull double[] paramArrayOfdouble) {
    RGBToLAB(Color.red(paramInt), Color.green(paramInt), Color.blue(paramInt), paramArrayOfdouble);
  }
  
  public static void colorToXYZ(@ColorInt int paramInt, @NonNull double[] paramArrayOfdouble) {
    RGBToXYZ(Color.red(paramInt), Color.green(paramInt), Color.blue(paramInt), paramArrayOfdouble);
  }
  
  private static int compositeAlpha(int paramInt1, int paramInt2) {
    return 255 - (255 - paramInt2) * (255 - paramInt1) / 255;
  }
  
  public static int compositeColors(@ColorInt int paramInt1, @ColorInt int paramInt2) {
    int i = Color.alpha(paramInt2);
    int j = Color.alpha(paramInt1);
    int k = compositeAlpha(j, i);
    return Color.argb(k, compositeComponent(Color.red(paramInt1), j, Color.red(paramInt2), i, k), compositeComponent(Color.green(paramInt1), j, Color.green(paramInt2), i, k), compositeComponent(Color.blue(paramInt1), j, Color.blue(paramInt2), i, k));
  }
  
  @NonNull
  @RequiresApi(26)
  public static Color compositeColors(@NonNull Color paramColor1, @NonNull Color paramColor2) {
    if (Objects.equals(paramColor1.getModel(), paramColor2.getModel())) {
      if (!Objects.equals(paramColor2.getColorSpace(), paramColor1.getColorSpace()))
        paramColor1 = paramColor1.convert(paramColor2.getColorSpace()); 
      float[] arrayOfFloat1 = paramColor1.getComponents();
      float[] arrayOfFloat2 = paramColor2.getComponents();
      float f1 = paramColor1.alpha();
      float f2 = paramColor2.alpha() * (1.0F - f1);
      int i = paramColor2.getComponentCount() - 1;
      arrayOfFloat2[i] = f1 + f2;
      float f3 = f2;
      float f4 = f1;
      if (arrayOfFloat2[i] > 0.0F) {
        f4 = f1 / arrayOfFloat2[i];
        f3 = f2 / arrayOfFloat2[i];
      } 
      for (byte b = 0; b < i; b++)
        arrayOfFloat2[b] = arrayOfFloat1[b] * f4 + arrayOfFloat2[b] * f3; 
      return Color.valueOf(arrayOfFloat2, paramColor2.getColorSpace());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Color models must match (");
    stringBuilder.append(paramColor1.getModel());
    stringBuilder.append(" vs. ");
    stringBuilder.append(paramColor2.getModel());
    stringBuilder.append(")");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  private static int compositeComponent(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    return (paramInt5 == 0) ? 0 : ((paramInt1 * 255 * paramInt2 + paramInt3 * paramInt4 * (255 - paramInt2)) / paramInt5 * 255);
  }
  
  private static float constrain(float paramFloat1, float paramFloat2, float paramFloat3) {
    if (paramFloat1 >= paramFloat2) {
      paramFloat2 = paramFloat1;
      if (paramFloat1 > paramFloat3)
        paramFloat2 = paramFloat3; 
    } 
    return paramFloat2;
  }
  
  private static int constrain(int paramInt1, int paramInt2, int paramInt3) {
    if (paramInt1 >= paramInt2) {
      paramInt2 = paramInt1;
      if (paramInt1 > paramInt3)
        paramInt2 = paramInt3; 
    } 
    return paramInt2;
  }
  
  public static double distanceEuclidean(@NonNull double[] paramArrayOfdouble1, @NonNull double[] paramArrayOfdouble2) {
    return Math.sqrt(Math.pow(paramArrayOfdouble1[0] - paramArrayOfdouble2[0], 2.0D) + Math.pow(paramArrayOfdouble1[1] - paramArrayOfdouble2[1], 2.0D) + Math.pow(paramArrayOfdouble1[2] - paramArrayOfdouble2[2], 2.0D));
  }
  
  private static double[] getTempDouble3Array() {
    double[] arrayOfDouble1 = TEMP_ARRAY.get();
    double[] arrayOfDouble2 = arrayOfDouble1;
    if (arrayOfDouble1 == null) {
      arrayOfDouble2 = new double[3];
      TEMP_ARRAY.set(arrayOfDouble2);
    } 
    return arrayOfDouble2;
  }
  
  private static double pivotXyzComponent(double paramDouble) {
    if (paramDouble > 0.008856D) {
      paramDouble = Math.pow(paramDouble, 0.3333333333333333D);
    } else {
      paramDouble = (paramDouble * 903.3D + 16.0D) / 116.0D;
    } 
    return paramDouble;
  }
  
  @ColorInt
  public static int setAlphaComponent(@ColorInt int paramInt1, @IntRange(from = 0L, to = 255L) int paramInt2) {
    if (paramInt2 >= 0 && paramInt2 <= 255)
      return paramInt1 & 0xFFFFFF | paramInt2 << 24; 
    throw new IllegalArgumentException("alpha must be between 0 and 255.");
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\graphics\ColorUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */