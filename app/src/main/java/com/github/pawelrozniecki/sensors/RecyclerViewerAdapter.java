package com.github.pawelrozniecki.sensors;

import android.hardware.Sensor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewerAdapter  extends RecyclerView.Adapter<RecyclerViewerAdapter.ViewHolder> {

    private List<Sensor> sensorArrayList = new ArrayList<>();



    RecyclerViewerAdapter( List<Sensor> sensorArrayList){
        this.sensorArrayList = sensorArrayList;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_viewer_item,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.sensorInfo.setText(sensorArrayList.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return sensorArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView sensorInfo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.item_holder);
            sensorInfo = itemView.findViewById(R.id.sensor_info);

        }
    }
}
