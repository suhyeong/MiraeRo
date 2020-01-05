package org.androidtown.miraero;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class ItemQNAFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private long item_id;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRef = database.getReference();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();

    Spinner spinner;
    private EditText question;
    private CheckBox not_open;
    private Button register;

    private TextView no_qna;
    private RecyclerView qna_list;
    private QnAAdapter qnaAdapter;
    private QNASpinnerAdapter qnaSpinnerAdapter;

    private String select_type;

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
        if(getArguments() != null) {
            item_id = getArguments().getLong("item_id");
        }
        View view = inflater.inflate(R.layout.fragment_item_qna, container, false);

        String value[] = {
                "상품 문의",
                "반품",
                "불량/오배송",
                "기타",
                "배송 문의"
        };

        no_qna = view.findViewById(R.id.no_qna);
        no_qna.setVisibility(View.GONE);

        qnaSpinnerAdapter = new QNASpinnerAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item);
        qnaSpinnerAdapter.addAll(value);
        qnaSpinnerAdapter.add("질문유형을 선택해주세요.");

        spinner = view.findViewById(R.id.qna_type_spinner);
        spinner.setAdapter(qnaSpinnerAdapter);
        spinner.setSelection(qnaSpinnerAdapter.getCount());
        spinner.setOnItemSelectedListener(this);

        question = view.findViewById(R.id.question_content);
        not_open = view.findViewById(R.id.not_open_checkbox);
        register = view.findViewById(R.id.question_register_button);
        register.setOnClickListener(this);

        qna_list = view.findViewById(R.id.qna_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        qna_list.setLayoutManager(linearLayoutManager);
        qnaAdapter = new QnAAdapter();
        qna_list.setAdapter(qnaAdapter);
        getQNAs();

        return view;
    }

    public void getQNAs() {
        mDatabaseRef.child("QNA").child(String.valueOf(item_id)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() == 0) {
                    no_qna.setVisibility(View.VISIBLE);
                    qna_list.setVisibility(View.GONE);
                } else {
                    for(DataSnapshot QnAs : dataSnapshot.getChildren()) {
                        QnA get_qna = QnAs.getValue(QnA.class);

                        QnA qna = new QnA();
                        qna.setDate(get_qna.getDate());
                        qna.setQwriter(get_qna.getQwriter());
                        qna.setWhat_open(get_qna.getWhat_open());
                        qna.setWhat_reply(get_qna.getWhat_reply());
                        qna.setQuestion(get_qna.getQuestion());
                        qna.setType(get_qna.getType());

                        qnaAdapter.addQnA(qna);
                        qnaAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.getMessage();
            }
        });
    }

    //등록하기 버튼 클릭시 수행 이벤트
    @Override
    public void onClick(View v) {
        if(question.getText().toString().length() == 0 || select_type.equals("질문유형을 선택해주세요.")) {
            Toast.makeText(getContext(), "질문 유형 선택 혹은 문의 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else {
            Snackbar snackbar = Snackbar.make(v, "문의를 남기시겠습니까?", Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
            snackbar.setAction("확인", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //확인 클릭시 DB에 저장
                    QnA new_qna = new QnA();
                    new_qna.setType(select_type);
                    new_qna.setDate(TodayDate());
                    new_qna.setWhat_reply(0);
                    //체크박스가 체크되어 있다면 비공개(0), 체크해제되어있다면 공개(1)
                    if(not_open.isChecked())
                        new_qna.setWhat_open(0);
                    else
                        new_qna.setWhat_open(1);
                    new_qna.setQwriter(user.getDisplayName());
                    new_qna.setQuestion(question.getText().toString());
                    mDatabaseRef.child("QNA").child(String.valueOf(item_id)).child(user.getUid()).setValue(new_qna)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getContext(), "문의가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                    spinner.setSelection(qnaSpinnerAdapter.getCount());
                                    question.setText(null);
                                }
                            });
                }
            });
        }
    }

    //스피너 클릭시 수행 이벤트
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        select_type = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //오늘 날짜 계산
    private String TodayDate() {
        Date currentTime = Calendar.getInstance().getTime();
        String date = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA).format(currentTime);
        return date;
    }
}
