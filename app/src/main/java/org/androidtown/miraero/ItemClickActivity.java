package org.androidtown.miraero;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
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

public class ItemClickActivity extends AppCompatActivity implements TabLayout.BaseOnTabSelectedListener {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseRef = database.getReference();

    final long one_byte = 1024 * 1024;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference mStroageRef = firebaseStorage.getReference();

    private ImageView item_image;
    private TextView item_name, item_content, item_price;
    private Toolbar item_toolbar;
    private TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_click);

        Intent intent = getIntent();
        long item_id = intent.getExtras().getLong("item_id");

        item_toolbar = findViewById(R.id.item_toolbar);
        setSupportActionBar(item_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        item_image = findViewById(R.id.item_click_image);
        item_name = findViewById(R.id.item_click_name);
        item_content = findViewById(R.id.item_click_content);
        item_price = findViewById(R.id.item_price);
        getItemData(item_id);

        tabLayout = findViewById(R.id.item_tab_layout);
        //tabLayout.addOnTabSelectedListener(this);

        viewPager = findViewById(R.id.item_tab_viewpager);

        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        //viewPager.setAdapter(tabPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(this);
    }

    public void getItemData(final long item_id) {
        mDatabaseRef.child("Item").child(String.valueOf(item_id)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Item item = dataSnapshot.getValue(Item.class);

                item_name.setText(item.getName());
                item_content.setText(item.getContent());
                item_price.setText(String.valueOf(item.getPrice()));
                item_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mStroageRef.child("/Item/"+item_id+".jpg").getBytes(one_byte).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        item_image.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });

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

    //탭 레이아웃 선택시
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
