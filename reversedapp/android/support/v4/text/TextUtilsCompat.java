package android.support.v4.text;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import java.util.Locale;

public final class TextUtilsCompat {
  private static final String ARAB_SCRIPT_SUBTAG = "Arab";
  
  private static final String HEBR_SCRIPT_SUBTAG = "Hebr";
  
  private static final Locale ROOT = new Locale("", "");
  
  private static int getLayoutDirectionFromFirstChar(@NonNull Locale paramLocale) {
    switch (Character.getDirectionality(paramLocale.getDisplayName(paramLocale).charAt(0))) {
      default:
        return 0;
      case 1:
      case 2:
        break;
    } 
    return 1;
  }
  
  public static int getLayoutDirectionFromLocale(@Nullable Locale paramLocale) {
    if (Build.VERSION.SDK_INT >= 17)
      return TextUtils.getLayoutDirectionFromLocale(paramLocale); 
    if (paramLocale != null && !paramLocale.equals(ROOT)) {
      String str = ICUCompat.maximizeAndGetScript(paramLocale);
      if (str == null)
        return getLayoutDirectionFromFirstChar(paramLocale); 
      if (str.equalsIgnoreCase("Arab") || str.equalsIgnoreCase("Hebr"))
        return 1; 
    } 
    return 0;
  }
  
  @NonNull
  public static String htmlEncode(@NonNull String paramString) {
    if (Build.VERSION.SDK_INT >= 17)
      return TextUtils.htmlEncode(paramString); 
    StringBuilder stringBuilder = new StringBuilder();
    for (byte b = 0; b < paramString.length(); b++) {
      char c = paramString.charAt(b);
      if (c != '"') {
        if (c != '<') {
          if (c != '>') {
            switch (c) {
              default:
                stringBuilder.append(c);
                break;
              case '\'':
                stringBuilder.append("&#39;");
                break;
              case '&':
                stringBuilder.append("&amp;");
                break;
            } 
          } else {
            stringBuilder.append("&gt;");
          } 
        } else {
          stringBuilder.append("&lt;");
        } 
      } else {
        stringBuilder.append("&quot;");
      } 
    } 
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\text\TextUtilsCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */