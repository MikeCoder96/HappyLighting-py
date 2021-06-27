package android.arch.lifecycle;

import android.app.Application;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import java.lang.reflect.InvocationTargetException;

public class ViewModelProvider {
  private static final String DEFAULT_KEY = "android.arch.lifecycle.ViewModelProvider.DefaultKey";
  
  private final Factory mFactory;
  
  private final ViewModelStore mViewModelStore;
  
  public ViewModelProvider(@NonNull ViewModelStore paramViewModelStore, @NonNull Factory paramFactory) {
    this.mFactory = paramFactory;
    this.mViewModelStore = paramViewModelStore;
  }
  
  public ViewModelProvider(@NonNull ViewModelStoreOwner paramViewModelStoreOwner, @NonNull Factory paramFactory) {
    this(paramViewModelStoreOwner.getViewModelStore(), paramFactory);
  }
  
  @MainThread
  @NonNull
  public <T extends ViewModel> T get(@NonNull Class<T> paramClass) {
    String str = paramClass.getCanonicalName();
    if (str != null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("android.arch.lifecycle.ViewModelProvider.DefaultKey:");
      stringBuilder.append(str);
      return get(stringBuilder.toString(), paramClass);
    } 
    throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
  }
  
  @MainThread
  @NonNull
  public <T extends ViewModel> T get(@NonNull String paramString, @NonNull Class<T> paramClass) {
    ViewModel viewModel = this.mViewModelStore.get(paramString);
    if (paramClass.isInstance(viewModel))
      return (T)viewModel; 
    paramClass = this.mFactory.create((Class)paramClass);
    this.mViewModelStore.put(paramString, (ViewModel)paramClass);
    return (T)paramClass;
  }
  
  public static class AndroidViewModelFactory extends NewInstanceFactory {
    private static AndroidViewModelFactory sInstance;
    
    private Application mApplication;
    
    public AndroidViewModelFactory(@NonNull Application param1Application) {
      this.mApplication = param1Application;
    }
    
    @NonNull
    public static AndroidViewModelFactory getInstance(@NonNull Application param1Application) {
      if (sInstance == null)
        sInstance = new AndroidViewModelFactory(param1Application); 
      return sInstance;
    }
    
    @NonNull
    public <T extends ViewModel> T create(@NonNull Class<T> param1Class) {
      if (AndroidViewModel.class.isAssignableFrom(param1Class))
        try {
          return (T)param1Class.getConstructor(new Class[] { Application.class }).newInstance(new Object[] { this.mApplication });
        } catch (NoSuchMethodException noSuchMethodException) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Cannot create an instance of ");
          stringBuilder.append(param1Class);
          throw new RuntimeException(stringBuilder.toString(), noSuchMethodException);
        } catch (IllegalAccessException illegalAccessException) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Cannot create an instance of ");
          stringBuilder.append(param1Class);
          throw new RuntimeException(stringBuilder.toString(), illegalAccessException);
        } catch (InstantiationException instantiationException) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Cannot create an instance of ");
          stringBuilder.append(param1Class);
          throw new RuntimeException(stringBuilder.toString(), instantiationException);
        } catch (InvocationTargetException invocationTargetException) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Cannot create an instance of ");
          stringBuilder.append(param1Class);
          throw new RuntimeException(stringBuilder.toString(), invocationTargetException);
        }  
      return super.create(param1Class);
    }
  }
  
  public static interface Factory {
    @NonNull
    <T extends ViewModel> T create(@NonNull Class<T> param1Class);
  }
  
  public static class NewInstanceFactory implements Factory {
    @NonNull
    public <T extends ViewModel> T create(@NonNull Class<T> param1Class) {
      try {
        return param1Class.newInstance();
      } catch (InstantiationException instantiationException) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot create an instance of ");
        stringBuilder.append(param1Class);
        throw new RuntimeException(stringBuilder.toString(), instantiationException);
      } catch (IllegalAccessException illegalAccessException) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot create an instance of ");
        stringBuilder.append(param1Class);
        throw new RuntimeException(stringBuilder.toString(), illegalAccessException);
      } 
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\arch\lifecycle\ViewModelProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */