package com.qh.blelight;

import android.annotation.TargetApi;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import com.qh.tools.DBAdapter;
import com.qh.tools.SampleGattAttributes;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.UUID;

@TargetApi(18)
public class BluetoothLeService<IWindowManager> extends Service {
  public static final String COMPANY_NAME = "^Triones-|^BRGlight|^Color\\+|^Triones\\+|^Dream-|^Light-|^Triones~|^Triones|^Color-|^QHM|^Flash|^LD-|^Dimmer|^Dream\\*|^Dream\\#|^Dream~";
  
  public static final int INT_PHOTOGRAPH = 2;
  
  public static final int SCAN_PERIOD = 4000;
  
  private static final int SREVICE_UPDATA = 5;
  
  private static final int STATE_CONNECTED = 2;
  
  private static final int STATE_CONNECTING = 1;
  
  private static final int STATE_DISCONNECTED = 0;
  
  private static final String TAG = "BluetoothLeService";
  
  public static final UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);
  
  public static final boolean isDebug = true;
  
  private static boolean isDofindService = false;
  
  private static boolean isFindService = false;
  
  private static boolean isStatr = false;
  
  public static String link_Addr = "";
  
  public static long time;
  
  private BluetoothDevice LEdevice;
  
  public Hashtable<String, MyBluetoothGatt> MyBluetoothGatts = new Hashtable<String, MyBluetoothGatt>();
  
  public Hashtable<String, Long> connTime = new Hashtable<String, Long>();
  
  public BluetoothGattCharacteristic connect_state;
  
  public Context context;
  
  public DBAdapter dbAdapter;
  
  public BluetoothGattCharacteristic device_addr;
  
  public BluetoothGattCharacteristic device_name;
  
  private boolean isLEenabled = false;
  
  public int linkNum = 0;
  
  private AudioManager mAudiomanager;
  
  private final IBinder mBinder = (IBinder)new LocalBinder();
  
  public Hashtable<String, BluetoothDevice> mBindingDevices = new Hashtable<String, BluetoothDevice>();
  
  private BluetoothAdapter mBluetoothAdapter;
  
  private String mBluetoothDeviceAddress;
  
  public BluetoothGatt mBluetoothGatt;
  
  private BluetoothManager mBluetoothManager;
  
  public Checkpwd mCheckpwd;
  
  public Hashtable<String, String> mConnectedDevices = new Hashtable<String, String>();
  
  private int mConnectionState = 0;
  
  public String mDeviceAddr;
  
  public Hashtable<String, BluetoothDevice> mDevices = new Hashtable<String, BluetoothDevice>();
  
  private Handler mHandler = new Handler();
  
  private BluetoothDevice mLEdevice;
  
  private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
      public BluetoothDevice mdevice;
      
      private int num = 0;
      
      public void onLeScan(BluetoothDevice param1BluetoothDevice, int param1Int, byte[] param1ArrayOfbyte) {
        // Byte code:
        //   0: aload_0
        //   1: monitorenter
        //   2: aload_1
        //   3: invokevirtual getName : ()Ljava/lang/String;
        //   6: astore_3
        //   7: aload_3
        //   8: ifnonnull -> 14
        //   11: aload_0
        //   12: monitorexit
        //   13: return
        //   14: ldc '^Triones-|^BRGlight|^Color\+|^Triones\+|^Dream-|^Light-|^Triones~|^Triones|^Color-|^QHM|^Flash|^LD-|^Dimmer|^Dream\*|^Dream\#|^Dream~'
        //   16: invokestatic compile : (Ljava/lang/String;)Ljava/util/regex/Pattern;
        //   19: aload_3
        //   20: invokevirtual matcher : (Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;
        //   23: invokevirtual find : ()Z
        //   26: istore #4
        //   28: iload #4
        //   30: ifne -> 36
        //   33: aload_0
        //   34: monitorexit
        //   35: return
        //   36: aload_0
        //   37: getfield this$0 : Lcom/qh/blelight/BluetoothLeService;
        //   40: getfield mDevices : Ljava/util/Hashtable;
        //   43: aload_1
        //   44: invokevirtual getAddress : ()Ljava/lang/String;
        //   47: invokevirtual containsKey : (Ljava/lang/Object;)Z
        //   50: ifne -> 164
        //   53: aload_0
        //   54: getfield this$0 : Lcom/qh/blelight/BluetoothLeService;
        //   57: aload_1
        //   58: invokevirtual getAddress : ()Ljava/lang/String;
        //   61: putfield mDeviceAddr : Ljava/lang/String;
        //   64: aload_0
        //   65: getfield this$0 : Lcom/qh/blelight/BluetoothLeService;
        //   68: getfield mDevices : Ljava/util/Hashtable;
        //   71: aload_1
        //   72: invokevirtual getAddress : ()Ljava/lang/String;
        //   75: aload_1
        //   76: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   79: pop
        //   80: new java/lang/StringBuilder
        //   83: astore_3
        //   84: aload_3
        //   85: invokespecial <init> : ()V
        //   88: aload_3
        //   89: ldc '---'
        //   91: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   94: pop
        //   95: aload_3
        //   96: aload_1
        //   97: invokevirtual getAddress : ()Ljava/lang/String;
        //   100: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   103: pop
        //   104: ldc '--'
        //   106: aload_3
        //   107: invokevirtual toString : ()Ljava/lang/String;
        //   110: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
        //   113: pop
        //   114: getstatic com/qh/blelight/MainActivity.ControlMACs : Ljava/util/Hashtable;
        //   117: aload_1
        //   118: invokevirtual getAddress : ()Ljava/lang/String;
        //   121: aload_1
        //   122: invokevirtual getAddress : ()Ljava/lang/String;
        //   125: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   128: pop
        //   129: aload_0
        //   130: getfield this$0 : Lcom/qh/blelight/BluetoothLeService;
        //   133: invokestatic access$400 : (Lcom/qh/blelight/BluetoothLeService;)Landroid/os/Handler;
        //   136: ifnull -> 164
        //   139: new android/os/Message
        //   142: astore_3
        //   143: aload_3
        //   144: invokespecial <init> : ()V
        //   147: aload_3
        //   148: iconst_1
        //   149: putfield what : I
        //   152: aload_0
        //   153: getfield this$0 : Lcom/qh/blelight/BluetoothLeService;
        //   156: invokestatic access$400 : (Lcom/qh/blelight/BluetoothLeService;)Landroid/os/Handler;
        //   159: aload_3
        //   160: invokevirtual sendMessage : (Landroid/os/Message;)Z
        //   163: pop
        //   164: aload_0
        //   165: getfield this$0 : Lcom/qh/blelight/BluetoothLeService;
        //   168: invokestatic access$500 : (Lcom/qh/blelight/BluetoothLeService;)Landroid/os/Handler;
        //   171: astore_3
        //   172: new com/qh/blelight/BluetoothLeService$4$1
        //   175: astore #5
        //   177: aload #5
        //   179: aload_0
        //   180: aload_1
        //   181: invokespecial <init> : (Lcom/qh/blelight/BluetoothLeService$4;Landroid/bluetooth/BluetoothDevice;)V
        //   184: aload_3
        //   185: aload #5
        //   187: aload_0
        //   188: getfield this$0 : Lcom/qh/blelight/BluetoothLeService;
        //   191: getfield linkNum : I
        //   194: sipush #150
        //   197: imul
        //   198: sipush #250
        //   201: iadd
        //   202: i2l
        //   203: invokevirtual postDelayed : (Ljava/lang/Runnable;J)Z
        //   206: pop
        //   207: aload_0
        //   208: monitorexit
        //   209: return
        //   210: astore_1
        //   211: aload_0
        //   212: monitorexit
        //   213: aload_1
        //   214: athrow
        // Exception table:
        //   from	to	target	type
        //   2	7	210	finally
        //   14	28	210	finally
        //   36	164	210	finally
        //   164	207	210	finally
      }
    };
  
  public Qicaidata mQicaidata = null;
  
  public Handler mScanHandler;
  
  private boolean mScanning = false;
  
  public BluetoothGattCharacteristic manufacturer_name;
  
  public Resetpwd mresetpwd = null;
  
  public MyApplication myApplication;
  
  public BluetoothLeService myBluetoothLeService;
  
  public Handler openScanHandler;
  
  private Handler operateHandler;
  
  public BluetoothGattCharacteristic photoCharacteristic;
  
  public BluetoothGattCharacteristic power_level;
  
  private Runnable scanRunnable = new Runnable() {
      public void run() {
        BluetoothLeService.this.scanLeDevice();
        BluetoothLeService.this.openScanHandler.postDelayed(BluetoothLeService.this.scanRunnable, 2000L);
      }
    };
  
  public SharedPreferences settings;
  
  public Hashtable<String, String> unlinkBleDevices = new Hashtable<String, String>();
  
  private void initialize() {
    this.mBluetoothAdapter = ((BluetoothManager)getSystemService("bluetooth")).getAdapter();
    this.mAudiomanager = (AudioManager)getSystemService("audio");
    isStatr = true;
  }
  
  public void StopScanLeDevice() {
    if (this.mBluetoothAdapter == null || !this.mBluetoothAdapter.isEnabled())
      return; 
    if (this.mScanning) {
      this.mBluetoothAdapter.stopLeScan(this.mLeScanCallback);
      this.mScanning = false;
    } 
  }
  
  public void addBindingDevices(String paramString) {
    if (this.mDevices.containsKey(paramString)) {
      BluetoothDevice bluetoothDevice = this.mDevices.get(paramString);
      if (bluetoothDevice != null)
        this.mBindingDevices.put(paramString, bluetoothDevice); 
    } 
  }
  
  public int connBLE(final String addr) {
    if (this.connTime.containsKey(addr)) {
      long l = ((Long)this.connTime.get(addr)).longValue();
      if (System.currentTimeMillis() - l <= 2000L)
        return 0; 
    } 
    if (this.MyBluetoothGatts.size() >= 4)
      return 1; 
    if (this.MyBluetoothGatts.containsKey(addr)) {
      MyBluetoothGatt myBluetoothGatt = this.MyBluetoothGatts.get(addr);
      if ((new Date()).getTime() - myBluetoothGatt.linktime > 19000L && myBluetoothGatt.mConnectionState != 2) {
        myBluetoothGatt.stopLEService();
        this.MyBluetoothGatts.remove(addr);
        this.mHandler.postDelayed(new Runnable() {
              public void run() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("------");
                stringBuilder.append(addr);
                Log.e("", stringBuilder.toString());
                MyBluetoothGatt myBluetoothGatt = new MyBluetoothGatt(BluetoothLeService.this.context, BluetoothLeService.this.mBluetoothAdapter, BluetoothLeService.this.myBluetoothLeService, BluetoothLeService.this.operateHandler, BluetoothLeService.this.dbAdapter);
                myBluetoothGatt.connectGatt(addr);
                BluetoothLeService.this.MyBluetoothGatts.put(addr, myBluetoothGatt);
                BluetoothLeService.this.connTime.put(addr, Long.valueOf(System.currentTimeMillis()));
              }
            }120L);
      } 
    } else {
      if (this.MyBluetoothGatts.size() > 5)
        return 1; 
      MyBluetoothGatt myBluetoothGatt = new MyBluetoothGatt(this.context, this.mBluetoothAdapter, this.myBluetoothLeService, this.operateHandler, this.dbAdapter);
      myBluetoothGatt.connectGatt(addr);
      this.MyBluetoothGatts.put(addr, myBluetoothGatt);
      this.connTime.put(addr, Long.valueOf(System.currentTimeMillis()));
    } 
    return 0;
  }
  
  public Hashtable<String, BluetoothDevice> getBindingDevice() {
    return new Hashtable<String, BluetoothDevice>(this.mBindingDevices);
  }
  
  public Hashtable<String, BluetoothDevice> getBluetoothDevice() {
    Hashtable<String, BluetoothDevice> hashtable = new Hashtable<String, BluetoothDevice>(this.mDevices);
    Iterator<String> iterator = this.mBindingDevices.keySet().iterator();
    while (iterator.hasNext())
      hashtable.remove(iterator.next()); 
    return hashtable;
  }
  
  public IBinder onBind(Intent paramIntent) {
    return this.mBinder;
  }
  
  public void onCreate() {
    this.context = (Context)this;
    this.openScanHandler = new Handler();
    initialize();
    this.settings = getSharedPreferences("BleLight", 0);
    this.dbAdapter = DBAdapter.init((Context)this);
    this.dbAdapter.open();
    this.myBluetoothLeService = this;
    this.myApplication = (MyApplication)getApplication();
    super.onCreate();
  }
  
  public void onDestroy() {
    super.onDestroy();
  }
  
  public boolean onUnbind(Intent paramIntent) {
    return super.onUnbind(paramIntent);
  }
  
  public void scanLeDevice(boolean paramBoolean) {
    this.openScanHandler.postDelayed(this.scanRunnable, 100L);
  }
  
  public boolean scanLeDevice() {
    if (this.mBluetoothAdapter == null || !this.mBluetoothAdapter.isEnabled())
      return false; 
    if (this.mScanning)
      return false; 
    this.mBluetoothAdapter.startLeScan(this.mLeScanCallback);
    this.mScanning = true;
    this.mHandler.postDelayed(new Runnable() {
          public void run() {
            BluetoothLeService.this.mBluetoothAdapter.stopLeScan(BluetoothLeService.this.mLeScanCallback);
          }
        },  4000L);
    this.mHandler.postDelayed(new Runnable() {
          public void run() {
            BluetoothLeService.access$202(BluetoothLeService.this, false);
          }
        }6000L);
    return true;
  }
  
  public void setHandler(Handler paramHandler) {
    this.mHandler = paramHandler;
  }
  
  public void setOperateHandler(Handler paramHandler) {
    this.operateHandler = paramHandler;
  }
  
  public void stopLEService() {
    this.mBluetoothAdapter.stopLeScan(this.mLeScanCallback);
    this.mScanning = false;
  }
  
  public static interface Checkpwd {
    void checkpwd(String param1String1, int param1Int, String param1String2);
  }
  
  public class LocalBinder extends Binder {
    BluetoothLeService getService() {
      return BluetoothLeService.this;
    }
  }
  
  public static interface Qicaidata {
    void readqicai(String param1String, int param1Int1, int param1Int2, boolean param1Boolean);
  }
  
  public static interface Resetpwd {
    void resetpwd(String param1String1, int param1Int, String param1String2);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\blelight\BluetoothLeService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */