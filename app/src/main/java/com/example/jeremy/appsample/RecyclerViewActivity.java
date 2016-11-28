package com.example.jeremy.appsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.example.jeremy.appsample.adapter.NormalRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewActivity extends AppCompatActivity {
    public final static String EXTRA_BUNDLE = "RecyclerViewActivity_extra_bundle";
    public final static String LAYOUT_MODE = "layout_mode";
    public final static int LAYOUT_MODE_LINEAR = 0;
    public final static int LAYOUT_MODE_GRID = 1;
    public final static int LAYOUT_MODE_STAGGERED_GRID = 2;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        ButterKnife.bind(this);
        Bundle bundle = this.getIntent().getBundleExtra(EXTRA_BUNDLE);
        int mode = bundle.getInt(this.LAYOUT_MODE);
        if (mode == LAYOUT_MODE_LINEAR) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        else if (mode == LAYOUT_MODE_GRID) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));//這裡用線性宮格顯示 類似於grid view
        }
        else if (mode == LAYOUT_MODE_STAGGERED_GRID) {
            mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));//這裡用線性宮格顯示 類似於瀑布流
        }
        mRecyclerView.setAdapter(new NormalRecyclerViewAdapter(this));
    }
}
