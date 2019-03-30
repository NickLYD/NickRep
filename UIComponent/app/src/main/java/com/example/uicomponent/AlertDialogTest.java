package com.example.uicomponent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class AlertDialogTest extends AppCompatActivity {
    private Button but;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alertdialogtext);
        new android.support.v7.app.AlertDialog.Builder(this)
                .setView(R.layout.my_dialog)
                .setPositiveButton("Sign In",null)
                .setNegativeButton("Cancel",null)
                .create()
                .show();
    }

}
