package appliation.com.nearmarket.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import java.util.ArrayList;

import appliation.com.nearmarket.Model.PlaceOrderModel;
import appliation.com.nearmarket.R;
import appliation.com.nearmarket.Util.Common;
import appliation.com.nearmarket.adapter.OrderDetailsAdapter;
import appliation.com.nearmarket.core.BaseActivity;
import appliation.com.nearmarket.database.CartDatabase;
import appliation.com.nearmarket.databinding.ActivityOrderDetailsBinding;

public class OrderDetails extends BaseActivity {

    private ActivityOrderDetailsBinding binding;
    private PlaceOrderModel placeOrderModel;
    private OrderDetailsAdapter adapter;
    private ArrayList<CartDatabase> items = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_order_details);
        Common.setToolbarWithBackAndTitle(mContext,"Order Details",true,R.drawable.ic_chevron_left_black_24dp);

        setEmptyAdapter();
        getData();
    }

    private void setEmptyAdapter(){
        binding.rv.setLayoutManager(new LinearLayoutManager(mContext));

        adapter = new OrderDetailsAdapter(mContext,items);
        binding.rv.setAdapter(adapter);
    }

    private void getData(){
        placeOrderModel = (PlaceOrderModel) getIntent().getSerializableExtra("orders");

        if (placeOrderModel != null){
            items.addAll(placeOrderModel.getOrders());
            adapter.notifyDataSetChanged();

            binding.tvName.setText(placeOrderModel.getUserName());

            binding.tvAdd.setText(placeOrderModel.getUserAddress());

            binding.tvPhoneNumber.setText(placeOrderModel.getUserPhone());

            binding.tvAmount.setText(placeOrderModel.getTotalBill());

            binding.tvDeliveryCharge.setText(placeOrderModel.getDeliveryCharge());

            binding.tvTotalAmount.setText(placeOrderModel.getAmountToPay());

            binding.tvCashOnDelivery.setText(placeOrderModel.getPaymentType() + " " + placeOrderModel.getAmountToPay());

            binding.tvOrderID.setText("Order ID:  " + placeOrderModel.getOrderId());
        }

    }

}
