package app.jw.mapable.gm;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.odsay.odsayandroidsdk.API;
import com.odsay.odsayandroidsdk.ODsayData;
import com.odsay.odsayandroidsdk.ODsayService;
import com.odsay.odsayandroidsdk.OnResultCallbackListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AfterSearchActivity extends AppCompatActivity{


    String[][][] pathAll;
    int pathAllLength;
    double startX, startY, endX, endY;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    //ProgressDialog progressDialog;

    ArrayList<Item> data = new ArrayList<>();

    public MapRecyclerAdapter adapter;

    Button buttonClose;

    RecyclerView recyclerView;
    String setWays = "";
    AfterSearchProgressDialog dialog;

    private ODsayService odsayService;

    SwipeRefreshLayout swipeRefreshLayout;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_search);

        buttonClose = findViewById(R.id.buttonClose);

        buttonClose.setOnClickListener(v -> {
            startActivity(new Intent(AfterSearchActivity.this, StartActivity.class));
            finish();
        });


        //ProgressDialog 초기화 및 실행
        dialog = new AfterSearchProgressDialog(AfterSearchActivity.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show(); // 보여주기


        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);


        preferences = getSharedPreferences("preferences", 0);
        preferences.edit().remove("prevString").apply();



        RecyclerView recyclerView = findViewById(R.id.recyclerVIew);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
     //   MapRecyclerAdapter mapRecyclerAdapter = new MapRecyclerAdapter(data);
     //   recyclerView.setAdapter(mapRecyclerAdapter);

        adapter = new MapRecyclerAdapter();
        recyclerView.setAdapter(adapter);


        sharedPreferences = getSharedPreferences("location", 0);
        editor = sharedPreferences.edit();



        init();
    }

    private void init() {

        Button bt_api_call = findViewById(R.id.bt_api_call);



        odsayService = ODsayService.init(AfterSearchActivity.this, getString(R.string.odsay_key));
        odsayService.setReadTimeout(5000);
        odsayService.setConnectionTimeout(5000);

        startX = getIntent().getDoubleExtra("startX", 0.0);
        startY = getIntent().getDoubleExtra("startY", 0.0);
        endX = getIntent().getDoubleExtra("endX", 0.0);
        endY = getIntent().getDoubleExtra("endY", 0.0);
        System.out.println("☆" + startX);

        editor.putString("startX", String.valueOf(startX));
        editor.putString("startY", String.valueOf(startY));
        editor.putString("endX", String.valueOf(endX));
        editor.putString("endY", String.valueOf(endY));
        editor.commit();


        odsayService.requestSearchPubTransPath(Double.toString(startY), Double.toString(startX), Double.toString(endY), Double.toString(endX), "0", "0", "0", onResultCallbackListener);


        bt_api_call.setOnClickListener(v-> odsayService.requestSearchPubTransPath(Double.toString(startY), Double.toString(startX), Double.toString(endY), Double.toString(endX), "0", "0", "0", onResultCallbackListener));





        swipeRefreshLayout.setOnRefreshListener(() -> {
            //odsayService.requestSearchPubTransPath(Double.toString(startY), Double.toString(startX), Double.toString(endY), Double.toString(endX), "0", "0", "0", onResultCallbackListener);
            Handler handler = new Handler();
            handler.postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 2000);
        });
        





    }



    private String getXmlData(String busStationName, String busStopX, String busStopY, String busNum)
    {
        StringBuilder buffer = new StringBuilder();

        String busStationID = null;
        System.out.println("");

        String firstUrl = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByName?serviceKey=sp6SSNzhDBUmWxvdUd28HDDq7E6j98Yvsmenqdy6rOCRyIlv605dNe8Wc7axsHxM759tgGzZpe%2Fr%2BTAb5nq7UA%3D%3D&stSrch=" + busStationName;

        try{
            URL url1 = new URL(firstUrl);
            InputStream inputStream = url1.openStream();
            System.out.println("◈들어옴");
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new InputStreamReader(inputStream, "UTF-8"));

            String tag;



                    xmlPullParser.next();

            int eventType = xmlPullParser.getEventType();


            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("start");
                        break;
                    case XmlPullParser.START_TAG:
                        tag = xmlPullParser.getName();
                        //조건 추가

                        if (tag.equals("arsId")) {
                            xmlPullParser.next();
                            buffer.append("정류장아이디" + xmlPullParser.getText()).append("\n");

                            busStationID = xmlPullParser.getText();
                            System.out.println("◈" + busStationID);
                        } else if (tag.equals("tmX")) {



                            xmlPullParser.next();

                            if(busStopX.equals(xmlPullParser.getText())) System.out.println("◈ + 같은정류장");
                            else System.out.println("\"◈\"" + busStopX + "//" + xmlPullParser.getText());

                            buffer.append("정류장엑스좌표" + xmlPullParser.getText()).append("\n");
                        } else if (tag.equals("tmY")) {
                            xmlPullParser.next();
                            buffer.append("정류장와이좌표" + xmlPullParser.getText()).append("\n");
                        }


                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        tag = xmlPullParser.getName();
                        if (tag.equals("item")) buffer.append("\n");
                        break;

                }

                eventType = xmlPullParser.next();
            }


        }
        catch (IOException | XmlPullParserException e) {
            System.out.println("◈Error");
            System.out.println("◈" + e);
            e.printStackTrace();
        }




        //String busStationID = "22010"; //TODO
        System.out.println("◈함수는들어옴" + busStationID);


        String url = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByUid?serviceKey=sp6SSNzhDBUmWxvdUd28HDDq7E6j98Yvsmenqdy6rOCRyIlv605dNe8Wc7axsHxM759tgGzZpe%2Fr%2BTAb5nq7UA%3D%3D&arsId=" + busStationID;

        try{
            URL url1 = new URL(url);
            InputStream inputStream = url1.openStream();
            System.out.println("◈들어옴");
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new InputStreamReader(inputStream, "UTF-8"));

            String tag;

            xmlPullParser.next();

            int eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                switch(eventType)
                {
                    case XmlPullParser.START_DOCUMENT: buffer.append("start"); break;
                    case XmlPullParser.START_TAG:
                        tag = xmlPullParser.getName();
                        if(tag.equals("arrmsg1")){
                            xmlPullParser.next();
                            System.out.println("◈" + xmlPullParser.getText());
                            buffer.append("버스도착1").append(xmlPullParser.getText()).append("\n");
                        }
                        else if(tag.equals("arrmsg2"))
                        {
                            xmlPullParser.next();
                            buffer.append("버스도착2").append(xmlPullParser.getText()).append("\n");
                        }
                        else if(tag.equals("busType1"))
                        {
                            xmlPullParser.next();
                            buffer.append("버스타입1").append(xmlPullParser.getText()).append("\n");
                        }
                        else if(tag.equals("busType2"))
                        {
                            xmlPullParser.next();
                            buffer.append("버스타입2").append(xmlPullParser.getText()).append("\n");
                        }
                        else if(tag.equals("rtNm"))
                        {
                            xmlPullParser.next();
                            System.out.println("◈" + xmlPullParser.getText());
                            buffer.append("버스번호").append(xmlPullParser.getText()).append("\n");
                        }
                        //arrmsg(1,2) : 버스 도착예정 + n번째 전
                        //busType(1,2) : 버스종류(0 일반 / 1 저상)
                        //rtNm : 버스번호



                        break;

                    case XmlPullParser.TEXT: break;
                    case XmlPullParser.END_TAG:
                        tag = xmlPullParser.getName();
                        if(tag.equals("item")) buffer.append("\n");
                        break;



                }

                eventType = xmlPullParser.next();
            }



        } catch (IOException | XmlPullParserException e) {
            System.out.println("◈Error");
            System.out.println("◈" + e);
            e.printStackTrace();
        }


        return buffer.toString();

    }




    private final OnResultCallbackListener onResultCallbackListener = new OnResultCallbackListener() {
        @Override
        public void onSuccess(ODsayData oDsayData, API api) {
            JSONObject jsonObject = oDsayData.getJson();
            JsonParse(jsonObject);



        }

        @Override
        public void onError(int i, String errorMessage, API api) {

        }
    };

    void JsonParse(JSONObject jsonObject)
    {

        try{
           System.out.println("☆" + jsonObject);
            JSONObject newObject = jsonObject.getJSONObject("result");
            JSONArray pathArray = newObject.getJSONArray("path");
            pathAllLength = pathArray.length();
            pathAll = new String[pathAllLength][1000][16];

            for(int i = 0; i<pathArray.length(); i++)
            {
                JSONObject pathObject = pathArray.getJSONObject(i);
                System.out.println(pathObject);
               // System.out.println(pathObject.getString("pathType"));
                    JSONObject infoObject = pathObject.getJSONObject("info");


                    pathAll[i][0][0] = infoObject.getString("trafficDistance"); //총 이동거리
                    pathAll[i][0][1] = infoObject.getString("totalWalk"); //걷는 거리
                    pathAll[i][0][2] = infoObject.getString("totalTime"); //걸리는 시간
                    pathAll[i][0][3] = infoObject.getString("payment"); // 요금

                    Item item = new Item();
                    float distance = (float) (Math.round((Integer.parseInt(pathAll[i][0][0]) / 10))/100.0);
                    item.setTrafficDistance("이동거리 " + distance + "km");
                    item.setTotalWalk("도보 " + pathAll[i][0][1] + "분");
                    item.setTotalTime(pathAll[i][0][2] + "분");
                    item.setPayment("카드 " + pathAll[i][0][3] + "원");




                JSONArray subPathArray = pathObject.getJSONArray("subPath");
                for(int j = 0; j<subPathArray.length(); j++)
                {
                    JSONObject subPathObject = subPathArray.getJSONObject(j);
                    int trafficType = subPathObject.getInt("trafficType");
                    pathAll[i][j+1][0] = Integer.toString(trafficType);
                    setWays = setWays + trafficType + "※";

                    switch (trafficType){
                        case 1: //지하철

                            pathAll[i][j+1][1] = subPathObject.getString("distance");//거리
                            pathAll[i][j+1][2] = subPathObject.getString("sectionTime");//걸리는시간
                            pathAll[i][j+1][3] = subPathObject.getString("stationCount");//정류장개수

                            System.out.println(subPathObject.getInt("distance")); //거리
                            System.out.println(subPathObject.getInt("sectionTime")); //걸리는시간
                            System.out.println(subPathObject.getInt("stationCount")); //정류장개수


                            JSONArray subwayLaneArray = subPathObject.getJSONArray("lane");
                            JSONObject subwayLaneObject = subwayLaneArray.getJSONObject(0);

                            pathAll[i][j+1][4] = subwayLaneObject.getString("name");//호선번호
                            pathAll[i][j+1][5] = subwayLaneObject.getString("subwayCode");//지하철 종류(분당, 수인, ..)

                            System.out.println(subwayLaneObject.getString("name")); //호선번호
                            System.out.println(subwayLaneObject.getInt("subwayCode")); //지하철 종류(분당, 수인, ..)

                            pathAll[i][j+1][6] = subPathObject.getString("startName");
                            pathAll[i][j+1][7] = subPathObject.getString("startX");
                            pathAll[i][j+1][8] = subPathObject.getString("startY");
                            pathAll[i][j+1][9] = subPathObject.getString("endName");
                            pathAll[i][j+1][10] = subPathObject.getString("endX");
                            pathAll[i][j+1][11] = subPathObject.getString("endY");


                            System.out.println(subPathObject.getString("startName")); //시작 정류소 이름
                            System.out.println(subPathObject.getDouble("startX")); //시작 정류소 좌표
                            System.out.println(subPathObject.getDouble("startY"));
                            System.out.println(subPathObject.getString("endName")); //도착 정류소 이름
                            System.out.println(subPathObject.getDouble("endX")); //도착 정류소 좌표
                            System.out.println(subPathObject.getDouble("endY"));


                            //way : 방향
                            //waycode
                            //door ; 빠른하차
                            //startExitX : 출구 좌표
                            JSONObject subwayStopObject = subPathObject.getJSONObject("passStopList");
                            JSONArray subwayStopArray = subwayStopObject.getJSONArray("stations");
                            String subwayStopList = "";
                            for(int k = 0; k<subwayStopArray.length(); k++)
                            {
                                JSONObject subwayNewStopObject = subwayStopArray.getJSONObject(k);
                                System.out.println(subwayNewStopObject.getString("stationName")); //중간 정류소 이름
                                System.out.println(subwayNewStopObject.getDouble("x")); //중간 정류소 좌표
                                System.out.println(subwayNewStopObject.getDouble("y"));

                                subwayStopList = subwayStopList + subwayNewStopObject.getString("stationName") + "☆" + subwayNewStopObject.getString("x") + "☆" + subwayNewStopObject.getString("y") + "★";
                            }
                            pathAll[i][j+1][12] = subwayStopList;

                            for(int l = 1; l<13; l++)
                            {
                                setWays = setWays + pathAll[i][j+1][l] + "※";
                            }
                            setWays = setWays + "§";
                            item.setWays(setWays);
                            System.out.println(setWays);

                            break;


                        case 2: //버스

                            pathAll[i][j+1][1] = subPathObject.getString("distance");//거리
                            pathAll[i][j+1][2] = subPathObject.getString("sectionTime");//걸리는시간
                            pathAll[i][j+1][3] = subPathObject.getString("stationCount");//정류장개수

                            System.out.println(subPathObject.getInt("distance")); //거리
                            System.out.println(subPathObject.getInt("sectionTime")); //걸리는시간
                            System.out.println(subPathObject.getInt("stationCount")); //정류장개수

                            JSONArray busLaneArray = subPathObject.getJSONArray("lane");
                            JSONObject busLaneObject = busLaneArray.getJSONObject(0);

                            pathAll[i][j+1][4] = busLaneObject.getString("busNo");//버스번호
                            pathAll[i][j+1][5] = busLaneObject.getString("type");//버스 종류(지선, 간선, ...)
                            pathAll[i][j+1][6] = busLaneObject.getString("busID");//버스ID(필요X)

                            System.out.println(busLaneObject.getString("busNo")); //버스번호
                            System.out.println(busLaneObject.getString("type")); //버스 종류(지선, 간선, ...)
                            System.out.println(busLaneObject.getString("busID")); //버스ID(필요X)

                            pathAll[i][j+1][7] = subPathObject.getString("startName");
                            pathAll[i][j+1][8] = subPathObject.getString("startX");
                            pathAll[i][j+1][9] = subPathObject.getString("startY");
                            pathAll[i][j+1][10] = subPathObject.getString("endName");
                            pathAll[i][j+1][11] = subPathObject.getString("endX");
                            pathAll[i][j+1][12] = subPathObject.getString("endY");

                            pathAll[i][j+1][14] = subPathObject.getString("startID");
                            System.out.println("◈" + subPathObject.getString("startID"));
                            pathAll[i][j+1][15] = subPathObject.getString("endID");

                            System.out.println(subPathObject.getString("startName")); //시작 정류소 이름
                            System.out.println(subPathObject.getDouble("startX")); //시작 정류소 좌표
                            System.out.println(subPathObject.getDouble("startY"));
                            System.out.println(subPathObject.getString("endName")); //도착 정류소 이름
                            System.out.println(subPathObject.getDouble("endX")); //도착 정류소 좌표
                            System.out.println(subPathObject.getDouble("endY"));

                            JSONObject busStopObject = subPathObject.getJSONObject("passStopList");
                            JSONArray busStopArray = busStopObject.getJSONArray("stations");
                            String busStopList = "";
                            for(int k = 0; k<busStopArray.length(); k++)
                            {
                                JSONObject busNewStopObject = busStopArray.getJSONObject(k);
                                System.out.println(busNewStopObject.getString("stationName")); //중간 정류소 이름
                                System.out.println(busNewStopObject.getDouble("x")); //중간 정류소 좌표
                                System.out.println(busNewStopObject.getDouble("y"));
                                busStopList = busStopList + busNewStopObject.getString("stationName") + "☆" + busNewStopObject.getString("x") + "☆" + busNewStopObject.getString("y") + "★";

                            }
                            pathAll[i][j+1][13] = busStopList;


                            for(int l = 1; l<14; l++)
                            {
                                setWays = setWays + pathAll[i][j+1][l] + "※";
                            }
                            setWays = setWays + "§";
                            item.setWays(setWays);
                            System.out.println(setWays);

                            //버스정류장 버스 도착정보


                            int finalI = i;
                            int finalJ = j;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String data = getXmlData(pathAll[finalI][finalJ +1][7], pathAll[finalI][finalJ +1][8], pathAll[finalI][finalJ +1][9], pathAll[finalI][finalJ +1][4] );
                                    System.out.println("◈" + data);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //UI 설정
                                        }
                                    });

                                }
                            }).start();





                            break;

                        case 3: //도보
                            pathAll[i][j+1][1] = subPathObject.getString("distance");//거리
                            pathAll[i][j+1][2] = subPathObject.getString("sectionTime");//걸리는시간
                            System.out.println(subPathObject.getInt("distance"));
                            System.out.println(subPathObject.getInt("sectionTime"));


                            for(int l = 1; l<3; l++)
                            {
                                setWays = setWays + pathAll[i][j+1][l] + "※";
                            }
                            setWays = setWays + "§";
                            item.setWays(setWays);
                            System.out.println(setWays);

                            break;
                    }

                }

                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    dialog.dismiss();
                    swipeRefreshLayout.setRefreshing(false);
                }, 1000);






                adapter.addItem(item);

            }

            adapter.notifyDataSetChanged();



            System.out.println(pathArray.length());

            System.out.println(pathArray.toString());
            //System.out.println(jsonObject.getString("result"));








        }
        catch (JSONException e)
        {
            try {
                JSONObject errorObject = jsonObject.getJSONObject("error");
                Toast.makeText(this, errorObject.getString("msg") + "\n출발지와 도착지를 올바른 곳으로 설정했는지 다시 한 번 확인해 주세요.", Toast.LENGTH_LONG).show();

            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(intent);
        finish();

    }
}
