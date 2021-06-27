package android.support.constraint.solver;

import java.io.PrintStream;
import java.util.Arrays;

public class ArrayLinkedVariables {
  private static final boolean DEBUG = false;
  
  private static final boolean FULL_NEW_CHECK = false;
  
  private static final int NONE = -1;
  
  private int ROW_SIZE = 8;
  
  private SolverVariable candidate = null;
  
  int currentSize = 0;
  
  private int[] mArrayIndices = new int[this.ROW_SIZE];
  
  private int[] mArrayNextIndices = new int[this.ROW_SIZE];
  
  private float[] mArrayValues = new float[this.ROW_SIZE];
  
  private final Cache mCache;
  
  private boolean mDidFillOnce = false;
  
  private int mHead = -1;
  
  private int mLast = -1;
  
  private final ArrayRow mRow;
  
  ArrayLinkedVariables(ArrayRow paramArrayRow, Cache paramCache) {
    this.mRow = paramArrayRow;
    this.mCache = paramCache;
  }
  
  private boolean isNew(SolverVariable paramSolverVariable, LinearSystem paramLinearSystem) {
    int i = paramSolverVariable.usageInRowCount;
    boolean bool = true;
    if (i > 1)
      bool = false; 
    return bool;
  }
  
  final void add(SolverVariable paramSolverVariable, float paramFloat, boolean paramBoolean) {
    if (paramFloat == 0.0F)
      return; 
    if (this.mHead == -1) {
      this.mHead = 0;
      this.mArrayValues[this.mHead] = paramFloat;
      this.mArrayIndices[this.mHead] = paramSolverVariable.id;
      this.mArrayNextIndices[this.mHead] = -1;
      paramSolverVariable.usageInRowCount++;
      paramSolverVariable.addToRow(this.mRow);
      this.currentSize++;
      if (!this.mDidFillOnce) {
        this.mLast++;
        if (this.mLast >= this.mArrayIndices.length) {
          this.mDidFillOnce = true;
          this.mLast = this.mArrayIndices.length - 1;
        } 
      } 
      return;
    } 
    int i = this.mHead;
    int j = 0;
    int k = -1;
    while (i != -1 && j < this.currentSize) {
      if (this.mArrayIndices[i] == paramSolverVariable.id) {
        float[] arrayOfFloat = this.mArrayValues;
        arrayOfFloat[i] = arrayOfFloat[i] + paramFloat;
        if (this.mArrayValues[i] == 0.0F) {
          if (i == this.mHead) {
            this.mHead = this.mArrayNextIndices[i];
          } else {
            this.mArrayNextIndices[k] = this.mArrayNextIndices[i];
          } 
          if (paramBoolean)
            paramSolverVariable.removeFromRow(this.mRow); 
          if (this.mDidFillOnce)
            this.mLast = i; 
          paramSolverVariable.usageInRowCount--;
          this.currentSize--;
        } 
        return;
      } 
      if (this.mArrayIndices[i] < paramSolverVariable.id)
        k = i; 
      i = this.mArrayNextIndices[i];
      j++;
    } 
    i = this.mLast + 1;
    if (this.mDidFillOnce)
      if (this.mArrayIndices[this.mLast] == -1) {
        i = this.mLast;
      } else {
        i = this.mArrayIndices.length;
      }  
    j = i;
    if (i >= this.mArrayIndices.length) {
      j = i;
      if (this.currentSize < this.mArrayIndices.length) {
        byte b = 0;
        while (true) {
          j = i;
          if (b < this.mArrayIndices.length) {
            if (this.mArrayIndices[b] == -1) {
              j = b;
              break;
            } 
            b++;
            continue;
          } 
          break;
        } 
      } 
    } 
    i = j;
    if (j >= this.mArrayIndices.length) {
      i = this.mArrayIndices.length;
      this.ROW_SIZE *= 2;
      this.mDidFillOnce = false;
      this.mLast = i - 1;
      this.mArrayValues = Arrays.copyOf(this.mArrayValues, this.ROW_SIZE);
      this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
      this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
    } 
    this.mArrayIndices[i] = paramSolverVariable.id;
    this.mArrayValues[i] = paramFloat;
    if (k != -1) {
      this.mArrayNextIndices[i] = this.mArrayNextIndices[k];
      this.mArrayNextIndices[k] = i;
    } else {
      this.mArrayNextIndices[i] = this.mHead;
      this.mHead = i;
    } 
    paramSolverVariable.usageInRowCount++;
    paramSolverVariable.addToRow(this.mRow);
    this.currentSize++;
    if (!this.mDidFillOnce)
      this.mLast++; 
    if (this.mLast >= this.mArrayIndices.length) {
      this.mDidFillOnce = true;
      this.mLast = this.mArrayIndices.length - 1;
    } 
  }
  
  SolverVariable chooseSubject(LinearSystem paramLinearSystem) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mHead : I
    //   4: istore_2
    //   5: aconst_null
    //   6: astore_3
    //   7: iconst_0
    //   8: istore #4
    //   10: aconst_null
    //   11: astore #5
    //   13: fconst_0
    //   14: fstore #6
    //   16: iconst_0
    //   17: istore #7
    //   19: fconst_0
    //   20: fstore #8
    //   22: iconst_0
    //   23: istore #9
    //   25: iload_2
    //   26: iconst_m1
    //   27: if_icmpeq -> 555
    //   30: iload #4
    //   32: aload_0
    //   33: getfield currentSize : I
    //   36: if_icmpge -> 555
    //   39: aload_0
    //   40: getfield mArrayValues : [F
    //   43: iload_2
    //   44: faload
    //   45: fstore #10
    //   47: aload_0
    //   48: getfield mCache : Landroid/support/constraint/solver/Cache;
    //   51: getfield mIndexedVariables : [Landroid/support/constraint/solver/SolverVariable;
    //   54: aload_0
    //   55: getfield mArrayIndices : [I
    //   58: iload_2
    //   59: iaload
    //   60: aaload
    //   61: astore #11
    //   63: fload #10
    //   65: fconst_0
    //   66: fcmpg
    //   67: ifge -> 104
    //   70: fload #10
    //   72: fstore #12
    //   74: fload #10
    //   76: ldc -0.001
    //   78: fcmpl
    //   79: ifle -> 135
    //   82: aload_0
    //   83: getfield mArrayValues : [F
    //   86: iload_2
    //   87: fconst_0
    //   88: fastore
    //   89: aload #11
    //   91: aload_0
    //   92: getfield mRow : Landroid/support/constraint/solver/ArrayRow;
    //   95: invokevirtual removeFromRow : (Landroid/support/constraint/solver/ArrayRow;)V
    //   98: fconst_0
    //   99: fstore #12
    //   101: goto -> 135
    //   104: fload #10
    //   106: fstore #12
    //   108: fload #10
    //   110: ldc 0.001
    //   112: fcmpg
    //   113: ifge -> 135
    //   116: aload_0
    //   117: getfield mArrayValues : [F
    //   120: iload_2
    //   121: fconst_0
    //   122: fastore
    //   123: aload #11
    //   125: aload_0
    //   126: getfield mRow : Landroid/support/constraint/solver/ArrayRow;
    //   129: invokevirtual removeFromRow : (Landroid/support/constraint/solver/ArrayRow;)V
    //   132: goto -> 98
    //   135: aload_3
    //   136: astore #13
    //   138: aload #5
    //   140: astore #14
    //   142: fload #6
    //   144: fstore #10
    //   146: iload #7
    //   148: istore #15
    //   150: fload #8
    //   152: fstore #16
    //   154: iload #9
    //   156: istore #17
    //   158: fload #12
    //   160: fconst_0
    //   161: fcmpl
    //   162: ifeq -> 519
    //   165: aload #11
    //   167: getfield mType : Landroid/support/constraint/solver/SolverVariable$Type;
    //   170: getstatic android/support/constraint/solver/SolverVariable$Type.UNRESTRICTED : Landroid/support/constraint/solver/SolverVariable$Type;
    //   173: if_acmpne -> 319
    //   176: aload_3
    //   177: ifnonnull -> 212
    //   180: aload_0
    //   181: aload #11
    //   183: aload_1
    //   184: invokespecial isNew : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/LinearSystem;)Z
    //   187: istore #15
    //   189: aload #11
    //   191: astore #13
    //   193: aload #5
    //   195: astore #14
    //   197: fload #12
    //   199: fstore #10
    //   201: fload #8
    //   203: fstore #16
    //   205: iload #9
    //   207: istore #17
    //   209: goto -> 519
    //   212: fload #6
    //   214: fload #12
    //   216: fcmpl
    //   217: ifle -> 232
    //   220: aload_0
    //   221: aload #11
    //   223: aload_1
    //   224: invokespecial isNew : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/LinearSystem;)Z
    //   227: istore #15
    //   229: goto -> 189
    //   232: aload_3
    //   233: astore #13
    //   235: aload #5
    //   237: astore #14
    //   239: fload #6
    //   241: fstore #10
    //   243: iload #7
    //   245: istore #15
    //   247: fload #8
    //   249: fstore #16
    //   251: iload #9
    //   253: istore #17
    //   255: iload #7
    //   257: ifne -> 519
    //   260: aload_3
    //   261: astore #13
    //   263: aload #5
    //   265: astore #14
    //   267: fload #6
    //   269: fstore #10
    //   271: iload #7
    //   273: istore #15
    //   275: fload #8
    //   277: fstore #16
    //   279: iload #9
    //   281: istore #17
    //   283: aload_0
    //   284: aload #11
    //   286: aload_1
    //   287: invokespecial isNew : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/LinearSystem;)Z
    //   290: ifeq -> 519
    //   293: iconst_1
    //   294: istore #15
    //   296: aload #11
    //   298: astore #13
    //   300: aload #5
    //   302: astore #14
    //   304: fload #12
    //   306: fstore #10
    //   308: fload #8
    //   310: fstore #16
    //   312: iload #9
    //   314: istore #17
    //   316: goto -> 519
    //   319: aload_3
    //   320: astore #13
    //   322: aload #5
    //   324: astore #14
    //   326: fload #6
    //   328: fstore #10
    //   330: iload #7
    //   332: istore #15
    //   334: fload #8
    //   336: fstore #16
    //   338: iload #9
    //   340: istore #17
    //   342: aload_3
    //   343: ifnonnull -> 519
    //   346: aload_3
    //   347: astore #13
    //   349: aload #5
    //   351: astore #14
    //   353: fload #6
    //   355: fstore #10
    //   357: iload #7
    //   359: istore #15
    //   361: fload #8
    //   363: fstore #16
    //   365: iload #9
    //   367: istore #17
    //   369: fload #12
    //   371: fconst_0
    //   372: fcmpg
    //   373: ifge -> 519
    //   376: aload #5
    //   378: ifnonnull -> 416
    //   381: aload_0
    //   382: aload #11
    //   384: aload_1
    //   385: invokespecial isNew : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/LinearSystem;)Z
    //   388: istore #15
    //   390: iload #15
    //   392: istore #17
    //   394: aload_3
    //   395: astore #13
    //   397: aload #11
    //   399: astore #14
    //   401: fload #6
    //   403: fstore #10
    //   405: iload #7
    //   407: istore #15
    //   409: fload #12
    //   411: fstore #16
    //   413: goto -> 519
    //   416: fload #8
    //   418: fload #12
    //   420: fcmpl
    //   421: ifle -> 436
    //   424: aload_0
    //   425: aload #11
    //   427: aload_1
    //   428: invokespecial isNew : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/LinearSystem;)Z
    //   431: istore #15
    //   433: goto -> 390
    //   436: aload_3
    //   437: astore #13
    //   439: aload #5
    //   441: astore #14
    //   443: fload #6
    //   445: fstore #10
    //   447: iload #7
    //   449: istore #15
    //   451: fload #8
    //   453: fstore #16
    //   455: iload #9
    //   457: istore #17
    //   459: iload #9
    //   461: ifne -> 519
    //   464: aload_3
    //   465: astore #13
    //   467: aload #5
    //   469: astore #14
    //   471: fload #6
    //   473: fstore #10
    //   475: iload #7
    //   477: istore #15
    //   479: fload #8
    //   481: fstore #16
    //   483: iload #9
    //   485: istore #17
    //   487: aload_0
    //   488: aload #11
    //   490: aload_1
    //   491: invokespecial isNew : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/LinearSystem;)Z
    //   494: ifeq -> 519
    //   497: iconst_1
    //   498: istore #17
    //   500: fload #12
    //   502: fstore #16
    //   504: iload #7
    //   506: istore #15
    //   508: fload #6
    //   510: fstore #10
    //   512: aload #11
    //   514: astore #14
    //   516: aload_3
    //   517: astore #13
    //   519: aload_0
    //   520: getfield mArrayNextIndices : [I
    //   523: iload_2
    //   524: iaload
    //   525: istore_2
    //   526: iinc #4, 1
    //   529: aload #13
    //   531: astore_3
    //   532: aload #14
    //   534: astore #5
    //   536: fload #10
    //   538: fstore #6
    //   540: iload #15
    //   542: istore #7
    //   544: fload #16
    //   546: fstore #8
    //   548: iload #17
    //   550: istore #9
    //   552: goto -> 25
    //   555: aload_3
    //   556: ifnull -> 561
    //   559: aload_3
    //   560: areturn
    //   561: aload #5
    //   563: areturn
  }
  
  public final void clear() {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      SolverVariable solverVariable = this.mCache.mIndexedVariables[this.mArrayIndices[i]];
      if (solverVariable != null)
        solverVariable.removeFromRow(this.mRow); 
      i = this.mArrayNextIndices[i];
    } 
    this.mHead = -1;
    this.mLast = -1;
    this.mDidFillOnce = false;
    this.currentSize = 0;
  }
  
  final boolean containsKey(SolverVariable paramSolverVariable) {
    if (this.mHead == -1)
      return false; 
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (this.mArrayIndices[i] == paramSolverVariable.id)
        return true; 
      i = this.mArrayNextIndices[i];
    } 
    return false;
  }
  
  public void display() {
    int i = this.currentSize;
    System.out.print("{ ");
    for (byte b = 0; b < i; b++) {
      SolverVariable solverVariable = getVariable(b);
      if (solverVariable != null) {
        PrintStream printStream = System.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(solverVariable);
        stringBuilder.append(" = ");
        stringBuilder.append(getVariableValue(b));
        stringBuilder.append(" ");
        printStream.print(stringBuilder.toString());
      } 
    } 
    System.out.println(" }");
  }
  
  void divideByAmount(float paramFloat) {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      float[] arrayOfFloat = this.mArrayValues;
      arrayOfFloat[i] = arrayOfFloat[i] / paramFloat;
      i = this.mArrayNextIndices[i];
    } 
  }
  
  public final float get(SolverVariable paramSolverVariable) {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (this.mArrayIndices[i] == paramSolverVariable.id)
        return this.mArrayValues[i]; 
      i = this.mArrayNextIndices[i];
    } 
    return 0.0F;
  }
  
  SolverVariable getPivotCandidate() {
    // Byte code:
    //   0: aload_0
    //   1: getfield candidate : Landroid/support/constraint/solver/SolverVariable;
    //   4: ifnonnull -> 100
    //   7: aload_0
    //   8: getfield mHead : I
    //   11: istore_1
    //   12: iconst_0
    //   13: istore_2
    //   14: aconst_null
    //   15: astore_3
    //   16: iload_1
    //   17: iconst_m1
    //   18: if_icmpeq -> 98
    //   21: iload_2
    //   22: aload_0
    //   23: getfield currentSize : I
    //   26: if_icmpge -> 98
    //   29: aload_3
    //   30: astore #4
    //   32: aload_0
    //   33: getfield mArrayValues : [F
    //   36: iload_1
    //   37: faload
    //   38: fconst_0
    //   39: fcmpg
    //   40: ifge -> 82
    //   43: aload_0
    //   44: getfield mCache : Landroid/support/constraint/solver/Cache;
    //   47: getfield mIndexedVariables : [Landroid/support/constraint/solver/SolverVariable;
    //   50: aload_0
    //   51: getfield mArrayIndices : [I
    //   54: iload_1
    //   55: iaload
    //   56: aaload
    //   57: astore #5
    //   59: aload_3
    //   60: ifnull -> 78
    //   63: aload_3
    //   64: astore #4
    //   66: aload_3
    //   67: getfield strength : I
    //   70: aload #5
    //   72: getfield strength : I
    //   75: if_icmpge -> 82
    //   78: aload #5
    //   80: astore #4
    //   82: aload_0
    //   83: getfield mArrayNextIndices : [I
    //   86: iload_1
    //   87: iaload
    //   88: istore_1
    //   89: iinc #2, 1
    //   92: aload #4
    //   94: astore_3
    //   95: goto -> 16
    //   98: aload_3
    //   99: areturn
    //   100: aload_0
    //   101: getfield candidate : Landroid/support/constraint/solver/SolverVariable;
    //   104: areturn
  }
  
  SolverVariable getPivotCandidate(boolean[] paramArrayOfboolean, SolverVariable paramSolverVariable) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mHead : I
    //   4: istore_3
    //   5: iconst_0
    //   6: istore #4
    //   8: aconst_null
    //   9: astore #5
    //   11: fconst_0
    //   12: fstore #6
    //   14: iload_3
    //   15: iconst_m1
    //   16: if_icmpeq -> 182
    //   19: iload #4
    //   21: aload_0
    //   22: getfield currentSize : I
    //   25: if_icmpge -> 182
    //   28: aload #5
    //   30: astore #7
    //   32: fload #6
    //   34: fstore #8
    //   36: aload_0
    //   37: getfield mArrayValues : [F
    //   40: iload_3
    //   41: faload
    //   42: fconst_0
    //   43: fcmpg
    //   44: ifge -> 161
    //   47: aload_0
    //   48: getfield mCache : Landroid/support/constraint/solver/Cache;
    //   51: getfield mIndexedVariables : [Landroid/support/constraint/solver/SolverVariable;
    //   54: aload_0
    //   55: getfield mArrayIndices : [I
    //   58: iload_3
    //   59: iaload
    //   60: aaload
    //   61: astore #9
    //   63: aload_1
    //   64: ifnull -> 85
    //   67: aload #5
    //   69: astore #7
    //   71: fload #6
    //   73: fstore #8
    //   75: aload_1
    //   76: aload #9
    //   78: getfield id : I
    //   81: baload
    //   82: ifne -> 161
    //   85: aload #5
    //   87: astore #7
    //   89: fload #6
    //   91: fstore #8
    //   93: aload #9
    //   95: aload_2
    //   96: if_acmpeq -> 161
    //   99: aload #9
    //   101: getfield mType : Landroid/support/constraint/solver/SolverVariable$Type;
    //   104: getstatic android/support/constraint/solver/SolverVariable$Type.SLACK : Landroid/support/constraint/solver/SolverVariable$Type;
    //   107: if_acmpeq -> 129
    //   110: aload #5
    //   112: astore #7
    //   114: fload #6
    //   116: fstore #8
    //   118: aload #9
    //   120: getfield mType : Landroid/support/constraint/solver/SolverVariable$Type;
    //   123: getstatic android/support/constraint/solver/SolverVariable$Type.ERROR : Landroid/support/constraint/solver/SolverVariable$Type;
    //   126: if_acmpne -> 161
    //   129: aload_0
    //   130: getfield mArrayValues : [F
    //   133: iload_3
    //   134: faload
    //   135: fstore #10
    //   137: aload #5
    //   139: astore #7
    //   141: fload #6
    //   143: fstore #8
    //   145: fload #10
    //   147: fload #6
    //   149: fcmpg
    //   150: ifge -> 161
    //   153: aload #9
    //   155: astore #7
    //   157: fload #10
    //   159: fstore #8
    //   161: aload_0
    //   162: getfield mArrayNextIndices : [I
    //   165: iload_3
    //   166: iaload
    //   167: istore_3
    //   168: iinc #4, 1
    //   171: aload #7
    //   173: astore #5
    //   175: fload #8
    //   177: fstore #6
    //   179: goto -> 14
    //   182: aload #5
    //   184: areturn
  }
  
  final SolverVariable getVariable(int paramInt) {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (b == paramInt)
        return this.mCache.mIndexedVariables[this.mArrayIndices[i]]; 
      i = this.mArrayNextIndices[i];
    } 
    return null;
  }
  
  final float getVariableValue(int paramInt) {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (b == paramInt)
        return this.mArrayValues[i]; 
      i = this.mArrayNextIndices[i];
    } 
    return 0.0F;
  }
  
  boolean hasAtLeastOnePositiveVariable() {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (this.mArrayValues[i] > 0.0F)
        return true; 
      i = this.mArrayNextIndices[i];
    } 
    return false;
  }
  
  void invert() {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      float[] arrayOfFloat = this.mArrayValues;
      arrayOfFloat[i] = arrayOfFloat[i] * -1.0F;
      i = this.mArrayNextIndices[i];
    } 
  }
  
  public final void put(SolverVariable paramSolverVariable, float paramFloat) {
    if (paramFloat == 0.0F) {
      remove(paramSolverVariable, true);
      return;
    } 
    if (this.mHead == -1) {
      this.mHead = 0;
      this.mArrayValues[this.mHead] = paramFloat;
      this.mArrayIndices[this.mHead] = paramSolverVariable.id;
      this.mArrayNextIndices[this.mHead] = -1;
      paramSolverVariable.usageInRowCount++;
      paramSolverVariable.addToRow(this.mRow);
      this.currentSize++;
      if (!this.mDidFillOnce) {
        this.mLast++;
        if (this.mLast >= this.mArrayIndices.length) {
          this.mDidFillOnce = true;
          this.mLast = this.mArrayIndices.length - 1;
        } 
      } 
      return;
    } 
    int i = this.mHead;
    int j = 0;
    int k = -1;
    while (i != -1 && j < this.currentSize) {
      if (this.mArrayIndices[i] == paramSolverVariable.id) {
        this.mArrayValues[i] = paramFloat;
        return;
      } 
      if (this.mArrayIndices[i] < paramSolverVariable.id)
        k = i; 
      i = this.mArrayNextIndices[i];
      j++;
    } 
    i = this.mLast + 1;
    if (this.mDidFillOnce)
      if (this.mArrayIndices[this.mLast] == -1) {
        i = this.mLast;
      } else {
        i = this.mArrayIndices.length;
      }  
    j = i;
    if (i >= this.mArrayIndices.length) {
      j = i;
      if (this.currentSize < this.mArrayIndices.length) {
        byte b = 0;
        while (true) {
          j = i;
          if (b < this.mArrayIndices.length) {
            if (this.mArrayIndices[b] == -1) {
              j = b;
              break;
            } 
            b++;
            continue;
          } 
          break;
        } 
      } 
    } 
    i = j;
    if (j >= this.mArrayIndices.length) {
      i = this.mArrayIndices.length;
      this.ROW_SIZE *= 2;
      this.mDidFillOnce = false;
      this.mLast = i - 1;
      this.mArrayValues = Arrays.copyOf(this.mArrayValues, this.ROW_SIZE);
      this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
      this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
    } 
    this.mArrayIndices[i] = paramSolverVariable.id;
    this.mArrayValues[i] = paramFloat;
    if (k != -1) {
      this.mArrayNextIndices[i] = this.mArrayNextIndices[k];
      this.mArrayNextIndices[k] = i;
    } else {
      this.mArrayNextIndices[i] = this.mHead;
      this.mHead = i;
    } 
    paramSolverVariable.usageInRowCount++;
    paramSolverVariable.addToRow(this.mRow);
    this.currentSize++;
    if (!this.mDidFillOnce)
      this.mLast++; 
    if (this.currentSize >= this.mArrayIndices.length)
      this.mDidFillOnce = true; 
    if (this.mLast >= this.mArrayIndices.length) {
      this.mDidFillOnce = true;
      this.mLast = this.mArrayIndices.length - 1;
    } 
  }
  
  public final float remove(SolverVariable paramSolverVariable, boolean paramBoolean) {
    if (this.candidate == paramSolverVariable)
      this.candidate = null; 
    if (this.mHead == -1)
      return 0.0F; 
    int i = this.mHead;
    byte b = 0;
    int j = -1;
    while (i != -1 && b < this.currentSize) {
      if (this.mArrayIndices[i] == paramSolverVariable.id) {
        if (i == this.mHead) {
          this.mHead = this.mArrayNextIndices[i];
        } else {
          this.mArrayNextIndices[j] = this.mArrayNextIndices[i];
        } 
        if (paramBoolean)
          paramSolverVariable.removeFromRow(this.mRow); 
        paramSolverVariable.usageInRowCount--;
        this.currentSize--;
        this.mArrayIndices[i] = -1;
        if (this.mDidFillOnce)
          this.mLast = i; 
        return this.mArrayValues[i];
      } 
      int k = this.mArrayNextIndices[i];
      b++;
      j = i;
      i = k;
    } 
    return 0.0F;
  }
  
  int sizeInBytes() {
    return this.mArrayIndices.length * 4 * 3 + 0 + 36;
  }
  
  public String toString() {
    String str = "";
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      StringBuilder stringBuilder2 = new StringBuilder();
      stringBuilder2.append(str);
      stringBuilder2.append(" -> ");
      String str1 = stringBuilder2.toString();
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(str1);
      stringBuilder1.append(this.mArrayValues[i]);
      stringBuilder1.append(" : ");
      str1 = stringBuilder1.toString();
      stringBuilder1 = new StringBuilder();
      stringBuilder1.append(str1);
      stringBuilder1.append(this.mCache.mIndexedVariables[this.mArrayIndices[i]]);
      str = stringBuilder1.toString();
      i = this.mArrayNextIndices[i];
    } 
    return str;
  }
  
  final void updateFromRow(ArrayRow paramArrayRow1, ArrayRow paramArrayRow2, boolean paramBoolean) {
    int i = this.mHead;
    label22: while (true) {
      for (int j = 0; i != -1 && j < this.currentSize; j++) {
        if (this.mArrayIndices[i] == paramArrayRow2.variable.id) {
          float f = this.mArrayValues[i];
          remove(paramArrayRow2.variable, paramBoolean);
          ArrayLinkedVariables arrayLinkedVariables = paramArrayRow2.variables;
          j = arrayLinkedVariables.mHead;
          for (i = 0; j != -1 && i < arrayLinkedVariables.currentSize; i++) {
            add(this.mCache.mIndexedVariables[arrayLinkedVariables.mArrayIndices[j]], arrayLinkedVariables.mArrayValues[j] * f, paramBoolean);
            j = arrayLinkedVariables.mArrayNextIndices[j];
          } 
          paramArrayRow1.constantValue += paramArrayRow2.constantValue * f;
          if (paramBoolean)
            paramArrayRow2.variable.removeFromRow(paramArrayRow1); 
          i = this.mHead;
          continue label22;
        } 
        i = this.mArrayNextIndices[i];
      } 
      break;
    } 
  }
  
  void updateFromSystem(ArrayRow paramArrayRow, ArrayRow[] paramArrayOfArrayRow) {
    int i = this.mHead;
    label22: while (true) {
      for (int j = 0; i != -1 && j < this.currentSize; j++) {
        SolverVariable solverVariable = this.mCache.mIndexedVariables[this.mArrayIndices[i]];
        if (solverVariable.definitionId != -1) {
          float f = this.mArrayValues[i];
          remove(solverVariable, true);
          ArrayRow arrayRow = paramArrayOfArrayRow[solverVariable.definitionId];
          if (!arrayRow.isSimpleDefinition) {
            ArrayLinkedVariables arrayLinkedVariables = arrayRow.variables;
            j = arrayLinkedVariables.mHead;
            for (i = 0; j != -1 && i < arrayLinkedVariables.currentSize; i++) {
              add(this.mCache.mIndexedVariables[arrayLinkedVariables.mArrayIndices[j]], arrayLinkedVariables.mArrayValues[j] * f, true);
              j = arrayLinkedVariables.mArrayNextIndices[j];
            } 
          } 
          paramArrayRow.constantValue += arrayRow.constantValue * f;
          arrayRow.variable.removeFromRow(paramArrayRow);
          i = this.mHead;
          continue label22;
        } 
        i = this.mArrayNextIndices[i];
      } 
      break;
    } 
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\constraint\solver\ArrayLinkedVariables.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */