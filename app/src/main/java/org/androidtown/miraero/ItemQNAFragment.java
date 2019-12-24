package org.androidtown.miraero;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ItemQNAFragment extends Fragment {

    public ItemQNAFragment() {
        // Required empty public constructor
    }

    public static ItemQNAFragment newInstance(String param1, String param2) {
        ItemQNAFragment fragment = new ItemQNAFragment();
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
        View view = inflater.inflate(R.layout.fragment_item_qna, container, false);
        return view;
    }

}
