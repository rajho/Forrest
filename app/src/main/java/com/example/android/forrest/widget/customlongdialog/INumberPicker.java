package com.example.android.forrest.widget.customlongdialog;

import android.app.Dialog;
import android.view.View;

public interface INumberPicker {
  int getTitle();

  View getContentView();

  Double getValueFromDialog(Dialog dialogView);
}
