package com.example.android.forrest.views.customlongdialog;

import android.app.Dialog;
import android.view.View;

public interface INumberPicker {

  /**
   * @return title resourceId
   */
  int getTitle();

  /**
   * Return the view that will be the content of your dialog and
   * will be used to set the double value required by the activity or fragment
   * call the dialog
   *
   * @return view inflated from xml layout
   */
  View getContentView();

  /**
   * Implement this method to return the value as a double from your UI.
   * You get the access to the dialog in the param, and from there you can
   * call findViewById to get the views created in your layout.
   *
   * @param dialogView dialog
   * @return number value as a double
   */
  Double getValueFromDialog(Dialog dialogView);
}
