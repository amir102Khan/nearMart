package appliation.com.nearmarket.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import appliation.com.nearmarket.R;
import appliation.com.nearmarket.core.BaseActivity;
import appliation.com.nearmarket.core.BaseFragment;
import appliation.com.nearmarket.databinding.FragmentAppStatusBinding;


public class AppStatus extends BaseFragment implements View.OnClickListener {

    private FragmentAppStatusBinding binding;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String appStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         binding = DataBindingUtil.inflate(inflater,R.layout.fragment_app_status, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(APP_STATUS);
        binding.btnUpdateStatus.setOnClickListener(this);
        if (checkInternetConnection()) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    appStatus = dataSnapshot.getValue(String.class);
                    setAppStatus();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            showToast("Please check your internet connection");
        }
    }

    @Override
    public void onClick(View view) {
        if (view == binding.btnUpdateStatus){
            if (checkInternetConnection()){
                updateStatus();
            }
            else {
                showToast("Please check your internet connection");
            }
        }
    }

    private void setAppStatus(){
        if (appStatus.equals(ACTIVE)){
            binding.rdActive.setChecked(true);
        }
        else if (appStatus.equals(BLOCKED)){
            binding.rdBlocked.setChecked(true);
        }
    }
    private void updateStatus(){

        int statusType = binding.statusTypeRadio.getCheckedRadioButtonId();
        if (statusType == binding.rdActive.getId()){
            checkInternet(ACTIVE);
        }
        else if (statusType == binding.rdBlocked.getId()){
            checkInternet(BLOCKED);
        }
        else {
            showToast("Please select payment type");
        }
    }

    private void checkInternet(String status){
        if (checkInternetConnection()){
            databaseReference.setValue(status);
            showToast("App status updated");
        }
        else {
            showToast("Please check your internet");
        }
    }
}
