package com.example.android.forrest.utils;

import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.databinding.BindingAdapter;

import com.example.android.forrest.R;

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
}
