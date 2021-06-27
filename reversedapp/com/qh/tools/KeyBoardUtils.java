package com.qh.tools;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyBoardUtils {
  private KeyBoardUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  
  public static void closeKeybord(Activity paramActivity, Context paramContext) {
    EditText editText;
    Window window = paramActivity.getWindow();
    View view2 = window.getCurrentFocus();
    View view1 = view2;
    if (view2 == null) {
      View view = window.getDecorView();
      view2 = view.findViewWithTag("keyboardTagView");
      view1 = view2;
      if (view2 == null) {
        editText = new EditText(window.getContext());
        editText.setTag("keyboardTagView");
        ((ViewGroup)view).addView((View)editText, 0, 0);
      } 
      editText.requestFocus();
    } 
    closeKeybord((View)editText, paramContext);
  }
  
  public static void closeKeybord(View paramView, Context paramContext) {
    if (paramView instanceof EditText)
      ((EditText)paramView).clearFocus(); 
    InputMethodManager inputMethodManager = (InputMethodManager)paramContext.getSystemService("input_method");
    if (inputMethodManager == null)
      return; 
    inputMethodManager.hideSoftInputFromWindow(paramView.getWindowToken(), 0);
  }
  
  public static boolean isShouldHideInput(View paramView, MotionEvent paramMotionEvent) {
    if (paramView != null && paramView instanceof EditText) {
      int[] arrayOfInt = new int[2];
      arrayOfInt[0] = 0;
      arrayOfInt[1] = 0;
      paramView.getLocationInWindow(arrayOfInt);
      int i = arrayOfInt[0];
      int j = arrayOfInt[1];
      int k = paramView.getHeight();
      int m = paramView.getWidth();
      return !(paramMotionEvent.getX() > i && paramMotionEvent.getX() < (m + i) && paramMotionEvent.getY() > j && paramMotionEvent.getY() < (k + j));
    } 
    return false;
  }
  
  public static void openKeybord(Context paramContext) {
    InputMethodManager inputMethodManager = (InputMethodManager)paramContext.getSystemService("input_method");
    if (inputMethodManager == null)
      return; 
    inputMethodManager.toggleSoftInput(2, 1);
  }
  
  public static void openKeybord(EditText paramEditText, Context paramContext) {
    paramEditText.setFocusable(true);
    paramEditText.setFocusableInTouchMode(true);
    paramEditText.requestFocus();
    InputMethodManager inputMethodManager = (InputMethodManager)paramContext.getSystemService("input_method");
    if (inputMethodManager == null)
      return; 
    inputMethodManager.toggleSoftInput(2, 1);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\tools\KeyBoardUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */