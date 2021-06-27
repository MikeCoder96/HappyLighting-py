package android.support.v4.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Shader;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.util.Log;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public final class ComplexColorCompat {
  private static final String LOG_TAG = "ComplexColorCompat";
  
  private int mColor;
  
  private final ColorStateList mColorStateList;
  
  private final Shader mShader;
  
  private ComplexColorCompat(Shader paramShader, ColorStateList paramColorStateList, @ColorInt int paramInt) {
    this.mShader = paramShader;
    this.mColorStateList = paramColorStateList;
    this.mColor = paramInt;
  }
  
  @NonNull
  private static ComplexColorCompat createFromXml(@NonNull Resources paramResources, @ColorRes int paramInt, @Nullable Resources.Theme paramTheme) throws IOException, XmlPullParserException {
    // Byte code:
    //   0: aload_0
    //   1: iload_1
    //   2: invokevirtual getXml : (I)Landroid/content/res/XmlResourceParser;
    //   5: astore_3
    //   6: aload_3
    //   7: invokestatic asAttributeSet : (Lorg/xmlpull/v1/XmlPullParser;)Landroid/util/AttributeSet;
    //   10: astore #4
    //   12: aload_3
    //   13: invokeinterface next : ()I
    //   18: istore #5
    //   20: iconst_1
    //   21: istore_1
    //   22: iload #5
    //   24: iconst_2
    //   25: if_icmpeq -> 37
    //   28: iload #5
    //   30: iconst_1
    //   31: if_icmpeq -> 37
    //   34: goto -> 12
    //   37: iload #5
    //   39: iconst_2
    //   40: if_icmpne -> 197
    //   43: aload_3
    //   44: invokeinterface getName : ()Ljava/lang/String;
    //   49: astore #6
    //   51: aload #6
    //   53: invokevirtual hashCode : ()I
    //   56: istore #5
    //   58: iload #5
    //   60: ldc 89650992
    //   62: if_icmpeq -> 90
    //   65: iload #5
    //   67: ldc 1191572447
    //   69: if_icmpeq -> 75
    //   72: goto -> 103
    //   75: aload #6
    //   77: ldc 'selector'
    //   79: invokevirtual equals : (Ljava/lang/Object;)Z
    //   82: ifeq -> 103
    //   85: iconst_0
    //   86: istore_1
    //   87: goto -> 105
    //   90: aload #6
    //   92: ldc 'gradient'
    //   94: invokevirtual equals : (Ljava/lang/Object;)Z
    //   97: ifeq -> 103
    //   100: goto -> 105
    //   103: iconst_m1
    //   104: istore_1
    //   105: iload_1
    //   106: tableswitch default -> 128, 0 -> 185, 1 -> 173
    //   128: new java/lang/StringBuilder
    //   131: dup
    //   132: invokespecial <init> : ()V
    //   135: astore_0
    //   136: aload_0
    //   137: aload_3
    //   138: invokeinterface getPositionDescription : ()Ljava/lang/String;
    //   143: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   146: pop
    //   147: aload_0
    //   148: ldc ': unsupported complex color tag '
    //   150: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   153: pop
    //   154: aload_0
    //   155: aload #6
    //   157: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   160: pop
    //   161: new org/xmlpull/v1/XmlPullParserException
    //   164: dup
    //   165: aload_0
    //   166: invokevirtual toString : ()Ljava/lang/String;
    //   169: invokespecial <init> : (Ljava/lang/String;)V
    //   172: athrow
    //   173: aload_0
    //   174: aload_3
    //   175: aload #4
    //   177: aload_2
    //   178: invokestatic createFromXmlInner : (Landroid/content/res/Resources;Lorg/xmlpull/v1/XmlPullParser;Landroid/util/AttributeSet;Landroid/content/res/Resources$Theme;)Landroid/graphics/Shader;
    //   181: invokestatic from : (Landroid/graphics/Shader;)Landroid/support/v4/content/res/ComplexColorCompat;
    //   184: areturn
    //   185: aload_0
    //   186: aload_3
    //   187: aload #4
    //   189: aload_2
    //   190: invokestatic createFromXmlInner : (Landroid/content/res/Resources;Lorg/xmlpull/v1/XmlPullParser;Landroid/util/AttributeSet;Landroid/content/res/Resources$Theme;)Landroid/content/res/ColorStateList;
    //   193: invokestatic from : (Landroid/content/res/ColorStateList;)Landroid/support/v4/content/res/ComplexColorCompat;
    //   196: areturn
    //   197: new org/xmlpull/v1/XmlPullParserException
    //   200: dup
    //   201: ldc 'No start tag found'
    //   203: invokespecial <init> : (Ljava/lang/String;)V
    //   206: athrow
  }
  
  static ComplexColorCompat from(@ColorInt int paramInt) {
    return new ComplexColorCompat(null, null, paramInt);
  }
  
  static ComplexColorCompat from(@NonNull ColorStateList paramColorStateList) {
    return new ComplexColorCompat(null, paramColorStateList, paramColorStateList.getDefaultColor());
  }
  
  static ComplexColorCompat from(@NonNull Shader paramShader) {
    return new ComplexColorCompat(paramShader, null, 0);
  }
  
  @Nullable
  public static ComplexColorCompat inflate(@NonNull Resources paramResources, @ColorRes int paramInt, @Nullable Resources.Theme paramTheme) {
    try {
      return createFromXml(paramResources, paramInt, paramTheme);
    } catch (Exception exception) {
      Log.e("ComplexColorCompat", "Failed to inflate ComplexColor.", exception);
      return null;
    } 
  }
  
  @ColorInt
  public int getColor() {
    return this.mColor;
  }
  
  @Nullable
  public Shader getShader() {
    return this.mShader;
  }
  
  public boolean isGradient() {
    boolean bool;
    if (this.mShader != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isStateful() {
    boolean bool;
    if (this.mShader == null && this.mColorStateList != null && this.mColorStateList.isStateful()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean onStateChanged(int[] paramArrayOfint) {
    if (isStateful()) {
      int i = this.mColorStateList.getColorForState(paramArrayOfint, this.mColorStateList.getDefaultColor());
      if (i != this.mColor) {
        boolean bool = true;
        this.mColor = i;
        return bool;
      } 
    } 
    return false;
  }
  
  public void setColor(@ColorInt int paramInt) {
    this.mColor = paramInt;
  }
  
  public boolean willDraw() {
    return (isGradient() || this.mColor != 0);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\content\res\ComplexColorCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */