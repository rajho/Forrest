package com.example.android.forrest.views.customlongdialog.pickers;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.example.android.forrest.R;
import com.example.android.forrest.views.customlongdialog.INumberPicker;

public class HeighPicker implements INumberPicker {

  private final View mContentView;

  public HeighPicker(LayoutInflater inflater) {
    mContentView = inflater.inflate(R.layout.dialog_content_height, null);
    setUpNumberPickers();
  }

  private void setUpNumberPickers() {
    NumberPicker heightPicker = mContentView.findViewById(R.id.height_picker);
    heightPicker.setMinValue(130);
    heightPicker.setMaxValue(210);
    heightPicker.setValue(170);
  }

  @Override
  public int getTitle() {
    return R.string.height;
  }

  @Override
  public View getContentView() {
    return mContentView;
  }

  @Override
  public Double getValueFromDialog(Dialog dialogView) {
    NumberPicker heightPicker = dialogView.findViewById(R.id.height_picker);
    return (double) heightPicker.getValue();
  }
}
