package com.example.jeremy.appsample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class RecyclerViewSamplesActivity extends AppCompatActivity {


    private ListView mListView;
    private String[] mList = {"RecyclerView", "RecyclerView2", "RecyclerView3"};
    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_samples);
        mListView = (ListView) findViewById(R.id.list_view);
        listAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mList);
        mListView.setAdapter(listAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                String info = ((TextView) view).getText().toString();
                if (info.equals("RecyclerView")) {
                    intent = new Intent(RecyclerViewSamplesActivity.this, RecyclerViewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(RecyclerViewActivity.LAYOUT_MODE, RecyclerViewActivity.LAYOUT_MODE_LINEAR);
                    intent.putExtra(RecyclerViewActivity.EXTRA_BUNDLE, bundle);
                    startActivity(intent);
                } else if (info.equals("RecyclerView2")) {
                    intent = new Intent(RecyclerViewSamplesActivity.this, RecyclerViewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(RecyclerViewActivity.LAYOUT_MODE, RecyclerViewActivity.LAYOUT_MODE_GRID);
                    intent.putExtra(RecyclerViewActivity.EXTRA_BUNDLE, bundle);
                    startActivity(intent);
                } else if (info.equals("RecyclerView3")) {
                    intent = new Intent(RecyclerViewSamplesActivity.this, RecyclerViewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(RecyclerViewActivity.LAYOUT_MODE, RecyclerViewActivity.LAYOUT_MODE_STAGGERED_GRID);
                    intent.putExtra(RecyclerViewActivity.EXTRA_BUNDLE, bundle);
                    startActivity(intent);
                }
            }
        });
    }

}
