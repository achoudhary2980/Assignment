package com.wiproassignment.arun.wiproassignment;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wiproassignment.arun.wiproassignment.network.model.Row;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import application.wiproassignment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private List<Row> rowList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewTitle)
        TextView textViewTitle;

        @BindView(R.id.textViewSubTitle)
        TextView textViewSubTitle;

        @BindView(R.id.imageArrow)
        ImageView imageArrow;
        
        @BindView(R.id.imageTitle)
        ImageView imageTitle;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public MyAdapter(Context context, List<Row> rowList) {
        this.context = context;
        this.rowList = rowList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Row row = rowList.get(position);
        if (row != null) {


            // Displaying dot from HTML character code
            if(row.getTitle()!=null) {
                holder.textViewTitle.setText(row.getTitle());
                holder.textViewTitle.setVisibility(View.VISIBLE);

            }else {
                holder.textViewTitle.setVisibility(View.GONE);
            }
            if(row.getDescription()!=null) {
                holder.textViewSubTitle.setText(row.getDescription());
                holder.textViewSubTitle.setVisibility(View.VISIBLE);

            }
            else {
                holder.textViewSubTitle.setVisibility(View.GONE);
            }
            holder.imageArrow.setVisibility(View.VISIBLE);
            if (row.getImageHref() != null ) {
                System.out.println("Arun image url "+row.getImageHref());
                Picasso.with(context).load(row.getImageHref()).fit().centerCrop()
                        .fit().into(holder.imageTitle, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        holder.imageTitle.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {
                        holder.imageTitle.setVisibility(View.GONE);

                    }
                });
            } else {
                holder.imageTitle.setVisibility(View.GONE);

            }
        }
        else {
            holder.imageArrow.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return rowList.size();
    }




}