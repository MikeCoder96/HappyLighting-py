package com.consmart.ble;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import java.util.UUID;

public class MainActivity extends Activity {
  public BleController mBleController;
  
  private Context mContext;
  
  private BleController.MyLeScanCallback mMyLeScanCallback = new BleController.MyLeScanCallback() {
      public void onLeScan(BluetoothDevice param1BluetoothDevice, int param1Int) {}
    };
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2130903040);
    this.mContext = getApplicationContext();
    this.mBleController = BleController.initialization(this.mContext);
    UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
    this.mBleController.setScanLeDeviceType(null, "123456");
    this.mBleController.setMyLeScanCallback(this.mMyLeScanCallback);
    this.mBleController.scanLeDevice();
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\consmart\ble\MainActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */