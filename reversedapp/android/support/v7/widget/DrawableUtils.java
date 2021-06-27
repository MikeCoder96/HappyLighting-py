package android.support.v7.widget;

import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.v4.graphics.drawable.WrappedDrawable;
import android.support.v7.graphics.drawable.DrawableWrapper;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class DrawableUtils {
  public static final Rect INSETS_NONE = new Rect();
  
  private static final String TAG = "DrawableUtils";
  
  private static final String VECTOR_DRAWABLE_CLAZZ_NAME = "android.graphics.drawable.VectorDrawable";
  
  private static Class<?> sInsetsClazz;
  
  static {
    if (Build.VERSION.SDK_INT >= 18)
      try {
        sInsetsClazz = Class.forName("android.graphics.Insets");
      } catch (ClassNotFoundException classNotFoundException) {} 
  }
  
  public static boolean canSafelyMutateDrawable(@NonNull Drawable paramDrawable) {
    Drawable[] arrayOfDrawable;
    if (Build.VERSION.SDK_INT < 15 && paramDrawable instanceof android.graphics.drawable.InsetDrawable)
      return false; 
    if (Build.VERSION.SDK_INT < 15 && paramDrawable instanceof android.graphics.drawable.GradientDrawable)
      return false; 
    if (Build.VERSION.SDK_INT < 17 && paramDrawable instanceof android.graphics.drawable.LayerDrawable)
      return false; 
    if (paramDrawable instanceof DrawableContainer) {
      Drawable.ConstantState constantState = paramDrawable.getConstantState();
      if (constantState instanceof DrawableContainer.DrawableContainerState) {
        arrayOfDrawable = ((DrawableContainer.DrawableContainerState)constantState).getChildren();
        int i = arrayOfDrawable.length;
        for (byte b = 0; b < i; b++) {
          if (!canSafelyMutateDrawable(arrayOfDrawable[b]))
            return false; 
        } 
      } 
    } else {
      if (arrayOfDrawable instanceof WrappedDrawable)
        return canSafelyMutateDrawable(((WrappedDrawable)arrayOfDrawable).getWrappedDrawable()); 
      if (arrayOfDrawable instanceof DrawableWrapper)
        return canSafelyMutateDrawable(((DrawableWrapper)arrayOfDrawable).getWrappedDrawable()); 
      if (arrayOfDrawable instanceof ScaleDrawable)
        return canSafelyMutateDrawable(((ScaleDrawable)arrayOfDrawable).getDrawable()); 
    } 
    return true;
  }
  
  static void fixDrawable(@NonNull Drawable paramDrawable) {
    if (Build.VERSION.SDK_INT == 21 && "android.graphics.drawable.VectorDrawable".equals(paramDrawable.getClass().getName()))
      fixVectorDrawableTinting(paramDrawable); 
  }
  
  private static void fixVectorDrawableTinting(Drawable paramDrawable) {
    int[] arrayOfInt = paramDrawable.getState();
    if (arrayOfInt == null || arrayOfInt.length == 0) {
      paramDrawable.setState(ThemeUtils.CHECKED_STATE_SET);
    } else {
      paramDrawable.setState(ThemeUtils.EMPTY_STATE_SET);
    } 
    paramDrawable.setState(arrayOfInt);
  }
  
  public static Rect getOpticalBounds(Drawable paramDrawable) {
    // Byte code:
    //   0: getstatic android/support/v7/widget/DrawableUtils.sInsetsClazz : Ljava/lang/Class;
    //   3: ifnull -> 285
    //   6: aload_0
    //   7: invokestatic unwrap : (Landroid/graphics/drawable/Drawable;)Landroid/graphics/drawable/Drawable;
    //   10: astore_0
    //   11: aload_0
    //   12: invokevirtual getClass : ()Ljava/lang/Class;
    //   15: ldc 'getOpticalInsets'
    //   17: iconst_0
    //   18: anewarray java/lang/Class
    //   21: invokevirtual getMethod : (Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   24: aload_0
    //   25: iconst_0
    //   26: anewarray java/lang/Object
    //   29: invokevirtual invoke : (Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   32: astore_1
    //   33: aload_1
    //   34: ifnull -> 285
    //   37: new android/graphics/Rect
    //   40: astore_0
    //   41: aload_0
    //   42: invokespecial <init> : ()V
    //   45: getstatic android/support/v7/widget/DrawableUtils.sInsetsClazz : Ljava/lang/Class;
    //   48: invokevirtual getFields : ()[Ljava/lang/reflect/Field;
    //   51: astore_2
    //   52: aload_2
    //   53: arraylength
    //   54: istore_3
    //   55: iconst_0
    //   56: istore #4
    //   58: iload #4
    //   60: iload_3
    //   61: if_icmpge -> 274
    //   64: aload_2
    //   65: iload #4
    //   67: aaload
    //   68: astore #5
    //   70: aload #5
    //   72: invokevirtual getName : ()Ljava/lang/String;
    //   75: astore #6
    //   77: aload #6
    //   79: invokevirtual hashCode : ()I
    //   82: istore #7
    //   84: iload #7
    //   86: ldc -1383228885
    //   88: if_icmpeq -> 163
    //   91: iload #7
    //   93: ldc 115029
    //   95: if_icmpeq -> 147
    //   98: iload #7
    //   100: ldc 3317767
    //   102: if_icmpeq -> 131
    //   105: iload #7
    //   107: ldc 108511772
    //   109: if_icmpeq -> 115
    //   112: goto -> 179
    //   115: aload #6
    //   117: ldc 'right'
    //   119: invokevirtual equals : (Ljava/lang/Object;)Z
    //   122: ifeq -> 179
    //   125: iconst_2
    //   126: istore #7
    //   128: goto -> 182
    //   131: aload #6
    //   133: ldc 'left'
    //   135: invokevirtual equals : (Ljava/lang/Object;)Z
    //   138: ifeq -> 179
    //   141: iconst_0
    //   142: istore #7
    //   144: goto -> 182
    //   147: aload #6
    //   149: ldc 'top'
    //   151: invokevirtual equals : (Ljava/lang/Object;)Z
    //   154: ifeq -> 179
    //   157: iconst_1
    //   158: istore #7
    //   160: goto -> 182
    //   163: aload #6
    //   165: ldc 'bottom'
    //   167: invokevirtual equals : (Ljava/lang/Object;)Z
    //   170: ifeq -> 179
    //   173: iconst_3
    //   174: istore #7
    //   176: goto -> 182
    //   179: iconst_m1
    //   180: istore #7
    //   182: iload #7
    //   184: tableswitch default -> 216, 0 -> 258, 1 -> 245, 2 -> 232, 3 -> 219
    //   216: goto -> 268
    //   219: aload_0
    //   220: aload #5
    //   222: aload_1
    //   223: invokevirtual getInt : (Ljava/lang/Object;)I
    //   226: putfield bottom : I
    //   229: goto -> 268
    //   232: aload_0
    //   233: aload #5
    //   235: aload_1
    //   236: invokevirtual getInt : (Ljava/lang/Object;)I
    //   239: putfield right : I
    //   242: goto -> 268
    //   245: aload_0
    //   246: aload #5
    //   248: aload_1
    //   249: invokevirtual getInt : (Ljava/lang/Object;)I
    //   252: putfield top : I
    //   255: goto -> 268
    //   258: aload_0
    //   259: aload #5
    //   261: aload_1
    //   262: invokevirtual getInt : (Ljava/lang/Object;)I
    //   265: putfield left : I
    //   268: iinc #4, 1
    //   271: goto -> 58
    //   274: aload_0
    //   275: areturn
    //   276: astore_0
    //   277: ldc 'DrawableUtils'
    //   279: ldc 'Couldn't obtain the optical insets. Ignoring.'
    //   281: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   284: pop
    //   285: getstatic android/support/v7/widget/DrawableUtils.INSETS_NONE : Landroid/graphics/Rect;
    //   288: areturn
    // Exception table:
    //   from	to	target	type
    //   6	33	276	java/lang/Exception
    //   37	55	276	java/lang/Exception
    //   70	84	276	java/lang/Exception
    //   115	125	276	java/lang/Exception
    //   131	141	276	java/lang/Exception
    //   147	157	276	java/lang/Exception
    //   163	173	276	java/lang/Exception
    //   219	229	276	java/lang/Exception
    //   232	242	276	java/lang/Exception
    //   245	255	276	java/lang/Exception
    //   258	268	276	java/lang/Exception
  }
  
  public static PorterDuff.Mode parseTintMode(int paramInt, PorterDuff.Mode paramMode) {
    if (paramInt != 3) {
      if (paramInt != 5) {
        if (paramInt != 9) {
          switch (paramInt) {
            default:
              return paramMode;
            case 16:
              return PorterDuff.Mode.ADD;
            case 15:
              return PorterDuff.Mode.SCREEN;
            case 14:
              break;
          } 
          return PorterDuff.Mode.MULTIPLY;
        } 
        return PorterDuff.Mode.SRC_ATOP;
      } 
      return PorterDuff.Mode.SRC_IN;
    } 
    return PorterDuff.Mode.SRC_OVER;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v7\widget\DrawableUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */