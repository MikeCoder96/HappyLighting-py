package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ActionProvider;
import android.support.v7.appcompat.R;
import android.support.v7.view.ActionBarPolicy;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.view.menu.BaseMenuPresenter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.ShowableListMenu;
import android.support.v7.view.menu.SubMenuBuilder;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

class ActionMenuPresenter extends BaseMenuPresenter implements ActionProvider.SubUiVisibilityListener {
  private static final String TAG = "ActionMenuPresenter";
  
  private final SparseBooleanArray mActionButtonGroups = new SparseBooleanArray();
  
  ActionButtonSubmenu mActionButtonPopup;
  
  private int mActionItemWidthLimit;
  
  private boolean mExpandedActionViewsExclusive;
  
  private int mMaxItems;
  
  private boolean mMaxItemsSet;
  
  private int mMinCellSize;
  
  int mOpenSubMenuId;
  
  OverflowMenuButton mOverflowButton;
  
  OverflowPopup mOverflowPopup;
  
  private Drawable mPendingOverflowIcon;
  
  private boolean mPendingOverflowIconSet;
  
  private ActionMenuPopupCallback mPopupCallback;
  
  final PopupPresenterCallback mPopupPresenterCallback = new PopupPresenterCallback();
  
  OpenOverflowRunnable mPostedOpenRunnable;
  
  private boolean mReserveOverflow;
  
  private boolean mReserveOverflowSet;
  
  private View mScrapActionButtonView;
  
  private boolean mStrictWidthLimit;
  
  private int mWidthLimit;
  
  private boolean mWidthLimitSet;
  
  public ActionMenuPresenter(Context paramContext) {
    super(paramContext, R.layout.abc_action_menu_layout, R.layout.abc_action_menu_item_layout);
  }
  
  private View findViewForItem(MenuItem paramMenuItem) {
    ViewGroup viewGroup = (ViewGroup)this.mMenuView;
    if (viewGroup == null)
      return null; 
    int i = viewGroup.getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = viewGroup.getChildAt(b);
      if (view instanceof MenuView.ItemView && ((MenuView.ItemView)view).getItemData() == paramMenuItem)
        return view; 
    } 
    return null;
  }
  
  public void bindItemView(MenuItemImpl paramMenuItemImpl, MenuView.ItemView paramItemView) {
    paramItemView.initialize(paramMenuItemImpl, 0);
    ActionMenuView actionMenuView = (ActionMenuView)this.mMenuView;
    ActionMenuItemView actionMenuItemView = (ActionMenuItemView)paramItemView;
    actionMenuItemView.setItemInvoker(actionMenuView);
    if (this.mPopupCallback == null)
      this.mPopupCallback = new ActionMenuPopupCallback(); 
    actionMenuItemView.setPopupCallback(this.mPopupCallback);
  }
  
  public boolean dismissPopupMenus() {
    return hideOverflowMenu() | hideSubMenus();
  }
  
  public boolean filterLeftoverView(ViewGroup paramViewGroup, int paramInt) {
    return (paramViewGroup.getChildAt(paramInt) == this.mOverflowButton) ? false : super.filterLeftoverView(paramViewGroup, paramInt);
  }
  
  public boolean flagActionItems() {
    // Byte code:
    //   0: aload_0
    //   1: astore_1
    //   2: aload_1
    //   3: getfield mMenu : Landroid/support/v7/view/menu/MenuBuilder;
    //   6: ifnull -> 25
    //   9: aload_1
    //   10: getfield mMenu : Landroid/support/v7/view/menu/MenuBuilder;
    //   13: invokevirtual getVisibleItems : ()Ljava/util/ArrayList;
    //   16: astore_2
    //   17: aload_2
    //   18: invokevirtual size : ()I
    //   21: istore_3
    //   22: goto -> 29
    //   25: aconst_null
    //   26: astore_2
    //   27: iconst_0
    //   28: istore_3
    //   29: aload_1
    //   30: getfield mMaxItems : I
    //   33: istore #4
    //   35: aload_1
    //   36: getfield mActionItemWidthLimit : I
    //   39: istore #5
    //   41: iconst_0
    //   42: iconst_0
    //   43: invokestatic makeMeasureSpec : (II)I
    //   46: istore #6
    //   48: aload_1
    //   49: getfield mMenuView : Landroid/support/v7/view/menu/MenuView;
    //   52: checkcast android/view/ViewGroup
    //   55: astore #7
    //   57: iconst_0
    //   58: istore #8
    //   60: iconst_0
    //   61: istore #9
    //   63: iconst_0
    //   64: istore #10
    //   66: iconst_0
    //   67: istore #11
    //   69: iload #8
    //   71: iload_3
    //   72: if_icmpge -> 153
    //   75: aload_2
    //   76: iload #8
    //   78: invokevirtual get : (I)Ljava/lang/Object;
    //   81: checkcast android/support/v7/view/menu/MenuItemImpl
    //   84: astore #12
    //   86: aload #12
    //   88: invokevirtual requiresActionButton : ()Z
    //   91: ifeq -> 100
    //   94: iinc #9, 1
    //   97: goto -> 117
    //   100: aload #12
    //   102: invokevirtual requestsActionButton : ()Z
    //   105: ifeq -> 114
    //   108: iinc #11, 1
    //   111: goto -> 117
    //   114: iconst_1
    //   115: istore #10
    //   117: iload #4
    //   119: istore #13
    //   121: aload_1
    //   122: getfield mExpandedActionViewsExclusive : Z
    //   125: ifeq -> 143
    //   128: iload #4
    //   130: istore #13
    //   132: aload #12
    //   134: invokevirtual isActionViewExpanded : ()Z
    //   137: ifeq -> 143
    //   140: iconst_0
    //   141: istore #13
    //   143: iinc #8, 1
    //   146: iload #13
    //   148: istore #4
    //   150: goto -> 69
    //   153: iload #4
    //   155: istore #8
    //   157: aload_1
    //   158: getfield mReserveOverflow : Z
    //   161: ifeq -> 189
    //   164: iload #10
    //   166: ifne -> 183
    //   169: iload #4
    //   171: istore #8
    //   173: iload #11
    //   175: iload #9
    //   177: iadd
    //   178: iload #4
    //   180: if_icmple -> 189
    //   183: iload #4
    //   185: iconst_1
    //   186: isub
    //   187: istore #8
    //   189: iload #8
    //   191: iload #9
    //   193: isub
    //   194: istore #4
    //   196: aload_1
    //   197: getfield mActionButtonGroups : Landroid/util/SparseBooleanArray;
    //   200: astore #12
    //   202: aload #12
    //   204: invokevirtual clear : ()V
    //   207: aload_1
    //   208: getfield mStrictWidthLimit : Z
    //   211: ifeq -> 251
    //   214: iload #5
    //   216: aload_1
    //   217: getfield mMinCellSize : I
    //   220: idiv
    //   221: istore #8
    //   223: aload_1
    //   224: getfield mMinCellSize : I
    //   227: istore #11
    //   229: aload_1
    //   230: getfield mMinCellSize : I
    //   233: istore #9
    //   235: iload #5
    //   237: iload #11
    //   239: irem
    //   240: iload #8
    //   242: idiv
    //   243: iload #9
    //   245: iadd
    //   246: istore #10
    //   248: goto -> 257
    //   251: iconst_0
    //   252: istore #8
    //   254: iconst_0
    //   255: istore #10
    //   257: iload #5
    //   259: istore #9
    //   261: iconst_0
    //   262: istore #13
    //   264: iconst_0
    //   265: istore #11
    //   267: iload_3
    //   268: istore #5
    //   270: aload_0
    //   271: astore_1
    //   272: iload #13
    //   274: iload #5
    //   276: if_icmpge -> 785
    //   279: aload_2
    //   280: iload #13
    //   282: invokevirtual get : (I)Ljava/lang/Object;
    //   285: checkcast android/support/v7/view/menu/MenuItemImpl
    //   288: astore #14
    //   290: aload #14
    //   292: invokevirtual requiresActionButton : ()Z
    //   295: ifeq -> 413
    //   298: aload_1
    //   299: aload #14
    //   301: aload_1
    //   302: getfield mScrapActionButtonView : Landroid/view/View;
    //   305: aload #7
    //   307: invokevirtual getItemView : (Landroid/support/v7/view/menu/MenuItemImpl;Landroid/view/View;Landroid/view/ViewGroup;)Landroid/view/View;
    //   310: astore #15
    //   312: aload_1
    //   313: getfield mScrapActionButtonView : Landroid/view/View;
    //   316: ifnonnull -> 325
    //   319: aload_1
    //   320: aload #15
    //   322: putfield mScrapActionButtonView : Landroid/view/View;
    //   325: aload_1
    //   326: getfield mStrictWidthLimit : Z
    //   329: ifeq -> 352
    //   332: iload #8
    //   334: aload #15
    //   336: iload #10
    //   338: iload #8
    //   340: iload #6
    //   342: iconst_0
    //   343: invokestatic measureChildForCells : (Landroid/view/View;IIII)I
    //   346: isub
    //   347: istore #8
    //   349: goto -> 361
    //   352: aload #15
    //   354: iload #6
    //   356: iload #6
    //   358: invokevirtual measure : (II)V
    //   361: aload #15
    //   363: invokevirtual getMeasuredWidth : ()I
    //   366: istore_3
    //   367: iload #9
    //   369: iload_3
    //   370: isub
    //   371: istore #9
    //   373: iload #11
    //   375: ifne -> 384
    //   378: iload_3
    //   379: istore #11
    //   381: goto -> 384
    //   384: aload #14
    //   386: invokevirtual getGroupId : ()I
    //   389: istore_3
    //   390: iload_3
    //   391: ifeq -> 404
    //   394: aload #12
    //   396: iload_3
    //   397: iconst_1
    //   398: invokevirtual put : (IZ)V
    //   401: goto -> 404
    //   404: aload #14
    //   406: iconst_1
    //   407: invokevirtual setIsActionButton : (Z)V
    //   410: goto -> 779
    //   413: aload #14
    //   415: invokevirtual requestsActionButton : ()Z
    //   418: ifeq -> 773
    //   421: aload #14
    //   423: invokevirtual getGroupId : ()I
    //   426: istore #16
    //   428: aload #12
    //   430: iload #16
    //   432: invokevirtual get : (I)Z
    //   435: istore #17
    //   437: iload #4
    //   439: ifgt -> 447
    //   442: iload #17
    //   444: ifeq -> 470
    //   447: iload #9
    //   449: ifle -> 470
    //   452: aload_1
    //   453: getfield mStrictWidthLimit : Z
    //   456: ifeq -> 464
    //   459: iload #8
    //   461: ifle -> 470
    //   464: iconst_1
    //   465: istore #18
    //   467: goto -> 473
    //   470: iconst_0
    //   471: istore #18
    //   473: iload #18
    //   475: ifeq -> 646
    //   478: aload_1
    //   479: aload #14
    //   481: aload_1
    //   482: getfield mScrapActionButtonView : Landroid/view/View;
    //   485: aload #7
    //   487: invokevirtual getItemView : (Landroid/support/v7/view/menu/MenuItemImpl;Landroid/view/View;Landroid/view/ViewGroup;)Landroid/view/View;
    //   490: astore #15
    //   492: aload_1
    //   493: getfield mScrapActionButtonView : Landroid/view/View;
    //   496: ifnonnull -> 505
    //   499: aload_1
    //   500: aload #15
    //   502: putfield mScrapActionButtonView : Landroid/view/View;
    //   505: aload_1
    //   506: getfield mStrictWidthLimit : Z
    //   509: ifeq -> 549
    //   512: aload #15
    //   514: iload #10
    //   516: iload #8
    //   518: iload #6
    //   520: iconst_0
    //   521: invokestatic measureChildForCells : (Landroid/view/View;IIII)I
    //   524: istore #19
    //   526: iload #8
    //   528: iload #19
    //   530: isub
    //   531: istore_3
    //   532: iload_3
    //   533: istore #8
    //   535: iload #19
    //   537: ifne -> 558
    //   540: iconst_0
    //   541: istore #18
    //   543: iload_3
    //   544: istore #8
    //   546: goto -> 558
    //   549: aload #15
    //   551: iload #6
    //   553: iload #6
    //   555: invokevirtual measure : (II)V
    //   558: aload #15
    //   560: invokevirtual getMeasuredWidth : ()I
    //   563: istore #19
    //   565: iload #9
    //   567: iload #19
    //   569: isub
    //   570: istore #9
    //   572: iload #11
    //   574: istore_3
    //   575: iload #11
    //   577: ifne -> 583
    //   580: iload #19
    //   582: istore_3
    //   583: aload_1
    //   584: getfield mStrictWidthLimit : Z
    //   587: ifeq -> 617
    //   590: iload #9
    //   592: iflt -> 601
    //   595: iconst_1
    //   596: istore #11
    //   598: goto -> 604
    //   601: iconst_0
    //   602: istore #11
    //   604: iload #18
    //   606: iload #11
    //   608: iand
    //   609: istore #18
    //   611: iload_3
    //   612: istore #11
    //   614: goto -> 646
    //   617: iload #9
    //   619: iload_3
    //   620: iadd
    //   621: ifle -> 630
    //   624: iconst_1
    //   625: istore #11
    //   627: goto -> 633
    //   630: iconst_0
    //   631: istore #11
    //   633: iload #18
    //   635: iload #11
    //   637: iand
    //   638: istore #18
    //   640: iload_3
    //   641: istore #11
    //   643: goto -> 646
    //   646: iload #18
    //   648: ifeq -> 670
    //   651: iload #16
    //   653: ifeq -> 670
    //   656: aload #12
    //   658: iload #16
    //   660: iconst_1
    //   661: invokevirtual put : (IZ)V
    //   664: iload #4
    //   666: istore_3
    //   667: goto -> 750
    //   670: iload #4
    //   672: istore_3
    //   673: iload #17
    //   675: ifeq -> 750
    //   678: aload #12
    //   680: iload #16
    //   682: iconst_0
    //   683: invokevirtual put : (IZ)V
    //   686: iconst_0
    //   687: istore #19
    //   689: iload #4
    //   691: istore_3
    //   692: iload #19
    //   694: iload #13
    //   696: if_icmpge -> 750
    //   699: aload_2
    //   700: iload #19
    //   702: invokevirtual get : (I)Ljava/lang/Object;
    //   705: checkcast android/support/v7/view/menu/MenuItemImpl
    //   708: astore_1
    //   709: iload #4
    //   711: istore_3
    //   712: aload_1
    //   713: invokevirtual getGroupId : ()I
    //   716: iload #16
    //   718: if_icmpne -> 741
    //   721: iload #4
    //   723: istore_3
    //   724: aload_1
    //   725: invokevirtual isActionButton : ()Z
    //   728: ifeq -> 736
    //   731: iload #4
    //   733: iconst_1
    //   734: iadd
    //   735: istore_3
    //   736: aload_1
    //   737: iconst_0
    //   738: invokevirtual setIsActionButton : (Z)V
    //   741: iinc #19, 1
    //   744: iload_3
    //   745: istore #4
    //   747: goto -> 689
    //   750: iload_3
    //   751: istore #4
    //   753: iload #18
    //   755: ifeq -> 763
    //   758: iload_3
    //   759: iconst_1
    //   760: isub
    //   761: istore #4
    //   763: aload #14
    //   765: iload #18
    //   767: invokevirtual setIsActionButton : (Z)V
    //   770: goto -> 410
    //   773: aload #14
    //   775: iconst_0
    //   776: invokevirtual setIsActionButton : (Z)V
    //   779: iinc #13, 1
    //   782: goto -> 270
    //   785: iconst_1
    //   786: ireturn
  }
  
  public View getItemView(MenuItemImpl paramMenuItemImpl, View paramView, ViewGroup paramViewGroup) {
    boolean bool;
    View view = paramMenuItemImpl.getActionView();
    if (view == null || paramMenuItemImpl.hasCollapsibleActionView())
      view = super.getItemView(paramMenuItemImpl, paramView, paramViewGroup); 
    if (paramMenuItemImpl.isActionViewExpanded()) {
      bool = true;
    } else {
      bool = false;
    } 
    view.setVisibility(bool);
    ActionMenuView actionMenuView = (ActionMenuView)paramViewGroup;
    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
    if (!actionMenuView.checkLayoutParams(layoutParams))
      view.setLayoutParams((ViewGroup.LayoutParams)actionMenuView.generateLayoutParams(layoutParams)); 
    return view;
  }
  
  public MenuView getMenuView(ViewGroup paramViewGroup) {
    MenuView menuView2 = this.mMenuView;
    MenuView menuView1 = super.getMenuView(paramViewGroup);
    if (menuView2 != menuView1)
      ((ActionMenuView)menuView1).setPresenter(this); 
    return menuView1;
  }
  
  public Drawable getOverflowIcon() {
    return (this.mOverflowButton != null) ? this.mOverflowButton.getDrawable() : (this.mPendingOverflowIconSet ? this.mPendingOverflowIcon : null);
  }
  
  public boolean hideOverflowMenu() {
    if (this.mPostedOpenRunnable != null && this.mMenuView != null) {
      ((View)this.mMenuView).removeCallbacks(this.mPostedOpenRunnable);
      this.mPostedOpenRunnable = null;
      return true;
    } 
    OverflowPopup overflowPopup = this.mOverflowPopup;
    if (overflowPopup != null) {
      overflowPopup.dismiss();
      return true;
    } 
    return false;
  }
  
  public boolean hideSubMenus() {
    if (this.mActionButtonPopup != null) {
      this.mActionButtonPopup.dismiss();
      return true;
    } 
    return false;
  }
  
  public void initForMenu(@NonNull Context paramContext, @Nullable MenuBuilder paramMenuBuilder) {
    super.initForMenu(paramContext, paramMenuBuilder);
    Resources resources = paramContext.getResources();
    ActionBarPolicy actionBarPolicy = ActionBarPolicy.get(paramContext);
    if (!this.mReserveOverflowSet)
      this.mReserveOverflow = actionBarPolicy.showsOverflowMenuButton(); 
    if (!this.mWidthLimitSet)
      this.mWidthLimit = actionBarPolicy.getEmbeddedMenuWidthLimit(); 
    if (!this.mMaxItemsSet)
      this.mMaxItems = actionBarPolicy.getMaxActionButtons(); 
    int i = this.mWidthLimit;
    if (this.mReserveOverflow) {
      if (this.mOverflowButton == null) {
        this.mOverflowButton = new OverflowMenuButton(this.mSystemContext);
        if (this.mPendingOverflowIconSet) {
          this.mOverflowButton.setImageDrawable(this.mPendingOverflowIcon);
          this.mPendingOverflowIcon = null;
          this.mPendingOverflowIconSet = false;
        } 
        int j = View.MeasureSpec.makeMeasureSpec(0, 0);
        this.mOverflowButton.measure(j, j);
      } 
      i -= this.mOverflowButton.getMeasuredWidth();
    } else {
      this.mOverflowButton = null;
    } 
    this.mActionItemWidthLimit = i;
    this.mMinCellSize = (int)((resources.getDisplayMetrics()).density * 56.0F);
    this.mScrapActionButtonView = null;
  }
  
  public boolean isOverflowMenuShowPending() {
    return (this.mPostedOpenRunnable != null || isOverflowMenuShowing());
  }
  
  public boolean isOverflowMenuShowing() {
    boolean bool;
    if (this.mOverflowPopup != null && this.mOverflowPopup.isShowing()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isOverflowReserved() {
    return this.mReserveOverflow;
  }
  
  public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean) {
    dismissPopupMenus();
    super.onCloseMenu(paramMenuBuilder, paramBoolean);
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    if (!this.mMaxItemsSet)
      this.mMaxItems = ActionBarPolicy.get(this.mContext).getMaxActionButtons(); 
    if (this.mMenu != null)
      this.mMenu.onItemsChanged(true); 
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState))
      return; 
    paramParcelable = paramParcelable;
    if (((SavedState)paramParcelable).openSubMenuId > 0) {
      MenuItem menuItem = this.mMenu.findItem(((SavedState)paramParcelable).openSubMenuId);
      if (menuItem != null)
        onSubMenuSelected((SubMenuBuilder)menuItem.getSubMenu()); 
    } 
  }
  
  public Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState();
    savedState.openSubMenuId = this.mOpenSubMenuId;
    return savedState;
  }
  
  public boolean onSubMenuSelected(SubMenuBuilder paramSubMenuBuilder) {
    boolean bool = paramSubMenuBuilder.hasVisibleItems();
    boolean bool1 = false;
    if (!bool)
      return false; 
    SubMenuBuilder subMenuBuilder;
    for (subMenuBuilder = paramSubMenuBuilder; subMenuBuilder.getParentMenu() != this.mMenu; subMenuBuilder = (SubMenuBuilder)subMenuBuilder.getParentMenu());
    View view = findViewForItem(subMenuBuilder.getItem());
    if (view == null)
      return false; 
    this.mOpenSubMenuId = paramSubMenuBuilder.getItem().getItemId();
    int i = paramSubMenuBuilder.size();
    byte b = 0;
    while (true) {
      bool = bool1;
      if (b < i) {
        MenuItem menuItem = paramSubMenuBuilder.getItem(b);
        if (menuItem.isVisible() && menuItem.getIcon() != null) {
          bool = true;
          break;
        } 
        b++;
        continue;
      } 
      break;
    } 
    this.mActionButtonPopup = new ActionButtonSubmenu(this.mContext, paramSubMenuBuilder, view);
    this.mActionButtonPopup.setForceShowIcon(bool);
    this.mActionButtonPopup.show();
    super.onSubMenuSelected(paramSubMenuBuilder);
    return true;
  }
  
  public void onSubUiVisibilityChanged(boolean paramBoolean) {
    if (paramBoolean) {
      super.onSubMenuSelected(null);
    } else if (this.mMenu != null) {
      this.mMenu.close(false);
    } 
  }
  
  public void setExpandedActionViewsExclusive(boolean paramBoolean) {
    this.mExpandedActionViewsExclusive = paramBoolean;
  }
  
  public void setItemLimit(int paramInt) {
    this.mMaxItems = paramInt;
    this.mMaxItemsSet = true;
  }
  
  public void setMenuView(ActionMenuView paramActionMenuView) {
    this.mMenuView = paramActionMenuView;
    paramActionMenuView.initialize(this.mMenu);
  }
  
  public void setOverflowIcon(Drawable paramDrawable) {
    if (this.mOverflowButton != null) {
      this.mOverflowButton.setImageDrawable(paramDrawable);
    } else {
      this.mPendingOverflowIconSet = true;
      this.mPendingOverflowIcon = paramDrawable;
    } 
  }
  
  public void setReserveOverflow(boolean paramBoolean) {
    this.mReserveOverflow = paramBoolean;
    this.mReserveOverflowSet = true;
  }
  
  public void setWidthLimit(int paramInt, boolean paramBoolean) {
    this.mWidthLimit = paramInt;
    this.mStrictWidthLimit = paramBoolean;
    this.mWidthLimitSet = true;
  }
  
  public boolean shouldIncludeItem(int paramInt, MenuItemImpl paramMenuItemImpl) {
    return paramMenuItemImpl.isActionButton();
  }
  
  public boolean showOverflowMenu() {
    if (this.mReserveOverflow && !isOverflowMenuShowing() && this.mMenu != null && this.mMenuView != null && this.mPostedOpenRunnable == null && !this.mMenu.getNonActionItems().isEmpty()) {
      this.mPostedOpenRunnable = new OpenOverflowRunnable(new OverflowPopup(this.mContext, this.mMenu, (View)this.mOverflowButton, true));
      ((View)this.mMenuView).post(this.mPostedOpenRunnable);
      super.onSubMenuSelected(null);
      return true;
    } 
    return false;
  }
  
  public void updateMenuView(boolean paramBoolean) {
    super.updateMenuView(paramBoolean);
    ((View)this.mMenuView).requestLayout();
    MenuBuilder<MenuItemImpl> menuBuilder = this.mMenu;
    byte b = 0;
    if (menuBuilder != null) {
      ArrayList<MenuItemImpl> arrayList = this.mMenu.getActionItems();
      int j = arrayList.size();
      for (byte b1 = 0; b1 < j; b1++) {
        ActionProvider actionProvider = ((MenuItemImpl)arrayList.get(b1)).getSupportActionProvider();
        if (actionProvider != null)
          actionProvider.setSubUiVisibilityListener(this); 
      } 
    } 
    if (this.mMenu != null) {
      ArrayList arrayList = this.mMenu.getNonActionItems();
    } else {
      menuBuilder = null;
    } 
    int i = b;
    if (this.mReserveOverflow) {
      i = b;
      if (menuBuilder != null) {
        int j = menuBuilder.size();
        if (j == 1) {
          i = ((MenuItemImpl)menuBuilder.get(0)).isActionViewExpanded() ^ true;
        } else {
          i = b;
          if (j > 0)
            i = 1; 
        } 
      } 
    } 
    if (i != 0) {
      if (this.mOverflowButton == null)
        this.mOverflowButton = new OverflowMenuButton(this.mSystemContext); 
      ViewGroup viewGroup = (ViewGroup)this.mOverflowButton.getParent();
      if (viewGroup != this.mMenuView) {
        if (viewGroup != null)
          viewGroup.removeView((View)this.mOverflowButton); 
        viewGroup = (ActionMenuView)this.mMenuView;
        viewGroup.addView((View)this.mOverflowButton, (ViewGroup.LayoutParams)viewGroup.generateOverflowButtonLayoutParams());
      } 
    } else if (this.mOverflowButton != null && this.mOverflowButton.getParent() == this.mMenuView) {
      ((ViewGroup)this.mMenuView).removeView((View)this.mOverflowButton);
    } 
    ((ActionMenuView)this.mMenuView).setOverflowReserved(this.mReserveOverflow);
  }
  
  private class ActionButtonSubmenu extends MenuPopupHelper {
    public ActionButtonSubmenu(Context param1Context, SubMenuBuilder param1SubMenuBuilder, View param1View) {
      super(param1Context, (MenuBuilder)param1SubMenuBuilder, param1View, false, R.attr.actionOverflowMenuStyle);
      if (!((MenuItemImpl)param1SubMenuBuilder.getItem()).isActionButton()) {
        ActionMenuPresenter.OverflowMenuButton overflowMenuButton;
        if (ActionMenuPresenter.this.mOverflowButton == null) {
          View view = (View)ActionMenuPresenter.this.mMenuView;
        } else {
          overflowMenuButton = ActionMenuPresenter.this.mOverflowButton;
        } 
        setAnchorView((View)overflowMenuButton);
      } 
      setPresenterCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
    }
    
    protected void onDismiss() {
      ActionMenuPresenter.this.mActionButtonPopup = null;
      ActionMenuPresenter.this.mOpenSubMenuId = 0;
      super.onDismiss();
    }
  }
  
  private class ActionMenuPopupCallback extends ActionMenuItemView.PopupCallback {
    public ShowableListMenu getPopup() {
      ShowableListMenu showableListMenu;
      if (ActionMenuPresenter.this.mActionButtonPopup != null) {
        showableListMenu = (ShowableListMenu)ActionMenuPresenter.this.mActionButtonPopup.getPopup();
      } else {
        showableListMenu = null;
      } 
      return showableListMenu;
    }
  }
  
  private class OpenOverflowRunnable implements Runnable {
    private ActionMenuPresenter.OverflowPopup mPopup;
    
    public OpenOverflowRunnable(ActionMenuPresenter.OverflowPopup param1OverflowPopup) {
      this.mPopup = param1OverflowPopup;
    }
    
    public void run() {
      if (ActionMenuPresenter.this.mMenu != null)
        ActionMenuPresenter.this.mMenu.changeMenuMode(); 
      View view = (View)ActionMenuPresenter.this.mMenuView;
      if (view != null && view.getWindowToken() != null && this.mPopup.tryShow())
        ActionMenuPresenter.this.mOverflowPopup = this.mPopup; 
      ActionMenuPresenter.this.mPostedOpenRunnable = null;
    }
  }
  
  private class OverflowMenuButton extends AppCompatImageView implements ActionMenuView.ActionMenuChildView {
    private final float[] mTempPts = new float[2];
    
    public OverflowMenuButton(Context param1Context) {
      super(param1Context, (AttributeSet)null, R.attr.actionOverflowButtonStyle);
      setClickable(true);
      setFocusable(true);
      setVisibility(0);
      setEnabled(true);
      TooltipCompat.setTooltipText((View)this, getContentDescription());
      setOnTouchListener(new ForwardingListener((View)this) {
            public ShowableListMenu getPopup() {
              return (ShowableListMenu)((ActionMenuPresenter.this.mOverflowPopup == null) ? null : ActionMenuPresenter.this.mOverflowPopup.getPopup());
            }
            
            public boolean onForwardingStarted() {
              ActionMenuPresenter.this.showOverflowMenu();
              return true;
            }
            
            public boolean onForwardingStopped() {
              if (ActionMenuPresenter.this.mPostedOpenRunnable != null)
                return false; 
              ActionMenuPresenter.this.hideOverflowMenu();
              return true;
            }
          });
    }
    
    public boolean needsDividerAfter() {
      return false;
    }
    
    public boolean needsDividerBefore() {
      return false;
    }
    
    public boolean performClick() {
      if (super.performClick())
        return true; 
      playSoundEffect(0);
      ActionMenuPresenter.this.showOverflowMenu();
      return true;
    }
    
    protected boolean setFrame(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      boolean bool = super.setFrame(param1Int1, param1Int2, param1Int3, param1Int4);
      Drawable drawable1 = getDrawable();
      Drawable drawable2 = getBackground();
      if (drawable1 != null && drawable2 != null) {
        int i = getWidth();
        param1Int2 = getHeight();
        param1Int1 = Math.max(i, param1Int2) / 2;
        int j = getPaddingLeft();
        int k = getPaddingRight();
        param1Int3 = getPaddingTop();
        param1Int4 = getPaddingBottom();
        j = (i + j - k) / 2;
        param1Int2 = (param1Int2 + param1Int3 - param1Int4) / 2;
        DrawableCompat.setHotspotBounds(drawable2, j - param1Int1, param1Int2 - param1Int1, j + param1Int1, param1Int2 + param1Int1);
      } 
      return bool;
    }
  }
  
  class null extends ForwardingListener {
    null(View param1View) {
      super(param1View);
    }
    
    public ShowableListMenu getPopup() {
      return (ShowableListMenu)((ActionMenuPresenter.this.mOverflowPopup == null) ? null : ActionMenuPresenter.this.mOverflowPopup.getPopup());
    }
    
    public boolean onForwardingStarted() {
      ActionMenuPresenter.this.showOverflowMenu();
      return true;
    }
    
    public boolean onForwardingStopped() {
      if (ActionMenuPresenter.this.mPostedOpenRunnable != null)
        return false; 
      ActionMenuPresenter.this.hideOverflowMenu();
      return true;
    }
  }
  
  private class OverflowPopup extends MenuPopupHelper {
    public OverflowPopup(Context param1Context, MenuBuilder param1MenuBuilder, View param1View, boolean param1Boolean) {
      super(param1Context, param1MenuBuilder, param1View, param1Boolean, R.attr.actionOverflowMenuStyle);
      setGravity(8388613);
      setPresenterCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
    }
    
    protected void onDismiss() {
      if (ActionMenuPresenter.this.mMenu != null)
        ActionMenuPresenter.this.mMenu.close(); 
      ActionMenuPresenter.this.mOverflowPopup = null;
      super.onDismiss();
    }
  }
  
  private class PopupPresenterCallback implements MenuPresenter.Callback {
    public void onCloseMenu(MenuBuilder param1MenuBuilder, boolean param1Boolean) {
      if (param1MenuBuilder instanceof SubMenuBuilder)
        param1MenuBuilder.getRootMenu().close(false); 
      MenuPresenter.Callback callback = ActionMenuPresenter.this.getCallback();
      if (callback != null)
        callback.onCloseMenu(param1MenuBuilder, param1Boolean); 
    }
    
    public boolean onOpenSubMenu(MenuBuilder param1MenuBuilder) {
      boolean bool = false;
      if (param1MenuBuilder == null)
        return false; 
      ActionMenuPresenter.this.mOpenSubMenuId = ((SubMenuBuilder)param1MenuBuilder).getItem().getItemId();
      MenuPresenter.Callback callback = ActionMenuPresenter.this.getCallback();
      if (callback != null)
        bool = callback.onOpenSubMenu(param1MenuBuilder); 
      return bool;
    }
  }
  
  private static class SavedState implements Parcelable {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
        public ActionMenuPresenter.SavedState createFromParcel(Parcel param2Parcel) {
          return new ActionMenuPresenter.SavedState(param2Parcel);
        }
        
        public ActionMenuPresenter.SavedState[] newArray(int param2Int) {
          return new ActionMenuPresenter.SavedState[param2Int];
        }
      };
    
    public int openSubMenuId;
    
    SavedState() {}
    
    SavedState(Parcel param1Parcel) {
      this.openSubMenuId = param1Parcel.readInt();
    }
    
    public int describeContents() {
      return 0;
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      param1Parcel.writeInt(this.openSubMenuId);
    }
  }
  
  static final class null implements Parcelable.Creator<SavedState> {
    public ActionMenuPresenter.SavedState createFromParcel(Parcel param1Parcel) {
      return new ActionMenuPresenter.SavedState(param1Parcel);
    }
    
    public ActionMenuPresenter.SavedState[] newArray(int param1Int) {
      return new ActionMenuPresenter.SavedState[param1Int];
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v7\widget\ActionMenuPresenter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */