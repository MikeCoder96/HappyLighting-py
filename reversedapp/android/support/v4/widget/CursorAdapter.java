package android.support.v4.widget;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Handler;
import android.support.annotation.RestrictTo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.FilterQueryProvider;
import android.widget.Filterable;

public abstract class CursorAdapter extends BaseAdapter implements Filterable, CursorFilter.CursorFilterClient {
  @Deprecated
  public static final int FLAG_AUTO_REQUERY = 1;
  
  public static final int FLAG_REGISTER_CONTENT_OBSERVER = 2;
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  protected boolean mAutoRequery;
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  protected ChangeObserver mChangeObserver;
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  protected Context mContext;
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  protected Cursor mCursor;
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  protected CursorFilter mCursorFilter;
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  protected DataSetObserver mDataSetObserver;
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  protected boolean mDataValid;
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  protected FilterQueryProvider mFilterQueryProvider;
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  protected int mRowIDColumn;
  
  @Deprecated
  public CursorAdapter(Context paramContext, Cursor paramCursor) {
    init(paramContext, paramCursor, 1);
  }
  
  public CursorAdapter(Context paramContext, Cursor paramCursor, int paramInt) {
    init(paramContext, paramCursor, paramInt);
  }
  
  public CursorAdapter(Context paramContext, Cursor paramCursor, boolean paramBoolean) {
    byte b;
    if (paramBoolean) {
      b = 1;
    } else {
      b = 2;
    } 
    init(paramContext, paramCursor, b);
  }
  
  public abstract void bindView(View paramView, Context paramContext, Cursor paramCursor);
  
  public void changeCursor(Cursor paramCursor) {
    paramCursor = swapCursor(paramCursor);
    if (paramCursor != null)
      paramCursor.close(); 
  }
  
  public CharSequence convertToString(Cursor paramCursor) {
    String str;
    if (paramCursor == null) {
      str = "";
    } else {
      str = str.toString();
    } 
    return str;
  }
  
  public int getCount() {
    return (this.mDataValid && this.mCursor != null) ? this.mCursor.getCount() : 0;
  }
  
  public Cursor getCursor() {
    return this.mCursor;
  }
  
  public View getDropDownView(int paramInt, View paramView, ViewGroup paramViewGroup) {
    if (this.mDataValid) {
      this.mCursor.moveToPosition(paramInt);
      View view = paramView;
      if (paramView == null)
        view = newDropDownView(this.mContext, this.mCursor, paramViewGroup); 
      bindView(view, this.mContext, this.mCursor);
      return view;
    } 
    return null;
  }
  
  public Filter getFilter() {
    if (this.mCursorFilter == null)
      this.mCursorFilter = new CursorFilter(this); 
    return this.mCursorFilter;
  }
  
  public FilterQueryProvider getFilterQueryProvider() {
    return this.mFilterQueryProvider;
  }
  
  public Object getItem(int paramInt) {
    if (this.mDataValid && this.mCursor != null) {
      this.mCursor.moveToPosition(paramInt);
      return this.mCursor;
    } 
    return null;
  }
  
  public long getItemId(int paramInt) {
    return (this.mDataValid && this.mCursor != null) ? (this.mCursor.moveToPosition(paramInt) ? this.mCursor.getLong(this.mRowIDColumn) : 0L) : 0L;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
    if (this.mDataValid) {
      if (this.mCursor.moveToPosition(paramInt)) {
        View view = paramView;
        if (paramView == null)
          view = newView(this.mContext, this.mCursor, paramViewGroup); 
        bindView(view, this.mContext, this.mCursor);
        return view;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("couldn't move cursor to position ");
      stringBuilder.append(paramInt);
      throw new IllegalStateException(stringBuilder.toString());
    } 
    throw new IllegalStateException("this should only be called when the cursor is valid");
  }
  
  public boolean hasStableIds() {
    return true;
  }
  
  void init(Context paramContext, Cursor paramCursor, int paramInt) {
    byte b;
    boolean bool = false;
    if ((paramInt & 0x1) == 1) {
      paramInt |= 0x2;
      this.mAutoRequery = true;
    } else {
      this.mAutoRequery = false;
    } 
    if (paramCursor != null)
      bool = true; 
    this.mCursor = paramCursor;
    this.mDataValid = bool;
    this.mContext = paramContext;
    if (bool) {
      b = paramCursor.getColumnIndexOrThrow("_id");
    } else {
      b = -1;
    } 
    this.mRowIDColumn = b;
    if ((paramInt & 0x2) == 2) {
      this.mChangeObserver = new ChangeObserver();
      this.mDataSetObserver = new MyDataSetObserver();
    } else {
      this.mChangeObserver = null;
      this.mDataSetObserver = null;
    } 
    if (bool) {
      if (this.mChangeObserver != null)
        paramCursor.registerContentObserver(this.mChangeObserver); 
      if (this.mDataSetObserver != null)
        paramCursor.registerDataSetObserver(this.mDataSetObserver); 
    } 
  }
  
  @Deprecated
  protected void init(Context paramContext, Cursor paramCursor, boolean paramBoolean) {
    byte b;
    if (paramBoolean) {
      b = 1;
    } else {
      b = 2;
    } 
    init(paramContext, paramCursor, b);
  }
  
  public View newDropDownView(Context paramContext, Cursor paramCursor, ViewGroup paramViewGroup) {
    return newView(paramContext, paramCursor, paramViewGroup);
  }
  
  public abstract View newView(Context paramContext, Cursor paramCursor, ViewGroup paramViewGroup);
  
  protected void onContentChanged() {
    if (this.mAutoRequery && this.mCursor != null && !this.mCursor.isClosed())
      this.mDataValid = this.mCursor.requery(); 
  }
  
  public Cursor runQueryOnBackgroundThread(CharSequence paramCharSequence) {
    return (this.mFilterQueryProvider != null) ? this.mFilterQueryProvider.runQuery(paramCharSequence) : this.mCursor;
  }
  
  public void setFilterQueryProvider(FilterQueryProvider paramFilterQueryProvider) {
    this.mFilterQueryProvider = paramFilterQueryProvider;
  }
  
  public Cursor swapCursor(Cursor paramCursor) {
    if (paramCursor == this.mCursor)
      return null; 
    Cursor cursor = this.mCursor;
    if (cursor != null) {
      if (this.mChangeObserver != null)
        cursor.unregisterContentObserver(this.mChangeObserver); 
      if (this.mDataSetObserver != null)
        cursor.unregisterDataSetObserver(this.mDataSetObserver); 
    } 
    this.mCursor = paramCursor;
    if (paramCursor != null) {
      if (this.mChangeObserver != null)
        paramCursor.registerContentObserver(this.mChangeObserver); 
      if (this.mDataSetObserver != null)
        paramCursor.registerDataSetObserver(this.mDataSetObserver); 
      this.mRowIDColumn = paramCursor.getColumnIndexOrThrow("_id");
      this.mDataValid = true;
      notifyDataSetChanged();
    } else {
      this.mRowIDColumn = -1;
      this.mDataValid = false;
      notifyDataSetInvalidated();
    } 
    return cursor;
  }
  
  private class ChangeObserver extends ContentObserver {
    ChangeObserver() {
      super(new Handler());
    }
    
    public boolean deliverSelfNotifications() {
      return true;
    }
    
    public void onChange(boolean param1Boolean) {
      CursorAdapter.this.onContentChanged();
    }
  }
  
  private class MyDataSetObserver extends DataSetObserver {
    public void onChanged() {
      CursorAdapter.this.mDataValid = true;
      CursorAdapter.this.notifyDataSetChanged();
    }
    
    public void onInvalidated() {
      CursorAdapter.this.mDataValid = false;
      CursorAdapter.this.notifyDataSetInvalidated();
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\widget\CursorAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */