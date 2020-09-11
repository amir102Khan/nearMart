package appliation.com.nearmarket.activities;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import appliation.com.nearmarket.Model.SignUpUserModel;
import appliation.com.nearmarket.Util.Common;
import appliation.com.nearmarket.R;
import appliation.com.nearmarket.core.BaseActivity;

import appliation.com.nearmarket.databinding.ActivitySignupBinding;

public class SignupActivity extends BaseActivity implements View.OnClickListener {

    private ActivitySignupBinding binding;
    private FirebaseAuth firebaseAuth;
    private String email, password, phone, name;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_signup);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(USER);
        implementListener();
        decorateText();
    }

    private void implementListener() {
        binding.btnSignp.setOnClickListener(this);
        binding.imgBAck.setOnClickListener(this);
    }

    private void signup() {
//        firebaseAuth.createUserWithEmailAndPassword(email,password)
        binding.loader.setVisibility(View.VISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(phone).exists()) {
                    showToast("Phone Number alraedy exists");
                } else {
                    SignUpUserModel userModel = new SignUpUserModel(name, email, password);
                    databaseReference.child(phone).setValue(userModel);
                    sp.setBoolean(ISLOGIN, true);
                    sp.setBoolean(ISADMIN, false);
                    sp.setString(USER_ID, phone);
                    startActivity(new Intent(mContext, Dashboard.class));
                    finish();
                }
                binding.loader.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                binding.loader.setVisibility(View.GONE);
            }
        });
       /* firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(mContext, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            FirebaseUser user = firebaseAuth.getCurrentUser();
//                            startActivity(new Intent(mContext, Login.class));
                            sp.setBoolean(Constant.ISLOGIN,true);
                            if (sp.getString(Constant.USER_TYPE).equals(Constant.CONSUMER)){
                                startActivity(new Intent(mContext, Dashboard.class));
                            }
                            else {
                                startActivity(new Intent(mContext, UpdateProductActivity.class));
                            }
                        }
                        else {
                            showToast("Signup Failed");
                        }
                        binding.loader.setVisibility(View.GONE);
                    }
                });*/

    }

    @Override
    public void onClick(View v) {
        if (v == binding.btnSignp) {
            validation();
        } else if (v == binding.imgBAck) {
            onBackPressed();
        }
    }

    private void decorateText() {
        SpannableString ss = new SpannableString(getString(R.string.already_have_an_account_sign_in));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                onBackPressed();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        ss.setSpan(clickableSpan, 25, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.black)), 25,
                ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ss.setSpan(new UnderlineSpan(),25,ss.length(),0);
        binding.tvAlreadyHaveAccount.setText(ss);
        binding.tvAlreadyHaveAccount.setMovementMethod(LinkMovementMethod.getInstance());
        binding.tvAlreadyHaveAccount.setHighlightColor(Color.TRANSPARENT);
    }

    private void validation() {
        if (!Common.validateEditText(binding.edtName.getText().toString())) {
            showToast("Name is empty");
        } else if (!Common.validateEditText(binding.edtEmail.getText().toString())) {
            showToast("Email is empty");
        } else if (!Common.isValidEmail(binding.edtEmail.getText().toString())) {
            showToast("Email is not valid");
        } else if (!Common.validateEditText(binding.edtPassword.getText().toString())) {
            showToast("Password is not valid");
        } else if (!Common.validateEditText(binding.edtPhone.getText().toString())) {
            showToast("Phone Number is empty");
        } else if (binding.edtPhone.getText().toString().length() < 10) {
            showToast("Phone number should be 10 digit");
        } else {
            email = binding.edtEmail.getText().toString();
            password = binding.edtPassword.getText().toString();
            phone = binding.edtPhone.getText().toString();
            name = binding.edtName.getText().toString();
            if (checkInternetConnection()) {
                //signup();

                SignUpUserModel userModel = new SignUpUserModel(name, email, password);
                startActivity(new Intent(mContext, PhoneVerification.class)
                        .putExtra(IS_FROM_SIGN_UP, true)
                        .putExtra("userData", userModel)
                        .putExtra("mobileNumber", phone));

            } else {
                showToast("Check your internet connectivity");
            }
        }
    }
}
