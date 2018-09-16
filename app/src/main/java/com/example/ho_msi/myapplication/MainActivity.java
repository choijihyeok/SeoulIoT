package com.example.ho_msi.myapplication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.sql.Timestamp;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;



import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import android.content.Intent;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Hackathon";
    //private static final String ACCESS_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiIxMjEiLCJjbGllbnRJZCI6ImhhY2thdGhvbiIsImlhdCI6MTUxMDE1OTczMywiZXhwIjoxNTExNDU1NzMzfQ.7VnFyl1EyRvzPaCqwt5hx-5cSvydbjLb6sJ8amgXhi8";
    private static final String ACCESS_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiIzMTgiLCJjbGllbnRJZCI6ImhhY2thdGhvbiIsImlhdCI6MTUzNTg3NjAwMiwiZXhwIjoxNTM3MTcyMDAyfQ.vPh0z28kc_4WNfQo7ESH8RwwkVeuC7GSSlaqjIEb3Qo";
    TextView txt1, txt2, textTime,
            txt3,textTime3,
            textTime2,
            txt4,textTime4;
    Button btn1, btn2;
    Button btn3, btn4;
    ProgressDialog progressDialog;
    int roach = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(roach == 1) {
            Intent intent = new Intent(this, loading.class);
            startActivity(intent);
            roach = 0;
        }


        Log.d(TAG, "onCreate");

        txt1 = (TextView) findViewById(R.id.txt1);
//        txt2 = (TextView) findViewById(R.id.txt02);

        textTime = (TextView) findViewById(R.id.time1);
        //날짜 시간 출력할 뷰

        btn1 = (Button) findViewById(R.id.btn01);
//        btn2 = (Button) findViewById(R.id.btn02);

        btn1.setOnClickListener(btn1Listener); // RESTful API GET
//        btn2.setOnClickListener(btn2Listener); // RESTful API PUT

        // 맵 전환하는 intent
        txt3 = (TextView) findViewById(R.id.txt3);
        textTime3 = (TextView) findViewById(R.id.time3);

        txt2 = (TextView) findViewById(R.id.txt2);
        textTime2 = (TextView) findViewById(R.id.time2);

        txt4 = (TextView) findViewById(R.id.txt4);
        textTime4 = (TextView) findViewById(R.id.time5);

        Button b = (Button)findViewById(R.id.newActivity);
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

    }


    private final View.OnClickListener btn1Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "Btn1 OnClickListener");
            //여기서 REST API 정보 받는것 설정
            //new JsonTaskGET().execute("https://api.sandbox.thingplus.net/v2/users/me");
            //new JsonTaskGET().execute("https://api.sandbox.thingplus.net/v2/gateways");
            //new JsonTaskGET().execute("https://api.sandbox.thingplus.net/v2/gateways/5ccf7fff21c6/sensors/led-5ccf7fff21c6-0/status");
            new JsonTaskGET().execute("https://api.sandbox.thingplus.net/v2/gateways/016598e9013e00000000000100100223/sensors/temperature-016598e9013e00000000000100100223-0/series");
            //new JsonTaskGET2().execute("https://api.sandbox.thingplus.net/v2/gateways/016598e9013e00000000000100100223/sensors/temperature-016598e9013e00000000000100100223-0/series");
            //new JsonTaskGET3().execute("https://api.sandbox.thingplus.net/v2/gateways/016598e9013e00000000000100100223/sensors/temperature-016598e9013e00000000000100100223-0/series");
            //new JsonTaskGET4().execute("https://api.sandbox.thingplus.net/v2/gateways/016598e9013e00000000000100100223/sensors/temperature-016598e9013e00000000000100100223-0/series");

        }
    };

//    private final View.OnClickListener btn2Listener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Log.d(TAG, "Btn2 OnClickListener");
//
//            new JsonTaskPUT().execute("https://api.sandbox.thingplus.net/v2/gateways/5ccf7fff21c6/sensors/led-5ccf7fff21c6-0/status");
//
//
//        }
//    };

    private class JsonTaskGET extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                connection.addRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
                connection.setRequestProperty("Accept", "application/json");

                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);


                }

                /////////////문자열 찾는 변수
                int findIndex;
                int subCnt;
                int timeIndex;
                String foundData;
                String updateTime;


                String subject = "value\":\"";
                subCnt = subject.length();

                findIndex = buffer.indexOf(subject);

                Log.d(TAG,"index succes!! ========== " + findIndex );
                Log.d(TAG, "index success!! ============ end " + buffer.indexOf("\"mtime"));


                foundData = buffer.substring(findIndex+subCnt, buffer.indexOf("\",\"mtime"));
                timeIndex = buffer.indexOf("time");
                updateTime = buffer.substring( timeIndex + 7 , buffer.indexOf("\",\"value"));


                //시간 계산
                long updateTimeL = Long.valueOf(updateTime);
                String ttt = changeTime(updateTimeL);
                textTime.setText(ttt);


                Log.d(TAG,"Time !!! end func : "+ ttt);
                Log.d(TAG,"found string!");

//                return buffer.toString();
                return foundData.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            txt1.setText(result);
            txt3.setText("0.01%");
            textTime3.setText("2018-09-16 13:03:32");
            txt2.setText("59%");
            textTime2.setText("2018-09-16 13:01:20");
            txt4.setText("0.02");
            textTime4.setText("2018-09-16 12:59:32");

        }
    }

    private class JsonTaskGET2 extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                connection.addRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
                connection.setRequestProperty("Accept", "application/json");

                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);


                }

                /////////////문자열 찾는 변수
                int findIndex;
                int subCnt;
                int timeIndex;
                String foundData;
                String updateTime;


                String subject = "value\":\"";
                subCnt = subject.length();

                findIndex = buffer.indexOf(subject);

                Log.d(TAG,"index succes!! ========== " + findIndex );
                Log.d(TAG, "index success!! ============ end " + buffer.indexOf("\"mtime"));


                foundData = buffer.substring(findIndex+subCnt, buffer.indexOf("\",\"mtime"));
                timeIndex = buffer.indexOf("time");
                updateTime = buffer.substring( timeIndex + 7 , buffer.indexOf("\",\"value"));


                //시간 계산
                long updateTimeL = Long.valueOf(updateTime);
                String ttt = changeTime(updateTimeL);
                textTime.setText(ttt);


                Log.d(TAG,"Time !!! end func : "+ ttt);
                Log.d(TAG,"found string!");

//                return buffer.toString();
                return foundData.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            txt1.setText(result);

        }
    }


    private class JsonTaskGET3 extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                connection.addRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
                connection.setRequestProperty("Accept", "application/json");

                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);


                }

                /////////////문자열 찾는 변수
                int findIndex;
                int subCnt;
                int timeIndex;
                String foundData;
                String updateTime;


                String subject = "value\":\"";
                subCnt = subject.length();

                findIndex = buffer.indexOf(subject);

                Log.d(TAG,"index succes!! ========== " + findIndex );
                Log.d(TAG, "index success!! ============ end " + buffer.indexOf("\"mtime"));


                foundData = buffer.substring(findIndex+subCnt, buffer.indexOf("\",\"mtime"));
                timeIndex = buffer.indexOf("time");
                updateTime = buffer.substring( timeIndex + 7 , buffer.indexOf("\",\"value"));


                //시간 계산
                long updateTimeL = Long.valueOf(updateTime);
                String ttt = changeTime(updateTimeL);
                textTime.setText(ttt);


                Log.d(TAG,"Time !!! end func : "+ ttt);
                Log.d(TAG,"found string!");

//                return buffer.toString();
                return foundData.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            txt1.setText(result);

        }
    }


    private class JsonTaskGET4 extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                connection.addRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
                connection.setRequestProperty("Accept", "application/json");

                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);


                }

                /////////////문자열 찾는 변수
                int findIndex;
                int subCnt;
                int timeIndex;
                String foundData;
                String updateTime;


                String subject = "value\":\"";
                subCnt = subject.length();

                findIndex = buffer.indexOf(subject);

                Log.d(TAG,"index succes!! ========== " + findIndex );
                Log.d(TAG, "index success!! ============ end " + buffer.indexOf("\"mtime"));


                foundData = buffer.substring(findIndex+subCnt, buffer.indexOf("\",\"mtime"));
                timeIndex = buffer.indexOf("time");
                updateTime = buffer.substring( timeIndex + 7 , buffer.indexOf("\",\"value"));


                //시간 계산
                long updateTimeL = Long.valueOf(updateTime);
                String ttt = changeTime(updateTimeL);
                textTime.setText(ttt);


                Log.d(TAG,"Time !!! end func : "+ ttt);
                Log.d(TAG,"found string!");

//                return buffer.toString();
                return foundData.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            txt1.setText(result);
        }
    }


    protected String changeTime(long sen) {
        long updateTimeL = Long.valueOf(sen);
        Log.d(TAG,"Start changeTime func : value = "+ sen);
        String pattern = "yyyy-MM-dd  HH:mm:ss.SSS";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String date = (String)formatter.format(new Timestamp(updateTimeL));
        Log.d(TAG, date);
        return date;
    }




//    private class JsonTaskPUT extends AsyncTask<String, String, String> {
//
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            progressDialog = new ProgressDialog(MainActivity.this);
//            progressDialog.setMessage("Please wait");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//        }
//
//        protected String doInBackground(String... params) {
//
//            HttpURLConnection connection = null;
//            BufferedReader reader = null;
//            BufferedWriter writer = null;
//
//            try {
//                URL url = new URL(params[0]);
//                connection = (HttpURLConnection) url.openConnection();
//                connection.setDoOutput(true);
//                connection.setRequestProperty("Content-Type","application/json");
//
//                connection.setRequestMethod("PUT");
//                connection.addRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
//                connection.setRequestProperty("Accept", "application/json");
//
//                connection.connect();
//
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("value", "off");
//                jsonObject.put("validDuration", 2000);
//
//                OutputStreamWriter out = new   OutputStreamWriter(connection.getOutputStream());
//                out.write(jsonObject.toString());
//                out.close();
//
//                StringBuilder sb = new StringBuilder();
//                int HttpResult = connection.getResponseCode();
//                if(HttpResult == HttpURLConnection.HTTP_OK){
//                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
//                    String line = null;
//                    while ((line = br.readLine()) != null) {
//                        sb.append(line + "\n");
//                    }
//                    br.close();
//
//                    Log.d(TAG, ""+sb.toString());
//
//                }else{
//
//                    Log.d(TAG, ""+sb.toString());
//                }
//
//                return String.valueOf(sb);
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } finally {
//                if (connection != null) {
//                    connection.disconnect();
//                }
//                try {
//                    if (reader != null) {
//                        reader.close();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            if (progressDialog.isShowing()){
//                progressDialog.dismiss();
//            }
//            txt2.setText(result);
//        }
//    }






}


