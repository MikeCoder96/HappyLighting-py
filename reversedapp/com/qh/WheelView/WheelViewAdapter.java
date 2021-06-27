package com.qh.WheelView;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

public interface WheelViewAdapter {
  View getEmptyItem(View paramView, ViewGroup paramViewGroup);
  
  View getItem(int paramInt, View paramView, ViewGroup paramViewGroup);
  
  int getItemsCount();
  
  void registerDataSetObserver(DataSetObserver paramDataSetObserver);
  
  void unregisterDataSetObserver(DataSetObserver paramDataSetObserver);
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\WheelView\WheelViewAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */