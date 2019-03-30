package com.example.uicomponent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import java.util.*;
import android.view.*;

public class SimpleAdapter extends AppCompatActivity {
    private String[] names = new String[]{"Lion","Tiger","Monkey","Dog","Cat","elephant"};
    private int[] image=new int[]{R.drawable.lion,R.drawable.tiger,R.drawable.monkey,R.drawable.dog,R.drawable.cat,R.drawable.elephant};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        List<Map<String,Object>> ListItems=new ArrayList<Map<String, Object>>();
        for (int i=0;i<names.length;i++){
            Map<String,Object> listItem=new HashMap<String,Object>();
            listItem.put("header",names[i]);
            listItem.put("images",image[i]);
            ListItems.add(listItem);
        }
        android.widget.SimpleAdapter simpleAdapter=new android.widget.SimpleAdapter(this,ListItems,R.layout.list_item,new String[]{"header","images"},new int[]{R.id.header,R.id.images});
        final ListView list=(ListView)findViewById(R.id.view);
        list.setAdapter(simpleAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String text=names[position];
                Toast .makeText(SimpleAdapter.this,text, Toast.LENGTH_SHORT).show();
            }
        });


    }


}
