package android.support.v4.os;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import java.util.Locale;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
interface LocaleListInterface {
  boolean equals(Object paramObject);
  
  Locale get(int paramInt);
  
  @Nullable
  Locale getFirstMatch(String[] paramArrayOfString);
  
  Object getLocaleList();
  
  int hashCode();
  
  @IntRange(from = -1L)
  int indexOf(Locale paramLocale);
  
  boolean isEmpty();
  
  void setLocaleList(@NonNull Locale... paramVarArgs);
  
  @IntRange(from = 0L)
  int size();
  
  String toLanguageTags();
  
  String toString();
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\os\LocaleListInterface.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */