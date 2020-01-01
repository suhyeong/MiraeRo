package org.androidtown.miraero;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements TextWatcher, SearchAdapter.OnItemClickListener {

    private Toolbar search_toolbar;
    private RecyclerView search_recyclerView;
    private EditText search_text;
    private SearchAdapter searchAdapter;
    private ArrayList<Item> search_arrayList;

    final long one_byte = 1024 * 1024;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference mStroageRef = firebaseStorage.getReference();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference sDatabaseRef = database.getReference();

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
        search_arrayList = new ArrayList<>();

        //search_getData();
        search_getData();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        search_recyclerView.setLayoutManager(linearLayoutManager);
        searchAdapter = new SearchAdapter(getApplicationContext(), search_arrayList);
        search_recyclerView.setAdapter(searchAdapter);
        searchAdapter.setOnItemClickListener(this);
    }

    //데이터베이스에서 아이템 아이디, 이름, 비트맵 정보 가져오기
    public void search_getData() {
        sDatabaseRef.child("Item").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot item : dataSnapshot.getChildren()) {
                    Item getData = item.getValue(Item.class);

                    final Item get_item = new Item();
                    get_item.setId(Integer.parseInt(item.getKey()));
                    get_item.setName(getData.getName());

                    //search_arrayList.add(get_item);
                    mStroageRef.child("/Item/"+get_item.getId()+".jpg").getBytes(one_byte).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap getdata_bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            get_item.setBitmap(getdata_bitmap);
                            search_arrayList.add(get_item);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.getMessage();
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
        long search_id = searchAdapter.getItemID(pos);
        Intent search_item_click = new Intent(v.getContext(), ItemClickActivity.class);
        search_item_click.putExtra("item_id", search_id);
        startActivity(search_item_click);
        //Toast.makeText(this, String.valueOf(searchAdapter.getItemID(pos)), Toast.LENGTH_SHORT).show();
    }
}
