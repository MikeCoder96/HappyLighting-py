package android.support.v7.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

public class SortedList<T> {
  private static final int CAPACITY_GROWTH = 10;
  
  private static final int DELETION = 2;
  
  private static final int INSERTION = 1;
  
  public static final int INVALID_POSITION = -1;
  
  private static final int LOOKUP = 4;
  
  private static final int MIN_CAPACITY = 10;
  
  private BatchedCallback mBatchedCallback;
  
  private Callback mCallback;
  
  T[] mData;
  
  private int mMergedSize;
  
  private T[] mOldData;
  
  private int mOldDataSize;
  
  private int mOldDataStart;
  
  private int mSize;
  
  private final Class<T> mTClass;
  
  public SortedList(Class<T> paramClass, Callback<T> paramCallback) {
    this(paramClass, paramCallback, 10);
  }
  
  public SortedList(Class<T> paramClass, Callback<T> paramCallback, int paramInt) {
    this.mTClass = paramClass;
    this.mData = (T[])Array.newInstance(paramClass, paramInt);
    this.mCallback = paramCallback;
    this.mSize = 0;
  }
  
  private int add(T paramT, boolean paramBoolean) {
    int j;
    int i = findIndexOf(paramT, this.mData, 0, this.mSize, 1);
    if (i == -1) {
      j = 0;
    } else {
      j = i;
      if (i < this.mSize) {
        T t = this.mData[i];
        j = i;
        if (this.mCallback.areItemsTheSame(t, paramT)) {
          if (this.mCallback.areContentsTheSame(t, paramT)) {
            this.mData[i] = paramT;
            return i;
          } 
          this.mData[i] = paramT;
          this.mCallback.onChanged(i, 1);
          return i;
        } 
      } 
    } 
    addToData(j, paramT);
    if (paramBoolean)
      this.mCallback.onInserted(j, 1); 
    return j;
  }
  
  private void addAllInternal(T[] paramArrayOfT) {
    boolean bool;
    if (!(this.mCallback instanceof BatchedCallback)) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool)
      beginBatchedUpdates(); 
    this.mOldData = this.mData;
    this.mOldDataStart = 0;
    this.mOldDataSize = this.mSize;
    Arrays.sort(paramArrayOfT, this.mCallback);
    int i = deduplicate(paramArrayOfT);
    if (this.mSize == 0) {
      this.mData = paramArrayOfT;
      this.mSize = i;
      this.mMergedSize = i;
      this.mCallback.onInserted(0, i);
    } else {
      merge(paramArrayOfT, i);
    } 
    this.mOldData = null;
    if (bool)
      endBatchedUpdates(); 
  }
  
  private void addToData(int paramInt, T paramT) {
    if (paramInt <= this.mSize) {
      if (this.mSize == this.mData.length) {
        Object[] arrayOfObject = (Object[])Array.newInstance(this.mTClass, this.mData.length + 10);
        System.arraycopy(this.mData, 0, arrayOfObject, 0, paramInt);
        arrayOfObject[paramInt] = paramT;
        System.arraycopy(this.mData, paramInt, arrayOfObject, paramInt + 1, this.mSize - paramInt);
        this.mData = (T[])arrayOfObject;
      } else {
        System.arraycopy(this.mData, paramInt, this.mData, paramInt + 1, this.mSize - paramInt);
        this.mData[paramInt] = paramT;
      } 
      this.mSize++;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("cannot add item to ");
    stringBuilder.append(paramInt);
    stringBuilder.append(" because size is ");
    stringBuilder.append(this.mSize);
    throw new IndexOutOfBoundsException(stringBuilder.toString());
  }
  
  private int deduplicate(T[] paramArrayOfT) {
    if (paramArrayOfT.length != 0) {
      int i = 0;
      byte b = 1;
      int j = 1;
      while (b < paramArrayOfT.length) {
        T t = paramArrayOfT[b];
        int k = this.mCallback.compare(paramArrayOfT[i], t);
        if (k <= 0) {
          if (k == 0) {
            k = findSameItem(t, paramArrayOfT, i, j);
            if (k != -1) {
              paramArrayOfT[k] = t;
            } else {
              if (j != b)
                paramArrayOfT[j] = t; 
              j++;
            } 
          } else {
            if (j != b)
              paramArrayOfT[j] = t; 
            k = j + 1;
            i = j;
            j = k;
          } 
          b++;
          continue;
        } 
        throw new IllegalArgumentException("Input must be sorted in ascending order.");
      } 
      return j;
    } 
    throw new IllegalArgumentException("Input array must be non-empty");
  }
  
  private int findIndexOf(T paramT, T[] paramArrayOfT, int paramInt1, int paramInt2, int paramInt3) {
    int i;
    for (i = paramInt2; paramInt1 < i; i = paramInt2) {
      paramInt2 = (paramInt1 + i) / 2;
      T t = paramArrayOfT[paramInt2];
      int j = this.mCallback.compare(t, paramT);
      if (j < 0) {
        paramInt1 = paramInt2 + 1;
        continue;
      } 
      if (j == 0) {
        if (this.mCallback.areItemsTheSame(t, paramT))
          return paramInt2; 
        i = linearEqualitySearch(paramT, paramInt2, paramInt1, i);
        if (paramInt3 == 1) {
          paramInt1 = i;
          if (i == -1)
            paramInt1 = paramInt2; 
          return paramInt1;
        } 
        return i;
      } 
    } 
    if (paramInt3 != 1)
      paramInt1 = -1; 
    return paramInt1;
  }
  
  private int findSameItem(T paramT, T[] paramArrayOfT, int paramInt1, int paramInt2) {
    while (paramInt1 < paramInt2) {
      if (this.mCallback.areItemsTheSame(paramArrayOfT[paramInt1], paramT))
        return paramInt1; 
      paramInt1++;
    } 
    return -1;
  }
  
  private int linearEqualitySearch(T paramT, int paramInt1, int paramInt2, int paramInt3) {
    int j;
    int i = paramInt1 - 1;
    while (true) {
      j = paramInt1;
      if (i >= paramInt2) {
        T t = this.mData[i];
        if (this.mCallback.compare(t, paramT) != 0) {
          j = paramInt1;
          break;
        } 
        if (this.mCallback.areItemsTheSame(t, paramT))
          return i; 
        i--;
        continue;
      } 
      break;
    } 
    while (true) {
      paramInt1 = j + 1;
      if (paramInt1 < paramInt3) {
        T t = this.mData[paramInt1];
        if (this.mCallback.compare(t, paramT) != 0)
          break; 
        j = paramInt1;
        if (this.mCallback.areItemsTheSame(t, paramT))
          return paramInt1; 
        continue;
      } 
      break;
    } 
    return -1;
  }
  
  private void merge(T[] paramArrayOfT, int paramInt) {
    int i = this.mSize;
    this.mData = (T[])Array.newInstance(this.mTClass, i + paramInt + 10);
    i = 0;
    this.mMergedSize = 0;
    while (true) {
      if (this.mOldDataStart < this.mOldDataSize || i < paramInt)
        if (this.mOldDataStart == this.mOldDataSize) {
          paramInt -= i;
          System.arraycopy(paramArrayOfT, i, this.mData, this.mMergedSize, paramInt);
          this.mMergedSize += paramInt;
          this.mSize += paramInt;
          this.mCallback.onInserted(this.mMergedSize - paramInt, paramInt);
        } else {
          T[] arrayOfT1;
          if (i == paramInt) {
            paramInt = this.mOldDataSize - this.mOldDataStart;
            System.arraycopy(this.mOldData, this.mOldDataStart, this.mData, this.mMergedSize, paramInt);
            this.mMergedSize += paramInt;
            return;
          } 
          T t1 = this.mOldData[this.mOldDataStart];
          T t2 = paramArrayOfT[i];
          int j = this.mCallback.compare(t1, t2);
          if (j > 0) {
            arrayOfT1 = this.mData;
            j = this.mMergedSize;
            this.mMergedSize = j + 1;
            arrayOfT1[j] = t2;
            this.mSize++;
            i++;
            this.mCallback.onInserted(this.mMergedSize - 1, 1);
            continue;
          } 
          if (j == 0 && this.mCallback.areItemsTheSame(arrayOfT1, (T[])t2)) {
            T[] arrayOfT = this.mData;
            j = this.mMergedSize;
            this.mMergedSize = j + 1;
            arrayOfT[j] = t2;
            j = i + 1;
            this.mOldDataStart++;
            i = j;
            if (!this.mCallback.areContentsTheSame(arrayOfT1, (T[])t2)) {
              this.mCallback.onChanged(this.mMergedSize - 1, 1);
              i = j;
            } 
            continue;
          } 
          T[] arrayOfT2 = this.mData;
          j = this.mMergedSize;
          this.mMergedSize = j + 1;
          arrayOfT2[j] = (T)arrayOfT1;
          this.mOldDataStart++;
          continue;
        }  
      return;
    } 
  }
  
  private boolean remove(T paramT, boolean paramBoolean) {
    int i = findIndexOf(paramT, this.mData, 0, this.mSize, 2);
    if (i == -1)
      return false; 
    removeItemAtIndex(i, paramBoolean);
    return true;
  }
  
  private void removeItemAtIndex(int paramInt, boolean paramBoolean) {
    System.arraycopy(this.mData, paramInt + 1, this.mData, paramInt, this.mSize - paramInt - 1);
    this.mSize--;
    this.mData[this.mSize] = null;
    if (paramBoolean)
      this.mCallback.onRemoved(paramInt, 1); 
  }
  
  private void throwIfMerging() {
    if (this.mOldData == null)
      return; 
    throw new IllegalStateException("Cannot call this method from within addAll");
  }
  
  public int add(T paramT) {
    throwIfMerging();
    return add(paramT, true);
  }
  
  public void addAll(Collection<T> paramCollection) {
    addAll(paramCollection.toArray((T[])Array.newInstance(this.mTClass, paramCollection.size())), true);
  }
  
  public void addAll(T... paramVarArgs) {
    addAll(paramVarArgs, false);
  }
  
  public void addAll(T[] paramArrayOfT, boolean paramBoolean) {
    throwIfMerging();
    if (paramArrayOfT.length == 0)
      return; 
    if (paramBoolean) {
      addAllInternal(paramArrayOfT);
    } else {
      Object[] arrayOfObject = (Object[])Array.newInstance(this.mTClass, paramArrayOfT.length);
      System.arraycopy(paramArrayOfT, 0, arrayOfObject, 0, paramArrayOfT.length);
      addAllInternal((T[])arrayOfObject);
    } 
  }
  
  public void beginBatchedUpdates() {
    throwIfMerging();
    if (this.mCallback instanceof BatchedCallback)
      return; 
    if (this.mBatchedCallback == null)
      this.mBatchedCallback = new BatchedCallback(this.mCallback); 
    this.mCallback = this.mBatchedCallback;
  }
  
  public void clear() {
    throwIfMerging();
    if (this.mSize == 0)
      return; 
    int i = this.mSize;
    Arrays.fill((Object[])this.mData, 0, i, (Object)null);
    this.mSize = 0;
    this.mCallback.onRemoved(0, i);
  }
  
  public void endBatchedUpdates() {
    throwIfMerging();
    if (this.mCallback instanceof BatchedCallback)
      ((BatchedCallback)this.mCallback).dispatchLastEvent(); 
    if (this.mCallback == this.mBatchedCallback)
      this.mCallback = this.mBatchedCallback.mWrappedCallback; 
  }
  
  public T get(int paramInt) throws IndexOutOfBoundsException {
    if (paramInt < this.mSize && paramInt >= 0)
      return (this.mOldData != null && paramInt >= this.mMergedSize) ? this.mOldData[paramInt - this.mMergedSize + this.mOldDataStart] : this.mData[paramInt]; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Asked to get item at ");
    stringBuilder.append(paramInt);
    stringBuilder.append(" but size is ");
    stringBuilder.append(this.mSize);
    throw new IndexOutOfBoundsException(stringBuilder.toString());
  }
  
  public int indexOf(T paramT) {
    if (this.mOldData != null) {
      int i = findIndexOf(paramT, this.mData, 0, this.mMergedSize, 4);
      if (i != -1)
        return i; 
      i = findIndexOf(paramT, this.mOldData, this.mOldDataStart, this.mOldDataSize, 4);
      return (i != -1) ? (i - this.mOldDataStart + this.mMergedSize) : -1;
    } 
    return findIndexOf(paramT, this.mData, 0, this.mSize, 4);
  }
  
  public void recalculatePositionOfItemAt(int paramInt) {
    throwIfMerging();
    T t = get(paramInt);
    removeItemAtIndex(paramInt, false);
    int i = add(t, false);
    if (paramInt != i)
      this.mCallback.onMoved(paramInt, i); 
  }
  
  public boolean remove(T paramT) {
    throwIfMerging();
    return remove(paramT, true);
  }
  
  public T removeItemAt(int paramInt) {
    throwIfMerging();
    T t = get(paramInt);
    removeItemAtIndex(paramInt, true);
    return t;
  }
  
  public int size() {
    return this.mSize;
  }
  
  public void updateItemAt(int paramInt, T paramT) {
    throwIfMerging();
    T t = get(paramInt);
    if (t == paramT || !this.mCallback.areContentsTheSame(t, paramT)) {
      i = 1;
    } else {
      i = 0;
    } 
    if (t != paramT && this.mCallback.compare(t, paramT) == 0) {
      this.mData[paramInt] = paramT;
      if (i)
        this.mCallback.onChanged(paramInt, 1); 
      return;
    } 
    if (i)
      this.mCallback.onChanged(paramInt, 1); 
    removeItemAtIndex(paramInt, false);
    int i = add(paramT, false);
    if (paramInt != i)
      this.mCallback.onMoved(paramInt, i); 
  }
  
  public static class BatchedCallback<T2> extends Callback<T2> {
    private final BatchingListUpdateCallback mBatchingListUpdateCallback;
    
    final SortedList.Callback<T2> mWrappedCallback;
    
    public BatchedCallback(SortedList.Callback<T2> param1Callback) {
      this.mWrappedCallback = param1Callback;
      this.mBatchingListUpdateCallback = new BatchingListUpdateCallback(this.mWrappedCallback);
    }
    
    public boolean areContentsTheSame(T2 param1T21, T2 param1T22) {
      return this.mWrappedCallback.areContentsTheSame(param1T21, param1T22);
    }
    
    public boolean areItemsTheSame(T2 param1T21, T2 param1T22) {
      return this.mWrappedCallback.areItemsTheSame(param1T21, param1T22);
    }
    
    public int compare(T2 param1T21, T2 param1T22) {
      return this.mWrappedCallback.compare(param1T21, param1T22);
    }
    
    public void dispatchLastEvent() {
      this.mBatchingListUpdateCallback.dispatchLastEvent();
    }
    
    public void onChanged(int param1Int1, int param1Int2) {
      this.mBatchingListUpdateCallback.onChanged(param1Int1, param1Int2, null);
    }
    
    public void onInserted(int param1Int1, int param1Int2) {
      this.mBatchingListUpdateCallback.onInserted(param1Int1, param1Int2);
    }
    
    public void onMoved(int param1Int1, int param1Int2) {
      this.mBatchingListUpdateCallback.onMoved(param1Int1, param1Int2);
    }
    
    public void onRemoved(int param1Int1, int param1Int2) {
      this.mBatchingListUpdateCallback.onRemoved(param1Int1, param1Int2);
    }
  }
  
  public static abstract class Callback<T2> implements Comparator<T2>, ListUpdateCallback {
    public abstract boolean areContentsTheSame(T2 param1T21, T2 param1T22);
    
    public abstract boolean areItemsTheSame(T2 param1T21, T2 param1T22);
    
    public abstract int compare(T2 param1T21, T2 param1T22);
    
    public abstract void onChanged(int param1Int1, int param1Int2);
    
    public void onChanged(int param1Int1, int param1Int2, Object param1Object) {
      onChanged(param1Int1, param1Int2);
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v\\util\SortedList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */