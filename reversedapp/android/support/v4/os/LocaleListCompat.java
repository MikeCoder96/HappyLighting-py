package android.support.v4.os;

import android.os.Build;
import android.os.LocaleList;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.Size;
import java.util.Locale;

public final class LocaleListCompat {
  static final LocaleListInterface IMPL;
  
  private static final LocaleListCompat sEmptyLocaleList = new LocaleListCompat();
  
  static {
    if (Build.VERSION.SDK_INT >= 24) {
      IMPL = new LocaleListCompatApi24Impl();
    } else {
      IMPL = new LocaleListCompatBaseImpl();
    } 
  }
  
  public static LocaleListCompat create(@NonNull Locale... paramVarArgs) {
    LocaleListCompat localeListCompat = new LocaleListCompat();
    localeListCompat.setLocaleListArray(paramVarArgs);
    return localeListCompat;
  }
  
  @NonNull
  public static LocaleListCompat forLanguageTags(@Nullable String paramString) {
    if (paramString == null || paramString.isEmpty())
      return getEmptyLocaleList(); 
    String[] arrayOfString = paramString.split(",", -1);
    Locale[] arrayOfLocale = new Locale[arrayOfString.length];
    for (byte b = 0; b < arrayOfLocale.length; b++) {
      Locale locale;
      if (Build.VERSION.SDK_INT >= 21) {
        locale = Locale.forLanguageTag(arrayOfString[b]);
      } else {
        locale = LocaleHelper.forLanguageTag(arrayOfString[b]);
      } 
      arrayOfLocale[b] = locale;
    } 
    LocaleListCompat localeListCompat = new LocaleListCompat();
    localeListCompat.setLocaleListArray(arrayOfLocale);
    return localeListCompat;
  }
  
  @NonNull
  @Size(min = 1L)
  public static LocaleListCompat getAdjustedDefault() {
    return (Build.VERSION.SDK_INT >= 24) ? wrap(LocaleList.getAdjustedDefault()) : create(new Locale[] { Locale.getDefault() });
  }
  
  @NonNull
  @Size(min = 1L)
  public static LocaleListCompat getDefault() {
    return (Build.VERSION.SDK_INT >= 24) ? wrap(LocaleList.getDefault()) : create(new Locale[] { Locale.getDefault() });
  }
  
  @NonNull
  public static LocaleListCompat getEmptyLocaleList() {
    return sEmptyLocaleList;
  }
  
  @RequiresApi(24)
  private void setLocaleList(LocaleList paramLocaleList) {
    int i = paramLocaleList.size();
    if (i > 0) {
      Locale[] arrayOfLocale = new Locale[i];
      for (byte b = 0; b < i; b++)
        arrayOfLocale[b] = paramLocaleList.get(b); 
      IMPL.setLocaleList(arrayOfLocale);
    } 
  }
  
  private void setLocaleListArray(Locale... paramVarArgs) {
    IMPL.setLocaleList(paramVarArgs);
  }
  
  @RequiresApi(24)
  public static LocaleListCompat wrap(Object paramObject) {
    LocaleListCompat localeListCompat = new LocaleListCompat();
    if (paramObject instanceof LocaleList)
      localeListCompat.setLocaleList((LocaleList)paramObject); 
    return localeListCompat;
  }
  
  public boolean equals(Object paramObject) {
    return IMPL.equals(paramObject);
  }
  
  public Locale get(int paramInt) {
    return IMPL.get(paramInt);
  }
  
  public Locale getFirstMatch(String[] paramArrayOfString) {
    return IMPL.getFirstMatch(paramArrayOfString);
  }
  
  public int hashCode() {
    return IMPL.hashCode();
  }
  
  @IntRange(from = -1L)
  public int indexOf(Locale paramLocale) {
    return IMPL.indexOf(paramLocale);
  }
  
  public boolean isEmpty() {
    return IMPL.isEmpty();
  }
  
  @IntRange(from = 0L)
  public int size() {
    return IMPL.size();
  }
  
  @NonNull
  public String toLanguageTags() {
    return IMPL.toLanguageTags();
  }
  
  public String toString() {
    return IMPL.toString();
  }
  
  @Nullable
  public Object unwrap() {
    return IMPL.getLocaleList();
  }
  
  @RequiresApi(24)
  static class LocaleListCompatApi24Impl implements LocaleListInterface {
    private LocaleList mLocaleList = new LocaleList(new Locale[0]);
    
    public boolean equals(Object param1Object) {
      return this.mLocaleList.equals(((LocaleListCompat)param1Object).unwrap());
    }
    
    public Locale get(int param1Int) {
      return this.mLocaleList.get(param1Int);
    }
    
    @Nullable
    public Locale getFirstMatch(String[] param1ArrayOfString) {
      return (this.mLocaleList != null) ? this.mLocaleList.getFirstMatch(param1ArrayOfString) : null;
    }
    
    public Object getLocaleList() {
      return this.mLocaleList;
    }
    
    public int hashCode() {
      return this.mLocaleList.hashCode();
    }
    
    @IntRange(from = -1L)
    public int indexOf(Locale param1Locale) {
      return this.mLocaleList.indexOf(param1Locale);
    }
    
    public boolean isEmpty() {
      return this.mLocaleList.isEmpty();
    }
    
    public void setLocaleList(@NonNull Locale... param1VarArgs) {
      this.mLocaleList = new LocaleList(param1VarArgs);
    }
    
    @IntRange(from = 0L)
    public int size() {
      return this.mLocaleList.size();
    }
    
    public String toLanguageTags() {
      return this.mLocaleList.toLanguageTags();
    }
    
    public String toString() {
      return this.mLocaleList.toString();
    }
  }
  
  static class LocaleListCompatBaseImpl implements LocaleListInterface {
    private LocaleListHelper mLocaleList = new LocaleListHelper(new Locale[0]);
    
    public boolean equals(Object param1Object) {
      return this.mLocaleList.equals(((LocaleListCompat)param1Object).unwrap());
    }
    
    public Locale get(int param1Int) {
      return this.mLocaleList.get(param1Int);
    }
    
    @Nullable
    public Locale getFirstMatch(String[] param1ArrayOfString) {
      return (this.mLocaleList != null) ? this.mLocaleList.getFirstMatch(param1ArrayOfString) : null;
    }
    
    public Object getLocaleList() {
      return this.mLocaleList;
    }
    
    public int hashCode() {
      return this.mLocaleList.hashCode();
    }
    
    @IntRange(from = -1L)
    public int indexOf(Locale param1Locale) {
      return this.mLocaleList.indexOf(param1Locale);
    }
    
    public boolean isEmpty() {
      return this.mLocaleList.isEmpty();
    }
    
    public void setLocaleList(@NonNull Locale... param1VarArgs) {
      this.mLocaleList = new LocaleListHelper(param1VarArgs);
    }
    
    @IntRange(from = 0L)
    public int size() {
      return this.mLocaleList.size();
    }
    
    public String toLanguageTags() {
      return this.mLocaleList.toLanguageTags();
    }
    
    public String toString() {
      return this.mLocaleList.toString();
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\os\LocaleListCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */