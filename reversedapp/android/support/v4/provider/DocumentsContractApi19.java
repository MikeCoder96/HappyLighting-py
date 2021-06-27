package android.support.v4.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

@RequiresApi(19)
class DocumentsContractApi19 {
  private static final int FLAG_VIRTUAL_DOCUMENT = 512;
  
  private static final String TAG = "DocumentFile";
  
  public static boolean canRead(Context paramContext, Uri paramUri) {
    return (paramContext.checkCallingOrSelfUriPermission(paramUri, 1) != 0) ? false : (!TextUtils.isEmpty(getRawType(paramContext, paramUri)));
  }
  
  public static boolean canWrite(Context paramContext, Uri paramUri) {
    if (paramContext.checkCallingOrSelfUriPermission(paramUri, 2) != 0)
      return false; 
    String str = getRawType(paramContext, paramUri);
    int i = queryForInt(paramContext, paramUri, "flags", 0);
    return TextUtils.isEmpty(str) ? false : (((i & 0x4) != 0) ? true : (("vnd.android.document/directory".equals(str) && (i & 0x8) != 0) ? true : ((!TextUtils.isEmpty(str) && (i & 0x2) != 0))));
  }
  
  private static void closeQuietly(@Nullable AutoCloseable paramAutoCloseable) {
    if (paramAutoCloseable != null)
      try {
        paramAutoCloseable.close();
      } catch (RuntimeException runtimeException) {
        throw runtimeException;
      } catch (Exception exception) {} 
  }
  
  public static boolean exists(Context paramContext, Uri paramUri) {
    ContentResolver contentResolver = paramContext.getContentResolver();
    boolean bool = true;
    Uri uri = null;
    paramContext = null;
    try {
    
    } catch (Exception exception) {
    
    } finally {
      ContentResolver contentResolver1;
      contentResolver = null;
      context = paramContext;
    } 
    paramContext = context;
    StringBuilder stringBuilder = new StringBuilder();
    paramContext = context;
    this();
    Context context;
    paramContext = context;
    stringBuilder.append("Failed query: ");
    paramContext = context;
    stringBuilder.append(contentResolver);
    paramContext = context;
    Log.w("DocumentFile", stringBuilder.toString());
    closeQuietly((AutoCloseable)context);
    return false;
  }
  
  public static long getFlags(Context paramContext, Uri paramUri) {
    return queryForLong(paramContext, paramUri, "flags", 0L);
  }
  
  @Nullable
  public static String getName(Context paramContext, Uri paramUri) {
    return queryForString(paramContext, paramUri, "_display_name", null);
  }
  
  @Nullable
  private static String getRawType(Context paramContext, Uri paramUri) {
    return queryForString(paramContext, paramUri, "mime_type", null);
  }
  
  @Nullable
  public static String getType(Context paramContext, Uri paramUri) {
    String str = getRawType(paramContext, paramUri);
    return "vnd.android.document/directory".equals(str) ? null : str;
  }
  
  public static boolean isDirectory(Context paramContext, Uri paramUri) {
    return "vnd.android.document/directory".equals(getRawType(paramContext, paramUri));
  }
  
  public static boolean isFile(Context paramContext, Uri paramUri) {
    String str = getRawType(paramContext, paramUri);
    return !("vnd.android.document/directory".equals(str) || TextUtils.isEmpty(str));
  }
  
  public static boolean isVirtual(Context paramContext, Uri paramUri) {
    boolean bool = DocumentsContract.isDocumentUri(paramContext, paramUri);
    boolean bool1 = false;
    if (!bool)
      return false; 
    if ((getFlags(paramContext, paramUri) & 0x200L) != 0L)
      bool1 = true; 
    return bool1;
  }
  
  public static long lastModified(Context paramContext, Uri paramUri) {
    return queryForLong(paramContext, paramUri, "last_modified", 0L);
  }
  
  public static long length(Context paramContext, Uri paramUri) {
    return queryForLong(paramContext, paramUri, "_size", 0L);
  }
  
  private static int queryForInt(Context paramContext, Uri paramUri, String paramString, int paramInt) {
    return (int)queryForLong(paramContext, paramUri, paramString, paramInt);
  }
  
  private static long queryForLong(Context paramContext, Uri paramUri, String paramString, long paramLong) {
    ContentResolver contentResolver = paramContext.getContentResolver();
    Uri uri2 = null;
    paramContext = null;
    try {
      Cursor cursor = contentResolver.query(paramUri, new String[] { paramString }, null, null, null);
      try {
        return paramLong;
      } catch (Exception null) {
      
      } finally {
        String str;
        paramString = null;
        Cursor cursor1 = cursor;
      } 
    } catch (Exception exception) {
      paramUri = uri2;
    } finally {}
    Uri uri1 = paramUri;
    StringBuilder stringBuilder = new StringBuilder();
    uri1 = paramUri;
    this();
    uri1 = paramUri;
    stringBuilder.append("Failed query: ");
    uri1 = paramUri;
    stringBuilder.append(exception);
    uri1 = paramUri;
    Log.w("DocumentFile", stringBuilder.toString());
    closeQuietly((AutoCloseable)paramUri);
    return paramLong;
  }
  
  @Nullable
  private static String queryForString(Context paramContext, Uri paramUri, String paramString1, @Nullable String paramString2) {
    ContentResolver contentResolver = paramContext.getContentResolver();
    Uri uri = null;
    paramContext = null;
    try {
    
    } catch (Exception exception) {
    
    } finally {
      String str;
      paramString1 = null;
      context = paramContext;
    } 
    paramContext = context;
    StringBuilder stringBuilder = new StringBuilder();
    paramContext = context;
    this();
    Context context;
    paramContext = context;
    stringBuilder.append("Failed query: ");
    paramContext = context;
    stringBuilder.append(paramString1);
    paramContext = context;
    Log.w("DocumentFile", stringBuilder.toString());
    closeQuietly((AutoCloseable)context);
    return paramString2;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\provider\DocumentsContractApi19.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */