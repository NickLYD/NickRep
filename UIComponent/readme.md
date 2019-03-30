实验三  UIComponent
=====
# `SimpleAdapter:`
- `SimpleAdapter.java代码展示:`
```javascript
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

```
- `listview.xml代码展示:`
```javascript
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">
    <ListView
        android:id="@+id/view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:divider="#000"
        android:dividerHeight="2dp"
        android:listSelector="@color/colorAccent"/>
</LinearLayout>

```
- `listitem.xml代码展示:`
```javascript
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="horizontal">
    <TextView
        android:id="@+id/header"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textColor="#000000"
        android:textSize="20dp"
        android:paddingLeft="10dp"
        />
    <RelativeLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content">
        <ImageView
            android:id="@+id/images"
            android:layout_width="50dp"
            android:layout_height="50dp"
            android:layout_marginRight="10dp"
            android:layout_alignParentRight="true"
            />
    </RelativeLayout>
</LinearLayout>

```
- `效果截图`
![效果](https://github.com/NickLYD/NickRep/blob/master/UIComponent/photos/SimpleAdapter.jpg)
# `AlterDialog:`
- `AlterDialogTest.java代码展示:`
```javascript
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

```
- `my_dialog.xml代码展示:`
```javascript
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">
    <ImageView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:src="@drawable/header_logo"/>
    <EditText
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Username"
        android:textSize="20dp"/>
    <EditText
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Password"
        android:inputType="textPassword"
        android:textSize="20dp"/>

</LinearLayout>

```
- `效果截图`
![效果](https://github.com/NickLYD/NickRep/blob/master/UIComponent/photos/AlterDialogTest.jpg)
# `XMLMenu:`
- `XML_Menu.java代码展示:`
```javascript
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

```
- `my_menu.xml代码展示:`
```javascript
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:id="@+id/fontsize"
        android:title="字体大小">
        <menu>
            <item
                android:id="@+id/font10"
                android:title="10号字体">
            </item>
            <item
                android:id="@+id/font16"
                android:title="16号字体">
            </item>
            <item
                android:id="@+id/font20"
                android:title="20号字体">
            </item>
        </menu>
    </item>
    <item
        android:id="@+id/commonmenu"
        android:title="普通菜单项">
    </item>
    <item
        android:id="@+id/fontcolor"
        android:title="字体颜色">
        <menu>
            <item
                android:id="@+id/red"
                android:title="红色字体">
            </item>
            <item
                android:id="@+id/black"
                android:title="黑色字体">
            </item>

        </menu>
    </item>
</menu>
```
- `xnl_menu.xml代码展示:`
```javascript
<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".XML_Menu">
    <TextView
        android:id="@+id/text"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="用于测试的内容！"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"/>

</android.support.constraint.ConstraintLayout>
```
- `效果截图`
![效果](https://github.com/NickLYD/NickRep/blob/master/UIComponent/photos/XMLMenu1.jpg)
![效果](https://github.com/NickLYD/NickRep/blob/master/UIComponent/photos/XMLMenu2.jpg)
![效果](https://github.com/NickLYD/NickRep/blob/master/UIComponent/photos/XMLMenu3.jpg)
![效果](https://github.com/NickLYD/NickRep/blob/master/UIComponent/photos/XMLMenu4.jpg)
# `ActionMode:`
- `ActionModeTest.java代码展示:`
```javascript
package com.example.uicomponent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.view.ActionMode.Callback;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionModeTest extends AppCompatActivity {
    private String[] numbers = new String[]{"One","Two","Three","Four","Five"};
    private Callback callback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_actionmode);
        List<Map<String,Object>> ListItems=new ArrayList<Map<String, Object>>();
        for (int i=0;i<numbers.length;i++){
            Map<String,Object> listItem=new HashMap<String,Object>();
            listItem.put("numbers",numbers[i]);
            ListItems.add(listItem);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,ListItems,R.layout.listview_actionmode_item,new String[]{"numbers"},new int[]{R.id.Num});
        final ListView list=(ListView)findViewById(R.id.listview);
        list.setAdapter(simpleAdapter);
        list.setChoiceMode(list.CHOICE_MODE_MULTIPLE_MODAL);
        list.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(android.view.ActionMode mode, int position, long id, boolean checked) {
                mode.setTitle(list.getCheckedItemCount()+" selected");
            }

            @Override
            public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
                  mode.finish();
                  return true;
            }

            @Override
            public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
                // Inflate the menu for the CAB
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu_delete_choice, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(android.view.ActionMode mode) {
                // Here you can make any necessary updates to the activity when
                // the CAB is removed. By default, selected items are deselected/unchecked.
            }

            @Override
            public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
                // Here you can perform updates to the CAB due to
                // an invalidate() request
                return false;
            }
        });

    }
}

```
- `listview_actionmode.xml代码展示:`
```javascript
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    >
    <ListView
        android:id="@+id/listview"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        >
    </ListView>

</LinearLayout>

```
- `listview_actionmode_item.xml代码展示:`
```javascript
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@drawable/items">
    <ImageView
        android:id="@+id/image"
        android:layout_width="55dp"
        android:layout_height="55dp"
        android:src="@mipmap/ic_launcher" />

    <TextView
        android:id="@+id/Num"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginLeft="20dp"
        android:layout_marginStart="20dp"
        android:layout_toRightOf="@id/image"
        android:textSize="25sp" />


</RelativeLayout>

```
- `menu_multi_delete.xml代码展示:`
```javascript
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <item android:id="@+id/action_delete"
        android:title="删除"
        android:orderInCategory="100"
        android:icon="@mipmap/delete"
        app:showAsAction="always"/>
</menu>
```
- `items.xml代码展示:`
```javascript
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
        <item
            android:drawable="@color/Blue"
            android:state_activated="true"/>
</selector>


```
- `效果截图`
![效果](https://github.com/NickLYD/NickRep/blob/master/UIComponent/photos/ActionMode.jpg)
