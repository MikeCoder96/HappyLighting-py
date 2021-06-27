package android.support.v4.graphics;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.v4.util.Preconditions;

public final class PathSegment {
  private final PointF mEnd;
  
  private final float mEndFraction;
  
  private final PointF mStart;
  
  private final float mStartFraction;
  
  public PathSegment(@NonNull PointF paramPointF1, float paramFloat1, @NonNull PointF paramPointF2, float paramFloat2) {
    this.mStart = (PointF)Preconditions.checkNotNull(paramPointF1, "start == null");
    this.mStartFraction = paramFloat1;
    this.mEnd = (PointF)Preconditions.checkNotNull(paramPointF2, "end == null");
    this.mEndFraction = paramFloat2;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = true;
    if (this == paramObject)
      return true; 
    if (!(paramObject instanceof PathSegment))
      return false; 
    paramObject = paramObject;
    if (Float.compare(this.mStartFraction, ((PathSegment)paramObject).mStartFraction) != 0 || Float.compare(this.mEndFraction, ((PathSegment)paramObject).mEndFraction) != 0 || !this.mStart.equals(((PathSegment)paramObject).mStart) || !this.mEnd.equals(((PathSegment)paramObject).mEnd))
      bool = false; 
    return bool;
  }
  
  @NonNull
  public PointF getEnd() {
    return this.mEnd;
  }
  
  public float getEndFraction() {
    return this.mEndFraction;
  }
  
  @NonNull
  public PointF getStart() {
    return this.mStart;
  }
  
  public float getStartFraction() {
    return this.mStartFraction;
  }
  
  public int hashCode() {
    byte b;
    int i = this.mStart.hashCode();
    float f = this.mStartFraction;
    int j = 0;
    if (f != 0.0F) {
      b = Float.floatToIntBits(this.mStartFraction);
    } else {
      b = 0;
    } 
    int k = this.mEnd.hashCode();
    if (this.mEndFraction != 0.0F)
      j = Float.floatToIntBits(this.mEndFraction); 
    return ((i * 31 + b) * 31 + k) * 31 + j;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("PathSegment{start=");
    stringBuilder.append(this.mStart);
    stringBuilder.append(", startFraction=");
    stringBuilder.append(this.mStartFraction);
    stringBuilder.append(", end=");
    stringBuilder.append(this.mEnd);
    stringBuilder.append(", endFraction=");
    stringBuilder.append(this.mEndFraction);
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\graphics\PathSegment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */