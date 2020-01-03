package org.androidtown.miraero;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ReviewAdapter extends  RecyclerView.Adapter<ReviewAdapter.viewHolder> {

    private ArrayList<Review> reviews = new ArrayList<>();

    @Override
    public viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {
        viewHolder.review_content.setText(reviews.get(i).getRcontent());
        viewHolder.review_name.setText(reviews.get(i).getWriter());
        ReviewGrade(viewHolder, reviews.get(i).getGrade());
    }

    public void ReviewGrade(viewHolder viewHolder, int grade) {
        switch (grade) {
            case 1:
                viewHolder.review_grade.setImageResource(R.drawable.review_grade_1);
                break;
            case 2:
                viewHolder.review_grade.setImageResource(R.drawable.review_grade_2);
                break;
            case 3:
                viewHolder.review_grade.setImageResource(R.drawable.review_grade_3);
                break;
            case 4:
                viewHolder.review_grade.setImageResource(R.drawable.review_grade_4);
                break;
            case 5:
                viewHolder.review_grade.setImageResource(R.drawable.review_grade_5);
                break;
        }
    }

    void addReview(Review review) {
        reviews.add(review);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private ImageView review_grade;
        private TextView review_name, review_content;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            review_grade = itemView.findViewById(R.id.review_grade);
            review_name = itemView.findViewById(R.id.review_writer);
            review_content = itemView.findViewById(R.id.review_content);
        }
    }
}