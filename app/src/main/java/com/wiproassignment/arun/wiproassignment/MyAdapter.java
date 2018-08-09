package com.wiproassignment.arun.wiproassignment;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.wiproassignment.arun.wiproassignment.network.model.Row;

import java.util.List;

import application.wiproassignment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private List<Row> rowList;

    public MyAdapter(Context context, List<Row> rowList) {
        this.context = context;
        this.rowList = rowList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row1, parent, false);

        return new MyViewHolder(itemView);
    }
    Picasso picasso =Picasso.with(wiproassignment.getInstance());
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Row row = rowList.get(position);
        if (row != null) {
            // Displaying dot from HTML character code
            if (row.getTitle() != null) {
                holder.textViewTitle.setText(row.getTitle());
                holder.textViewTitle.setVisibility(View.VISIBLE);

            } else {
                holder.textViewTitle.setVisibility(View.GONE);
            }
            if (row.getDescription() != null) {
                holder.textViewSubTitle.setText(row.getDescription());
                holder.textViewSubTitle.setVisibility(View.VISIBLE);

            } else {
                holder.textViewSubTitle.setVisibility(View.GONE);
            }
            if (row.getImageHref() != null) {
                holder.imageTitle.setVisibility(View.VISIBLE);
                System.out.println("Arun image url " + row.getImageHref().toString().replace("http", "https"));
                picasso.load(row.getImageHref().toString().replace("http", "https")).placeholder(R.drawable.ic_launcher_background)
                        .into(holder.imageTitle, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {

                                Picasso.with(context.getApplicationContext()).load(row.getImageHref().toString()).placeholder(R.drawable.ic_launcher_background)
                                        .into(holder.imageTitle);
                            }
                        });

            } else {
                holder.imageTitle.setVisibility(View.GONE);

            }
        }
    }

    @Override
    public int getItemCount() {
        return rowList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewTitle)
        TextView textViewTitle;

        @BindView(R.id.textViewSubTitle)
        TextView textViewSubTitle;


        @BindView(R.id.imageTitle)
        ImageView imageTitle;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}
