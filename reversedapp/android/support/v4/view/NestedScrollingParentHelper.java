package android.support.v4.view;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

public class NestedScrollingParentHelper {
  private int mNestedScrollAxes;
  
  private final ViewGroup mViewGroup;
  
  public NestedScrollingParentHelper(@NonNull ViewGroup paramViewGroup) {
    this.mViewGroup = paramViewGroup;
  }
  
  public int getNestedScrollAxes() {
    return this.mNestedScrollAxes;
  }
  
  public void onNestedScrollAccepted(@NonNull View paramView1, @NonNull View paramView2, int paramInt) {
    onNestedScrollAccepted(paramView1, paramView2, paramInt, 0);
  }
  
  public void onNestedScrollAccepted(@NonNull View paramView1, @NonNull View paramView2, int paramInt1, int paramInt2) {
    this.mNestedScrollAxes = paramInt1;
  }
  
  public void onStopNestedScroll(@NonNull View paramView) {
    onStopNestedScroll(paramView, 0);
  }
  
  public void onStopNestedScroll(@NonNull View paramView, int paramInt) {
    this.mNestedScrollAxes = 0;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\view\NestedScrollingParentHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */