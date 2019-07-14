package com.therelishprince;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.therelishprince.Common.Common;
import com.therelishprince.model.Token;


public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    private TextView phoneNumber;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabsPagerAdapter mTabsPagerAdapter;
    private String mCurrentUser;


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

        mViewPager=(ViewPager) findViewById(R.id.main_tabs_pager);
        mTabsPagerAdapter=new TabsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mTabsPagerAdapter);

        mTabLayout=(TabLayout) findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setSelectedTabIndicatorColor(Color.parseColor("#3e3a3a"));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    startActivity(new Intent(Home.this,CartActivity.class));

                } catch (ActivityNotFoundException e) {
                    //TODO smth
                }
            }

        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.toggleColor));
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View mView = navigationView.getHeaderView(0);
        phoneNumber = (TextView)mView.findViewById(R.id.userPhone);
        phoneNumber.setText(mCurrentUser);



            mAuth = FirebaseAuth.getInstance();

            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    if (firebaseAuth.getCurrentUser() == null) {
                        startActivity(new Intent(Home.this, MainActivity.class));
                    }
                    else
                    {
                        updateToken(FirebaseInstanceId.getInstance().getToken(),firebaseAuth);
                    }
                }
            };
        }

    private void updateToken(String token,FirebaseAuth firebaseAuth) {

        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference tokens=db.getReference("Tokens");
        Token data= new Token(token,false);
        tokens.child(mCurrentUser).setValue(data);
    }


    private Boolean exit = false;
        @Override
        public void onBackPressed () {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected (MenuItem item){
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.nav_home)
            {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

            }
            else if (id == R.id.nav_festive) {
                Intent intent= new Intent(Home.this,FestiveActivity.class);
                startActivity(intent);

            } else if (id == R.id.nav_cart) {
                Intent intent= new Intent(Home.this,CartActivity.class);
                startActivity(intent);

            } else if (id == R.id.nav_orders) {
                Intent intent= new Intent(Home.this,OrderActivity.class);
                startActivity(intent);

            } else if (id == R.id.nav_share) {

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "Check out The Relish Prince, I use it to order amazing and delicious food. Get it for free at-  https://play.google.com/store/apps/details?id=com.therelishprince");
                startActivity(sharingIntent);

            } else if (id == R.id.nav_rate) {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.therelishprince");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }else if (id == R.id.nav_feedback)
            {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","therelishprince2622@email.com", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }

            else if (id == R.id.nav_about) {
                Intent intent= new Intent(Home.this,AboutActivity.class);
                startActivity(intent);
            }
            else if (id == R.id.nav_logout) {
                mAuth.signOut();
                Intent startIntent = new Intent(Home.this, MainActivity.class);
                startActivity(startIntent);
                finish();
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
    }


