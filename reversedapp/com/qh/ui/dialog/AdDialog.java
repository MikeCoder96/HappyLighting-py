package com.qh.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.qh.ui.dialog.listener.IAdDialogClickListener;

public class AdDialog extends BaseDialogFragment {
  private IAdDialogClickListener listener;
  
  private ImageView mIvAd;
  
  private RelativeLayout mRlCancel;
  
  public int getLayoutId() {
    return 2131361845;
  }
  
  protected void init(View paramView) {
    this.mRlCancel = (RelativeLayout)paramView.findViewById(2131231106);
    this.mIvAd = (ImageView)paramView.findViewById(2131230946);
    Bundle bundle = getArguments();
    if (bundle.containsKey("image")) {
      int i = bundle.getInt("image");
      this.mIvAd.setImageResource(i);
    } 
    if (bundle.containsKey("url")) {
      String str = bundle.getString("url");
      loadImageView(getContext(), str, this.mIvAd);
    } 
    this.mIvAd.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (AdDialog.this.listener != null) {
              AdDialog.this.listener.onPicClick();
              AdDialog.this.dismiss();
            } 
          }
        });
    this.mRlCancel.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (AdDialog.this.listener != null) {
              AdDialog.this.listener.onCancelClick();
              AdDialog.this.dismiss();
            } 
          }
        });
  }
  
  public void loadImageView(Context paramContext, String paramString, ImageView paramImageView) {
    if (paramContext != null && Build.VERSION.SDK_INT >= 17 && paramContext instanceof Activity)
      ((Activity)paramContext).isDestroyed(); 
  }
  
  public void onStart() {
    super.onStart();
    initWindowSize(2);
  }
  
  public void setOnDialogClickListener(IAdDialogClickListener paramIAdDialogClickListener) {
    this.listener = paramIAdDialogClickListener;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\q\\ui\dialog\AdDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */