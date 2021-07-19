package com.example.android.forrest.views.customlongdialog;

import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.example.android.forrest.views.customlongdialog.pickers.DistancePicker;
import com.example.android.forrest.views.customlongdialog.pickers.DurationPicker;
import com.example.android.forrest.views.customlongdialog.pickers.HeighPicker;
import com.example.android.forrest.views.customlongdialog.pickers.WeightPicker;

public class NumberPickerFactory {

  private final LayoutInflater mInflater;

  public NumberPickerFactory(LayoutInflater inflater) {
    mInflater = inflater;
  }

  public INumberPicker getNumberPicker(@NonNull PickerType type) {
    switch (type) {
      case HEIGHT: return new HeighPicker(mInflater);

      case WEIGHT: return new WeightPicker(mInflater);

      case DISTANCE: return new DistancePicker(mInflater);

      case DURATION: return new DurationPicker(mInflater);

      default: return null;
    }
  }
}
