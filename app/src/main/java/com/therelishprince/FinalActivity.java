package com.therelishprince;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class FinalActivity extends AppCompatActivity {

    private TextView Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        final String mId=getIntent().getStringExtra("orderId");

        Id=(TextView)findViewById(R.id.orderId);
        Id.setText("Order Id #"+mId);

    }
    private Boolean exit = false;
    @Override
    public void onBackPressed() {

//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent intent= new Intent(FinalActivity.this,Home.class);
        startActivity(intent);
        finish();

    }
}
