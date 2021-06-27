package android.support.v4.view;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.compat.R;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;

public final class ViewGroupCompat {
  public static final int LAYOUT_MODE_CLIP_BOUNDS = 0;
  
  public static final int LAYOUT_MODE_OPTICAL_BOUNDS = 1;
  
  public static int getLayoutMode(@NonNull ViewGroup paramViewGroup) {
    return (Build.VERSION.SDK_INT >= 18) ? paramViewGroup.getLayoutMode() : 0;
  }
  
  public static int getNestedScrollAxes(@NonNull ViewGroup paramViewGroup) {
    return (Build.VERSION.SDK_INT >= 21) ? paramViewGroup.getNestedScrollAxes() : ((paramViewGroup instanceof NestedScrollingParent) ? ((NestedScrollingParent)paramViewGroup).getNestedScrollAxes() : 0);
  }
  
  public static boolean isTransitionGroup(@NonNull ViewGroup paramViewGroup) {
    if (Build.VERSION.SDK_INT >= 21)
      return paramViewGroup.isTransitionGroup(); 
    Boolean bool = (Boolean)paramViewGroup.getTag(R.id.tag_transition_group);
    return ((bool != null && bool.booleanValue()) || paramViewGroup.getBackground() != null || ViewCompat.getTransitionName((View)paramViewGroup) != null);
  }
  
  @Deprecated
  public static boolean onRequestSendAccessibilityEvent(ViewGroup paramViewGroup, View paramView, AccessibilityEvent paramAccessibilityEvent) {
    return paramViewGroup.onRequestSendAccessibilityEvent(paramView, paramAccessibilityEvent);
  }
  
  public static void setLayoutMode(@NonNull ViewGroup paramViewGroup, int paramInt) {
    if (Build.VERSION.SDK_INT >= 18)
      paramViewGroup.setLayoutMode(paramInt); 
  }
  
  @Deprecated
  public static void setMotionEventSplittingEnabled(ViewGroup paramViewGroup, boolean paramBoolean) {
    paramViewGroup.setMotionEventSplittingEnabled(paramBoolean);
  }
  
  public static void setTransitionGroup(@NonNull ViewGroup paramViewGroup, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 21) {
      paramViewGroup.setTransitionGroup(paramBoolean);
    } else {
      paramViewGroup.setTag(R.id.tag_transition_group, Boolean.valueOf(paramBoolean));
    } 
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\view\ViewGroupCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */