package android.support.v7.widget;

import java.util.List;

class OpReorderer {
  final Callback mCallback;
  
  OpReorderer(Callback paramCallback) {
    this.mCallback = paramCallback;
  }
  
  private int getLastMoveOutOfOrder(List<AdapterHelper.UpdateOp> paramList) {
    int i = paramList.size() - 1;
    for (boolean bool = false; i >= 0; bool = bool1) {
      boolean bool1;
      if (((AdapterHelper.UpdateOp)paramList.get(i)).cmd == 8) {
        bool1 = bool;
        if (bool)
          return i; 
      } else {
        bool1 = true;
      } 
      i--;
    } 
    return -1;
  }
  
  private void swapMoveAdd(List<AdapterHelper.UpdateOp> paramList, int paramInt1, AdapterHelper.UpdateOp paramUpdateOp1, int paramInt2, AdapterHelper.UpdateOp paramUpdateOp2) {
    byte b;
    if (paramUpdateOp1.itemCount < paramUpdateOp2.positionStart) {
      b = -1;
    } else {
      b = 0;
    } 
    int i = b;
    if (paramUpdateOp1.positionStart < paramUpdateOp2.positionStart)
      i = b + 1; 
    if (paramUpdateOp2.positionStart <= paramUpdateOp1.positionStart)
      paramUpdateOp1.positionStart += paramUpdateOp2.itemCount; 
    if (paramUpdateOp2.positionStart <= paramUpdateOp1.itemCount)
      paramUpdateOp1.itemCount += paramUpdateOp2.itemCount; 
    paramUpdateOp2.positionStart += i;
    paramList.set(paramInt1, paramUpdateOp2);
    paramList.set(paramInt2, paramUpdateOp1);
  }
  
  private void swapMoveOp(List<AdapterHelper.UpdateOp> paramList, int paramInt1, int paramInt2) {
    AdapterHelper.UpdateOp updateOp1 = paramList.get(paramInt1);
    AdapterHelper.UpdateOp updateOp2 = paramList.get(paramInt2);
    int i = updateOp2.cmd;
    if (i != 4) {
      switch (i) {
        default:
          return;
        case 2:
          swapMoveRemove(paramList, paramInt1, updateOp1, paramInt2, updateOp2);
        case 1:
          break;
      } 
      swapMoveAdd(paramList, paramInt1, updateOp1, paramInt2, updateOp2);
    } 
    swapMoveUpdate(paramList, paramInt1, updateOp1, paramInt2, updateOp2);
  }
  
  void reorderOps(List<AdapterHelper.UpdateOp> paramList) {
    while (true) {
      int i = getLastMoveOutOfOrder(paramList);
      if (i != -1) {
        swapMoveOp(paramList, i, i + 1);
        continue;
      } 
      break;
    } 
  }
  
  void swapMoveRemove(List<AdapterHelper.UpdateOp> paramList, int paramInt1, AdapterHelper.UpdateOp paramUpdateOp1, int paramInt2, AdapterHelper.UpdateOp paramUpdateOp2) {
    // Byte code:
    //   0: aload_3
    //   1: getfield positionStart : I
    //   4: istore #6
    //   6: aload_3
    //   7: getfield itemCount : I
    //   10: istore #7
    //   12: iconst_0
    //   13: istore #8
    //   15: iload #6
    //   17: iload #7
    //   19: if_icmpge -> 66
    //   22: aload #5
    //   24: getfield positionStart : I
    //   27: aload_3
    //   28: getfield positionStart : I
    //   31: if_icmpne -> 60
    //   34: aload #5
    //   36: getfield itemCount : I
    //   39: aload_3
    //   40: getfield itemCount : I
    //   43: aload_3
    //   44: getfield positionStart : I
    //   47: isub
    //   48: if_icmpne -> 60
    //   51: iconst_0
    //   52: istore #6
    //   54: iconst_1
    //   55: istore #8
    //   57: goto -> 106
    //   60: iconst_0
    //   61: istore #6
    //   63: goto -> 106
    //   66: aload #5
    //   68: getfield positionStart : I
    //   71: aload_3
    //   72: getfield itemCount : I
    //   75: iconst_1
    //   76: iadd
    //   77: if_icmpne -> 103
    //   80: aload #5
    //   82: getfield itemCount : I
    //   85: aload_3
    //   86: getfield positionStart : I
    //   89: aload_3
    //   90: getfield itemCount : I
    //   93: isub
    //   94: if_icmpne -> 103
    //   97: iconst_1
    //   98: istore #6
    //   100: goto -> 54
    //   103: iconst_1
    //   104: istore #6
    //   106: aload_3
    //   107: getfield itemCount : I
    //   110: aload #5
    //   112: getfield positionStart : I
    //   115: if_icmpge -> 133
    //   118: aload #5
    //   120: aload #5
    //   122: getfield positionStart : I
    //   125: iconst_1
    //   126: isub
    //   127: putfield positionStart : I
    //   130: goto -> 202
    //   133: aload_3
    //   134: getfield itemCount : I
    //   137: aload #5
    //   139: getfield positionStart : I
    //   142: aload #5
    //   144: getfield itemCount : I
    //   147: iadd
    //   148: if_icmpge -> 202
    //   151: aload #5
    //   153: aload #5
    //   155: getfield itemCount : I
    //   158: iconst_1
    //   159: isub
    //   160: putfield itemCount : I
    //   163: aload_3
    //   164: iconst_2
    //   165: putfield cmd : I
    //   168: aload_3
    //   169: iconst_1
    //   170: putfield itemCount : I
    //   173: aload #5
    //   175: getfield itemCount : I
    //   178: ifne -> 201
    //   181: aload_1
    //   182: iload #4
    //   184: invokeinterface remove : (I)Ljava/lang/Object;
    //   189: pop
    //   190: aload_0
    //   191: getfield mCallback : Landroid/support/v7/widget/OpReorderer$Callback;
    //   194: aload #5
    //   196: invokeinterface recycleUpdateOp : (Landroid/support/v7/widget/AdapterHelper$UpdateOp;)V
    //   201: return
    //   202: aload_3
    //   203: getfield positionStart : I
    //   206: istore #9
    //   208: aload #5
    //   210: getfield positionStart : I
    //   213: istore #7
    //   215: aconst_null
    //   216: astore #10
    //   218: iload #9
    //   220: iload #7
    //   222: if_icmpgt -> 240
    //   225: aload #5
    //   227: aload #5
    //   229: getfield positionStart : I
    //   232: iconst_1
    //   233: iadd
    //   234: putfield positionStart : I
    //   237: goto -> 320
    //   240: aload_3
    //   241: getfield positionStart : I
    //   244: aload #5
    //   246: getfield positionStart : I
    //   249: aload #5
    //   251: getfield itemCount : I
    //   254: iadd
    //   255: if_icmpge -> 320
    //   258: aload #5
    //   260: getfield positionStart : I
    //   263: istore #7
    //   265: aload #5
    //   267: getfield itemCount : I
    //   270: istore #11
    //   272: aload_3
    //   273: getfield positionStart : I
    //   276: istore #9
    //   278: aload_0
    //   279: getfield mCallback : Landroid/support/v7/widget/OpReorderer$Callback;
    //   282: iconst_2
    //   283: aload_3
    //   284: getfield positionStart : I
    //   287: iconst_1
    //   288: iadd
    //   289: iload #7
    //   291: iload #11
    //   293: iadd
    //   294: iload #9
    //   296: isub
    //   297: aconst_null
    //   298: invokeinterface obtainUpdateOp : (IIILjava/lang/Object;)Landroid/support/v7/widget/AdapterHelper$UpdateOp;
    //   303: astore #10
    //   305: aload #5
    //   307: aload_3
    //   308: getfield positionStart : I
    //   311: aload #5
    //   313: getfield positionStart : I
    //   316: isub
    //   317: putfield itemCount : I
    //   320: iload #8
    //   322: ifeq -> 355
    //   325: aload_1
    //   326: iload_2
    //   327: aload #5
    //   329: invokeinterface set : (ILjava/lang/Object;)Ljava/lang/Object;
    //   334: pop
    //   335: aload_1
    //   336: iload #4
    //   338: invokeinterface remove : (I)Ljava/lang/Object;
    //   343: pop
    //   344: aload_0
    //   345: getfield mCallback : Landroid/support/v7/widget/OpReorderer$Callback;
    //   348: aload_3
    //   349: invokeinterface recycleUpdateOp : (Landroid/support/v7/widget/AdapterHelper$UpdateOp;)V
    //   354: return
    //   355: iload #6
    //   357: ifeq -> 472
    //   360: aload #10
    //   362: ifnull -> 417
    //   365: aload_3
    //   366: getfield positionStart : I
    //   369: aload #10
    //   371: getfield positionStart : I
    //   374: if_icmple -> 391
    //   377: aload_3
    //   378: aload_3
    //   379: getfield positionStart : I
    //   382: aload #10
    //   384: getfield itemCount : I
    //   387: isub
    //   388: putfield positionStart : I
    //   391: aload_3
    //   392: getfield itemCount : I
    //   395: aload #10
    //   397: getfield positionStart : I
    //   400: if_icmple -> 417
    //   403: aload_3
    //   404: aload_3
    //   405: getfield itemCount : I
    //   408: aload #10
    //   410: getfield itemCount : I
    //   413: isub
    //   414: putfield itemCount : I
    //   417: aload_3
    //   418: getfield positionStart : I
    //   421: aload #5
    //   423: getfield positionStart : I
    //   426: if_icmple -> 443
    //   429: aload_3
    //   430: aload_3
    //   431: getfield positionStart : I
    //   434: aload #5
    //   436: getfield itemCount : I
    //   439: isub
    //   440: putfield positionStart : I
    //   443: aload_3
    //   444: getfield itemCount : I
    //   447: aload #5
    //   449: getfield positionStart : I
    //   452: if_icmple -> 581
    //   455: aload_3
    //   456: aload_3
    //   457: getfield itemCount : I
    //   460: aload #5
    //   462: getfield itemCount : I
    //   465: isub
    //   466: putfield itemCount : I
    //   469: goto -> 581
    //   472: aload #10
    //   474: ifnull -> 529
    //   477: aload_3
    //   478: getfield positionStart : I
    //   481: aload #10
    //   483: getfield positionStart : I
    //   486: if_icmplt -> 503
    //   489: aload_3
    //   490: aload_3
    //   491: getfield positionStart : I
    //   494: aload #10
    //   496: getfield itemCount : I
    //   499: isub
    //   500: putfield positionStart : I
    //   503: aload_3
    //   504: getfield itemCount : I
    //   507: aload #10
    //   509: getfield positionStart : I
    //   512: if_icmplt -> 529
    //   515: aload_3
    //   516: aload_3
    //   517: getfield itemCount : I
    //   520: aload #10
    //   522: getfield itemCount : I
    //   525: isub
    //   526: putfield itemCount : I
    //   529: aload_3
    //   530: getfield positionStart : I
    //   533: aload #5
    //   535: getfield positionStart : I
    //   538: if_icmplt -> 555
    //   541: aload_3
    //   542: aload_3
    //   543: getfield positionStart : I
    //   546: aload #5
    //   548: getfield itemCount : I
    //   551: isub
    //   552: putfield positionStart : I
    //   555: aload_3
    //   556: getfield itemCount : I
    //   559: aload #5
    //   561: getfield positionStart : I
    //   564: if_icmplt -> 581
    //   567: aload_3
    //   568: aload_3
    //   569: getfield itemCount : I
    //   572: aload #5
    //   574: getfield itemCount : I
    //   577: isub
    //   578: putfield itemCount : I
    //   581: aload_1
    //   582: iload_2
    //   583: aload #5
    //   585: invokeinterface set : (ILjava/lang/Object;)Ljava/lang/Object;
    //   590: pop
    //   591: aload_3
    //   592: getfield positionStart : I
    //   595: aload_3
    //   596: getfield itemCount : I
    //   599: if_icmpeq -> 615
    //   602: aload_1
    //   603: iload #4
    //   605: aload_3
    //   606: invokeinterface set : (ILjava/lang/Object;)Ljava/lang/Object;
    //   611: pop
    //   612: goto -> 624
    //   615: aload_1
    //   616: iload #4
    //   618: invokeinterface remove : (I)Ljava/lang/Object;
    //   623: pop
    //   624: aload #10
    //   626: ifnull -> 638
    //   629: aload_1
    //   630: iload_2
    //   631: aload #10
    //   633: invokeinterface add : (ILjava/lang/Object;)V
    //   638: return
  }
  
  void swapMoveUpdate(List<AdapterHelper.UpdateOp> paramList, int paramInt1, AdapterHelper.UpdateOp paramUpdateOp1, int paramInt2, AdapterHelper.UpdateOp paramUpdateOp2) {
    // Byte code:
    //   0: aload_3
    //   1: getfield itemCount : I
    //   4: istore #6
    //   6: aload #5
    //   8: getfield positionStart : I
    //   11: istore #7
    //   13: aconst_null
    //   14: astore #8
    //   16: iload #6
    //   18: iload #7
    //   20: if_icmpge -> 38
    //   23: aload #5
    //   25: aload #5
    //   27: getfield positionStart : I
    //   30: iconst_1
    //   31: isub
    //   32: putfield positionStart : I
    //   35: goto -> 93
    //   38: aload_3
    //   39: getfield itemCount : I
    //   42: aload #5
    //   44: getfield positionStart : I
    //   47: aload #5
    //   49: getfield itemCount : I
    //   52: iadd
    //   53: if_icmpge -> 93
    //   56: aload #5
    //   58: aload #5
    //   60: getfield itemCount : I
    //   63: iconst_1
    //   64: isub
    //   65: putfield itemCount : I
    //   68: aload_0
    //   69: getfield mCallback : Landroid/support/v7/widget/OpReorderer$Callback;
    //   72: iconst_4
    //   73: aload_3
    //   74: getfield positionStart : I
    //   77: iconst_1
    //   78: aload #5
    //   80: getfield payload : Ljava/lang/Object;
    //   83: invokeinterface obtainUpdateOp : (IIILjava/lang/Object;)Landroid/support/v7/widget/AdapterHelper$UpdateOp;
    //   88: astore #9
    //   90: goto -> 96
    //   93: aconst_null
    //   94: astore #9
    //   96: aload_3
    //   97: getfield positionStart : I
    //   100: aload #5
    //   102: getfield positionStart : I
    //   105: if_icmpgt -> 123
    //   108: aload #5
    //   110: aload #5
    //   112: getfield positionStart : I
    //   115: iconst_1
    //   116: iadd
    //   117: putfield positionStart : I
    //   120: goto -> 197
    //   123: aload_3
    //   124: getfield positionStart : I
    //   127: aload #5
    //   129: getfield positionStart : I
    //   132: aload #5
    //   134: getfield itemCount : I
    //   137: iadd
    //   138: if_icmpge -> 197
    //   141: aload #5
    //   143: getfield positionStart : I
    //   146: aload #5
    //   148: getfield itemCount : I
    //   151: iadd
    //   152: aload_3
    //   153: getfield positionStart : I
    //   156: isub
    //   157: istore #7
    //   159: aload_0
    //   160: getfield mCallback : Landroid/support/v7/widget/OpReorderer$Callback;
    //   163: iconst_4
    //   164: aload_3
    //   165: getfield positionStart : I
    //   168: iconst_1
    //   169: iadd
    //   170: iload #7
    //   172: aload #5
    //   174: getfield payload : Ljava/lang/Object;
    //   177: invokeinterface obtainUpdateOp : (IIILjava/lang/Object;)Landroid/support/v7/widget/AdapterHelper$UpdateOp;
    //   182: astore #8
    //   184: aload #5
    //   186: aload #5
    //   188: getfield itemCount : I
    //   191: iload #7
    //   193: isub
    //   194: putfield itemCount : I
    //   197: aload_1
    //   198: iload #4
    //   200: aload_3
    //   201: invokeinterface set : (ILjava/lang/Object;)Ljava/lang/Object;
    //   206: pop
    //   207: aload #5
    //   209: getfield itemCount : I
    //   212: ifle -> 228
    //   215: aload_1
    //   216: iload_2
    //   217: aload #5
    //   219: invokeinterface set : (ILjava/lang/Object;)Ljava/lang/Object;
    //   224: pop
    //   225: goto -> 247
    //   228: aload_1
    //   229: iload_2
    //   230: invokeinterface remove : (I)Ljava/lang/Object;
    //   235: pop
    //   236: aload_0
    //   237: getfield mCallback : Landroid/support/v7/widget/OpReorderer$Callback;
    //   240: aload #5
    //   242: invokeinterface recycleUpdateOp : (Landroid/support/v7/widget/AdapterHelper$UpdateOp;)V
    //   247: aload #9
    //   249: ifnull -> 261
    //   252: aload_1
    //   253: iload_2
    //   254: aload #9
    //   256: invokeinterface add : (ILjava/lang/Object;)V
    //   261: aload #8
    //   263: ifnull -> 275
    //   266: aload_1
    //   267: iload_2
    //   268: aload #8
    //   270: invokeinterface add : (ILjava/lang/Object;)V
    //   275: return
  }
  
  static interface Callback {
    AdapterHelper.UpdateOp obtainUpdateOp(int param1Int1, int param1Int2, int param1Int3, Object param1Object);
    
    void recycleUpdateOp(AdapterHelper.UpdateOp param1UpdateOp);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v7\widget\OpReorderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */