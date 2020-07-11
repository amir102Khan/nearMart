package appliation.com.nearmarket.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import appliation.com.nearmarket.R;
import appliation.com.nearmarket.core.BaseActivity;

public class Splash extends BaseActivity {

    private Handler handler;
    private static final int TIMER = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sp.getBoolean(ISLOGIN)){

                    if (sp.getBoolean(ISADMIN)) {

                        startActivity(new Intent(mContext, DashboardAdmin.class));
                    }
                    else {
                        startActivity(new Intent(mContext, Dashboard.class));
                    }

                    //startActivity(new Intent(mContext, DashboardAdmin.class));


                }
                else {
                    startActivity(new Intent(mContext, Login.class));
                }
                finish();
            }
        },TIMER);
    }
}
