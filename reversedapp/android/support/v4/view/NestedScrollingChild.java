package android.support.v4.view;

import android.support.annotation.Nullable;

public interface NestedScrollingChild {
  boolean dispatchNestedFling(float paramFloat1, float paramFloat2, boolean paramBoolean);
  
  boolean dispatchNestedPreFling(float paramFloat1, float paramFloat2);
  
  boolean dispatchNestedPreScroll(int paramInt1, int paramInt2, @Nullable int[] paramArrayOfint1, @Nullable int[] paramArrayOfint2);
  
  boolean dispatchNestedScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, @Nullable int[] paramArrayOfint);
  
  boolean hasNestedScrollingParent();
  
  boolean isNestedScrollingEnabled();
  
  void setNestedScrollingEnabled(boolean paramBoolean);
  
  boolean startNestedScroll(int paramInt);
  
  void stopNestedScroll();
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\view\NestedScrollingChild.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */