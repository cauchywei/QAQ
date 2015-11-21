package org.sssta.qaq.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.sssta.qaq.R;
import org.sssta.qaq.adapter.GalleryAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cauchywei on 15/11/22.
 */
public class CollectionFragment extends BasePageFragment {

    private RecyclerView recyclerView;
    private GalleryAdapter mAdapter;
    private List<Integer> ids = new ArrayList<>();

    @Override
    public String getTitle() {
        return "我的收藏";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hot_photo,container,false);


        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_hot_photo);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(layoutManager);


        ids.add(R.drawable.exp2);
        ids.add(R.drawable.exp6);
        ids.add(R.drawable.exp8);
        ids.add(R.drawable.exp12);
        ids.add(R.drawable.exp14);
        ids.add(R.drawable.exp17);


        mAdapter = new GalleryAdapter(getContext(),ids);
        recyclerView.setAdapter(mAdapter);

        return rootView;
    }
}
