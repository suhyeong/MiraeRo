package org.androidtown.miraero;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
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

public class EventClickActivity extends AppCompatActivity implements RecyclerAdapter.OnItemClickListener {

    private int event_id;
    private Toolbar event_toolbar;

    final long one_byte = 1024 * 1024;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference mStroageRef = firebaseStorage.getReference();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_click);

        Intent intent = getIntent();
        event_id = intent.getExtras().getInt("event_id");

        event_toolbar = findViewById(R.id.event_click_toolbar);
        setSupportActionBar(event_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.click_event_item_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerAdapter = new RecyclerAdapter();
        recyclerView.setAdapter(recyclerAdapter);
        getData();
        recyclerAdapter.setOnItemClickListener(this);
    }

    //파이어베이스에서 정보 가져와 기획전에 맞는 아이템을 리스트에 넣기
    public void getData() {
        mDatabaseRef.child("Item").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ItemData : dataSnapshot.getChildren()) {
                    Item getData = ItemData.getValue(Item.class);

                    if(getData.getEvent_id() == event_id) {
                        final Item item = new Item();
                        item.setId(Integer.parseInt(ItemData.getKey()));
                        item.setName(getData.getName());
                        item.setContent(getData.getContent());
                        item.setEvent_id(getData.getEvent_id());
                        item.setSellcount(getData.getSellcount());
                        item.setNorth_or_South(getData.getNorth_or_South());

                        mStroageRef.child("/Item/" + item.getId() + ".jpg").getBytes(one_byte).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap getdata_bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                item.setBitmap(getdata_bitmap);
                                recyclerAdapter.addItem(item);
                                recyclerAdapter.notifyDataSetChanged();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.getMessage();
            }
        });
    }

    //리스트의 아이템 선택시 수행 이벤트
    @Override
    public void OnItemClick(View v, int pos) {
        long position = recyclerAdapter.getItemID(pos);
        Intent item_intent = new Intent(v.getContext(), ItemClickActivity.class);
        item_intent.putExtra("item_id", position);
        startActivity(item_intent);
    }

    //툴바 뒤로가기 버튼
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
