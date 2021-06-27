package com.qh.ui.dialog;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.qh.ui.dialog.listener.IEditQuantityClickListener;
import java.math.BigDecimal;

public class EditQuantityDialog extends BaseDialogFragment {
  private IEditQuantityClickListener listener;
  
  private Button mBtMinus;
  
  private Button mBtPlus;
  
  private EditText mEtCart;
  
  private TextView mTvCancel;
  
  private TextView mTvEnsure;
  
  private TextView mTvMessage;
  
  private BigDecimal maxValue = new BigDecimal(99999);
  
  public static EditQuantityDialog newInstance() {
    return new EditQuantityDialog();
  }
  
  public static EditQuantityDialog newInstance(int paramInt, String paramString1, String paramString2) {
    EditQuantityDialog editQuantityDialog = new EditQuantityDialog();
    Bundle bundle = new Bundle();
    bundle.putString("left", paramString1);
    bundle.putString("right", paramString2);
    bundle.putSerializable("value", Integer.valueOf(paramInt));
    editQuantityDialog.setArguments(bundle);
    return editQuantityDialog;
  }
  
  public static EditQuantityDialog newInstance(int paramInt, BigDecimal paramBigDecimal) {
    EditQuantityDialog editQuantityDialog = new EditQuantityDialog();
    Bundle bundle = new Bundle();
    bundle.putInt("value", paramInt);
    bundle.putSerializable("maxValue", paramBigDecimal);
    editQuantityDialog.setArguments(bundle);
    return editQuantityDialog;
  }
  
  public static EditQuantityDialog newInstance(String paramString, int paramInt, BigDecimal paramBigDecimal) {
    EditQuantityDialog editQuantityDialog = new EditQuantityDialog();
    Bundle bundle = new Bundle();
    bundle.putString("message", paramString);
    bundle.putInt("value", paramInt);
    bundle.putSerializable("maxValue", paramBigDecimal);
    editQuantityDialog.setArguments(bundle);
    return editQuantityDialog;
  }
  
  public static EditQuantityDialog newInstance(String paramString1, String paramString2, String paramString3, int paramInt, BigDecimal paramBigDecimal) {
    EditQuantityDialog editQuantityDialog = new EditQuantityDialog();
    Bundle bundle = new Bundle();
    bundle.putString("message", paramString1);
    bundle.putString("left", paramString2);
    bundle.putString("right", paramString3);
    bundle.putInt("value", paramInt);
    bundle.putSerializable("maxValue", paramBigDecimal);
    editQuantityDialog.setArguments(bundle);
    return editQuantityDialog;
  }
  
  public int getLayoutId() {
    return 2131361859;
  }
  
  protected void init(View paramView) {
    this.mTvCancel = (TextView)paramView.findViewById(2131231196);
    this.mTvEnsure = (TextView)paramView.findViewById(2131231207);
    this.mTvMessage = (TextView)paramView.findViewById(2131231212);
    this.mEtCart = (EditText)paramView.findViewById(2131230792);
    this.mBtMinus = (Button)paramView.findViewById(2131230760);
    this.mBtPlus = (Button)paramView.findViewById(2131230761);
    this.mEtCart.requestFocus();
    getDialog().getWindow().setSoftInputMode(16);
    Bundle bundle = getArguments();
    if (bundle != null) {
      if (bundle.containsKey("message")) {
        String str = bundle.getString("message");
        if (!TextUtils.isEmpty(str))
          this.mTvMessage.setText(str); 
      } 
      if (bundle.containsKey("left")) {
        String str = bundle.getString("left");
        if (!TextUtils.isEmpty(str))
          this.mTvCancel.setText(str); 
      } 
      if (bundle.containsKey("right")) {
        String str = bundle.getString("right");
        if (!TextUtils.isEmpty(str))
          this.mTvEnsure.setText(str); 
      } 
      if (bundle.containsKey("maxValue"))
        this.maxValue = (BigDecimal)bundle.getSerializable("maxValue"); 
      if (bundle.containsKey("value")) {
        int i = bundle.getInt("value");
        EditText editText = this.mEtCart;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
        stringBuilder.append(i);
        editText.setText(stringBuilder.toString());
        this.mEtCart.setSelection(String.valueOf(i).toString().length());
      } 
    } 
    this.mEtCart.addTextChangedListener(new TextWatcher() {
          public void afterTextChanged(Editable param1Editable) {
            String str = param1Editable.toString();
            if (!TextUtils.isEmpty(str)) {
              BigDecimal bigDecimal = new BigDecimal(str);
              if (bigDecimal.compareTo(new BigDecimal(0)) == 0 || bigDecimal.compareTo(new BigDecimal(0)) == -1) {
                EditQuantityDialog.this.mEtCart.setText("1");
                EditQuantityDialog.this.mEtCart.setSelection(1);
                return;
              } 
              if (bigDecimal.compareTo(EditQuantityDialog.this.maxValue) == 1) {
                EditQuantityDialog.this.mEtCart.setText(EditQuantityDialog.this.maxValue.toString());
                EditQuantityDialog.this.mEtCart.setSelection(EditQuantityDialog.this.maxValue.toString().length());
              } 
            } 
          }
          
          public void beforeTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
          
          public void onTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
        });
    this.mBtMinus.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            String str = EditQuantityDialog.this.mEtCart.getText().toString();
            if (!TextUtils.isEmpty(str) && (new BigDecimal(str)).compareTo(new BigDecimal(1)) == 1) {
              str = (new BigDecimal(str)).subtract(new BigDecimal(1)).toString();
              EditQuantityDialog.this.mEtCart.setText(str);
              EditQuantityDialog.this.mEtCart.setSelection(str.length());
            } 
          }
        });
    this.mBtPlus.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            String str = EditQuantityDialog.this.mEtCart.getText().toString();
            if (!TextUtils.isEmpty(str) && (new BigDecimal(str)).compareTo(EditQuantityDialog.this.maxValue) == -1) {
              str = (new BigDecimal(str)).add(new BigDecimal(1)).toString();
              EditQuantityDialog.this.mEtCart.setText(str);
              EditQuantityDialog.this.mEtCart.setSelection(str.length());
            } 
          }
        });
    this.mTvCancel.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (EditQuantityDialog.this.listener != null)
              EditQuantityDialog.this.listener.onCancelClick(); 
            ((InputMethodManager)EditQuantityDialog.this.getContext().getSystemService("input_method")).hideSoftInputFromWindow(EditQuantityDialog.this.getView().getWindowToken(), 0);
            EditQuantityDialog.this.dismiss();
          }
        });
    this.mTvEnsure.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (!TextUtils.isEmpty(EditQuantityDialog.this.mEtCart.getText().toString())) {
              if (EditQuantityDialog.this.listener != null)
                EditQuantityDialog.this.listener.onEnsureClick(EditQuantityDialog.this.mEtCart.getText().toString()); 
              ((InputMethodManager)EditQuantityDialog.this.getContext().getSystemService("input_method")).hideSoftInputFromWindow(EditQuantityDialog.this.getView().getWindowToken(), 0);
              EditQuantityDialog.this.dismiss();
            } else if (EditQuantityDialog.this.listener != null) {
              EditQuantityDialog.this.listener.onEnsureNull();
            } 
          }
        });
  }
  
  public void onStart() {
    super.onStart();
  }
  
  public void setOnClickListener(IEditQuantityClickListener paramIEditQuantityClickListener) {
    this.listener = paramIEditQuantityClickListener;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\q\\ui\dialog\EditQuantityDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */