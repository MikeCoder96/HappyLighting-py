package android.support.v13.view;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.view.DragAndDropPermissions;
import android.view.DragEvent;

public final class DragAndDropPermissionsCompat {
  private Object mDragAndDropPermissions;
  
  private DragAndDropPermissionsCompat(Object paramObject) {
    this.mDragAndDropPermissions = paramObject;
  }
  
  @Nullable
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static DragAndDropPermissionsCompat request(Activity paramActivity, DragEvent paramDragEvent) {
    if (Build.VERSION.SDK_INT >= 24) {
      DragAndDropPermissions dragAndDropPermissions = paramActivity.requestDragAndDropPermissions(paramDragEvent);
      if (dragAndDropPermissions != null)
        return new DragAndDropPermissionsCompat(dragAndDropPermissions); 
    } 
    return null;
  }
  
  public void release() {
    if (Build.VERSION.SDK_INT >= 24)
      ((DragAndDropPermissions)this.mDragAndDropPermissions).release(); 
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v13\view\DragAndDropPermissionsCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */