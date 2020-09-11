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

import appliation.com.nearmarket.R;
import appliation.com.nearmarket.database.CartDatabase;
import appliation.com.nearmarket.interfaces.Constants;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.MyViewHOlder> {

    private Context context;
    private ArrayList<CartDatabase> orders;

    public OrderDetailsAdapter(Context context, ArrayList<CartDatabase> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public MyViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_details_item, parent, false);
        return new MyViewHOlder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHOlder holder, int position) {
        try {

            Picasso.with(context)
                    .load(orders.get(position).getProductImage())
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(holder.imageView);
        }
        catch (Exception e){
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.logo));
        }

        holder.tvNAme.setText(orders.get(position).getName());

        holder.tvPrice.setText(Constants.RUPEES + " " +orders.get(position).getItemTotalPrice());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class MyViewHOlder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView tvNAme;
        private TextView tvPrice;

        public MyViewHOlder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imgProduct);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvNAme = itemView.findViewById(R.id.tvOrderId);
        }
    }
}
