package com.my.formerseller.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.my.formerseller.Chat.MsgChatAct;
import com.my.formerseller.Preference;
import com.my.formerseller.R;
import com.my.formerseller.act.OrderDetailsUser;
import com.my.formerseller.model.GetOrderNotification;
import com.my.formerseller.model.HomeModel;
import com.my.formerseller.updated_orderSttusListener;

import java.util.ArrayList;

public class SubProductRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<GetOrderNotification.Result> modelList;
    private OnItemClickListener mItemClickListener;
    private updated_orderSttusListener updated_orderSttus;


    public SubProductRecyclerViewAdapter(Context context, ArrayList<GetOrderNotification.Result> modelList,updated_orderSttusListener updated_orderSttus) {
        this.mContext = context;
        this.modelList = modelList;
        this.updated_orderSttus = updated_orderSttus;
    }

    public void updateList(ArrayList<GetOrderNotification.Result> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sub_product, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final GetOrderNotification.Result model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

            genericViewHolder.txtUserName.setText(model.getUserData().getName());
            genericViewHolder.txtOrder.setText(model.getProductCount()+" "+"Orders");

            if(model.getUserData().getImage() !=null)
            {
                Glide.with(mContext).load(model.getUserData().getImage()).placeholder(R.drawable.john).into(genericViewHolder.imgUser);
            }

            if(model.getStatus().equalsIgnoreCase("Cancel"))
            {
                genericViewHolder.RR_chat.setVisibility(View.GONE);
                genericViewHolder.txtDetails.setVisibility(View.GONE);
                genericViewHolder.llAccept.setVisibility(View.GONE);
                genericViewHolder.llTransit.setVisibility(View.GONE);

            } if(model.getStatus().equalsIgnoreCase("Transit"))
            {
                genericViewHolder.RR_chat.setVisibility(View.VISIBLE);
                genericViewHolder.llAccept.setVisibility(View.GONE);
                genericViewHolder.llTransit.setVisibility(View.GONE);

            }if(model.getStatus().equalsIgnoreCase("Accepted"))
            {
               genericViewHolder.RR_chat.setVisibility(View.GONE);
                genericViewHolder.llAccept.setVisibility(View.GONE);
                genericViewHolder.llTransit.setVisibility(View.VISIBLE);

            }else if(model.getStatus().equalsIgnoreCase("Pending"))
            {
                genericViewHolder.RR_chat.setVisibility(View.GONE);
             //   genericViewHolder.txtDetails.setVisibility(View.VISIBLE);
                genericViewHolder.llAccept.setVisibility(View.VISIBLE);
                genericViewHolder.llTransit.setVisibility(View.GONE);
            }

            genericViewHolder.txtDetails.setOnClickListener(v -> {

                Preference.save(mContext, Preference.KEYType_Order_details_id,model.getId());

                mContext.startActivity(new Intent(mContext, OrderDetailsUser.class));

            });


           genericViewHolder.llTransit.setOnClickListener(v -> {

               updated_orderSttus.addItem(model.getId(),"Transit");

            });

            genericViewHolder.txtAccepted.setOnClickListener(v -> {

                updated_orderSttus.addItem(model.getId(),"Accepted");

            });

            genericViewHolder.txtCancel.setOnClickListener(v -> {

                updated_orderSttus.addItem(model.getId(),"Cancel");

            });


            genericViewHolder.RR_chat.setOnClickListener(v -> {

                mContext.startActivity(new Intent(mContext, MsgChatAct.class)
                        .putExtra("UserId",model.getUserData().getId())
                        .putExtra("UserName",model.getUserData().getName())
                        .putExtra("UserImage",model.getUserData().getImage())
                        .putExtra("request_id",model.getId()));
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

    private GetOrderNotification.Result getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position, GetOrderNotification.Result model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtUserName;
        private TextView txtOrder;
        private TextView txtDetails;
        private TextView txtAccepted;
        private TextView txtCancel;
        private ImageView imgUser;
        private LinearLayout llTransit;
        private LinearLayout llAccept;
        private RelativeLayout RR_chat;

        public ViewHolder(final View itemView) {
            super(itemView);

         this.txtUserName=itemView.findViewById(R.id.txtUserName);
         this.txtOrder=itemView.findViewById(R.id.txtOrder);
         this.txtDetails=itemView.findViewById(R.id.txtDetails);
         this.imgUser=itemView.findViewById(R.id.imgUser);
         this.txtAccepted=itemView.findViewById(R.id.txtAccepted);
         this.txtCancel=itemView.findViewById(R.id.txtCancel);
         this.llTransit=itemView.findViewById(R.id.llTransit);
         this.llAccept=itemView.findViewById(R.id.llAccept);
         this.RR_chat=itemView.findViewById(R.id.RR_chat);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                }
            });
        }
    }


}

