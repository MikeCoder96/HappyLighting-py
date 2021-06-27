package android.arch.core.internal;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class SafeIterableMap<K, V> implements Iterable<Map.Entry<K, V>> {
  private Entry<K, V> mEnd;
  
  private WeakHashMap<SupportRemove<K, V>, Boolean> mIterators = new WeakHashMap<SupportRemove<K, V>, Boolean>();
  
  private int mSize = 0;
  
  private Entry<K, V> mStart;
  
  public Iterator<Map.Entry<K, V>> descendingIterator() {
    DescendingIterator<K, V> descendingIterator = new DescendingIterator<K, V>(this.mEnd, this.mStart);
    this.mIterators.put(descendingIterator, Boolean.valueOf(false));
    return descendingIterator;
  }
  
  public Map.Entry<K, V> eldest() {
    return this.mStart;
  }
  
  public boolean equals(Object<Map.Entry<K, V>> paramObject) {
    boolean bool = true;
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof SafeIterableMap))
      return false; 
    SafeIterableMap safeIterableMap = (SafeIterableMap)paramObject;
    if (size() != safeIterableMap.size())
      return false; 
    paramObject = (Object<Map.Entry<K, V>>)iterator();
    Iterator<Object> iterator = safeIterableMap.iterator();
    while (paramObject.hasNext() && iterator.hasNext()) {
      Map.Entry entry = paramObject.next();
      Object object = iterator.next();
      if ((entry == null && object != null) || (entry != null && !entry.equals(object)))
        return false; 
    } 
    if (paramObject.hasNext() || iterator.hasNext())
      bool = false; 
    return bool;
  }
  
  protected Entry<K, V> get(K paramK) {
    Entry<K, V> entry;
    for (entry = this.mStart; entry != null && !entry.mKey.equals(paramK); entry = entry.mNext);
    return entry;
  }
  
  @NonNull
  public Iterator<Map.Entry<K, V>> iterator() {
    AscendingIterator<K, V> ascendingIterator = new AscendingIterator<K, V>(this.mStart, this.mEnd);
    this.mIterators.put(ascendingIterator, Boolean.valueOf(false));
    return ascendingIterator;
  }
  
  public IteratorWithAdditions iteratorWithAdditions() {
    IteratorWithAdditions iteratorWithAdditions = new IteratorWithAdditions();
    this.mIterators.put(iteratorWithAdditions, Boolean.valueOf(false));
    return iteratorWithAdditions;
  }
  
  public Map.Entry<K, V> newest() {
    return this.mEnd;
  }
  
  protected Entry<K, V> put(@NonNull K paramK, @NonNull V paramV) {
    Entry<K, V> entry = new Entry<K, V>(paramK, paramV);
    this.mSize++;
    if (this.mEnd == null) {
      this.mStart = entry;
      this.mEnd = this.mStart;
      return entry;
    } 
    this.mEnd.mNext = entry;
    entry.mPrevious = this.mEnd;
    this.mEnd = entry;
    return entry;
  }
  
  public V putIfAbsent(@NonNull K paramK, @NonNull V paramV) {
    Entry<K, V> entry = get(paramK);
    if (entry != null)
      return entry.mValue; 
    put(paramK, paramV);
    return null;
  }
  
  public V remove(@NonNull K paramK) {
    Entry<K, V> entry = get(paramK);
    if (entry == null)
      return null; 
    this.mSize--;
    if (!this.mIterators.isEmpty()) {
      Iterator<SupportRemove<K, V>> iterator = this.mIterators.keySet().iterator();
      while (iterator.hasNext())
        ((SupportRemove<K, V>)iterator.next()).supportRemove(entry); 
    } 
    if (entry.mPrevious != null) {
      entry.mPrevious.mNext = entry.mNext;
    } else {
      this.mStart = entry.mNext;
    } 
    if (entry.mNext != null) {
      entry.mNext.mPrevious = entry.mPrevious;
    } else {
      this.mEnd = entry.mPrevious;
    } 
    entry.mNext = null;
    entry.mPrevious = null;
    return entry.mValue;
  }
  
  public int size() {
    return this.mSize;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[");
    Iterator<Map.Entry<K, V>> iterator = iterator();
    while (iterator.hasNext()) {
      stringBuilder.append(((Map.Entry)iterator.next()).toString());
      if (iterator.hasNext())
        stringBuilder.append(", "); 
    } 
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
  
  static class AscendingIterator<K, V> extends ListIterator<K, V> {
    AscendingIterator(SafeIterableMap.Entry<K, V> param1Entry1, SafeIterableMap.Entry<K, V> param1Entry2) {
      super(param1Entry1, param1Entry2);
    }
    
    SafeIterableMap.Entry<K, V> backward(SafeIterableMap.Entry<K, V> param1Entry) {
      return param1Entry.mPrevious;
    }
    
    SafeIterableMap.Entry<K, V> forward(SafeIterableMap.Entry<K, V> param1Entry) {
      return param1Entry.mNext;
    }
  }
  
  private static class DescendingIterator<K, V> extends ListIterator<K, V> {
    DescendingIterator(SafeIterableMap.Entry<K, V> param1Entry1, SafeIterableMap.Entry<K, V> param1Entry2) {
      super(param1Entry1, param1Entry2);
    }
    
    SafeIterableMap.Entry<K, V> backward(SafeIterableMap.Entry<K, V> param1Entry) {
      return param1Entry.mNext;
    }
    
    SafeIterableMap.Entry<K, V> forward(SafeIterableMap.Entry<K, V> param1Entry) {
      return param1Entry.mPrevious;
    }
  }
  
  static class Entry<K, V> implements Map.Entry<K, V> {
    @NonNull
    final K mKey;
    
    Entry<K, V> mNext;
    
    Entry<K, V> mPrevious;
    
    @NonNull
    final V mValue;
    
    Entry(@NonNull K param1K, @NonNull V param1V) {
      this.mKey = param1K;
      this.mValue = param1V;
    }
    
    public boolean equals(Object param1Object) {
      boolean bool = true;
      if (param1Object == this)
        return true; 
      if (!(param1Object instanceof Entry))
        return false; 
      param1Object = param1Object;
      if (!this.mKey.equals(((Entry)param1Object).mKey) || !this.mValue.equals(((Entry)param1Object).mValue))
        bool = false; 
      return bool;
    }
    
    @NonNull
    public K getKey() {
      return this.mKey;
    }
    
    @NonNull
    public V getValue() {
      return this.mValue;
    }
    
    public V setValue(V param1V) {
      throw new UnsupportedOperationException("An entry modification is not supported");
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.mKey);
      stringBuilder.append("=");
      stringBuilder.append(this.mValue);
      return stringBuilder.toString();
    }
  }
  
  private class IteratorWithAdditions implements Iterator<Map.Entry<K, V>>, SupportRemove<K, V> {
    private boolean mBeforeStart = true;
    
    private SafeIterableMap.Entry<K, V> mCurrent;
    
    private IteratorWithAdditions() {}
    
    public boolean hasNext() {
      boolean bool = this.mBeforeStart;
      boolean bool1 = false;
      boolean bool2 = false;
      if (bool) {
        if (SafeIterableMap.this.mStart != null)
          bool2 = true; 
        return bool2;
      } 
      bool2 = bool1;
      if (this.mCurrent != null) {
        bool2 = bool1;
        if (this.mCurrent.mNext != null)
          bool2 = true; 
      } 
      return bool2;
    }
    
    public Map.Entry<K, V> next() {
      if (this.mBeforeStart) {
        this.mBeforeStart = false;
        this.mCurrent = SafeIterableMap.this.mStart;
      } else {
        SafeIterableMap.Entry entry;
        if (this.mCurrent != null) {
          entry = this.mCurrent.mNext;
        } else {
          entry = null;
        } 
        this.mCurrent = entry;
      } 
      return this.mCurrent;
    }
    
    public void supportRemove(@NonNull SafeIterableMap.Entry<K, V> param1Entry) {
      if (param1Entry == this.mCurrent) {
        boolean bool;
        this.mCurrent = this.mCurrent.mPrevious;
        if (this.mCurrent == null) {
          bool = true;
        } else {
          bool = false;
        } 
        this.mBeforeStart = bool;
      } 
    }
  }
  
  private static abstract class ListIterator<K, V> implements Iterator<Map.Entry<K, V>>, SupportRemove<K, V> {
    SafeIterableMap.Entry<K, V> mExpectedEnd;
    
    SafeIterableMap.Entry<K, V> mNext;
    
    ListIterator(SafeIterableMap.Entry<K, V> param1Entry1, SafeIterableMap.Entry<K, V> param1Entry2) {
      this.mExpectedEnd = param1Entry2;
      this.mNext = param1Entry1;
    }
    
    private SafeIterableMap.Entry<K, V> nextNode() {
      return (this.mNext == this.mExpectedEnd || this.mExpectedEnd == null) ? null : forward(this.mNext);
    }
    
    abstract SafeIterableMap.Entry<K, V> backward(SafeIterableMap.Entry<K, V> param1Entry);
    
    abstract SafeIterableMap.Entry<K, V> forward(SafeIterableMap.Entry<K, V> param1Entry);
    
    public boolean hasNext() {
      boolean bool;
      if (this.mNext != null) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public Map.Entry<K, V> next() {
      SafeIterableMap.Entry<K, V> entry = this.mNext;
      this.mNext = nextNode();
      return entry;
    }
    
    public void supportRemove(@NonNull SafeIterableMap.Entry<K, V> param1Entry) {
      if (this.mExpectedEnd == param1Entry && param1Entry == this.mNext) {
        this.mNext = null;
        this.mExpectedEnd = null;
      } 
      if (this.mExpectedEnd == param1Entry)
        this.mExpectedEnd = backward(this.mExpectedEnd); 
      if (this.mNext == param1Entry)
        this.mNext = nextNode(); 
    }
  }
  
  static interface SupportRemove<K, V> {
    void supportRemove(@NonNull SafeIterableMap.Entry<K, V> param1Entry);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\arch\core\internal\SafeIterableMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */