package appliation.com.nearmarket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import appliation.com.nearmarket.R;
import appliation.com.nearmarket.interfaces.OnAdapterItemClick;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.MyViewHolder>{

    private ArrayList<String> banners;
    private OnAdapterItemClick onAdapterItemClick;
    private Context context;
    public BannerAdapter(Context context,ArrayList<String> banners,OnAdapterItemClick onAdapterItemClick){

        this.context = context ;
        this.banners = banners;
        this.onAdapterItemClick = onAdapterItemClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_adapter,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Picasso.with(context)
                .load(banners.get(position))
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(holder.imgBanner);
    }

    @Override
    public int getItemCount() {
        return banners.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgBanner;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgBanner = itemView.findViewById(R.id.imgBanner);
        }
    }
}
