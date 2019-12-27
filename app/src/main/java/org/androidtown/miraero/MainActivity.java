package org.androidtown.miraero;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

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
import java.util.Comparator;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RecyclerAdapter.OnItemClickListener, View.OnClickListener {

    private View layout;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;

    private ViewFlipper eventviewFlipper;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference mStroageRef = firebaseStorage.getReference();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseRef = database.getReference();

    final long one_byte = 1024 * 1024;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private ArrayList<Integer> event_order = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        layout = findViewById(R.id.main_layout);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        eventviewFlipper = findViewById(R.id.event_image_slide);
        EventFliperImage();
        eventviewFlipper.setOnClickListener(this);

        recyclerView = findViewById(R.id.best_item_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerAdapter = new RecyclerAdapter();
        recyclerView.setAdapter(recyclerAdapter);
        getData();
        recyclerAdapter.setOnItemClickListener(this);
    }

    //기획전 이미지 불러오기
    private void EventFliperImage() {
        //mStroageRef.child("Event").
        for(int i=0;i<3;i++) {
            final int finalI = i;
            mStroageRef.child("/Event/event"+(finalI+1)+".png").getBytes(one_byte).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    flipperImages(bitmap);
                    event_order.add(finalI+1);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                }
            });
        }

        eventviewFlipper.setFlipInterval(3000);
        eventviewFlipper.setAutoStart(true);
        eventviewFlipper.setInAnimation(this, R.anim.translate_toleft_eventimage);
        eventviewFlipper.setOutAnimation(this, R.anim.translate_toright_eventimage);
    }

    public void flipperImages(Bitmap bitmap) {
        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(bitmap);
        eventviewFlipper.addView(imageView);
    }

    //파이어베이스에서 정보 가져와 Itemlist에 넣기
    public void getData() {
        mDatabaseRef.child("Item").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ItemData : dataSnapshot.getChildren()) {
                    Item getData = ItemData.getValue(Item.class);

                    final Item item = new Item();
                    item.setId(Integer.parseInt(ItemData.getKey()));
                    item.setName(getData.getName());
                    item.setContent(getData.getContent());
                    item.setEvent_id(getData.getEvent_id());
                    item.setSellcount(getData.getSellcount());
                    item.setNorth_or_South(getData.getNorth_or_South());

                    mStroageRef.child("/Item/"+item.getId()+".jpg").getBytes(one_byte).addOnSuccessListener(new OnSuccessListener<byte[]>() {
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.getMessage();
            }
        });
    }

    //툴바 활성화
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    //툴바 메뉴 클릭시 이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_search:
                Snackbar search_snackbar = Snackbar.make(layout, "검색 클릭 !", Snackbar.LENGTH_SHORT);
                search_snackbar.show();
                return true;
            case R.id.action_cart:
                Snackbar cart_snackbar = Snackbar.make(layout, "카트 클릭 !", Snackbar.LENGTH_SHORT);
                cart_snackbar.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                Toast.makeText(this, "홈 클릭 !", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_order:
                Toast.makeText(this, "주문/배송 내역 클릭 !", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_notice:
                Toast.makeText(this, "공지사항 클릭 !", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_customer_service:
                Toast.makeText(this, "고객센터 클릭 !", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_about_app:
                Toast.makeText(this, "about 미래로 클릭 !", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                Toast.makeText(this, "로그아웃 클릭 !", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    //RecyclerView 리스트마다 클릭시 수행 이벤트
    @Override
    public void OnItemClick(View v, int pos) {
        long position = recyclerAdapter.getItemID(pos);
        Intent item_intent = new Intent(v.getContext(), ItemClickActivity.class);
        item_intent.putExtra("item_id", position);
        startActivity(item_intent);
    }

    //기획전 클릭시 수행 이벤트
    @Override
    public void onClick(View v) {
        Intent event_intent = new Intent(v.getContext(), EventClickActivity.class);
        switch (eventviewFlipper.getDisplayedChild()) {
            case 0:
                event_intent.putExtra("event_id", event_order.get(0));
                startActivity(event_intent);
                break;
            case 1:
                event_intent.putExtra("event_id", event_order.get(1));
                startActivity(event_intent);
                break;
            case 2:
                event_intent.putExtra("event_id", event_order.get(2));
                startActivity(event_intent);
                break;
        }
    }
}
