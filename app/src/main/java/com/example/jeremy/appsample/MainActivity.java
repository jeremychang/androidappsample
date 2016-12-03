package com.example.jeremy.appsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jeremy.appsample.web.WebInterfaceService;


public class MainActivity extends AppCompatActivity {

    public final static String TAG = "MainActivity";

    private ListView mListView;
    private String[] mList = {"test", "Map", "WebView", "Fragment",
                              "ViewPager", "BLE", "Retrofit",
                              "RecyclerView", "Picasso Sample",
                               "WebSocket"
                             };
    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        mListView = (ListView)findViewById(R.id.list_view);
        listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, mList);
        mListView.setAdapter(listAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                String info = ((TextView) view).getText().toString();
                if (info.equals("test")) {
                    sendMessage(view);
                }
                else if (info.equals("Map")) {
                    intent = new Intent(MainActivity.this, MapsActivity.class);
                    startActivity(intent);
                }
                else if (info.equals("WebView")) {
                    intent = new Intent(MainActivity.this, WebActivity.class);
                    startActivity(intent);
                }
                else if (info.equals("Fragment")) {
                    intent = new Intent(MainActivity.this, FragmentRootActivity.class);
                    startActivity(intent);
                }
                else if (info.equals("ViewPager")) {
                    intent = new Intent(MainActivity.this, ViewPagerActivity.class);
                    startActivity(intent);
                }
                else if (info.equals("BLE")) {
                    intent = new Intent(MainActivity.this, BLEActivity.class);
                    startActivity(intent);
                }
                else if (info.equals("Retrofit")) {
                    intent = new Intent(MainActivity.this, RetrofitDemoActivity.class);
                    startActivity(intent);
                }
                else if (info.equals("RecyclerView")) {
                    intent = new Intent(MainActivity.this, RecyclerViewSamplesActivity.class);
                    startActivity(intent);
                }
                else if (info.equals("Picasso Sample")) {
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.example.picasso.development");
                    if (launchIntent != null) {
                        startActivity(launchIntent);
                    }
                }
                else if (info.equals("WebSocket")) {
                    intent = new Intent(MainActivity.this, WebSocketClientActivity.class);
                    startActivity(intent);
                }
            }
        });

        WebInterfaceService.startWebServer(this);
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
                MyDialogBuilder builder = new MyDialogBuilder(this);
                builder.show();
                return true;
            case R.id.settings:
                Log.d(TAG, "go to settings");
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
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
        bundle.putString(Main2Activity.INPUT_NAME, "lala");
        intent.putExtra(Main2Activity.EXTRA_BUNDLE, bundle);
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
        WebInterfaceService.stopWebServer(this);
        Log.i(TAG, "onDestroy");
    }
}
