package app.jw.mapable.gm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MapRecyclerAdapter2 extends RecyclerView.Adapter<MapRecyclerAdapter2.ItemViewHolder> {


    private ArrayList<Item2> listItem = new ArrayList<>();

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

    class ItemViewHolder extends RecyclerView.ViewHolder
    {
        TextView[] textViews = new TextView[6];

        ItemViewHolder(View itemView)
        {
            super(itemView);
            textViews[0] = itemView.findViewById(R.id.textDistance);
            textViews[1] = itemView.findViewById(R.id.textSectionTime);
            textViews[2] = itemView.findViewById(R.id.textTrafficCount);
            textViews[3] = itemView.findViewById(R.id.textName);
            textViews[4] = itemView.findViewById(R.id.textStartStation);
            textViews[5] = itemView.findViewById(R.id.textEndStation);

        }

        void onBind(Item2 item)
        {
            textViews[0].setText(item.getDistance());
            textViews[1].setText(item.getSectionTime());
            textViews[2].setText(item.getTrafficCount());
            textViews[3].setText(item.getName());
            textViews[4].setText(item.getStartStation());
            textViews[5].setText(item.getEndStation());

        }
    }
}
