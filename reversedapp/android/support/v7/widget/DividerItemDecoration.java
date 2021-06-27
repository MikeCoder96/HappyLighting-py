package android.support.v7.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
  private static final int[] ATTRS = new int[] { 16843284 };
  
  public static final int HORIZONTAL = 0;
  
  public static final int VERTICAL = 1;
  
  private final Rect mBounds = new Rect();
  
  private Drawable mDivider;
  
  private int mOrientation;
  
  public DividerItemDecoration(Context paramContext, int paramInt) {
    TypedArray typedArray = paramContext.obtainStyledAttributes(ATTRS);
    this.mDivider = typedArray.getDrawable(0);
    typedArray.recycle();
    setOrientation(paramInt);
  }
  
  @SuppressLint({"NewApi"})
  private void drawHorizontal(Canvas paramCanvas, RecyclerView paramRecyclerView) {
    boolean bool1;
    int i;
    paramCanvas.save();
    boolean bool = paramRecyclerView.getClipToPadding();
    byte b = 0;
    if (bool) {
      bool1 = paramRecyclerView.getPaddingTop();
      i = paramRecyclerView.getHeight() - paramRecyclerView.getPaddingBottom();
      paramCanvas.clipRect(paramRecyclerView.getPaddingLeft(), bool1, paramRecyclerView.getWidth() - paramRecyclerView.getPaddingRight(), i);
    } else {
      i = paramRecyclerView.getHeight();
      bool1 = false;
    } 
    int j = paramRecyclerView.getChildCount();
    while (b < j) {
      View view = paramRecyclerView.getChildAt(b);
      paramRecyclerView.getLayoutManager().getDecoratedBoundsWithMargins(view, this.mBounds);
      int k = this.mBounds.right + Math.round(view.getTranslationX());
      int m = this.mDivider.getIntrinsicWidth();
      this.mDivider.setBounds(k - m, bool1, k, i);
      this.mDivider.draw(paramCanvas);
      b++;
    } 
    paramCanvas.restore();
  }
  
  @SuppressLint({"NewApi"})
  private void drawVertical(Canvas paramCanvas, RecyclerView paramRecyclerView) {
    boolean bool1;
    int i;
    paramCanvas.save();
    boolean bool = paramRecyclerView.getClipToPadding();
    byte b = 0;
    if (bool) {
      bool1 = paramRecyclerView.getPaddingLeft();
      i = paramRecyclerView.getWidth() - paramRecyclerView.getPaddingRight();
      paramCanvas.clipRect(bool1, paramRecyclerView.getPaddingTop(), i, paramRecyclerView.getHeight() - paramRecyclerView.getPaddingBottom());
    } else {
      i = paramRecyclerView.getWidth();
      bool1 = false;
    } 
    int j = paramRecyclerView.getChildCount();
    while (b < j) {
      View view = paramRecyclerView.getChildAt(b);
      paramRecyclerView.getDecoratedBoundsWithMargins(view, this.mBounds);
      int k = this.mBounds.bottom + Math.round(view.getTranslationY());
      int m = this.mDivider.getIntrinsicHeight();
      this.mDivider.setBounds(bool1, k - m, i, k);
      this.mDivider.draw(paramCanvas);
      b++;
    } 
    paramCanvas.restore();
  }
  
  public void getItemOffsets(Rect paramRect, View paramView, RecyclerView paramRecyclerView, RecyclerView.State paramState) {
    if (this.mOrientation == 1) {
      paramRect.set(0, 0, 0, this.mDivider.getIntrinsicHeight());
    } else {
      paramRect.set(0, 0, this.mDivider.getIntrinsicWidth(), 0);
    } 
  }
  
  public void onDraw(Canvas paramCanvas, RecyclerView paramRecyclerView, RecyclerView.State paramState) {
    if (paramRecyclerView.getLayoutManager() == null)
      return; 
    if (this.mOrientation == 1) {
      drawVertical(paramCanvas, paramRecyclerView);
    } else {
      drawHorizontal(paramCanvas, paramRecyclerView);
    } 
  }
  
  public void setDrawable(@NonNull Drawable paramDrawable) {
    if (paramDrawable != null) {
      this.mDivider = paramDrawable;
      return;
    } 
    throw new IllegalArgumentException("Drawable cannot be null.");
  }
  
  public void setOrientation(int paramInt) {
    if (paramInt == 0 || paramInt == 1) {
      this.mOrientation = paramInt;
      return;
    } 
    throw new IllegalArgumentException("Invalid orientation. It should be either HORIZONTAL or VERTICAL");
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v7\widget\DividerItemDecoration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */