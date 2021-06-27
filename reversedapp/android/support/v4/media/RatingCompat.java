package android.support.v4.media;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RestrictTo;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class RatingCompat implements Parcelable {
  public static final Parcelable.Creator<RatingCompat> CREATOR = new Parcelable.Creator<RatingCompat>() {
      public RatingCompat createFromParcel(Parcel param1Parcel) {
        return new RatingCompat(param1Parcel.readInt(), param1Parcel.readFloat());
      }
      
      public RatingCompat[] newArray(int param1Int) {
        return new RatingCompat[param1Int];
      }
    };
  
  public static final int RATING_3_STARS = 3;
  
  public static final int RATING_4_STARS = 4;
  
  public static final int RATING_5_STARS = 5;
  
  public static final int RATING_HEART = 1;
  
  public static final int RATING_NONE = 0;
  
  private static final float RATING_NOT_RATED = -1.0F;
  
  public static final int RATING_PERCENTAGE = 6;
  
  public static final int RATING_THUMB_UP_DOWN = 2;
  
  private static final String TAG = "Rating";
  
  private Object mRatingObj;
  
  private final int mRatingStyle;
  
  private final float mRatingValue;
  
  RatingCompat(int paramInt, float paramFloat) {
    this.mRatingStyle = paramInt;
    this.mRatingValue = paramFloat;
  }
  
  public static RatingCompat fromRating(Object paramObject) {
    RatingCompat ratingCompat;
    if (paramObject == null || Build.VERSION.SDK_INT < 19)
      return null; 
    int i = RatingCompatKitkat.getRatingStyle(paramObject);
    if (RatingCompatKitkat.isRated(paramObject)) {
      switch (i) {
        default:
          return null;
        case 6:
          ratingCompat = newPercentageRating(RatingCompatKitkat.getPercentRating(paramObject));
          ratingCompat.mRatingObj = paramObject;
          return ratingCompat;
        case 3:
        case 4:
        case 5:
          ratingCompat = newStarRating(i, RatingCompatKitkat.getStarRating(paramObject));
          ratingCompat.mRatingObj = paramObject;
          return ratingCompat;
        case 2:
          ratingCompat = newThumbRating(RatingCompatKitkat.isThumbUp(paramObject));
          ratingCompat.mRatingObj = paramObject;
          return ratingCompat;
        case 1:
          break;
      } 
      ratingCompat = newHeartRating(RatingCompatKitkat.hasHeart(paramObject));
    } else {
      ratingCompat = newUnratedRating(i);
    } 
    ratingCompat.mRatingObj = paramObject;
    return ratingCompat;
  }
  
  public static RatingCompat newHeartRating(boolean paramBoolean) {
    float f;
    if (paramBoolean) {
      f = 1.0F;
    } else {
      f = 0.0F;
    } 
    return new RatingCompat(1, f);
  }
  
  public static RatingCompat newPercentageRating(float paramFloat) {
    if (paramFloat < 0.0F || paramFloat > 100.0F) {
      Log.e("Rating", "Invalid percentage-based rating value");
      return null;
    } 
    return new RatingCompat(6, paramFloat);
  }
  
  public static RatingCompat newStarRating(int paramInt, float paramFloat) {
    StringBuilder stringBuilder;
    float f;
    switch (paramInt) {
      default:
        stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid rating style (");
        stringBuilder.append(paramInt);
        stringBuilder.append(") for a star rating");
        Log.e("Rating", stringBuilder.toString());
        return null;
      case 5:
        f = 5.0F;
        break;
      case 4:
        f = 4.0F;
        break;
      case 3:
        f = 3.0F;
        break;
    } 
    if (paramFloat < 0.0F || paramFloat > f) {
      Log.e("Rating", "Trying to set out of range star-based rating");
      return null;
    } 
    return new RatingCompat(paramInt, paramFloat);
  }
  
  public static RatingCompat newThumbRating(boolean paramBoolean) {
    float f;
    if (paramBoolean) {
      f = 1.0F;
    } else {
      f = 0.0F;
    } 
    return new RatingCompat(2, f);
  }
  
  public static RatingCompat newUnratedRating(int paramInt) {
    switch (paramInt) {
      default:
        return null;
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
        break;
    } 
    return new RatingCompat(paramInt, -1.0F);
  }
  
  public int describeContents() {
    return this.mRatingStyle;
  }
  
  public float getPercentRating() {
    return (this.mRatingStyle != 6 || !isRated()) ? -1.0F : this.mRatingValue;
  }
  
  public Object getRating() {
    if (this.mRatingObj != null || Build.VERSION.SDK_INT < 19)
      return this.mRatingObj; 
    if (isRated()) {
      switch (this.mRatingStyle) {
        default:
          return null;
        case 6:
          this.mRatingObj = RatingCompatKitkat.newPercentageRating(getPercentRating());
        case 3:
        case 4:
        case 5:
          this.mRatingObj = RatingCompatKitkat.newStarRating(this.mRatingStyle, getStarRating());
          return this.mRatingObj;
        case 2:
          this.mRatingObj = RatingCompatKitkat.newThumbRating(isThumbUp());
          return this.mRatingObj;
        case 1:
          break;
      } 
      this.mRatingObj = RatingCompatKitkat.newHeartRating(hasHeart());
    } else {
      this.mRatingObj = RatingCompatKitkat.newUnratedRating(this.mRatingStyle);
    } 
    return this.mRatingObj;
  }
  
  public int getRatingStyle() {
    return this.mRatingStyle;
  }
  
  public float getStarRating() {
    switch (this.mRatingStyle) {
      default:
        return -1.0F;
      case 3:
      case 4:
      case 5:
        break;
    } 
    if (isRated())
      return this.mRatingValue; 
  }
  
  public boolean hasHeart() {
    int i = this.mRatingStyle;
    boolean bool = false;
    if (i != 1)
      return false; 
    if (this.mRatingValue == 1.0F)
      bool = true; 
    return bool;
  }
  
  public boolean isRated() {
    boolean bool;
    if (this.mRatingValue >= 0.0F) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isThumbUp() {
    int i = this.mRatingStyle;
    boolean bool = false;
    if (i != 2)
      return false; 
    if (this.mRatingValue == 1.0F)
      bool = true; 
    return bool;
  }
  
  public String toString() {
    String str;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Rating:style=");
    stringBuilder.append(this.mRatingStyle);
    stringBuilder.append(" rating=");
    if (this.mRatingValue < 0.0F) {
      str = "unrated";
    } else {
      str = String.valueOf(this.mRatingValue);
    } 
    stringBuilder.append(str);
    return stringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    paramParcel.writeInt(this.mRatingStyle);
    paramParcel.writeFloat(this.mRatingValue);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface StarStyle {}
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface Style {}
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\media\RatingCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */