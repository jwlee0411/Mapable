package app.jw.mapable.gm.AfterSearch;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appyvet.materialrangebar.RangeBar;
import com.github.mikephil.charting.charts.BarChart;
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
import java.net.URL;
import java.util.ArrayList;

import app.jw.mapable.gm.AfterRoad.AfterRoadActivity;
import app.jw.mapable.gm.R;

public class MapRecyclerAdapter extends RecyclerView.Adapter<MapRecyclerAdapter.ItemViewHolder> {


    RecyclerView thisRecyclerView;
    ConstraintLayout constraintLayout;
    int[] timeValue;

    ODsayService odsayService;

    Context context;
    private final ArrayList<Item> listItem = new ArrayList<>();

    BarChart barChart;
    RangeBar rangeBar;
    public MapRecyclerAdapter2 adapter2;
    SharedPreferences preferences;
    RecyclerView recyclerView;


    //TODO : 210520 추가
    ImageView imageIcon;

    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    // 직전에 클릭됐던 Item의 position
    private int prePosition = -1;


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler1, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(listItem.get(position), position);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    void addItem(Item item)
    {
        listItem.add(item);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        final TextView[] textViews = new TextView[6];

        ItemViewHolder(View itemView) {
            super(itemView);



            textViews[0] = itemView.findViewById(R.id.textTrafficDistance);
            textViews[1] = itemView.findViewById(R.id.textTotalTime);
            textViews[2] = itemView.findViewById(R.id.textPayment);
            recyclerView = itemView.findViewById(R.id.recyclerView2);
            //recyclerView.addItemDecoration(new DividerItemDecoration(itemView.getContext(), DividerItemDecoration.VERTICAL));
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));

            adapter2 = new MapRecyclerAdapter2();
            recyclerView.setAdapter(adapter2);

            barChart = itemView.findViewById(R.id.barChart);

            constraintLayout = itemView.findViewById(R.id.constraintLayout01);


        }

        void onBind(Item item, int position)
        {



            textViews[0].setText(item.getTrafficDistance());
            textViews[1].setText(item.getTotalTime());
            textViews[2].setText(item.getPayment());

            changeVisibility(selectedItems.get(position));

            int totalTime = Integer.parseInt(item.getTotalTime().replace("분", ""));






                float distance;

                String getWays = item.getWays();
                System.out.println("○" + getWays);
                preferences = textViews[0].getContext().getSharedPreferences("preferences", 0);
                String prevString = preferences.getString("prevString", "");
                System.out.println("○" + prevString);

                preferences.edit().putString("prevString", getWays).apply();
                getWays = getWays.replace(prevString, "");






                String[] waysSplit = getWays.split("§");
                System.out.println("§§§§§" + getWays);




                String[][] waysNewSplit = new String[waysSplit.length][16];

                preferences.edit().putString("finalGetWays", getWays).apply();

            String finalGetWays = getWays;




            constraintLayout.setOnClickListener(view -> {

                if(selectedItems.get(position))
                {
                    selectedItems.delete(position);
                }
                else
                {
                    selectedItems.delete(prePosition);
                    selectedItems.put(position, true);
                }
                if(prePosition != -1)
                {
                    notifyItemChanged(prePosition);
                }
                notifyItemChanged(position);

                prePosition = position;



                //TODO : 아마 필요없을듯
//                Intent intent = new Intent(itemView.getContext(), AfterRoadActivity.class);
//                intent.putExtra("ways", finalGetWays);
//                itemView.getContext().startActivity(intent);
            });


            odsayService = ODsayService.init(context, context.getString(R.string.odsay_key));
            odsayService.setReadTimeout(5000);
            odsayService.setConnectionTimeout(5000);





                for(int i = 0; i<waysSplit.length; i++)
                {

                    waysNewSplit[i] = waysSplit[i].split("※");
                    System.out.println("★" + waysSplit[i]);



                    Item2 item2 = new Item2();
                    switch (waysNewSplit[i][0])
                    {
                        case "1"://지하철
                            distance = (float) (Math.round((Integer.parseInt(waysNewSplit[i][1]) / 10))/100.0);
                            item2.setDistance(distance + "km");
                            item2.setSectionTime(waysNewSplit[i][2] + "분");
                            item2.setName(waysNewSplit[i][4]);
                            item2.setTrafficCount("지하철");
                            item2.setStartStation(waysNewSplit[i][6]);
                            item2.setEndStation(waysNewSplit[i][9]);
                            item2.setTrafficType(waysNewSplit[i][5]);






                            adapter2.addItem(item2);
                            break;
                        case "2": //버스
                            distance = (float) (Math.round((Integer.parseInt(waysNewSplit[i][1]) / 10))/100.0);
                            item2.setDistance(distance + "km");
                            item2.setSectionTime(waysNewSplit[i][2] + "분");
                            item2.setName(waysNewSplit[i][4]);
                            item2.setTrafficCount("버스");
                            item2.setStartStation(waysNewSplit[i][7]);
                            item2.setEndStation(waysNewSplit[i][10]);
                            item2.setTrafficType(waysNewSplit[i][5]);




                         //
                            System.out.println("§§§§§§§" + waysNewSplit[i][14]);

                            odsayService.requestBusStationInfo(waysNewSplit[i][14], onResultCallbackListener);



                            adapter2.addItem(item2);

                            System.out.println("2");
                            break;
                        case "3": //도보
                            distance = (float) (Math.round((Integer.parseInt(waysNewSplit[i][1]) / 10))/100.0);
                            item2.setDistance(distance + "km");
                            item2.setSectionTime(waysNewSplit[i][2] + "분");
                            item2.setTrafficCount("도보");
                            adapter2.addItem(item2);

                            System.out.println("3");
                            break;

                    }



                }

                adapter2.notifyDataSetChanged();







        }
    }

    private final OnResultCallbackListener onResultCallbackListener = new OnResultCallbackListener() {
        @Override
        public void onSuccess(ODsayData oDsayData, API api) {
            JSONObject jsonObject = oDsayData.getJson();
            JsonParse(jsonObject); //TODO



        }

        @Override
        public void onError(int i, String errorMessage, API api) {

        }
    };



    void JsonParse(JSONObject jsonObject)
    {
        try
        {
            JSONObject newObject = jsonObject.getJSONObject("result");
            String stationID = newObject.getString("arsID");

            //JSONArray pathArray = newObject.getJSONArray("path");


            System.out.println("§§§§§§§" +stationID);

            stationID = stationID.replace("-", "");




            //서울버스API : 서울진입하는 모든 버스 조회 가능

            StringBuilder buffer = new StringBuilder();
            String url = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByUid?serviceKey=sp6SSNzhDBUmWxvdUd28HDDq7E6j98Yvsmenqdy6rOCRyIlv605dNe8Wc7axsHxM759tgGzZpe%2Fr%2BTAb5nq7UA%3D%3D&arsId=" + stationID;

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



            }







        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void changeVisibility(final boolean isExpanded) {
        // height 값을 dp로 지정해서 넣고싶으면 아래 소스를 이용
        int dpValue = 150;
        float d = context.getResources().getDisplayMetrics().density;
        int height = (int) (dpValue * d);

        // ValueAnimator.ofInt(int... values)는 View가 변할 값을 지정, 인자는 int 배열
        ValueAnimator va = isExpanded ? ValueAnimator.ofInt(0, height) : ValueAnimator.ofInt(height, 0);
        // Animation이 실행되는 시간, n/1000초
        va.setDuration(600);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // value는 height 값
                int value = (int) animation.getAnimatedValue();
                // imageView의 높이 변경
                recyclerView.getLayoutParams().height = value;
                recyclerView.requestLayout();
                // imageView가 실제로 사라지게하는 부분
                recyclerView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            }
        });
        // Animation start
        va.start();
    }


}
