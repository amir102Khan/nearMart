package appliation.com.nearmarket.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import appliation.com.nearmarket.Model.PlaceOrderModel;
import appliation.com.nearmarket.R;
import appliation.com.nearmarket.interfaces.Constants;
import appliation.com.nearmarket.interfaces.OnAdapterItemClick;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<PlaceOrderModel> orders;
    private OnAdapterItemClick adapterItemClick;

    public MyOrderAdapter(Context context, ArrayList<PlaceOrderModel> orders, OnAdapterItemClick adapterItemClick) {
        this.context = context;
        this.orders = orders;
        this.adapterItemClick = adapterItemClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_items, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        try {

            Picasso.with(context)
                    .load(orders.get(position).getOrders().get(0).getProductImage())
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(holder.imageView);

        }
        catch (Exception e){
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.logo));
        }

        holder.tvOrderId.setText(orders.get(position).getOrderId());

        holder.tvNumberOfItems.setText("" + orders.get(position).getOrders().size() + " ITEMS");

        holder.tvTotalAmount.setText(" " + orders.get(position).getAmountToPay());

        holder.tvOrderOn.setText(orders.get(position).getOrderTime());

        if (orders.get(position).getOrderStatus() == Constants.PENDING){
            holder.tvOrderStatus.setText("Pending");
        }
        else if (orders.get(position).getOrderStatus() == Constants.ACCEPTED){
            holder.tvOrderStatus.setText("Accepted");
        }
        else if (orders.get(position).getOrderStatus() == Constants.REJECTED){
            holder.tvOrderStatus.setText("Rejected");
        }
        else if (orders.get(position).getOrderStatus() == Constants.ON_THE_WAY){
            holder.tvOrderStatus.setText("Order is coming soon");
        }
        else if (orders.get(position).getOrderStatus() == Constants.DELIVERED){
            holder.tvOrderStatus.setText("Delivered");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterItemClick.onItemClick(position,"");
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView tvOrderId;
        private TextView tvNumberOfItems;
        private TextView tvOrderOn;
        private TextView tvTotalAmount;
        private TextView tvOrderStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgProduct);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvNumberOfItems = itemView.findViewById(R.id.tvNoItems);
            tvOrderOn = itemView.findViewById(R.id.tvOrderOn);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStaus);
        }
    }
}
