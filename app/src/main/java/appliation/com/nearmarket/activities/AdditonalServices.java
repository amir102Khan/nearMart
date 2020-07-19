package appliation.com.nearmarket.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import appliation.com.nearmarket.Model.BucketModel;
import appliation.com.nearmarket.Model.ProductModel;
import appliation.com.nearmarket.R;
import appliation.com.nearmarket.Util.Common;
import appliation.com.nearmarket.adapter.AdditionalAdapter;
import appliation.com.nearmarket.adapter.GroceryListAdapter;
import appliation.com.nearmarket.core.BaseActivity;
import appliation.com.nearmarket.core.BaseFragment;
import appliation.com.nearmarket.databinding.ActivityAdditonalServicesBinding;

public class AdditonalServices extends BaseActivity {

    private ActivityAdditonalServicesBinding binding;

    private String title;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<String> prodcuts = new ArrayList<>();
   // private GroceryListAdapter adapter ;
    private AdditionalAdapter additionalAdapter;
    private DatabaseReference bucketDatabase;
    private int POSITION_TO_WORK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_additonal_services);

        title = getIntent().getStringExtra(ADDITIONAL_TYPE);
        Common.setToolbarWithBackAndTitle(mContext,title,true,R.drawable.ic_chevron_left_black_24dp);
        setEmptyAdapter();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(title);

        if (checkInternetConnection()){
            getDAta();
        }
        else {
            showToast("Please check your internet connection");
        }
    }


    private void getDAta(){

        showLoader();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    prodcuts.add(dataSnapshot.getValue(String.class));
                }
                additionalAdapter.notifyDataSetChanged();
                hideLoader();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideLoader();
            }
        });

    }
    private void hideLoader(){
        binding.loader.setVisibility(View.GONE);
    }

    private void showLoader(){
        binding.loader.setVisibility(View.VISIBLE);
    }

    private void setEmptyAdapter(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2, LinearLayoutManager.VERTICAL,false);
       // recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        binding.rv.setLayoutManager(gridLayoutManager);
        additionalAdapter = new AdditionalAdapter(prodcuts,mContext);
        binding.rv.setAdapter(additionalAdapter);
    }
}
