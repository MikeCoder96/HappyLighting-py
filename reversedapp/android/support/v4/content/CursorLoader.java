package android.support.v4.content;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.os.CancellationSignal;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;

public class CursorLoader extends AsyncTaskLoader<Cursor> {
  CancellationSignal mCancellationSignal;
  
  Cursor mCursor;
  
  final Loader<Cursor>.ForceLoadContentObserver mObserver = new Loader.ForceLoadContentObserver(this);
  
  String[] mProjection;
  
  String mSelection;
  
  String[] mSelectionArgs;
  
  String mSortOrder;
  
  Uri mUri;
  
  public CursorLoader(@NonNull Context paramContext) {
    super(paramContext);
  }
  
  public CursorLoader(@NonNull Context paramContext, @NonNull Uri paramUri, @Nullable String[] paramArrayOfString1, @Nullable String paramString1, @Nullable String[] paramArrayOfString2, @Nullable String paramString2) {
    super(paramContext);
    this.mUri = paramUri;
    this.mProjection = paramArrayOfString1;
    this.mSelection = paramString1;
    this.mSelectionArgs = paramArrayOfString2;
    this.mSortOrder = paramString2;
  }
  
  public void cancelLoadInBackground() {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial cancelLoadInBackground : ()V
    //   4: aload_0
    //   5: monitorenter
    //   6: aload_0
    //   7: getfield mCancellationSignal : Landroid/support/v4/os/CancellationSignal;
    //   10: ifnull -> 20
    //   13: aload_0
    //   14: getfield mCancellationSignal : Landroid/support/v4/os/CancellationSignal;
    //   17: invokevirtual cancel : ()V
    //   20: aload_0
    //   21: monitorexit
    //   22: return
    //   23: astore_1
    //   24: aload_0
    //   25: monitorexit
    //   26: aload_1
    //   27: athrow
    // Exception table:
    //   from	to	target	type
    //   6	20	23	finally
    //   20	22	23	finally
    //   24	26	23	finally
  }
  
  public void deliverResult(Cursor paramCursor) {
    if (isReset()) {
      if (paramCursor != null)
        paramCursor.close(); 
      return;
    } 
    Cursor cursor = this.mCursor;
    this.mCursor = paramCursor;
    if (isStarted())
      super.deliverResult(paramCursor); 
    if (cursor != null && cursor != paramCursor && !cursor.isClosed())
      cursor.close(); 
  }
  
  @Deprecated
  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString) {
    super.dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mUri=");
    paramPrintWriter.println(this.mUri);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mProjection=");
    paramPrintWriter.println(Arrays.toString((Object[])this.mProjection));
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mSelection=");
    paramPrintWriter.println(this.mSelection);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mSelectionArgs=");
    paramPrintWriter.println(Arrays.toString((Object[])this.mSelectionArgs));
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mSortOrder=");
    paramPrintWriter.println(this.mSortOrder);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mCursor=");
    paramPrintWriter.println(this.mCursor);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mContentChanged=");
    paramPrintWriter.println(this.mContentChanged);
  }
  
  @Nullable
  public String[] getProjection() {
    return this.mProjection;
  }
  
  @Nullable
  public String getSelection() {
    return this.mSelection;
  }
  
  @Nullable
  public String[] getSelectionArgs() {
    return this.mSelectionArgs;
  }
  
  @Nullable
  public String getSortOrder() {
    return this.mSortOrder;
  }
  
  @NonNull
  public Uri getUri() {
    return this.mUri;
  }
  
  public Cursor loadInBackground() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual isLoadInBackgroundCanceled : ()Z
    //   6: ifne -> 125
    //   9: new android/support/v4/os/CancellationSignal
    //   12: astore_1
    //   13: aload_1
    //   14: invokespecial <init> : ()V
    //   17: aload_0
    //   18: aload_1
    //   19: putfield mCancellationSignal : Landroid/support/v4/os/CancellationSignal;
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_0
    //   25: invokevirtual getContext : ()Landroid/content/Context;
    //   28: invokevirtual getContentResolver : ()Landroid/content/ContentResolver;
    //   31: aload_0
    //   32: getfield mUri : Landroid/net/Uri;
    //   35: aload_0
    //   36: getfield mProjection : [Ljava/lang/String;
    //   39: aload_0
    //   40: getfield mSelection : Ljava/lang/String;
    //   43: aload_0
    //   44: getfield mSelectionArgs : [Ljava/lang/String;
    //   47: aload_0
    //   48: getfield mSortOrder : Ljava/lang/String;
    //   51: aload_0
    //   52: getfield mCancellationSignal : Landroid/support/v4/os/CancellationSignal;
    //   55: invokestatic query : (Landroid/content/ContentResolver;Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Landroid/support/v4/os/CancellationSignal;)Landroid/database/Cursor;
    //   58: astore_1
    //   59: aload_1
    //   60: ifnull -> 92
    //   63: aload_1
    //   64: invokeinterface getCount : ()I
    //   69: pop
    //   70: aload_1
    //   71: aload_0
    //   72: getfield mObserver : Landroid/support/v4/content/Loader$ForceLoadContentObserver;
    //   75: invokeinterface registerContentObserver : (Landroid/database/ContentObserver;)V
    //   80: goto -> 92
    //   83: astore_2
    //   84: aload_1
    //   85: invokeinterface close : ()V
    //   90: aload_2
    //   91: athrow
    //   92: aload_0
    //   93: monitorenter
    //   94: aload_0
    //   95: aconst_null
    //   96: putfield mCancellationSignal : Landroid/support/v4/os/CancellationSignal;
    //   99: aload_0
    //   100: monitorexit
    //   101: aload_1
    //   102: areturn
    //   103: astore_1
    //   104: aload_0
    //   105: monitorexit
    //   106: aload_1
    //   107: athrow
    //   108: astore_1
    //   109: aload_0
    //   110: monitorenter
    //   111: aload_0
    //   112: aconst_null
    //   113: putfield mCancellationSignal : Landroid/support/v4/os/CancellationSignal;
    //   116: aload_0
    //   117: monitorexit
    //   118: aload_1
    //   119: athrow
    //   120: astore_1
    //   121: aload_0
    //   122: monitorexit
    //   123: aload_1
    //   124: athrow
    //   125: new android/support/v4/os/OperationCanceledException
    //   128: astore_1
    //   129: aload_1
    //   130: invokespecial <init> : ()V
    //   133: aload_1
    //   134: athrow
    //   135: astore_1
    //   136: aload_0
    //   137: monitorexit
    //   138: aload_1
    //   139: athrow
    // Exception table:
    //   from	to	target	type
    //   2	24	135	finally
    //   24	59	108	finally
    //   63	80	83	java/lang/RuntimeException
    //   63	80	108	finally
    //   84	92	108	finally
    //   94	101	103	finally
    //   104	106	103	finally
    //   111	118	120	finally
    //   121	123	120	finally
    //   125	135	135	finally
    //   136	138	135	finally
  }
  
  public void onCanceled(Cursor paramCursor) {
    if (paramCursor != null && !paramCursor.isClosed())
      paramCursor.close(); 
  }
  
  protected void onReset() {
    super.onReset();
    onStopLoading();
    if (this.mCursor != null && !this.mCursor.isClosed())
      this.mCursor.close(); 
    this.mCursor = null;
  }
  
  protected void onStartLoading() {
    if (this.mCursor != null)
      deliverResult(this.mCursor); 
    if (takeContentChanged() || this.mCursor == null)
      forceLoad(); 
  }
  
  protected void onStopLoading() {
    cancelLoad();
  }
  
  public void setProjection(@Nullable String[] paramArrayOfString) {
    this.mProjection = paramArrayOfString;
  }
  
  public void setSelection(@Nullable String paramString) {
    this.mSelection = paramString;
  }
  
  public void setSelectionArgs(@Nullable String[] paramArrayOfString) {
    this.mSelectionArgs = paramArrayOfString;
  }
  
  public void setSortOrder(@Nullable String paramString) {
    this.mSortOrder = paramString;
  }
  
  public void setUri(@NonNull Uri paramUri) {
    this.mUri = paramUri;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\content\CursorLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */