package com.consmart.ble;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

@TargetApi(18)
public class MyBluetoothGatt {
  public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
  
  public static final int INT_PHOTOGRAPH = 2;
  
  public static final int SREVICE_UPDATA = 5;
  
  public static final int STATE_CONNECTED = 2;
  
  public static final int STATE_CONNECTING = 1;
  
  public static final int STATE_DISCONNECTED = 0;
  
  public BluetoothAdapter bluetoothAdapter;
  
  private byte[] checkdata = new byte[] { 1, 1, 1, 1 };
  
  public Context context;
  
  private boolean isCheck = false;
  
  public BleController mBleController;
  
  private BluetoothGatt mBluetoothGatt;
  
  public int mConnectionState = 0;
  
  private BluetoothGattCallback mGattCallback;
  
  private Handler mHandler;
  
  private BluetoothDevice mLEdevice;
  
  public MyBluetoothGatt mMyBluetoothGatt;
  
  public MyBluetoothGattCallback mMyBluetoothGattCallback;
  
  public Resources mResources;
  
  public MyBluetoothGatt(Context paramContext, BluetoothAdapter paramBluetoothAdapter) {
    this.context = paramContext;
    this.bluetoothAdapter = paramBluetoothAdapter;
    this.mMyBluetoothGatt = this;
    this.mBleController = BleController.initialization(this.context);
    this.mHandler = new Handler();
    this.mGattCallback = new BluetoothGattCallback() {
        public void onCharacteristicChanged(BluetoothGatt param1BluetoothGatt, BluetoothGattCharacteristic param1BluetoothGattCharacteristic) {
          if (MyBluetoothGatt.this.mMyBluetoothGattCallback != null)
            MyBluetoothGatt.this.mMyBluetoothGattCallback.onCharacteristicChanged(MyBluetoothGatt.this.mMyBluetoothGatt, param1BluetoothGattCharacteristic); 
          if ("0000fff4-0000-1000-8000-00805f9b34fb".equalsIgnoreCase(param1BluetoothGattCharacteristic.getUuid().toString())) {
            byte[] arrayOfByte = param1BluetoothGattCharacteristic.getValue();
            if (arrayOfByte != null && arrayOfByte.length == 18 && 102 == (arrayOfByte[0] & 0xFF) && 187 == (arrayOfByte[17] & 0xFF) && arrayOfByte[1] == MyBluetoothGatt.this.checkdata[0] && arrayOfByte[2] == MyBluetoothGatt.this.checkdata[1] && arrayOfByte[3] == MyBluetoothGatt.this.checkdata[2] && arrayOfByte[4] == MyBluetoothGatt.this.checkdata[3]) {
              MyBluetoothGatt.this.isCheck = true;
              Log.e("", "验证成功！");
              if (MyBluetoothGatt.this.mMyBluetoothGattCallback != null)
                MyBluetoothGatt.this.mMyBluetoothGattCallback.oncheck(); 
            } 
          } 
        }
        
        public void onCharacteristicRead(BluetoothGatt param1BluetoothGatt, BluetoothGattCharacteristic param1BluetoothGattCharacteristic, int param1Int) {
          if (MyBluetoothGatt.this.mMyBluetoothGattCallback != null)
            MyBluetoothGatt.this.mMyBluetoothGattCallback.onCharacteristicRead(MyBluetoothGatt.this.mMyBluetoothGatt, param1BluetoothGattCharacteristic, param1Int); 
        }
        
        public void onCharacteristicWrite(BluetoothGatt param1BluetoothGatt, BluetoothGattCharacteristic param1BluetoothGattCharacteristic, int param1Int) {
          if (MyBluetoothGatt.this.mMyBluetoothGattCallback != null)
            MyBluetoothGatt.this.mMyBluetoothGattCallback.onCharacteristicWrite(MyBluetoothGatt.this.mMyBluetoothGatt, param1BluetoothGattCharacteristic, param1Int); 
          super.onCharacteristicWrite(param1BluetoothGatt, param1BluetoothGattCharacteristic, param1Int);
        }
        
        public void onConnectionStateChange(BluetoothGatt param1BluetoothGatt, int param1Int1, int param1Int2) {
          MyBluetoothGatt.this.mConnectionState = param1Int1;
          if (MyBluetoothGatt.this.mMyBluetoothGattCallback != null)
            MyBluetoothGatt.this.mMyBluetoothGattCallback.onConnectionStateChange(MyBluetoothGatt.this.mMyBluetoothGatt, param1Int1, param1Int2); 
          if (param1Int2 == 2)
            param1BluetoothGatt.discoverServices(); 
          if (param1Int2 == 0)
            MyBluetoothGatt.this.mBleController.removeMyBluetoothGatt(param1BluetoothGatt.getDevice().getAddress()); 
        }
        
        public void onDescriptorWrite(BluetoothGatt param1BluetoothGatt, BluetoothGattDescriptor param1BluetoothGattDescriptor, int param1Int) {
          if (MyBluetoothGatt.this.mMyBluetoothGattCallback != null)
            MyBluetoothGatt.this.mMyBluetoothGattCallback.onDescriptorWrite(MyBluetoothGatt.this.mMyBluetoothGatt, param1BluetoothGattDescriptor, param1Int); 
          StringBuilder stringBuilder = new StringBuilder("descriptor -");
          stringBuilder.append(param1BluetoothGattDescriptor.getCharacteristic().getUuid().toString());
          Log.e("", stringBuilder.toString());
          if (param1Int == 0 && "0000fff4-0000-1000-8000-00805f9b34fb".equalsIgnoreCase(param1BluetoothGattDescriptor.getCharacteristic().getUuid().toString()))
            MyBluetoothGatt.this.check(); 
          super.onDescriptorWrite(param1BluetoothGatt, param1BluetoothGattDescriptor, param1Int);
        }
        
        public void onReadRemoteRssi(BluetoothGatt param1BluetoothGatt, int param1Int1, int param1Int2) {
          if (MyBluetoothGatt.this.mMyBluetoothGattCallback != null)
            MyBluetoothGatt.this.mMyBluetoothGattCallback.onReadRemoteRssi(MyBluetoothGatt.this.mMyBluetoothGatt, param1Int1, param1Int2); 
        }
        
        public void onServicesDiscovered(BluetoothGatt param1BluetoothGatt, int param1Int) {
          if (MyBluetoothGatt.this.mMyBluetoothGattCallback != null)
            MyBluetoothGatt.this.mMyBluetoothGattCallback.onServicesDiscovered(MyBluetoothGatt.this.mMyBluetoothGatt, param1Int); 
          if (param1BluetoothGatt != null)
            MyBluetoothGatt.this.mBluetoothGatt = param1BluetoothGatt; 
          if (param1Int == 0)
            MyBluetoothGatt.this.mHandler.postDelayed(new Runnable() {
                  public void run() {
                    Log.e("", "--->>设置验证");
                    MyBluetoothGatt.null.access$0(MyBluetoothGatt.null.this).setCharacteristicNotify("0000fff0-0000-1000-8000-00805f9b34fb", "0000fff4-0000-1000-8000-00805f9b34fb");
                  }
                }100L); 
        }
      };
  }
  
  private void check() {
    Log.e("", "验证.....");
    Random random = new Random();
    byte[] arrayOfByte = new byte[18];
    arrayOfByte[0] = -86;
    arrayOfByte[1] = -81;
    arrayOfByte[2] = -86;
    arrayOfByte[3] = 70;
    arrayOfByte[4] = -21;
    arrayOfByte[5] = 28;
    arrayOfByte[6] = -21;
    arrayOfByte[7] = 15;
    arrayOfByte[8] = -7;
    arrayOfByte[9] = 68;
    arrayOfByte[10] = 73;
    arrayOfByte[11] = 118;
    arrayOfByte[12] = 53;
    arrayOfByte[13] = -42;
    arrayOfByte[14] = 123;
    arrayOfByte[15] = 64;
    arrayOfByte[16] = 4;
    arrayOfByte[17] = 85;
    for (int i = 0;; i++) {
      byte[] arrayOfByte1;
      if (i >= this.checkdata.length) {
        try {
          arrayOfByte1 = AES.Encrypt(this.checkdata);
          for (i = 0;; i = m) {
            if (i >= arrayOfByte1.length) {
              writeCharacteristic("0000fff0-0000-1000-8000-00805f9b34fb", "0000fff5-0000-1000-8000-00805f9b34fb", arrayOfByte, false);
              break;
            } 
            int m = i + 1;
            arrayOfByte[m] = (byte)arrayOfByte1[i];
          } 
        } catch (Exception exception) {
          exception.printStackTrace();
        } 
        return;
      } 
      int k = arrayOfByte1.nextInt(200);
      int j = k;
      if (k == 0)
        j = 1; 
      this.checkdata[i] = (byte)(byte)(j & 0xFF);
    } 
  }
  
  private BluetoothGattCallback getmGattCallback() {
    return this.mGattCallback;
  }
  
  private boolean setCharacteristicNotify(String paramString1, String paramString2) {
    UUID uUID2 = UUID.fromString(paramString1);
    UUID uUID1 = UUID.fromString(paramString2);
    if (this.mBluetoothGatt == null)
      return false; 
    BluetoothGattService bluetoothGattService = this.mBluetoothGatt.getService(uUID2);
    if (bluetoothGattService == null)
      return false; 
    BluetoothGattCharacteristic bluetoothGattCharacteristic = bluetoothGattService.getCharacteristic(uUID1);
    this.mBluetoothGatt.setCharacteristicNotification(bluetoothGattCharacteristic, true);
    BluetoothGattDescriptor bluetoothGattDescriptor = bluetoothGattCharacteristic.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
    if (bluetoothGattDescriptor != null) {
      bluetoothGattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
      return this.mBluetoothGatt.writeDescriptor(bluetoothGattDescriptor);
    } 
    return false;
  }
  
  private void setMsg(String paramString, int paramInt) {
    Message message = new Message();
    message.what = paramInt;
    Bundle bundle = new Bundle();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramString);
    bundle.putString("deviceAddr", stringBuilder.toString());
    message.setData(bundle);
    this.mHandler.sendMessage(message);
  }
  
  private boolean writeCharacteristic(String paramString1, String paramString2, byte[] paramArrayOfbyte, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokestatic fromString : (Ljava/lang/String;)Ljava/util/UUID;
    //   6: astore #5
    //   8: aload_2
    //   9: invokestatic fromString : (Ljava/lang/String;)Ljava/util/UUID;
    //   12: astore_1
    //   13: aload_0
    //   14: getfield mBluetoothGatt : Landroid/bluetooth/BluetoothGatt;
    //   17: astore_2
    //   18: aload_2
    //   19: ifnonnull -> 26
    //   22: aload_0
    //   23: monitorexit
    //   24: iconst_0
    //   25: ireturn
    //   26: aload_0
    //   27: getfield mBluetoothGatt : Landroid/bluetooth/BluetoothGatt;
    //   30: aload #5
    //   32: invokevirtual getService : (Ljava/util/UUID;)Landroid/bluetooth/BluetoothGattService;
    //   35: astore_2
    //   36: aload_2
    //   37: ifnonnull -> 44
    //   40: aload_0
    //   41: monitorexit
    //   42: iconst_0
    //   43: ireturn
    //   44: aload_2
    //   45: aload_1
    //   46: invokevirtual getCharacteristic : (Ljava/util/UUID;)Landroid/bluetooth/BluetoothGattCharacteristic;
    //   49: astore_1
    //   50: aload_1
    //   51: ifnonnull -> 58
    //   54: aload_0
    //   55: monitorexit
    //   56: iconst_0
    //   57: ireturn
    //   58: aload_1
    //   59: iconst_2
    //   60: invokevirtual setWriteType : (I)V
    //   63: aload_1
    //   64: aload_3
    //   65: invokevirtual setValue : ([B)Z
    //   68: pop
    //   69: aload_0
    //   70: getfield mBluetoothGatt : Landroid/bluetooth/BluetoothGatt;
    //   73: aload_1
    //   74: invokevirtual writeCharacteristic : (Landroid/bluetooth/BluetoothGattCharacteristic;)Z
    //   77: istore #4
    //   79: aload_0
    //   80: monitorexit
    //   81: iload #4
    //   83: ireturn
    //   84: astore_1
    //   85: aload_0
    //   86: monitorexit
    //   87: aload_1
    //   88: athrow
    // Exception table:
    //   from	to	target	type
    //   2	18	84	finally
    //   26	36	84	finally
    //   44	50	84	finally
    //   58	79	84	finally
  }
  
  @SuppressLint({"NewApi"})
  public void connectGatt(String paramString) {
    if (this.bluetoothAdapter == null)
      return; 
    this.mLEdevice = this.bluetoothAdapter.getRemoteDevice(paramString);
    this.mBluetoothGatt = this.mLEdevice.connectGatt(this.context, false, this.mGattCallback);
  }
  
  public ArrayList<BluetoothGattService> getBluetoothGattService() {
    ArrayList<BluetoothGattService> arrayList = new ArrayList();
    if (this.mBluetoothGatt != null)
      arrayList = (ArrayList)this.mBluetoothGatt.getServices(); 
    return arrayList;
  }
  
  public BluetoothGattService getService(UUID paramUUID) {
    if (this.mBluetoothGatt != null) {
      BluetoothGattService bluetoothGattService = this.mBluetoothGatt.getService(paramUUID);
    } else {
      paramUUID = null;
    } 
    return (BluetoothGattService)paramUUID;
  }
  
  public BluetoothDevice getmLEdevice() {
    return this.mLEdevice;
  }
  
  public boolean readCharacteristic(String paramString1, String paramString2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield isCheck : Z
    //   6: istore_3
    //   7: iload_3
    //   8: ifne -> 15
    //   11: aload_0
    //   12: monitorexit
    //   13: iconst_0
    //   14: ireturn
    //   15: aload_0
    //   16: getfield mBluetoothGatt : Landroid/bluetooth/BluetoothGatt;
    //   19: astore #4
    //   21: aload #4
    //   23: ifnonnull -> 30
    //   26: aload_0
    //   27: monitorexit
    //   28: iconst_0
    //   29: ireturn
    //   30: aload_1
    //   31: invokestatic fromString : (Ljava/lang/String;)Ljava/util/UUID;
    //   34: astore #4
    //   36: aload_2
    //   37: invokestatic fromString : (Ljava/lang/String;)Ljava/util/UUID;
    //   40: astore_1
    //   41: aload_0
    //   42: getfield mBluetoothGatt : Landroid/bluetooth/BluetoothGatt;
    //   45: aload #4
    //   47: invokevirtual getService : (Ljava/util/UUID;)Landroid/bluetooth/BluetoothGattService;
    //   50: astore_2
    //   51: aload_2
    //   52: ifnonnull -> 59
    //   55: aload_0
    //   56: monitorexit
    //   57: iconst_0
    //   58: ireturn
    //   59: aload_2
    //   60: aload_1
    //   61: invokevirtual getCharacteristic : (Ljava/util/UUID;)Landroid/bluetooth/BluetoothGattCharacteristic;
    //   64: astore_1
    //   65: aload_1
    //   66: ifnonnull -> 73
    //   69: aload_0
    //   70: monitorexit
    //   71: iconst_0
    //   72: ireturn
    //   73: aload_0
    //   74: getfield mBluetoothGatt : Landroid/bluetooth/BluetoothGatt;
    //   77: aload_1
    //   78: invokevirtual readCharacteristic : (Landroid/bluetooth/BluetoothGattCharacteristic;)Z
    //   81: istore_3
    //   82: aload_0
    //   83: monitorexit
    //   84: iload_3
    //   85: ireturn
    //   86: astore_1
    //   87: aload_0
    //   88: monitorexit
    //   89: aload_1
    //   90: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	86	finally
    //   15	21	86	finally
    //   30	51	86	finally
    //   59	65	86	finally
    //   73	82	86	finally
  }
  
  public boolean readRSSI() {
    return (this.mBluetoothGatt == null) ? false : this.mBluetoothGatt.readRemoteRssi();
  }
  
  public boolean setCharacteristicNotify(String paramString1, String paramString2, boolean paramBoolean) {
    if (!this.isCheck)
      return false; 
    UUID uUID2 = UUID.fromString(paramString1);
    UUID uUID1 = UUID.fromString(paramString2);
    if (this.mBluetoothGatt == null)
      return false; 
    BluetoothGattService bluetoothGattService = this.mBluetoothGatt.getService(uUID2);
    if (bluetoothGattService == null)
      return false; 
    BluetoothGattCharacteristic bluetoothGattCharacteristic = bluetoothGattService.getCharacteristic(uUID1);
    this.mBluetoothGatt.setCharacteristicNotification(bluetoothGattCharacteristic, paramBoolean);
    BluetoothGattDescriptor bluetoothGattDescriptor = bluetoothGattCharacteristic.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
    bluetoothGattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
    return this.mBluetoothGatt.writeDescriptor(bluetoothGattDescriptor);
  }
  
  public void setMyBluetoothGattCallback(MyBluetoothGattCallback paramMyBluetoothGattCallback) {
    this.mMyBluetoothGattCallback = paramMyBluetoothGattCallback;
  }
  
  public void stopLEService() {
    if (this.mBluetoothGatt == null)
      return; 
    this.mBluetoothGatt.disconnect();
  }
  
  public boolean writeCharacteristic(String paramString1, String paramString2, byte[] paramArrayOfbyte) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield isCheck : Z
    //   6: istore #4
    //   8: iload #4
    //   10: ifne -> 17
    //   13: aload_0
    //   14: monitorexit
    //   15: iconst_0
    //   16: ireturn
    //   17: aload_1
    //   18: invokestatic fromString : (Ljava/lang/String;)Ljava/util/UUID;
    //   21: astore_1
    //   22: aload_2
    //   23: invokestatic fromString : (Ljava/lang/String;)Ljava/util/UUID;
    //   26: astore_2
    //   27: aload_0
    //   28: getfield mBluetoothGatt : Landroid/bluetooth/BluetoothGatt;
    //   31: astore #5
    //   33: aload #5
    //   35: ifnonnull -> 42
    //   38: aload_0
    //   39: monitorexit
    //   40: iconst_0
    //   41: ireturn
    //   42: aload_0
    //   43: getfield mBluetoothGatt : Landroid/bluetooth/BluetoothGatt;
    //   46: aload_1
    //   47: invokevirtual getService : (Ljava/util/UUID;)Landroid/bluetooth/BluetoothGattService;
    //   50: astore_1
    //   51: aload_1
    //   52: ifnonnull -> 59
    //   55: aload_0
    //   56: monitorexit
    //   57: iconst_0
    //   58: ireturn
    //   59: aload_1
    //   60: aload_2
    //   61: invokevirtual getCharacteristic : (Ljava/util/UUID;)Landroid/bluetooth/BluetoothGattCharacteristic;
    //   64: astore_1
    //   65: aload_1
    //   66: ifnonnull -> 73
    //   69: aload_0
    //   70: monitorexit
    //   71: iconst_0
    //   72: ireturn
    //   73: aload_1
    //   74: iconst_2
    //   75: invokevirtual setWriteType : (I)V
    //   78: aload_1
    //   79: aload_3
    //   80: invokevirtual setValue : ([B)Z
    //   83: pop
    //   84: aload_0
    //   85: getfield mBluetoothGatt : Landroid/bluetooth/BluetoothGatt;
    //   88: aload_1
    //   89: invokevirtual writeCharacteristic : (Landroid/bluetooth/BluetoothGattCharacteristic;)Z
    //   92: istore #4
    //   94: aload_0
    //   95: monitorexit
    //   96: iload #4
    //   98: ireturn
    //   99: astore_1
    //   100: aload_0
    //   101: monitorexit
    //   102: aload_1
    //   103: athrow
    // Exception table:
    //   from	to	target	type
    //   2	8	99	finally
    //   17	33	99	finally
    //   42	51	99	finally
    //   59	65	99	finally
    //   73	94	99	finally
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\consmart\ble\MyBluetoothGatt.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */