实验一  HelloWorld
=====
`代码展示:`
----------
```javascript
package com.example.helloword;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
public class MainActivity extends AppCompatActivity {
    private final String tag="MainActivityLife";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(tag,"调用onCreate()");
    }
    @Override
    protected  void onStart(){
        super.onStart();
        Log.d(tag,"调用onStart()");
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.d(tag,"调用onResume()");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.d(tag,"调用onPause()");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.d(tag,"调用onStop()");
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        Log.d(tag,"调用onRestart()");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d(tag,"调用onDestroy()");
    }

}

```
`效果截图`
-----------
![效果](https://github.com/NickLYD/NickRep/blob/master/HelloWorld/photos/HelloWorld.jpg)