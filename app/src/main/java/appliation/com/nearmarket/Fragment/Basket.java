package appliation.com.nearmarket.Fragment;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import appliation.com.nearmarket.Model.PlaceOrderModel;
import appliation.com.nearmarket.Model.SignUpUserModel;
import appliation.com.nearmarket.R;
import appliation.com.nearmarket.Util.Common;
import appliation.com.nearmarket.activities.Dashboard;
import appliation.com.nearmarket.activities.MyOrders;
import appliation.com.nearmarket.adapter.BucketListAdapter;
import appliation.com.nearmarket.core.BaseFragment;
import appliation.com.nearmarket.database.CartData;
import appliation.com.nearmarket.database.CartDatabase;
import appliation.com.nearmarket.database.DatabaseClient;
import appliation.com.nearmarket.databinding.FragmentBasketBinding;
import appliation.com.nearmarket.interfaces.OnAdapterItemClickWithType;


public class Basket extends BaseFragment implements OnAdapterItemClickWithType, View.OnClickListener {

    public Basket() {
        // Required empty public constructor
    }

    private FragmentBasketBinding binding;
    private BucketListAdapter adapter;
    private ArrayList<CartDatabase> prodcuts = new ArrayList<>();
    private int POSITION_TO_WORK;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private DatabaseReference orderDatabase;
    private int areaSelectedPosition;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_basket,null,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setEmptyAdapter();
        getCart();
        setListener();
        setVisibilities();
        selectSPinner();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(USER).child(sp.getString(USER_ID));
        orderDatabase = database.getReference(ORDER_PLACED).child(sp.getString(USER_ID));

        if (checkInternetConnection()){
            getUserData();
        }
        else {
            showToast(getString(R.string.internet_not_there));
        }

        getTimeSlot();
    }

    private void setListener(){
        binding.btnDwnld.setOnClickListener(this);
    }

    private void setVisibilities(){
        ((Animatable) binding.check.getDrawable()).stop();
        binding.check.setVisibility(View.GONE);
        binding.btnDwnld.setVisibility(View.VISIBLE);
        binding.layoutProgress.setVisibility(View.GONE);
    }

    private void setAddLAyoutVisibilties(int view, int noData){
        binding.layoutAdd.setVisibility(view);
        binding.noData.setVisibility(noData);
    }
    private void getCart(){
        CartData cartData = new CartData(getActivity().getApplicationContext());
        List<CartDatabase> cart = cartData.getCart();
        if (cart.size() == 0){
            setAddLAyoutVisibilties(View.GONE,View.VISIBLE);
        }
        else {
            setAddLAyoutVisibilties(View.VISIBLE,View.GONE);
        }
        prodcuts.addAll(cart);
        adapter.notifyDataSetChanged();
        setBill();
    }

    private void setEmptyAdapter(){
        binding.rv.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new BucketListAdapter(mContext, prodcuts,this);
        binding.rv.setAdapter(adapter);
    }


    @Override
    public void onItemClick(int position, int type, String data) {
        POSITION_TO_WORK = position;
        if (type == ADD){
            int count = prodcuts.get(position).getCount();
            count = count + 1;
            prodcuts.get(position).setCount(count);
            adapter.notifyDataSetChanged();
            //addProductToBucket();
            updateCart();
        }
        else if (type == SUB){
            int count = prodcuts.get(position).getCount();
            if (count > 0){
                count = count - 1;
                if (count == 0){
                    removeDataFromLocal();
                }
                else {
                    prodcuts.get(position).setCount(count);
                    updateCart();
                    adapter.notifyDataSetChanged();
                }

                // addProductToBucket();
            }
        }
    }

    private void updateCart(){
        final int count = prodcuts.get(POSITION_TO_WORK).getCount();
        final int quantity = count * Integer.valueOf(prodcuts.get(POSITION_TO_WORK).getQty());
        final String quantityOrder = String.valueOf(quantity) + prodcuts.get(POSITION_TO_WORK).getUnit();
        final int itemTotalPrice = count * Integer.valueOf(prodcuts.get(POSITION_TO_WORK).getPrice());
        prodcuts.get(POSITION_TO_WORK).setItemTotalPrice(String.valueOf(itemTotalPrice));
        adapter.notifyDataSetChanged();
        addProductInCartToLocal(quantityOrder,count,itemTotalPrice);
    }

    private void removeDataFromLocal(){
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(mContext).getAppDatabase()
                        .cartDao()
                        .delete(prodcuts.get(POSITION_TO_WORK));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                prodcuts.remove(POSITION_TO_WORK);
                adapter.notifyDataSetChanged();
                if (prodcuts.size() == 0){
                    setAddLAyoutVisibilties(View.GONE,View.VISIBLE);
                }
            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();

    }


    private void addProductInCartToLocal(final String quantityOrder, final int count, final int itemTotalPrice){
      //  showLoader();
        class SaveData extends AsyncTask<Void,Void,Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                // creating database

                CartDatabase cartDatabase = new CartDatabase();
                cartDatabase.setProductKey(prodcuts.get(POSITION_TO_WORK).getProductKey());
                cartDatabase.setCount(count);
                cartDatabase.setQuantity(quantityOrder);
                cartDatabase.setItemTotalPrice(String.valueOf(itemTotalPrice));
                cartDatabase.setPrice(prodcuts.get(POSITION_TO_WORK).getPrice());
                cartDatabase.setName(prodcuts.get(POSITION_TO_WORK).getName());
                cartDatabase.setProductImage(prodcuts.get(POSITION_TO_WORK).getProductImage());
                cartDatabase.setQty(prodcuts.get(POSITION_TO_WORK).getQty());
                cartDatabase.setUnit(prodcuts.get(POSITION_TO_WORK).getUnit());
                // adding in database

                DatabaseClient.getInstance(getActivity().getApplicationContext())
                        .getAppDatabase()
                        .cartDao()
                        .insert(cartDatabase);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
              //  hideLoader();
                setBill();
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);

                // showToast(String.valueOf(values.length));

            }
        }


        SaveData saveData = new SaveData();
        saveData.execute();
    }

    @Override
    public void onClick(View v) {
        if (v == binding.btnDwnld){
            validation();

        }

    }


    private void start(){

        binding.btnDwnld.setVisibility(View.GONE);
        binding.layoutProgress.setVisibility(View.VISIBLE);
        binding.tvStatus.setVisibility(View.VISIBLE);
        binding.progressBar1.animateProgress(5000,0,100);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               // startCheckAnimation();
            }
        }, 5000);
    }


    private void startCheckAnimation(){

        ((Animatable) binding.check.getDrawable()).start();
        //binding.tvStatus.setVisibility(View.GONE);
        binding.check.setVisibility(View.VISIBLE);
        binding.tvStatus.setText("Order Placed");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(mContext, MyOrders.class));
            }
        }, 1000);
    }

    private void setBill(){
        int amount =0;
        int deliveryCharge = 0;
        int totalBill = 0;
        for (int i =0; i < prodcuts.size() ; i++){
            amount = amount + Integer.parseInt(prodcuts.get(i).getItemTotalPrice());
        }
        binding.tvAmount.setText(RUPEES + " " + amount);
        if (amount < 299){
            deliveryCharge = 30;
        }
        totalBill = amount + deliveryCharge;
        binding.tvDeliveryCharge.setText(RUPEES + " " + deliveryCharge);
        binding.tvTotalAmount.setText(RUPEES + " " + totalBill);
    }

    private void getUserData(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SignUpUserModel userModel = dataSnapshot.getValue(SignUpUserModel.class);
                setData(userModel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void setData(SignUpUserModel userModel){
        binding.edName.setText(userModel.getUsername());
        binding.edPhone.setText(sp.getString(USER_ID));
    }

    private void selectSPinner(){
        binding.spnArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                areaSelectedPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void validation(){
        if (!Common.validateEditText(binding.edName.getText().toString())){
            showToast("Name is empty");
        }
        else if (!Common.validateEditText(binding.edPhone.getText().toString())){
            showToast("Phone is empty");
        }
        else if (binding.edPhone.getText().toString().length() != 10){
            showToast("Please enter a valid phone number");
        }
        else if (areaSelectedPosition == 0){
            showToast("Please select your area");
        }
        else {
            if (checkInternetConnection()){
                start();
                placeOrder();
            }
            else {
                showToast(getString(R.string.internet_not_there));
            }
        }
    }
    private void placeOrder(){
        final String key = orderDatabase.push().getKey();
        String name = binding.edName.getText().toString();

        String phone = binding.edPhone.getText().toString();
        String userAddress = binding.edAdd1.getText().toString() +
                binding.spnArea.getSelectedItem().toString() +
                binding.edAdd2.getText().toString() +
                binding.edAdd3.getText().toString();

        String totalBill = binding.tvAmount.getText().toString();
        String amountToPay = binding.tvTotalAmount.getText().toString();
        String deliveryCharge = binding.tvDeliveryCharge.getText().toString();
        DateFormat dateFormat = new SimpleDateFormat(DATE3);
        final String orderDate = dateFormat.format(Calendar.getInstance().getTime());

        String deliveryTimeSlot = (String) binding.spnDeliveryTimeSLot.getSelectedItem();
        CartData cartData = new CartData(getActivity().getApplicationContext());
        List<CartDatabase> cart = cartData.getCart();
        final PlaceOrderModel placeOrderModel = new PlaceOrderModel(name,
                phone,
                userAddress,
                totalBill,
                amountToPay,
                deliveryCharge,
                orderDate,
                cart,
                PENDING,
                key,
                deliveryTimeSlot);

        orderDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderDatabase.child(key).setValue(placeOrderModel);
                emptyLocalData();
                startCheckAnimation();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void emptyLocalData(){
        class DeleteData extends AsyncTask<Void,Void,Void> {
            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(mContext)
                        .getAppDatabase()
                        .cartDao()
                        .deleteAll();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //  hideLoader();

            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);

                // showToast(String.valueOf(values.length));

            }
        }


        DeleteData saveData = new DeleteData();
        saveData.execute();
    }


    private void getTimeSlot(){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh a");

            ArrayList<String> times = new ArrayList<>();
            Date currentTimea = Calendar.getInstance().getTime();
            Date after2Hours = new Date (currentTimea.getTime () + 2L * 60L * 60L * 1000L);

            String tie = simpleDateFormat.format(after2Hours);
            String firstTime = tie;

            String secondTime = "8 PM";

            String format = "hh a";

            SimpleDateFormat sdf = new SimpleDateFormat(format);

            Date dateObj1 = sdf.parse( firstTime);
            Date dateObj2 = sdf.parse(secondTime);

            long dif = dateObj1.getTime();
            while (dif < dateObj2.getTime()) {
                Date slot = new Date(dif);
                Date endTime = new Date(dif  + 2L * 60L * 60L * 1000L);
                String time = simpleDateFormat.format(slot);
                String timee = simpleDateFormat.format(endTime);
                times.add(time + " - "+ timee);
                dif += 3600000 *2;
            }

            setSpinnerADapter(times);
        }
        catch (Exception ex){

            String e = ex.getMessage();
        }

        int j= 98;
    }

    private void setSpinnerADapter(ArrayList<String> time){
        //Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter aa = new ArrayAdapter(mContext,android.R.layout.simple_spinner_item,time);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//Setting the ArrayAdapter data on the Spinner
        binding.spnDeliveryTimeSLot.setAdapter(aa);
    }
}
