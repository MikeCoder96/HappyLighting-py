package android.support.v7.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class AlertDialogLayout extends LinearLayoutCompat {
  public AlertDialogLayout(@Nullable Context paramContext) {
    super(paramContext);
  }
  
  public AlertDialogLayout(@Nullable Context paramContext, @Nullable AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  private void forceUniformWidth(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
    for (byte b = 0; b < paramInt1; b++) {
      View view = getChildAt(b);
      if (view.getVisibility() != 8) {
        LinearLayoutCompat.LayoutParams layoutParams = (LinearLayoutCompat.LayoutParams)view.getLayoutParams();
        if (layoutParams.width == -1) {
          int j = layoutParams.height;
          layoutParams.height = view.getMeasuredHeight();
          measureChildWithMargins(view, i, 0, paramInt2, 0);
          layoutParams.height = j;
        } 
      } 
    } 
  }
  
  private static int resolveMinimumHeight(View paramView) {
    int i = ViewCompat.getMinimumHeight(paramView);
    if (i > 0)
      return i; 
    if (paramView instanceof ViewGroup) {
      ViewGroup viewGroup = (ViewGroup)paramView;
      if (viewGroup.getChildCount() == 1)
        return resolveMinimumHeight(viewGroup.getChildAt(0)); 
    } 
    return 0;
  }
  
  private void setChildFrame(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    paramView.layout(paramInt1, paramInt2, paramInt3 + paramInt1, paramInt4 + paramInt2);
  }
  
  private boolean tryOnMeasure(int paramInt1, int paramInt2) {
    int i3;
    byte b;
    int i = getChildCount();
    View view1 = null;
    View view2 = view1;
    View view3 = view2;
    int j = 0;
    View view4 = view2;
    while (j < i) {
      view2 = getChildAt(j);
      if (view2.getVisibility() != 8) {
        k = view2.getId();
        if (k == R.id.topPanel) {
          view1 = view2;
        } else if (k == R.id.buttonPanel) {
          view4 = view2;
        } else if (k == R.id.contentPanel || k == R.id.customPanel) {
          if (view3 != null)
            return false; 
          view3 = view2;
        } else {
          return false;
        } 
      } 
      j++;
    } 
    int m = View.MeasureSpec.getMode(paramInt2);
    int n = View.MeasureSpec.getSize(paramInt2);
    int i1 = View.MeasureSpec.getMode(paramInt1);
    int i2 = getPaddingTop() + getPaddingBottom();
    if (view1 != null) {
      view1.measure(paramInt1, 0);
      i2 += view1.getMeasuredHeight();
      k = View.combineMeasuredStates(0, view1.getMeasuredState());
    } else {
      k = 0;
    } 
    if (view4 != null) {
      view4.measure(paramInt1, 0);
      j = resolveMinimumHeight(view4);
      i3 = view4.getMeasuredHeight() - j;
      i2 += j;
      k = View.combineMeasuredStates(k, view4.getMeasuredState());
    } else {
      j = 0;
      i3 = 0;
    } 
    if (view3 != null) {
      int i7;
      if (m == 0) {
        i7 = 0;
      } else {
        i7 = View.MeasureSpec.makeMeasureSpec(Math.max(0, n - i2), m);
      } 
      view3.measure(paramInt1, i7);
      b = view3.getMeasuredHeight();
      i2 += b;
      k = View.combineMeasuredStates(k, view3.getMeasuredState());
    } else {
      b = 0;
    } 
    int i5 = n - i2;
    n = k;
    int i6 = i5;
    int i4 = i2;
    if (view4 != null) {
      i3 = Math.min(i5, i3);
      n = i5;
      i4 = j;
      if (i3 > 0) {
        n = i5 - i3;
        i4 = j + i3;
      } 
      view4.measure(paramInt1, View.MeasureSpec.makeMeasureSpec(i4, 1073741824));
      i4 = i2 - j + view4.getMeasuredHeight();
      j = View.combineMeasuredStates(k, view4.getMeasuredState());
      i6 = n;
      n = j;
    } 
    int k = n;
    j = i4;
    if (view3 != null) {
      k = n;
      j = i4;
      if (i6 > 0) {
        view3.measure(paramInt1, View.MeasureSpec.makeMeasureSpec(b + i6, m));
        j = i4 - b + view3.getMeasuredHeight();
        k = View.combineMeasuredStates(n, view3.getMeasuredState());
      } 
    } 
    n = 0;
    for (i4 = 0; n < i; i4 = i2) {
      view2 = getChildAt(n);
      i2 = i4;
      if (view2.getVisibility() != 8)
        i2 = Math.max(i4, view2.getMeasuredWidth()); 
      n++;
    } 
    setMeasuredDimension(View.resolveSizeAndState(i4 + getPaddingLeft() + getPaddingRight(), paramInt1, k), View.resolveSizeAndState(j, paramInt2, 0));
    if (i1 != 1073741824)
      forceUniformWidth(i, paramInt2); 
    return true;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = getPaddingLeft();
    int j = paramInt3 - paramInt1;
    int k = getPaddingRight();
    int m = getPaddingRight();
    paramInt1 = getMeasuredHeight();
    int n = getChildCount();
    int i1 = getGravity();
    paramInt3 = i1 & 0x70;
    if (paramInt3 != 16) {
      if (paramInt3 != 80) {
        paramInt1 = getPaddingTop();
      } else {
        paramInt1 = getPaddingTop() + paramInt4 - paramInt2 - paramInt1;
      } 
    } else {
      paramInt3 = getPaddingTop();
      paramInt1 = (paramInt4 - paramInt2 - paramInt1) / 2 + paramInt3;
    } 
    Drawable drawable = getDividerDrawable();
    if (drawable == null) {
      paramInt3 = 0;
    } else {
      paramInt3 = drawable.getIntrinsicHeight();
    } 
    paramInt4 = 0;
    while (paramInt4 < n) {
      View view = getChildAt(paramInt4);
      paramInt2 = paramInt1;
      if (view != null) {
        paramInt2 = paramInt1;
        if (view.getVisibility() != 8) {
          int i2 = view.getMeasuredWidth();
          int i3 = view.getMeasuredHeight();
          LinearLayoutCompat.LayoutParams layoutParams = (LinearLayoutCompat.LayoutParams)view.getLayoutParams();
          int i4 = layoutParams.gravity;
          paramInt2 = i4;
          if (i4 < 0)
            paramInt2 = i1 & 0x800007; 
          paramInt2 = GravityCompat.getAbsoluteGravity(paramInt2, ViewCompat.getLayoutDirection((View)this)) & 0x7;
          if (paramInt2 != 1) {
            if (paramInt2 != 5) {
              paramInt2 = layoutParams.leftMargin + i;
            } else {
              paramInt2 = j - k - i2 - layoutParams.rightMargin;
            } 
          } else {
            paramInt2 = (j - i - m - i2) / 2 + i + layoutParams.leftMargin - layoutParams.rightMargin;
          } 
          i4 = paramInt1;
          if (hasDividerBeforeChildAt(paramInt4))
            i4 = paramInt1 + paramInt3; 
          paramInt1 = i4 + layoutParams.topMargin;
          setChildFrame(view, paramInt2, paramInt1, i2, i3);
          paramInt2 = paramInt1 + i3 + layoutParams.bottomMargin;
        } 
      } 
      paramInt4++;
      paramInt1 = paramInt2;
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    if (!tryOnMeasure(paramInt1, paramInt2))
      super.onMeasure(paramInt1, paramInt2); 
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v7\widget\AlertDialogLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */