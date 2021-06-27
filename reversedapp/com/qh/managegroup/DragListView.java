package com.qh.managegroup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ListView;

public class DragListView extends ListView {
  private static final int ANIMATION_DURATION = 200;
  
  public static final int MSG_DRAG_MOVE = 4098;
  
  public static final int MSG_DRAG_STOP = 4097;
  
  private static final int step = 1;
  
  private boolean bHasGetSapcing = false;
  
  public View cache;
  
  private int current_Step;
  
  private int downScrollBounce;
  
  private ImageView dragImageView;
  
  private ViewGroup dragItemView = null;
  
  private int dragOffset;
  
  private int dragPoint;
  
  private int dragPosition;
  
  private int holdPosition;
  
  private boolean isDragItemMoving = false;
  
  private boolean isLock;
  
  private boolean isMoving = false;
  
  private boolean isNormal = true;
  
  private boolean isSameDragDirection = true;
  
  private boolean isScroll = false;
  
  private int lastFlag = -1;
  
  private int lastPosition;
  
  private int mCurFirstVisiblePosition;
  
  private int mCurLastVisiblePosition;
  
  private ItemInfo mDragItemInfo;
  
  public DragListChange mDragListChange;
  
  private int mFirstVisiblePosition;
  
  Handler mHandler = new Handler() {
      public void handleMessage(Message param1Message) {
        switch (param1Message.what) {
          default:
            return;
          case 4098:
            DragListView.this.onDrag(param1Message.arg1);
          case 4097:
            break;
        } 
        DragListView.this.stopDrag();
        DragListView.this.onDrop(param1Message.arg1);
      }
    };
  
  private int mItemVerticalSpacing = 0;
  
  private int mLastVisiblePosition;
  
  private int scaledTouchSlop;
  
  private int startPosition;
  
  private int turnDownPosition;
  
  private int turnUpPosition;
  
  private int upScrollBounce;
  
  private WindowManager windowManager;
  
  private WindowManager.LayoutParams windowParams;
  
  @SuppressLint({"NewApi"})
  public DragListView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    setLayerType(2, null);
    this.scaledTouchSlop = ViewConfiguration.get(paramContext).getScaledTouchSlop();
    this.mDragItemInfo = new ItemInfo();
    init();
  }
  
  private void getSpacing() {
    this.bHasGetSapcing = true;
    this.upScrollBounce = getHeight() / 3;
    this.downScrollBounce = getHeight() * 2 / 3;
    int[] arrayOfInt1 = new int[2];
    int[] arrayOfInt2 = new int[2];
    ViewGroup viewGroup1 = (ViewGroup)getChildAt(0);
    ViewGroup viewGroup2 = (ViewGroup)getChildAt(1);
    if (viewGroup1 != null) {
      viewGroup1.getLocationOnScreen(arrayOfInt1);
      if (viewGroup2 != null) {
        viewGroup2.getLocationOnScreen(arrayOfInt2);
        this.mItemVerticalSpacing = Math.abs(arrayOfInt2[1] - arrayOfInt1[1]);
        return;
      } 
      return;
    } 
  }
  
  private void hideDropItem() {
    ((DragListAdapter)getAdapter()).showDropItem(false);
  }
  
  private void init() {
    this.windowManager = (WindowManager)getContext().getSystemService("window");
  }
  
  private void onChangeCopy(int paramInt1, int paramInt2) {
    DragListAdapter dragListAdapter = (DragListAdapter)getAdapter();
    if (paramInt1 != paramInt2) {
      dragListAdapter.exchangeCopy(paramInt1, paramInt2);
      Log.i("wanggang", "onChange");
    } 
  }
  
  private void onDrop(int paramInt1, int paramInt2) {
    DragListAdapter dragListAdapter = (DragListAdapter)getAdapter();
    dragListAdapter.setInvisiblePosition(-1);
    dragListAdapter.showDropItem(true);
    dragListAdapter.notifyDataSetChanged();
  }
  
  private void startDrag(Bitmap paramBitmap, int paramInt) {
    this.windowParams = new WindowManager.LayoutParams();
    this.windowParams.gravity = 48;
    this.windowParams.x = 0;
    this.windowParams.y = paramInt - this.dragPoint + this.dragOffset;
    this.windowParams.width = -2;
    this.windowParams.height = -2;
    this.windowParams.flags = 408;
    this.windowParams.windowAnimations = 0;
    this.windowParams.alpha = 0.8F;
    this.windowParams.format = -3;
    ImageView imageView = new ImageView(getContext());
    imageView.setImageBitmap(paramBitmap);
    this.windowManager.addView((View)imageView, (ViewGroup.LayoutParams)this.windowParams);
    this.dragImageView = imageView;
  }
  
  private void testAnimation(int paramInt) {
    DragListAdapter dragListAdapter = (DragListAdapter)getAdapter();
    int i = pointToPosition(0, paramInt);
    if (i == -1 || i == this.lastPosition)
      return; 
    this.mFirstVisiblePosition = getFirstVisiblePosition();
    this.dragPosition = i;
    onChangeCopy(this.lastPosition, this.dragPosition);
    int j = i - this.lastPosition;
    int k = Math.abs(j);
    for (paramInt = 1; paramInt <= k; paramInt++) {
      int m;
      Animation animation;
      if (j > 0) {
        if (this.lastFlag == -1) {
          this.lastFlag = 0;
          this.isSameDragDirection = true;
        } 
        if (this.lastFlag == 1) {
          this.turnUpPosition = i;
          this.lastFlag = 0;
          this.isSameDragDirection ^= 0x1;
        } 
        if (this.isSameDragDirection) {
          this.holdPosition = this.lastPosition + 1;
        } else if (this.startPosition < i) {
          this.holdPosition = this.lastPosition + 1;
          this.isSameDragDirection ^= 0x1;
        } else {
          this.holdPosition = this.lastPosition;
        } 
        m = -this.mItemVerticalSpacing;
        this.lastPosition++;
      } else {
        if (this.lastFlag == -1) {
          this.lastFlag = 1;
          this.isSameDragDirection = true;
        } 
        if (this.lastFlag == 0) {
          this.turnDownPosition = i;
          this.lastFlag = 1;
          this.isSameDragDirection ^= 0x1;
        } 
        if (this.isSameDragDirection) {
          this.holdPosition = this.lastPosition - 1;
        } else if (this.startPosition > i) {
          this.holdPosition = this.lastPosition - 1;
          this.isSameDragDirection ^= 0x1;
        } else {
          this.holdPosition = this.lastPosition;
        } 
        m = this.mItemVerticalSpacing;
        this.lastPosition--;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("getFirstVisiblePosition() = ");
      stringBuilder.append(getFirstVisiblePosition());
      Log.i("wanggang", stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("getLastVisiblePosition() = ");
      stringBuilder.append(getLastVisiblePosition());
      Log.i("wanggang", stringBuilder.toString());
      dragListAdapter.setHeight(this.mItemVerticalSpacing);
      dragListAdapter.setIsSameDragDirection(this.isSameDragDirection);
      dragListAdapter.setLastFlag(this.lastFlag);
      ViewGroup viewGroup = (ViewGroup)getChildAt(this.holdPosition - getFirstVisiblePosition());
      if (this.isSameDragDirection) {
        animation = getFromSelfAnimation(0, m);
      } else {
        animation = getToSelfAnimation(0, -m);
      } 
      viewGroup.startAnimation(animation);
    } 
  }
  
  public void doScroller(int paramInt) {
    if (paramInt < this.upScrollBounce) {
      this.current_Step = (this.upScrollBounce - paramInt) / 10 + 1;
    } else if (paramInt > this.downScrollBounce) {
      this.current_Step = -(paramInt - this.downScrollBounce + 1) / 10;
    } else {
      this.isScroll = false;
      this.current_Step = 0;
    } 
    View view = getChildAt(this.dragPosition - getFirstVisiblePosition());
    setSelectionFromTop(this.dragPosition, view.getTop() + this.current_Step);
  }
  
  public Animation getAbsMoveAnimation(int paramInt1, int paramInt2) {
    TranslateAnimation translateAnimation = new TranslateAnimation(1, 0.0F, 0, paramInt1, 1, 0.0F, 0, paramInt2);
    translateAnimation.setInterpolator((Interpolator)new AccelerateDecelerateInterpolator());
    translateAnimation.setFillAfter(true);
    translateAnimation.setDuration(200L);
    translateAnimation.setInterpolator((Interpolator)new AccelerateInterpolator());
    return (Animation)translateAnimation;
  }
  
  public Animation getAbsMoveAnimation2(int paramInt1, int paramInt2) {
    TranslateAnimation translateAnimation = new TranslateAnimation(0, paramInt1, 1, 0.0F, 0, paramInt2, 1, 0.0F);
    translateAnimation.setInterpolator((Interpolator)new AccelerateDecelerateInterpolator());
    translateAnimation.setFillAfter(true);
    translateAnimation.setDuration(200L);
    translateAnimation.setInterpolator((Interpolator)new AccelerateInterpolator());
    return (Animation)translateAnimation;
  }
  
  public Animation getAnimation(int paramInt1, int paramInt2) {
    TranslateAnimation translateAnimation = new TranslateAnimation(1, 0.0F, 0, 0.0F, 0, paramInt1, 0, paramInt2);
    translateAnimation.setInterpolator((Interpolator)new AccelerateDecelerateInterpolator());
    translateAnimation.setFillAfter(true);
    translateAnimation.setDuration(200L);
    translateAnimation.setInterpolator((Interpolator)new AccelerateInterpolator());
    return (Animation)translateAnimation;
  }
  
  public Animation getFromSelfAnimation(int paramInt1, int paramInt2) {
    TranslateAnimation translateAnimation = new TranslateAnimation(1, 0.0F, 0, paramInt1, 1, 0.0F, 0, paramInt2);
    translateAnimation.setInterpolator((Interpolator)new AccelerateDecelerateInterpolator());
    translateAnimation.setFillAfter(true);
    translateAnimation.setDuration(200L);
    translateAnimation.setInterpolator((Interpolator)new AccelerateInterpolator());
    return (Animation)translateAnimation;
  }
  
  public Animation getScaleAnimation() {
    ScaleAnimation scaleAnimation = new ScaleAnimation(0.0F, 0.0F, 0.0F, 0.0F, 1, 0.5F, 1, 0.5F);
    scaleAnimation.setFillAfter(true);
    return (Animation)scaleAnimation;
  }
  
  public Animation getToSelfAnimation(int paramInt1, int paramInt2) {
    TranslateAnimation translateAnimation = new TranslateAnimation(0, paramInt1, 1, 0.0F, 0, paramInt2, 1, 0.0F);
    translateAnimation.setInterpolator((Interpolator)new AccelerateDecelerateInterpolator());
    translateAnimation.setFillAfter(true);
    translateAnimation.setDuration(200L);
    translateAnimation.setInterpolator((Interpolator)new AccelerateInterpolator());
    return (Animation)translateAnimation;
  }
  
  public void onDrag(int paramInt) {
    int i = this.dragPoint;
    if (this.dragImageView != null && paramInt - i >= 0) {
      this.windowParams.alpha = 1.0F;
      this.windowParams.y = paramInt - this.dragPoint + this.dragOffset;
      this.windowManager.updateViewLayout((View)this.dragImageView, (ViewGroup.LayoutParams)this.windowParams);
    } 
    doScroller(paramInt);
  }
  
  public void onDrop(int paramInt) {
    onDrop(0, paramInt);
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    Bitmap bitmap;
    if (paramMotionEvent.getAction() == 0 && !this.isLock && !this.isMoving && !this.isDragItemMoving) {
      int i = (int)paramMotionEvent.getX();
      int j = (int)paramMotionEvent.getY();
      int k = pointToPosition(i, j);
      this.dragPosition = k;
      this.startPosition = k;
      this.lastPosition = k;
      if (this.dragPosition == -1)
        return super.onInterceptTouchEvent(paramMotionEvent); 
      if (!this.bHasGetSapcing)
        getSpacing(); 
      ViewGroup viewGroup = (ViewGroup)getChildAt(this.dragPosition - getFirstVisiblePosition());
      DragListAdapter dragListAdapter = (DragListAdapter)getAdapter();
      this.mDragItemInfo.obj = dragListAdapter.getItem(this.dragPosition - getFirstVisiblePosition());
      this.dragPoint = j - viewGroup.getTop();
      this.dragOffset = (int)(paramMotionEvent.getRawY() - j);
      View view = viewGroup.findViewById(2131230951);
      if (view != null && i > view.getLeft() - 20) {
        this.cache = view;
        this.dragItemView = viewGroup;
        viewGroup.destroyDrawingCache();
        viewGroup.setDrawingCacheEnabled(true);
        viewGroup.setBackgroundColor(1431655765);
        bitmap = Bitmap.createBitmap(viewGroup.getDrawingCache(true));
        hideDropItem();
        dragListAdapter.setInvisiblePosition(this.startPosition);
        dragListAdapter.notifyDataSetChanged();
        startDrag(bitmap, j);
        this.isMoving = false;
        dragListAdapter.copyList();
      } 
      return false;
    } 
    return super.onInterceptTouchEvent((MotionEvent)bitmap);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    MyListData myListData;
    if (this.dragImageView != null && this.dragPosition != -1 && !this.isLock) {
      int i;
      StringBuilder stringBuilder;
      MyListData myListData1;
      DragListAdapter dragListAdapter;
      int j;
      switch (paramMotionEvent.getAction()) {
        case 2:
          i = (int)paramMotionEvent.getY();
          onDrag(i);
          testAnimation(i);
          break;
        case 1:
          stringBuilder = new StringBuilder();
          stringBuilder.append("dragPosition = ");
          stringBuilder.append(this.dragPosition);
          Log.e("", stringBuilder.toString());
          dragListAdapter = (DragListAdapter)getAdapter();
          myListData1 = (MyListData)this.cache.getTag();
          j = this.dragPosition - 1;
          i = j;
          if (j < 0)
            i = 0; 
          j = i;
          if (i > dragListAdapter.arrayTitles.size())
            j = dragListAdapter.arrayTitles.size() - 1; 
          i = (int)paramMotionEvent.getY();
          stopDrag();
          onDrop(i);
          myListData = dragListAdapter.arrayTitles.get(j);
          if (this.mDragListChange != null)
            this.mDragListChange.change(myListData1, myListData); 
          break;
      } 
      return true;
    } 
    return super.onTouchEvent((MotionEvent)myListData);
  }
  
  public void setDragListChange(DragListChange paramDragListChange) {
    DragListAdapter dragListAdapter = (DragListAdapter)getAdapter();
    if (dragListAdapter != null)
      dragListAdapter.setDragListChange(paramDragListChange); 
    this.mDragListChange = paramDragListChange;
  }
  
  public void setLock(boolean paramBoolean) {
    this.isLock = paramBoolean;
  }
  
  public void stopDrag() {
    this.isMoving = false;
    if (this.dragImageView != null) {
      this.windowManager.removeView((View)this.dragImageView);
      this.dragImageView = null;
    } 
    this.isSameDragDirection = true;
    this.lastFlag = -1;
    DragListAdapter dragListAdapter = (DragListAdapter)getAdapter();
    dragListAdapter.setLastFlag(this.lastFlag);
    dragListAdapter.pastList();
  }
  
  public static interface DragListChange {
    void change(MyListData param1MyListData1, MyListData param1MyListData2);
    
    void delect(MyListData param1MyListData);
    
    void resetname(MyListData param1MyListData);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\managegroup\DragListView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */