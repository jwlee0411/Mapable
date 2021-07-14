package app.jw.mapable.gm.aftersearch;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.odsay.odsayandroidsdk.API;
import com.odsay.odsayandroidsdk.ODsayData;
import com.odsay.odsayandroidsdk.ODsayService;
import com.odsay.odsayandroidsdk.OnResultCallbackListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


import app.jw.mapable.gm.R;
import app.jw.mapable.gm.afterroad.AfterRoadActivity;


public class MapRecyclerAdapter extends RecyclerView.Adapter<MapRecyclerAdapter.ItemViewHolder> {


    ConstraintLayout constraintLayout;

    float distance;
    Item recyclerItem;
    ODsayService odsayService;
    final TextView[] textViews = new TextView[6];
    String[] waysSplit;
    String[][] waysNewSplit;
    Context context;
    private final ArrayList<Item> listItem = new ArrayList<>();


    public MapRecyclerAdapter2 adapter2;
    SharedPreferences preferences;
    RecyclerView recyclerView;



    StringBuilder buffer;
    String firstUrl, firstUrl2;

    private final SparseBooleanArray selectedItems = new SparseBooleanArray();
    // 직전에 클릭됐던 Item의 position
    private final int prePosition = -1;




    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler1, parent, false);

        odsayService = ODsayService.init(context, context.getString(R.string.odsay_key));
        odsayService.setReadTimeout(5000);
        odsayService.setConnectionTimeout(5000);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(listItem.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    void addItem(Item item) {
        listItem.add(item);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {


        ItemViewHolder(View itemView) {
            super(itemView);


            textViews[0] = itemView.findViewById(R.id.textTrafficDistance);
            textViews[1] = itemView.findViewById(R.id.textTotalTime);
            textViews[2] = itemView.findViewById(R.id.textPayment);
            textViews[3] = itemView.findViewById(R.id.textTotalWalk);
            recyclerView = itemView.findViewById(R.id.recyclerView2);
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));

            adapter2 = new MapRecyclerAdapter2();
            recyclerView.setAdapter(adapter2);

            constraintLayout = itemView.findViewById(R.id.constraintLayout01);


        }

        void onBind(Item item) {

            recyclerItem = item;
            setRouteArray();
            constraintLayout.setOnClickListener(v -> {

                Intent intent = new Intent(itemView.getContext(), AfterRoadActivity.class);
                intent.putExtra("ways", preferences.getString("finalGetWays", ""));
                itemView.getContext().startActivity(intent);


            });


        }


    }

    void setRouteArray() {
        String getWays = recyclerItem.getWays();
        System.out.println("○" + getWays);
        preferences = textViews[0].getContext().getSharedPreferences("preferences", 0);
        String prevString = preferences.getString("prevString", "");
        System.out.println("○" + prevString);

        preferences.edit().putString("prevString", getWays).apply();
        getWays = getWays.replace(prevString, "");


        preferences.edit().putString("finalGetWays", getWays).apply();


        waysSplit = getWays.split("§");
        waysNewSplit = new String[waysSplit.length][16];
        for(int i = 0; i<waysSplit.length; i++)
        {
            waysNewSplit[i] = waysSplit[i].split("※");
        }




        setAdapter();
    }

    void setAdapter() {
        textViews[0].setText(recyclerItem.getTrafficDistance());
        textViews[1].setText(recyclerItem.getTotalTime());
        textViews[2].setText(recyclerItem.getPayment());
        textViews[3].setText(recyclerItem.getTotalWalk());

        setRecyclerView();

    }

    void setRecyclerView() {


        for (int i = 0; i < waysSplit.length; i++) {

            Item2 item2 = new Item2();
            switch (waysNewSplit[i][0]) {
                case "1"://지하철
                    distance = (float) (Math.round((Integer.parseInt(waysNewSplit[i][1]) / 10)) / 100.0);
                    item2.setDistance(distance + "km");
                    item2.setSectionTime(waysNewSplit[i][2] + "분");
                    item2.setName(waysNewSplit[i][4]);
                    item2.setTrafficCount("지하철");
                    item2.setStartStation(waysNewSplit[i][6]);
                    item2.setEndStation(waysNewSplit[i][9]);
                    item2.setTrafficType(waysNewSplit[i][5]);

                    seoulSubwayLocationSearch(waysNewSplit[i][5]); //지하철번호 + 역명 + 상하행정보(상행 : 1, 하행 : 2)


                    adapter2.addItem(item2);
                    break;

                case "2": //버스



                    distance = (float) (Math.round((Integer.parseInt(waysNewSplit[i][1]) / 10)) / 100.0);
                    item2.setDistance(distance + "km");
                    item2.setSectionTime(waysNewSplit[i][2] + "분");
                    item2.setName(waysNewSplit[i][4]);
                    item2.setTrafficCount("버스");
                    item2.setStartStation(waysNewSplit[i][7]);
                    item2.setEndStation(waysNewSplit[i][10]);
                    item2.setTrafficType(waysNewSplit[i][5]);


                    if(i==1)
                    {
                        odsayService.requestBusStationInfo(waysNewSplit[i][14], onBusStopIdResultCallbackListener);
                    }




                    adapter2.addItem(item2);
                    break;
                case "3": //도보
                    distance = (float) (Math.round((Integer.parseInt(waysNewSplit[i][1]) / 10)) / 100.0);

                    //도보 거리가 0.0이 아닌 경우(동일 정류장인 경우) 도보 옵션을 RecyclerView에 띄우지 않기
                    if( distance != 0.0 && distance != 0)
                    {
                        item2.setDistance(distance + "km");
                        item2.setSectionTime(waysNewSplit[i][2] + "분");
                        item2.setTrafficCount("도보");
                        adapter2.addItem(item2);
                    }

                    break;

            }


        }

        adapter2.notifyDataSetChanged();

    }




    OnResultCallbackListener onBusStopIdResultCallbackListener = new OnResultCallbackListener() {
        @Override
        public void onSuccess(ODsayData oDsayData, API api) {
            JSONObject jsonObject = oDsayData.getJson();
            BusStopIdParse(jsonObject);

        }

        @Override
        public void onError(int i, String errorMessage, API api) {

        }
    };

    void seoulSubwayLocationSearch(String stationName) //지하철번호 + 역명 + 상하행정보(상행 : 1, 하행 : 2)
    {
        buffer = new StringBuilder();
        System.out.println("@함수 호출");

        firstUrl = "http://swopenAPI.seoul.go.kr/api/subway/sample/xml/realtimeStationArrival/0/5/" + stationName;

        firstUrl2 = "http://swopenAPI.seoul.go.kr/api/subway/sample/json/realtimeStationArrival/0/5/" + stationName;


        new seoulSubwayLocationSearch().execute();


    }


    private class seoulSubwayLocationSearch extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {

            System.out.println("@asynctask 호출");

            try
            {


                String tag;
                URL url1 = new URL(firstUrl);

                Connection.Response response = Jsoup.connect(firstUrl2).method(Connection.Method.GET).execute();
                Document document = response.parse();
                System.out.println("@" + document);



                InputStream inputStream = url1.openStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = factory.newPullParser();
                xmlPullParser.setInput(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                xmlPullParser.next();
                int eventType = 0;
                eventType = xmlPullParser.getEventType();


                while (eventType != XmlPullParser.END_DOCUMENT) {

                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            buffer.append("start");
                            break;
                        case XmlPullParser.START_TAG:
                            tag = xmlPullParser.getName();
                            System.out.println("@" + tag);
                            System.out.println("＠" + xmlPullParser.getText());
                            //조건 추가


                            if (tag.equals("subwayId")) {

                                xmlPullParser.next();

                                System.out.println("＠" + xmlPullParser.getText());
                                buffer.append("노선번호").append(xmlPullParser.getText()).append("\n");
                            }

                            if(tag.equals("trainLineNm"))
                            {

                            }

                            if(tag.equals("updnLine"))
                            {

                            }

                            if(tag.equals("arvlMsg2"))
                            {

                            }

                            //updnLine : 상행 / 하행
                            //trainLineNm : XX행 - XX방면
                            //arvlMsg2 : 현위치(전역 출발 / 2분 35초 후)


                            break;


                        case XmlPullParser.TEXT:
                            break;
                        case XmlPullParser.END_TAG:
                            tag = xmlPullParser.getName();
                            if (tag.equals("row")) buffer.append("\n");
                            break;

                    }

                    eventType = xmlPullParser.next();

                }
            }catch (XmlPullParserException | IOException e)
            {
                e.printStackTrace();
            }

            return null;
        }


    }

    void BusStopIdParse(JSONObject jsonObject) {
        try {
            JSONObject busStopObject = jsonObject.getJSONObject("result");
            String arsID = busStopObject.getString("arsID");


            System.out.println("§§§§§§§" + arsID);

            arsID = arsID.replace("-", "");

            seoulBusLocationSearch(arsID);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    void seoulBusLocationSearch(String arsID) {
        //서울버스API : 서울진입하는 모든 버스 조회 가능

        StringBuilder buffer = new StringBuilder();
        String url = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByUid?serviceKey=sp6SSNzhDBUmWxvdUd28HDDq7E6j98Yvsmenqdy6rOCRyIlv605dNe8Wc7axsHxM759tgGzZpe%2Fr%2BTAb5nq7UA%3D%3D&arsId=" + arsID;

        try {
            URL url1 = new URL(url);
            InputStream inputStream = url1.openStream();
            System.out.println("◈들어옴");
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

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
                        switch (tag) {
                            case "arrmsg1":
                                xmlPullParser.next();
                                System.out.println("◈" + xmlPullParser.getText());
                                buffer.append("버스도착1").append(xmlPullParser.getText()).append("\n");
                                break;
                            case "arrmsg2":
                                xmlPullParser.next();
                                buffer.append("버스도착2").append(xmlPullParser.getText()).append("\n");
                                break;
                            case "busType1":
                                xmlPullParser.next();
                                buffer.append("버스타입1").append(xmlPullParser.getText()).append("\n");
                                break;
                            case "busType2":
                                xmlPullParser.next();
                                buffer.append("버스타입2").append(xmlPullParser.getText()).append("\n");
                                break;
                            case "rtNm":
                                xmlPullParser.next();
                                System.out.println("◈" + xmlPullParser.getText());
                                buffer.append("버스번호").append(xmlPullParser.getText()).append("\n");
                                break;
                        }
                        //arrmsg(1,2) : 버스 도착예정 + n번째 전
                        //busType(1,2) : 버스종류(0 일반 / 1 저상)
                        //rtNm : 버스번호


                        break;

                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        tag = xmlPullParser.getName();
                        if (tag.equals("item")) buffer.append("\n");
                        break;


                }

                eventType = xmlPullParser.next();
            }


        } catch (IOException | XmlPullParserException ignored) {


        }
    }


}