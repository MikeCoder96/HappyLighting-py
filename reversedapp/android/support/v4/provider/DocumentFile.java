package android.support.v4.provider;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.File;

public abstract class DocumentFile {
  static final String TAG = "DocumentFile";
  
  @Nullable
  private final DocumentFile mParent;
  
  DocumentFile(@Nullable DocumentFile paramDocumentFile) {
    this.mParent = paramDocumentFile;
  }
  
  @NonNull
  public static DocumentFile fromFile(@NonNull File paramFile) {
    return new RawDocumentFile(null, paramFile);
  }
  
  @Nullable
  public static DocumentFile fromSingleUri(@NonNull Context paramContext, @NonNull Uri paramUri) {
    return (Build.VERSION.SDK_INT >= 19) ? new SingleDocumentFile(null, paramContext, paramUri) : null;
  }
  
  @Nullable
  public static DocumentFile fromTreeUri(@NonNull Context paramContext, @NonNull Uri paramUri) {
    return (Build.VERSION.SDK_INT >= 21) ? new TreeDocumentFile(null, paramContext, DocumentsContract.buildDocumentUriUsingTree(paramUri, DocumentsContract.getTreeDocumentId(paramUri))) : null;
  }
  
  public static boolean isDocumentUri(@NonNull Context paramContext, @Nullable Uri paramUri) {
    return (Build.VERSION.SDK_INT >= 19) ? DocumentsContract.isDocumentUri(paramContext, paramUri) : false;
  }
  
  public abstract boolean canRead();
  
  public abstract boolean canWrite();
  
  @Nullable
  public abstract DocumentFile createDirectory(@NonNull String paramString);
  
  @Nullable
  public abstract DocumentFile createFile(@NonNull String paramString1, @NonNull String paramString2);
  
  public abstract boolean delete();
  
  public abstract boolean exists();
  
  @Nullable
  public DocumentFile findFile(@NonNull String paramString) {
    for (DocumentFile documentFile : listFiles()) {
      if (paramString.equals(documentFile.getName()))
        return documentFile; 
    } 
    return null;
  }
  
  @Nullable
  public abstract String getName();
  
  @Nullable
  public DocumentFile getParentFile() {
    return this.mParent;
  }
  
  @Nullable
  public abstract String getType();
  
  @NonNull
  public abstract Uri getUri();
  
  public abstract boolean isDirectory();
  
  public abstract boolean isFile();
  
  public abstract boolean isVirtual();
  
  public abstract long lastModified();
  
  public abstract long length();
  
  @NonNull
  public abstract DocumentFile[] listFiles();
  
  public abstract boolean renameTo(@NonNull String paramString);
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\provider\DocumentFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */