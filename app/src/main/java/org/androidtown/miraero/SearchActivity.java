package org.androidtown.miraero;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements TextWatcher, RecyclerAdapter.OnItemClickListener {

    private Toolbar search_toolbar;
    private RecyclerView search_recyclerView;
    private EditText search_text;
    private SearchAdapter searchAdapter;
    private ArrayList<String> search_name_arrayList;

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference mStroageRef = firebaseStorage.getReference();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseRef = database.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search_toolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(search_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        search_recyclerView = findViewById(R.id.search_recyclerview);
        search_text = findViewById(R.id.search_edittext);
        search_text.addTextChangedListener(this);
        search_name_arrayList = new ArrayList<>();

        getData();
        searchAdapter = new SearchAdapter(getApplicationContext(), search_name_arrayList);
        search_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        search_recyclerView.setAdapter(searchAdapter);
        searchAdapter.setOnItemClickListener(this);
    }

    //데이터베이스에서 아이템 아이디, 이름, 비트맵 정보 가져오기
    public void getData() {
        mDatabaseRef.child("Item").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot item : dataSnapshot.getChildren()) {
                    Item getData = item.getValue(Item.class);
                    search_name_arrayList.add(getData.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //검색 텍스쳐 수정될때마다 수행 이벤트
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        searchAdapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    //검색 후 RecyclerView 아이템 선택시 수행 이벤트
    @Override
    public void OnItemClick(View v, int pos) {
        //String name = searchAdapter.get
        Toast.makeText(this, "클릭", Toast.LENGTH_SHORT).show();
    }
}
