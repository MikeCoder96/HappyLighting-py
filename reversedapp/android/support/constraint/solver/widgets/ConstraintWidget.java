package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Cache;
import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.SolverVariable;
import java.util.ArrayList;

public class ConstraintWidget {
  protected static final int ANCHOR_BASELINE = 4;
  
  protected static final int ANCHOR_BOTTOM = 3;
  
  protected static final int ANCHOR_LEFT = 0;
  
  protected static final int ANCHOR_RIGHT = 1;
  
  protected static final int ANCHOR_TOP = 2;
  
  private static final boolean AUTOTAG_CENTER = false;
  
  public static final int CHAIN_PACKED = 2;
  
  public static final int CHAIN_SPREAD = 0;
  
  public static final int CHAIN_SPREAD_INSIDE = 1;
  
  public static float DEFAULT_BIAS = 0.5F;
  
  static final int DIMENSION_HORIZONTAL = 0;
  
  static final int DIMENSION_VERTICAL = 1;
  
  protected static final int DIRECT = 2;
  
  public static final int GONE = 8;
  
  public static final int HORIZONTAL = 0;
  
  public static final int INVISIBLE = 4;
  
  public static final int MATCH_CONSTRAINT_PERCENT = 2;
  
  public static final int MATCH_CONSTRAINT_RATIO = 3;
  
  public static final int MATCH_CONSTRAINT_RATIO_RESOLVED = 4;
  
  public static final int MATCH_CONSTRAINT_SPREAD = 0;
  
  public static final int MATCH_CONSTRAINT_WRAP = 1;
  
  protected static final int SOLVER = 1;
  
  public static final int UNKNOWN = -1;
  
  public static final int VERTICAL = 1;
  
  public static final int VISIBLE = 0;
  
  private static final int WRAP = -2;
  
  protected ArrayList<ConstraintAnchor> mAnchors = new ArrayList<ConstraintAnchor>();
  
  ConstraintAnchor mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
  
  int mBaselineDistance = 0;
  
  ConstraintWidgetGroup mBelongingGroup = null;
  
  ConstraintAnchor mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
  
  boolean mBottomHasCentered;
  
  ConstraintAnchor mCenter = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
  
  ConstraintAnchor mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
  
  ConstraintAnchor mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
  
  private float mCircleConstraintAngle = 0.0F;
  
  private Object mCompanionWidget;
  
  private int mContainerItemSkip = 0;
  
  private String mDebugName = null;
  
  protected float mDimensionRatio = 0.0F;
  
  protected int mDimensionRatioSide = -1;
  
  int mDistToBottom;
  
  int mDistToLeft;
  
  int mDistToRight;
  
  int mDistToTop;
  
  private int mDrawHeight = 0;
  
  private int mDrawWidth = 0;
  
  private int mDrawX = 0;
  
  private int mDrawY = 0;
  
  boolean mGroupsToSolver = false;
  
  int mHeight = 0;
  
  float mHorizontalBiasPercent = DEFAULT_BIAS;
  
  boolean mHorizontalChainFixedPosition;
  
  int mHorizontalChainStyle = 0;
  
  ConstraintWidget mHorizontalNextWidget = null;
  
  public int mHorizontalResolution = -1;
  
  boolean mHorizontalWrapVisited;
  
  boolean mIsHeightWrapContent;
  
  boolean mIsWidthWrapContent;
  
  ConstraintAnchor mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
  
  boolean mLeftHasCentered;
  
  protected ConstraintAnchor[] mListAnchors = new ConstraintAnchor[] { this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, this.mCenter };
  
  protected DimensionBehaviour[] mListDimensionBehaviors = new DimensionBehaviour[] { DimensionBehaviour.FIXED, DimensionBehaviour.FIXED };
  
  protected ConstraintWidget[] mListNextMatchConstraintsWidget = new ConstraintWidget[] { null, null };
  
  int mMatchConstraintDefaultHeight = 0;
  
  int mMatchConstraintDefaultWidth = 0;
  
  int mMatchConstraintMaxHeight = 0;
  
  int mMatchConstraintMaxWidth = 0;
  
  int mMatchConstraintMinHeight = 0;
  
  int mMatchConstraintMinWidth = 0;
  
  float mMatchConstraintPercentHeight = 1.0F;
  
  float mMatchConstraintPercentWidth = 1.0F;
  
  private int[] mMaxDimension = new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE };
  
  protected int mMinHeight;
  
  protected int mMinWidth;
  
  protected ConstraintWidget[] mNextChainWidget = new ConstraintWidget[] { null, null };
  
  protected int mOffsetX = 0;
  
  protected int mOffsetY = 0;
  
  boolean mOptimizerMeasurable = false;
  
  boolean mOptimizerMeasured = false;
  
  ConstraintWidget mParent = null;
  
  int mRelX = 0;
  
  int mRelY = 0;
  
  ResolutionDimension mResolutionHeight;
  
  ResolutionDimension mResolutionWidth;
  
  float mResolvedDimensionRatio = 1.0F;
  
  int mResolvedDimensionRatioSide = -1;
  
  int[] mResolvedMatchConstraintDefault = new int[2];
  
  ConstraintAnchor mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
  
  boolean mRightHasCentered;
  
  ConstraintAnchor mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
  
  boolean mTopHasCentered;
  
  private String mType = null;
  
  float mVerticalBiasPercent = DEFAULT_BIAS;
  
  boolean mVerticalChainFixedPosition;
  
  int mVerticalChainStyle = 0;
  
  ConstraintWidget mVerticalNextWidget = null;
  
  public int mVerticalResolution = -1;
  
  boolean mVerticalWrapVisited;
  
  private int mVisibility = 0;
  
  float[] mWeight = new float[] { -1.0F, -1.0F };
  
  int mWidth = 0;
  
  private int mWrapHeight;
  
  private int mWrapWidth;
  
  protected int mX = 0;
  
  protected int mY = 0;
  
  public ConstraintWidget() {
    addAnchors();
  }
  
  public ConstraintWidget(int paramInt1, int paramInt2) {
    this(0, 0, paramInt1, paramInt2);
  }
  
  public ConstraintWidget(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.mX = paramInt1;
    this.mY = paramInt2;
    this.mWidth = paramInt3;
    this.mHeight = paramInt4;
    addAnchors();
    forceUpdateDrawPosition();
  }
  
  private void addAnchors() {
    this.mAnchors.add(this.mLeft);
    this.mAnchors.add(this.mTop);
    this.mAnchors.add(this.mRight);
    this.mAnchors.add(this.mBottom);
    this.mAnchors.add(this.mCenterX);
    this.mAnchors.add(this.mCenterY);
    this.mAnchors.add(this.mCenter);
    this.mAnchors.add(this.mBaseline);
  }
  
  private void applyConstraints(LinearSystem paramLinearSystem, boolean paramBoolean1, SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, DimensionBehaviour paramDimensionBehaviour, boolean paramBoolean2, ConstraintAnchor paramConstraintAnchor1, ConstraintAnchor paramConstraintAnchor2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, float paramFloat1, boolean paramBoolean3, boolean paramBoolean4, int paramInt5, int paramInt6, int paramInt7, float paramFloat2, boolean paramBoolean5) {
    // Byte code:
    //   0: aload_1
    //   1: aload #7
    //   3: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   6: astore #21
    //   8: aload_1
    //   9: aload #8
    //   11: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   14: astore #22
    //   16: aload_1
    //   17: aload #7
    //   19: invokevirtual getTarget : ()Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   22: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   25: astore #23
    //   27: aload_1
    //   28: aload #8
    //   30: invokevirtual getTarget : ()Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   33: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   36: astore #24
    //   38: aload_1
    //   39: getfield graphOptimizer : Z
    //   42: ifeq -> 128
    //   45: aload #7
    //   47: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   50: getfield state : I
    //   53: iconst_1
    //   54: if_icmpne -> 128
    //   57: aload #8
    //   59: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   62: getfield state : I
    //   65: iconst_1
    //   66: if_icmpne -> 128
    //   69: invokestatic getMetrics : ()Landroid/support/constraint/solver/Metrics;
    //   72: ifnull -> 89
    //   75: invokestatic getMetrics : ()Landroid/support/constraint/solver/Metrics;
    //   78: astore_3
    //   79: aload_3
    //   80: aload_3
    //   81: getfield resolvedWidgets : J
    //   84: lconst_1
    //   85: ladd
    //   86: putfield resolvedWidgets : J
    //   89: aload #7
    //   91: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   94: aload_1
    //   95: invokevirtual addResolvedValue : (Landroid/support/constraint/solver/LinearSystem;)V
    //   98: aload #8
    //   100: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   103: aload_1
    //   104: invokevirtual addResolvedValue : (Landroid/support/constraint/solver/LinearSystem;)V
    //   107: iload #15
    //   109: ifne -> 127
    //   112: iload_2
    //   113: ifeq -> 127
    //   116: aload_1
    //   117: aload #4
    //   119: aload #22
    //   121: iconst_0
    //   122: bipush #6
    //   124: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   127: return
    //   128: invokestatic getMetrics : ()Landroid/support/constraint/solver/Metrics;
    //   131: ifnull -> 151
    //   134: invokestatic getMetrics : ()Landroid/support/constraint/solver/Metrics;
    //   137: astore #25
    //   139: aload #25
    //   141: aload #25
    //   143: getfield nonresolvedWidgets : J
    //   146: lconst_1
    //   147: ladd
    //   148: putfield nonresolvedWidgets : J
    //   151: aload #7
    //   153: invokevirtual isConnected : ()Z
    //   156: istore #26
    //   158: aload #8
    //   160: invokevirtual isConnected : ()Z
    //   163: istore #27
    //   165: aload_0
    //   166: getfield mCenter : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   169: invokevirtual isConnected : ()Z
    //   172: istore #28
    //   174: iload #26
    //   176: ifeq -> 185
    //   179: iconst_1
    //   180: istore #29
    //   182: goto -> 188
    //   185: iconst_0
    //   186: istore #29
    //   188: iload #29
    //   190: istore #30
    //   192: iload #27
    //   194: ifeq -> 203
    //   197: iload #29
    //   199: iconst_1
    //   200: iadd
    //   201: istore #30
    //   203: iload #30
    //   205: istore #29
    //   207: iload #28
    //   209: ifeq -> 218
    //   212: iload #30
    //   214: iconst_1
    //   215: iadd
    //   216: istore #29
    //   218: iload #14
    //   220: ifeq -> 229
    //   223: iconst_3
    //   224: istore #31
    //   226: goto -> 233
    //   229: iload #16
    //   231: istore #31
    //   233: getstatic android/support/constraint/solver/widgets/ConstraintWidget$1.$SwitchMap$android$support$constraint$solver$widgets$ConstraintWidget$DimensionBehaviour : [I
    //   236: aload #5
    //   238: invokevirtual ordinal : ()I
    //   241: iaload
    //   242: tableswitch default -> 272, 1 -> 272, 2 -> 272, 3 -> 272, 4 -> 278
    //   272: iconst_0
    //   273: istore #30
    //   275: goto -> 290
    //   278: iload #31
    //   280: iconst_4
    //   281: if_icmpne -> 287
    //   284: goto -> 272
    //   287: iconst_1
    //   288: istore #30
    //   290: aload_0
    //   291: getfield mVisibility : I
    //   294: bipush #8
    //   296: if_icmpne -> 308
    //   299: iconst_0
    //   300: istore #16
    //   302: iconst_0
    //   303: istore #30
    //   305: goto -> 312
    //   308: iload #10
    //   310: istore #16
    //   312: iload #20
    //   314: ifeq -> 372
    //   317: iload #26
    //   319: ifne -> 343
    //   322: iload #27
    //   324: ifne -> 343
    //   327: iload #28
    //   329: ifne -> 343
    //   332: aload_1
    //   333: aload #21
    //   335: iload #9
    //   337: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;I)V
    //   340: goto -> 372
    //   343: iload #26
    //   345: ifeq -> 372
    //   348: iload #27
    //   350: ifne -> 372
    //   353: aload_1
    //   354: aload #21
    //   356: aload #23
    //   358: aload #7
    //   360: invokevirtual getMargin : ()I
    //   363: bipush #6
    //   365: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   368: pop
    //   369: goto -> 372
    //   372: iload #30
    //   374: ifne -> 467
    //   377: iload #6
    //   379: ifeq -> 435
    //   382: aload_1
    //   383: aload #22
    //   385: aload #21
    //   387: iconst_0
    //   388: iconst_3
    //   389: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   392: pop
    //   393: iload #11
    //   395: ifle -> 413
    //   398: aload_1
    //   399: aload #22
    //   401: aload #21
    //   403: iload #11
    //   405: bipush #6
    //   407: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   410: goto -> 413
    //   413: iload #12
    //   415: ldc 2147483647
    //   417: if_icmpge -> 448
    //   420: aload_1
    //   421: aload #22
    //   423: aload #21
    //   425: iload #12
    //   427: bipush #6
    //   429: invokevirtual addLowerThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   432: goto -> 448
    //   435: aload_1
    //   436: aload #22
    //   438: aload #21
    //   440: iload #16
    //   442: bipush #6
    //   444: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   447: pop
    //   448: iload #17
    //   450: istore #9
    //   452: iload #18
    //   454: istore #10
    //   456: iload #30
    //   458: istore #12
    //   460: iload #10
    //   462: istore #16
    //   464: goto -> 828
    //   467: iload #17
    //   469: bipush #-2
    //   471: if_icmpne -> 481
    //   474: iload #16
    //   476: istore #9
    //   478: goto -> 485
    //   481: iload #17
    //   483: istore #9
    //   485: iload #18
    //   487: istore #10
    //   489: iload #18
    //   491: bipush #-2
    //   493: if_icmpne -> 500
    //   496: iload #16
    //   498: istore #10
    //   500: iload #9
    //   502: ifle -> 529
    //   505: aload_1
    //   506: aload #22
    //   508: aload #21
    //   510: iload #9
    //   512: bipush #6
    //   514: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   517: iload #16
    //   519: iload #9
    //   521: invokestatic max : (II)I
    //   524: istore #16
    //   526: goto -> 529
    //   529: iload #16
    //   531: istore #17
    //   533: iload #10
    //   535: ifle -> 559
    //   538: aload_1
    //   539: aload #22
    //   541: aload #21
    //   543: iload #10
    //   545: bipush #6
    //   547: invokevirtual addLowerThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   550: iload #16
    //   552: iload #10
    //   554: invokestatic min : (II)I
    //   557: istore #17
    //   559: iload #31
    //   561: iconst_1
    //   562: if_icmpne -> 620
    //   565: iload_2
    //   566: ifeq -> 585
    //   569: aload_1
    //   570: aload #22
    //   572: aload #21
    //   574: iload #17
    //   576: bipush #6
    //   578: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   581: pop
    //   582: goto -> 745
    //   585: iload #15
    //   587: ifeq -> 605
    //   590: aload_1
    //   591: aload #22
    //   593: aload #21
    //   595: iload #17
    //   597: iconst_4
    //   598: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   601: pop
    //   602: goto -> 745
    //   605: aload_1
    //   606: aload #22
    //   608: aload #21
    //   610: iload #17
    //   612: iconst_1
    //   613: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   616: pop
    //   617: goto -> 745
    //   620: iload #31
    //   622: iconst_2
    //   623: if_icmpne -> 745
    //   626: aload #7
    //   628: invokevirtual getType : ()Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   631: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   634: if_acmpeq -> 686
    //   637: aload #7
    //   639: invokevirtual getType : ()Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   642: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   645: if_acmpne -> 651
    //   648: goto -> 686
    //   651: aload_1
    //   652: aload_0
    //   653: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   656: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   659: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   662: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   665: astore #5
    //   667: aload_1
    //   668: aload_0
    //   669: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   672: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   675: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   678: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   681: astore #25
    //   683: goto -> 718
    //   686: aload_1
    //   687: aload_0
    //   688: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   691: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   694: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   697: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   700: astore #5
    //   702: aload_1
    //   703: aload_0
    //   704: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   707: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   710: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   713: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   716: astore #25
    //   718: aload_1
    //   719: aload_1
    //   720: invokevirtual createRow : ()Landroid/support/constraint/solver/ArrayRow;
    //   723: aload #22
    //   725: aload #21
    //   727: aload #25
    //   729: aload #5
    //   731: fload #19
    //   733: invokevirtual createRowDimensionRatio : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;F)Landroid/support/constraint/solver/ArrayRow;
    //   736: invokevirtual addConstraint : (Landroid/support/constraint/solver/ArrayRow;)V
    //   739: iconst_0
    //   740: istore #12
    //   742: goto -> 749
    //   745: iload #30
    //   747: istore #12
    //   749: iload #12
    //   751: ifeq -> 825
    //   754: iload #29
    //   756: iconst_2
    //   757: if_icmpeq -> 822
    //   760: iload #14
    //   762: ifne -> 815
    //   765: iload #9
    //   767: iload #17
    //   769: invokestatic max : (II)I
    //   772: istore #16
    //   774: iload #16
    //   776: istore #12
    //   778: iload #10
    //   780: ifle -> 792
    //   783: iload #10
    //   785: iload #16
    //   787: invokestatic min : (II)I
    //   790: istore #12
    //   792: aload_1
    //   793: aload #22
    //   795: aload #21
    //   797: iload #12
    //   799: bipush #6
    //   801: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   804: pop
    //   805: iconst_0
    //   806: istore #12
    //   808: iload #10
    //   810: istore #16
    //   812: goto -> 828
    //   815: iload #10
    //   817: istore #16
    //   819: goto -> 828
    //   822: goto -> 460
    //   825: goto -> 460
    //   828: aload #23
    //   830: astore #5
    //   832: iload #20
    //   834: ifeq -> 1433
    //   837: iload #15
    //   839: ifeq -> 845
    //   842: goto -> 1433
    //   845: iload #26
    //   847: ifne -> 880
    //   850: iload #27
    //   852: ifne -> 880
    //   855: iload #28
    //   857: ifne -> 880
    //   860: iload_2
    //   861: ifeq -> 877
    //   864: aload_1
    //   865: aload #4
    //   867: aload #22
    //   869: iconst_0
    //   870: iconst_5
    //   871: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   874: goto -> 877
    //   877: goto -> 1417
    //   880: iload #26
    //   882: ifeq -> 907
    //   885: iload #27
    //   887: ifne -> 907
    //   890: iload_2
    //   891: ifeq -> 877
    //   894: aload_1
    //   895: aload #4
    //   897: aload #22
    //   899: iconst_0
    //   900: iconst_5
    //   901: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   904: goto -> 877
    //   907: iload #26
    //   909: ifne -> 950
    //   912: iload #27
    //   914: ifeq -> 950
    //   917: aload_1
    //   918: aload #22
    //   920: aload #24
    //   922: aload #8
    //   924: invokevirtual getMargin : ()I
    //   927: ineg
    //   928: bipush #6
    //   930: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   933: pop
    //   934: iload_2
    //   935: ifeq -> 877
    //   938: aload_1
    //   939: aload #21
    //   941: aload_3
    //   942: iconst_0
    //   943: iconst_5
    //   944: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   947: goto -> 877
    //   950: iload #26
    //   952: ifeq -> 877
    //   955: iload #27
    //   957: ifeq -> 877
    //   960: iload #12
    //   962: ifeq -> 1190
    //   965: iload_2
    //   966: ifeq -> 985
    //   969: iload #11
    //   971: ifne -> 985
    //   974: aload_1
    //   975: aload #22
    //   977: aload #21
    //   979: iconst_0
    //   980: bipush #6
    //   982: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   985: iload #31
    //   987: ifne -> 1092
    //   990: iload #16
    //   992: ifgt -> 1013
    //   995: iload #9
    //   997: ifle -> 1003
    //   1000: goto -> 1013
    //   1003: bipush #6
    //   1005: istore #11
    //   1007: iconst_0
    //   1008: istore #10
    //   1010: goto -> 1019
    //   1013: iconst_4
    //   1014: istore #11
    //   1016: iconst_1
    //   1017: istore #10
    //   1019: aload_1
    //   1020: aload #21
    //   1022: aload #5
    //   1024: aload #7
    //   1026: invokevirtual getMargin : ()I
    //   1029: iload #11
    //   1031: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   1034: pop
    //   1035: aload_1
    //   1036: aload #22
    //   1038: aload #24
    //   1040: aload #8
    //   1042: invokevirtual getMargin : ()I
    //   1045: ineg
    //   1046: iload #11
    //   1048: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   1051: pop
    //   1052: iload #16
    //   1054: ifgt -> 1071
    //   1057: iload #9
    //   1059: ifle -> 1065
    //   1062: goto -> 1071
    //   1065: iconst_0
    //   1066: istore #9
    //   1068: goto -> 1074
    //   1071: iconst_1
    //   1072: istore #9
    //   1074: iload #10
    //   1076: istore #16
    //   1078: iconst_5
    //   1079: istore #11
    //   1081: iload #9
    //   1083: istore #10
    //   1085: iload #16
    //   1087: istore #9
    //   1089: goto -> 1199
    //   1092: iload #31
    //   1094: iconst_1
    //   1095: if_icmpne -> 1111
    //   1098: bipush #6
    //   1100: istore #11
    //   1102: iconst_1
    //   1103: istore #10
    //   1105: iconst_1
    //   1106: istore #9
    //   1108: goto -> 1199
    //   1111: iload #31
    //   1113: iconst_3
    //   1114: if_icmpne -> 1184
    //   1117: iload #14
    //   1119: ifne -> 1142
    //   1122: aload_0
    //   1123: getfield mResolvedDimensionRatioSide : I
    //   1126: iconst_m1
    //   1127: if_icmpeq -> 1142
    //   1130: iload #16
    //   1132: ifgt -> 1142
    //   1135: bipush #6
    //   1137: istore #9
    //   1139: goto -> 1145
    //   1142: iconst_4
    //   1143: istore #9
    //   1145: aload_1
    //   1146: aload #21
    //   1148: aload #5
    //   1150: aload #7
    //   1152: invokevirtual getMargin : ()I
    //   1155: iload #9
    //   1157: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   1160: pop
    //   1161: aload_1
    //   1162: aload #22
    //   1164: aload #24
    //   1166: aload #8
    //   1168: invokevirtual getMargin : ()I
    //   1171: ineg
    //   1172: iload #9
    //   1174: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   1177: pop
    //   1178: iconst_5
    //   1179: istore #11
    //   1181: goto -> 1102
    //   1184: iconst_0
    //   1185: istore #10
    //   1187: goto -> 1193
    //   1190: iconst_1
    //   1191: istore #10
    //   1193: iconst_5
    //   1194: istore #11
    //   1196: iconst_0
    //   1197: istore #9
    //   1199: iload #10
    //   1201: ifeq -> 1302
    //   1204: aload_1
    //   1205: aload #21
    //   1207: aload #5
    //   1209: aload #7
    //   1211: invokevirtual getMargin : ()I
    //   1214: fload #13
    //   1216: aload #24
    //   1218: aload #22
    //   1220: aload #8
    //   1222: invokevirtual getMargin : ()I
    //   1225: iload #11
    //   1227: invokevirtual addCentering : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;IFLandroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   1230: iconst_1
    //   1231: istore #14
    //   1233: aload #7
    //   1235: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1238: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1241: instanceof android/support/constraint/solver/widgets/Barrier
    //   1244: istore #6
    //   1246: aload #8
    //   1248: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1251: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1254: instanceof android/support/constraint/solver/widgets/Barrier
    //   1257: istore #15
    //   1259: iload #6
    //   1261: ifeq -> 1279
    //   1264: iload #15
    //   1266: ifne -> 1279
    //   1269: iconst_1
    //   1270: istore #6
    //   1272: bipush #6
    //   1274: istore #11
    //   1276: goto -> 1308
    //   1279: iload #6
    //   1281: ifne -> 1302
    //   1284: iload #15
    //   1286: ifeq -> 1302
    //   1289: iload_2
    //   1290: istore #6
    //   1292: iconst_5
    //   1293: istore #11
    //   1295: bipush #6
    //   1297: istore #10
    //   1299: goto -> 1314
    //   1302: iload_2
    //   1303: istore #6
    //   1305: iconst_5
    //   1306: istore #11
    //   1308: iload_2
    //   1309: istore #14
    //   1311: iconst_5
    //   1312: istore #10
    //   1314: iload #9
    //   1316: ifeq -> 1330
    //   1319: bipush #6
    //   1321: istore #11
    //   1323: bipush #6
    //   1325: istore #10
    //   1327: goto -> 1342
    //   1330: iload #11
    //   1332: istore #16
    //   1334: iload #10
    //   1336: istore #11
    //   1338: iload #16
    //   1340: istore #10
    //   1342: iload #12
    //   1344: ifne -> 1352
    //   1347: iload #14
    //   1349: ifne -> 1357
    //   1352: iload #9
    //   1354: ifeq -> 1372
    //   1357: aload_1
    //   1358: aload #21
    //   1360: aload #5
    //   1362: aload #7
    //   1364: invokevirtual getMargin : ()I
    //   1367: iload #11
    //   1369: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   1372: iload #12
    //   1374: ifne -> 1382
    //   1377: iload #6
    //   1379: ifne -> 1387
    //   1382: iload #9
    //   1384: ifeq -> 1403
    //   1387: aload_1
    //   1388: aload #22
    //   1390: aload #24
    //   1392: aload #8
    //   1394: invokevirtual getMargin : ()I
    //   1397: ineg
    //   1398: iload #10
    //   1400: invokevirtual addLowerThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   1403: iload_2
    //   1404: ifeq -> 877
    //   1407: aload_1
    //   1408: aload #21
    //   1410: aload_3
    //   1411: iconst_0
    //   1412: bipush #6
    //   1414: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   1417: iload_2
    //   1418: ifeq -> 1432
    //   1421: aload_1
    //   1422: aload #4
    //   1424: aload #22
    //   1426: iconst_0
    //   1427: bipush #6
    //   1429: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   1432: return
    //   1433: iload #29
    //   1435: iconst_2
    //   1436: if_icmpge -> 1464
    //   1439: iload_2
    //   1440: ifeq -> 1464
    //   1443: aload_1
    //   1444: aload #21
    //   1446: aload_3
    //   1447: iconst_0
    //   1448: bipush #6
    //   1450: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   1453: aload_1
    //   1454: aload #4
    //   1456: aload #22
    //   1458: iconst_0
    //   1459: bipush #6
    //   1461: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   1464: return
  }
  
  private boolean isChainHead(int paramInt) {
    paramInt *= 2;
    ConstraintAnchor constraintAnchor = (this.mListAnchors[paramInt]).mTarget;
    null = true;
    if (constraintAnchor != null && (this.mListAnchors[paramInt]).mTarget.mTarget != this.mListAnchors[paramInt]) {
      ConstraintAnchor[] arrayOfConstraintAnchor = this.mListAnchors;
      if ((arrayOfConstraintAnchor[++paramInt]).mTarget != null && (this.mListAnchors[paramInt]).mTarget.mTarget == this.mListAnchors[paramInt])
        return null; 
    } 
    return false;
  }
  
  public void addToSolver(LinearSystem paramLinearSystem) {
    // Byte code:
    //   0: aload_1
    //   1: aload_0
    //   2: getfield mLeft : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   5: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   8: astore_2
    //   9: aload_1
    //   10: aload_0
    //   11: getfield mRight : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   14: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   17: astore_3
    //   18: aload_1
    //   19: aload_0
    //   20: getfield mTop : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   23: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   26: astore #4
    //   28: aload_1
    //   29: aload_0
    //   30: getfield mBottom : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   33: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   36: astore #5
    //   38: aload_1
    //   39: aload_0
    //   40: getfield mBaseline : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   43: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   46: astore #6
    //   48: aload_0
    //   49: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   52: ifnull -> 308
    //   55: aload_0
    //   56: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   59: ifnull -> 83
    //   62: aload_0
    //   63: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   66: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   69: iconst_0
    //   70: aaload
    //   71: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   74: if_acmpne -> 83
    //   77: iconst_1
    //   78: istore #7
    //   80: goto -> 86
    //   83: iconst_0
    //   84: istore #7
    //   86: aload_0
    //   87: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   90: ifnull -> 114
    //   93: aload_0
    //   94: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   97: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   100: iconst_1
    //   101: aaload
    //   102: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   105: if_acmpne -> 114
    //   108: iconst_1
    //   109: istore #8
    //   111: goto -> 117
    //   114: iconst_0
    //   115: istore #8
    //   117: aload_0
    //   118: iconst_0
    //   119: invokespecial isChainHead : (I)Z
    //   122: ifeq -> 143
    //   125: aload_0
    //   126: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   129: checkcast android/support/constraint/solver/widgets/ConstraintWidgetContainer
    //   132: aload_0
    //   133: iconst_0
    //   134: invokevirtual addChain : (Landroid/support/constraint/solver/widgets/ConstraintWidget;I)V
    //   137: iconst_1
    //   138: istore #9
    //   140: goto -> 149
    //   143: aload_0
    //   144: invokevirtual isInHorizontalChain : ()Z
    //   147: istore #9
    //   149: aload_0
    //   150: iconst_1
    //   151: invokespecial isChainHead : (I)Z
    //   154: ifeq -> 175
    //   157: aload_0
    //   158: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   161: checkcast android/support/constraint/solver/widgets/ConstraintWidgetContainer
    //   164: aload_0
    //   165: iconst_1
    //   166: invokevirtual addChain : (Landroid/support/constraint/solver/widgets/ConstraintWidget;I)V
    //   169: iconst_1
    //   170: istore #10
    //   172: goto -> 181
    //   175: aload_0
    //   176: invokevirtual isInVerticalChain : ()Z
    //   179: istore #10
    //   181: iload #7
    //   183: ifeq -> 233
    //   186: aload_0
    //   187: getfield mVisibility : I
    //   190: bipush #8
    //   192: if_icmpeq -> 233
    //   195: aload_0
    //   196: getfield mLeft : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   199: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   202: ifnonnull -> 233
    //   205: aload_0
    //   206: getfield mRight : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   209: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   212: ifnonnull -> 233
    //   215: aload_1
    //   216: aload_1
    //   217: aload_0
    //   218: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   221: getfield mRight : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   224: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   227: aload_3
    //   228: iconst_0
    //   229: iconst_1
    //   230: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   233: iload #8
    //   235: ifeq -> 293
    //   238: aload_0
    //   239: getfield mVisibility : I
    //   242: bipush #8
    //   244: if_icmpeq -> 293
    //   247: aload_0
    //   248: getfield mTop : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   251: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   254: ifnonnull -> 293
    //   257: aload_0
    //   258: getfield mBottom : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   261: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   264: ifnonnull -> 293
    //   267: aload_0
    //   268: getfield mBaseline : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   271: ifnonnull -> 293
    //   274: aload_1
    //   275: aload_1
    //   276: aload_0
    //   277: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   280: getfield mBottom : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   283: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   286: aload #5
    //   288: iconst_0
    //   289: iconst_1
    //   290: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   293: iload #10
    //   295: istore #11
    //   297: iload #7
    //   299: istore #10
    //   301: iload #11
    //   303: istore #7
    //   305: goto -> 320
    //   308: iconst_0
    //   309: istore #10
    //   311: iconst_0
    //   312: istore #8
    //   314: iconst_0
    //   315: istore #9
    //   317: iconst_0
    //   318: istore #7
    //   320: aload_0
    //   321: getfield mWidth : I
    //   324: istore #12
    //   326: iload #12
    //   328: istore #13
    //   330: iload #12
    //   332: aload_0
    //   333: getfield mMinWidth : I
    //   336: if_icmpge -> 345
    //   339: aload_0
    //   340: getfield mMinWidth : I
    //   343: istore #13
    //   345: aload_0
    //   346: getfield mHeight : I
    //   349: istore #14
    //   351: iload #14
    //   353: istore #12
    //   355: iload #14
    //   357: aload_0
    //   358: getfield mMinHeight : I
    //   361: if_icmpge -> 370
    //   364: aload_0
    //   365: getfield mMinHeight : I
    //   368: istore #12
    //   370: aload_0
    //   371: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   374: iconst_0
    //   375: aaload
    //   376: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   379: if_acmpeq -> 388
    //   382: iconst_1
    //   383: istore #11
    //   385: goto -> 391
    //   388: iconst_0
    //   389: istore #11
    //   391: aload_0
    //   392: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   395: iconst_1
    //   396: aaload
    //   397: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   400: if_acmpeq -> 409
    //   403: iconst_1
    //   404: istore #15
    //   406: goto -> 412
    //   409: iconst_0
    //   410: istore #15
    //   412: aload_0
    //   413: aload_0
    //   414: getfield mDimensionRatioSide : I
    //   417: putfield mResolvedDimensionRatioSide : I
    //   420: aload_0
    //   421: aload_0
    //   422: getfield mDimensionRatio : F
    //   425: putfield mResolvedDimensionRatio : F
    //   428: aload_0
    //   429: getfield mMatchConstraintDefaultWidth : I
    //   432: istore #16
    //   434: aload_0
    //   435: getfield mMatchConstraintDefaultHeight : I
    //   438: istore #17
    //   440: aload_0
    //   441: getfield mDimensionRatio : F
    //   444: fconst_0
    //   445: fcmpl
    //   446: ifle -> 775
    //   449: aload_0
    //   450: getfield mVisibility : I
    //   453: bipush #8
    //   455: if_icmpeq -> 775
    //   458: iload #16
    //   460: istore #14
    //   462: aload_0
    //   463: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   466: iconst_0
    //   467: aaload
    //   468: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   471: if_acmpne -> 486
    //   474: iload #16
    //   476: istore #14
    //   478: iload #16
    //   480: ifne -> 486
    //   483: iconst_3
    //   484: istore #14
    //   486: iload #17
    //   488: istore #16
    //   490: aload_0
    //   491: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   494: iconst_1
    //   495: aaload
    //   496: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   499: if_acmpne -> 514
    //   502: iload #17
    //   504: istore #16
    //   506: iload #17
    //   508: ifne -> 514
    //   511: iconst_3
    //   512: istore #16
    //   514: aload_0
    //   515: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   518: iconst_0
    //   519: aaload
    //   520: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   523: if_acmpne -> 565
    //   526: aload_0
    //   527: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   530: iconst_1
    //   531: aaload
    //   532: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   535: if_acmpne -> 565
    //   538: iload #14
    //   540: iconst_3
    //   541: if_icmpne -> 565
    //   544: iload #16
    //   546: iconst_3
    //   547: if_icmpne -> 565
    //   550: aload_0
    //   551: iload #10
    //   553: iload #8
    //   555: iload #11
    //   557: iload #15
    //   559: invokevirtual setupDimensionRatio : (ZZZZ)V
    //   562: goto -> 741
    //   565: aload_0
    //   566: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   569: iconst_0
    //   570: aaload
    //   571: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   574: if_acmpne -> 642
    //   577: iload #14
    //   579: iconst_3
    //   580: if_icmpne -> 642
    //   583: aload_0
    //   584: iconst_0
    //   585: putfield mResolvedDimensionRatioSide : I
    //   588: aload_0
    //   589: getfield mResolvedDimensionRatio : F
    //   592: aload_0
    //   593: getfield mHeight : I
    //   596: i2f
    //   597: fmul
    //   598: f2i
    //   599: istore #13
    //   601: aload_0
    //   602: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   605: iconst_1
    //   606: aaload
    //   607: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   610: if_acmpeq -> 635
    //   613: iload #13
    //   615: istore #14
    //   617: iload #16
    //   619: istore #17
    //   621: iload #12
    //   623: istore #16
    //   625: iconst_4
    //   626: istore #13
    //   628: iload #17
    //   630: istore #12
    //   632: goto -> 791
    //   635: iload #13
    //   637: istore #17
    //   639: goto -> 745
    //   642: aload_0
    //   643: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   646: iconst_1
    //   647: aaload
    //   648: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   651: if_acmpne -> 741
    //   654: iload #16
    //   656: iconst_3
    //   657: if_icmpne -> 741
    //   660: aload_0
    //   661: iconst_1
    //   662: putfield mResolvedDimensionRatioSide : I
    //   665: aload_0
    //   666: getfield mDimensionRatioSide : I
    //   669: iconst_m1
    //   670: if_icmpne -> 683
    //   673: aload_0
    //   674: fconst_1
    //   675: aload_0
    //   676: getfield mResolvedDimensionRatio : F
    //   679: fdiv
    //   680: putfield mResolvedDimensionRatio : F
    //   683: aload_0
    //   684: getfield mResolvedDimensionRatio : F
    //   687: aload_0
    //   688: getfield mWidth : I
    //   691: i2f
    //   692: fmul
    //   693: f2i
    //   694: istore #17
    //   696: aload_0
    //   697: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   700: iconst_0
    //   701: aaload
    //   702: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   705: if_acmpeq -> 730
    //   708: iload #17
    //   710: istore #16
    //   712: iload #13
    //   714: istore #17
    //   716: iload #14
    //   718: istore #13
    //   720: iconst_4
    //   721: istore #12
    //   723: iload #17
    //   725: istore #14
    //   727: goto -> 791
    //   730: iload #13
    //   732: istore #12
    //   734: iload #17
    //   736: istore #13
    //   738: goto -> 753
    //   741: iload #13
    //   743: istore #17
    //   745: iload #12
    //   747: istore #13
    //   749: iload #17
    //   751: istore #12
    //   753: iconst_1
    //   754: istore #17
    //   756: iload #14
    //   758: istore #18
    //   760: iload #16
    //   762: istore #14
    //   764: iload #12
    //   766: istore #19
    //   768: iload #13
    //   770: istore #16
    //   772: goto -> 806
    //   775: iload #13
    //   777: istore #14
    //   779: iload #16
    //   781: istore #13
    //   783: iload #12
    //   785: istore #16
    //   787: iload #17
    //   789: istore #12
    //   791: iconst_0
    //   792: istore #17
    //   794: iload #14
    //   796: istore #19
    //   798: iload #12
    //   800: istore #14
    //   802: iload #13
    //   804: istore #18
    //   806: aload_0
    //   807: getfield mResolvedMatchConstraintDefault : [I
    //   810: iconst_0
    //   811: iload #18
    //   813: iastore
    //   814: aload_0
    //   815: getfield mResolvedMatchConstraintDefault : [I
    //   818: iconst_1
    //   819: iload #14
    //   821: iastore
    //   822: iload #17
    //   824: ifeq -> 851
    //   827: aload_0
    //   828: getfield mResolvedDimensionRatioSide : I
    //   831: ifeq -> 845
    //   834: aload_0
    //   835: getfield mResolvedDimensionRatioSide : I
    //   838: iconst_m1
    //   839: if_icmpne -> 851
    //   842: goto -> 845
    //   845: iconst_1
    //   846: istore #11
    //   848: goto -> 854
    //   851: iconst_0
    //   852: istore #11
    //   854: aload_0
    //   855: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   858: iconst_0
    //   859: aaload
    //   860: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   863: if_acmpne -> 879
    //   866: aload_0
    //   867: instanceof android/support/constraint/solver/widgets/ConstraintWidgetContainer
    //   870: ifeq -> 879
    //   873: iconst_1
    //   874: istore #15
    //   876: goto -> 882
    //   879: iconst_0
    //   880: istore #15
    //   882: aload_0
    //   883: getfield mCenter : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   886: invokevirtual isConnected : ()Z
    //   889: iconst_1
    //   890: ixor
    //   891: istore #20
    //   893: aload_0
    //   894: getfield mHorizontalResolution : I
    //   897: iconst_2
    //   898: if_icmpeq -> 1023
    //   901: aload_0
    //   902: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   905: ifnull -> 924
    //   908: aload_1
    //   909: aload_0
    //   910: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   913: getfield mRight : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   916: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   919: astore #21
    //   921: goto -> 927
    //   924: aconst_null
    //   925: astore #21
    //   927: aload_0
    //   928: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   931: ifnull -> 950
    //   934: aload_1
    //   935: aload_0
    //   936: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   939: getfield mLeft : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   942: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   945: astore #22
    //   947: goto -> 953
    //   950: aconst_null
    //   951: astore #22
    //   953: aload_0
    //   954: aload_1
    //   955: iload #10
    //   957: aload #22
    //   959: aload #21
    //   961: aload_0
    //   962: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   965: iconst_0
    //   966: aaload
    //   967: iload #15
    //   969: aload_0
    //   970: getfield mLeft : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   973: aload_0
    //   974: getfield mRight : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   977: aload_0
    //   978: getfield mX : I
    //   981: iload #19
    //   983: aload_0
    //   984: getfield mMinWidth : I
    //   987: aload_0
    //   988: getfield mMaxDimension : [I
    //   991: iconst_0
    //   992: iaload
    //   993: aload_0
    //   994: getfield mHorizontalBiasPercent : F
    //   997: iload #11
    //   999: iload #9
    //   1001: iload #18
    //   1003: aload_0
    //   1004: getfield mMatchConstraintMinWidth : I
    //   1007: aload_0
    //   1008: getfield mMatchConstraintMaxWidth : I
    //   1011: aload_0
    //   1012: getfield mMatchConstraintPercentWidth : F
    //   1015: iload #20
    //   1017: invokespecial applyConstraints : (Landroid/support/constraint/solver/LinearSystem;ZLandroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;ZLandroid/support/constraint/solver/widgets/ConstraintAnchor;Landroid/support/constraint/solver/widgets/ConstraintAnchor;IIIIFZZIIIFZ)V
    //   1020: goto -> 1023
    //   1023: aload #4
    //   1025: astore #21
    //   1027: aload_3
    //   1028: astore #22
    //   1030: aload_0
    //   1031: getfield mVerticalResolution : I
    //   1034: iconst_2
    //   1035: if_icmpne -> 1039
    //   1038: return
    //   1039: aload_0
    //   1040: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1043: iconst_1
    //   1044: aaload
    //   1045: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1048: if_acmpne -> 1064
    //   1051: aload_0
    //   1052: instanceof android/support/constraint/solver/widgets/ConstraintWidgetContainer
    //   1055: ifeq -> 1064
    //   1058: iconst_1
    //   1059: istore #9
    //   1061: goto -> 1067
    //   1064: iconst_0
    //   1065: istore #9
    //   1067: iload #17
    //   1069: ifeq -> 1094
    //   1072: aload_0
    //   1073: getfield mResolvedDimensionRatioSide : I
    //   1076: iconst_1
    //   1077: if_icmpeq -> 1088
    //   1080: aload_0
    //   1081: getfield mResolvedDimensionRatioSide : I
    //   1084: iconst_m1
    //   1085: if_icmpne -> 1094
    //   1088: iconst_1
    //   1089: istore #10
    //   1091: goto -> 1097
    //   1094: iconst_0
    //   1095: istore #10
    //   1097: aload_0
    //   1098: getfield mBaselineDistance : I
    //   1101: ifle -> 1186
    //   1104: aload_0
    //   1105: getfield mBaseline : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1108: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1111: getfield state : I
    //   1114: iconst_1
    //   1115: if_icmpne -> 1132
    //   1118: aload_0
    //   1119: getfield mBaseline : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1122: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1125: aload_1
    //   1126: invokevirtual addResolvedValue : (Landroid/support/constraint/solver/LinearSystem;)V
    //   1129: goto -> 1186
    //   1132: aload_1
    //   1133: astore_3
    //   1134: aload_3
    //   1135: aload #6
    //   1137: aload #21
    //   1139: aload_0
    //   1140: invokevirtual getBaselineDistance : ()I
    //   1143: bipush #6
    //   1145: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   1148: pop
    //   1149: aload_0
    //   1150: getfield mBaseline : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1153: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1156: ifnull -> 1186
    //   1159: aload_3
    //   1160: aload #6
    //   1162: aload_3
    //   1163: aload_0
    //   1164: getfield mBaseline : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1167: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1170: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   1173: iconst_0
    //   1174: bipush #6
    //   1176: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   1179: pop
    //   1180: iconst_0
    //   1181: istore #11
    //   1183: goto -> 1190
    //   1186: iload #20
    //   1188: istore #11
    //   1190: aload_1
    //   1191: astore #4
    //   1193: aload #21
    //   1195: astore_3
    //   1196: aload_0
    //   1197: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1200: ifnull -> 1220
    //   1203: aload #4
    //   1205: aload_0
    //   1206: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1209: getfield mBottom : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1212: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   1215: astore #6
    //   1217: goto -> 1223
    //   1220: aconst_null
    //   1221: astore #6
    //   1223: aload_0
    //   1224: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1227: ifnull -> 1247
    //   1230: aload #4
    //   1232: aload_0
    //   1233: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1236: getfield mTop : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1239: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   1242: astore #21
    //   1244: goto -> 1250
    //   1247: aconst_null
    //   1248: astore #21
    //   1250: aload_0
    //   1251: aload_1
    //   1252: iload #8
    //   1254: aload #21
    //   1256: aload #6
    //   1258: aload_0
    //   1259: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1262: iconst_1
    //   1263: aaload
    //   1264: iload #9
    //   1266: aload_0
    //   1267: getfield mTop : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1270: aload_0
    //   1271: getfield mBottom : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1274: aload_0
    //   1275: getfield mY : I
    //   1278: iload #16
    //   1280: aload_0
    //   1281: getfield mMinHeight : I
    //   1284: aload_0
    //   1285: getfield mMaxDimension : [I
    //   1288: iconst_1
    //   1289: iaload
    //   1290: aload_0
    //   1291: getfield mVerticalBiasPercent : F
    //   1294: iload #10
    //   1296: iload #7
    //   1298: iload #14
    //   1300: aload_0
    //   1301: getfield mMatchConstraintMinHeight : I
    //   1304: aload_0
    //   1305: getfield mMatchConstraintMaxHeight : I
    //   1308: aload_0
    //   1309: getfield mMatchConstraintPercentHeight : F
    //   1312: iload #11
    //   1314: invokespecial applyConstraints : (Landroid/support/constraint/solver/LinearSystem;ZLandroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;ZLandroid/support/constraint/solver/widgets/ConstraintAnchor;Landroid/support/constraint/solver/widgets/ConstraintAnchor;IIIIFZZIIIFZ)V
    //   1317: iload #17
    //   1319: ifeq -> 1374
    //   1322: aload_0
    //   1323: astore #6
    //   1325: aload #6
    //   1327: getfield mResolvedDimensionRatioSide : I
    //   1330: iconst_1
    //   1331: if_icmpne -> 1354
    //   1334: aload_1
    //   1335: aload #5
    //   1337: aload_3
    //   1338: aload #22
    //   1340: aload_2
    //   1341: aload #6
    //   1343: getfield mResolvedDimensionRatio : F
    //   1346: bipush #6
    //   1348: invokevirtual addRatio : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;FI)V
    //   1351: goto -> 1374
    //   1354: aload_1
    //   1355: aload #22
    //   1357: aload_2
    //   1358: aload #5
    //   1360: aload_3
    //   1361: aload #6
    //   1363: getfield mResolvedDimensionRatio : F
    //   1366: bipush #6
    //   1368: invokevirtual addRatio : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;FI)V
    //   1371: goto -> 1374
    //   1374: aload_0
    //   1375: astore #6
    //   1377: aload #6
    //   1379: getfield mCenter : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1382: invokevirtual isConnected : ()Z
    //   1385: ifeq -> 1427
    //   1388: aload_1
    //   1389: aload #6
    //   1391: aload #6
    //   1393: getfield mCenter : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1396: invokevirtual getTarget : ()Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1399: invokevirtual getOwner : ()Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1402: aload #6
    //   1404: getfield mCircleConstraintAngle : F
    //   1407: ldc_w 90.0
    //   1410: fadd
    //   1411: f2d
    //   1412: invokestatic toRadians : (D)D
    //   1415: d2f
    //   1416: aload #6
    //   1418: getfield mCenter : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1421: invokevirtual getMargin : ()I
    //   1424: invokevirtual addCenterPoint : (Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintWidget;FI)V
    //   1427: return
  }
  
  public boolean allowedInBarrier() {
    boolean bool;
    if (this.mVisibility != 8) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void analyze(int paramInt) {
    Optimizer.analyze(paramInt, this);
  }
  
  public void connect(ConstraintAnchor.Type paramType1, ConstraintWidget paramConstraintWidget, ConstraintAnchor.Type paramType2) {
    connect(paramType1, paramConstraintWidget, paramType2, 0, ConstraintAnchor.Strength.STRONG);
  }
  
  public void connect(ConstraintAnchor.Type paramType1, ConstraintWidget paramConstraintWidget, ConstraintAnchor.Type paramType2, int paramInt) {
    connect(paramType1, paramConstraintWidget, paramType2, paramInt, ConstraintAnchor.Strength.STRONG);
  }
  
  public void connect(ConstraintAnchor.Type paramType1, ConstraintWidget paramConstraintWidget, ConstraintAnchor.Type paramType2, int paramInt, ConstraintAnchor.Strength paramStrength) {
    connect(paramType1, paramConstraintWidget, paramType2, paramInt, paramStrength, 0);
  }
  
  public void connect(ConstraintAnchor.Type paramType1, ConstraintWidget paramConstraintWidget, ConstraintAnchor.Type paramType2, int paramInt1, ConstraintAnchor.Strength paramStrength, int paramInt2) {
    ConstraintAnchor constraintAnchor;
    ConstraintAnchor.Type type = ConstraintAnchor.Type.CENTER;
    boolean bool = false;
    if (paramType1 == type) {
      if (paramType2 == ConstraintAnchor.Type.CENTER) {
        ConstraintAnchor constraintAnchor1 = getAnchor(ConstraintAnchor.Type.LEFT);
        ConstraintAnchor constraintAnchor2 = getAnchor(ConstraintAnchor.Type.RIGHT);
        constraintAnchor = getAnchor(ConstraintAnchor.Type.TOP);
        ConstraintAnchor constraintAnchor3 = getAnchor(ConstraintAnchor.Type.BOTTOM);
        bool = true;
        if ((constraintAnchor1 != null && constraintAnchor1.isConnected()) || (constraintAnchor2 != null && constraintAnchor2.isConnected())) {
          paramInt1 = 0;
        } else {
          connect(ConstraintAnchor.Type.LEFT, paramConstraintWidget, ConstraintAnchor.Type.LEFT, 0, paramStrength, paramInt2);
          connect(ConstraintAnchor.Type.RIGHT, paramConstraintWidget, ConstraintAnchor.Type.RIGHT, 0, paramStrength, paramInt2);
          paramInt1 = 1;
        } 
        if ((constraintAnchor != null && constraintAnchor.isConnected()) || (constraintAnchor3 != null && constraintAnchor3.isConnected())) {
          bool = false;
        } else {
          connect(ConstraintAnchor.Type.TOP, paramConstraintWidget, ConstraintAnchor.Type.TOP, 0, paramStrength, paramInt2);
          connect(ConstraintAnchor.Type.BOTTOM, paramConstraintWidget, ConstraintAnchor.Type.BOTTOM, 0, paramStrength, paramInt2);
        } 
        if (paramInt1 != 0 && bool) {
          getAnchor(ConstraintAnchor.Type.CENTER).connect(paramConstraintWidget.getAnchor(ConstraintAnchor.Type.CENTER), 0, paramInt2);
        } else if (paramInt1 != 0) {
          getAnchor(ConstraintAnchor.Type.CENTER_X).connect(paramConstraintWidget.getAnchor(ConstraintAnchor.Type.CENTER_X), 0, paramInt2);
        } else if (bool) {
          getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(paramConstraintWidget.getAnchor(ConstraintAnchor.Type.CENTER_Y), 0, paramInt2);
        } 
      } else {
        if (constraintAnchor == ConstraintAnchor.Type.LEFT || constraintAnchor == ConstraintAnchor.Type.RIGHT) {
          connect(ConstraintAnchor.Type.LEFT, paramConstraintWidget, (ConstraintAnchor.Type)constraintAnchor, 0, paramStrength, paramInt2);
          paramType1 = ConstraintAnchor.Type.RIGHT;
          try {
            connect(paramType1, paramConstraintWidget, (ConstraintAnchor.Type)constraintAnchor, 0, paramStrength, paramInt2);
            getAnchor(ConstraintAnchor.Type.CENTER).connect(paramConstraintWidget.getAnchor((ConstraintAnchor.Type)constraintAnchor), 0, paramInt2);
            return;
          } catch (Throwable throwable) {
            throw throwable;
          } 
        } 
        if (constraintAnchor == ConstraintAnchor.Type.TOP || constraintAnchor == ConstraintAnchor.Type.BOTTOM) {
          connect(ConstraintAnchor.Type.TOP, paramConstraintWidget, (ConstraintAnchor.Type)constraintAnchor, 0, paramStrength, paramInt2);
          connect(ConstraintAnchor.Type.BOTTOM, paramConstraintWidget, (ConstraintAnchor.Type)constraintAnchor, 0, paramStrength, paramInt2);
          getAnchor(ConstraintAnchor.Type.CENTER).connect(paramConstraintWidget.getAnchor((ConstraintAnchor.Type)constraintAnchor), 0, paramInt2);
        } 
      } 
    } else {
      ConstraintAnchor constraintAnchor1;
      ConstraintAnchor constraintAnchor2;
      if (throwable == ConstraintAnchor.Type.CENTER_X && (constraintAnchor == ConstraintAnchor.Type.LEFT || constraintAnchor == ConstraintAnchor.Type.RIGHT)) {
        constraintAnchor1 = getAnchor(ConstraintAnchor.Type.LEFT);
        constraintAnchor = paramConstraintWidget.getAnchor((ConstraintAnchor.Type)constraintAnchor);
        constraintAnchor2 = getAnchor(ConstraintAnchor.Type.RIGHT);
        constraintAnchor1.connect(constraintAnchor, 0, paramInt2);
        constraintAnchor2.connect(constraintAnchor, 0, paramInt2);
        getAnchor(ConstraintAnchor.Type.CENTER_X).connect(constraintAnchor, 0, paramInt2);
      } else if (constraintAnchor1 == ConstraintAnchor.Type.CENTER_Y && (constraintAnchor == ConstraintAnchor.Type.TOP || constraintAnchor == ConstraintAnchor.Type.BOTTOM)) {
        constraintAnchor1 = constraintAnchor2.getAnchor((ConstraintAnchor.Type)constraintAnchor);
        getAnchor(ConstraintAnchor.Type.TOP).connect(constraintAnchor1, 0, paramInt2);
        getAnchor(ConstraintAnchor.Type.BOTTOM).connect(constraintAnchor1, 0, paramInt2);
        getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(constraintAnchor1, 0, paramInt2);
      } else if (constraintAnchor1 == ConstraintAnchor.Type.CENTER_X && constraintAnchor == ConstraintAnchor.Type.CENTER_X) {
        getAnchor(ConstraintAnchor.Type.LEFT).connect(constraintAnchor2.getAnchor(ConstraintAnchor.Type.LEFT), 0, paramInt2);
        getAnchor(ConstraintAnchor.Type.RIGHT).connect(constraintAnchor2.getAnchor(ConstraintAnchor.Type.RIGHT), 0, paramInt2);
        getAnchor(ConstraintAnchor.Type.CENTER_X).connect(constraintAnchor2.getAnchor((ConstraintAnchor.Type)constraintAnchor), 0, paramInt2);
      } else if (constraintAnchor1 == ConstraintAnchor.Type.CENTER_Y && constraintAnchor == ConstraintAnchor.Type.CENTER_Y) {
        getAnchor(ConstraintAnchor.Type.TOP).connect(constraintAnchor2.getAnchor(ConstraintAnchor.Type.TOP), 0, paramInt2);
        getAnchor(ConstraintAnchor.Type.BOTTOM).connect(constraintAnchor2.getAnchor(ConstraintAnchor.Type.BOTTOM), 0, paramInt2);
        getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(constraintAnchor2.getAnchor((ConstraintAnchor.Type)constraintAnchor), 0, paramInt2);
      } else {
        ConstraintAnchor constraintAnchor3 = getAnchor((ConstraintAnchor.Type)constraintAnchor1);
        constraintAnchor2 = constraintAnchor2.getAnchor((ConstraintAnchor.Type)constraintAnchor);
        if (constraintAnchor3.isValidConnection(constraintAnchor2)) {
          if (constraintAnchor1 == ConstraintAnchor.Type.BASELINE) {
            constraintAnchor1 = getAnchor(ConstraintAnchor.Type.TOP);
            constraintAnchor = getAnchor(ConstraintAnchor.Type.BOTTOM);
            if (constraintAnchor1 != null)
              constraintAnchor1.reset(); 
            paramInt1 = bool;
            if (constraintAnchor != null) {
              constraintAnchor.reset();
              paramInt1 = bool;
            } 
          } else if (constraintAnchor1 == ConstraintAnchor.Type.TOP || constraintAnchor1 == ConstraintAnchor.Type.BOTTOM) {
            constraintAnchor = getAnchor(ConstraintAnchor.Type.BASELINE);
            if (constraintAnchor != null)
              constraintAnchor.reset(); 
            constraintAnchor = getAnchor(ConstraintAnchor.Type.CENTER);
            if (constraintAnchor.getTarget() != constraintAnchor2)
              constraintAnchor.reset(); 
            constraintAnchor = getAnchor((ConstraintAnchor.Type)constraintAnchor1).getOpposite();
            constraintAnchor1 = getAnchor(ConstraintAnchor.Type.CENTER_Y);
            if (constraintAnchor1.isConnected()) {
              constraintAnchor.reset();
              constraintAnchor1.reset();
            } 
          } else if (constraintAnchor1 == ConstraintAnchor.Type.LEFT || constraintAnchor1 == ConstraintAnchor.Type.RIGHT) {
            constraintAnchor = getAnchor(ConstraintAnchor.Type.CENTER);
            if (constraintAnchor.getTarget() != constraintAnchor2)
              constraintAnchor.reset(); 
            constraintAnchor = getAnchor((ConstraintAnchor.Type)constraintAnchor1).getOpposite();
            constraintAnchor1 = getAnchor(ConstraintAnchor.Type.CENTER_X);
            if (constraintAnchor1.isConnected()) {
              constraintAnchor.reset();
              constraintAnchor1.reset();
            } 
          } 
          constraintAnchor3.connect(constraintAnchor2, paramInt1, paramStrength, paramInt2);
          constraintAnchor2.getOwner().connectedTo(constraintAnchor3.getOwner());
        } 
      } 
    } 
  }
  
  public void connect(ConstraintAnchor paramConstraintAnchor1, ConstraintAnchor paramConstraintAnchor2, int paramInt) {
    connect(paramConstraintAnchor1, paramConstraintAnchor2, paramInt, ConstraintAnchor.Strength.STRONG, 0);
  }
  
  public void connect(ConstraintAnchor paramConstraintAnchor1, ConstraintAnchor paramConstraintAnchor2, int paramInt1, int paramInt2) {
    connect(paramConstraintAnchor1, paramConstraintAnchor2, paramInt1, ConstraintAnchor.Strength.STRONG, paramInt2);
  }
  
  public void connect(ConstraintAnchor paramConstraintAnchor1, ConstraintAnchor paramConstraintAnchor2, int paramInt1, ConstraintAnchor.Strength paramStrength, int paramInt2) {
    if (paramConstraintAnchor1.getOwner() == this)
      connect(paramConstraintAnchor1.getType(), paramConstraintAnchor2.getOwner(), paramConstraintAnchor2.getType(), paramInt1, paramStrength, paramInt2); 
  }
  
  public void connectCircularConstraint(ConstraintWidget paramConstraintWidget, float paramFloat, int paramInt) {
    immediateConnect(ConstraintAnchor.Type.CENTER, paramConstraintWidget, ConstraintAnchor.Type.CENTER, paramInt, 0);
    this.mCircleConstraintAngle = paramFloat;
  }
  
  public void connectedTo(ConstraintWidget paramConstraintWidget) {}
  
  public void createObjectVariables(LinearSystem paramLinearSystem) {
    paramLinearSystem.createObjectVariable(this.mLeft);
    paramLinearSystem.createObjectVariable(this.mTop);
    paramLinearSystem.createObjectVariable(this.mRight);
    paramLinearSystem.createObjectVariable(this.mBottom);
    if (this.mBaselineDistance > 0)
      paramLinearSystem.createObjectVariable(this.mBaseline); 
  }
  
  public void disconnectUnlockedWidget(ConstraintWidget paramConstraintWidget) {
    ArrayList<ConstraintAnchor> arrayList = getAnchors();
    int i = arrayList.size();
    for (byte b = 0; b < i; b++) {
      ConstraintAnchor constraintAnchor = arrayList.get(b);
      if (constraintAnchor.isConnected() && constraintAnchor.getTarget().getOwner() == paramConstraintWidget && constraintAnchor.getConnectionCreator() == 2)
        constraintAnchor.reset(); 
    } 
  }
  
  public void disconnectWidget(ConstraintWidget paramConstraintWidget) {
    ArrayList<ConstraintAnchor> arrayList = getAnchors();
    int i = arrayList.size();
    for (byte b = 0; b < i; b++) {
      ConstraintAnchor constraintAnchor = arrayList.get(b);
      if (constraintAnchor.isConnected() && constraintAnchor.getTarget().getOwner() == paramConstraintWidget)
        constraintAnchor.reset(); 
    } 
  }
  
  public void forceUpdateDrawPosition() {
    int i = this.mX;
    int j = this.mY;
    int k = this.mX;
    int m = this.mWidth;
    int n = this.mY;
    int i1 = this.mHeight;
    this.mDrawX = i;
    this.mDrawY = j;
    this.mDrawWidth = k + m - i;
    this.mDrawHeight = n + i1 - j;
  }
  
  public ConstraintAnchor getAnchor(ConstraintAnchor.Type paramType) {
    switch (paramType) {
      default:
        throw new AssertionError(paramType.name());
      case null:
        return null;
      case null:
        return this.mCenterY;
      case null:
        return this.mCenterX;
      case null:
        return this.mCenter;
      case null:
        return this.mBaseline;
      case MATCH_CONSTRAINT:
        return this.mBottom;
      case MATCH_PARENT:
        return this.mRight;
      case WRAP_CONTENT:
        return this.mTop;
      case FIXED:
        break;
    } 
    return this.mLeft;
  }
  
  public ArrayList<ConstraintAnchor> getAnchors() {
    return this.mAnchors;
  }
  
  public int getBaselineDistance() {
    return this.mBaselineDistance;
  }
  
  public float getBiasPercent(int paramInt) {
    return (paramInt == 0) ? this.mHorizontalBiasPercent : ((paramInt == 1) ? this.mVerticalBiasPercent : -1.0F);
  }
  
  public int getBottom() {
    return getY() + this.mHeight;
  }
  
  public Object getCompanionWidget() {
    return this.mCompanionWidget;
  }
  
  public int getContainerItemSkip() {
    return this.mContainerItemSkip;
  }
  
  public String getDebugName() {
    return this.mDebugName;
  }
  
  public DimensionBehaviour getDimensionBehaviour(int paramInt) {
    return (paramInt == 0) ? getHorizontalDimensionBehaviour() : ((paramInt == 1) ? getVerticalDimensionBehaviour() : null);
  }
  
  public float getDimensionRatio() {
    return this.mDimensionRatio;
  }
  
  public int getDimensionRatioSide() {
    return this.mDimensionRatioSide;
  }
  
  public int getDrawBottom() {
    return getDrawY() + this.mDrawHeight;
  }
  
  public int getDrawHeight() {
    return this.mDrawHeight;
  }
  
  public int getDrawRight() {
    return getDrawX() + this.mDrawWidth;
  }
  
  public int getDrawWidth() {
    return this.mDrawWidth;
  }
  
  public int getDrawX() {
    return this.mDrawX + this.mOffsetX;
  }
  
  public int getDrawY() {
    return this.mDrawY + this.mOffsetY;
  }
  
  public int getHeight() {
    return (this.mVisibility == 8) ? 0 : this.mHeight;
  }
  
  public float getHorizontalBiasPercent() {
    return this.mHorizontalBiasPercent;
  }
  
  public ConstraintWidget getHorizontalChainControlWidget() {
    ConstraintWidget constraintWidget;
    if (isInHorizontalChain()) {
      ConstraintWidget constraintWidget1 = this;
      ConstraintAnchor constraintAnchor = null;
      while (true) {
        constraintWidget = (ConstraintWidget)constraintAnchor;
        if (constraintAnchor == null) {
          constraintWidget = (ConstraintWidget)constraintAnchor;
          if (constraintWidget1 != null) {
            ConstraintWidget constraintWidget2;
            ConstraintAnchor constraintAnchor1;
            constraintWidget = (ConstraintWidget)constraintWidget1.getAnchor(ConstraintAnchor.Type.LEFT);
            if (constraintWidget == null) {
              constraintWidget = null;
            } else {
              constraintWidget = (ConstraintWidget)constraintWidget.getTarget();
            } 
            if (constraintWidget == null) {
              constraintWidget = null;
            } else {
              constraintWidget2 = constraintWidget.getOwner();
            } 
            if (constraintWidget2 == getParent()) {
              constraintWidget2 = constraintWidget1;
              break;
            } 
            if (constraintWidget2 == null) {
              constraintAnchor1 = null;
            } else {
              constraintAnchor1 = constraintWidget2.getAnchor(ConstraintAnchor.Type.RIGHT).getTarget();
            } 
            if (constraintAnchor1 != null && constraintAnchor1.getOwner() != constraintWidget1) {
              ConstraintWidget constraintWidget3 = constraintWidget1;
              continue;
            } 
            constraintWidget1 = constraintWidget2;
            continue;
          } 
        } 
        break;
      } 
    } else {
      constraintWidget = null;
    } 
    return constraintWidget;
  }
  
  public int getHorizontalChainStyle() {
    return this.mHorizontalChainStyle;
  }
  
  public DimensionBehaviour getHorizontalDimensionBehaviour() {
    return this.mListDimensionBehaviors[0];
  }
  
  public int getInternalDrawBottom() {
    return this.mDrawY + this.mDrawHeight;
  }
  
  public int getInternalDrawRight() {
    return this.mDrawX + this.mDrawWidth;
  }
  
  int getInternalDrawX() {
    return this.mDrawX;
  }
  
  int getInternalDrawY() {
    return this.mDrawY;
  }
  
  public int getLeft() {
    return getX();
  }
  
  public int getLength(int paramInt) {
    return (paramInt == 0) ? getWidth() : ((paramInt == 1) ? getHeight() : 0);
  }
  
  public int getMaxHeight() {
    return this.mMaxDimension[1];
  }
  
  public int getMaxWidth() {
    return this.mMaxDimension[0];
  }
  
  public int getMinHeight() {
    return this.mMinHeight;
  }
  
  public int getMinWidth() {
    return this.mMinWidth;
  }
  
  public int getOptimizerWrapHeight() {
    int i = this.mHeight;
    int j = i;
    if (this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT) {
      if (this.mMatchConstraintDefaultHeight == 1) {
        i = Math.max(this.mMatchConstraintMinHeight, i);
      } else if (this.mMatchConstraintMinHeight > 0) {
        i = this.mMatchConstraintMinHeight;
        this.mHeight = i;
      } else {
        i = 0;
      } 
      j = i;
      if (this.mMatchConstraintMaxHeight > 0) {
        j = i;
        if (this.mMatchConstraintMaxHeight < i)
          j = this.mMatchConstraintMaxHeight; 
      } 
    } 
    return j;
  }
  
  public int getOptimizerWrapWidth() {
    int i = this.mWidth;
    int j = i;
    if (this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT) {
      if (this.mMatchConstraintDefaultWidth == 1) {
        i = Math.max(this.mMatchConstraintMinWidth, i);
      } else if (this.mMatchConstraintMinWidth > 0) {
        i = this.mMatchConstraintMinWidth;
        this.mWidth = i;
      } else {
        i = 0;
      } 
      j = i;
      if (this.mMatchConstraintMaxWidth > 0) {
        j = i;
        if (this.mMatchConstraintMaxWidth < i)
          j = this.mMatchConstraintMaxWidth; 
      } 
    } 
    return j;
  }
  
  public ConstraintWidget getParent() {
    return this.mParent;
  }
  
  int getRelativePositioning(int paramInt) {
    return (paramInt == 0) ? this.mRelX : ((paramInt == 1) ? this.mRelY : 0);
  }
  
  public ResolutionDimension getResolutionHeight() {
    if (this.mResolutionHeight == null)
      this.mResolutionHeight = new ResolutionDimension(); 
    return this.mResolutionHeight;
  }
  
  public ResolutionDimension getResolutionWidth() {
    if (this.mResolutionWidth == null)
      this.mResolutionWidth = new ResolutionDimension(); 
    return this.mResolutionWidth;
  }
  
  public int getRight() {
    return getX() + this.mWidth;
  }
  
  public WidgetContainer getRootWidgetContainer() {
    ConstraintWidget constraintWidget;
    for (constraintWidget = this; constraintWidget.getParent() != null; constraintWidget = constraintWidget.getParent());
    return (constraintWidget instanceof WidgetContainer) ? (WidgetContainer)constraintWidget : null;
  }
  
  protected int getRootX() {
    return this.mX + this.mOffsetX;
  }
  
  protected int getRootY() {
    return this.mY + this.mOffsetY;
  }
  
  public int getTop() {
    return getY();
  }
  
  public String getType() {
    return this.mType;
  }
  
  public float getVerticalBiasPercent() {
    return this.mVerticalBiasPercent;
  }
  
  public ConstraintWidget getVerticalChainControlWidget() {
    ConstraintWidget constraintWidget;
    if (isInVerticalChain()) {
      ConstraintWidget constraintWidget1 = this;
      ConstraintAnchor constraintAnchor = null;
      while (true) {
        constraintWidget = (ConstraintWidget)constraintAnchor;
        if (constraintAnchor == null) {
          constraintWidget = (ConstraintWidget)constraintAnchor;
          if (constraintWidget1 != null) {
            ConstraintWidget constraintWidget2;
            ConstraintAnchor constraintAnchor1;
            constraintWidget = (ConstraintWidget)constraintWidget1.getAnchor(ConstraintAnchor.Type.TOP);
            if (constraintWidget == null) {
              constraintWidget = null;
            } else {
              constraintWidget = (ConstraintWidget)constraintWidget.getTarget();
            } 
            if (constraintWidget == null) {
              constraintWidget = null;
            } else {
              constraintWidget2 = constraintWidget.getOwner();
            } 
            if (constraintWidget2 == getParent()) {
              constraintWidget2 = constraintWidget1;
              break;
            } 
            if (constraintWidget2 == null) {
              constraintAnchor1 = null;
            } else {
              constraintAnchor1 = constraintWidget2.getAnchor(ConstraintAnchor.Type.BOTTOM).getTarget();
            } 
            if (constraintAnchor1 != null && constraintAnchor1.getOwner() != constraintWidget1) {
              ConstraintWidget constraintWidget3 = constraintWidget1;
              continue;
            } 
            constraintWidget1 = constraintWidget2;
            continue;
          } 
        } 
        break;
      } 
    } else {
      constraintWidget = null;
    } 
    return constraintWidget;
  }
  
  public int getVerticalChainStyle() {
    return this.mVerticalChainStyle;
  }
  
  public DimensionBehaviour getVerticalDimensionBehaviour() {
    return this.mListDimensionBehaviors[1];
  }
  
  public int getVisibility() {
    return this.mVisibility;
  }
  
  public int getWidth() {
    return (this.mVisibility == 8) ? 0 : this.mWidth;
  }
  
  public int getWrapHeight() {
    return this.mWrapHeight;
  }
  
  public int getWrapWidth() {
    return this.mWrapWidth;
  }
  
  public int getX() {
    return this.mX;
  }
  
  public int getY() {
    return this.mY;
  }
  
  public boolean hasAncestor(ConstraintWidget paramConstraintWidget) {
    ConstraintWidget constraintWidget1 = getParent();
    if (constraintWidget1 == paramConstraintWidget)
      return true; 
    ConstraintWidget constraintWidget2 = constraintWidget1;
    if (constraintWidget1 == paramConstraintWidget.getParent())
      return false; 
    while (constraintWidget2 != null) {
      if (constraintWidget2 == paramConstraintWidget)
        return true; 
      if (constraintWidget2 == paramConstraintWidget.getParent())
        return true; 
      constraintWidget2 = constraintWidget2.getParent();
    } 
    return false;
  }
  
  public boolean hasBaseline() {
    boolean bool;
    if (this.mBaselineDistance > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void immediateConnect(ConstraintAnchor.Type paramType1, ConstraintWidget paramConstraintWidget, ConstraintAnchor.Type paramType2, int paramInt1, int paramInt2) {
    getAnchor(paramType1).connect(paramConstraintWidget.getAnchor(paramType2), paramInt1, paramInt2, ConstraintAnchor.Strength.STRONG, 0, true);
  }
  
  public boolean isFullyResolved() {
    return ((this.mLeft.getResolutionNode()).state == 1 && (this.mRight.getResolutionNode()).state == 1 && (this.mTop.getResolutionNode()).state == 1 && (this.mBottom.getResolutionNode()).state == 1);
  }
  
  public boolean isHeightWrapContent() {
    return this.mIsHeightWrapContent;
  }
  
  public boolean isInHorizontalChain() {
    return ((this.mLeft.mTarget != null && this.mLeft.mTarget.mTarget == this.mLeft) || (this.mRight.mTarget != null && this.mRight.mTarget.mTarget == this.mRight));
  }
  
  public boolean isInVerticalChain() {
    return ((this.mTop.mTarget != null && this.mTop.mTarget.mTarget == this.mTop) || (this.mBottom.mTarget != null && this.mBottom.mTarget.mTarget == this.mBottom));
  }
  
  public boolean isInsideConstraintLayout() {
    ConstraintWidget constraintWidget1 = getParent();
    ConstraintWidget constraintWidget2 = constraintWidget1;
    if (constraintWidget1 == null)
      return false; 
    while (constraintWidget2 != null) {
      if (constraintWidget2 instanceof ConstraintWidgetContainer)
        return true; 
      constraintWidget2 = constraintWidget2.getParent();
    } 
    return false;
  }
  
  public boolean isRoot() {
    boolean bool;
    if (this.mParent == null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isRootContainer() {
    boolean bool;
    if (this instanceof ConstraintWidgetContainer && (this.mParent == null || !(this.mParent instanceof ConstraintWidgetContainer))) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isSpreadHeight() {
    int i = this.mMatchConstraintDefaultHeight;
    boolean bool = true;
    if (i != 0 || this.mDimensionRatio != 0.0F || this.mMatchConstraintMinHeight != 0 || this.mMatchConstraintMaxHeight != 0 || this.mListDimensionBehaviors[1] != DimensionBehaviour.MATCH_CONSTRAINT)
      bool = false; 
    return bool;
  }
  
  public boolean isSpreadWidth() {
    int i = this.mMatchConstraintDefaultWidth;
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (i == 0) {
      bool2 = bool1;
      if (this.mDimensionRatio == 0.0F) {
        bool2 = bool1;
        if (this.mMatchConstraintMinWidth == 0) {
          bool2 = bool1;
          if (this.mMatchConstraintMaxWidth == 0) {
            bool2 = bool1;
            if (this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT)
              bool2 = true; 
          } 
        } 
      } 
    } 
    return bool2;
  }
  
  public boolean isWidthWrapContent() {
    return this.mIsWidthWrapContent;
  }
  
  public void reset() {
    this.mLeft.reset();
    this.mTop.reset();
    this.mRight.reset();
    this.mBottom.reset();
    this.mBaseline.reset();
    this.mCenterX.reset();
    this.mCenterY.reset();
    this.mCenter.reset();
    this.mParent = null;
    this.mCircleConstraintAngle = 0.0F;
    this.mWidth = 0;
    this.mHeight = 0;
    this.mDimensionRatio = 0.0F;
    this.mDimensionRatioSide = -1;
    this.mX = 0;
    this.mY = 0;
    this.mDrawX = 0;
    this.mDrawY = 0;
    this.mDrawWidth = 0;
    this.mDrawHeight = 0;
    this.mOffsetX = 0;
    this.mOffsetY = 0;
    this.mBaselineDistance = 0;
    this.mMinWidth = 0;
    this.mMinHeight = 0;
    this.mWrapWidth = 0;
    this.mWrapHeight = 0;
    this.mHorizontalBiasPercent = DEFAULT_BIAS;
    this.mVerticalBiasPercent = DEFAULT_BIAS;
    this.mListDimensionBehaviors[0] = DimensionBehaviour.FIXED;
    this.mListDimensionBehaviors[1] = DimensionBehaviour.FIXED;
    this.mCompanionWidget = null;
    this.mContainerItemSkip = 0;
    this.mVisibility = 0;
    this.mType = null;
    this.mHorizontalWrapVisited = false;
    this.mVerticalWrapVisited = false;
    this.mHorizontalChainStyle = 0;
    this.mVerticalChainStyle = 0;
    this.mHorizontalChainFixedPosition = false;
    this.mVerticalChainFixedPosition = false;
    this.mWeight[0] = -1.0F;
    this.mWeight[1] = -1.0F;
    this.mHorizontalResolution = -1;
    this.mVerticalResolution = -1;
    this.mMaxDimension[0] = Integer.MAX_VALUE;
    this.mMaxDimension[1] = Integer.MAX_VALUE;
    this.mMatchConstraintDefaultWidth = 0;
    this.mMatchConstraintDefaultHeight = 0;
    this.mMatchConstraintPercentWidth = 1.0F;
    this.mMatchConstraintPercentHeight = 1.0F;
    this.mMatchConstraintMaxWidth = Integer.MAX_VALUE;
    this.mMatchConstraintMaxHeight = Integer.MAX_VALUE;
    this.mMatchConstraintMinWidth = 0;
    this.mMatchConstraintMinHeight = 0;
    this.mResolvedDimensionRatioSide = -1;
    this.mResolvedDimensionRatio = 1.0F;
    if (this.mResolutionWidth != null)
      this.mResolutionWidth.reset(); 
    if (this.mResolutionHeight != null)
      this.mResolutionHeight.reset(); 
    this.mBelongingGroup = null;
    this.mOptimizerMeasurable = false;
    this.mOptimizerMeasured = false;
    this.mGroupsToSolver = false;
  }
  
  public void resetAllConstraints() {
    resetAnchors();
    setVerticalBiasPercent(DEFAULT_BIAS);
    setHorizontalBiasPercent(DEFAULT_BIAS);
    if (this instanceof ConstraintWidgetContainer)
      return; 
    if (getHorizontalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT)
      if (getWidth() == getWrapWidth()) {
        setHorizontalDimensionBehaviour(DimensionBehaviour.WRAP_CONTENT);
      } else if (getWidth() > getMinWidth()) {
        setHorizontalDimensionBehaviour(DimensionBehaviour.FIXED);
      }  
    if (getVerticalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT)
      if (getHeight() == getWrapHeight()) {
        setVerticalDimensionBehaviour(DimensionBehaviour.WRAP_CONTENT);
      } else if (getHeight() > getMinHeight()) {
        setVerticalDimensionBehaviour(DimensionBehaviour.FIXED);
      }  
  }
  
  public void resetAnchor(ConstraintAnchor paramConstraintAnchor) {
    if (getParent() != null && getParent() instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)getParent()).handlesInternalConstraints())
      return; 
    ConstraintAnchor constraintAnchor1 = getAnchor(ConstraintAnchor.Type.LEFT);
    ConstraintAnchor constraintAnchor2 = getAnchor(ConstraintAnchor.Type.RIGHT);
    ConstraintAnchor constraintAnchor3 = getAnchor(ConstraintAnchor.Type.TOP);
    ConstraintAnchor constraintAnchor4 = getAnchor(ConstraintAnchor.Type.BOTTOM);
    ConstraintAnchor constraintAnchor5 = getAnchor(ConstraintAnchor.Type.CENTER);
    ConstraintAnchor constraintAnchor6 = getAnchor(ConstraintAnchor.Type.CENTER_X);
    ConstraintAnchor constraintAnchor7 = getAnchor(ConstraintAnchor.Type.CENTER_Y);
    if (paramConstraintAnchor == constraintAnchor5) {
      if (constraintAnchor1.isConnected() && constraintAnchor2.isConnected() && constraintAnchor1.getTarget() == constraintAnchor2.getTarget()) {
        constraintAnchor1.reset();
        constraintAnchor2.reset();
      } 
      if (constraintAnchor3.isConnected() && constraintAnchor4.isConnected() && constraintAnchor3.getTarget() == constraintAnchor4.getTarget()) {
        constraintAnchor3.reset();
        constraintAnchor4.reset();
      } 
      this.mHorizontalBiasPercent = 0.5F;
      this.mVerticalBiasPercent = 0.5F;
    } else if (paramConstraintAnchor == constraintAnchor6) {
      if (constraintAnchor1.isConnected() && constraintAnchor2.isConnected() && constraintAnchor1.getTarget().getOwner() == constraintAnchor2.getTarget().getOwner()) {
        constraintAnchor1.reset();
        constraintAnchor2.reset();
      } 
      this.mHorizontalBiasPercent = 0.5F;
    } else if (paramConstraintAnchor == constraintAnchor7) {
      if (constraintAnchor3.isConnected() && constraintAnchor4.isConnected() && constraintAnchor3.getTarget().getOwner() == constraintAnchor4.getTarget().getOwner()) {
        constraintAnchor3.reset();
        constraintAnchor4.reset();
      } 
      this.mVerticalBiasPercent = 0.5F;
    } else if (paramConstraintAnchor == constraintAnchor1 || paramConstraintAnchor == constraintAnchor2) {
      if (constraintAnchor1.isConnected() && constraintAnchor1.getTarget() == constraintAnchor2.getTarget())
        constraintAnchor5.reset(); 
    } else if ((paramConstraintAnchor == constraintAnchor3 || paramConstraintAnchor == constraintAnchor4) && constraintAnchor3.isConnected() && constraintAnchor3.getTarget() == constraintAnchor4.getTarget()) {
      constraintAnchor5.reset();
    } 
    paramConstraintAnchor.reset();
  }
  
  public void resetAnchors() {
    ConstraintWidget constraintWidget = getParent();
    if (constraintWidget != null && constraintWidget instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)getParent()).handlesInternalConstraints())
      return; 
    byte b = 0;
    int i = this.mAnchors.size();
    while (b < i) {
      ((ConstraintAnchor)this.mAnchors.get(b)).reset();
      b++;
    } 
  }
  
  public void resetAnchors(int paramInt) {
    ConstraintWidget constraintWidget = getParent();
    if (constraintWidget != null && constraintWidget instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)getParent()).handlesInternalConstraints())
      return; 
    byte b = 0;
    int i = this.mAnchors.size();
    while (b < i) {
      ConstraintAnchor constraintAnchor = this.mAnchors.get(b);
      if (paramInt == constraintAnchor.getConnectionCreator()) {
        if (constraintAnchor.isVerticalAnchor()) {
          setVerticalBiasPercent(DEFAULT_BIAS);
        } else {
          setHorizontalBiasPercent(DEFAULT_BIAS);
        } 
        constraintAnchor.reset();
      } 
      b++;
    } 
  }
  
  public void resetResolutionNodes() {
    for (byte b = 0; b < 6; b++)
      this.mListAnchors[b].getResolutionNode().reset(); 
  }
  
  public void resetSolverVariables(Cache paramCache) {
    this.mLeft.resetSolverVariable(paramCache);
    this.mTop.resetSolverVariable(paramCache);
    this.mRight.resetSolverVariable(paramCache);
    this.mBottom.resetSolverVariable(paramCache);
    this.mBaseline.resetSolverVariable(paramCache);
    this.mCenter.resetSolverVariable(paramCache);
    this.mCenterX.resetSolverVariable(paramCache);
    this.mCenterY.resetSolverVariable(paramCache);
  }
  
  public void resolve() {}
  
  public void setBaselineDistance(int paramInt) {
    this.mBaselineDistance = paramInt;
  }
  
  public void setCompanionWidget(Object paramObject) {
    this.mCompanionWidget = paramObject;
  }
  
  public void setContainerItemSkip(int paramInt) {
    if (paramInt >= 0) {
      this.mContainerItemSkip = paramInt;
    } else {
      this.mContainerItemSkip = 0;
    } 
  }
  
  public void setDebugName(String paramString) {
    this.mDebugName = paramString;
  }
  
  public void setDebugSolverName(LinearSystem paramLinearSystem, String paramString) {
    this.mDebugName = paramString;
    SolverVariable solverVariable1 = paramLinearSystem.createObjectVariable(this.mLeft);
    SolverVariable solverVariable2 = paramLinearSystem.createObjectVariable(this.mTop);
    SolverVariable solverVariable3 = paramLinearSystem.createObjectVariable(this.mRight);
    SolverVariable solverVariable4 = paramLinearSystem.createObjectVariable(this.mBottom);
    StringBuilder stringBuilder4 = new StringBuilder();
    stringBuilder4.append(paramString);
    stringBuilder4.append(".left");
    solverVariable1.setName(stringBuilder4.toString());
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append(paramString);
    stringBuilder1.append(".top");
    solverVariable2.setName(stringBuilder1.toString());
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append(paramString);
    stringBuilder2.append(".right");
    solverVariable3.setName(stringBuilder2.toString());
    StringBuilder stringBuilder3 = new StringBuilder();
    stringBuilder3.append(paramString);
    stringBuilder3.append(".bottom");
    solverVariable4.setName(stringBuilder3.toString());
    if (this.mBaselineDistance > 0) {
      SolverVariable solverVariable = paramLinearSystem.createObjectVariable(this.mBaseline);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(paramString);
      stringBuilder.append(".baseline");
      solverVariable.setName(stringBuilder.toString());
    } 
  }
  
  public void setDimension(int paramInt1, int paramInt2) {
    this.mWidth = paramInt1;
    if (this.mWidth < this.mMinWidth)
      this.mWidth = this.mMinWidth; 
    this.mHeight = paramInt2;
    if (this.mHeight < this.mMinHeight)
      this.mHeight = this.mMinHeight; 
  }
  
  public void setDimensionRatio(float paramFloat, int paramInt) {
    this.mDimensionRatio = paramFloat;
    this.mDimensionRatioSide = paramInt;
  }
  
  public void setDimensionRatio(String paramString) {
    // Byte code:
    //   0: aload_1
    //   1: ifnull -> 263
    //   4: aload_1
    //   5: invokevirtual length : ()I
    //   8: ifne -> 14
    //   11: goto -> 263
    //   14: iconst_m1
    //   15: istore_2
    //   16: aload_1
    //   17: invokevirtual length : ()I
    //   20: istore_3
    //   21: aload_1
    //   22: bipush #44
    //   24: invokevirtual indexOf : (I)I
    //   27: istore #4
    //   29: iconst_0
    //   30: istore #5
    //   32: iload_2
    //   33: istore #6
    //   35: iload #5
    //   37: istore #7
    //   39: iload #4
    //   41: ifle -> 108
    //   44: iload_2
    //   45: istore #6
    //   47: iload #5
    //   49: istore #7
    //   51: iload #4
    //   53: iload_3
    //   54: iconst_1
    //   55: isub
    //   56: if_icmpge -> 108
    //   59: aload_1
    //   60: iconst_0
    //   61: iload #4
    //   63: invokevirtual substring : (II)Ljava/lang/String;
    //   66: astore #8
    //   68: aload #8
    //   70: ldc_w 'W'
    //   73: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   76: ifeq -> 85
    //   79: iconst_0
    //   80: istore #6
    //   82: goto -> 102
    //   85: iload_2
    //   86: istore #6
    //   88: aload #8
    //   90: ldc_w 'H'
    //   93: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   96: ifeq -> 102
    //   99: iconst_1
    //   100: istore #6
    //   102: iload #4
    //   104: iconst_1
    //   105: iadd
    //   106: istore #7
    //   108: aload_1
    //   109: bipush #58
    //   111: invokevirtual indexOf : (I)I
    //   114: istore_2
    //   115: iload_2
    //   116: iflt -> 217
    //   119: iload_2
    //   120: iload_3
    //   121: iconst_1
    //   122: isub
    //   123: if_icmpge -> 217
    //   126: aload_1
    //   127: iload #7
    //   129: iload_2
    //   130: invokevirtual substring : (II)Ljava/lang/String;
    //   133: astore #8
    //   135: aload_1
    //   136: iload_2
    //   137: iconst_1
    //   138: iadd
    //   139: invokevirtual substring : (I)Ljava/lang/String;
    //   142: astore_1
    //   143: aload #8
    //   145: invokevirtual length : ()I
    //   148: ifle -> 240
    //   151: aload_1
    //   152: invokevirtual length : ()I
    //   155: ifle -> 240
    //   158: aload #8
    //   160: invokestatic parseFloat : (Ljava/lang/String;)F
    //   163: fstore #9
    //   165: aload_1
    //   166: invokestatic parseFloat : (Ljava/lang/String;)F
    //   169: fstore #10
    //   171: fload #9
    //   173: fconst_0
    //   174: fcmpl
    //   175: ifle -> 240
    //   178: fload #10
    //   180: fconst_0
    //   181: fcmpl
    //   182: ifle -> 240
    //   185: iload #6
    //   187: iconst_1
    //   188: if_icmpne -> 204
    //   191: fload #10
    //   193: fload #9
    //   195: fdiv
    //   196: invokestatic abs : (F)F
    //   199: fstore #9
    //   201: goto -> 243
    //   204: fload #9
    //   206: fload #10
    //   208: fdiv
    //   209: invokestatic abs : (F)F
    //   212: fstore #9
    //   214: goto -> 243
    //   217: aload_1
    //   218: iload #7
    //   220: invokevirtual substring : (I)Ljava/lang/String;
    //   223: astore_1
    //   224: aload_1
    //   225: invokevirtual length : ()I
    //   228: ifle -> 240
    //   231: aload_1
    //   232: invokestatic parseFloat : (Ljava/lang/String;)F
    //   235: fstore #9
    //   237: goto -> 243
    //   240: fconst_0
    //   241: fstore #9
    //   243: fload #9
    //   245: fconst_0
    //   246: fcmpl
    //   247: ifle -> 262
    //   250: aload_0
    //   251: fload #9
    //   253: putfield mDimensionRatio : F
    //   256: aload_0
    //   257: iload #6
    //   259: putfield mDimensionRatioSide : I
    //   262: return
    //   263: aload_0
    //   264: fconst_0
    //   265: putfield mDimensionRatio : F
    //   268: return
    //   269: astore_1
    //   270: goto -> 240
    // Exception table:
    //   from	to	target	type
    //   158	171	269	java/lang/NumberFormatException
    //   191	201	269	java/lang/NumberFormatException
    //   204	214	269	java/lang/NumberFormatException
    //   231	237	269	java/lang/NumberFormatException
  }
  
  public void setDrawHeight(int paramInt) {
    this.mDrawHeight = paramInt;
  }
  
  public void setDrawOrigin(int paramInt1, int paramInt2) {
    this.mDrawX = paramInt1 - this.mOffsetX;
    this.mDrawY = paramInt2 - this.mOffsetY;
    this.mX = this.mDrawX;
    this.mY = this.mDrawY;
  }
  
  public void setDrawWidth(int paramInt) {
    this.mDrawWidth = paramInt;
  }
  
  public void setDrawX(int paramInt) {
    this.mDrawX = paramInt - this.mOffsetX;
    this.mX = this.mDrawX;
  }
  
  public void setDrawY(int paramInt) {
    this.mDrawY = paramInt - this.mOffsetY;
    this.mY = this.mDrawY;
  }
  
  public void setFrame(int paramInt1, int paramInt2, int paramInt3) {
    if (paramInt3 == 0) {
      setHorizontalDimension(paramInt1, paramInt2);
    } else if (paramInt3 == 1) {
      setVerticalDimension(paramInt1, paramInt2);
    } 
    this.mOptimizerMeasured = true;
  }
  
  public void setFrame(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = paramInt3 - paramInt1;
    paramInt3 = paramInt4 - paramInt2;
    this.mX = paramInt1;
    this.mY = paramInt2;
    if (this.mVisibility == 8) {
      this.mWidth = 0;
      this.mHeight = 0;
      return;
    } 
    paramInt1 = i;
    if (this.mListDimensionBehaviors[0] == DimensionBehaviour.FIXED) {
      paramInt1 = i;
      if (i < this.mWidth)
        paramInt1 = this.mWidth; 
    } 
    paramInt2 = paramInt3;
    if (this.mListDimensionBehaviors[1] == DimensionBehaviour.FIXED) {
      paramInt2 = paramInt3;
      if (paramInt3 < this.mHeight)
        paramInt2 = this.mHeight; 
    } 
    this.mWidth = paramInt1;
    this.mHeight = paramInt2;
    if (this.mHeight < this.mMinHeight)
      this.mHeight = this.mMinHeight; 
    if (this.mWidth < this.mMinWidth)
      this.mWidth = this.mMinWidth; 
    this.mOptimizerMeasured = true;
  }
  
  public void setGoneMargin(ConstraintAnchor.Type paramType, int paramInt) {
    switch (paramType) {
      default:
        return;
      case MATCH_CONSTRAINT:
        this.mBottom.mGoneMargin = paramInt;
      case MATCH_PARENT:
        this.mRight.mGoneMargin = paramInt;
      case WRAP_CONTENT:
        this.mTop.mGoneMargin = paramInt;
      case FIXED:
        break;
    } 
    this.mLeft.mGoneMargin = paramInt;
  }
  
  public void setHeight(int paramInt) {
    this.mHeight = paramInt;
    if (this.mHeight < this.mMinHeight)
      this.mHeight = this.mMinHeight; 
  }
  
  public void setHeightWrapContent(boolean paramBoolean) {
    this.mIsHeightWrapContent = paramBoolean;
  }
  
  public void setHorizontalBiasPercent(float paramFloat) {
    this.mHorizontalBiasPercent = paramFloat;
  }
  
  public void setHorizontalChainStyle(int paramInt) {
    this.mHorizontalChainStyle = paramInt;
  }
  
  public void setHorizontalDimension(int paramInt1, int paramInt2) {
    this.mX = paramInt1;
    this.mWidth = paramInt2 - paramInt1;
    if (this.mWidth < this.mMinWidth)
      this.mWidth = this.mMinWidth; 
  }
  
  public void setHorizontalDimensionBehaviour(DimensionBehaviour paramDimensionBehaviour) {
    this.mListDimensionBehaviors[0] = paramDimensionBehaviour;
    if (paramDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT)
      setWidth(this.mWrapWidth); 
  }
  
  public void setHorizontalMatchStyle(int paramInt1, int paramInt2, int paramInt3, float paramFloat) {
    this.mMatchConstraintDefaultWidth = paramInt1;
    this.mMatchConstraintMinWidth = paramInt2;
    this.mMatchConstraintMaxWidth = paramInt3;
    this.mMatchConstraintPercentWidth = paramFloat;
    if (paramFloat < 1.0F && this.mMatchConstraintDefaultWidth == 0)
      this.mMatchConstraintDefaultWidth = 2; 
  }
  
  public void setHorizontalWeight(float paramFloat) {
    this.mWeight[0] = paramFloat;
  }
  
  public void setLength(int paramInt1, int paramInt2) {
    if (paramInt2 == 0) {
      setWidth(paramInt1);
    } else if (paramInt2 == 1) {
      setHeight(paramInt1);
    } 
  }
  
  public void setMaxHeight(int paramInt) {
    this.mMaxDimension[1] = paramInt;
  }
  
  public void setMaxWidth(int paramInt) {
    this.mMaxDimension[0] = paramInt;
  }
  
  public void setMinHeight(int paramInt) {
    if (paramInt < 0) {
      this.mMinHeight = 0;
    } else {
      this.mMinHeight = paramInt;
    } 
  }
  
  public void setMinWidth(int paramInt) {
    if (paramInt < 0) {
      this.mMinWidth = 0;
    } else {
      this.mMinWidth = paramInt;
    } 
  }
  
  public void setOffset(int paramInt1, int paramInt2) {
    this.mOffsetX = paramInt1;
    this.mOffsetY = paramInt2;
  }
  
  public void setOrigin(int paramInt1, int paramInt2) {
    this.mX = paramInt1;
    this.mY = paramInt2;
  }
  
  public void setParent(ConstraintWidget paramConstraintWidget) {
    this.mParent = paramConstraintWidget;
  }
  
  void setRelativePositioning(int paramInt1, int paramInt2) {
    if (paramInt2 == 0) {
      this.mRelX = paramInt1;
    } else if (paramInt2 == 1) {
      this.mRelY = paramInt1;
    } 
  }
  
  public void setType(String paramString) {
    this.mType = paramString;
  }
  
  public void setVerticalBiasPercent(float paramFloat) {
    this.mVerticalBiasPercent = paramFloat;
  }
  
  public void setVerticalChainStyle(int paramInt) {
    this.mVerticalChainStyle = paramInt;
  }
  
  public void setVerticalDimension(int paramInt1, int paramInt2) {
    this.mY = paramInt1;
    this.mHeight = paramInt2 - paramInt1;
    if (this.mHeight < this.mMinHeight)
      this.mHeight = this.mMinHeight; 
  }
  
  public void setVerticalDimensionBehaviour(DimensionBehaviour paramDimensionBehaviour) {
    this.mListDimensionBehaviors[1] = paramDimensionBehaviour;
    if (paramDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT)
      setHeight(this.mWrapHeight); 
  }
  
  public void setVerticalMatchStyle(int paramInt1, int paramInt2, int paramInt3, float paramFloat) {
    this.mMatchConstraintDefaultHeight = paramInt1;
    this.mMatchConstraintMinHeight = paramInt2;
    this.mMatchConstraintMaxHeight = paramInt3;
    this.mMatchConstraintPercentHeight = paramFloat;
    if (paramFloat < 1.0F && this.mMatchConstraintDefaultHeight == 0)
      this.mMatchConstraintDefaultHeight = 2; 
  }
  
  public void setVerticalWeight(float paramFloat) {
    this.mWeight[1] = paramFloat;
  }
  
  public void setVisibility(int paramInt) {
    this.mVisibility = paramInt;
  }
  
  public void setWidth(int paramInt) {
    this.mWidth = paramInt;
    if (this.mWidth < this.mMinWidth)
      this.mWidth = this.mMinWidth; 
  }
  
  public void setWidthWrapContent(boolean paramBoolean) {
    this.mIsWidthWrapContent = paramBoolean;
  }
  
  public void setWrapHeight(int paramInt) {
    this.mWrapHeight = paramInt;
  }
  
  public void setWrapWidth(int paramInt) {
    this.mWrapWidth = paramInt;
  }
  
  public void setX(int paramInt) {
    this.mX = paramInt;
  }
  
  public void setY(int paramInt) {
    this.mY = paramInt;
  }
  
  public void setupDimensionRatio(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) {
    if (this.mResolvedDimensionRatioSide == -1)
      if (paramBoolean3 && !paramBoolean4) {
        this.mResolvedDimensionRatioSide = 0;
      } else if (!paramBoolean3 && paramBoolean4) {
        this.mResolvedDimensionRatioSide = 1;
        if (this.mDimensionRatioSide == -1)
          this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio; 
      }  
    if (this.mResolvedDimensionRatioSide == 0 && (!this.mTop.isConnected() || !this.mBottom.isConnected())) {
      this.mResolvedDimensionRatioSide = 1;
    } else if (this.mResolvedDimensionRatioSide == 1 && (!this.mLeft.isConnected() || !this.mRight.isConnected())) {
      this.mResolvedDimensionRatioSide = 0;
    } 
    if (this.mResolvedDimensionRatioSide == -1 && (!this.mTop.isConnected() || !this.mBottom.isConnected() || !this.mLeft.isConnected() || !this.mRight.isConnected()))
      if (this.mTop.isConnected() && this.mBottom.isConnected()) {
        this.mResolvedDimensionRatioSide = 0;
      } else if (this.mLeft.isConnected() && this.mRight.isConnected()) {
        this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio;
        this.mResolvedDimensionRatioSide = 1;
      }  
    if (this.mResolvedDimensionRatioSide == -1)
      if (paramBoolean1 && !paramBoolean2) {
        this.mResolvedDimensionRatioSide = 0;
      } else if (!paramBoolean1 && paramBoolean2) {
        this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio;
        this.mResolvedDimensionRatioSide = 1;
      }  
    if (this.mResolvedDimensionRatioSide == -1)
      if (this.mMatchConstraintMinWidth > 0 && this.mMatchConstraintMinHeight == 0) {
        this.mResolvedDimensionRatioSide = 0;
      } else if (this.mMatchConstraintMinWidth == 0 && this.mMatchConstraintMinHeight > 0) {
        this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio;
        this.mResolvedDimensionRatioSide = 1;
      }  
    if (this.mResolvedDimensionRatioSide == -1 && paramBoolean1 && paramBoolean2) {
      this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio;
      this.mResolvedDimensionRatioSide = 1;
    } 
  }
  
  public String toString() {
    String str;
    StringBuilder stringBuilder = new StringBuilder();
    if (this.mType != null) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("type: ");
      stringBuilder1.append(this.mType);
      stringBuilder1.append(" ");
      str = stringBuilder1.toString();
    } else {
      str = "";
    } 
    stringBuilder.append(str);
    if (this.mDebugName != null) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("id: ");
      stringBuilder1.append(this.mDebugName);
      stringBuilder1.append(" ");
      String str1 = stringBuilder1.toString();
    } else {
      str = "";
    } 
    stringBuilder.append(str);
    stringBuilder.append("(");
    stringBuilder.append(this.mX);
    stringBuilder.append(", ");
    stringBuilder.append(this.mY);
    stringBuilder.append(") - (");
    stringBuilder.append(this.mWidth);
    stringBuilder.append(" x ");
    stringBuilder.append(this.mHeight);
    stringBuilder.append(") wrap: (");
    stringBuilder.append(this.mWrapWidth);
    stringBuilder.append(" x ");
    stringBuilder.append(this.mWrapHeight);
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
  
  public void updateDrawPosition() {
    int i = this.mX;
    int j = this.mY;
    int k = this.mX;
    int m = this.mWidth;
    int n = this.mY;
    int i1 = this.mHeight;
    this.mDrawX = i;
    this.mDrawY = j;
    this.mDrawWidth = k + m - i;
    this.mDrawHeight = n + i1 - j;
  }
  
  public void updateFromSolver(LinearSystem paramLinearSystem) {
    // Byte code:
    //   0: aload_1
    //   1: aload_0
    //   2: getfield mLeft : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   5: invokevirtual getObjectVariableValue : (Ljava/lang/Object;)I
    //   8: istore_2
    //   9: aload_1
    //   10: aload_0
    //   11: getfield mTop : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   14: invokevirtual getObjectVariableValue : (Ljava/lang/Object;)I
    //   17: istore_3
    //   18: aload_1
    //   19: aload_0
    //   20: getfield mRight : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   23: invokevirtual getObjectVariableValue : (Ljava/lang/Object;)I
    //   26: istore #4
    //   28: aload_1
    //   29: aload_0
    //   30: getfield mBottom : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   33: invokevirtual getObjectVariableValue : (Ljava/lang/Object;)I
    //   36: istore #5
    //   38: iload #4
    //   40: iload_2
    //   41: isub
    //   42: iflt -> 112
    //   45: iload #5
    //   47: iload_3
    //   48: isub
    //   49: iflt -> 112
    //   52: iload_2
    //   53: ldc_w -2147483648
    //   56: if_icmpeq -> 112
    //   59: iload_2
    //   60: ldc 2147483647
    //   62: if_icmpeq -> 112
    //   65: iload_3
    //   66: ldc_w -2147483648
    //   69: if_icmpeq -> 112
    //   72: iload_3
    //   73: ldc 2147483647
    //   75: if_icmpeq -> 112
    //   78: iload #4
    //   80: ldc_w -2147483648
    //   83: if_icmpeq -> 112
    //   86: iload #4
    //   88: ldc 2147483647
    //   90: if_icmpeq -> 112
    //   93: iload #5
    //   95: ldc_w -2147483648
    //   98: if_icmpeq -> 112
    //   101: iload #5
    //   103: istore #6
    //   105: iload #5
    //   107: ldc 2147483647
    //   109: if_icmpne -> 122
    //   112: iconst_0
    //   113: istore #6
    //   115: iconst_0
    //   116: istore_2
    //   117: iconst_0
    //   118: istore_3
    //   119: iconst_0
    //   120: istore #4
    //   122: aload_0
    //   123: iload_2
    //   124: iload_3
    //   125: iload #4
    //   127: iload #6
    //   129: invokevirtual setFrame : (IIII)V
    //   132: return
  }
  
  public void updateResolutionNodes() {
    for (byte b = 0; b < 6; b++)
      this.mListAnchors[b].getResolutionNode().update(); 
  }
  
  public enum ContentAlignment {
    BEGIN, BOTTOM, END, LEFT, MIDDLE, RIGHT, TOP, VERTICAL_MIDDLE;
    
    static {
      BOTTOM = new ContentAlignment("BOTTOM", 5);
      LEFT = new ContentAlignment("LEFT", 6);
      RIGHT = new ContentAlignment("RIGHT", 7);
      $VALUES = new ContentAlignment[] { BEGIN, MIDDLE, END, TOP, VERTICAL_MIDDLE, BOTTOM, LEFT, RIGHT };
    }
  }
  
  public enum DimensionBehaviour {
    FIXED, MATCH_CONSTRAINT, MATCH_PARENT, WRAP_CONTENT;
    
    static {
      $VALUES = new DimensionBehaviour[] { FIXED, WRAP_CONTENT, MATCH_CONSTRAINT, MATCH_PARENT };
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\constraint\solver\widgets\ConstraintWidget.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */