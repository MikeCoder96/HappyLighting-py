package com.qh.ui.dialog;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.qh.ui.dialog.listener.IEditQuantityClickListener;
import java.math.BigDecimal;

public class EditDialog extends BaseDialogFragment {
  private InputFilter filter = new InputFilter() {
      public CharSequence filter(CharSequence param1CharSequence, int param1Int1, int param1Int2, Spanned param1Spanned, int param1Int3, int param1Int4) {
        return param1CharSequence.equals(" ") ? "" : null;
      }
    };
  
  private BigDecimal initValue;
  
  private IEditQuantityClickListener listener;
  
  private EditText mEt;
  
  private TextView mTvCancel;
  
  private TextView mTvEnsure;
  
  private TextView mTvMessage;
  
  private BigDecimal maxValue;
  
  private void initNumber(Bundle paramBundle) {
    if (paramBundle.containsKey("maxValue"))
      this.maxValue = (BigDecimal)paramBundle.getSerializable("maxValue"); 
    if (paramBundle.containsKey("value")) {
      this.initValue = (BigDecimal)paramBundle.getSerializable("value");
      this.mEt.setText(this.initValue.toString());
      this.mEt.setSelection(this.initValue.toString().length());
    } 
    this.mEt.addTextChangedListener(new TextWatcher() {
          public void afterTextChanged(Editable param1Editable) {
            String str = param1Editable.toString();
            if (!TextUtils.isEmpty(str)) {
              BigDecimal bigDecimal = new BigDecimal(str);
              if (bigDecimal.compareTo(new BigDecimal(0)) == -1) {
                EditDialog.this.mEt.setText("0");
                EditDialog.this.mEt.setSelection(1);
              } else if (bigDecimal.compareTo(EditDialog.this.maxValue) == 1) {
                EditDialog.this.mEt.setText(EditDialog.this.maxValue.toString());
                EditDialog.this.mEt.setSelection(EditDialog.this.maxValue.toString().length());
              } 
            } 
          }
          
          public void beforeTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
          
          public void onTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
        });
  }
  
  public static EditDialog newInstance(String paramString1, String paramString2, String paramString3, String paramString4, BigDecimal paramBigDecimal1, BigDecimal paramBigDecimal2, String paramString5) {
    EditDialog editDialog = new EditDialog();
    Bundle bundle = new Bundle();
    bundle.putString("message", paramString1);
    bundle.putString("content", paramString4);
    bundle.putString("left", paramString2);
    bundle.putString("right", paramString3);
    bundle.putSerializable("value", paramBigDecimal1);
    bundle.putSerializable("maxValue", paramBigDecimal2);
    bundle.putString("type", paramString5);
    editDialog.setArguments(bundle);
    return editDialog;
  }
  
  public static EditDialog newInstance(BigDecimal paramBigDecimal1, BigDecimal paramBigDecimal2, String paramString) {
    EditDialog editDialog = new EditDialog();
    Bundle bundle = new Bundle();
    bundle.putSerializable("value", paramBigDecimal1);
    bundle.putSerializable("maxValue", paramBigDecimal2);
    bundle.putString("type", paramString);
    editDialog.setArguments(bundle);
    return editDialog;
  }
  
  public int getLayoutId() {
    return 2131361858;
  }
  
  protected void init(View paramView) {
    this.mTvCancel = (TextView)paramView.findViewById(2131231196);
    this.mTvEnsure = (TextView)paramView.findViewById(2131231207);
    this.mTvMessage = (TextView)paramView.findViewById(2131231212);
    this.mEt = (EditText)paramView.findViewById(2131230792);
    this.mEt.setFilters(new InputFilter[] { this.filter });
    (new Handler()).postDelayed(new Runnable() {
          public void run() {
            EditDialog.this.mEt.requestFocus();
            ((InputMethodManager)EditDialog.this.getContext().getSystemService("input_method")).showSoftInput((View)EditDialog.this.mEt, 1);
          }
        }200L);
    Bundle bundle = getArguments();
    if (bundle != null) {
      String str = bundle.getString("type");
      if ("number".equals(str)) {
        this.mEt.setInputType(2);
        initNumber(bundle);
      } else if ("numberDecimal".equals(str)) {
        this.mEt.setInputType(8194);
        initNumber(bundle);
      } else {
        "any".equals(str);
      } 
      if (bundle.containsKey("message")) {
        str = bundle.getString("message");
        if (!TextUtils.isEmpty(str))
          this.mTvMessage.setText(str); 
      } 
      if (bundle.containsKey("content")) {
        str = bundle.getString("content");
        if (!TextUtils.isEmpty(str)) {
          this.mEt.setText(str);
          this.mEt.setSelection(str.length());
        } 
      } 
      if (bundle.containsKey("left")) {
        str = bundle.getString("left");
        if (!TextUtils.isEmpty(str))
          this.mTvCancel.setText(str); 
      } 
      if (bundle.containsKey("right")) {
        String str1 = bundle.getString("right");
        if (!TextUtils.isEmpty(str1))
          this.mTvEnsure.setText(str1); 
      } 
    } 
    this.mTvCancel.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (EditDialog.this.listener != null)
              EditDialog.this.listener.onCancelClick(); 
            ((InputMethodManager)EditDialog.this.getContext().getSystemService("input_method")).hideSoftInputFromWindow(EditDialog.this.getView().getWindowToken(), 0);
            EditDialog.this.dismiss();
          }
        });
    this.mTvEnsure.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (!TextUtils.isEmpty(EditDialog.this.mEt.getText().toString())) {
              if (EditDialog.this.listener != null)
                EditDialog.this.listener.onEnsureClick(EditDialog.this.mEt.getText().toString()); 
              ((InputMethodManager)EditDialog.this.getContext().getSystemService("input_method")).hideSoftInputFromWindow(EditDialog.this.getView().getWindowToken(), 0);
              EditDialog.this.dismiss();
            } else if (EditDialog.this.listener != null) {
              EditDialog.this.listener.onEnsureNull();
            } 
          }
        });
  }
  
  public void onStart() {
    super.onStart();
    initWindowSize(-1);
  }
  
  public void setOnClickListener(IEditQuantityClickListener paramIEditQuantityClickListener) {
    this.listener = paramIEditQuantityClickListener;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\q\\ui\dialog\EditDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */