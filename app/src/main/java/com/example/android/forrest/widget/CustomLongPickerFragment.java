package com.example.android.forrest.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.android.forrest.R;


public class CustomLongPickerFragment extends DialogFragment {

  private final PickerType         mType;
  private final LongPickerListener mListener;

  public CustomLongPickerFragment(PickerType type, LongPickerListener listener) {
    mType  = type;
    mListener = listener;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    final int  title;
    final View contentView;

    LayoutInflater inflater = requireActivity().getLayoutInflater();
    if (mType == PickerType.HEIGHT) {
      title       = R.string.height;
      contentView = inflater.inflate(R.layout.dialog_content_height, null);

      NumberPicker heightPicker = contentView.findViewById(R.id.height_picker);
      heightPicker.setMinValue(130);
      heightPicker.setMaxValue(210);
      heightPicker.setValue(170);
    } else {
      title       = R.string.weight;
      contentView = inflater.inflate(R.layout.dialog_content_weight, null);

      NumberPicker intPicker = contentView.findViewById(R.id.kg_int_picker);
      intPicker.setMinValue(30);
      intPicker.setMaxValue(110);
      intPicker.setValue(70);

      NumberPicker floatPicker = contentView.findViewById(R.id.kg_float_picker);
      floatPicker.setMinValue(0);
      floatPicker.setMaxValue(9);
    }

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle(title)
           .setView(contentView)
           .setPositiveButton(R.string.set, (dialog, which) -> {
             Dialog dialogView = CustomLongPickerFragment.this.getDialog();
             if (dialogView != null) {
               NumberPicker heightPicker = dialogView.findViewById(R.id.height_picker);

               if (heightPicker != null) {
                 // Height
                 mListener.onDialogPositiveClick((double) heightPicker.getValue(), PickerType.HEIGHT);
               } else {
                 // Weight
                 NumberPicker kgIntPicker = dialogView.findViewById(R.id.kg_int_picker);
                 NumberPicker kgFloatPicker = dialogView.findViewById(R.id.kg_float_picker);

                 double value = kgIntPicker.getValue() + 0.1 * kgFloatPicker.getValue();
                 mListener.onDialogPositiveClick(value, PickerType.WEIGHT);
               }
             }
           })
           .setNegativeButton(R.string.cancel, (dialog, which) -> {

           });

    return builder.create();
  }

  public interface LongPickerListener {
    void onDialogPositiveClick(Double value, PickerType type);
  }

}
