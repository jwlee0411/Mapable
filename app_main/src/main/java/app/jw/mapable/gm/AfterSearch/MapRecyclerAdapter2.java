package app.jw.mapable.gm.AfterSearch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.jw.mapable.gm.AfterRoad.AfterRoadActivity;
import app.jw.mapable.gm.R;

public class MapRecyclerAdapter2 extends RecyclerView.Adapter<MapRecyclerAdapter2.ItemViewHolder> {


    private final ArrayList<Item2> listItem = new ArrayList<>();


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler2, parent, false);

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
    void addItem(Item2 item)
    {
        listItem.add(item);
    }
    void removeItem(Item2 item)
    {
        listItem.remove(item);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder
    {
        final TextView[] textViews = new TextView[11];
        ImageView imageIcon;
        ImageView imageIcon2;
        SharedPreferences preferences;

        View colorView;

        ItemViewHolder(View itemView)
        {
            super(itemView);







            textViews[0] = itemView.findViewById(R.id.textDistance);
            textViews[1] = itemView.findViewById(R.id.textSectionTime);
            textViews[2] = itemView.findViewById(R.id.textTrafficCount);
            textViews[3] = itemView.findViewById(R.id.textName);
            textViews[4] = itemView.findViewById(R.id.textStartStation);
            textViews[5] = itemView.findViewById(R.id.textEndStation);

            imageIcon2 = itemView.findViewById(R.id.imageViewTransportationType);
            imageIcon = itemView.findViewById(R.id.imageViewMainIcon);

            textViews[6] = itemView.findViewById(R.id.textDistance2);
            textViews[7] = itemView.findViewById(R.id.textSectionTime2);
            textViews[8] = itemView.findViewById(R.id.textSection2);

            textViews[9] = itemView.findViewById(R.id.textSection);
            textViews[10] = itemView.findViewById(R.id.textArrive);

            colorView = itemView.findViewById(R.id.lineView);


            preferences = textViews[0].getContext().getSharedPreferences("preferences", 0);
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), AfterRoadActivity.class);
                intent.putExtra("ways", preferences.getString("finalGetWays", ""));
                itemView.getContext().startActivity(intent);

            });

        }

        void onBind(Item2 item)
        {
            textViews[2].setText(item.getTrafficCount());
            if(item.getTrafficCount().equals("지하철"))
            {

                for(int i = 0; i<6; i++)
                {
                    textViews[i].setVisibility(View.VISIBLE);
                }

                for(int i = 6; i<9; i++)
                {
                    textViews[i].setVisibility(View.GONE);
                }
                imageIcon.setVisibility(View.VISIBLE);
                textViews[0].setVisibility(View.GONE);


                textViews[1].setText(item.getSectionTime());
                textViews[3].setText(item.getName());
                textViews[4].setText(item.getStartStation());
                textViews[5].setText(item.getEndStation());

                textViews[9].setVisibility(View.VISIBLE);
                //TODO : REMIND ==> 실시간 정보를 아직 불러오지 않았음
              //  textViews[10].setVisibility(View.VISIBLE);


                if(item.getName().equals("수도권 1호선") || item.getName().equals("수도권 1호선(급행)"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_line1, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.line1));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.line1));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_line1_icon, null));
                }
                else if(item.getName().equals("수도권 2호선"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_line2, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.line2));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.line2));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_line2_icon, null));
                }
                else if(item.getName().equals("수도권 3호선"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_line3, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.line3));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.line3));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_line3_icon, null));
                }
                else if(item.getName().equals("수도권 4호선"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_line4, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.line4));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.line4));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_line4_icon, null));
                }
                else if(item.getName().equals("수도권 5호선"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_line5, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.line5));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.line5));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_line5_icon, null));
                }
                else if(item.getName().equals("수도권 6호선"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_line6, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.line6));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.line6));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_line6_icon, null));
                }
                else if(item.getName().equals("수도권 7호선"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_line7, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.line7));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.line7));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_line7_icon, null));
                }
                else if(item.getName().equals("수도권 8호선"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_line8, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.line8));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.line8));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_line8_icon, null));
                }
                else if(item.getName().equals("수도권 9호선") || item.getName().equals("수도권 9호선(급행)"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_line9, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.line9));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.line9));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_line9_icon, null));
                }
                else if(item.getName().equals("수도권 공항철도"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_lineairport, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.lineAirport));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.lineAirport));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_lineairport_icon, null));
                }
                else if(item.getName().equals("수도권 자기부상열차"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_linemaglev, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.lineMaglev));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.lineMaglev));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_linemaglev_icon, null));
                }
                else if(item.getName().equals("경의중앙선"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_linegyeongui, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.lineGyeongui));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.lineGyeongui));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_linegyeongui_icon, null));
                }
                else if(item.getName().equals("수도권 에버라인"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_lineyongin, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.lineYongin));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.lineYongin));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_lineyongin_icon, null));
                }
                else if(item.getName().equals("경춘선"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_linegyeongchun, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.lineGyeongchun));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.lineGyeongchun));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_linegyeongchun_icon, null));
                }
                else if(item.getName().equals("수도권 신분당선"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_lineshinbundang, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.lineShinBundang));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.lineShinBundang));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_lineshinbundang_icon, null));
                }
                else if(item.getName().equals("수도권 의정부경전철"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_lineuijeongbu, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.lineUijeongbu));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.lineUijeongbu));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_lineuijeongbu_icon, null));
                }
                else if(item.getName().equals("수도권 경강선"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_linegyeonggang, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.lineGyeonggang));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.lineGyeonggang));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_linegyeonggang_icon, null));
                }
                else if(item.getName().equals("수도권 우이신설선"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_lineui, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.lineUi));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.lineUi));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_lineui_icon, null));
                }
                else if(item.getName().equals("수도권 서해선"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_lineseohae, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.lineSeohae));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.lineSeohae));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_lineseohae_icon, null));
                }
                else if(item.getName().equals("수도권 김포골드라인"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_linegimpo, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.lineGimpo));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.lineGimpo));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_linegimpo, null));
                }
                else if(item.getName().equals("수도권 수인.분당선") || item.getName().equals("수도권 분당선(급행)"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_linebundang, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.lineBundang));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.lineBundang));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_linebundang_icon, null));
                }
                else if(item.getName().equals("인천 1호선"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_lineincheon1, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.lineIncheon1));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.lineIncheon1));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_lineincheon1, null));
                }
                else if(item.getName().equals("인천 2호선"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_lineincheon2, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.lineIncheon2));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.lineIncheon2));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_lineincheon2_icon, null));
                }
                //TODO : 아래부터는 다시!
                else if(item.getName().equals("대전 1호선"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_linedaejeon1, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.lineDaejeon1));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.lineDaejeon1));
                    //imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_linedaejeon1_icon, null));
                }
                else if(item.getName().equals("대구 1호선"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_linedaegu1, null));
                }
                else if(item.getName().equals("대구 2호선"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_linedaegu2, null));
                }
                else if(item.getName().equals("대구 3호선"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_linedaegu3, null));
                }
                else if(item.getName().equals("광주 1호선"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_linegwangju1, null));
                }
                else if(item.getName().equals("부산 1호선"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_linebusan1, null));
                }
                else if(item.getName().equals("부산 2호선"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_linebusan2, null));
                }
                else if(item.getName().equals("부산 3호선"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_linebusan3, null));
                }
                else if(item.getName().equals("부산 4호선"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_linebusan4, null));
                }
                else if(item.getName().equals("동해선"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_linedonghae, null));
                }
                else if(item.getName().equals("부산-김해경전철"))
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_linegimhae, null));
                }





            }
            else if(item.getTrafficCount().equals("버스"))
            {
                for(int i = 0; i<6; i++)
                {
                    textViews[i].setVisibility(View.VISIBLE);
                }

                for(int i = 6; i<9; i++)
                {
                    textViews[i].setVisibility(View.GONE);
                }
                imageIcon.setVisibility(View.VISIBLE);
                textViews[0].setVisibility(View.GONE);

                textViews[1].setText(item.getSectionTime());

                textViews[3].setText(item.getName());
                textViews[4].setText(item.getStartStation());
                textViews[5].setText(item.getEndStation());

                textViews[9].setVisibility(View.VISIBLE);
                //TODO : REMIND ==> 실시간 정보를 아직 불러오지 않았음
                //  textViews[10].setVisibility(View.VISIBLE);




                if(item.getTrafficType().equals("1")) //일반
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.bus_gbus, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.gBus));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.gBus));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.bus_gbus_icon, null));
                }
                else if(item.getTrafficType().equals("2")) //좌석
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.bus_seat, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.jwaseokBus));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.jwaseokBus));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.bus_seat_icon, null));
                }
                else if(item.getTrafficType().equals("3")) //마을
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.bus_maeul, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.greenBus));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.greenBus));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.bus_green_icon, null));
                }
                else if(item.getTrafficType().equals("4")) //직행(광역)
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.bus_jichaeng, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.redBus));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.redBus));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.bus_red_icon, null));
                }
                else if(item.getTrafficType().equals("5")) //공항
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.bus_maeul, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.greenBus));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.greenBus));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.bus_green_icon, null));
                }
                else if(item.getTrafficType().equals("6")) //간선급행(광역)
                {
                    //OK
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.bus_gwangyeok, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.redBus));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.redBus));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.bus_red_icon, null));
                }
                else if(item.getTrafficType().equals("10")) //외곽순환(경기/광역)
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.bus_gwangyeok, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.redBus));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.redBus));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.bus_red_icon, null));
                }
                else if(item.getTrafficType().equals("11")) //간선
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.bus_ganseon, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.blueBus));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.blueBus));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.bus_blue_icon, null));
                }
                else if(item.getTrafficType().equals("12")) //지선
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.bus_giseon, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.greenBus));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.greenBus));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.bus_green_icon, null));
                }
                else if(item.getTrafficType().equals("13")) //순환
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.bus_sunhwan, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.yellowBus));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.yellowBus));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.bus_yellow_icon, null));
                }
                else if(item.getTrafficType().equals("14")) //광역
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.bus_gwangyeok, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.redBus));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.redBus));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.bus_red_icon, null));
                }
                else if(item.getTrafficType().equals("15")) //급행
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.bus_geuphaeng, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.redBus));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.redBus));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.bus_red_icon, null));
                }
                else if(item.getTrafficType().equals("21") | item.getTrafficType().equals("22"))  // 제주도 시외형 / 경기도 시외형 /
                {
                    imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.bus_siwae, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.siwaeBus));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.siwaeBus));
                    imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.bus_siwae_icon, null));
                }
                else //농어촌 /급행간선
                {
                   imageIcon2.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.bus_nongcheon, null));
                    textViews[3].setTextColor(itemView.getResources().getColor(R.color.nongcheonBus));
                    colorView.setBackgroundColor(itemView.getResources().getColor(R.color.nongcheonBus));
                   imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.bus_nongcheon_icon, null));
                }



            }
            else //도보
            {
                for(int i = 0; i<6; i++)
                {
                    textViews[i].setVisibility(View.GONE);
                }

                for(int i = 6; i<9; i++)
                {
                    textViews[i].setVisibility(View.VISIBLE);
                }

                textViews[2].setVisibility(View.VISIBLE);
                //imageIcon.setVisibility(View.GONE);

                textViews[9].setVisibility(View.GONE);
                textViews[10].setVisibility(View.GONE);

                textViews[6].setText(item.getDistance());
                textViews[7].setText(item.getSectionTime());
                imageIcon2.setVisibility(View.GONE);

                //왜 이거 안됨
             //   imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.icon_disabled, null));

               // imageIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.train_lineincheon1, null));


            }







        }
    }
}
