package com.my.formerseller.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.my.formerseller.R;
import com.my.formerseller.act.UserDetails;
import com.my.formerseller.model.GetOrderNotification;
import com.my.formerseller.model.ProductDetailsUser;

import java.util.ArrayList;


public class ProductDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<ProductDetailsUser.Result.Product> modelList;
    private OnItemClickListener mItemClickListener;


    public ProductDetailsAdapter(Context context, ArrayList<ProductDetailsUser.Result.Product> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<ProductDetailsUser.Result.Product> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_product_details, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final ProductDetailsUser.Result.Product model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

            genericViewHolder.txtName.setText(model.getName());
            genericViewHolder.txtPrice.setText(model.getPrice()+" ");

            if(model.getImage() !=null)
            {
                Glide.with(mContext).load(model.getImage()).placeholder(R.drawable.img_home_one).into(genericViewHolder.ImgProduct);
            }

            //genericViewHolder.txtOrder.setText(model.getProductCount()+" "+"Orders");

        }
    }

    @Override
    public int getItemCount() {

        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private ProductDetailsUser.Result.Product getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position, ProductDetailsUser.Result.Product model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName;
        private TextView txtPrice;
        private TextView txtOrder;
        private ImageView ImgProduct;

        public ViewHolder(final View itemView) {
            super(itemView);

         this.txtName=itemView.findViewById(R.id.txtName);
         this.txtPrice=itemView.findViewById(R.id.txtPrice);
         this.txtOrder=itemView.findViewById(R.id.txtOrder);
         this.ImgProduct=itemView.findViewById(R.id.ImgProduct);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                }
            });
        }
    }


}

