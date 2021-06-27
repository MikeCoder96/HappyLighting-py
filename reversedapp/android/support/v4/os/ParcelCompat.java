package android.support.v4.os;

import android.os.Parcel;

public final class ParcelCompat {
  public static boolean readBoolean(Parcel paramParcel) {
    boolean bool;
    if (paramParcel.readInt() != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static void writeBoolean(Parcel paramParcel, boolean paramBoolean) {
    paramParcel.writeInt(paramBoolean);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\os\ParcelCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */