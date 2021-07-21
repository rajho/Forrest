package com.example.android.forrest.views.customnumberdialog;

import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.example.android.forrest.views.customnumberdialog.pickers.DistancePicker;
import com.example.android.forrest.views.customnumberdialog.pickers.DurationPicker;
import com.example.android.forrest.views.customnumberdialog.pickers.HeighPicker;
import com.example.android.forrest.views.customnumberdialog.pickers.WeightPicker;

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
