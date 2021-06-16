package app.jw.mapable.gm.AfterSearch;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.odsay.odsayandroidsdk.API;
import com.odsay.odsayandroidsdk.ODsayData;
import com.odsay.odsayandroidsdk.ODsayService;
import com.odsay.odsayandroidsdk.OnResultCallbackListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.jw.mapable.gm.R;
import app.jw.mapable.gm.Search.SearchActivity;
import app.jw.mapable.gm.Start.StartActivity;

public class AfterSearchActivity extends AppCompatActivity {


    String[][][] pathAll;
    String setWays = "", locationStart, locationEnd;
    int pathAllLength, MAX_PATHALL_SIZE = 24;
    double startX, startY, endX, endY;

    String startLocation, endLocation;

    LottieAnimationView lottieAnimationView;
    View progressView;
    TextView textViewStart, textViewEnd;
    MapRecyclerAdapter adapter;
    Button buttonClose, buttonOpposite;
    RecyclerView recyclerView;
    ODsayService odsayService;
    SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_search);

        findView();
    }

    private void findView() {
        buttonClose = findViewById(R.id.buttonClose);
        progressView = findViewById(R.id.view);
        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        textViewStart = findViewById(R.id.textViewStart);
        textViewEnd = findViewById(R.id.textViewEnd);
        lottieAnimationView = findViewById(R.id.lottieView2);
        buttonOpposite = findViewById(R.id.buttonOpposite);


        setonClick();
    }

    private void setonClick() {
        buttonClose.setOnClickListener(v -> {

            Intent intent = new Intent(AfterSearchActivity.this, StartActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });


        textViewStart.setOnClickListener(v -> {
            Intent intent = new Intent(AfterSearchActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        textViewEnd.setOnClickListener(v -> {
            Intent intent = new Intent(AfterSearchActivity.this, SearchActivity.class);
            startActivity(intent);
        });



        getPath();


    }

    private void getPath() {


        preferences = getSharedPreferences("preferences", 0);
        editor = preferences.edit();
        editor.remove("prevString").apply();

        editor.putBoolean("start", false);
        editor.putBoolean("end", false);
        editor.apply();

        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new MapRecyclerAdapter();
        recyclerView.setAdapter(adapter);


        startX = preferences.getFloat("startX", 0.0F);
        startY = preferences.getFloat("startY", 0.0F);
        endX = preferences.getFloat("endX", 0.0F);
        endY = preferences.getFloat("endY", 0.0F);

        startLocation = preferences.getString("startLocation", "");
        endLocation = preferences.getString("endLocation", "");
        startLocation = startLocation.replace("대한민국 ", "");
        endLocation = endLocation.replace("대한민국 ", "");


        textViewStart.setText(startLocation);
        textViewEnd.setText(endLocation);

        buttonOpposite.setOnClickListener(v -> {
            editor.putFloat("endX", (float) startX);
            editor.putFloat("endY", (float) startY);
            editor.putFloat("startX", (float)endX);
            editor.putFloat("startY", (float)endY);
            editor.putString("startLocation", endLocation);
            editor.putString("endLocation", startLocation);
            editor.apply();


            Intent intent = new Intent(this, AfterSearchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });


        if (startX != 0.0) {

            odsayService = ODsayService.init(AfterSearchActivity.this, getString(R.string.odsay_key));
            odsayService.setReadTimeout(5000);
            odsayService.setConnectionTimeout(5000);

            lottieAnimationView.setVisibility(View.VISIBLE);
            progressView.setVisibility(View.VISIBLE);

            editor.putString("startX", String.valueOf(startX));
            editor.putString("startY", String.valueOf(startY));
            editor.putString("endX", String.valueOf(endX));
            editor.putString("endY", String.valueOf(endY));
            editor.commit();


            odsayService.requestSearchPubTransPath(Double.toString(startY), Double.toString(startX), Double.toString(endY), Double.toString(endX), "0", "0", "0", onRoadFoundResultCallbackListener);


            //TODO : 추후 수정 예정, swiperefreshlayout 사용해서 실제 새로고침이 가능하도록
            swipeRefreshLayout.setOnRefreshListener(() -> {

                editor.putFloat("endX", (float) endX);
                editor.putFloat("endY", (float) endY);
                editor.putFloat("startX", (float)startX);
                editor.putFloat("startY", (float)startY);
                editor.putString("startLocation", startLocation);
                editor.putString("endLocation", endLocation);
                editor.apply();


                Intent intent = new Intent(this, AfterSearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();


                Handler handler = new Handler();
                handler.postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 2000);
            });
        }


    }


    private final OnResultCallbackListener onRoadFoundResultCallbackListener = new OnResultCallbackListener() {
        @Override
        public void onSuccess(ODsayData oDsayData, API api) {
            JSONObject jsonObject = oDsayData.getJson();
            RoadFoundParse(jsonObject);


        }

        @Override
        public void onError(int i, String errorMessage, API api) {

        }
    };

    void RoadFoundParse(JSONObject jsonObject) {

        try {
            JSONObject newObject = jsonObject.getJSONObject("result");
            JSONArray pathArray = newObject.getJSONArray("path");
            pathAllLength = pathArray.length();
            pathAll = new String[pathAllLength][1000][MAX_PATHALL_SIZE];

            for (int i = 0; i < pathArray.length(); i++) {
                JSONObject pathObject = pathArray.getJSONObject(i);
                System.out.println(pathObject);

                JSONObject infoObject = pathObject.getJSONObject("info");


                pathAll[i][0][0] = infoObject.getString("trafficDistance"); //총 이동거리
                pathAll[i][0][1] = infoObject.getString("totalWalk"); //걷는 거리
                pathAll[i][0][2] = infoObject.getString("totalTime"); //걸리는 시간
                pathAll[i][0][3] = infoObject.getString("payment"); // 요금

                Item item = new Item();
                float distance = (float) (Math.round((Integer.parseInt(pathAll[i][0][0]) / 10)) / 100.0);
                item.setTrafficDistance("이동거리 " + distance + "km");
                item.setTotalWalk("도보 " + pathAll[i][0][1] + "분");
                item.setTotalTime(pathAll[i][0][2] + "분");
                item.setPayment("카드 " + pathAll[i][0][3] + "원");


                JSONArray subPathArray = pathObject.getJSONArray("subPath");
                for (int j = 0; j < subPathArray.length(); j++) {
                    JSONObject subPathObject = subPathArray.getJSONObject(j);
                    int trafficType = subPathObject.getInt("trafficType");
                    pathAll[i][j + 1][0] = Integer.toString(trafficType);
                    setWays = setWays + trafficType + "※";

                    switch (trafficType) {
                        case 1: //지하철

                            pathAll[i][j + 1][1] = subPathObject.getString("distance");//거리
                            pathAll[i][j + 1][2] = subPathObject.getString("sectionTime");//걸리는시간
                            pathAll[i][j + 1][3] = subPathObject.getString("stationCount");//정류장개수


                            JSONArray subwayLaneArray = subPathObject.getJSONArray("lane");
                            JSONObject subwayLaneObject = subwayLaneArray.getJSONObject(0);

                            pathAll[i][j + 1][4] = subwayLaneObject.getString("name");//호선번호
                            pathAll[i][j + 1][5] = subwayLaneObject.getString("subwayCode");//지하철 종류(분당, 수인, ..)
                            pathAll[i][j + 1][6] = subPathObject.getString("startName");//시작 정류소 이름
                            pathAll[i][j + 1][7] = subPathObject.getString("startX");//시작 정류소 좌표
                            pathAll[i][j + 1][8] = subPathObject.getString("startY");
                            pathAll[i][j + 1][9] = subPathObject.getString("endName");//도착 정류소 이름
                            pathAll[i][j + 1][10] = subPathObject.getString("endX");//도착 정류소 좌표
                            pathAll[i][j + 1][11] = subPathObject.getString("endY");
                            //12번 인덱스 : 상세경로, 13번 인덱스 : 값 없음
                            pathAll[i][j + 1][14] = subPathObject.getString("startID");
                            pathAll[i][j + 1][15] = subPathObject.getString("endID");
                            pathAll[i][j + 1][16] = subPathObject.getString("wayCode"); //방향 정보 => 1 상행, 2 하행
                            pathAll[i][j + 1][17] = subPathObject.getString("door");

                            //아래 데이터가 없는 경우가 있기 때문에 try-catch문 사용해서 exception 처리
                            try
                            {
                                pathAll[i][j + 1][18] = subPathObject.getString("startExitNo"); //시작 정류소 출입구 번호
                                pathAll[i][j + 1][19] = subPathObject.getString("startExitX"); //시작 정류소 출입구 좌표
                                pathAll[i][j + 1][20] = subPathObject.getString("startExitY");
                            }
                            catch (JSONException e)
                            {
                                pathAll[i][j + 1][18] = "0"; pathAll[i][j + 1][19] = "0"; pathAll[i][j + 1][20] = "0";
                                e.printStackTrace();
                            }

                            try
                            {
                                pathAll[i][j + 1][21] = subPathObject.getString("endExitNo"); //도착 정류소 출입구 번호
                                pathAll[i][j + 1][22] = subPathObject.getString("endExitX"); //도착 정류소 출입구 좌표
                                pathAll[i][j + 1][23] = subPathObject.getString("endExitY");
                            }
                            catch (JSONException e)
                            {
                                pathAll[i][j + 1][21] = "0"; pathAll[i][j + 1][22] = "0"; pathAll[i][j + 1][23] = "0";
                                e.printStackTrace();
                            }



                            JSONObject subwayStopObject = subPathObject.getJSONObject("passStopList");
                            JSONArray subwayStopArray = subwayStopObject.getJSONArray("stations");
                            String subwayStopList = "";

                            for (int k = 0; k < subwayStopArray.length(); k++) {
                                JSONObject subwayNewStopObject = subwayStopArray.getJSONObject(k);
                                System.out.println(subwayNewStopObject.getString("stationName")); //중간 정류소 이름
                                System.out.println(subwayNewStopObject.getDouble("x")); //중간 정류소 좌표
                                System.out.println(subwayNewStopObject.getDouble("y"));

                                subwayStopList = subwayStopList + subwayNewStopObject.getString("stationName") + "☆" + subwayNewStopObject.getString("x") + "☆" + subwayNewStopObject.getString("y") + "★";
                            }
                            pathAll[i][j + 1][12] = subwayStopList;


                            for (int l = 1; l < MAX_PATHALL_SIZE; l++)

                            {
                                setWays = setWays + pathAll[i][j + 1][l] + "※";
                            }
                            setWays = setWays + "§";
                            item.setWays(setWays);
                            System.out.println(setWays);

                            break;


                        case 2: //버스

                            pathAll[i][j + 1][1] = subPathObject.getString("distance");//거리
                            pathAll[i][j + 1][2] = subPathObject.getString("sectionTime");//걸리는시간
                            pathAll[i][j + 1][3] = subPathObject.getString("stationCount");//정류장개수


                            JSONArray busLaneArray = subPathObject.getJSONArray("lane");
                            JSONObject busLaneObject = busLaneArray.getJSONObject(0);

                            pathAll[i][j + 1][4] = busLaneObject.getString("busNo");//버스번호
                            pathAll[i][j + 1][5] = busLaneObject.getString("type");//버스 종류(지선, 간선, ...)
                            pathAll[i][j + 1][6] = busLaneObject.getString("busID");//버스ID(필요X)
                            pathAll[i][j + 1][7] = subPathObject.getString("startName"); //시작 정류소 이름
                            pathAll[i][j + 1][8] = subPathObject.getString("startX");//시작 정류소 좌표
                            pathAll[i][j + 1][9] = subPathObject.getString("startY");
                            pathAll[i][j + 1][10] = subPathObject.getString("endName");//도착 정류소 이름
                            pathAll[i][j + 1][11] = subPathObject.getString("endX");//도착 정류소 좌표
                            pathAll[i][j + 1][12] = subPathObject.getString("endY");
                            //13 : 상세경로
                            pathAll[i][j + 1][14] = subPathObject.getString("startID");
                            pathAll[i][j + 1][15] = subPathObject.getString("endID");


                            JSONObject busStopObject = subPathObject.getJSONObject("passStopList");
                            JSONArray busStopArray = busStopObject.getJSONArray("stations");
                            String busStopList = "";
                            for (int k = 0; k < busStopArray.length(); k++) {
                                JSONObject busNewStopObject = busStopArray.getJSONObject(k);
                                System.out.println(busNewStopObject.getString("stationName")); //중간 정류소 이름
                                System.out.println(busNewStopObject.getDouble("x")); //중간 정류소 좌표
                                System.out.println(busNewStopObject.getDouble("y"));
                                busStopList = busStopList + busNewStopObject.getString("stationName") + "☆" + busNewStopObject.getString("x") + "☆" + busNewStopObject.getString("y") + "★";

                            }
                            pathAll[i][j + 1][13] = busStopList;


                            for (int l = 1; l < 16; l++) {
                                setWays = setWays + pathAll[i][j + 1][l] + "※";
                            }
                            setWays = setWays + "§";
                            item.setWays(setWays);
                            System.out.println(setWays);

                            break;

                        case 3: //도보
                            pathAll[i][j + 1][1] = subPathObject.getString("distance");//거리
                            pathAll[i][j + 1][2] = subPathObject.getString("sectionTime");//걸리는시간

                            for (int l = 1; l < 3; l++) {
                                setWays = setWays + pathAll[i][j + 1][l] + "※";
                            }
                            setWays = setWays + "§";
                            item.setWays(setWays);
                            System.out.println(setWays);
                            break;
                    }

                }


                adapter.addItem(item);

            }


            adapter.notifyDataSetChanged();
            fadeOutAnimation();
            swipeRefreshLayout.setRefreshing(false);


        } catch (JSONException e) {
            try {
                JSONObject errorObject = jsonObject.getJSONObject("error");
                Toast.makeText(this, errorObject.getString("msg") + "\n출발지와 도착지를 올바른 곳으로 설정했는지 다시 한 번 확인해 주세요.", Toast.LENGTH_LONG).show();
                fadeOutAnimation();

            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
                Toast.makeText(this, "알 수 없는 오류가 발생하였습니다.\n" + e, Toast.LENGTH_LONG).show();
                fadeOutAnimation();
            }

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), StartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }


    void fadeOutAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_fade_out);
        lottieAnimationView.startAnimation(animation);
        progressView.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                lottieAnimationView.setVisibility(View.GONE);
                progressView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
