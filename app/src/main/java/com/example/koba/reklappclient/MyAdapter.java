package com.example.koba.reklappclient;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Koba on 17/07/2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private String mNavTitles[];

    private String name;
    private int profile;
    private String number;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private int Holderid;
        private TextView textView;
        private ImageView imageView;
        private ImageView profile;
        private TextView Name;
        private TextView Number;

        public ViewHolder(View itemView, int ViewType) {
            super(itemView);
            if(ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText);
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);
                Holderid = 1;
            }
            else{
                Name = (TextView) itemView.findViewById(R.id.name);
                Number = (TextView) itemView.findViewById(R.id.number);
                profile = (ImageView) itemView.findViewById(R.id.circleView);
                Holderid = 0;
            }
        }
    }

    public MyAdapter(String Titles[], String Name, String Email, int Profile){
        mNavTitles = Titles;
        name = Name;
        number = Email;
        profile = Profile;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent,false);
            ViewHolder vhItem = new ViewHolder(v,viewType);
            return vhItem;
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header,parent,false);
            ViewHolder vhHeader = new ViewHolder(v,viewType);
            return vhHeader;


        }
        return null;

    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        if(holder.Holderid ==1) {
            holder.textView.setText(mNavTitles[position - 1]);
        }
        else{

            holder.profile.setImageResource(profile);
            holder.Name.setText(name);
            holder.Number.setText(number);
        }
    }


    @Override
    public int getItemCount() {
        return mNavTitles.length+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }
}
