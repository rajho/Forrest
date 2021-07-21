package com.example.android.forrest.views.customnumberdialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.android.forrest.R;


public class CustomNumberPickerDialog extends DialogFragment {

  private final PickerType                 mType;
  private final NumberPickerDialogListener mListener;

  public CustomNumberPickerDialog(PickerType type, NumberPickerDialogListener listener) {
    mType  = type;
    mListener = listener;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    // Getting number picker by type
    LayoutInflater inflater     = requireActivity().getLayoutInflater();
    INumberPicker  numberPicker = ((new NumberPickerFactory(inflater)).getNumberPicker(mType));

    // Creating dialog
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle(numberPicker.getTitle())
           .setView(numberPicker.getContentView())
           .setPositiveButton(R.string.set, (dialog, which) -> {
             Dialog dialogView = CustomNumberPickerDialog.this.getDialog();
             mListener.onDialogPositiveClick((double) numberPicker.getValueFromDialog(dialogView), mType);
           })
           .setNegativeButton(R.string.cancel, (dialog, which) -> {

           });

    return builder.create();
  }

  public interface NumberPickerDialogListener {
    void onDialogPositiveClick(Double value, PickerType type);
  }

}
