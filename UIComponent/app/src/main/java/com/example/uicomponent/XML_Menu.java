package com.example.uicomponent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class XML_Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xml_menu);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        TextView tv=(TextView)findViewById(R.id.text);
        switch(item.getItemId()){
            case R.id.font10:
                tv.setTextSize(10.0f);
                break;
            case R.id.font16:
                tv.setTextSize(16.0f);
                break;
            case R.id.font20:
                tv.setTextSize(20.0f);
                break;
            case R.id.commonmenu:
                Toast.makeText(XML_Menu.this,item.getTitle(),Toast.LENGTH_SHORT).show();
                break;
            case R.id.red:
                tv.setTextColor(getApplicationContext().getResources().getColor(R.color.Red));
                break;
            case R.id.black:
                tv.setTextColor(getApplicationContext().getResources().getColor(R.color.Black));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
