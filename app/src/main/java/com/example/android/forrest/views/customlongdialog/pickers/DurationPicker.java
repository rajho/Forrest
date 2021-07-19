package com.example.android.forrest.views.customlongdialog.pickers;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.example.android.forrest.R;
import com.example.android.forrest.views.customlongdialog.INumberPicker;

import java.util.Locale;

public class DurationPicker implements INumberPicker {

  private final View mContentView;

  public DurationPicker(LayoutInflater inflater) {
    mContentView = inflater.inflate(R.layout.dialog_content_duration, null);
    setUpNumberPickers();
  }

  private void setUpNumberPickers() {
    NumberPicker.Formatter formatter = value -> String.format(Locale.getDefault(), "%02d", value);

    NumberPicker hoursPicker = mContentView.findViewById(R.id.hour_picker);
    hoursPicker.setMinValue(0);
    hoursPicker.setMaxValue(23);
    hoursPicker.setFormatter(formatter);

    NumberPicker minutesPicker = mContentView.findViewById(R.id.minutes_picker);
    minutesPicker.setMinValue(0);
    minutesPicker.setMaxValue(59);
    minutesPicker.setFormatter(formatter);
  }

  @Override
  public int getTitle() {
    return R.string.set_duration;
  }

  @Override
  public View getContentView() {
    return mContentView;
  }

  @Override
  public Double getValueFromDialog(Dialog dialogView) {
    NumberPicker hourPicker = dialogView.findViewById(R.id.hour_picker);
    NumberPicker minutePicker = dialogView.findViewById(R.id.minutes_picker);

    /* Hour represented as a double h.m
     * 22:15 would be represented by a double 22.15
     */
    return hourPicker.getValue() + 0.01 * minutePicker.getValue();
  }
}
