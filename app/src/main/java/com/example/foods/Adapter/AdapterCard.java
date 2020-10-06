package com.example.foods.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.example.foods.Model.Item;
import com.example.foods.R;

import java.util.List;

public class AdapterCard extends RecyclerView.Adapter<AdapterCard.ViewgolderCard> {
    private Context context;
    private List<Item> mlist;
    private Onitemclicklistener listener;

    public AdapterCard(Context context, List<Item> mlist) {
        this.context = context;
        this.mlist = mlist;
    }

    @NonNull
    @Override
    public ViewgolderCard onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.cart_item_view,parent,false);

        return new ViewgolderCard(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewgolderCard holder, int position) {
        Item item=mlist.get(position);

        holder.tvname.setText(""+item.getItemName());
        holder.tvpri.setText(""+item.getItemPrice()+" Rs");
        holder.tvsta.setText(""+item.getItemDescription());

        Picasso.get()
                .load(item.getItemImage())
                .centerCrop()
                .fit()
                .into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }


    public class ViewgolderCard extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView iv;
        private TextView tvname,tvpri,tvsta;

        public ViewgolderCard(@NonNull View itemView) {
            super(itemView);

            iv=itemView.findViewById(R.id.imageview);

            tvname=itemView.findViewById(R.id.tvid);
            tvpri=itemView.findViewById(R.id.tvprice);

            tvsta=itemView.findViewById(R.id.tvstatus);


            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if(listener!=null)
            {
                int position=getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION)
                {
                    listener.clickitem(position);
                }

            }
        }
    }

    public interface Onitemclicklistener
    {
        void clickitem(int position);
    }

    public void setOnitemclicklistener(Onitemclicklistener mlistener)
    {
        listener=mlistener;
    }
}
