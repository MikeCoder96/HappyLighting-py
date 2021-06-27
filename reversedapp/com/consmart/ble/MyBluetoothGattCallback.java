package com.consmart.ble;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

public interface MyBluetoothGattCallback {
  void onCharacteristicChanged(MyBluetoothGatt paramMyBluetoothGatt, BluetoothGattCharacteristic paramBluetoothGattCharacteristic);
  
  void onCharacteristicRead(MyBluetoothGatt paramMyBluetoothGatt, BluetoothGattCharacteristic paramBluetoothGattCharacteristic, int paramInt);
  
  void onCharacteristicWrite(MyBluetoothGatt paramMyBluetoothGatt, BluetoothGattCharacteristic paramBluetoothGattCharacteristic, int paramInt);
  
  void onConnectionStateChange(MyBluetoothGatt paramMyBluetoothGatt, int paramInt1, int paramInt2);
  
  void onDescriptorWrite(MyBluetoothGatt paramMyBluetoothGatt, BluetoothGattDescriptor paramBluetoothGattDescriptor, int paramInt);
  
  void onReadRemoteRssi(MyBluetoothGatt paramMyBluetoothGatt, int paramInt1, int paramInt2);
  
  void onServicesDiscovered(MyBluetoothGatt paramMyBluetoothGatt, int paramInt);
  
  void oncheck();
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\consmart\ble\MyBluetoothGattCallback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */