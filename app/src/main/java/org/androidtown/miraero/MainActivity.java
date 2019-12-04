package org.androidtown.miraero;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private View layout;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;

    ViewFlipper eventviewFlipper;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference mStroageRef = firebaseStorage.getReference();

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;

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

        final long one_byte = 1024 * 1024;
        for(int i=0;i<3;i++) {
            final int finalI = i;
            mStroageRef.child("/Event/event"+(finalI+1)+".png").getBytes(one_byte).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    fllipperImages(bitmap);
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

        //final String[] list = {"list1","list2","list3","list4","list5","list6","list7","list8"};
        recyclerView = findViewById(R.id.best_item_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerAdapter = new RecyclerAdapter();
        recyclerView.setAdapter(recyclerAdapter);
        getData();
    }

    private void getData() {
        List<String> name = Arrays.asList("첫번째 아이템", "두번째 아이템", "세번째 아이템");
        List<String> content = Arrays.asList("첫번째 아이템 설명입니다","두번째 아이템 설명입니다아아아아아 일이삼사오 육칠팔구십 십일십이 십삼 되라 ~","세번째 아이템 설명입니다");
        List<Integer> id = Arrays.asList(R.drawable.p1, R.drawable.p2, R.drawable.p3);

        for(int i=0;i<name.size();i++) {
            Item item = new Item();
            item.setName(name.get(i));
            item.setContent(content.get(i));
            item.setId(id.get(i));

            //각 값에 들어간 데이터를 Adapter에 추가
            recyclerAdapter.addItem(item);
        }
        //Adapter의 값이 변경됐음을 알림
        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

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
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    public void fllipperImages(Bitmap bitmap) {
        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(bitmap);
        eventviewFlipper.addView(imageView);
    }

}
