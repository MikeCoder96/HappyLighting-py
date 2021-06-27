package android.support.constraint;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

public class Group extends ConstraintHelper {
  public Group(Context paramContext) {
    super(paramContext);
  }
  
  public Group(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  public Group(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  protected void init(AttributeSet paramAttributeSet) {
    super.init(paramAttributeSet);
    this.mUseViewMeasure = false;
  }
  
  public void updatePostLayout(ConstraintLayout paramConstraintLayout) {
    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)getLayoutParams();
    layoutParams.widget.setWidth(0);
    layoutParams.widget.setHeight(0);
  }
  
  public void updatePreLayout(ConstraintLayout paramConstraintLayout) {
    float f;
    int i = getVisibility();
    if (Build.VERSION.SDK_INT >= 21) {
      f = getElevation();
    } else {
      f = 0.0F;
    } 
    for (byte b = 0; b < this.mCount; b++) {
      View view = paramConstraintLayout.getViewById(this.mIds[b]);
      if (view != null) {
        view.setVisibility(i);
        if (f > 0.0F && Build.VERSION.SDK_INT >= 21)
          view.setElevation(f); 
      } 
    } 
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\constraint\Group.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */