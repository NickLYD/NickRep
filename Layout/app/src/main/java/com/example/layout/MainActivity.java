package com.example.layout;

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
        but=new Button[3];
        but[0]=(Button)findViewById(R.id.but1);
        but[1]=(Button)findViewById(R.id.but2);
        but[2]=(Button)findViewById(R.id.but3);
        but[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,LinearLayout.class);
                startActivity(intent);
            }
        });
        but[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ConstraintLayout.class);
                startActivity(intent);
            }
        });
        but[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,TableLayout.class);
                startActivity(intent);
            }
        });

    }
}
