package com.example.cs_360fitnessapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class EditWeightDialogFragment extends DialogFragment {
    public interface OnEditWeightListener {
        void onEditWeight(float newWeight);
    }

    private OnEditWeightListener mListener;

    @NonNull
    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        final EditText editText = new EditText(getActivity());
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText.setMaxLines(1);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dialog_fragment_edit_weight)
                .setView(editText)
                .setPositiveButton(R.string.dialog_fragment_positive, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        float newWeight = Float.parseFloat(editText.getText().toString());
                        mListener.onEditWeight(newWeight);
                    }
                })
                .setNegativeButton(R.string.dialog_fragment_negative, null)
                .create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (OnEditWeightListener) context;
    }
}
