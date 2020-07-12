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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthResult;
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

import appliation.com.nearmarket.databinding.ActivityLoginBinding;

public class Login extends BaseActivity implements View.OnClickListener {

    private ActivityLoginBinding binding;
    private String email,password;
    private FirebaseAuth firebaseAuth;
    private BottomSheetBehavior bottomSheetBehavior;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        implementListener();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(USER);
        decorateText();
        /*  bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.getRoot());*/
        /* bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.clBottomSheetParent);*/
        /* hideBottomSheet();*/
    }

    private void implementListener(){
        binding.btnLogin.setOnClickListener(this);
        binding.tvSignUp.setOnClickListener(this);
        binding.tvForgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.btnLogin){
            validation();
        }
        else if (v == binding.tvForgotPassword){
            startActivity(new Intent(mContext,EnterPhoneNumber.class));
        }

       /* else if (v == binding.bottomSheet.btnConsumer){
            sp.setString(Constant.USER_TYPE,Constant.CONSUMER);
            startActivity(new Intent(this, SignupActivity.class));
        }
        else if (v == binding.bottomSheet.btnProvider){
            sp.setString(Constant.USER_TYPE,Constant.PROVIDER);
            startActivity(new Intent(this, SignupActivity.class));
        }
        else if (v == binding.bottomSheet.ivCross){
            hideBottomSheet();
        }*/
    }

    private void validation(){
        if (!Common.validateEditText(binding.edtEmail.getText().toString())){
            showSnackBar(binding.getRoot(),"Phone number is empty");
        }
       /* else if (!Common.isValidEmail(binding.edtEmail.getText().toString())){
            showToast("Email is not vaild");
        }*/
        else if (!Common.validateEditText(binding.edtPassword.getText().toString())){
            showSnackBar(binding.getRoot(),"Password is not valid");
        }
        else {
            email = binding.edtEmail.getText().toString();
            password = binding.edtPassword.getText().toString();
            if (checkInternetConnection()){
                login();
            }
            else {
                showToast("Check your internet connectivity");
            }
        }
    }

    private void decorateText() {
        SpannableString ss = new SpannableString(getString(R.string.don_t_have_an_account_sign_up));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                //  openBottomSheet();
                // openDaialog();
                startActivity(new Intent(mContext, SignupActivity.class));

            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        ss.setSpan(clickableSpan, 23, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.cream)), 23,
                ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.tvSignUp.setText(ss);
        binding.tvSignUp.setMovementMethod(LinkMovementMethod.getInstance());
        binding.tvSignUp.setHighlightColor(Color.TRANSPARENT);
    }


    private void login(){
        binding.loader.setVisibility(View.VISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(email).exists()){
                    SignUpUserModel userModel = dataSnapshot.child(email).getValue(SignUpUserModel.class);
                    if (userModel.getPassword().equals(password)){
                        sp.setBoolean(ISLOGIN,true);
                        if (email.equals(ADMIN_PHONE)){
                            sp.setBoolean(ISADMIN,true);
                         startActivity(new Intent(mContext,DashboardAdmin.class));
                        }
                        else {
                            sp.setString(USER_ID,email);
                            sp.setBoolean(ISADMIN,false);
                            startActivity(new Intent(mContext, Dashboard.class));
                        }
                        finish();
                    }
                    else {
                        showToast("Login failed");
                    }
                }
                else {
                    showToast("User does not exists");
                }
                binding.loader.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                binding.loader.setVisibility(View.GONE);
            }
        });
        /*firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(mContext, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            sp.setBoolean(Constant.ISLOGIN,true);
                            if (sp.getString(Constant.USER_TYPE).equals(Constant.CONSUMER)){
                                startActivity(new Intent(mContext, Dashboard.class));
                            }
                            else {
                                startActivity(new Intent(mContext, UpdateProductActivity.class));
                            }

                        }
                        else {
                            showToast("Login is failed");
                        }

                        binding.loader.setVisibility(View.GONE);
                    }
                });*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finishAffinity();
    }

    private void hideBottomSheet() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    private void openBottomSheet() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    private void openDaialog(){
        final   BottomSheetDialog dialog = new BottomSheetDialog(mContext);
        dialog.setContentView(R.layout.layout_bottom_sheet_signup_type);
        dialog.setCanceledOnTouchOutside(false);

        ImageView btnClose =  dialog.findViewById(R.id.ivCross);
        Button consumer = dialog.findViewById(R.id.btn_consumer);
        Button provider = dialog.findViewById(R.id.btn_provider);

        consumer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.setString(USER_TYPE,CONSUMER);
                startActivity(new Intent(mContext, SignupActivity.class));
                dialog.dismiss();
            }
        });

        provider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.setString(USER_TYPE,PROVIDER);
                startActivity(new Intent(mContext, SignupActivity.class));
                dialog.dismiss();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
