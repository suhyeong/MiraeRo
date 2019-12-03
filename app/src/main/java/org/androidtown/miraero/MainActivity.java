package org.androidtown.miraero;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private View layout;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;

    ImageView before_event, next_event;
    ViewFlipper eventviewFlipper;

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance("gs://miraero-771a2.appspot.com/");
    StorageReference mStroageRef = firebaseStorage.getReference();
    StorageReference pathRaference;

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

        pathRaference = mStroageRef.child("Event/event1");
        pathRaference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Toast.makeText(getApplicationContext(), pathRaference.getPath().toString(), Toast.LENGTH_SHORT).show();
                fllipperImages(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });

//        for(int i=0;i<2;i++) {
//            //String string = mStroageRef.child("/Event/event"+(i+1)+".png").getParent().toString();
//            pathRaference = mStroageRef.child("/Event/event"+(i+1)+".png");
//            Toast.makeText(getApplicationContext(), pathRaference.getPath(), Toast.LENGTH_SHORT).show();
//            pathRaference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                    Toast.makeText(getApplicationContext(), pathRaference.getPath().toString(), Toast.LENGTH_SHORT).show();
//                    fllipperImages(uri);
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    e.printStackTrace();
//                }
//            });
//        }

//        mStroageRef = FirebaseStorage.getInstance("gs://miraero-771a2.appspot.com/").getReference();
//        mStroageRef.child("/Event").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                uri.get
//            }
//        })
//        mStroageRef.child("/Event").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                for(int i=0;i<event_image.length;i++) {
//                    event_image[i].setImageURI(uri);
//                }
//                event_image[0].setImageURI(uri);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                e.printStackTrace();
//            }
//        });
//
//        int images[] = {
//                R.drawable.event1,
//                R.drawable.event2
//        };
//
//        for(int image : images) {
//            fllipperImages(image);
//        }
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

    public void fllipperImages(Uri uri) {
        ImageView imageView = new ImageView(this);
        imageView.setImageURI(uri);
        //imageView.setBackgroundResource(image);
        //imageView.setBackground(background);

        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        eventviewFlipper.addView(imageView);
        eventviewFlipper.setFlipInterval(4000);
        eventviewFlipper.setAutoStart(true);

        eventviewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        eventviewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);
    }

}
