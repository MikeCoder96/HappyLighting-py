package android.support.v4.provider;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.Handler;
import android.provider.BaseColumns;
import android.support.annotation.GuardedBy;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.TypefaceCompat;
import android.support.v4.graphics.TypefaceCompatUtil;
import android.support.v4.util.LruCache;
import android.support.v4.util.Preconditions;
import android.support.v4.util.SimpleArrayMap;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class FontsContractCompat {
  private static final int BACKGROUND_THREAD_KEEP_ALIVE_DURATION_MS = 10000;
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static final String PARCEL_FONT_RESULTS = "font_results";
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  static final int RESULT_CODE_PROVIDER_NOT_FOUND = -1;
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  static final int RESULT_CODE_WRONG_CERTIFICATES = -2;
  
  private static final String TAG = "FontsContractCompat";
  
  private static final SelfDestructiveThread sBackgroundThread;
  
  private static final Comparator<byte[]> sByteArrayComparator;
  
  static final Object sLock;
  
  @GuardedBy("sLock")
  static final SimpleArrayMap<String, ArrayList<SelfDestructiveThread.ReplyCallback<TypefaceResult>>> sPendingReplies;
  
  static final LruCache<String, Typeface> sTypefaceCache = new LruCache(16);
  
  static {
    sBackgroundThread = new SelfDestructiveThread("fonts", 10, 10000);
    sLock = new Object();
    sPendingReplies = new SimpleArrayMap();
    sByteArrayComparator = new Comparator<byte[]>() {
        public int compare(byte[] param1ArrayOfbyte1, byte[] param1ArrayOfbyte2) {
          if (param1ArrayOfbyte1.length != param1ArrayOfbyte2.length)
            return param1ArrayOfbyte1.length - param1ArrayOfbyte2.length; 
          for (byte b = 0; b < param1ArrayOfbyte1.length; b++) {
            if (param1ArrayOfbyte1[b] != param1ArrayOfbyte2[b])
              return param1ArrayOfbyte1[b] - param1ArrayOfbyte2[b]; 
          } 
          return 0;
        }
      };
  }
  
  @Nullable
  public static Typeface buildTypeface(@NonNull Context paramContext, @Nullable CancellationSignal paramCancellationSignal, @NonNull FontInfo[] paramArrayOfFontInfo) {
    return TypefaceCompat.createFromFontInfo(paramContext, paramCancellationSignal, paramArrayOfFontInfo, 0);
  }
  
  private static List<byte[]> convertToByteArrayList(Signature[] paramArrayOfSignature) {
    ArrayList<byte[]> arrayList = new ArrayList();
    for (byte b = 0; b < paramArrayOfSignature.length; b++)
      arrayList.add(paramArrayOfSignature[b].toByteArray()); 
    return (List<byte[]>)arrayList;
  }
  
  private static boolean equalsByteArrayList(List<byte[]> paramList1, List<byte[]> paramList2) {
    if (paramList1.size() != paramList2.size())
      return false; 
    for (byte b = 0; b < paramList1.size(); b++) {
      if (!Arrays.equals(paramList1.get(b), paramList2.get(b)))
        return false; 
    } 
    return true;
  }
  
  @NonNull
  public static FontFamilyResult fetchFonts(@NonNull Context paramContext, @Nullable CancellationSignal paramCancellationSignal, @NonNull FontRequest paramFontRequest) throws PackageManager.NameNotFoundException {
    ProviderInfo providerInfo = getProvider(paramContext.getPackageManager(), paramFontRequest, paramContext.getResources());
    return (providerInfo == null) ? new FontFamilyResult(1, null) : new FontFamilyResult(0, getFontFromProvider(paramContext, paramFontRequest, providerInfo.authority, paramCancellationSignal));
  }
  
  private static List<List<byte[]>> getCertificates(FontRequest paramFontRequest, Resources paramResources) {
    return (paramFontRequest.getCertificates() != null) ? paramFontRequest.getCertificates() : FontResourcesParserCompat.readCerts(paramResources, paramFontRequest.getCertificatesArrayResId());
  }
  
  @NonNull
  @VisibleForTesting
  static FontInfo[] getFontFromProvider(Context paramContext, FontRequest paramFontRequest, String paramString, CancellationSignal paramCancellationSignal) {
    // Byte code:
    //   0: new java/util/ArrayList
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore #4
    //   9: new android/net/Uri$Builder
    //   12: dup
    //   13: invokespecial <init> : ()V
    //   16: ldc 'content'
    //   18: invokevirtual scheme : (Ljava/lang/String;)Landroid/net/Uri$Builder;
    //   21: aload_2
    //   22: invokevirtual authority : (Ljava/lang/String;)Landroid/net/Uri$Builder;
    //   25: invokevirtual build : ()Landroid/net/Uri;
    //   28: astore #5
    //   30: new android/net/Uri$Builder
    //   33: dup
    //   34: invokespecial <init> : ()V
    //   37: ldc 'content'
    //   39: invokevirtual scheme : (Ljava/lang/String;)Landroid/net/Uri$Builder;
    //   42: aload_2
    //   43: invokevirtual authority : (Ljava/lang/String;)Landroid/net/Uri$Builder;
    //   46: ldc 'file'
    //   48: invokevirtual appendPath : (Ljava/lang/String;)Landroid/net/Uri$Builder;
    //   51: invokevirtual build : ()Landroid/net/Uri;
    //   54: astore #6
    //   56: aconst_null
    //   57: astore #7
    //   59: aload #7
    //   61: astore_2
    //   62: getstatic android/os/Build$VERSION.SDK_INT : I
    //   65: bipush #16
    //   67: if_icmple -> 157
    //   70: aload #7
    //   72: astore_2
    //   73: aload_0
    //   74: invokevirtual getContentResolver : ()Landroid/content/ContentResolver;
    //   77: astore_0
    //   78: aload #7
    //   80: astore_2
    //   81: aload_1
    //   82: invokevirtual getQuery : ()Ljava/lang/String;
    //   85: astore_1
    //   86: aload #7
    //   88: astore_2
    //   89: aload_0
    //   90: aload #5
    //   92: bipush #7
    //   94: anewarray java/lang/String
    //   97: dup
    //   98: iconst_0
    //   99: ldc '_id'
    //   101: aastore
    //   102: dup
    //   103: iconst_1
    //   104: ldc 'file_id'
    //   106: aastore
    //   107: dup
    //   108: iconst_2
    //   109: ldc 'font_ttc_index'
    //   111: aastore
    //   112: dup
    //   113: iconst_3
    //   114: ldc_w 'font_variation_settings'
    //   117: aastore
    //   118: dup
    //   119: iconst_4
    //   120: ldc_w 'font_weight'
    //   123: aastore
    //   124: dup
    //   125: iconst_5
    //   126: ldc_w 'font_italic'
    //   129: aastore
    //   130: dup
    //   131: bipush #6
    //   133: ldc_w 'result_code'
    //   136: aastore
    //   137: ldc_w 'query = ?'
    //   140: iconst_1
    //   141: anewarray java/lang/String
    //   144: dup
    //   145: iconst_0
    //   146: aload_1
    //   147: aastore
    //   148: aconst_null
    //   149: aload_3
    //   150: invokevirtual query : (Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/database/Cursor;
    //   153: astore_0
    //   154: goto -> 243
    //   157: aload #7
    //   159: astore_2
    //   160: aload_0
    //   161: invokevirtual getContentResolver : ()Landroid/content/ContentResolver;
    //   164: astore_0
    //   165: aload #7
    //   167: astore_2
    //   168: aload_1
    //   169: invokevirtual getQuery : ()Ljava/lang/String;
    //   172: astore_1
    //   173: aload #7
    //   175: astore_2
    //   176: aload_0
    //   177: aload #5
    //   179: bipush #7
    //   181: anewarray java/lang/String
    //   184: dup
    //   185: iconst_0
    //   186: ldc '_id'
    //   188: aastore
    //   189: dup
    //   190: iconst_1
    //   191: ldc 'file_id'
    //   193: aastore
    //   194: dup
    //   195: iconst_2
    //   196: ldc 'font_ttc_index'
    //   198: aastore
    //   199: dup
    //   200: iconst_3
    //   201: ldc_w 'font_variation_settings'
    //   204: aastore
    //   205: dup
    //   206: iconst_4
    //   207: ldc_w 'font_weight'
    //   210: aastore
    //   211: dup
    //   212: iconst_5
    //   213: ldc_w 'font_italic'
    //   216: aastore
    //   217: dup
    //   218: bipush #6
    //   220: ldc_w 'result_code'
    //   223: aastore
    //   224: ldc_w 'query = ?'
    //   227: iconst_1
    //   228: anewarray java/lang/String
    //   231: dup
    //   232: iconst_0
    //   233: aload_1
    //   234: aastore
    //   235: aconst_null
    //   236: invokevirtual query : (Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   239: astore_0
    //   240: goto -> 154
    //   243: aload #4
    //   245: astore_1
    //   246: aload_0
    //   247: ifnull -> 546
    //   250: aload #4
    //   252: astore_1
    //   253: aload_0
    //   254: astore_2
    //   255: aload_0
    //   256: invokeinterface getCount : ()I
    //   261: ifle -> 546
    //   264: aload_0
    //   265: astore_2
    //   266: aload_0
    //   267: ldc_w 'result_code'
    //   270: invokeinterface getColumnIndex : (Ljava/lang/String;)I
    //   275: istore #8
    //   277: aload_0
    //   278: astore_2
    //   279: new java/util/ArrayList
    //   282: astore_3
    //   283: aload_0
    //   284: astore_2
    //   285: aload_3
    //   286: invokespecial <init> : ()V
    //   289: aload_0
    //   290: astore_2
    //   291: aload_0
    //   292: ldc '_id'
    //   294: invokeinterface getColumnIndex : (Ljava/lang/String;)I
    //   299: istore #9
    //   301: aload_0
    //   302: astore_2
    //   303: aload_0
    //   304: ldc 'file_id'
    //   306: invokeinterface getColumnIndex : (Ljava/lang/String;)I
    //   311: istore #10
    //   313: aload_0
    //   314: astore_2
    //   315: aload_0
    //   316: ldc 'font_ttc_index'
    //   318: invokeinterface getColumnIndex : (Ljava/lang/String;)I
    //   323: istore #11
    //   325: aload_0
    //   326: astore_2
    //   327: aload_0
    //   328: ldc_w 'font_weight'
    //   331: invokeinterface getColumnIndex : (Ljava/lang/String;)I
    //   336: istore #12
    //   338: aload_0
    //   339: astore_2
    //   340: aload_0
    //   341: ldc_w 'font_italic'
    //   344: invokeinterface getColumnIndex : (Ljava/lang/String;)I
    //   349: istore #13
    //   351: aload_0
    //   352: astore_2
    //   353: aload_0
    //   354: invokeinterface moveToNext : ()Z
    //   359: ifeq -> 544
    //   362: iload #8
    //   364: iconst_m1
    //   365: if_icmpeq -> 383
    //   368: aload_0
    //   369: astore_2
    //   370: aload_0
    //   371: iload #8
    //   373: invokeinterface getInt : (I)I
    //   378: istore #14
    //   380: goto -> 386
    //   383: iconst_0
    //   384: istore #14
    //   386: iload #11
    //   388: iconst_m1
    //   389: if_icmpeq -> 407
    //   392: aload_0
    //   393: astore_2
    //   394: aload_0
    //   395: iload #11
    //   397: invokeinterface getInt : (I)I
    //   402: istore #15
    //   404: goto -> 410
    //   407: iconst_0
    //   408: istore #15
    //   410: iload #10
    //   412: iconst_m1
    //   413: if_icmpne -> 435
    //   416: aload_0
    //   417: astore_2
    //   418: aload #5
    //   420: aload_0
    //   421: iload #9
    //   423: invokeinterface getLong : (I)J
    //   428: invokestatic withAppendedId : (Landroid/net/Uri;J)Landroid/net/Uri;
    //   431: astore_1
    //   432: goto -> 454
    //   435: aload_0
    //   436: astore_2
    //   437: aload #6
    //   439: aload_0
    //   440: iload #10
    //   442: invokeinterface getLong : (I)J
    //   447: invokestatic withAppendedId : (Landroid/net/Uri;J)Landroid/net/Uri;
    //   450: astore_1
    //   451: goto -> 432
    //   454: iload #12
    //   456: iconst_m1
    //   457: if_icmpeq -> 475
    //   460: aload_0
    //   461: astore_2
    //   462: aload_0
    //   463: iload #12
    //   465: invokeinterface getInt : (I)I
    //   470: istore #16
    //   472: goto -> 480
    //   475: sipush #400
    //   478: istore #16
    //   480: iload #13
    //   482: iconst_m1
    //   483: if_icmpeq -> 506
    //   486: aload_0
    //   487: astore_2
    //   488: aload_0
    //   489: iload #13
    //   491: invokeinterface getInt : (I)I
    //   496: iconst_1
    //   497: if_icmpne -> 506
    //   500: iconst_1
    //   501: istore #17
    //   503: goto -> 509
    //   506: iconst_0
    //   507: istore #17
    //   509: aload_0
    //   510: astore_2
    //   511: new android/support/v4/provider/FontsContractCompat$FontInfo
    //   514: astore #4
    //   516: aload_0
    //   517: astore_2
    //   518: aload #4
    //   520: aload_1
    //   521: iload #15
    //   523: iload #16
    //   525: iload #17
    //   527: iload #14
    //   529: invokespecial <init> : (Landroid/net/Uri;IIZI)V
    //   532: aload_0
    //   533: astore_2
    //   534: aload_3
    //   535: aload #4
    //   537: invokevirtual add : (Ljava/lang/Object;)Z
    //   540: pop
    //   541: goto -> 351
    //   544: aload_3
    //   545: astore_1
    //   546: aload_0
    //   547: ifnull -> 556
    //   550: aload_0
    //   551: invokeinterface close : ()V
    //   556: aload_1
    //   557: iconst_0
    //   558: anewarray android/support/v4/provider/FontsContractCompat$FontInfo
    //   561: invokevirtual toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   564: checkcast [Landroid/support/v4/provider/FontsContractCompat$FontInfo;
    //   567: areturn
    //   568: astore_0
    //   569: aload_2
    //   570: ifnull -> 579
    //   573: aload_2
    //   574: invokeinterface close : ()V
    //   579: aload_0
    //   580: athrow
    // Exception table:
    //   from	to	target	type
    //   62	70	568	finally
    //   73	78	568	finally
    //   81	86	568	finally
    //   89	154	568	finally
    //   160	165	568	finally
    //   168	173	568	finally
    //   176	240	568	finally
    //   255	264	568	finally
    //   266	277	568	finally
    //   279	283	568	finally
    //   285	289	568	finally
    //   291	301	568	finally
    //   303	313	568	finally
    //   315	325	568	finally
    //   327	338	568	finally
    //   340	351	568	finally
    //   353	362	568	finally
    //   370	380	568	finally
    //   394	404	568	finally
    //   418	432	568	finally
    //   437	451	568	finally
    //   462	472	568	finally
    //   488	500	568	finally
    //   511	516	568	finally
    //   518	532	568	finally
    //   534	541	568	finally
  }
  
  @NonNull
  static TypefaceResult getFontInternal(Context paramContext, FontRequest paramFontRequest, int paramInt) {
    try {
      FontFamilyResult fontFamilyResult = fetchFonts(paramContext, null, paramFontRequest);
      int i = fontFamilyResult.getStatusCode();
      byte b = -3;
      if (i == 0) {
        Typeface typeface = TypefaceCompat.createFromFontInfo(paramContext, null, fontFamilyResult.getFonts(), paramInt);
        if (typeface != null)
          b = 0; 
        return new TypefaceResult(typeface, b);
      } 
      if (fontFamilyResult.getStatusCode() == 1)
        b = -2; 
      return new TypefaceResult(null, b);
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      return new TypefaceResult(null, -1);
    } 
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static Typeface getFontSync(Context paramContext, final FontRequest request, @Nullable final ResourcesCompat.FontCallback fontCallback, @Nullable final Handler handler, boolean paramBoolean, int paramInt1, final int style) {
    final TypefaceResult context;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(request.getIdentifier());
    stringBuilder.append("-");
    stringBuilder.append(style);
    final String id = stringBuilder.toString();
    Typeface typeface = (Typeface)sTypefaceCache.get(str);
    if (typeface != null) {
      if (fontCallback != null)
        fontCallback.onFontRetrieved(typeface); 
      return typeface;
    } 
    if (paramBoolean && paramInt1 == -1) {
      typefaceResult = getFontInternal(paramContext, request, style);
      if (fontCallback != null)
        if (typefaceResult.mResult == 0) {
          fontCallback.callbackSuccessAsync(typefaceResult.mTypeface, handler);
        } else {
          fontCallback.callbackFailAsync(typefaceResult.mResult, handler);
        }  
      return typefaceResult.mTypeface;
    } 
    Callable<TypefaceResult> callable = new Callable<TypefaceResult>() {
        public FontsContractCompat.TypefaceResult call() throws Exception {
          FontsContractCompat.TypefaceResult typefaceResult = FontsContractCompat.getFontInternal(context, request, style);
          if (typefaceResult.mTypeface != null)
            FontsContractCompat.sTypefaceCache.put(id, typefaceResult.mTypeface); 
          return typefaceResult;
        }
      };
    if (paramBoolean)
      try {
        return ((TypefaceResult)sBackgroundThread.postAndWait((Callable)callable, paramInt1)).mTypeface;
      } catch (InterruptedException interruptedException) {
        return null;
      }  
    if (fontCallback == null) {
      typefaceResult = null;
    } else {
      null = new SelfDestructiveThread.ReplyCallback<TypefaceResult>() {
          public void onReply(FontsContractCompat.TypefaceResult param1TypefaceResult) {
            if (param1TypefaceResult == null) {
              fontCallback.callbackFailAsync(1, handler);
            } else if (param1TypefaceResult.mResult == 0) {
              fontCallback.callbackSuccessAsync(param1TypefaceResult.mTypeface, handler);
            } else {
              fontCallback.callbackFailAsync(param1TypefaceResult.mResult, handler);
            } 
          }
        };
    } 
    synchronized (sLock) {
      if (sPendingReplies.containsKey(str)) {
        if (null != null)
          ((ArrayList<SelfDestructiveThread.ReplyCallback<TypefaceResult>>)sPendingReplies.get(str)).add(null); 
        return null;
      } 
      if (null != null) {
        ArrayList<SelfDestructiveThread.ReplyCallback<TypefaceResult>> arrayList = new ArrayList();
        this();
        arrayList.add(null);
        sPendingReplies.put(str, arrayList);
      } 
      sBackgroundThread.postAndReply(callable, new SelfDestructiveThread.ReplyCallback<TypefaceResult>() {
            public void onReply(FontsContractCompat.TypefaceResult param1TypefaceResult) {
              synchronized (FontsContractCompat.sLock) {
                ArrayList<SelfDestructiveThread.ReplyCallback<FontsContractCompat.TypefaceResult>> arrayList = (ArrayList)FontsContractCompat.sPendingReplies.get(id);
                if (arrayList == null)
                  return; 
                FontsContractCompat.sPendingReplies.remove(id);
                for (byte b = 0; b < arrayList.size(); b++)
                  ((SelfDestructiveThread.ReplyCallback<FontsContractCompat.TypefaceResult>)arrayList.get(b)).onReply(param1TypefaceResult); 
                return;
              } 
            }
          });
      return null;
    } 
  }
  
  @Nullable
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  @VisibleForTesting
  public static ProviderInfo getProvider(@NonNull PackageManager paramPackageManager, @NonNull FontRequest paramFontRequest, @Nullable Resources paramResources) throws PackageManager.NameNotFoundException {
    String str = paramFontRequest.getProviderAuthority();
    byte b = 0;
    ProviderInfo providerInfo = paramPackageManager.resolveContentProvider(str, 0);
    if (providerInfo != null) {
      ArrayList<byte> arrayList;
      if (providerInfo.packageName.equals(paramFontRequest.getProviderPackage())) {
        List<byte[]> list = convertToByteArrayList((paramPackageManager.getPackageInfo(providerInfo.packageName, 64)).signatures);
        Collections.sort((List)list, (Comparator)sByteArrayComparator);
        List<List<byte[]>> list1 = getCertificates(paramFontRequest, paramResources);
        while (b < list1.size()) {
          arrayList = new ArrayList(list1.get(b));
          Collections.sort(arrayList, (Comparator)sByteArrayComparator);
          if (equalsByteArrayList(list, (List)arrayList))
            return providerInfo; 
          b++;
        } 
        return null;
      } 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Found content provider ");
      stringBuilder1.append(str);
      stringBuilder1.append(", but package was not ");
      stringBuilder1.append(arrayList.getProviderPackage());
      throw new PackageManager.NameNotFoundException(stringBuilder1.toString());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("No package found for authority: ");
    stringBuilder.append(str);
    throw new PackageManager.NameNotFoundException(stringBuilder.toString());
  }
  
  @RequiresApi(19)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static Map<Uri, ByteBuffer> prepareFontData(Context paramContext, FontInfo[] paramArrayOfFontInfo, CancellationSignal paramCancellationSignal) {
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    int i = paramArrayOfFontInfo.length;
    for (byte b = 0; b < i; b++) {
      FontInfo fontInfo = paramArrayOfFontInfo[b];
      if (fontInfo.getResultCode() == 0) {
        Uri uri = fontInfo.getUri();
        if (!hashMap.containsKey(uri))
          hashMap.put(uri, TypefaceCompatUtil.mmap(paramContext, paramCancellationSignal, uri)); 
      } 
    } 
    return (Map)Collections.unmodifiableMap(hashMap);
  }
  
  public static void requestFont(@NonNull final Context context, @NonNull final FontRequest request, @NonNull final FontRequestCallback callback, @NonNull Handler paramHandler) {
    paramHandler.post(new Runnable() {
          public void run() {
            try {
              FontsContractCompat.FontFamilyResult fontFamilyResult = FontsContractCompat.fetchFonts(context, null, request);
              if (fontFamilyResult.getStatusCode() != 0) {
                switch (fontFamilyResult.getStatusCode()) {
                  default:
                    callerThreadHandler.post(new Runnable() {
                          public void run() {
                            callback.onTypefaceRequestFailed(-3);
                          }
                        });
                    return;
                  case 2:
                    callerThreadHandler.post(new Runnable() {
                          public void run() {
                            callback.onTypefaceRequestFailed(-3);
                          }
                        });
                    return;
                  case 1:
                    break;
                } 
                callerThreadHandler.post(new Runnable() {
                      public void run() {
                        callback.onTypefaceRequestFailed(-2);
                      }
                    });
                return;
              } 
              FontsContractCompat.FontInfo[] arrayOfFontInfo = fontFamilyResult.getFonts();
              if (arrayOfFontInfo == null || arrayOfFontInfo.length == 0) {
                callerThreadHandler.post(new Runnable() {
                      public void run() {
                        callback.onTypefaceRequestFailed(1);
                      }
                    });
                return;
              } 
              int i = arrayOfFontInfo.length;
              for (final int resultCode = 0; j < i; j++) {
                FontsContractCompat.FontInfo fontInfo = arrayOfFontInfo[j];
                if (fontInfo.getResultCode() != 0) {
                  j = fontInfo.getResultCode();
                  if (j < 0) {
                    callerThreadHandler.post(new Runnable() {
                          public void run() {
                            callback.onTypefaceRequestFailed(-3);
                          }
                        });
                  } else {
                    callerThreadHandler.post(new Runnable() {
                          public void run() {
                            callback.onTypefaceRequestFailed(resultCode);
                          }
                        });
                  } 
                  return;
                } 
              } 
              final Typeface typeface = FontsContractCompat.buildTypeface(context, null, arrayOfFontInfo);
              if (typeface == null) {
                callerThreadHandler.post(new Runnable() {
                      public void run() {
                        callback.onTypefaceRequestFailed(-3);
                      }
                    });
                return;
              } 
              callerThreadHandler.post(new Runnable() {
                    public void run() {
                      callback.onTypefaceRetrieved(typeface);
                    }
                  });
              return;
            } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
              callerThreadHandler.post(new Runnable() {
                    public void run() {
                      callback.onTypefaceRequestFailed(-1);
                    }
                  });
              return;
            } 
          }
        });
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static void resetCache() {
    sTypefaceCache.evictAll();
  }
  
  public static final class Columns implements BaseColumns {
    public static final String FILE_ID = "file_id";
    
    public static final String ITALIC = "font_italic";
    
    public static final String RESULT_CODE = "result_code";
    
    public static final int RESULT_CODE_FONT_NOT_FOUND = 1;
    
    public static final int RESULT_CODE_FONT_UNAVAILABLE = 2;
    
    public static final int RESULT_CODE_MALFORMED_QUERY = 3;
    
    public static final int RESULT_CODE_OK = 0;
    
    public static final String TTC_INDEX = "font_ttc_index";
    
    public static final String VARIATION_SETTINGS = "font_variation_settings";
    
    public static final String WEIGHT = "font_weight";
  }
  
  public static class FontFamilyResult {
    public static final int STATUS_OK = 0;
    
    public static final int STATUS_UNEXPECTED_DATA_PROVIDED = 2;
    
    public static final int STATUS_WRONG_CERTIFICATES = 1;
    
    private final FontsContractCompat.FontInfo[] mFonts;
    
    private final int mStatusCode;
    
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public FontFamilyResult(int param1Int, @Nullable FontsContractCompat.FontInfo[] param1ArrayOfFontInfo) {
      this.mStatusCode = param1Int;
      this.mFonts = param1ArrayOfFontInfo;
    }
    
    public FontsContractCompat.FontInfo[] getFonts() {
      return this.mFonts;
    }
    
    public int getStatusCode() {
      return this.mStatusCode;
    }
  }
  
  public static class FontInfo {
    private final boolean mItalic;
    
    private final int mResultCode;
    
    private final int mTtcIndex;
    
    private final Uri mUri;
    
    private final int mWeight;
    
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public FontInfo(@NonNull Uri param1Uri, @IntRange(from = 0L) int param1Int1, @IntRange(from = 1L, to = 1000L) int param1Int2, boolean param1Boolean, int param1Int3) {
      this.mUri = (Uri)Preconditions.checkNotNull(param1Uri);
      this.mTtcIndex = param1Int1;
      this.mWeight = param1Int2;
      this.mItalic = param1Boolean;
      this.mResultCode = param1Int3;
    }
    
    public int getResultCode() {
      return this.mResultCode;
    }
    
    @IntRange(from = 0L)
    public int getTtcIndex() {
      return this.mTtcIndex;
    }
    
    @NonNull
    public Uri getUri() {
      return this.mUri;
    }
    
    @IntRange(from = 1L, to = 1000L)
    public int getWeight() {
      return this.mWeight;
    }
    
    public boolean isItalic() {
      return this.mItalic;
    }
  }
  
  public static class FontRequestCallback {
    public static final int FAIL_REASON_FONT_LOAD_ERROR = -3;
    
    public static final int FAIL_REASON_FONT_NOT_FOUND = 1;
    
    public static final int FAIL_REASON_FONT_UNAVAILABLE = 2;
    
    public static final int FAIL_REASON_MALFORMED_QUERY = 3;
    
    public static final int FAIL_REASON_PROVIDER_NOT_FOUND = -1;
    
    public static final int FAIL_REASON_SECURITY_VIOLATION = -4;
    
    public static final int FAIL_REASON_WRONG_CERTIFICATES = -2;
    
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public static final int RESULT_OK = 0;
    
    public void onTypefaceRequestFailed(int param1Int) {}
    
    public void onTypefaceRetrieved(Typeface param1Typeface) {}
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public static @interface FontRequestFailReason {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface FontRequestFailReason {}
  
  private static final class TypefaceResult {
    final int mResult;
    
    final Typeface mTypeface;
    
    TypefaceResult(@Nullable Typeface param1Typeface, int param1Int) {
      this.mTypeface = param1Typeface;
      this.mResult = param1Int;
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\provider\FontsContractCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */