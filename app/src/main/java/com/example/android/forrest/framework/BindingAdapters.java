package com.example.android.forrest.framework;

import android.annotation.SuppressLint;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.LiveData;

import com.example.android.forrest.R;
import com.example.android.forrest.utils.FitnessOperations;
import com.google.android.material.timepicker.TimeFormat;

import java.text.DecimalFormat;
import java.util.Locale;

public class BindingAdapters {

  @BindingAdapter("dropdownOptions")
  public static void setDropdownOptions(AutoCompleteTextView view, String[] options) {
    ArrayAdapter<String> adapter = new ArrayAdapter<>(
        view.getContext(),
        R.layout.list_item_frequency_dropdown,
        options
    );
    view.setAdapter(adapter);
    view.setInputType(InputType.TYPE_NULL);
    view.setText(view.getResources().getString(R.string.select), false);
  }

  @SuppressLint("SetTextI18n")
  @BindingAdapter({ "duration", "distance"})
  public static void setRhythm(TextView view, Long duration, Double distance) {
    String formattedRhythm = FitnessOperations.getFormattedRhythm(duration, distance);
    view.setText(formattedRhythm);
  }

  @BindingAdapter("distance")
  public static void setDistance(TextView view, Double distance) {
    DecimalFormat df = new DecimalFormat("####0.0#");
    view.setText(String.format("%s km", df.format(distance)));
  }

  @BindingAdapter("calories")
  public static void setCalories(TextView view, Double calories) {
    DecimalFormat df = new DecimalFormat("####0.0");
    view.setText(String.format("%s cal", df.format(calories)));
  }


}
