package org.androidtown.miraero;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ItemOriginFragment extends Fragment {

    public ItemOriginFragment() {
        // Required empty public constructor
    }

    public static ItemOriginFragment newInstance(String param1, String param2) {
        ItemOriginFragment fragment = new ItemOriginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item_origin, container, false);
        return view;
    }

}
