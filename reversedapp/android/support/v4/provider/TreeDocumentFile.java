package android.support.v4.provider;

import android.content.Context;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
class TreeDocumentFile extends DocumentFile {
  private Context mContext;
  
  private Uri mUri;
  
  TreeDocumentFile(@Nullable DocumentFile paramDocumentFile, Context paramContext, Uri paramUri) {
    super(paramDocumentFile);
    this.mContext = paramContext;
    this.mUri = paramUri;
  }
  
  private static void closeQuietly(@Nullable AutoCloseable paramAutoCloseable) {
    if (paramAutoCloseable != null)
      try {
        paramAutoCloseable.close();
      } catch (RuntimeException runtimeException) {
        throw runtimeException;
      } catch (Exception exception) {} 
  }
  
  @Nullable
  private static Uri createFile(Context paramContext, Uri paramUri, String paramString1, String paramString2) {
    try {
      return DocumentsContract.createDocument(paramContext.getContentResolver(), paramUri, paramString1, paramString2);
    } catch (Exception exception) {
      return null;
    } 
  }
  
  public boolean canRead() {
    return DocumentsContractApi19.canRead(this.mContext, this.mUri);
  }
  
  public boolean canWrite() {
    return DocumentsContractApi19.canWrite(this.mContext, this.mUri);
  }
  
  @Nullable
  public DocumentFile createDirectory(String paramString) {
    Uri uri = createFile(this.mContext, this.mUri, "vnd.android.document/directory", paramString);
    if (uri != null) {
      TreeDocumentFile treeDocumentFile = new TreeDocumentFile(this, this.mContext, uri);
    } else {
      uri = null;
    } 
    return (DocumentFile)uri;
  }
  
  @Nullable
  public DocumentFile createFile(String paramString1, String paramString2) {
    Uri uri = createFile(this.mContext, this.mUri, paramString1, paramString2);
    if (uri != null) {
      TreeDocumentFile treeDocumentFile = new TreeDocumentFile(this, this.mContext, uri);
    } else {
      uri = null;
    } 
    return (DocumentFile)uri;
  }
  
  public boolean delete() {
    try {
      return DocumentsContract.deleteDocument(this.mContext.getContentResolver(), this.mUri);
    } catch (Exception exception) {
      return false;
    } 
  }
  
  public boolean exists() {
    return DocumentsContractApi19.exists(this.mContext, this.mUri);
  }
  
  @Nullable
  public String getName() {
    return DocumentsContractApi19.getName(this.mContext, this.mUri);
  }
  
  @Nullable
  public String getType() {
    return DocumentsContractApi19.getType(this.mContext, this.mUri);
  }
  
  public Uri getUri() {
    return this.mUri;
  }
  
  public boolean isDirectory() {
    return DocumentsContractApi19.isDirectory(this.mContext, this.mUri);
  }
  
  public boolean isFile() {
    return DocumentsContractApi19.isFile(this.mContext, this.mUri);
  }
  
  public boolean isVirtual() {
    return DocumentsContractApi19.isVirtual(this.mContext, this.mUri);
  }
  
  public long lastModified() {
    return DocumentsContractApi19.lastModified(this.mContext, this.mUri);
  }
  
  public long length() {
    return DocumentsContractApi19.length(this.mContext, this.mUri);
  }
  
  public DocumentFile[] listFiles() {
    // Byte code:
    //   0: aload_0
    //   1: getfield mContext : Landroid/content/Context;
    //   4: invokevirtual getContentResolver : ()Landroid/content/ContentResolver;
    //   7: astore_1
    //   8: aload_0
    //   9: getfield mUri : Landroid/net/Uri;
    //   12: aload_0
    //   13: getfield mUri : Landroid/net/Uri;
    //   16: invokestatic getDocumentId : (Landroid/net/Uri;)Ljava/lang/String;
    //   19: invokestatic buildChildDocumentsUriUsingTree : (Landroid/net/Uri;Ljava/lang/String;)Landroid/net/Uri;
    //   22: astore_2
    //   23: new java/util/ArrayList
    //   26: dup
    //   27: invokespecial <init> : ()V
    //   30: astore_3
    //   31: iconst_0
    //   32: istore #4
    //   34: aconst_null
    //   35: astore #5
    //   37: aconst_null
    //   38: astore #6
    //   40: aload_1
    //   41: aload_2
    //   42: iconst_1
    //   43: anewarray java/lang/String
    //   46: dup
    //   47: iconst_0
    //   48: ldc 'document_id'
    //   50: aastore
    //   51: aconst_null
    //   52: aconst_null
    //   53: aconst_null
    //   54: invokevirtual query : (Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   57: astore_2
    //   58: aload_2
    //   59: invokeinterface moveToNext : ()Z
    //   64: ifeq -> 93
    //   67: aload_2
    //   68: iconst_0
    //   69: invokeinterface getString : (I)Ljava/lang/String;
    //   74: astore #6
    //   76: aload_3
    //   77: aload_0
    //   78: getfield mUri : Landroid/net/Uri;
    //   81: aload #6
    //   83: invokestatic buildDocumentUriUsingTree : (Landroid/net/Uri;Ljava/lang/String;)Landroid/net/Uri;
    //   86: invokevirtual add : (Ljava/lang/Object;)Z
    //   89: pop
    //   90: goto -> 58
    //   93: aload_2
    //   94: invokestatic closeQuietly : (Ljava/lang/AutoCloseable;)V
    //   97: goto -> 176
    //   100: astore_1
    //   101: aload_2
    //   102: astore #6
    //   104: aload_1
    //   105: astore_2
    //   106: goto -> 235
    //   109: astore_1
    //   110: goto -> 121
    //   113: astore_2
    //   114: goto -> 235
    //   117: astore_1
    //   118: aload #5
    //   120: astore_2
    //   121: aload_2
    //   122: astore #6
    //   124: new java/lang/StringBuilder
    //   127: astore #5
    //   129: aload_2
    //   130: astore #6
    //   132: aload #5
    //   134: invokespecial <init> : ()V
    //   137: aload_2
    //   138: astore #6
    //   140: aload #5
    //   142: ldc 'Failed query: '
    //   144: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   147: pop
    //   148: aload_2
    //   149: astore #6
    //   151: aload #5
    //   153: aload_1
    //   154: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   157: pop
    //   158: aload_2
    //   159: astore #6
    //   161: ldc 'DocumentFile'
    //   163: aload #5
    //   165: invokevirtual toString : ()Ljava/lang/String;
    //   168: invokestatic w : (Ljava/lang/String;Ljava/lang/String;)I
    //   171: pop
    //   172: aload_2
    //   173: invokestatic closeQuietly : (Ljava/lang/AutoCloseable;)V
    //   176: aload_3
    //   177: aload_3
    //   178: invokevirtual size : ()I
    //   181: anewarray android/net/Uri
    //   184: invokevirtual toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   187: checkcast [Landroid/net/Uri;
    //   190: astore_2
    //   191: aload_2
    //   192: arraylength
    //   193: anewarray android/support/v4/provider/DocumentFile
    //   196: astore #6
    //   198: iload #4
    //   200: aload_2
    //   201: arraylength
    //   202: if_icmpge -> 232
    //   205: aload #6
    //   207: iload #4
    //   209: new android/support/v4/provider/TreeDocumentFile
    //   212: dup
    //   213: aload_0
    //   214: aload_0
    //   215: getfield mContext : Landroid/content/Context;
    //   218: aload_2
    //   219: iload #4
    //   221: aaload
    //   222: invokespecial <init> : (Landroid/support/v4/provider/DocumentFile;Landroid/content/Context;Landroid/net/Uri;)V
    //   225: aastore
    //   226: iinc #4, 1
    //   229: goto -> 198
    //   232: aload #6
    //   234: areturn
    //   235: aload #6
    //   237: invokestatic closeQuietly : (Ljava/lang/AutoCloseable;)V
    //   240: aload_2
    //   241: athrow
    // Exception table:
    //   from	to	target	type
    //   40	58	117	java/lang/Exception
    //   40	58	113	finally
    //   58	90	109	java/lang/Exception
    //   58	90	100	finally
    //   124	129	113	finally
    //   132	137	113	finally
    //   140	148	113	finally
    //   151	158	113	finally
    //   161	172	113	finally
  }
  
  public boolean renameTo(String paramString) {
    try {
      Uri uri = DocumentsContract.renameDocument(this.mContext.getContentResolver(), this.mUri, paramString);
      if (uri != null) {
        this.mUri = uri;
        return true;
      } 
      return false;
    } catch (Exception exception) {
      return false;
    } 
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\provider\TreeDocumentFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */