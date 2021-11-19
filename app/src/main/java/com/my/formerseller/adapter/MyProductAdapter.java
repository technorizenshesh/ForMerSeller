package com.my.formerseller.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.my.formerseller.Preference;
import com.my.formerseller.R;
import com.my.formerseller.act.MyProduct;
import com.my.formerseller.act.UpdateProductActivity;
import com.my.formerseller.model.HomeModel;
import com.my.formerseller.model.MyProductDataModel;

import java.util.ArrayList;


public class MyProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<MyProductDataModel> modelList;
    private OnItemClickListener mItemClickListener;


    public MyProductAdapter(Context context, ArrayList<MyProductDataModel> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<MyProductDataModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_my_produuct, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final MyProductDataModel model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

            genericViewHolder.txtProductName.setText(model.getName());

            if(model.getImage() !=null)
            {
                Glide.with(mContext).load(model.getImage()).circleCrop().into(genericViewHolder.img1);
            }

            genericViewHolder.RRUpdateProduct.setOnClickListener(v -> {

                Preference.save(mContext, Preference.KEY_product_details_id,model.getId());

                mContext.startActivity(new Intent(mContext, UpdateProductActivity.class));

            });
        }

    }


    @Override
    public int getItemCount() {

        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private MyProductDataModel getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position, MyProductDataModel model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtProductName;
        private ImageView img1;
        private RelativeLayout RRUpdateProduct;


        public ViewHolder(final View itemView) {
            super(itemView);

         this.txtProductName=itemView.findViewById(R.id.txtProductName);
         this.img1=itemView.findViewById(R.id.img1);
         this.RRUpdateProduct=itemView.findViewById(R.id.RRUpdateProduct);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                }
            });
        }
    }


}

