package com.qh.ui.dialog;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public abstract class BaseDialogFragment extends DialogFragment {
  protected static final float COLOR_DEFAULT = 1.0F;
  
  protected static final float COLOR_TRANSPARENT = 0.0F;
  
  protected static final int SIZE_DEFAULT = -1;
  
  protected static final int SIZE_FULL_WINDOW = 2;
  
  protected static final int SIZE_LOADING = 1;
  
  private float color = 1.0F;
  
  private int size = -1;
  
  private void initDialog() {
    getDialog().getWindow().setBackgroundDrawable((Drawable)new ColorDrawable(0));
    getDialog().getWindow().requestFeature(1);
  }
  
  public abstract int getLayoutId();
  
  protected abstract void init(View paramView);
  
  protected void initWindowSize() {
    Dialog dialog = getDialog();
    if (dialog != null) {
      DisplayMetrics displayMetrics = new DisplayMetrics();
      getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
      if (this.size == 1) {
        dialog.getWindow().setLayout(-2, -2);
      } else {
        Window window;
        WindowManager.LayoutParams layoutParams;
        if (this.size == 2) {
          window = dialog.getWindow();
          layoutParams = window.getAttributes();
          layoutParams.width = -1;
          layoutParams.height = -1;
          window.setAttributes(layoutParams);
        } else {
          window = window.getWindow();
          double d = ((DisplayMetrics)layoutParams).widthPixels;
          Double.isNaN(d);
          window.setLayout((int)(d * 0.8D), -2);
        } 
      } 
    } 
  }
  
  protected void initWindowSize(int paramInt) {
    this.size = paramInt;
    initWindowSize();
  }
  
  @NonNull
  public Dialog onCreateDialog(Bundle paramBundle) {
    return super.onCreateDialog(paramBundle);
  }
  
  @Nullable
  public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle) {
    View view = paramLayoutInflater.inflate(getLayoutId(), paramViewGroup, false);
    init(view);
    initDialog();
    return view;
  }
  
  public void onStart() {
    super.onStart();
    if (this.color != 1.0F) {
      Window window = getDialog().getWindow();
      WindowManager.LayoutParams layoutParams = window.getAttributes();
      layoutParams.dimAmount = this.color;
      window.setAttributes(layoutParams);
    } 
  }
  
  protected void setOutsideColor(float paramFloat) {
    this.color = paramFloat;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\q\\ui\dialog\BaseDialogFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */