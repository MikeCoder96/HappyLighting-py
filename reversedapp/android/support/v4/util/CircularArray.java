package android.support.v4.util;

public final class CircularArray<E> {
  private int mCapacityBitmask;
  
  private E[] mElements;
  
  private int mHead;
  
  private int mTail;
  
  public CircularArray() {
    this(8);
  }
  
  public CircularArray(int paramInt) {
    if (paramInt >= 1) {
      if (paramInt <= 1073741824) {
        int i = paramInt;
        if (Integer.bitCount(paramInt) != 1)
          i = Integer.highestOneBit(paramInt - 1) << 1; 
        this.mCapacityBitmask = i - 1;
        this.mElements = (E[])new Object[i];
        return;
      } 
      throw new IllegalArgumentException("capacity must be <= 2^30");
    } 
    throw new IllegalArgumentException("capacity must be >= 1");
  }
  
  private void doubleCapacity() {
    int i = this.mElements.length;
    int j = i - this.mHead;
    int k = i << 1;
    if (k >= 0) {
      Object[] arrayOfObject = new Object[k];
      System.arraycopy(this.mElements, this.mHead, arrayOfObject, 0, j);
      System.arraycopy(this.mElements, 0, arrayOfObject, j, this.mHead);
      this.mElements = (E[])arrayOfObject;
      this.mHead = 0;
      this.mTail = i;
      this.mCapacityBitmask = k - 1;
      return;
    } 
    throw new RuntimeException("Max array capacity exceeded");
  }
  
  public void addFirst(E paramE) {
    this.mHead = this.mHead - 1 & this.mCapacityBitmask;
    this.mElements[this.mHead] = paramE;
    if (this.mHead == this.mTail)
      doubleCapacity(); 
  }
  
  public void addLast(E paramE) {
    this.mElements[this.mTail] = paramE;
    this.mTail = this.mTail + 1 & this.mCapacityBitmask;
    if (this.mTail == this.mHead)
      doubleCapacity(); 
  }
  
  public void clear() {
    removeFromStart(size());
  }
  
  public E get(int paramInt) {
    if (paramInt >= 0 && paramInt < size()) {
      E[] arrayOfE = this.mElements;
      int i = this.mHead;
      return arrayOfE[this.mCapacityBitmask & i + paramInt];
    } 
    throw new ArrayIndexOutOfBoundsException();
  }
  
  public E getFirst() {
    if (this.mHead != this.mTail)
      return this.mElements[this.mHead]; 
    throw new ArrayIndexOutOfBoundsException();
  }
  
  public E getLast() {
    if (this.mHead != this.mTail)
      return this.mElements[this.mTail - 1 & this.mCapacityBitmask]; 
    throw new ArrayIndexOutOfBoundsException();
  }
  
  public boolean isEmpty() {
    boolean bool;
    if (this.mHead == this.mTail) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public E popFirst() {
    if (this.mHead != this.mTail) {
      E e = this.mElements[this.mHead];
      this.mElements[this.mHead] = null;
      this.mHead = this.mHead + 1 & this.mCapacityBitmask;
      return e;
    } 
    throw new ArrayIndexOutOfBoundsException();
  }
  
  public E popLast() {
    if (this.mHead != this.mTail) {
      int i = this.mTail - 1 & this.mCapacityBitmask;
      E e = this.mElements[i];
      this.mElements[i] = null;
      this.mTail = i;
      return e;
    } 
    throw new ArrayIndexOutOfBoundsException();
  }
  
  public void removeFromEnd(int paramInt) {
    if (paramInt <= 0)
      return; 
    if (paramInt <= size()) {
      int i = 0;
      if (paramInt < this.mTail)
        i = this.mTail - paramInt; 
      for (int j = i; j < this.mTail; j++)
        this.mElements[j] = null; 
      i = this.mTail - i;
      paramInt -= i;
      this.mTail -= i;
      if (paramInt > 0) {
        this.mTail = this.mElements.length;
        i = this.mTail - paramInt;
        for (paramInt = i; paramInt < this.mTail; paramInt++)
          this.mElements[paramInt] = null; 
        this.mTail = i;
      } 
      return;
    } 
    throw new ArrayIndexOutOfBoundsException();
  }
  
  public void removeFromStart(int paramInt) {
    if (paramInt <= 0)
      return; 
    if (paramInt <= size()) {
      int i = this.mElements.length;
      int j = i;
      if (paramInt < i - this.mHead)
        j = this.mHead + paramInt; 
      for (i = this.mHead; i < j; i++)
        this.mElements[i] = null; 
      i = j - this.mHead;
      j = paramInt - i;
      paramInt = this.mHead;
      this.mHead = this.mCapacityBitmask & paramInt + i;
      if (j > 0) {
        for (paramInt = 0; paramInt < j; paramInt++)
          this.mElements[paramInt] = null; 
        this.mHead = j;
      } 
      return;
    } 
    throw new ArrayIndexOutOfBoundsException();
  }
  
  public int size() {
    return this.mTail - this.mHead & this.mCapacityBitmask;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v\\util\CircularArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */