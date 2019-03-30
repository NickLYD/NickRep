package com.example.uicomponent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button but[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        but=new Button[4];
        but[0]=(Button)findViewById(R.id.but1);
        but[1]=(Button)findViewById(R.id.but2);
        but[2]=(Button)findViewById(R.id.but3);
        but[3]=(Button)findViewById(R.id.but4);
        but[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, SimpleAdapter.class);
                startActivity(intent);
            }
        });
        but[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, AlertDialogTest.class);
                startActivity(intent);
            }
        });
        but[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, XML_Menu.class);
                startActivity(intent);
            }
        });
        but[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ActionModeTest.class);
                startActivity(intent);
            }
        });

    }
}

