package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Cache;
import java.util.ArrayList;

public class WidgetContainer extends ConstraintWidget {
  protected ArrayList<ConstraintWidget> mChildren = new ArrayList<ConstraintWidget>();
  
  public WidgetContainer() {}
  
  public WidgetContainer(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public WidgetContainer(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public static Rectangle getBounds(ArrayList<ConstraintWidget> paramArrayList) {
    Rectangle rectangle = new Rectangle();
    if (paramArrayList.size() == 0)
      return rectangle; 
    int i = paramArrayList.size();
    int j = Integer.MAX_VALUE;
    byte b = 0;
    int k = Integer.MAX_VALUE;
    int m = 0;
    int n;
    for (n = 0; b < i; n = i4) {
      ConstraintWidget constraintWidget = paramArrayList.get(b);
      int i1 = j;
      if (constraintWidget.getX() < j)
        i1 = constraintWidget.getX(); 
      int i2 = k;
      if (constraintWidget.getY() < k)
        i2 = constraintWidget.getY(); 
      int i3 = m;
      if (constraintWidget.getRight() > m)
        i3 = constraintWidget.getRight(); 
      int i4 = n;
      if (constraintWidget.getBottom() > n)
        i4 = constraintWidget.getBottom(); 
      b++;
      j = i1;
      k = i2;
      m = i3;
    } 
    rectangle.setBounds(j, k, m - j, n - k);
    return rectangle;
  }
  
  public void add(ConstraintWidget paramConstraintWidget) {
    this.mChildren.add(paramConstraintWidget);
    if (paramConstraintWidget.getParent() != null)
      ((WidgetContainer)paramConstraintWidget.getParent()).remove(paramConstraintWidget); 
    paramConstraintWidget.setParent(this);
  }
  
  public void add(ConstraintWidget... paramVarArgs) {
    int i = paramVarArgs.length;
    for (byte b = 0; b < i; b++)
      add(paramVarArgs[b]); 
  }
  
  public ConstraintWidget findWidget(float paramFloat1, float paramFloat2) {
    ConstraintWidget constraintWidget1;
    int i = getDrawX();
    int j = getDrawY();
    int k = getWidth();
    int m = getHeight();
    if (paramFloat1 >= i && paramFloat1 <= (k + i) && paramFloat2 >= j && paramFloat2 <= (m + j)) {
      constraintWidget1 = this;
    } else {
      constraintWidget1 = null;
    } 
    m = 0;
    k = this.mChildren.size();
    ConstraintWidget constraintWidget2;
    for (constraintWidget2 = constraintWidget1; m < k; constraintWidget2 = constraintWidget1) {
      ConstraintWidget constraintWidget = this.mChildren.get(m);
      if (constraintWidget instanceof WidgetContainer) {
        constraintWidget = ((WidgetContainer)constraintWidget).findWidget(paramFloat1, paramFloat2);
        constraintWidget1 = constraintWidget2;
        if (constraintWidget != null)
          constraintWidget1 = constraintWidget; 
      } else {
        int n = constraintWidget.getDrawX();
        j = constraintWidget.getDrawY();
        i = constraintWidget.getWidth();
        int i1 = constraintWidget.getHeight();
        constraintWidget1 = constraintWidget2;
        if (paramFloat1 >= n) {
          constraintWidget1 = constraintWidget2;
          if (paramFloat1 <= (i + n)) {
            constraintWidget1 = constraintWidget2;
            if (paramFloat2 >= j) {
              constraintWidget1 = constraintWidget2;
              if (paramFloat2 <= (i1 + j))
                constraintWidget1 = constraintWidget; 
            } 
          } 
        } 
      } 
      m++;
    } 
    return constraintWidget2;
  }
  
  public ArrayList<ConstraintWidget> findWidgets(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    ArrayList<ConstraintWidget> arrayList = new ArrayList();
    Rectangle rectangle = new Rectangle();
    rectangle.setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
    paramInt2 = this.mChildren.size();
    for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++) {
      ConstraintWidget constraintWidget = this.mChildren.get(paramInt1);
      Rectangle rectangle1 = new Rectangle();
      rectangle1.setBounds(constraintWidget.getDrawX(), constraintWidget.getDrawY(), constraintWidget.getWidth(), constraintWidget.getHeight());
      if (rectangle.intersects(rectangle1))
        arrayList.add(constraintWidget); 
    } 
    return arrayList;
  }
  
  public ArrayList<ConstraintWidget> getChildren() {
    return this.mChildren;
  }
  
  public ConstraintWidgetContainer getRootConstraintContainer() {
    ConstraintWidgetContainer constraintWidgetContainer;
    ConstraintWidget constraintWidget = getParent();
    if (this instanceof ConstraintWidgetContainer) {
      constraintWidgetContainer = (ConstraintWidgetContainer)this;
    } else {
      constraintWidgetContainer = null;
    } 
    while (constraintWidget != null) {
      ConstraintWidget constraintWidget1 = constraintWidget.getParent();
      if (constraintWidget instanceof ConstraintWidgetContainer)
        constraintWidgetContainer = (ConstraintWidgetContainer)constraintWidget; 
      constraintWidget = constraintWidget1;
    } 
    return constraintWidgetContainer;
  }
  
  public void layout() {
    updateDrawPosition();
    if (this.mChildren == null)
      return; 
    int i = this.mChildren.size();
    for (byte b = 0; b < i; b++) {
      ConstraintWidget constraintWidget = this.mChildren.get(b);
      if (constraintWidget instanceof WidgetContainer)
        ((WidgetContainer)constraintWidget).layout(); 
    } 
  }
  
  public void remove(ConstraintWidget paramConstraintWidget) {
    this.mChildren.remove(paramConstraintWidget);
    paramConstraintWidget.setParent(null);
  }
  
  public void removeAllChildren() {
    this.mChildren.clear();
  }
  
  public void reset() {
    this.mChildren.clear();
    super.reset();
  }
  
  public void resetSolverVariables(Cache paramCache) {
    super.resetSolverVariables(paramCache);
    int i = this.mChildren.size();
    for (byte b = 0; b < i; b++)
      ((ConstraintWidget)this.mChildren.get(b)).resetSolverVariables(paramCache); 
  }
  
  public void setOffset(int paramInt1, int paramInt2) {
    super.setOffset(paramInt1, paramInt2);
    paramInt2 = this.mChildren.size();
    for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++)
      ((ConstraintWidget)this.mChildren.get(paramInt1)).setOffset(getRootX(), getRootY()); 
  }
  
  public void updateDrawPosition() {
    super.updateDrawPosition();
    if (this.mChildren == null)
      return; 
    int i = this.mChildren.size();
    for (byte b = 0; b < i; b++) {
      ConstraintWidget constraintWidget = this.mChildren.get(b);
      constraintWidget.setOffset(getDrawX(), getDrawY());
      if (!(constraintWidget instanceof ConstraintWidgetContainer))
        constraintWidget.updateDrawPosition(); 
    } 
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\constraint\solver\widgets\WidgetContainer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */