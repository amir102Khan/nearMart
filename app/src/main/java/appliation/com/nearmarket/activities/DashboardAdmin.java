package appliation.com.nearmarket.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.flarebit.flarebarlib.Flaretab;
import com.flarebit.flarebarlib.TabEventObject;

import java.util.ArrayList;

import appliation.com.nearmarket.Fragment.Basket;
import appliation.com.nearmarket.Fragment.Home;
import appliation.com.nearmarket.Fragment.Profile;
import appliation.com.nearmarket.R;
import appliation.com.nearmarket.core.BaseActivity;
import appliation.com.nearmarket.databinding.ActivityDashboardAdminBinding;

public class DashboardAdmin extends BaseActivity {
    private ActivityDashboardAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_dashboard_admin);
    }




    private void switchToFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
    }
}
