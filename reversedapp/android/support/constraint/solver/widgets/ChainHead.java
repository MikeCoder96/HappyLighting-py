package android.support.constraint.solver.widgets;

import java.util.ArrayList;

public class ChainHead {
  private boolean mDefined;
  
  protected ConstraintWidget mFirst;
  
  protected ConstraintWidget mFirstMatchConstraintWidget;
  
  protected ConstraintWidget mFirstVisibleWidget;
  
  protected boolean mHasComplexMatchWeights;
  
  protected boolean mHasDefinedWeights;
  
  protected boolean mHasUndefinedWeights;
  
  protected ConstraintWidget mHead;
  
  private boolean mIsRtl = false;
  
  protected ConstraintWidget mLast;
  
  protected ConstraintWidget mLastMatchConstraintWidget;
  
  protected ConstraintWidget mLastVisibleWidget;
  
  private int mOrientation;
  
  protected float mTotalWeight = 0.0F;
  
  protected ArrayList<ConstraintWidget> mWeightedMatchConstraintsWidgets;
  
  protected int mWidgetsCount;
  
  protected int mWidgetsMatchCount;
  
  public ChainHead(ConstraintWidget paramConstraintWidget, int paramInt, boolean paramBoolean) {
    this.mFirst = paramConstraintWidget;
    this.mOrientation = paramInt;
    this.mIsRtl = paramBoolean;
  }
  
  private void defineChainProperties() {
    int i = this.mOrientation * 2;
    ConstraintWidget constraintWidget1 = this.mFirst;
    ConstraintWidget constraintWidget2 = this.mFirst;
    ConstraintWidget constraintWidget3 = this.mFirst;
    boolean bool1 = false;
    boolean bool2 = false;
    while (!bool2) {
      this.mWidgetsCount++;
      ConstraintWidget[] arrayOfConstraintWidget = constraintWidget2.mNextChainWidget;
      int j = this.mOrientation;
      constraintWidget3 = null;
      arrayOfConstraintWidget[j] = null;
      constraintWidget2.mListNextMatchConstraintsWidget[this.mOrientation] = null;
      if (constraintWidget2.getVisibility() != 8) {
        if (this.mFirstVisibleWidget == null)
          this.mFirstVisibleWidget = constraintWidget2; 
        this.mLastVisibleWidget = constraintWidget2;
        if (constraintWidget2.mListDimensionBehaviors[this.mOrientation] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (constraintWidget2.mResolvedMatchConstraintDefault[this.mOrientation] == 0 || constraintWidget2.mResolvedMatchConstraintDefault[this.mOrientation] == 3 || constraintWidget2.mResolvedMatchConstraintDefault[this.mOrientation] == 2)) {
          this.mWidgetsMatchCount++;
          float f = constraintWidget2.mWeight[this.mOrientation];
          if (f > 0.0F)
            this.mTotalWeight += constraintWidget2.mWeight[this.mOrientation]; 
          if (isMatchConstraintEqualityCandidate(constraintWidget2, this.mOrientation)) {
            if (f < 0.0F) {
              this.mHasUndefinedWeights = true;
            } else {
              this.mHasDefinedWeights = true;
            } 
            if (this.mWeightedMatchConstraintsWidgets == null)
              this.mWeightedMatchConstraintsWidgets = new ArrayList<ConstraintWidget>(); 
            this.mWeightedMatchConstraintsWidgets.add(constraintWidget2);
          } 
          if (this.mFirstMatchConstraintWidget == null)
            this.mFirstMatchConstraintWidget = constraintWidget2; 
          if (this.mLastMatchConstraintWidget != null)
            this.mLastMatchConstraintWidget.mListNextMatchConstraintsWidget[this.mOrientation] = constraintWidget2; 
          this.mLastMatchConstraintWidget = constraintWidget2;
        } 
      } 
      if (constraintWidget1 != constraintWidget2)
        constraintWidget1.mNextChainWidget[this.mOrientation] = constraintWidget2; 
      ConstraintAnchor constraintAnchor = (constraintWidget2.mListAnchors[i + 1]).mTarget;
      constraintWidget1 = constraintWidget3;
      if (constraintAnchor != null) {
        ConstraintWidget constraintWidget = constraintAnchor.mOwner;
        constraintWidget1 = constraintWidget3;
        if ((constraintWidget.mListAnchors[i]).mTarget != null)
          if ((constraintWidget.mListAnchors[i]).mTarget.mOwner != constraintWidget2) {
            constraintWidget1 = constraintWidget3;
          } else {
            constraintWidget1 = constraintWidget;
          }  
      } 
      if (constraintWidget1 == null) {
        constraintWidget1 = constraintWidget2;
        bool2 = true;
      } 
      constraintWidget3 = constraintWidget2;
      constraintWidget2 = constraintWidget1;
      constraintWidget1 = constraintWidget3;
    } 
    this.mLast = constraintWidget2;
    if (this.mOrientation == 0 && this.mIsRtl) {
      this.mHead = this.mLast;
    } else {
      this.mHead = this.mFirst;
    } 
    boolean bool3 = bool1;
    if (this.mHasDefinedWeights) {
      bool3 = bool1;
      if (this.mHasUndefinedWeights)
        bool3 = true; 
    } 
    this.mHasComplexMatchWeights = bool3;
  }
  
  private static boolean isMatchConstraintEqualityCandidate(ConstraintWidget paramConstraintWidget, int paramInt) {
    boolean bool;
    if (paramConstraintWidget.getVisibility() != 8 && paramConstraintWidget.mListDimensionBehaviors[paramInt] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (paramConstraintWidget.mResolvedMatchConstraintDefault[paramInt] == 0 || paramConstraintWidget.mResolvedMatchConstraintDefault[paramInt] == 3)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void define() {
    if (!this.mDefined)
      defineChainProperties(); 
    this.mDefined = true;
  }
  
  public ConstraintWidget getFirst() {
    return this.mFirst;
  }
  
  public ConstraintWidget getFirstMatchConstraintWidget() {
    return this.mFirstMatchConstraintWidget;
  }
  
  public ConstraintWidget getFirstVisibleWidget() {
    return this.mFirstVisibleWidget;
  }
  
  public ConstraintWidget getHead() {
    return this.mHead;
  }
  
  public ConstraintWidget getLast() {
    return this.mLast;
  }
  
  public ConstraintWidget getLastMatchConstraintWidget() {
    return this.mLastMatchConstraintWidget;
  }
  
  public ConstraintWidget getLastVisibleWidget() {
    return this.mLastVisibleWidget;
  }
  
  public float getTotalWeight() {
    return this.mTotalWeight;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\constraint\solver\widgets\ChainHead.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */