package android.support.v4.print;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class PrintHelper {
  @SuppressLint({"InlinedApi"})
  public static final int COLOR_MODE_COLOR = 2;
  
  @SuppressLint({"InlinedApi"})
  public static final int COLOR_MODE_MONOCHROME = 1;
  
  static final boolean IS_MIN_MARGINS_HANDLING_CORRECT;
  
  private static final String LOG_TAG = "PrintHelper";
  
  private static final int MAX_PRINT_SIZE = 3500;
  
  public static final int ORIENTATION_LANDSCAPE = 1;
  
  public static final int ORIENTATION_PORTRAIT = 2;
  
  static final boolean PRINT_ACTIVITY_RESPECTS_ORIENTATION;
  
  public static final int SCALE_MODE_FILL = 2;
  
  public static final int SCALE_MODE_FIT = 1;
  
  int mColorMode = 2;
  
  final Context mContext;
  
  BitmapFactory.Options mDecodeOptions = null;
  
  final Object mLock = new Object();
  
  int mOrientation = 1;
  
  int mScaleMode = 2;
  
  static {
    int i = Build.VERSION.SDK_INT;
    boolean bool1 = false;
    if (i < 20 || Build.VERSION.SDK_INT > 23) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    PRINT_ACTIVITY_RESPECTS_ORIENTATION = bool2;
    boolean bool2 = bool1;
    if (Build.VERSION.SDK_INT != 23)
      bool2 = true; 
    IS_MIN_MARGINS_HANDLING_CORRECT = bool2;
  }
  
  public PrintHelper(@NonNull Context paramContext) {
    this.mContext = paramContext;
  }
  
  static Bitmap convertBitmapForColorMode(Bitmap paramBitmap, int paramInt) {
    if (paramInt != 1)
      return paramBitmap; 
    Bitmap bitmap = Bitmap.createBitmap(paramBitmap.getWidth(), paramBitmap.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    Paint paint = new Paint();
    ColorMatrix colorMatrix = new ColorMatrix();
    colorMatrix.setSaturation(0.0F);
    paint.setColorFilter((ColorFilter)new ColorMatrixColorFilter(colorMatrix));
    canvas.drawBitmap(paramBitmap, 0.0F, 0.0F, paint);
    canvas.setBitmap(null);
    return bitmap;
  }
  
  @RequiresApi(19)
  private static PrintAttributes.Builder copyAttributes(PrintAttributes paramPrintAttributes) {
    PrintAttributes.Builder builder = (new PrintAttributes.Builder()).setMediaSize(paramPrintAttributes.getMediaSize()).setResolution(paramPrintAttributes.getResolution()).setMinMargins(paramPrintAttributes.getMinMargins());
    if (paramPrintAttributes.getColorMode() != 0)
      builder.setColorMode(paramPrintAttributes.getColorMode()); 
    if (Build.VERSION.SDK_INT >= 23 && paramPrintAttributes.getDuplexMode() != 0)
      builder.setDuplexMode(paramPrintAttributes.getDuplexMode()); 
    return builder;
  }
  
  static Matrix getMatrix(int paramInt1, int paramInt2, RectF paramRectF, int paramInt3) {
    Matrix matrix = new Matrix();
    float f1 = paramRectF.width();
    float f2 = paramInt1;
    f1 /= f2;
    if (paramInt3 == 2) {
      f1 = Math.max(f1, paramRectF.height() / paramInt2);
    } else {
      f1 = Math.min(f1, paramRectF.height() / paramInt2);
    } 
    matrix.postScale(f1, f1);
    matrix.postTranslate((paramRectF.width() - f2 * f1) / 2.0F, (paramRectF.height() - paramInt2 * f1) / 2.0F);
    return matrix;
  }
  
  static boolean isPortrait(Bitmap paramBitmap) {
    boolean bool;
    if (paramBitmap.getWidth() <= paramBitmap.getHeight()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private Bitmap loadBitmap(Uri paramUri, BitmapFactory.Options paramOptions) throws FileNotFoundException {
    if (paramUri != null && this.mContext != null) {
      BitmapFactory.Options options = null;
      try {
        InputStream inputStream = this.mContext.getContentResolver().openInputStream(paramUri);
      } finally {
        paramUri = null;
      } 
      if (paramOptions != null)
        try {
          paramOptions.close();
        } catch (IOException iOException) {
          Log.w("PrintHelper", "close fail ", iOException);
        }  
      throw paramUri;
    } 
    throw new IllegalArgumentException("bad argument to loadBitmap");
  }
  
  public static boolean systemSupportsPrint() {
    boolean bool;
    if (Build.VERSION.SDK_INT >= 19) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public int getColorMode() {
    return this.mColorMode;
  }
  
  public int getOrientation() {
    return (Build.VERSION.SDK_INT >= 19 && this.mOrientation == 0) ? 1 : this.mOrientation;
  }
  
  public int getScaleMode() {
    return this.mScaleMode;
  }
  
  Bitmap loadConstrainedBitmap(Uri paramUri) throws FileNotFoundException {
    if (paramUri != null && this.mContext != null) {
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      loadBitmap(paramUri, options);
      int i = options.outWidth;
      int j = options.outHeight;
      if (i <= 0 || j <= 0)
        return null; 
      int k = Math.max(i, j);
      int m;
      for (m = 1; k > 3500; m <<= 1)
        k >>>= 1; 
      if (m <= 0 || Math.min(i, j) / m <= 0)
        return null; 
      synchronized (this.mLock) {
        BitmapFactory.Options options1 = new BitmapFactory.Options();
        this();
        this.mDecodeOptions = options1;
        this.mDecodeOptions.inMutable = true;
        this.mDecodeOptions.inSampleSize = m;
        options1 = this.mDecodeOptions;
        try {
          null = loadBitmap(paramUri, options1);
        } finally {
          null = null;
        } 
      } 
    } 
    throw new IllegalArgumentException("bad argument to getScaledBitmap");
  }
  
  public void printBitmap(@NonNull String paramString, @NonNull Bitmap paramBitmap) {
    printBitmap(paramString, paramBitmap, (OnPrintFinishCallback)null);
  }
  
  public void printBitmap(@NonNull String paramString, @NonNull Bitmap paramBitmap, @Nullable OnPrintFinishCallback paramOnPrintFinishCallback) {
    PrintAttributes.MediaSize mediaSize;
    if (Build.VERSION.SDK_INT < 19 || paramBitmap == null)
      return; 
    PrintManager printManager = (PrintManager)this.mContext.getSystemService("print");
    if (isPortrait(paramBitmap)) {
      mediaSize = PrintAttributes.MediaSize.UNKNOWN_PORTRAIT;
    } else {
      mediaSize = PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE;
    } 
    PrintAttributes printAttributes = (new PrintAttributes.Builder()).setMediaSize(mediaSize).setColorMode(this.mColorMode).build();
    printManager.print(paramString, new PrintBitmapAdapter(paramString, this.mScaleMode, paramBitmap, paramOnPrintFinishCallback), printAttributes);
  }
  
  public void printBitmap(@NonNull String paramString, @NonNull Uri paramUri) throws FileNotFoundException {
    printBitmap(paramString, paramUri, (OnPrintFinishCallback)null);
  }
  
  public void printBitmap(@NonNull String paramString, @NonNull Uri paramUri, @Nullable OnPrintFinishCallback paramOnPrintFinishCallback) throws FileNotFoundException {
    if (Build.VERSION.SDK_INT < 19)
      return; 
    PrintUriAdapter printUriAdapter = new PrintUriAdapter(paramString, paramUri, paramOnPrintFinishCallback, this.mScaleMode);
    PrintManager printManager = (PrintManager)this.mContext.getSystemService("print");
    PrintAttributes.Builder builder = new PrintAttributes.Builder();
    builder.setColorMode(this.mColorMode);
    if (this.mOrientation == 1 || this.mOrientation == 0) {
      builder.setMediaSize(PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE);
    } else if (this.mOrientation == 2) {
      builder.setMediaSize(PrintAttributes.MediaSize.UNKNOWN_PORTRAIT);
    } 
    printManager.print(paramString, printUriAdapter, builder.build());
  }
  
  public void setColorMode(int paramInt) {
    this.mColorMode = paramInt;
  }
  
  public void setOrientation(int paramInt) {
    this.mOrientation = paramInt;
  }
  
  public void setScaleMode(int paramInt) {
    this.mScaleMode = paramInt;
  }
  
  @RequiresApi(19)
  void writeBitmap(final PrintAttributes attributes, final int fittingMode, final Bitmap bitmap, final ParcelFileDescriptor fileDescriptor, final CancellationSignal cancellationSignal, final PrintDocumentAdapter.WriteResultCallback writeResultCallback) {
    final PrintAttributes pdfAttributes;
    if (IS_MIN_MARGINS_HANDLING_CORRECT) {
      printAttributes = attributes;
    } else {
      printAttributes = copyAttributes(attributes).setMinMargins(new PrintAttributes.Margins(0, 0, 0, 0)).build();
    } 
    (new AsyncTask<Void, Void, Throwable>() {
        protected Throwable doInBackground(Void... param1VarArgs) {
          try {
            if (cancellationSignal.isCanceled())
              return null; 
            PrintedPdfDocument printedPdfDocument = new PrintedPdfDocument();
            this(PrintHelper.this.mContext, pdfAttributes);
            Bitmap bitmap = PrintHelper.convertBitmapForColorMode(bitmap, pdfAttributes.getColorMode());
            boolean bool = cancellationSignal.isCanceled();
            if (bool)
              return null; 
            try {
              RectF rectF;
              PdfDocument.Page page = printedPdfDocument.startPage(1);
              if (PrintHelper.IS_MIN_MARGINS_HANDLING_CORRECT) {
                rectF = new RectF();
                this(page.getInfo().getContentRect());
              } else {
                PrintedPdfDocument printedPdfDocument1 = new PrintedPdfDocument();
                this(PrintHelper.this.mContext, attributes);
                PdfDocument.Page page1 = printedPdfDocument1.startPage(1);
                rectF = new RectF();
                this(page1.getInfo().getContentRect());
                printedPdfDocument1.finishPage(page1);
                printedPdfDocument1.close();
              } 
              Matrix matrix = PrintHelper.getMatrix(bitmap.getWidth(), bitmap.getHeight(), rectF, fittingMode);
              if (!PrintHelper.IS_MIN_MARGINS_HANDLING_CORRECT) {
                matrix.postTranslate(rectF.left, rectF.top);
                page.getCanvas().clipRect(rectF);
              } 
              page.getCanvas().drawBitmap(bitmap, matrix, null);
              printedPdfDocument.finishPage(page);
              bool = cancellationSignal.isCanceled();
              if (bool)
                return null; 
              FileOutputStream fileOutputStream = new FileOutputStream();
              this(fileDescriptor.getFileDescriptor());
              printedPdfDocument.writeTo(fileOutputStream);
              return null;
            } finally {
              printedPdfDocument.close();
              ParcelFileDescriptor parcelFileDescriptor = fileDescriptor;
              if (parcelFileDescriptor != null)
                try {
                  fileDescriptor.close();
                } catch (IOException iOException) {} 
              if (bitmap != bitmap)
                bitmap.recycle(); 
            } 
          } catch (Throwable null) {
            return null;
          } 
        }
        
        protected void onPostExecute(Throwable param1Throwable) {
          if (cancellationSignal.isCanceled()) {
            writeResultCallback.onWriteCancelled();
          } else if (param1Throwable == null) {
            writeResultCallback.onWriteFinished(new PageRange[] { PageRange.ALL_PAGES });
          } else {
            Log.e("PrintHelper", "Error writing printed content", param1Throwable);
            writeResultCallback.onWriteFailed(null);
          } 
        }
      }).execute((Object[])new Void[0]);
  }
  
  public static interface OnPrintFinishCallback {
    void onFinish();
  }
  
  @RequiresApi(19)
  private class PrintBitmapAdapter extends PrintDocumentAdapter {
    private PrintAttributes mAttributes;
    
    private final Bitmap mBitmap;
    
    private final PrintHelper.OnPrintFinishCallback mCallback;
    
    private final int mFittingMode;
    
    private final String mJobName;
    
    PrintBitmapAdapter(String param1String, int param1Int, Bitmap param1Bitmap, PrintHelper.OnPrintFinishCallback param1OnPrintFinishCallback) {
      this.mJobName = param1String;
      this.mFittingMode = param1Int;
      this.mBitmap = param1Bitmap;
      this.mCallback = param1OnPrintFinishCallback;
    }
    
    public void onFinish() {
      if (this.mCallback != null)
        this.mCallback.onFinish(); 
    }
    
    public void onLayout(PrintAttributes param1PrintAttributes1, PrintAttributes param1PrintAttributes2, CancellationSignal param1CancellationSignal, PrintDocumentAdapter.LayoutResultCallback param1LayoutResultCallback, Bundle param1Bundle) {
      this.mAttributes = param1PrintAttributes2;
      param1LayoutResultCallback.onLayoutFinished((new PrintDocumentInfo.Builder(this.mJobName)).setContentType(1).setPageCount(1).build(), param1PrintAttributes2.equals(param1PrintAttributes1) ^ true);
    }
    
    public void onWrite(PageRange[] param1ArrayOfPageRange, ParcelFileDescriptor param1ParcelFileDescriptor, CancellationSignal param1CancellationSignal, PrintDocumentAdapter.WriteResultCallback param1WriteResultCallback) {
      PrintHelper.this.writeBitmap(this.mAttributes, this.mFittingMode, this.mBitmap, param1ParcelFileDescriptor, param1CancellationSignal, param1WriteResultCallback);
    }
  }
  
  @RequiresApi(19)
  private class PrintUriAdapter extends PrintDocumentAdapter {
    PrintAttributes mAttributes;
    
    Bitmap mBitmap;
    
    final PrintHelper.OnPrintFinishCallback mCallback;
    
    final int mFittingMode;
    
    final Uri mImageFile;
    
    final String mJobName;
    
    AsyncTask<Uri, Boolean, Bitmap> mLoadBitmap;
    
    PrintUriAdapter(String param1String, Uri param1Uri, PrintHelper.OnPrintFinishCallback param1OnPrintFinishCallback, int param1Int) {
      this.mJobName = param1String;
      this.mImageFile = param1Uri;
      this.mCallback = param1OnPrintFinishCallback;
      this.mFittingMode = param1Int;
      this.mBitmap = null;
    }
    
    void cancelLoad() {
      synchronized (PrintHelper.this.mLock) {
        if (PrintHelper.this.mDecodeOptions != null) {
          if (Build.VERSION.SDK_INT < 24)
            PrintHelper.this.mDecodeOptions.requestCancelDecode(); 
          PrintHelper.this.mDecodeOptions = null;
        } 
        return;
      } 
    }
    
    public void onFinish() {
      super.onFinish();
      cancelLoad();
      if (this.mLoadBitmap != null)
        this.mLoadBitmap.cancel(true); 
      if (this.mCallback != null)
        this.mCallback.onFinish(); 
      if (this.mBitmap != null) {
        this.mBitmap.recycle();
        this.mBitmap = null;
      } 
    }
    
    public void onLayout(PrintAttributes param1PrintAttributes1, PrintAttributes param1PrintAttributes2, CancellationSignal param1CancellationSignal, PrintDocumentAdapter.LayoutResultCallback param1LayoutResultCallback, Bundle param1Bundle) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: aload_2
      //   4: putfield mAttributes : Landroid/print/PrintAttributes;
      //   7: aload_0
      //   8: monitorexit
      //   9: aload_3
      //   10: invokevirtual isCanceled : ()Z
      //   13: ifeq -> 22
      //   16: aload #4
      //   18: invokevirtual onLayoutCancelled : ()V
      //   21: return
      //   22: aload_0
      //   23: getfield mBitmap : Landroid/graphics/Bitmap;
      //   26: ifnull -> 64
      //   29: aload #4
      //   31: new android/print/PrintDocumentInfo$Builder
      //   34: dup
      //   35: aload_0
      //   36: getfield mJobName : Ljava/lang/String;
      //   39: invokespecial <init> : (Ljava/lang/String;)V
      //   42: iconst_1
      //   43: invokevirtual setContentType : (I)Landroid/print/PrintDocumentInfo$Builder;
      //   46: iconst_1
      //   47: invokevirtual setPageCount : (I)Landroid/print/PrintDocumentInfo$Builder;
      //   50: invokevirtual build : ()Landroid/print/PrintDocumentInfo;
      //   53: aload_2
      //   54: aload_1
      //   55: invokevirtual equals : (Ljava/lang/Object;)Z
      //   58: iconst_1
      //   59: ixor
      //   60: invokevirtual onLayoutFinished : (Landroid/print/PrintDocumentInfo;Z)V
      //   63: return
      //   64: aload_0
      //   65: new android/support/v4/print/PrintHelper$PrintUriAdapter$1
      //   68: dup
      //   69: aload_0
      //   70: aload_3
      //   71: aload_2
      //   72: aload_1
      //   73: aload #4
      //   75: invokespecial <init> : (Landroid/support/v4/print/PrintHelper$PrintUriAdapter;Landroid/os/CancellationSignal;Landroid/print/PrintAttributes;Landroid/print/PrintAttributes;Landroid/print/PrintDocumentAdapter$LayoutResultCallback;)V
      //   78: iconst_0
      //   79: anewarray android/net/Uri
      //   82: invokevirtual execute : ([Ljava/lang/Object;)Landroid/os/AsyncTask;
      //   85: putfield mLoadBitmap : Landroid/os/AsyncTask;
      //   88: return
      //   89: astore_1
      //   90: aload_0
      //   91: monitorexit
      //   92: aload_1
      //   93: athrow
      // Exception table:
      //   from	to	target	type
      //   2	9	89	finally
      //   90	92	89	finally
    }
    
    public void onWrite(PageRange[] param1ArrayOfPageRange, ParcelFileDescriptor param1ParcelFileDescriptor, CancellationSignal param1CancellationSignal, PrintDocumentAdapter.WriteResultCallback param1WriteResultCallback) {
      PrintHelper.this.writeBitmap(this.mAttributes, this.mFittingMode, this.mBitmap, param1ParcelFileDescriptor, param1CancellationSignal, param1WriteResultCallback);
    }
  }
  
  class null extends AsyncTask<Uri, Boolean, Bitmap> {
    protected Bitmap doInBackground(Uri... param1VarArgs) {
      try {
        return PrintHelper.this.loadConstrainedBitmap(this.this$1.mImageFile);
      } catch (FileNotFoundException fileNotFoundException) {
        return null;
      } 
    }
    
    protected void onCancelled(Bitmap param1Bitmap) {
      layoutResultCallback.onLayoutCancelled();
      this.this$1.mLoadBitmap = null;
    }
    
    protected void onPostExecute(Bitmap param1Bitmap) {
      // Byte code:
      //   0: aload_0
      //   1: aload_1
      //   2: invokespecial onPostExecute : (Ljava/lang/Object;)V
      //   5: aload_1
      //   6: astore_2
      //   7: aload_1
      //   8: ifnull -> 106
      //   11: getstatic android/support/v4/print/PrintHelper.PRINT_ACTIVITY_RESPECTS_ORIENTATION : Z
      //   14: ifeq -> 32
      //   17: aload_1
      //   18: astore_2
      //   19: aload_0
      //   20: getfield this$1 : Landroid/support/v4/print/PrintHelper$PrintUriAdapter;
      //   23: getfield this$0 : Landroid/support/v4/print/PrintHelper;
      //   26: getfield mOrientation : I
      //   29: ifne -> 106
      //   32: aload_0
      //   33: monitorenter
      //   34: aload_0
      //   35: getfield this$1 : Landroid/support/v4/print/PrintHelper$PrintUriAdapter;
      //   38: getfield mAttributes : Landroid/print/PrintAttributes;
      //   41: invokevirtual getMediaSize : ()Landroid/print/PrintAttributes$MediaSize;
      //   44: astore_3
      //   45: aload_0
      //   46: monitorexit
      //   47: aload_1
      //   48: astore_2
      //   49: aload_3
      //   50: ifnull -> 106
      //   53: aload_1
      //   54: astore_2
      //   55: aload_3
      //   56: invokevirtual isPortrait : ()Z
      //   59: aload_1
      //   60: invokestatic isPortrait : (Landroid/graphics/Bitmap;)Z
      //   63: if_icmpeq -> 106
      //   66: new android/graphics/Matrix
      //   69: dup
      //   70: invokespecial <init> : ()V
      //   73: astore_2
      //   74: aload_2
      //   75: ldc 90.0
      //   77: invokevirtual postRotate : (F)Z
      //   80: pop
      //   81: aload_1
      //   82: iconst_0
      //   83: iconst_0
      //   84: aload_1
      //   85: invokevirtual getWidth : ()I
      //   88: aload_1
      //   89: invokevirtual getHeight : ()I
      //   92: aload_2
      //   93: iconst_1
      //   94: invokestatic createBitmap : (Landroid/graphics/Bitmap;IIIILandroid/graphics/Matrix;Z)Landroid/graphics/Bitmap;
      //   97: astore_2
      //   98: goto -> 106
      //   101: astore_1
      //   102: aload_0
      //   103: monitorexit
      //   104: aload_1
      //   105: athrow
      //   106: aload_0
      //   107: getfield this$1 : Landroid/support/v4/print/PrintHelper$PrintUriAdapter;
      //   110: aload_2
      //   111: putfield mBitmap : Landroid/graphics/Bitmap;
      //   114: aload_2
      //   115: ifnull -> 172
      //   118: new android/print/PrintDocumentInfo$Builder
      //   121: dup
      //   122: aload_0
      //   123: getfield this$1 : Landroid/support/v4/print/PrintHelper$PrintUriAdapter;
      //   126: getfield mJobName : Ljava/lang/String;
      //   129: invokespecial <init> : (Ljava/lang/String;)V
      //   132: iconst_1
      //   133: invokevirtual setContentType : (I)Landroid/print/PrintDocumentInfo$Builder;
      //   136: iconst_1
      //   137: invokevirtual setPageCount : (I)Landroid/print/PrintDocumentInfo$Builder;
      //   140: invokevirtual build : ()Landroid/print/PrintDocumentInfo;
      //   143: astore_1
      //   144: aload_0
      //   145: getfield val$newPrintAttributes : Landroid/print/PrintAttributes;
      //   148: aload_0
      //   149: getfield val$oldPrintAttributes : Landroid/print/PrintAttributes;
      //   152: invokevirtual equals : (Ljava/lang/Object;)Z
      //   155: istore #4
      //   157: aload_0
      //   158: getfield val$layoutResultCallback : Landroid/print/PrintDocumentAdapter$LayoutResultCallback;
      //   161: aload_1
      //   162: iconst_1
      //   163: iload #4
      //   165: ixor
      //   166: invokevirtual onLayoutFinished : (Landroid/print/PrintDocumentInfo;Z)V
      //   169: goto -> 180
      //   172: aload_0
      //   173: getfield val$layoutResultCallback : Landroid/print/PrintDocumentAdapter$LayoutResultCallback;
      //   176: aconst_null
      //   177: invokevirtual onLayoutFailed : (Ljava/lang/CharSequence;)V
      //   180: aload_0
      //   181: getfield this$1 : Landroid/support/v4/print/PrintHelper$PrintUriAdapter;
      //   184: aconst_null
      //   185: putfield mLoadBitmap : Landroid/os/AsyncTask;
      //   188: return
      // Exception table:
      //   from	to	target	type
      //   34	47	101	finally
      //   102	104	101	finally
    }
    
    protected void onPreExecute() {
      cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            public void onCancel() {
              this.this$2.this$1.cancelLoad();
              PrintHelper.PrintUriAdapter.null.this.cancel(false);
            }
          });
    }
  }
  
  class null implements CancellationSignal.OnCancelListener {
    public void onCancel() {
      this.this$2.this$1.cancelLoad();
      this.this$2.cancel(false);
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\print\PrintHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */