package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

abstract class AbsActionBarView extends ViewGroup {
  private static final int FADE_DURATION = 200;
  
  protected ActionMenuPresenter mActionMenuPresenter;
  
  protected int mContentHeight;
  
  private boolean mEatingHover;
  
  private boolean mEatingTouch;
  
  protected ActionMenuView mMenuView;
  
  protected final Context mPopupContext;
  
  protected final VisibilityAnimListener mVisAnimListener = new VisibilityAnimListener();
  
  protected ViewPropertyAnimatorCompat mVisibilityAnim;
  
  AbsActionBarView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  AbsActionBarView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  AbsActionBarView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    TypedValue typedValue = new TypedValue();
    if (paramContext.getTheme().resolveAttribute(R.attr.actionBarPopupTheme, typedValue, true) && typedValue.resourceId != 0) {
      this.mPopupContext = (Context)new ContextThemeWrapper(paramContext, typedValue.resourceId);
    } else {
      this.mPopupContext = paramContext;
    } 
  }
  
  protected static int next(int paramInt1, int paramInt2, boolean paramBoolean) {
    if (paramBoolean) {
      paramInt1 -= paramInt2;
    } else {
      paramInt1 += paramInt2;
    } 
    return paramInt1;
  }
  
  public void animateToVisibility(int paramInt) {
    setupAnimatorToVisibility(paramInt, 200L).start();
  }
  
  public boolean canShowOverflowMenu() {
    boolean bool;
    if (isOverflowReserved() && getVisibility() == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void dismissPopupMenus() {
    if (this.mActionMenuPresenter != null)
      this.mActionMenuPresenter.dismissPopupMenus(); 
  }
  
  public int getAnimatedVisibility() {
    return (this.mVisibilityAnim != null) ? this.mVisAnimListener.mFinalVisibility : getVisibility();
  }
  
  public int getContentHeight() {
    return this.mContentHeight;
  }
  
  public boolean hideOverflowMenu() {
    return (this.mActionMenuPresenter != null) ? this.mActionMenuPresenter.hideOverflowMenu() : false;
  }
  
  public boolean isOverflowMenuShowPending() {
    return (this.mActionMenuPresenter != null) ? this.mActionMenuPresenter.isOverflowMenuShowPending() : false;
  }
  
  public boolean isOverflowMenuShowing() {
    return (this.mActionMenuPresenter != null) ? this.mActionMenuPresenter.isOverflowMenuShowing() : false;
  }
  
  public boolean isOverflowReserved() {
    boolean bool;
    if (this.mActionMenuPresenter != null && this.mActionMenuPresenter.isOverflowReserved()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  protected int measureChildView(View paramView, int paramInt1, int paramInt2, int paramInt3) {
    paramView.measure(View.MeasureSpec.makeMeasureSpec(paramInt1, -2147483648), paramInt2);
    return Math.max(0, paramInt1 - paramView.getMeasuredWidth() - paramInt3);
  }
  
  protected void onConfigurationChanged(Configuration paramConfiguration) {
    super.onConfigurationChanged(paramConfiguration);
    TypedArray typedArray = getContext().obtainStyledAttributes(null, R.styleable.ActionBar, R.attr.actionBarStyle, 0);
    setContentHeight(typedArray.getLayoutDimension(R.styleable.ActionBar_height, 0));
    typedArray.recycle();
    if (this.mActionMenuPresenter != null)
      this.mActionMenuPresenter.onConfigurationChanged(paramConfiguration); 
  }
  
  public boolean onHoverEvent(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getActionMasked();
    if (i == 9)
      this.mEatingHover = false; 
    if (!this.mEatingHover) {
      boolean bool = super.onHoverEvent(paramMotionEvent);
      if (i == 9 && !bool)
        this.mEatingHover = true; 
    } 
    if (i == 10 || i == 3)
      this.mEatingHover = false; 
    return true;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getActionMasked();
    if (i == 0)
      this.mEatingTouch = false; 
    if (!this.mEatingTouch) {
      boolean bool = super.onTouchEvent(paramMotionEvent);
      if (i == 0 && !bool)
        this.mEatingTouch = true; 
    } 
    if (i == 1 || i == 3)
      this.mEatingTouch = false; 
    return true;
  }
  
  protected int positionChild(View paramView, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean) {
    int i = paramView.getMeasuredWidth();
    int j = paramView.getMeasuredHeight();
    paramInt2 += (paramInt3 - j) / 2;
    if (paramBoolean) {
      paramView.layout(paramInt1 - i, paramInt2, paramInt1, j + paramInt2);
    } else {
      paramView.layout(paramInt1, paramInt2, paramInt1 + i, j + paramInt2);
    } 
    paramInt1 = i;
    if (paramBoolean)
      paramInt1 = -i; 
    return paramInt1;
  }
  
  public void postShowOverflowMenu() {
    post(new Runnable() {
          public void run() {
            AbsActionBarView.this.showOverflowMenu();
          }
        });
  }
  
  public void setContentHeight(int paramInt) {
    this.mContentHeight = paramInt;
    requestLayout();
  }
  
  public void setVisibility(int paramInt) {
    if (paramInt != getVisibility()) {
      if (this.mVisibilityAnim != null)
        this.mVisibilityAnim.cancel(); 
      super.setVisibility(paramInt);
    } 
  }
  
  public ViewPropertyAnimatorCompat setupAnimatorToVisibility(int paramInt, long paramLong) {
    if (this.mVisibilityAnim != null)
      this.mVisibilityAnim.cancel(); 
    if (paramInt == 0) {
      if (getVisibility() != 0)
        setAlpha(0.0F); 
      ViewPropertyAnimatorCompat viewPropertyAnimatorCompat1 = ViewCompat.animate((View)this).alpha(1.0F);
      viewPropertyAnimatorCompat1.setDuration(paramLong);
      viewPropertyAnimatorCompat1.setListener(this.mVisAnimListener.withFinalVisibility(viewPropertyAnimatorCompat1, paramInt));
      return viewPropertyAnimatorCompat1;
    } 
    ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = ViewCompat.animate((View)this).alpha(0.0F);
    viewPropertyAnimatorCompat.setDuration(paramLong);
    viewPropertyAnimatorCompat.setListener(this.mVisAnimListener.withFinalVisibility(viewPropertyAnimatorCompat, paramInt));
    return viewPropertyAnimatorCompat;
  }
  
  public boolean showOverflowMenu() {
    return (this.mActionMenuPresenter != null) ? this.mActionMenuPresenter.showOverflowMenu() : false;
  }
  
  protected class VisibilityAnimListener implements ViewPropertyAnimatorListener {
    private boolean mCanceled = false;
    
    int mFinalVisibility;
    
    public void onAnimationCancel(View param1View) {
      this.mCanceled = true;
    }
    
    public void onAnimationEnd(View param1View) {
      if (this.mCanceled)
        return; 
      AbsActionBarView.this.mVisibilityAnim = null;
      AbsActionBarView.this.setVisibility(this.mFinalVisibility);
    }
    
    public void onAnimationStart(View param1View) {
      AbsActionBarView.this.setVisibility(0);
      this.mCanceled = false;
    }
    
    public VisibilityAnimListener withFinalVisibility(ViewPropertyAnimatorCompat param1ViewPropertyAnimatorCompat, int param1Int) {
      AbsActionBarView.this.mVisibilityAnim = param1ViewPropertyAnimatorCompat;
      this.mFinalVisibility = param1Int;
      return this;
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v7\widget\AbsActionBarView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */