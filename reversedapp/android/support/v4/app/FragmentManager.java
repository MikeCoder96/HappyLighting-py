package android.support.v4.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;
import android.view.View;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

public abstract class FragmentManager {
  public static final int POP_BACK_STACK_INCLUSIVE = 1;
  
  public static void enableDebugLogging(boolean paramBoolean) {
    FragmentManagerImpl.DEBUG = paramBoolean;
  }
  
  public abstract void addOnBackStackChangedListener(@NonNull OnBackStackChangedListener paramOnBackStackChangedListener);
  
  @NonNull
  public abstract FragmentTransaction beginTransaction();
  
  public abstract void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString);
  
  public abstract boolean executePendingTransactions();
  
  @Nullable
  public abstract Fragment findFragmentById(@IdRes int paramInt);
  
  @Nullable
  public abstract Fragment findFragmentByTag(@Nullable String paramString);
  
  @NonNull
  public abstract BackStackEntry getBackStackEntryAt(int paramInt);
  
  public abstract int getBackStackEntryCount();
  
  @Nullable
  public abstract Fragment getFragment(@NonNull Bundle paramBundle, @NonNull String paramString);
  
  @NonNull
  public abstract List<Fragment> getFragments();
  
  @Nullable
  public abstract Fragment getPrimaryNavigationFragment();
  
  public abstract boolean isDestroyed();
  
  public abstract boolean isStateSaved();
  
  @Deprecated
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public FragmentTransaction openTransaction() {
    return beginTransaction();
  }
  
  public abstract void popBackStack();
  
  public abstract void popBackStack(int paramInt1, int paramInt2);
  
  public abstract void popBackStack(@Nullable String paramString, int paramInt);
  
  public abstract boolean popBackStackImmediate();
  
  public abstract boolean popBackStackImmediate(int paramInt1, int paramInt2);
  
  public abstract boolean popBackStackImmediate(@Nullable String paramString, int paramInt);
  
  public abstract void putFragment(@NonNull Bundle paramBundle, @NonNull String paramString, @NonNull Fragment paramFragment);
  
  public abstract void registerFragmentLifecycleCallbacks(@NonNull FragmentLifecycleCallbacks paramFragmentLifecycleCallbacks, boolean paramBoolean);
  
  public abstract void removeOnBackStackChangedListener(@NonNull OnBackStackChangedListener paramOnBackStackChangedListener);
  
  @Nullable
  public abstract Fragment.SavedState saveFragmentInstanceState(Fragment paramFragment);
  
  public abstract void unregisterFragmentLifecycleCallbacks(@NonNull FragmentLifecycleCallbacks paramFragmentLifecycleCallbacks);
  
  public static interface BackStackEntry {
    @Nullable
    CharSequence getBreadCrumbShortTitle();
    
    @StringRes
    int getBreadCrumbShortTitleRes();
    
    @Nullable
    CharSequence getBreadCrumbTitle();
    
    @StringRes
    int getBreadCrumbTitleRes();
    
    int getId();
    
    @Nullable
    String getName();
  }
  
  public static abstract class FragmentLifecycleCallbacks {
    public void onFragmentActivityCreated(@NonNull FragmentManager param1FragmentManager, @NonNull Fragment param1Fragment, @Nullable Bundle param1Bundle) {}
    
    public void onFragmentAttached(@NonNull FragmentManager param1FragmentManager, @NonNull Fragment param1Fragment, @NonNull Context param1Context) {}
    
    public void onFragmentCreated(@NonNull FragmentManager param1FragmentManager, @NonNull Fragment param1Fragment, @Nullable Bundle param1Bundle) {}
    
    public void onFragmentDestroyed(@NonNull FragmentManager param1FragmentManager, @NonNull Fragment param1Fragment) {}
    
    public void onFragmentDetached(@NonNull FragmentManager param1FragmentManager, @NonNull Fragment param1Fragment) {}
    
    public void onFragmentPaused(@NonNull FragmentManager param1FragmentManager, @NonNull Fragment param1Fragment) {}
    
    public void onFragmentPreAttached(@NonNull FragmentManager param1FragmentManager, @NonNull Fragment param1Fragment, @NonNull Context param1Context) {}
    
    public void onFragmentPreCreated(@NonNull FragmentManager param1FragmentManager, @NonNull Fragment param1Fragment, @Nullable Bundle param1Bundle) {}
    
    public void onFragmentResumed(@NonNull FragmentManager param1FragmentManager, @NonNull Fragment param1Fragment) {}
    
    public void onFragmentSaveInstanceState(@NonNull FragmentManager param1FragmentManager, @NonNull Fragment param1Fragment, @NonNull Bundle param1Bundle) {}
    
    public void onFragmentStarted(@NonNull FragmentManager param1FragmentManager, @NonNull Fragment param1Fragment) {}
    
    public void onFragmentStopped(@NonNull FragmentManager param1FragmentManager, @NonNull Fragment param1Fragment) {}
    
    public void onFragmentViewCreated(@NonNull FragmentManager param1FragmentManager, @NonNull Fragment param1Fragment, @NonNull View param1View, @Nullable Bundle param1Bundle) {}
    
    public void onFragmentViewDestroyed(@NonNull FragmentManager param1FragmentManager, @NonNull Fragment param1Fragment) {}
  }
  
  public static interface OnBackStackChangedListener {
    void onBackStackChanged();
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\app\FragmentManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */