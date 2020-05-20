package appliation.com.nearmarket.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import appliation.com.nearmarket.Model.ProductModel;
import appliation.com.nearmarket.R;
import appliation.com.nearmarket.activities.AdditionalListItems;
import appliation.com.nearmarket.activities.GiftListItems;
import appliation.com.nearmarket.activities.GroceryListItems;
import appliation.com.nearmarket.activities.VegListItems;
import appliation.com.nearmarket.adapter.BannerAdapter;
import appliation.com.nearmarket.core.BaseFragment;
import appliation.com.nearmarket.databinding.FragmentHomeBinding;
import appliation.com.nearmarket.interfaces.OnAdapterItemClick;


public class Home extends BaseFragment implements OnAdapterItemClick, View.OnClickListener {

    public Home() {
        // Required empty public constructor
    }

    private FragmentHomeBinding binding;
    private BannerAdapter bannerAdapter;
    private ArrayList<String> banners = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,null,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       setEmptyAdapter();
       setLiteners();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(BANNER);
        if (checkInternetConnection()){
            getBanners();
        }
        else {
            showToast(getString(R.string.internet_not_there));
        }
    }


    private void getBanners(){

        showLoader();
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        banners.add(dataSnapshot.getValue(String.class));
                    }
                    bannerAdapter.notifyDataSetChanged();
                    hideLoader();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    hideLoader();
                }
            });

    }

    private  void setEmptyAdapter(){
        binding.rvBanners.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
        bannerAdapter = new BannerAdapter(mContext,banners,this);
        binding.rvBanners.setAdapter(bannerAdapter);
    }

    private void setLiteners(){
       binding.cardGrocery.setOnClickListener(this);
       binding.cardAdditional.setOnClickListener(this);
       binding.cardGift.setOnClickListener(this);
       binding.cardVeg.setOnClickListener(this);
    }

    @Override
    public void onItemClick(int position, String data) {


    }

    @Override
    public void onClick(View v) {
        if (v == binding.cardGrocery){
            startActivity(new Intent(mContext, GroceryListItems.class)
            .putExtra(MAIN_CATEGORY,GROCERY));
        }
        else if (v == binding.cardAdditional){
            startActivity(new Intent(mContext, AdditionalListItems.class)
            .putExtra(MAIN_CATEGORY,ADDITIONAL_SERVICE));
        }
        else  if (v == binding.cardGift){
            startActivity(new Intent(mContext, GiftListItems.class)
                    .putExtra(MAIN_CATEGORY,GIFTS));
        }
        else if (v == binding.cardVeg){
            startActivity(new Intent(mContext, VegListItems.class)
            .putExtra(MAIN_CATEGORY,VEGETABLES));
        }
    }

    private void showLoader(){
        binding.loader.setVisibility(View.VISIBLE);
    }

    private void hideLoader(){
        binding.loader.setVisibility(View.GONE);
    }
}
