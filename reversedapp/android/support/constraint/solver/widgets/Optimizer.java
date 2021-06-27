package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;

public class Optimizer {
  static final int FLAG_CHAIN_DANGLING = 1;
  
  static final int FLAG_RECOMPUTE_BOUNDS = 2;
  
  static final int FLAG_USE_OPTIMIZE = 0;
  
  public static final int OPTIMIZATION_BARRIER = 2;
  
  public static final int OPTIMIZATION_CHAIN = 4;
  
  public static final int OPTIMIZATION_DIMENSIONS = 8;
  
  public static final int OPTIMIZATION_DIRECT = 1;
  
  public static final int OPTIMIZATION_GROUPS = 32;
  
  public static final int OPTIMIZATION_NONE = 0;
  
  public static final int OPTIMIZATION_RATIO = 16;
  
  public static final int OPTIMIZATION_STANDARD = 7;
  
  static boolean[] flags = new boolean[3];
  
  static void analyze(int paramInt, ConstraintWidget paramConstraintWidget) {
    int i;
    paramConstraintWidget.updateResolutionNodes();
    ResolutionAnchor resolutionAnchor1 = paramConstraintWidget.mLeft.getResolutionNode();
    ResolutionAnchor resolutionAnchor2 = paramConstraintWidget.mTop.getResolutionNode();
    ResolutionAnchor resolutionAnchor3 = paramConstraintWidget.mRight.getResolutionNode();
    ResolutionAnchor resolutionAnchor4 = paramConstraintWidget.mBottom.getResolutionNode();
    if ((paramInt & 0x8) == 8) {
      paramInt = 1;
    } else {
      paramInt = 0;
    } 
    if (paramConstraintWidget.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && optimizableMatchConstraint(paramConstraintWidget, 0)) {
      i = 1;
    } else {
      i = 0;
    } 
    if (resolutionAnchor1.type != 4 && resolutionAnchor3.type != 4)
      if (paramConstraintWidget.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.FIXED || (i && paramConstraintWidget.getVisibility() == 8)) {
        if (paramConstraintWidget.mLeft.mTarget == null && paramConstraintWidget.mRight.mTarget == null) {
          resolutionAnchor1.setType(1);
          resolutionAnchor3.setType(1);
          if (paramInt != 0) {
            resolutionAnchor3.dependsOn(resolutionAnchor1, 1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor3.dependsOn(resolutionAnchor1, paramConstraintWidget.getWidth());
          } 
        } else if (paramConstraintWidget.mLeft.mTarget != null && paramConstraintWidget.mRight.mTarget == null) {
          resolutionAnchor1.setType(1);
          resolutionAnchor3.setType(1);
          if (paramInt != 0) {
            resolutionAnchor3.dependsOn(resolutionAnchor1, 1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor3.dependsOn(resolutionAnchor1, paramConstraintWidget.getWidth());
          } 
        } else if (paramConstraintWidget.mLeft.mTarget == null && paramConstraintWidget.mRight.mTarget != null) {
          resolutionAnchor1.setType(1);
          resolutionAnchor3.setType(1);
          resolutionAnchor1.dependsOn(resolutionAnchor3, -paramConstraintWidget.getWidth());
          if (paramInt != 0) {
            resolutionAnchor1.dependsOn(resolutionAnchor3, -1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor1.dependsOn(resolutionAnchor3, -paramConstraintWidget.getWidth());
          } 
        } else if (paramConstraintWidget.mLeft.mTarget != null && paramConstraintWidget.mRight.mTarget != null) {
          resolutionAnchor1.setType(2);
          resolutionAnchor3.setType(2);
          if (paramInt != 0) {
            paramConstraintWidget.getResolutionWidth().addDependent(resolutionAnchor1);
            paramConstraintWidget.getResolutionWidth().addDependent(resolutionAnchor3);
            resolutionAnchor1.setOpposite(resolutionAnchor3, -1, paramConstraintWidget.getResolutionWidth());
            resolutionAnchor3.setOpposite(resolutionAnchor1, 1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor1.setOpposite(resolutionAnchor3, -paramConstraintWidget.getWidth());
            resolutionAnchor3.setOpposite(resolutionAnchor1, paramConstraintWidget.getWidth());
          } 
        } 
      } else if (i) {
        i = paramConstraintWidget.getWidth();
        resolutionAnchor1.setType(1);
        resolutionAnchor3.setType(1);
        if (paramConstraintWidget.mLeft.mTarget == null && paramConstraintWidget.mRight.mTarget == null) {
          if (paramInt != 0) {
            resolutionAnchor3.dependsOn(resolutionAnchor1, 1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor3.dependsOn(resolutionAnchor1, i);
          } 
        } else if (paramConstraintWidget.mLeft.mTarget != null && paramConstraintWidget.mRight.mTarget == null) {
          if (paramInt != 0) {
            resolutionAnchor3.dependsOn(resolutionAnchor1, 1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor3.dependsOn(resolutionAnchor1, i);
          } 
        } else if (paramConstraintWidget.mLeft.mTarget == null && paramConstraintWidget.mRight.mTarget != null) {
          if (paramInt != 0) {
            resolutionAnchor1.dependsOn(resolutionAnchor3, -1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor1.dependsOn(resolutionAnchor3, -i);
          } 
        } else if (paramConstraintWidget.mLeft.mTarget != null && paramConstraintWidget.mRight.mTarget != null) {
          if (paramInt != 0) {
            paramConstraintWidget.getResolutionWidth().addDependent(resolutionAnchor1);
            paramConstraintWidget.getResolutionWidth().addDependent(resolutionAnchor3);
          } 
          if (paramConstraintWidget.mDimensionRatio == 0.0F) {
            resolutionAnchor1.setType(3);
            resolutionAnchor3.setType(3);
            resolutionAnchor1.setOpposite(resolutionAnchor3, 0.0F);
            resolutionAnchor3.setOpposite(resolutionAnchor1, 0.0F);
          } else {
            resolutionAnchor1.setType(2);
            resolutionAnchor3.setType(2);
            resolutionAnchor1.setOpposite(resolutionAnchor3, -i);
            resolutionAnchor3.setOpposite(resolutionAnchor1, i);
            paramConstraintWidget.setWidth(i);
          } 
        } 
      }  
    if (paramConstraintWidget.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && optimizableMatchConstraint(paramConstraintWidget, 1)) {
      i = 1;
    } else {
      i = 0;
    } 
    if (resolutionAnchor2.type != 4 && resolutionAnchor4.type != 4) {
      if (paramConstraintWidget.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.FIXED || (i != 0 && paramConstraintWidget.getVisibility() == 8)) {
        if (paramConstraintWidget.mTop.mTarget == null && paramConstraintWidget.mBottom.mTarget == null) {
          resolutionAnchor2.setType(1);
          resolutionAnchor4.setType(1);
          if (paramInt != 0) {
            resolutionAnchor4.dependsOn(resolutionAnchor2, 1, paramConstraintWidget.getResolutionHeight());
          } else {
            resolutionAnchor4.dependsOn(resolutionAnchor2, paramConstraintWidget.getHeight());
          } 
          if (paramConstraintWidget.mBaseline.mTarget != null) {
            paramConstraintWidget.mBaseline.getResolutionNode().setType(1);
            resolutionAnchor2.dependsOn(1, paramConstraintWidget.mBaseline.getResolutionNode(), -paramConstraintWidget.mBaselineDistance);
          } 
        } else if (paramConstraintWidget.mTop.mTarget != null && paramConstraintWidget.mBottom.mTarget == null) {
          resolutionAnchor2.setType(1);
          resolutionAnchor4.setType(1);
          if (paramInt != 0) {
            resolutionAnchor4.dependsOn(resolutionAnchor2, 1, paramConstraintWidget.getResolutionHeight());
          } else {
            resolutionAnchor4.dependsOn(resolutionAnchor2, paramConstraintWidget.getHeight());
          } 
          if (paramConstraintWidget.mBaselineDistance > 0)
            paramConstraintWidget.mBaseline.getResolutionNode().dependsOn(1, resolutionAnchor2, paramConstraintWidget.mBaselineDistance); 
        } else if (paramConstraintWidget.mTop.mTarget == null && paramConstraintWidget.mBottom.mTarget != null) {
          resolutionAnchor2.setType(1);
          resolutionAnchor4.setType(1);
          if (paramInt != 0) {
            resolutionAnchor2.dependsOn(resolutionAnchor4, -1, paramConstraintWidget.getResolutionHeight());
          } else {
            resolutionAnchor2.dependsOn(resolutionAnchor4, -paramConstraintWidget.getHeight());
          } 
          if (paramConstraintWidget.mBaselineDistance > 0)
            paramConstraintWidget.mBaseline.getResolutionNode().dependsOn(1, resolutionAnchor2, paramConstraintWidget.mBaselineDistance); 
        } else if (paramConstraintWidget.mTop.mTarget != null && paramConstraintWidget.mBottom.mTarget != null) {
          resolutionAnchor2.setType(2);
          resolutionAnchor4.setType(2);
          if (paramInt != 0) {
            resolutionAnchor2.setOpposite(resolutionAnchor4, -1, paramConstraintWidget.getResolutionHeight());
            resolutionAnchor4.setOpposite(resolutionAnchor2, 1, paramConstraintWidget.getResolutionHeight());
            paramConstraintWidget.getResolutionHeight().addDependent(resolutionAnchor2);
            paramConstraintWidget.getResolutionWidth().addDependent(resolutionAnchor4);
          } else {
            resolutionAnchor2.setOpposite(resolutionAnchor4, -paramConstraintWidget.getHeight());
            resolutionAnchor4.setOpposite(resolutionAnchor2, paramConstraintWidget.getHeight());
          } 
          if (paramConstraintWidget.mBaselineDistance > 0)
            paramConstraintWidget.mBaseline.getResolutionNode().dependsOn(1, resolutionAnchor2, paramConstraintWidget.mBaselineDistance); 
        } 
        return;
      } 
      if (i != 0) {
        i = paramConstraintWidget.getHeight();
        resolutionAnchor2.setType(1);
        resolutionAnchor4.setType(1);
        if (paramConstraintWidget.mTop.mTarget == null && paramConstraintWidget.mBottom.mTarget == null) {
          if (paramInt != 0) {
            resolutionAnchor4.dependsOn(resolutionAnchor2, 1, paramConstraintWidget.getResolutionHeight());
          } else {
            resolutionAnchor4.dependsOn(resolutionAnchor2, i);
          } 
        } else if (paramConstraintWidget.mTop.mTarget != null && paramConstraintWidget.mBottom.mTarget == null) {
          if (paramInt != 0) {
            resolutionAnchor4.dependsOn(resolutionAnchor2, 1, paramConstraintWidget.getResolutionHeight());
          } else {
            resolutionAnchor4.dependsOn(resolutionAnchor2, i);
          } 
        } else if (paramConstraintWidget.mTop.mTarget == null && paramConstraintWidget.mBottom.mTarget != null) {
          if (paramInt != 0) {
            resolutionAnchor2.dependsOn(resolutionAnchor4, -1, paramConstraintWidget.getResolutionHeight());
          } else {
            resolutionAnchor2.dependsOn(resolutionAnchor4, -i);
          } 
        } else if (paramConstraintWidget.mTop.mTarget != null && paramConstraintWidget.mBottom.mTarget != null) {
          if (paramInt != 0) {
            paramConstraintWidget.getResolutionHeight().addDependent(resolutionAnchor2);
            paramConstraintWidget.getResolutionWidth().addDependent(resolutionAnchor4);
          } 
          if (paramConstraintWidget.mDimensionRatio == 0.0F) {
            resolutionAnchor2.setType(3);
            resolutionAnchor4.setType(3);
            resolutionAnchor2.setOpposite(resolutionAnchor4, 0.0F);
            resolutionAnchor4.setOpposite(resolutionAnchor2, 0.0F);
          } else {
            resolutionAnchor2.setType(2);
            resolutionAnchor4.setType(2);
            resolutionAnchor2.setOpposite(resolutionAnchor4, -i);
            resolutionAnchor4.setOpposite(resolutionAnchor2, i);
            paramConstraintWidget.setHeight(i);
            if (paramConstraintWidget.mBaselineDistance > 0)
              paramConstraintWidget.mBaseline.getResolutionNode().dependsOn(1, resolutionAnchor2, paramConstraintWidget.mBaselineDistance); 
          } 
        } 
      } 
    } 
  }
  
  static boolean applyChainOptimized(ConstraintWidgetContainer paramConstraintWidgetContainer, LinearSystem paramLinearSystem, int paramInt1, int paramInt2, ChainHead paramChainHead) {
    // Byte code:
    //   0: aload #4
    //   2: getfield mFirst : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   5: astore #5
    //   7: aload #4
    //   9: getfield mLast : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   12: astore #6
    //   14: aload #4
    //   16: getfield mFirstVisibleWidget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   19: astore #7
    //   21: aload #4
    //   23: getfield mLastVisibleWidget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   26: astore #8
    //   28: aload #4
    //   30: getfield mHead : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   33: astore #9
    //   35: aload #4
    //   37: getfield mTotalWeight : F
    //   40: fstore #10
    //   42: aload #4
    //   44: getfield mFirstMatchConstraintWidget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   47: astore #11
    //   49: aload #4
    //   51: getfield mLastMatchConstraintWidget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   54: astore #4
    //   56: aload_0
    //   57: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   60: iload_2
    //   61: aaload
    //   62: astore_0
    //   63: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   66: astore_0
    //   67: iload_2
    //   68: ifne -> 151
    //   71: aload #9
    //   73: getfield mHorizontalChainStyle : I
    //   76: ifne -> 85
    //   79: iconst_1
    //   80: istore #12
    //   82: goto -> 88
    //   85: iconst_0
    //   86: istore #12
    //   88: aload #9
    //   90: getfield mHorizontalChainStyle : I
    //   93: iconst_1
    //   94: if_icmpne -> 103
    //   97: iconst_1
    //   98: istore #13
    //   100: goto -> 106
    //   103: iconst_0
    //   104: istore #13
    //   106: iload #12
    //   108: istore #14
    //   110: iload #13
    //   112: istore #15
    //   114: aload #9
    //   116: getfield mHorizontalChainStyle : I
    //   119: iconst_2
    //   120: if_icmpne -> 141
    //   123: iload #13
    //   125: istore #15
    //   127: iload #12
    //   129: istore #14
    //   131: iconst_1
    //   132: istore #12
    //   134: iload #14
    //   136: istore #13
    //   138: goto -> 214
    //   141: iconst_0
    //   142: istore #12
    //   144: iload #14
    //   146: istore #13
    //   148: goto -> 214
    //   151: aload #9
    //   153: getfield mVerticalChainStyle : I
    //   156: ifne -> 165
    //   159: iconst_1
    //   160: istore #12
    //   162: goto -> 168
    //   165: iconst_0
    //   166: istore #12
    //   168: aload #9
    //   170: getfield mVerticalChainStyle : I
    //   173: iconst_1
    //   174: if_icmpne -> 183
    //   177: iconst_1
    //   178: istore #13
    //   180: goto -> 186
    //   183: iconst_0
    //   184: istore #13
    //   186: iload #12
    //   188: istore #14
    //   190: iload #13
    //   192: istore #15
    //   194: aload #9
    //   196: getfield mVerticalChainStyle : I
    //   199: iconst_2
    //   200: if_icmpne -> 141
    //   203: iload #12
    //   205: istore #14
    //   207: iload #13
    //   209: istore #15
    //   211: goto -> 131
    //   214: aload #5
    //   216: astore #4
    //   218: iconst_0
    //   219: istore #16
    //   221: iconst_0
    //   222: istore #17
    //   224: iconst_0
    //   225: istore #14
    //   227: fconst_0
    //   228: fstore #18
    //   230: fconst_0
    //   231: fstore #19
    //   233: iload #14
    //   235: ifne -> 608
    //   238: iload #17
    //   240: istore #20
    //   242: fload #18
    //   244: fstore #21
    //   246: fload #19
    //   248: fstore #22
    //   250: aload #4
    //   252: invokevirtual getVisibility : ()I
    //   255: bipush #8
    //   257: if_icmpeq -> 381
    //   260: iload #17
    //   262: iconst_1
    //   263: iadd
    //   264: istore #20
    //   266: iload_2
    //   267: ifne -> 284
    //   270: fload #18
    //   272: aload #4
    //   274: invokevirtual getWidth : ()I
    //   277: i2f
    //   278: fadd
    //   279: fstore #21
    //   281: goto -> 295
    //   284: fload #18
    //   286: aload #4
    //   288: invokevirtual getHeight : ()I
    //   291: i2f
    //   292: fadd
    //   293: fstore #21
    //   295: fload #21
    //   297: fstore #22
    //   299: aload #4
    //   301: aload #7
    //   303: if_acmpeq -> 322
    //   306: fload #21
    //   308: aload #4
    //   310: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   313: iload_3
    //   314: aaload
    //   315: invokevirtual getMargin : ()I
    //   318: i2f
    //   319: fadd
    //   320: fstore #22
    //   322: fload #22
    //   324: fstore #21
    //   326: aload #4
    //   328: aload #8
    //   330: if_acmpeq -> 351
    //   333: fload #22
    //   335: aload #4
    //   337: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   340: iload_3
    //   341: iconst_1
    //   342: iadd
    //   343: aaload
    //   344: invokevirtual getMargin : ()I
    //   347: i2f
    //   348: fadd
    //   349: fstore #21
    //   351: fload #19
    //   353: aload #4
    //   355: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   358: iload_3
    //   359: aaload
    //   360: invokevirtual getMargin : ()I
    //   363: i2f
    //   364: fadd
    //   365: aload #4
    //   367: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   370: iload_3
    //   371: iconst_1
    //   372: iadd
    //   373: aaload
    //   374: invokevirtual getMargin : ()I
    //   377: i2f
    //   378: fadd
    //   379: fstore #22
    //   381: aload #4
    //   383: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   386: iload_3
    //   387: aaload
    //   388: astore_0
    //   389: iload #16
    //   391: istore #17
    //   393: aload #4
    //   395: invokevirtual getVisibility : ()I
    //   398: bipush #8
    //   400: if_icmpeq -> 501
    //   403: iload #16
    //   405: istore #17
    //   407: aload #4
    //   409: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   412: iload_2
    //   413: aaload
    //   414: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   417: if_acmpne -> 501
    //   420: iload #16
    //   422: iconst_1
    //   423: iadd
    //   424: istore #17
    //   426: iload_2
    //   427: ifne -> 458
    //   430: aload #4
    //   432: getfield mMatchConstraintDefaultWidth : I
    //   435: ifeq -> 440
    //   438: iconst_0
    //   439: ireturn
    //   440: aload #4
    //   442: getfield mMatchConstraintMinWidth : I
    //   445: ifne -> 456
    //   448: aload #4
    //   450: getfield mMatchConstraintMaxWidth : I
    //   453: ifeq -> 487
    //   456: iconst_0
    //   457: ireturn
    //   458: aload #4
    //   460: getfield mMatchConstraintDefaultHeight : I
    //   463: ifeq -> 468
    //   466: iconst_0
    //   467: ireturn
    //   468: aload #4
    //   470: getfield mMatchConstraintMinHeight : I
    //   473: ifne -> 499
    //   476: aload #4
    //   478: getfield mMatchConstraintMaxHeight : I
    //   481: ifeq -> 487
    //   484: goto -> 499
    //   487: aload #4
    //   489: getfield mDimensionRatio : F
    //   492: fconst_0
    //   493: fcmpl
    //   494: ifeq -> 501
    //   497: iconst_0
    //   498: ireturn
    //   499: iconst_0
    //   500: ireturn
    //   501: aload #4
    //   503: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   506: iload_3
    //   507: iconst_1
    //   508: iadd
    //   509: aaload
    //   510: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   513: astore_0
    //   514: aload_0
    //   515: ifnull -> 558
    //   518: aload_0
    //   519: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   522: astore_0
    //   523: aload_0
    //   524: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   527: iload_3
    //   528: aaload
    //   529: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   532: ifnull -> 558
    //   535: aload_0
    //   536: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   539: iload_3
    //   540: aaload
    //   541: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   544: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   547: aload #4
    //   549: if_acmpeq -> 555
    //   552: goto -> 558
    //   555: goto -> 560
    //   558: aconst_null
    //   559: astore_0
    //   560: aload_0
    //   561: ifnull -> 586
    //   564: iload #17
    //   566: istore #16
    //   568: iload #20
    //   570: istore #17
    //   572: aload_0
    //   573: astore #4
    //   575: fload #21
    //   577: fstore #18
    //   579: fload #22
    //   581: fstore #19
    //   583: goto -> 233
    //   586: iconst_1
    //   587: istore #14
    //   589: iload #17
    //   591: istore #16
    //   593: iload #20
    //   595: istore #17
    //   597: fload #21
    //   599: fstore #18
    //   601: fload #22
    //   603: fstore #19
    //   605: goto -> 233
    //   608: aload #5
    //   610: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   613: iload_3
    //   614: aaload
    //   615: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   618: astore #9
    //   620: aload #6
    //   622: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   625: astore_0
    //   626: iload_3
    //   627: iconst_1
    //   628: iadd
    //   629: istore #14
    //   631: aload_0
    //   632: iload #14
    //   634: aaload
    //   635: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   638: astore_0
    //   639: aload #9
    //   641: getfield target : Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   644: ifnull -> 1858
    //   647: aload_0
    //   648: getfield target : Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   651: ifnonnull -> 657
    //   654: goto -> 1858
    //   657: aload #9
    //   659: getfield target : Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   662: getfield state : I
    //   665: iconst_1
    //   666: if_icmpne -> 1856
    //   669: aload_0
    //   670: getfield target : Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   673: getfield state : I
    //   676: iconst_1
    //   677: if_icmpeq -> 683
    //   680: goto -> 1856
    //   683: iload #16
    //   685: ifle -> 697
    //   688: iload #16
    //   690: iload #17
    //   692: if_icmpeq -> 697
    //   695: iconst_0
    //   696: ireturn
    //   697: iload #12
    //   699: ifne -> 721
    //   702: iload #13
    //   704: ifne -> 721
    //   707: iload #15
    //   709: ifeq -> 715
    //   712: goto -> 721
    //   715: fconst_0
    //   716: fstore #21
    //   718: goto -> 771
    //   721: aload #7
    //   723: ifnull -> 742
    //   726: aload #7
    //   728: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   731: iload_3
    //   732: aaload
    //   733: invokevirtual getMargin : ()I
    //   736: i2f
    //   737: fstore #22
    //   739: goto -> 745
    //   742: fconst_0
    //   743: fstore #22
    //   745: fload #22
    //   747: fstore #21
    //   749: aload #8
    //   751: ifnull -> 771
    //   754: fload #22
    //   756: aload #8
    //   758: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   761: iload #14
    //   763: aaload
    //   764: invokevirtual getMargin : ()I
    //   767: i2f
    //   768: fadd
    //   769: fstore #21
    //   771: aload #9
    //   773: getfield target : Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   776: getfield resolvedOffset : F
    //   779: fstore #23
    //   781: aload_0
    //   782: getfield target : Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   785: getfield resolvedOffset : F
    //   788: fstore #22
    //   790: fload #23
    //   792: fload #22
    //   794: fcmpg
    //   795: ifge -> 811
    //   798: fload #22
    //   800: fload #23
    //   802: fsub
    //   803: fload #18
    //   805: fsub
    //   806: fstore #22
    //   808: goto -> 821
    //   811: fload #23
    //   813: fload #22
    //   815: fsub
    //   816: fload #18
    //   818: fsub
    //   819: fstore #22
    //   821: iload #16
    //   823: ifle -> 1137
    //   826: iload #16
    //   828: iload #17
    //   830: if_icmpne -> 1137
    //   833: aload #4
    //   835: invokevirtual getParent : ()Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   838: ifnull -> 859
    //   841: aload #4
    //   843: invokevirtual getParent : ()Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   846: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   849: iload_2
    //   850: aaload
    //   851: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   854: if_acmpne -> 859
    //   857: iconst_0
    //   858: ireturn
    //   859: fload #22
    //   861: fload #18
    //   863: fadd
    //   864: fload #19
    //   866: fsub
    //   867: fstore #22
    //   869: aload #5
    //   871: astore_0
    //   872: aload_0
    //   873: ifnull -> 1135
    //   876: getstatic android/support/constraint/solver/LinearSystem.sMetrics : Landroid/support/constraint/solver/Metrics;
    //   879: ifnull -> 933
    //   882: getstatic android/support/constraint/solver/LinearSystem.sMetrics : Landroid/support/constraint/solver/Metrics;
    //   885: astore #4
    //   887: aload #4
    //   889: aload #4
    //   891: getfield nonresolvedWidgets : J
    //   894: lconst_1
    //   895: lsub
    //   896: putfield nonresolvedWidgets : J
    //   899: getstatic android/support/constraint/solver/LinearSystem.sMetrics : Landroid/support/constraint/solver/Metrics;
    //   902: astore #4
    //   904: aload #4
    //   906: aload #4
    //   908: getfield resolvedWidgets : J
    //   911: lconst_1
    //   912: ladd
    //   913: putfield resolvedWidgets : J
    //   916: getstatic android/support/constraint/solver/LinearSystem.sMetrics : Landroid/support/constraint/solver/Metrics;
    //   919: astore #4
    //   921: aload #4
    //   923: aload #4
    //   925: getfield chainConnectionResolved : J
    //   928: lconst_1
    //   929: ladd
    //   930: putfield chainConnectionResolved : J
    //   933: aload_0
    //   934: getfield mNextChainWidget : [Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   937: iload_2
    //   938: aaload
    //   939: astore #4
    //   941: aload #4
    //   943: ifnonnull -> 958
    //   946: aload_0
    //   947: aload #6
    //   949: if_acmpne -> 955
    //   952: goto -> 958
    //   955: goto -> 1129
    //   958: fload #22
    //   960: iload #16
    //   962: i2f
    //   963: fdiv
    //   964: fstore #21
    //   966: fload #10
    //   968: fconst_0
    //   969: fcmpl
    //   970: ifle -> 1005
    //   973: aload_0
    //   974: getfield mWeight : [F
    //   977: iload_2
    //   978: faload
    //   979: ldc -1.0
    //   981: fcmpl
    //   982: ifne -> 991
    //   985: fconst_0
    //   986: fstore #21
    //   988: goto -> 1005
    //   991: aload_0
    //   992: getfield mWeight : [F
    //   995: iload_2
    //   996: faload
    //   997: fload #22
    //   999: fmul
    //   1000: fload #10
    //   1002: fdiv
    //   1003: fstore #21
    //   1005: aload_0
    //   1006: invokevirtual getVisibility : ()I
    //   1009: bipush #8
    //   1011: if_icmpne -> 1017
    //   1014: fconst_0
    //   1015: fstore #21
    //   1017: fload #23
    //   1019: aload_0
    //   1020: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1023: iload_3
    //   1024: aaload
    //   1025: invokevirtual getMargin : ()I
    //   1028: i2f
    //   1029: fadd
    //   1030: fstore #19
    //   1032: aload_0
    //   1033: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1036: iload_3
    //   1037: aaload
    //   1038: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1041: aload #9
    //   1043: getfield resolvedTarget : Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1046: fload #19
    //   1048: invokevirtual resolve : (Landroid/support/constraint/solver/widgets/ResolutionAnchor;F)V
    //   1051: aload_0
    //   1052: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1055: iload #14
    //   1057: aaload
    //   1058: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1061: astore #5
    //   1063: aload #9
    //   1065: getfield resolvedTarget : Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1068: astore #7
    //   1070: fload #19
    //   1072: fload #21
    //   1074: fadd
    //   1075: fstore #21
    //   1077: aload #5
    //   1079: aload #7
    //   1081: fload #21
    //   1083: invokevirtual resolve : (Landroid/support/constraint/solver/widgets/ResolutionAnchor;F)V
    //   1086: aload_0
    //   1087: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1090: iload_3
    //   1091: aaload
    //   1092: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1095: aload_1
    //   1096: invokevirtual addResolvedValue : (Landroid/support/constraint/solver/LinearSystem;)V
    //   1099: aload_0
    //   1100: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1103: iload #14
    //   1105: aaload
    //   1106: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1109: aload_1
    //   1110: invokevirtual addResolvedValue : (Landroid/support/constraint/solver/LinearSystem;)V
    //   1113: fload #21
    //   1115: aload_0
    //   1116: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1119: iload #14
    //   1121: aaload
    //   1122: invokevirtual getMargin : ()I
    //   1125: i2f
    //   1126: fadd
    //   1127: fstore #23
    //   1129: aload #4
    //   1131: astore_0
    //   1132: goto -> 872
    //   1135: iconst_1
    //   1136: ireturn
    //   1137: fload #22
    //   1139: fconst_0
    //   1140: fcmpg
    //   1141: ifge -> 1153
    //   1144: iconst_1
    //   1145: istore #12
    //   1147: iconst_0
    //   1148: istore #13
    //   1150: iconst_0
    //   1151: istore #15
    //   1153: iload #12
    //   1155: ifeq -> 1407
    //   1158: aload #5
    //   1160: astore_0
    //   1161: fload #23
    //   1163: fload #22
    //   1165: fload #21
    //   1167: fsub
    //   1168: aload_0
    //   1169: iload_2
    //   1170: invokevirtual getBiasPercent : (I)F
    //   1173: fmul
    //   1174: fadd
    //   1175: fstore #21
    //   1177: aload_0
    //   1178: ifnull -> 1404
    //   1181: getstatic android/support/constraint/solver/LinearSystem.sMetrics : Landroid/support/constraint/solver/Metrics;
    //   1184: ifnull -> 1238
    //   1187: getstatic android/support/constraint/solver/LinearSystem.sMetrics : Landroid/support/constraint/solver/Metrics;
    //   1190: astore #4
    //   1192: aload #4
    //   1194: aload #4
    //   1196: getfield nonresolvedWidgets : J
    //   1199: lconst_1
    //   1200: lsub
    //   1201: putfield nonresolvedWidgets : J
    //   1204: getstatic android/support/constraint/solver/LinearSystem.sMetrics : Landroid/support/constraint/solver/Metrics;
    //   1207: astore #4
    //   1209: aload #4
    //   1211: aload #4
    //   1213: getfield resolvedWidgets : J
    //   1216: lconst_1
    //   1217: ladd
    //   1218: putfield resolvedWidgets : J
    //   1221: getstatic android/support/constraint/solver/LinearSystem.sMetrics : Landroid/support/constraint/solver/Metrics;
    //   1224: astore #4
    //   1226: aload #4
    //   1228: aload #4
    //   1230: getfield chainConnectionResolved : J
    //   1233: lconst_1
    //   1234: ladd
    //   1235: putfield chainConnectionResolved : J
    //   1238: aload_0
    //   1239: getfield mNextChainWidget : [Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1242: iload_2
    //   1243: aaload
    //   1244: astore #4
    //   1246: aload #4
    //   1248: ifnonnull -> 1261
    //   1251: fload #21
    //   1253: fstore #22
    //   1255: aload_0
    //   1256: aload #6
    //   1258: if_acmpne -> 1394
    //   1261: iload_2
    //   1262: ifne -> 1275
    //   1265: aload_0
    //   1266: invokevirtual getWidth : ()I
    //   1269: i2f
    //   1270: fstore #22
    //   1272: goto -> 1282
    //   1275: aload_0
    //   1276: invokevirtual getHeight : ()I
    //   1279: i2f
    //   1280: fstore #22
    //   1282: fload #21
    //   1284: aload_0
    //   1285: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1288: iload_3
    //   1289: aaload
    //   1290: invokevirtual getMargin : ()I
    //   1293: i2f
    //   1294: fadd
    //   1295: fstore #21
    //   1297: aload_0
    //   1298: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1301: iload_3
    //   1302: aaload
    //   1303: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1306: aload #9
    //   1308: getfield resolvedTarget : Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1311: fload #21
    //   1313: invokevirtual resolve : (Landroid/support/constraint/solver/widgets/ResolutionAnchor;F)V
    //   1316: aload_0
    //   1317: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1320: iload #14
    //   1322: aaload
    //   1323: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1326: astore #7
    //   1328: aload #9
    //   1330: getfield resolvedTarget : Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1333: astore #5
    //   1335: fload #21
    //   1337: fload #22
    //   1339: fadd
    //   1340: fstore #21
    //   1342: aload #7
    //   1344: aload #5
    //   1346: fload #21
    //   1348: invokevirtual resolve : (Landroid/support/constraint/solver/widgets/ResolutionAnchor;F)V
    //   1351: aload_0
    //   1352: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1355: iload_3
    //   1356: aaload
    //   1357: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1360: aload_1
    //   1361: invokevirtual addResolvedValue : (Landroid/support/constraint/solver/LinearSystem;)V
    //   1364: aload_0
    //   1365: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1368: iload #14
    //   1370: aaload
    //   1371: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1374: aload_1
    //   1375: invokevirtual addResolvedValue : (Landroid/support/constraint/solver/LinearSystem;)V
    //   1378: fload #21
    //   1380: aload_0
    //   1381: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1384: iload #14
    //   1386: aaload
    //   1387: invokevirtual getMargin : ()I
    //   1390: i2f
    //   1391: fadd
    //   1392: fstore #22
    //   1394: aload #4
    //   1396: astore_0
    //   1397: fload #22
    //   1399: fstore #21
    //   1401: goto -> 1177
    //   1404: goto -> 1854
    //   1407: iload #13
    //   1409: ifne -> 1417
    //   1412: iload #15
    //   1414: ifeq -> 1404
    //   1417: iload #13
    //   1419: ifeq -> 1432
    //   1422: fload #22
    //   1424: fload #21
    //   1426: fsub
    //   1427: fstore #19
    //   1429: goto -> 1448
    //   1432: fload #22
    //   1434: fstore #19
    //   1436: iload #15
    //   1438: ifeq -> 1448
    //   1441: fload #22
    //   1443: fload #21
    //   1445: fsub
    //   1446: fstore #19
    //   1448: fload #19
    //   1450: iload #17
    //   1452: iconst_1
    //   1453: iadd
    //   1454: i2f
    //   1455: fdiv
    //   1456: fstore #22
    //   1458: iload #15
    //   1460: ifeq -> 1488
    //   1463: iload #17
    //   1465: iconst_1
    //   1466: if_icmple -> 1482
    //   1469: fload #19
    //   1471: iload #17
    //   1473: iconst_1
    //   1474: isub
    //   1475: i2f
    //   1476: fdiv
    //   1477: fstore #22
    //   1479: goto -> 1488
    //   1482: fload #19
    //   1484: fconst_2
    //   1485: fdiv
    //   1486: fstore #22
    //   1488: aload #5
    //   1490: invokevirtual getVisibility : ()I
    //   1493: bipush #8
    //   1495: if_icmpeq -> 1508
    //   1498: fload #23
    //   1500: fload #22
    //   1502: fadd
    //   1503: fstore #21
    //   1505: goto -> 1512
    //   1508: fload #23
    //   1510: fstore #21
    //   1512: fload #21
    //   1514: fstore #19
    //   1516: iload #15
    //   1518: ifeq -> 1547
    //   1521: fload #21
    //   1523: fstore #19
    //   1525: iload #17
    //   1527: iconst_1
    //   1528: if_icmple -> 1547
    //   1531: aload #7
    //   1533: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1536: iload_3
    //   1537: aaload
    //   1538: invokevirtual getMargin : ()I
    //   1541: i2f
    //   1542: fload #23
    //   1544: fadd
    //   1545: fstore #19
    //   1547: aload #5
    //   1549: astore_0
    //   1550: fload #19
    //   1552: fstore #21
    //   1554: iload #13
    //   1556: ifeq -> 1590
    //   1559: aload #5
    //   1561: astore_0
    //   1562: fload #19
    //   1564: fstore #21
    //   1566: aload #7
    //   1568: ifnull -> 1590
    //   1571: fload #19
    //   1573: aload #7
    //   1575: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1578: iload_3
    //   1579: aaload
    //   1580: invokevirtual getMargin : ()I
    //   1583: i2f
    //   1584: fadd
    //   1585: fstore #21
    //   1587: aload #5
    //   1589: astore_0
    //   1590: aload_0
    //   1591: ifnull -> 1404
    //   1594: getstatic android/support/constraint/solver/LinearSystem.sMetrics : Landroid/support/constraint/solver/Metrics;
    //   1597: ifnull -> 1651
    //   1600: getstatic android/support/constraint/solver/LinearSystem.sMetrics : Landroid/support/constraint/solver/Metrics;
    //   1603: astore #4
    //   1605: aload #4
    //   1607: aload #4
    //   1609: getfield nonresolvedWidgets : J
    //   1612: lconst_1
    //   1613: lsub
    //   1614: putfield nonresolvedWidgets : J
    //   1617: getstatic android/support/constraint/solver/LinearSystem.sMetrics : Landroid/support/constraint/solver/Metrics;
    //   1620: astore #4
    //   1622: aload #4
    //   1624: aload #4
    //   1626: getfield resolvedWidgets : J
    //   1629: lconst_1
    //   1630: ladd
    //   1631: putfield resolvedWidgets : J
    //   1634: getstatic android/support/constraint/solver/LinearSystem.sMetrics : Landroid/support/constraint/solver/Metrics;
    //   1637: astore #4
    //   1639: aload #4
    //   1641: aload #4
    //   1643: getfield chainConnectionResolved : J
    //   1646: lconst_1
    //   1647: ladd
    //   1648: putfield chainConnectionResolved : J
    //   1651: aload_0
    //   1652: getfield mNextChainWidget : [Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1655: iload_2
    //   1656: aaload
    //   1657: astore #4
    //   1659: aload #4
    //   1661: ifnonnull -> 1684
    //   1664: fload #21
    //   1666: fstore #19
    //   1668: aload_0
    //   1669: aload #6
    //   1671: if_acmpne -> 1677
    //   1674: goto -> 1684
    //   1677: fload #19
    //   1679: fstore #21
    //   1681: goto -> 1848
    //   1684: iload_2
    //   1685: ifne -> 1698
    //   1688: aload_0
    //   1689: invokevirtual getWidth : ()I
    //   1692: i2f
    //   1693: fstore #19
    //   1695: goto -> 1705
    //   1698: aload_0
    //   1699: invokevirtual getHeight : ()I
    //   1702: i2f
    //   1703: fstore #19
    //   1705: fload #21
    //   1707: fstore #23
    //   1709: aload_0
    //   1710: aload #7
    //   1712: if_acmpeq -> 1730
    //   1715: fload #21
    //   1717: aload_0
    //   1718: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1721: iload_3
    //   1722: aaload
    //   1723: invokevirtual getMargin : ()I
    //   1726: i2f
    //   1727: fadd
    //   1728: fstore #23
    //   1730: aload_0
    //   1731: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1734: iload_3
    //   1735: aaload
    //   1736: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1739: aload #9
    //   1741: getfield resolvedTarget : Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1744: fload #23
    //   1746: invokevirtual resolve : (Landroid/support/constraint/solver/widgets/ResolutionAnchor;F)V
    //   1749: aload_0
    //   1750: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1753: iload #14
    //   1755: aaload
    //   1756: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1759: aload #9
    //   1761: getfield resolvedTarget : Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1764: fload #23
    //   1766: fload #19
    //   1768: fadd
    //   1769: invokevirtual resolve : (Landroid/support/constraint/solver/widgets/ResolutionAnchor;F)V
    //   1772: aload_0
    //   1773: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1776: iload_3
    //   1777: aaload
    //   1778: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1781: aload_1
    //   1782: invokevirtual addResolvedValue : (Landroid/support/constraint/solver/LinearSystem;)V
    //   1785: aload_0
    //   1786: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1789: iload #14
    //   1791: aaload
    //   1792: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1795: aload_1
    //   1796: invokevirtual addResolvedValue : (Landroid/support/constraint/solver/LinearSystem;)V
    //   1799: fload #23
    //   1801: fload #19
    //   1803: aload_0
    //   1804: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1807: iload #14
    //   1809: aaload
    //   1810: invokevirtual getMargin : ()I
    //   1813: i2f
    //   1814: fadd
    //   1815: fadd
    //   1816: fstore #23
    //   1818: fload #23
    //   1820: fstore #19
    //   1822: aload #4
    //   1824: ifnull -> 1677
    //   1827: fload #23
    //   1829: fstore #21
    //   1831: aload #4
    //   1833: invokevirtual getVisibility : ()I
    //   1836: bipush #8
    //   1838: if_icmpeq -> 1848
    //   1841: fload #23
    //   1843: fload #22
    //   1845: fadd
    //   1846: fstore #21
    //   1848: aload #4
    //   1850: astore_0
    //   1851: goto -> 1590
    //   1854: iconst_1
    //   1855: ireturn
    //   1856: iconst_0
    //   1857: ireturn
    //   1858: iconst_0
    //   1859: ireturn
  }
  
  static void checkMatchParent(ConstraintWidgetContainer paramConstraintWidgetContainer, LinearSystem paramLinearSystem, ConstraintWidget paramConstraintWidget) {
    if (paramConstraintWidgetContainer.mListDimensionBehaviors[0] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && paramConstraintWidget.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
      int i = paramConstraintWidget.mLeft.mMargin;
      int j = paramConstraintWidgetContainer.getWidth() - paramConstraintWidget.mRight.mMargin;
      paramConstraintWidget.mLeft.mSolverVariable = paramLinearSystem.createObjectVariable(paramConstraintWidget.mLeft);
      paramConstraintWidget.mRight.mSolverVariable = paramLinearSystem.createObjectVariable(paramConstraintWidget.mRight);
      paramLinearSystem.addEquality(paramConstraintWidget.mLeft.mSolverVariable, i);
      paramLinearSystem.addEquality(paramConstraintWidget.mRight.mSolverVariable, j);
      paramConstraintWidget.mHorizontalResolution = 2;
      paramConstraintWidget.setHorizontalDimension(i, j);
    } 
    if (paramConstraintWidgetContainer.mListDimensionBehaviors[1] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && paramConstraintWidget.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
      int i = paramConstraintWidget.mTop.mMargin;
      int j = paramConstraintWidgetContainer.getHeight() - paramConstraintWidget.mBottom.mMargin;
      paramConstraintWidget.mTop.mSolverVariable = paramLinearSystem.createObjectVariable(paramConstraintWidget.mTop);
      paramConstraintWidget.mBottom.mSolverVariable = paramLinearSystem.createObjectVariable(paramConstraintWidget.mBottom);
      paramLinearSystem.addEquality(paramConstraintWidget.mTop.mSolverVariable, i);
      paramLinearSystem.addEquality(paramConstraintWidget.mBottom.mSolverVariable, j);
      if (paramConstraintWidget.mBaselineDistance > 0 || paramConstraintWidget.getVisibility() == 8) {
        paramConstraintWidget.mBaseline.mSolverVariable = paramLinearSystem.createObjectVariable(paramConstraintWidget.mBaseline);
        paramLinearSystem.addEquality(paramConstraintWidget.mBaseline.mSolverVariable, paramConstraintWidget.mBaselineDistance + i);
      } 
      paramConstraintWidget.mVerticalResolution = 2;
      paramConstraintWidget.setVerticalDimension(i, j);
    } 
  }
  
  private static boolean optimizableMatchConstraint(ConstraintWidget paramConstraintWidget, int paramInt) {
    ConstraintWidget.DimensionBehaviour[] arrayOfDimensionBehaviour;
    if (paramConstraintWidget.mListDimensionBehaviors[paramInt] != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT)
      return false; 
    float f = paramConstraintWidget.mDimensionRatio;
    boolean bool = true;
    if (f != 0.0F) {
      arrayOfDimensionBehaviour = paramConstraintWidget.mListDimensionBehaviors;
      if (paramInt == 0) {
        paramInt = bool;
      } else {
        paramInt = 0;
      } 
      return (arrayOfDimensionBehaviour[paramInt] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) ? false : false;
    } 
    if (paramInt == 0) {
      if (((ConstraintWidget)arrayOfDimensionBehaviour).mMatchConstraintDefaultWidth != 0)
        return false; 
      if (((ConstraintWidget)arrayOfDimensionBehaviour).mMatchConstraintMinWidth != 0 || ((ConstraintWidget)arrayOfDimensionBehaviour).mMatchConstraintMaxWidth != 0)
        return false; 
    } else {
      if (((ConstraintWidget)arrayOfDimensionBehaviour).mMatchConstraintDefaultHeight != 0)
        return false; 
      if (((ConstraintWidget)arrayOfDimensionBehaviour).mMatchConstraintMinHeight != 0 || ((ConstraintWidget)arrayOfDimensionBehaviour).mMatchConstraintMaxHeight != 0)
        return false; 
    } 
    return true;
  }
  
  static void setOptimizedWidget(ConstraintWidget paramConstraintWidget, int paramInt1, int paramInt2) {
    int i = paramInt1 * 2;
    int j = i + 1;
    (paramConstraintWidget.mListAnchors[i].getResolutionNode()).resolvedTarget = (paramConstraintWidget.getParent()).mLeft.getResolutionNode();
    (paramConstraintWidget.mListAnchors[i].getResolutionNode()).resolvedOffset = paramInt2;
    (paramConstraintWidget.mListAnchors[i].getResolutionNode()).state = 1;
    (paramConstraintWidget.mListAnchors[j].getResolutionNode()).resolvedTarget = paramConstraintWidget.mListAnchors[i].getResolutionNode();
    (paramConstraintWidget.mListAnchors[j].getResolutionNode()).resolvedOffset = paramConstraintWidget.getLength(paramInt1);
    (paramConstraintWidget.mListAnchors[j].getResolutionNode()).state = 1;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\constraint\solver\widgets\Optimizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */