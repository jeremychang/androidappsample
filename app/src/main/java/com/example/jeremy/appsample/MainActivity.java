package com.example.jeremy.appsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.example.jeremy.widgetsample.MESSAGE";
    public final static String EXTRA_BUNDLE = "com.example.jeremy.widgetsample.BUNDLE";
    public final static String INPUT_NAME = "NAME";
    public final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new Button.OnClickListener(){
                                             @Override
                                             public void onClick(View view) {
                                                 sendMessage(view);
                                             }
                                         }
        );
        Button buttonMap = (Button)findViewById(R.id.buttonMap);
        buttonMap.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
        Button buttonWebView = (Button)findViewById(R.id.buttonWebView);
        buttonWebView.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WebActivity.class);
                startActivity(intent);
            }
        });

        Log.i(TAG, "onCreate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        //return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help:
                Log.d(TAG, "Help selected");
                return true;
            case R.id.version:
                Log.d(TAG, "version selected");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, Main2Activity.class);

        Bundle bundle = new Bundle();
        bundle.putString(INPUT_NAME, "lala");
        intent.putExtra(EXTRA_BUNDLE, bundle);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
}
