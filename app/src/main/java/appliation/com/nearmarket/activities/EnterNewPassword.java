package appliation.com.nearmarket.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.OnClickAction;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import appliation.com.nearmarket.Model.SignUpUserModel;
import appliation.com.nearmarket.R;
import appliation.com.nearmarket.Util.Common;
import appliation.com.nearmarket.core.BaseActivity;
import appliation.com.nearmarket.databinding.ActivityEnterNewPasswordBinding;

public class EnterNewPassword extends BaseActivity implements View.OnClickListener {

    private ActivityEnterNewPasswordBinding binding;
    private String phoneNumber;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_enter_new_password);
        binding.imgBAck.setOnClickListener(this);
        binding.btnVerify.setOnClickListener(this);
        getData();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(USER);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.imgBAck) {
            onBackPressed();
        } else if (view == binding.btnVerify) {
            verify();
        }
    }

    private void getData() {
        phoneNumber = getIntent().getStringExtra("mobileNumber");
    }

    private void verify() {
        String password = binding.edPsswd.getText().toString();
        String cnfrmPssd = binding.edCnfmPasswd.getText().toString();

        if (!Common.validateEditText(password)) {
            showSnackBar(binding.getRoot(), "Please enter password");
        } else if (!cnfrmPssd.equals(password)) {
            showSnackBar(binding.getRoot(), "Confirm password is not same");
        } else {
            if (checkInternetConnection()){
                login(password);
            }
            else {
                showToast("Check your internet connectivity");

            }
        }
    }

    private void changePassword(String password) {

    }


    private void login(final String password) {
        binding.loader.setVisibility(View.VISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(phoneNumber).exists()) {

                    databaseReference.child(phoneNumber).child("password").setValue(password);
                    SignUpUserModel userModel = dataSnapshot.child(phoneNumber).getValue(SignUpUserModel.class);
                    sp.setBoolean(ISLOGIN, true);
                    if (phoneNumber.equals(ADMIN_PHONE)) {
                        sp.setBoolean(ISADMIN, true);
                        startActivity(new Intent(mContext, DashboardAdmin.class));
                    } else {
                        sp.setString(USER_ID, phoneNumber);
                        sp.setBoolean(ISADMIN, false);
                        startActivity(new Intent(mContext, Dashboard.class));
                    }
                    finish();
                } else {
                    showToast("User does not exists");
                }
                binding.loader.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                binding.loader.setVisibility(View.GONE);
            }
        });
    }
}
