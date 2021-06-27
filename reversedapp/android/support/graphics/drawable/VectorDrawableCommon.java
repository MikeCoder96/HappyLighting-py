package android.support.graphics.drawable;

import android.content.res.Resources;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.graphics.drawable.TintAwareDrawable;

abstract class VectorDrawableCommon extends Drawable implements TintAwareDrawable {
  Drawable mDelegateDrawable;
  
  public void applyTheme(Resources.Theme paramTheme) {
    if (this.mDelegateDrawable != null) {
      DrawableCompat.applyTheme(this.mDelegateDrawable, paramTheme);
      return;
    } 
  }
  
  public void clearColorFilter() {
    if (this.mDelegateDrawable != null) {
      this.mDelegateDrawable.clearColorFilter();
      return;
    } 
    super.clearColorFilter();
  }
  
  public ColorFilter getColorFilter() {
    return (this.mDelegateDrawable != null) ? DrawableCompat.getColorFilter(this.mDelegateDrawable) : null;
  }
  
  public Drawable getCurrent() {
    return (this.mDelegateDrawable != null) ? this.mDelegateDrawable.getCurrent() : super.getCurrent();
  }
  
  public int getMinimumHeight() {
    return (this.mDelegateDrawable != null) ? this.mDelegateDrawable.getMinimumHeight() : super.getMinimumHeight();
  }
  
  public int getMinimumWidth() {
    return (this.mDelegateDrawable != null) ? this.mDelegateDrawable.getMinimumWidth() : super.getMinimumWidth();
  }
  
  public boolean getPadding(Rect paramRect) {
    return (this.mDelegateDrawable != null) ? this.mDelegateDrawable.getPadding(paramRect) : super.getPadding(paramRect);
  }
  
  public int[] getState() {
    return (this.mDelegateDrawable != null) ? this.mDelegateDrawable.getState() : super.getState();
  }
  
  public Region getTransparentRegion() {
    return (this.mDelegateDrawable != null) ? this.mDelegateDrawable.getTransparentRegion() : super.getTransparentRegion();
  }
  
  public void jumpToCurrentState() {
    if (this.mDelegateDrawable != null) {
      DrawableCompat.jumpToCurrentState(this.mDelegateDrawable);
      return;
    } 
  }
  
  protected void onBoundsChange(Rect paramRect) {
    if (this.mDelegateDrawable != null) {
      this.mDelegateDrawable.setBounds(paramRect);
      return;
    } 
    super.onBoundsChange(paramRect);
  }
  
  protected boolean onLevelChange(int paramInt) {
    return (this.mDelegateDrawable != null) ? this.mDelegateDrawable.setLevel(paramInt) : super.onLevelChange(paramInt);
  }
  
  public void setChangingConfigurations(int paramInt) {
    if (this.mDelegateDrawable != null) {
      this.mDelegateDrawable.setChangingConfigurations(paramInt);
      return;
    } 
    super.setChangingConfigurations(paramInt);
  }
  
  public void setColorFilter(int paramInt, PorterDuff.Mode paramMode) {
    if (this.mDelegateDrawable != null) {
      this.mDelegateDrawable.setColorFilter(paramInt, paramMode);
      return;
    } 
    super.setColorFilter(paramInt, paramMode);
  }
  
  public void setFilterBitmap(boolean paramBoolean) {
    if (this.mDelegateDrawable != null) {
      this.mDelegateDrawable.setFilterBitmap(paramBoolean);
      return;
    } 
  }
  
  public void setHotspot(float paramFloat1, float paramFloat2) {
    if (this.mDelegateDrawable != null)
      DrawableCompat.setHotspot(this.mDelegateDrawable, paramFloat1, paramFloat2); 
  }
  
  public void setHotspotBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (this.mDelegateDrawable != null) {
      DrawableCompat.setHotspotBounds(this.mDelegateDrawable, paramInt1, paramInt2, paramInt3, paramInt4);
      return;
    } 
  }
  
  public boolean setState(int[] paramArrayOfint) {
    return (this.mDelegateDrawable != null) ? this.mDelegateDrawable.setState(paramArrayOfint) : super.setState(paramArrayOfint);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\graphics\drawable\VectorDrawableCommon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */