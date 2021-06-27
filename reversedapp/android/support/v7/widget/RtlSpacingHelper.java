package android.support.v7.widget;

class RtlSpacingHelper {
  public static final int UNDEFINED = -2147483648;
  
  private int mEnd = Integer.MIN_VALUE;
  
  private int mExplicitLeft = 0;
  
  private int mExplicitRight = 0;
  
  private boolean mIsRelative = false;
  
  private boolean mIsRtl = false;
  
  private int mLeft = 0;
  
  private int mRight = 0;
  
  private int mStart = Integer.MIN_VALUE;
  
  public int getEnd() {
    int i;
    if (this.mIsRtl) {
      i = this.mLeft;
    } else {
      i = this.mRight;
    } 
    return i;
  }
  
  public int getLeft() {
    return this.mLeft;
  }
  
  public int getRight() {
    return this.mRight;
  }
  
  public int getStart() {
    int i;
    if (this.mIsRtl) {
      i = this.mRight;
    } else {
      i = this.mLeft;
    } 
    return i;
  }
  
  public void setAbsolute(int paramInt1, int paramInt2) {
    this.mIsRelative = false;
    if (paramInt1 != Integer.MIN_VALUE) {
      this.mExplicitLeft = paramInt1;
      this.mLeft = paramInt1;
    } 
    if (paramInt2 != Integer.MIN_VALUE) {
      this.mExplicitRight = paramInt2;
      this.mRight = paramInt2;
    } 
  }
  
  public void setDirection(boolean paramBoolean) {
    if (paramBoolean == this.mIsRtl)
      return; 
    this.mIsRtl = paramBoolean;
    if (this.mIsRelative) {
      if (paramBoolean) {
        int i;
        if (this.mEnd != Integer.MIN_VALUE) {
          i = this.mEnd;
        } else {
          i = this.mExplicitLeft;
        } 
        this.mLeft = i;
        if (this.mStart != Integer.MIN_VALUE) {
          i = this.mStart;
        } else {
          i = this.mExplicitRight;
        } 
        this.mRight = i;
      } else {
        int i;
        if (this.mStart != Integer.MIN_VALUE) {
          i = this.mStart;
        } else {
          i = this.mExplicitLeft;
        } 
        this.mLeft = i;
        if (this.mEnd != Integer.MIN_VALUE) {
          i = this.mEnd;
        } else {
          i = this.mExplicitRight;
        } 
        this.mRight = i;
      } 
    } else {
      this.mLeft = this.mExplicitLeft;
      this.mRight = this.mExplicitRight;
    } 
  }
  
  public void setRelative(int paramInt1, int paramInt2) {
    this.mStart = paramInt1;
    this.mEnd = paramInt2;
    this.mIsRelative = true;
    if (this.mIsRtl) {
      if (paramInt2 != Integer.MIN_VALUE)
        this.mLeft = paramInt2; 
      if (paramInt1 != Integer.MIN_VALUE)
        this.mRight = paramInt1; 
    } else {
      if (paramInt1 != Integer.MIN_VALUE)
        this.mLeft = paramInt1; 
      if (paramInt2 != Integer.MIN_VALUE)
        this.mRight = paramInt2; 
    } 
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v7\widget\RtlSpacingHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */