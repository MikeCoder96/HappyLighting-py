package com.consmart.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import java.util.Hashtable;
import java.util.UUID;

public class BleController {
  private static BleController mBleController;
  
  private Hashtable<String, String> autoConnection = new Hashtable<String, String>();
  
  private Context context;
  
  private boolean isOpenScan = false;
  
  private BluetoothAdapter mBluetoothAdapter;
  
  private BluetoothManager mBluetoothManager;
  
  private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
      public void onLeScan(BluetoothDevice param1BluetoothDevice, int param1Int, byte[] param1ArrayOfbyte) {
        String str = param1BluetoothDevice.getName();
        if (BleController.this.name != null || "".equals(BleController.this.name)) {
          str = str.substring(0, BleController.this.name.length());
          if (BleController.this.name.equals(str) && BleController.this.mMyLeScanCallback != null)
            BleController.this.mMyLeScanCallback.onLeScan(param1BluetoothDevice, param1Int); 
          return;
        } 
        if (BleController.this.mMyLeScanCallback != null)
          BleController.this.mMyLeScanCallback.onLeScan(param1BluetoothDevice, param1Int); 
      }
    };
  
  private Hashtable<String, MyBluetoothGatt> mMyBluetoothGatts = new Hashtable<String, MyBluetoothGatt>();
  
  private MyLeScanCallback mMyLeScanCallback;
  
  private String name;
  
  private Handler scanHandler = new Handler();
  
  private Runnable scanRunnable = new Runnable() {
      public void run() {}
    };
  
  private UUID[] serviceUuids;
  
  private BleController(Context paramContext) {
    this.mBluetoothAdapter = ((BluetoothManager)paramContext.getSystemService("bluetooth")).getAdapter();
  }
  
  public static BleController initialization(Context paramContext) {
    if (paramContext == null)
      return null; 
    if (mBleController == null)
      mBleController = new BleController(paramContext); 
    return mBleController;
  }
  
  public MyBluetoothGatt ConnectGatt(String paramString, MyBluetoothGattCallback paramMyBluetoothGattCallback) {
    MyBluetoothGatt myBluetoothGatt = new MyBluetoothGatt(this.context, this.mBluetoothAdapter);
    myBluetoothGatt.setMyBluetoothGattCallback(paramMyBluetoothGattCallback);
    myBluetoothGatt.connectGatt(paramString);
    return myBluetoothGatt;
  }
  
  public void addAutoConnection(String paramString) {
    this.autoConnection.put(paramString, paramString);
  }
  
  public void removeMyBluetoothGatt(String paramString) {
    if (this.mMyBluetoothGatts.containsKey(paramString))
      this.mMyBluetoothGatts.remove(paramString); 
  }
  
  public int scanLeDevice() {
    if (this.mBluetoothAdapter == null || !this.mBluetoothAdapter.isEnabled())
      return 0; 
    if (this.serviceUuids == null) {
      this.mBluetoothAdapter.startLeScan(this.mLeScanCallback);
      Log.e("", "--->> 1开启成功");
    } else {
      this.mBluetoothAdapter.startLeScan(this.serviceUuids, this.mLeScanCallback);
    } 
    return 1;
  }
  
  public void setMyLeScanCallback(MyLeScanCallback paramMyLeScanCallback) {
    this.mMyLeScanCallback = paramMyLeScanCallback;
  }
  
  public void setScanLeDeviceType(UUID[] paramArrayOfUUID, String paramString) {
    this.serviceUuids = paramArrayOfUUID;
    this.name = paramString;
  }
  
  public void stopScanLeDevice() {
    if (this.mBluetoothAdapter != null)
      this.mBluetoothAdapter.stopLeScan(this.mLeScanCallback); 
  }
  
  public static interface MyLeScanCallback {
    void onLeScan(BluetoothDevice param1BluetoothDevice, int param1Int);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\consmart\ble\BleController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */