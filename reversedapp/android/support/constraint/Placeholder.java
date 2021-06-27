package android.support.constraint;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class Placeholder extends View {
  private View mContent = null;
  
  private int mContentId = -1;
  
  private int mEmptyVisibility = 4;
  
  public Placeholder(Context paramContext) {
    super(paramContext);
    init((AttributeSet)null);
  }
  
  public Placeholder(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramAttributeSet);
  }
  
  public Placeholder(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramAttributeSet);
  }
  
  public Placeholder(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    super(paramContext, paramAttributeSet, paramInt1);
    init(paramAttributeSet);
  }
  
  private void init(AttributeSet paramAttributeSet) {
    setVisibility(this.mEmptyVisibility);
    this.mContentId = -1;
    if (paramAttributeSet != null) {
      TypedArray typedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.ConstraintLayout_placeholder);
      int i = typedArray.getIndexCount();
      for (byte b = 0; b < i; b++) {
        int j = typedArray.getIndex(b);
        if (j == R.styleable.ConstraintLayout_placeholder_content) {
          this.mContentId = typedArray.getResourceId(j, this.mContentId);
        } else if (j == R.styleable.ConstraintLayout_placeholder_emptyVisibility) {
          this.mEmptyVisibility = typedArray.getInt(j, this.mEmptyVisibility);
        } 
      } 
    } 
  }
  
  public View getContent() {
    return this.mContent;
  }
  
  public int getEmptyVisibility() {
    return this.mEmptyVisibility;
  }
  
  public void onDraw(Canvas paramCanvas) {
    if (isInEditMode()) {
      paramCanvas.drawRGB(223, 223, 223);
      Paint paint = new Paint();
      paint.setARGB(255, 210, 210, 210);
      paint.setTextAlign(Paint.Align.CENTER);
      paint.setTypeface(Typeface.create(Typeface.DEFAULT, 0));
      Rect rect = new Rect();
      paramCanvas.getClipBounds(rect);
      paint.setTextSize(rect.height());
      int i = rect.height();
      int j = rect.width();
      paint.setTextAlign(Paint.Align.LEFT);
      paint.getTextBounds("?", 0, "?".length(), rect);
      paramCanvas.drawText("?", j / 2.0F - rect.width() / 2.0F - rect.left, i / 2.0F + rect.height() / 2.0F - rect.bottom, paint);
    } 
  }
  
  public void setContentId(int paramInt) {
    if (this.mContentId == paramInt)
      return; 
    if (this.mContent != null) {
      this.mContent.setVisibility(0);
      ((ConstraintLayout.LayoutParams)this.mContent.getLayoutParams()).isInPlaceholder = false;
      this.mContent = null;
    } 
    this.mContentId = paramInt;
    if (paramInt != -1) {
      View view = ((View)getParent()).findViewById(paramInt);
      if (view != null)
        view.setVisibility(8); 
    } 
  }
  
  public void setEmptyVisibility(int paramInt) {
    this.mEmptyVisibility = paramInt;
  }
  
  public void updatePostMeasure(ConstraintLayout paramConstraintLayout) {
    if (this.mContent == null)
      return; 
    ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams)getLayoutParams();
    ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams)this.mContent.getLayoutParams();
    layoutParams1.widget.setVisibility(0);
    layoutParams2.widget.setWidth(layoutParams1.widget.getWidth());
    layoutParams2.widget.setHeight(layoutParams1.widget.getHeight());
    layoutParams1.widget.setVisibility(8);
  }
  
  public void updatePreLayout(ConstraintLayout paramConstraintLayout) {
    if (this.mContentId == -1 && !isInEditMode())
      setVisibility(this.mEmptyVisibility); 
    this.mContent = paramConstraintLayout.findViewById(this.mContentId);
    if (this.mContent != null) {
      ((ConstraintLayout.LayoutParams)this.mContent.getLayoutParams()).isInPlaceholder = true;
      this.mContent.setVisibility(0);
      setVisibility(0);
    } 
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\constraint\Placeholder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */