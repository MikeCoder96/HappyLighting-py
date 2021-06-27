package android.support.v7.widget;

import android.os.SystemClock;
import android.support.annotation.RestrictTo;
import android.support.v7.view.menu.ShowableListMenu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public abstract class ForwardingListener implements View.OnTouchListener, View.OnAttachStateChangeListener {
  private int mActivePointerId;
  
  private Runnable mDisallowIntercept;
  
  private boolean mForwarding;
  
  private final int mLongPressTimeout;
  
  private final float mScaledTouchSlop;
  
  final View mSrc;
  
  private final int mTapTimeout;
  
  private final int[] mTmpLocation = new int[2];
  
  private Runnable mTriggerLongPress;
  
  public ForwardingListener(View paramView) {
    this.mSrc = paramView;
    paramView.setLongClickable(true);
    paramView.addOnAttachStateChangeListener(this);
    this.mScaledTouchSlop = ViewConfiguration.get(paramView.getContext()).getScaledTouchSlop();
    this.mTapTimeout = ViewConfiguration.getTapTimeout();
    this.mLongPressTimeout = (this.mTapTimeout + ViewConfiguration.getLongPressTimeout()) / 2;
  }
  
  private void clearCallbacks() {
    if (this.mTriggerLongPress != null)
      this.mSrc.removeCallbacks(this.mTriggerLongPress); 
    if (this.mDisallowIntercept != null)
      this.mSrc.removeCallbacks(this.mDisallowIntercept); 
  }
  
  private boolean onTouchForwarded(MotionEvent paramMotionEvent) {
    View view = this.mSrc;
    ShowableListMenu showableListMenu = getPopup();
    if (showableListMenu == null || !showableListMenu.isShowing())
      return false; 
    DropDownListView dropDownListView = (DropDownListView)showableListMenu.getListView();
    if (dropDownListView == null || !dropDownListView.isShown())
      return false; 
    MotionEvent motionEvent = MotionEvent.obtainNoHistory(paramMotionEvent);
    toGlobalMotionEvent(view, motionEvent);
    toLocalMotionEvent((View)dropDownListView, motionEvent);
    boolean bool = dropDownListView.onForwardedEvent(motionEvent, this.mActivePointerId);
    motionEvent.recycle();
    int i = paramMotionEvent.getActionMasked();
    boolean bool1 = true;
    if (i != 1 && i != 3) {
      i = 1;
    } else {
      i = 0;
    } 
    if (!bool || i == 0)
      bool1 = false; 
    return bool1;
  }
  
  private boolean onTouchObserved(MotionEvent paramMotionEvent) {
    int i;
    View view = this.mSrc;
    if (!view.isEnabled())
      return false; 
    switch (paramMotionEvent.getActionMasked()) {
      default:
        return false;
      case 2:
        i = paramMotionEvent.findPointerIndex(this.mActivePointerId);
        if (i >= 0 && !pointInView(view, paramMotionEvent.getX(i), paramMotionEvent.getY(i), this.mScaledTouchSlop)) {
          clearCallbacks();
          view.getParent().requestDisallowInterceptTouchEvent(true);
          return true;
        } 
      case 1:
      case 3:
        clearCallbacks();
      case 0:
        break;
    } 
    this.mActivePointerId = paramMotionEvent.getPointerId(0);
    if (this.mDisallowIntercept == null)
      this.mDisallowIntercept = new DisallowIntercept(); 
    view.postDelayed(this.mDisallowIntercept, this.mTapTimeout);
    if (this.mTriggerLongPress == null)
      this.mTriggerLongPress = new TriggerLongPress(); 
    view.postDelayed(this.mTriggerLongPress, this.mLongPressTimeout);
  }
  
  private static boolean pointInView(View paramView, float paramFloat1, float paramFloat2, float paramFloat3) {
    boolean bool;
    float f = -paramFloat3;
    if (paramFloat1 >= f && paramFloat2 >= f && paramFloat1 < (paramView.getRight() - paramView.getLeft()) + paramFloat3 && paramFloat2 < (paramView.getBottom() - paramView.getTop()) + paramFloat3) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private boolean toGlobalMotionEvent(View paramView, MotionEvent paramMotionEvent) {
    int[] arrayOfInt = this.mTmpLocation;
    paramView.getLocationOnScreen(arrayOfInt);
    paramMotionEvent.offsetLocation(arrayOfInt[0], arrayOfInt[1]);
    return true;
  }
  
  private boolean toLocalMotionEvent(View paramView, MotionEvent paramMotionEvent) {
    int[] arrayOfInt = this.mTmpLocation;
    paramView.getLocationOnScreen(arrayOfInt);
    paramMotionEvent.offsetLocation(-arrayOfInt[0], -arrayOfInt[1]);
    return true;
  }
  
  public abstract ShowableListMenu getPopup();
  
  protected boolean onForwardingStarted() {
    ShowableListMenu showableListMenu = getPopup();
    if (showableListMenu != null && !showableListMenu.isShowing())
      showableListMenu.show(); 
    return true;
  }
  
  protected boolean onForwardingStopped() {
    ShowableListMenu showableListMenu = getPopup();
    if (showableListMenu != null && showableListMenu.isShowing())
      showableListMenu.dismiss(); 
    return true;
  }
  
  void onLongPress() {
    clearCallbacks();
    View view = this.mSrc;
    if (!view.isEnabled() || view.isLongClickable())
      return; 
    if (!onForwardingStarted())
      return; 
    view.getParent().requestDisallowInterceptTouchEvent(true);
    long l = SystemClock.uptimeMillis();
    MotionEvent motionEvent = MotionEvent.obtain(l, l, 3, 0.0F, 0.0F, 0);
    view.onTouchEvent(motionEvent);
    motionEvent.recycle();
    this.mForwarding = true;
  }
  
  public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
    boolean bool2;
    boolean bool = this.mForwarding;
    boolean bool1 = false;
    if (bool) {
      if (onTouchForwarded(paramMotionEvent) || !onForwardingStopped()) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
    } else {
      boolean bool3;
      if (onTouchObserved(paramMotionEvent) && onForwardingStarted()) {
        bool3 = true;
      } else {
        bool3 = false;
      } 
      bool2 = bool3;
      if (bool3) {
        long l = SystemClock.uptimeMillis();
        MotionEvent motionEvent = MotionEvent.obtain(l, l, 3, 0.0F, 0.0F, 0);
        this.mSrc.onTouchEvent(motionEvent);
        motionEvent.recycle();
        bool2 = bool3;
      } 
    } 
    this.mForwarding = bool2;
    if (!bool2) {
      boolean bool3 = bool1;
      return bool ? true : bool3;
    } 
    return true;
  }
  
  public void onViewAttachedToWindow(View paramView) {}
  
  public void onViewDetachedFromWindow(View paramView) {
    this.mForwarding = false;
    this.mActivePointerId = -1;
    if (this.mDisallowIntercept != null)
      this.mSrc.removeCallbacks(this.mDisallowIntercept); 
  }
  
  private class DisallowIntercept implements Runnable {
    public void run() {
      ViewParent viewParent = ForwardingListener.this.mSrc.getParent();
      if (viewParent != null)
        viewParent.requestDisallowInterceptTouchEvent(true); 
    }
  }
  
  private class TriggerLongPress implements Runnable {
    public void run() {
      ForwardingListener.this.onLongPress();
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v7\widget\ForwardingListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */