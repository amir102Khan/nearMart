package appliation.com.nearmarket.adapter;

import android.content.Context;
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

import appliation.com.nearmarket.Model.ProductModel;
import appliation.com.nearmarket.R;
import appliation.com.nearmarket.database.CartDatabase;
import appliation.com.nearmarket.interfaces.Constants;
import appliation.com.nearmarket.interfaces.OnAdapterItemClickWithType;


public class BucketListAdapter extends RecyclerView.Adapter<BucketListAdapter.MyViewHolder> {


    private ArrayList<CartDatabase> products;
    private Context context;
    private OnAdapterItemClickWithType itemClickWithType;
    private String countValue;
    private int count;

    public BucketListAdapter(Context context, ArrayList<CartDatabase> products, OnAdapterItemClickWithType onAdapterItemClickWithType) {
        this.context = context;
        this.products = products;
        this.itemClickWithType = onAdapterItemClickWithType;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_items_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull MyViewHolder holder, final int position) {
        holder.tvProdcutName.setText(products.get(position).getName());
        holder.tvProductPrice.setText(Constants.RUPEES + " " + products.get(position).getPrice() + " /" + products.get(position).getUnit());
        holder.tvProdcutUnit.setText(products.get(position).getQty() + " " + products.get(position).getUnit());
        holder.tvProductMinQty.setText("+" + products.get(position).getQty() + " " + products.get(position).getUnit());
        holder.tvCount.setText(countValue);

        holder.tvCount.setText("" + products.get(position).getCount());

        holder.tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickWithType.onItemClick(position, Constants.ADD, String.valueOf(count));
            }
        });


        holder.tvSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickWithType.onItemClick(position, Constants.SUB, String.valueOf(count));
            }
        });


        holder.tvTotalPrice.setText("Total Price : " + Constants.RUPEES + " " + products.get(position).getItemTotalPrice());

        try {
            Picasso.with(context)
                    .load(products.get(position).getProductImage())
                    .error(R.drawable.logo)
                    .placeholder(R.drawable.logo)
                    .into(holder.imageProdcut);
        }
        catch (Exception e){
            holder.imageProdcut.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.logo));
        }


    }


    @Override
    public int getItemCount() {
        return products.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageProdcut;
        private TextView tvProdcutName;
        private TextView tvProductPrice;
        private TextView tvProductMinQty;
        private TextView tvProdcutUnit;
        private TextView tvCount;
        private TextView tvSub;
        private TextView tvAdd;
        private TextView tvTotalPrice;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProdcut = itemView.findViewById(R.id.productImage);
            tvProdcutName = itemView.findViewById(R.id.productName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductMinQty = itemView.findViewById(R.id.tvQty);
            tvProdcutUnit = itemView.findViewById(R.id.tvMin);
            tvCount = itemView.findViewById(R.id.tvCount);
            tvSub = itemView.findViewById(R.id.tvSub);
            tvAdd = itemView.findViewById(R.id.tvAdd);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
        }
    }

    public void getCount(String countValue) {
        this.countValue = countValue;
    }
}
