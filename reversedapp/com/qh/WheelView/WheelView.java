package com.qh.WheelView;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class WheelView extends View {
  private static final int DEF_VISIBLE_ITEMS = 5;
  
  private static final int ITEM_OFFSET_PERCENT = 10;
  
  private static final int PADDING = 1;
  
  private static final int[] SHADOWS_COLORS = new int[] { 0, 0, 0 };
  
  private GradientDrawable bottomShadow;
  
  private Drawable centerDrawable;
  
  private List<OnWheelChangedListener> changingListeners = new LinkedList<OnWheelChangedListener>();
  
  private List<OnWheelClickedListener> clickingListeners = new LinkedList<OnWheelClickedListener>();
  
  private int currentItem = 0;
  
  private DataSetObserver dataObserver = new DataSetObserver() {
      public void onChanged() {
        WheelView.this.invalidateWheel(false);
      }
      
      public void onInvalidated() {
        WheelView.this.invalidateWheel(true);
      }
    };
  
  private int firstItem;
  
  boolean isCyclic = false;
  
  private boolean isScrollingPerformed;
  
  private int itemHeight = 0;
  
  private LinearLayout itemsLayout;
  
  private WheelRecycle recycle = new WheelRecycle(this);
  
  private WheelScroller scroller;
  
  WheelScroller.ScrollingListener scrollingListener = new WheelScroller.ScrollingListener() {
      public void onFinished() {
        if (WheelView.this.isScrollingPerformed) {
          WheelView.this.notifyScrollingListenersAboutEnd();
          WheelView.access$002(WheelView.this, false);
        } 
        WheelView.access$202(WheelView.this, 0);
        WheelView.this.invalidate();
      }
      
      public void onJustify() {
        if (Math.abs(WheelView.this.scrollingOffset) > 1)
          WheelView.this.scroller.scroll(WheelView.this.scrollingOffset, 0); 
      }
      
      public void onScroll(int param1Int) {
        WheelView.this.doScroll(param1Int);
        int i = WheelView.this.getHeight();
        if (WheelView.this.scrollingOffset > i) {
          WheelView.access$202(WheelView.this, i);
          WheelView.this.scroller.stopScrolling();
        } else {
          param1Int = WheelView.this.scrollingOffset;
          i = -i;
          if (param1Int < i) {
            WheelView.access$202(WheelView.this, i);
            WheelView.this.scroller.stopScrolling();
          } 
        } 
      }
      
      public void onStarted() {
        WheelView.access$002(WheelView.this, true);
        WheelView.this.notifyScrollingListenersAboutStart();
      }
    };
  
  private List<OnWheelScrollListener> scrollingListeners = new LinkedList<OnWheelScrollListener>();
  
  private int scrollingOffset;
  
  private GradientDrawable topShadow;
  
  private WheelViewAdapter viewAdapter;
  
  private int visibleItems = 5;
  
  public WheelView(Context paramContext) {
    super(paramContext);
    initData(paramContext);
  }
  
  public WheelView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    initData(paramContext);
  }
  
  public WheelView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    initData(paramContext);
  }
  
  private boolean addViewItem(int paramInt, boolean paramBoolean) {
    View view = getItemView(paramInt);
    if (view != null) {
      if (paramBoolean) {
        this.itemsLayout.addView(view, 0);
      } else {
        this.itemsLayout.addView(view);
      } 
      return true;
    } 
    return false;
  }
  
  private void buildViewForMeasuring() {
    if (this.itemsLayout != null) {
      this.recycle.recycleItems(this.itemsLayout, this.firstItem, new ItemsRange());
    } else {
      createItemsLayout();
    } 
    int i = this.visibleItems / 2;
    for (int j = this.currentItem + i; j >= this.currentItem - i; j--) {
      if (addViewItem(j, true))
        this.firstItem = j; 
    } 
  }
  
  private int calculateLayoutWidth(int paramInt1, int paramInt2) {
    initResourcesIfNecessary();
    this.itemsLayout.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
    this.itemsLayout.measure(View.MeasureSpec.makeMeasureSpec(paramInt1, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
    int i = this.itemsLayout.getMeasuredWidth();
    if (paramInt2 != 1073741824) {
      i = Math.max(i + 2, getSuggestedMinimumWidth());
      if (paramInt2 != Integer.MIN_VALUE || paramInt1 >= i)
        paramInt1 = i; 
    } 
    this.itemsLayout.measure(View.MeasureSpec.makeMeasureSpec(paramInt1 - 2, 1073741824), View.MeasureSpec.makeMeasureSpec(0, 0));
    return paramInt1;
  }
  
  private void createItemsLayout() {
    if (this.itemsLayout == null) {
      this.itemsLayout = new LinearLayout(getContext());
      this.itemsLayout.setOrientation(1);
    } 
  }
  
  private void doScroll(int paramInt) {
    int i1;
    this.scrollingOffset += paramInt;
    int i = getItemHeight();
    int j = this.scrollingOffset / i;
    int k = this.currentItem - j;
    int m = this.viewAdapter.getItemsCount();
    paramInt = this.scrollingOffset % i;
    int n = paramInt;
    if (Math.abs(paramInt) <= i / 2)
      n = 0; 
    if (this.isCyclic && m > 0) {
      if (n > 0) {
        i1 = k - 1;
        paramInt = j + 1;
      } else {
        paramInt = j;
        i1 = k;
        if (n < 0) {
          i1 = k + 1;
          paramInt = j - 1;
        } 
      } 
      while (i1 < 0)
        i1 += m; 
      i1 %= m;
    } else if (k < 0) {
      paramInt = this.currentItem;
      i1 = 0;
    } else if (k >= m) {
      paramInt = this.currentItem - m + 1;
      i1 = m - 1;
    } else if (k > 0 && n > 0) {
      i1 = k - 1;
      paramInt = j + 1;
    } else {
      paramInt = j;
      i1 = k;
      if (k < m - 1) {
        paramInt = j;
        i1 = k;
        if (n < 0) {
          i1 = k + 1;
          paramInt = j - 1;
        } 
      } 
    } 
    j = this.scrollingOffset;
    if (i1 != this.currentItem) {
      setCurrentItem(i1, false);
    } else {
      invalidate();
    } 
    this.scrollingOffset = j - paramInt * i;
    if (this.scrollingOffset > getHeight())
      this.scrollingOffset = this.scrollingOffset % getHeight() + getHeight(); 
  }
  
  private void drawCenterRect(Canvas paramCanvas) {
    int i = getHeight() / 2;
    double d = (getItemHeight() / 2);
    Double.isNaN(d);
    int j = (int)(d * 1.2D);
    this.centerDrawable.setBounds(0, i - j, getWidth(), i + j);
    this.centerDrawable.draw(paramCanvas);
  }
  
  private void drawItems(Canvas paramCanvas) {
    paramCanvas.save();
    paramCanvas.translate(1.0F, (-((this.currentItem - this.firstItem) * getItemHeight() + (getItemHeight() - getHeight()) / 2) + this.scrollingOffset));
    this.itemsLayout.draw(paramCanvas);
    paramCanvas.restore();
  }
  
  private void drawShadows(Canvas paramCanvas) {
    double d = getItemHeight();
    Double.isNaN(d);
    int i = (int)(d * 1.5D);
    this.topShadow.setBounds(0, 0, getWidth(), i);
    this.topShadow.draw(paramCanvas);
    this.bottomShadow.setBounds(0, getHeight() - i, getWidth(), getHeight());
    this.bottomShadow.draw(paramCanvas);
  }
  
  private int getDesiredHeight(LinearLayout paramLinearLayout) {
    if (paramLinearLayout != null && paramLinearLayout.getChildAt(0) != null)
      this.itemHeight = paramLinearLayout.getChildAt(0).getMeasuredHeight(); 
    return Math.max(this.itemHeight * this.visibleItems - this.itemHeight * 10 / 50, getSuggestedMinimumHeight());
  }
  
  private int getItemHeight() {
    if (this.itemHeight != 0)
      return this.itemHeight; 
    if (this.itemsLayout != null && this.itemsLayout.getChildAt(0) != null) {
      this.itemHeight = this.itemsLayout.getChildAt(0).getHeight();
      return this.itemHeight;
    } 
    return getHeight() / this.visibleItems;
  }
  
  private View getItemView(int paramInt) {
    if (this.viewAdapter == null || this.viewAdapter.getItemsCount() == 0)
      return null; 
    int i = this.viewAdapter.getItemsCount();
    int j = paramInt;
    if (!isValidItemIndex(paramInt))
      return this.viewAdapter.getEmptyItem(this.recycle.getEmptyItem(), (ViewGroup)this.itemsLayout); 
    while (j < 0)
      j += i; 
    return this.viewAdapter.getItem(j % i, this.recycle.getItem(), (ViewGroup)this.itemsLayout);
  }
  
  private ItemsRange getItemsRange() {
    if (getItemHeight() == 0)
      return null; 
    int i = this.currentItem;
    byte b;
    for (b = 1; getItemHeight() * b < getHeight(); b += 2)
      i--; 
    int j = i;
    int k = b;
    if (this.scrollingOffset != 0) {
      k = i;
      if (this.scrollingOffset > 0)
        k = i - 1; 
      i = this.scrollingOffset / getItemHeight();
      j = k - i;
      double d1 = (b + 1);
      double d2 = Math.asin(i);
      Double.isNaN(d1);
      k = (int)(d1 + d2);
    } 
    return new ItemsRange(j, k);
  }
  
  private void initData(Context paramContext) {
    this.scroller = new WheelScroller(getContext(), this.scrollingListener);
  }
  
  private void initResourcesIfNecessary() {
    if (this.centerDrawable == null)
      this.centerDrawable = getContext().getResources().getDrawable(2131165469); 
    if (this.topShadow == null)
      this.topShadow = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, SHADOWS_COLORS); 
    if (this.bottomShadow == null)
      this.bottomShadow = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, SHADOWS_COLORS); 
    setBackgroundResource(2131165468);
  }
  
  private boolean isValidItemIndex(int paramInt) {
    boolean bool;
    if (this.viewAdapter != null && this.viewAdapter.getItemsCount() > 0 && (this.isCyclic || (paramInt >= 0 && paramInt < this.viewAdapter.getItemsCount()))) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void layout(int paramInt1, int paramInt2) {
    this.itemsLayout.layout(0, 0, paramInt1 - 2, paramInt2);
  }
  
  private boolean rebuildItems() {
    boolean bool1;
    ItemsRange itemsRange = getItemsRange();
    if (this.itemsLayout != null) {
      int i = this.recycle.recycleItems(this.itemsLayout, this.firstItem, itemsRange);
      if (this.firstItem != i) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      this.firstItem = i;
    } else {
      createItemsLayout();
      bool1 = true;
    } 
    boolean bool2 = bool1;
    if (this.itemsLayout != null) {
      bool2 = bool1;
      if (itemsRange != null) {
        bool2 = bool1;
        if (!bool1) {
          if (this.firstItem != itemsRange.getFirst() || this.itemsLayout.getChildCount() != itemsRange.getCount()) {
            bool1 = true;
          } else {
            bool1 = false;
          } 
          bool2 = bool1;
        } 
        if (this.firstItem > itemsRange.getFirst() && this.firstItem <= itemsRange.getLast()) {
          for (int k = this.firstItem - 1; k >= itemsRange.getFirst() && addViewItem(k, true); k--)
            this.firstItem = k; 
        } else {
          this.firstItem = itemsRange.getFirst();
        } 
        int j = this.firstItem;
        int i = this.itemsLayout.getChildCount();
        while (i < itemsRange.getCount()) {
          int k = j;
          if (!addViewItem(this.firstItem + i, false)) {
            k = j;
            if (this.itemsLayout.getChildCount() == 0)
              k = j + 1; 
          } 
          i++;
          j = k;
        } 
        this.firstItem = j;
      } 
    } 
    return bool2;
  }
  
  private void updateView() {
    if (rebuildItems()) {
      calculateLayoutWidth(getWidth(), 1073741824);
      layout(getWidth(), getHeight());
    } 
  }
  
  public void addChangingListener(OnWheelChangedListener paramOnWheelChangedListener) {
    this.changingListeners.add(paramOnWheelChangedListener);
  }
  
  public void addClickingListener(OnWheelClickedListener paramOnWheelClickedListener) {
    this.clickingListeners.add(paramOnWheelClickedListener);
  }
  
  public void addScrollingListener(OnWheelScrollListener paramOnWheelScrollListener) {
    this.scrollingListeners.add(paramOnWheelScrollListener);
  }
  
  public int getCurrentItem() {
    return this.currentItem;
  }
  
  public WheelViewAdapter getViewAdapter() {
    return this.viewAdapter;
  }
  
  public int getVisibleItems() {
    return this.visibleItems;
  }
  
  public void invalidateWheel(boolean paramBoolean) {
    if (paramBoolean) {
      this.recycle.clearAll();
      if (this.itemsLayout != null)
        this.itemsLayout.removeAllViews(); 
      this.scrollingOffset = 0;
    } else if (this.itemsLayout != null) {
      this.recycle.recycleItems(this.itemsLayout, this.firstItem, new ItemsRange());
    } 
    invalidate();
  }
  
  public boolean isCyclic() {
    return this.isCyclic;
  }
  
  protected void notifyChangingListeners(int paramInt1, int paramInt2) {
    Iterator<OnWheelChangedListener> iterator = this.changingListeners.iterator();
    while (iterator.hasNext())
      ((OnWheelChangedListener)iterator.next()).onChanged(this, paramInt1, paramInt2); 
  }
  
  protected void notifyClickListenersAboutClick(int paramInt) {
    Iterator<OnWheelClickedListener> iterator = this.clickingListeners.iterator();
    while (iterator.hasNext())
      ((OnWheelClickedListener)iterator.next()).onItemClicked(this, paramInt); 
  }
  
  protected void notifyScrollingListenersAboutEnd() {
    Iterator<OnWheelScrollListener> iterator = this.scrollingListeners.iterator();
    while (iterator.hasNext())
      ((OnWheelScrollListener)iterator.next()).onScrollingFinished(this); 
  }
  
  protected void notifyScrollingListenersAboutStart() {
    Iterator<OnWheelScrollListener> iterator = this.scrollingListeners.iterator();
    while (iterator.hasNext())
      ((OnWheelScrollListener)iterator.next()).onScrollingStarted(this); 
  }
  
  protected void onDraw(Canvas paramCanvas) {
    super.onDraw(paramCanvas);
    if (this.viewAdapter != null && this.viewAdapter.getItemsCount() > 0) {
      updateView();
      drawItems(paramCanvas);
      drawCenterRect(paramCanvas);
    } 
    drawShadows(paramCanvas);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    layout(paramInt3 - paramInt1, paramInt4 - paramInt2);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.getMode(paramInt1);
    int j = View.MeasureSpec.getMode(paramInt2);
    int k = View.MeasureSpec.getSize(paramInt1);
    paramInt1 = View.MeasureSpec.getSize(paramInt2);
    buildViewForMeasuring();
    i = calculateLayoutWidth(k, i);
    if (j != 1073741824) {
      paramInt2 = getDesiredHeight(this.itemsLayout);
      if (j == Integer.MIN_VALUE) {
        paramInt1 = Math.min(paramInt2, paramInt1);
      } else {
        paramInt1 = paramInt2;
      } 
    } 
    setMeasuredDimension(i, paramInt1);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    if (!isEnabled() || getViewAdapter() == null)
      return true; 
    switch (paramMotionEvent.getAction()) {
      default:
        return this.scroller.onTouchEvent(paramMotionEvent);
      case 2:
        if (getParent() != null)
          getParent().requestDisallowInterceptTouchEvent(true); 
      case 1:
        break;
    } 
    if (!this.isScrollingPerformed) {
      int i = (int)paramMotionEvent.getY() - getHeight() / 2;
      if (i > 0) {
        i += getItemHeight() / 2;
      } else {
        i -= getItemHeight() / 2;
      } 
      i /= getItemHeight();
      if (i != 0 && isValidItemIndex(this.currentItem + i))
        notifyClickListenersAboutClick(this.currentItem + i); 
    } 
  }
  
  public void removeChangingListener(OnWheelChangedListener paramOnWheelChangedListener) {
    this.changingListeners.remove(paramOnWheelChangedListener);
  }
  
  public void removeClickingListener(OnWheelClickedListener paramOnWheelClickedListener) {
    this.clickingListeners.remove(paramOnWheelClickedListener);
  }
  
  public void removeScrollingListener(OnWheelScrollListener paramOnWheelScrollListener) {
    this.scrollingListeners.remove(paramOnWheelScrollListener);
  }
  
  public void scroll(int paramInt1, int paramInt2) {
    int i = getItemHeight();
    int j = this.scrollingOffset;
    this.scroller.scroll(paramInt1 * i - j, paramInt2);
  }
  
  public void setCurrentItem(int paramInt) {
    setCurrentItem(paramInt, false);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("setCurrentItem=");
    stringBuilder.append(paramInt);
    Log.e("setCurrentItem", stringBuilder.toString());
  }
  
  public void setCurrentItem(int paramInt, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: getfield viewAdapter : Lcom/qh/WheelView/WheelViewAdapter;
    //   4: ifnull -> 190
    //   7: aload_0
    //   8: getfield viewAdapter : Lcom/qh/WheelView/WheelViewAdapter;
    //   11: invokeinterface getItemsCount : ()I
    //   16: ifne -> 22
    //   19: goto -> 190
    //   22: aload_0
    //   23: getfield viewAdapter : Lcom/qh/WheelView/WheelViewAdapter;
    //   26: invokeinterface getItemsCount : ()I
    //   31: istore_3
    //   32: iload_1
    //   33: iflt -> 44
    //   36: iload_1
    //   37: istore #4
    //   39: iload_1
    //   40: iload_3
    //   41: if_icmplt -> 67
    //   44: aload_0
    //   45: getfield isCyclic : Z
    //   48: ifeq -> 189
    //   51: iload_1
    //   52: ifge -> 62
    //   55: iload_1
    //   56: iload_3
    //   57: iadd
    //   58: istore_1
    //   59: goto -> 51
    //   62: iload_1
    //   63: iload_3
    //   64: irem
    //   65: istore #4
    //   67: iload #4
    //   69: aload_0
    //   70: getfield currentItem : I
    //   73: if_icmpeq -> 188
    //   76: iload_2
    //   77: ifeq -> 159
    //   80: iload #4
    //   82: aload_0
    //   83: getfield currentItem : I
    //   86: isub
    //   87: istore #5
    //   89: iload #5
    //   91: istore_1
    //   92: aload_0
    //   93: getfield isCyclic : Z
    //   96: ifeq -> 150
    //   99: iload_3
    //   100: iload #4
    //   102: aload_0
    //   103: getfield currentItem : I
    //   106: invokestatic min : (II)I
    //   109: iadd
    //   110: iload #4
    //   112: aload_0
    //   113: getfield currentItem : I
    //   116: invokestatic max : (II)I
    //   119: isub
    //   120: istore #4
    //   122: iload #5
    //   124: istore_1
    //   125: iload #4
    //   127: iload #5
    //   129: invokestatic abs : (I)I
    //   132: if_icmpge -> 150
    //   135: iload #5
    //   137: ifge -> 146
    //   140: iload #4
    //   142: istore_1
    //   143: goto -> 150
    //   146: iload #4
    //   148: ineg
    //   149: istore_1
    //   150: aload_0
    //   151: iload_1
    //   152: iconst_0
    //   153: invokevirtual scroll : (II)V
    //   156: goto -> 188
    //   159: aload_0
    //   160: iconst_0
    //   161: putfield scrollingOffset : I
    //   164: aload_0
    //   165: getfield currentItem : I
    //   168: istore_1
    //   169: aload_0
    //   170: iload #4
    //   172: putfield currentItem : I
    //   175: aload_0
    //   176: iload_1
    //   177: aload_0
    //   178: getfield currentItem : I
    //   181: invokevirtual notifyChangingListeners : (II)V
    //   184: aload_0
    //   185: invokevirtual invalidate : ()V
    //   188: return
    //   189: return
    //   190: return
  }
  
  public void setCyclic(boolean paramBoolean) {
    this.isCyclic = paramBoolean;
    invalidateWheel(false);
  }
  
  public void setInterpolator(Interpolator paramInterpolator) {
    this.scroller.setInterpolator(paramInterpolator);
  }
  
  public void setViewAdapter(WheelViewAdapter paramWheelViewAdapter) {
    if (this.viewAdapter != null)
      this.viewAdapter.unregisterDataSetObserver(this.dataObserver); 
    this.viewAdapter = paramWheelViewAdapter;
    if (this.viewAdapter != null)
      this.viewAdapter.registerDataSetObserver(this.dataObserver); 
    invalidateWheel(true);
  }
  
  public void setVisibleItems(int paramInt) {
    this.visibleItems = paramInt;
  }
  
  public void stopScrolling() {
    this.scroller.stopScrolling();
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\WheelView\WheelView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */