package com.example.jeremy.appsample.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jeremy.appsample.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by jeremy on 28/11/2016.
 */

public class ImageViewItemAdapter extends RecyclerView.Adapter<ImageViewItemAdapter.ImageViewHolder> {
    protected final String TAG = getClass().getSimpleName();
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private String[] mTitles;

    public ImageViewItemAdapter(Context context) {
        mTitles = context.getResources().getStringArray(R.array.titles);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageViewHolder imageViewHolder = new ImageViewHolder(mLayoutInflater.inflate(R.layout.item_image, parent, false));
        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.mTextView.setText(mTitles[position]);
    }

    @Override
    public int getItemCount() {
        return mTitles == null ? 0 : mTitles.length;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view)
        TextView mTextView;
        @BindView(R.id.image_view)
        ImageView mImageView;

        ImageViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.cv_item)
        void onItemClick() {
            Log.d("ImageViewHolder", "onClick--> position = " + getPosition() + " text = " + mTextView.getText());
        }
    }
}
