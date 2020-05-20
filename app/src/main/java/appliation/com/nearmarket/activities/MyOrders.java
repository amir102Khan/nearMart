package appliation.com.nearmarket.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import appliation.com.nearmarket.Model.PlaceOrderModel;
import appliation.com.nearmarket.Model.ProductModel;
import appliation.com.nearmarket.R;
import appliation.com.nearmarket.Util.Common;
import appliation.com.nearmarket.adapter.MyOrderAdapter;
import appliation.com.nearmarket.core.BaseActivity;
import appliation.com.nearmarket.databinding.ActivityMyOrdersBinding;
import appliation.com.nearmarket.interfaces.OnAdapterItemClick;

public class MyOrders extends BaseActivity implements OnAdapterItemClick {

    private ActivityMyOrdersBinding binding;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private MyOrderAdapter adapter;
    private ArrayList<PlaceOrderModel> orders = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_my_orders);
        Common.setToolbarWithBackAndTitle(mContext,"My Order",true,R.drawable.ic_chevron_left_black_24dp);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(ORDER_PLACED).child(sp.getString(USER_ID));

        setEMptyADapter();
        if (checkInternetConnection()){
            getMyOrders();
        }
        else {
            showToast(getString(R.string.internet_not_there));
        }
    }

    private void setEMptyADapter(){
        binding.rv.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new MyOrderAdapter(mContext,orders,this);
        binding.rv.setAdapter(adapter);
    }

    private void getMyOrders(){
        showLoader();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PlaceOrderModel model= snapshot.getValue(PlaceOrderModel.class);
                    orders.add(model);
                }
                Collections.reverse(orders);
                adapter.notifyDataSetChanged();
                hideLoader();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideLoader();
            }
        });
    }

    private void showLoader(){
        binding.loader.setVisibility(View.VISIBLE);
    }

    private void hideLoader(){
        binding.loader.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(int position, String data) {
        startActivity(new Intent(mContext,OrderDetails.class)
        .putExtra("orders",orders.get(position)));
    }
}
