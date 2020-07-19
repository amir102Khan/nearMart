package appliation.com.nearmarket.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import appliation.com.nearmarket.Model.SignUpUserModel;
import appliation.com.nearmarket.R;
import appliation.com.nearmarket.activities.Dashboard;
import appliation.com.nearmarket.core.BaseActivity;
import appliation.com.nearmarket.databinding.ActivityPhoneVerificationBinding;

public class PhoneVerification extends BaseActivity implements View.OnClickListener, TextWatcher {


    private ActivityPhoneVerificationBinding binding;
    private int[] otp = new int[]{-1, -1, -1, -1, -1, -1};
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private String phoneNumber;
    private FirebaseAuth firebaseAuth;
    private SignUpUserModel userModel;
    private String verificationId;
    private boolean isFromSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone_verification);

        implementListener();

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(USER);
        getData();
    }

    private void getData() {
        isFromSignUp = getIntent().getBooleanExtra(IS_FROM_SIGN_UP, true);
        phoneNumber = getIntent().getStringExtra("mobileNumber");
        if (isFromSignUp) {
            userModel = (SignUpUserModel) getIntent().getSerializableExtra("userData");
        }
        sendVerificationCode(phoneNumber);
    }

    private void implementListener() {

        binding.imgBAck.setOnClickListener(this);
        binding.btnVerify.setOnClickListener(this);


        binding.edOtp1.requestFocus();
        binding.edOtp1.addTextChangedListener(this);
        binding.edOtp2.addTextChangedListener(this);
        binding.edOtp3.addTextChangedListener(this);
        binding.edOtp4.addTextChangedListener(this);
        binding.edOtp5.addTextChangedListener(this);
        binding.edOtp6.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.imgBAck) {
            onBackPressed();
            finish();
        } else if (v == binding.btnVerify) {
            validateData();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().hashCode() == binding.edOtp1.getText().toString().hashCode()) {
            binding.edOtp2.requestFocus();
        } else if (s.toString().hashCode() == binding.edOtp2.getText().toString().hashCode()) {
            binding.edOtp3.requestFocus();
        } else if (s.toString().hashCode() == binding.edOtp3.getText().toString().hashCode()) {
            binding.edOtp4.requestFocus();
        } else if (s.toString().hashCode() == binding.edOtp4.getText().toString().hashCode()) {
            binding.edOtp5.requestFocus();
        } else if (s.toString().hashCode() == binding.edOtp5.getText().toString().hashCode()) {
            binding.edOtp6.requestFocus();
        } else if (s.toString().hashCode() == binding.edOtp6.getText().toString().hashCode()) {
            hideKeyboard();
        }

    }

    private void validateData() {
        try {
            otp[0] = Integer.parseInt(binding.edOtp1.getText().toString());
            otp[1] = Integer.parseInt(binding.edOtp2.getText().toString());
            otp[2] = Integer.parseInt(binding.edOtp3.getText().toString());
            otp[3] = Integer.parseInt(binding.edOtp4.getText().toString());
            otp[4] = Integer.parseInt(binding.edOtp5.getText().toString());
            otp[5] = Integer.parseInt(binding.edOtp6.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            otp = new int[]{-1, -1, -1, -1, -1, -1};
        }

        String otpString = "";
        for (int value : otp) {
            if (value == -1) {
                showToast(getString(R.string.invalidOtp));
                return;
            } else {
                otpString = otpString + value;
            }
        }

        if (checkInternetConnection()) {
           /* if (otpString.equals("1234")){
                startActivity(new Intent(mContext,CustomerDashboard.class));
                finishAffinity();
            }
            else {
                showToast("OTP is not valid");
            }*/

            verifyVerificationCode(otpString);

        } else {
            showToast(getString(R.string.internet_not_there));
        }
    }


    private void verifyVerificationCode(String otp) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        showLoader();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(mContext, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity

                            if (isFromSignUp) {
                                signup();
                            } else {
                                setNewPassword();
                            }
                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }
                            showSnackBar(binding.getRoot(), message);
                        }

                        hideLoader();
                    }
                });

    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            showToast(e.getMessage());
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;

        }
    };

    private void signup() {

        showLoader();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(phoneNumber).exists()) {
                    showToast("Phone Number alraedy exists");
                } else {
                    databaseReference.child(phoneNumber).setValue(userModel);
                    sp.setBoolean(ISLOGIN, true);
                    sp.setString(USER_ID,phoneNumber);
                    if (phoneNumber.equals(ADMIN_PHONE)){
                        sp.setBoolean(ISADMIN,true);
                        startActivity(new Intent(mContext,DashboardAdmin.class));
                    }
                    else {
                        sp.setBoolean(ISADMIN,false);
                        startActivity(new Intent(mContext, Dashboard.class));
                    }
                    finish();
                    return;
                }

                hideLoader();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideLoader();
            }
        });

    }

    private void setNewPassword() {
        startActivity(new Intent(mContext, EnterNewPassword.class)
                .putExtra("mobileNumber", phoneNumber));
    }

    private void showLoader() {
        binding.loader.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        binding.loader.setVisibility(View.GONE);
    }
}

