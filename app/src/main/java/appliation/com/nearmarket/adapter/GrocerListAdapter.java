package appliation.com.nearmarket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import appliation.com.nearmarket.R;
import appliation.com.nearmarket.interfaces.OnAdapterItemClick;

public class GrocerListAdapter extends RecyclerView.Adapter<GrocerListAdapter.MyViewHolder> {

    private ArrayList<String> list;
    private Context context;
    private OnAdapterItemClick onAdapterItemClick;

    public GrocerListAdapter(Context context, ArrayList<String> list, OnAdapterItemClick onAdapterItemClick){
        this.context = context;
        this.list = list;
        this.onAdapterItemClick = onAdapterItemClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_type,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        if (position == list.size() - 1){
            holder.view.setVisibility(View.GONE);
        }
        else {
            holder.view.setVisibility(View.VISIBLE);
        }

        String index = String.valueOf(position +1);
        holder.tvItem.setText(index + " " + list.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAdapterItemClick.onItemClick(position,list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItem;
        private View view;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tvListName);
            view = itemView.findViewById(R.id.view);
        }
    }
}
