package com.example.ho_msi.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.io.IOException;
import java.util.ArrayList;


public class mapActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback{
    private static String TAG = "hack";
    private Context mContext = null;
    private boolean m_bTrackingMode = true;

    private TMapGpsManager tmapgps = null;
    private TMapView tMapView = null;
    private static String mApiKey = "85e67e95-5ab4-4e81-b2f4-3a12816ed432";
    private static int mMarkerID = 0;

    private ArrayList<TMapPoint> m_tmapPoint = new ArrayList<TMapPoint>();
    private ArrayList<String> mArrayMarkerID = new ArrayList<String>();
    private ArrayList<MapPoint> m_mapPoint = new ArrayList<MapPoint>();

    TMapMarkerItem markerItem1 = new TMapMarkerItem();
    TMapMarkerItem markerItem2 = new TMapMarkerItem();
    TMapMarkerItem markerItem3 = new TMapMarkerItem();
    TMapMarkerItem markerItem4 = new TMapMarkerItem();
    TMapMarkerItem markerItem5 = new TMapMarkerItem();
    TMapMarkerItem markerItem6 = new TMapMarkerItem();
    TMapMarkerItem markerItem7 = new TMapMarkerItem();
    TMapMarkerItem markerItem8 = new TMapMarkerItem();



    @Override
    public void onLocationChange(Location location) {
        Log.d(TAG ,""+ location.getLatitude() + " !!! " + location.getLongitude());
        double lat = location.getLatitude();
        double lon = location.getLongitude();


        if(m_bTrackingMode){
//            tMapView.setCenterPoint(lat,lon);
//            tMapView.setLocationPoint(lat, lon);

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mContext = this;





        Button b = (Button)findViewById(R.id.orgActivity);
        b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(),
                        mapActivity.class);
                startActivity(intent);
            }
        });

        Button b2 = (Button)findViewById(R.id.newActivity2);
        b2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(),
                        Main2Activity.class);
                startActivity(intent);
            }
        });

        Button b3 = (Button)findViewById(R.id.newActivity3);
        b3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(),
                        MainActivity.class);
                startActivity(intent);
            }
        });


        //선언
        RelativeLayout linearLayoutTmap = (RelativeLayout)findViewById(R.id.map_view);
        final TMapView tMapView = new TMapView(this);

        final int[] check = {1};

        Button creMar = (Button)findViewById(R.id.button2);
        creMar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(check[0] == 0) {
                    check[0] = 1;
                    createMarker(tMapView);
                }else{
                    check[0] = 0;
//                    tMapView.removeAllMarkerItem();
                    tMapView.removeMarkerItem("markerItem1");
                    tMapView.removeMarkerItem("markerItem3");
                    tMapView.removeMarkerItem("markerItem4");
                    tMapView.removeMarkerItem("markerItem6");
                }
            }
        });

        //키 값
        //브라우저
        //tMapView.setSKTMapApiKey("f1fe2d9f-490e-403d-b6a9-8c698c5a63b0");
        //서버
        tMapView.setSKTMapApiKey("85e67e95-5ab4-4e81-b2f4-3a12816ed432");
        linearLayoutTmap.addView(tMapView);

        tMapView.setIconVisibility(true);


        //addPoint();
        //showMarkerPoint();

        tMapView.setCompassMode(true);
        tMapView.setIconVisibility(true);
        tMapView.setZoomLevel(15);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);

        tmapgps = new TMapGpsManager(mapActivity.this);
        tmapgps.setMinTime(1000);
        tmapgps.setMinDistance(5);
        tmapgps.setProvider(tmapgps.NETWORK_PROVIDER);

        tmapgps.OpenGps();
//        Location L1 = null;
//        Log.d(TAG ,""+ L1.getLatitude() + L1.getLongitude());

//        tMapView.setLocationPoint(37.480624, 126.8960523);

//        37.480783, 126.895991
//        tMapView.setCenterPoint(37.480783, 126.895991);
//        tMapView.setLocationPoint(37.480783, 126.895991);
        tMapView.setCenterPoint(126.65318, 37.449666);
        tMapView.setLocationPoint(126.65318, 37.449666);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        tMapView.setZoomLevel(17);
        //37.448742, 126.653877

        createMarker(tMapView);
        //마커 생성



        tMapView.setTrackingMode(true);
        tMapView.setSightVisible(true);

        tMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
            @Override
            public void onCalloutRightButton(TMapMarkerItem tMapMarkerItem) {
                Toast.makeText(mapActivity.this, "Click", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void addPoint(){
        m_mapPoint.add(new MapPoint("강남",37.510350, 127.066847));
    }

    public void showMarkerPoint(){
        for(int i=0; i<m_mapPoint.size(); i++){
            TMapPoint point = new TMapPoint(m_mapPoint.get(i).getLatitude(),
                    m_mapPoint.get(i).getLongitude());
            TMapMarkerItem item1 = null;

            item1 = new TMapMarkerItem();

            Bitmap bitmap = null;
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);

            item1.setTMapPoint(point);
            item1.setName(m_mapPoint.get(i).getName());
            item1.setVisible(item1.VISIBLE);

            item1.setIcon(bitmap);

            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);

            item1.setCalloutTitle(m_mapPoint.get(i).getName());
            item1.setCalloutSubTitle("서울");
            item1.setCanShowCallout(true);
            item1.setAutoCalloutVisible(true);

            Bitmap bitmap_i = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);

            item1.setCalloutRightButtonImage(bitmap_i);

            String strID = String.format("pamarker%d", mMarkerID++);



            tMapView.addMarkerItem(strID, item1);
            mArrayMarkerID.add(strID);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void createMarker(TMapView tMapView){


//      마커1
        TMapMarkerItem markerItem1 = new TMapMarkerItem();
        TMapPoint tMapPoint1 = new TMapPoint(37.448742, 126.653877);

        // 마커 아이콘
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.end);

        markerItem1.setIcon(bitmap); // 마커 아이콘 지정
        markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
        markerItem1.setTMapPoint( tMapPoint1 ); // 마커의 좌표 지정
        markerItem1.setName(""); // 마커의 타이틀 지정
        tMapView.addMarkerItem("markerItem1", markerItem1); // 지도에 마커 추가

        tMapView.setCenterPoint( 37.448742, 126.653877);

//      마커 2
        TMapMarkerItem markerItem2 = new TMapMarkerItem();
        TMapPoint tMapPoint2 = new TMapPoint(37.448486, 126.652536);

        // 마커 아이콘
        Bitmap bitmap2 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.smokeduc);

        markerItem2.setIcon(bitmap2); // 마커 아이콘 지정
        markerItem2.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
        markerItem2.setTMapPoint( tMapPoint2 ); // 마커의 좌표 지정
        markerItem2.setName(""); // 마커의 타이틀 지정
        tMapView.addMarkerItem("markerItem2", markerItem2); // 지도에 마커 추가

        tMapView.setCenterPoint( 37.448486, 126.652536);

//        37.450015, 126.653577
        TMapMarkerItem markerItem3 = new TMapMarkerItem();
        TMapPoint tMapPoint3 = new TMapPoint(37.450015, 126.653577);

        // 마커 아이콘
        Bitmap bitmap3 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.end);

        markerItem3.setIcon(bitmap3); // 마커 아이콘 지정
        markerItem3.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
        markerItem3.setTMapPoint( tMapPoint3 ); // 마커의 좌표 지정
        markerItem3.setName(""); // 마커의 타이틀 지정
        tMapView.addMarkerItem("markerItem3", markerItem3); // 지도에 마커 추가

        tMapView.setCenterPoint( 37.450015, 126.653577);

//        37.450577, 126.652273
        TMapMarkerItem markerItem4 = new TMapMarkerItem();
        TMapPoint tMapPoint4 = new TMapPoint(37.450577, 126.652273);

        // 마커 아이콘
        Bitmap bitmap4 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.end);

        markerItem4.setIcon(bitmap4); // 마커 아이콘 지정
        markerItem4.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
        markerItem4.setTMapPoint( tMapPoint4 ); // 마커의 좌표 지정
        markerItem4.setName(""); // 마커의 타이틀 지정
        tMapView.addMarkerItem("markerItem4", markerItem4); // 지도에 마커 추가

        tMapView.setCenterPoint( 37.450577, 126.652273);

//        37.450185, 126.654102

        TMapMarkerItem markerItem5 = new TMapMarkerItem();
        TMapPoint tMapPoint5 = new TMapPoint(37.450185, 126.654102);

        // 마커 아이콘
        Bitmap bitmap5 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.smokeduc);

        markerItem5.setIcon(bitmap5); // 마커 아이콘 지정
        markerItem5.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
        markerItem5.setTMapPoint( tMapPoint5 ); // 마커의 좌표 지정
        markerItem5.setName(""); // 마커의 타이틀 지정
        tMapView.addMarkerItem("markerItem5", markerItem5); // 지도에 마커 추가

        tMapView.setCenterPoint( 37.450185, 126.654102);


//        37.447587, 126.652713
        TMapMarkerItem markerItem6 = new TMapMarkerItem();
        TMapPoint tMapPoint6 = new TMapPoint(37.447587, 126.652713);

        // 마커 아이콘
        Bitmap bitmap6 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.end);

        markerItem6.setIcon(bitmap6); // 마커 아이콘 지정
        markerItem6.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
        markerItem6.setTMapPoint( tMapPoint6 ); // 마커의 좌표 지정
        markerItem6.setName(""); // 마커의 타이틀 지정
        tMapView.addMarkerItem("markerItem6", markerItem6); // 지도에 마커 추가

        tMapView.setCenterPoint( 37.447587, 126.652713);

//        37.449906, 126.651173
        TMapMarkerItem markerItem7 = new TMapMarkerItem();
        TMapPoint tMapPoint7 = new TMapPoint(37.449906, 126.651173);

        // 마커 아이콘
        Bitmap bitmap7 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.smokeduc);

        markerItem7.setIcon(bitmap7); // 마커 아이콘 지정
        markerItem7.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
        markerItem7.setTMapPoint( tMapPoint7 ); // 마커의 좌표 지정
        markerItem7.setName(""); // 마커의 타이틀 지정
        tMapView.addMarkerItem("markerItem7", markerItem7); // 지도에 마커 추가

        tMapView.setCenterPoint( 37.449906, 126.651173);


//        37.451165, 126.653129

        TMapMarkerItem markerItem8 = new TMapMarkerItem();
        TMapPoint tMapPoint8 = new TMapPoint( 37.451165, 126.653129);

        // 마커 아이콘
        Bitmap bitmap8 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.smokeduc);

        markerItem8.setIcon(bitmap8); // 마커 아이콘 지정
        markerItem8.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
        markerItem8.setTMapPoint( tMapPoint8 ); // 마커의 좌표 지정
        markerItem8.setName(""); // 마커의 타이틀 지정
        tMapView.addMarkerItem("markerItem8", markerItem8); // 지도에 마커 추가

        tMapView.setCenterPoint(  37.451165, 126.653129);

    }


}


























