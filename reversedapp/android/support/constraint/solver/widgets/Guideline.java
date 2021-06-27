package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.SolverVariable;
import java.util.ArrayList;

public class Guideline extends ConstraintWidget {
  public static final int HORIZONTAL = 0;
  
  public static final int RELATIVE_BEGIN = 1;
  
  public static final int RELATIVE_END = 2;
  
  public static final int RELATIVE_PERCENT = 0;
  
  public static final int RELATIVE_UNKNWON = -1;
  
  public static final int VERTICAL = 1;
  
  private ConstraintAnchor mAnchor = this.mTop;
  
  private Rectangle mHead;
  
  private int mHeadSize;
  
  private boolean mIsPositionRelaxed;
  
  private int mMinimumPosition;
  
  private int mOrientation;
  
  protected int mRelativeBegin = -1;
  
  protected int mRelativeEnd = -1;
  
  protected float mRelativePercent = -1.0F;
  
  public Guideline() {
    byte b = 0;
    this.mOrientation = 0;
    this.mIsPositionRelaxed = false;
    this.mMinimumPosition = 0;
    this.mHead = new Rectangle();
    this.mHeadSize = 8;
    this.mAnchors.clear();
    this.mAnchors.add(this.mAnchor);
    int i = this.mListAnchors.length;
    while (b < i) {
      this.mListAnchors[b] = this.mAnchor;
      b++;
    } 
  }
  
  public void addToSolver(LinearSystem paramLinearSystem) {
    boolean bool;
    ConstraintWidgetContainer constraintWidgetContainer = (ConstraintWidgetContainer)getParent();
    if (constraintWidgetContainer == null)
      return; 
    ConstraintAnchor constraintAnchor1 = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.LEFT);
    ConstraintAnchor constraintAnchor2 = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.RIGHT);
    if (this.mParent != null && this.mParent.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
      bool = true;
    } else {
      bool = false;
    } 
    if (this.mOrientation == 0) {
      constraintAnchor1 = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.TOP);
      constraintAnchor2 = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.BOTTOM);
      if (this.mParent != null && this.mParent.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
        bool = true;
      } else {
        bool = false;
      } 
    } 
    if (this.mRelativeBegin != -1) {
      SolverVariable solverVariable = paramLinearSystem.createObjectVariable(this.mAnchor);
      paramLinearSystem.addEquality(solverVariable, paramLinearSystem.createObjectVariable(constraintAnchor1), this.mRelativeBegin, 6);
      if (bool)
        paramLinearSystem.addGreaterThan(paramLinearSystem.createObjectVariable(constraintAnchor2), solverVariable, 0, 5); 
    } else {
      SolverVariable solverVariable;
      if (this.mRelativeEnd != -1) {
        SolverVariable solverVariable1 = paramLinearSystem.createObjectVariable(this.mAnchor);
        solverVariable = paramLinearSystem.createObjectVariable(constraintAnchor2);
        paramLinearSystem.addEquality(solverVariable1, solverVariable, -this.mRelativeEnd, 6);
        if (bool) {
          paramLinearSystem.addGreaterThan(solverVariable1, paramLinearSystem.createObjectVariable(constraintAnchor1), 0, 5);
          paramLinearSystem.addGreaterThan(solverVariable, solverVariable1, 0, 5);
        } 
      } else if (this.mRelativePercent != -1.0F) {
        paramLinearSystem.addConstraint(LinearSystem.createRowDimensionPercent(paramLinearSystem, paramLinearSystem.createObjectVariable(this.mAnchor), paramLinearSystem.createObjectVariable(constraintAnchor1), paramLinearSystem.createObjectVariable(solverVariable), this.mRelativePercent, this.mIsPositionRelaxed));
      } 
    } 
  }
  
  public boolean allowedInBarrier() {
    return true;
  }
  
  public void analyze(int paramInt) {
    ConstraintWidget constraintWidget = getParent();
    if (constraintWidget == null)
      return; 
    if (getOrientation() == 1) {
      this.mTop.getResolutionNode().dependsOn(1, constraintWidget.mTop.getResolutionNode(), 0);
      this.mBottom.getResolutionNode().dependsOn(1, constraintWidget.mTop.getResolutionNode(), 0);
      if (this.mRelativeBegin != -1) {
        this.mLeft.getResolutionNode().dependsOn(1, constraintWidget.mLeft.getResolutionNode(), this.mRelativeBegin);
        this.mRight.getResolutionNode().dependsOn(1, constraintWidget.mLeft.getResolutionNode(), this.mRelativeBegin);
      } else if (this.mRelativeEnd != -1) {
        this.mLeft.getResolutionNode().dependsOn(1, constraintWidget.mRight.getResolutionNode(), -this.mRelativeEnd);
        this.mRight.getResolutionNode().dependsOn(1, constraintWidget.mRight.getResolutionNode(), -this.mRelativeEnd);
      } else if (this.mRelativePercent != -1.0F && constraintWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.FIXED) {
        paramInt = (int)(constraintWidget.mWidth * this.mRelativePercent);
        this.mLeft.getResolutionNode().dependsOn(1, constraintWidget.mLeft.getResolutionNode(), paramInt);
        this.mRight.getResolutionNode().dependsOn(1, constraintWidget.mLeft.getResolutionNode(), paramInt);
      } 
    } else {
      this.mLeft.getResolutionNode().dependsOn(1, constraintWidget.mLeft.getResolutionNode(), 0);
      this.mRight.getResolutionNode().dependsOn(1, constraintWidget.mLeft.getResolutionNode(), 0);
      if (this.mRelativeBegin != -1) {
        this.mTop.getResolutionNode().dependsOn(1, constraintWidget.mTop.getResolutionNode(), this.mRelativeBegin);
        this.mBottom.getResolutionNode().dependsOn(1, constraintWidget.mTop.getResolutionNode(), this.mRelativeBegin);
      } else if (this.mRelativeEnd != -1) {
        this.mTop.getResolutionNode().dependsOn(1, constraintWidget.mBottom.getResolutionNode(), -this.mRelativeEnd);
        this.mBottom.getResolutionNode().dependsOn(1, constraintWidget.mBottom.getResolutionNode(), -this.mRelativeEnd);
      } else if (this.mRelativePercent != -1.0F && constraintWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.FIXED) {
        paramInt = (int)(constraintWidget.mHeight * this.mRelativePercent);
        this.mTop.getResolutionNode().dependsOn(1, constraintWidget.mTop.getResolutionNode(), paramInt);
        this.mBottom.getResolutionNode().dependsOn(1, constraintWidget.mTop.getResolutionNode(), paramInt);
      } 
    } 
  }
  
  public void cyclePosition() {
    if (this.mRelativeBegin != -1) {
      inferRelativePercentPosition();
    } else if (this.mRelativePercent != -1.0F) {
      inferRelativeEndPosition();
    } else if (this.mRelativeEnd != -1) {
      inferRelativeBeginPosition();
    } 
  }
  
  public ConstraintAnchor getAnchor() {
    return this.mAnchor;
  }
  
  public ConstraintAnchor getAnchor(ConstraintAnchor.Type paramType) {
    switch (paramType) {
      default:
        throw new AssertionError(paramType.name());
      case BASELINE:
      case CENTER:
      case CENTER_X:
      case CENTER_Y:
      case NONE:
        return null;
      case TOP:
      case BOTTOM:
        if (this.mOrientation == 0)
          return this.mAnchor; 
      case LEFT:
      case RIGHT:
        break;
    } 
    if (this.mOrientation == 1)
      return this.mAnchor; 
  }
  
  public ArrayList<ConstraintAnchor> getAnchors() {
    return this.mAnchors;
  }
  
  public Rectangle getHead() {
    this.mHead.setBounds(getDrawX() - this.mHeadSize, getDrawY() - this.mHeadSize * 2, this.mHeadSize * 2, this.mHeadSize * 2);
    if (getOrientation() == 0)
      this.mHead.setBounds(getDrawX() - this.mHeadSize * 2, getDrawY() - this.mHeadSize, this.mHeadSize * 2, this.mHeadSize * 2); 
    return this.mHead;
  }
  
  public int getOrientation() {
    return this.mOrientation;
  }
  
  public int getRelativeBegin() {
    return this.mRelativeBegin;
  }
  
  public int getRelativeBehaviour() {
    return (this.mRelativePercent != -1.0F) ? 0 : ((this.mRelativeBegin != -1) ? 1 : ((this.mRelativeEnd != -1) ? 2 : -1));
  }
  
  public int getRelativeEnd() {
    return this.mRelativeEnd;
  }
  
  public float getRelativePercent() {
    return this.mRelativePercent;
  }
  
  public String getType() {
    return "Guideline";
  }
  
  void inferRelativeBeginPosition() {
    int i = getX();
    if (this.mOrientation == 0)
      i = getY(); 
    setGuideBegin(i);
  }
  
  void inferRelativeEndPosition() {
    int i = getParent().getWidth() - getX();
    if (this.mOrientation == 0)
      i = getParent().getHeight() - getY(); 
    setGuideEnd(i);
  }
  
  void inferRelativePercentPosition() {
    float f = getX() / getParent().getWidth();
    if (this.mOrientation == 0)
      f = getY() / getParent().getHeight(); 
    setGuidePercent(f);
  }
  
  public void setDrawOrigin(int paramInt1, int paramInt2) {
    if (this.mOrientation == 1) {
      paramInt1 -= this.mOffsetX;
      if (this.mRelativeBegin != -1) {
        setGuideBegin(paramInt1);
      } else if (this.mRelativeEnd != -1) {
        setGuideEnd(getParent().getWidth() - paramInt1);
      } else if (this.mRelativePercent != -1.0F) {
        setGuidePercent(paramInt1 / getParent().getWidth());
      } 
    } else {
      paramInt1 = paramInt2 - this.mOffsetY;
      if (this.mRelativeBegin != -1) {
        setGuideBegin(paramInt1);
      } else if (this.mRelativeEnd != -1) {
        setGuideEnd(getParent().getHeight() - paramInt1);
      } else if (this.mRelativePercent != -1.0F) {
        setGuidePercent(paramInt1 / getParent().getHeight());
      } 
    } 
  }
  
  public void setGuideBegin(int paramInt) {
    if (paramInt > -1) {
      this.mRelativePercent = -1.0F;
      this.mRelativeBegin = paramInt;
      this.mRelativeEnd = -1;
    } 
  }
  
  public void setGuideEnd(int paramInt) {
    if (paramInt > -1) {
      this.mRelativePercent = -1.0F;
      this.mRelativeBegin = -1;
      this.mRelativeEnd = paramInt;
    } 
  }
  
  public void setGuidePercent(float paramFloat) {
    if (paramFloat > -1.0F) {
      this.mRelativePercent = paramFloat;
      this.mRelativeBegin = -1;
      this.mRelativeEnd = -1;
    } 
  }
  
  public void setGuidePercent(int paramInt) {
    setGuidePercent(paramInt / 100.0F);
  }
  
  public void setMinimumPosition(int paramInt) {
    this.mMinimumPosition = paramInt;
  }
  
  public void setOrientation(int paramInt) {
    if (this.mOrientation == paramInt)
      return; 
    this.mOrientation = paramInt;
    this.mAnchors.clear();
    if (this.mOrientation == 1) {
      this.mAnchor = this.mLeft;
    } else {
      this.mAnchor = this.mTop;
    } 
    this.mAnchors.add(this.mAnchor);
    int i = this.mListAnchors.length;
    for (paramInt = 0; paramInt < i; paramInt++)
      this.mListAnchors[paramInt] = this.mAnchor; 
  }
  
  public void setPositionRelaxed(boolean paramBoolean) {
    if (this.mIsPositionRelaxed == paramBoolean)
      return; 
    this.mIsPositionRelaxed = paramBoolean;
  }
  
  public void updateFromSolver(LinearSystem paramLinearSystem) {
    if (getParent() == null)
      return; 
    int i = paramLinearSystem.getObjectVariableValue(this.mAnchor);
    if (this.mOrientation == 1) {
      setX(i);
      setY(0);
      setHeight(getParent().getHeight());
      setWidth(0);
    } else {
      setX(0);
      setY(i);
      setWidth(getParent().getWidth());
      setHeight(0);
    } 
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\constraint\solver\widgets\Guideline.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */