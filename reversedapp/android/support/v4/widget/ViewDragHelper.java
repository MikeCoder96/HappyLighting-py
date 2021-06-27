package android.support.v4.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.OverScroller;
import java.util.Arrays;

public class ViewDragHelper {
  private static final int BASE_SETTLE_DURATION = 256;
  
  public static final int DIRECTION_ALL = 3;
  
  public static final int DIRECTION_HORIZONTAL = 1;
  
  public static final int DIRECTION_VERTICAL = 2;
  
  public static final int EDGE_ALL = 15;
  
  public static final int EDGE_BOTTOM = 8;
  
  public static final int EDGE_LEFT = 1;
  
  public static final int EDGE_RIGHT = 2;
  
  private static final int EDGE_SIZE = 20;
  
  public static final int EDGE_TOP = 4;
  
  public static final int INVALID_POINTER = -1;
  
  private static final int MAX_SETTLE_DURATION = 600;
  
  public static final int STATE_DRAGGING = 1;
  
  public static final int STATE_IDLE = 0;
  
  public static final int STATE_SETTLING = 2;
  
  private static final String TAG = "ViewDragHelper";
  
  private static final Interpolator sInterpolator = new Interpolator() {
      public float getInterpolation(float param1Float) {
        param1Float--;
        return param1Float * param1Float * param1Float * param1Float * param1Float + 1.0F;
      }
    };
  
  private int mActivePointerId = -1;
  
  private final Callback mCallback;
  
  private View mCapturedView;
  
  private int mDragState;
  
  private int[] mEdgeDragsInProgress;
  
  private int[] mEdgeDragsLocked;
  
  private int mEdgeSize;
  
  private int[] mInitialEdgesTouched;
  
  private float[] mInitialMotionX;
  
  private float[] mInitialMotionY;
  
  private float[] mLastMotionX;
  
  private float[] mLastMotionY;
  
  private float mMaxVelocity;
  
  private float mMinVelocity;
  
  private final ViewGroup mParentView;
  
  private int mPointersDown;
  
  private boolean mReleaseInProgress;
  
  private OverScroller mScroller;
  
  private final Runnable mSetIdleRunnable = new Runnable() {
      public void run() {
        ViewDragHelper.this.setDragState(0);
      }
    };
  
  private int mTouchSlop;
  
  private int mTrackingEdges;
  
  private VelocityTracker mVelocityTracker;
  
  private ViewDragHelper(@NonNull Context paramContext, @NonNull ViewGroup paramViewGroup, @NonNull Callback paramCallback) {
    if (paramViewGroup != null) {
      if (paramCallback != null) {
        this.mParentView = paramViewGroup;
        this.mCallback = paramCallback;
        ViewConfiguration viewConfiguration = ViewConfiguration.get(paramContext);
        this.mEdgeSize = (int)((paramContext.getResources().getDisplayMetrics()).density * 20.0F + 0.5F);
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.mMaxVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        this.mMinVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mScroller = new OverScroller(paramContext, sInterpolator);
        return;
      } 
      throw new IllegalArgumentException("Callback may not be null");
    } 
    throw new IllegalArgumentException("Parent view may not be null");
  }
  
  private boolean checkNewEdgeDrag(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2) {
    paramFloat1 = Math.abs(paramFloat1);
    paramFloat2 = Math.abs(paramFloat2);
    int i = this.mInitialEdgesTouched[paramInt1];
    boolean bool1 = false;
    if ((i & paramInt2) != paramInt2 || (this.mTrackingEdges & paramInt2) == 0 || (this.mEdgeDragsLocked[paramInt1] & paramInt2) == paramInt2 || (this.mEdgeDragsInProgress[paramInt1] & paramInt2) == paramInt2 || (paramFloat1 <= this.mTouchSlop && paramFloat2 <= this.mTouchSlop))
      return false; 
    if (paramFloat1 < paramFloat2 * 0.5F && this.mCallback.onEdgeLock(paramInt2)) {
      int[] arrayOfInt = this.mEdgeDragsLocked;
      arrayOfInt[paramInt1] = arrayOfInt[paramInt1] | paramInt2;
      return false;
    } 
    boolean bool2 = bool1;
    if ((this.mEdgeDragsInProgress[paramInt1] & paramInt2) == 0) {
      bool2 = bool1;
      if (paramFloat1 > this.mTouchSlop)
        bool2 = true; 
    } 
    return bool2;
  }
  
  private boolean checkTouchSlop(View paramView, float paramFloat1, float paramFloat2) {
    boolean bool4;
    boolean bool5;
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    if (paramView == null)
      return false; 
    if (this.mCallback.getViewHorizontalDragRange(paramView) > 0) {
      bool4 = true;
    } else {
      bool4 = false;
    } 
    if (this.mCallback.getViewVerticalDragRange(paramView) > 0) {
      bool5 = true;
    } else {
      bool5 = false;
    } 
    if (bool4 && bool5) {
      if (paramFloat1 * paramFloat1 + paramFloat2 * paramFloat2 > (this.mTouchSlop * this.mTouchSlop))
        bool3 = true; 
      return bool3;
    } 
    if (bool4) {
      bool3 = bool1;
      if (Math.abs(paramFloat1) > this.mTouchSlop)
        bool3 = true; 
      return bool3;
    } 
    if (bool5) {
      bool3 = bool2;
      if (Math.abs(paramFloat2) > this.mTouchSlop)
        bool3 = true; 
      return bool3;
    } 
    return false;
  }
  
  private float clampMag(float paramFloat1, float paramFloat2, float paramFloat3) {
    float f = Math.abs(paramFloat1);
    if (f < paramFloat2)
      return 0.0F; 
    if (f > paramFloat3) {
      if (paramFloat1 <= 0.0F)
        paramFloat3 = -paramFloat3; 
      return paramFloat3;
    } 
    return paramFloat1;
  }
  
  private int clampMag(int paramInt1, int paramInt2, int paramInt3) {
    int i = Math.abs(paramInt1);
    if (i < paramInt2)
      return 0; 
    if (i > paramInt3) {
      if (paramInt1 <= 0)
        paramInt3 = -paramInt3; 
      return paramInt3;
    } 
    return paramInt1;
  }
  
  private void clearMotionHistory() {
    if (this.mInitialMotionX == null)
      return; 
    Arrays.fill(this.mInitialMotionX, 0.0F);
    Arrays.fill(this.mInitialMotionY, 0.0F);
    Arrays.fill(this.mLastMotionX, 0.0F);
    Arrays.fill(this.mLastMotionY, 0.0F);
    Arrays.fill(this.mInitialEdgesTouched, 0);
    Arrays.fill(this.mEdgeDragsInProgress, 0);
    Arrays.fill(this.mEdgeDragsLocked, 0);
    this.mPointersDown = 0;
  }
  
  private void clearMotionHistory(int paramInt) {
    if (this.mInitialMotionX == null || !isPointerDown(paramInt))
      return; 
    this.mInitialMotionX[paramInt] = 0.0F;
    this.mInitialMotionY[paramInt] = 0.0F;
    this.mLastMotionX[paramInt] = 0.0F;
    this.mLastMotionY[paramInt] = 0.0F;
    this.mInitialEdgesTouched[paramInt] = 0;
    this.mEdgeDragsInProgress[paramInt] = 0;
    this.mEdgeDragsLocked[paramInt] = 0;
    this.mPointersDown = (1 << paramInt ^ 0xFFFFFFFF) & this.mPointersDown;
  }
  
  private int computeAxisDuration(int paramInt1, int paramInt2, int paramInt3) {
    if (paramInt1 == 0)
      return 0; 
    int i = this.mParentView.getWidth();
    int j = i / 2;
    float f1 = Math.min(1.0F, Math.abs(paramInt1) / i);
    float f2 = j;
    f1 = distanceInfluenceForSnapDuration(f1);
    paramInt2 = Math.abs(paramInt2);
    if (paramInt2 > 0) {
      paramInt1 = Math.round(Math.abs((f2 + f1 * f2) / paramInt2) * 1000.0F) * 4;
    } else {
      paramInt1 = (int)((Math.abs(paramInt1) / paramInt3 + 1.0F) * 256.0F);
    } 
    return Math.min(paramInt1, 600);
  }
  
  private int computeSettleDuration(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    float f2;
    paramInt3 = clampMag(paramInt3, (int)this.mMinVelocity, (int)this.mMaxVelocity);
    paramInt4 = clampMag(paramInt4, (int)this.mMinVelocity, (int)this.mMaxVelocity);
    int i = Math.abs(paramInt1);
    int j = Math.abs(paramInt2);
    int k = Math.abs(paramInt3);
    int m = Math.abs(paramInt4);
    int n = k + m;
    int i1 = i + j;
    if (paramInt3 != 0) {
      f1 = k;
      f2 = n;
    } else {
      f1 = i;
      f2 = i1;
    } 
    float f3 = f1 / f2;
    if (paramInt4 != 0) {
      f2 = m;
      f1 = n;
    } else {
      f2 = j;
      f1 = i1;
    } 
    float f1 = f2 / f1;
    paramInt1 = computeAxisDuration(paramInt1, paramInt3, this.mCallback.getViewHorizontalDragRange(paramView));
    paramInt2 = computeAxisDuration(paramInt2, paramInt4, this.mCallback.getViewVerticalDragRange(paramView));
    return (int)(paramInt1 * f3 + paramInt2 * f1);
  }
  
  public static ViewDragHelper create(@NonNull ViewGroup paramViewGroup, float paramFloat, @NonNull Callback paramCallback) {
    ViewDragHelper viewDragHelper = create(paramViewGroup, paramCallback);
    viewDragHelper.mTouchSlop = (int)(viewDragHelper.mTouchSlop * 1.0F / paramFloat);
    return viewDragHelper;
  }
  
  public static ViewDragHelper create(@NonNull ViewGroup paramViewGroup, @NonNull Callback paramCallback) {
    return new ViewDragHelper(paramViewGroup.getContext(), paramViewGroup, paramCallback);
  }
  
  private void dispatchViewReleased(float paramFloat1, float paramFloat2) {
    this.mReleaseInProgress = true;
    this.mCallback.onViewReleased(this.mCapturedView, paramFloat1, paramFloat2);
    this.mReleaseInProgress = false;
    if (this.mDragState == 1)
      setDragState(0); 
  }
  
  private float distanceInfluenceForSnapDuration(float paramFloat) {
    return (float)Math.sin(((paramFloat - 0.5F) * 0.47123894F));
  }
  
  private void dragTo(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = this.mCapturedView.getLeft();
    int j = this.mCapturedView.getTop();
    int k = paramInt1;
    if (paramInt3 != 0) {
      k = this.mCallback.clampViewPositionHorizontal(this.mCapturedView, paramInt1, paramInt3);
      ViewCompat.offsetLeftAndRight(this.mCapturedView, k - i);
    } 
    paramInt1 = paramInt2;
    if (paramInt4 != 0) {
      paramInt1 = this.mCallback.clampViewPositionVertical(this.mCapturedView, paramInt2, paramInt4);
      ViewCompat.offsetTopAndBottom(this.mCapturedView, paramInt1 - j);
    } 
    if (paramInt3 != 0 || paramInt4 != 0)
      this.mCallback.onViewPositionChanged(this.mCapturedView, k, paramInt1, k - i, paramInt1 - j); 
  }
  
  private void ensureMotionHistorySizeForId(int paramInt) {
    if (this.mInitialMotionX == null || this.mInitialMotionX.length <= paramInt) {
      float[] arrayOfFloat1 = new float[++paramInt];
      float[] arrayOfFloat2 = new float[paramInt];
      float[] arrayOfFloat3 = new float[paramInt];
      float[] arrayOfFloat4 = new float[paramInt];
      int[] arrayOfInt1 = new int[paramInt];
      int[] arrayOfInt2 = new int[paramInt];
      int[] arrayOfInt3 = new int[paramInt];
      if (this.mInitialMotionX != null) {
        System.arraycopy(this.mInitialMotionX, 0, arrayOfFloat1, 0, this.mInitialMotionX.length);
        System.arraycopy(this.mInitialMotionY, 0, arrayOfFloat2, 0, this.mInitialMotionY.length);
        System.arraycopy(this.mLastMotionX, 0, arrayOfFloat3, 0, this.mLastMotionX.length);
        System.arraycopy(this.mLastMotionY, 0, arrayOfFloat4, 0, this.mLastMotionY.length);
        System.arraycopy(this.mInitialEdgesTouched, 0, arrayOfInt1, 0, this.mInitialEdgesTouched.length);
        System.arraycopy(this.mEdgeDragsInProgress, 0, arrayOfInt2, 0, this.mEdgeDragsInProgress.length);
        System.arraycopy(this.mEdgeDragsLocked, 0, arrayOfInt3, 0, this.mEdgeDragsLocked.length);
      } 
      this.mInitialMotionX = arrayOfFloat1;
      this.mInitialMotionY = arrayOfFloat2;
      this.mLastMotionX = arrayOfFloat3;
      this.mLastMotionY = arrayOfFloat4;
      this.mInitialEdgesTouched = arrayOfInt1;
      this.mEdgeDragsInProgress = arrayOfInt2;
      this.mEdgeDragsLocked = arrayOfInt3;
    } 
  }
  
  private boolean forceSettleCapturedViewAt(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = this.mCapturedView.getLeft();
    int j = this.mCapturedView.getTop();
    paramInt1 -= i;
    paramInt2 -= j;
    if (paramInt1 == 0 && paramInt2 == 0) {
      this.mScroller.abortAnimation();
      setDragState(0);
      return false;
    } 
    paramInt3 = computeSettleDuration(this.mCapturedView, paramInt1, paramInt2, paramInt3, paramInt4);
    this.mScroller.startScroll(i, j, paramInt1, paramInt2, paramInt3);
    setDragState(2);
    return true;
  }
  
  private int getEdgesTouched(int paramInt1, int paramInt2) {
    if (paramInt1 < this.mParentView.getLeft() + this.mEdgeSize) {
      i = 1;
    } else {
      i = 0;
    } 
    int j = i;
    if (paramInt2 < this.mParentView.getTop() + this.mEdgeSize)
      j = i | 0x4; 
    int i = j;
    if (paramInt1 > this.mParentView.getRight() - this.mEdgeSize)
      i = j | 0x2; 
    paramInt1 = i;
    if (paramInt2 > this.mParentView.getBottom() - this.mEdgeSize)
      paramInt1 = i | 0x8; 
    return paramInt1;
  }
  
  private boolean isValidPointerForActionMove(int paramInt) {
    if (!isPointerDown(paramInt)) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Ignoring pointerId=");
      stringBuilder.append(paramInt);
      stringBuilder.append(" because ACTION_DOWN was not received ");
      stringBuilder.append("for this pointer before ACTION_MOVE. It likely happened because ");
      stringBuilder.append(" ViewDragHelper did not receive all the events in the event stream.");
      Log.e("ViewDragHelper", stringBuilder.toString());
      return false;
    } 
    return true;
  }
  
  private void releaseViewForPointerUp() {
    this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaxVelocity);
    dispatchViewReleased(clampMag(this.mVelocityTracker.getXVelocity(this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity), clampMag(this.mVelocityTracker.getYVelocity(this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity));
  }
  
  private void reportNewEdgeDrags(float paramFloat1, float paramFloat2, int paramInt) {
    int i = 1;
    if (!checkNewEdgeDrag(paramFloat1, paramFloat2, paramInt, 1))
      i = 0; 
    int j = i;
    if (checkNewEdgeDrag(paramFloat2, paramFloat1, paramInt, 4))
      j = i | 0x4; 
    i = j;
    if (checkNewEdgeDrag(paramFloat1, paramFloat2, paramInt, 2))
      i = j | 0x2; 
    j = i;
    if (checkNewEdgeDrag(paramFloat2, paramFloat1, paramInt, 8))
      j = i | 0x8; 
    if (j != 0) {
      int[] arrayOfInt = this.mEdgeDragsInProgress;
      arrayOfInt[paramInt] = arrayOfInt[paramInt] | j;
      this.mCallback.onEdgeDragStarted(j, paramInt);
    } 
  }
  
  private void saveInitialMotion(float paramFloat1, float paramFloat2, int paramInt) {
    ensureMotionHistorySizeForId(paramInt);
    float[] arrayOfFloat = this.mInitialMotionX;
    this.mLastMotionX[paramInt] = paramFloat1;
    arrayOfFloat[paramInt] = paramFloat1;
    arrayOfFloat = this.mInitialMotionY;
    this.mLastMotionY[paramInt] = paramFloat2;
    arrayOfFloat[paramInt] = paramFloat2;
    this.mInitialEdgesTouched[paramInt] = getEdgesTouched((int)paramFloat1, (int)paramFloat2);
    this.mPointersDown |= 1 << paramInt;
  }
  
  private void saveLastMotion(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getPointerCount();
    for (byte b = 0; b < i; b++) {
      int j = paramMotionEvent.getPointerId(b);
      if (isValidPointerForActionMove(j)) {
        float f1 = paramMotionEvent.getX(b);
        float f2 = paramMotionEvent.getY(b);
        this.mLastMotionX[j] = f1;
        this.mLastMotionY[j] = f2;
      } 
    } 
  }
  
  public void abort() {
    cancel();
    if (this.mDragState == 2) {
      int i = this.mScroller.getCurrX();
      int j = this.mScroller.getCurrY();
      this.mScroller.abortAnimation();
      int k = this.mScroller.getCurrX();
      int m = this.mScroller.getCurrY();
      this.mCallback.onViewPositionChanged(this.mCapturedView, k, m, k - i, m - j);
    } 
    setDragState(0);
  }
  
  protected boolean canScroll(@NonNull View paramView, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    boolean bool = paramView instanceof ViewGroup;
    boolean bool1 = true;
    if (bool) {
      ViewGroup viewGroup = (ViewGroup)paramView;
      int i = paramView.getScrollX();
      int j = paramView.getScrollY();
      for (int k = viewGroup.getChildCount() - 1; k >= 0; k--) {
        View view = viewGroup.getChildAt(k);
        int m = paramInt3 + i;
        if (m >= view.getLeft() && m < view.getRight()) {
          int n = paramInt4 + j;
          if (n >= view.getTop() && n < view.getBottom() && canScroll(view, true, paramInt1, paramInt2, m - view.getLeft(), n - view.getTop()))
            return true; 
        } 
      } 
    } 
    if (paramBoolean) {
      paramBoolean = bool1;
      if (!paramView.canScrollHorizontally(-paramInt1)) {
        if (paramView.canScrollVertically(-paramInt2))
          return bool1; 
      } else {
        return paramBoolean;
      } 
    } 
    return false;
  }
  
  public void cancel() {
    this.mActivePointerId = -1;
    clearMotionHistory();
    if (this.mVelocityTracker != null) {
      this.mVelocityTracker.recycle();
      this.mVelocityTracker = null;
    } 
  }
  
  public void captureChildView(@NonNull View paramView, int paramInt) {
    if (paramView.getParent() == this.mParentView) {
      this.mCapturedView = paramView;
      this.mActivePointerId = paramInt;
      this.mCallback.onViewCaptured(paramView, paramInt);
      setDragState(1);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("captureChildView: parameter must be a descendant of the ViewDragHelper's tracked parent view (");
    stringBuilder.append(this.mParentView);
    stringBuilder.append(")");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public boolean checkTouchSlop(int paramInt) {
    int i = this.mInitialMotionX.length;
    for (byte b = 0; b < i; b++) {
      if (checkTouchSlop(paramInt, b))
        return true; 
    } 
    return false;
  }
  
  public boolean checkTouchSlop(int paramInt1, int paramInt2) {
    boolean bool4;
    boolean bool = isPointerDown(paramInt2);
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    if (!bool)
      return false; 
    if ((paramInt1 & 0x1) == 1) {
      bool4 = true;
    } else {
      bool4 = false;
    } 
    if ((paramInt1 & 0x2) == 2) {
      paramInt1 = 1;
    } else {
      paramInt1 = 0;
    } 
    float f1 = this.mLastMotionX[paramInt2] - this.mInitialMotionX[paramInt2];
    float f2 = this.mLastMotionY[paramInt2] - this.mInitialMotionY[paramInt2];
    if (bool4 && paramInt1 != 0) {
      if (f1 * f1 + f2 * f2 > (this.mTouchSlop * this.mTouchSlop))
        bool3 = true; 
      return bool3;
    } 
    if (bool4) {
      bool3 = bool1;
      if (Math.abs(f1) > this.mTouchSlop)
        bool3 = true; 
      return bool3;
    } 
    if (paramInt1 != 0) {
      bool3 = bool2;
      if (Math.abs(f2) > this.mTouchSlop)
        bool3 = true; 
      return bool3;
    } 
    return false;
  }
  
  public boolean continueSettling(boolean paramBoolean) {
    int i = this.mDragState;
    boolean bool = false;
    if (i == 2) {
      boolean bool1 = this.mScroller.computeScrollOffset();
      int j = this.mScroller.getCurrX();
      int k = this.mScroller.getCurrY();
      i = j - this.mCapturedView.getLeft();
      int m = k - this.mCapturedView.getTop();
      if (i != 0)
        ViewCompat.offsetLeftAndRight(this.mCapturedView, i); 
      if (m != 0)
        ViewCompat.offsetTopAndBottom(this.mCapturedView, m); 
      if (i != 0 || m != 0)
        this.mCallback.onViewPositionChanged(this.mCapturedView, j, k, i, m); 
      boolean bool2 = bool1;
      if (bool1) {
        bool2 = bool1;
        if (j == this.mScroller.getFinalX()) {
          bool2 = bool1;
          if (k == this.mScroller.getFinalY()) {
            this.mScroller.abortAnimation();
            bool2 = false;
          } 
        } 
      } 
      if (!bool2)
        if (paramBoolean) {
          this.mParentView.post(this.mSetIdleRunnable);
        } else {
          setDragState(0);
        }  
    } 
    paramBoolean = bool;
    if (this.mDragState == 2)
      paramBoolean = true; 
    return paramBoolean;
  }
  
  @Nullable
  public View findTopChildUnder(int paramInt1, int paramInt2) {
    for (int i = this.mParentView.getChildCount() - 1; i >= 0; i--) {
      View view = this.mParentView.getChildAt(this.mCallback.getOrderedChildIndex(i));
      if (paramInt1 >= view.getLeft() && paramInt1 < view.getRight() && paramInt2 >= view.getTop() && paramInt2 < view.getBottom())
        return view; 
    } 
    return null;
  }
  
  public void flingCapturedView(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (this.mReleaseInProgress) {
      this.mScroller.fling(this.mCapturedView.getLeft(), this.mCapturedView.getTop(), (int)this.mVelocityTracker.getXVelocity(this.mActivePointerId), (int)this.mVelocityTracker.getYVelocity(this.mActivePointerId), paramInt1, paramInt3, paramInt2, paramInt4);
      setDragState(2);
      return;
    } 
    throw new IllegalStateException("Cannot flingCapturedView outside of a call to Callback#onViewReleased");
  }
  
  public int getActivePointerId() {
    return this.mActivePointerId;
  }
  
  @Nullable
  public View getCapturedView() {
    return this.mCapturedView;
  }
  
  @Px
  public int getEdgeSize() {
    return this.mEdgeSize;
  }
  
  public float getMinVelocity() {
    return this.mMinVelocity;
  }
  
  @Px
  public int getTouchSlop() {
    return this.mTouchSlop;
  }
  
  public int getViewDragState() {
    return this.mDragState;
  }
  
  public boolean isCapturedViewUnder(int paramInt1, int paramInt2) {
    return isViewUnder(this.mCapturedView, paramInt1, paramInt2);
  }
  
  public boolean isEdgeTouched(int paramInt) {
    int i = this.mInitialEdgesTouched.length;
    for (byte b = 0; b < i; b++) {
      if (isEdgeTouched(paramInt, b))
        return true; 
    } 
    return false;
  }
  
  public boolean isEdgeTouched(int paramInt1, int paramInt2) {
    boolean bool;
    if (isPointerDown(paramInt2) && (paramInt1 & this.mInitialEdgesTouched[paramInt2]) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isPointerDown(int paramInt) {
    int i = this.mPointersDown;
    boolean bool = true;
    if ((1 << paramInt & i) == 0)
      bool = false; 
    return bool;
  }
  
  public boolean isViewUnder(@Nullable View paramView, int paramInt1, int paramInt2) {
    boolean bool1 = false;
    if (paramView == null)
      return false; 
    boolean bool2 = bool1;
    if (paramInt1 >= paramView.getLeft()) {
      bool2 = bool1;
      if (paramInt1 < paramView.getRight()) {
        bool2 = bool1;
        if (paramInt2 >= paramView.getTop()) {
          bool2 = bool1;
          if (paramInt2 < paramView.getBottom())
            bool2 = true; 
        } 
      } 
    } 
    return bool2;
  }
  
  public void processTouchEvent(@NonNull MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getActionMasked();
    int j = paramMotionEvent.getActionIndex();
    if (i == 0)
      cancel(); 
    if (this.mVelocityTracker == null)
      this.mVelocityTracker = VelocityTracker.obtain(); 
    this.mVelocityTracker.addMovement(paramMotionEvent);
    int k = 0;
    int m = 0;
    switch (i) {
      default:
        return;
      case 6:
        k = paramMotionEvent.getPointerId(j);
        if (this.mDragState == 1 && k == this.mActivePointerId) {
          j = paramMotionEvent.getPointerCount();
          while (true) {
            if (m < j) {
              i = paramMotionEvent.getPointerId(m);
              if (i != this.mActivePointerId) {
                float f3 = paramMotionEvent.getX(m);
                float f4 = paramMotionEvent.getY(m);
                if (findTopChildUnder((int)f3, (int)f4) == this.mCapturedView && tryCaptureViewForDrag(this.mCapturedView, i)) {
                  m = this.mActivePointerId;
                  break;
                } 
              } 
              m++;
              continue;
            } 
            m = -1;
            break;
          } 
          if (m == -1)
            releaseViewForPointerUp(); 
        } 
        clearMotionHistory(k);
      case 5:
        m = paramMotionEvent.getPointerId(j);
        f1 = paramMotionEvent.getX(j);
        f2 = paramMotionEvent.getY(j);
        saveInitialMotion(f1, f2, m);
        if (this.mDragState == 0) {
          tryCaptureViewForDrag(findTopChildUnder((int)f1, (int)f2), m);
          k = this.mInitialEdgesTouched[m];
          if ((this.mTrackingEdges & k) != 0)
            this.mCallback.onEdgeTouched(k & this.mTrackingEdges, m); 
        } else if (isCapturedViewUnder((int)f1, (int)f2)) {
          tryCaptureViewForDrag(this.mCapturedView, m);
        } 
      case 3:
        if (this.mDragState == 1)
          dispatchViewReleased(0.0F, 0.0F); 
        cancel();
      case 2:
        if (this.mDragState == 1) {
          if (isValidPointerForActionMove(this.mActivePointerId)) {
            m = paramMotionEvent.findPointerIndex(this.mActivePointerId);
            f1 = paramMotionEvent.getX(m);
            f2 = paramMotionEvent.getY(m);
            m = (int)(f1 - this.mLastMotionX[this.mActivePointerId]);
            k = (int)(f2 - this.mLastMotionY[this.mActivePointerId]);
            dragTo(this.mCapturedView.getLeft() + m, this.mCapturedView.getTop() + k, m, k);
            saveLastMotion(paramMotionEvent);
          } 
        } else {
          j = paramMotionEvent.getPointerCount();
          for (m = k; m < j; m++) {
            k = paramMotionEvent.getPointerId(m);
            if (isValidPointerForActionMove(k)) {
              float f3 = paramMotionEvent.getX(m);
              f1 = paramMotionEvent.getY(m);
              f2 = f3 - this.mInitialMotionX[k];
              float f4 = f1 - this.mInitialMotionY[k];
              reportNewEdgeDrags(f2, f4, k);
              if (this.mDragState == 1)
                break; 
              View view1 = findTopChildUnder((int)f3, (int)f1);
              if (checkTouchSlop(view1, f2, f4) && tryCaptureViewForDrag(view1, k))
                break; 
            } 
          } 
          saveLastMotion(paramMotionEvent);
        } 
      case 1:
        if (this.mDragState == 1)
          releaseViewForPointerUp(); 
        cancel();
      case 0:
        break;
    } 
    float f2 = paramMotionEvent.getX();
    float f1 = paramMotionEvent.getY();
    k = paramMotionEvent.getPointerId(0);
    View view = findTopChildUnder((int)f2, (int)f1);
    saveInitialMotion(f2, f1, k);
    tryCaptureViewForDrag(view, k);
    m = this.mInitialEdgesTouched[k];
    if ((this.mTrackingEdges & m) != 0)
      this.mCallback.onEdgeTouched(m & this.mTrackingEdges, k); 
  }
  
  void setDragState(int paramInt) {
    this.mParentView.removeCallbacks(this.mSetIdleRunnable);
    if (this.mDragState != paramInt) {
      this.mDragState = paramInt;
      this.mCallback.onViewDragStateChanged(paramInt);
      if (this.mDragState == 0)
        this.mCapturedView = null; 
    } 
  }
  
  public void setEdgeTrackingEnabled(int paramInt) {
    this.mTrackingEdges = paramInt;
  }
  
  public void setMinVelocity(float paramFloat) {
    this.mMinVelocity = paramFloat;
  }
  
  public boolean settleCapturedViewAt(int paramInt1, int paramInt2) {
    if (this.mReleaseInProgress)
      return forceSettleCapturedViewAt(paramInt1, paramInt2, (int)this.mVelocityTracker.getXVelocity(this.mActivePointerId), (int)this.mVelocityTracker.getYVelocity(this.mActivePointerId)); 
    throw new IllegalStateException("Cannot settleCapturedViewAt outside of a call to Callback#onViewReleased");
  }
  
  public boolean shouldInterceptTouchEvent(@NonNull MotionEvent paramMotionEvent) {
    View view;
    float f1;
    float f2;
    int k;
    int i = paramMotionEvent.getActionMasked();
    int j = paramMotionEvent.getActionIndex();
    if (i == 0)
      cancel(); 
    if (this.mVelocityTracker == null)
      this.mVelocityTracker = VelocityTracker.obtain(); 
    this.mVelocityTracker.addMovement(paramMotionEvent);
    switch (i) {
      case 6:
        clearMotionHistory(paramMotionEvent.getPointerId(j));
        break;
      case 5:
        i = paramMotionEvent.getPointerId(j);
        f1 = paramMotionEvent.getX(j);
        f2 = paramMotionEvent.getY(j);
        saveInitialMotion(f1, f2, i);
        if (this.mDragState == 0) {
          j = this.mInitialEdgesTouched[i];
          if ((this.mTrackingEdges & j) != 0)
            this.mCallback.onEdgeTouched(j & this.mTrackingEdges, i); 
          break;
        } 
        if (this.mDragState == 2) {
          view = findTopChildUnder((int)f1, (int)f2);
          if (view == this.mCapturedView)
            tryCaptureViewForDrag(view, i); 
        } 
        break;
      case 2:
        if (this.mInitialMotionX == null || this.mInitialMotionY == null)
          break; 
        k = view.getPointerCount();
        for (j = 0; j < k; j++) {
          int m = view.getPointerId(j);
          if (isValidPointerForActionMove(m)) {
            float f3 = view.getX(j);
            float f4 = view.getY(j);
            f1 = f3 - this.mInitialMotionX[m];
            f2 = f4 - this.mInitialMotionY[m];
            View view1 = findTopChildUnder((int)f3, (int)f4);
            if (view1 != null && checkTouchSlop(view1, f1, f2)) {
              i = 1;
            } else {
              i = 0;
            } 
            if (i != 0) {
              int n = view1.getLeft();
              int i1 = (int)f1;
              int i2 = this.mCallback.clampViewPositionHorizontal(view1, n + i1, i1);
              i1 = view1.getTop();
              int i3 = (int)f2;
              int i4 = this.mCallback.clampViewPositionVertical(view1, i1 + i3, i3);
              int i5 = this.mCallback.getViewHorizontalDragRange(view1);
              i3 = this.mCallback.getViewVerticalDragRange(view1);
              if ((i5 == 0 || (i5 > 0 && i2 == n)) && (i3 == 0 || (i3 > 0 && i4 == i1)))
                break; 
            } 
            reportNewEdgeDrags(f1, f2, m);
            if (this.mDragState == 1 || (i != 0 && tryCaptureViewForDrag(view1, m)))
              break; 
          } 
        } 
        saveLastMotion((MotionEvent)view);
        break;
      case 1:
      case 3:
        cancel();
        break;
      case 0:
        f2 = view.getX();
        f1 = view.getY();
        j = view.getPointerId(0);
        saveInitialMotion(f2, f1, j);
        view = findTopChildUnder((int)f2, (int)f1);
        if (view == this.mCapturedView && this.mDragState == 2)
          tryCaptureViewForDrag(view, j); 
        i = this.mInitialEdgesTouched[j];
        if ((this.mTrackingEdges & i) != 0)
          this.mCallback.onEdgeTouched(i & this.mTrackingEdges, j); 
        break;
    } 
    boolean bool = false;
    if (this.mDragState == 1)
      bool = true; 
    return bool;
  }
  
  public boolean smoothSlideViewTo(@NonNull View paramView, int paramInt1, int paramInt2) {
    this.mCapturedView = paramView;
    this.mActivePointerId = -1;
    boolean bool = forceSettleCapturedViewAt(paramInt1, paramInt2, 0, 0);
    if (!bool && this.mDragState == 0 && this.mCapturedView != null)
      this.mCapturedView = null; 
    return bool;
  }
  
  boolean tryCaptureViewForDrag(View paramView, int paramInt) {
    if (paramView == this.mCapturedView && this.mActivePointerId == paramInt)
      return true; 
    if (paramView != null && this.mCallback.tryCaptureView(paramView, paramInt)) {
      this.mActivePointerId = paramInt;
      captureChildView(paramView, paramInt);
      return true;
    } 
    return false;
  }
  
  public static abstract class Callback {
    public int clampViewPositionHorizontal(@NonNull View param1View, int param1Int1, int param1Int2) {
      return 0;
    }
    
    public int clampViewPositionVertical(@NonNull View param1View, int param1Int1, int param1Int2) {
      return 0;
    }
    
    public int getOrderedChildIndex(int param1Int) {
      return param1Int;
    }
    
    public int getViewHorizontalDragRange(@NonNull View param1View) {
      return 0;
    }
    
    public int getViewVerticalDragRange(@NonNull View param1View) {
      return 0;
    }
    
    public void onEdgeDragStarted(int param1Int1, int param1Int2) {}
    
    public boolean onEdgeLock(int param1Int) {
      return false;
    }
    
    public void onEdgeTouched(int param1Int1, int param1Int2) {}
    
    public void onViewCaptured(@NonNull View param1View, int param1Int) {}
    
    public void onViewDragStateChanged(int param1Int) {}
    
    public void onViewPositionChanged(@NonNull View param1View, int param1Int1, int param1Int2, @Px int param1Int3, @Px int param1Int4) {}
    
    public void onViewReleased(@NonNull View param1View, float param1Float1, float param1Float2) {}
    
    public abstract boolean tryCaptureView(@NonNull View param1View, int param1Int);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\widget\ViewDragHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */