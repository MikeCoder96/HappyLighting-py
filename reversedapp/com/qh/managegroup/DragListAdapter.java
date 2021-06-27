package com.qh.managegroup;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;

public class DragListAdapter extends BaseAdapter {
  private static final String TAG = "DragListAdapter";
  
  public Hashtable<Integer, ItemInfo> ItemInfos = new Hashtable<Integer, ItemInfo>();
  
  private boolean ShowItem = false;
  
  public ArrayList<MyListData> arrayTitles;
  
  private Context context;
  
  private int dragPosition = -1;
  
  private int height;
  
  private int invisilePosition = -1;
  
  private boolean isChanged = true;
  
  public boolean isHidden;
  
  private boolean isSameDragDirection = true;
  
  private int lastFlag = -1;
  
  private ArrayList<MyListData> mCopyList = new ArrayList<MyListData>();
  
  public DragListView.DragListChange mDragListChange;
  
  public DragListAdapter(Context paramContext, ArrayList<MyListData> paramArrayList) {
    this.context = paramContext;
    this.arrayTitles = paramArrayList;
  }
  
  public void addDragItem(int paramInt, MyListData paramMyListData) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("start");
    stringBuilder.append(paramInt);
    Log.i("DragListAdapter", stringBuilder.toString());
    MyListData myListData = this.arrayTitles.get(paramInt);
    this.arrayTitles.remove(paramInt);
    this.arrayTitles.add(paramInt, paramMyListData);
  }
  
  public void copyList() {
    this.mCopyList.clear();
    for (MyListData myListData : this.arrayTitles)
      this.mCopyList.add(myListData); 
  }
  
  public void exchange(int paramInt1, int paramInt2) {
    MyListData myListData = (MyListData)getItem(paramInt1);
    PrintStream printStream = System.out;
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append(paramInt1);
    stringBuilder2.append("========");
    stringBuilder2.append(paramInt2);
    printStream.println(stringBuilder2.toString());
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append("startPostion ==== ");
    stringBuilder1.append(paramInt1);
    Log.d("ON", stringBuilder1.toString());
    stringBuilder1 = new StringBuilder();
    stringBuilder1.append("endPosition ==== ");
    stringBuilder1.append(paramInt2);
    Log.d("ON", stringBuilder1.toString());
    if (paramInt1 < paramInt2) {
      this.arrayTitles.add(paramInt2 + 1, myListData);
      this.arrayTitles.remove(paramInt1);
    } else {
      this.arrayTitles.add(paramInt2, myListData);
      this.arrayTitles.remove(paramInt1 + 1);
    } 
    this.isChanged = true;
  }
  
  public void exchangeCopy(int paramInt1, int paramInt2) {
    PrintStream printStream1 = System.out;
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append(paramInt1);
    stringBuilder2.append("--");
    stringBuilder2.append(paramInt2);
    printStream1.println(stringBuilder2.toString());
    MyListData myListData = (MyListData)getCopyItem(paramInt1);
    PrintStream printStream2 = System.out;
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append(paramInt1);
    stringBuilder1.append("========");
    stringBuilder1.append(paramInt2);
    printStream2.println(stringBuilder1.toString());
    stringBuilder1 = new StringBuilder();
    stringBuilder1.append("startPostion ==== ");
    stringBuilder1.append(paramInt1);
    Log.d("ON", stringBuilder1.toString());
    stringBuilder1 = new StringBuilder();
    stringBuilder1.append("endPosition ==== ");
    stringBuilder1.append(paramInt2);
    Log.d("ON", stringBuilder1.toString());
    if (paramInt1 < paramInt2) {
      this.mCopyList.add(paramInt2 + 1, myListData);
      this.mCopyList.remove(paramInt1);
    } else {
      this.mCopyList.add(paramInt2, myListData);
      this.mCopyList.remove(paramInt1 + 1);
    } 
    this.isChanged = true;
  }
  
  public Object getCopyItem(int paramInt) {
    return this.mCopyList.get(paramInt);
  }
  
  public int getCount() {
    return this.arrayTitles.size();
  }
  
  public Animation getFromSelfAnimation(int paramInt1, int paramInt2) {
    TranslateAnimation translateAnimation = new TranslateAnimation(1, 0.0F, 0, paramInt1, 1, 0.0F, 0, paramInt2);
    translateAnimation.setInterpolator((Interpolator)new AccelerateDecelerateInterpolator());
    translateAnimation.setFillAfter(true);
    translateAnimation.setDuration(100L);
    translateAnimation.setInterpolator((Interpolator)new AccelerateInterpolator());
    return (Animation)translateAnimation;
  }
  
  public Object getItem(int paramInt) {
    return this.arrayTitles.get(paramInt);
  }
  
  public long getItemId(int paramInt) {
    return paramInt;
  }
  
  public Animation getToSelfAnimation(int paramInt1, int paramInt2) {
    TranslateAnimation translateAnimation = new TranslateAnimation(0, paramInt1, 1, 0.0F, 0, paramInt2, 1, 0.0F);
    translateAnimation.setInterpolator((Interpolator)new AccelerateDecelerateInterpolator());
    translateAnimation.setFillAfter(true);
    translateAnimation.setDuration(100L);
    translateAnimation.setInterpolator((Interpolator)new AccelerateInterpolator());
    return (Animation)translateAnimation;
  }
  
  public View getView(final int position, View paramView, ViewGroup paramViewGroup) {
    MyListData myListData = this.arrayTitles.get(position);
    if (myListData.isGroup) {
      paramView = LayoutInflater.from(this.context).inflate(2131361866, null);
    } else {
      paramView = LayoutInflater.from(this.context).inflate(2131361867, null);
    } 
    ItemInfo itemInfo = new ItemInfo();
    if (myListData.isGroup) {
      itemInfo.name = (TextView)paramView.findViewById(2131230808);
      itemInfo.reset = (ImageView)paramView.findViewById(2131231098);
      itemInfo.reset_text = (TextView)paramView.findViewById(2131231099);
      if (position == 0) {
        itemInfo.reset.setVisibility(8);
        itemInfo.reset_text.setVisibility(8);
      } 
      itemInfo.light_img = (ImageView)paramView.findViewById(2131230754);
      itemInfo.light_img.setTag(myListData);
    } else {
      itemInfo.name = (TextView)paramView.findViewById(2131230952);
      itemInfo.light_img = (ImageView)paramView.findViewById(2131230951);
      itemInfo.light_img.setTag(myListData);
      itemInfo.reset = (ImageView)paramView.findViewById(2131230944);
      itemInfo.reset_text = (TextView)paramView.findViewById(2131231099);
    } 
    itemInfo.tx_delete = (TextView)paramView.findViewById(2131231241);
    itemInfo.tx_delete.setOnClickListener((new DelectClickListener()).setData(myListData));
    itemInfo.reset.setOnClickListener((new resetClickListener()).setData(myListData));
    itemInfo.reset_text.setOnClickListener((new resetClickListener()).setData(myListData));
    if (myListData.isGroup)
      if (myListData.tag) {
        itemInfo.light_img.setImageResource(2131165270);
      } else {
        itemInfo.light_img.setImageResource(2131165269);
      }  
    itemInfo.light_img.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            int i = ((MyListData)DragListAdapter.this.arrayTitles.get(position)).groupId;
            for (byte b = 0; b < DragListAdapter.this.arrayTitles.size(); b++) {
              if (i == ((MyListData)DragListAdapter.this.arrayTitles.get(b)).groupId) {
                if (((MyListData)DragListAdapter.this.arrayTitles.get(b)).tag) {
                  ((MyListData)DragListAdapter.this.arrayTitles.get(b)).tag = false;
                } else {
                  ((MyListData)DragListAdapter.this.arrayTitles.get(b)).tag = true;
                } 
                if (!((MyListData)DragListAdapter.this.arrayTitles.get(b)).isGroup)
                  if (((MyListData)DragListAdapter.this.arrayTitles.get(b)).isshow) {
                    ((MyListData)DragListAdapter.this.arrayTitles.get(b)).isshow = false;
                  } else {
                    ((MyListData)DragListAdapter.this.arrayTitles.get(b)).isshow = true;
                  }  
              } 
            } 
            DragListAdapter.this.notifyDataSetChanged();
          }
        });
    itemInfo.name.setText(myListData.name);
    if (myListData.isdel)
      itemInfo.tx_delete.setVisibility(0); 
    if (this.isChanged) {
      if (position == this.invisilePosition && !this.ShowItem) {
        itemInfo.name.setVisibility(4);
        itemInfo.light_img.setVisibility(4);
        itemInfo.reset_text.setVisibility(4);
      } 
      if (this.lastFlag != -1)
        if (this.lastFlag == 1) {
          if (position > this.invisilePosition)
            paramView.startAnimation(getFromSelfAnimation(0, -this.height)); 
        } else if (this.lastFlag == 0 && position < this.invisilePosition) {
          paramView.startAnimation(getFromSelfAnimation(0, this.height));
        }  
    } 
    this.ItemInfos.put(Integer.valueOf(position), itemInfo);
    paramView.setTag(myListData);
    if (((MyListData)this.arrayTitles.get(position)).isshow) {
      paramView.setVisibility(0);
    } else {
      paramView.setVisibility(8);
      paramView.setLayoutParams(new ViewGroup.LayoutParams(-1, 1));
    } 
    return paramView;
  }
  
  public void pastList() {
    this.arrayTitles.clear();
    for (MyListData myListData : this.mCopyList)
      this.arrayTitles.add(myListData); 
  }
  
  public void setCurrentDragPosition(int paramInt) {
    this.dragPosition = paramInt;
  }
  
  public void setDragListChange(DragListView.DragListChange paramDragListChange) {
    this.mDragListChange = paramDragListChange;
  }
  
  public void setHeight(int paramInt) {
    this.height = paramInt;
  }
  
  public void setInvisiblePosition(int paramInt) {
    this.invisilePosition = paramInt;
  }
  
  public void setIsSameDragDirection(boolean paramBoolean) {
    this.isSameDragDirection = paramBoolean;
  }
  
  public void setLastFlag(int paramInt) {
    this.lastFlag = paramInt;
  }
  
  public void showDropItem(boolean paramBoolean) {
    this.ShowItem = paramBoolean;
  }
  
  private class DelectClickListener implements View.OnClickListener {
    private MyListData data;
    
    private DelectClickListener() {}
    
    public void onClick(View param1View) {
      if (this.data == null && DragListAdapter.this.mDragListChange != null)
        return; 
      DragListAdapter.this.mDragListChange.delect(this.data);
    }
    
    public DelectClickListener setData(MyListData param1MyListData) {
      this.data = param1MyListData;
      return this;
    }
  }
  
  private class resetClickListener implements View.OnClickListener {
    private MyListData data;
    
    private resetClickListener() {}
    
    public void onClick(View param1View) {
      if (this.data == null && DragListAdapter.this.mDragListChange != null)
        return; 
      DragListAdapter.this.mDragListChange.resetname(this.data);
    }
    
    public resetClickListener setData(MyListData param1MyListData) {
      this.data = param1MyListData;
      return this;
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\managegroup\DragListAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */