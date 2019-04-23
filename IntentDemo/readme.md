实验五  IntentDemo
=====
# `Web:`
- `MainActivity代码展示:`
```javascript
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

```
- `activity_main.xml代码展示:`
```javascript
<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity"
    android:orientation="vertical">

    <EditText
        android:id="@+id/edUrl"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textSize="20dp"
        android:hint="输入URL"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
    <Button
        android:id="@+id/but"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="访问"
        app:layout_constraintTop_toBottomOf="@id/edUrl"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        />

</android.support.constraint.ConstraintLayout>
```
# `WebView:`
- `MainActivity代码展示:`
```javascript
package com.example.webview;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView=(WebView)findViewById(R.id.myWebView);
        Bundle bundle=getIntent().getExtras();
        url=bundle.getString("url");
        WebSettings webSettings=webView.getSettings();//管理webview
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });


    }


}

```
- `activity_main.xml代码展示:`
```javascript
<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity"
    android:orientation="vertical">
    <WebView
        android:id="@+id/myWebView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</android.support.constraint.ConstraintLayout>
```
- `AndroidMainfest.xml代码展示:`
```javascript
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
package="com.example.webview">
<uses-permission android:name="android.permission.INTERNET"/>
<application
    android:allowBackup="true"
    android:icon="@mipmap/ic_launcher"
    android:label="@string/app_name"
    android:roundIcon="@mipmap/ic_launcher_round"
    android:supportsRtl="true"
    android:theme="@style/AppTheme"
    android:usesCleartextTraffic="true">
    <activity android:name=".MainActivity">
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />

            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>
        <intent-filter>
            <action android:name="android.intent.action.VIEW" />
            <category android:name="android.intent.category.DEFAULT" />
        </intent-filter>
    </activity>
</application>

</manifest>
```
- `效果截图`

![效果](https://github.com/NickLYD/NickRep/blob/master/IntentDemo/photos/webview1.jpg)

![效果](https://github.com/NickLYD/NickRep/blob/master/IntentDemo/photos/webview2.jpg)

![效果](https://github.com/NickLYD/NickRep/blob/master/IntentDemo/photos/webview3.jpg)