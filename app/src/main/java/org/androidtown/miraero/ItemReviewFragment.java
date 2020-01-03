package org.androidtown.miraero;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ItemReviewFragment extends Fragment {

    private RecyclerView recyclerView;
    private ReviewAdapter reviewAdapter;

    private long item_id;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRef = database.getReference();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public ItemReviewFragment() {
        // Required empty public constructor
    }

    public static ItemReviewFragment newInstance(String param1, String param2) {
        ItemReviewFragment fragment = new ItemReviewFragment();
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
        if(getArguments() != null) {
            item_id = getArguments().getLong("item_id");
        }
        View view = inflater.inflate(R.layout.fragment_item_review, container, false);

        recyclerView = view.findViewById(R.id.review_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        reviewAdapter = new ReviewAdapter();
        recyclerView.setAdapter(reviewAdapter);
        getReviews();
        return view;
    }

    public void getReviews() {
        mDatabaseRef.child("Review").child(String.valueOf(item_id)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot Reviews : dataSnapshot.getChildren()) {
                    Review get_review = Reviews.getValue(Review.class);

                    Review review = new Review();
                    review.setGrade(get_review.getGrade());
                    review.setRcontent(get_review.getRcontent());
                    String writer_id = Reviews.getKey();
                    review.setWriter(writer_id);
                    reviewAdapter.addReview(review);
                    reviewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.getMessage();
            }
        });
    }

}
