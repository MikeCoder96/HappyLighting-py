package android.support.constraint;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.support.constraint.solver.widgets.Helper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.util.Arrays;

public abstract class ConstraintHelper extends View {
  protected int mCount;
  
  protected Helper mHelperWidget;
  
  protected int[] mIds = new int[32];
  
  private String mReferenceIds;
  
  protected boolean mUseViewMeasure = false;
  
  protected Context myContext;
  
  public ConstraintHelper(Context paramContext) {
    super(paramContext);
    this.myContext = paramContext;
    init((AttributeSet)null);
  }
  
  public ConstraintHelper(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    this.myContext = paramContext;
    init(paramAttributeSet);
  }
  
  public ConstraintHelper(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    this.myContext = paramContext;
    init(paramAttributeSet);
  }
  
  private void addID(String paramString) {
    if (paramString == null)
      return; 
    if (this.myContext == null)
      return; 
    paramString = paramString.trim();
    try {
      i = R.id.class.getField(paramString).getInt(null);
    } catch (Exception exception) {
      i = 0;
    } 
    int j = i;
    if (!i)
      j = this.myContext.getResources().getIdentifier(paramString, "id", this.myContext.getPackageName()); 
    int i = j;
    if (j == 0) {
      i = j;
      if (isInEditMode()) {
        i = j;
        if (getParent() instanceof ConstraintLayout) {
          Object object = ((ConstraintLayout)getParent()).getDesignInformation(0, paramString);
          i = j;
          if (object != null) {
            i = j;
            if (object instanceof Integer)
              i = ((Integer)object).intValue(); 
          } 
        } 
      } 
    } 
    if (i != 0) {
      setTag(i, (Object)null);
    } else {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Could not find id of \"");
      stringBuilder.append(paramString);
      stringBuilder.append("\"");
      Log.w("ConstraintHelper", stringBuilder.toString());
    } 
  }
  
  private void setIds(String paramString) {
    if (paramString == null)
      return; 
    for (int i = 0;; i = j + 1) {
      int j = paramString.indexOf(',', i);
      if (j == -1) {
        addID(paramString.substring(i));
        return;
      } 
      addID(paramString.substring(i, j));
    } 
  }
  
  public int[] getReferencedIds() {
    return Arrays.copyOf(this.mIds, this.mCount);
  }
  
  protected void init(AttributeSet paramAttributeSet) {
    if (paramAttributeSet != null) {
      TypedArray typedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.ConstraintLayout_Layout);
      int i = typedArray.getIndexCount();
      for (byte b = 0; b < i; b++) {
        int j = typedArray.getIndex(b);
        if (j == R.styleable.ConstraintLayout_Layout_constraint_referenced_ids) {
          this.mReferenceIds = typedArray.getString(j);
          setIds(this.mReferenceIds);
        } 
      } 
    } 
  }
  
  public void onDraw(Canvas paramCanvas) {}
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    if (this.mUseViewMeasure) {
      super.onMeasure(paramInt1, paramInt2);
    } else {
      setMeasuredDimension(0, 0);
    } 
  }
  
  public void setReferencedIds(int[] paramArrayOfint) {
    byte b = 0;
    this.mCount = 0;
    while (b < paramArrayOfint.length) {
      setTag(paramArrayOfint[b], (Object)null);
      b++;
    } 
  }
  
  public void setTag(int paramInt, Object paramObject) {
    if (this.mCount + 1 > this.mIds.length)
      this.mIds = Arrays.copyOf(this.mIds, this.mIds.length * 2); 
    this.mIds[this.mCount] = paramInt;
    this.mCount++;
  }
  
  public void updatePostLayout(ConstraintLayout paramConstraintLayout) {}
  
  public void updatePostMeasure(ConstraintLayout paramConstraintLayout) {}
  
  public void updatePreLayout(ConstraintLayout paramConstraintLayout) {
    if (isInEditMode())
      setIds(this.mReferenceIds); 
    if (this.mHelperWidget == null)
      return; 
    this.mHelperWidget.removeAllIds();
    for (byte b = 0; b < this.mCount; b++) {
      View view = paramConstraintLayout.getViewById(this.mIds[b]);
      if (view != null)
        this.mHelperWidget.add(paramConstraintLayout.getViewWidget(view)); 
    } 
  }
  
  public void validateParams() {
    if (this.mHelperWidget == null)
      return; 
    ViewGroup.LayoutParams layoutParams = getLayoutParams();
    if (layoutParams instanceof ConstraintLayout.LayoutParams)
      ((ConstraintLayout.LayoutParams)layoutParams).widget = (ConstraintWidget)this.mHelperWidget; 
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\constraint\ConstraintHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */