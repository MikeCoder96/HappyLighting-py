package android.support.v4.graphics.drawable;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.util.Preconditions;
import android.text.TextUtils;
import android.util.Log;
import androidx.versionedparcelable.CustomVersionedParcelable;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;

public class IconCompat extends CustomVersionedParcelable {
  private static final float ADAPTIVE_ICON_INSET_FACTOR = 0.25F;
  
  private static final int AMBIENT_SHADOW_ALPHA = 30;
  
  private static final float BLUR_FACTOR = 0.010416667F;
  
  static final PorterDuff.Mode DEFAULT_TINT_MODE = PorterDuff.Mode.SRC_IN;
  
  private static final float DEFAULT_VIEW_PORT_SCALE = 0.6666667F;
  
  private static final String EXTRA_INT1 = "int1";
  
  private static final String EXTRA_INT2 = "int2";
  
  private static final String EXTRA_OBJ = "obj";
  
  private static final String EXTRA_TINT_LIST = "tint_list";
  
  private static final String EXTRA_TINT_MODE = "tint_mode";
  
  private static final String EXTRA_TYPE = "type";
  
  private static final float ICON_DIAMETER_FACTOR = 0.9166667F;
  
  private static final int KEY_SHADOW_ALPHA = 61;
  
  private static final float KEY_SHADOW_OFFSET_FACTOR = 0.020833334F;
  
  private static final String TAG = "IconCompat";
  
  public static final int TYPE_UNKNOWN = -1;
  
  @RestrictTo({RestrictTo.Scope.LIBRARY})
  public byte[] mData;
  
  @RestrictTo({RestrictTo.Scope.LIBRARY})
  public int mInt1;
  
  @RestrictTo({RestrictTo.Scope.LIBRARY})
  public int mInt2;
  
  Object mObj1;
  
  @RestrictTo({RestrictTo.Scope.LIBRARY})
  public Parcelable mParcelable;
  
  @RestrictTo({RestrictTo.Scope.LIBRARY})
  public ColorStateList mTintList = null;
  
  PorterDuff.Mode mTintMode = DEFAULT_TINT_MODE;
  
  @RestrictTo({RestrictTo.Scope.LIBRARY})
  public String mTintModeStr;
  
  @RestrictTo({RestrictTo.Scope.LIBRARY})
  public int mType;
  
  @RestrictTo({RestrictTo.Scope.LIBRARY})
  public IconCompat() {}
  
  private IconCompat(int paramInt) {
    this.mType = paramInt;
  }
  
  @Nullable
  public static IconCompat createFromBundle(@NonNull Bundle paramBundle) {
    StringBuilder stringBuilder;
    int i = paramBundle.getInt("type");
    IconCompat iconCompat = new IconCompat(i);
    iconCompat.mInt1 = paramBundle.getInt("int1");
    iconCompat.mInt2 = paramBundle.getInt("int2");
    if (paramBundle.containsKey("tint_list"))
      iconCompat.mTintList = (ColorStateList)paramBundle.getParcelable("tint_list"); 
    if (paramBundle.containsKey("tint_mode"))
      iconCompat.mTintMode = PorterDuff.Mode.valueOf(paramBundle.getString("tint_mode")); 
    if (i != -1)
      switch (i) {
        default:
          stringBuilder = new StringBuilder();
          stringBuilder.append("Unknown type ");
          stringBuilder.append(i);
          Log.w("IconCompat", stringBuilder.toString());
          return null;
        case 3:
          iconCompat.mObj1 = stringBuilder.getByteArray("obj");
          return iconCompat;
        case 2:
        case 4:
          iconCompat.mObj1 = stringBuilder.getString("obj");
          return iconCompat;
        case 1:
        case 5:
          break;
      }  
    iconCompat.mObj1 = stringBuilder.getParcelable("obj");
    return iconCompat;
  }
  
  @Nullable
  @RequiresApi(23)
  public static IconCompat createFromIcon(@NonNull Context paramContext, @NonNull Icon paramIcon) {
    IconCompat iconCompat;
    Preconditions.checkNotNull(paramIcon);
    int i = getType(paramIcon);
    if (i != 2) {
      if (i != 4) {
        iconCompat = new IconCompat(-1);
        iconCompat.mObj1 = paramIcon;
        return iconCompat;
      } 
      return createWithContentUri(getUri(paramIcon));
    } 
    String str = getResPackage(paramIcon);
    try {
      return createWithResource(getResources((Context)iconCompat, str), str, getResId(paramIcon));
    } catch (android.content.res.Resources.NotFoundException notFoundException) {
      throw new IllegalArgumentException("Icon resource cannot be found");
    } 
  }
  
  @Nullable
  @RequiresApi(23)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static IconCompat createFromIcon(@NonNull Icon paramIcon) {
    Preconditions.checkNotNull(paramIcon);
    int i = getType(paramIcon);
    if (i != 2) {
      if (i != 4) {
        IconCompat iconCompat = new IconCompat(-1);
        iconCompat.mObj1 = paramIcon;
        return iconCompat;
      } 
      return createWithContentUri(getUri(paramIcon));
    } 
    return createWithResource(null, getResPackage(paramIcon), getResId(paramIcon));
  }
  
  @VisibleForTesting
  static Bitmap createLegacyIconFromAdaptiveIcon(Bitmap paramBitmap, boolean paramBoolean) {
    int i = (int)(Math.min(paramBitmap.getWidth(), paramBitmap.getHeight()) * 0.6666667F);
    Bitmap bitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    Paint paint = new Paint(3);
    float f1 = i;
    float f2 = 0.5F * f1;
    float f3 = 0.9166667F * f2;
    if (paramBoolean) {
      float f = 0.010416667F * f1;
      paint.setColor(0);
      paint.setShadowLayer(f, 0.0F, f1 * 0.020833334F, 1023410176);
      canvas.drawCircle(f2, f2, f3, paint);
      paint.setShadowLayer(f, 0.0F, 0.0F, 503316480);
      canvas.drawCircle(f2, f2, f3, paint);
      paint.clearShadowLayer();
    } 
    paint.setColor(-16777216);
    BitmapShader bitmapShader = new BitmapShader(paramBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    Matrix matrix = new Matrix();
    matrix.setTranslate((-(paramBitmap.getWidth() - i) / 2), (-(paramBitmap.getHeight() - i) / 2));
    bitmapShader.setLocalMatrix(matrix);
    paint.setShader((Shader)bitmapShader);
    canvas.drawCircle(f2, f2, f3, paint);
    canvas.setBitmap(null);
    return bitmap;
  }
  
  public static IconCompat createWithAdaptiveBitmap(Bitmap paramBitmap) {
    if (paramBitmap != null) {
      IconCompat iconCompat = new IconCompat(5);
      iconCompat.mObj1 = paramBitmap;
      return iconCompat;
    } 
    throw new IllegalArgumentException("Bitmap must not be null.");
  }
  
  public static IconCompat createWithBitmap(Bitmap paramBitmap) {
    if (paramBitmap != null) {
      IconCompat iconCompat = new IconCompat(1);
      iconCompat.mObj1 = paramBitmap;
      return iconCompat;
    } 
    throw new IllegalArgumentException("Bitmap must not be null.");
  }
  
  public static IconCompat createWithContentUri(Uri paramUri) {
    if (paramUri != null)
      return createWithContentUri(paramUri.toString()); 
    throw new IllegalArgumentException("Uri must not be null.");
  }
  
  public static IconCompat createWithContentUri(String paramString) {
    if (paramString != null) {
      IconCompat iconCompat = new IconCompat(4);
      iconCompat.mObj1 = paramString;
      return iconCompat;
    } 
    throw new IllegalArgumentException("Uri must not be null.");
  }
  
  public static IconCompat createWithData(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    if (paramArrayOfbyte != null) {
      IconCompat iconCompat = new IconCompat(3);
      iconCompat.mObj1 = paramArrayOfbyte;
      iconCompat.mInt1 = paramInt1;
      iconCompat.mInt2 = paramInt2;
      return iconCompat;
    } 
    throw new IllegalArgumentException("Data must not be null.");
  }
  
  public static IconCompat createWithResource(Context paramContext, @DrawableRes int paramInt) {
    if (paramContext != null)
      return createWithResource(paramContext.getResources(), paramContext.getPackageName(), paramInt); 
    throw new IllegalArgumentException("Context must not be null.");
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY})
  public static IconCompat createWithResource(Resources paramResources, String paramString, @DrawableRes int paramInt) {
    if (paramString != null) {
      if (paramInt != 0) {
        IconCompat iconCompat = new IconCompat(2);
        iconCompat.mInt1 = paramInt;
        if (paramResources != null) {
          try {
            iconCompat.mObj1 = paramResources.getResourceName(paramInt);
          } catch (android.content.res.Resources.NotFoundException notFoundException) {
            throw new IllegalArgumentException("Icon resource cannot be found");
          } 
        } else {
          iconCompat.mObj1 = paramString;
        } 
        return iconCompat;
      } 
      throw new IllegalArgumentException("Drawable resource ID must not be 0");
    } 
    throw new IllegalArgumentException("Package must not be null.");
  }
  
  @DrawableRes
  @IdRes
  @RequiresApi(23)
  private static int getResId(@NonNull Icon paramIcon) {
    if (Build.VERSION.SDK_INT >= 28)
      return paramIcon.getResId(); 
    try {
      return ((Integer)paramIcon.getClass().getMethod("getResId", new Class[0]).invoke(paramIcon, new Object[0])).intValue();
    } catch (IllegalAccessException illegalAccessException) {
      Log.e("IconCompat", "Unable to get icon resource", illegalAccessException);
      return 0;
    } catch (InvocationTargetException invocationTargetException) {
      Log.e("IconCompat", "Unable to get icon resource", invocationTargetException);
      return 0;
    } catch (NoSuchMethodException noSuchMethodException) {
      Log.e("IconCompat", "Unable to get icon resource", noSuchMethodException);
      return 0;
    } 
  }
  
  @Nullable
  @RequiresApi(23)
  private static String getResPackage(@NonNull Icon paramIcon) {
    if (Build.VERSION.SDK_INT >= 28)
      return paramIcon.getResPackage(); 
    try {
      return (String)paramIcon.getClass().getMethod("getResPackage", new Class[0]).invoke(paramIcon, new Object[0]);
    } catch (IllegalAccessException illegalAccessException) {
      Log.e("IconCompat", "Unable to get icon package", illegalAccessException);
      return null;
    } catch (InvocationTargetException invocationTargetException) {
      Log.e("IconCompat", "Unable to get icon package", invocationTargetException);
      return null;
    } catch (NoSuchMethodException noSuchMethodException) {
      Log.e("IconCompat", "Unable to get icon package", noSuchMethodException);
      return null;
    } 
  }
  
  private static Resources getResources(Context paramContext, String paramString) {
    if ("android".equals(paramString))
      return Resources.getSystem(); 
    PackageManager packageManager = paramContext.getPackageManager();
    try {
      ApplicationInfo applicationInfo = packageManager.getApplicationInfo(paramString, 8192);
      return (applicationInfo != null) ? packageManager.getResourcesForApplication(applicationInfo) : null;
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      Log.e("IconCompat", String.format("Unable to find pkg=%s for icon", new Object[] { paramString }), (Throwable)nameNotFoundException);
      return null;
    } 
  }
  
  @RequiresApi(23)
  private static int getType(@NonNull Icon paramIcon) {
    if (Build.VERSION.SDK_INT >= 28)
      return paramIcon.getType(); 
    try {
      return ((Integer)paramIcon.getClass().getMethod("getType", new Class[0]).invoke(paramIcon, new Object[0])).intValue();
    } catch (IllegalAccessException illegalAccessException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unable to get icon type ");
      stringBuilder.append(paramIcon);
      Log.e("IconCompat", stringBuilder.toString(), illegalAccessException);
      return -1;
    } catch (InvocationTargetException invocationTargetException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unable to get icon type ");
      stringBuilder.append(paramIcon);
      Log.e("IconCompat", stringBuilder.toString(), invocationTargetException);
      return -1;
    } catch (NoSuchMethodException noSuchMethodException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unable to get icon type ");
      stringBuilder.append(paramIcon);
      Log.e("IconCompat", stringBuilder.toString(), noSuchMethodException);
      return -1;
    } 
  }
  
  @Nullable
  @RequiresApi(23)
  private static Uri getUri(@NonNull Icon paramIcon) {
    if (Build.VERSION.SDK_INT >= 28)
      return paramIcon.getUri(); 
    try {
      return (Uri)paramIcon.getClass().getMethod("getUri", new Class[0]).invoke(paramIcon, new Object[0]);
    } catch (IllegalAccessException illegalAccessException) {
      Log.e("IconCompat", "Unable to get icon uri", illegalAccessException);
      return null;
    } catch (InvocationTargetException invocationTargetException) {
      Log.e("IconCompat", "Unable to get icon uri", invocationTargetException);
      return null;
    } catch (NoSuchMethodException noSuchMethodException) {
      Log.e("IconCompat", "Unable to get icon uri", noSuchMethodException);
      return null;
    } 
  }
  
  private Drawable loadDrawableInner(Context paramContext) {
    Uri uri;
    String str1;
    String str3;
    String str2;
    Resources resources;
    switch (this.mType) {
      default:
        return null;
      case 5:
        return (Drawable)new BitmapDrawable(paramContext.getResources(), createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1, false));
      case 4:
        uri = Uri.parse((String)this.mObj1);
        str3 = uri.getScheme();
        if ("content".equals(str3) || "file".equals(str3)) {
          try {
            InputStream inputStream = paramContext.getContentResolver().openInputStream(uri);
          } catch (Exception null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to load image from URI: ");
            stringBuilder.append(uri);
            Log.w("IconCompat", stringBuilder.toString(), exception);
            exception = null;
          } 
        } else {
          try {
            File file = new File();
            this((String)this.mObj1);
            FileInputStream fileInputStream = new FileInputStream(file);
          } catch (FileNotFoundException exception) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to load image from path: ");
            stringBuilder.append(uri);
            Log.w("IconCompat", stringBuilder.toString(), exception);
            exception = null;
          } 
        } 
        if (exception != null)
          return (Drawable)new BitmapDrawable(paramContext.getResources(), BitmapFactory.decodeStream((InputStream)exception)); 
      case 3:
        return (Drawable)new BitmapDrawable(paramContext.getResources(), BitmapFactory.decodeByteArray((byte[])this.mObj1, this.mInt1, this.mInt2));
      case 2:
        str1 = getResPackage();
        str2 = str1;
        if (TextUtils.isEmpty(str1))
          str2 = paramContext.getPackageName(); 
        resources = getResources(paramContext, str2);
        try {
          return ResourcesCompat.getDrawable(resources, this.mInt1, paramContext.getTheme());
        } catch (RuntimeException runtimeException) {
          Log.e("IconCompat", String.format("Unable to load resource 0x%08x from pkg=%s", new Object[] { Integer.valueOf(this.mInt1), this.mObj1 }), runtimeException);
        } 
      case 1:
        break;
    } 
    return (Drawable)new BitmapDrawable(runtimeException.getResources(), (Bitmap)this.mObj1);
  }
  
  private static String typeToString(int paramInt) {
    switch (paramInt) {
      default:
        return "UNKNOWN";
      case 5:
        return "BITMAP_MASKABLE";
      case 4:
        return "URI";
      case 3:
        return "DATA";
      case 2:
        return "RESOURCE";
      case 1:
        break;
    } 
    return "BITMAP";
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void addToShortcutIntent(@NonNull Intent paramIntent, @Nullable Drawable paramDrawable, @NonNull Context paramContext) {
    StringBuilder stringBuilder;
    Bitmap bitmap;
    checkResource(paramContext);
    int i = this.mType;
    if (i != 5) {
      Bitmap bitmap1;
      switch (i) {
        default:
          throw new IllegalArgumentException("Icon type not supported for intent shortcuts");
        case 2:
          try {
            Bitmap bitmap2;
            paramContext = paramContext.createPackageContext(getResPackage(), 0);
            if (paramDrawable == null) {
              paramIntent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", (Parcelable)Intent.ShortcutIconResource.fromContext(paramContext, this.mInt1));
              return;
            } 
            Drawable drawable = ContextCompat.getDrawable(paramContext, this.mInt1);
            if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
              i = ((ActivityManager)paramContext.getSystemService("activity")).getLauncherLargeIconSize();
              bitmap2 = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
            } else {
              bitmap2 = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            } 
            drawable.setBounds(0, 0, bitmap2.getWidth(), bitmap2.getHeight());
            Canvas canvas = new Canvas();
            this(bitmap2);
            drawable.draw(canvas);
          } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Can't find package ");
            stringBuilder.append(this.mObj1);
            throw new IllegalArgumentException(stringBuilder.toString(), nameNotFoundException);
          } 
          break;
        case 1:
          bitmap1 = (Bitmap)this.mObj1;
          bitmap = bitmap1;
          if (nameNotFoundException != null)
            bitmap = bitmap1.copy(bitmap1.getConfig(), true); 
          break;
      } 
    } else {
      bitmap = createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1, true);
    } 
    if (nameNotFoundException != null) {
      i = bitmap.getWidth();
      int j = bitmap.getHeight();
      nameNotFoundException.setBounds(i / 2, j / 2, i, j);
      nameNotFoundException.draw(new Canvas(bitmap));
    } 
    stringBuilder.putExtra("android.intent.extra.shortcut.ICON", (Parcelable)bitmap);
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void checkResource(Context paramContext) {
    if (this.mType == 2) {
      String str1 = (String)this.mObj1;
      if (!str1.contains(":"))
        return; 
      String str2 = str1.split(":", -1)[1];
      String str3 = str2.split("/", -1)[0];
      str2 = str2.split("/", -1)[1];
      str1 = str1.split(":", -1)[0];
      int i = getResources(paramContext, str1).getIdentifier(str2, str3, str1);
      if (this.mInt1 != i) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Id has changed for ");
        stringBuilder.append(str1);
        stringBuilder.append("/");
        stringBuilder.append(str2);
        Log.i("IconCompat", stringBuilder.toString());
        this.mInt1 = i;
      } 
    } 
  }
  
  @IdRes
  public int getResId() {
    if (this.mType == -1 && Build.VERSION.SDK_INT >= 23)
      return getResId((Icon)this.mObj1); 
    if (this.mType == 2)
      return this.mInt1; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("called getResId() on ");
    stringBuilder.append(this);
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  @NonNull
  public String getResPackage() {
    if (this.mType == -1 && Build.VERSION.SDK_INT >= 23)
      return getResPackage((Icon)this.mObj1); 
    if (this.mType == 2)
      return ((String)this.mObj1).split(":", -1)[0]; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("called getResPackage() on ");
    stringBuilder.append(this);
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  public int getType() {
    return (this.mType == -1 && Build.VERSION.SDK_INT >= 23) ? getType((Icon)this.mObj1) : this.mType;
  }
  
  @NonNull
  public Uri getUri() {
    return (this.mType == -1 && Build.VERSION.SDK_INT >= 23) ? getUri((Icon)this.mObj1) : Uri.parse((String)this.mObj1);
  }
  
  public Drawable loadDrawable(Context paramContext) {
    checkResource(paramContext);
    if (Build.VERSION.SDK_INT >= 23)
      return toIcon().loadDrawable(paramContext); 
    Drawable drawable = loadDrawableInner(paramContext);
    if (drawable != null && (this.mTintList != null || this.mTintMode != DEFAULT_TINT_MODE)) {
      drawable.mutate();
      DrawableCompat.setTintList(drawable, this.mTintList);
      DrawableCompat.setTintMode(drawable, this.mTintMode);
    } 
    return drawable;
  }
  
  public void onPostParceling() {
    this.mTintMode = PorterDuff.Mode.valueOf(this.mTintModeStr);
    int i = this.mType;
    if (i != -1) {
      switch (i) {
        default:
          return;
        case 3:
          this.mObj1 = this.mData;
        case 2:
        case 4:
          this.mObj1 = new String(this.mData, Charset.forName("UTF-16"));
        case 1:
        case 5:
          break;
      } 
      if (this.mParcelable != null)
        this.mObj1 = this.mParcelable; 
      this.mObj1 = this.mData;
      this.mType = 3;
      this.mInt1 = 0;
      this.mInt2 = this.mData.length;
    } 
    if (this.mParcelable != null)
      this.mObj1 = this.mParcelable; 
    throw new IllegalArgumentException("Invalid icon");
  }
  
  public void onPreParceling(boolean paramBoolean) {
    this.mTintModeStr = this.mTintMode.name();
    int i = this.mType;
    if (i != -1) {
      switch (i) {
        default:
          return;
        case 4:
          this.mData = this.mObj1.toString().getBytes(Charset.forName("UTF-16"));
        case 3:
          this.mData = (byte[])this.mObj1;
        case 2:
          this.mData = ((String)this.mObj1).getBytes(Charset.forName("UTF-16"));
        case 1:
        case 5:
          break;
      } 
      if (paramBoolean) {
        Bitmap bitmap = (Bitmap)this.mObj1;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
        this.mData = byteArrayOutputStream.toByteArray();
      } 
      this.mParcelable = (Parcelable)this.mObj1;
    } 
    if (!paramBoolean)
      this.mParcelable = (Parcelable)this.mObj1; 
    throw new IllegalArgumentException("Can't serialize Icon created with IconCompat#createFromIcon");
  }
  
  public IconCompat setTint(@ColorInt int paramInt) {
    return setTintList(ColorStateList.valueOf(paramInt));
  }
  
  public IconCompat setTintList(ColorStateList paramColorStateList) {
    this.mTintList = paramColorStateList;
    return this;
  }
  
  public IconCompat setTintMode(PorterDuff.Mode paramMode) {
    this.mTintMode = paramMode;
    return this;
  }
  
  public Bundle toBundle() {
    Bundle bundle = new Bundle();
    int i = this.mType;
    if (i != -1) {
      switch (i) {
        default:
          throw new IllegalArgumentException("Invalid icon");
        case 3:
          bundle.putByteArray("obj", (byte[])this.mObj1);
          break;
        case 2:
        case 4:
          bundle.putString("obj", (String)this.mObj1);
          break;
        case 1:
        case 5:
          bundle.putParcelable("obj", (Parcelable)this.mObj1);
          break;
      } 
    } else {
      bundle.putParcelable("obj", (Parcelable)this.mObj1);
    } 
    bundle.putInt("type", this.mType);
    bundle.putInt("int1", this.mInt1);
    bundle.putInt("int2", this.mInt2);
    if (this.mTintList != null)
      bundle.putParcelable("tint_list", (Parcelable)this.mTintList); 
    if (this.mTintMode != DEFAULT_TINT_MODE)
      bundle.putString("tint_mode", this.mTintMode.name()); 
    return bundle;
  }
  
  @RequiresApi(23)
  public Icon toIcon() {
    int i = this.mType;
    if (i != -1) {
      Icon icon;
      switch (i) {
        default:
          throw new IllegalArgumentException("Unknown type");
        case 5:
          if (Build.VERSION.SDK_INT >= 26) {
            Icon icon1 = Icon.createWithAdaptiveBitmap((Bitmap)this.mObj1);
            break;
          } 
          icon = Icon.createWithBitmap(createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1, false));
          break;
        case 4:
          icon = Icon.createWithContentUri((String)this.mObj1);
          break;
        case 3:
          icon = Icon.createWithData((byte[])this.mObj1, this.mInt1, this.mInt2);
          break;
        case 2:
          icon = Icon.createWithResource(getResPackage(), this.mInt1);
          break;
        case 1:
          icon = Icon.createWithBitmap((Bitmap)this.mObj1);
          break;
      } 
      if (this.mTintList != null)
        icon.setTintList(this.mTintList); 
      if (this.mTintMode != DEFAULT_TINT_MODE)
        icon.setTintMode(this.mTintMode); 
      return icon;
    } 
    return (Icon)this.mObj1;
  }
  
  public String toString() {
    if (this.mType == -1)
      return String.valueOf(this.mObj1); 
    StringBuilder stringBuilder = new StringBuilder("Icon(typ=");
    stringBuilder.append(typeToString(this.mType));
    switch (this.mType) {
      case 4:
        stringBuilder.append(" uri=");
        stringBuilder.append(this.mObj1);
        break;
      case 3:
        stringBuilder.append(" len=");
        stringBuilder.append(this.mInt1);
        if (this.mInt2 != 0) {
          stringBuilder.append(" off=");
          stringBuilder.append(this.mInt2);
        } 
        break;
      case 2:
        stringBuilder.append(" pkg=");
        stringBuilder.append(getResPackage());
        stringBuilder.append(" id=");
        stringBuilder.append(String.format("0x%08x", new Object[] { Integer.valueOf(getResId()) }));
        break;
      case 1:
      case 5:
        stringBuilder.append(" size=");
        stringBuilder.append(((Bitmap)this.mObj1).getWidth());
        stringBuilder.append("x");
        stringBuilder.append(((Bitmap)this.mObj1).getHeight());
        break;
    } 
    if (this.mTintList != null) {
      stringBuilder.append(" tint=");
      stringBuilder.append(this.mTintList);
    } 
    if (this.mTintMode != DEFAULT_TINT_MODE) {
      stringBuilder.append(" mode=");
      stringBuilder.append(this.mTintMode);
    } 
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY})
  public static @interface IconType {}
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\graphics\drawable\IconCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */