package org.sssta.qaq.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.sssta.qaq.PhotoDetailActivity;
import org.sssta.qaq.R;

import java.util.List;

/**
 * Created by cauchywei on 15/11/22.
 */
public class GalleryAdapter extends RecyclerView.Adapter {


    private List<Integer> mIds;
    private LayoutInflater mInflater;
    private Context mContext;

    public class PhotoViewHolder extends RecyclerView.ViewHolder {

        public ImageView photo;

        public PhotoViewHolder(final View item) {
            super(item);
            photo = (ImageView) item.findViewById(R.id.photo);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PhotoDetailActivity.class);
                    intent.putExtra("id",mIds.get(getAdapterPosition()).intValue());
                    mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, v, "detailPhoto").toBundle());
                }
            });
        }
    }

    public GalleryAdapter(Context context, List<Integer> ids) {

        mInflater = LayoutInflater.from(context);
        mContext = context;
        mIds = ids;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PhotoViewHolder viewHolder = (PhotoViewHolder) holder;
        viewHolder.photo.setImageResource(mIds.get(position));

    }

    @Override
    public int getItemCount() {
        return mIds.size();
    }


}