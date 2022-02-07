package com.example.cs_360fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity implements TextWatcher,
                                SetGoalWeightDialogFragment.OnSetGoalWeightListener,
                                SendSMSPermissionRationaleDialog.OnSendSMSPermissionRationaleDialogResultListener {
    private static final int SEND_SMS_REQUEST_CODE = 0;

    private String mSessionToken = null;

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private TextView mErrorTextView;

    private FitnessAppDatabase mDatabase;

    private boolean validateNewUsernamePasswordPair(String user, String pass){
        if(user.trim().length() <= 0 || pass.trim().length() <= 0)
            return false;

        return mDatabase.accountDao().getPassword(user) == null;
    }

    private boolean validateUsernamePasswordPair(String user, String pass){
        String dbHash = mDatabase.accountDao().getPassword(user);
        byte [] dbSalt = mDatabase.accountDao().getSalt(user);

        return (dbHash != null) && (dbSalt != null) && dbHash.equals(HashAndSalter.getHash(pass, dbSalt));
    }

    private boolean hasSendSMSPermissions() {
        String sendSMSPermission = Manifest.permission.SEND_SMS;
        if(checkSelfPermission(sendSMSPermission) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(sendSMSPermission)) {
                FragmentManager manager = getSupportFragmentManager();
                SendSMSPermissionRationaleDialog dialog = new SendSMSPermissionRationaleDialog();

                dialog.show(manager, "sendSMSPermissionRationaleDialog");
            } else {
                requestPermissions(new String[] { sendSMSPermission }, SEND_SMS_REQUEST_CODE);
            }
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        logIn();
    }

    private void logIn(){
        Intent intent = new Intent(this, WeightTrackingActivity.class);
        intent.putExtra("username", mSessionToken);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null) {
            mSessionToken = savedInstanceState.getString("sessionToken");
        }

        if(mSessionToken != null){
            logIn();
        }

        mUsernameEditText = findViewById(R.id.editUserNameText);
        mPasswordEditText = findViewById(R.id.editPasswordText);
        mErrorTextView = findViewById(R.id.loginActivityErrorTextView);

        mUsernameEditText.addTextChangedListener(this);
        mPasswordEditText.addTextChangedListener(this);

        mDatabase = FitnessAppDatabase.getInstance(getApplicationContext());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("sessionToken", mSessionToken);
    }

    public void onButtonNewAccountClick(View view){
        String newUsername = mUsernameEditText.getText().toString();
        String newPassword = mPasswordEditText.getText().toString();
        if(validateNewUsernamePasswordPair(newUsername, newPassword)) {
            byte [] salt = HashAndSalter.getSalt();
            String hash = HashAndSalter.getHash(newPassword, salt);
            mDatabase.accountDao().insertAccount(new Account(newUsername, hash, salt));

            mSessionToken = newUsername;

            FragmentManager manager = getSupportFragmentManager();
            SetGoalWeightDialogFragment dialog = new SetGoalWeightDialogFragment();

            dialog.show(manager, "setGoalWeightDialog");
        } else {
            mErrorTextView.setText(R.string.error_text_new_account);
            mErrorTextView.setVisibility(View.VISIBLE);
        }
    }

    public void onButtonLogInClick(View view){
        String username = mUsernameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        if(validateUsernamePasswordPair(username, password)){
            mSessionToken = username;
            logIn();
        } else {
            mErrorTextView.setText(R.string.error_text_log_in);
            mErrorTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(mErrorTextView.getVisibility() == View.VISIBLE){
            mErrorTextView.setText("");
            mErrorTextView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) { }

    @Override
    public void onSetGoalWeight(float weight) {
        if(mSessionToken == null)
            throw new RuntimeException();

        mDatabase.accountDao().setGoalWeight(weight, mSessionToken);

        if(hasSendSMSPermissions())
            logIn();
    }

    @Override
    public void onSendSMSPermissionRationaleDialogResult(boolean result) {
        String sendSMSPermission = Manifest.permission.SEND_SMS;

        if(result) {
            requestPermissions(new String[] { sendSMSPermission }, SEND_SMS_REQUEST_CODE);
        } else {
            logIn();
        }
    }
}