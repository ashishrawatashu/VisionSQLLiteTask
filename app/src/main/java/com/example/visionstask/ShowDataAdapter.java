package com.example.visionstask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowDataAdapter extends RecyclerView.Adapter<ShowDataAdapter.MyViewHolder> implements Filterable {

    Context context;
    ArrayList<UserDataPojo> getUserDataPojo;
    ArrayList<UserDataPojo> filterGetUserDataPojo;
    ArrayList<UserDataPojo> selectItems = new ArrayList<>();

    ItemLongClick itemLongClick;
    boolean isSelected = false;

    public ShowDataAdapter(Context context, ArrayList<UserDataPojo> getUserDataPojo,ItemLongClick itemLongClick) {
        this.context = context;
        this.getUserDataPojo = getUserDataPojo;
        this.itemLongClick = itemLongClick;
        filterGetUserDataPojo = new ArrayList<>(getUserDataPojo);

    }

    @NonNull
    @Override
    public ShowDataAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
        ShowDataAdapter.MyViewHolder vh = new ShowDataAdapter.MyViewHolder(v);
        return vh;

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ShowDataAdapter.MyViewHolder holder, int position) {

        holder.user_name_TV.setText(getUserDataPojo.get(position).getName());
        holder.user_gender_TV.setText(getUserDataPojo.get(position).getGender());

        Glide.with(context)
                .load(getUserDataPojo.get(position).getImagePath())
                .placeholder(R.drawable.user)
                .into(holder.user_image_CIV);

    }

    @Override
    public int getItemCount() {
        return getUserDataPojo.size();
    }

    @Override
    public Filter getFilter() {
        return filterList;
    }
    private Filter filterList = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<UserDataPojo> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length()==0){
                filteredList.addAll(filterGetUserDataPojo);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (UserDataPojo item : filterGetUserDataPojo){
                    if (item.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            getUserDataPojo.clear();
            getUserDataPojo.addAll((List<UserDataPojo>)results.values);
            notifyDataSetChanged();

        }
    };
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView user_gender_TV, user_name_TV;
        CircleImageView user_image_CIV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            user_name_TV = itemView.findViewById(R.id.user_name_TV);
            user_gender_TV=itemView.findViewById(R.id.user_gender_TV);
            user_image_CIV = itemView.findViewById(R.id.user_image_CIV);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {


                    isSelected = true;
                    if (selectItems.contains(getUserDataPojo.get(getAdapterPosition()))){
                        itemView.setBackgroundColor(Color.TRANSPARENT);
                        selectItems.remove(getUserDataPojo.get(getAdapterPosition()));

                    }else {
                        itemView.setBackgroundResource(R.color.lightGrey);
                        selectItems.add(getUserDataPojo.get(getAdapterPosition()));

                    }

                    if (itemLongClick!=null){
                        itemLongClick.itemLongClick(selectItems);
                    }

                    if (selectItems.size() == 0){
                        isSelected = false;
                    }

                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isSelected){
                        if (selectItems.contains(getUserDataPojo.get(getAdapterPosition()))){
                            itemView.setBackgroundColor(Color.TRANSPARENT);
                            selectItems.remove(getUserDataPojo.get(getAdapterPosition()));
                        }else {
                            itemView.setBackgroundResource(R.color.lightGrey);
                            selectItems.add(getUserDataPojo.get(getAdapterPosition()));

                        }

                        if (itemLongClick!=null){
                            itemLongClick.itemLongClick(selectItems);
                        }

                        if (selectItems.size() == 0)
                            isSelected = false;
                    }
                }
            });
        }
    }
}