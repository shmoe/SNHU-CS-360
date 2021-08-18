package com.example.cs_360fitnessapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class SendSMSPermissionRationaleDialog extends DialogFragment {
    public interface OnSendSMSPermissionRationaleDialogResultListener {
        void onSendSMSPermissionRationaleDialogResult(boolean result);
    }

    private SendSMSPermissionRationaleDialog.OnSendSMSPermissionRationaleDialogResultListener mListener;

    @NonNull
    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        final TextView textView = new TextView(getActivity());
        textView.setText(R.string.dialog_send_sms_permission_rationale_text);
        textView.setPadding(30,10,30,10);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dialog_send_sms_permission_rationale_title)
                .setView(textView)
                .setPositiveButton(R.string.dialog_fragment_positive, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mListener.onSendSMSPermissionRationaleDialogResult(true);
                    }
                })
                .setNegativeButton(R.string.dialog_fragment_casual_negative, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mListener.onSendSMSPermissionRationaleDialogResult(false);
                    }
                })
                .create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (SendSMSPermissionRationaleDialog.OnSendSMSPermissionRationaleDialogResultListener) context;
    }
}
