package org.sssta.qaq.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.sssta.qaq.R;

public abstract class BasePageFragment extends Fragment {


    public abstract String getTitle();


    public BasePageFragment() {
        // Required empty public constructor
    }
}
