package app.jw.mapable.gm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appyvet.materialrangebar.RangeBar;
import com.github.mikephil.charting.charts.BarChart;

import java.util.ArrayList;

public class MapRecyclerAdapter extends RecyclerView.Adapter<MapRecyclerAdapter.ItemViewHolder> {


    RecyclerView thisRecyclerView;
    ConstraintLayout constraintLayout;
    int[] timeValue;
    private ArrayList<Item> listItem = new ArrayList<>();

    BarChart barChart;
    RangeBar rangeBar;
    public MapRecyclerAdapter2 adapter2;
    SharedPreferences preferences;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler1, parent, false);
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

    void addItem(Item item)
    {
        listItem.add(item);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        TextView[] textViews = new TextView[3];

        ItemViewHolder(View itemView) {
            super(itemView);



            textViews[0] = itemView.findViewById(R.id.textTrafficDistance);
            textViews[1] = itemView.findViewById(R.id.textTotalTime);
            textViews[2] = itemView.findViewById(R.id.textPayment);
            RecyclerView recyclerView = itemView.findViewById(R.id.recyclerView2);
            recyclerView.addItemDecoration(new DividerItemDecoration(itemView.getContext(), DividerItemDecoration.VERTICAL));
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));

            adapter2 = new MapRecyclerAdapter2();
            recyclerView.setAdapter(adapter2);

            barChart = itemView.findViewById(R.id.barChart);

            constraintLayout = itemView.findViewById(R.id.constraintLayout01);




        }

        void onBind(Item item)
        {



            textViews[0].setText(item.getTrafficDistance());
            textViews[1].setText(item.getTotalTime());
            textViews[2].setText(item.getPayment());

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



                String[][] waysNewSplit = new String[waysSplit.length][14];


            String finalGetWays = getWays;
            constraintLayout.setOnClickListener(view -> {
                Intent intent = new Intent(itemView.getContext(), AfterRoadActivity.class);
                intent.putExtra("ways", finalGetWays);
                itemView.getContext().startActivity(intent);
            });



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


}
