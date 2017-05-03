package com.androidtest.xkf.androidtest;


import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<String> data = new ArrayList<>();
    private List<String> number = new ArrayList<>();
    private List<String> name = new ArrayList<>();
    private List<String> adress = new ArrayList<>();
    private List<Integer> image = new ArrayList<>();

    public MyAdapter() {
        super();
        data.add("2017.04.24");
        data.add("2017.03.14");
        data.add("2017.03.14");
        data.add("2017.03.13");
        data.add("2017.03.18");
        number.add("7020");
        number.add("6980");
        number.add("7040");
        number.add("7080");
        number.add("7080");
        name.add("临潼青石榴");
        adress.add("西安临潼");
        image.add(R.drawable.shiliu);
        name.add("优质苹果");
        adress.add("洛川");
        image.add(R.drawable.pingguo);
        name.add("猕猴桃");
        adress.add("西安周至");
        image.add(R.drawable.mihoutao);
        name.add("青枣");
        adress.add("榆林");
        image.add(R.drawable.qinzao);
        name.add("油桃");
        adress.add("宝鸡");
        image.add(R.drawable.youtao);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new MyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.numberView.setText(number.get(position % number.size()));
        holder.dataView.setText(data.get(position % data.size()));
        holder.name.setText(name.get(position % name.size()));
        holder.adress.setText(adress.get(position % adress.size()));
        holder.pic.setImageResource(image.get(position % image.size()));
    }

    @Override
    public int getItemCount() {
        return 100;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dataView;
        public TextView numberView;
        public TextView name;
        public TextView adress;
        public ImageView pic;

        public ViewHolder(View itemView) {
            super(itemView);
            dataView = (TextView) itemView.findViewById(R.id.data);
            numberView = (TextView) itemView.findViewById(R.id.numbers);
            name = (TextView) itemView.findViewById(R.id.textView);
            adress = (TextView) itemView.findViewById(R.id.textView6);
            pic = (ImageView) itemView.findViewById(R.id.imageView2);
        }
    }
}
