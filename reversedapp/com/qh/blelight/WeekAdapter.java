package com.qh.blelight;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import java.util.ArrayList;

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.MyViewHolder> {
  private Context context;
  
  private int mClickPositon;
  
  private OnWeekClickListener onWeekClickListener;
  
  private ArrayList<Integer> weekList = new ArrayList<Integer>();
  
  public WeekAdapter(Context paramContext, ArrayList<Integer> paramArrayList) {
    this.context = paramContext;
    this.weekList.clear();
    this.weekList.addAll(paramArrayList);
  }
  
  public void changePostion(int paramInt) {
    if (this.mClickPositon != paramInt) {
      this.mClickPositon = paramInt;
      notifyDataSetChanged();
    } 
  }
  
  public int getItemCount() {
    return this.weekList.size();
  }
  
  public void onBindViewHolder(MyViewHolder paramMyViewHolder, final int position) {
    int i = ((Integer)this.weekList.get(position)).intValue();
    paramMyViewHolder.itemView.setTag(Integer.valueOf(position));
    if (this.mClickPositon == position) {
      paramMyViewHolder.img_type_mod.setPadding(11, 11, 11, 11);
      paramMyViewHolder.img_q.setVisibility(0);
    } else {
      paramMyViewHolder.img_type_mod.setPadding(22, 22, 22, 22);
      paramMyViewHolder.img_q.setVisibility(4);
    } 
    if (i != 0) {
      paramMyViewHolder.img_type_mod.setImageResource(i);
      paramMyViewHolder.img_type_mod.setVisibility(0);
    } else {
      paramMyViewHolder.img_type_mod.setVisibility(4);
    } 
    paramMyViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (WeekAdapter.this.onWeekClickListener != null)
              WeekAdapter.this.onWeekClickListener.scrollMid(position); 
          }
        });
  }
  
  public MyViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
    return new MyViewHolder(View.inflate(this.context, 2131361888, null));
  }
  
  public void setOnWeekClickListener(OnWeekClickListener paramOnWeekClickListener) {
    this.onWeekClickListener = paramOnWeekClickListener;
  }
  
  class MyViewHolder extends RecyclerView.ViewHolder {
    FrameLayout flView;
    
    ImageView img_q;
    
    ImageView img_type_mod;
    
    MyViewHolder(View param1View) {
      super(param1View);
      this.flView = (FrameLayout)param1View.findViewById(2131230803);
      this.img_type_mod = (ImageView)param1View.findViewById(2131230930);
      this.img_q = (ImageView)param1View.findViewById(2131230920);
    }
  }
  
  public static interface OnWeekClickListener {
    void scrollMid(int param1Int);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\blelight\WeekAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */