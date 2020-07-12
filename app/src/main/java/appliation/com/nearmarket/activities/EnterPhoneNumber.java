package appliation.com.nearmarket.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import appliation.com.nearmarket.R;
import appliation.com.nearmarket.Util.Common;
import appliation.com.nearmarket.core.BaseActivity;
import appliation.com.nearmarket.databinding.ActivityEnterPhoneNumberBinding;

public class EnterPhoneNumber extends BaseActivity implements View.OnClickListener {


    private ActivityEnterPhoneNumberBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_phone_number);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_enter_phone_number);
        binding.imgBAck.setOnClickListener(this);
        binding.btnVerify.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.imgBAck){
            onBackPressed();
        }
        else if (view == binding.btnVerify){
            String phoneNumber = binding.edPhone.getText().toString();
            if (!Common.validateEditText(phoneNumber)){
                showSnackBar(binding.getRoot(),"Please enter phone number");
            }
            else if (phoneNumber.length() < 10){
                showSnackBar(binding.getRoot(),"Please enter valid phone number");
            }
            else {
                startActivity(new Intent(mContext,PhoneVerification.class)
                        .putExtra(IS_FROM_SIGN_UP,false)
                        .putExtra("mobileNumber",phoneNumber));
            }
        }
    }
}
