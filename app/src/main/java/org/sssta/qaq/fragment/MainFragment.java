package org.sssta.qaq.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.sssta.qaq.R;

/**
 * Created by cauchywei on 15/11/22.
 */
public class MainFragment extends BasePageFragment {


    private Button mCameraButton;
    private Button mGalleryButton;

    @Override
    public String getTitle() {
        return "制作";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main,container,false);
        return rootView;
    }
}
