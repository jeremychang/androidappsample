package com.example.jeremy.appsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {

    public final static String TAG = "Main2Activity";
    public final static String EXTRA_BUNDLE = "Main2Activity.BUNDLE";
    public final static String INPUT_NAME = "Main2Activity.NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setTitle("Hello Activity");

        TextView textView = (TextView) findViewById(R.id.textview);
        Bundle b = this.getIntent().getBundleExtra(EXTRA_BUNDLE);
        String name = b.getString(INPUT_NAME);
        textView.setText(name);
        Log.i(TAG, "onCreate");
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                // Show a Toast to say Back:
                Toast.makeText(this, "Back!", Toast.LENGTH_SHORT).show();
        }
        return super.onKeyUp(keyCode, event);
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
