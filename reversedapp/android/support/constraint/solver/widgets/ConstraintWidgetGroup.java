package android.support.constraint.solver.widgets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConstraintWidgetGroup {
  public List<ConstraintWidget> mConstrainedGroup;
  
  public final int[] mGroupDimensions = new int[] { this.mGroupWidth, this.mGroupHeight };
  
  int mGroupHeight = -1;
  
  int mGroupWidth = -1;
  
  public boolean mSkipSolver = false;
  
  List<ConstraintWidget> mStartHorizontalWidgets = new ArrayList<ConstraintWidget>();
  
  List<ConstraintWidget> mStartVerticalWidgets = new ArrayList<ConstraintWidget>();
  
  List<ConstraintWidget> mUnresolvedWidgets = new ArrayList<ConstraintWidget>();
  
  HashSet<ConstraintWidget> mWidgetsToSetHorizontal = new HashSet<ConstraintWidget>();
  
  HashSet<ConstraintWidget> mWidgetsToSetVertical = new HashSet<ConstraintWidget>();
  
  List<ConstraintWidget> mWidgetsToSolve = new ArrayList<ConstraintWidget>();
  
  ConstraintWidgetGroup(List<ConstraintWidget> paramList) {
    this.mConstrainedGroup = paramList;
  }
  
  ConstraintWidgetGroup(List<ConstraintWidget> paramList, boolean paramBoolean) {
    this.mConstrainedGroup = paramList;
    this.mSkipSolver = paramBoolean;
  }
  
  private void getWidgetsToSolveTraversal(ArrayList<ConstraintWidget> paramArrayList, ConstraintWidget paramConstraintWidget) {
    if (paramConstraintWidget.mGroupsToSolver)
      return; 
    paramArrayList.add(paramConstraintWidget);
    paramConstraintWidget.mGroupsToSolver = true;
    if (paramConstraintWidget.isFullyResolved())
      return; 
    boolean bool = paramConstraintWidget instanceof Helper;
    byte b1 = 0;
    if (bool) {
      Helper helper = (Helper)paramConstraintWidget;
      int j = helper.mWidgetsCount;
      for (byte b = 0; b < j; b++)
        getWidgetsToSolveTraversal(paramArrayList, helper.mWidgets[b]); 
    } 
    int i = paramConstraintWidget.mListAnchors.length;
    for (byte b2 = b1; b2 < i; b2++) {
      ConstraintAnchor constraintAnchor = (paramConstraintWidget.mListAnchors[b2]).mTarget;
      if (constraintAnchor != null) {
        ConstraintWidget constraintWidget = constraintAnchor.mOwner;
        if (constraintAnchor != null && constraintWidget != paramConstraintWidget.getParent())
          getWidgetsToSolveTraversal(paramArrayList, constraintWidget); 
      } 
    } 
  }
  
  private void updateResolvedDimension(ConstraintWidget paramConstraintWidget) {
    // Byte code:
    //   0: aload_1
    //   1: getfield mOptimizerMeasurable : Z
    //   4: ifeq -> 438
    //   7: aload_1
    //   8: invokevirtual isFullyResolved : ()Z
    //   11: ifeq -> 15
    //   14: return
    //   15: aload_1
    //   16: getfield mRight : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   19: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   22: astore_2
    //   23: iconst_0
    //   24: istore_3
    //   25: aload_2
    //   26: ifnull -> 35
    //   29: iconst_1
    //   30: istore #4
    //   32: goto -> 38
    //   35: iconst_0
    //   36: istore #4
    //   38: iload #4
    //   40: ifeq -> 54
    //   43: aload_1
    //   44: getfield mRight : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   47: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   50: astore_2
    //   51: goto -> 62
    //   54: aload_1
    //   55: getfield mLeft : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   58: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   61: astore_2
    //   62: aload_2
    //   63: ifnull -> 140
    //   66: aload_2
    //   67: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   70: getfield mOptimizerMeasured : Z
    //   73: ifne -> 84
    //   76: aload_0
    //   77: aload_2
    //   78: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   81: invokespecial updateResolvedDimension : (Landroid/support/constraint/solver/widgets/ConstraintWidget;)V
    //   84: aload_2
    //   85: getfield mType : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   88: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   91: if_acmpne -> 118
    //   94: aload_2
    //   95: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   98: getfield mX : I
    //   101: istore #5
    //   103: aload_2
    //   104: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   107: invokevirtual getWidth : ()I
    //   110: iload #5
    //   112: iadd
    //   113: istore #5
    //   115: goto -> 143
    //   118: aload_2
    //   119: getfield mType : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   122: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   125: if_acmpne -> 140
    //   128: aload_2
    //   129: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   132: getfield mX : I
    //   135: istore #5
    //   137: goto -> 143
    //   140: iconst_0
    //   141: istore #5
    //   143: iload #4
    //   145: ifeq -> 163
    //   148: iload #5
    //   150: aload_1
    //   151: getfield mRight : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   154: invokevirtual getMargin : ()I
    //   157: isub
    //   158: istore #5
    //   160: goto -> 180
    //   163: iload #5
    //   165: aload_1
    //   166: getfield mLeft : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   169: invokevirtual getMargin : ()I
    //   172: aload_1
    //   173: invokevirtual getWidth : ()I
    //   176: iadd
    //   177: iadd
    //   178: istore #5
    //   180: aload_1
    //   181: iload #5
    //   183: aload_1
    //   184: invokevirtual getWidth : ()I
    //   187: isub
    //   188: iload #5
    //   190: invokevirtual setHorizontalDimension : (II)V
    //   193: aload_1
    //   194: getfield mBaseline : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   197: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   200: ifnull -> 270
    //   203: aload_1
    //   204: getfield mBaseline : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   207: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   210: astore_2
    //   211: aload_2
    //   212: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   215: getfield mOptimizerMeasured : Z
    //   218: ifne -> 229
    //   221: aload_0
    //   222: aload_2
    //   223: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   226: invokespecial updateResolvedDimension : (Landroid/support/constraint/solver/widgets/ConstraintWidget;)V
    //   229: aload_2
    //   230: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   233: getfield mY : I
    //   236: aload_2
    //   237: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   240: getfield mBaselineDistance : I
    //   243: iadd
    //   244: aload_1
    //   245: getfield mBaselineDistance : I
    //   248: isub
    //   249: istore #5
    //   251: aload_1
    //   252: iload #5
    //   254: aload_1
    //   255: getfield mHeight : I
    //   258: iload #5
    //   260: iadd
    //   261: invokevirtual setVerticalDimension : (II)V
    //   264: aload_1
    //   265: iconst_1
    //   266: putfield mOptimizerMeasured : Z
    //   269: return
    //   270: aload_1
    //   271: getfield mBottom : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   274: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   277: ifnull -> 282
    //   280: iconst_1
    //   281: istore_3
    //   282: iload_3
    //   283: ifeq -> 297
    //   286: aload_1
    //   287: getfield mBottom : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   290: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   293: astore_2
    //   294: goto -> 305
    //   297: aload_1
    //   298: getfield mTop : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   301: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   304: astore_2
    //   305: iload #5
    //   307: istore #4
    //   309: aload_2
    //   310: ifnull -> 384
    //   313: aload_2
    //   314: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   317: getfield mOptimizerMeasured : Z
    //   320: ifne -> 331
    //   323: aload_0
    //   324: aload_2
    //   325: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   328: invokespecial updateResolvedDimension : (Landroid/support/constraint/solver/widgets/ConstraintWidget;)V
    //   331: aload_2
    //   332: getfield mType : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   335: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   338: if_acmpne -> 361
    //   341: aload_2
    //   342: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   345: getfield mY : I
    //   348: aload_2
    //   349: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   352: invokevirtual getHeight : ()I
    //   355: iadd
    //   356: istore #4
    //   358: goto -> 384
    //   361: iload #5
    //   363: istore #4
    //   365: aload_2
    //   366: getfield mType : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   369: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   372: if_acmpne -> 384
    //   375: aload_2
    //   376: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   379: getfield mY : I
    //   382: istore #4
    //   384: iload_3
    //   385: ifeq -> 403
    //   388: iload #4
    //   390: aload_1
    //   391: getfield mBottom : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   394: invokevirtual getMargin : ()I
    //   397: isub
    //   398: istore #5
    //   400: goto -> 420
    //   403: iload #4
    //   405: aload_1
    //   406: getfield mTop : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   409: invokevirtual getMargin : ()I
    //   412: aload_1
    //   413: invokevirtual getHeight : ()I
    //   416: iadd
    //   417: iadd
    //   418: istore #5
    //   420: aload_1
    //   421: iload #5
    //   423: aload_1
    //   424: invokevirtual getHeight : ()I
    //   427: isub
    //   428: iload #5
    //   430: invokevirtual setVerticalDimension : (II)V
    //   433: aload_1
    //   434: iconst_1
    //   435: putfield mOptimizerMeasured : Z
    //   438: return
  }
  
  void addWidgetsToSet(ConstraintWidget paramConstraintWidget, int paramInt) {
    if (paramInt == 0) {
      this.mWidgetsToSetHorizontal.add(paramConstraintWidget);
    } else if (paramInt == 1) {
      this.mWidgetsToSetVertical.add(paramConstraintWidget);
    } 
  }
  
  public List<ConstraintWidget> getStartWidgets(int paramInt) {
    return (paramInt == 0) ? this.mStartHorizontalWidgets : ((paramInt == 1) ? this.mStartVerticalWidgets : null);
  }
  
  Set<ConstraintWidget> getWidgetsToSet(int paramInt) {
    return (paramInt == 0) ? this.mWidgetsToSetHorizontal : ((paramInt == 1) ? this.mWidgetsToSetVertical : null);
  }
  
  List<ConstraintWidget> getWidgetsToSolve() {
    if (!this.mWidgetsToSolve.isEmpty())
      return this.mWidgetsToSolve; 
    int i = this.mConstrainedGroup.size();
    for (byte b = 0; b < i; b++) {
      ConstraintWidget constraintWidget = this.mConstrainedGroup.get(b);
      if (!constraintWidget.mOptimizerMeasurable)
        getWidgetsToSolveTraversal((ArrayList<ConstraintWidget>)this.mWidgetsToSolve, constraintWidget); 
    } 
    this.mUnresolvedWidgets.clear();
    this.mUnresolvedWidgets.addAll(this.mConstrainedGroup);
    this.mUnresolvedWidgets.removeAll(this.mWidgetsToSolve);
    return this.mWidgetsToSolve;
  }
  
  void updateUnresolvedWidgets() {
    int i = this.mUnresolvedWidgets.size();
    for (byte b = 0; b < i; b++)
      updateResolvedDimension(this.mUnresolvedWidgets.get(b)); 
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\constraint\solver\widgets\ConstraintWidgetGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */