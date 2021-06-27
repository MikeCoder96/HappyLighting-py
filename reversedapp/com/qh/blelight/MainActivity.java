package com.qh.blelight;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TabActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import com.android.soundrecorder.RecordingActivity;
import com.qh.blelight.scroll.SwitchViewActivity;
import com.qh.data.MySendThread;
import com.qh.data.SwitchInterface;
import com.qh.managegroup.DragListActivity;
import com.qh.tools.DBAdapter;
import com.qh.tools.StatusBarUtil;
import com.qh.tools.Tool;
import com.qh.ui.MyDialog;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

@SuppressLint({"NewApi"})
public class MainActivity extends TabActivity {
  public static Hashtable<String, String> ControlMACs = new Hashtable<String, String>();
  
  public static Context context;
  
  public static int heightPixels = 0;
  
  public static TabHost mTabHost;
  
  public static final int mic_msg = 60001;
  
  public static int widthPixels;
  
  public DBAdapter dbAdapter;
  
  private DrawerLayout drawerLayout;
  
  private ArrayList<Integer> groupIDs = new ArrayList<Integer>();
  
  private ArrayList<String> groupNames = new ArrayList<String>();
  
  private ImageView img_adjust;
  
  private ImageView img_list;
  
  private ImageView img_mid;
  
  private ImageView img_mod;
  
  private ImageView img_music;
  
  private ImageView img_recording;
  
  private ImageView img_set;
  
  private ImageView img_timing;
  
  public int isExpandedID = -1;
  
  private boolean isOpenyaoyiyao = false;
  
  public boolean isRequest = false;
  
  private boolean isopenGPS = false;
  
  public boolean issucceed = false;
  
  public boolean ist = true;
  
  private RelativeLayout leftLayout;
  
  public int len = 5;
  
  private RelativeLayout lin_about;
  
  private RelativeLayout lin_change;
  
  public RelativeLayout lin_close;
  
  private RelativeLayout lin_directions;
  
  public RelativeLayout lin_open;
  
  private RelativeLayout lin_sethuancai;
  
  private RelativeLayout lin_setqicai;
  
  private RelativeLayout lin_yaoyiyao;
  
  private BluetoothAdapter mBluetoothAdapter;
  
  private BluetoothLeService mBluetoothLeService;
  
  public BluetoothLeService.Checkpwd mCheckpwd = new BluetoothLeService.Checkpwd() {
      public void checkpwd(String param1String1, int param1Int, String param1String2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("000checkpwd ");
        stringBuilder.append(param1Int);
        Log.e("checkpwd", stringBuilder.toString());
        if (param1Int == 0) {
          Message message = new Message();
          message.what = 4001;
          message.obj = param1String1;
          MainActivity.this.mHandler.sendMessage(message);
        } else if (param1Int == 1) {
          MainActivity.this.mHandler.sendEmptyMessage(4003);
        } 
      }
    };
  
  public Handler mHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message param1Message) {
          Intent intent;
          String str1;
          String str2;
          int i = param1Message.what;
          switch (i) {
            default:
              switch (i) {
                case 4002:
                  str2 = (String)param1Message.obj;
                  intent = new Intent((Context)MainActivity.this, ResetpwdActivity.class);
                  intent.putExtra("addr", str2);
                  MainActivity.this.startActivity(intent);
                  break;
                case 4001:
                  str1 = (String)((Message)intent).obj;
                  MainActivity.this.setpwd(str1);
                  break;
              } 
            case 3:
              switch (MainActivity.this.rb_num) {
                default:
                  return false;
                case 2:
                  if (MainActivity.this.mMyApplication.ModHandler != null)
                    MainActivity.this.mMyApplication.ModHandler.sendEmptyMessage(0); 
                case 1:
                  if (MainActivity.this.mMyApplication.MusicHandler != null)
                    MainActivity.this.mMyApplication.MusicHandler.sendEmptyMessage(0); 
                case 0:
                  break;
              } 
              if (MainActivity.this.mMyApplication.AdjustHandler != null)
                MainActivity.this.mMyApplication.AdjustHandler.sendEmptyMessage(0); 
            case 2:
              if (MainActivity.this.groupNames.size() > ((Message)str1).arg1)
                MainActivity.this.myExpandableListView.collapseGroup(((Message)str1).arg1); 
            case 1:
              break;
          } 
          if (MainActivity.this.groupNames.size() > ((Message)str1).arg1)
            MainActivity.this.myExpandableListView.expandGroup(((Message)str1).arg1); 
        }
      });
  
  public MyApplication mMyApplication;
  
  public MyExpandableListAdapter mMyExpandableListAdapter;
  
  public Handler mOperateHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message param1Message) {
          // Byte code:
          //   0: aload_0
          //   1: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   4: invokevirtual isopenble : ()Z
          //   7: ifeq -> 1795
          //   10: aload_1
          //   11: getfield what : I
          //   14: lookupswitch default -> 80, 0 -> 1782, 1 -> 1738, 3 -> 1027, 4 -> 279, 400 -> 179, 500 -> 131, 501 -> 83
          //   80: goto -> 1802
          //   83: aload_0
          //   84: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   87: iconst_1
          //   88: putfield ist : Z
          //   91: aload_0
          //   92: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   95: iconst_1
          //   96: putfield req : Z
          //   99: aload_0
          //   100: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   103: getfield rel_pro : Landroid/widget/RelativeLayout;
          //   106: bipush #8
          //   108: invokevirtual setVisibility : (I)V
          //   111: aload_0
          //   112: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   115: iconst_1
          //   116: iconst_0
          //   117: invokevirtual add : (ZZ)V
          //   120: aload_0
          //   121: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   124: iconst_0
          //   125: putfield issucceed : Z
          //   128: goto -> 1802
          //   131: aload_0
          //   132: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   135: iconst_1
          //   136: putfield req : Z
          //   139: aload_0
          //   140: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   143: iconst_1
          //   144: putfield issucceed : Z
          //   147: aload_0
          //   148: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   151: iconst_1
          //   152: putfield ist : Z
          //   155: aload_0
          //   156: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   159: getfield rel_pro : Landroid/widget/RelativeLayout;
          //   162: bipush #8
          //   164: invokevirtual setVisibility : (I)V
          //   167: aload_0
          //   168: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   171: iconst_1
          //   172: iconst_1
          //   173: invokevirtual add : (ZZ)V
          //   176: goto -> 1802
          //   179: aload_1
          //   180: invokevirtual getData : ()Landroid/os/Bundle;
          //   183: ldc 'deviceAddr'
          //   185: ldc ''
          //   187: invokevirtual getString : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
          //   190: astore_2
          //   191: aload_0
          //   192: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   195: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   198: getfield mBluetoothLeService : Lcom/qh/blelight/BluetoothLeService;
          //   201: getfield mDevices : Ljava/util/Hashtable;
          //   204: aload_2
          //   205: invokevirtual containsKey : (Ljava/lang/Object;)Z
          //   208: ifeq -> 248
          //   211: aload_0
          //   212: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   215: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   218: getfield mBluetoothLeService : Lcom/qh/blelight/BluetoothLeService;
          //   221: getfield mDevices : Ljava/util/Hashtable;
          //   224: aload_2
          //   225: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
          //   228: checkcast android/bluetooth/BluetoothDevice
          //   231: astore_1
          //   232: aload_0
          //   233: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   236: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   239: getfield errorDevices : Ljava/util/HashMap;
          //   242: aload_2
          //   243: aload_1
          //   244: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
          //   247: pop
          //   248: aload_0
          //   249: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   252: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   255: getfield errorHandler : Landroid/os/Handler;
          //   258: ifnull -> 1802
          //   261: aload_0
          //   262: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   265: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   268: getfield errorHandler : Landroid/os/Handler;
          //   271: iconst_0
          //   272: invokevirtual sendEmptyMessage : (I)Z
          //   275: pop
          //   276: goto -> 1802
          //   279: aload_0
          //   280: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   283: getfield mMyExpandableListAdapter : Lcom/qh/blelight/MyExpandableListAdapter;
          //   286: invokevirtual notifyDataSetChanged : ()V
          //   289: aload_0
          //   290: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   293: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   296: getfield TimingHandler : Landroid/os/Handler;
          //   299: ifnull -> 390
          //   302: aload_1
          //   303: invokevirtual getData : ()Landroid/os/Bundle;
          //   306: ldc 'deviceAddr'
          //   308: ldc ''
          //   310: invokevirtual getString : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
          //   313: astore_3
          //   314: new android/os/Message
          //   317: dup
          //   318: invokespecial <init> : ()V
          //   321: astore_2
          //   322: new android/os/Bundle
          //   325: dup
          //   326: invokespecial <init> : ()V
          //   329: astore_1
          //   330: new java/lang/StringBuilder
          //   333: dup
          //   334: invokespecial <init> : ()V
          //   337: astore #4
          //   339: aload #4
          //   341: ldc ''
          //   343: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   346: pop
          //   347: aload #4
          //   349: aload_3
          //   350: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   353: pop
          //   354: aload_1
          //   355: ldc 'deviceAddr'
          //   357: aload #4
          //   359: invokevirtual toString : ()Ljava/lang/String;
          //   362: invokevirtual putString : (Ljava/lang/String;Ljava/lang/String;)V
          //   365: aload_2
          //   366: aload_1
          //   367: invokevirtual setData : (Landroid/os/Bundle;)V
          //   370: aload_2
          //   371: iconst_4
          //   372: putfield what : I
          //   375: aload_0
          //   376: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   379: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   382: getfield TimingHandler : Landroid/os/Handler;
          //   385: aload_2
          //   386: invokevirtual sendMessage : (Landroid/os/Message;)Z
          //   389: pop
          //   390: aload_0
          //   391: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   394: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   397: iconst_0
          //   398: putfield ishaveDream : Z
          //   401: aload_0
          //   402: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   405: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   408: iconst_0
          //   409: putfield ishaveT : Z
          //   412: aload_0
          //   413: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   416: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   419: iconst_0
          //   420: putfield ishaveqicai : Z
          //   423: aload_0
          //   424: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   427: invokestatic access$300 : (Lcom/qh/blelight/MainActivity;)Lcom/qh/blelight/BluetoothLeService;
          //   430: getfield MyBluetoothGatts : Ljava/util/Hashtable;
          //   433: invokevirtual keySet : ()Ljava/util/Set;
          //   436: invokeinterface iterator : ()Ljava/util/Iterator;
          //   441: astore_1
          //   442: aload_1
          //   443: invokeinterface hasNext : ()Z
          //   448: ifeq -> 686
          //   451: aload_1
          //   452: invokeinterface next : ()Ljava/lang/Object;
          //   457: checkcast java/lang/String
          //   460: astore_2
          //   461: aload_0
          //   462: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   465: invokestatic access$300 : (Lcom/qh/blelight/MainActivity;)Lcom/qh/blelight/BluetoothLeService;
          //   468: getfield MyBluetoothGatts : Ljava/util/Hashtable;
          //   471: aload_2
          //   472: invokevirtual containsKey : (Ljava/lang/Object;)Z
          //   475: ifeq -> 442
          //   478: aload_0
          //   479: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   482: invokestatic access$300 : (Lcom/qh/blelight/MainActivity;)Lcom/qh/blelight/BluetoothLeService;
          //   485: getfield MyBluetoothGatts : Ljava/util/Hashtable;
          //   488: aload_2
          //   489: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
          //   492: checkcast com/qh/blelight/MyBluetoothGatt
          //   495: astore_2
          //   496: aload_2
          //   497: ifnull -> 442
          //   500: aload_2
          //   501: getfield mConnectionState : I
          //   504: iconst_2
          //   505: if_icmpne -> 442
          //   508: aload_2
          //   509: getfield mLEdevice : Landroid/bluetooth/BluetoothDevice;
          //   512: ifnull -> 618
          //   515: aload_2
          //   516: getfield mLEdevice : Landroid/bluetooth/BluetoothDevice;
          //   519: invokevirtual getName : ()Ljava/lang/String;
          //   522: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
          //   525: ifne -> 618
          //   528: ldc '^Color\+|^Color-'
          //   530: invokestatic compile : (Ljava/lang/String;)Ljava/util/regex/Pattern;
          //   533: aload_2
          //   534: getfield mLEdevice : Landroid/bluetooth/BluetoothDevice;
          //   537: invokevirtual getName : ()Ljava/lang/String;
          //   540: invokevirtual matcher : (Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;
          //   543: invokevirtual find : ()Z
          //   546: ifeq -> 563
          //   549: aload_0
          //   550: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   553: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   556: iconst_1
          //   557: putfield ishaveColor : Z
          //   560: goto -> 618
          //   563: aload_2
          //   564: getfield mLEdevice : Landroid/bluetooth/BluetoothDevice;
          //   567: invokevirtual getName : ()Ljava/lang/String;
          //   570: ldc 'Dream'
          //   572: invokevirtual contains : (Ljava/lang/CharSequence;)Z
          //   575: ifeq -> 592
          //   578: aload_0
          //   579: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   582: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   585: iconst_1
          //   586: putfield ishaveDream : Z
          //   589: goto -> 618
          //   592: aload_2
          //   593: getfield mLEdevice : Landroid/bluetooth/BluetoothDevice;
          //   596: invokevirtual getName : ()Ljava/lang/String;
          //   599: ldc 'Flash'
          //   601: invokevirtual contains : (Ljava/lang/CharSequence;)Z
          //   604: ifne -> 618
          //   607: aload_0
          //   608: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   611: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   614: iconst_1
          //   615: putfield ishaveT : Z
          //   618: aload_2
          //   619: ifnull -> 442
          //   622: aload_2
          //   623: getfield mLEdevice : Landroid/bluetooth/BluetoothDevice;
          //   626: ifnull -> 442
          //   629: aload_2
          //   630: getfield mLEdevice : Landroid/bluetooth/BluetoothDevice;
          //   633: invokevirtual getName : ()Ljava/lang/String;
          //   636: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
          //   639: ifne -> 442
          //   642: aload_2
          //   643: getfield mLEdevice : Landroid/bluetooth/BluetoothDevice;
          //   646: invokevirtual getName : ()Ljava/lang/String;
          //   649: ldc 'Triones:'
          //   651: invokevirtual contains : (Ljava/lang/CharSequence;)Z
          //   654: ifne -> 672
          //   657: aload_2
          //   658: getfield mLEdevice : Landroid/bluetooth/BluetoothDevice;
          //   661: invokevirtual getName : ()Ljava/lang/String;
          //   664: ldc 'Triones#'
          //   666: invokevirtual contains : (Ljava/lang/CharSequence;)Z
          //   669: ifeq -> 442
          //   672: aload_0
          //   673: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   676: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   679: iconst_1
          //   680: putfield ishaveqicai : Z
          //   683: goto -> 442
          //   686: aload_0
          //   687: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   690: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   693: getfield ishaveDream : Z
          //   696: ifne -> 730
          //   699: aload_0
          //   700: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   703: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   706: invokevirtual isHaveX : ()Z
          //   709: ifeq -> 715
          //   712: goto -> 730
          //   715: aload_0
          //   716: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   719: invokestatic access$400 : (Lcom/qh/blelight/MainActivity;)Landroid/widget/RelativeLayout;
          //   722: bipush #8
          //   724: invokevirtual setVisibility : (I)V
          //   727: goto -> 741
          //   730: aload_0
          //   731: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   734: invokestatic access$400 : (Lcom/qh/blelight/MainActivity;)Landroid/widget/RelativeLayout;
          //   737: iconst_0
          //   738: invokevirtual setVisibility : (I)V
          //   741: aload_0
          //   742: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   745: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   748: getfield ishaveqicai : Z
          //   751: ifeq -> 768
          //   754: aload_0
          //   755: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   758: invokestatic access$500 : (Lcom/qh/blelight/MainActivity;)Landroid/widget/RelativeLayout;
          //   761: iconst_0
          //   762: invokevirtual setVisibility : (I)V
          //   765: goto -> 780
          //   768: aload_0
          //   769: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   772: invokestatic access$500 : (Lcom/qh/blelight/MainActivity;)Landroid/widget/RelativeLayout;
          //   775: bipush #8
          //   777: invokevirtual setVisibility : (I)V
          //   780: aload_0
          //   781: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   784: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   787: invokevirtual isopenmic2 : ()Z
          //   790: ifeq -> 818
          //   793: aload_0
          //   794: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   797: getfield lin_open : Landroid/widget/RelativeLayout;
          //   800: iconst_0
          //   801: invokevirtual setVisibility : (I)V
          //   804: aload_0
          //   805: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   808: getfield lin_close : Landroid/widget/RelativeLayout;
          //   811: iconst_0
          //   812: invokevirtual setVisibility : (I)V
          //   815: goto -> 842
          //   818: aload_0
          //   819: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   822: getfield lin_open : Landroid/widget/RelativeLayout;
          //   825: bipush #8
          //   827: invokevirtual setVisibility : (I)V
          //   830: aload_0
          //   831: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   834: getfield lin_close : Landroid/widget/RelativeLayout;
          //   837: bipush #8
          //   839: invokevirtual setVisibility : (I)V
          //   842: aload_0
          //   843: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   846: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   849: getfield AdjustHandler : Landroid/os/Handler;
          //   852: ifnull -> 870
          //   855: aload_0
          //   856: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   859: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   862: getfield AdjustHandler : Landroid/os/Handler;
          //   865: iconst_4
          //   866: invokevirtual sendEmptyMessage : (I)Z
          //   869: pop
          //   870: new java/lang/StringBuilder
          //   873: astore_1
          //   874: aload_1
          //   875: invokespecial <init> : ()V
          //   878: aload_1
          //   879: ldc_w 'ishaveDream='
          //   882: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   885: pop
          //   886: aload_1
          //   887: aload_0
          //   888: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   891: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   894: getfield ishaveDream : Z
          //   897: invokevirtual append : (Z)Ljava/lang/StringBuilder;
          //   900: pop
          //   901: ldc_w '--'
          //   904: aload_1
          //   905: invokevirtual toString : ()Ljava/lang/String;
          //   908: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
          //   911: pop
          //   912: aload_0
          //   913: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   916: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   919: getfield ModHandler : Landroid/os/Handler;
          //   922: ifnull -> 940
          //   925: aload_0
          //   926: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   929: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   932: getfield ModHandler : Landroid/os/Handler;
          //   935: iconst_1
          //   936: invokevirtual sendEmptyMessage : (I)Z
          //   939: pop
          //   940: aload_0
          //   941: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   944: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   947: getfield RecordingHandler : Landroid/os/Handler;
          //   950: ifnull -> 968
          //   953: aload_0
          //   954: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   957: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   960: getfield RecordingHandler : Landroid/os/Handler;
          //   963: iconst_0
          //   964: invokevirtual sendEmptyMessage : (I)Z
          //   967: pop
          //   968: aload_0
          //   969: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   972: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   975: getfield huancaiHandler : Landroid/os/Handler;
          //   978: ifnull -> 996
          //   981: aload_0
          //   982: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   985: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   988: getfield huancaiHandler : Landroid/os/Handler;
          //   991: iconst_4
          //   992: invokevirtual sendEmptyMessage : (I)Z
          //   995: pop
          //   996: aload_0
          //   997: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1000: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   1003: getfield qicaiHandler : Landroid/os/Handler;
          //   1006: ifnull -> 1802
          //   1009: aload_0
          //   1010: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1013: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   1016: getfield qicaiHandler : Landroid/os/Handler;
          //   1019: iconst_4
          //   1020: invokevirtual sendEmptyMessage : (I)Z
          //   1023: pop
          //   1024: goto -> 1802
          //   1027: aload_1
          //   1028: invokevirtual getData : ()Landroid/os/Bundle;
          //   1031: ldc 'deviceAddr'
          //   1033: ldc ''
          //   1035: invokevirtual getString : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
          //   1038: astore_2
          //   1039: new java/lang/StringBuilder
          //   1042: dup
          //   1043: invokespecial <init> : ()V
          //   1046: astore_3
          //   1047: aload_3
          //   1048: ldc_w '--'
          //   1051: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   1054: pop
          //   1055: aload_3
          //   1056: aload_2
          //   1057: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   1060: pop
          //   1061: ldc ''
          //   1063: aload_3
          //   1064: invokevirtual toString : ()Ljava/lang/String;
          //   1067: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
          //   1070: pop
          //   1071: aload_0
          //   1072: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1075: getfield mMyExpandableListAdapter : Lcom/qh/blelight/MyExpandableListAdapter;
          //   1078: invokevirtual notifyDataSetChanged : ()V
          //   1081: aload_0
          //   1082: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1085: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   1088: getfield TimingHandler : Landroid/os/Handler;
          //   1091: ifnull -> 1180
          //   1094: aload_1
          //   1095: invokevirtual getData : ()Landroid/os/Bundle;
          //   1098: ldc 'deviceAddr'
          //   1100: ldc ''
          //   1102: invokevirtual getString : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
          //   1105: astore #4
          //   1107: new android/os/Message
          //   1110: dup
          //   1111: invokespecial <init> : ()V
          //   1114: astore_2
          //   1115: new android/os/Bundle
          //   1118: dup
          //   1119: invokespecial <init> : ()V
          //   1122: astore_1
          //   1123: new java/lang/StringBuilder
          //   1126: dup
          //   1127: invokespecial <init> : ()V
          //   1130: astore_3
          //   1131: aload_3
          //   1132: ldc ''
          //   1134: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   1137: pop
          //   1138: aload_3
          //   1139: aload #4
          //   1141: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   1144: pop
          //   1145: aload_1
          //   1146: ldc 'deviceAddr'
          //   1148: aload_3
          //   1149: invokevirtual toString : ()Ljava/lang/String;
          //   1152: invokevirtual putString : (Ljava/lang/String;Ljava/lang/String;)V
          //   1155: aload_2
          //   1156: aload_1
          //   1157: invokevirtual setData : (Landroid/os/Bundle;)V
          //   1160: aload_2
          //   1161: iconst_3
          //   1162: putfield what : I
          //   1165: aload_0
          //   1166: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1169: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   1172: getfield TimingHandler : Landroid/os/Handler;
          //   1175: aload_2
          //   1176: invokevirtual sendMessage : (Landroid/os/Message;)Z
          //   1179: pop
          //   1180: aload_0
          //   1181: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1184: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   1187: iconst_0
          //   1188: putfield ishaveDream : Z
          //   1191: aload_0
          //   1192: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1195: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   1198: iconst_0
          //   1199: putfield ishaveT : Z
          //   1202: aload_0
          //   1203: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1206: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   1209: iconst_0
          //   1210: putfield ishaveqicai : Z
          //   1213: aload_0
          //   1214: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1217: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   1220: iconst_0
          //   1221: putfield ishaveColor : Z
          //   1224: aload_0
          //   1225: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1228: invokestatic access$300 : (Lcom/qh/blelight/MainActivity;)Lcom/qh/blelight/BluetoothLeService;
          //   1231: getfield MyBluetoothGatts : Ljava/util/Hashtable;
          //   1234: invokevirtual keySet : ()Ljava/util/Set;
          //   1237: invokeinterface iterator : ()Ljava/util/Iterator;
          //   1242: astore_1
          //   1243: aload_1
          //   1244: invokeinterface hasNext : ()Z
          //   1249: ifeq -> 1443
          //   1252: aload_1
          //   1253: invokeinterface next : ()Ljava/lang/Object;
          //   1258: checkcast java/lang/String
          //   1261: astore_2
          //   1262: aload_0
          //   1263: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1266: invokestatic access$300 : (Lcom/qh/blelight/MainActivity;)Lcom/qh/blelight/BluetoothLeService;
          //   1269: getfield MyBluetoothGatts : Ljava/util/Hashtable;
          //   1272: aload_2
          //   1273: invokevirtual containsKey : (Ljava/lang/Object;)Z
          //   1276: ifeq -> 1243
          //   1279: aload_0
          //   1280: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1283: invokestatic access$300 : (Lcom/qh/blelight/MainActivity;)Lcom/qh/blelight/BluetoothLeService;
          //   1286: getfield MyBluetoothGatts : Ljava/util/Hashtable;
          //   1289: aload_2
          //   1290: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
          //   1293: checkcast com/qh/blelight/MyBluetoothGatt
          //   1296: astore_2
          //   1297: aload_2
          //   1298: ifnull -> 1243
          //   1301: aload_2
          //   1302: getfield mConnectionState : I
          //   1305: iconst_2
          //   1306: if_icmpne -> 1243
          //   1309: ldc '^Color\+|^Color-'
          //   1311: invokestatic compile : (Ljava/lang/String;)Ljava/util/regex/Pattern;
          //   1314: aload_2
          //   1315: getfield mLEdevice : Landroid/bluetooth/BluetoothDevice;
          //   1318: invokevirtual getName : ()Ljava/lang/String;
          //   1321: invokevirtual matcher : (Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;
          //   1324: invokevirtual find : ()Z
          //   1327: ifeq -> 1344
          //   1330: aload_0
          //   1331: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1334: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   1337: iconst_1
          //   1338: putfield ishaveColor : Z
          //   1341: goto -> 1399
          //   1344: aload_2
          //   1345: getfield mLEdevice : Landroid/bluetooth/BluetoothDevice;
          //   1348: invokevirtual getName : ()Ljava/lang/String;
          //   1351: ldc 'Dream'
          //   1353: invokevirtual contains : (Ljava/lang/CharSequence;)Z
          //   1356: ifeq -> 1373
          //   1359: aload_0
          //   1360: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1363: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   1366: iconst_1
          //   1367: putfield ishaveDream : Z
          //   1370: goto -> 1399
          //   1373: aload_2
          //   1374: getfield mLEdevice : Landroid/bluetooth/BluetoothDevice;
          //   1377: invokevirtual getName : ()Ljava/lang/String;
          //   1380: ldc 'Flash'
          //   1382: invokevirtual contains : (Ljava/lang/CharSequence;)Z
          //   1385: ifne -> 1399
          //   1388: aload_0
          //   1389: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1392: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   1395: iconst_1
          //   1396: putfield ishaveT : Z
          //   1399: aload_2
          //   1400: getfield mLEdevice : Landroid/bluetooth/BluetoothDevice;
          //   1403: invokevirtual getName : ()Ljava/lang/String;
          //   1406: ldc 'Triones:'
          //   1408: invokevirtual contains : (Ljava/lang/CharSequence;)Z
          //   1411: ifne -> 1429
          //   1414: aload_2
          //   1415: getfield mLEdevice : Landroid/bluetooth/BluetoothDevice;
          //   1418: invokevirtual getName : ()Ljava/lang/String;
          //   1421: ldc 'Triones#'
          //   1423: invokevirtual contains : (Ljava/lang/CharSequence;)Z
          //   1426: ifeq -> 1243
          //   1429: aload_0
          //   1430: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1433: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   1436: iconst_1
          //   1437: putfield ishaveqicai : Z
          //   1440: goto -> 1243
          //   1443: aload_0
          //   1444: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1447: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   1450: getfield ishaveDream : Z
          //   1453: ifne -> 1487
          //   1456: aload_0
          //   1457: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1460: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   1463: invokevirtual isHaveX : ()Z
          //   1466: ifeq -> 1472
          //   1469: goto -> 1487
          //   1472: aload_0
          //   1473: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1476: invokestatic access$400 : (Lcom/qh/blelight/MainActivity;)Landroid/widget/RelativeLayout;
          //   1479: bipush #8
          //   1481: invokevirtual setVisibility : (I)V
          //   1484: goto -> 1498
          //   1487: aload_0
          //   1488: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1491: invokestatic access$400 : (Lcom/qh/blelight/MainActivity;)Landroid/widget/RelativeLayout;
          //   1494: iconst_0
          //   1495: invokevirtual setVisibility : (I)V
          //   1498: aload_0
          //   1499: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1502: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   1505: getfield ishaveqicai : Z
          //   1508: ifeq -> 1525
          //   1511: aload_0
          //   1512: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1515: invokestatic access$500 : (Lcom/qh/blelight/MainActivity;)Landroid/widget/RelativeLayout;
          //   1518: iconst_0
          //   1519: invokevirtual setVisibility : (I)V
          //   1522: goto -> 1537
          //   1525: aload_0
          //   1526: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1529: invokestatic access$500 : (Lcom/qh/blelight/MainActivity;)Landroid/widget/RelativeLayout;
          //   1532: bipush #8
          //   1534: invokevirtual setVisibility : (I)V
          //   1537: aload_0
          //   1538: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1541: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   1544: invokevirtual isopenmic2 : ()Z
          //   1547: ifeq -> 1575
          //   1550: aload_0
          //   1551: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1554: getfield lin_open : Landroid/widget/RelativeLayout;
          //   1557: iconst_0
          //   1558: invokevirtual setVisibility : (I)V
          //   1561: aload_0
          //   1562: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1565: getfield lin_close : Landroid/widget/RelativeLayout;
          //   1568: iconst_0
          //   1569: invokevirtual setVisibility : (I)V
          //   1572: goto -> 1599
          //   1575: aload_0
          //   1576: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1579: getfield lin_open : Landroid/widget/RelativeLayout;
          //   1582: bipush #8
          //   1584: invokevirtual setVisibility : (I)V
          //   1587: aload_0
          //   1588: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1591: getfield lin_close : Landroid/widget/RelativeLayout;
          //   1594: bipush #8
          //   1596: invokevirtual setVisibility : (I)V
          //   1599: aload_0
          //   1600: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1603: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   1606: getfield AdjustHandler : Landroid/os/Handler;
          //   1609: ifnull -> 1627
          //   1612: aload_0
          //   1613: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1616: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   1619: getfield AdjustHandler : Landroid/os/Handler;
          //   1622: iconst_4
          //   1623: invokevirtual sendEmptyMessage : (I)Z
          //   1626: pop
          //   1627: new java/lang/StringBuilder
          //   1630: dup
          //   1631: invokespecial <init> : ()V
          //   1634: astore_1
          //   1635: aload_1
          //   1636: ldc_w 'ishaveDream='
          //   1639: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   1642: pop
          //   1643: aload_1
          //   1644: aload_0
          //   1645: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1648: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   1651: getfield ishaveDream : Z
          //   1654: invokevirtual append : (Z)Ljava/lang/StringBuilder;
          //   1657: pop
          //   1658: ldc_w '--'
          //   1661: aload_1
          //   1662: invokevirtual toString : ()Ljava/lang/String;
          //   1665: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
          //   1668: pop
          //   1669: aload_0
          //   1670: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1673: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   1676: getfield ModHandler : Landroid/os/Handler;
          //   1679: ifnull -> 1697
          //   1682: aload_0
          //   1683: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1686: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   1689: getfield ModHandler : Landroid/os/Handler;
          //   1692: iconst_1
          //   1693: invokevirtual sendEmptyMessage : (I)Z
          //   1696: pop
          //   1697: aload_0
          //   1698: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1701: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   1704: getfield RecordingHandler : Landroid/os/Handler;
          //   1707: ifnull -> 1725
          //   1710: aload_0
          //   1711: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1714: getfield mMyApplication : Lcom/qh/blelight/MyApplication;
          //   1717: getfield RecordingHandler : Landroid/os/Handler;
          //   1720: iconst_0
          //   1721: invokevirtual sendEmptyMessage : (I)Z
          //   1724: pop
          //   1725: ldc_w '  = = =  '
          //   1728: ldc_w 'handleMessage: 3333333333'
          //   1731: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
          //   1734: pop
          //   1735: goto -> 1802
          //   1738: aload_0
          //   1739: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1742: invokevirtual setListData : ()V
          //   1745: aload_0
          //   1746: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1749: getfield mMyExpandableListAdapter : Lcom/qh/blelight/MyExpandableListAdapter;
          //   1752: aload_0
          //   1753: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1756: invokestatic access$000 : (Lcom/qh/blelight/MainActivity;)Ljava/util/ArrayList;
          //   1759: aload_0
          //   1760: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1763: invokestatic access$200 : (Lcom/qh/blelight/MainActivity;)Ljava/util/ArrayList;
          //   1766: invokevirtual setgroupNames : (Ljava/util/ArrayList;Ljava/util/ArrayList;)V
          //   1769: aload_0
          //   1770: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1773: getfield mMyExpandableListAdapter : Lcom/qh/blelight/MyExpandableListAdapter;
          //   1776: invokevirtual notifyDataSetChanged : ()V
          //   1779: goto -> 1802
          //   1782: aload_0
          //   1783: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1786: getfield mMyExpandableListAdapter : Lcom/qh/blelight/MyExpandableListAdapter;
          //   1789: invokevirtual notifyDataSetChanged : ()V
          //   1792: goto -> 1802
          //   1795: aload_0
          //   1796: getfield this$0 : Lcom/qh/blelight/MainActivity;
          //   1799: invokevirtual openble : ()V
          //   1802: iconst_0
          //   1803: ireturn
          //   1804: astore_1
          //   1805: goto -> 940
          // Exception table:
          //   from	to	target	type
          //   390	442	1804	java/util/ConcurrentModificationException
          //   442	496	1804	java/util/ConcurrentModificationException
          //   500	560	1804	java/util/ConcurrentModificationException
          //   563	589	1804	java/util/ConcurrentModificationException
          //   592	618	1804	java/util/ConcurrentModificationException
          //   622	672	1804	java/util/ConcurrentModificationException
          //   672	683	1804	java/util/ConcurrentModificationException
          //   686	712	1804	java/util/ConcurrentModificationException
          //   715	727	1804	java/util/ConcurrentModificationException
          //   730	741	1804	java/util/ConcurrentModificationException
          //   741	765	1804	java/util/ConcurrentModificationException
          //   768	780	1804	java/util/ConcurrentModificationException
          //   780	815	1804	java/util/ConcurrentModificationException
          //   818	842	1804	java/util/ConcurrentModificationException
          //   842	870	1804	java/util/ConcurrentModificationException
          //   870	940	1804	java/util/ConcurrentModificationException
        }
      });
  
  public Resources mResources;
  
  public Handler mainHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message param1Message) {
          if (param1Message.what == 60001) {
            MainActivity.this.mMyApplication.isopenmic = false;
            MainActivity.this.uimic();
            if (MainActivity.this.mMyApplication.AdjustHandler != null)
              MainActivity.this.mMyApplication.AdjustHandler.sendEmptyMessage(5); 
          } 
          return false;
        }
      });
  
  private RadioGroup main_radio;
  
  public MyDialog mreNameDialog;
  
  private SwitchInterface msetSwitchInterface = new SwitchInterface() {
      public void LightSwitch(String param1String, boolean param1Boolean) {
        MainActivity.this.mMyApplication.Open = param1Boolean;
        if (MainActivity.this.mBluetoothLeService.MyBluetoothGatts.containsKey(param1String)) {
          MyBluetoothGatt myBluetoothGatt = MainActivity.this.mBluetoothLeService.MyBluetoothGatts.get(param1String);
          if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2)
            if (myBluetoothGatt.getmLEdevice().getName().contains("Dream")) {
              if (MainActivity.this.mMyApplication.myMediaPlayer != null && MainActivity.this.mMyApplication.myMediaPlayer.isPlaying()) {
                new MySendThread(myBluetoothGatt, param1Boolean);
              } else if (MainActivity.this.mMyApplication.isRecorder) {
                new MySendThread(myBluetoothGatt, param1Boolean);
              } else {
                myBluetoothGatt.openLight(param1Boolean);
              } 
            } else {
              myBluetoothGatt.openLight(param1Boolean);
            }  
        } 
      }
    };
  
  public ExpandableListView myExpandableListView;
  
  public View.OnClickListener myOnClickListener = new View.OnClickListener() {
      public void onClick(View param1View) {
        int i = param1View.getId();
        if (i != 2131230960) {
          if (i == 2131230968 && MainActivity.this.ist) {
            MainActivity.this.ist = false;
            MainActivity.this.req = false;
            MainActivity.this.issucceed = false;
            MainActivity.this.showDialog(true);
            MainActivity.this.mMyApplication.sendDimmerData(true);
          } 
        } else if (MainActivity.this.ist) {
          MainActivity.this.ist = false;
          MainActivity.this.req = false;
          MainActivity.this.issucceed = false;
          MainActivity.this.showDialog(false);
          MainActivity.this.mMyApplication.sendDimmerData(false);
        } 
      }
    };
  
  private LinearLayout panelContent;
  
  private LinearLayout panelContent1;
  
  private LinearLayout rb_adjust;
  
  private LinearLayout rb_mod;
  
  private LinearLayout rb_music;
  
  private int rb_num = 0;
  
  private LinearLayout rb_recording;
  
  private LinearLayout rb_timing;
  
  private RelativeLayout rel_management;
  
  private RelativeLayout rel_math;
  
  private RelativeLayout rel_permission;
  
  public RelativeLayout rel_pro;
  
  public boolean req = false;
  
  private RelativeLayout rightLayout;
  
  private ServiceConnection sc;
  
  private SensorEventListener sensorEventListener = new SensorEventListener() {
      private long time = 0L;
      
      public void onAccuracyChanged(Sensor param1Sensor, int param1Int) {}
      
      public void onSensorChanged(SensorEvent param1SensorEvent) {
        float[] arrayOfFloat = param1SensorEvent.values;
        float f1 = arrayOfFloat[0];
        float f2 = arrayOfFloat[1];
        f2 = arrayOfFloat[2];
        if (Math.abs(f1) > 7.3F) {
          Date date = new Date();
          if (date.getTime() - this.time >= 700L) {
            MainActivity.this.vibrator.vibrate(100L);
            Message message = new Message();
            message.what = 3;
            MainActivity.this.mHandler.sendMessage(message);
            this.time = date.getTime();
          } 
        } 
      }
    };
  
  private SensorManager sensorManager;
  
  public SharedPreferences settings;
  
  private TextView tv_finish;
  
  private TextView tv_goto;
  
  private TextView tx_adjust;
  
  private TextView tx_mod;
  
  private TextView tx_music;
  
  private TextView tx_recording;
  
  private TextView tx_timing;
  
  private int type = 0;
  
  private Vibrator vibrator;
  
  private ImageView yao_open;
  
  public static TabHost getmTabHost() {
    return mTabHost;
  }
  
  private void init() {
    this.myExpandableListView = (ExpandableListView)findViewById(2131230949);
    this.mMyExpandableListAdapter = new MyExpandableListAdapter(context, (Activity)this, this.dbAdapter, this.mHandler, this.mBluetoothLeService);
    this.mMyExpandableListAdapter.setSwitchInterface(this.msetSwitchInterface);
    this.mMyExpandableListAdapter.setgroupNames(this.groupNames, this.groupIDs);
    this.myExpandableListView.setAdapter((ExpandableListAdapter)this.mMyExpandableListAdapter);
    this.mMyApplication.mMyExpandableListAdapter = this.mMyExpandableListAdapter;
    this.myExpandableListView.expandGroup(0);
    this.myExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
          public void onGroupExpand(int param1Int) {
            Log.e(" = ==    ", "onGroupExpand: ");
            int i = MainActivity.this.myExpandableListView.getExpandableListAdapter().getGroupCount();
            for (byte b = 0; b < i; b++);
            MainActivity.this.isExpandedID = param1Int;
          }
        });
    this.rel_permission = (RelativeLayout)findViewById(2131231078);
    this.rel_permission.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {}
        });
    this.tv_goto = (TextView)findViewById(2131231210);
    this.tv_goto.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            Intent intent = new Intent();
            intent.addFlags(536870912);
            if (Build.VERSION.SDK_INT >= 9) {
              intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
              intent.setData(Uri.fromParts("package", MainActivity.this.getPackageName(), null));
              MainActivity.this.startActivity(intent);
            } 
          }
        });
    this.tv_finish = (TextView)findViewById(2131231208);
    this.tv_finish.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setFlags(268435456);
            intent.addCategory("android.intent.category.HOME");
            MainActivity.this.startActivity(intent);
          }
        });
  }
  
  private void setSensorManagerListener(boolean paramBoolean) {
    if (this.sensorManager != null)
      if (paramBoolean) {
        this.sensorManager.registerListener(this.sensorEventListener, this.sensorManager.getDefaultSensor(1), 3);
      } else {
        this.sensorManager.unregisterListener(this.sensorEventListener);
      }  
  }
  
  private void showMultiBtnDialog() {
    if (this.mMyApplication.isshow == true)
      return; 
    startActivity(new Intent((Context)this, UnlawfulActivity.class));
    this.mMyApplication.isshow = true;
  }
  
  public void add(boolean paramBoolean1, boolean paramBoolean2) {
    if (this.mreNameDialog != null && this.mreNameDialog.isShowing())
      return; 
    View view = getLayoutInflater().inflate(2131361849, null);
    TextView textView1 = (TextView)view.findViewById(2131231195);
    TextView textView2 = (TextView)view.findViewById(2131231213);
    if (paramBoolean2) {
      textView2.setText(2131624051);
    } else {
      textView2.setText(2131624046);
    } 
    this.mreNameDialog = new MyDialog((Context)this, 0, 0, view, 2131689636);
    this.mreNameDialog.setCancelable(true);
    this.mreNameDialog.show();
    textView1.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            MainActivity.this.mreNameDialog.cancel();
          }
        });
  }
  
  public void addMarginTopEqualStatusBarHeight(@NonNull View paramView) {
    if (Build.VERSION.SDK_INT < 19)
      return; 
    Object object = paramView.getTag(-123);
    if (object != null && ((Boolean)object).booleanValue())
      return; 
    object = paramView.getLayoutParams();
    object.setMargins(((ViewGroup.MarginLayoutParams)object).leftMargin, ((ViewGroup.MarginLayoutParams)object).topMargin + getStatusBarHeight(), ((ViewGroup.MarginLayoutParams)object).rightMargin, ((ViewGroup.MarginLayoutParams)object).bottomMargin);
    paramView.setTag(-123, Boolean.valueOf(true));
  }
  
  public int getStatusBarHeight() {
    int i = getResources().getIdentifier("status_bar_height", "dimen", "android");
    if (i > 0) {
      i = getResources().getDimensionPixelSize(i);
    } else {
      i = 0;
    } 
    return i;
  }
  
  @SuppressLint({"NewApi"})
  public void initialize() {
    this.mBluetoothAdapter = ((BluetoothManager)getSystemService("bluetooth")).getAdapter();
    openble();
    this.sc = new ServiceConnection() {
        public void onServiceConnected(ComponentName param1ComponentName, IBinder param1IBinder) {
          MainActivity.access$302(MainActivity.this, ((BluetoothLeService.LocalBinder)param1IBinder).getService());
          if (MainActivity.this.mBluetoothLeService == null)
            return; 
          MainActivity.this.mMyApplication.mBluetoothLeService = MainActivity.this.mBluetoothLeService;
          MainActivity.this.mBluetoothLeService.scanLeDevice(true);
          MainActivity.this.mBluetoothLeService.setOperateHandler(MainActivity.this.mOperateHandler);
          MainActivity.this.mBluetoothLeService.mCheckpwd = MainActivity.this.mCheckpwd;
          MainActivity.this.init();
        }
        
        public void onServiceDisconnected(ComponentName param1ComponentName) {}
      };
    Intent intent = new Intent(getApplicationContext(), BluetoothLeService.class);
    getApplicationContext().bindService(intent, this.sc, 1);
  }
  
  public boolean isopenble() {
    return (this.mBluetoothAdapter == null) ? false : this.mBluetoothAdapter.isEnabled();
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
    if (paramInt1 == 100) {
      setListData();
      if (this.mMyExpandableListAdapter == null)
        this.mMyExpandableListAdapter = new MyExpandableListAdapter(context, (Activity)this, this.dbAdapter, this.mHandler, this.mBluetoothLeService); 
      this.mMyExpandableListAdapter.setgroupNames(this.groupNames, this.groupIDs);
      this.myExpandableListView.setAdapter((ExpandableListAdapter)this.mMyExpandableListAdapter);
      this.mMyApplication.mMyExpandableListAdapter = this.mMyExpandableListAdapter;
    } 
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131361828);
    StatusBarUtil.setRootViewFitsSystemWindows((Activity)this, false);
    StatusBarUtil.setTranslucentStatus((Activity)this);
    context = (Context)this;
    this.mResources = getResources();
    mTabHost = getTabHost();
    mTabHost.getTabWidget().setStripEnabled(false);
    this.mMyApplication = (MyApplication)getApplication();
    this.dbAdapter = DBAdapter.init((Context)this);
    this.dbAdapter.open();
    setListData();
    this.settings = getSharedPreferences("BleLight", 0);
    this.mMyApplication.setMusicHop(this.settings.getBoolean("isOpenMusicHop", false), true);
    this.mMyApplication.mainHandler = this.mainHandler;
    DisplayMetrics displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    widthPixels = displayMetrics.widthPixels;
    heightPixels = displayMetrics.heightPixels;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("widthPixels=");
    stringBuilder.append(widthPixels);
    stringBuilder.append(" heightPixels=");
    stringBuilder.append(heightPixels);
    Log.e("--", stringBuilder.toString());
    initialize();
    mTabHost.addTab(mTabHost.newTabSpec("TAG1").setIndicator("0").setContent(new Intent((Context)this, AdjustActivity.class)));
    mTabHost.addTab(mTabHost.newTabSpec("TAG2").setIndicator("1").setContent(new Intent((Context)this, MusicActivity.class)));
    mTabHost.addTab(mTabHost.newTabSpec("TAG3").setIndicator("2").setContent(new Intent((Context)this, ModActivity.class)));
    mTabHost.addTab(mTabHost.newTabSpec("TAG4").setIndicator("3").setContent(new Intent((Context)this, RecordingActivity.class)));
    mTabHost.addTab(mTabHost.newTabSpec("TAG5").setIndicator("4").setContent(new Intent((Context)this, TimingActivity.class)));
    this.main_radio = (RadioGroup)findViewById(2131230984);
    this.main_radio = (RadioGroup)findViewById(2131230984);
    this.rb_adjust = (LinearLayout)findViewById(2131230750);
    this.rb_music = (LinearLayout)findViewById(2131230993);
    this.rb_mod = (LinearLayout)findViewById(2131230990);
    this.rb_recording = (LinearLayout)findViewById(2131231044);
    this.rb_timing = (LinearLayout)findViewById(2131231177);
    this.rel_management = (RelativeLayout)findViewById(2131231071);
    this.panelContent = (LinearLayout)findViewById(2131231008);
    this.panelContent1 = (LinearLayout)findViewById(2131231009);
    this.lin_directions = (RelativeLayout)findViewById(2131230964);
    this.lin_sethuancai = (RelativeLayout)findViewById(2131230970);
    this.lin_setqicai = (RelativeLayout)findViewById(2131230971);
    this.rel_math = (RelativeLayout)findViewById(2131231072);
    this.lin_change = (RelativeLayout)findViewById(2131230959);
    this.lin_about = (RelativeLayout)findViewById(2131230956);
    this.lin_yaoyiyao = (RelativeLayout)findViewById(2131230973);
    this.yao_open = (ImageView)findViewById(2131231278);
    this.img_adjust = (ImageView)findViewById(2131230862);
    this.img_music = (ImageView)findViewById(2131230914);
    this.img_mod = (ImageView)findViewById(2131230900);
    this.img_recording = (ImageView)findViewById(2131230921);
    this.img_timing = (ImageView)findViewById(2131230929);
    this.tx_adjust = (TextView)findViewById(2131231238);
    this.tx_music = (TextView)findViewById(2131231248);
    this.tx_mod = (TextView)findViewById(2131231246);
    this.tx_recording = (TextView)findViewById(2131231250);
    this.tx_timing = (TextView)findViewById(2131231255);
    this.img_list = (ImageView)findViewById(2131230897);
    this.img_set = (ImageView)findViewById(2131230923);
    this.img_mid = (ImageView)findViewById(2131230899);
    this.drawerLayout = (DrawerLayout)findViewById(2131230788);
    this.leftLayout = (RelativeLayout)findViewById(2131230948);
    this.rightLayout = (RelativeLayout)findViewById(2131231102);
    setViewLayoutParams((View)this.leftLayout, widthPixels / 4 * 3, -1);
    setViewLayoutParams((View)this.rightLayout, widthPixels / 4 * 3, -1);
    addMarginTopEqualStatusBarHeight((View)this.panelContent);
    addMarginTopEqualStatusBarHeight((View)this.panelContent1);
    if (this.mMyApplication.bgsrc.length > this.mMyApplication.typebg)
      this.rel_math.setBackgroundResource(this.mMyApplication.bgsrc[this.mMyApplication.typebg]); 
    this.lin_open = (RelativeLayout)findViewById(2131230968);
    this.lin_close = (RelativeLayout)findViewById(2131230960);
    this.lin_open.setOnClickListener(this.myOnClickListener);
    this.lin_close.setOnClickListener(this.myOnClickListener);
    this.drawerLayout.setScrimColor(0);
    this.tx_adjust.setTextColor(-11872414);
    this.rb_adjust.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            MainActivity.mTabHost.setCurrentTab(0);
            MainActivity.this.setTabBackground(MainActivity.this.type);
            MainActivity.this.mMyApplication.isRecorder = false;
            MainActivity.this.img_adjust.setImageResource(2131165331);
            MainActivity.access$602(MainActivity.this, 0);
            MainActivity.this.tx_adjust.setTextColor(-11872414);
            MainActivity.access$102(MainActivity.this, 0);
            boolean bool = MainActivity.this.mMyApplication.mMediaRecorderDemo.isPlay;
            MainActivity.this.mMyApplication.mMediaRecorderDemo.stopRecord();
            if (bool)
              MainActivity.this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                      MainActivity.this.mMyApplication.reSetCMD();
                    }
                  },  500L); 
          }
        });
    this.rb_music.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            MainActivity.this.mMyApplication.isRecorder = false;
            if (Build.VERSION.SDK_INT >= 23) {
              if (MainActivity.this.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != 0) {
                MainActivity.this.requestPermissions(new String[] { "android.permission.READ_EXTERNAL_STORAGE" }, 1011);
                return;
              } 
              if (MainActivity.this.checkSelfPermission("android.permission.RECORD_AUDIO") != 0) {
                MainActivity.this.requestPermissions(new String[] { "android.permission.RECORD_AUDIO" }, 1009);
                return;
              } 
            } 
            if (Environment.getExternalStorageState().equals("mounted")) {
              Environment.getExternalStorageDirectory();
              try {
                MediaPlayer mediaPlayer = new MediaPlayer();
                this();
                new Visualizer.MeasurementPeakRms();
                MainActivity.mTabHost.setCurrentTab(1);
                MainActivity.this.setTabBackground(MainActivity.this.type);
                MainActivity.this.img_music.setImageResource(2131165413);
                MainActivity.access$602(MainActivity.this, 1);
                MainActivity.this.tx_music.setTextColor(-11872414);
                MainActivity.access$102(MainActivity.this, 1);
                boolean bool = MainActivity.this.mMyApplication.mMediaRecorderDemo.isPlay;
                MainActivity.this.mMyApplication.mMediaRecorderDemo.stopRecord();
                if (bool)
                  MainActivity.this.mHandler.postDelayed(new Runnable() {
                        public void run() {
                          MainActivity.this.mMyApplication.reSetCMD();
                        }
                      },  500L); 
                return;
              } catch (Exception exception) {
                Toast.makeText(MainActivity.context, MainActivity.this.mResources.getString(2131624044), 1).show();
                return;
              } 
            } 
            Toast.makeText(MainActivity.context, "SD card Error !", 1).show();
          }
        });
    this.rb_mod.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            MainActivity.this.mMyApplication.isRecorder = false;
            MainActivity.mTabHost.setCurrentTab(2);
            MainActivity.this.setTabBackground(MainActivity.this.type);
            MainActivity.this.img_mod.setImageResource(2131165405);
            MainActivity.access$602(MainActivity.this, 2);
            MainActivity.this.tx_mod.setTextColor(-11872414);
            MainActivity.access$102(MainActivity.this, 2);
            boolean bool = MainActivity.this.mMyApplication.mMediaRecorderDemo.isPlay;
            MainActivity.this.mMyApplication.mMediaRecorderDemo.stopRecord();
            if (bool)
              MainActivity.this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                      MainActivity.this.mMyApplication.reSetCMD();
                    }
                  },  500L); 
          }
        });
    this.rb_recording.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            MainActivity.this.mMyApplication.isRecorder = true;
            if (Build.VERSION.SDK_INT >= 23 && MainActivity.this.checkSelfPermission("android.permission.RECORD_AUDIO") != 0) {
              MainActivity.this.requestPermissions(new String[] { "android.permission.RECORD_AUDIO" }, 1009);
              return;
            } 
            MainActivity.mTabHost.setCurrentTab(3);
            MainActivity.this.setTabBackground(MainActivity.this.type);
            MainActivity.this.img_recording.setImageResource(2131165425);
            MainActivity.access$602(MainActivity.this, 3);
            MainActivity.this.tx_recording.setTextColor(-11872414);
            MainActivity.access$102(MainActivity.this, 3);
            if (MainActivity.this.mMyApplication.MusicHandler != null)
              MainActivity.this.mMyApplication.MusicHandler.sendEmptyMessage(2); 
            MainActivity.this.mMyApplication.isopenmic = false;
            MainActivity.this.mMyApplication.openmic(MainActivity.this.mMyApplication.isopenmic);
            if (MainActivity.this.mMyApplication.AdjustHandler != null)
              MainActivity.this.mMyApplication.AdjustHandler.sendEmptyMessage(5); 
            if (MainActivity.this.mMyApplication.RecordingHandler != null) {
              MainActivity.this.mMyApplication.RecordingHandler.sendEmptyMessageDelayed(0, 500L);
              Log.e("88", " modid RecordingHandler!=null");
            } else {
              Log.e("88", " modid RecordingHandler==null");
            } 
          }
        });
    this.rb_timing.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            MainActivity.mTabHost.setCurrentTab(4);
            MainActivity.this.setTabBackground(MainActivity.this.type);
            MainActivity.this.img_timing.setImageResource(2131165435);
            MainActivity.access$602(MainActivity.this, 4);
            MainActivity.this.tx_timing.setTextColor(-11872414);
            MainActivity.access$102(MainActivity.this, 4);
            boolean bool = MainActivity.this.mMyApplication.mMediaRecorderDemo.isPlay;
            MainActivity.this.mMyApplication.mMediaRecorderDemo.stopRecord();
          }
        });
    this.img_mid.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (MainActivity.this.mMyApplication.Open) {
              MainActivity.this.mMyApplication.Open = false;
            } else {
              MainActivity.this.mMyApplication.Open = true;
            } 
            MainActivity.this.mMyApplication.openAll(MainActivity.this.mMyApplication.Open);
            if (MainActivity.this.mMyApplication.Open) {
              MainActivity.this.img_mid.setImageResource(2131165334);
            } else {
              MainActivity.this.img_mid.setImageResource(2131165333);
            } 
            if (MainActivity.this.mOperateHandler != null)
              MainActivity.this.mOperateHandler.sendEmptyMessageDelayed(0, 400L); 
            MainActivity.this.mHandler.postDelayed(new Runnable() {
                  public void run() {
                    MainActivity.this.mMyApplication.reSetCMD();
                  }
                },  500L);
          }
        });
    this.img_list.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            MainActivity.this.drawerLayout.openDrawer((View)MainActivity.this.leftLayout, true);
          }
        });
    this.img_set.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            MainActivity.this.drawerLayout.openDrawer((View)MainActivity.this.rightLayout, true);
          }
        });
    this.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
          public void onDrawerClosed(@NonNull View param1View) {
            if (param1View.getId() == MainActivity.this.leftLayout.getId()) {
              MainActivity.this.img_list.setVisibility(0);
            } else {
              MainActivity.this.img_set.setVisibility(0);
            } 
          }
          
          public void onDrawerOpened(@NonNull View param1View) {
            if (param1View.getId() == MainActivity.this.leftLayout.getId()) {
              MainActivity.this.img_list.setVisibility(8);
            } else {
              MainActivity.this.img_set.setVisibility(8);
            } 
          }
          
          public void onDrawerSlide(@NonNull View param1View, float param1Float) {}
          
          public void onDrawerStateChanged(int param1Int) {}
        });
    this.rel_management.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            Intent intent = new Intent((Context)MainActivity.this, DragListActivity.class);
            MainActivity.this.startActivityForResult(intent, 100);
          }
        });
    this.lin_yaoyiyao.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            MainActivity.access$2302(MainActivity.this, MainActivity.this.isOpenyaoyiyao ^ true);
            MainActivity.this.setSensorManagerListener(MainActivity.this.isOpenyaoyiyao);
            if (MainActivity.this.isOpenyaoyiyao) {
              MainActivity.this.yao_open.setImageResource(2131165415);
            } else {
              MainActivity.this.yao_open.setImageResource(2131165349);
            } 
          }
        });
    this.lin_directions.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            Intent intent = new Intent((Context)MainActivity.this, SwitchViewActivity.class);
            MainActivity.this.startActivity(intent);
          }
        });
    this.lin_about.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            Intent intent = new Intent((Context)MainActivity.this, AboutActivity.class);
            MainActivity.this.startActivity(intent);
          }
        });
    this.lin_sethuancai.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            Intent intent = new Intent((Context)MainActivity.this, SethuancaiActivity.class);
            MainActivity.this.startActivity(intent);
          }
        });
    this.lin_setqicai.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            Intent intent = new Intent((Context)MainActivity.this, SetqicaiActivity.class);
            MainActivity.this.startActivity(intent);
          }
        });
    this.sensorManager = (SensorManager)getSystemService("sensor");
    this.vibrator = (Vibrator)getSystemService("vibrator");
    this.rb_num = 0;
    if (this.mMyApplication.isallopen) {
      this.img_mid.setImageResource(2131165334);
    } else {
      this.img_mid.setImageResource(2131165333);
    } 
    this.lin_change.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            Intent intent = new Intent((Context)MainActivity.this, ChangeBgActivity.class);
            MainActivity.this.startActivity(intent);
          }
        });
  }
  
  public void onRequestPermissionsResult(int paramInt, @NonNull String[] paramArrayOfString, @NonNull int[] paramArrayOfint) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("requestCode=");
    stringBuilder.append(paramInt);
    Log.e("--", stringBuilder.toString());
    if (paramInt == 1) {
      this.isRequest = true;
      if (Build.VERSION.SDK_INT >= 23)
        if (checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
          this.rel_permission.setVisibility(0);
        } else {
          this.rel_permission.setVisibility(8);
        }  
    } 
    super.onRequestPermissionsResult(paramInt, paramArrayOfString, paramArrayOfint);
  }
  
  protected void onResume() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("-Build.VERSION.SDK_INT -");
    stringBuilder.append(Build.VERSION.SDK_INT);
    Log.e("--", stringBuilder.toString());
    if (this.mMyApplication.bgsrc.length > this.mMyApplication.typebg)
      this.rel_math.setBackgroundResource(this.mMyApplication.bgsrc[this.mMyApplication.typebg]); 
    this.rel_permission = (RelativeLayout)findViewById(2131231078);
    if (!Tool.isOPen(getApplicationContext()) && !this.isopenGPS) {
      Toast.makeText(getApplicationContext(), getResources().getString(2131624050), 1).show();
      Tool.openGPS((Activity)this);
      this.isopenGPS = true;
    } 
    if (Build.VERSION.SDK_INT >= 23) {
      if (ContextCompat.checkSelfPermission((Context)this, "android.permission.ACCESS_COARSE_LOCATION") != 0) {
        if (!this.isRequest) {
          requestPermissions(new String[] { "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION" }, 1);
        } else {
          this.rel_permission.setVisibility(0);
        } 
        stringBuilder = new StringBuilder();
        stringBuilder.append("-requestPermissions-");
        stringBuilder.append(checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION"));
        Log.e("--", stringBuilder.toString());
      } else {
        if (Build.VERSION.SDK_INT >= 26 && !Tool.isOPen(getApplicationContext()) && !this.isopenGPS) {
          Tool.openGPS((Activity)this);
          Toast.makeText(getApplicationContext(), getResources().getString(2131624050), 1).show();
          this.isopenGPS = true;
        } 
        this.rel_permission.setVisibility(8);
      } 
    } else {
      this.rel_permission.setVisibility(8);
    } 
    super.onResume();
  }
  
  public void openble() {
    if (this.mBluetoothAdapter != null) {
      boolean bool = this.mBluetoothAdapter.isEnabled();
      if (this.mBluetoothAdapter == null || !bool) {
        startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 11);
        Toast.makeText(context, this.mResources.getText(2131624049), 1).show();
      } 
    } else {
      startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 11);
      Toast.makeText(context, this.mResources.getText(2131624049), 1).show();
    } 
  }
  
  public void setListData() {
    this.groupNames.clear();
    this.groupIDs.clear();
    this.groupNames.add(this.mResources.getString(2131623941));
    this.groupIDs.add(Integer.valueOf(0));
    Cursor cursor = this.dbAdapter.queryAllData();
    while (cursor.moveToNext()) {
      String str = cursor.getString(cursor.getColumnIndex("groupName"));
      int i = cursor.getInt(cursor.getColumnIndex("_id"));
      this.groupNames.add(str);
      this.groupIDs.add(Integer.valueOf(i));
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("-id = ");
      stringBuilder.append(i);
      Log.e("", stringBuilder.toString());
    } 
  }
  
  public void setTabBackground(int paramInt) {
    switch (paramInt) {
      default:
        return;
      case 4:
        this.rb_timing.setBackgroundColor(0);
        this.img_timing.setImageResource(2131165436);
        this.tx_timing.setTextColor(-1);
      case 3:
        this.rb_recording.setBackgroundColor(0);
        this.img_recording.setImageResource(2131165426);
        this.tx_recording.setTextColor(-1);
      case 2:
        this.rb_mod.setBackgroundColor(0);
        this.img_mod.setImageResource(2131165408);
        this.tx_mod.setTextColor(-1);
      case 1:
        this.rb_music.setBackgroundColor(0);
        this.img_music.setImageResource(2131165414);
        this.tx_music.setTextColor(-1);
      case 0:
        break;
    } 
    this.rb_adjust.setBackgroundColor(0);
    this.img_adjust.setImageResource(2131165332);
    this.tx_adjust.setTextColor(-1);
  }
  
  public void setViewLayoutParams(View paramView, int paramInt1, int paramInt2) {
    ViewGroup.LayoutParams layoutParams = paramView.getLayoutParams();
    if (layoutParams.height != paramInt2 || layoutParams.width != paramInt1) {
      layoutParams.width = paramInt1;
      layoutParams.height = paramInt2;
      paramView.setLayoutParams(layoutParams);
    } 
  }
  
  public void setpwd(String paramString) {
    Intent intent = new Intent((Context)this, InputpwdActivity.class);
    intent.putExtra("addr", paramString);
    startActivity(intent);
  }
  
  public void showDialog(final boolean isopen) {
    if (this.rel_pro == null)
      this.rel_pro = (RelativeLayout)findViewById(2131231079); 
    this.rel_pro.setVisibility(0);
    (new Handler()).postDelayed(new Runnable() {
          public void run() {
            if (!MainActivity.this.req) {
              MainActivity.this.rel_pro.setVisibility(8);
              MainActivity.this.add(isopen, false);
            } 
            MainActivity.this.ist = true;
          }
        }1500L);
  }
  
  public void uimic() {}
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\blelight\MainActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */