package com.example.web;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et;
    private Button but;
    private String urlHead="http://";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        but=(Button)findViewById(R.id.but);
        et=(EditText)findViewById(R.id.edUrl);
        but.setOnClickListener(this);
    }
    public void onClick(View view){
        switch(view.getId()){
            case R.id.but:
                Intent intent=new Intent(Intent.ACTION_VIEW);
                //intent.setData(Uri.parse(urlHead+et.getText().toString()));
                intent.putExtra("url",urlHead+et.getText().toString());
                startActivity(intent);
                break;

        }
    }
}
