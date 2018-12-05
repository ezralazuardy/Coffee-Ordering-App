package com.coffeeorderingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextFirstName, editTextLastName;
    private Button btnNext, btnExitApp;
    private AlertDialog exitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        editTextFirstName = findViewById(R.id.edittext_firstname_hint);
        editTextLastName = findViewById(R.id.edittext_lastname_hint);
        btnNext = findViewById(R.id.btn_next_hint);
        btnExitApp = findViewById(R.id.btn_exitapp_hint);
    }

    private void setListener() {
        btnNext.setOnClickListener(SignUpActivity.this);
        btnExitApp.setOnClickListener(SignUpActivity.this);
        exitDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                exitDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
                exitDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exitDialog.dismiss();
                    }
                });
            }
        });
    }

    private void unsetListener() {
        if(btnNext != null) btnNext.setOnClickListener(null);
        if(btnExitApp != null) btnExitApp.setOnClickListener(null);
        if(exitDialog != null) exitDialog.setOnShowListener(null);
    }

    private void buildDialog() {
        exitDialog = new AlertDialog.Builder(SignUpActivity.this)
                .setTitle(getResources().getString(R.string.dialog_title_exitapp))
                .setMessage(getResources().getString(R.string.dialog_body_exitapp))
                .setPositiveButton(getResources().getString(R.string.dialog_btn_yes), null)
                .setNegativeButton(getResources().getString(R.string.dialog_btn_no), null)
                .setCancelable(true)
                .create();
    }

    private void destroyDialog() {
        if (exitDialog != null) exitDialog = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next_hint:
                if (!editTextFirstName.getText().toString().equals("") && !editTextLastName.getText().toString().equals("")) {
                    startActivity(new Intent(SignUpActivity.this, MenuActivity.class).putExtra(getResources().getString(R.string.hint_edittext_firstname_signup), editTextFirstName.getText().toString()).putExtra(getResources().getString(R.string.hint_edittext_lastname_signup), editTextLastName.getText().toString()));
                    finish();
                } else {
                    if (editTextFirstName.getText().toString().equals("")) {
                        Toast.makeText(SignUpActivity.this, getResources().getString(R.string.toast_error_firstname_null), Toast.LENGTH_SHORT).show();
                    } else if (editTextLastName.getText().toString().equals("")) {
                        Toast.makeText(SignUpActivity.this, getResources().getString(R.string.toast_error_lastname_null), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btn_exitapp_hint:
                exitDialog.show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        exitDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        buildDialog();
        setListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unsetListener();
        destroyDialog();
    }
}
