package org.androidtown.miraero;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class QnAAdapter extends RecyclerView.Adapter<QnAAdapter.viewHolder> {

    private ArrayList<QnA> qnas = new ArrayList<>();

    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.qna, viewGroup, false);
        return new QnAAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {
        viewHolder.qna_date.setText(qnas.get(i).getDate());
        String name = qnas.get(i).getQwriter();
        String pname = name.substring(0, 1);
        for(int n=0;n<name.length()-1;n++) {
            pname = pname.concat("*");
        }
        viewHolder.qna_writer.setText(pname);
        if(qnas.get(i).getWhat_open() == 0) {
            //QnA가 비공개라면
            viewHolder.qna_question.setText("비밀글입니다.");
        } else {
            //QnA가 공개라면
            viewHolder.qna_question.setText(qnas.get(i).getQuestion());
        }
    }

    void addQnA(QnA qna) {
        qnas.add(qna);
    }

    @Override
    public int getItemCount() {
        return qnas.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private TextView qna_date, qna_writer, qna_question;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            qna_date = itemView.findViewById(R.id.qna_date);
            qna_writer = itemView.findViewById(R.id.qna_wirter);
            qna_question = itemView.findViewById(R.id.qna_contents);
        }
    }
}
