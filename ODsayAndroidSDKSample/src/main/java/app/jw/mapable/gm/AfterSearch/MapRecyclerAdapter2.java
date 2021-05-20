package app.jw.mapable.gm.AfterSearch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
        SharedPreferences preferences;

        ItemViewHolder(View itemView)
        {
            super(itemView);






            textViews[0] = itemView.findViewById(R.id.textDistance);
            textViews[1] = itemView.findViewById(R.id.textSectionTime);
            textViews[2] = itemView.findViewById(R.id.textTrafficCount);
            textViews[3] = itemView.findViewById(R.id.textName);
            textViews[4] = itemView.findViewById(R.id.textStartStation);
            textViews[5] = itemView.findViewById(R.id.textEndStation);

            imageIcon = itemView.findViewById(R.id.imageViewTransportationType);
            textViews[6] = itemView.findViewById(R.id.textDistance2);
            textViews[7] = itemView.findViewById(R.id.textSectionTime2);
            textViews[8] = itemView.findViewById(R.id.textSection2);

            textViews[9] = itemView.findViewById(R.id.textSection);
            textViews[10] = itemView.findViewById(R.id.textArrive);



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
                textViews[10].setVisibility(View.VISIBLE);

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
                textViews[10].setVisibility(View.VISIBLE);

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
                imageIcon.setVisibility(View.GONE);

                textViews[9].setVisibility(View.GONE);
                textViews[10].setVisibility(View.GONE);

                textViews[6].setText(item.getDistance());
                textViews[7].setText(item.getSectionTime());

            }







        }
    }
}
