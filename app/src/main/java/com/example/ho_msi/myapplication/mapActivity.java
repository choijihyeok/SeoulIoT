package com.example.ho_msi.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.skt.Tmap.TMapView;


public class mapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Button b = (Button)findViewById(R.id.orgActivity);
        b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(),
                        MainActivity.class);
                startActivity(intent);
            }
        });

        //선언
        LinearLayout linearLayoutTmap = (LinearLayout)findViewById(R.id.map_view);
        TMapView tMapView = new TMapView(this);

        //키 값
        //브라우저
        //tMapView.setSKTMapApiKey("f1fe2d9f-490e-403d-b6a9-8c698c5a63b0");
        //서버
        tMapView.setSKTMapApiKey("85e67e95-5ab4-4e81-b2f4-3a12816ed432");
        linearLayoutTmap.addView(tMapView);

    }



}
