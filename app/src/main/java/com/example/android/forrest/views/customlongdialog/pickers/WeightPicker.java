package com.example.android.forrest.views.customlongdialog.pickers;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.example.android.forrest.R;
import com.example.android.forrest.views.customlongdialog.INumberPicker;

public class WeightPicker implements INumberPicker {
  private final View mContentView;

  public WeightPicker(LayoutInflater inflater) {
    mContentView = inflater.inflate(R.layout.dialog_content_weight, null);
    setUpNumberPickers();
  }

  private void setUpNumberPickers() {
    NumberPicker intPicker = mContentView.findViewById(R.id.kg_int_picker);
    intPicker.setMinValue(30);
    intPicker.setMaxValue(110);
    intPicker.setValue(70);

    NumberPicker floatPicker = mContentView.findViewById(R.id.kg_float_picker);
    floatPicker.setMinValue(0);
    floatPicker.setMaxValue(9);
  }

  @Override
  public int getTitle() {
    return R.string.weight;
  }

  @Override
  public View getContentView() {
    return mContentView;
  }

  @Override
  public Double getValueFromDialog(Dialog dialogView) {
    NumberPicker kgIntPicker = dialogView.findViewById(R.id.kg_int_picker);
    NumberPicker kgFloatPicker = dialogView.findViewById(R.id.kg_float_picker);

    return kgIntPicker.getValue() + 0.1 * kgFloatPicker.getValue();
  }
}
