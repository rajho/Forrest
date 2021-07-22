package com.example.android.forrest.utils;

import android.annotation.SuppressLint;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.LiveData;

import com.example.android.forrest.R;
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
    if (duration < 1000 || distance == null || distance == 0.0) {
      view.setText(String.format(Locale.getDefault(), "%02d:%02d min/km", 0, 0));
      return;
    }

    double  rhytmInMillis = duration / distance;
    Integer minutes       = (int) (rhytmInMillis / (60 * 1000));
    Integer remaining     = (int) (duration % (60 * 1000));
    Integer seconds       = remaining / 1000;

    view.setText(String.format(Locale.getDefault(), "%02d:%02d min/km", minutes, seconds));
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
