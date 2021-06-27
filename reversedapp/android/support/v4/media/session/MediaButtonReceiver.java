package android.support.v4.media.session;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import java.util.List;

public class MediaButtonReceiver extends BroadcastReceiver {
  private static final String TAG = "MediaButtonReceiver";
  
  public static PendingIntent buildMediaButtonPendingIntent(Context paramContext, long paramLong) {
    ComponentName componentName = getMediaButtonReceiverComponent(paramContext);
    if (componentName == null) {
      Log.w("MediaButtonReceiver", "A unique media button receiver could not be found in the given context, so couldn't build a pending intent.");
      return null;
    } 
    return buildMediaButtonPendingIntent(paramContext, componentName, paramLong);
  }
  
  public static PendingIntent buildMediaButtonPendingIntent(Context paramContext, ComponentName paramComponentName, long paramLong) {
    StringBuilder stringBuilder;
    if (paramComponentName == null) {
      Log.w("MediaButtonReceiver", "The component name of media button receiver should be provided.");
      return null;
    } 
    int i = PlaybackStateCompat.toKeyCode(paramLong);
    if (i == 0) {
      stringBuilder = new StringBuilder();
      stringBuilder.append("Cannot build a media button pending intent with the given action: ");
      stringBuilder.append(paramLong);
      Log.w("MediaButtonReceiver", stringBuilder.toString());
      return null;
    } 
    Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
    intent.setComponent(paramComponentName);
    intent.putExtra("android.intent.extra.KEY_EVENT", (Parcelable)new KeyEvent(0, i));
    return PendingIntent.getBroadcast((Context)stringBuilder, i, intent, 0);
  }
  
  static ComponentName getMediaButtonReceiverComponent(Context paramContext) {
    ResolveInfo resolveInfo;
    Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
    intent.setPackage(paramContext.getPackageName());
    List<ResolveInfo> list = paramContext.getPackageManager().queryBroadcastReceivers(intent, 0);
    if (list.size() == 1) {
      resolveInfo = list.get(0);
      return new ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
    } 
    if (resolveInfo.size() > 1)
      Log.w("MediaButtonReceiver", "More than one BroadcastReceiver that handles android.intent.action.MEDIA_BUTTON was found, returning null."); 
    return null;
  }
  
  public static KeyEvent handleIntent(MediaSessionCompat paramMediaSessionCompat, Intent paramIntent) {
    if (paramMediaSessionCompat == null || paramIntent == null || !"android.intent.action.MEDIA_BUTTON".equals(paramIntent.getAction()) || !paramIntent.hasExtra("android.intent.extra.KEY_EVENT"))
      return null; 
    KeyEvent keyEvent = (KeyEvent)paramIntent.getParcelableExtra("android.intent.extra.KEY_EVENT");
    paramMediaSessionCompat.getController().dispatchMediaButtonEvent(keyEvent);
    return keyEvent;
  }
  
  public void onReceive(Context paramContext, Intent paramIntent) {
    Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
    intent.setPackage(paramContext.getPackageName());
    PackageManager packageManager = paramContext.getPackageManager();
    List<ResolveInfo> list1 = packageManager.queryIntentServices(intent, 0);
    List<ResolveInfo> list2 = list1;
    if (list1.isEmpty()) {
      intent.setAction("android.media.browse.MediaBrowserService");
      list2 = packageManager.queryIntentServices(intent, 0);
    } 
    if (!list2.isEmpty()) {
      ResolveInfo resolveInfo;
      if (list2.size() == 1) {
        resolveInfo = list2.get(0);
        paramIntent.setComponent(new ComponentName(resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name));
        paramContext.startService(paramIntent);
        return;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Expected 1 Service that handles ");
      stringBuilder.append(intent.getAction());
      stringBuilder.append(", found ");
      stringBuilder.append(resolveInfo.size());
      throw new IllegalStateException(stringBuilder.toString());
    } 
    throw new IllegalStateException("Could not find any Service that handles android.intent.action.MEDIA_BUTTON or a media browser service implementation");
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\media\session\MediaButtonReceiver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */