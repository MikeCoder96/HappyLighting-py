package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.Metrics;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConstraintWidgetContainer extends WidgetContainer {
  private static final boolean DEBUG = false;
  
  static final boolean DEBUG_GRAPH = false;
  
  private static final boolean DEBUG_LAYOUT = false;
  
  private static final int MAX_ITERATIONS = 8;
  
  private static final boolean USE_SNAPSHOT = true;
  
  int mDebugSolverPassCount = 0;
  
  public boolean mGroupsWrapOptimized = false;
  
  private boolean mHeightMeasuredTooSmall = false;
  
  ChainHead[] mHorizontalChainsArray = new ChainHead[4];
  
  int mHorizontalChainsSize = 0;
  
  public boolean mHorizontalWrapOptimized = false;
  
  private boolean mIsRtl = false;
  
  private int mOptimizationLevel = 7;
  
  int mPaddingBottom;
  
  int mPaddingLeft;
  
  int mPaddingRight;
  
  int mPaddingTop;
  
  public boolean mSkipSolver = false;
  
  private Snapshot mSnapshot;
  
  protected LinearSystem mSystem = new LinearSystem();
  
  ChainHead[] mVerticalChainsArray = new ChainHead[4];
  
  int mVerticalChainsSize = 0;
  
  public boolean mVerticalWrapOptimized = false;
  
  public List<ConstraintWidgetGroup> mWidgetGroups = new ArrayList<ConstraintWidgetGroup>();
  
  private boolean mWidthMeasuredTooSmall = false;
  
  public int mWrapFixedHeight = 0;
  
  public int mWrapFixedWidth = 0;
  
  public ConstraintWidgetContainer() {}
  
  public ConstraintWidgetContainer(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public ConstraintWidgetContainer(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  private void addHorizontalChain(ConstraintWidget paramConstraintWidget) {
    if (this.mHorizontalChainsSize + 1 >= this.mHorizontalChainsArray.length)
      this.mHorizontalChainsArray = Arrays.<ChainHead>copyOf(this.mHorizontalChainsArray, this.mHorizontalChainsArray.length * 2); 
    this.mHorizontalChainsArray[this.mHorizontalChainsSize] = new ChainHead(paramConstraintWidget, 0, isRtl());
    this.mHorizontalChainsSize++;
  }
  
  private void addVerticalChain(ConstraintWidget paramConstraintWidget) {
    if (this.mVerticalChainsSize + 1 >= this.mVerticalChainsArray.length)
      this.mVerticalChainsArray = Arrays.<ChainHead>copyOf(this.mVerticalChainsArray, this.mVerticalChainsArray.length * 2); 
    this.mVerticalChainsArray[this.mVerticalChainsSize] = new ChainHead(paramConstraintWidget, 1, isRtl());
    this.mVerticalChainsSize++;
  }
  
  private void resetChains() {
    this.mHorizontalChainsSize = 0;
    this.mVerticalChainsSize = 0;
  }
  
  void addChain(ConstraintWidget paramConstraintWidget, int paramInt) {
    if (paramInt == 0) {
      addHorizontalChain(paramConstraintWidget);
    } else if (paramInt == 1) {
      addVerticalChain(paramConstraintWidget);
    } 
  }
  
  public boolean addChildrenToSolver(LinearSystem paramLinearSystem) {
    addToSolver(paramLinearSystem);
    int i = this.mChildren.size();
    for (byte b = 0; b < i; b++) {
      ConstraintWidget constraintWidget = this.mChildren.get(b);
      if (constraintWidget instanceof ConstraintWidgetContainer) {
        ConstraintWidget.DimensionBehaviour dimensionBehaviour1 = constraintWidget.mListDimensionBehaviors[0];
        ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = constraintWidget.mListDimensionBehaviors[1];
        if (dimensionBehaviour1 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)
          constraintWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED); 
        if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)
          constraintWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED); 
        constraintWidget.addToSolver(paramLinearSystem);
        if (dimensionBehaviour1 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)
          constraintWidget.setHorizontalDimensionBehaviour(dimensionBehaviour1); 
        if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)
          constraintWidget.setVerticalDimensionBehaviour(dimensionBehaviour2); 
      } else {
        Optimizer.checkMatchParent(this, paramLinearSystem, constraintWidget);
        constraintWidget.addToSolver(paramLinearSystem);
      } 
    } 
    if (this.mHorizontalChainsSize > 0)
      Chain.applyChainConstraints(this, paramLinearSystem, 0); 
    if (this.mVerticalChainsSize > 0)
      Chain.applyChainConstraints(this, paramLinearSystem, 1); 
    return true;
  }
  
  public void analyze(int paramInt) {
    super.analyze(paramInt);
    int i = this.mChildren.size();
    for (byte b = 0; b < i; b++)
      ((ConstraintWidget)this.mChildren.get(b)).analyze(paramInt); 
  }
  
  public void fillMetrics(Metrics paramMetrics) {
    this.mSystem.fillMetrics(paramMetrics);
  }
  
  public ArrayList<Guideline> getHorizontalGuidelines() {
    ArrayList<ConstraintWidget> arrayList = new ArrayList();
    int i = this.mChildren.size();
    for (byte b = 0; b < i; b++) {
      ConstraintWidget constraintWidget = this.mChildren.get(b);
      if (constraintWidget instanceof Guideline) {
        constraintWidget = constraintWidget;
        if (constraintWidget.getOrientation() == 0)
          arrayList.add(constraintWidget); 
      } 
    } 
    return (ArrayList)arrayList;
  }
  
  public int getOptimizationLevel() {
    return this.mOptimizationLevel;
  }
  
  public LinearSystem getSystem() {
    return this.mSystem;
  }
  
  public String getType() {
    return "ConstraintLayout";
  }
  
  public ArrayList<Guideline> getVerticalGuidelines() {
    ArrayList<ConstraintWidget> arrayList = new ArrayList();
    int i = this.mChildren.size();
    for (byte b = 0; b < i; b++) {
      ConstraintWidget constraintWidget = this.mChildren.get(b);
      if (constraintWidget instanceof Guideline) {
        constraintWidget = constraintWidget;
        if (constraintWidget.getOrientation() == 1)
          arrayList.add(constraintWidget); 
      } 
    } 
    return (ArrayList)arrayList;
  }
  
  public List<ConstraintWidgetGroup> getWidgetGroups() {
    return this.mWidgetGroups;
  }
  
  public boolean handlesInternalConstraints() {
    return false;
  }
  
  public boolean isHeightMeasuredTooSmall() {
    return this.mHeightMeasuredTooSmall;
  }
  
  public boolean isRtl() {
    return this.mIsRtl;
  }
  
  public boolean isWidthMeasuredTooSmall() {
    return this.mWidthMeasuredTooSmall;
  }
  
  public void layout() {
    boolean bool;
    int i = this.mX;
    int j = this.mY;
    int k = Math.max(0, getWidth());
    int m = Math.max(0, getHeight());
    this.mWidthMeasuredTooSmall = false;
    this.mHeightMeasuredTooSmall = false;
    if (this.mParent != null) {
      if (this.mSnapshot == null)
        this.mSnapshot = new Snapshot(this); 
      this.mSnapshot.updateFrom(this);
      setX(this.mPaddingLeft);
      setY(this.mPaddingTop);
      resetAnchors();
      resetSolverVariables(this.mSystem.getCache());
    } else {
      this.mX = 0;
      this.mY = 0;
    } 
    if (this.mOptimizationLevel != 0) {
      if (!optimizeFor(8))
        optimizeReset(); 
      if (!optimizeFor(32))
        optimize(); 
      this.mSystem.graphOptimizer = true;
    } else {
      this.mSystem.graphOptimizer = false;
    } 
    ConstraintWidget.DimensionBehaviour dimensionBehaviour1 = this.mListDimensionBehaviors[1];
    ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = this.mListDimensionBehaviors[0];
    resetChains();
    if (this.mWidgetGroups.size() == 0) {
      this.mWidgetGroups.clear();
      this.mWidgetGroups.add(0, new ConstraintWidgetGroup(this.mChildren));
    } 
    int n = this.mWidgetGroups.size();
    ArrayList<ConstraintWidget> arrayList = this.mChildren;
    if (getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
      bool = true;
    } else {
      bool = false;
    } 
    int i1 = 0;
    for (byte b = 0; b < n && !this.mSkipSolver; b++) {
      if (!((ConstraintWidgetGroup)this.mWidgetGroups.get(b)).mSkipSolver) {
        if (optimizeFor(32))
          if (getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.FIXED && getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.FIXED) {
            this.mChildren = (ArrayList<ConstraintWidget>)((ConstraintWidgetGroup)this.mWidgetGroups.get(b)).getWidgetsToSolve();
          } else {
            this.mChildren = (ArrayList<ConstraintWidget>)((ConstraintWidgetGroup)this.mWidgetGroups.get(b)).mConstrainedGroup;
          }  
        resetChains();
        int i2 = this.mChildren.size();
        int i3;
        for (i3 = 0; i3 < i2; i3++) {
          ConstraintWidget constraintWidget = this.mChildren.get(i3);
          if (constraintWidget instanceof WidgetContainer)
            ((WidgetContainer)constraintWidget).layout(); 
        } 
        i3 = i1;
        int i4 = 0;
        boolean bool1 = true;
        i1 = n;
        n = i3;
        i3 = i4;
        label152: while (bool1) {
          i4 = i3 + 1;
          i3 = n;
          try {
            this.mSystem.reset();
            i3 = n;
            resetChains();
            i3 = n;
            createObjectVariables(this.mSystem);
            byte b1 = 0;
            while (true) {
              if (b1 < i2) {
                i3 = n;
                ConstraintWidget constraintWidget = this.mChildren.get(b1);
                try {
                  constraintWidget.createObjectVariables(this.mSystem);
                  b1++;
                } catch (Exception exception) {
                  continue label152;
                } 
                continue;
              } 
              i3 = n;
              boolean bool3 = addChildrenToSolver(this.mSystem);
              if (bool3)
                try {
                  this.mSystem.minimize();
                } catch (Exception exception) {
                  bool1 = bool3;
                  n = i3;
                  continue label152;
                }  
              bool1 = bool3;
              n = i3;
              break;
            } 
          } catch (Exception exception) {
            n = i3;
            exception.printStackTrace();
            PrintStream printStream = System.out;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("EXCEPTION : ");
            stringBuilder.append(exception);
            printStream.println(stringBuilder.toString());
          } 
          if (bool1) {
            updateChildrenFromSolver(this.mSystem, Optimizer.flags);
          } else {
            updateFromSolver(this.mSystem);
            for (i3 = 0; i3 < i2; i3++) {
              ConstraintWidget constraintWidget = this.mChildren.get(i3);
              if (constraintWidget.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.getWidth() < constraintWidget.getWrapWidth()) {
                Optimizer.flags[2] = true;
                break;
              } 
              if (constraintWidget.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.getHeight() < constraintWidget.getWrapHeight()) {
                Optimizer.flags[2] = true;
                break;
              } 
            } 
          } 
          if (bool && i4 < 8 && Optimizer.flags[2]) {
            boolean bool3 = false;
            int i6 = 0;
            int i5 = 0;
            i3 = i4;
            for (i4 = bool3; i4 < i2; i4++) {
              ConstraintWidget constraintWidget = this.mChildren.get(i4);
              i6 = Math.max(i6, constraintWidget.mX + constraintWidget.getWidth());
              i5 = Math.max(i5, constraintWidget.mY + constraintWidget.getHeight());
            } 
            i6 = Math.max(this.mMinWidth, i6);
            i4 = Math.max(this.mMinHeight, i5);
            if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && getWidth() < i6) {
              setWidth(i6);
              this.mListDimensionBehaviors[0] = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
              bool1 = true;
              n = 1;
            } else {
              bool1 = false;
            } 
            if (dimensionBehaviour1 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && getHeight() < i4) {
              setHeight(i4);
              this.mListDimensionBehaviors[1] = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
              bool1 = true;
              n = 1;
            } 
          } else {
            i3 = i4;
            bool1 = false;
          } 
          i4 = Math.max(this.mMinWidth, getWidth());
          if (i4 > getWidth()) {
            setWidth(i4);
            this.mListDimensionBehaviors[0] = ConstraintWidget.DimensionBehaviour.FIXED;
            bool1 = true;
            n = 1;
          } 
          i4 = Math.max(this.mMinHeight, getHeight());
          if (i4 > getHeight()) {
            setHeight(i4);
            this.mListDimensionBehaviors[1] = ConstraintWidget.DimensionBehaviour.FIXED;
            bool1 = true;
            n = 1;
          } 
          boolean bool2 = bool1;
          i4 = n;
          if (n == 0) {
            boolean bool3 = bool1;
            int i5 = n;
            if (this.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
              bool3 = bool1;
              i5 = n;
              if (k > 0) {
                bool3 = bool1;
                i5 = n;
                if (getWidth() > k) {
                  this.mWidthMeasuredTooSmall = true;
                  this.mListDimensionBehaviors[0] = ConstraintWidget.DimensionBehaviour.FIXED;
                  setWidth(k);
                  bool3 = true;
                  i5 = 1;
                } 
              } 
            } 
            bool2 = bool3;
            i4 = i5;
            if (this.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
              bool2 = bool3;
              i4 = i5;
              if (m > 0) {
                bool2 = bool3;
                i4 = i5;
                if (getHeight() > m) {
                  this.mHeightMeasuredTooSmall = true;
                  this.mListDimensionBehaviors[1] = ConstraintWidget.DimensionBehaviour.FIXED;
                  setHeight(m);
                  bool1 = true;
                  n = 1;
                  continue;
                } 
              } 
            } 
          } 
          bool1 = bool2;
          n = i4;
        } 
        ((ConstraintWidgetGroup)this.mWidgetGroups.get(b)).updateUnresolvedWidgets();
        i3 = n;
        n = i1;
        i1 = i3;
      } 
    } 
    this.mChildren = arrayList;
    if (this.mParent != null) {
      n = Math.max(this.mMinWidth, getWidth());
      int i2 = Math.max(this.mMinHeight, getHeight());
      this.mSnapshot.applyTo(this);
      setWidth(n + this.mPaddingLeft + this.mPaddingRight);
      setHeight(i2 + this.mPaddingTop + this.mPaddingBottom);
    } else {
      this.mX = i;
      this.mY = j;
    } 
    if (i1 != 0) {
      this.mListDimensionBehaviors[0] = dimensionBehaviour2;
      this.mListDimensionBehaviors[1] = dimensionBehaviour1;
    } 
    resetSolverVariables(this.mSystem.getCache());
    if (this == getRootConstraintContainer())
      updateDrawPosition(); 
  }
  
  public void optimize() {
    if (!optimizeFor(8))
      analyze(this.mOptimizationLevel); 
    solveGraph();
  }
  
  public boolean optimizeFor(int paramInt) {
    boolean bool;
    if ((this.mOptimizationLevel & paramInt) == paramInt) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void optimizeForDimensions(int paramInt1, int paramInt2) {
    if (this.mListDimensionBehaviors[0] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && this.mResolutionWidth != null)
      this.mResolutionWidth.resolve(paramInt1); 
    if (this.mListDimensionBehaviors[1] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && this.mResolutionHeight != null)
      this.mResolutionHeight.resolve(paramInt2); 
  }
  
  public void optimizeReset() {
    int i = this.mChildren.size();
    resetResolutionNodes();
    for (byte b = 0; b < i; b++)
      ((ConstraintWidget)this.mChildren.get(b)).resetResolutionNodes(); 
  }
  
  public void preOptimize() {
    optimizeReset();
    analyze(this.mOptimizationLevel);
  }
  
  public void reset() {
    this.mSystem.reset();
    this.mPaddingLeft = 0;
    this.mPaddingRight = 0;
    this.mPaddingTop = 0;
    this.mPaddingBottom = 0;
    this.mWidgetGroups.clear();
    this.mSkipSolver = false;
    super.reset();
  }
  
  public void resetGraph() {
    ResolutionAnchor resolutionAnchor1 = getAnchor(ConstraintAnchor.Type.LEFT).getResolutionNode();
    ResolutionAnchor resolutionAnchor2 = getAnchor(ConstraintAnchor.Type.TOP).getResolutionNode();
    resolutionAnchor1.invalidateAnchors();
    resolutionAnchor2.invalidateAnchors();
    resolutionAnchor1.resolve(null, 0.0F);
    resolutionAnchor2.resolve(null, 0.0F);
  }
  
  public void setOptimizationLevel(int paramInt) {
    this.mOptimizationLevel = paramInt;
  }
  
  public void setPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.mPaddingLeft = paramInt1;
    this.mPaddingTop = paramInt2;
    this.mPaddingRight = paramInt3;
    this.mPaddingBottom = paramInt4;
  }
  
  public void setRtl(boolean paramBoolean) {
    this.mIsRtl = paramBoolean;
  }
  
  public void solveGraph() {
    ResolutionAnchor resolutionAnchor1 = getAnchor(ConstraintAnchor.Type.LEFT).getResolutionNode();
    ResolutionAnchor resolutionAnchor2 = getAnchor(ConstraintAnchor.Type.TOP).getResolutionNode();
    resolutionAnchor1.resolve(null, 0.0F);
    resolutionAnchor2.resolve(null, 0.0F);
  }
  
  public void updateChildrenFromSolver(LinearSystem paramLinearSystem, boolean[] paramArrayOfboolean) {
    paramArrayOfboolean[2] = false;
    updateFromSolver(paramLinearSystem);
    int i = this.mChildren.size();
    for (byte b = 0; b < i; b++) {
      ConstraintWidget constraintWidget = this.mChildren.get(b);
      constraintWidget.updateFromSolver(paramLinearSystem);
      if (constraintWidget.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.getWidth() < constraintWidget.getWrapWidth())
        paramArrayOfboolean[2] = true; 
      if (constraintWidget.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.getHeight() < constraintWidget.getWrapHeight())
        paramArrayOfboolean[2] = true; 
    } 
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\constraint\solver\widgets\ConstraintWidgetContainer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */