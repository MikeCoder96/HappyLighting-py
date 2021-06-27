package android.support.v7.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import java.util.Arrays;

public class GridLayoutManager extends LinearLayoutManager {
  private static final boolean DEBUG = false;
  
  public static final int DEFAULT_SPAN_COUNT = -1;
  
  private static final String TAG = "GridLayoutManager";
  
  int[] mCachedBorders;
  
  final Rect mDecorInsets = new Rect();
  
  boolean mPendingSpanCountChange = false;
  
  final SparseIntArray mPreLayoutSpanIndexCache = new SparseIntArray();
  
  final SparseIntArray mPreLayoutSpanSizeCache = new SparseIntArray();
  
  View[] mSet;
  
  int mSpanCount = -1;
  
  SpanSizeLookup mSpanSizeLookup = new DefaultSpanSizeLookup();
  
  public GridLayoutManager(Context paramContext, int paramInt) {
    super(paramContext);
    setSpanCount(paramInt);
  }
  
  public GridLayoutManager(Context paramContext, int paramInt1, int paramInt2, boolean paramBoolean) {
    super(paramContext, paramInt2, paramBoolean);
    setSpanCount(paramInt1);
  }
  
  public GridLayoutManager(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    setSpanCount((getProperties(paramContext, paramAttributeSet, paramInt1, paramInt2)).spanCount);
  }
  
  private void assignSpans(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt1, int paramInt2, boolean paramBoolean) {
    int i = -1;
    int j = 0;
    if (paramBoolean) {
      i = paramInt1;
      paramInt1 = 0;
      paramInt2 = 1;
    } else {
      paramInt1--;
      paramInt2 = -1;
    } 
    while (paramInt1 != i) {
      View view = this.mSet[paramInt1];
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      layoutParams.mSpanSize = getSpanSize(paramRecycler, paramState, getPosition(view));
      layoutParams.mSpanIndex = j;
      j += layoutParams.mSpanSize;
      paramInt1 += paramInt2;
    } 
  }
  
  private void cachePreLayoutSpanMapping() {
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      LayoutParams layoutParams = (LayoutParams)getChildAt(b).getLayoutParams();
      int j = layoutParams.getViewLayoutPosition();
      this.mPreLayoutSpanSizeCache.put(j, layoutParams.getSpanSize());
      this.mPreLayoutSpanIndexCache.put(j, layoutParams.getSpanIndex());
    } 
  }
  
  private void calculateItemBorders(int paramInt) {
    this.mCachedBorders = calculateItemBorders(this.mCachedBorders, this.mSpanCount, paramInt);
  }
  
  static int[] calculateItemBorders(int[] paramArrayOfint, int paramInt1, int paramInt2) {
    // Byte code:
    //   0: iconst_1
    //   1: istore_3
    //   2: aload_0
    //   3: ifnull -> 27
    //   6: aload_0
    //   7: arraylength
    //   8: iload_1
    //   9: iconst_1
    //   10: iadd
    //   11: if_icmpne -> 27
    //   14: aload_0
    //   15: astore #4
    //   17: aload_0
    //   18: aload_0
    //   19: arraylength
    //   20: iconst_1
    //   21: isub
    //   22: iaload
    //   23: iload_2
    //   24: if_icmpeq -> 34
    //   27: iload_1
    //   28: iconst_1
    //   29: iadd
    //   30: newarray int
    //   32: astore #4
    //   34: iconst_0
    //   35: istore #5
    //   37: aload #4
    //   39: iconst_0
    //   40: iconst_0
    //   41: iastore
    //   42: iload_2
    //   43: iload_1
    //   44: idiv
    //   45: istore #6
    //   47: iload_2
    //   48: iload_1
    //   49: irem
    //   50: istore #7
    //   52: iconst_0
    //   53: istore #8
    //   55: iload #5
    //   57: istore_2
    //   58: iload_3
    //   59: iload_1
    //   60: if_icmpgt -> 116
    //   63: iload_2
    //   64: iload #7
    //   66: iadd
    //   67: istore_2
    //   68: iload_2
    //   69: ifle -> 93
    //   72: iload_1
    //   73: iload_2
    //   74: isub
    //   75: iload #7
    //   77: if_icmpge -> 93
    //   80: iload #6
    //   82: iconst_1
    //   83: iadd
    //   84: istore #5
    //   86: iload_2
    //   87: iload_1
    //   88: isub
    //   89: istore_2
    //   90: goto -> 97
    //   93: iload #6
    //   95: istore #5
    //   97: iload #8
    //   99: iload #5
    //   101: iadd
    //   102: istore #8
    //   104: aload #4
    //   106: iload_3
    //   107: iload #8
    //   109: iastore
    //   110: iinc #3, 1
    //   113: goto -> 58
    //   116: aload #4
    //   118: areturn
  }
  
  private void clearPreLayoutSpanMappingCache() {
    this.mPreLayoutSpanSizeCache.clear();
    this.mPreLayoutSpanIndexCache.clear();
  }
  
  private void ensureAnchorIsInCorrectSpan(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, LinearLayoutManager.AnchorInfo paramAnchorInfo, int paramInt) {
    int i;
    if (paramInt == 1) {
      i = 1;
    } else {
      i = 0;
    } 
    paramInt = getSpanIndex(paramRecycler, paramState, paramAnchorInfo.mPosition);
    if (i) {
      while (paramInt > 0 && paramAnchorInfo.mPosition > 0) {
        paramAnchorInfo.mPosition--;
        paramInt = getSpanIndex(paramRecycler, paramState, paramAnchorInfo.mPosition);
      } 
    } else {
      int j = paramState.getItemCount();
      i = paramAnchorInfo.mPosition;
      while (i < j - 1) {
        int k = i + 1;
        int m = getSpanIndex(paramRecycler, paramState, k);
        if (m > paramInt) {
          i = k;
          paramInt = m;
        } 
      } 
      paramAnchorInfo.mPosition = i;
    } 
  }
  
  private void ensureViewSet() {
    if (this.mSet == null || this.mSet.length != this.mSpanCount)
      this.mSet = new View[this.mSpanCount]; 
  }
  
  private int getSpanGroupIndex(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt) {
    if (!paramState.isPreLayout())
      return this.mSpanSizeLookup.getSpanGroupIndex(paramInt, this.mSpanCount); 
    int i = paramRecycler.convertPreLayoutPositionToPostLayout(paramInt);
    if (i == -1) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Cannot find span size for pre layout position. ");
      stringBuilder.append(paramInt);
      Log.w("GridLayoutManager", stringBuilder.toString());
      return 0;
    } 
    return this.mSpanSizeLookup.getSpanGroupIndex(i, this.mSpanCount);
  }
  
  private int getSpanIndex(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt) {
    if (!paramState.isPreLayout())
      return this.mSpanSizeLookup.getCachedSpanIndex(paramInt, this.mSpanCount); 
    int i = this.mPreLayoutSpanIndexCache.get(paramInt, -1);
    if (i != -1)
      return i; 
    i = paramRecycler.convertPreLayoutPositionToPostLayout(paramInt);
    if (i == -1) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:");
      stringBuilder.append(paramInt);
      Log.w("GridLayoutManager", stringBuilder.toString());
      return 0;
    } 
    return this.mSpanSizeLookup.getCachedSpanIndex(i, this.mSpanCount);
  }
  
  private int getSpanSize(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt) {
    if (!paramState.isPreLayout())
      return this.mSpanSizeLookup.getSpanSize(paramInt); 
    int i = this.mPreLayoutSpanSizeCache.get(paramInt, -1);
    if (i != -1)
      return i; 
    i = paramRecycler.convertPreLayoutPositionToPostLayout(paramInt);
    if (i == -1) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:");
      stringBuilder.append(paramInt);
      Log.w("GridLayoutManager", stringBuilder.toString());
      return 1;
    } 
    return this.mSpanSizeLookup.getSpanSize(i);
  }
  
  private void guessMeasurement(float paramFloat, int paramInt) {
    calculateItemBorders(Math.max(Math.round(paramFloat * this.mSpanCount), paramInt));
  }
  
  private void measureChild(View paramView, int paramInt, boolean paramBoolean) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    Rect rect = layoutParams.mDecorInsets;
    int i = rect.top + rect.bottom + layoutParams.topMargin + layoutParams.bottomMargin;
    int j = rect.left + rect.right + layoutParams.leftMargin + layoutParams.rightMargin;
    int k = getSpaceForSpanRange(layoutParams.mSpanIndex, layoutParams.mSpanSize);
    if (this.mOrientation == 1) {
      j = getChildMeasureSpec(k, paramInt, j, layoutParams.width, false);
      paramInt = getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), getHeightMode(), i, layoutParams.height, true);
    } else {
      paramInt = getChildMeasureSpec(k, paramInt, i, layoutParams.height, false);
      j = getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), getWidthMode(), j, layoutParams.width, true);
    } 
    measureChildWithDecorationsAndMargin(paramView, j, paramInt, paramBoolean);
  }
  
  private void measureChildWithDecorationsAndMargin(View paramView, int paramInt1, int paramInt2, boolean paramBoolean) {
    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)paramView.getLayoutParams();
    if (paramBoolean) {
      paramBoolean = shouldReMeasureChild(paramView, paramInt1, paramInt2, layoutParams);
    } else {
      paramBoolean = shouldMeasureChild(paramView, paramInt1, paramInt2, layoutParams);
    } 
    if (paramBoolean)
      paramView.measure(paramInt1, paramInt2); 
  }
  
  private void updateMeasurements() {
    int i;
    if (getOrientation() == 1) {
      i = getWidth() - getPaddingRight() - getPaddingLeft();
    } else {
      i = getHeight() - getPaddingBottom() - getPaddingTop();
    } 
    calculateItemBorders(i);
  }
  
  public boolean checkLayoutParams(RecyclerView.LayoutParams paramLayoutParams) {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  void collectPrefetchPositionsForLayoutState(RecyclerView.State paramState, LinearLayoutManager.LayoutState paramLayoutState, RecyclerView.LayoutManager.LayoutPrefetchRegistry paramLayoutPrefetchRegistry) {
    int i = this.mSpanCount;
    for (byte b = 0; b < this.mSpanCount && paramLayoutState.hasMore(paramState) && i > 0; b++) {
      int j = paramLayoutState.mCurrentPosition;
      paramLayoutPrefetchRegistry.addPosition(j, Math.max(0, paramLayoutState.mScrollingOffset));
      i -= this.mSpanSizeLookup.getSpanSize(j);
      paramLayoutState.mCurrentPosition += paramLayoutState.mItemDirection;
    } 
  }
  
  View findReferenceChild(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt1, int paramInt2, int paramInt3) {
    byte b;
    ensureLayoutState();
    int i = this.mOrientationHelper.getStartAfterPadding();
    int j = this.mOrientationHelper.getEndAfterPadding();
    if (paramInt2 > paramInt1) {
      b = 1;
    } else {
      b = -1;
    } 
    View view1 = null;
    View view2;
    for (view2 = null; paramInt1 != paramInt2; view2 = view5) {
      View view3 = getChildAt(paramInt1);
      int k = getPosition(view3);
      View view4 = view1;
      View view5 = view2;
      if (k >= 0) {
        view4 = view1;
        view5 = view2;
        if (k < paramInt3)
          if (getSpanIndex(paramRecycler, paramState, k) != 0) {
            view4 = view1;
            view5 = view2;
          } else if (((RecyclerView.LayoutParams)view3.getLayoutParams()).isItemRemoved()) {
            view4 = view1;
            view5 = view2;
            if (view2 == null) {
              view5 = view3;
              view4 = view1;
            } 
          } else if (this.mOrientationHelper.getDecoratedStart(view3) >= j || this.mOrientationHelper.getDecoratedEnd(view3) < i) {
            view4 = view1;
            view5 = view2;
            if (view1 == null) {
              view4 = view3;
              view5 = view2;
            } 
          } else {
            return view3;
          }  
      } 
      paramInt1 += b;
      view1 = view4;
    } 
    if (view1 != null)
      view2 = view1; 
    return view2;
  }
  
  public RecyclerView.LayoutParams generateDefaultLayoutParams() {
    return (this.mOrientation == 0) ? new LayoutParams(-2, -1) : new LayoutParams(-1, -2);
  }
  
  public RecyclerView.LayoutParams generateLayoutParams(Context paramContext, AttributeSet paramAttributeSet) {
    return new LayoutParams(paramContext, paramAttributeSet);
  }
  
  public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return (paramLayoutParams instanceof ViewGroup.MarginLayoutParams) ? new LayoutParams((ViewGroup.MarginLayoutParams)paramLayoutParams) : new LayoutParams(paramLayoutParams);
  }
  
  public int getColumnCountForAccessibility(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    return (this.mOrientation == 1) ? this.mSpanCount : ((paramState.getItemCount() < 1) ? 0 : (getSpanGroupIndex(paramRecycler, paramState, paramState.getItemCount() - 1) + 1));
  }
  
  public int getRowCountForAccessibility(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    return (this.mOrientation == 0) ? this.mSpanCount : ((paramState.getItemCount() < 1) ? 0 : (getSpanGroupIndex(paramRecycler, paramState, paramState.getItemCount() - 1) + 1));
  }
  
  int getSpaceForSpanRange(int paramInt1, int paramInt2) {
    return (this.mOrientation == 1 && isLayoutRTL()) ? (this.mCachedBorders[this.mSpanCount - paramInt1] - this.mCachedBorders[this.mSpanCount - paramInt1 - paramInt2]) : (this.mCachedBorders[paramInt2 + paramInt1] - this.mCachedBorders[paramInt1]);
  }
  
  public int getSpanCount() {
    return this.mSpanCount;
  }
  
  public SpanSizeLookup getSpanSizeLookup() {
    return this.mSpanSizeLookup;
  }
  
  void layoutChunk(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, LinearLayoutManager.LayoutState paramLayoutState, LinearLayoutManager.LayoutChunkResult paramLayoutChunkResult) {
    StringBuilder stringBuilder;
    int j;
    int k;
    boolean bool;
    int i = this.mOrientationHelper.getModeInOther();
    if (i != 1073741824) {
      j = 1;
    } else {
      j = 0;
    } 
    if (getChildCount() > 0) {
      k = this.mCachedBorders[this.mSpanCount];
    } else {
      k = 0;
    } 
    if (j)
      updateMeasurements(); 
    if (paramLayoutState.mItemDirection == 1) {
      bool = true;
    } else {
      bool = false;
    } 
    int m = this.mSpanCount;
    if (!bool)
      m = getSpanIndex(paramRecycler, paramState, paramLayoutState.mCurrentPosition) + getSpanSize(paramRecycler, paramState, paramLayoutState.mCurrentPosition); 
    int n = 0;
    byte b = 0;
    while (b < this.mSpanCount && paramLayoutState.hasMore(paramState) && m > 0) {
      int i2 = paramLayoutState.mCurrentPosition;
      int i3 = getSpanSize(paramRecycler, paramState, i2);
      if (i3 <= this.mSpanCount) {
        m -= i3;
        if (m < 0)
          break; 
        View view = paramLayoutState.next(paramRecycler);
        if (view == null)
          break; 
        n += i3;
        this.mSet[b] = view;
        b++;
        continue;
      } 
      stringBuilder = new StringBuilder();
      stringBuilder.append("Item at position ");
      stringBuilder.append(i2);
      stringBuilder.append(" requires ");
      stringBuilder.append(i3);
      stringBuilder.append(" spans but GridLayoutManager has only ");
      stringBuilder.append(this.mSpanCount);
      stringBuilder.append(" spans.");
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    if (b == 0) {
      paramLayoutChunkResult.mFinished = true;
      return;
    } 
    float f = 0.0F;
    assignSpans((RecyclerView.Recycler)stringBuilder, paramState, b, n, bool);
    int i1 = 0;
    m = 0;
    while (i1 < b) {
      View view = this.mSet[i1];
      if (paramLayoutState.mScrapList == null) {
        if (bool) {
          addView(view);
        } else {
          addView(view, 0);
        } 
      } else if (bool) {
        addDisappearingView(view);
      } else {
        addDisappearingView(view, 0);
      } 
      calculateItemDecorationsForChild(view, this.mDecorInsets);
      measureChild(view, i, false);
      int i2 = this.mOrientationHelper.getDecoratedMeasurement(view);
      n = m;
      if (i2 > m)
        n = i2; 
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      float f1 = this.mOrientationHelper.getDecoratedMeasurementInOther(view) * 1.0F / layoutParams.mSpanSize;
      float f2 = f;
      if (f1 > f)
        f2 = f1; 
      i1++;
      m = n;
      f = f2;
    } 
    n = m;
    if (j) {
      guessMeasurement(f, k);
      j = 0;
      m = 0;
      while (true) {
        n = m;
        if (j < b) {
          View view = this.mSet[j];
          measureChild(view, 1073741824, true);
          k = this.mOrientationHelper.getDecoratedMeasurement(view);
          n = m;
          if (k > m)
            n = k; 
          j++;
          m = n;
          continue;
        } 
        break;
      } 
    } 
    for (m = 0; m < b; m++) {
      View view = this.mSet[m];
      if (this.mOrientationHelper.getDecoratedMeasurement(view) != n) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        Rect rect = layoutParams.mDecorInsets;
        k = rect.top + rect.bottom + layoutParams.topMargin + layoutParams.bottomMargin;
        j = rect.left + rect.right + layoutParams.leftMargin + layoutParams.rightMargin;
        i1 = getSpaceForSpanRange(layoutParams.mSpanIndex, layoutParams.mSpanSize);
        if (this.mOrientation == 1) {
          j = getChildMeasureSpec(i1, 1073741824, j, layoutParams.width, false);
          k = View.MeasureSpec.makeMeasureSpec(n - k, 1073741824);
        } else {
          j = View.MeasureSpec.makeMeasureSpec(n - j, 1073741824);
          k = getChildMeasureSpec(i1, 1073741824, k, layoutParams.height, false);
        } 
        measureChildWithDecorationsAndMargin(view, j, k, true);
      } 
    } 
    i1 = 0;
    paramLayoutChunkResult.mConsumed = n;
    if (this.mOrientation == 1) {
      if (paramLayoutState.mLayoutDirection == -1) {
        j = paramLayoutState.mOffset;
        m = j;
        n = j - n;
      } else {
        j = paramLayoutState.mOffset;
        m = j;
        j = n + j;
        n = m;
        m = j;
      } 
      j = 0;
      k = 0;
    } else if (paramLayoutState.mLayoutDirection == -1) {
      j = paramLayoutState.mOffset;
      boolean bool1 = false;
      m = 0;
      k = j;
      j -= n;
      n = bool1;
    } else {
      j = paramLayoutState.mOffset;
      k = n + j;
      n = 0;
      m = 0;
    } 
    while (i1 < b) {
      View view = this.mSet[i1];
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      if (this.mOrientation == 1) {
        if (isLayoutRTL()) {
          k = getPaddingLeft() + this.mCachedBorders[this.mSpanCount - layoutParams.mSpanIndex];
          int i3 = this.mOrientationHelper.getDecoratedMeasurementInOther(view);
          j = k;
          k -= i3;
        } else {
          j = getPaddingLeft() + this.mCachedBorders[layoutParams.mSpanIndex];
          k = this.mOrientationHelper.getDecoratedMeasurementInOther(view) + j;
          int i3 = j;
          j = k;
          k = i3;
        } 
      } else {
        n = getPaddingTop() + this.mCachedBorders[layoutParams.mSpanIndex];
        m = this.mOrientationHelper.getDecoratedMeasurementInOther(view) + n;
        int i3 = j;
        j = k;
        k = i3;
      } 
      layoutDecoratedWithMargins(view, k, n, j, m);
      if (layoutParams.isItemRemoved() || layoutParams.isItemChanged())
        paramLayoutChunkResult.mIgnoreConsumed = true; 
      paramLayoutChunkResult.mFocusable |= view.hasFocusable();
      int i2 = i1 + 1;
      i1 = j;
      j = k;
      k = i1;
      i1 = i2;
    } 
    Arrays.fill((Object[])this.mSet, (Object)null);
  }
  
  void onAnchorReady(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, LinearLayoutManager.AnchorInfo paramAnchorInfo, int paramInt) {
    super.onAnchorReady(paramRecycler, paramState, paramAnchorInfo, paramInt);
    updateMeasurements();
    if (paramState.getItemCount() > 0 && !paramState.isPreLayout())
      ensureAnchorIsInCorrectSpan(paramRecycler, paramState, paramAnchorInfo, paramInt); 
    ensureViewSet();
  }
  
  public View onFocusSearchFailed(View paramView, int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokevirtual findContainingItemView : (Landroid/view/View;)Landroid/view/View;
    //   5: astore #5
    //   7: aconst_null
    //   8: astore #6
    //   10: aload #5
    //   12: ifnonnull -> 17
    //   15: aconst_null
    //   16: areturn
    //   17: aload #5
    //   19: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   22: checkcast android/support/v7/widget/GridLayoutManager$LayoutParams
    //   25: astore #7
    //   27: aload #7
    //   29: getfield mSpanIndex : I
    //   32: istore #8
    //   34: aload #7
    //   36: getfield mSpanIndex : I
    //   39: aload #7
    //   41: getfield mSpanSize : I
    //   44: iadd
    //   45: istore #9
    //   47: aload_0
    //   48: aload_1
    //   49: iload_2
    //   50: aload_3
    //   51: aload #4
    //   53: invokespecial onFocusSearchFailed : (Landroid/view/View;ILandroid/support/v7/widget/RecyclerView$Recycler;Landroid/support/v7/widget/RecyclerView$State;)Landroid/view/View;
    //   56: ifnonnull -> 61
    //   59: aconst_null
    //   60: areturn
    //   61: aload_0
    //   62: iload_2
    //   63: invokevirtual convertFocusDirectionToLayoutDirection : (I)I
    //   66: iconst_1
    //   67: if_icmpne -> 76
    //   70: iconst_1
    //   71: istore #10
    //   73: goto -> 79
    //   76: iconst_0
    //   77: istore #10
    //   79: iload #10
    //   81: aload_0
    //   82: getfield mShouldReverseLayout : Z
    //   85: if_icmpeq -> 93
    //   88: iconst_1
    //   89: istore_2
    //   90: goto -> 95
    //   93: iconst_0
    //   94: istore_2
    //   95: iload_2
    //   96: ifeq -> 116
    //   99: aload_0
    //   100: invokevirtual getChildCount : ()I
    //   103: iconst_1
    //   104: isub
    //   105: istore #11
    //   107: iconst_m1
    //   108: istore #12
    //   110: iconst_m1
    //   111: istore #13
    //   113: goto -> 128
    //   116: aload_0
    //   117: invokevirtual getChildCount : ()I
    //   120: istore #12
    //   122: iconst_0
    //   123: istore #11
    //   125: iconst_1
    //   126: istore #13
    //   128: aload_0
    //   129: getfield mOrientation : I
    //   132: iconst_1
    //   133: if_icmpne -> 149
    //   136: aload_0
    //   137: invokevirtual isLayoutRTL : ()Z
    //   140: ifeq -> 149
    //   143: iconst_1
    //   144: istore #14
    //   146: goto -> 152
    //   149: iconst_0
    //   150: istore #14
    //   152: aload_0
    //   153: aload_3
    //   154: aload #4
    //   156: iload #11
    //   158: invokespecial getSpanGroupIndex : (Landroid/support/v7/widget/RecyclerView$Recycler;Landroid/support/v7/widget/RecyclerView$State;I)I
    //   161: istore #15
    //   163: aconst_null
    //   164: astore_1
    //   165: iconst_m1
    //   166: istore #16
    //   168: iconst_0
    //   169: istore #17
    //   171: iconst_0
    //   172: istore_2
    //   173: iconst_m1
    //   174: istore #18
    //   176: iload #12
    //   178: istore #19
    //   180: iload #16
    //   182: istore #12
    //   184: iload #11
    //   186: iload #19
    //   188: if_icmpeq -> 559
    //   191: aload_0
    //   192: aload_3
    //   193: aload #4
    //   195: iload #11
    //   197: invokespecial getSpanGroupIndex : (Landroid/support/v7/widget/RecyclerView$Recycler;Landroid/support/v7/widget/RecyclerView$State;I)I
    //   200: istore #16
    //   202: aload_0
    //   203: iload #11
    //   205: invokevirtual getChildAt : (I)Landroid/view/View;
    //   208: astore #7
    //   210: aload #7
    //   212: aload #5
    //   214: if_acmpne -> 220
    //   217: goto -> 559
    //   220: aload #7
    //   222: invokevirtual hasFocusable : ()Z
    //   225: ifeq -> 246
    //   228: iload #16
    //   230: iload #15
    //   232: if_icmpeq -> 246
    //   235: aload #6
    //   237: ifnull -> 243
    //   240: goto -> 559
    //   243: goto -> 549
    //   246: aload #7
    //   248: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   251: checkcast android/support/v7/widget/GridLayoutManager$LayoutParams
    //   254: astore #20
    //   256: aload #20
    //   258: getfield mSpanIndex : I
    //   261: istore #21
    //   263: aload #20
    //   265: getfield mSpanIndex : I
    //   268: aload #20
    //   270: getfield mSpanSize : I
    //   273: iadd
    //   274: istore #22
    //   276: aload #7
    //   278: invokevirtual hasFocusable : ()Z
    //   281: ifeq -> 301
    //   284: iload #21
    //   286: iload #8
    //   288: if_icmpne -> 301
    //   291: iload #22
    //   293: iload #9
    //   295: if_icmpne -> 301
    //   298: aload #7
    //   300: areturn
    //   301: aload #7
    //   303: invokevirtual hasFocusable : ()Z
    //   306: ifeq -> 314
    //   309: aload #6
    //   311: ifnull -> 326
    //   314: aload #7
    //   316: invokevirtual hasFocusable : ()Z
    //   319: ifne -> 332
    //   322: aload_1
    //   323: ifnonnull -> 332
    //   326: iconst_1
    //   327: istore #16
    //   329: goto -> 470
    //   332: iload #21
    //   334: iload #8
    //   336: invokestatic max : (II)I
    //   339: istore #16
    //   341: iload #22
    //   343: iload #9
    //   345: invokestatic min : (II)I
    //   348: iload #16
    //   350: isub
    //   351: istore #23
    //   353: aload #7
    //   355: invokevirtual hasFocusable : ()Z
    //   358: ifeq -> 404
    //   361: iload #23
    //   363: iload #17
    //   365: if_icmple -> 371
    //   368: goto -> 326
    //   371: iload #23
    //   373: iload #17
    //   375: if_icmpne -> 467
    //   378: iload #21
    //   380: iload #12
    //   382: if_icmple -> 391
    //   385: iconst_1
    //   386: istore #16
    //   388: goto -> 394
    //   391: iconst_0
    //   392: istore #16
    //   394: iload #14
    //   396: iload #16
    //   398: if_icmpne -> 467
    //   401: goto -> 326
    //   404: aload #6
    //   406: ifnonnull -> 467
    //   409: iconst_0
    //   410: istore #24
    //   412: aload_0
    //   413: aload #7
    //   415: iconst_0
    //   416: iconst_1
    //   417: invokevirtual isViewPartiallyVisible : (Landroid/view/View;ZZ)Z
    //   420: ifeq -> 467
    //   423: iload_2
    //   424: istore #16
    //   426: iload #23
    //   428: iload #16
    //   430: if_icmple -> 436
    //   433: goto -> 326
    //   436: iload #23
    //   438: iload #16
    //   440: if_icmpne -> 467
    //   443: iload #24
    //   445: istore #16
    //   447: iload #21
    //   449: iload #18
    //   451: if_icmple -> 457
    //   454: iconst_1
    //   455: istore #16
    //   457: iload #14
    //   459: iload #16
    //   461: if_icmpne -> 467
    //   464: goto -> 326
    //   467: iconst_0
    //   468: istore #16
    //   470: iload #16
    //   472: ifeq -> 549
    //   475: aload #7
    //   477: invokevirtual hasFocusable : ()Z
    //   480: ifeq -> 514
    //   483: aload #20
    //   485: getfield mSpanIndex : I
    //   488: istore #12
    //   490: iload #22
    //   492: iload #9
    //   494: invokestatic min : (II)I
    //   497: iload #21
    //   499: iload #8
    //   501: invokestatic max : (II)I
    //   504: isub
    //   505: istore #17
    //   507: aload #7
    //   509: astore #6
    //   511: goto -> 549
    //   514: aload #20
    //   516: getfield mSpanIndex : I
    //   519: istore #18
    //   521: iload #22
    //   523: iload #9
    //   525: invokestatic min : (II)I
    //   528: istore_2
    //   529: iload #21
    //   531: iload #8
    //   533: invokestatic max : (II)I
    //   536: istore #16
    //   538: aload #7
    //   540: astore_1
    //   541: iload_2
    //   542: iload #16
    //   544: isub
    //   545: istore_2
    //   546: goto -> 549
    //   549: iload #11
    //   551: iload #13
    //   553: iadd
    //   554: istore #11
    //   556: goto -> 184
    //   559: aload #6
    //   561: ifnull -> 570
    //   564: aload #6
    //   566: astore_1
    //   567: goto -> 570
    //   570: aload_1
    //   571: areturn
  }
  
  public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, View paramView, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat) {
    ViewGroup.LayoutParams layoutParams1 = paramView.getLayoutParams();
    if (!(layoutParams1 instanceof LayoutParams)) {
      onInitializeAccessibilityNodeInfoForItem(paramView, paramAccessibilityNodeInfoCompat);
      return;
    } 
    LayoutParams layoutParams = (LayoutParams)layoutParams1;
    int i = getSpanGroupIndex(paramRecycler, paramState, layoutParams.getViewLayoutPosition());
    if (this.mOrientation == 0) {
      boolean bool;
      int j = layoutParams.getSpanIndex();
      int k = layoutParams.getSpanSize();
      if (this.mSpanCount > 1 && layoutParams.getSpanSize() == this.mSpanCount) {
        bool = true;
      } else {
        bool = false;
      } 
      paramAccessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(j, k, i, 1, bool, false));
    } else {
      boolean bool;
      int k = layoutParams.getSpanIndex();
      int j = layoutParams.getSpanSize();
      if (this.mSpanCount > 1 && layoutParams.getSpanSize() == this.mSpanCount) {
        bool = true;
      } else {
        bool = false;
      } 
      paramAccessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(i, 1, k, j, bool, false));
    } 
  }
  
  public void onItemsAdded(RecyclerView paramRecyclerView, int paramInt1, int paramInt2) {
    this.mSpanSizeLookup.invalidateSpanIndexCache();
  }
  
  public void onItemsChanged(RecyclerView paramRecyclerView) {
    this.mSpanSizeLookup.invalidateSpanIndexCache();
  }
  
  public void onItemsMoved(RecyclerView paramRecyclerView, int paramInt1, int paramInt2, int paramInt3) {
    this.mSpanSizeLookup.invalidateSpanIndexCache();
  }
  
  public void onItemsRemoved(RecyclerView paramRecyclerView, int paramInt1, int paramInt2) {
    this.mSpanSizeLookup.invalidateSpanIndexCache();
  }
  
  public void onItemsUpdated(RecyclerView paramRecyclerView, int paramInt1, int paramInt2, Object paramObject) {
    this.mSpanSizeLookup.invalidateSpanIndexCache();
  }
  
  public void onLayoutChildren(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    if (paramState.isPreLayout())
      cachePreLayoutSpanMapping(); 
    super.onLayoutChildren(paramRecycler, paramState);
    clearPreLayoutSpanMappingCache();
  }
  
  public void onLayoutCompleted(RecyclerView.State paramState) {
    super.onLayoutCompleted(paramState);
    this.mPendingSpanCountChange = false;
  }
  
  public int scrollHorizontallyBy(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    updateMeasurements();
    ensureViewSet();
    return super.scrollHorizontallyBy(paramInt, paramRecycler, paramState);
  }
  
  public int scrollVerticallyBy(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    updateMeasurements();
    ensureViewSet();
    return super.scrollVerticallyBy(paramInt, paramRecycler, paramState);
  }
  
  public void setMeasuredDimension(Rect paramRect, int paramInt1, int paramInt2) {
    if (this.mCachedBorders == null)
      super.setMeasuredDimension(paramRect, paramInt1, paramInt2); 
    int i = getPaddingLeft() + getPaddingRight();
    int j = getPaddingTop() + getPaddingBottom();
    if (this.mOrientation == 1) {
      paramInt2 = chooseSize(paramInt2, paramRect.height() + j, getMinimumHeight());
      j = chooseSize(paramInt1, this.mCachedBorders[this.mCachedBorders.length - 1] + i, getMinimumWidth());
      paramInt1 = paramInt2;
      paramInt2 = j;
    } else {
      paramInt1 = chooseSize(paramInt1, paramRect.width() + i, getMinimumWidth());
      j = chooseSize(paramInt2, this.mCachedBorders[this.mCachedBorders.length - 1] + j, getMinimumHeight());
      paramInt2 = paramInt1;
      paramInt1 = j;
    } 
    setMeasuredDimension(paramInt2, paramInt1);
  }
  
  public void setSpanCount(int paramInt) {
    if (paramInt == this.mSpanCount)
      return; 
    this.mPendingSpanCountChange = true;
    if (paramInt >= 1) {
      this.mSpanCount = paramInt;
      this.mSpanSizeLookup.invalidateSpanIndexCache();
      requestLayout();
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Span count should be at least 1. Provided ");
    stringBuilder.append(paramInt);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void setSpanSizeLookup(SpanSizeLookup paramSpanSizeLookup) {
    this.mSpanSizeLookup = paramSpanSizeLookup;
  }
  
  public void setStackFromEnd(boolean paramBoolean) {
    if (!paramBoolean) {
      super.setStackFromEnd(false);
      return;
    } 
    throw new UnsupportedOperationException("GridLayoutManager does not support stack from end. Consider using reverse layout");
  }
  
  public boolean supportsPredictiveItemAnimations() {
    boolean bool;
    if (this.mPendingSavedState == null && !this.mPendingSpanCountChange) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static final class DefaultSpanSizeLookup extends SpanSizeLookup {
    public int getSpanIndex(int param1Int1, int param1Int2) {
      return param1Int1 % param1Int2;
    }
    
    public int getSpanSize(int param1Int) {
      return 1;
    }
  }
  
  public static class LayoutParams extends RecyclerView.LayoutParams {
    public static final int INVALID_SPAN_ID = -1;
    
    int mSpanIndex = -1;
    
    int mSpanSize = 0;
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
    }
    
    public LayoutParams(RecyclerView.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super(param1MarginLayoutParams);
    }
    
    public int getSpanIndex() {
      return this.mSpanIndex;
    }
    
    public int getSpanSize() {
      return this.mSpanSize;
    }
  }
  
  public static abstract class SpanSizeLookup {
    private boolean mCacheSpanIndices = false;
    
    final SparseIntArray mSpanIndexCache = new SparseIntArray();
    
    int findReferenceIndexFromCache(int param1Int) {
      int i = this.mSpanIndexCache.size() - 1;
      int j = 0;
      while (j <= i) {
        int k = j + i >>> 1;
        if (this.mSpanIndexCache.keyAt(k) < param1Int) {
          j = k + 1;
          continue;
        } 
        i = k - 1;
      } 
      param1Int = j - 1;
      return (param1Int >= 0 && param1Int < this.mSpanIndexCache.size()) ? this.mSpanIndexCache.keyAt(param1Int) : -1;
    }
    
    int getCachedSpanIndex(int param1Int1, int param1Int2) {
      if (!this.mCacheSpanIndices)
        return getSpanIndex(param1Int1, param1Int2); 
      int i = this.mSpanIndexCache.get(param1Int1, -1);
      if (i != -1)
        return i; 
      param1Int2 = getSpanIndex(param1Int1, param1Int2);
      this.mSpanIndexCache.put(param1Int1, param1Int2);
      return param1Int2;
    }
    
    public int getSpanGroupIndex(int param1Int1, int param1Int2) {
      int i = getSpanSize(param1Int1);
      byte b = 0;
      int j = 0;
      int k;
      for (k = 0; b < param1Int1; k = i1) {
        int i1;
        int m = getSpanSize(b);
        int n = j + m;
        if (n == param1Int2) {
          i1 = k + 1;
          j = 0;
        } else {
          j = n;
          i1 = k;
          if (n > param1Int2) {
            i1 = k + 1;
            j = m;
          } 
        } 
        b++;
      } 
      param1Int1 = k;
      if (j + i > param1Int2)
        param1Int1 = k + 1; 
      return param1Int1;
    }
    
    public int getSpanIndex(int param1Int1, int param1Int2) {
      // Byte code:
      //   0: aload_0
      //   1: iload_1
      //   2: invokevirtual getSpanSize : (I)I
      //   5: istore_3
      //   6: iload_3
      //   7: iload_2
      //   8: if_icmpne -> 13
      //   11: iconst_0
      //   12: ireturn
      //   13: aload_0
      //   14: getfield mCacheSpanIndices : Z
      //   17: ifeq -> 66
      //   20: aload_0
      //   21: getfield mSpanIndexCache : Landroid/util/SparseIntArray;
      //   24: invokevirtual size : ()I
      //   27: ifle -> 66
      //   30: aload_0
      //   31: iload_1
      //   32: invokevirtual findReferenceIndexFromCache : (I)I
      //   35: istore #4
      //   37: iload #4
      //   39: iflt -> 66
      //   42: aload_0
      //   43: getfield mSpanIndexCache : Landroid/util/SparseIntArray;
      //   46: iload #4
      //   48: invokevirtual get : (I)I
      //   51: aload_0
      //   52: iload #4
      //   54: invokevirtual getSpanSize : (I)I
      //   57: iadd
      //   58: istore #5
      //   60: iinc #4, 1
      //   63: goto -> 72
      //   66: iconst_0
      //   67: istore #4
      //   69: iconst_0
      //   70: istore #5
      //   72: iload #4
      //   74: iload_1
      //   75: if_icmpge -> 125
      //   78: aload_0
      //   79: iload #4
      //   81: invokevirtual getSpanSize : (I)I
      //   84: istore #6
      //   86: iload #5
      //   88: iload #6
      //   90: iadd
      //   91: istore #7
      //   93: iload #7
      //   95: iload_2
      //   96: if_icmpne -> 105
      //   99: iconst_0
      //   100: istore #5
      //   102: goto -> 119
      //   105: iload #7
      //   107: istore #5
      //   109: iload #7
      //   111: iload_2
      //   112: if_icmple -> 119
      //   115: iload #6
      //   117: istore #5
      //   119: iinc #4, 1
      //   122: goto -> 72
      //   125: iload_3
      //   126: iload #5
      //   128: iadd
      //   129: iload_2
      //   130: if_icmpgt -> 136
      //   133: iload #5
      //   135: ireturn
      //   136: iconst_0
      //   137: ireturn
    }
    
    public abstract int getSpanSize(int param1Int);
    
    public void invalidateSpanIndexCache() {
      this.mSpanIndexCache.clear();
    }
    
    public boolean isSpanIndexCacheEnabled() {
      return this.mCacheSpanIndices;
    }
    
    public void setSpanIndexCacheEnabled(boolean param1Boolean) {
      this.mCacheSpanIndices = param1Boolean;
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v7\widget\GridLayoutManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */