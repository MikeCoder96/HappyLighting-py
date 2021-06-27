package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;

class Chain {
  private static final boolean DEBUG = false;
  
  static void applyChainConstraints(ConstraintWidgetContainer paramConstraintWidgetContainer, LinearSystem paramLinearSystem, int paramInt) {
    int i;
    ChainHead[] arrayOfChainHead;
    byte b2;
    byte b1 = 0;
    if (paramInt == 0) {
      i = paramConstraintWidgetContainer.mHorizontalChainsSize;
      arrayOfChainHead = paramConstraintWidgetContainer.mHorizontalChainsArray;
      b2 = 0;
    } else {
      b2 = 2;
      i = paramConstraintWidgetContainer.mVerticalChainsSize;
      arrayOfChainHead = paramConstraintWidgetContainer.mVerticalChainsArray;
    } 
    while (b1 < i) {
      ChainHead chainHead = arrayOfChainHead[b1];
      chainHead.define();
      if (paramConstraintWidgetContainer.optimizeFor(4)) {
        if (!Optimizer.applyChainOptimized(paramConstraintWidgetContainer, paramLinearSystem, paramInt, b2, chainHead))
          applyChainConstraints(paramConstraintWidgetContainer, paramLinearSystem, paramInt, b2, chainHead); 
      } else {
        applyChainConstraints(paramConstraintWidgetContainer, paramLinearSystem, paramInt, b2, chainHead);
      } 
      b1++;
    } 
  }
  
  static void applyChainConstraints(ConstraintWidgetContainer paramConstraintWidgetContainer, LinearSystem paramLinearSystem, int paramInt1, int paramInt2, ChainHead paramChainHead) {
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
    //   54: astore #11
    //   56: aload_0
    //   57: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   60: iload_2
    //   61: aaload
    //   62: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   65: if_acmpne -> 74
    //   68: iconst_1
    //   69: istore #12
    //   71: goto -> 77
    //   74: iconst_0
    //   75: istore #12
    //   77: iload_2
    //   78: ifne -> 164
    //   81: aload #9
    //   83: getfield mHorizontalChainStyle : I
    //   86: ifne -> 95
    //   89: iconst_1
    //   90: istore #13
    //   92: goto -> 98
    //   95: iconst_0
    //   96: istore #13
    //   98: aload #9
    //   100: getfield mHorizontalChainStyle : I
    //   103: iconst_1
    //   104: if_icmpne -> 113
    //   107: iconst_1
    //   108: istore #14
    //   110: goto -> 116
    //   113: iconst_0
    //   114: istore #14
    //   116: iload #14
    //   118: istore #15
    //   120: iload #13
    //   122: istore #16
    //   124: aload #9
    //   126: getfield mHorizontalChainStyle : I
    //   129: iconst_2
    //   130: if_icmpne -> 147
    //   133: iload #14
    //   135: istore #15
    //   137: iconst_1
    //   138: istore #14
    //   140: iload #13
    //   142: istore #16
    //   144: goto -> 150
    //   147: iconst_0
    //   148: istore #14
    //   150: aload #5
    //   152: astore #17
    //   154: iconst_0
    //   155: istore #13
    //   157: iload #14
    //   159: istore #18
    //   161: goto -> 223
    //   164: aload #9
    //   166: getfield mVerticalChainStyle : I
    //   169: ifne -> 178
    //   172: iconst_1
    //   173: istore #13
    //   175: goto -> 181
    //   178: iconst_0
    //   179: istore #13
    //   181: aload #9
    //   183: getfield mVerticalChainStyle : I
    //   186: iconst_1
    //   187: if_icmpne -> 196
    //   190: iconst_1
    //   191: istore #14
    //   193: goto -> 199
    //   196: iconst_0
    //   197: istore #14
    //   199: iload #14
    //   201: istore #15
    //   203: iload #13
    //   205: istore #16
    //   207: aload #9
    //   209: getfield mVerticalChainStyle : I
    //   212: iconst_2
    //   213: if_icmpne -> 147
    //   216: iload #14
    //   218: istore #15
    //   220: goto -> 137
    //   223: aconst_null
    //   224: astore #19
    //   226: iload #13
    //   228: ifne -> 610
    //   231: aload #17
    //   233: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   236: iload_3
    //   237: aaload
    //   238: astore #11
    //   240: iload #12
    //   242: ifne -> 259
    //   245: iload #18
    //   247: ifeq -> 253
    //   250: goto -> 259
    //   253: iconst_4
    //   254: istore #14
    //   256: goto -> 262
    //   259: iconst_1
    //   260: istore #14
    //   262: aload #11
    //   264: invokevirtual getMargin : ()I
    //   267: istore #20
    //   269: iload #20
    //   271: istore #21
    //   273: aload #11
    //   275: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   278: ifnull -> 305
    //   281: iload #20
    //   283: istore #21
    //   285: aload #17
    //   287: aload #5
    //   289: if_acmpeq -> 305
    //   292: iload #20
    //   294: aload #11
    //   296: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   299: invokevirtual getMargin : ()I
    //   302: iadd
    //   303: istore #21
    //   305: iload #18
    //   307: ifeq -> 331
    //   310: aload #17
    //   312: aload #5
    //   314: if_acmpeq -> 331
    //   317: aload #17
    //   319: aload #7
    //   321: if_acmpeq -> 331
    //   324: bipush #6
    //   326: istore #14
    //   328: goto -> 347
    //   331: iload #16
    //   333: ifeq -> 347
    //   336: iload #12
    //   338: ifeq -> 347
    //   341: iconst_4
    //   342: istore #14
    //   344: goto -> 347
    //   347: aload #11
    //   349: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   352: ifnull -> 431
    //   355: aload #17
    //   357: aload #7
    //   359: if_acmpne -> 385
    //   362: aload_1
    //   363: aload #11
    //   365: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   368: aload #11
    //   370: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   373: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   376: iload #21
    //   378: iconst_5
    //   379: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   382: goto -> 406
    //   385: aload_1
    //   386: aload #11
    //   388: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   391: aload #11
    //   393: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   396: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   399: iload #21
    //   401: bipush #6
    //   403: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   406: aload_1
    //   407: aload #11
    //   409: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   412: aload #11
    //   414: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   417: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   420: iload #21
    //   422: iload #14
    //   424: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   427: pop
    //   428: goto -> 431
    //   431: iload #12
    //   433: ifeq -> 516
    //   436: aload #17
    //   438: invokevirtual getVisibility : ()I
    //   441: bipush #8
    //   443: if_icmpeq -> 490
    //   446: aload #17
    //   448: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   451: iload_2
    //   452: aaload
    //   453: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   456: if_acmpne -> 490
    //   459: aload_1
    //   460: aload #17
    //   462: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   465: iload_3
    //   466: iconst_1
    //   467: iadd
    //   468: aaload
    //   469: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   472: aload #17
    //   474: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   477: iload_3
    //   478: aaload
    //   479: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   482: iconst_0
    //   483: iconst_5
    //   484: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   487: goto -> 490
    //   490: aload_1
    //   491: aload #17
    //   493: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   496: iload_3
    //   497: aaload
    //   498: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   501: aload_0
    //   502: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   505: iload_3
    //   506: aaload
    //   507: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   510: iconst_0
    //   511: bipush #6
    //   513: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   516: aload #17
    //   518: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   521: iload_3
    //   522: iconst_1
    //   523: iadd
    //   524: aaload
    //   525: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   528: astore #22
    //   530: aload #19
    //   532: astore #11
    //   534: aload #22
    //   536: ifnull -> 592
    //   539: aload #22
    //   541: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   544: astore #22
    //   546: aload #19
    //   548: astore #11
    //   550: aload #22
    //   552: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   555: iload_3
    //   556: aaload
    //   557: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   560: ifnull -> 592
    //   563: aload #22
    //   565: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   568: iload_3
    //   569: aaload
    //   570: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   573: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   576: aload #17
    //   578: if_acmpeq -> 588
    //   581: aload #19
    //   583: astore #11
    //   585: goto -> 592
    //   588: aload #22
    //   590: astore #11
    //   592: aload #11
    //   594: ifnull -> 604
    //   597: aload #11
    //   599: astore #17
    //   601: goto -> 607
    //   604: iconst_1
    //   605: istore #13
    //   607: goto -> 223
    //   610: aload #8
    //   612: ifnull -> 681
    //   615: aload #6
    //   617: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   620: astore #11
    //   622: iload_3
    //   623: iconst_1
    //   624: iadd
    //   625: istore #13
    //   627: aload #11
    //   629: iload #13
    //   631: aaload
    //   632: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   635: ifnull -> 681
    //   638: aload #8
    //   640: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   643: iload #13
    //   645: aaload
    //   646: astore #11
    //   648: aload_1
    //   649: aload #11
    //   651: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   654: aload #6
    //   656: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   659: iload #13
    //   661: aaload
    //   662: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   665: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   668: aload #11
    //   670: invokevirtual getMargin : ()I
    //   673: ineg
    //   674: iconst_5
    //   675: invokevirtual addLowerThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   678: goto -> 681
    //   681: iload #12
    //   683: ifeq -> 731
    //   686: aload_0
    //   687: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   690: astore_0
    //   691: iload_3
    //   692: iconst_1
    //   693: iadd
    //   694: istore #13
    //   696: aload_1
    //   697: aload_0
    //   698: iload #13
    //   700: aaload
    //   701: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   704: aload #6
    //   706: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   709: iload #13
    //   711: aaload
    //   712: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   715: aload #6
    //   717: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   720: iload #13
    //   722: aaload
    //   723: invokevirtual getMargin : ()I
    //   726: bipush #6
    //   728: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   731: aload #4
    //   733: getfield mWeightedMatchConstraintsWidgets : Ljava/util/ArrayList;
    //   736: astore_0
    //   737: aload_0
    //   738: ifnull -> 1027
    //   741: aload_0
    //   742: invokevirtual size : ()I
    //   745: istore #14
    //   747: iload #14
    //   749: iconst_1
    //   750: if_icmple -> 1027
    //   753: fload #10
    //   755: fstore #23
    //   757: aload #4
    //   759: getfield mHasUndefinedWeights : Z
    //   762: ifeq -> 785
    //   765: fload #10
    //   767: fstore #23
    //   769: aload #4
    //   771: getfield mHasComplexMatchWeights : Z
    //   774: ifne -> 785
    //   777: aload #4
    //   779: getfield mWidgetsMatchCount : I
    //   782: i2f
    //   783: fstore #23
    //   785: aconst_null
    //   786: astore #11
    //   788: iconst_0
    //   789: istore #13
    //   791: fconst_0
    //   792: fstore #24
    //   794: iload #13
    //   796: iload #14
    //   798: if_icmpge -> 1027
    //   801: aload_0
    //   802: iload #13
    //   804: invokevirtual get : (I)Ljava/lang/Object;
    //   807: checkcast android/support/constraint/solver/widgets/ConstraintWidget
    //   810: astore #17
    //   812: aload #17
    //   814: getfield mWeight : [F
    //   817: iload_2
    //   818: faload
    //   819: fstore #10
    //   821: fload #10
    //   823: fconst_0
    //   824: fcmpg
    //   825: ifge -> 874
    //   828: aload #4
    //   830: getfield mHasComplexMatchWeights : Z
    //   833: ifeq -> 868
    //   836: aload_1
    //   837: aload #17
    //   839: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   842: iload_3
    //   843: iconst_1
    //   844: iadd
    //   845: aaload
    //   846: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   849: aload #17
    //   851: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   854: iload_3
    //   855: aaload
    //   856: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   859: iconst_0
    //   860: iconst_4
    //   861: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   864: pop
    //   865: goto -> 911
    //   868: fconst_1
    //   869: fstore #10
    //   871: goto -> 874
    //   874: fload #10
    //   876: fconst_0
    //   877: fcmpl
    //   878: ifne -> 914
    //   881: aload_1
    //   882: aload #17
    //   884: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   887: iload_3
    //   888: iconst_1
    //   889: iadd
    //   890: aaload
    //   891: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   894: aload #17
    //   896: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   899: iload_3
    //   900: aaload
    //   901: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   904: iconst_0
    //   905: bipush #6
    //   907: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   910: pop
    //   911: goto -> 1021
    //   914: aload #11
    //   916: ifnull -> 1013
    //   919: aload #11
    //   921: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   924: iload_3
    //   925: aaload
    //   926: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   929: astore #19
    //   931: aload #11
    //   933: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   936: astore #11
    //   938: iload_3
    //   939: iconst_1
    //   940: iadd
    //   941: istore #12
    //   943: aload #11
    //   945: iload #12
    //   947: aaload
    //   948: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   951: astore #25
    //   953: aload #17
    //   955: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   958: iload_3
    //   959: aaload
    //   960: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   963: astore #22
    //   965: aload #17
    //   967: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   970: iload #12
    //   972: aaload
    //   973: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   976: astore #26
    //   978: aload_1
    //   979: invokevirtual createRow : ()Landroid/support/constraint/solver/ArrayRow;
    //   982: astore #11
    //   984: aload #11
    //   986: fload #24
    //   988: fload #23
    //   990: fload #10
    //   992: aload #19
    //   994: aload #25
    //   996: aload #22
    //   998: aload #26
    //   1000: invokevirtual createRowEqualMatchDimensions : (FFFLandroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;)Landroid/support/constraint/solver/ArrayRow;
    //   1003: pop
    //   1004: aload_1
    //   1005: aload #11
    //   1007: invokevirtual addConstraint : (Landroid/support/constraint/solver/ArrayRow;)V
    //   1010: goto -> 1013
    //   1013: aload #17
    //   1015: astore #11
    //   1017: fload #10
    //   1019: fstore #24
    //   1021: iinc #13, 1
    //   1024: goto -> 794
    //   1027: aload #7
    //   1029: ifnull -> 1236
    //   1032: aload #7
    //   1034: aload #8
    //   1036: if_acmpeq -> 1044
    //   1039: iload #18
    //   1041: ifeq -> 1236
    //   1044: aload #5
    //   1046: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1049: iload_3
    //   1050: aaload
    //   1051: astore #17
    //   1053: aload #6
    //   1055: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1058: astore_0
    //   1059: iload_3
    //   1060: iconst_1
    //   1061: iadd
    //   1062: istore #13
    //   1064: aload_0
    //   1065: iload #13
    //   1067: aaload
    //   1068: astore #11
    //   1070: aload #5
    //   1072: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1075: iload_3
    //   1076: aaload
    //   1077: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1080: ifnull -> 1100
    //   1083: aload #5
    //   1085: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1088: iload_3
    //   1089: aaload
    //   1090: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1093: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1096: astore_0
    //   1097: goto -> 1102
    //   1100: aconst_null
    //   1101: astore_0
    //   1102: aload #6
    //   1104: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1107: iload #13
    //   1109: aaload
    //   1110: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1113: ifnull -> 1135
    //   1116: aload #6
    //   1118: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1121: iload #13
    //   1123: aaload
    //   1124: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1127: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1130: astore #4
    //   1132: goto -> 1138
    //   1135: aconst_null
    //   1136: astore #4
    //   1138: aload #7
    //   1140: aload #8
    //   1142: if_acmpne -> 1164
    //   1145: aload #7
    //   1147: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1150: iload_3
    //   1151: aaload
    //   1152: astore #17
    //   1154: aload #7
    //   1156: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1159: iload #13
    //   1161: aaload
    //   1162: astore #11
    //   1164: aload_0
    //   1165: ifnull -> 2347
    //   1168: aload #4
    //   1170: ifnull -> 2347
    //   1173: iload_2
    //   1174: ifne -> 1187
    //   1177: aload #9
    //   1179: getfield mHorizontalBiasPercent : F
    //   1182: fstore #10
    //   1184: goto -> 1197
    //   1187: aload #9
    //   1189: getfield mVerticalBiasPercent : F
    //   1192: fstore #10
    //   1194: goto -> 1184
    //   1197: aload #17
    //   1199: invokevirtual getMargin : ()I
    //   1202: istore #13
    //   1204: aload #11
    //   1206: invokevirtual getMargin : ()I
    //   1209: istore_2
    //   1210: aload_1
    //   1211: aload #17
    //   1213: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1216: aload_0
    //   1217: iload #13
    //   1219: fload #10
    //   1221: aload #4
    //   1223: aload #11
    //   1225: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1228: iload_2
    //   1229: iconst_5
    //   1230: invokevirtual addCentering : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;IFLandroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   1233: goto -> 2347
    //   1236: iload #16
    //   1238: ifeq -> 1734
    //   1241: aload #7
    //   1243: ifnull -> 1734
    //   1246: aload #4
    //   1248: getfield mWidgetsMatchCount : I
    //   1251: ifle -> 1273
    //   1254: aload #4
    //   1256: getfield mWidgetsCount : I
    //   1259: aload #4
    //   1261: getfield mWidgetsMatchCount : I
    //   1264: if_icmpne -> 1273
    //   1267: iconst_1
    //   1268: istore #12
    //   1270: goto -> 1276
    //   1273: iconst_0
    //   1274: istore #12
    //   1276: aload #7
    //   1278: astore #11
    //   1280: aload #11
    //   1282: astore #9
    //   1284: aload #9
    //   1286: ifnull -> 2347
    //   1289: aload #9
    //   1291: getfield mNextChainWidget : [Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1294: iload_2
    //   1295: aaload
    //   1296: astore_0
    //   1297: aload_0
    //   1298: ifnull -> 1320
    //   1301: aload_0
    //   1302: invokevirtual getVisibility : ()I
    //   1305: bipush #8
    //   1307: if_icmpne -> 1320
    //   1310: aload_0
    //   1311: getfield mNextChainWidget : [Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1314: iload_2
    //   1315: aaload
    //   1316: astore_0
    //   1317: goto -> 1297
    //   1320: aload_0
    //   1321: ifnonnull -> 1337
    //   1324: aload #9
    //   1326: aload #8
    //   1328: if_acmpne -> 1334
    //   1331: goto -> 1337
    //   1334: goto -> 1711
    //   1337: aload #9
    //   1339: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1342: iload_3
    //   1343: aaload
    //   1344: astore #19
    //   1346: aload #19
    //   1348: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1351: astore #25
    //   1353: aload #19
    //   1355: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1358: ifnull -> 1374
    //   1361: aload #19
    //   1363: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1366: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1369: astore #17
    //   1371: goto -> 1377
    //   1374: aconst_null
    //   1375: astore #17
    //   1377: aload #11
    //   1379: aload #9
    //   1381: if_acmpeq -> 1401
    //   1384: aload #11
    //   1386: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1389: iload_3
    //   1390: iconst_1
    //   1391: iadd
    //   1392: aaload
    //   1393: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1396: astore #4
    //   1398: goto -> 1457
    //   1401: aload #17
    //   1403: astore #4
    //   1405: aload #9
    //   1407: aload #7
    //   1409: if_acmpne -> 1457
    //   1412: aload #17
    //   1414: astore #4
    //   1416: aload #11
    //   1418: aload #9
    //   1420: if_acmpne -> 1457
    //   1423: aload #5
    //   1425: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1428: iload_3
    //   1429: aaload
    //   1430: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1433: ifnull -> 1454
    //   1436: aload #5
    //   1438: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1441: iload_3
    //   1442: aaload
    //   1443: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1446: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1449: astore #4
    //   1451: goto -> 1457
    //   1454: aconst_null
    //   1455: astore #4
    //   1457: aload #19
    //   1459: invokevirtual getMargin : ()I
    //   1462: istore #14
    //   1464: aload #9
    //   1466: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1469: astore #17
    //   1471: iload_3
    //   1472: iconst_1
    //   1473: iadd
    //   1474: istore #21
    //   1476: aload #17
    //   1478: iload #21
    //   1480: aaload
    //   1481: invokevirtual getMargin : ()I
    //   1484: istore #18
    //   1486: aload_0
    //   1487: ifnull -> 1521
    //   1490: aload_0
    //   1491: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1494: iload_3
    //   1495: aaload
    //   1496: astore #22
    //   1498: aload #22
    //   1500: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1503: astore #17
    //   1505: aload #9
    //   1507: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1510: iload #21
    //   1512: aaload
    //   1513: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1516: astore #19
    //   1518: goto -> 1568
    //   1521: aload #6
    //   1523: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1526: iload #21
    //   1528: aaload
    //   1529: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1532: astore #22
    //   1534: aload #22
    //   1536: ifnull -> 1549
    //   1539: aload #22
    //   1541: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1544: astore #17
    //   1546: goto -> 1552
    //   1549: aconst_null
    //   1550: astore #17
    //   1552: aload #9
    //   1554: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1557: iload #21
    //   1559: aaload
    //   1560: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1563: astore #19
    //   1565: goto -> 1518
    //   1568: iload #18
    //   1570: istore #13
    //   1572: aload #22
    //   1574: ifnull -> 1587
    //   1577: iload #18
    //   1579: aload #22
    //   1581: invokevirtual getMargin : ()I
    //   1584: iadd
    //   1585: istore #13
    //   1587: aload #11
    //   1589: ifnull -> 1611
    //   1592: iload #14
    //   1594: aload #11
    //   1596: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1599: iload #21
    //   1601: aaload
    //   1602: invokevirtual getMargin : ()I
    //   1605: iadd
    //   1606: istore #14
    //   1608: goto -> 1611
    //   1611: aload #25
    //   1613: ifnull -> 1708
    //   1616: aload #4
    //   1618: ifnull -> 1708
    //   1621: aload #17
    //   1623: ifnull -> 1708
    //   1626: aload #19
    //   1628: ifnull -> 1708
    //   1631: aload #9
    //   1633: aload #7
    //   1635: if_acmpne -> 1650
    //   1638: aload #7
    //   1640: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1643: iload_3
    //   1644: aaload
    //   1645: invokevirtual getMargin : ()I
    //   1648: istore #14
    //   1650: aload #9
    //   1652: aload #8
    //   1654: if_acmpne -> 1670
    //   1657: aload #8
    //   1659: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1662: iload #21
    //   1664: aaload
    //   1665: invokevirtual getMargin : ()I
    //   1668: istore #13
    //   1670: iload #12
    //   1672: ifeq -> 1682
    //   1675: bipush #6
    //   1677: istore #18
    //   1679: goto -> 1685
    //   1682: iconst_4
    //   1683: istore #18
    //   1685: aload_1
    //   1686: aload #25
    //   1688: aload #4
    //   1690: iload #14
    //   1692: ldc 0.5
    //   1694: aload #17
    //   1696: aload #19
    //   1698: iload #13
    //   1700: iload #18
    //   1702: invokevirtual addCentering : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;IFLandroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   1705: goto -> 1711
    //   1708: goto -> 1334
    //   1711: aload #9
    //   1713: invokevirtual getVisibility : ()I
    //   1716: bipush #8
    //   1718: if_icmpeq -> 1728
    //   1721: aload #9
    //   1723: astore #11
    //   1725: goto -> 1728
    //   1728: aload_0
    //   1729: astore #9
    //   1731: goto -> 1284
    //   1734: iload #15
    //   1736: ifeq -> 2347
    //   1739: aload #7
    //   1741: ifnull -> 2347
    //   1744: aload #4
    //   1746: getfield mWidgetsMatchCount : I
    //   1749: ifle -> 1771
    //   1752: aload #4
    //   1754: getfield mWidgetsCount : I
    //   1757: aload #4
    //   1759: getfield mWidgetsMatchCount : I
    //   1762: if_icmpne -> 1771
    //   1765: iconst_1
    //   1766: istore #13
    //   1768: goto -> 1774
    //   1771: iconst_0
    //   1772: istore #13
    //   1774: aload #7
    //   1776: astore #4
    //   1778: aload #4
    //   1780: astore_0
    //   1781: aload_0
    //   1782: astore #9
    //   1784: aload #9
    //   1786: ifnull -> 2156
    //   1789: aload #9
    //   1791: getfield mNextChainWidget : [Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1794: iload_2
    //   1795: aaload
    //   1796: astore_0
    //   1797: aload_0
    //   1798: ifnull -> 1820
    //   1801: aload_0
    //   1802: invokevirtual getVisibility : ()I
    //   1805: bipush #8
    //   1807: if_icmpne -> 1820
    //   1810: aload_0
    //   1811: getfield mNextChainWidget : [Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1814: iload_2
    //   1815: aaload
    //   1816: astore_0
    //   1817: goto -> 1797
    //   1820: aload #9
    //   1822: aload #7
    //   1824: if_acmpeq -> 2136
    //   1827: aload #9
    //   1829: aload #8
    //   1831: if_acmpeq -> 2136
    //   1834: aload_0
    //   1835: ifnull -> 2136
    //   1838: aload_0
    //   1839: aload #8
    //   1841: if_acmpne -> 1849
    //   1844: aconst_null
    //   1845: astore_0
    //   1846: goto -> 1849
    //   1849: aload #9
    //   1851: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1854: iload_3
    //   1855: aaload
    //   1856: astore #11
    //   1858: aload #11
    //   1860: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1863: astore #25
    //   1865: aload #11
    //   1867: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1870: ifnull -> 1883
    //   1873: aload #11
    //   1875: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1878: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1881: astore #17
    //   1883: aload #4
    //   1885: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1888: astore #17
    //   1890: iload_3
    //   1891: iconst_1
    //   1892: iadd
    //   1893: istore #21
    //   1895: aload #17
    //   1897: iload #21
    //   1899: aaload
    //   1900: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1903: astore #26
    //   1905: aload #11
    //   1907: invokevirtual getMargin : ()I
    //   1910: istore #18
    //   1912: aload #9
    //   1914: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1917: iload #21
    //   1919: aaload
    //   1920: invokevirtual getMargin : ()I
    //   1923: istore #12
    //   1925: aload_0
    //   1926: ifnull -> 1979
    //   1929: aload_0
    //   1930: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1933: iload_3
    //   1934: aaload
    //   1935: astore #22
    //   1937: aload #22
    //   1939: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1942: astore #19
    //   1944: aload #22
    //   1946: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1949: ifnull -> 1965
    //   1952: aload #22
    //   1954: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1957: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1960: astore #11
    //   1962: goto -> 1968
    //   1965: aconst_null
    //   1966: astore #11
    //   1968: aload #11
    //   1970: astore #17
    //   1972: aload #22
    //   1974: astore #11
    //   1976: goto -> 2031
    //   1979: aload #9
    //   1981: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1984: iload #21
    //   1986: aaload
    //   1987: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1990: astore #22
    //   1992: aload #22
    //   1994: ifnull -> 2007
    //   1997: aload #22
    //   1999: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2002: astore #11
    //   2004: goto -> 2010
    //   2007: aconst_null
    //   2008: astore #11
    //   2010: aload #9
    //   2012: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2015: iload #21
    //   2017: aaload
    //   2018: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2021: astore #17
    //   2023: aload #11
    //   2025: astore #19
    //   2027: aload #22
    //   2029: astore #11
    //   2031: iload #12
    //   2033: istore #14
    //   2035: aload #11
    //   2037: ifnull -> 2050
    //   2040: iload #12
    //   2042: aload #11
    //   2044: invokevirtual getMargin : ()I
    //   2047: iadd
    //   2048: istore #14
    //   2050: iload #18
    //   2052: istore #12
    //   2054: aload #4
    //   2056: ifnull -> 2075
    //   2059: iload #18
    //   2061: aload #4
    //   2063: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2066: iload #21
    //   2068: aaload
    //   2069: invokevirtual getMargin : ()I
    //   2072: iadd
    //   2073: istore #12
    //   2075: iload #13
    //   2077: ifeq -> 2087
    //   2080: bipush #6
    //   2082: istore #18
    //   2084: goto -> 2090
    //   2087: iconst_4
    //   2088: istore #18
    //   2090: aload #25
    //   2092: ifnull -> 2133
    //   2095: aload #26
    //   2097: ifnull -> 2133
    //   2100: aload #19
    //   2102: ifnull -> 2133
    //   2105: aload #17
    //   2107: ifnull -> 2133
    //   2110: aload_1
    //   2111: aload #25
    //   2113: aload #26
    //   2115: iload #12
    //   2117: ldc 0.5
    //   2119: aload #19
    //   2121: aload #17
    //   2123: iload #14
    //   2125: iload #18
    //   2127: invokevirtual addCentering : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;IFLandroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   2130: goto -> 2133
    //   2133: goto -> 2136
    //   2136: aload #9
    //   2138: invokevirtual getVisibility : ()I
    //   2141: bipush #8
    //   2143: if_icmpeq -> 2153
    //   2146: aload #9
    //   2148: astore #4
    //   2150: goto -> 2153
    //   2153: goto -> 1781
    //   2156: aload #7
    //   2158: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2161: iload_3
    //   2162: aaload
    //   2163: astore_0
    //   2164: aload #5
    //   2166: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2169: iload_3
    //   2170: aaload
    //   2171: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2174: astore #17
    //   2176: aload #8
    //   2178: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2181: astore #4
    //   2183: iload_3
    //   2184: iconst_1
    //   2185: iadd
    //   2186: istore_2
    //   2187: aload #4
    //   2189: iload_2
    //   2190: aaload
    //   2191: astore #11
    //   2193: aload #6
    //   2195: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2198: iload_2
    //   2199: aaload
    //   2200: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2203: astore #4
    //   2205: aload #17
    //   2207: ifnull -> 2308
    //   2210: aload #7
    //   2212: aload #8
    //   2214: if_acmpeq -> 2241
    //   2217: aload_1
    //   2218: aload_0
    //   2219: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2222: aload #17
    //   2224: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2227: aload_0
    //   2228: invokevirtual getMargin : ()I
    //   2231: iconst_5
    //   2232: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   2235: pop
    //   2236: aload_1
    //   2237: astore_0
    //   2238: goto -> 2310
    //   2241: aload #4
    //   2243: ifnull -> 2236
    //   2246: aload_0
    //   2247: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2250: astore #9
    //   2252: aload #17
    //   2254: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2257: astore #17
    //   2259: aload_0
    //   2260: invokevirtual getMargin : ()I
    //   2263: istore_2
    //   2264: aload #11
    //   2266: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2269: astore #19
    //   2271: aload #4
    //   2273: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2276: astore #5
    //   2278: aload #11
    //   2280: invokevirtual getMargin : ()I
    //   2283: istore #13
    //   2285: aload_1
    //   2286: astore_0
    //   2287: aload_1
    //   2288: aload #9
    //   2290: aload #17
    //   2292: iload_2
    //   2293: ldc 0.5
    //   2295: aload #19
    //   2297: aload #5
    //   2299: iload #13
    //   2301: iconst_5
    //   2302: invokevirtual addCentering : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;IFLandroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   2305: goto -> 2310
    //   2308: aload_1
    //   2309: astore_0
    //   2310: aload #4
    //   2312: ifnull -> 2347
    //   2315: aload #7
    //   2317: aload #8
    //   2319: if_acmpeq -> 2347
    //   2322: aload_0
    //   2323: aload #11
    //   2325: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2328: aload #4
    //   2330: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2333: aload #11
    //   2335: invokevirtual getMargin : ()I
    //   2338: ineg
    //   2339: iconst_5
    //   2340: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   2343: pop
    //   2344: goto -> 2347
    //   2347: iload #16
    //   2349: ifne -> 2357
    //   2352: iload #15
    //   2354: ifeq -> 2556
    //   2357: aload #7
    //   2359: ifnull -> 2556
    //   2362: aload #7
    //   2364: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2367: iload_3
    //   2368: aaload
    //   2369: astore #11
    //   2371: aload #8
    //   2373: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2376: astore_0
    //   2377: iload_3
    //   2378: iconst_1
    //   2379: iadd
    //   2380: istore_2
    //   2381: aload_0
    //   2382: iload_2
    //   2383: aaload
    //   2384: astore #9
    //   2386: aload #11
    //   2388: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2391: ifnull -> 2407
    //   2394: aload #11
    //   2396: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2399: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2402: astore #4
    //   2404: goto -> 2410
    //   2407: aconst_null
    //   2408: astore #4
    //   2410: aload #9
    //   2412: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2415: ifnull -> 2430
    //   2418: aload #9
    //   2420: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2423: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2426: astore_0
    //   2427: goto -> 2432
    //   2430: aconst_null
    //   2431: astore_0
    //   2432: aload #6
    //   2434: aload #8
    //   2436: if_acmpeq -> 2467
    //   2439: aload #6
    //   2441: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2444: iload_2
    //   2445: aaload
    //   2446: astore_0
    //   2447: aload_0
    //   2448: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2451: ifnull -> 2465
    //   2454: aload_0
    //   2455: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2458: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2461: astore_0
    //   2462: goto -> 2467
    //   2465: aconst_null
    //   2466: astore_0
    //   2467: aload #7
    //   2469: aload #8
    //   2471: if_acmpne -> 2492
    //   2474: aload #7
    //   2476: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2479: iload_3
    //   2480: aaload
    //   2481: astore #11
    //   2483: aload #7
    //   2485: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2488: iload_2
    //   2489: aaload
    //   2490: astore #9
    //   2492: aload #4
    //   2494: ifnull -> 2556
    //   2497: aload_0
    //   2498: ifnull -> 2556
    //   2501: aload #11
    //   2503: invokevirtual getMargin : ()I
    //   2506: istore_3
    //   2507: aload #8
    //   2509: ifnonnull -> 2519
    //   2512: aload #6
    //   2514: astore #17
    //   2516: goto -> 2523
    //   2519: aload #8
    //   2521: astore #17
    //   2523: aload #17
    //   2525: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2528: iload_2
    //   2529: aaload
    //   2530: invokevirtual getMargin : ()I
    //   2533: istore_2
    //   2534: aload_1
    //   2535: aload #11
    //   2537: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2540: aload #4
    //   2542: iload_3
    //   2543: ldc 0.5
    //   2545: aload_0
    //   2546: aload #9
    //   2548: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2551: iload_2
    //   2552: iconst_5
    //   2553: invokevirtual addCentering : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;IFLandroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   2556: return
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\constraint\solver\widgets\Chain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */