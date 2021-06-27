package android.support.constraint.solver;

final class Pools {
  private static final boolean DEBUG = false;
  
  static interface Pool<T> {
    T acquire();
    
    boolean release(T param1T);
    
    void releaseAll(T[] param1ArrayOfT, int param1Int);
  }
  
  static class SimplePool<T> implements Pool<T> {
    private final Object[] mPool;
    
    private int mPoolSize;
    
    SimplePool(int param1Int) {
      if (param1Int > 0) {
        this.mPool = new Object[param1Int];
        return;
      } 
      throw new IllegalArgumentException("The max pool size must be > 0");
    }
    
    private boolean isInPool(T param1T) {
      for (byte b = 0; b < this.mPoolSize; b++) {
        if (this.mPool[b] == param1T)
          return true; 
      } 
      return false;
    }
    
    public T acquire() {
      if (this.mPoolSize > 0) {
        int i = this.mPoolSize - 1;
        Object object = this.mPool[i];
        this.mPool[i] = null;
        this.mPoolSize--;
        return (T)object;
      } 
      return null;
    }
    
    public boolean release(T param1T) {
      if (this.mPoolSize < this.mPool.length) {
        this.mPool[this.mPoolSize] = param1T;
        this.mPoolSize++;
        return true;
      } 
      return false;
    }
    
    public void releaseAll(T[] param1ArrayOfT, int param1Int) {
      int i = param1Int;
      if (param1Int > param1ArrayOfT.length)
        i = param1ArrayOfT.length; 
      for (param1Int = 0; param1Int < i; param1Int++) {
        T t = param1ArrayOfT[param1Int];
        if (this.mPoolSize < this.mPool.length) {
          this.mPool[this.mPoolSize] = t;
          this.mPoolSize++;
        } 
      } 
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\constraint\solver\Pools.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */