package appliation.com.nearmarket.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import appliation.com.nearmarket.InActiveAppStatus;
import appliation.com.nearmarket.R;
import appliation.com.nearmarket.core.BaseActivity;
import appliation.com.nearmarket.databinding.ActivityEnterNewPasswordBinding;

public class Splash extends BaseActivity {

    private Handler handler;
    private static final int TIMER = 2000;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String appStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler = new Handler();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(APP_STATUS);

        if (checkInternetConnection()) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    appStatus = dataSnapshot.getValue(String.class);
                    start();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    int jj =9;
                }

            });
        } else {
            showToast("Please check your internet connection");
            finish();
        }


    }

    private void start(){
        if (sp.getBoolean(ISLOGIN)) {

            if (sp.getBoolean(ISADMIN)) {

                startActivity(new Intent(mContext, DashboardAdmin.class));
            } else {
                if (appStatus.equals(ACTIVE)) {
                    startActivity(new Intent(mContext, Dashboard.class));
                } else {
                    startActivity(new Intent(mContext, InActiveAppStatus.class));
                }
            }


        } else {
            if (appStatus.equals(ACTIVE)) {
                startActivity(new Intent(mContext, Login.class));
            } else {
                startActivity(new Intent(mContext, InActiveAppStatus.class));

            }
        }

        finish();

    }

    private void startTimer() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                            }
        }, TIMER);
    }
}
