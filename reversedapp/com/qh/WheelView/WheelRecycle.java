package com.qh.WheelView;

import android.view.View;
import android.widget.LinearLayout;
import java.util.LinkedList;
import java.util.List;

public class WheelRecycle {
  private List<View> emptyItems;
  
  private List<View> items;
  
  private WheelView wheel;
  
  public WheelRecycle(WheelView paramWheelView) {
    this.wheel = paramWheelView;
  }
  
  private List<View> addView(View paramView, List<View> paramList) {
    List<View> list = paramList;
    if (paramList == null)
      list = new LinkedList<View>(); 
    list.add(paramView);
    return list;
  }
  
  private View getCachedView(List<View> paramList) {
    if (paramList != null && paramList.size() > 0) {
      View view = paramList.get(0);
      paramList.remove(0);
      return view;
    } 
    return null;
  }
  
  private void recycleView(View paramView, int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: getfield wheel : Lcom/qh/WheelView/WheelView;
    //   4: invokevirtual getViewAdapter : ()Lcom/qh/WheelView/WheelViewAdapter;
    //   7: invokeinterface getItemsCount : ()I
    //   12: istore_3
    //   13: iload_2
    //   14: iflt -> 25
    //   17: iload_2
    //   18: istore #4
    //   20: iload_2
    //   21: iload_3
    //   22: if_icmplt -> 54
    //   25: iload_2
    //   26: istore #4
    //   28: aload_0
    //   29: getfield wheel : Lcom/qh/WheelView/WheelView;
    //   32: invokevirtual isCyclic : ()Z
    //   35: ifne -> 54
    //   38: aload_0
    //   39: aload_0
    //   40: aload_1
    //   41: aload_0
    //   42: getfield emptyItems : Ljava/util/List;
    //   45: invokespecial addView : (Landroid/view/View;Ljava/util/List;)Ljava/util/List;
    //   48: putfield emptyItems : Ljava/util/List;
    //   51: goto -> 81
    //   54: iload #4
    //   56: ifge -> 68
    //   59: iload #4
    //   61: iload_3
    //   62: iadd
    //   63: istore #4
    //   65: goto -> 54
    //   68: aload_0
    //   69: aload_0
    //   70: aload_1
    //   71: aload_0
    //   72: getfield items : Ljava/util/List;
    //   75: invokespecial addView : (Landroid/view/View;Ljava/util/List;)Ljava/util/List;
    //   78: putfield items : Ljava/util/List;
    //   81: return
  }
  
  public void clearAll() {
    if (this.items != null)
      this.items.clear(); 
    if (this.emptyItems != null)
      this.emptyItems.clear(); 
  }
  
  public View getEmptyItem() {
    return getCachedView(this.emptyItems);
  }
  
  public View getItem() {
    return getCachedView(this.items);
  }
  
  public int recycleItems(LinearLayout paramLinearLayout, int paramInt, ItemsRange paramItemsRange) {
    int i = 0;
    int j;
    for (j = paramInt; i < paramLinearLayout.getChildCount(); j = m) {
      int k;
      int m;
      if (!paramItemsRange.contains(paramInt)) {
        recycleView(paramLinearLayout.getChildAt(i), paramInt);
        paramLinearLayout.removeViewAt(i);
        k = i;
        m = j;
        if (i == 0) {
          m = j + 1;
          k = i;
        } 
      } else {
        k = i + 1;
        m = j;
      } 
      paramInt++;
      i = k;
    } 
    return j;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\WheelView\WheelRecycle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */