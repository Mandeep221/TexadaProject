package com.example.muhbandtekamshuru.demoapp.ui.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.muhbandtekamshuru.demoapp.R;
import com.example.muhbandtekamshuru.demoapp.data.network.model.Movie;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    List<Movie> dataList;

    public HomeAdapter(List<Movie> dataList){
        this.dataList = dataList;
        //Log.d("Friks", String.valueOf(dataList.size()));
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false));
        //Log.d("Friks", "Game");
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Log.d("Friks", "Game");
       holder.tv.setText(dataList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        Log.d("Friks", String.valueOf(dataList.size()));
        return dataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
