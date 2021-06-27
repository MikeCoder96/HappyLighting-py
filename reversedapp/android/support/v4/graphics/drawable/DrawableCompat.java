package android.support.v4.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import java.io.IOException;
import java.lang.reflect.Method;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class DrawableCompat {
  private static final String TAG = "DrawableCompat";
  
  private static Method sGetLayoutDirectionMethod;
  
  private static boolean sGetLayoutDirectionMethodFetched;
  
  private static Method sSetLayoutDirectionMethod;
  
  private static boolean sSetLayoutDirectionMethodFetched;
  
  public static void applyTheme(@NonNull Drawable paramDrawable, @NonNull Resources.Theme paramTheme) {
    if (Build.VERSION.SDK_INT >= 21)
      paramDrawable.applyTheme(paramTheme); 
  }
  
  public static boolean canApplyTheme(@NonNull Drawable paramDrawable) {
    return (Build.VERSION.SDK_INT >= 21) ? paramDrawable.canApplyTheme() : false;
  }
  
  public static void clearColorFilter(@NonNull Drawable paramDrawable) {
    if (Build.VERSION.SDK_INT >= 23) {
      paramDrawable.clearColorFilter();
    } else if (Build.VERSION.SDK_INT >= 21) {
      paramDrawable.clearColorFilter();
      if (paramDrawable instanceof InsetDrawable) {
        clearColorFilter(((InsetDrawable)paramDrawable).getDrawable());
      } else if (paramDrawable instanceof WrappedDrawable) {
        clearColorFilter(((WrappedDrawable)paramDrawable).getWrappedDrawable());
      } else if (paramDrawable instanceof DrawableContainer) {
        DrawableContainer.DrawableContainerState drawableContainerState = (DrawableContainer.DrawableContainerState)((DrawableContainer)paramDrawable).getConstantState();
        if (drawableContainerState != null) {
          byte b = 0;
          int i = drawableContainerState.getChildCount();
          while (b < i) {
            paramDrawable = drawableContainerState.getChild(b);
            if (paramDrawable != null)
              clearColorFilter(paramDrawable); 
            b++;
          } 
        } 
      } 
    } else {
      paramDrawable.clearColorFilter();
    } 
  }
  
  public static int getAlpha(@NonNull Drawable paramDrawable) {
    return (Build.VERSION.SDK_INT >= 19) ? paramDrawable.getAlpha() : 0;
  }
  
  public static ColorFilter getColorFilter(@NonNull Drawable paramDrawable) {
    return (Build.VERSION.SDK_INT >= 21) ? paramDrawable.getColorFilter() : null;
  }
  
  public static int getLayoutDirection(@NonNull Drawable paramDrawable) {
    if (Build.VERSION.SDK_INT >= 23)
      return paramDrawable.getLayoutDirection(); 
    if (Build.VERSION.SDK_INT >= 17) {
      if (!sGetLayoutDirectionMethodFetched) {
        try {
          sGetLayoutDirectionMethod = Drawable.class.getDeclaredMethod("getLayoutDirection", new Class[0]);
          sGetLayoutDirectionMethod.setAccessible(true);
        } catch (NoSuchMethodException noSuchMethodException) {
          Log.i("DrawableCompat", "Failed to retrieve getLayoutDirection() method", noSuchMethodException);
        } 
        sGetLayoutDirectionMethodFetched = true;
      } 
      if (sGetLayoutDirectionMethod != null)
        try {
          return ((Integer)sGetLayoutDirectionMethod.invoke(paramDrawable, new Object[0])).intValue();
        } catch (Exception exception) {
          Log.i("DrawableCompat", "Failed to invoke getLayoutDirection() via reflection", exception);
          sGetLayoutDirectionMethod = null;
        }  
      return 0;
    } 
    return 0;
  }
  
  public static void inflate(@NonNull Drawable paramDrawable, @NonNull Resources paramResources, @NonNull XmlPullParser paramXmlPullParser, @NonNull AttributeSet paramAttributeSet, @Nullable Resources.Theme paramTheme) throws XmlPullParserException, IOException {
    if (Build.VERSION.SDK_INT >= 21) {
      paramDrawable.inflate(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
    } else {
      paramDrawable.inflate(paramResources, paramXmlPullParser, paramAttributeSet);
    } 
  }
  
  public static boolean isAutoMirrored(@NonNull Drawable paramDrawable) {
    return (Build.VERSION.SDK_INT >= 19) ? paramDrawable.isAutoMirrored() : false;
  }
  
  @Deprecated
  public static void jumpToCurrentState(@NonNull Drawable paramDrawable) {
    paramDrawable.jumpToCurrentState();
  }
  
  public static void setAutoMirrored(@NonNull Drawable paramDrawable, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 19)
      paramDrawable.setAutoMirrored(paramBoolean); 
  }
  
  public static void setHotspot(@NonNull Drawable paramDrawable, float paramFloat1, float paramFloat2) {
    if (Build.VERSION.SDK_INT >= 21)
      paramDrawable.setHotspot(paramFloat1, paramFloat2); 
  }
  
  public static void setHotspotBounds(@NonNull Drawable paramDrawable, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (Build.VERSION.SDK_INT >= 21)
      paramDrawable.setHotspotBounds(paramInt1, paramInt2, paramInt3, paramInt4); 
  }
  
  public static boolean setLayoutDirection(@NonNull Drawable paramDrawable, int paramInt) {
    if (Build.VERSION.SDK_INT >= 23)
      return paramDrawable.setLayoutDirection(paramInt); 
    if (Build.VERSION.SDK_INT >= 17) {
      if (!sSetLayoutDirectionMethodFetched) {
        try {
          sSetLayoutDirectionMethod = Drawable.class.getDeclaredMethod("setLayoutDirection", new Class[] { int.class });
          sSetLayoutDirectionMethod.setAccessible(true);
        } catch (NoSuchMethodException noSuchMethodException) {
          Log.i("DrawableCompat", "Failed to retrieve setLayoutDirection(int) method", noSuchMethodException);
        } 
        sSetLayoutDirectionMethodFetched = true;
      } 
      if (sSetLayoutDirectionMethod != null)
        try {
          sSetLayoutDirectionMethod.invoke(paramDrawable, new Object[] { Integer.valueOf(paramInt) });
          return true;
        } catch (Exception exception) {
          Log.i("DrawableCompat", "Failed to invoke setLayoutDirection(int) via reflection", exception);
          sSetLayoutDirectionMethod = null;
        }  
      return false;
    } 
    return false;
  }
  
  public static void setTint(@NonNull Drawable paramDrawable, @ColorInt int paramInt) {
    if (Build.VERSION.SDK_INT >= 21) {
      paramDrawable.setTint(paramInt);
    } else if (paramDrawable instanceof TintAwareDrawable) {
      ((TintAwareDrawable)paramDrawable).setTint(paramInt);
    } 
  }
  
  public static void setTintList(@NonNull Drawable paramDrawable, @Nullable ColorStateList paramColorStateList) {
    if (Build.VERSION.SDK_INT >= 21) {
      paramDrawable.setTintList(paramColorStateList);
    } else if (paramDrawable instanceof TintAwareDrawable) {
      ((TintAwareDrawable)paramDrawable).setTintList(paramColorStateList);
    } 
  }
  
  public static void setTintMode(@NonNull Drawable paramDrawable, @NonNull PorterDuff.Mode paramMode) {
    if (Build.VERSION.SDK_INT >= 21) {
      paramDrawable.setTintMode(paramMode);
    } else if (paramDrawable instanceof TintAwareDrawable) {
      ((TintAwareDrawable)paramDrawable).setTintMode(paramMode);
    } 
  }
  
  public static <T extends Drawable> T unwrap(@NonNull Drawable paramDrawable) {
    return (T)((paramDrawable instanceof WrappedDrawable) ? ((WrappedDrawable)paramDrawable).getWrappedDrawable() : paramDrawable);
  }
  
  public static Drawable wrap(@NonNull Drawable paramDrawable) {
    return (Build.VERSION.SDK_INT >= 23) ? paramDrawable : ((Build.VERSION.SDK_INT >= 21) ? (!(paramDrawable instanceof TintAwareDrawable) ? new WrappedDrawableApi21(paramDrawable) : paramDrawable) : (!(paramDrawable instanceof TintAwareDrawable) ? new WrappedDrawableApi14(paramDrawable) : paramDrawable));
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\graphics\drawable\DrawableCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */