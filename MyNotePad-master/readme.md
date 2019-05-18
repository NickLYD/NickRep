# NotePad应用的基本功能及扩展功能的实现
## 基本功能:
- 时间戳
- 搜索

## 拓展功能：
- UI美化
- 背景更换
- 笔记分类
- 排序
- 导出笔记
---
### 基本功能解析：
#### 时间戳
1> 首先，找到列表布局文件的noteslist_item.xml，添加一个TextView下来显示时间。
```javascript
<TextView
        android:id="@+id/text2"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:textAppearance="?android:attr/textAppearanceLarge"
        android:textSize="12dp"
        android:gravity="center_vertical"
        android:paddingLeft="10dip"
        android:singleLine="true"
        android:layout_weight="1"
        android:layout_margin="0dp"
        android:textColor="@color/colorBlack"
        />
```
2> 在添加时间前可以先查看数据库的结构，找到NotePadProvider.java中建表部分，可以看出数据库中已有文本创建时间和修改时间的字段（该功能所有涉及颜色部分都暂且忽略，见下文的修改背景功能）。
```javascript
       @Override
       public void onCreate(SQLiteDatabase db) {
           db.execSQL("CREATE TABLE " + NotePad.Notes.TABLE_NAME + " ("
                   + NotePad.Notes._ID + " INTEGER PRIMARY KEY,"
                   + NotePad.Notes.COLUMN_NAME_TITLE + " TEXT,"
                   + NotePad.Notes.COLUMN_NAME_NOTE + " TEXT,"
                   + NotePad.Notes.COLUMN_NAME_CREATE_DATE + " INTEGER,"
                   + NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE + " Text,"
                   + NotePad.Notes.COLUMN_NAME_BACK_COLOR + " INTEGER" //颜色
                   + ");");
       }
```
3> 打开NodeEditor.java,找到updateNode()函数，获取当前系统时间并将其格式化存入数据库中。
```javascript
   ContentValues values = new ContentValues();
   Date newTime = new Date(System.currentTimeMillis());
   SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   String retStrFormatNowDate = sdFormatter.format(newTime);
   values.put(NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE, retStrFormatNowDate);
```
4> 在NoteList.java的PROJECTION数组中增加修改时间字段的描述。
```javascript
    private static final String[] PROJECTION = new String[] {
            NotePad.Notes._ID, // 0
            NotePad.Notes.COLUMN_NAME_TITLE, // 1
            NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE,
            NotePad.Notes.COLUMN_NAME_BACK_COLOR
    };
```
5> Cursor不做调整，在dataColumns，viewIDs中补充时间部分。
```javascript
private String[] dataColumns = { NotePad.Notes.COLUMN_NAME_TITLE,NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE};
private int[] viewIDs = { R.id.text1,R.id.text2 };
```
 `效果展示`

![效果](https://github.com/NickLYD/NickRep/blob/master/MyNotePad-master/photos/note1.jpg)
#### 搜索
1> 在NodeList.java的菜单文件list_options_menu.xml中添加搜索菜单项。
```javascript
    <item
        android:id="@+id/menu_search"
        android:title="menu_search"
        android:icon="@drawable/ic_search"
        app:showAsAction="always">
    </item>
```
2> 新建note_search.xml布局文件，用以显示搜索结果。
```javascript
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    xmlns:android="http://schemas.android.com/apk/res/android">
    <SearchView
        android:id="@+id/sv"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:queryHint="输入搜索内容..."
        >
    </SearchView>
    <ListView
        android:id="@+id/tv"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</LinearLayout>
```
3> 新建NoteSearch的activity,该activity实现LoaderManager.LoaderCallbacks<Cursor>接口，用于实时更新数据。使用note_search的布局文件,通过对SearchView控件与ListView结合采用模糊以完成动态搜索。搜索出来的笔记跳转原理与NotesList中一致，动态搜索的实现最主要的部分在onQueryTextChange方法中，当SearchView中文本发生变化时，执行其中代码。(其中的ColorCursorAdapter用于列表的颜色变化，在下文有做说明，现在暂且忽略)。
```javascript
package com.example.mynotepad;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.content.Loader;
import android.content.CursorLoader;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;

public class NoteSearch extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String[] PROJECTION = new String[] {
            NotePad.Notes._ID, // 0
            NotePad.Notes.COLUMN_NAME_TITLE, // 1
            NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE,
            NotePad.Notes.COLUMN_NAME_BACK_COLOR
    };
    private ListView listView;
    private SearchView searchView;
    private ColorCursorAdapter adapter;
    private int[] viewIDs = { R.id.text1,R.id.text2 };
    private String[] dataColumns = { NotePad.Notes.COLUMN_NAME_TITLE ,NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE} ;
    private Cursor updatecursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_search);
        setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);
        listView=findViewById(R.id.tv);
        searchView=findViewById(R.id.sv);
        searchView.onActionViewExpanded();
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String selection = NotePad.Notes.COLUMN_NAME_TITLE + " Like ? ";
                String[] selectionArgs = { "%"+newText+"%" };
                if(!newText.equals("")){
                    updatecursor = getContentResolver().query(
                            getIntent().getData(),            // Use the default content URI for the provider.
                            PROJECTION,                       // Return the note ID and title for each note.
                            selection,                             // No where clause, return all records.
                            selectionArgs,                             // No where clause, therefore no where column values.
                            NotePad.Notes.DEFAULT_SORT_ORDER  // Use the default sort order.
                    );
                }
                else {
                    updatecursor = getContentResolver().query(
                            getIntent().getData(),            // Use the default content URI for the provider.
                            PROJECTION,                       // Return the note ID and title for each note.
                            null,                             // No where clause, return all records.
                            null,                             // No where clause, therefore no where column values.
                            NotePad.Notes.DEFAULT_SORT_ORDER  // Use the default sort order.
                    );
                }
                adapter.swapCursor(updatecursor);

                // adapter.notifyDataSetChanged();
                return false;
            }
        });
        Intent intent = getIntent();
        if (intent.getData() == null) {
            intent.setData(NotePad.Notes.CONTENT_URI);

        }
        listView.setOnCreateContextMenuListener(this);

        Cursor cursor = getContentResolver().query(
                getIntent().getData(),            // Use the default content URI for the provider.
                PROJECTION,                       // Return the note ID and title for each note.
                null,                             // No where clause, return all records.
                null,                             // No where clause, therefore no where column values.
                NotePad.Notes.DEFAULT_SORT_ORDER  // Use the default sort order.
        );

        adapter = new ColorCursorAdapter(
                this,                             // The Context for the ListView
                R.layout.noteslist_item,          // Points to the XML for a list item
                cursor,                           // The cursor to get items from
                dataColumns,
                viewIDs
        );

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Constructs a new URI from the incoming URI and the row ID
                Uri uri = ContentUris.withAppendedId(getIntent().getData(), id);

                // Gets the action from the incoming Intent
                String action = getIntent().getAction();

                // Handles requests for note data
                if (Intent.ACTION_PICK.equals(action) || Intent.ACTION_GET_CONTENT.equals(action)) {

                    // Sets the result to return to the component that called this Activity. The
                    // result contains the new URI
                    setResult(RESULT_OK, new Intent().setData(uri));
                } else {

                    // Sends out an Intent to start an Activity that can handle ACTION_EDIT. The
                    // Intent's data is the note ID URI. The effect is to call NoteEdit.
                    startActivity(new Intent(Intent.ACTION_EDIT, uri));
                }
            }
        });
        LoaderManager loaderManager=getLoaderManager();
        loaderManager.initLoader(0,null, this);

    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,getIntent().getData() , null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

}
```
4> 最后在NotesList中找到onOptionsItemSelected方法，在switch中添加搜索的case语句。
```javascript
        case R.id.menu_search:
          Intent intent = new Intent();
          intent.setClass(NotesList.this,NoteSearch.class);
          NotesList.this.startActivity(intent);
          return true;
```
 `效果展示`

![效果](https://github.com/NickLYD/NickRep/blob/master/MyNotePad-master/photos/search1.jpg)

![效果](https://github.com/NickLYD/NickRep/blob/master/MyNotePad-master/photos/search2.jpg)

---
### 拓展功能解析：
#### UI美化
- ##### 列表颜色背景美化
1> 先给NotesList换个主题，把黑色换成白色，在AndroidManifest.xml中NotesList的Activity中修改主题。
```javascript
android:theme="@android:style/Theme.Holo.Light"
```
2> 要保存颜色，首先要在创建数据库表地方添加颜色的字段。
```javascript
       @Override
       public void onCreate(SQLiteDatabase db) {
           db.execSQL("CREATE TABLE " + NotePad.Notes.TABLE_NAME + " ("
                   + NotePad.Notes._ID + " INTEGER PRIMARY KEY,"
                   + NotePad.Notes.COLUMN_NAME_TITLE + " TEXT,"
                   + NotePad.Notes.COLUMN_NAME_NOTE + " TEXT,"
                   + NotePad.Notes.COLUMN_NAME_CREATE_DATE + " INTEGER,"
                   + NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE + " Text,"
                   + NotePad.Notes.COLUMN_NAME_BACK_COLOR + " INTEGER" //颜色
                   + ");");
       }
```
3> 相应的在NotePad契约类中添加颜色的说明和颜色对应的int值，以后根据颜色对应不int值选择要显示的颜色。
```javascript
public static final String COLUMN_NAME_BACK_COLOR = "color";
```
```javascript
public static final int DEFAULT_COLOR = 0; //白
public static final int YELLOW_COLOR = 1; //黄
public static final int BLUE_COLOR = 2; //蓝
public static final int GREEN_COLOR = 3; //绿
public static final int RED_COLOR = 4; //红
```
4> 由于数据库中多了一个字段，所以要在NotePadProvider的static{}中添加对其相应的处理。
```javascript
sNotesProjectionMap.put(
        NotePad.Notes.COLUMN_NAME_BACK_COLOR,
        NotePad.Notes.COLUMN_NAME_BACK_COLOR);
```
5> 在 insert中也要添加相应的处理。
```javascript
 // If the values map doesn't contain a background,sets the value to a white background.
        if (values.containsKey(NotePad.Notes.COLUMN_NAME_BACK_COLOR) == false) {
            values.put(NotePad.Notes.COLUMN_NAME_BACK_COLOR, NotePad.Notes.DEFAULT_COLOR);
        }
```
6> 将颜色填充到ListView，通过自定义一个CursorAdapter继承SimpleCursorAdapter，完成列表的内容与背景填充。
```javascript
package com.example.mynotepad;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.widget.SimpleCursorAdapter;

public class ColorCursorAdapter extends SimpleCursorAdapter {
    public ColorCursorAdapter(Context context, int layout, Cursor c,
                              String[] from, int[] to) {
        super(context, layout, c, from, to);
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor){
        super.bindView(view, context, cursor);
        //从数据库中读取的cursor中获取笔记列表对应的颜色数据，并设置笔记颜色
        int x = cursor.getInt(cursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_BACK_COLOR));
        switch (x){
            case NotePad.Notes.DEFAULT_COLOR:
                view.setBackgroundColor(Color.rgb(255, 255, 255));//白
                break;
            case NotePad.Notes.YELLOW_COLOR:
                view.setBackgroundColor(Color.rgb(247, 216, 133));//黄
                break;
            case NotePad.Notes.BLUE_COLOR:
                view.setBackgroundColor(Color.rgb(165, 202, 237));//蓝
                break;
            case NotePad.Notes.GREEN_COLOR:
                view.setBackgroundColor(Color.rgb(161, 214, 174));//绿
                break;
            case NotePad.Notes.RED_COLOR:
                view.setBackgroundColor(Color.rgb(244, 149, 133));//红
                break;
            default:
                view.setBackgroundColor(Color.rgb(255, 255, 255));
                break;
        }
    }
}
```
7> 在NotesList中的PROJECTION添加颜色项。
```javascript
    private static final String[] PROJECTION = new String[] {
            NotePad.Notes._ID, // 0
            NotePad.Notes.COLUMN_NAME_TITLE, // 1
            NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE,
            NotePad.Notes.COLUMN_NAME_BACK_COLOR
    };
```
8> 并且将NotesList中用的SimpleCursorAdapter改使用刚才自定义的ColorCursorAdapter。
```javascript
adapter = new ColorCursorAdapter(
                      this,                             // The Context for the ListView
                      R.layout.noteslist_item,          // Points to the XML for a list item
                      cursor,                           // The cursor to get items from
                      dataColumns,
                      viewIDs
              );
```
 `效果展示`

![效果](https://github.com/NickLYD/NickRep/blob/master/MyNotePad-master/photos/note1.jpg)

- ##### 工具栏伸缩折叠美化
1> 修改listview.xml布局文件，（先忽略drawlayout和navigation，该布局主要用于实现侧滑菜单，用于下文的笔记分类,如果仅实现工具栏伸缩折叠可以删除）要想实现工具栏的伸缩折叠，要使用CoordinatorLayout 作为最外层容器，其内部需要一个AppBarLayout，并且使用Design Library库中的CollapsingToolbarLayout控件来实现标题逐渐缩放到Toolbar上，而背景图片则在滑动到一定程度后变成了Toolbar的颜色的效果，app:contentScrim="@color/colorPrimary"用于设置toolbar在折叠后的颜色，在CollapsingToolbarLayout中可自行添加toolbar下滑时显示的组件，我这里只添加了一个ImageView的组件。使用android.support.v4.widget.NestedScrollView嵌套NoSrollListView(继承ListView)来实现列表的嵌套活动。
```javascript
<?xml version="1.0" encoding="utf-8"?>
<android.support.v4.widget.DrawerLayout android:id="@+id/drawer_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <android.support.design.widget.CoordinatorLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <android.support.design.widget.AppBarLayout
            android:layout_width="match_parent"
            android:layout_height="200dp"
            android:fitsSystemWindows="true">

            <android.support.design.widget.CollapsingToolbarLayout
                android:id="@+id/collapsing_toolbar_layout"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                app:collapsedTitleTextAppearance="@style/style_textsize1"
                app:expandedTitleTextAppearance="@style/style_textsize"
                app:contentScrim="@color/colorPrimary"
                app:expandedTitleMarginEnd="10dp"
                app:expandedTitleMarginStart="10dp"
                app:layout_scrollFlags="scroll|exitUntilCollapsed">

                <ImageView
                    android:id="@+id/iv"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:scaleType="centerCrop"
                    android:src="@drawable/ic_greentea"
                    app:layout_collapseMode="parallax"
                    app:layout_collapseParallaxMultiplier="0.5" />

                <android.support.v7.widget.Toolbar
                    android:id="@+id/toolbar"
                    android:layout_width="match_parent"
                    android:layout_height="?attr/actionBarSize"
                    app:layout_collapseMode="pin" />
            </android.support.design.widget.CollapsingToolbarLayout>

        </android.support.design.widget.AppBarLayout>

        <android.support.v4.widget.NestedScrollView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:layout_behavior="@string/appbar_scrolling_view_behavior">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical">
                <com.example.mynotepad.NoScrollListView
                    android:id="@+id/tv"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"/>
            </LinearLayout>
        </android.support.v4.widget.NestedScrollView>
    </android.support.design.widget.CoordinatorLayout>
    <android.support.design.widget.NavigationView
        android:id="@+id/nav_view"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:layout_gravity="start"
        android:background="@color/design_default_color_primary"
        app:headerLayout="@layout/nav_header_main"
        app:menu="@menu/activity_main_drawer"
        >
    </android.support.design.widget.NavigationView>
</android.support.v4.widget.DrawerLayout>
```
2> 新建类NoSrollListView继承ListView，因为NestedScrollView嵌套ListView只能显示一行，所以必须重写listview的onMeasure方法。

```javascript
package com.example.mynotepad;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class NoScrollListView extends ListView {
    public NoScrollListView(Context context) {
        super(context);
    }

    public NoScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
```
3> 在NoteList的onCreate中添加toolBar的相关操作。
```javascript
private Toolbar toolbar;
toolbar = (Toolbar) findViewById(R.id.toolbar);
setSupportActionBar(toolbar);
```
4> 在Manifest.xml中添加主题，不在显示原来的ActionBar。
```javascript
android:theme="@style/AppNoActionTheme"
```
5> 整体的style如下：

```javascript
    <style name="AppNoActionTheme" parent="Theme.AppCompat.Light.NoActionBar">
        <!-- Customize your theme here. -->
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
    </style>
    <style name="style_textsize">
        <item name="android:textSize">23sp</item>
    </style>
    <style name="style_textsize1">
        <item name="android:textSize">18sp</item>
    </style>
```
 `效果展示`


![效果](https://github.com/NickLYD/NickRep/blob/master/MyNotePad-master/photos/note2.jpg)
#### 背景更换
1> 对于该功能与前面说明的listview背景美化有所关联，即在Note编辑中选定的背景颜色与其相对应listview的背景始终保持一致。首先在菜单文件中添加一个更改背景的选项，editor_options_menu.xml，图标自己添加，item总是显示。

```javascript
    <item android:id="@+id/menu_color"
        android:title="menu_color"
        android:icon="@drawable/ic_menu_color"
        app:showAsAction="always"/>
```
2> 在NoteEditor中的PROJECTION添加颜色项。
```javascript
    private static final String[] PROJECTION =
        new String[] {
            NotePad.Notes._ID,
            NotePad.Notes.COLUMN_NAME_TITLE,
            NotePad.Notes.COLUMN_NAME_NOTE,
            NotePad.Notes.COLUMN_NAME_BACK_COLOR
    };
```
3> NoteEditor类中有onResume()方法，onResume()方法在正常启动时会被调用，一般是onStart()后会执行onResume()，在Acitivity从Pause状态转化到Active状态也会被调用，利用这个特点，将从数据库读取颜色并设置编辑界面背景色操作放入其中，好处除了从笔记列表点进来时可以被执行到，跳到改变颜色的NoteColor的Activity（接下来会提到）回来时也会被执行到。
```javascript
int x = mCursor.getInt(mCursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_BACK_COLOR));
            switch (x){
                case NotePad.Notes.DEFAULT_COLOR:
                    mText.setBackgroundColor(Color.rgb(255, 255, 255));//白
                    break;
                case NotePad.Notes.YELLOW_COLOR:
                    mText.setBackgroundColor(Color.rgb(247, 216, 133));//黄
                    break;
                case NotePad.Notes.BLUE_COLOR:
                    mText.setBackgroundColor(Color.rgb(165, 202, 237));//蓝
                    break;
                case NotePad.Notes.GREEN_COLOR:
                    mText.setBackgroundColor(Color.rgb(161, 214, 174));//绿
                    break;
                case NotePad.Notes.RED_COLOR:
                    mText.setBackgroundColor(Color.rgb(244, 149, 133));//红
                    break;
                default:
                    mText.setBackgroundColor(Color.rgb(255, 255, 255));
                    break;
            }
```
4> 在NoteEditor中找到onOptionsItemSelected()方法，在菜单的switch中添加case相应的操作。
```javascript
        case R.id.menu_color:
            changeColor();
            break;
```
5> 在NoteEditor中添加函数changeColor()。
```javascript
    private final void changeColor() {
        Intent intent = new Intent(null,mUri);
        intent.setClass(NoteEditor.this,NoteColor.class);
        NoteEditor.this.startActivity(intent);
    }
```
6>  新建布局文件note_color.xml，用于显示背景颜色的选择样式和对应的笔记标签，这里的一种颜色对应一种标签，至于标签的说明会运用到笔记的分类中，下文有涉及。
```javascript
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="horizontal"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <LinearLayout
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_weight="1"
    android:orientation="vertical">
    <ImageButton
        android:id="@+id/color_white"
        android:layout_width="match_parent"
        android:layout_height="50dp"
        android:layout_weight="1"
        android:background="@color/colorWhite"
        android:onClick="white"/>
    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:layout_gravity="center"
        android:text="娱乐"/>
    </LinearLayout>
    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:orientation="vertical">
        <ImageButton
            android:id="@+id/color_yellow"
            android:layout_width="match_parent"
            android:layout_height="50dp"
            android:layout_weight="1"
            android:background="@color/colorYellow"
            android:onClick="yellow"/>
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:layout_gravity="center"
            android:text="工作"/>
    </LinearLayout>
    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:orientation="vertical">
        <ImageButton
            android:id="@+id/color_blue"
            android:layout_width="match_parent"
            android:layout_height="50dp"
            android:layout_weight="1"
            android:background="@color/colorBlue"
            android:onClick="blue"/>
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:layout_gravity="center"
            android:text="学习"/>
    </LinearLayout>
    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:orientation="vertical">
        <ImageButton
            android:id="@+id/color_green"
            android:layout_width="match_parent"
            android:layout_height="50dp"
            android:layout_weight="1"
            android:background="@color/colorGreen"
            android:onClick="green"/>
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:layout_gravity="center"
            android:text="社交"/>
    </LinearLayout>
    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:orientation="vertical">
        <ImageButton
            android:id="@+id/color_red"
            android:layout_width="match_parent"
            android:layout_height="50dp"
            android:layout_weight="1"
            android:background="@color/colorRed"
            android:onClick="red"/>
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:layout_gravity="center"
            android:text="生活"/>
    </LinearLayout>
</LinearLayout>
```
7> 新建NoteColor的activity，用作选择笔记本编辑的内部背景颜色，该Acitvity将被定义为对话框样式（方便使用）。该Actitvity在onPause()中将颜色存入数据库，Activity从NoteColor回到NoteEditor，NoteEditor被唤醒，会调用NoteEditor的onResume()，onResume()中有读取数据库颜色信息将设置背景的操作，就达到了换背景色的作用，并且也达到了NoteList中笔记颜色更改与编辑背景一致的效果。
```javascript
package com.example.mynotepad;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
public class NoteColor extends Activity {
    private Cursor mCursor;
    private Uri mUri;
    private int color;
    private static final int COLUMN_INDEX_TITLE = 1;
    private static final String[] PROJECTION = new String[] {
            NotePad.Notes._ID, // 0
            NotePad.Notes.COLUMN_NAME_BACK_COLOR,
    };
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_color);
        mUri = getIntent().getData();
        mCursor =getContentResolver().query(
                mUri,        // The URI for the note that is to be retrieved.
                PROJECTION,  // The columns to retrieve
                null,        // No selection criteria are used, so no where columns are needed.
                null,        // No where columns are used, so no where values are needed.
                null         // No sort order is needed.
        );

    }
    @Override
    protected void onResume(){
        if (mCursor != null) {
            mCursor.moveToFirst();
            color = mCursor.getInt(COLUMN_INDEX_TITLE);
        }
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        ContentValues values = new ContentValues();
        values.put(NotePad.Notes.COLUMN_NAME_BACK_COLOR, color);
        getContentResolver().update(mUri, values, null, null);

    }
    public void white(View view){
        color = NotePad.Notes.DEFAULT_COLOR;
        finish();
    }
    public void yellow(View view){
        color = NotePad.Notes.YELLOW_COLOR;
        finish();
    }
    public void blue(View view){
        color = NotePad.Notes.BLUE_COLOR;
        finish();
    }
    public void green(View view){
        color = NotePad.Notes.GREEN_COLOR;
        finish();
    }
    public void red(View view){
        color = NotePad.Notes.RED_COLOR;
        finish();
    }
}
```
8> 在AndroidManifest.xml中将NoteColor的Acitvity主题定义为对话框样式。
```javascript
        <activity
            android:name=".NoteColor"
            android:label="ChangeColor"
            android:theme="@android:style/Theme.Holo.Dialog"
            android:windowSoftInputMode="stateVisible" />
```
 `效果展示`

![效果](https://github.com/NickLYD/NickRep/blob/master/MyNotePad-master/photos/color0.jpg)

![效果](https://github.com/NickLYD/NickRep/blob/master/MyNotePad-master/photos/color1.jpg)

![效果](https://github.com/NickLYD/NickRep/blob/master/MyNotePad-master/photos/color2.jpg)

![效果](https://github.com/NickLYD/NickRep/blob/master/MyNotePad-master/photos/color3.jpg)

![效果](https://github.com/NickLYD/NickRep/blob/master/MyNotePad-master/photos/color4.jpg)
#### 笔记分类
1> 该功能的实现主要基于DrawLayout和Navigation,在上文也有提到，主要涉及的布局文件为listview.xml（上文提过，这里再贴一次，如果已在这里插入图片描述实现上文的布局文件可以跳过该步骤）。
```javascript
<?xml version="1.0" encoding="utf-8"?>
<android.support.v4.widget.DrawerLayout android:id="@+id/drawer_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <android.support.design.widget.CoordinatorLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <android.support.design.widget.AppBarLayout
            android:layout_width="match_parent"
            android:layout_height="200dp"
            android:fitsSystemWindows="true">

            <android.support.design.widget.CollapsingToolbarLayout
                android:id="@+id/collapsing_toolbar_layout"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                app:collapsedTitleTextAppearance="@style/style_textsize1"
                app:expandedTitleTextAppearance="@style/style_textsize"
                app:contentScrim="@color/colorPrimary"
                app:expandedTitleMarginEnd="10dp"
                app:expandedTitleMarginStart="10dp"
                app:layout_scrollFlags="scroll|exitUntilCollapsed">

                <ImageView
                    android:id="@+id/iv"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:scaleType="centerCrop"
                    android:src="@drawable/ic_greentea"
                    app:layout_collapseMode="parallax"
                    app:layout_collapseParallaxMultiplier="0.5" />

                <android.support.v7.widget.Toolbar
                    android:id="@+id/toolbar"
                    android:layout_width="match_parent"
                    android:layout_height="?attr/actionBarSize"
                    app:layout_collapseMode="pin" />
            </android.support.design.widget.CollapsingToolbarLayout>

        </android.support.design.widget.AppBarLayout>

        <android.support.v4.widget.NestedScrollView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:layout_behavior="@string/appbar_scrolling_view_behavior">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical">
                <com.example.mynotepad.NoScrollListView
                    android:id="@+id/tv"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"/>
            </LinearLayout>
        </android.support.v4.widget.NestedScrollView>
    </android.support.design.widget.CoordinatorLayout>
    <android.support.design.widget.NavigationView
        android:id="@+id/nav_view"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:layout_gravity="start"
        android:background="@color/design_default_color_primary"
        app:headerLayout="@layout/nav_header_main"
        app:menu="@menu/activity_main_drawer"
        >
    </android.support.design.widget.NavigationView>
</android.support.v4.widget.DrawerLayout>
```
2> 新建nav_header_main，设置侧滑栏的头部。
```javascript
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="@dimen/nav_header_height"
    android:background="@color/colorBlue"
    android:gravity="bottom"
    android:orientation="vertical"
    android:paddingLeft="@dimen/activity_horizontal_margin"
    android:paddingTop="@dimen/activity_vertical_margin"
    android:paddingRight="@dimen/activity_horizontal_margin"
    android:paddingBottom="@dimen/activity_vertical_margin"
    android:theme="@style/ThemeOverlay.AppCompat.Dark">

    <ImageView
        android:id="@+id/imageView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:contentDescription="@string/nav_header_desc"
        android:paddingTop="@dimen/nav_header_vertical_spacing"
        app:srcCompat="@mipmap/ic_launcher_round" />

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:paddingTop="@dimen/nav_header_vertical_spacing"
        android:text="NotePad User"
        android:textAppearance="@style/TextAppearance.AppCompat.Body1" />

    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/nav_header_subtitle" />

</LinearLayout>
```
3> 设置侧滑栏的菜单项布局activity_main_drawer，用以实现笔记本的分类功能，每个item对应一种标签笔记。
```javascript
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    tools:showIn="navigation_view">
    <item android:title="记事本分类">
        <menu>
            <item
                android:id="@+id/nav_all"
                android:icon="@color/colorBlack"
                android:title="全体" />
            <item
                android:id="@+id/nav_joy"
                android:icon="@color/colorWhite"
                android:title="娱乐" />
            <item
                android:id="@+id/nav_job"
                android:icon="@color/colorYellow"
                android:title="工作" />
            <item
                android:id="@+id/nav_study"
                android:icon="@color/colorBlue"
                android:title="学习" />
            <item
                android:id="@+id/nav_communication"
                android:icon="@color/colorGreen"
                android:title="社交" />
            <item
                android:id="@+id/nav_life"
                android:icon="@color/colorRed"
                android:title="生活" />
        </menu>
    </item>

</menu>
```
4> 在NoteList中添加toolbar与侧滑栏涉及的相关操作，编写Home按钮方便打开侧滑栏，这个Home旋转开关按钮实际上是通过ActionBarDrawerToggle代码绑定到toolbar上的，ActionBarDrawerToggle是和DrawerLayout搭配使用的，它可以改变android.R.id.home返回图标，监听drawer的显示和隐藏。ActionBarDrawerToggle的syncState()方法会和Toolbar关联，将图标放入到Toolbar上。
```javascript
private Toolbar toolbar;
private DrawerLayout drawerLayout;
toolbar = (Toolbar) findViewById(R.id.toolbar);
setSupportActionBar(toolbar);
drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
//以下是实现带toolbar上Home旋转开关按钮，通过点击按钮直接打开侧滑栏
ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,         R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
onNavigationItemSelected();
```
5> 在NoteList中实现onNavigationItemSelected方法，用于监听Navigation。
```javascript
public void onNavigationItemSelected() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.nav_all) {
                    onQueryTextChange(-1);
                } else if (id == R.id.nav_joy) {
                    onQueryTextChange(NotePad.Notes.DEFAULT_COLOR);

                } else if (id == R.id.nav_job) {
                    onQueryTextChange(NotePad.Notes.YELLOW_COLOR);

                } else if (id == R.id.nav_study) {
                    onQueryTextChange(NotePad.Notes.BLUE_COLOR);

                } else if (id == R.id.nav_communication) {
                    onQueryTextChange(NotePad.Notes.GREEN_COLOR);

                } else if (id == R.id.nav_life) {
                    onQueryTextChange(NotePad.Notes.RED_COLOR);

                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        // Handle navigation view item clicks here.
    }
```
6> 在NoteList中实现onQueryTextChange方法，这里的方法与上文的搜索功能中的方法原理一样，只是参数不一样。
```javascript
public void onQueryTextChange(int newText){
        String selection = NotePad.Notes.COLUMN_NAME_BACK_COLOR + "=?";
        String[] selectionArgs = { ""+newText+"" };
        if(newText!=-1){
            updatecursor =  getContentResolver().query(
                    getIntent().getData(),
                    PROJECTION,
                    selection,
                    selectionArgs,
                    NotePad.Notes.DEFAULT_SORT_ORDER
            );
        }else{
            updatecursor =  getContentResolver().query(
                    getIntent().getData(),
                    PROJECTION,
                    null,
                    null,
                    NotePad.Notes.DEFAULT_SORT_ORDER
            );
        }

        adapter = new ColorCursorAdapter(
                this,
                R.layout.noteslist_item,
                updatecursor,
                dataColumns,
                viewIDs
        );
        listView.setAdapter(adapter);
    }
```
 `效果展示`
“全体”显示全部笔记

![效果](https://github.com/NickLYD/NickRep/blob/master/MyNotePad-master/photos/navigation1.jpg)

娱乐标签的笔记分类：

![效果](https://github.com/NickLYD/NickRep/blob/master/MyNotePad-master/photos/navigation2.jpg)

工作标签的笔记分类：

![效果](https://github.com/NickLYD/NickRep/blob/master/MyNotePad-master/photos/navigation3.jpg)

学习标签的笔记分类：

![效果](https://github.com/NickLYD/NickRep/blob/master/MyNotePad-master/photos/navigation4.jpg)

社交标签的笔记分类：

![效果](https://github.com/NickLYD/NickRep/blob/master/MyNotePad-master/photos/navigation5.jpg)

生活标签的笔记分类：

![效果](https://github.com/NickLYD/NickRep/blob/master/MyNotePad-master/photos/navigation6.jpg)

#### 排序
1> 排序的实现比较简单，只要把Cursor的排序参数变换下就可以了。首先在菜单文件list_options_menu.xml中设计菜单布局。
```javascript
    <item
        android:id="@+id/menu_sort"
        android:title="menu_sort"
        android:icon="@android:drawable/ic_menu_sort_by_size"
        app:showAsAction="always" >
        <menu>
            <item
                android:id="@+id/menu_sort1"
                android:title="创建时间排序"/>
            <item
                android:id="@+id/menu_sort2"
                android:title="修改时间排序"/>
            <item
                android:id="@+id/menu_sort3"
                android:title="颜色排序"/>
        </menu>
    </item>
```
2> 在NotesList菜单switch下添加case操作，这里的颜色排序是根据存在数据库中颜色对应的int值来排序，关于颜色对应的int值可参考上文的契约类。
```javascript
        //创建时间排序
        case R.id.menu_sort1:
          updatecursor =  getContentResolver().query(
                  getIntent().getData(),
                  PROJECTION,
                  null,
                  null,
                  NotePad.Notes._ID
           );
          adapter = new ColorCursorAdapter(
                  this,
                  R.layout.noteslist_item,
                  updatecursor,
                  dataColumns,
                  viewIDs
          );
          listView.setAdapter(adapter);
          return true;
        //修改时间排序
        case R.id.menu_sort2:
          updatecursor = getContentResolver().query(
                  getIntent().getData(),
                  PROJECTION,
                  null,
                  null,
                  NotePad.Notes.DEFAULT_SORT_ORDER
          );
          adapter = new ColorCursorAdapter(
                  this,
                  R.layout.noteslist_item,
                  updatecursor,
                  dataColumns,
                  viewIDs
          );
          listView.setAdapter(adapter);
          return true;
        //颜色排序
        case R.id.menu_sort3:
            updatecursor = getContentResolver().query(
                  getIntent().getData(),
                  PROJECTION,
                  null,
                  null,
                  NotePad.Notes.COLUMN_NAME_BACK_COLOR
          );
          adapter = new ColorCursorAdapter(
                  this,
                  R.layout.noteslist_item,
                  updatecursor,
                  dataColumns,
                  viewIDs
                );
          listView.setAdapter(adapter);
          return true;
```

 `效果展示`

![效果](https://github.com/NickLYD/NickRep/blob/master/MyNotePad-master/photos/sort1.jpg)

按创建时间排序：

![效果](https://github.com/NickLYD/NickRep/blob/master/MyNotePad-master/photos/sort2.jpg)

按修改时间排序：

![效果](https://github.com/NickLYD/NickRep/blob/master/MyNotePad-master/photos/sort3.jpg)

按颜色排序：

![效果](https://github.com/NickLYD/NickRep/blob/master/MyNotePad-master/photos/sort4.jpg)

#### 导出笔记
1> 先在菜单文件editor_options_menu.xml中添加一个导出笔记的选项。
```javascript
    <item android:id="@+id/menu_output"
        android:title="Output diary" />
```
2> 在NoteEditor中找到onOptionsItemSelected()方法，在菜单的switch中添加case操作。
```javascript
        case R.id.menu_output:
            outputNote();
            break;
```
3> 在NoteEditor中添加函数outputNote()。
```javascript
    //跳转导出笔记的activity，将uri信息传到新的activity
    private final void outputNote() {
        Intent intent = new Intent(null,mUri);
        intent.setClass(NoteEditor.this,NoteOutput.class);
        NoteEditor.this.startActivity(intent);
    }
```
4> 在此之前，要对选择导出文件界面进行布局，新建布局output_text.xml，显示导出时的布局。
```javascript
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:paddingLeft="6dip"
    android:paddingRight="6dip"
    android:paddingBottom="3dip">
    <EditText android:id="@+id/output_name"
        android:maxLines="1"
        android:layout_marginTop="2dp"
        android:layout_marginBottom="15dp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:scrollHorizontally="true" />
    <Button android:id="@+id/output_ok"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="right"
        android:text="导出"
        android:onClick="OutputOk" />
</LinearLayout>
```
5> 创建OutputText的的Acitvity即NoteOutput，用来选择导出笔记时的标题名称,因为导出文件是写在onPause()中的，点击保存响应的函数里调用了finish()，而onPause()会在finish()之后调用，但是，onPause()在点击手机的返回键退出当前activity也会调用，也就是说，如果没有经过特殊处理，点击手机返回键也会进行导出文件，这与预想不符。所以特别设置了一个flag用于判断是否是点击导出按钮，是点击按钮，则flag设置为true，可以进行文件导出，否则flag为默认的false，判断语句为假，不能执行到导出文件的操作。
```javascript
package com.example.mynotepad;
import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class NoteOutput extends AppCompatActivity {
    private static final String[] PROJECTION = new String[] {
            NotePad.Notes._ID, // 0
            NotePad.Notes.COLUMN_NAME_TITLE, // 1
            NotePad.Notes.COLUMN_NAME_NOTE, // 2
            NotePad.Notes.COLUMN_NAME_CREATE_DATE, // 3
            NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE, // 4
    };
    //读取出的值放入这些变量
    private String TITLE;
    private String NOTE;
    private String CREATE_DATE;
    private String MODIFICATION_DATE;
    //读取该笔记信息
    private Cursor mCursor;
    //导出文件的名字
    private EditText mName;
    //NoteEditor传入的uri，用于从数据库查出该笔记
    private Uri mUri;
    //关于返回与保存按钮的一个特殊标记，返回的话不执行导出，点击按钮才导出
    private boolean flag = false;
    private static final int COLUMN_INDEX_TITLE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.output_text);
        mUri = getIntent().getData();
        mCursor = getContentResolver().query(
                mUri,        // The URI for the note that is to be retrieved.
                PROJECTION,  // The columns to retrieve
                null,        // No selection criteria are used, so no where columns are needed.
                null,        // No where columns are used, so no where values are needed.
                null         // No sort order is needed.
        );
        mName = (EditText) findViewById(R.id.output_name);
    }
    @Override
    protected void onResume(){
        super.onResume();
        if (mCursor != null) {
            // The Cursor was just retrieved, so its index is set to one record *before* the first
            // record retrieved. This moves it to the first record.
            mCursor.moveToFirst();
            //编辑框默认的文件名为标题，可自行更改
            mName.setText(mCursor.getString(COLUMN_INDEX_TITLE));
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mCursor != null) {
            //从mCursor读取对应值
            TITLE = mCursor.getString(mCursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_TITLE));
            NOTE =mCursor.getString(mCursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_NOTE));
            CREATE_DATE =mCursor.getString(mCursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_CREATE_DATE));
            MODIFICATION_DATE =mCursor.getString(mCursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE));
            //flag在点击导出按钮时会设置为true，执行写文件
            if (flag == true) {
                write();
            }
            flag = false;
        }
    }
    public void OutputOk(View v){
        flag = true;
        finish();
    }
    private void write()
    {
        try
        {
            // 获取SD卡的目录
            File sdDir = Environment.getExternalStorageDirectory();
            //创建文件目录
            File file = new File(sdDir.getCanonicalPath() + "/" + mName.getText() + ".txt");
            //写文件
            PrintWriter ps = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            ps.println("标题"+TITLE);
            ps.println(NOTE);
            ps.println("创建时间：" + CREATE_DATE);
            ps.println("最后一次修改时间：" + MODIFICATION_DATE);
            ps.close();
            Toast.makeText(this, "保存成功,保存位置为：" + sdDir.getCanonicalPath() + "/" + mName.getText() + ".txt", Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

```
6>在AndroidManifest.xml中将这个Acitvity主题定义为对话框样式：
```javascript
        <activity android:name=".NoteOutput"
            android:label="笔记导出的标题"
            android:theme="@style/Theme.AppCompat.Dialog"
            android:windowSoftInputMode="stateVisible">
        </activity>
```
7>在AndroidManifest.xml中加入对SD卡的读写权限，这里仅添加写权限，其实写权限就包括了读写权限
```javascript
    <!-- 向SD卡写入数据权限 -->
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```
8> 由于Android6.0以上提高了安全度，所以不但需要在AndroidManifest文件中进行配置，还需要使用API来对权限进行动态申请。于是在NoteList中添加对SD卡的读权限的getPermission方法，并在OnCreate中调用该方法。
```javascript
    public void getPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {//这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 1);
            }
        }
    }
```
 `效果展示`

![效果](https://github.com/NickLYD/NickRep/blob/master/MyNotePad-master/photos/output1.jpg)

![效果](https://github.com/NickLYD/NickRep/blob/master/MyNotePad-master/photos/output2.jpg)

![效果](https://github.com/NickLYD/NickRep/blob/master/MyNotePad-master/photos/output3.jpg)

![效果](https://github.com/NickLYD/NickRep/blob/master/MyNotePad-master/photos/output4.jpg)

![效果](https://github.com/NickLYD/NickRep/blob/master/MyNotePad-master/photos/output5.jpg)

![效果](https://github.com/NickLYD/NickRep/blob/master/MyNotePad-master/photos/output6.jpg)

![效果](https://github.com/NickLYD/NickRep/blob/master/MyNotePad-master/photos/output7.jpg)
