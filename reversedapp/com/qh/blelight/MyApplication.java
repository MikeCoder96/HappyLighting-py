package com.qh.blelight;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.android.soundrecorder.AudioRecordDemo;
import com.consmart.ble.AES2;
import com.qh.data.MySendThread;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

public class MyApplication extends Application {
  public static final String COMPANY_NAME = "^Color\\+|^Color-|^QHM|^Dream~";
  
  public static final String COMPANY_NAME2 = "^Color\\+|^Color-|^QHM";
  
  public static final String COMPANY_NAME_DREAM = "^Dream";
  
  public static final String COMPANY_NAME_DREAMX = "^Dream\\*";
  
  public static final String COMPANY_NAME_DREAMXX = "^Dream~";
  
  public static final String COMPANY_NAME_DREAMY = "^Dream\\#|^Dream~";
  
  public static final String COMPANY_NAME_DREAMY_BLX = "^Dream~|^Dream\\#";
  
  public static final String COMPANY_NAME_TRI = "^Triones-";
  
  public static final String COMPANY_NAME_XX = "^Flash";
  
  public static final int MAXCOLOR = 254;
  
  public static final String SetCMD_NAME_DREAMY = "^Triones-|^Dream-|^Dream\\*";
  
  public static int blue = 0;
  
  public static int calpha = 0;
  
  public static int cblue = 0;
  
  public static int cgreen = 50;
  
  public static int changenum = 5;
  
  public static int cred = 150;
  
  public static int debugNum = 0;
  
  public static int green = 50;
  
  public static int red = 150;
  
  public Handler AdjustHandler;
  
  String Dimmer = "^Dimmer-";
  
  public Handler ModHandler;
  
  public Handler MusicHandler;
  
  public boolean Open = true;
  
  public Handler RecordingHandler;
  
  public Handler TimingHandler;
  
  private int a = 0;
  
  public int[] bgsrc = new int[] { 2131165337, 2131165272, 2131165273, 2131165274, 2131165275, 2131165276, 2131165278, 2131165280, 2131165282 };
  
  private double cachedb = 5.0D;
  
  private int cachenum = 0;
  
  private double cachesrcdb = 80.0D;
  
  private int dblue = 0;
  
  private int dgreen = 0;
  
  private int dred = 0;
  
  public HashMap<String, BluetoothDevice> errorDevices = new HashMap<String, BluetoothDevice>();
  
  public Handler errorHandler;
  
  public Handler huancaiHandler;
  
  public boolean isHaveColor_A_or_QHM = false;
  
  public boolean isHaveColor_A_or_QHM2 = false;
  
  public boolean isHaveDreamXX = false;
  
  private boolean isOpenMusicHop = true;
  
  public boolean isOpenVisualizer = true;
  
  public boolean isRecorder = false;
  
  public boolean isallopen = true;
  
  private boolean isfrst = true;
  
  public boolean ishaveColor = false;
  
  public boolean ishaveDream = false;
  
  public boolean ishaveT = false;
  
  public boolean ishaveqicai = false;
  
  public boolean isopenmic = false;
  
  public boolean isshow = false;
  
  public int limitdb = 30;
  
  public HashMap<String, String> mBadPhone = new HashMap<String, String>();
  
  public BluetoothLeService mBluetoothLeService;
  
  public AudioRecordDemo mMediaRecorderDemo;
  
  public MyExpandableListAdapter mMyExpandableListAdapter;
  
  public Handler mShowHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message param1Message) {
          Bundle bundle = param1Message.getData();
          if (bundle != null) {
            double d1 = bundle.getDouble("-db");
            double d2 = bundle.getDouble("db");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("handleMessagedb: ");
            stringBuilder.append(d1);
            Log.e(" = = = = ", stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append("handleMessage:srcdb ");
            stringBuilder.append(d2);
            Log.e(" = = = = ", stringBuilder.toString());
            MyApplication.this.setCircleShow(d1, d2);
          } 
          return false;
        }
      });
  
  public Handler mainHandler;
  
  private double min = 1.0D;
  
  private Handler myHandler = new Handler();
  
  public MediaPlayer myMediaPlayer;
  
  private Runnable myRunnable = new Runnable() {
      public void run() {
        MyApplication.this.reSetCMD();
      }
    };
  
  private int num = 0;
  
  public Handler qicaiHandler;
  
  public SharedPreferences settings;
  
  private long showtime = 0L;
  
  public Handler soundControlHandler;
  
  public boolean swOpen = true;
  
  public double tsrcdb = 0.0D;
  
  public int typebg = 0;
  
  private void addColor() {
    int i;
    this.a = (int)Math.floor(Math.random() * 6.0D);
    if (this.a < 0) {
      i = 0;
    } else {
      i = this.a;
    } 
    this.a = i;
    if (this.a > 5) {
      i = 5;
    } else {
      i = this.a;
    } 
    this.a = i;
    if (this.a == 0) {
      red = 254;
      blue = 0;
      green += changenum;
      if (green >= 254) {
        green = 254;
        this.a = 1;
      } 
    } else if (this.a == 1) {
      green = 254;
      red -= changenum;
      blue = 0;
      if (red <= 0) {
        red = 0;
        this.a = 2;
      } 
    } else if (this.a == 2) {
      green = 254;
      blue += changenum;
      red = 0;
      if (blue >= 254) {
        blue = 200;
        this.a = 3;
      } 
    } else if (this.a == 3) {
      blue = 254;
      green -= changenum;
      red = 0;
      if (green <= 0) {
        green = 0;
        this.a = 4;
      } 
    } else if (this.a == 4) {
      blue = 254;
      red += changenum;
      green = 0;
      if (red >= 254) {
        red = 254;
        this.a = 5;
      } 
    } else if (this.a == 5) {
      red = 254;
      blue -= changenum;
      green = 0;
      if (blue <= 0) {
        blue = 0;
        this.a = 0;
      } 
    } else if (this.a == 6) {
      red = 100 - changenum;
      blue = 100 - changenum;
      green = 100 - changenum;
    } 
  }
  
  private int addcolor(int paramInt1, int paramInt2) {
    paramInt2 = paramInt1 + paramInt2;
    paramInt1 = paramInt2;
    if (paramInt2 > 255)
      paramInt1 = 255; 
    paramInt2 = paramInt1;
    if (paramInt1 < 0)
      paramInt2 = 0; 
    return paramInt2;
  }
  
  private void setCircleShow(double paramDouble1, double paramDouble2) {
    if (paramDouble2 <= (80 - this.limitdb))
      return; 
    this.tsrcdb = paramDouble2;
    setcolor(paramDouble1, paramDouble2);
    setMusicColor(Color.argb(255, cred, cgreen, cblue));
    addColor();
  }
  
  private void setMusicColor(int paramInt) {
    if (this.mBluetoothLeService == null)
      return; 
    try {
      for (String str : this.mBluetoothLeService.MyBluetoothGatts.keySet()) {
        byte b = 0;
        int i = b;
        if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
          MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(str);
          i = b;
          if (myBluetoothGatt != null) {
            i = b;
            if ((myBluetoothGatt.datas[2] & 0xFF) == 35)
              i = 1; 
          } 
        } 
        if (!i)
          continue; 
        i = Color.argb(255, Color.red(paramInt), Color.green(paramInt), Color.blue(paramInt));
        if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
          MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(str);
          if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2 && MainActivity.ControlMACs.containsKey(str)) {
            if (myBluetoothGatt.mLEdevice != null && !TextUtils.isEmpty(myBluetoothGatt.mLEdevice.getName()) && myBluetoothGatt.mLEdevice.getName().contains("Triones^")) {
              if (!this.isopenmic)
                myBluetoothGatt.setMusicColor(i); 
              continue;
            } 
            if (myBluetoothGatt.mLEdevice != null && !TextUtils.isEmpty(myBluetoothGatt.mLEdevice.getName()) && myBluetoothGatt.mLEdevice.getName().contains("Dream#")) {
              myBluetoothGatt.setMusicColor(i, this.tsrcdb);
              continue;
            } 
            if (myBluetoothGatt.mLEdevice != null && !TextUtils.isEmpty(myBluetoothGatt.mLEdevice.getName()) && myBluetoothGatt.mLEdevice.getName().contains("Dream~")) {
              myBluetoothGatt.setMusicColor(i, this.tsrcdb);
              continue;
            } 
            myBluetoothGatt.setMusicColor(i);
          } 
        } 
      } 
    } catch (ConcurrentModificationException concurrentModificationException) {}
  }
  
  private void setcolor(double paramDouble1, double paramDouble2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iconst_0
    //   3: putstatic com/qh/blelight/MyApplication.cred : I
    //   6: iconst_0
    //   7: putstatic com/qh/blelight/MyApplication.cgreen : I
    //   10: iconst_0
    //   11: putstatic com/qh/blelight/MyApplication.cblue : I
    //   14: iconst_0
    //   15: putstatic com/qh/blelight/MyApplication.calpha : I
    //   18: aload_0
    //   19: getfield limitdb : I
    //   22: istore #5
    //   24: dload_3
    //   25: bipush #80
    //   27: iload #5
    //   29: isub
    //   30: i2d
    //   31: dcmpg
    //   32: ifgt -> 38
    //   35: aload_0
    //   36: monitorexit
    //   37: return
    //   38: dload_1
    //   39: invokestatic abs : (D)D
    //   42: ldc2_w 3.0
    //   45: dcmpg
    //   46: ifge -> 200
    //   49: aload_0
    //   50: getfield isfrst : Z
    //   53: ifne -> 200
    //   56: getstatic com/qh/blelight/MyApplication.red : I
    //   59: i2d
    //   60: dstore #6
    //   62: aload_0
    //   63: getfield dred : I
    //   66: bipush #12
    //   68: idiv
    //   69: istore #5
    //   71: iload #5
    //   73: i2d
    //   74: dstore #8
    //   76: dload #8
    //   78: invokestatic isNaN : (D)Z
    //   81: pop
    //   82: dload #6
    //   84: invokestatic isNaN : (D)Z
    //   87: pop
    //   88: dload #6
    //   90: dload #8
    //   92: dload_1
    //   93: dmul
    //   94: dadd
    //   95: d2i
    //   96: istore #5
    //   98: iload #5
    //   100: putstatic com/qh/blelight/MyApplication.cred : I
    //   103: getstatic com/qh/blelight/MyApplication.green : I
    //   106: i2d
    //   107: dstore #6
    //   109: aload_0
    //   110: getfield dgreen : I
    //   113: bipush #12
    //   115: idiv
    //   116: istore #5
    //   118: iload #5
    //   120: i2d
    //   121: dstore #8
    //   123: dload #8
    //   125: invokestatic isNaN : (D)Z
    //   128: pop
    //   129: dload #6
    //   131: invokestatic isNaN : (D)Z
    //   134: pop
    //   135: dload #6
    //   137: dload #8
    //   139: dload_1
    //   140: dmul
    //   141: dadd
    //   142: d2i
    //   143: istore #5
    //   145: iload #5
    //   147: putstatic com/qh/blelight/MyApplication.cgreen : I
    //   150: getstatic com/qh/blelight/MyApplication.blue : I
    //   153: i2d
    //   154: dstore #6
    //   156: aload_0
    //   157: getfield dblue : I
    //   160: bipush #12
    //   162: idiv
    //   163: istore #5
    //   165: iload #5
    //   167: i2d
    //   168: dstore #8
    //   170: dload #8
    //   172: invokestatic isNaN : (D)Z
    //   175: pop
    //   176: dload #6
    //   178: invokestatic isNaN : (D)Z
    //   181: pop
    //   182: dload #6
    //   184: dload #8
    //   186: dload_1
    //   187: dmul
    //   188: dadd
    //   189: d2i
    //   190: istore #5
    //   192: iload #5
    //   194: putstatic com/qh/blelight/MyApplication.cblue : I
    //   197: goto -> 335
    //   200: getstatic com/qh/blelight/MyApplication.red : I
    //   203: i2d
    //   204: dstore #8
    //   206: getstatic com/qh/blelight/MyApplication.red : I
    //   209: iconst_5
    //   210: idiv
    //   211: istore #5
    //   213: iload #5
    //   215: i2d
    //   216: dstore #6
    //   218: dload #6
    //   220: invokestatic isNaN : (D)Z
    //   223: pop
    //   224: dload #8
    //   226: invokestatic isNaN : (D)Z
    //   229: pop
    //   230: dload #8
    //   232: dload #6
    //   234: dload_1
    //   235: dmul
    //   236: dadd
    //   237: d2i
    //   238: istore #5
    //   240: iload #5
    //   242: putstatic com/qh/blelight/MyApplication.cred : I
    //   245: getstatic com/qh/blelight/MyApplication.green : I
    //   248: i2d
    //   249: dstore #8
    //   251: getstatic com/qh/blelight/MyApplication.green : I
    //   254: iconst_5
    //   255: idiv
    //   256: istore #5
    //   258: iload #5
    //   260: i2d
    //   261: dstore #6
    //   263: dload #6
    //   265: invokestatic isNaN : (D)Z
    //   268: pop
    //   269: dload #8
    //   271: invokestatic isNaN : (D)Z
    //   274: pop
    //   275: dload #8
    //   277: dload #6
    //   279: dload_1
    //   280: dmul
    //   281: dadd
    //   282: d2i
    //   283: istore #5
    //   285: iload #5
    //   287: putstatic com/qh/blelight/MyApplication.cgreen : I
    //   290: getstatic com/qh/blelight/MyApplication.blue : I
    //   293: i2d
    //   294: dstore #8
    //   296: getstatic com/qh/blelight/MyApplication.blue : I
    //   299: iconst_5
    //   300: idiv
    //   301: istore #5
    //   303: iload #5
    //   305: i2d
    //   306: dstore #6
    //   308: dload #6
    //   310: invokestatic isNaN : (D)Z
    //   313: pop
    //   314: dload #8
    //   316: invokestatic isNaN : (D)Z
    //   319: pop
    //   320: dload #8
    //   322: dload #6
    //   324: dload_1
    //   325: dmul
    //   326: dadd
    //   327: d2i
    //   328: istore #5
    //   330: iload #5
    //   332: putstatic com/qh/blelight/MyApplication.cblue : I
    //   335: dload_1
    //   336: dstore #6
    //   338: dload_3
    //   339: ldc2_w 30.0
    //   342: dcmpl
    //   343: ifle -> 398
    //   346: aload_0
    //   347: aload_0
    //   348: getfield cachesrcdb : D
    //   351: dload_3
    //   352: dadd
    //   353: ldc2_w 2.0
    //   356: ddiv
    //   357: putfield cachesrcdb : D
    //   360: aload_0
    //   361: aload_0
    //   362: getfield cachedb : D
    //   365: dload_1
    //   366: dadd
    //   367: ldc2_w 2.0
    //   370: ddiv
    //   371: putfield cachedb : D
    //   374: aload_0
    //   375: getfield cachedb : D
    //   378: dstore #6
    //   380: aload_0
    //   381: aload_0
    //   382: getfield min : D
    //   385: dload #6
    //   387: invokestatic abs : (D)D
    //   390: dadd
    //   391: ldc2_w 2.0
    //   394: ddiv
    //   395: putfield min : D
    //   398: dload #6
    //   400: dconst_0
    //   401: dcmpl
    //   402: iflt -> 804
    //   405: dload #6
    //   407: ldc2_w 1.5
    //   410: dcmpl
    //   411: ifle -> 507
    //   414: dload #6
    //   416: ldc2_w 2.0
    //   419: dcmpg
    //   420: ifgt -> 507
    //   423: getstatic com/qh/blelight/MyApplication.cred : I
    //   426: istore #5
    //   428: iload #5
    //   430: i2d
    //   431: dstore_1
    //   432: dload_1
    //   433: invokestatic isNaN : (D)Z
    //   436: pop
    //   437: dload_1
    //   438: ldc2_w 1.1
    //   441: dmul
    //   442: d2i
    //   443: istore #5
    //   445: iload #5
    //   447: putstatic com/qh/blelight/MyApplication.cred : I
    //   450: getstatic com/qh/blelight/MyApplication.cgreen : I
    //   453: istore #5
    //   455: iload #5
    //   457: i2d
    //   458: dstore_1
    //   459: dload_1
    //   460: invokestatic isNaN : (D)Z
    //   463: pop
    //   464: dload_1
    //   465: ldc2_w 1.1
    //   468: dmul
    //   469: d2i
    //   470: istore #5
    //   472: iload #5
    //   474: putstatic com/qh/blelight/MyApplication.cgreen : I
    //   477: getstatic com/qh/blelight/MyApplication.cblue : I
    //   480: istore #5
    //   482: iload #5
    //   484: i2d
    //   485: dstore_1
    //   486: dload_1
    //   487: invokestatic isNaN : (D)Z
    //   490: pop
    //   491: dload_1
    //   492: ldc2_w 1.1
    //   495: dmul
    //   496: d2i
    //   497: istore #5
    //   499: iload #5
    //   501: putstatic com/qh/blelight/MyApplication.cblue : I
    //   504: goto -> 1233
    //   507: dload #6
    //   509: ldc2_w 2.0
    //   512: dcmpl
    //   513: ifle -> 609
    //   516: dload #6
    //   518: ldc2_w 3.0
    //   521: dcmpg
    //   522: ifge -> 609
    //   525: getstatic com/qh/blelight/MyApplication.cred : I
    //   528: istore #5
    //   530: iload #5
    //   532: i2d
    //   533: dstore_1
    //   534: dload_1
    //   535: invokestatic isNaN : (D)Z
    //   538: pop
    //   539: dload_1
    //   540: ldc2_w 1.2
    //   543: dmul
    //   544: d2i
    //   545: istore #5
    //   547: iload #5
    //   549: putstatic com/qh/blelight/MyApplication.cred : I
    //   552: getstatic com/qh/blelight/MyApplication.cgreen : I
    //   555: istore #5
    //   557: iload #5
    //   559: i2d
    //   560: dstore_1
    //   561: dload_1
    //   562: invokestatic isNaN : (D)Z
    //   565: pop
    //   566: dload_1
    //   567: ldc2_w 1.2
    //   570: dmul
    //   571: d2i
    //   572: istore #5
    //   574: iload #5
    //   576: putstatic com/qh/blelight/MyApplication.cgreen : I
    //   579: getstatic com/qh/blelight/MyApplication.cblue : I
    //   582: istore #5
    //   584: iload #5
    //   586: i2d
    //   587: dstore_1
    //   588: dload_1
    //   589: invokestatic isNaN : (D)Z
    //   592: pop
    //   593: dload_1
    //   594: ldc2_w 1.2
    //   597: dmul
    //   598: d2i
    //   599: istore #5
    //   601: iload #5
    //   603: putstatic com/qh/blelight/MyApplication.cblue : I
    //   606: goto -> 1233
    //   609: dload #6
    //   611: ldc2_w 3.0
    //   614: dcmpl
    //   615: ifle -> 711
    //   618: dload #6
    //   620: ldc2_w 5.0
    //   623: dcmpg
    //   624: ifge -> 711
    //   627: getstatic com/qh/blelight/MyApplication.cred : I
    //   630: istore #5
    //   632: iload #5
    //   634: i2d
    //   635: dstore_1
    //   636: dload_1
    //   637: invokestatic isNaN : (D)Z
    //   640: pop
    //   641: dload_1
    //   642: ldc2_w 1.4
    //   645: dmul
    //   646: d2i
    //   647: istore #5
    //   649: iload #5
    //   651: putstatic com/qh/blelight/MyApplication.cred : I
    //   654: getstatic com/qh/blelight/MyApplication.cgreen : I
    //   657: istore #5
    //   659: iload #5
    //   661: i2d
    //   662: dstore_1
    //   663: dload_1
    //   664: invokestatic isNaN : (D)Z
    //   667: pop
    //   668: dload_1
    //   669: ldc2_w 1.4
    //   672: dmul
    //   673: d2i
    //   674: istore #5
    //   676: iload #5
    //   678: putstatic com/qh/blelight/MyApplication.cgreen : I
    //   681: getstatic com/qh/blelight/MyApplication.cblue : I
    //   684: istore #5
    //   686: iload #5
    //   688: i2d
    //   689: dstore_1
    //   690: dload_1
    //   691: invokestatic isNaN : (D)Z
    //   694: pop
    //   695: dload_1
    //   696: ldc2_w 1.4
    //   699: dmul
    //   700: d2i
    //   701: istore #5
    //   703: iload #5
    //   705: putstatic com/qh/blelight/MyApplication.cblue : I
    //   708: goto -> 1233
    //   711: dload #6
    //   713: ldc2_w 5.0
    //   716: dcmpl
    //   717: iflt -> 1233
    //   720: getstatic com/qh/blelight/MyApplication.cred : I
    //   723: istore #5
    //   725: iload #5
    //   727: i2d
    //   728: dstore_1
    //   729: dload_1
    //   730: invokestatic isNaN : (D)Z
    //   733: pop
    //   734: dload_1
    //   735: ldc2_w 1.5
    //   738: dmul
    //   739: d2i
    //   740: istore #5
    //   742: iload #5
    //   744: putstatic com/qh/blelight/MyApplication.cred : I
    //   747: getstatic com/qh/blelight/MyApplication.cgreen : I
    //   750: istore #5
    //   752: iload #5
    //   754: i2d
    //   755: dstore_1
    //   756: dload_1
    //   757: invokestatic isNaN : (D)Z
    //   760: pop
    //   761: dload_1
    //   762: ldc2_w 1.5
    //   765: dmul
    //   766: d2i
    //   767: istore #5
    //   769: iload #5
    //   771: putstatic com/qh/blelight/MyApplication.cgreen : I
    //   774: getstatic com/qh/blelight/MyApplication.cblue : I
    //   777: istore #5
    //   779: iload #5
    //   781: i2d
    //   782: dstore_1
    //   783: dload_1
    //   784: invokestatic isNaN : (D)Z
    //   787: pop
    //   788: dload_1
    //   789: ldc2_w 1.5
    //   792: dmul
    //   793: d2i
    //   794: istore #5
    //   796: iload #5
    //   798: putstatic com/qh/blelight/MyApplication.cblue : I
    //   801: goto -> 1233
    //   804: dload #6
    //   806: ldc2_w -1.0
    //   809: dcmpg
    //   810: ifge -> 906
    //   813: dload #6
    //   815: ldc2_w -1.5
    //   818: dcmpl
    //   819: ifle -> 906
    //   822: getstatic com/qh/blelight/MyApplication.cred : I
    //   825: istore #5
    //   827: iload #5
    //   829: i2d
    //   830: dstore_1
    //   831: dload_1
    //   832: invokestatic isNaN : (D)Z
    //   835: pop
    //   836: dload_1
    //   837: ldc2_w 0.7
    //   840: dmul
    //   841: d2i
    //   842: istore #5
    //   844: iload #5
    //   846: putstatic com/qh/blelight/MyApplication.cred : I
    //   849: getstatic com/qh/blelight/MyApplication.cgreen : I
    //   852: istore #5
    //   854: iload #5
    //   856: i2d
    //   857: dstore_1
    //   858: dload_1
    //   859: invokestatic isNaN : (D)Z
    //   862: pop
    //   863: dload_1
    //   864: ldc2_w 0.7
    //   867: dmul
    //   868: d2i
    //   869: istore #5
    //   871: iload #5
    //   873: putstatic com/qh/blelight/MyApplication.cgreen : I
    //   876: getstatic com/qh/blelight/MyApplication.cblue : I
    //   879: istore #5
    //   881: iload #5
    //   883: i2d
    //   884: dstore_1
    //   885: dload_1
    //   886: invokestatic isNaN : (D)Z
    //   889: pop
    //   890: dload_1
    //   891: ldc2_w 0.7
    //   894: dmul
    //   895: d2i
    //   896: istore #5
    //   898: iload #5
    //   900: putstatic com/qh/blelight/MyApplication.cblue : I
    //   903: goto -> 1233
    //   906: dload #6
    //   908: ldc2_w -1.5
    //   911: dcmpg
    //   912: ifgt -> 1008
    //   915: dload #6
    //   917: ldc2_w -2.0
    //   920: dcmpl
    //   921: ifle -> 1008
    //   924: getstatic com/qh/blelight/MyApplication.cred : I
    //   927: istore #5
    //   929: iload #5
    //   931: i2d
    //   932: dstore_1
    //   933: dload_1
    //   934: invokestatic isNaN : (D)Z
    //   937: pop
    //   938: dload_1
    //   939: ldc2_w 0.6
    //   942: dmul
    //   943: d2i
    //   944: istore #5
    //   946: iload #5
    //   948: putstatic com/qh/blelight/MyApplication.cred : I
    //   951: getstatic com/qh/blelight/MyApplication.cgreen : I
    //   954: istore #5
    //   956: iload #5
    //   958: i2d
    //   959: dstore_1
    //   960: dload_1
    //   961: invokestatic isNaN : (D)Z
    //   964: pop
    //   965: dload_1
    //   966: ldc2_w 0.6
    //   969: dmul
    //   970: d2i
    //   971: istore #5
    //   973: iload #5
    //   975: putstatic com/qh/blelight/MyApplication.cgreen : I
    //   978: getstatic com/qh/blelight/MyApplication.cblue : I
    //   981: istore #5
    //   983: iload #5
    //   985: i2d
    //   986: dstore_1
    //   987: dload_1
    //   988: invokestatic isNaN : (D)Z
    //   991: pop
    //   992: dload_1
    //   993: ldc2_w 0.6
    //   996: dmul
    //   997: d2i
    //   998: istore #5
    //   1000: iload #5
    //   1002: putstatic com/qh/blelight/MyApplication.cblue : I
    //   1005: goto -> 1233
    //   1008: dload #6
    //   1010: ldc2_w -2.0
    //   1013: dcmpg
    //   1014: ifgt -> 1110
    //   1017: dload #6
    //   1019: ldc2_w -3.0
    //   1022: dcmpl
    //   1023: ifle -> 1110
    //   1026: getstatic com/qh/blelight/MyApplication.cred : I
    //   1029: istore #5
    //   1031: iload #5
    //   1033: i2d
    //   1034: dstore_1
    //   1035: dload_1
    //   1036: invokestatic isNaN : (D)Z
    //   1039: pop
    //   1040: dload_1
    //   1041: ldc2_w 0.5
    //   1044: dmul
    //   1045: d2i
    //   1046: istore #5
    //   1048: iload #5
    //   1050: putstatic com/qh/blelight/MyApplication.cred : I
    //   1053: getstatic com/qh/blelight/MyApplication.cgreen : I
    //   1056: istore #5
    //   1058: iload #5
    //   1060: i2d
    //   1061: dstore_1
    //   1062: dload_1
    //   1063: invokestatic isNaN : (D)Z
    //   1066: pop
    //   1067: dload_1
    //   1068: ldc2_w 0.5
    //   1071: dmul
    //   1072: d2i
    //   1073: istore #5
    //   1075: iload #5
    //   1077: putstatic com/qh/blelight/MyApplication.cgreen : I
    //   1080: getstatic com/qh/blelight/MyApplication.cblue : I
    //   1083: istore #5
    //   1085: iload #5
    //   1087: i2d
    //   1088: dstore_1
    //   1089: dload_1
    //   1090: invokestatic isNaN : (D)Z
    //   1093: pop
    //   1094: dload_1
    //   1095: ldc2_w 0.5
    //   1098: dmul
    //   1099: d2i
    //   1100: istore #5
    //   1102: iload #5
    //   1104: putstatic com/qh/blelight/MyApplication.cblue : I
    //   1107: goto -> 1233
    //   1110: dload #6
    //   1112: ldc2_w -3.0
    //   1115: dcmpg
    //   1116: ifgt -> 1212
    //   1119: dload #6
    //   1121: ldc2_w -4.0
    //   1124: dcmpl
    //   1125: ifle -> 1212
    //   1128: getstatic com/qh/blelight/MyApplication.cred : I
    //   1131: istore #5
    //   1133: iload #5
    //   1135: i2d
    //   1136: dstore_1
    //   1137: dload_1
    //   1138: invokestatic isNaN : (D)Z
    //   1141: pop
    //   1142: dload_1
    //   1143: ldc2_w 0.4
    //   1146: dmul
    //   1147: d2i
    //   1148: istore #5
    //   1150: iload #5
    //   1152: putstatic com/qh/blelight/MyApplication.cred : I
    //   1155: getstatic com/qh/blelight/MyApplication.cgreen : I
    //   1158: istore #5
    //   1160: iload #5
    //   1162: i2d
    //   1163: dstore_1
    //   1164: dload_1
    //   1165: invokestatic isNaN : (D)Z
    //   1168: pop
    //   1169: dload_1
    //   1170: ldc2_w 0.4
    //   1173: dmul
    //   1174: d2i
    //   1175: istore #5
    //   1177: iload #5
    //   1179: putstatic com/qh/blelight/MyApplication.cgreen : I
    //   1182: getstatic com/qh/blelight/MyApplication.cblue : I
    //   1185: istore #5
    //   1187: iload #5
    //   1189: i2d
    //   1190: dstore_1
    //   1191: dload_1
    //   1192: invokestatic isNaN : (D)Z
    //   1195: pop
    //   1196: dload_1
    //   1197: ldc2_w 0.4
    //   1200: dmul
    //   1201: d2i
    //   1202: istore #5
    //   1204: iload #5
    //   1206: putstatic com/qh/blelight/MyApplication.cblue : I
    //   1209: goto -> 1233
    //   1212: dload #6
    //   1214: ldc2_w -5.0
    //   1217: dcmpg
    //   1218: ifgt -> 1233
    //   1221: iconst_0
    //   1222: putstatic com/qh/blelight/MyApplication.cred : I
    //   1225: iconst_0
    //   1226: putstatic com/qh/blelight/MyApplication.cgreen : I
    //   1229: iconst_0
    //   1230: putstatic com/qh/blelight/MyApplication.cblue : I
    //   1233: aload_0
    //   1234: getstatic com/qh/blelight/MyApplication.cred : I
    //   1237: iconst_0
    //   1238: invokespecial addcolor : (II)I
    //   1241: putstatic com/qh/blelight/MyApplication.cred : I
    //   1244: aload_0
    //   1245: getstatic com/qh/blelight/MyApplication.cgreen : I
    //   1248: iconst_0
    //   1249: invokespecial addcolor : (II)I
    //   1252: putstatic com/qh/blelight/MyApplication.cgreen : I
    //   1255: aload_0
    //   1256: getstatic com/qh/blelight/MyApplication.cblue : I
    //   1259: iconst_0
    //   1260: invokespecial addcolor : (II)I
    //   1263: putstatic com/qh/blelight/MyApplication.cblue : I
    //   1266: aload_0
    //   1267: getstatic com/qh/blelight/MyApplication.cred : I
    //   1270: putfield dred : I
    //   1273: aload_0
    //   1274: getstatic com/qh/blelight/MyApplication.cgreen : I
    //   1277: putfield dgreen : I
    //   1280: aload_0
    //   1281: getstatic com/qh/blelight/MyApplication.cblue : I
    //   1284: putfield dblue : I
    //   1287: aload_0
    //   1288: monitorexit
    //   1289: return
    //   1290: astore #10
    //   1292: aload_0
    //   1293: monitorexit
    //   1294: aload #10
    //   1296: athrow
    // Exception table:
    //   from	to	target	type
    //   2	24	1290	finally
    //   38	71	1290	finally
    //   98	118	1290	finally
    //   145	165	1290	finally
    //   192	197	1290	finally
    //   200	213	1290	finally
    //   240	258	1290	finally
    //   285	303	1290	finally
    //   330	335	1290	finally
    //   346	398	1290	finally
    //   423	428	1290	finally
    //   445	455	1290	finally
    //   472	482	1290	finally
    //   499	504	1290	finally
    //   525	530	1290	finally
    //   547	557	1290	finally
    //   574	584	1290	finally
    //   601	606	1290	finally
    //   627	632	1290	finally
    //   649	659	1290	finally
    //   676	686	1290	finally
    //   703	708	1290	finally
    //   720	725	1290	finally
    //   742	752	1290	finally
    //   769	779	1290	finally
    //   796	801	1290	finally
    //   822	827	1290	finally
    //   844	854	1290	finally
    //   871	881	1290	finally
    //   898	903	1290	finally
    //   924	929	1290	finally
    //   946	956	1290	finally
    //   973	983	1290	finally
    //   1000	1005	1290	finally
    //   1026	1031	1290	finally
    //   1048	1058	1290	finally
    //   1075	1085	1290	finally
    //   1102	1107	1290	finally
    //   1128	1133	1290	finally
    //   1150	1160	1290	finally
    //   1177	1187	1290	finally
    //   1204	1209	1290	finally
    //   1221	1233	1290	finally
    //   1233	1287	1290	finally
  }
  
  public void SMG(Message paramMessage) {
    if (this.AdjustHandler != null)
      this.AdjustHandler.sendMessage(paramMessage); 
    if (this.ModHandler != null)
      this.ModHandler.sendMessage(paramMessage); 
    if (this.MusicHandler != null)
      this.MusicHandler.sendMessage(paramMessage); 
    if (this.RecordingHandler != null)
      this.RecordingHandler.sendMessage(paramMessage); 
    if (this.TimingHandler != null)
      this.TimingHandler.sendMessage(paramMessage); 
  }
  
  public void checkpwd(String paramString1, String paramString2) {
    if (this.mBluetoothLeService == null)
      return; 
    if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(paramString1)) {
      MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(paramString1);
      if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2)
        myBluetoothGatt.checkpwd(paramString2); 
    } 
  }
  
  public void disconn(String paramString) {
    if (this.mBluetoothLeService == null)
      return; 
    this.mBluetoothLeService.unlinkBleDevices.put(paramString, paramString);
    if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(paramString)) {
      MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(paramString);
      if (myBluetoothGatt != null)
        myBluetoothGatt.stopLEService(); 
    } 
  }
  
  public boolean isDreamY(String paramString) {
    if (this.mBluetoothLeService == null)
      return false; 
    Pattern pattern = Pattern.compile("^Dream\\#|^Dream~");
    if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(paramString)) {
      MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(paramString);
      if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2 && myBluetoothGatt.mLEdevice != null && !TextUtils.isEmpty(myBluetoothGatt.mLEdevice.getName()) && pattern.matcher(myBluetoothGatt.mLEdevice.getName()).find())
        return true; 
    } 
    return false;
  }
  
  public boolean isDreamf() {
    if (this.mBluetoothLeService == null)
      return false; 
    Pattern pattern = Pattern.compile("^Dream\\*");
    for (String str : this.mBluetoothLeService.MyBluetoothGatts.keySet()) {
      if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2 && myBluetoothGatt.mLEdevice != null && !TextUtils.isEmpty(myBluetoothGatt.mLEdevice.getName()) && pattern.matcher(myBluetoothGatt.mLEdevice.getName()).find())
          return true; 
      } 
    } 
    return false;
  }
  
  public boolean isDreamf(String paramString) {
    return (this.mBluetoothLeService == null) ? false : (Pattern.compile("^Dream\\*").matcher(paramString).find());
  }
  
  public boolean isHaveColor_A_or_QHM() {
    this.isHaveColor_A_or_QHM = false;
    if (this.mBluetoothLeService == null)
      return this.isHaveColor_A_or_QHM; 
    Pattern pattern = Pattern.compile("^Color\\+|^Color-|^QHM|^Dream~");
    for (String str : this.mBluetoothLeService.MyBluetoothGatts.keySet()) {
      if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2 && myBluetoothGatt.mLEdevice != null && !TextUtils.isEmpty(myBluetoothGatt.mLEdevice.getName()) && myBluetoothGatt.mLEdevice.getName() != null && pattern.matcher(myBluetoothGatt.mLEdevice.getName()).find())
          this.isHaveColor_A_or_QHM = true; 
      } 
    } 
    return this.isHaveColor_A_or_QHM;
  }
  
  public boolean isHaveColor_A_or_QHM2() {
    this.isHaveColor_A_or_QHM2 = false;
    if (this.mBluetoothLeService == null)
      return this.isHaveColor_A_or_QHM2; 
    Pattern pattern = Pattern.compile("^Color\\+|^Color-|^QHM");
    for (String str : this.mBluetoothLeService.MyBluetoothGatts.keySet()) {
      if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2 && myBluetoothGatt.mLEdevice != null && !TextUtils.isEmpty(myBluetoothGatt.mLEdevice.getName()) && myBluetoothGatt.mLEdevice.getName() != null && pattern.matcher(myBluetoothGatt.mLEdevice.getName()).find())
          this.isHaveColor_A_or_QHM2 = true; 
      } 
    } 
    return this.isHaveColor_A_or_QHM2;
  }
  
  public boolean isHaveDreamXX() {
    this.isHaveDreamXX = false;
    if (this.mBluetoothLeService == null)
      return this.isHaveDreamXX; 
    Pattern pattern = Pattern.compile("^Dream~");
    for (String str : this.mBluetoothLeService.MyBluetoothGatts.keySet()) {
      if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2 && myBluetoothGatt.mLEdevice != null && !TextUtils.isEmpty(myBluetoothGatt.mLEdevice.getName()) && myBluetoothGatt.mLEdevice.getName() != null && pattern.matcher(myBluetoothGatt.mLEdevice.getName()).find())
          this.isHaveDreamXX = true; 
      } 
    } 
    return this.isHaveDreamXX;
  }
  
  public boolean isHaveTri() {
    BluetoothLeService bluetoothLeService = this.mBluetoothLeService;
    byte b = 0;
    if (bluetoothLeService == null)
      return false; 
    Pattern pattern = Pattern.compile("^Triones-");
    Iterator<String> iterator = this.mBluetoothLeService.MyBluetoothGatts.keySet().iterator();
    boolean bool = false;
    while (iterator.hasNext()) {
      String str = iterator.next();
      if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2 && myBluetoothGatt.mLEdevice != null && !TextUtils.isEmpty(myBluetoothGatt.mLEdevice.getName())) {
          if (pattern.matcher(myBluetoothGatt.mLEdevice.getName()).find())
            bool = true; 
          b++;
        } 
      } 
    } 
    if (b == 0)
      bool = true; 
    return bool;
  }
  
  public boolean isHaveX() {
    if (this.mBluetoothLeService == null)
      return false; 
    Pattern pattern = Pattern.compile("^Flash");
    for (String str : this.mBluetoothLeService.MyBluetoothGatts.keySet()) {
      if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2 && myBluetoothGatt.mLEdevice != null && !TextUtils.isEmpty(myBluetoothGatt.mLEdevice.getName()) && myBluetoothGatt.mLEdevice.getName() != null && pattern.matcher(myBluetoothGatt.mLEdevice.getName()).find())
          return true; 
      } 
    } 
    return false;
  }
  
  public boolean isIshaveDreamBLX() {
    BluetoothLeService bluetoothLeService = this.mBluetoothLeService;
    boolean bool = false;
    if (bluetoothLeService == null)
      return false; 
    Pattern pattern = Pattern.compile("^Dream~|^Dream\\#");
    for (String str : this.mBluetoothLeService.MyBluetoothGatts.keySet()) {
      if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2 && myBluetoothGatt.mLEdevice != null && !TextUtils.isEmpty(myBluetoothGatt.mLEdevice.getName()) && pattern.matcher(myBluetoothGatt.mLEdevice.getName()).find())
          bool = true; 
      } 
    } 
    return bool;
  }
  
  public boolean isOnlyDreamY() {
    if (this.mBluetoothLeService == null)
      return false; 
    Pattern pattern1 = Pattern.compile("^Dream");
    Pattern pattern2 = Pattern.compile("^Dream\\#|^Dream~");
    for (String str : this.mBluetoothLeService.MyBluetoothGatts.keySet()) {
      if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2 && myBluetoothGatt.mLEdevice != null && !TextUtils.isEmpty(myBluetoothGatt.mLEdevice.getName()) && pattern1.matcher(myBluetoothGatt.mLEdevice.getName()).find() && !pattern2.matcher(myBluetoothGatt.mLEdevice.getName()).find())
          return false; 
      } 
    } 
    return true;
  }
  
  public boolean isOnlyDreamf() {
    if (this.mBluetoothLeService == null)
      return false; 
    Pattern pattern1 = Pattern.compile("^Dream");
    Pattern pattern2 = Pattern.compile("^Dream\\*");
    for (String str : this.mBluetoothLeService.MyBluetoothGatts.keySet()) {
      if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2 && myBluetoothGatt.mLEdevice != null && !TextUtils.isEmpty(myBluetoothGatt.mLEdevice.getName()) && pattern1.matcher(myBluetoothGatt.mLEdevice.getName()).find() && !pattern2.matcher(myBluetoothGatt.mLEdevice.getName()).find())
          return false; 
      } 
    } 
    return true;
  }
  
  public boolean isOnlyHaveX() {
    if (this.mBluetoothLeService == null)
      return false; 
    Pattern pattern = Pattern.compile("^Flash");
    for (String str : this.mBluetoothLeService.MyBluetoothGatts.keySet()) {
      if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2 && myBluetoothGatt.mLEdevice != null && myBluetoothGatt.mLEdevice.getName() != null && !pattern.matcher(myBluetoothGatt.mLEdevice.getName()).find())
          return false; 
      } 
    } 
    return true;
  }
  
  public boolean isOpenMusicHop() {
    return true;
  }
  
  public boolean isTri(String paramString) {
    return Pattern.compile("^Triones-").matcher(paramString).find();
  }
  
  public boolean isconn(String paramString) {
    if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(paramString)) {
      MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(paramString);
      if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2)
        return true; 
    } 
    return false;
  }
  
  public boolean ishavesQHM() {
    if (this.mBluetoothLeService == null)
      return false; 
    for (String str : this.mBluetoothLeService.MyBluetoothGatts.keySet()) {
      if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2 && myBluetoothGatt.mLEdevice != null && !TextUtils.isEmpty(myBluetoothGatt.mLEdevice.getName()) && myBluetoothGatt.mLEdevice.getName().contains("QHM"))
          return true; 
      } 
    } 
    return false;
  }
  
  public boolean ishavess() {
    if (this.mBluetoothLeService == null)
      return false; 
    for (String str : this.mBluetoothLeService.MyBluetoothGatts.keySet()) {
      if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2 && myBluetoothGatt.mLEdevice.getName().contains("Triones#"))
          return true; 
      } 
    } 
    return false;
  }
  
  public boolean isopenmic() {
    if (this.mBluetoothLeService == null)
      return false; 
    if (this.mBluetoothLeService.MyBluetoothGatts == null)
      return false; 
    Iterator<String> iterator = this.mBluetoothLeService.MyBluetoothGatts.keySet().iterator();
    if (iterator == null)
      return false; 
    while (iterator.hasNext()) {
      String str = iterator.next();
      if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2 && myBluetoothGatt.mLEdevice != null && !TextUtils.isEmpty(myBluetoothGatt.mLEdevice.getName()) && (myBluetoothGatt.mLEdevice.getName().contains("Triones^") || myBluetoothGatt.mLEdevice.getName().contains("Dimmer") || myBluetoothGatt.mLEdevice.getName().contains("Color+")))
          return true; 
      } 
    } 
    return false;
  }
  
  public boolean isopenmic2() {
    if (this.mBluetoothLeService == null)
      return false; 
    for (String str : this.mBluetoothLeService.MyBluetoothGatts.keySet()) {
      if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2 && myBluetoothGatt.mLEdevice != null && !TextUtils.isEmpty(myBluetoothGatt.mLEdevice.getName()) && myBluetoothGatt.mLEdevice.getName().contains("Dimmer"))
          return true; 
      } 
    } 
    return false;
  }
  
  public void onCreate() {
    super.onCreate();
    Log.e("", "-----myApplication-----");
    this.mMediaRecorderDemo = new AudioRecordDemo();
    this.mMediaRecorderDemo.mShowHandler = this.mShowHandler;
    this.mBadPhone.put("HUAWEI P7-L091", "HUAWEI P7-L091");
    if (this.mBadPhone.containsKey(Build.MODEL))
      this.isOpenVisualizer = false; 
    this.settings = getSharedPreferences("BleLight", 0);
    this.limitdb = this.settings.getInt("limitdb", 30);
    this.typebg = this.settings.getInt("typebg", 0);
    AES2.setKey(new byte[] { 
          -48, -7, -12, -116, 89, -94, 105, 29, 32, 83, 
          -53, -38, Byte.MIN_VALUE, -124, 67, -109 });
  }
  
  public void openAll(boolean paramBoolean) {
    if (this.mBluetoothLeService == null)
      return; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("mac = ");
    stringBuilder.append(paramBoolean);
    Log.e("", stringBuilder.toString());
    this.isallopen = paramBoolean;
    stringBuilder = new StringBuilder();
    stringBuilder.append("isallopen = ");
    stringBuilder.append(this.isallopen);
    Log.e("-", stringBuilder.toString());
    for (String str : this.mBluetoothLeService.MyBluetoothGatts.keySet()) {
      if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(str) && MainActivity.ControlMACs.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2) {
          StringBuilder stringBuilder2 = new StringBuilder();
          stringBuilder2.append("openAll = ");
          stringBuilder2.append(str);
          Log.e("", stringBuilder2.toString());
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append("openAll = ");
          stringBuilder1.append(paramBoolean);
          Log.e("", stringBuilder1.toString());
          if (myBluetoothGatt.getmLEdevice().getName().contains("Dream")) {
            if (this.myMediaPlayer != null && this.myMediaPlayer.isPlaying()) {
              new MySendThread(myBluetoothGatt, paramBoolean);
              continue;
            } 
            if (this.isRecorder) {
              new MySendThread(myBluetoothGatt, paramBoolean);
              continue;
            } 
            myBluetoothGatt.openLight(paramBoolean);
            continue;
          } 
          myBluetoothGatt.openLight(paramBoolean);
        } 
      } 
    } 
  }
  
  public void openmic(boolean paramBoolean) {
    if (this.mBluetoothLeService == null)
      return; 
    for (String str : this.mBluetoothLeService.MyBluetoothGatts.keySet()) {
      if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2 && myBluetoothGatt.mLEdevice != null && !TextUtils.isEmpty(myBluetoothGatt.mLEdevice.getName()) && (myBluetoothGatt.mLEdevice.getName().contains("Triones^") || myBluetoothGatt.mLEdevice.getName().contains("Color+")))
          myBluetoothGatt.openmic(paramBoolean); 
      } 
    } 
  }
  
  public void reSetCMD() {
    if (this.mBluetoothLeService == null)
      return; 
    for (String str : MainActivity.ControlMACs.keySet()) {
      if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2) {
          Pattern pattern = Pattern.compile("^Triones-|^Dream-|^Dream\\*");
          if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2 && myBluetoothGatt.mLEdevice != null && !TextUtils.isEmpty(myBluetoothGatt.mLEdevice.getName()) && myBluetoothGatt.mLEdevice.getName() != null && pattern.matcher(myBluetoothGatt.mLEdevice.getName()).find())
            myBluetoothGatt.reSetCMD(); 
        } 
      } 
    } 
  }
  
  public void readhuancai(String paramString) {
    if (this.mBluetoothLeService == null)
      return; 
    if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(paramString)) {
      MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(paramString);
      if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2)
        myBluetoothGatt.readhuancai(); 
    } 
  }
  
  public void resetpwd(String paramString1, String paramString2) {
    if (this.mBluetoothLeService == null)
      return; 
    if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(paramString1)) {
      MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(paramString1);
      if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2)
        myBluetoothGatt.setpwd(paramString2); 
    } 
  }
  
  public void sendColor_m_data(boolean paramBoolean, int paramInt1, int paramInt2) {
    if (this.mBluetoothLeService == null)
      return; 
    for (String str : this.mBluetoothLeService.MyBluetoothGatts.keySet()) {
      if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2 && myBluetoothGatt.mLEdevice != null && !TextUtils.isEmpty(myBluetoothGatt.mLEdevice.getName()) && myBluetoothGatt.mLEdevice.getName() != null && Pattern.compile("^Color\\+|^Color-|^QHM").matcher(myBluetoothGatt.mLEdevice.getName()).find())
          myBluetoothGatt.sendColor_m_data(paramBoolean, paramInt1, paramInt2); 
      } 
    } 
  }
  
  public void sendColor_m_data(boolean paramBoolean1, int paramInt1, int paramInt2, boolean paramBoolean2) {
    if (this.mBluetoothLeService == null)
      return; 
    for (String str : this.mBluetoothLeService.MyBluetoothGatts.keySet()) {
      if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2 && myBluetoothGatt.mLEdevice != null && !TextUtils.isEmpty(myBluetoothGatt.mLEdevice.getName()) && myBluetoothGatt.mLEdevice.getName() != null && Pattern.compile("^Color\\+|^Color-|^QHM").matcher(myBluetoothGatt.mLEdevice.getName()).find())
          myBluetoothGatt.sendColor_m_data(paramBoolean1, paramInt1, paramInt2, paramBoolean2); 
      } 
    } 
  }
  
  public void sendDimmerData(boolean paramBoolean) {
    if (this.mBluetoothLeService == null)
      return; 
    Pattern pattern = Pattern.compile(this.Dimmer);
    for (String str : this.mBluetoothLeService.MyBluetoothGatts.keySet()) {
      if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2 && myBluetoothGatt.mLEdevice != null && !TextUtils.isEmpty(myBluetoothGatt.mLEdevice.getName()) && MainActivity.ControlMACs.containsKey(str) && pattern.matcher(myBluetoothGatt.mLEdevice.getName()).find())
          myBluetoothGatt.sendDimmer(paramBoolean); 
      } 
    } 
  }
  
  public void sendDreamWM(boolean paramBoolean, int paramInt1, int paramInt2) {
    if (this.mBluetoothLeService == null)
      return; 
    for (String str : this.mBluetoothLeService.MyBluetoothGatts.keySet()) {
      if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2 && myBluetoothGatt.mLEdevice != null && !TextUtils.isEmpty(myBluetoothGatt.mLEdevice.getName()) && myBluetoothGatt.mLEdevice.getName() != null && Pattern.compile("^Dream~").matcher(myBluetoothGatt.mLEdevice.getName()).find()) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("speed = ");
          stringBuilder.append(paramInt1);
          Log.e("---------------", stringBuilder.toString());
          myBluetoothGatt.sendDreamWM(paramBoolean, paramInt1, paramInt2);
        } 
      } 
    } 
  }
  
  public void sendNewMod(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("speed=");
    stringBuilder.append(paramInt2);
    stringBuilder.append(" brightness=");
    stringBuilder.append(paramInt3);
    Log.e("--", stringBuilder.toString());
    if (this.mBluetoothLeService == null)
      return; 
    Pattern pattern = Pattern.compile("^Flash");
    for (String str : this.mBluetoothLeService.MyBluetoothGatts.keySet()) {
      if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2 && myBluetoothGatt.mLEdevice != null && !TextUtils.isEmpty(myBluetoothGatt.mLEdevice.getName()) && MainActivity.ControlMACs.containsKey(str) && pattern.matcher(myBluetoothGatt.mLEdevice.getName()).find())
          myBluetoothGatt.sendNewMod(paramInt1 + 1, paramInt2, paramInt3, paramInt4); 
      } 
    } 
  }
  
  public void setBG(int paramInt) {
    this.typebg = paramInt;
    SharedPreferences.Editor editor = this.settings.edit();
    editor.putInt("typebg", paramInt);
    editor.commit();
  }
  
  public void setData(String paramString, byte[] paramArrayOfbyte) {
    if (this.mBluetoothLeService == null)
      return; 
    if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(paramString)) {
      MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(paramString);
      if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2)
        myBluetoothGatt.setValues(paramArrayOfbyte); 
    } 
  }
  
  public void setData(byte[] paramArrayOfbyte) {
    if (this.mBluetoothLeService == null)
      return; 
    for (String str : MainActivity.ControlMACs.keySet()) {
      if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2)
          myBluetoothGatt.setValues(paramArrayOfbyte); 
      } 
    } 
  }
  
  public void setMusicHop(boolean paramBoolean) {
    this.isOpenMusicHop = paramBoolean;
  }
  
  public void setMusicHop(boolean paramBoolean1, boolean paramBoolean2) {
    this.isOpenMusicHop = paramBoolean1;
    if (paramBoolean2) {
      this.myHandler.removeCallbacks(this.myRunnable);
      this.myHandler.postDelayed(this.myRunnable, 500L);
    } 
  }
  
  public boolean setqicaidata(String paramString, int paramInt1, int paramInt2, boolean paramBoolean) {
    if (this.mBluetoothLeService == null)
      return false; 
    if (this.mBluetoothLeService.MyBluetoothGatts.containsKey(paramString)) {
      MyBluetoothGatt myBluetoothGatt = this.mBluetoothLeService.MyBluetoothGatts.get(paramString);
      if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2) {
        myBluetoothGatt.setqicaidata(paramInt1, paramInt2, paramBoolean);
        return true;
      } 
    } 
    return false;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\blelight\MyApplication.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */