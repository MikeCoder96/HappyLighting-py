package android.support.v4.media;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.text.TextUtils;

public final class MediaDescriptionCompat implements Parcelable {
  public static final long BT_FOLDER_TYPE_ALBUMS = 2L;
  
  public static final long BT_FOLDER_TYPE_ARTISTS = 3L;
  
  public static final long BT_FOLDER_TYPE_GENRES = 4L;
  
  public static final long BT_FOLDER_TYPE_MIXED = 0L;
  
  public static final long BT_FOLDER_TYPE_PLAYLISTS = 5L;
  
  public static final long BT_FOLDER_TYPE_TITLES = 1L;
  
  public static final long BT_FOLDER_TYPE_YEARS = 6L;
  
  public static final Parcelable.Creator<MediaDescriptionCompat> CREATOR = new Parcelable.Creator<MediaDescriptionCompat>() {
      public MediaDescriptionCompat createFromParcel(Parcel param1Parcel) {
        return (Build.VERSION.SDK_INT < 21) ? new MediaDescriptionCompat(param1Parcel) : MediaDescriptionCompat.fromMediaDescription(MediaDescriptionCompatApi21.fromParcel(param1Parcel));
      }
      
      public MediaDescriptionCompat[] newArray(int param1Int) {
        return new MediaDescriptionCompat[param1Int];
      }
    };
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static final String DESCRIPTION_KEY_MEDIA_URI = "android.support.v4.media.description.MEDIA_URI";
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static final String DESCRIPTION_KEY_NULL_BUNDLE_FLAG = "android.support.v4.media.description.NULL_BUNDLE_FLAG";
  
  public static final String EXTRA_BT_FOLDER_TYPE = "android.media.extra.BT_FOLDER_TYPE";
  
  private final CharSequence mDescription;
  
  private Object mDescriptionObj;
  
  private final Bundle mExtras;
  
  private final Bitmap mIcon;
  
  private final Uri mIconUri;
  
  private final String mMediaId;
  
  private final Uri mMediaUri;
  
  private final CharSequence mSubtitle;
  
  private final CharSequence mTitle;
  
  MediaDescriptionCompat(Parcel paramParcel) {
    this.mMediaId = paramParcel.readString();
    this.mTitle = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel);
    this.mSubtitle = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel);
    this.mDescription = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel);
    this.mIcon = (Bitmap)paramParcel.readParcelable(null);
    this.mIconUri = (Uri)paramParcel.readParcelable(null);
    this.mExtras = paramParcel.readBundle();
    this.mMediaUri = (Uri)paramParcel.readParcelable(null);
  }
  
  MediaDescriptionCompat(String paramString, CharSequence paramCharSequence1, CharSequence paramCharSequence2, CharSequence paramCharSequence3, Bitmap paramBitmap, Uri paramUri1, Bundle paramBundle, Uri paramUri2) {
    this.mMediaId = paramString;
    this.mTitle = paramCharSequence1;
    this.mSubtitle = paramCharSequence2;
    this.mDescription = paramCharSequence3;
    this.mIcon = paramBitmap;
    this.mIconUri = paramUri1;
    this.mExtras = paramBundle;
    this.mMediaUri = paramUri2;
  }
  
  public static MediaDescriptionCompat fromMediaDescription(Object paramObject) {
    Uri uri;
    Bundle bundle1 = null;
    if (paramObject == null || Build.VERSION.SDK_INT < 21)
      return null; 
    Builder builder = new Builder();
    builder.setMediaId(MediaDescriptionCompatApi21.getMediaId(paramObject));
    builder.setTitle(MediaDescriptionCompatApi21.getTitle(paramObject));
    builder.setSubtitle(MediaDescriptionCompatApi21.getSubtitle(paramObject));
    builder.setDescription(MediaDescriptionCompatApi21.getDescription(paramObject));
    builder.setIconBitmap(MediaDescriptionCompatApi21.getIconBitmap(paramObject));
    builder.setIconUri(MediaDescriptionCompatApi21.getIconUri(paramObject));
    Bundle bundle2 = MediaDescriptionCompatApi21.getExtras(paramObject);
    if (bundle2 == null) {
      uri = null;
    } else {
      uri = (Uri)bundle2.getParcelable("android.support.v4.media.description.MEDIA_URI");
    } 
    if (uri != null)
      if (bundle2.containsKey("android.support.v4.media.description.NULL_BUNDLE_FLAG") && bundle2.size() == 2) {
        bundle2 = bundle1;
      } else {
        bundle2.remove("android.support.v4.media.description.MEDIA_URI");
        bundle2.remove("android.support.v4.media.description.NULL_BUNDLE_FLAG");
      }  
    builder.setExtras(bundle2);
    if (uri != null) {
      builder.setMediaUri(uri);
    } else if (Build.VERSION.SDK_INT >= 23) {
      builder.setMediaUri(MediaDescriptionCompatApi23.getMediaUri(paramObject));
    } 
    MediaDescriptionCompat mediaDescriptionCompat = builder.build();
    mediaDescriptionCompat.mDescriptionObj = paramObject;
    return mediaDescriptionCompat;
  }
  
  public int describeContents() {
    return 0;
  }
  
  @Nullable
  public CharSequence getDescription() {
    return this.mDescription;
  }
  
  @Nullable
  public Bundle getExtras() {
    return this.mExtras;
  }
  
  @Nullable
  public Bitmap getIconBitmap() {
    return this.mIcon;
  }
  
  @Nullable
  public Uri getIconUri() {
    return this.mIconUri;
  }
  
  public Object getMediaDescription() {
    if (this.mDescriptionObj != null || Build.VERSION.SDK_INT < 21)
      return this.mDescriptionObj; 
    Object object = MediaDescriptionCompatApi21.Builder.newInstance();
    MediaDescriptionCompatApi21.Builder.setMediaId(object, this.mMediaId);
    MediaDescriptionCompatApi21.Builder.setTitle(object, this.mTitle);
    MediaDescriptionCompatApi21.Builder.setSubtitle(object, this.mSubtitle);
    MediaDescriptionCompatApi21.Builder.setDescription(object, this.mDescription);
    MediaDescriptionCompatApi21.Builder.setIconBitmap(object, this.mIcon);
    MediaDescriptionCompatApi21.Builder.setIconUri(object, this.mIconUri);
    Bundle bundle1 = this.mExtras;
    Bundle bundle2 = bundle1;
    if (Build.VERSION.SDK_INT < 23) {
      bundle2 = bundle1;
      if (this.mMediaUri != null) {
        bundle2 = bundle1;
        if (bundle1 == null) {
          bundle2 = new Bundle();
          bundle2.putBoolean("android.support.v4.media.description.NULL_BUNDLE_FLAG", true);
        } 
        bundle2.putParcelable("android.support.v4.media.description.MEDIA_URI", (Parcelable)this.mMediaUri);
      } 
    } 
    MediaDescriptionCompatApi21.Builder.setExtras(object, bundle2);
    if (Build.VERSION.SDK_INT >= 23)
      MediaDescriptionCompatApi23.Builder.setMediaUri(object, this.mMediaUri); 
    this.mDescriptionObj = MediaDescriptionCompatApi21.Builder.build(object);
    return this.mDescriptionObj;
  }
  
  @Nullable
  public String getMediaId() {
    return this.mMediaId;
  }
  
  @Nullable
  public Uri getMediaUri() {
    return this.mMediaUri;
  }
  
  @Nullable
  public CharSequence getSubtitle() {
    return this.mSubtitle;
  }
  
  @Nullable
  public CharSequence getTitle() {
    return this.mTitle;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.mTitle);
    stringBuilder.append(", ");
    stringBuilder.append(this.mSubtitle);
    stringBuilder.append(", ");
    stringBuilder.append(this.mDescription);
    return stringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    if (Build.VERSION.SDK_INT < 21) {
      paramParcel.writeString(this.mMediaId);
      TextUtils.writeToParcel(this.mTitle, paramParcel, paramInt);
      TextUtils.writeToParcel(this.mSubtitle, paramParcel, paramInt);
      TextUtils.writeToParcel(this.mDescription, paramParcel, paramInt);
      paramParcel.writeParcelable((Parcelable)this.mIcon, paramInt);
      paramParcel.writeParcelable((Parcelable)this.mIconUri, paramInt);
      paramParcel.writeBundle(this.mExtras);
      paramParcel.writeParcelable((Parcelable)this.mMediaUri, paramInt);
    } else {
      MediaDescriptionCompatApi21.writeToParcel(getMediaDescription(), paramParcel, paramInt);
    } 
  }
  
  public static final class Builder {
    private CharSequence mDescription;
    
    private Bundle mExtras;
    
    private Bitmap mIcon;
    
    private Uri mIconUri;
    
    private String mMediaId;
    
    private Uri mMediaUri;
    
    private CharSequence mSubtitle;
    
    private CharSequence mTitle;
    
    public MediaDescriptionCompat build() {
      return new MediaDescriptionCompat(this.mMediaId, this.mTitle, this.mSubtitle, this.mDescription, this.mIcon, this.mIconUri, this.mExtras, this.mMediaUri);
    }
    
    public Builder setDescription(@Nullable CharSequence param1CharSequence) {
      this.mDescription = param1CharSequence;
      return this;
    }
    
    public Builder setExtras(@Nullable Bundle param1Bundle) {
      this.mExtras = param1Bundle;
      return this;
    }
    
    public Builder setIconBitmap(@Nullable Bitmap param1Bitmap) {
      this.mIcon = param1Bitmap;
      return this;
    }
    
    public Builder setIconUri(@Nullable Uri param1Uri) {
      this.mIconUri = param1Uri;
      return this;
    }
    
    public Builder setMediaId(@Nullable String param1String) {
      this.mMediaId = param1String;
      return this;
    }
    
    public Builder setMediaUri(@Nullable Uri param1Uri) {
      this.mMediaUri = param1Uri;
      return this;
    }
    
    public Builder setSubtitle(@Nullable CharSequence param1CharSequence) {
      this.mSubtitle = param1CharSequence;
      return this;
    }
    
    public Builder setTitle(@Nullable CharSequence param1CharSequence) {
      this.mTitle = param1CharSequence;
      return this;
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\media\MediaDescriptionCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */