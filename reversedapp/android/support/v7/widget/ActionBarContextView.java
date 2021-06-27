package android.support.v7.widget;

import android.content.Context;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.appcompat.R;
import android.support.v7.view.ActionMode;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPresenter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class ActionBarContextView extends AbsActionBarView {
  private static final String TAG = "ActionBarContextView";
  
  private View mClose;
  
  private int mCloseItemLayout;
  
  private View mCustomView;
  
  private CharSequence mSubtitle;
  
  private int mSubtitleStyleRes;
  
  private TextView mSubtitleView;
  
  private CharSequence mTitle;
  
  private LinearLayout mTitleLayout;
  
  private boolean mTitleOptional;
  
  private int mTitleStyleRes;
  
  private TextView mTitleView;
  
  public ActionBarContextView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public ActionBarContextView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.actionModeStyle);
  }
  
  public ActionBarContextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.ActionMode, paramInt, 0);
    ViewCompat.setBackground((View)this, tintTypedArray.getDrawable(R.styleable.ActionMode_background));
    this.mTitleStyleRes = tintTypedArray.getResourceId(R.styleable.ActionMode_titleTextStyle, 0);
    this.mSubtitleStyleRes = tintTypedArray.getResourceId(R.styleable.ActionMode_subtitleTextStyle, 0);
    this.mContentHeight = tintTypedArray.getLayoutDimension(R.styleable.ActionMode_height, 0);
    this.mCloseItemLayout = tintTypedArray.getResourceId(R.styleable.ActionMode_closeItemLayout, R.layout.abc_action_mode_close_item_material);
    tintTypedArray.recycle();
  }
  
  private void initTitle() {
    // Byte code:
    //   0: aload_0
    //   1: getfield mTitleLayout : Landroid/widget/LinearLayout;
    //   4: ifnonnull -> 117
    //   7: aload_0
    //   8: invokevirtual getContext : ()Landroid/content/Context;
    //   11: invokestatic from : (Landroid/content/Context;)Landroid/view/LayoutInflater;
    //   14: getstatic android/support/v7/appcompat/R$layout.abc_action_bar_title_item : I
    //   17: aload_0
    //   18: invokevirtual inflate : (ILandroid/view/ViewGroup;)Landroid/view/View;
    //   21: pop
    //   22: aload_0
    //   23: aload_0
    //   24: aload_0
    //   25: invokevirtual getChildCount : ()I
    //   28: iconst_1
    //   29: isub
    //   30: invokevirtual getChildAt : (I)Landroid/view/View;
    //   33: checkcast android/widget/LinearLayout
    //   36: putfield mTitleLayout : Landroid/widget/LinearLayout;
    //   39: aload_0
    //   40: aload_0
    //   41: getfield mTitleLayout : Landroid/widget/LinearLayout;
    //   44: getstatic android/support/v7/appcompat/R$id.action_bar_title : I
    //   47: invokevirtual findViewById : (I)Landroid/view/View;
    //   50: checkcast android/widget/TextView
    //   53: putfield mTitleView : Landroid/widget/TextView;
    //   56: aload_0
    //   57: aload_0
    //   58: getfield mTitleLayout : Landroid/widget/LinearLayout;
    //   61: getstatic android/support/v7/appcompat/R$id.action_bar_subtitle : I
    //   64: invokevirtual findViewById : (I)Landroid/view/View;
    //   67: checkcast android/widget/TextView
    //   70: putfield mSubtitleView : Landroid/widget/TextView;
    //   73: aload_0
    //   74: getfield mTitleStyleRes : I
    //   77: ifeq -> 95
    //   80: aload_0
    //   81: getfield mTitleView : Landroid/widget/TextView;
    //   84: aload_0
    //   85: invokevirtual getContext : ()Landroid/content/Context;
    //   88: aload_0
    //   89: getfield mTitleStyleRes : I
    //   92: invokevirtual setTextAppearance : (Landroid/content/Context;I)V
    //   95: aload_0
    //   96: getfield mSubtitleStyleRes : I
    //   99: ifeq -> 117
    //   102: aload_0
    //   103: getfield mSubtitleView : Landroid/widget/TextView;
    //   106: aload_0
    //   107: invokevirtual getContext : ()Landroid/content/Context;
    //   110: aload_0
    //   111: getfield mSubtitleStyleRes : I
    //   114: invokevirtual setTextAppearance : (Landroid/content/Context;I)V
    //   117: aload_0
    //   118: getfield mTitleView : Landroid/widget/TextView;
    //   121: aload_0
    //   122: getfield mTitle : Ljava/lang/CharSequence;
    //   125: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   128: aload_0
    //   129: getfield mSubtitleView : Landroid/widget/TextView;
    //   132: aload_0
    //   133: getfield mSubtitle : Ljava/lang/CharSequence;
    //   136: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   139: aload_0
    //   140: getfield mTitle : Ljava/lang/CharSequence;
    //   143: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   146: istore_1
    //   147: aload_0
    //   148: getfield mSubtitle : Ljava/lang/CharSequence;
    //   151: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   154: iconst_1
    //   155: ixor
    //   156: istore_2
    //   157: aload_0
    //   158: getfield mSubtitleView : Landroid/widget/TextView;
    //   161: astore_3
    //   162: bipush #8
    //   164: istore #4
    //   166: iload_2
    //   167: ifeq -> 176
    //   170: iconst_0
    //   171: istore #5
    //   173: goto -> 180
    //   176: bipush #8
    //   178: istore #5
    //   180: aload_3
    //   181: iload #5
    //   183: invokevirtual setVisibility : (I)V
    //   186: aload_0
    //   187: getfield mTitleLayout : Landroid/widget/LinearLayout;
    //   190: astore_3
    //   191: iload_1
    //   192: iconst_1
    //   193: ixor
    //   194: ifne -> 205
    //   197: iload #4
    //   199: istore #5
    //   201: iload_2
    //   202: ifeq -> 208
    //   205: iconst_0
    //   206: istore #5
    //   208: aload_3
    //   209: iload #5
    //   211: invokevirtual setVisibility : (I)V
    //   214: aload_0
    //   215: getfield mTitleLayout : Landroid/widget/LinearLayout;
    //   218: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   221: ifnonnull -> 232
    //   224: aload_0
    //   225: aload_0
    //   226: getfield mTitleLayout : Landroid/widget/LinearLayout;
    //   229: invokevirtual addView : (Landroid/view/View;)V
    //   232: return
  }
  
  public void closeMode() {
    if (this.mClose == null) {
      killMode();
      return;
    } 
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
    return (ViewGroup.LayoutParams)new ViewGroup.MarginLayoutParams(-1, -2);
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return (ViewGroup.LayoutParams)new ViewGroup.MarginLayoutParams(getContext(), paramAttributeSet);
  }
  
  public CharSequence getSubtitle() {
    return this.mSubtitle;
  }
  
  public CharSequence getTitle() {
    return this.mTitle;
  }
  
  public boolean hideOverflowMenu() {
    return (this.mActionMenuPresenter != null) ? this.mActionMenuPresenter.hideOverflowMenu() : false;
  }
  
  public void initForMode(final ActionMode mode) {
    if (this.mClose == null) {
      this.mClose = LayoutInflater.from(getContext()).inflate(this.mCloseItemLayout, this, false);
      addView(this.mClose);
    } else if (this.mClose.getParent() == null) {
      addView(this.mClose);
    } 
    this.mClose.findViewById(R.id.action_mode_close_button).setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            mode.finish();
          }
        });
    MenuBuilder menuBuilder = (MenuBuilder)mode.getMenu();
    if (this.mActionMenuPresenter != null)
      this.mActionMenuPresenter.dismissPopupMenus(); 
    this.mActionMenuPresenter = new ActionMenuPresenter(getContext());
    this.mActionMenuPresenter.setReserveOverflow(true);
    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-2, -1);
    menuBuilder.addMenuPresenter((MenuPresenter)this.mActionMenuPresenter, this.mPopupContext);
    this.mMenuView = (ActionMenuView)this.mActionMenuPresenter.getMenuView(this);
    ViewCompat.setBackground((View)this.mMenuView, null);
    addView((View)this.mMenuView, layoutParams);
  }
  
  public boolean isOverflowMenuShowing() {
    return (this.mActionMenuPresenter != null) ? this.mActionMenuPresenter.isOverflowMenuShowing() : false;
  }
  
  public boolean isTitleOptional() {
    return this.mTitleOptional;
  }
  
  public void killMode() {
    removeAllViews();
    this.mCustomView = null;
    this.mMenuView = null;
  }
  
  public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (this.mActionMenuPresenter != null) {
      this.mActionMenuPresenter.hideOverflowMenu();
      this.mActionMenuPresenter.hideSubMenus();
    } 
  }
  
  public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    if (paramAccessibilityEvent.getEventType() == 32) {
      paramAccessibilityEvent.setSource((View)this);
      paramAccessibilityEvent.setClassName(getClass().getName());
      paramAccessibilityEvent.setPackageName(getContext().getPackageName());
      paramAccessibilityEvent.setContentDescription(this.mTitle);
    } else {
      super.onInitializeAccessibilityEvent(paramAccessibilityEvent);
    } 
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i;
    paramBoolean = ViewUtils.isLayoutRtl((View)this);
    if (paramBoolean) {
      i = paramInt3 - paramInt1 - getPaddingRight();
    } else {
      i = getPaddingLeft();
    } 
    int j = getPaddingTop();
    int k = paramInt4 - paramInt2 - getPaddingTop() - getPaddingBottom();
    if (this.mClose != null && this.mClose.getVisibility() != 8) {
      ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)this.mClose.getLayoutParams();
      if (paramBoolean) {
        paramInt2 = marginLayoutParams.rightMargin;
      } else {
        paramInt2 = marginLayoutParams.leftMargin;
      } 
      if (paramBoolean) {
        paramInt4 = marginLayoutParams.leftMargin;
      } else {
        paramInt4 = marginLayoutParams.rightMargin;
      } 
      paramInt2 = next(i, paramInt2, paramBoolean);
      paramInt2 = next(paramInt2 + positionChild(this.mClose, paramInt2, j, k, paramBoolean), paramInt4, paramBoolean);
    } else {
      paramInt2 = i;
    } 
    paramInt4 = paramInt2;
    if (this.mTitleLayout != null) {
      paramInt4 = paramInt2;
      if (this.mCustomView == null) {
        paramInt4 = paramInt2;
        if (this.mTitleLayout.getVisibility() != 8)
          paramInt4 = paramInt2 + positionChild((View)this.mTitleLayout, paramInt2, j, k, paramBoolean); 
      } 
    } 
    if (this.mCustomView != null)
      positionChild(this.mCustomView, paramInt4, j, k, paramBoolean); 
    if (paramBoolean) {
      paramInt1 = getPaddingLeft();
    } else {
      paramInt1 = paramInt3 - paramInt1 - getPaddingRight();
    } 
    if (this.mMenuView != null)
      positionChild((View)this.mMenuView, paramInt1, j, k, paramBoolean ^ true); 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.getMode(paramInt1);
    int j = 1073741824;
    if (i == 1073741824) {
      if (View.MeasureSpec.getMode(paramInt2) != 0) {
        int k = View.MeasureSpec.getSize(paramInt1);
        if (this.mContentHeight > 0) {
          i = this.mContentHeight;
        } else {
          i = View.MeasureSpec.getSize(paramInt2);
        } 
        int m = getPaddingTop() + getPaddingBottom();
        paramInt1 = k - getPaddingLeft() - getPaddingRight();
        int n = i - m;
        int i1 = View.MeasureSpec.makeMeasureSpec(n, -2147483648);
        View view = this.mClose;
        boolean bool = false;
        paramInt2 = paramInt1;
        if (view != null) {
          paramInt1 = measureChildView(this.mClose, paramInt1, i1, 0);
          ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)this.mClose.getLayoutParams();
          paramInt2 = paramInt1 - marginLayoutParams.leftMargin + marginLayoutParams.rightMargin;
        } 
        paramInt1 = paramInt2;
        if (this.mMenuView != null) {
          paramInt1 = paramInt2;
          if (this.mMenuView.getParent() == this)
            paramInt1 = measureChildView((View)this.mMenuView, paramInt2, i1, 0); 
        } 
        paramInt2 = paramInt1;
        if (this.mTitleLayout != null) {
          paramInt2 = paramInt1;
          if (this.mCustomView == null)
            if (this.mTitleOptional) {
              paramInt2 = View.MeasureSpec.makeMeasureSpec(0, 0);
              this.mTitleLayout.measure(paramInt2, i1);
              int i2 = this.mTitleLayout.getMeasuredWidth();
              if (i2 <= paramInt1) {
                i1 = 1;
              } else {
                i1 = 0;
              } 
              paramInt2 = paramInt1;
              if (i1 != 0)
                paramInt2 = paramInt1 - i2; 
              LinearLayout linearLayout = this.mTitleLayout;
              if (i1 != 0) {
                paramInt1 = 0;
              } else {
                paramInt1 = 8;
              } 
              linearLayout.setVisibility(paramInt1);
            } else {
              paramInt2 = measureChildView((View)this.mTitleLayout, paramInt1, i1, 0);
            }  
        } 
        if (this.mCustomView != null) {
          ViewGroup.LayoutParams layoutParams = this.mCustomView.getLayoutParams();
          if (layoutParams.width != -2) {
            paramInt1 = 1073741824;
          } else {
            paramInt1 = Integer.MIN_VALUE;
          } 
          i1 = paramInt2;
          if (layoutParams.width >= 0)
            i1 = Math.min(layoutParams.width, paramInt2); 
          if (layoutParams.height != -2) {
            paramInt2 = j;
          } else {
            paramInt2 = Integer.MIN_VALUE;
          } 
          j = n;
          if (layoutParams.height >= 0)
            j = Math.min(layoutParams.height, n); 
          this.mCustomView.measure(View.MeasureSpec.makeMeasureSpec(i1, paramInt1), View.MeasureSpec.makeMeasureSpec(j, paramInt2));
        } 
        if (this.mContentHeight <= 0) {
          j = getChildCount();
          i = 0;
          paramInt1 = bool;
          while (paramInt1 < j) {
            i1 = getChildAt(paramInt1).getMeasuredHeight() + m;
            paramInt2 = i;
            if (i1 > i)
              paramInt2 = i1; 
            paramInt1++;
            i = paramInt2;
          } 
          setMeasuredDimension(k, i);
        } else {
          setMeasuredDimension(k, i);
        } 
        return;
      } 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(getClass().getSimpleName());
      stringBuilder1.append(" can only be used ");
      stringBuilder1.append("with android:layout_height=\"wrap_content\"");
      throw new IllegalStateException(stringBuilder1.toString());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(getClass().getSimpleName());
    stringBuilder.append(" can only be used ");
    stringBuilder.append("with android:layout_width=\"match_parent\" (or fill_parent)");
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  public void setContentHeight(int paramInt) {
    this.mContentHeight = paramInt;
  }
  
  public void setCustomView(View paramView) {
    if (this.mCustomView != null)
      removeView(this.mCustomView); 
    this.mCustomView = paramView;
    if (paramView != null && this.mTitleLayout != null) {
      removeView((View)this.mTitleLayout);
      this.mTitleLayout = null;
    } 
    if (paramView != null)
      addView(paramView); 
    requestLayout();
  }
  
  public void setSubtitle(CharSequence paramCharSequence) {
    this.mSubtitle = paramCharSequence;
    initTitle();
  }
  
  public void setTitle(CharSequence paramCharSequence) {
    this.mTitle = paramCharSequence;
    initTitle();
  }
  
  public void setTitleOptional(boolean paramBoolean) {
    if (paramBoolean != this.mTitleOptional)
      requestLayout(); 
    this.mTitleOptional = paramBoolean;
  }
  
  public boolean shouldDelayChildPressedState() {
    return false;
  }
  
  public boolean showOverflowMenu() {
    return (this.mActionMenuPresenter != null) ? this.mActionMenuPresenter.showOverflowMenu() : false;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v7\widget\ActionBarContextView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */