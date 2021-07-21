package com.example.android.forrest.views.customnumberdialog.pickers;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.example.android.forrest.R;
import com.example.android.forrest.views.customnumberdialog.INumberPicker;

public class DistancePicker implements INumberPicker {
  private final View mContentView;

  public DistancePicker(LayoutInflater inflater) {
    mContentView = inflater.inflate(R.layout.dialog_content_distance, null);
    setUpNumberPickers();
  }

  private void setUpNumberPickers() {
    NumberPicker intPicker = mContentView.findViewById(R.id.km_int_picker);
    intPicker.setMinValue(1);
    intPicker.setMaxValue(70);
    intPicker.setValue(10);

    NumberPicker floatPicker = mContentView.findViewById(R.id.km_float_picker);
    floatPicker.setMinValue(0);
    floatPicker.setMaxValue(9);
  }

  @Override
  public int getTitle() {
    return R.string.set_distance;
  }

  @Override
  public View getContentView() {
    return mContentView;
  }

  @Override
  public Double getValueFromDialog(Dialog dialogView) {
    NumberPicker kmIntPicker = dialogView.findViewById(R.id.km_int_picker);
    NumberPicker kmFloatPicker = dialogView.findViewById(R.id.km_float_picker);

    return kmIntPicker.getValue() + 0.1 * kmFloatPicker.getValue();
  }
}
