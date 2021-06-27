package android.support.v4.os;

import android.os.Build;
import android.support.annotation.GuardedBy;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.Size;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
final class LocaleListHelper {
  private static final Locale EN_LATN;
  
  private static final Locale LOCALE_AR_XB;
  
  private static final Locale LOCALE_EN_XA;
  
  private static final int NUM_PSEUDO_LOCALES = 2;
  
  private static final String STRING_AR_XB = "ar-XB";
  
  private static final String STRING_EN_XA = "en-XA";
  
  @GuardedBy("sLock")
  private static LocaleListHelper sDefaultAdjustedLocaleList;
  
  @GuardedBy("sLock")
  private static LocaleListHelper sDefaultLocaleList;
  
  private static final Locale[] sEmptyList = new Locale[0];
  
  private static final LocaleListHelper sEmptyLocaleList = new LocaleListHelper(new Locale[0]);
  
  @GuardedBy("sLock")
  private static Locale sLastDefaultLocale;
  
  @GuardedBy("sLock")
  private static LocaleListHelper sLastExplicitlySetLocaleList;
  
  private static final Object sLock;
  
  private final Locale[] mList;
  
  @NonNull
  private final String mStringRepresentation;
  
  static {
    LOCALE_EN_XA = new Locale("en", "XA");
    LOCALE_AR_XB = new Locale("ar", "XB");
    EN_LATN = LocaleHelper.forLanguageTag("en-Latn");
    sLock = new Object();
    sLastExplicitlySetLocaleList = null;
    sDefaultLocaleList = null;
    sDefaultAdjustedLocaleList = null;
    sLastDefaultLocale = null;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  LocaleListHelper(@NonNull Locale paramLocale, LocaleListHelper paramLocaleListHelper) {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial <init> : ()V
    //   4: aload_1
    //   5: ifnull -> 293
    //   8: iconst_0
    //   9: istore_3
    //   10: aload_2
    //   11: ifnonnull -> 20
    //   14: iconst_0
    //   15: istore #4
    //   17: goto -> 27
    //   20: aload_2
    //   21: getfield mList : [Ljava/util/Locale;
    //   24: arraylength
    //   25: istore #4
    //   27: iconst_0
    //   28: istore #5
    //   30: iload #5
    //   32: iload #4
    //   34: if_icmpge -> 60
    //   37: aload_1
    //   38: aload_2
    //   39: getfield mList : [Ljava/util/Locale;
    //   42: iload #5
    //   44: aaload
    //   45: invokevirtual equals : (Ljava/lang/Object;)Z
    //   48: ifeq -> 54
    //   51: goto -> 63
    //   54: iinc #5, 1
    //   57: goto -> 30
    //   60: iconst_m1
    //   61: istore #5
    //   63: iload #5
    //   65: iconst_m1
    //   66: if_icmpne -> 75
    //   69: iconst_1
    //   70: istore #6
    //   72: goto -> 78
    //   75: iconst_0
    //   76: istore #6
    //   78: iload #6
    //   80: iload #4
    //   82: iadd
    //   83: istore #7
    //   85: iload #7
    //   87: anewarray java/util/Locale
    //   90: astore #8
    //   92: aload #8
    //   94: iconst_0
    //   95: aload_1
    //   96: invokevirtual clone : ()Ljava/lang/Object;
    //   99: checkcast java/util/Locale
    //   102: aastore
    //   103: iload #5
    //   105: iconst_m1
    //   106: if_icmpne -> 150
    //   109: iconst_0
    //   110: istore #5
    //   112: iload #5
    //   114: iload #4
    //   116: if_icmpge -> 225
    //   119: iload #5
    //   121: iconst_1
    //   122: iadd
    //   123: istore #6
    //   125: aload #8
    //   127: iload #6
    //   129: aload_2
    //   130: getfield mList : [Ljava/util/Locale;
    //   133: iload #5
    //   135: aaload
    //   136: invokevirtual clone : ()Ljava/lang/Object;
    //   139: checkcast java/util/Locale
    //   142: aastore
    //   143: iload #6
    //   145: istore #5
    //   147: goto -> 112
    //   150: iconst_0
    //   151: istore #6
    //   153: iload #6
    //   155: iload #5
    //   157: if_icmpge -> 191
    //   160: iload #6
    //   162: iconst_1
    //   163: iadd
    //   164: istore #9
    //   166: aload #8
    //   168: iload #9
    //   170: aload_2
    //   171: getfield mList : [Ljava/util/Locale;
    //   174: iload #6
    //   176: aaload
    //   177: invokevirtual clone : ()Ljava/lang/Object;
    //   180: checkcast java/util/Locale
    //   183: aastore
    //   184: iload #9
    //   186: istore #6
    //   188: goto -> 153
    //   191: iinc #5, 1
    //   194: iload #5
    //   196: iload #4
    //   198: if_icmpge -> 225
    //   201: aload #8
    //   203: iload #5
    //   205: aload_2
    //   206: getfield mList : [Ljava/util/Locale;
    //   209: iload #5
    //   211: aaload
    //   212: invokevirtual clone : ()Ljava/lang/Object;
    //   215: checkcast java/util/Locale
    //   218: aastore
    //   219: iinc #5, 1
    //   222: goto -> 194
    //   225: new java/lang/StringBuilder
    //   228: dup
    //   229: invokespecial <init> : ()V
    //   232: astore_1
    //   233: iload_3
    //   234: istore #5
    //   236: iload #5
    //   238: iload #7
    //   240: if_icmpge -> 278
    //   243: aload_1
    //   244: aload #8
    //   246: iload #5
    //   248: aaload
    //   249: invokestatic toLanguageTag : (Ljava/util/Locale;)Ljava/lang/String;
    //   252: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   255: pop
    //   256: iload #5
    //   258: iload #7
    //   260: iconst_1
    //   261: isub
    //   262: if_icmpge -> 272
    //   265: aload_1
    //   266: bipush #44
    //   268: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   271: pop
    //   272: iinc #5, 1
    //   275: goto -> 236
    //   278: aload_0
    //   279: aload #8
    //   281: putfield mList : [Ljava/util/Locale;
    //   284: aload_0
    //   285: aload_1
    //   286: invokevirtual toString : ()Ljava/lang/String;
    //   289: putfield mStringRepresentation : Ljava/lang/String;
    //   292: return
    //   293: new java/lang/NullPointerException
    //   296: dup
    //   297: ldc 'topLocale is null'
    //   299: invokespecial <init> : (Ljava/lang/String;)V
    //   302: athrow
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  LocaleListHelper(@NonNull Locale... paramVarArgs) {
    if (paramVarArgs.length == 0) {
      this.mList = sEmptyList;
      this.mStringRepresentation = "";
    } else {
      Locale[] arrayOfLocale = new Locale[paramVarArgs.length];
      HashSet<Locale> hashSet = new HashSet();
      StringBuilder stringBuilder = new StringBuilder();
      byte b = 0;
      while (b < paramVarArgs.length) {
        Locale locale = paramVarArgs[b];
        if (locale != null) {
          if (!hashSet.contains(locale)) {
            locale = (Locale)locale.clone();
            arrayOfLocale[b] = locale;
            stringBuilder.append(LocaleHelper.toLanguageTag(locale));
            if (b < paramVarArgs.length - 1)
              stringBuilder.append(','); 
            hashSet.add(locale);
            b++;
            continue;
          } 
          StringBuilder stringBuilder2 = new StringBuilder();
          stringBuilder2.append("list[");
          stringBuilder2.append(b);
          stringBuilder2.append("] is a repetition");
          throw new IllegalArgumentException(stringBuilder2.toString());
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("list[");
        stringBuilder1.append(b);
        stringBuilder1.append("] is null");
        throw new NullPointerException(stringBuilder1.toString());
      } 
      this.mList = arrayOfLocale;
      this.mStringRepresentation = stringBuilder.toString();
    } 
  }
  
  private Locale computeFirstMatch(Collection<String> paramCollection, boolean paramBoolean) {
    Locale locale;
    int i = computeFirstMatchIndex(paramCollection, paramBoolean);
    if (i == -1) {
      paramCollection = null;
    } else {
      locale = this.mList[i];
    } 
    return locale;
  }
  
  private int computeFirstMatchIndex(Collection<String> paramCollection, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mList : [Ljava/util/Locale;
    //   4: arraylength
    //   5: iconst_1
    //   6: if_icmpne -> 11
    //   9: iconst_0
    //   10: ireturn
    //   11: aload_0
    //   12: getfield mList : [Ljava/util/Locale;
    //   15: arraylength
    //   16: ifne -> 21
    //   19: iconst_m1
    //   20: ireturn
    //   21: iload_2
    //   22: ifeq -> 48
    //   25: aload_0
    //   26: getstatic android/support/v4/os/LocaleListHelper.EN_LATN : Ljava/util/Locale;
    //   29: invokespecial findFirstMatchIndex : (Ljava/util/Locale;)I
    //   32: istore_3
    //   33: iload_3
    //   34: ifne -> 39
    //   37: iconst_0
    //   38: ireturn
    //   39: iload_3
    //   40: ldc 2147483647
    //   42: if_icmpge -> 48
    //   45: goto -> 51
    //   48: ldc 2147483647
    //   50: istore_3
    //   51: aload_1
    //   52: invokeinterface iterator : ()Ljava/util/Iterator;
    //   57: astore_1
    //   58: aload_1
    //   59: invokeinterface hasNext : ()Z
    //   64: ifeq -> 104
    //   67: aload_0
    //   68: aload_1
    //   69: invokeinterface next : ()Ljava/lang/Object;
    //   74: checkcast java/lang/String
    //   77: invokestatic forLanguageTag : (Ljava/lang/String;)Ljava/util/Locale;
    //   80: invokespecial findFirstMatchIndex : (Ljava/util/Locale;)I
    //   83: istore #4
    //   85: iload #4
    //   87: ifne -> 92
    //   90: iconst_0
    //   91: ireturn
    //   92: iload #4
    //   94: iload_3
    //   95: if_icmpge -> 58
    //   98: iload #4
    //   100: istore_3
    //   101: goto -> 58
    //   104: iload_3
    //   105: ldc 2147483647
    //   107: if_icmpne -> 112
    //   110: iconst_0
    //   111: ireturn
    //   112: iload_3
    //   113: ireturn
  }
  
  private int findFirstMatchIndex(Locale paramLocale) {
    for (byte b = 0; b < this.mList.length; b++) {
      if (matchScore(paramLocale, this.mList[b]) > 0)
        return b; 
    } 
    return Integer.MAX_VALUE;
  }
  
  @NonNull
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  static LocaleListHelper forLanguageTags(@Nullable String paramString) {
    if (paramString == null || paramString.isEmpty())
      return getEmptyLocaleList(); 
    String[] arrayOfString = paramString.split(",", -1);
    Locale[] arrayOfLocale = new Locale[arrayOfString.length];
    for (byte b = 0; b < arrayOfLocale.length; b++)
      arrayOfLocale[b] = LocaleHelper.forLanguageTag(arrayOfString[b]); 
    return new LocaleListHelper(arrayOfLocale);
  }
  
  @NonNull
  @Size(min = 1L)
  static LocaleListHelper getAdjustedDefault() {
    getDefault();
    synchronized (sLock) {
      return sDefaultAdjustedLocaleList;
    } 
  }
  
  @NonNull
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  @Size(min = 1L)
  static LocaleListHelper getDefault() {
    null = Locale.getDefault();
    synchronized (sLock) {
      if (!null.equals(sLastDefaultLocale)) {
        LocaleListHelper localeListHelper1;
        sLastDefaultLocale = null;
        if (sDefaultLocaleList != null && null.equals(sDefaultLocaleList.get(0))) {
          localeListHelper1 = sDefaultLocaleList;
          return localeListHelper1;
        } 
        LocaleListHelper localeListHelper2 = new LocaleListHelper();
        this((Locale)localeListHelper1, sLastExplicitlySetLocaleList);
        sDefaultLocaleList = localeListHelper2;
        sDefaultAdjustedLocaleList = sDefaultLocaleList;
      } 
      return sDefaultLocaleList;
    } 
  }
  
  @NonNull
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  static LocaleListHelper getEmptyLocaleList() {
    return sEmptyLocaleList;
  }
  
  private static String getLikelyScript(Locale paramLocale) {
    if (Build.VERSION.SDK_INT >= 21) {
      String str = paramLocale.getScript();
      return !str.isEmpty() ? str : "";
    } 
    return "";
  }
  
  private static boolean isPseudoLocale(String paramString) {
    return ("en-XA".equals(paramString) || "ar-XB".equals(paramString));
  }
  
  private static boolean isPseudoLocale(Locale paramLocale) {
    return (LOCALE_EN_XA.equals(paramLocale) || LOCALE_AR_XB.equals(paramLocale));
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  static boolean isPseudoLocalesOnly(@Nullable String[] paramArrayOfString) {
    if (paramArrayOfString == null)
      return true; 
    if (paramArrayOfString.length > 3)
      return false; 
    int i = paramArrayOfString.length;
    for (byte b = 0; b < i; b++) {
      String str = paramArrayOfString[b];
      if (!str.isEmpty() && !isPseudoLocale(str))
        return false; 
    } 
    return true;
  }
  
  @IntRange(from = 0L, to = 1L)
  private static int matchScore(Locale paramLocale1, Locale paramLocale2) {
    boolean bool = paramLocale1.equals(paramLocale2);
    boolean bool1 = true;
    if (bool)
      return 1; 
    if (!paramLocale1.getLanguage().equals(paramLocale2.getLanguage()))
      return 0; 
    if (isPseudoLocale(paramLocale1) || isPseudoLocale(paramLocale2))
      return 0; 
    String str = getLikelyScript(paramLocale1);
    if (str.isEmpty()) {
      String str1 = paramLocale1.getCountry();
      boolean bool2 = bool1;
      if (!str1.isEmpty())
        if (str1.equals(paramLocale2.getCountry())) {
          bool2 = bool1;
        } else {
          bool2 = false;
        }  
      return bool2;
    } 
    return str.equals(getLikelyScript(paramLocale2));
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  static void setDefault(@NonNull @Size(min = 1L) LocaleListHelper paramLocaleListHelper) {
    setDefault(paramLocaleListHelper, 0);
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  static void setDefault(@NonNull @Size(min = 1L) LocaleListHelper paramLocaleListHelper, int paramInt) {
    if (paramLocaleListHelper != null) {
      if (!paramLocaleListHelper.isEmpty())
        synchronized (sLock) {
          sLastDefaultLocale = paramLocaleListHelper.get(paramInt);
          Locale.setDefault(sLastDefaultLocale);
          sLastExplicitlySetLocaleList = paramLocaleListHelper;
          sDefaultLocaleList = paramLocaleListHelper;
          if (paramInt == 0) {
            sDefaultAdjustedLocaleList = sDefaultLocaleList;
          } else {
            paramLocaleListHelper = new LocaleListHelper();
            this(sLastDefaultLocale, sDefaultLocaleList);
            sDefaultAdjustedLocaleList = paramLocaleListHelper;
          } 
          return;
        }  
      throw new IllegalArgumentException("locales is empty");
    } 
    throw new NullPointerException("locales is null");
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof LocaleListHelper))
      return false; 
    paramObject = ((LocaleListHelper)paramObject).mList;
    if (this.mList.length != paramObject.length)
      return false; 
    for (byte b = 0; b < this.mList.length; b++) {
      if (!this.mList[b].equals(paramObject[b]))
        return false; 
    } 
    return true;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  Locale get(int paramInt) {
    Locale locale;
    if (paramInt >= 0 && paramInt < this.mList.length) {
      locale = this.mList[paramInt];
    } else {
      locale = null;
    } 
    return locale;
  }
  
  @Nullable
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  Locale getFirstMatch(String[] paramArrayOfString) {
    return computeFirstMatch(Arrays.asList(paramArrayOfString), false);
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  int getFirstMatchIndex(String[] paramArrayOfString) {
    return computeFirstMatchIndex(Arrays.asList(paramArrayOfString), false);
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  int getFirstMatchIndexWithEnglishSupported(Collection<String> paramCollection) {
    return computeFirstMatchIndex(paramCollection, true);
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  int getFirstMatchIndexWithEnglishSupported(String[] paramArrayOfString) {
    return getFirstMatchIndexWithEnglishSupported(Arrays.asList(paramArrayOfString));
  }
  
  @Nullable
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  Locale getFirstMatchWithEnglishSupported(String[] paramArrayOfString) {
    return computeFirstMatch(Arrays.asList(paramArrayOfString), true);
  }
  
  public int hashCode() {
    int i = 1;
    for (byte b = 0; b < this.mList.length; b++)
      i = i * 31 + this.mList[b].hashCode(); 
    return i;
  }
  
  @IntRange(from = -1L)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  int indexOf(Locale paramLocale) {
    for (byte b = 0; b < this.mList.length; b++) {
      if (this.mList[b].equals(paramLocale))
        return b; 
    } 
    return -1;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  boolean isEmpty() {
    boolean bool;
    if (this.mList.length == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  @IntRange(from = 0L)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  int size() {
    return this.mList.length;
  }
  
  @NonNull
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  String toLanguageTags() {
    return this.mStringRepresentation;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[");
    for (byte b = 0; b < this.mList.length; b++) {
      stringBuilder.append(this.mList[b]);
      if (b < this.mList.length - 1)
        stringBuilder.append(','); 
    } 
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\os\LocaleListHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */