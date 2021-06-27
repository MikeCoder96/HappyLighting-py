package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;
import java.util.ArrayList;

public class ConstraintTableLayout extends ConstraintWidgetContainer {
  public static final int ALIGN_CENTER = 0;
  
  private static final int ALIGN_FULL = 3;
  
  public static final int ALIGN_LEFT = 1;
  
  public static final int ALIGN_RIGHT = 2;
  
  private ArrayList<Guideline> mHorizontalGuidelines = new ArrayList<Guideline>();
  
  private ArrayList<HorizontalSlice> mHorizontalSlices = new ArrayList<HorizontalSlice>();
  
  private int mNumCols = 0;
  
  private int mNumRows = 0;
  
  private int mPadding = 8;
  
  private boolean mVerticalGrowth = true;
  
  private ArrayList<Guideline> mVerticalGuidelines = new ArrayList<Guideline>();
  
  private ArrayList<VerticalSlice> mVerticalSlices = new ArrayList<VerticalSlice>();
  
  private LinearSystem system = null;
  
  public ConstraintTableLayout() {}
  
  public ConstraintTableLayout(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public ConstraintTableLayout(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  private void setChildrenConnections() {
    int i = this.mChildren.size();
    byte b = 0;
    int j = 0;
    while (b < i) {
      ConstraintWidget constraintWidget1 = this.mChildren.get(b);
      int k = j + constraintWidget1.getContainerItemSkip();
      j = this.mNumCols;
      int m = k / this.mNumCols;
      HorizontalSlice horizontalSlice = this.mHorizontalSlices.get(m);
      VerticalSlice verticalSlice = this.mVerticalSlices.get(k % j);
      ConstraintWidget constraintWidget3 = verticalSlice.left;
      ConstraintWidget constraintWidget4 = verticalSlice.right;
      ConstraintWidget constraintWidget5 = horizontalSlice.top;
      ConstraintWidget constraintWidget2 = horizontalSlice.bottom;
      constraintWidget1.getAnchor(ConstraintAnchor.Type.LEFT).connect(constraintWidget3.getAnchor(ConstraintAnchor.Type.LEFT), this.mPadding);
      if (constraintWidget4 instanceof Guideline) {
        constraintWidget1.getAnchor(ConstraintAnchor.Type.RIGHT).connect(constraintWidget4.getAnchor(ConstraintAnchor.Type.LEFT), this.mPadding);
      } else {
        constraintWidget1.getAnchor(ConstraintAnchor.Type.RIGHT).connect(constraintWidget4.getAnchor(ConstraintAnchor.Type.RIGHT), this.mPadding);
      } 
      switch (verticalSlice.alignment) {
        case 3:
          constraintWidget1.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
          break;
        case 2:
          constraintWidget1.getAnchor(ConstraintAnchor.Type.LEFT).setStrength(ConstraintAnchor.Strength.WEAK);
          constraintWidget1.getAnchor(ConstraintAnchor.Type.RIGHT).setStrength(ConstraintAnchor.Strength.STRONG);
          break;
        case 1:
          constraintWidget1.getAnchor(ConstraintAnchor.Type.LEFT).setStrength(ConstraintAnchor.Strength.STRONG);
          constraintWidget1.getAnchor(ConstraintAnchor.Type.RIGHT).setStrength(ConstraintAnchor.Strength.WEAK);
          break;
      } 
      constraintWidget1.getAnchor(ConstraintAnchor.Type.TOP).connect(constraintWidget5.getAnchor(ConstraintAnchor.Type.TOP), this.mPadding);
      if (constraintWidget2 instanceof Guideline) {
        constraintWidget1.getAnchor(ConstraintAnchor.Type.BOTTOM).connect(constraintWidget2.getAnchor(ConstraintAnchor.Type.TOP), this.mPadding);
      } else {
        constraintWidget1.getAnchor(ConstraintAnchor.Type.BOTTOM).connect(constraintWidget2.getAnchor(ConstraintAnchor.Type.BOTTOM), this.mPadding);
      } 
      j = k + 1;
      b++;
    } 
  }
  
  private void setHorizontalSlices() {
    this.mHorizontalSlices.clear();
    float f1 = 100.0F / this.mNumRows;
    ConstraintTableLayout constraintTableLayout = this;
    float f2 = f1;
    for (byte b = 0; b < this.mNumRows; b++) {
      HorizontalSlice horizontalSlice = new HorizontalSlice();
      horizontalSlice.top = constraintTableLayout;
      if (b < this.mNumRows - 1) {
        Guideline guideline = new Guideline();
        guideline.setOrientation(0);
        guideline.setParent(this);
        guideline.setGuidePercent((int)f2);
        f2 += f1;
        horizontalSlice.bottom = guideline;
        this.mHorizontalGuidelines.add(guideline);
      } else {
        horizontalSlice.bottom = this;
      } 
      ConstraintWidget constraintWidget = horizontalSlice.bottom;
      this.mHorizontalSlices.add(horizontalSlice);
    } 
    updateDebugSolverNames();
  }
  
  private void setVerticalSlices() {
    this.mVerticalSlices.clear();
    float f1 = 100.0F / this.mNumCols;
    byte b = 0;
    ConstraintTableLayout constraintTableLayout = this;
    float f2 = f1;
    while (b < this.mNumCols) {
      VerticalSlice verticalSlice = new VerticalSlice();
      verticalSlice.left = constraintTableLayout;
      if (b < this.mNumCols - 1) {
        Guideline guideline = new Guideline();
        guideline.setOrientation(1);
        guideline.setParent(this);
        guideline.setGuidePercent((int)f2);
        f2 += f1;
        verticalSlice.right = guideline;
        this.mVerticalGuidelines.add(guideline);
      } else {
        verticalSlice.right = this;
      } 
      ConstraintWidget constraintWidget = verticalSlice.right;
      this.mVerticalSlices.add(verticalSlice);
      b++;
    } 
    updateDebugSolverNames();
  }
  
  private void updateDebugSolverNames() {
    if (this.system == null)
      return; 
    int i = this.mVerticalGuidelines.size();
    boolean bool = false;
    byte b;
    for (b = 0; b < i; b++) {
      Guideline guideline = this.mVerticalGuidelines.get(b);
      LinearSystem linearSystem = this.system;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(getDebugName());
      stringBuilder.append(".VG");
      stringBuilder.append(b);
      guideline.setDebugSolverName(linearSystem, stringBuilder.toString());
    } 
    i = this.mHorizontalGuidelines.size();
    for (b = bool; b < i; b++) {
      Guideline guideline = this.mHorizontalGuidelines.get(b);
      LinearSystem linearSystem = this.system;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(getDebugName());
      stringBuilder.append(".HG");
      stringBuilder.append(b);
      guideline.setDebugSolverName(linearSystem, stringBuilder.toString());
    } 
  }
  
  public void addToSolver(LinearSystem paramLinearSystem) {
    super.addToSolver(paramLinearSystem);
    int i = this.mChildren.size();
    if (i == 0)
      return; 
    setTableDimensions();
    if (paramLinearSystem == this.mSystem) {
      int j = this.mVerticalGuidelines.size();
      boolean bool = false;
      byte b = 0;
      while (true) {
        boolean bool1 = true;
        if (b < j) {
          Guideline guideline = this.mVerticalGuidelines.get(b);
          if (getHorizontalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)
            bool1 = false; 
          guideline.setPositionRelaxed(bool1);
          guideline.addToSolver(paramLinearSystem);
          b++;
          continue;
        } 
        int k = this.mHorizontalGuidelines.size();
        j = 0;
        while (true) {
          b = bool;
          if (j < k) {
            Guideline guideline = this.mHorizontalGuidelines.get(j);
            if (getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
              bool1 = true;
            } else {
              bool1 = false;
            } 
            guideline.setPositionRelaxed(bool1);
            guideline.addToSolver(paramLinearSystem);
            j++;
            continue;
          } 
          break;
        } 
        while (b < i) {
          ((ConstraintWidget)this.mChildren.get(b)).addToSolver(paramLinearSystem);
          b++;
        } 
        break;
      } 
    } 
  }
  
  public void computeGuidelinesPercentPositions() {
    int i = this.mVerticalGuidelines.size();
    boolean bool = false;
    byte b;
    for (b = 0; b < i; b++)
      ((Guideline)this.mVerticalGuidelines.get(b)).inferRelativePercentPosition(); 
    i = this.mHorizontalGuidelines.size();
    for (b = bool; b < i; b++)
      ((Guideline)this.mHorizontalGuidelines.get(b)).inferRelativePercentPosition(); 
  }
  
  public void cycleColumnAlignment(int paramInt) {
    VerticalSlice verticalSlice = this.mVerticalSlices.get(paramInt);
    switch (verticalSlice.alignment) {
      case 2:
        verticalSlice.alignment = 1;
        break;
      case 1:
        verticalSlice.alignment = 0;
        break;
      case 0:
        verticalSlice.alignment = 2;
        break;
    } 
    setChildrenConnections();
  }
  
  public String getColumnAlignmentRepresentation(int paramInt) {
    VerticalSlice verticalSlice = this.mVerticalSlices.get(paramInt);
    return (verticalSlice.alignment == 1) ? "L" : ((verticalSlice.alignment == 0) ? "C" : ((verticalSlice.alignment == 3) ? "F" : ((verticalSlice.alignment == 2) ? "R" : "!")));
  }
  
  public String getColumnsAlignmentRepresentation() {
    int i = this.mVerticalSlices.size();
    String str = "";
    byte b = 0;
    while (b < i) {
      String str1;
      VerticalSlice verticalSlice = this.mVerticalSlices.get(b);
      if (verticalSlice.alignment == 1) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append("L");
        str1 = stringBuilder.toString();
      } else if (verticalSlice.alignment == 0) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append("C");
        str1 = stringBuilder.toString();
      } else if (verticalSlice.alignment == 3) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append("F");
        str1 = stringBuilder.toString();
      } else {
        str1 = str;
        if (verticalSlice.alignment == 2) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(str);
          stringBuilder.append("R");
          str1 = stringBuilder.toString();
        } 
      } 
      b++;
      str = str1;
    } 
    return str;
  }
  
  public ArrayList<Guideline> getHorizontalGuidelines() {
    return this.mHorizontalGuidelines;
  }
  
  public int getNumCols() {
    return this.mNumCols;
  }
  
  public int getNumRows() {
    return this.mNumRows;
  }
  
  public int getPadding() {
    return this.mPadding;
  }
  
  public String getType() {
    return "ConstraintTableLayout";
  }
  
  public ArrayList<Guideline> getVerticalGuidelines() {
    return this.mVerticalGuidelines;
  }
  
  public boolean handlesInternalConstraints() {
    return true;
  }
  
  public boolean isVerticalGrowth() {
    return this.mVerticalGrowth;
  }
  
  public void setColumnAlignment(int paramInt1, int paramInt2) {
    if (paramInt1 < this.mVerticalSlices.size()) {
      ((VerticalSlice)this.mVerticalSlices.get(paramInt1)).alignment = paramInt2;
      setChildrenConnections();
    } 
  }
  
  public void setColumnAlignment(String paramString) {
    int i = paramString.length();
    for (byte b = 0; b < i; b++) {
      char c = paramString.charAt(b);
      if (c == 'L') {
        setColumnAlignment(b, 1);
      } else if (c == 'C') {
        setColumnAlignment(b, 0);
      } else if (c == 'F') {
        setColumnAlignment(b, 3);
      } else if (c == 'R') {
        setColumnAlignment(b, 2);
      } else {
        setColumnAlignment(b, 0);
      } 
    } 
  }
  
  public void setDebugSolverName(LinearSystem paramLinearSystem, String paramString) {
    this.system = paramLinearSystem;
    super.setDebugSolverName(paramLinearSystem, paramString);
    updateDebugSolverNames();
  }
  
  public void setNumCols(int paramInt) {
    if (this.mVerticalGrowth && this.mNumCols != paramInt) {
      this.mNumCols = paramInt;
      setVerticalSlices();
      setTableDimensions();
    } 
  }
  
  public void setNumRows(int paramInt) {
    if (!this.mVerticalGrowth && this.mNumCols != paramInt) {
      this.mNumRows = paramInt;
      setHorizontalSlices();
      setTableDimensions();
    } 
  }
  
  public void setPadding(int paramInt) {
    if (paramInt > 1)
      this.mPadding = paramInt; 
  }
  
  public void setTableDimensions() {
    int i = this.mChildren.size();
    int j = 0;
    int k = 0;
    while (j < i) {
      k += ((ConstraintWidget)this.mChildren.get(j)).getContainerItemSkip();
      j++;
    } 
    i += k;
    if (this.mVerticalGrowth) {
      if (this.mNumCols == 0)
        setNumCols(1); 
      j = i / this.mNumCols;
      k = j;
      if (this.mNumCols * j < i)
        k = j + 1; 
      if (this.mNumRows == k && this.mVerticalGuidelines.size() == this.mNumCols - 1)
        return; 
      this.mNumRows = k;
      setHorizontalSlices();
    } else {
      if (this.mNumRows == 0)
        setNumRows(1); 
      j = i / this.mNumRows;
      k = j;
      if (this.mNumRows * j < i)
        k = j + 1; 
      if (this.mNumCols == k && this.mHorizontalGuidelines.size() == this.mNumRows - 1)
        return; 
      this.mNumCols = k;
      setVerticalSlices();
    } 
    setChildrenConnections();
  }
  
  public void setVerticalGrowth(boolean paramBoolean) {
    this.mVerticalGrowth = paramBoolean;
  }
  
  public void updateFromSolver(LinearSystem paramLinearSystem) {
    super.updateFromSolver(paramLinearSystem);
    if (paramLinearSystem == this.mSystem) {
      int i = this.mVerticalGuidelines.size();
      boolean bool = false;
      byte b;
      for (b = 0; b < i; b++)
        ((Guideline)this.mVerticalGuidelines.get(b)).updateFromSolver(paramLinearSystem); 
      i = this.mHorizontalGuidelines.size();
      for (b = bool; b < i; b++)
        ((Guideline)this.mHorizontalGuidelines.get(b)).updateFromSolver(paramLinearSystem); 
    } 
  }
  
  class HorizontalSlice {
    ConstraintWidget bottom;
    
    int padding;
    
    ConstraintWidget top;
  }
  
  class VerticalSlice {
    int alignment = 1;
    
    ConstraintWidget left;
    
    int padding;
    
    ConstraintWidget right;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\constraint\solver\widgets\ConstraintTableLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */