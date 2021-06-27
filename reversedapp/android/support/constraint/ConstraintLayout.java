package android.support.constraint;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.constraint.solver.Metrics;
import android.support.constraint.solver.widgets.Analyzer;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.support.constraint.solver.widgets.ConstraintWidgetContainer;
import android.support.constraint.solver.widgets.Guideline;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.HashMap;

public class ConstraintLayout extends ViewGroup {
  static final boolean ALLOWS_EMBEDDED = false;
  
  private static final boolean CACHE_MEASURED_DIMENSION = false;
  
  private static final boolean DEBUG = false;
  
  public static final int DESIGN_INFO_ID = 0;
  
  private static final String TAG = "ConstraintLayout";
  
  private static final boolean USE_CONSTRAINTS_HELPER = true;
  
  public static final String VERSION = "ConstraintLayout-1.1.3";
  
  SparseArray<View> mChildrenByIds = new SparseArray();
  
  private ArrayList<ConstraintHelper> mConstraintHelpers = new ArrayList<ConstraintHelper>(4);
  
  private ConstraintSet mConstraintSet = null;
  
  private int mConstraintSetId = -1;
  
  private HashMap<String, Integer> mDesignIds = new HashMap<String, Integer>();
  
  private boolean mDirtyHierarchy = true;
  
  private int mLastMeasureHeight = -1;
  
  int mLastMeasureHeightMode = 0;
  
  int mLastMeasureHeightSize = -1;
  
  private int mLastMeasureWidth = -1;
  
  int mLastMeasureWidthMode = 0;
  
  int mLastMeasureWidthSize = -1;
  
  ConstraintWidgetContainer mLayoutWidget = new ConstraintWidgetContainer();
  
  private int mMaxHeight = Integer.MAX_VALUE;
  
  private int mMaxWidth = Integer.MAX_VALUE;
  
  private Metrics mMetrics;
  
  private int mMinHeight = 0;
  
  private int mMinWidth = 0;
  
  private int mOptimizationLevel = 7;
  
  private final ArrayList<ConstraintWidget> mVariableDimensionsWidgets = new ArrayList<ConstraintWidget>(100);
  
  public ConstraintLayout(Context paramContext) {
    super(paramContext);
    init((AttributeSet)null);
  }
  
  public ConstraintLayout(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramAttributeSet);
  }
  
  public ConstraintLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramAttributeSet);
  }
  
  private final ConstraintWidget getTargetWidget(int paramInt) {
    ConstraintWidget constraintWidget;
    if (paramInt == 0)
      return (ConstraintWidget)this.mLayoutWidget; 
    View view1 = (View)this.mChildrenByIds.get(paramInt);
    View view2 = view1;
    if (view1 == null) {
      view1 = findViewById(paramInt);
      view2 = view1;
      if (view1 != null) {
        view2 = view1;
        if (view1 != this) {
          view2 = view1;
          if (view1.getParent() == this) {
            onViewAdded(view1);
            view2 = view1;
          } 
        } 
      } 
    } 
    if (view2 == this)
      return (ConstraintWidget)this.mLayoutWidget; 
    if (view2 == null) {
      view2 = null;
    } else {
      constraintWidget = ((LayoutParams)view2.getLayoutParams()).widget;
    } 
    return constraintWidget;
  }
  
  private void init(AttributeSet paramAttributeSet) {
    this.mLayoutWidget.setCompanionWidget(this);
    this.mChildrenByIds.put(getId(), this);
    this.mConstraintSet = null;
    if (paramAttributeSet != null) {
      TypedArray typedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.ConstraintLayout_Layout);
      int i = typedArray.getIndexCount();
      for (byte b = 0; b < i; b++) {
        int j = typedArray.getIndex(b);
        if (j == R.styleable.ConstraintLayout_Layout_android_minWidth) {
          this.mMinWidth = typedArray.getDimensionPixelOffset(j, this.mMinWidth);
        } else if (j == R.styleable.ConstraintLayout_Layout_android_minHeight) {
          this.mMinHeight = typedArray.getDimensionPixelOffset(j, this.mMinHeight);
        } else if (j == R.styleable.ConstraintLayout_Layout_android_maxWidth) {
          this.mMaxWidth = typedArray.getDimensionPixelOffset(j, this.mMaxWidth);
        } else if (j == R.styleable.ConstraintLayout_Layout_android_maxHeight) {
          this.mMaxHeight = typedArray.getDimensionPixelOffset(j, this.mMaxHeight);
        } else if (j == R.styleable.ConstraintLayout_Layout_layout_optimizationLevel) {
          this.mOptimizationLevel = typedArray.getInt(j, this.mOptimizationLevel);
        } else if (j == R.styleable.ConstraintLayout_Layout_constraintSet) {
          j = typedArray.getResourceId(j, 0);
          try {
            ConstraintSet constraintSet = new ConstraintSet();
            this();
            this.mConstraintSet = constraintSet;
            this.mConstraintSet.load(getContext(), j);
          } catch (android.content.res.Resources.NotFoundException notFoundException) {
            this.mConstraintSet = null;
          } 
          this.mConstraintSetId = j;
        } 
      } 
      typedArray.recycle();
    } 
    this.mLayoutWidget.setOptimizationLevel(this.mOptimizationLevel);
  }
  
  private void internalMeasureChildren(int paramInt1, int paramInt2) {
    int i = getPaddingTop() + getPaddingBottom();
    int j = getPaddingLeft() + getPaddingRight();
    int k = getChildCount();
    for (byte b = 0; b < k; b++) {
      View view = getChildAt(b);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        ConstraintWidget constraintWidget = layoutParams.widget;
        if (!layoutParams.isGuideline && !layoutParams.isHelper) {
          int i1;
          int i2;
          int i3;
          boolean bool;
          constraintWidget.setVisibility(view.getVisibility());
          int m = layoutParams.width;
          int n = layoutParams.height;
          if (layoutParams.horizontalDimensionFixed || layoutParams.verticalDimensionFixed || (!layoutParams.horizontalDimensionFixed && layoutParams.matchConstraintDefaultWidth == 1) || layoutParams.width == -1 || (!layoutParams.verticalDimensionFixed && (layoutParams.matchConstraintDefaultHeight == 1 || layoutParams.height == -1))) {
            i1 = 1;
          } else {
            i1 = 0;
          } 
          if (i1) {
            boolean bool1;
            if (m == 0) {
              i2 = getChildMeasureSpec(paramInt1, j, -2);
              i1 = 1;
            } else if (m == -1) {
              i2 = getChildMeasureSpec(paramInt1, j, -1);
              i1 = 0;
            } else {
              if (m == -2) {
                i1 = 1;
              } else {
                i1 = 0;
              } 
              i2 = getChildMeasureSpec(paramInt1, j, m);
            } 
            if (n == 0) {
              i3 = getChildMeasureSpec(paramInt2, i, -2);
              bool = true;
            } else if (n == -1) {
              i3 = getChildMeasureSpec(paramInt2, i, -1);
              bool = false;
            } else {
              if (n == -2) {
                bool = true;
              } else {
                bool = false;
              } 
              i3 = getChildMeasureSpec(paramInt2, i, n);
            } 
            view.measure(i2, i3);
            if (this.mMetrics != null) {
              Metrics metrics = this.mMetrics;
              metrics.measures++;
            } 
            if (m == -2) {
              bool1 = true;
            } else {
              bool1 = false;
            } 
            constraintWidget.setWidthWrapContent(bool1);
            if (n == -2) {
              bool1 = true;
            } else {
              bool1 = false;
            } 
            constraintWidget.setHeightWrapContent(bool1);
            i2 = view.getMeasuredWidth();
            i3 = view.getMeasuredHeight();
          } else {
            i1 = 0;
            bool = false;
            i3 = n;
            i2 = m;
          } 
          constraintWidget.setWidth(i2);
          constraintWidget.setHeight(i3);
          if (i1)
            constraintWidget.setWrapWidth(i2); 
          if (bool)
            constraintWidget.setWrapHeight(i3); 
          if (layoutParams.needsBaseline) {
            i1 = view.getBaseline();
            if (i1 != -1)
              constraintWidget.setBaselineDistance(i1); 
          } 
        } 
      } 
    } 
  }
  
  private void internalMeasureDimensions(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getPaddingTop : ()I
    //   4: aload_0
    //   5: invokevirtual getPaddingBottom : ()I
    //   8: iadd
    //   9: istore_3
    //   10: aload_0
    //   11: invokevirtual getPaddingLeft : ()I
    //   14: aload_0
    //   15: invokevirtual getPaddingRight : ()I
    //   18: iadd
    //   19: istore #4
    //   21: aload_0
    //   22: invokevirtual getChildCount : ()I
    //   25: istore #5
    //   27: iconst_0
    //   28: istore #6
    //   30: lconst_1
    //   31: lstore #7
    //   33: iload #6
    //   35: iload #5
    //   37: if_icmpge -> 405
    //   40: aload_0
    //   41: iload #6
    //   43: invokevirtual getChildAt : (I)Landroid/view/View;
    //   46: astore #9
    //   48: aload #9
    //   50: invokevirtual getVisibility : ()I
    //   53: bipush #8
    //   55: if_icmpne -> 61
    //   58: goto -> 399
    //   61: aload #9
    //   63: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   66: checkcast android/support/constraint/ConstraintLayout$LayoutParams
    //   69: astore #10
    //   71: aload #10
    //   73: getfield widget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   76: astore #11
    //   78: aload #10
    //   80: getfield isGuideline : Z
    //   83: ifne -> 58
    //   86: aload #10
    //   88: getfield isHelper : Z
    //   91: ifeq -> 97
    //   94: goto -> 58
    //   97: aload #11
    //   99: aload #9
    //   101: invokevirtual getVisibility : ()I
    //   104: invokevirtual setVisibility : (I)V
    //   107: aload #10
    //   109: getfield width : I
    //   112: istore #12
    //   114: aload #10
    //   116: getfield height : I
    //   119: istore #13
    //   121: iload #12
    //   123: ifeq -> 383
    //   126: iload #13
    //   128: ifne -> 134
    //   131: goto -> 383
    //   134: iload #12
    //   136: bipush #-2
    //   138: if_icmpne -> 147
    //   141: iconst_1
    //   142: istore #14
    //   144: goto -> 150
    //   147: iconst_0
    //   148: istore #14
    //   150: iload_1
    //   151: iload #4
    //   153: iload #12
    //   155: invokestatic getChildMeasureSpec : (III)I
    //   158: istore #15
    //   160: iload #13
    //   162: bipush #-2
    //   164: if_icmpne -> 173
    //   167: iconst_1
    //   168: istore #16
    //   170: goto -> 176
    //   173: iconst_0
    //   174: istore #16
    //   176: aload #9
    //   178: iload #15
    //   180: iload_2
    //   181: iload_3
    //   182: iload #13
    //   184: invokestatic getChildMeasureSpec : (III)I
    //   187: invokevirtual measure : (II)V
    //   190: aload_0
    //   191: getfield mMetrics : Landroid/support/constraint/solver/Metrics;
    //   194: ifnull -> 218
    //   197: aload_0
    //   198: getfield mMetrics : Landroid/support/constraint/solver/Metrics;
    //   201: astore #17
    //   203: aload #17
    //   205: aload #17
    //   207: getfield measures : J
    //   210: lconst_1
    //   211: ladd
    //   212: putfield measures : J
    //   215: goto -> 218
    //   218: iload #12
    //   220: bipush #-2
    //   222: if_icmpne -> 231
    //   225: iconst_1
    //   226: istore #18
    //   228: goto -> 234
    //   231: iconst_0
    //   232: istore #18
    //   234: aload #11
    //   236: iload #18
    //   238: invokevirtual setWidthWrapContent : (Z)V
    //   241: iload #13
    //   243: bipush #-2
    //   245: if_icmpne -> 254
    //   248: iconst_1
    //   249: istore #18
    //   251: goto -> 257
    //   254: iconst_0
    //   255: istore #18
    //   257: aload #11
    //   259: iload #18
    //   261: invokevirtual setHeightWrapContent : (Z)V
    //   264: aload #9
    //   266: invokevirtual getMeasuredWidth : ()I
    //   269: istore #12
    //   271: aload #9
    //   273: invokevirtual getMeasuredHeight : ()I
    //   276: istore #13
    //   278: aload #11
    //   280: iload #12
    //   282: invokevirtual setWidth : (I)V
    //   285: aload #11
    //   287: iload #13
    //   289: invokevirtual setHeight : (I)V
    //   292: iload #14
    //   294: ifeq -> 304
    //   297: aload #11
    //   299: iload #12
    //   301: invokevirtual setWrapWidth : (I)V
    //   304: iload #16
    //   306: ifeq -> 316
    //   309: aload #11
    //   311: iload #13
    //   313: invokevirtual setWrapHeight : (I)V
    //   316: aload #10
    //   318: getfield needsBaseline : Z
    //   321: ifeq -> 344
    //   324: aload #9
    //   326: invokevirtual getBaseline : ()I
    //   329: istore #14
    //   331: iload #14
    //   333: iconst_m1
    //   334: if_icmpeq -> 344
    //   337: aload #11
    //   339: iload #14
    //   341: invokevirtual setBaselineDistance : (I)V
    //   344: aload #10
    //   346: getfield horizontalDimensionFixed : Z
    //   349: ifeq -> 399
    //   352: aload #10
    //   354: getfield verticalDimensionFixed : Z
    //   357: ifeq -> 399
    //   360: aload #11
    //   362: invokevirtual getResolutionWidth : ()Landroid/support/constraint/solver/widgets/ResolutionDimension;
    //   365: iload #12
    //   367: invokevirtual resolve : (I)V
    //   370: aload #11
    //   372: invokevirtual getResolutionHeight : ()Landroid/support/constraint/solver/widgets/ResolutionDimension;
    //   375: iload #13
    //   377: invokevirtual resolve : (I)V
    //   380: goto -> 399
    //   383: aload #11
    //   385: invokevirtual getResolutionWidth : ()Landroid/support/constraint/solver/widgets/ResolutionDimension;
    //   388: invokevirtual invalidate : ()V
    //   391: aload #11
    //   393: invokevirtual getResolutionHeight : ()Landroid/support/constraint/solver/widgets/ResolutionDimension;
    //   396: invokevirtual invalidate : ()V
    //   399: iinc #6, 1
    //   402: goto -> 30
    //   405: aload_0
    //   406: getfield mLayoutWidget : Landroid/support/constraint/solver/widgets/ConstraintWidgetContainer;
    //   409: invokevirtual solveGraph : ()V
    //   412: iconst_0
    //   413: istore #19
    //   415: iload #19
    //   417: iload #5
    //   419: if_icmpge -> 1285
    //   422: aload_0
    //   423: iload #19
    //   425: invokevirtual getChildAt : (I)Landroid/view/View;
    //   428: astore #17
    //   430: aload #17
    //   432: invokevirtual getVisibility : ()I
    //   435: bipush #8
    //   437: if_icmpne -> 443
    //   440: goto -> 1279
    //   443: aload #17
    //   445: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   448: checkcast android/support/constraint/ConstraintLayout$LayoutParams
    //   451: astore #10
    //   453: aload #10
    //   455: getfield widget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   458: astore #11
    //   460: aload #10
    //   462: getfield isGuideline : Z
    //   465: ifne -> 440
    //   468: aload #10
    //   470: getfield isHelper : Z
    //   473: ifeq -> 479
    //   476: goto -> 440
    //   479: aload #11
    //   481: aload #17
    //   483: invokevirtual getVisibility : ()I
    //   486: invokevirtual setVisibility : (I)V
    //   489: aload #10
    //   491: getfield width : I
    //   494: istore #13
    //   496: aload #10
    //   498: getfield height : I
    //   501: istore #15
    //   503: iload #13
    //   505: ifeq -> 516
    //   508: iload #15
    //   510: ifeq -> 516
    //   513: goto -> 440
    //   516: aload #11
    //   518: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   521: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   524: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   527: astore #20
    //   529: aload #11
    //   531: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   534: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   537: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   540: astore #21
    //   542: aload #11
    //   544: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   547: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   550: invokevirtual getTarget : ()Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   553: ifnull -> 576
    //   556: aload #11
    //   558: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   561: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   564: invokevirtual getTarget : ()Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   567: ifnull -> 576
    //   570: iconst_1
    //   571: istore #14
    //   573: goto -> 579
    //   576: iconst_0
    //   577: istore #14
    //   579: aload #11
    //   581: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   584: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   587: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   590: astore #9
    //   592: aload #11
    //   594: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   597: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   600: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   603: astore #22
    //   605: aload #11
    //   607: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   610: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   613: invokevirtual getTarget : ()Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   616: ifnull -> 639
    //   619: aload #11
    //   621: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   624: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   627: invokevirtual getTarget : ()Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   630: ifnull -> 639
    //   633: iconst_1
    //   634: istore #23
    //   636: goto -> 642
    //   639: iconst_0
    //   640: istore #23
    //   642: iload #13
    //   644: ifne -> 668
    //   647: iload #15
    //   649: ifne -> 668
    //   652: iload #14
    //   654: ifeq -> 668
    //   657: iload #23
    //   659: ifeq -> 668
    //   662: lconst_1
    //   663: lstore #7
    //   665: goto -> 1279
    //   668: aload_0
    //   669: getfield mLayoutWidget : Landroid/support/constraint/solver/widgets/ConstraintWidgetContainer;
    //   672: invokevirtual getHorizontalDimensionBehaviour : ()Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   675: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   678: if_acmpeq -> 687
    //   681: iconst_1
    //   682: istore #12
    //   684: goto -> 690
    //   687: iconst_0
    //   688: istore #12
    //   690: aload_0
    //   691: getfield mLayoutWidget : Landroid/support/constraint/solver/widgets/ConstraintWidgetContainer;
    //   694: invokevirtual getVerticalDimensionBehaviour : ()Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   697: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   700: if_acmpeq -> 709
    //   703: iconst_1
    //   704: istore #6
    //   706: goto -> 712
    //   709: iconst_0
    //   710: istore #6
    //   712: iload #12
    //   714: ifne -> 725
    //   717: aload #11
    //   719: invokevirtual getResolutionWidth : ()Landroid/support/constraint/solver/widgets/ResolutionDimension;
    //   722: invokevirtual invalidate : ()V
    //   725: iload #6
    //   727: ifne -> 738
    //   730: aload #11
    //   732: invokevirtual getResolutionHeight : ()Landroid/support/constraint/solver/widgets/ResolutionDimension;
    //   735: invokevirtual invalidate : ()V
    //   738: iload #13
    //   740: ifne -> 837
    //   743: iload #12
    //   745: ifeq -> 814
    //   748: aload #11
    //   750: invokevirtual isSpreadWidth : ()Z
    //   753: ifeq -> 814
    //   756: iload #14
    //   758: ifeq -> 814
    //   761: aload #20
    //   763: invokevirtual isResolved : ()Z
    //   766: ifeq -> 814
    //   769: aload #21
    //   771: invokevirtual isResolved : ()Z
    //   774: ifeq -> 814
    //   777: aload #21
    //   779: invokevirtual getResolvedValue : ()F
    //   782: aload #20
    //   784: invokevirtual getResolvedValue : ()F
    //   787: fsub
    //   788: f2i
    //   789: istore #13
    //   791: aload #11
    //   793: invokevirtual getResolutionWidth : ()Landroid/support/constraint/solver/widgets/ResolutionDimension;
    //   796: iload #13
    //   798: invokevirtual resolve : (I)V
    //   801: iload_1
    //   802: iload #4
    //   804: iload #13
    //   806: invokestatic getChildMeasureSpec : (III)I
    //   809: istore #16
    //   811: goto -> 852
    //   814: iload_1
    //   815: iload #4
    //   817: bipush #-2
    //   819: invokestatic getChildMeasureSpec : (III)I
    //   822: istore #16
    //   824: iconst_0
    //   825: istore #24
    //   827: iconst_1
    //   828: istore #14
    //   830: iload #13
    //   832: istore #25
    //   834: goto -> 900
    //   837: iload #13
    //   839: iconst_m1
    //   840: if_icmpne -> 866
    //   843: iload_1
    //   844: iload #4
    //   846: iconst_m1
    //   847: invokestatic getChildMeasureSpec : (III)I
    //   850: istore #16
    //   852: iconst_0
    //   853: istore #14
    //   855: iload #12
    //   857: istore #24
    //   859: iload #13
    //   861: istore #25
    //   863: goto -> 900
    //   866: iload #13
    //   868: bipush #-2
    //   870: if_icmpne -> 879
    //   873: iconst_1
    //   874: istore #14
    //   876: goto -> 882
    //   879: iconst_0
    //   880: istore #14
    //   882: iload_1
    //   883: iload #4
    //   885: iload #13
    //   887: invokestatic getChildMeasureSpec : (III)I
    //   890: istore #16
    //   892: iload #13
    //   894: istore #25
    //   896: iload #12
    //   898: istore #24
    //   900: iload #15
    //   902: ifne -> 993
    //   905: iload #6
    //   907: ifeq -> 975
    //   910: aload #11
    //   912: invokevirtual isSpreadHeight : ()Z
    //   915: ifeq -> 975
    //   918: iload #23
    //   920: ifeq -> 975
    //   923: aload #9
    //   925: invokevirtual isResolved : ()Z
    //   928: ifeq -> 975
    //   931: aload #22
    //   933: invokevirtual isResolved : ()Z
    //   936: ifeq -> 975
    //   939: aload #22
    //   941: invokevirtual getResolvedValue : ()F
    //   944: aload #9
    //   946: invokevirtual getResolvedValue : ()F
    //   949: fsub
    //   950: f2i
    //   951: istore #15
    //   953: aload #11
    //   955: invokevirtual getResolutionHeight : ()Landroid/support/constraint/solver/widgets/ResolutionDimension;
    //   958: iload #15
    //   960: invokevirtual resolve : (I)V
    //   963: iload_2
    //   964: iload_3
    //   965: iload #15
    //   967: invokestatic getChildMeasureSpec : (III)I
    //   970: istore #13
    //   972: goto -> 1011
    //   975: iload_2
    //   976: iload_3
    //   977: bipush #-2
    //   979: invokestatic getChildMeasureSpec : (III)I
    //   982: istore #13
    //   984: iconst_0
    //   985: istore #6
    //   987: iconst_1
    //   988: istore #12
    //   990: goto -> 1043
    //   993: iload_3
    //   994: istore #13
    //   996: iload #15
    //   998: iconst_m1
    //   999: if_icmpne -> 1017
    //   1002: iload_2
    //   1003: iload #13
    //   1005: iconst_m1
    //   1006: invokestatic getChildMeasureSpec : (III)I
    //   1009: istore #13
    //   1011: iconst_0
    //   1012: istore #12
    //   1014: goto -> 1043
    //   1017: iload #15
    //   1019: bipush #-2
    //   1021: if_icmpne -> 1030
    //   1024: iconst_1
    //   1025: istore #12
    //   1027: goto -> 1033
    //   1030: iconst_0
    //   1031: istore #12
    //   1033: iload_2
    //   1034: iload #13
    //   1036: iload #15
    //   1038: invokestatic getChildMeasureSpec : (III)I
    //   1041: istore #13
    //   1043: aload #17
    //   1045: iload #16
    //   1047: iload #13
    //   1049: invokevirtual measure : (II)V
    //   1052: aload_0
    //   1053: getfield mMetrics : Landroid/support/constraint/solver/Metrics;
    //   1056: ifnull -> 1080
    //   1059: aload_0
    //   1060: getfield mMetrics : Landroid/support/constraint/solver/Metrics;
    //   1063: astore #9
    //   1065: aload #9
    //   1067: aload #9
    //   1069: getfield measures : J
    //   1072: lconst_1
    //   1073: ladd
    //   1074: putfield measures : J
    //   1077: goto -> 1080
    //   1080: lconst_1
    //   1081: lstore #26
    //   1083: iload #25
    //   1085: bipush #-2
    //   1087: if_icmpne -> 1096
    //   1090: iconst_1
    //   1091: istore #18
    //   1093: goto -> 1099
    //   1096: iconst_0
    //   1097: istore #18
    //   1099: aload #11
    //   1101: iload #18
    //   1103: invokevirtual setWidthWrapContent : (Z)V
    //   1106: iload #15
    //   1108: bipush #-2
    //   1110: if_icmpne -> 1119
    //   1113: iconst_1
    //   1114: istore #18
    //   1116: goto -> 1122
    //   1119: iconst_0
    //   1120: istore #18
    //   1122: aload #11
    //   1124: iload #18
    //   1126: invokevirtual setHeightWrapContent : (Z)V
    //   1129: aload #17
    //   1131: invokevirtual getMeasuredWidth : ()I
    //   1134: istore #13
    //   1136: aload #17
    //   1138: invokevirtual getMeasuredHeight : ()I
    //   1141: istore #16
    //   1143: aload #11
    //   1145: iload #13
    //   1147: invokevirtual setWidth : (I)V
    //   1150: aload #11
    //   1152: iload #16
    //   1154: invokevirtual setHeight : (I)V
    //   1157: iload #14
    //   1159: ifeq -> 1169
    //   1162: aload #11
    //   1164: iload #13
    //   1166: invokevirtual setWrapWidth : (I)V
    //   1169: iload #12
    //   1171: ifeq -> 1181
    //   1174: aload #11
    //   1176: iload #16
    //   1178: invokevirtual setWrapHeight : (I)V
    //   1181: iload #24
    //   1183: ifeq -> 1199
    //   1186: aload #11
    //   1188: invokevirtual getResolutionWidth : ()Landroid/support/constraint/solver/widgets/ResolutionDimension;
    //   1191: iload #13
    //   1193: invokevirtual resolve : (I)V
    //   1196: goto -> 1207
    //   1199: aload #11
    //   1201: invokevirtual getResolutionWidth : ()Landroid/support/constraint/solver/widgets/ResolutionDimension;
    //   1204: invokevirtual remove : ()V
    //   1207: iload #6
    //   1209: ifeq -> 1225
    //   1212: aload #11
    //   1214: invokevirtual getResolutionHeight : ()Landroid/support/constraint/solver/widgets/ResolutionDimension;
    //   1217: iload #16
    //   1219: invokevirtual resolve : (I)V
    //   1222: goto -> 1236
    //   1225: aload #11
    //   1227: invokevirtual getResolutionHeight : ()Landroid/support/constraint/solver/widgets/ResolutionDimension;
    //   1230: invokevirtual remove : ()V
    //   1233: goto -> 1222
    //   1236: aload #10
    //   1238: getfield needsBaseline : Z
    //   1241: ifeq -> 1275
    //   1244: aload #17
    //   1246: invokevirtual getBaseline : ()I
    //   1249: istore #6
    //   1251: lload #26
    //   1253: lstore #7
    //   1255: iload #6
    //   1257: iconst_m1
    //   1258: if_icmpeq -> 1279
    //   1261: aload #11
    //   1263: iload #6
    //   1265: invokevirtual setBaselineDistance : (I)V
    //   1268: lload #26
    //   1270: lstore #7
    //   1272: goto -> 1279
    //   1275: lload #26
    //   1277: lstore #7
    //   1279: iinc #19, 1
    //   1282: goto -> 415
    //   1285: return
  }
  
  private void setChildrenConstraints() {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual isInEditMode : ()Z
    //   4: istore_1
    //   5: aload_0
    //   6: invokevirtual getChildCount : ()I
    //   9: istore_2
    //   10: iconst_0
    //   11: istore_3
    //   12: iload_1
    //   13: ifeq -> 112
    //   16: iconst_0
    //   17: istore #4
    //   19: iload #4
    //   21: iload_2
    //   22: if_icmpge -> 112
    //   25: aload_0
    //   26: iload #4
    //   28: invokevirtual getChildAt : (I)Landroid/view/View;
    //   31: astore #5
    //   33: aload_0
    //   34: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   37: aload #5
    //   39: invokevirtual getId : ()I
    //   42: invokevirtual getResourceName : (I)Ljava/lang/String;
    //   45: astore #6
    //   47: aload_0
    //   48: iconst_0
    //   49: aload #6
    //   51: aload #5
    //   53: invokevirtual getId : ()I
    //   56: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   59: invokevirtual setDesignInformation : (ILjava/lang/Object;Ljava/lang/Object;)V
    //   62: aload #6
    //   64: bipush #47
    //   66: invokevirtual indexOf : (I)I
    //   69: istore #7
    //   71: aload #6
    //   73: astore #8
    //   75: iload #7
    //   77: iconst_m1
    //   78: if_icmpeq -> 92
    //   81: aload #6
    //   83: iload #7
    //   85: iconst_1
    //   86: iadd
    //   87: invokevirtual substring : (I)Ljava/lang/String;
    //   90: astore #8
    //   92: aload_0
    //   93: aload #5
    //   95: invokevirtual getId : ()I
    //   98: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   101: aload #8
    //   103: invokevirtual setDebugName : (Ljava/lang/String;)V
    //   106: iinc #4, 1
    //   109: goto -> 19
    //   112: iconst_0
    //   113: istore #4
    //   115: iload #4
    //   117: iload_2
    //   118: if_icmpge -> 152
    //   121: aload_0
    //   122: aload_0
    //   123: iload #4
    //   125: invokevirtual getChildAt : (I)Landroid/view/View;
    //   128: invokevirtual getViewWidget : (Landroid/view/View;)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   131: astore #8
    //   133: aload #8
    //   135: ifnonnull -> 141
    //   138: goto -> 146
    //   141: aload #8
    //   143: invokevirtual reset : ()V
    //   146: iinc #4, 1
    //   149: goto -> 115
    //   152: aload_0
    //   153: getfield mConstraintSetId : I
    //   156: iconst_m1
    //   157: if_icmpeq -> 215
    //   160: iconst_0
    //   161: istore #4
    //   163: iload #4
    //   165: iload_2
    //   166: if_icmpge -> 215
    //   169: aload_0
    //   170: iload #4
    //   172: invokevirtual getChildAt : (I)Landroid/view/View;
    //   175: astore #8
    //   177: aload #8
    //   179: invokevirtual getId : ()I
    //   182: aload_0
    //   183: getfield mConstraintSetId : I
    //   186: if_icmpne -> 209
    //   189: aload #8
    //   191: instanceof android/support/constraint/Constraints
    //   194: ifeq -> 209
    //   197: aload_0
    //   198: aload #8
    //   200: checkcast android/support/constraint/Constraints
    //   203: invokevirtual getConstraintSet : ()Landroid/support/constraint/ConstraintSet;
    //   206: putfield mConstraintSet : Landroid/support/constraint/ConstraintSet;
    //   209: iinc #4, 1
    //   212: goto -> 163
    //   215: aload_0
    //   216: getfield mConstraintSet : Landroid/support/constraint/ConstraintSet;
    //   219: ifnull -> 230
    //   222: aload_0
    //   223: getfield mConstraintSet : Landroid/support/constraint/ConstraintSet;
    //   226: aload_0
    //   227: invokevirtual applyToInternal : (Landroid/support/constraint/ConstraintLayout;)V
    //   230: aload_0
    //   231: getfield mLayoutWidget : Landroid/support/constraint/solver/widgets/ConstraintWidgetContainer;
    //   234: invokevirtual removeAllChildren : ()V
    //   237: aload_0
    //   238: getfield mConstraintHelpers : Ljava/util/ArrayList;
    //   241: invokevirtual size : ()I
    //   244: istore #7
    //   246: iload #7
    //   248: ifle -> 283
    //   251: iconst_0
    //   252: istore #4
    //   254: iload #4
    //   256: iload #7
    //   258: if_icmpge -> 283
    //   261: aload_0
    //   262: getfield mConstraintHelpers : Ljava/util/ArrayList;
    //   265: iload #4
    //   267: invokevirtual get : (I)Ljava/lang/Object;
    //   270: checkcast android/support/constraint/ConstraintHelper
    //   273: aload_0
    //   274: invokevirtual updatePreLayout : (Landroid/support/constraint/ConstraintLayout;)V
    //   277: iinc #4, 1
    //   280: goto -> 254
    //   283: iconst_0
    //   284: istore #4
    //   286: iload #4
    //   288: iload_2
    //   289: if_icmpge -> 323
    //   292: aload_0
    //   293: iload #4
    //   295: invokevirtual getChildAt : (I)Landroid/view/View;
    //   298: astore #8
    //   300: aload #8
    //   302: instanceof android/support/constraint/Placeholder
    //   305: ifeq -> 317
    //   308: aload #8
    //   310: checkcast android/support/constraint/Placeholder
    //   313: aload_0
    //   314: invokevirtual updatePreLayout : (Landroid/support/constraint/ConstraintLayout;)V
    //   317: iinc #4, 1
    //   320: goto -> 286
    //   323: iconst_0
    //   324: istore #7
    //   326: iload_3
    //   327: istore #4
    //   329: iload #7
    //   331: iload_2
    //   332: if_icmpge -> 2047
    //   335: aload_0
    //   336: iload #7
    //   338: invokevirtual getChildAt : (I)Landroid/view/View;
    //   341: astore #5
    //   343: aload_0
    //   344: aload #5
    //   346: invokevirtual getViewWidget : (Landroid/view/View;)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   349: astore #6
    //   351: aload #6
    //   353: ifnonnull -> 363
    //   356: iload #4
    //   358: istore #9
    //   360: goto -> 2037
    //   363: aload #5
    //   365: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   368: checkcast android/support/constraint/ConstraintLayout$LayoutParams
    //   371: astore #8
    //   373: aload #8
    //   375: invokevirtual validate : ()V
    //   378: aload #8
    //   380: getfield helped : Z
    //   383: ifeq -> 396
    //   386: aload #8
    //   388: iload #4
    //   390: putfield helped : Z
    //   393: goto -> 461
    //   396: iload_1
    //   397: ifeq -> 461
    //   400: aload_0
    //   401: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   404: aload #5
    //   406: invokevirtual getId : ()I
    //   409: invokevirtual getResourceName : (I)Ljava/lang/String;
    //   412: astore #10
    //   414: aload_0
    //   415: iload #4
    //   417: aload #10
    //   419: aload #5
    //   421: invokevirtual getId : ()I
    //   424: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   427: invokevirtual setDesignInformation : (ILjava/lang/Object;Ljava/lang/Object;)V
    //   430: aload #10
    //   432: aload #10
    //   434: ldc_w 'id/'
    //   437: invokevirtual indexOf : (Ljava/lang/String;)I
    //   440: iconst_3
    //   441: iadd
    //   442: invokevirtual substring : (I)Ljava/lang/String;
    //   445: astore #10
    //   447: aload_0
    //   448: aload #5
    //   450: invokevirtual getId : ()I
    //   453: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   456: aload #10
    //   458: invokevirtual setDebugName : (Ljava/lang/String;)V
    //   461: aload #6
    //   463: aload #5
    //   465: invokevirtual getVisibility : ()I
    //   468: invokevirtual setVisibility : (I)V
    //   471: aload #8
    //   473: getfield isInPlaceholder : Z
    //   476: ifeq -> 486
    //   479: aload #6
    //   481: bipush #8
    //   483: invokevirtual setVisibility : (I)V
    //   486: aload #6
    //   488: aload #5
    //   490: invokevirtual setCompanionWidget : (Ljava/lang/Object;)V
    //   493: aload_0
    //   494: getfield mLayoutWidget : Landroid/support/constraint/solver/widgets/ConstraintWidgetContainer;
    //   497: aload #6
    //   499: invokevirtual add : (Landroid/support/constraint/solver/widgets/ConstraintWidget;)V
    //   502: aload #8
    //   504: getfield verticalDimensionFixed : Z
    //   507: ifeq -> 518
    //   510: aload #8
    //   512: getfield horizontalDimensionFixed : Z
    //   515: ifne -> 528
    //   518: aload_0
    //   519: getfield mVariableDimensionsWidgets : Ljava/util/ArrayList;
    //   522: aload #6
    //   524: invokevirtual add : (Ljava/lang/Object;)Z
    //   527: pop
    //   528: aload #8
    //   530: getfield isGuideline : Z
    //   533: ifeq -> 656
    //   536: aload #6
    //   538: checkcast android/support/constraint/solver/widgets/Guideline
    //   541: astore #6
    //   543: aload #8
    //   545: getfield resolvedGuideBegin : I
    //   548: istore #9
    //   550: aload #8
    //   552: getfield resolvedGuideEnd : I
    //   555: istore_3
    //   556: aload #8
    //   558: getfield resolvedGuidePercent : F
    //   561: fstore #11
    //   563: getstatic android/os/Build$VERSION.SDK_INT : I
    //   566: bipush #17
    //   568: if_icmpge -> 591
    //   571: aload #8
    //   573: getfield guideBegin : I
    //   576: istore #9
    //   578: aload #8
    //   580: getfield guideEnd : I
    //   583: istore_3
    //   584: aload #8
    //   586: getfield guidePercent : F
    //   589: fstore #11
    //   591: fload #11
    //   593: ldc_w -1.0
    //   596: fcmpl
    //   597: ifeq -> 614
    //   600: aload #6
    //   602: fload #11
    //   604: invokevirtual setGuidePercent : (F)V
    //   607: iload #4
    //   609: istore #9
    //   611: goto -> 2037
    //   614: iload #9
    //   616: iconst_m1
    //   617: if_icmpeq -> 634
    //   620: aload #6
    //   622: iload #9
    //   624: invokevirtual setGuideBegin : (I)V
    //   627: iload #4
    //   629: istore #9
    //   631: goto -> 2037
    //   634: iload #4
    //   636: istore #9
    //   638: iload_3
    //   639: iconst_m1
    //   640: if_icmpeq -> 2037
    //   643: aload #6
    //   645: iload_3
    //   646: invokevirtual setGuideEnd : (I)V
    //   649: iload #4
    //   651: istore #9
    //   653: goto -> 2037
    //   656: aload #8
    //   658: getfield leftToLeft : I
    //   661: iconst_m1
    //   662: if_icmpne -> 822
    //   665: aload #8
    //   667: getfield leftToRight : I
    //   670: iconst_m1
    //   671: if_icmpne -> 822
    //   674: aload #8
    //   676: getfield rightToLeft : I
    //   679: iconst_m1
    //   680: if_icmpne -> 822
    //   683: aload #8
    //   685: getfield rightToRight : I
    //   688: iconst_m1
    //   689: if_icmpne -> 822
    //   692: aload #8
    //   694: getfield startToStart : I
    //   697: iconst_m1
    //   698: if_icmpne -> 822
    //   701: aload #8
    //   703: getfield startToEnd : I
    //   706: iconst_m1
    //   707: if_icmpne -> 822
    //   710: aload #8
    //   712: getfield endToStart : I
    //   715: iconst_m1
    //   716: if_icmpne -> 822
    //   719: aload #8
    //   721: getfield endToEnd : I
    //   724: iconst_m1
    //   725: if_icmpne -> 822
    //   728: aload #8
    //   730: getfield topToTop : I
    //   733: iconst_m1
    //   734: if_icmpne -> 822
    //   737: aload #8
    //   739: getfield topToBottom : I
    //   742: iconst_m1
    //   743: if_icmpne -> 822
    //   746: aload #8
    //   748: getfield bottomToTop : I
    //   751: iconst_m1
    //   752: if_icmpne -> 822
    //   755: aload #8
    //   757: getfield bottomToBottom : I
    //   760: iconst_m1
    //   761: if_icmpne -> 822
    //   764: aload #8
    //   766: getfield baselineToBaseline : I
    //   769: iconst_m1
    //   770: if_icmpne -> 822
    //   773: aload #8
    //   775: getfield editorAbsoluteX : I
    //   778: iconst_m1
    //   779: if_icmpne -> 822
    //   782: aload #8
    //   784: getfield editorAbsoluteY : I
    //   787: iconst_m1
    //   788: if_icmpne -> 822
    //   791: aload #8
    //   793: getfield circleConstraint : I
    //   796: iconst_m1
    //   797: if_icmpne -> 822
    //   800: aload #8
    //   802: getfield width : I
    //   805: iconst_m1
    //   806: if_icmpeq -> 822
    //   809: iload #4
    //   811: istore #9
    //   813: aload #8
    //   815: getfield height : I
    //   818: iconst_m1
    //   819: if_icmpne -> 2037
    //   822: aload #8
    //   824: getfield resolvedLeftToLeft : I
    //   827: istore #12
    //   829: aload #8
    //   831: getfield resolvedLeftToRight : I
    //   834: istore #13
    //   836: aload #8
    //   838: getfield resolvedRightToLeft : I
    //   841: istore_3
    //   842: aload #8
    //   844: getfield resolvedRightToRight : I
    //   847: istore #4
    //   849: aload #8
    //   851: getfield resolveGoneLeftMargin : I
    //   854: istore #14
    //   856: aload #8
    //   858: getfield resolveGoneRightMargin : I
    //   861: istore #9
    //   863: aload #8
    //   865: getfield resolvedHorizontalBias : F
    //   868: fstore #11
    //   870: getstatic android/os/Build$VERSION.SDK_INT : I
    //   873: bipush #17
    //   875: if_icmpge -> 1093
    //   878: aload #8
    //   880: getfield leftToLeft : I
    //   883: istore #13
    //   885: aload #8
    //   887: getfield leftToRight : I
    //   890: istore #9
    //   892: aload #8
    //   894: getfield rightToLeft : I
    //   897: istore #12
    //   899: aload #8
    //   901: getfield rightToRight : I
    //   904: istore #15
    //   906: aload #8
    //   908: getfield goneLeftMargin : I
    //   911: istore #14
    //   913: aload #8
    //   915: getfield goneRightMargin : I
    //   918: istore #16
    //   920: aload #8
    //   922: getfield horizontalBias : F
    //   925: fstore #11
    //   927: iload #13
    //   929: istore_3
    //   930: iload #9
    //   932: istore #4
    //   934: iload #13
    //   936: iconst_m1
    //   937: if_icmpne -> 1001
    //   940: iload #13
    //   942: istore_3
    //   943: iload #9
    //   945: istore #4
    //   947: iload #9
    //   949: iconst_m1
    //   950: if_icmpne -> 1001
    //   953: aload #8
    //   955: getfield startToStart : I
    //   958: iconst_m1
    //   959: if_icmpeq -> 975
    //   962: aload #8
    //   964: getfield startToStart : I
    //   967: istore_3
    //   968: iload #9
    //   970: istore #4
    //   972: goto -> 1001
    //   975: iload #13
    //   977: istore_3
    //   978: iload #9
    //   980: istore #4
    //   982: aload #8
    //   984: getfield startToEnd : I
    //   987: iconst_m1
    //   988: if_icmpeq -> 1001
    //   991: aload #8
    //   993: getfield startToEnd : I
    //   996: istore #4
    //   998: iload #13
    //   1000: istore_3
    //   1001: iload_3
    //   1002: istore #9
    //   1004: iload #4
    //   1006: istore #13
    //   1008: iload #12
    //   1010: istore_3
    //   1011: iload #15
    //   1013: istore #4
    //   1015: iload #12
    //   1017: iconst_m1
    //   1018: if_icmpne -> 1082
    //   1021: iload #12
    //   1023: istore_3
    //   1024: iload #15
    //   1026: istore #4
    //   1028: iload #15
    //   1030: iconst_m1
    //   1031: if_icmpne -> 1082
    //   1034: aload #8
    //   1036: getfield endToStart : I
    //   1039: iconst_m1
    //   1040: if_icmpeq -> 1056
    //   1043: aload #8
    //   1045: getfield endToStart : I
    //   1048: istore_3
    //   1049: iload #15
    //   1051: istore #4
    //   1053: goto -> 1082
    //   1056: iload #12
    //   1058: istore_3
    //   1059: iload #15
    //   1061: istore #4
    //   1063: aload #8
    //   1065: getfield endToEnd : I
    //   1068: iconst_m1
    //   1069: if_icmpeq -> 1082
    //   1072: aload #8
    //   1074: getfield endToEnd : I
    //   1077: istore #4
    //   1079: iload #12
    //   1081: istore_3
    //   1082: iload #9
    //   1084: istore #12
    //   1086: iload #16
    //   1088: istore #9
    //   1090: goto -> 1093
    //   1093: aload #8
    //   1095: getfield circleConstraint : I
    //   1098: iconst_m1
    //   1099: if_icmpeq -> 1138
    //   1102: aload_0
    //   1103: aload #8
    //   1105: getfield circleConstraint : I
    //   1108: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1111: astore #5
    //   1113: aload #5
    //   1115: ifnull -> 1699
    //   1118: aload #6
    //   1120: aload #5
    //   1122: aload #8
    //   1124: getfield circleAngle : F
    //   1127: aload #8
    //   1129: getfield circleRadius : I
    //   1132: invokevirtual connectCircularConstraint : (Landroid/support/constraint/solver/widgets/ConstraintWidget;FI)V
    //   1135: goto -> 1699
    //   1138: iload #12
    //   1140: iconst_m1
    //   1141: if_icmpeq -> 1183
    //   1144: aload_0
    //   1145: iload #12
    //   1147: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1150: astore #5
    //   1152: aload #5
    //   1154: ifnull -> 1180
    //   1157: aload #6
    //   1159: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1162: aload #5
    //   1164: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1167: aload #8
    //   1169: getfield leftMargin : I
    //   1172: iload #14
    //   1174: invokevirtual immediateConnect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;II)V
    //   1177: goto -> 1180
    //   1180: goto -> 1238
    //   1183: iload #4
    //   1185: istore #12
    //   1187: iload #12
    //   1189: istore #4
    //   1191: iload #13
    //   1193: iconst_m1
    //   1194: if_icmpeq -> 1238
    //   1197: aload_0
    //   1198: iload #13
    //   1200: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1203: astore #5
    //   1205: iload #12
    //   1207: istore #4
    //   1209: aload #5
    //   1211: ifnull -> 1238
    //   1214: aload #6
    //   1216: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1219: aload #5
    //   1221: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1224: aload #8
    //   1226: getfield leftMargin : I
    //   1229: iload #14
    //   1231: invokevirtual immediateConnect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;II)V
    //   1234: iload #12
    //   1236: istore #4
    //   1238: iload_3
    //   1239: iconst_m1
    //   1240: if_icmpeq -> 1278
    //   1243: aload_0
    //   1244: iload_3
    //   1245: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1248: astore #5
    //   1250: aload #5
    //   1252: ifnull -> 1317
    //   1255: aload #6
    //   1257: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1260: aload #5
    //   1262: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1265: aload #8
    //   1267: getfield rightMargin : I
    //   1270: iload #9
    //   1272: invokevirtual immediateConnect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;II)V
    //   1275: goto -> 1317
    //   1278: iload #4
    //   1280: iconst_m1
    //   1281: if_icmpeq -> 1317
    //   1284: aload_0
    //   1285: iload #4
    //   1287: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1290: astore #5
    //   1292: aload #5
    //   1294: ifnull -> 1317
    //   1297: aload #6
    //   1299: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1302: aload #5
    //   1304: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1307: aload #8
    //   1309: getfield rightMargin : I
    //   1312: iload #9
    //   1314: invokevirtual immediateConnect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;II)V
    //   1317: aload #8
    //   1319: getfield topToTop : I
    //   1322: iconst_m1
    //   1323: if_icmpeq -> 1368
    //   1326: aload_0
    //   1327: aload #8
    //   1329: getfield topToTop : I
    //   1332: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1335: astore #5
    //   1337: aload #5
    //   1339: ifnull -> 1416
    //   1342: aload #6
    //   1344: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1347: aload #5
    //   1349: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1352: aload #8
    //   1354: getfield topMargin : I
    //   1357: aload #8
    //   1359: getfield goneTopMargin : I
    //   1362: invokevirtual immediateConnect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;II)V
    //   1365: goto -> 1416
    //   1368: aload #8
    //   1370: getfield topToBottom : I
    //   1373: iconst_m1
    //   1374: if_icmpeq -> 1416
    //   1377: aload_0
    //   1378: aload #8
    //   1380: getfield topToBottom : I
    //   1383: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1386: astore #5
    //   1388: aload #5
    //   1390: ifnull -> 1416
    //   1393: aload #6
    //   1395: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1398: aload #5
    //   1400: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1403: aload #8
    //   1405: getfield topMargin : I
    //   1408: aload #8
    //   1410: getfield goneTopMargin : I
    //   1413: invokevirtual immediateConnect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;II)V
    //   1416: aload #8
    //   1418: getfield bottomToTop : I
    //   1421: iconst_m1
    //   1422: if_icmpeq -> 1467
    //   1425: aload_0
    //   1426: aload #8
    //   1428: getfield bottomToTop : I
    //   1431: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1434: astore #5
    //   1436: aload #5
    //   1438: ifnull -> 1515
    //   1441: aload #6
    //   1443: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1446: aload #5
    //   1448: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1451: aload #8
    //   1453: getfield bottomMargin : I
    //   1456: aload #8
    //   1458: getfield goneBottomMargin : I
    //   1461: invokevirtual immediateConnect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;II)V
    //   1464: goto -> 1515
    //   1467: aload #8
    //   1469: getfield bottomToBottom : I
    //   1472: iconst_m1
    //   1473: if_icmpeq -> 1515
    //   1476: aload_0
    //   1477: aload #8
    //   1479: getfield bottomToBottom : I
    //   1482: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1485: astore #5
    //   1487: aload #5
    //   1489: ifnull -> 1515
    //   1492: aload #6
    //   1494: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1497: aload #5
    //   1499: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1502: aload #8
    //   1504: getfield bottomMargin : I
    //   1507: aload #8
    //   1509: getfield goneBottomMargin : I
    //   1512: invokevirtual immediateConnect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;II)V
    //   1515: aload #8
    //   1517: getfield baselineToBaseline : I
    //   1520: iconst_m1
    //   1521: if_icmpeq -> 1644
    //   1524: aload_0
    //   1525: getfield mChildrenByIds : Landroid/util/SparseArray;
    //   1528: aload #8
    //   1530: getfield baselineToBaseline : I
    //   1533: invokevirtual get : (I)Ljava/lang/Object;
    //   1536: checkcast android/view/View
    //   1539: astore #10
    //   1541: aload_0
    //   1542: aload #8
    //   1544: getfield baselineToBaseline : I
    //   1547: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1550: astore #5
    //   1552: aload #5
    //   1554: ifnull -> 1644
    //   1557: aload #10
    //   1559: ifnull -> 1644
    //   1562: aload #10
    //   1564: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1567: instanceof android/support/constraint/ConstraintLayout$LayoutParams
    //   1570: ifeq -> 1644
    //   1573: aload #10
    //   1575: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1578: checkcast android/support/constraint/ConstraintLayout$LayoutParams
    //   1581: astore #10
    //   1583: aload #8
    //   1585: iconst_1
    //   1586: putfield needsBaseline : Z
    //   1589: aload #10
    //   1591: iconst_1
    //   1592: putfield needsBaseline : Z
    //   1595: aload #6
    //   1597: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BASELINE : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1600: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1603: aload #5
    //   1605: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BASELINE : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1608: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1611: iconst_0
    //   1612: iconst_m1
    //   1613: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Strength.STRONG : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Strength;
    //   1616: iconst_0
    //   1617: iconst_1
    //   1618: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;IILandroid/support/constraint/solver/widgets/ConstraintAnchor$Strength;IZ)Z
    //   1621: pop
    //   1622: aload #6
    //   1624: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1627: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1630: invokevirtual reset : ()V
    //   1633: aload #6
    //   1635: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1638: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1641: invokevirtual reset : ()V
    //   1644: fload #11
    //   1646: fconst_0
    //   1647: fcmpl
    //   1648: iflt -> 1667
    //   1651: fload #11
    //   1653: ldc_w 0.5
    //   1656: fcmpl
    //   1657: ifeq -> 1667
    //   1660: aload #6
    //   1662: fload #11
    //   1664: invokevirtual setHorizontalBiasPercent : (F)V
    //   1667: aload #8
    //   1669: getfield verticalBias : F
    //   1672: fconst_0
    //   1673: fcmpl
    //   1674: iflt -> 1699
    //   1677: aload #8
    //   1679: getfield verticalBias : F
    //   1682: ldc_w 0.5
    //   1685: fcmpl
    //   1686: ifeq -> 1699
    //   1689: aload #6
    //   1691: aload #8
    //   1693: getfield verticalBias : F
    //   1696: invokevirtual setVerticalBiasPercent : (F)V
    //   1699: iload_1
    //   1700: ifeq -> 1736
    //   1703: aload #8
    //   1705: getfield editorAbsoluteX : I
    //   1708: iconst_m1
    //   1709: if_icmpne -> 1721
    //   1712: aload #8
    //   1714: getfield editorAbsoluteY : I
    //   1717: iconst_m1
    //   1718: if_icmpeq -> 1736
    //   1721: aload #6
    //   1723: aload #8
    //   1725: getfield editorAbsoluteX : I
    //   1728: aload #8
    //   1730: getfield editorAbsoluteY : I
    //   1733: invokevirtual setOrigin : (II)V
    //   1736: aload #8
    //   1738: getfield horizontalDimensionFixed : Z
    //   1741: ifne -> 1813
    //   1744: aload #8
    //   1746: getfield width : I
    //   1749: iconst_m1
    //   1750: if_icmpne -> 1796
    //   1753: aload #6
    //   1755: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_PARENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1758: invokevirtual setHorizontalDimensionBehaviour : (Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1761: aload #6
    //   1763: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1766: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1769: aload #8
    //   1771: getfield leftMargin : I
    //   1774: putfield mMargin : I
    //   1777: aload #6
    //   1779: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1782: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1785: aload #8
    //   1787: getfield rightMargin : I
    //   1790: putfield mMargin : I
    //   1793: goto -> 1831
    //   1796: aload #6
    //   1798: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1801: invokevirtual setHorizontalDimensionBehaviour : (Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1804: aload #6
    //   1806: iconst_0
    //   1807: invokevirtual setWidth : (I)V
    //   1810: goto -> 1831
    //   1813: aload #6
    //   1815: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1818: invokevirtual setHorizontalDimensionBehaviour : (Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1821: aload #6
    //   1823: aload #8
    //   1825: getfield width : I
    //   1828: invokevirtual setWidth : (I)V
    //   1831: aload #8
    //   1833: getfield verticalDimensionFixed : Z
    //   1836: ifne -> 1908
    //   1839: aload #8
    //   1841: getfield height : I
    //   1844: iconst_m1
    //   1845: if_icmpne -> 1891
    //   1848: aload #6
    //   1850: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_PARENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1853: invokevirtual setVerticalDimensionBehaviour : (Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1856: aload #6
    //   1858: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1861: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1864: aload #8
    //   1866: getfield topMargin : I
    //   1869: putfield mMargin : I
    //   1872: aload #6
    //   1874: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1877: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1880: aload #8
    //   1882: getfield bottomMargin : I
    //   1885: putfield mMargin : I
    //   1888: goto -> 1926
    //   1891: aload #6
    //   1893: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1896: invokevirtual setVerticalDimensionBehaviour : (Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1899: aload #6
    //   1901: iconst_0
    //   1902: invokevirtual setHeight : (I)V
    //   1905: goto -> 1926
    //   1908: aload #6
    //   1910: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1913: invokevirtual setVerticalDimensionBehaviour : (Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1916: aload #6
    //   1918: aload #8
    //   1920: getfield height : I
    //   1923: invokevirtual setHeight : (I)V
    //   1926: iconst_0
    //   1927: istore #9
    //   1929: aload #8
    //   1931: getfield dimensionRatio : Ljava/lang/String;
    //   1934: ifnull -> 1947
    //   1937: aload #6
    //   1939: aload #8
    //   1941: getfield dimensionRatio : Ljava/lang/String;
    //   1944: invokevirtual setDimensionRatio : (Ljava/lang/String;)V
    //   1947: aload #6
    //   1949: aload #8
    //   1951: getfield horizontalWeight : F
    //   1954: invokevirtual setHorizontalWeight : (F)V
    //   1957: aload #6
    //   1959: aload #8
    //   1961: getfield verticalWeight : F
    //   1964: invokevirtual setVerticalWeight : (F)V
    //   1967: aload #6
    //   1969: aload #8
    //   1971: getfield horizontalChainStyle : I
    //   1974: invokevirtual setHorizontalChainStyle : (I)V
    //   1977: aload #6
    //   1979: aload #8
    //   1981: getfield verticalChainStyle : I
    //   1984: invokevirtual setVerticalChainStyle : (I)V
    //   1987: aload #6
    //   1989: aload #8
    //   1991: getfield matchConstraintDefaultWidth : I
    //   1994: aload #8
    //   1996: getfield matchConstraintMinWidth : I
    //   1999: aload #8
    //   2001: getfield matchConstraintMaxWidth : I
    //   2004: aload #8
    //   2006: getfield matchConstraintPercentWidth : F
    //   2009: invokevirtual setHorizontalMatchStyle : (IIIF)V
    //   2012: aload #6
    //   2014: aload #8
    //   2016: getfield matchConstraintDefaultHeight : I
    //   2019: aload #8
    //   2021: getfield matchConstraintMinHeight : I
    //   2024: aload #8
    //   2026: getfield matchConstraintMaxHeight : I
    //   2029: aload #8
    //   2031: getfield matchConstraintPercentHeight : F
    //   2034: invokevirtual setVerticalMatchStyle : (IIIF)V
    //   2037: iinc #7, 1
    //   2040: iload #9
    //   2042: istore #4
    //   2044: goto -> 329
    //   2047: return
    //   2048: astore #8
    //   2050: goto -> 106
    //   2053: astore #10
    //   2055: goto -> 461
    // Exception table:
    //   from	to	target	type
    //   33	71	2048	android/content/res/Resources$NotFoundException
    //   81	92	2048	android/content/res/Resources$NotFoundException
    //   92	106	2048	android/content/res/Resources$NotFoundException
    //   400	461	2053	android/content/res/Resources$NotFoundException
  }
  
  private void setSelfDimensionBehaviour(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: iload_1
    //   1: invokestatic getMode : (I)I
    //   4: istore_3
    //   5: iload_1
    //   6: invokestatic getSize : (I)I
    //   9: istore_1
    //   10: iload_2
    //   11: invokestatic getMode : (I)I
    //   14: istore #4
    //   16: iload_2
    //   17: invokestatic getSize : (I)I
    //   20: istore_2
    //   21: aload_0
    //   22: invokevirtual getPaddingTop : ()I
    //   25: istore #5
    //   27: aload_0
    //   28: invokevirtual getPaddingBottom : ()I
    //   31: istore #6
    //   33: aload_0
    //   34: invokevirtual getPaddingLeft : ()I
    //   37: istore #7
    //   39: aload_0
    //   40: invokevirtual getPaddingRight : ()I
    //   43: istore #8
    //   45: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   48: astore #9
    //   50: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   53: astore #10
    //   55: aload_0
    //   56: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   59: pop
    //   60: iload_3
    //   61: ldc_w -2147483648
    //   64: if_icmpeq -> 109
    //   67: iload_3
    //   68: ifeq -> 101
    //   71: iload_3
    //   72: ldc_w 1073741824
    //   75: if_icmpeq -> 83
    //   78: iconst_0
    //   79: istore_1
    //   80: goto -> 114
    //   83: aload_0
    //   84: getfield mMaxWidth : I
    //   87: iload_1
    //   88: invokestatic min : (II)I
    //   91: iload #7
    //   93: iload #8
    //   95: iadd
    //   96: isub
    //   97: istore_1
    //   98: goto -> 114
    //   101: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   104: astore #9
    //   106: goto -> 78
    //   109: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   112: astore #9
    //   114: iload #4
    //   116: ldc_w -2147483648
    //   119: if_icmpeq -> 166
    //   122: iload #4
    //   124: ifeq -> 158
    //   127: iload #4
    //   129: ldc_w 1073741824
    //   132: if_icmpeq -> 140
    //   135: iconst_0
    //   136: istore_2
    //   137: goto -> 171
    //   140: aload_0
    //   141: getfield mMaxHeight : I
    //   144: iload_2
    //   145: invokestatic min : (II)I
    //   148: iload #5
    //   150: iload #6
    //   152: iadd
    //   153: isub
    //   154: istore_2
    //   155: goto -> 171
    //   158: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   161: astore #10
    //   163: goto -> 135
    //   166: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   169: astore #10
    //   171: aload_0
    //   172: getfield mLayoutWidget : Landroid/support/constraint/solver/widgets/ConstraintWidgetContainer;
    //   175: iconst_0
    //   176: invokevirtual setMinWidth : (I)V
    //   179: aload_0
    //   180: getfield mLayoutWidget : Landroid/support/constraint/solver/widgets/ConstraintWidgetContainer;
    //   183: iconst_0
    //   184: invokevirtual setMinHeight : (I)V
    //   187: aload_0
    //   188: getfield mLayoutWidget : Landroid/support/constraint/solver/widgets/ConstraintWidgetContainer;
    //   191: aload #9
    //   193: invokevirtual setHorizontalDimensionBehaviour : (Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   196: aload_0
    //   197: getfield mLayoutWidget : Landroid/support/constraint/solver/widgets/ConstraintWidgetContainer;
    //   200: iload_1
    //   201: invokevirtual setWidth : (I)V
    //   204: aload_0
    //   205: getfield mLayoutWidget : Landroid/support/constraint/solver/widgets/ConstraintWidgetContainer;
    //   208: aload #10
    //   210: invokevirtual setVerticalDimensionBehaviour : (Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   213: aload_0
    //   214: getfield mLayoutWidget : Landroid/support/constraint/solver/widgets/ConstraintWidgetContainer;
    //   217: iload_2
    //   218: invokevirtual setHeight : (I)V
    //   221: aload_0
    //   222: getfield mLayoutWidget : Landroid/support/constraint/solver/widgets/ConstraintWidgetContainer;
    //   225: aload_0
    //   226: getfield mMinWidth : I
    //   229: aload_0
    //   230: invokevirtual getPaddingLeft : ()I
    //   233: isub
    //   234: aload_0
    //   235: invokevirtual getPaddingRight : ()I
    //   238: isub
    //   239: invokevirtual setMinWidth : (I)V
    //   242: aload_0
    //   243: getfield mLayoutWidget : Landroid/support/constraint/solver/widgets/ConstraintWidgetContainer;
    //   246: aload_0
    //   247: getfield mMinHeight : I
    //   250: aload_0
    //   251: invokevirtual getPaddingTop : ()I
    //   254: isub
    //   255: aload_0
    //   256: invokevirtual getPaddingBottom : ()I
    //   259: isub
    //   260: invokevirtual setMinHeight : (I)V
    //   263: return
  }
  
  private void updateHierarchy() {
    boolean bool2;
    int i = getChildCount();
    boolean bool1 = false;
    byte b = 0;
    while (true) {
      bool2 = bool1;
      if (b < i) {
        if (getChildAt(b).isLayoutRequested()) {
          bool2 = true;
          break;
        } 
        b++;
        continue;
      } 
      break;
    } 
    if (bool2) {
      this.mVariableDimensionsWidgets.clear();
      setChildrenConstraints();
    } 
  }
  
  private void updatePostMeasures() {
    int i = getChildCount();
    boolean bool = false;
    byte b;
    for (b = 0; b < i; b++) {
      View view = getChildAt(b);
      if (view instanceof Placeholder)
        ((Placeholder)view).updatePostMeasure(this); 
    } 
    i = this.mConstraintHelpers.size();
    if (i > 0)
      for (b = bool; b < i; b++)
        ((ConstraintHelper)this.mConstraintHelpers.get(b)).updatePostMeasure(this);  
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams) {
    super.addView(paramView, paramInt, paramLayoutParams);
    if (Build.VERSION.SDK_INT < 14)
      onViewAdded(paramView); 
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  public void dispatchDraw(Canvas paramCanvas) {
    super.dispatchDraw(paramCanvas);
    if (isInEditMode()) {
      int i = getChildCount();
      float f1 = getWidth();
      float f2 = getHeight();
      for (byte b = 0; b < i; b++) {
        View view = getChildAt(b);
        if (view.getVisibility() != 8) {
          Object object = view.getTag();
          if (object != null && object instanceof String) {
            object = ((String)object).split(",");
            if (object.length == 4) {
              int j = Integer.parseInt((String)object[0]);
              int k = Integer.parseInt((String)object[1]);
              int m = Integer.parseInt((String)object[2]);
              int n = Integer.parseInt((String)object[3]);
              j = (int)(j / 1080.0F * f1);
              k = (int)(k / 1920.0F * f2);
              m = (int)(m / 1080.0F * f1);
              n = (int)(n / 1920.0F * f2);
              object = new Paint();
              object.setColor(-65536);
              float f3 = j;
              float f4 = k;
              float f5 = (j + m);
              paramCanvas.drawLine(f3, f4, f5, f4, (Paint)object);
              float f6 = (k + n);
              paramCanvas.drawLine(f5, f4, f5, f6, (Paint)object);
              paramCanvas.drawLine(f5, f6, f3, f6, (Paint)object);
              paramCanvas.drawLine(f3, f6, f3, f4, (Paint)object);
              object.setColor(-16711936);
              paramCanvas.drawLine(f3, f4, f5, f6, (Paint)object);
              paramCanvas.drawLine(f3, f6, f5, f4, (Paint)object);
            } 
          } 
        } 
      } 
    } 
  }
  
  public void fillMetrics(Metrics paramMetrics) {
    this.mMetrics = paramMetrics;
    this.mLayoutWidget.fillMetrics(paramMetrics);
  }
  
  protected LayoutParams generateDefaultLayoutParams() {
    return new LayoutParams(-2, -2);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return (ViewGroup.LayoutParams)new LayoutParams(paramLayoutParams);
  }
  
  public Object getDesignInformation(int paramInt, Object paramObject) {
    if (paramInt == 0 && paramObject instanceof String) {
      paramObject = paramObject;
      if (this.mDesignIds != null && this.mDesignIds.containsKey(paramObject))
        return this.mDesignIds.get(paramObject); 
    } 
    return null;
  }
  
  public int getMaxHeight() {
    return this.mMaxHeight;
  }
  
  public int getMaxWidth() {
    return this.mMaxWidth;
  }
  
  public int getMinHeight() {
    return this.mMinHeight;
  }
  
  public int getMinWidth() {
    return this.mMinWidth;
  }
  
  public int getOptimizationLevel() {
    return this.mLayoutWidget.getOptimizationLevel();
  }
  
  public View getViewById(int paramInt) {
    return (View)this.mChildrenByIds.get(paramInt);
  }
  
  public final ConstraintWidget getViewWidget(View paramView) {
    ConstraintWidget constraintWidget;
    if (paramView == this)
      return (ConstraintWidget)this.mLayoutWidget; 
    if (paramView == null) {
      paramView = null;
    } else {
      constraintWidget = ((LayoutParams)paramView.getLayoutParams()).widget;
    } 
    return constraintWidget;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    paramInt3 = getChildCount();
    paramBoolean = isInEditMode();
    paramInt2 = 0;
    for (paramInt1 = 0; paramInt1 < paramInt3; paramInt1++) {
      View view = getChildAt(paramInt1);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      ConstraintWidget constraintWidget = layoutParams.widget;
      if ((view.getVisibility() != 8 || layoutParams.isGuideline || layoutParams.isHelper || paramBoolean) && !layoutParams.isInPlaceholder) {
        int i = constraintWidget.getDrawX();
        paramInt4 = constraintWidget.getDrawY();
        int j = constraintWidget.getWidth() + i;
        int k = constraintWidget.getHeight() + paramInt4;
        view.layout(i, paramInt4, j, k);
        if (view instanceof Placeholder) {
          View view1 = ((Placeholder)view).getContent();
          if (view1 != null) {
            view1.setVisibility(0);
            view1.layout(i, paramInt4, j, k);
          } 
        } 
      } 
    } 
    paramInt3 = this.mConstraintHelpers.size();
    if (paramInt3 > 0)
      for (paramInt1 = paramInt2; paramInt1 < paramInt3; paramInt1++)
        ((ConstraintHelper)this.mConstraintHelpers.get(paramInt1)).updatePostLayout(this);  
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i4;
    boolean bool;
    System.currentTimeMillis();
    int i = View.MeasureSpec.getMode(paramInt1);
    int j = View.MeasureSpec.getSize(paramInt1);
    int k = View.MeasureSpec.getMode(paramInt2);
    int m = View.MeasureSpec.getSize(paramInt2);
    int n = getPaddingLeft();
    int i1 = getPaddingTop();
    this.mLayoutWidget.setX(n);
    this.mLayoutWidget.setY(i1);
    this.mLayoutWidget.setMaxWidth(this.mMaxWidth);
    this.mLayoutWidget.setMaxHeight(this.mMaxHeight);
    if (Build.VERSION.SDK_INT >= 17) {
      boolean bool1;
      ConstraintWidgetContainer constraintWidgetContainer = this.mLayoutWidget;
      if (getLayoutDirection() == 1) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      constraintWidgetContainer.setRtl(bool1);
    } 
    setSelfDimensionBehaviour(paramInt1, paramInt2);
    int i2 = this.mLayoutWidget.getWidth();
    int i3 = this.mLayoutWidget.getHeight();
    if (this.mDirtyHierarchy) {
      this.mDirtyHierarchy = false;
      updateHierarchy();
      i4 = 1;
    } else {
      i4 = 0;
    } 
    if ((this.mOptimizationLevel & 0x8) == 8) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool) {
      this.mLayoutWidget.preOptimize();
      this.mLayoutWidget.optimizeForDimensions(i2, i3);
      internalMeasureDimensions(paramInt1, paramInt2);
    } else {
      internalMeasureChildren(paramInt1, paramInt2);
    } 
    updatePostMeasures();
    if (getChildCount() > 0 && i4)
      Analyzer.determineGroups(this.mLayoutWidget); 
    if (this.mLayoutWidget.mGroupsWrapOptimized) {
      if (this.mLayoutWidget.mHorizontalWrapOptimized && i == Integer.MIN_VALUE) {
        if (this.mLayoutWidget.mWrapFixedWidth < j)
          this.mLayoutWidget.setWidth(this.mLayoutWidget.mWrapFixedWidth); 
        this.mLayoutWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
      } 
      if (this.mLayoutWidget.mVerticalWrapOptimized && k == Integer.MIN_VALUE) {
        if (this.mLayoutWidget.mWrapFixedHeight < m)
          this.mLayoutWidget.setHeight(this.mLayoutWidget.mWrapFixedHeight); 
        this.mLayoutWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
      } 
    } 
    if ((this.mOptimizationLevel & 0x20) == 32) {
      int i7 = this.mLayoutWidget.getWidth();
      i4 = this.mLayoutWidget.getHeight();
      if (this.mLastMeasureWidth != i7 && i == 1073741824)
        Analyzer.setPosition(this.mLayoutWidget.mWidgetGroups, 0, i7); 
      if (this.mLastMeasureHeight != i4 && k == 1073741824)
        Analyzer.setPosition(this.mLayoutWidget.mWidgetGroups, 1, i4); 
      if (this.mLayoutWidget.mHorizontalWrapOptimized && this.mLayoutWidget.mWrapFixedWidth > j)
        Analyzer.setPosition(this.mLayoutWidget.mWidgetGroups, 0, j); 
      if (this.mLayoutWidget.mVerticalWrapOptimized && this.mLayoutWidget.mWrapFixedHeight > m)
        Analyzer.setPosition(this.mLayoutWidget.mWidgetGroups, 1, m); 
    } 
    if (getChildCount() > 0)
      solveLinearSystem("First pass"); 
    j = this.mVariableDimensionsWidgets.size();
    int i5 = i1 + getPaddingBottom();
    int i6 = n + getPaddingRight();
    if (j > 0) {
      boolean bool1;
      if (this.mLayoutWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
        k = 1;
      } else {
        k = 0;
      } 
      if (this.mLayoutWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      i1 = Math.max(this.mLayoutWidget.getWidth(), this.mMinWidth);
      n = Math.max(this.mLayoutWidget.getHeight(), this.mMinHeight);
      byte b = 0;
      m = 0;
      i4 = 0;
      while (b < j) {
        ConstraintWidget constraintWidget = this.mVariableDimensionsWidgets.get(b);
        View view = (View)constraintWidget.getCompanionWidget();
        if (view != null) {
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          if (!layoutParams.isHelper && !layoutParams.isGuideline) {
            int i7 = view.getVisibility();
            i = m;
            if (i7 != 8 && (!bool || !constraintWidget.getResolutionWidth().isResolved() || !constraintWidget.getResolutionHeight().isResolved())) {
              if (layoutParams.width == -2 && layoutParams.horizontalDimensionFixed) {
                m = getChildMeasureSpec(paramInt1, i6, layoutParams.width);
              } else {
                m = View.MeasureSpec.makeMeasureSpec(constraintWidget.getWidth(), 1073741824);
              } 
              if (layoutParams.height == -2 && layoutParams.verticalDimensionFixed) {
                i7 = getChildMeasureSpec(paramInt2, i5, layoutParams.height);
              } else {
                i7 = View.MeasureSpec.makeMeasureSpec(constraintWidget.getHeight(), 1073741824);
              } 
              view.measure(m, i7);
              if (this.mMetrics != null) {
                Metrics metrics = this.mMetrics;
                metrics.additionalMeasures++;
              } 
              int i8 = view.getMeasuredWidth();
              i7 = view.getMeasuredHeight();
              m = i1;
              if (i8 != constraintWidget.getWidth()) {
                constraintWidget.setWidth(i8);
                if (bool)
                  constraintWidget.getResolutionWidth().resolve(i8); 
                m = i1;
                if (k != 0) {
                  m = i1;
                  if (constraintWidget.getRight() > i1)
                    m = Math.max(i1, constraintWidget.getRight() + constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).getMargin()); 
                } 
                i = 1;
              } 
              i1 = n;
              if (i7 != constraintWidget.getHeight()) {
                constraintWidget.setHeight(i7);
                if (bool)
                  constraintWidget.getResolutionHeight().resolve(i7); 
                i1 = n;
                if (bool1) {
                  i1 = n;
                  if (constraintWidget.getBottom() > n)
                    i1 = Math.max(n, constraintWidget.getBottom() + constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).getMargin()); 
                } 
                i = 1;
              } 
              n = i;
              if (layoutParams.needsBaseline) {
                i7 = view.getBaseline();
                n = i;
                if (i7 != -1) {
                  n = i;
                  if (i7 != constraintWidget.getBaselineDistance()) {
                    constraintWidget.setBaselineDistance(i7);
                    n = 1;
                  } 
                } 
              } 
              if (Build.VERSION.SDK_INT >= 11) {
                i4 = combineMeasuredStates(i4, view.getMeasuredState());
                i = i1;
                i1 = m;
                m = n;
              } else {
                i = i1;
                i1 = m;
                m = n;
              } 
              continue;
            } 
          } 
        } 
        i = n;
        continue;
        b++;
        n = i;
      } 
      i = i4;
      if (m != 0) {
        this.mLayoutWidget.setWidth(i2);
        this.mLayoutWidget.setHeight(i3);
        if (bool)
          this.mLayoutWidget.solveGraph(); 
        solveLinearSystem("2nd pass");
        if (this.mLayoutWidget.getWidth() < i1) {
          this.mLayoutWidget.setWidth(i1);
          i4 = 1;
        } else {
          i4 = 0;
        } 
        if (this.mLayoutWidget.getHeight() < n) {
          this.mLayoutWidget.setHeight(n);
          i4 = 1;
        } 
        if (i4 != 0)
          solveLinearSystem("3rd pass"); 
      } 
      n = 0;
      while (true) {
        i4 = i;
        if (n < j) {
          ConstraintWidget constraintWidget = this.mVariableDimensionsWidgets.get(n);
          View view = (View)constraintWidget.getCompanionWidget();
          if (view != null && (view.getMeasuredWidth() != constraintWidget.getWidth() || view.getMeasuredHeight() != constraintWidget.getHeight()) && constraintWidget.getVisibility() != 8) {
            view.measure(View.MeasureSpec.makeMeasureSpec(constraintWidget.getWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(constraintWidget.getHeight(), 1073741824));
            if (this.mMetrics != null) {
              Metrics metrics = this.mMetrics;
              metrics.additionalMeasures++;
            } 
          } 
          n++;
          continue;
        } 
        break;
      } 
    } else {
      i4 = 0;
    } 
    n = this.mLayoutWidget.getWidth() + i6;
    i1 = this.mLayoutWidget.getHeight() + i5;
    if (Build.VERSION.SDK_INT >= 11) {
      paramInt1 = resolveSizeAndState(n, paramInt1, i4);
      i4 = resolveSizeAndState(i1, paramInt2, i4 << 16);
      paramInt2 = Math.min(this.mMaxWidth, paramInt1 & 0xFFFFFF);
      i4 = Math.min(this.mMaxHeight, i4 & 0xFFFFFF);
      paramInt1 = paramInt2;
      if (this.mLayoutWidget.isWidthMeasuredTooSmall())
        paramInt1 = paramInt2 | 0x1000000; 
      paramInt2 = i4;
      if (this.mLayoutWidget.isHeightMeasuredTooSmall())
        paramInt2 = i4 | 0x1000000; 
      setMeasuredDimension(paramInt1, paramInt2);
      this.mLastMeasureWidth = paramInt1;
      this.mLastMeasureHeight = paramInt2;
    } else {
      setMeasuredDimension(n, i1);
      this.mLastMeasureWidth = n;
      this.mLastMeasureHeight = i1;
    } 
  }
  
  public void onViewAdded(View paramView) {
    if (Build.VERSION.SDK_INT >= 14)
      super.onViewAdded(paramView); 
    ConstraintWidget constraintWidget = getViewWidget(paramView);
    if (paramView instanceof Guideline && !(constraintWidget instanceof Guideline)) {
      LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
      layoutParams.widget = (ConstraintWidget)new Guideline();
      layoutParams.isGuideline = true;
      ((Guideline)layoutParams.widget).setOrientation(layoutParams.orientation);
    } 
    if (paramView instanceof ConstraintHelper) {
      ConstraintHelper constraintHelper = (ConstraintHelper)paramView;
      constraintHelper.validateParams();
      ((LayoutParams)paramView.getLayoutParams()).isHelper = true;
      if (!this.mConstraintHelpers.contains(constraintHelper))
        this.mConstraintHelpers.add(constraintHelper); 
    } 
    this.mChildrenByIds.put(paramView.getId(), paramView);
    this.mDirtyHierarchy = true;
  }
  
  public void onViewRemoved(View paramView) {
    if (Build.VERSION.SDK_INT >= 14)
      super.onViewRemoved(paramView); 
    this.mChildrenByIds.remove(paramView.getId());
    ConstraintWidget constraintWidget = getViewWidget(paramView);
    this.mLayoutWidget.remove(constraintWidget);
    this.mConstraintHelpers.remove(paramView);
    this.mVariableDimensionsWidgets.remove(constraintWidget);
    this.mDirtyHierarchy = true;
  }
  
  public void removeView(View paramView) {
    super.removeView(paramView);
    if (Build.VERSION.SDK_INT < 14)
      onViewRemoved(paramView); 
  }
  
  public void requestLayout() {
    super.requestLayout();
    this.mDirtyHierarchy = true;
    this.mLastMeasureWidth = -1;
    this.mLastMeasureHeight = -1;
    this.mLastMeasureWidthSize = -1;
    this.mLastMeasureHeightSize = -1;
    this.mLastMeasureWidthMode = 0;
    this.mLastMeasureHeightMode = 0;
  }
  
  public void setConstraintSet(ConstraintSet paramConstraintSet) {
    this.mConstraintSet = paramConstraintSet;
  }
  
  public void setDesignInformation(int paramInt, Object paramObject1, Object paramObject2) {
    if (paramInt == 0 && paramObject1 instanceof String && paramObject2 instanceof Integer) {
      if (this.mDesignIds == null)
        this.mDesignIds = new HashMap<String, Integer>(); 
      String str = (String)paramObject1;
      paramInt = str.indexOf("/");
      paramObject1 = str;
      if (paramInt != -1)
        paramObject1 = str.substring(paramInt + 1); 
      paramInt = ((Integer)paramObject2).intValue();
      this.mDesignIds.put(paramObject1, Integer.valueOf(paramInt));
    } 
  }
  
  public void setId(int paramInt) {
    this.mChildrenByIds.remove(getId());
    super.setId(paramInt);
    this.mChildrenByIds.put(getId(), this);
  }
  
  public void setMaxHeight(int paramInt) {
    if (paramInt == this.mMaxHeight)
      return; 
    this.mMaxHeight = paramInt;
    requestLayout();
  }
  
  public void setMaxWidth(int paramInt) {
    if (paramInt == this.mMaxWidth)
      return; 
    this.mMaxWidth = paramInt;
    requestLayout();
  }
  
  public void setMinHeight(int paramInt) {
    if (paramInt == this.mMinHeight)
      return; 
    this.mMinHeight = paramInt;
    requestLayout();
  }
  
  public void setMinWidth(int paramInt) {
    if (paramInt == this.mMinWidth)
      return; 
    this.mMinWidth = paramInt;
    requestLayout();
  }
  
  public void setOptimizationLevel(int paramInt) {
    this.mLayoutWidget.setOptimizationLevel(paramInt);
  }
  
  public boolean shouldDelayChildPressedState() {
    return false;
  }
  
  protected void solveLinearSystem(String paramString) {
    this.mLayoutWidget.layout();
    if (this.mMetrics != null) {
      Metrics metrics = this.mMetrics;
      metrics.resolutions++;
    } 
  }
  
  public static class LayoutParams extends ViewGroup.MarginLayoutParams {
    public static final int BASELINE = 5;
    
    public static final int BOTTOM = 4;
    
    public static final int CHAIN_PACKED = 2;
    
    public static final int CHAIN_SPREAD = 0;
    
    public static final int CHAIN_SPREAD_INSIDE = 1;
    
    public static final int END = 7;
    
    public static final int HORIZONTAL = 0;
    
    public static final int LEFT = 1;
    
    public static final int MATCH_CONSTRAINT = 0;
    
    public static final int MATCH_CONSTRAINT_PERCENT = 2;
    
    public static final int MATCH_CONSTRAINT_SPREAD = 0;
    
    public static final int MATCH_CONSTRAINT_WRAP = 1;
    
    public static final int PARENT_ID = 0;
    
    public static final int RIGHT = 2;
    
    public static final int START = 6;
    
    public static final int TOP = 3;
    
    public static final int UNSET = -1;
    
    public static final int VERTICAL = 1;
    
    public int baselineToBaseline = -1;
    
    public int bottomToBottom = -1;
    
    public int bottomToTop = -1;
    
    public float circleAngle = 0.0F;
    
    public int circleConstraint = -1;
    
    public int circleRadius = 0;
    
    public boolean constrainedHeight = false;
    
    public boolean constrainedWidth = false;
    
    public String dimensionRatio = null;
    
    int dimensionRatioSide = 1;
    
    float dimensionRatioValue = 0.0F;
    
    public int editorAbsoluteX = -1;
    
    public int editorAbsoluteY = -1;
    
    public int endToEnd = -1;
    
    public int endToStart = -1;
    
    public int goneBottomMargin = -1;
    
    public int goneEndMargin = -1;
    
    public int goneLeftMargin = -1;
    
    public int goneRightMargin = -1;
    
    public int goneStartMargin = -1;
    
    public int goneTopMargin = -1;
    
    public int guideBegin = -1;
    
    public int guideEnd = -1;
    
    public float guidePercent = -1.0F;
    
    public boolean helped = false;
    
    public float horizontalBias = 0.5F;
    
    public int horizontalChainStyle = 0;
    
    boolean horizontalDimensionFixed = true;
    
    public float horizontalWeight = -1.0F;
    
    boolean isGuideline = false;
    
    boolean isHelper = false;
    
    boolean isInPlaceholder = false;
    
    public int leftToLeft = -1;
    
    public int leftToRight = -1;
    
    public int matchConstraintDefaultHeight = 0;
    
    public int matchConstraintDefaultWidth = 0;
    
    public int matchConstraintMaxHeight = 0;
    
    public int matchConstraintMaxWidth = 0;
    
    public int matchConstraintMinHeight = 0;
    
    public int matchConstraintMinWidth = 0;
    
    public float matchConstraintPercentHeight = 1.0F;
    
    public float matchConstraintPercentWidth = 1.0F;
    
    boolean needsBaseline = false;
    
    public int orientation = -1;
    
    int resolveGoneLeftMargin = -1;
    
    int resolveGoneRightMargin = -1;
    
    int resolvedGuideBegin;
    
    int resolvedGuideEnd;
    
    float resolvedGuidePercent;
    
    float resolvedHorizontalBias = 0.5F;
    
    int resolvedLeftToLeft = -1;
    
    int resolvedLeftToRight = -1;
    
    int resolvedRightToLeft = -1;
    
    int resolvedRightToRight = -1;
    
    public int rightToLeft = -1;
    
    public int rightToRight = -1;
    
    public int startToEnd = -1;
    
    public int startToStart = -1;
    
    public int topToBottom = -1;
    
    public int topToTop = -1;
    
    public float verticalBias = 0.5F;
    
    public int verticalChainStyle = 0;
    
    boolean verticalDimensionFixed = true;
    
    public float verticalWeight = -1.0F;
    
    ConstraintWidget widget = new ConstraintWidget();
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      // Byte code:
      //   0: aload_0
      //   1: aload_1
      //   2: aload_2
      //   3: invokespecial <init> : (Landroid/content/Context;Landroid/util/AttributeSet;)V
      //   6: aload_0
      //   7: iconst_m1
      //   8: putfield guideBegin : I
      //   11: aload_0
      //   12: iconst_m1
      //   13: putfield guideEnd : I
      //   16: aload_0
      //   17: ldc -1.0
      //   19: putfield guidePercent : F
      //   22: aload_0
      //   23: iconst_m1
      //   24: putfield leftToLeft : I
      //   27: aload_0
      //   28: iconst_m1
      //   29: putfield leftToRight : I
      //   32: aload_0
      //   33: iconst_m1
      //   34: putfield rightToLeft : I
      //   37: aload_0
      //   38: iconst_m1
      //   39: putfield rightToRight : I
      //   42: aload_0
      //   43: iconst_m1
      //   44: putfield topToTop : I
      //   47: aload_0
      //   48: iconst_m1
      //   49: putfield topToBottom : I
      //   52: aload_0
      //   53: iconst_m1
      //   54: putfield bottomToTop : I
      //   57: aload_0
      //   58: iconst_m1
      //   59: putfield bottomToBottom : I
      //   62: aload_0
      //   63: iconst_m1
      //   64: putfield baselineToBaseline : I
      //   67: aload_0
      //   68: iconst_m1
      //   69: putfield circleConstraint : I
      //   72: aload_0
      //   73: iconst_0
      //   74: putfield circleRadius : I
      //   77: aload_0
      //   78: fconst_0
      //   79: putfield circleAngle : F
      //   82: aload_0
      //   83: iconst_m1
      //   84: putfield startToEnd : I
      //   87: aload_0
      //   88: iconst_m1
      //   89: putfield startToStart : I
      //   92: aload_0
      //   93: iconst_m1
      //   94: putfield endToStart : I
      //   97: aload_0
      //   98: iconst_m1
      //   99: putfield endToEnd : I
      //   102: aload_0
      //   103: iconst_m1
      //   104: putfield goneLeftMargin : I
      //   107: aload_0
      //   108: iconst_m1
      //   109: putfield goneTopMargin : I
      //   112: aload_0
      //   113: iconst_m1
      //   114: putfield goneRightMargin : I
      //   117: aload_0
      //   118: iconst_m1
      //   119: putfield goneBottomMargin : I
      //   122: aload_0
      //   123: iconst_m1
      //   124: putfield goneStartMargin : I
      //   127: aload_0
      //   128: iconst_m1
      //   129: putfield goneEndMargin : I
      //   132: aload_0
      //   133: ldc 0.5
      //   135: putfield horizontalBias : F
      //   138: aload_0
      //   139: ldc 0.5
      //   141: putfield verticalBias : F
      //   144: aload_0
      //   145: aconst_null
      //   146: putfield dimensionRatio : Ljava/lang/String;
      //   149: aload_0
      //   150: fconst_0
      //   151: putfield dimensionRatioValue : F
      //   154: aload_0
      //   155: iconst_1
      //   156: putfield dimensionRatioSide : I
      //   159: aload_0
      //   160: ldc -1.0
      //   162: putfield horizontalWeight : F
      //   165: aload_0
      //   166: ldc -1.0
      //   168: putfield verticalWeight : F
      //   171: aload_0
      //   172: iconst_0
      //   173: putfield horizontalChainStyle : I
      //   176: aload_0
      //   177: iconst_0
      //   178: putfield verticalChainStyle : I
      //   181: aload_0
      //   182: iconst_0
      //   183: putfield matchConstraintDefaultWidth : I
      //   186: aload_0
      //   187: iconst_0
      //   188: putfield matchConstraintDefaultHeight : I
      //   191: aload_0
      //   192: iconst_0
      //   193: putfield matchConstraintMinWidth : I
      //   196: aload_0
      //   197: iconst_0
      //   198: putfield matchConstraintMinHeight : I
      //   201: aload_0
      //   202: iconst_0
      //   203: putfield matchConstraintMaxWidth : I
      //   206: aload_0
      //   207: iconst_0
      //   208: putfield matchConstraintMaxHeight : I
      //   211: aload_0
      //   212: fconst_1
      //   213: putfield matchConstraintPercentWidth : F
      //   216: aload_0
      //   217: fconst_1
      //   218: putfield matchConstraintPercentHeight : F
      //   221: aload_0
      //   222: iconst_m1
      //   223: putfield editorAbsoluteX : I
      //   226: aload_0
      //   227: iconst_m1
      //   228: putfield editorAbsoluteY : I
      //   231: aload_0
      //   232: iconst_m1
      //   233: putfield orientation : I
      //   236: aload_0
      //   237: iconst_0
      //   238: putfield constrainedWidth : Z
      //   241: aload_0
      //   242: iconst_0
      //   243: putfield constrainedHeight : Z
      //   246: aload_0
      //   247: iconst_1
      //   248: putfield horizontalDimensionFixed : Z
      //   251: aload_0
      //   252: iconst_1
      //   253: putfield verticalDimensionFixed : Z
      //   256: aload_0
      //   257: iconst_0
      //   258: putfield needsBaseline : Z
      //   261: aload_0
      //   262: iconst_0
      //   263: putfield isGuideline : Z
      //   266: aload_0
      //   267: iconst_0
      //   268: putfield isHelper : Z
      //   271: aload_0
      //   272: iconst_0
      //   273: putfield isInPlaceholder : Z
      //   276: aload_0
      //   277: iconst_m1
      //   278: putfield resolvedLeftToLeft : I
      //   281: aload_0
      //   282: iconst_m1
      //   283: putfield resolvedLeftToRight : I
      //   286: aload_0
      //   287: iconst_m1
      //   288: putfield resolvedRightToLeft : I
      //   291: aload_0
      //   292: iconst_m1
      //   293: putfield resolvedRightToRight : I
      //   296: aload_0
      //   297: iconst_m1
      //   298: putfield resolveGoneLeftMargin : I
      //   301: aload_0
      //   302: iconst_m1
      //   303: putfield resolveGoneRightMargin : I
      //   306: aload_0
      //   307: ldc 0.5
      //   309: putfield resolvedHorizontalBias : F
      //   312: aload_0
      //   313: new android/support/constraint/solver/widgets/ConstraintWidget
      //   316: dup
      //   317: invokespecial <init> : ()V
      //   320: putfield widget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
      //   323: aload_0
      //   324: iconst_0
      //   325: putfield helped : Z
      //   328: aload_1
      //   329: aload_2
      //   330: getstatic android/support/constraint/R$styleable.ConstraintLayout_Layout : [I
      //   333: invokevirtual obtainStyledAttributes : (Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;
      //   336: astore_1
      //   337: aload_1
      //   338: invokevirtual getIndexCount : ()I
      //   341: istore_3
      //   342: iconst_0
      //   343: istore #4
      //   345: iload #4
      //   347: iload_3
      //   348: if_icmpge -> 2039
      //   351: aload_1
      //   352: iload #4
      //   354: invokevirtual getIndex : (I)I
      //   357: istore #5
      //   359: getstatic android/support/constraint/ConstraintLayout$LayoutParams$Table.map : Landroid/util/SparseIntArray;
      //   362: iload #5
      //   364: invokevirtual get : (I)I
      //   367: tableswitch default -> 584, 0 -> 2033, 1 -> 2019, 2 -> 1983, 3 -> 1966, 4 -> 1920, 5 -> 1903, 6 -> 1886, 7 -> 1869, 8 -> 1833, 9 -> 1797, 10 -> 1761, 11 -> 1725, 12 -> 1689, 13 -> 1653, 14 -> 1617, 15 -> 1581, 16 -> 1545, 17 -> 1509, 18 -> 1473, 19 -> 1437, 20 -> 1401, 21 -> 1384, 22 -> 1367, 23 -> 1350, 24 -> 1333, 25 -> 1316, 26 -> 1299, 27 -> 1282, 28 -> 1265, 29 -> 1248, 30 -> 1231, 31 -> 1199, 32 -> 1167, 33 -> 1125, 34 -> 1083, 35 -> 1062, 36 -> 1020, 37 -> 978, 38 -> 957, 39 -> 2033, 40 -> 2033, 41 -> 2033, 42 -> 2033, 43 -> 584, 44 -> 683, 45 -> 666, 46 -> 649, 47 -> 635, 48 -> 621, 49 -> 604, 50 -> 587
      //   584: goto -> 2033
      //   587: aload_0
      //   588: aload_1
      //   589: iload #5
      //   591: aload_0
      //   592: getfield editorAbsoluteY : I
      //   595: invokevirtual getDimensionPixelOffset : (II)I
      //   598: putfield editorAbsoluteY : I
      //   601: goto -> 2033
      //   604: aload_0
      //   605: aload_1
      //   606: iload #5
      //   608: aload_0
      //   609: getfield editorAbsoluteX : I
      //   612: invokevirtual getDimensionPixelOffset : (II)I
      //   615: putfield editorAbsoluteX : I
      //   618: goto -> 2033
      //   621: aload_0
      //   622: aload_1
      //   623: iload #5
      //   625: iconst_0
      //   626: invokevirtual getInt : (II)I
      //   629: putfield verticalChainStyle : I
      //   632: goto -> 2033
      //   635: aload_0
      //   636: aload_1
      //   637: iload #5
      //   639: iconst_0
      //   640: invokevirtual getInt : (II)I
      //   643: putfield horizontalChainStyle : I
      //   646: goto -> 2033
      //   649: aload_0
      //   650: aload_1
      //   651: iload #5
      //   653: aload_0
      //   654: getfield verticalWeight : F
      //   657: invokevirtual getFloat : (IF)F
      //   660: putfield verticalWeight : F
      //   663: goto -> 2033
      //   666: aload_0
      //   667: aload_1
      //   668: iload #5
      //   670: aload_0
      //   671: getfield horizontalWeight : F
      //   674: invokevirtual getFloat : (IF)F
      //   677: putfield horizontalWeight : F
      //   680: goto -> 2033
      //   683: aload_0
      //   684: aload_1
      //   685: iload #5
      //   687: invokevirtual getString : (I)Ljava/lang/String;
      //   690: putfield dimensionRatio : Ljava/lang/String;
      //   693: aload_0
      //   694: ldc_w NaN
      //   697: putfield dimensionRatioValue : F
      //   700: aload_0
      //   701: iconst_m1
      //   702: putfield dimensionRatioSide : I
      //   705: aload_0
      //   706: getfield dimensionRatio : Ljava/lang/String;
      //   709: ifnull -> 2033
      //   712: aload_0
      //   713: getfield dimensionRatio : Ljava/lang/String;
      //   716: invokevirtual length : ()I
      //   719: istore #6
      //   721: aload_0
      //   722: getfield dimensionRatio : Ljava/lang/String;
      //   725: bipush #44
      //   727: invokevirtual indexOf : (I)I
      //   730: istore #5
      //   732: iload #5
      //   734: ifle -> 796
      //   737: iload #5
      //   739: iload #6
      //   741: iconst_1
      //   742: isub
      //   743: if_icmpge -> 796
      //   746: aload_0
      //   747: getfield dimensionRatio : Ljava/lang/String;
      //   750: iconst_0
      //   751: iload #5
      //   753: invokevirtual substring : (II)Ljava/lang/String;
      //   756: astore_2
      //   757: aload_2
      //   758: ldc_w 'W'
      //   761: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
      //   764: ifeq -> 775
      //   767: aload_0
      //   768: iconst_0
      //   769: putfield dimensionRatioSide : I
      //   772: goto -> 790
      //   775: aload_2
      //   776: ldc_w 'H'
      //   779: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
      //   782: ifeq -> 790
      //   785: aload_0
      //   786: iconst_1
      //   787: putfield dimensionRatioSide : I
      //   790: iinc #5, 1
      //   793: goto -> 799
      //   796: iconst_0
      //   797: istore #5
      //   799: aload_0
      //   800: getfield dimensionRatio : Ljava/lang/String;
      //   803: bipush #58
      //   805: invokevirtual indexOf : (I)I
      //   808: istore #7
      //   810: iload #7
      //   812: iflt -> 929
      //   815: iload #7
      //   817: iload #6
      //   819: iconst_1
      //   820: isub
      //   821: if_icmpge -> 929
      //   824: aload_0
      //   825: getfield dimensionRatio : Ljava/lang/String;
      //   828: iload #5
      //   830: iload #7
      //   832: invokevirtual substring : (II)Ljava/lang/String;
      //   835: astore #8
      //   837: aload_0
      //   838: getfield dimensionRatio : Ljava/lang/String;
      //   841: iload #7
      //   843: iconst_1
      //   844: iadd
      //   845: invokevirtual substring : (I)Ljava/lang/String;
      //   848: astore_2
      //   849: aload #8
      //   851: invokevirtual length : ()I
      //   854: ifle -> 2033
      //   857: aload_2
      //   858: invokevirtual length : ()I
      //   861: ifle -> 2033
      //   864: aload #8
      //   866: invokestatic parseFloat : (Ljava/lang/String;)F
      //   869: fstore #9
      //   871: aload_2
      //   872: invokestatic parseFloat : (Ljava/lang/String;)F
      //   875: fstore #10
      //   877: fload #9
      //   879: fconst_0
      //   880: fcmpl
      //   881: ifle -> 2033
      //   884: fload #10
      //   886: fconst_0
      //   887: fcmpl
      //   888: ifle -> 2033
      //   891: aload_0
      //   892: getfield dimensionRatioSide : I
      //   895: iconst_1
      //   896: if_icmpne -> 914
      //   899: aload_0
      //   900: fload #10
      //   902: fload #9
      //   904: fdiv
      //   905: invokestatic abs : (F)F
      //   908: putfield dimensionRatioValue : F
      //   911: goto -> 2033
      //   914: aload_0
      //   915: fload #9
      //   917: fload #10
      //   919: fdiv
      //   920: invokestatic abs : (F)F
      //   923: putfield dimensionRatioValue : F
      //   926: goto -> 2033
      //   929: aload_0
      //   930: getfield dimensionRatio : Ljava/lang/String;
      //   933: iload #5
      //   935: invokevirtual substring : (I)Ljava/lang/String;
      //   938: astore_2
      //   939: aload_2
      //   940: invokevirtual length : ()I
      //   943: ifle -> 2033
      //   946: aload_0
      //   947: aload_2
      //   948: invokestatic parseFloat : (Ljava/lang/String;)F
      //   951: putfield dimensionRatioValue : F
      //   954: goto -> 2033
      //   957: aload_0
      //   958: fconst_0
      //   959: aload_1
      //   960: iload #5
      //   962: aload_0
      //   963: getfield matchConstraintPercentHeight : F
      //   966: invokevirtual getFloat : (IF)F
      //   969: invokestatic max : (FF)F
      //   972: putfield matchConstraintPercentHeight : F
      //   975: goto -> 2033
      //   978: aload_0
      //   979: aload_1
      //   980: iload #5
      //   982: aload_0
      //   983: getfield matchConstraintMaxHeight : I
      //   986: invokevirtual getDimensionPixelSize : (II)I
      //   989: putfield matchConstraintMaxHeight : I
      //   992: goto -> 2033
      //   995: astore_2
      //   996: aload_1
      //   997: iload #5
      //   999: aload_0
      //   1000: getfield matchConstraintMaxHeight : I
      //   1003: invokevirtual getInt : (II)I
      //   1006: bipush #-2
      //   1008: if_icmpne -> 2033
      //   1011: aload_0
      //   1012: bipush #-2
      //   1014: putfield matchConstraintMaxHeight : I
      //   1017: goto -> 2033
      //   1020: aload_0
      //   1021: aload_1
      //   1022: iload #5
      //   1024: aload_0
      //   1025: getfield matchConstraintMinHeight : I
      //   1028: invokevirtual getDimensionPixelSize : (II)I
      //   1031: putfield matchConstraintMinHeight : I
      //   1034: goto -> 2033
      //   1037: astore_2
      //   1038: aload_1
      //   1039: iload #5
      //   1041: aload_0
      //   1042: getfield matchConstraintMinHeight : I
      //   1045: invokevirtual getInt : (II)I
      //   1048: bipush #-2
      //   1050: if_icmpne -> 2033
      //   1053: aload_0
      //   1054: bipush #-2
      //   1056: putfield matchConstraintMinHeight : I
      //   1059: goto -> 2033
      //   1062: aload_0
      //   1063: fconst_0
      //   1064: aload_1
      //   1065: iload #5
      //   1067: aload_0
      //   1068: getfield matchConstraintPercentWidth : F
      //   1071: invokevirtual getFloat : (IF)F
      //   1074: invokestatic max : (FF)F
      //   1077: putfield matchConstraintPercentWidth : F
      //   1080: goto -> 2033
      //   1083: aload_0
      //   1084: aload_1
      //   1085: iload #5
      //   1087: aload_0
      //   1088: getfield matchConstraintMaxWidth : I
      //   1091: invokevirtual getDimensionPixelSize : (II)I
      //   1094: putfield matchConstraintMaxWidth : I
      //   1097: goto -> 2033
      //   1100: astore_2
      //   1101: aload_1
      //   1102: iload #5
      //   1104: aload_0
      //   1105: getfield matchConstraintMaxWidth : I
      //   1108: invokevirtual getInt : (II)I
      //   1111: bipush #-2
      //   1113: if_icmpne -> 2033
      //   1116: aload_0
      //   1117: bipush #-2
      //   1119: putfield matchConstraintMaxWidth : I
      //   1122: goto -> 2033
      //   1125: aload_0
      //   1126: aload_1
      //   1127: iload #5
      //   1129: aload_0
      //   1130: getfield matchConstraintMinWidth : I
      //   1133: invokevirtual getDimensionPixelSize : (II)I
      //   1136: putfield matchConstraintMinWidth : I
      //   1139: goto -> 2033
      //   1142: astore_2
      //   1143: aload_1
      //   1144: iload #5
      //   1146: aload_0
      //   1147: getfield matchConstraintMinWidth : I
      //   1150: invokevirtual getInt : (II)I
      //   1153: bipush #-2
      //   1155: if_icmpne -> 2033
      //   1158: aload_0
      //   1159: bipush #-2
      //   1161: putfield matchConstraintMinWidth : I
      //   1164: goto -> 2033
      //   1167: aload_0
      //   1168: aload_1
      //   1169: iload #5
      //   1171: iconst_0
      //   1172: invokevirtual getInt : (II)I
      //   1175: putfield matchConstraintDefaultHeight : I
      //   1178: aload_0
      //   1179: getfield matchConstraintDefaultHeight : I
      //   1182: iconst_1
      //   1183: if_icmpne -> 2033
      //   1186: ldc_w 'ConstraintLayout'
      //   1189: ldc_w 'layout_constraintHeight_default="wrap" is deprecated.\\nUse layout_height="WRAP_CONTENT" and layout_constrainedHeight="true" instead.'
      //   1192: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
      //   1195: pop
      //   1196: goto -> 2033
      //   1199: aload_0
      //   1200: aload_1
      //   1201: iload #5
      //   1203: iconst_0
      //   1204: invokevirtual getInt : (II)I
      //   1207: putfield matchConstraintDefaultWidth : I
      //   1210: aload_0
      //   1211: getfield matchConstraintDefaultWidth : I
      //   1214: iconst_1
      //   1215: if_icmpne -> 2033
      //   1218: ldc_w 'ConstraintLayout'
      //   1221: ldc_w 'layout_constraintWidth_default="wrap" is deprecated.\\nUse layout_width="WRAP_CONTENT" and layout_constrainedWidth="true" instead.'
      //   1224: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
      //   1227: pop
      //   1228: goto -> 2033
      //   1231: aload_0
      //   1232: aload_1
      //   1233: iload #5
      //   1235: aload_0
      //   1236: getfield verticalBias : F
      //   1239: invokevirtual getFloat : (IF)F
      //   1242: putfield verticalBias : F
      //   1245: goto -> 2033
      //   1248: aload_0
      //   1249: aload_1
      //   1250: iload #5
      //   1252: aload_0
      //   1253: getfield horizontalBias : F
      //   1256: invokevirtual getFloat : (IF)F
      //   1259: putfield horizontalBias : F
      //   1262: goto -> 2033
      //   1265: aload_0
      //   1266: aload_1
      //   1267: iload #5
      //   1269: aload_0
      //   1270: getfield constrainedHeight : Z
      //   1273: invokevirtual getBoolean : (IZ)Z
      //   1276: putfield constrainedHeight : Z
      //   1279: goto -> 2033
      //   1282: aload_0
      //   1283: aload_1
      //   1284: iload #5
      //   1286: aload_0
      //   1287: getfield constrainedWidth : Z
      //   1290: invokevirtual getBoolean : (IZ)Z
      //   1293: putfield constrainedWidth : Z
      //   1296: goto -> 2033
      //   1299: aload_0
      //   1300: aload_1
      //   1301: iload #5
      //   1303: aload_0
      //   1304: getfield goneEndMargin : I
      //   1307: invokevirtual getDimensionPixelSize : (II)I
      //   1310: putfield goneEndMargin : I
      //   1313: goto -> 2033
      //   1316: aload_0
      //   1317: aload_1
      //   1318: iload #5
      //   1320: aload_0
      //   1321: getfield goneStartMargin : I
      //   1324: invokevirtual getDimensionPixelSize : (II)I
      //   1327: putfield goneStartMargin : I
      //   1330: goto -> 2033
      //   1333: aload_0
      //   1334: aload_1
      //   1335: iload #5
      //   1337: aload_0
      //   1338: getfield goneBottomMargin : I
      //   1341: invokevirtual getDimensionPixelSize : (II)I
      //   1344: putfield goneBottomMargin : I
      //   1347: goto -> 2033
      //   1350: aload_0
      //   1351: aload_1
      //   1352: iload #5
      //   1354: aload_0
      //   1355: getfield goneRightMargin : I
      //   1358: invokevirtual getDimensionPixelSize : (II)I
      //   1361: putfield goneRightMargin : I
      //   1364: goto -> 2033
      //   1367: aload_0
      //   1368: aload_1
      //   1369: iload #5
      //   1371: aload_0
      //   1372: getfield goneTopMargin : I
      //   1375: invokevirtual getDimensionPixelSize : (II)I
      //   1378: putfield goneTopMargin : I
      //   1381: goto -> 2033
      //   1384: aload_0
      //   1385: aload_1
      //   1386: iload #5
      //   1388: aload_0
      //   1389: getfield goneLeftMargin : I
      //   1392: invokevirtual getDimensionPixelSize : (II)I
      //   1395: putfield goneLeftMargin : I
      //   1398: goto -> 2033
      //   1401: aload_0
      //   1402: aload_1
      //   1403: iload #5
      //   1405: aload_0
      //   1406: getfield endToEnd : I
      //   1409: invokevirtual getResourceId : (II)I
      //   1412: putfield endToEnd : I
      //   1415: aload_0
      //   1416: getfield endToEnd : I
      //   1419: iconst_m1
      //   1420: if_icmpne -> 2033
      //   1423: aload_0
      //   1424: aload_1
      //   1425: iload #5
      //   1427: iconst_m1
      //   1428: invokevirtual getInt : (II)I
      //   1431: putfield endToEnd : I
      //   1434: goto -> 2033
      //   1437: aload_0
      //   1438: aload_1
      //   1439: iload #5
      //   1441: aload_0
      //   1442: getfield endToStart : I
      //   1445: invokevirtual getResourceId : (II)I
      //   1448: putfield endToStart : I
      //   1451: aload_0
      //   1452: getfield endToStart : I
      //   1455: iconst_m1
      //   1456: if_icmpne -> 2033
      //   1459: aload_0
      //   1460: aload_1
      //   1461: iload #5
      //   1463: iconst_m1
      //   1464: invokevirtual getInt : (II)I
      //   1467: putfield endToStart : I
      //   1470: goto -> 2033
      //   1473: aload_0
      //   1474: aload_1
      //   1475: iload #5
      //   1477: aload_0
      //   1478: getfield startToStart : I
      //   1481: invokevirtual getResourceId : (II)I
      //   1484: putfield startToStart : I
      //   1487: aload_0
      //   1488: getfield startToStart : I
      //   1491: iconst_m1
      //   1492: if_icmpne -> 2033
      //   1495: aload_0
      //   1496: aload_1
      //   1497: iload #5
      //   1499: iconst_m1
      //   1500: invokevirtual getInt : (II)I
      //   1503: putfield startToStart : I
      //   1506: goto -> 2033
      //   1509: aload_0
      //   1510: aload_1
      //   1511: iload #5
      //   1513: aload_0
      //   1514: getfield startToEnd : I
      //   1517: invokevirtual getResourceId : (II)I
      //   1520: putfield startToEnd : I
      //   1523: aload_0
      //   1524: getfield startToEnd : I
      //   1527: iconst_m1
      //   1528: if_icmpne -> 2033
      //   1531: aload_0
      //   1532: aload_1
      //   1533: iload #5
      //   1535: iconst_m1
      //   1536: invokevirtual getInt : (II)I
      //   1539: putfield startToEnd : I
      //   1542: goto -> 2033
      //   1545: aload_0
      //   1546: aload_1
      //   1547: iload #5
      //   1549: aload_0
      //   1550: getfield baselineToBaseline : I
      //   1553: invokevirtual getResourceId : (II)I
      //   1556: putfield baselineToBaseline : I
      //   1559: aload_0
      //   1560: getfield baselineToBaseline : I
      //   1563: iconst_m1
      //   1564: if_icmpne -> 2033
      //   1567: aload_0
      //   1568: aload_1
      //   1569: iload #5
      //   1571: iconst_m1
      //   1572: invokevirtual getInt : (II)I
      //   1575: putfield baselineToBaseline : I
      //   1578: goto -> 2033
      //   1581: aload_0
      //   1582: aload_1
      //   1583: iload #5
      //   1585: aload_0
      //   1586: getfield bottomToBottom : I
      //   1589: invokevirtual getResourceId : (II)I
      //   1592: putfield bottomToBottom : I
      //   1595: aload_0
      //   1596: getfield bottomToBottom : I
      //   1599: iconst_m1
      //   1600: if_icmpne -> 2033
      //   1603: aload_0
      //   1604: aload_1
      //   1605: iload #5
      //   1607: iconst_m1
      //   1608: invokevirtual getInt : (II)I
      //   1611: putfield bottomToBottom : I
      //   1614: goto -> 2033
      //   1617: aload_0
      //   1618: aload_1
      //   1619: iload #5
      //   1621: aload_0
      //   1622: getfield bottomToTop : I
      //   1625: invokevirtual getResourceId : (II)I
      //   1628: putfield bottomToTop : I
      //   1631: aload_0
      //   1632: getfield bottomToTop : I
      //   1635: iconst_m1
      //   1636: if_icmpne -> 2033
      //   1639: aload_0
      //   1640: aload_1
      //   1641: iload #5
      //   1643: iconst_m1
      //   1644: invokevirtual getInt : (II)I
      //   1647: putfield bottomToTop : I
      //   1650: goto -> 2033
      //   1653: aload_0
      //   1654: aload_1
      //   1655: iload #5
      //   1657: aload_0
      //   1658: getfield topToBottom : I
      //   1661: invokevirtual getResourceId : (II)I
      //   1664: putfield topToBottom : I
      //   1667: aload_0
      //   1668: getfield topToBottom : I
      //   1671: iconst_m1
      //   1672: if_icmpne -> 2033
      //   1675: aload_0
      //   1676: aload_1
      //   1677: iload #5
      //   1679: iconst_m1
      //   1680: invokevirtual getInt : (II)I
      //   1683: putfield topToBottom : I
      //   1686: goto -> 2033
      //   1689: aload_0
      //   1690: aload_1
      //   1691: iload #5
      //   1693: aload_0
      //   1694: getfield topToTop : I
      //   1697: invokevirtual getResourceId : (II)I
      //   1700: putfield topToTop : I
      //   1703: aload_0
      //   1704: getfield topToTop : I
      //   1707: iconst_m1
      //   1708: if_icmpne -> 2033
      //   1711: aload_0
      //   1712: aload_1
      //   1713: iload #5
      //   1715: iconst_m1
      //   1716: invokevirtual getInt : (II)I
      //   1719: putfield topToTop : I
      //   1722: goto -> 2033
      //   1725: aload_0
      //   1726: aload_1
      //   1727: iload #5
      //   1729: aload_0
      //   1730: getfield rightToRight : I
      //   1733: invokevirtual getResourceId : (II)I
      //   1736: putfield rightToRight : I
      //   1739: aload_0
      //   1740: getfield rightToRight : I
      //   1743: iconst_m1
      //   1744: if_icmpne -> 2033
      //   1747: aload_0
      //   1748: aload_1
      //   1749: iload #5
      //   1751: iconst_m1
      //   1752: invokevirtual getInt : (II)I
      //   1755: putfield rightToRight : I
      //   1758: goto -> 2033
      //   1761: aload_0
      //   1762: aload_1
      //   1763: iload #5
      //   1765: aload_0
      //   1766: getfield rightToLeft : I
      //   1769: invokevirtual getResourceId : (II)I
      //   1772: putfield rightToLeft : I
      //   1775: aload_0
      //   1776: getfield rightToLeft : I
      //   1779: iconst_m1
      //   1780: if_icmpne -> 2033
      //   1783: aload_0
      //   1784: aload_1
      //   1785: iload #5
      //   1787: iconst_m1
      //   1788: invokevirtual getInt : (II)I
      //   1791: putfield rightToLeft : I
      //   1794: goto -> 2033
      //   1797: aload_0
      //   1798: aload_1
      //   1799: iload #5
      //   1801: aload_0
      //   1802: getfield leftToRight : I
      //   1805: invokevirtual getResourceId : (II)I
      //   1808: putfield leftToRight : I
      //   1811: aload_0
      //   1812: getfield leftToRight : I
      //   1815: iconst_m1
      //   1816: if_icmpne -> 2033
      //   1819: aload_0
      //   1820: aload_1
      //   1821: iload #5
      //   1823: iconst_m1
      //   1824: invokevirtual getInt : (II)I
      //   1827: putfield leftToRight : I
      //   1830: goto -> 2033
      //   1833: aload_0
      //   1834: aload_1
      //   1835: iload #5
      //   1837: aload_0
      //   1838: getfield leftToLeft : I
      //   1841: invokevirtual getResourceId : (II)I
      //   1844: putfield leftToLeft : I
      //   1847: aload_0
      //   1848: getfield leftToLeft : I
      //   1851: iconst_m1
      //   1852: if_icmpne -> 2033
      //   1855: aload_0
      //   1856: aload_1
      //   1857: iload #5
      //   1859: iconst_m1
      //   1860: invokevirtual getInt : (II)I
      //   1863: putfield leftToLeft : I
      //   1866: goto -> 2033
      //   1869: aload_0
      //   1870: aload_1
      //   1871: iload #5
      //   1873: aload_0
      //   1874: getfield guidePercent : F
      //   1877: invokevirtual getFloat : (IF)F
      //   1880: putfield guidePercent : F
      //   1883: goto -> 2033
      //   1886: aload_0
      //   1887: aload_1
      //   1888: iload #5
      //   1890: aload_0
      //   1891: getfield guideEnd : I
      //   1894: invokevirtual getDimensionPixelOffset : (II)I
      //   1897: putfield guideEnd : I
      //   1900: goto -> 2033
      //   1903: aload_0
      //   1904: aload_1
      //   1905: iload #5
      //   1907: aload_0
      //   1908: getfield guideBegin : I
      //   1911: invokevirtual getDimensionPixelOffset : (II)I
      //   1914: putfield guideBegin : I
      //   1917: goto -> 2033
      //   1920: aload_0
      //   1921: aload_1
      //   1922: iload #5
      //   1924: aload_0
      //   1925: getfield circleAngle : F
      //   1928: invokevirtual getFloat : (IF)F
      //   1931: ldc_w 360.0
      //   1934: frem
      //   1935: putfield circleAngle : F
      //   1938: aload_0
      //   1939: getfield circleAngle : F
      //   1942: fconst_0
      //   1943: fcmpg
      //   1944: ifge -> 2033
      //   1947: aload_0
      //   1948: ldc_w 360.0
      //   1951: aload_0
      //   1952: getfield circleAngle : F
      //   1955: fsub
      //   1956: ldc_w 360.0
      //   1959: frem
      //   1960: putfield circleAngle : F
      //   1963: goto -> 2033
      //   1966: aload_0
      //   1967: aload_1
      //   1968: iload #5
      //   1970: aload_0
      //   1971: getfield circleRadius : I
      //   1974: invokevirtual getDimensionPixelSize : (II)I
      //   1977: putfield circleRadius : I
      //   1980: goto -> 2033
      //   1983: aload_0
      //   1984: aload_1
      //   1985: iload #5
      //   1987: aload_0
      //   1988: getfield circleConstraint : I
      //   1991: invokevirtual getResourceId : (II)I
      //   1994: putfield circleConstraint : I
      //   1997: aload_0
      //   1998: getfield circleConstraint : I
      //   2001: iconst_m1
      //   2002: if_icmpne -> 2033
      //   2005: aload_0
      //   2006: aload_1
      //   2007: iload #5
      //   2009: iconst_m1
      //   2010: invokevirtual getInt : (II)I
      //   2013: putfield circleConstraint : I
      //   2016: goto -> 2033
      //   2019: aload_0
      //   2020: aload_1
      //   2021: iload #5
      //   2023: aload_0
      //   2024: getfield orientation : I
      //   2027: invokevirtual getInt : (II)I
      //   2030: putfield orientation : I
      //   2033: iinc #4, 1
      //   2036: goto -> 345
      //   2039: aload_1
      //   2040: invokevirtual recycle : ()V
      //   2043: aload_0
      //   2044: invokevirtual validate : ()V
      //   2047: return
      //   2048: astore_2
      //   2049: goto -> 2033
      // Exception table:
      //   from	to	target	type
      //   864	877	2048	java/lang/NumberFormatException
      //   891	911	2048	java/lang/NumberFormatException
      //   914	926	2048	java/lang/NumberFormatException
      //   946	954	2048	java/lang/NumberFormatException
      //   978	992	995	java/lang/Exception
      //   1020	1034	1037	java/lang/Exception
      //   1083	1097	1100	java/lang/Exception
      //   1125	1139	1142	java/lang/Exception
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
      this.guideBegin = param1LayoutParams.guideBegin;
      this.guideEnd = param1LayoutParams.guideEnd;
      this.guidePercent = param1LayoutParams.guidePercent;
      this.leftToLeft = param1LayoutParams.leftToLeft;
      this.leftToRight = param1LayoutParams.leftToRight;
      this.rightToLeft = param1LayoutParams.rightToLeft;
      this.rightToRight = param1LayoutParams.rightToRight;
      this.topToTop = param1LayoutParams.topToTop;
      this.topToBottom = param1LayoutParams.topToBottom;
      this.bottomToTop = param1LayoutParams.bottomToTop;
      this.bottomToBottom = param1LayoutParams.bottomToBottom;
      this.baselineToBaseline = param1LayoutParams.baselineToBaseline;
      this.circleConstraint = param1LayoutParams.circleConstraint;
      this.circleRadius = param1LayoutParams.circleRadius;
      this.circleAngle = param1LayoutParams.circleAngle;
      this.startToEnd = param1LayoutParams.startToEnd;
      this.startToStart = param1LayoutParams.startToStart;
      this.endToStart = param1LayoutParams.endToStart;
      this.endToEnd = param1LayoutParams.endToEnd;
      this.goneLeftMargin = param1LayoutParams.goneLeftMargin;
      this.goneTopMargin = param1LayoutParams.goneTopMargin;
      this.goneRightMargin = param1LayoutParams.goneRightMargin;
      this.goneBottomMargin = param1LayoutParams.goneBottomMargin;
      this.goneStartMargin = param1LayoutParams.goneStartMargin;
      this.goneEndMargin = param1LayoutParams.goneEndMargin;
      this.horizontalBias = param1LayoutParams.horizontalBias;
      this.verticalBias = param1LayoutParams.verticalBias;
      this.dimensionRatio = param1LayoutParams.dimensionRatio;
      this.dimensionRatioValue = param1LayoutParams.dimensionRatioValue;
      this.dimensionRatioSide = param1LayoutParams.dimensionRatioSide;
      this.horizontalWeight = param1LayoutParams.horizontalWeight;
      this.verticalWeight = param1LayoutParams.verticalWeight;
      this.horizontalChainStyle = param1LayoutParams.horizontalChainStyle;
      this.verticalChainStyle = param1LayoutParams.verticalChainStyle;
      this.constrainedWidth = param1LayoutParams.constrainedWidth;
      this.constrainedHeight = param1LayoutParams.constrainedHeight;
      this.matchConstraintDefaultWidth = param1LayoutParams.matchConstraintDefaultWidth;
      this.matchConstraintDefaultHeight = param1LayoutParams.matchConstraintDefaultHeight;
      this.matchConstraintMinWidth = param1LayoutParams.matchConstraintMinWidth;
      this.matchConstraintMaxWidth = param1LayoutParams.matchConstraintMaxWidth;
      this.matchConstraintMinHeight = param1LayoutParams.matchConstraintMinHeight;
      this.matchConstraintMaxHeight = param1LayoutParams.matchConstraintMaxHeight;
      this.matchConstraintPercentWidth = param1LayoutParams.matchConstraintPercentWidth;
      this.matchConstraintPercentHeight = param1LayoutParams.matchConstraintPercentHeight;
      this.editorAbsoluteX = param1LayoutParams.editorAbsoluteX;
      this.editorAbsoluteY = param1LayoutParams.editorAbsoluteY;
      this.orientation = param1LayoutParams.orientation;
      this.horizontalDimensionFixed = param1LayoutParams.horizontalDimensionFixed;
      this.verticalDimensionFixed = param1LayoutParams.verticalDimensionFixed;
      this.needsBaseline = param1LayoutParams.needsBaseline;
      this.isGuideline = param1LayoutParams.isGuideline;
      this.resolvedLeftToLeft = param1LayoutParams.resolvedLeftToLeft;
      this.resolvedLeftToRight = param1LayoutParams.resolvedLeftToRight;
      this.resolvedRightToLeft = param1LayoutParams.resolvedRightToLeft;
      this.resolvedRightToRight = param1LayoutParams.resolvedRightToRight;
      this.resolveGoneLeftMargin = param1LayoutParams.resolveGoneLeftMargin;
      this.resolveGoneRightMargin = param1LayoutParams.resolveGoneRightMargin;
      this.resolvedHorizontalBias = param1LayoutParams.resolvedHorizontalBias;
      this.widget = param1LayoutParams.widget;
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public void reset() {
      if (this.widget != null)
        this.widget.reset(); 
    }
    
    @TargetApi(17)
    public void resolveLayoutDirection(int param1Int) {
      // Byte code:
      //   0: aload_0
      //   1: getfield leftMargin : I
      //   4: istore_2
      //   5: aload_0
      //   6: getfield rightMargin : I
      //   9: istore_3
      //   10: aload_0
      //   11: iload_1
      //   12: invokespecial resolveLayoutDirection : (I)V
      //   15: aload_0
      //   16: iconst_m1
      //   17: putfield resolvedRightToLeft : I
      //   20: aload_0
      //   21: iconst_m1
      //   22: putfield resolvedRightToRight : I
      //   25: aload_0
      //   26: iconst_m1
      //   27: putfield resolvedLeftToLeft : I
      //   30: aload_0
      //   31: iconst_m1
      //   32: putfield resolvedLeftToRight : I
      //   35: aload_0
      //   36: iconst_m1
      //   37: putfield resolveGoneLeftMargin : I
      //   40: aload_0
      //   41: iconst_m1
      //   42: putfield resolveGoneRightMargin : I
      //   45: aload_0
      //   46: aload_0
      //   47: getfield goneLeftMargin : I
      //   50: putfield resolveGoneLeftMargin : I
      //   53: aload_0
      //   54: aload_0
      //   55: getfield goneRightMargin : I
      //   58: putfield resolveGoneRightMargin : I
      //   61: aload_0
      //   62: aload_0
      //   63: getfield horizontalBias : F
      //   66: putfield resolvedHorizontalBias : F
      //   69: aload_0
      //   70: aload_0
      //   71: getfield guideBegin : I
      //   74: putfield resolvedGuideBegin : I
      //   77: aload_0
      //   78: aload_0
      //   79: getfield guideEnd : I
      //   82: putfield resolvedGuideEnd : I
      //   85: aload_0
      //   86: aload_0
      //   87: getfield guidePercent : F
      //   90: putfield resolvedGuidePercent : F
      //   93: aload_0
      //   94: invokevirtual getLayoutDirection : ()I
      //   97: istore_1
      //   98: iconst_0
      //   99: istore #4
      //   101: iconst_1
      //   102: iload_1
      //   103: if_icmpne -> 111
      //   106: iconst_1
      //   107: istore_1
      //   108: goto -> 113
      //   111: iconst_0
      //   112: istore_1
      //   113: iload_1
      //   114: ifeq -> 350
      //   117: aload_0
      //   118: getfield startToEnd : I
      //   121: iconst_m1
      //   122: if_icmpeq -> 138
      //   125: aload_0
      //   126: aload_0
      //   127: getfield startToEnd : I
      //   130: putfield resolvedRightToLeft : I
      //   133: iconst_1
      //   134: istore_1
      //   135: goto -> 160
      //   138: iload #4
      //   140: istore_1
      //   141: aload_0
      //   142: getfield startToStart : I
      //   145: iconst_m1
      //   146: if_icmpeq -> 160
      //   149: aload_0
      //   150: aload_0
      //   151: getfield startToStart : I
      //   154: putfield resolvedRightToRight : I
      //   157: goto -> 133
      //   160: aload_0
      //   161: getfield endToStart : I
      //   164: iconst_m1
      //   165: if_icmpeq -> 178
      //   168: aload_0
      //   169: aload_0
      //   170: getfield endToStart : I
      //   173: putfield resolvedLeftToRight : I
      //   176: iconst_1
      //   177: istore_1
      //   178: aload_0
      //   179: getfield endToEnd : I
      //   182: iconst_m1
      //   183: if_icmpeq -> 196
      //   186: aload_0
      //   187: aload_0
      //   188: getfield endToEnd : I
      //   191: putfield resolvedLeftToLeft : I
      //   194: iconst_1
      //   195: istore_1
      //   196: aload_0
      //   197: getfield goneStartMargin : I
      //   200: iconst_m1
      //   201: if_icmpeq -> 212
      //   204: aload_0
      //   205: aload_0
      //   206: getfield goneStartMargin : I
      //   209: putfield resolveGoneRightMargin : I
      //   212: aload_0
      //   213: getfield goneEndMargin : I
      //   216: iconst_m1
      //   217: if_icmpeq -> 228
      //   220: aload_0
      //   221: aload_0
      //   222: getfield goneEndMargin : I
      //   225: putfield resolveGoneLeftMargin : I
      //   228: iload_1
      //   229: ifeq -> 242
      //   232: aload_0
      //   233: fconst_1
      //   234: aload_0
      //   235: getfield horizontalBias : F
      //   238: fsub
      //   239: putfield resolvedHorizontalBias : F
      //   242: aload_0
      //   243: getfield isGuideline : Z
      //   246: ifeq -> 446
      //   249: aload_0
      //   250: getfield orientation : I
      //   253: iconst_1
      //   254: if_icmpne -> 446
      //   257: aload_0
      //   258: getfield guidePercent : F
      //   261: ldc -1.0
      //   263: fcmpl
      //   264: ifeq -> 290
      //   267: aload_0
      //   268: fconst_1
      //   269: aload_0
      //   270: getfield guidePercent : F
      //   273: fsub
      //   274: putfield resolvedGuidePercent : F
      //   277: aload_0
      //   278: iconst_m1
      //   279: putfield resolvedGuideBegin : I
      //   282: aload_0
      //   283: iconst_m1
      //   284: putfield resolvedGuideEnd : I
      //   287: goto -> 446
      //   290: aload_0
      //   291: getfield guideBegin : I
      //   294: iconst_m1
      //   295: if_icmpeq -> 320
      //   298: aload_0
      //   299: aload_0
      //   300: getfield guideBegin : I
      //   303: putfield resolvedGuideEnd : I
      //   306: aload_0
      //   307: iconst_m1
      //   308: putfield resolvedGuideBegin : I
      //   311: aload_0
      //   312: ldc -1.0
      //   314: putfield resolvedGuidePercent : F
      //   317: goto -> 446
      //   320: aload_0
      //   321: getfield guideEnd : I
      //   324: iconst_m1
      //   325: if_icmpeq -> 446
      //   328: aload_0
      //   329: aload_0
      //   330: getfield guideEnd : I
      //   333: putfield resolvedGuideBegin : I
      //   336: aload_0
      //   337: iconst_m1
      //   338: putfield resolvedGuideEnd : I
      //   341: aload_0
      //   342: ldc -1.0
      //   344: putfield resolvedGuidePercent : F
      //   347: goto -> 446
      //   350: aload_0
      //   351: getfield startToEnd : I
      //   354: iconst_m1
      //   355: if_icmpeq -> 366
      //   358: aload_0
      //   359: aload_0
      //   360: getfield startToEnd : I
      //   363: putfield resolvedLeftToRight : I
      //   366: aload_0
      //   367: getfield startToStart : I
      //   370: iconst_m1
      //   371: if_icmpeq -> 382
      //   374: aload_0
      //   375: aload_0
      //   376: getfield startToStart : I
      //   379: putfield resolvedLeftToLeft : I
      //   382: aload_0
      //   383: getfield endToStart : I
      //   386: iconst_m1
      //   387: if_icmpeq -> 398
      //   390: aload_0
      //   391: aload_0
      //   392: getfield endToStart : I
      //   395: putfield resolvedRightToLeft : I
      //   398: aload_0
      //   399: getfield endToEnd : I
      //   402: iconst_m1
      //   403: if_icmpeq -> 414
      //   406: aload_0
      //   407: aload_0
      //   408: getfield endToEnd : I
      //   411: putfield resolvedRightToRight : I
      //   414: aload_0
      //   415: getfield goneStartMargin : I
      //   418: iconst_m1
      //   419: if_icmpeq -> 430
      //   422: aload_0
      //   423: aload_0
      //   424: getfield goneStartMargin : I
      //   427: putfield resolveGoneLeftMargin : I
      //   430: aload_0
      //   431: getfield goneEndMargin : I
      //   434: iconst_m1
      //   435: if_icmpeq -> 446
      //   438: aload_0
      //   439: aload_0
      //   440: getfield goneEndMargin : I
      //   443: putfield resolveGoneRightMargin : I
      //   446: aload_0
      //   447: getfield endToStart : I
      //   450: iconst_m1
      //   451: if_icmpne -> 612
      //   454: aload_0
      //   455: getfield endToEnd : I
      //   458: iconst_m1
      //   459: if_icmpne -> 612
      //   462: aload_0
      //   463: getfield startToStart : I
      //   466: iconst_m1
      //   467: if_icmpne -> 612
      //   470: aload_0
      //   471: getfield startToEnd : I
      //   474: iconst_m1
      //   475: if_icmpne -> 612
      //   478: aload_0
      //   479: getfield rightToLeft : I
      //   482: iconst_m1
      //   483: if_icmpeq -> 513
      //   486: aload_0
      //   487: aload_0
      //   488: getfield rightToLeft : I
      //   491: putfield resolvedRightToLeft : I
      //   494: aload_0
      //   495: getfield rightMargin : I
      //   498: ifgt -> 545
      //   501: iload_3
      //   502: ifle -> 545
      //   505: aload_0
      //   506: iload_3
      //   507: putfield rightMargin : I
      //   510: goto -> 545
      //   513: aload_0
      //   514: getfield rightToRight : I
      //   517: iconst_m1
      //   518: if_icmpeq -> 545
      //   521: aload_0
      //   522: aload_0
      //   523: getfield rightToRight : I
      //   526: putfield resolvedRightToRight : I
      //   529: aload_0
      //   530: getfield rightMargin : I
      //   533: ifgt -> 545
      //   536: iload_3
      //   537: ifle -> 545
      //   540: aload_0
      //   541: iload_3
      //   542: putfield rightMargin : I
      //   545: aload_0
      //   546: getfield leftToLeft : I
      //   549: iconst_m1
      //   550: if_icmpeq -> 580
      //   553: aload_0
      //   554: aload_0
      //   555: getfield leftToLeft : I
      //   558: putfield resolvedLeftToLeft : I
      //   561: aload_0
      //   562: getfield leftMargin : I
      //   565: ifgt -> 612
      //   568: iload_2
      //   569: ifle -> 612
      //   572: aload_0
      //   573: iload_2
      //   574: putfield leftMargin : I
      //   577: goto -> 612
      //   580: aload_0
      //   581: getfield leftToRight : I
      //   584: iconst_m1
      //   585: if_icmpeq -> 612
      //   588: aload_0
      //   589: aload_0
      //   590: getfield leftToRight : I
      //   593: putfield resolvedLeftToRight : I
      //   596: aload_0
      //   597: getfield leftMargin : I
      //   600: ifgt -> 612
      //   603: iload_2
      //   604: ifle -> 612
      //   607: aload_0
      //   608: iload_2
      //   609: putfield leftMargin : I
      //   612: return
    }
    
    public void validate() {
      this.isGuideline = false;
      this.horizontalDimensionFixed = true;
      this.verticalDimensionFixed = true;
      if (this.width == -2 && this.constrainedWidth) {
        this.horizontalDimensionFixed = false;
        this.matchConstraintDefaultWidth = 1;
      } 
      if (this.height == -2 && this.constrainedHeight) {
        this.verticalDimensionFixed = false;
        this.matchConstraintDefaultHeight = 1;
      } 
      if (this.width == 0 || this.width == -1) {
        this.horizontalDimensionFixed = false;
        if (this.width == 0 && this.matchConstraintDefaultWidth == 1) {
          this.width = -2;
          this.constrainedWidth = true;
        } 
      } 
      if (this.height == 0 || this.height == -1) {
        this.verticalDimensionFixed = false;
        if (this.height == 0 && this.matchConstraintDefaultHeight == 1) {
          this.height = -2;
          this.constrainedHeight = true;
        } 
      } 
      if (this.guidePercent != -1.0F || this.guideBegin != -1 || this.guideEnd != -1) {
        this.isGuideline = true;
        this.horizontalDimensionFixed = true;
        this.verticalDimensionFixed = true;
        if (!(this.widget instanceof Guideline))
          this.widget = (ConstraintWidget)new Guideline(); 
        ((Guideline)this.widget).setOrientation(this.orientation);
      } 
    }
    
    private static class Table {
      public static final int ANDROID_ORIENTATION = 1;
      
      public static final int LAYOUT_CONSTRAINED_HEIGHT = 28;
      
      public static final int LAYOUT_CONSTRAINED_WIDTH = 27;
      
      public static final int LAYOUT_CONSTRAINT_BASELINE_CREATOR = 43;
      
      public static final int LAYOUT_CONSTRAINT_BASELINE_TO_BASELINE_OF = 16;
      
      public static final int LAYOUT_CONSTRAINT_BOTTOM_CREATOR = 42;
      
      public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_BOTTOM_OF = 15;
      
      public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_TOP_OF = 14;
      
      public static final int LAYOUT_CONSTRAINT_CIRCLE = 2;
      
      public static final int LAYOUT_CONSTRAINT_CIRCLE_ANGLE = 4;
      
      public static final int LAYOUT_CONSTRAINT_CIRCLE_RADIUS = 3;
      
      public static final int LAYOUT_CONSTRAINT_DIMENSION_RATIO = 44;
      
      public static final int LAYOUT_CONSTRAINT_END_TO_END_OF = 20;
      
      public static final int LAYOUT_CONSTRAINT_END_TO_START_OF = 19;
      
      public static final int LAYOUT_CONSTRAINT_GUIDE_BEGIN = 5;
      
      public static final int LAYOUT_CONSTRAINT_GUIDE_END = 6;
      
      public static final int LAYOUT_CONSTRAINT_GUIDE_PERCENT = 7;
      
      public static final int LAYOUT_CONSTRAINT_HEIGHT_DEFAULT = 32;
      
      public static final int LAYOUT_CONSTRAINT_HEIGHT_MAX = 37;
      
      public static final int LAYOUT_CONSTRAINT_HEIGHT_MIN = 36;
      
      public static final int LAYOUT_CONSTRAINT_HEIGHT_PERCENT = 38;
      
      public static final int LAYOUT_CONSTRAINT_HORIZONTAL_BIAS = 29;
      
      public static final int LAYOUT_CONSTRAINT_HORIZONTAL_CHAINSTYLE = 47;
      
      public static final int LAYOUT_CONSTRAINT_HORIZONTAL_WEIGHT = 45;
      
      public static final int LAYOUT_CONSTRAINT_LEFT_CREATOR = 39;
      
      public static final int LAYOUT_CONSTRAINT_LEFT_TO_LEFT_OF = 8;
      
      public static final int LAYOUT_CONSTRAINT_LEFT_TO_RIGHT_OF = 9;
      
      public static final int LAYOUT_CONSTRAINT_RIGHT_CREATOR = 41;
      
      public static final int LAYOUT_CONSTRAINT_RIGHT_TO_LEFT_OF = 10;
      
      public static final int LAYOUT_CONSTRAINT_RIGHT_TO_RIGHT_OF = 11;
      
      public static final int LAYOUT_CONSTRAINT_START_TO_END_OF = 17;
      
      public static final int LAYOUT_CONSTRAINT_START_TO_START_OF = 18;
      
      public static final int LAYOUT_CONSTRAINT_TOP_CREATOR = 40;
      
      public static final int LAYOUT_CONSTRAINT_TOP_TO_BOTTOM_OF = 13;
      
      public static final int LAYOUT_CONSTRAINT_TOP_TO_TOP_OF = 12;
      
      public static final int LAYOUT_CONSTRAINT_VERTICAL_BIAS = 30;
      
      public static final int LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE = 48;
      
      public static final int LAYOUT_CONSTRAINT_VERTICAL_WEIGHT = 46;
      
      public static final int LAYOUT_CONSTRAINT_WIDTH_DEFAULT = 31;
      
      public static final int LAYOUT_CONSTRAINT_WIDTH_MAX = 34;
      
      public static final int LAYOUT_CONSTRAINT_WIDTH_MIN = 33;
      
      public static final int LAYOUT_CONSTRAINT_WIDTH_PERCENT = 35;
      
      public static final int LAYOUT_EDITOR_ABSOLUTEX = 49;
      
      public static final int LAYOUT_EDITOR_ABSOLUTEY = 50;
      
      public static final int LAYOUT_GONE_MARGIN_BOTTOM = 24;
      
      public static final int LAYOUT_GONE_MARGIN_END = 26;
      
      public static final int LAYOUT_GONE_MARGIN_LEFT = 21;
      
      public static final int LAYOUT_GONE_MARGIN_RIGHT = 23;
      
      public static final int LAYOUT_GONE_MARGIN_START = 25;
      
      public static final int LAYOUT_GONE_MARGIN_TOP = 22;
      
      public static final int UNUSED = 0;
      
      public static final SparseIntArray map = new SparseIntArray();
      
      static {
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toLeftOf, 8);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toRightOf, 9);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toLeftOf, 10);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toRightOf, 11);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toTopOf, 12);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toBottomOf, 13);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toTopOf, 14);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toBottomOf, 15);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_toBaselineOf, 16);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircle, 2);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleRadius, 3);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleAngle, 4);
        map.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteX, 49);
        map.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteY, 50);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_begin, 5);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_end, 6);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_percent, 7);
        map.append(R.styleable.ConstraintLayout_Layout_android_orientation, 1);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toEndOf, 17);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toStartOf, 18);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toStartOf, 19);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toEndOf, 20);
        map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginLeft, 21);
        map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginTop, 22);
        map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginRight, 23);
        map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginBottom, 24);
        map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginStart, 25);
        map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginEnd, 26);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_bias, 29);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_bias, 30);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintDimensionRatio, 44);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_weight, 45);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_weight, 46);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_chainStyle, 47);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_chainStyle, 48);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constrainedWidth, 27);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constrainedHeight, 28);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_default, 31);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_default, 32);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_min, 33);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_max, 34);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_percent, 35);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_min, 36);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_max, 37);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_percent, 38);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_creator, 39);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_creator, 40);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_creator, 41);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_creator, 42);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_creator, 43);
      }
    }
  }
  
  private static class Table {
    public static final int ANDROID_ORIENTATION = 1;
    
    public static final int LAYOUT_CONSTRAINED_HEIGHT = 28;
    
    public static final int LAYOUT_CONSTRAINED_WIDTH = 27;
    
    public static final int LAYOUT_CONSTRAINT_BASELINE_CREATOR = 43;
    
    public static final int LAYOUT_CONSTRAINT_BASELINE_TO_BASELINE_OF = 16;
    
    public static final int LAYOUT_CONSTRAINT_BOTTOM_CREATOR = 42;
    
    public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_BOTTOM_OF = 15;
    
    public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_TOP_OF = 14;
    
    public static final int LAYOUT_CONSTRAINT_CIRCLE = 2;
    
    public static final int LAYOUT_CONSTRAINT_CIRCLE_ANGLE = 4;
    
    public static final int LAYOUT_CONSTRAINT_CIRCLE_RADIUS = 3;
    
    public static final int LAYOUT_CONSTRAINT_DIMENSION_RATIO = 44;
    
    public static final int LAYOUT_CONSTRAINT_END_TO_END_OF = 20;
    
    public static final int LAYOUT_CONSTRAINT_END_TO_START_OF = 19;
    
    public static final int LAYOUT_CONSTRAINT_GUIDE_BEGIN = 5;
    
    public static final int LAYOUT_CONSTRAINT_GUIDE_END = 6;
    
    public static final int LAYOUT_CONSTRAINT_GUIDE_PERCENT = 7;
    
    public static final int LAYOUT_CONSTRAINT_HEIGHT_DEFAULT = 32;
    
    public static final int LAYOUT_CONSTRAINT_HEIGHT_MAX = 37;
    
    public static final int LAYOUT_CONSTRAINT_HEIGHT_MIN = 36;
    
    public static final int LAYOUT_CONSTRAINT_HEIGHT_PERCENT = 38;
    
    public static final int LAYOUT_CONSTRAINT_HORIZONTAL_BIAS = 29;
    
    public static final int LAYOUT_CONSTRAINT_HORIZONTAL_CHAINSTYLE = 47;
    
    public static final int LAYOUT_CONSTRAINT_HORIZONTAL_WEIGHT = 45;
    
    public static final int LAYOUT_CONSTRAINT_LEFT_CREATOR = 39;
    
    public static final int LAYOUT_CONSTRAINT_LEFT_TO_LEFT_OF = 8;
    
    public static final int LAYOUT_CONSTRAINT_LEFT_TO_RIGHT_OF = 9;
    
    public static final int LAYOUT_CONSTRAINT_RIGHT_CREATOR = 41;
    
    public static final int LAYOUT_CONSTRAINT_RIGHT_TO_LEFT_OF = 10;
    
    public static final int LAYOUT_CONSTRAINT_RIGHT_TO_RIGHT_OF = 11;
    
    public static final int LAYOUT_CONSTRAINT_START_TO_END_OF = 17;
    
    public static final int LAYOUT_CONSTRAINT_START_TO_START_OF = 18;
    
    public static final int LAYOUT_CONSTRAINT_TOP_CREATOR = 40;
    
    public static final int LAYOUT_CONSTRAINT_TOP_TO_BOTTOM_OF = 13;
    
    public static final int LAYOUT_CONSTRAINT_TOP_TO_TOP_OF = 12;
    
    public static final int LAYOUT_CONSTRAINT_VERTICAL_BIAS = 30;
    
    public static final int LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE = 48;
    
    public static final int LAYOUT_CONSTRAINT_VERTICAL_WEIGHT = 46;
    
    public static final int LAYOUT_CONSTRAINT_WIDTH_DEFAULT = 31;
    
    public static final int LAYOUT_CONSTRAINT_WIDTH_MAX = 34;
    
    public static final int LAYOUT_CONSTRAINT_WIDTH_MIN = 33;
    
    public static final int LAYOUT_CONSTRAINT_WIDTH_PERCENT = 35;
    
    public static final int LAYOUT_EDITOR_ABSOLUTEX = 49;
    
    public static final int LAYOUT_EDITOR_ABSOLUTEY = 50;
    
    public static final int LAYOUT_GONE_MARGIN_BOTTOM = 24;
    
    public static final int LAYOUT_GONE_MARGIN_END = 26;
    
    public static final int LAYOUT_GONE_MARGIN_LEFT = 21;
    
    public static final int LAYOUT_GONE_MARGIN_RIGHT = 23;
    
    public static final int LAYOUT_GONE_MARGIN_START = 25;
    
    public static final int LAYOUT_GONE_MARGIN_TOP = 22;
    
    public static final int UNUSED = 0;
    
    public static final SparseIntArray map = new SparseIntArray();
    
    static {
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toLeftOf, 8);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toRightOf, 9);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toLeftOf, 10);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toRightOf, 11);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toTopOf, 12);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toBottomOf, 13);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toTopOf, 14);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toBottomOf, 15);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_toBaselineOf, 16);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircle, 2);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleRadius, 3);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleAngle, 4);
      map.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteX, 49);
      map.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteY, 50);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_begin, 5);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_end, 6);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_percent, 7);
      map.append(R.styleable.ConstraintLayout_Layout_android_orientation, 1);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toEndOf, 17);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toStartOf, 18);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toStartOf, 19);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toEndOf, 20);
      map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginLeft, 21);
      map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginTop, 22);
      map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginRight, 23);
      map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginBottom, 24);
      map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginStart, 25);
      map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginEnd, 26);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_bias, 29);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_bias, 30);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintDimensionRatio, 44);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_weight, 45);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_weight, 46);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_chainStyle, 47);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_chainStyle, 48);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constrainedWidth, 27);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constrainedHeight, 28);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_default, 31);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_default, 32);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_min, 33);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_max, 34);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_percent, 35);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_min, 36);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_max, 37);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_percent, 38);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_creator, 39);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_creator, 40);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_creator, 41);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_creator, 42);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_creator, 43);
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\constraint\ConstraintLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */