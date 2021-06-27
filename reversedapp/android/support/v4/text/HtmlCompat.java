package android.support.v4.text;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;

@SuppressLint({"InlinedApi"})
public final class HtmlCompat {
  public static final int FROM_HTML_MODE_COMPACT = 63;
  
  public static final int FROM_HTML_MODE_LEGACY = 0;
  
  public static final int FROM_HTML_OPTION_USE_CSS_COLORS = 256;
  
  public static final int FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE = 32;
  
  public static final int FROM_HTML_SEPARATOR_LINE_BREAK_DIV = 16;
  
  public static final int FROM_HTML_SEPARATOR_LINE_BREAK_HEADING = 2;
  
  public static final int FROM_HTML_SEPARATOR_LINE_BREAK_LIST = 8;
  
  public static final int FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM = 4;
  
  public static final int FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH = 1;
  
  public static final int TO_HTML_PARAGRAPH_LINES_CONSECUTIVE = 0;
  
  public static final int TO_HTML_PARAGRAPH_LINES_INDIVIDUAL = 1;
  
  @NonNull
  public static Spanned fromHtml(@NonNull String paramString, int paramInt) {
    return (Build.VERSION.SDK_INT >= 24) ? Html.fromHtml(paramString, paramInt) : Html.fromHtml(paramString);
  }
  
  @NonNull
  public static Spanned fromHtml(@NonNull String paramString, int paramInt, @Nullable Html.ImageGetter paramImageGetter, @Nullable Html.TagHandler paramTagHandler) {
    return (Build.VERSION.SDK_INT >= 24) ? Html.fromHtml(paramString, paramInt, paramImageGetter, paramTagHandler) : Html.fromHtml(paramString, paramImageGetter, paramTagHandler);
  }
  
  @NonNull
  public static String toHtml(@NonNull Spanned paramSpanned, int paramInt) {
    return (Build.VERSION.SDK_INT >= 24) ? Html.toHtml(paramSpanned, paramInt) : Html.toHtml(paramSpanned);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\text\HtmlCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */