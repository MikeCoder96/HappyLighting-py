package androidx.core.graphics.drawable;

import android.content.res.ColorStateList;
import android.os.Parcelable;
import android.support.annotation.RestrictTo;
import android.support.v4.graphics.drawable.IconCompat;
import androidx.versionedparcelable.VersionedParcel;

@RestrictTo({RestrictTo.Scope.LIBRARY})
public class IconCompatParcelizer {
  public static IconCompat read(VersionedParcel paramVersionedParcel) {
    IconCompat iconCompat = new IconCompat();
    iconCompat.mType = paramVersionedParcel.readInt(iconCompat.mType, 1);
    iconCompat.mData = paramVersionedParcel.readByteArray(iconCompat.mData, 2);
    iconCompat.mParcelable = paramVersionedParcel.readParcelable(iconCompat.mParcelable, 3);
    iconCompat.mInt1 = paramVersionedParcel.readInt(iconCompat.mInt1, 4);
    iconCompat.mInt2 = paramVersionedParcel.readInt(iconCompat.mInt2, 5);
    iconCompat.mTintList = (ColorStateList)paramVersionedParcel.readParcelable((Parcelable)iconCompat.mTintList, 6);
    iconCompat.mTintModeStr = paramVersionedParcel.readString(iconCompat.mTintModeStr, 7);
    iconCompat.onPostParceling();
    return iconCompat;
  }
  
  public static void write(IconCompat paramIconCompat, VersionedParcel paramVersionedParcel) {
    paramVersionedParcel.setSerializationFlags(true, true);
    paramIconCompat.onPreParceling(paramVersionedParcel.isStream());
    paramVersionedParcel.writeInt(paramIconCompat.mType, 1);
    paramVersionedParcel.writeByteArray(paramIconCompat.mData, 2);
    paramVersionedParcel.writeParcelable(paramIconCompat.mParcelable, 3);
    paramVersionedParcel.writeInt(paramIconCompat.mInt1, 4);
    paramVersionedParcel.writeInt(paramIconCompat.mInt2, 5);
    paramVersionedParcel.writeParcelable((Parcelable)paramIconCompat.mTintList, 6);
    paramVersionedParcel.writeString(paramIconCompat.mTintModeStr, 7);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\androidx\core\graphics\drawable\IconCompatParcelizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */