package com.qh.blelight;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.qh.tools.DBAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class GroupManagement extends Activity {
  public DBAdapter dbAdapter;
  
  private ImageView dragImageView;
  
  private int dragOffset;
  
  private int dragPoint;
  
  private int dragPosition;
  
  private int dragSrcPosition;
  
  private ArrayList<Integer> groupIDs = new ArrayList<Integer>();
  
  private ArrayList<String> groupNames = new ArrayList<String>();
  
  public int isExpandedID = -1;
  
  private int len = 5;
  
  private RelativeLayout lin_back;
  
  Handler mHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message param1Message) {
          if (param1Message.what == 2) {
            GroupManagement.this.finish();
            return false;
          } 
          GroupManagement.this.mMyExpandableListAdapter.setgroupNames();
          GroupManagement.this.myExpandableListView.setAdapter((ExpandableListAdapter)GroupManagement.this.mMyExpandableListAdapter);
          if (GroupManagement.this.isExpandedID != -1)
            GroupManagement.this.myExpandableListView.expandGroup(GroupManagement.this.isExpandedID); 
          GroupManagement.this.mMyExpandableListAdapter.notifyDataSetChanged();
          return false;
        }
      });
  
  public MyApplication mMyApplication;
  
  public MyMExpandableListAdapter mMyExpandableListAdapter;
  
  private Resources mResources;
  
  public Context mcontext;
  
  public ExpandableListView myExpandableListView;
  
  private RelativeLayout rel_add;
  
  public RelativeLayout rel_main;
  
  private WindowManager windowManager;
  
  private WindowManager.LayoutParams windowParams;
  
  private void setOnTouch() {
    this.myExpandableListView.setOnTouchListener(new View.OnTouchListener() {
          private float startX = 0.0F;
          
          private float startY = 0.0F;
          
          @SuppressLint({"NewApi"})
          public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
            int i;
            switch (param1MotionEvent.getAction()) {
              default:
                return false;
              case 2:
                param1MotionEvent.getX();
                i = (int)param1MotionEvent.getY();
                GroupManagement.this.onDrag(i);
                if (param1View.getHeight() - param1MotionEvent.getY() < 50.0F)
                  GroupManagement.this.myExpandableListView.scrollListBy(50); 
                if (param1MotionEvent.getY() < 50.0F)
                  GroupManagement.this.myExpandableListView.scrollListBy(-50); 
              case 1:
                GroupManagement.this.stopDrag();
                param1MotionEvent.getY();
                if (GroupManagement.this.mMyExpandableListAdapter.moveV != null) {
                  MyMExpandableListAdapter.MChildViewHolder mChildViewHolder = (MyMExpandableListAdapter.MChildViewHolder)GroupManagement.this.mMyExpandableListAdapter.moveV.getTag();
                  if (mChildViewHolder != null) {
                    i = GroupManagement.this.getGroupPos((int)param1MotionEvent.getRawY());
                    if (i != GroupManagement.this.isExpandedID) {
                      GroupManagement.this.mMyExpandableListAdapter.move(mChildViewHolder.groupPosition, i, mChildViewHolder.childPosition);
                      GroupManagement.this.mMyExpandableListAdapter.notifyDataSetChanged();
                    } 
                  } 
                  GroupManagement.this.mMyExpandableListAdapter.moveV = null;
                } 
              case 0:
                break;
            } 
            this.startX = param1MotionEvent.getX();
            this.startY = param1MotionEvent.getY();
            if (GroupManagement.this.mMyExpandableListAdapter.moveV != null) {
              GroupManagement.access$202(GroupManagement.this, (int)param1MotionEvent.getY() - GroupManagement.this.mMyExpandableListAdapter.moveV.getTop());
              GroupManagement.access$302(GroupManagement.this, (int)(param1MotionEvent.getRawY() - (int)param1MotionEvent.getY()));
            } 
            GroupManagement.this.mHandler.postDelayed(new Runnable() {
                  public void run() {
                    HashMap<Integer, View> hashMap = GroupManagement.this.mMyExpandableListAdapter.mViews;
                    Iterator<Integer> iterator = hashMap.keySet().iterator();
                    while (iterator.hasNext()) {
                      View view = hashMap.get(iterator.next());
                      if (view != null)
                        view.getLocationOnScreen(new int[] { 0, 0 }, ); 
                    } 
                  }
                },  1000L);
            GroupManagement.this.getFromSelfAnimation(0, 20);
            GroupManagement.this.mHandler.postDelayed(new Runnable() {
                  public void run() {
                    if (GroupManagement.this.mMyExpandableListAdapter.moveV != null) {
                      GroupManagement.this.mMyExpandableListAdapter.moveV.buildDrawingCache();
                      Bitmap bitmap = Bitmap.createBitmap(GroupManagement.this.mMyExpandableListAdapter.moveV.getDrawingCache());
                      GroupManagement.this.startDrag(bitmap, (int)GroupManagement.null.this.startY);
                    } 
                  }
                }400L);
          }
        });
  }
  
  private void startDrag(Bitmap paramBitmap, int paramInt) {
    this.windowParams = new WindowManager.LayoutParams();
    this.windowParams.gravity = 48;
    this.windowParams.x = 0;
    this.windowParams.y = paramInt - this.dragPoint + this.dragOffset;
    this.windowParams.width = -2;
    this.windowParams.height = -2;
    this.windowParams.flags = 408;
    this.windowParams.windowAnimations = 0;
    this.windowParams.alpha = 0.8F;
    this.windowParams.format = -3;
    ImageView imageView = new ImageView(this.mcontext);
    imageView.setImageBitmap(paramBitmap);
    this.windowManager.addView((View)imageView, (ViewGroup.LayoutParams)this.windowParams);
    this.dragImageView = imageView;
  }
  
  public Animation getAbsMoveAnimation2(int paramInt1, int paramInt2) {
    TranslateAnimation translateAnimation = new TranslateAnimation(0, paramInt1, 1, 0.0F, 0, paramInt2, 1, 0.0F);
    translateAnimation.setInterpolator((Interpolator)new AccelerateDecelerateInterpolator());
    translateAnimation.setFillAfter(true);
    translateAnimation.setDuration(200L);
    translateAnimation.setInterpolator((Interpolator)new AccelerateInterpolator());
    return (Animation)translateAnimation;
  }
  
  public Animation getFromSelfAnimation(int paramInt1, int paramInt2) {
    TranslateAnimation translateAnimation = new TranslateAnimation(1, 0.0F, 0, paramInt1, 1, 0.0F, 0, paramInt2);
    translateAnimation.setInterpolator((Interpolator)new AccelerateDecelerateInterpolator());
    translateAnimation.setFillAfter(true);
    translateAnimation.setDuration(20L);
    translateAnimation.setInterpolator((Interpolator)new AccelerateInterpolator());
    return (Animation)translateAnimation;
  }
  
  public int getGroupPos(int paramInt) {
    if (this.mMyExpandableListAdapter.moveV == null)
      return -1; 
    Iterator<Integer> iterator = this.mMyExpandableListAdapter.mViews.keySet().iterator();
    ArrayList<Integer> arrayList = new ArrayList();
    while (iterator.hasNext())
      arrayList.add(iterator.next()); 
    Collections.sort(arrayList);
    for (byte b = 0; b < arrayList.size(); b++) {
      byte b1;
      int i = ((Integer)arrayList.get(b)).intValue();
      if (i == this.isExpandedID) {
        b1 = ((HashMap)this.mMyExpandableListAdapter.groupViews.get(Integer.valueOf(i))).size() * 121;
      } else {
        b1 = 0;
      } 
      View view = this.mMyExpandableListAdapter.mViews.get(Integer.valueOf(i));
      if (view != null) {
        int[] arrayOfInt = new int[2];
        arrayOfInt[0] = 0;
        arrayOfInt[1] = 0;
        view.getLocationOnScreen(arrayOfInt);
        int j = arrayOfInt[1];
        if (paramInt < 340)
          return 0; 
        if (j + b1 > paramInt - 150)
          return i; 
      } else {
        Log.e("", "mView==null");
      } 
    } 
    return -1;
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131361829);
    this.mResources = getResources();
    this.lin_back = (RelativeLayout)findViewById(2131230957);
    this.rel_add = (RelativeLayout)findViewById(2131231054);
    this.rel_main = (RelativeLayout)findViewById(2131231070);
    if (this.mMyApplication.bgsrc.length > this.mMyApplication.typebg)
      this.rel_main.setBackgroundResource(this.mMyApplication.bgsrc[this.mMyApplication.typebg]); 
    this.lin_back.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            Intent intent = new Intent();
            intent.putExtra("result", 1);
            GroupManagement.this.setResult(-1, intent);
            GroupManagement.this.finish();
          }
        });
    this.rel_add.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            final EditText et = new EditText(GroupManagement.this.mcontext);
            AlertDialog.Builder builder2 = new AlertDialog.Builder(GroupManagement.this.mcontext);
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("");
            stringBuilder2.append(GroupManagement.this.mcontext.getResources().getString(2131623943));
            builder2 = builder2.setTitle(stringBuilder2.toString()).setIcon(17301659).setView((View)editText);
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("");
            stringBuilder2.append(GroupManagement.this.mcontext.getResources().getString(2131624047));
            AlertDialog.Builder builder1 = builder2.setPositiveButton(stringBuilder2.toString(), new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface param2DialogInterface, int param2Int) {
                    String str = et.getText().toString();
                    if ("".equals(str))
                      return; 
                    StringBuilder stringBuilder1 = new StringBuilder();
                    stringBuilder1.append("name = ");
                    stringBuilder1.append(str);
                    Log.e("", stringBuilder1.toString());
                    ContentValues contentValues = new ContentValues();
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("");
                    stringBuilder2.append(str);
                    contentValues.put("groupName", stringBuilder2.toString());
                    GroupManagement.this.dbAdapter.insert("mygroup", contentValues);
                    GroupManagement.this.setListData();
                    GroupManagement.this.mHandler.postDelayed(new Runnable() {
                          public void run() {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("groupNames ");
                            stringBuilder.append(GroupManagement.this.groupNames.size());
                            stringBuilder.append(" groupIDs ");
                            stringBuilder.append(GroupManagement.this.groupIDs.size());
                            Log.e("", stringBuilder.toString());
                            GroupManagement.this.mMyExpandableListAdapter.setgroupNames();
                            GroupManagement.this.myExpandableListView.setAdapter((ExpandableListAdapter)GroupManagement.this.mMyExpandableListAdapter);
                          }
                        }50L);
                  }
                });
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append("");
            stringBuilder1.append(GroupManagement.this.mcontext.getResources().getString(2131624006));
            builder1.setNegativeButton(stringBuilder1.toString(), null).show();
          }
        });
    this.dbAdapter = DBAdapter.init((Context)this);
    this.dbAdapter.open();
    this.mcontext = (Context)this;
    this.windowManager = (WindowManager)this.mcontext.getSystemService("window");
    this.mMyApplication = (MyApplication)getApplication();
    this.myExpandableListView = (ExpandableListView)findViewById(2131230985);
    this.mMyExpandableListAdapter = new MyMExpandableListAdapter(this.mcontext, this, this.dbAdapter, this.mMyApplication.mBluetoothLeService);
    this.mMyExpandableListAdapter.mHandler = this.mHandler;
    setOnTouch();
    this.myExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
          public void onGroupExpand(int param1Int) {
            int i = GroupManagement.this.myExpandableListView.getExpandableListAdapter().getGroupCount();
            for (byte b = 0; b < i; b++) {
              if (param1Int != b)
                GroupManagement.this.myExpandableListView.collapseGroup(b); 
            } 
            GroupManagement.this.isExpandedID = param1Int;
          }
        });
    this.mMyExpandableListAdapter.setgroupNames();
    this.myExpandableListView.setAdapter((ExpandableListAdapter)this.mMyExpandableListAdapter);
    this.myExpandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
          public boolean onItemLongClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
            boolean bool = ((Boolean)param1View.getTag(2131230821)).booleanValue();
            param1Int = ((Integer)param1View.getTag(2131230825)).intValue();
            int i = ((Integer)param1View.getTag(2131230826)).intValue();
            if (bool) {
              if (param1Int == 0)
                return true; 
              MyMExpandableListAdapter.GroupViewHolder groupViewHolder = (MyMExpandableListAdapter.GroupViewHolder)param1View.getTag(2131230827);
              if (groupViewHolder != null)
                groupViewHolder.delect.setVisibility(0); 
            } else {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("num = ");
              stringBuilder.append(param1Int);
              stringBuilder.append(" child = ");
              stringBuilder.append(i);
              Log.e("", stringBuilder.toString());
              MyMExpandableListAdapter.MChildViewHolder mChildViewHolder = (MyMExpandableListAdapter.MChildViewHolder)param1View.getTag(2131230827);
              if (mChildViewHolder != null)
                mChildViewHolder.tx_delete.setVisibility(0); 
            } 
            return true;
          }
        });
    this.myExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
          public boolean onChildClick(ExpandableListView param1ExpandableListView, View param1View, int param1Int1, int param1Int2, long param1Long) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("--? ");
            stringBuilder.append(param1Int1);
            stringBuilder.append(" ");
            stringBuilder.append(param1Int2);
            Log.e("", stringBuilder.toString());
            MyMExpandableListAdapter.MChildViewHolder mChildViewHolder = (MyMExpandableListAdapter.MChildViewHolder)param1View.getTag(2131230827);
            if (mChildViewHolder != null)
              mChildViewHolder.tx_delete.setVisibility(8); 
            return false;
          }
        });
  }
  
  public void onDrag(int paramInt) {
    int i = this.dragPoint;
    if (this.dragImageView != null && paramInt - i >= 0) {
      this.windowParams.alpha = 1.0F;
      this.windowParams.y = paramInt - this.dragPoint + this.dragOffset;
      this.windowManager.updateViewLayout((View)this.dragImageView, (ViewGroup.LayoutParams)this.windowParams);
    } 
  }
  
  public void setListData() {
    this.groupNames.clear();
    this.groupIDs.clear();
    Cursor cursor = this.dbAdapter.queryAllData();
    while (cursor.moveToNext()) {
      String str = cursor.getString(cursor.getColumnIndex("groupName"));
      int i = cursor.getInt(cursor.getColumnIndex("_id"));
      this.groupNames.add(str);
      this.groupIDs.add(Integer.valueOf(i));
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("-id = ");
      stringBuilder.append(i);
      Log.e("", stringBuilder.toString());
    } 
  }
  
  public void stopDrag() {
    if (this.dragImageView != null) {
      this.windowManager.removeView((View)this.dragImageView);
      this.dragImageView = null;
    } 
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\blelight\GroupManagement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */