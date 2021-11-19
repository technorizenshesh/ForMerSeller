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
import com.my.formerseller.model.ConverSationList;


import java.util.ArrayList;


public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<ConverSationList.Result> modelList;
    private OnItemClickListener mItemClickListener;


    public ChatAdapter(Context context, ArrayList<ConverSationList.Result> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<ConverSationList.Result> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final ConverSationList.Result model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;
           // genericViewHolder.txtName.setText(model.getName());

            genericViewHolder.txtUserName.setText(""+model.getName());
            genericViewHolder.txtlastMsg.setText(""+model.getLastMessage());

            if(model.getImage()!=null)
            {
                Glide.with(mContext).load(""+model.getImage()).placeholder(R.drawable.john).into(genericViewHolder.imgSeller);
            }
        }
    }


    @Override
    public int getItemCount() {

        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private ConverSationList.Result getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position, ConverSationList.Result model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgSeller;
        private TextView txtUserName;
        private TextView txtlastMsg;


        public ViewHolder(final View itemView) {
            super(itemView);

         this.txtUserName=itemView.findViewById(R.id.txtUserName);
         this.imgSeller=itemView.findViewById(R.id.imgSeller);
         this.txtlastMsg=itemView.findViewById(R.id.txtlastMsg);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                }
            });
        }
    }


}

